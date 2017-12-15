package org.szwj.ca.identityauthsrv.controller;

import org.szwj.ca.identityauthsrv.entity.dao.CertInfoEntity;
import org.szwj.ca.identityauthsrv.entity.dao.LoginInfoEntity;
import org.szwj.ca.identityauthsrv.entity.httpRequest.GetRandomEntity;
import org.szwj.ca.identityauthsrv.entity.httpRequest.LoginEntity;
import org.szwj.ca.identityauthsrv.entity.httpRequest.VerifyTokenEntity;
import org.szwj.ca.identityauthsrv.entity.httpResponse.EventValueEntity;
import org.szwj.ca.identityauthsrv.service.intfc.CertInfoService;
import org.szwj.ca.identityauthsrv.service.intfc.LoginInfoService;
import org.szwj.ca.identityauthsrv.service.intfc.UserService;
import org.szwj.ca.identityauthsrv.util.common.CalendarUtil;
import org.szwj.ca.identityauthsrv.util.bussiness.CertHandleUtil;
import org.szwj.ca.identityauthsrv.util.common.ParameterCheckUtil;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sglca.helper.SZWJ_CaHelper;
import sglca.helper.ICaHelper;
import sglca.helper.models.CaHelperException;
import sglca.helper.utils.Base64Util;
import sglca.helper.utils.JsonHelper;
import sglca.helper.utils.Tools;

@RestController
@RequestMapping("/v1.0/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static final int defaultExpiredMinute = 30;

    @Autowired
    private UserService userService;

    @Autowired
    private CertInfoService certInfoService;

    @Autowired
    private LoginInfoService loginInfoService;

    public LoginController() throws IOException {
    }

    private HttpEntity commonGetRandom(String sn, GetRandomEntity getRandomEntity) {
        // 校验请求报文中证书颁发机构
        if (!ParameterCheckUtil.isCertAuthorityValid(getRandomEntity.getAuthority())) {
            String responesJson = JsonHelper
                .generateResponse(-1,
                    String.format("Invalid authority: %s.", getRandomEntity.getAuthority()),
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 获取相应的cahelper
        ICaHelper helper = SZWJ_CaHelper.getCaHelperImplByAuthority(getRandomEntity.getAuthority());

        // 生成随机数
        String randomNum = helper.GetRandomNum();

        // 将随机数与证书sn码关联记录到数据库
        String loginInfoID = UUID.randomUUID().toString();
        LoginInfoEntity loginInfoEntity = new LoginInfoEntity();
        loginInfoEntity.setID(loginInfoID);
        loginInfoEntity.setBusinessSystemCode(getRandomEntity.getBusinessSystemCode());
        loginInfoEntity.setBusinessTypeCode(getRandomEntity.getBusinessTypeCode());
        loginInfoEntity.setAuthority(getRandomEntity.getAuthority());
        loginInfoEntity.setRandomNum(randomNum);
        loginInfoService.InsertRandomNumRecord(loginInfoEntity);

        // 生成响应报文
        EventValueEntity eventValue = new EventValueEntity();
        eventValue.setLoginInfoID(loginInfoID);
        eventValue.setRandomNum(randomNum.replace("\r\n", ""));
        String responesJson = JsonHelper
            .generateResponse(0, "get random num successfully.", eventValue);
        return new ResponseEntity(responesJson, HttpStatus.OK);
    }

    // 请求随机数（旧接口，兼容保留）
    @RequestMapping(method = RequestMethod.POST, value = "/getrandomnum/{sn}")
    public HttpEntity getRandomNum(@PathVariable("sn") String sn,
        @RequestBody GetRandomEntity getRandomEntity) {
        return commonGetRandom(sn, getRandomEntity);
    }

    // 请求随机数（新接口）
    @RequestMapping(method = RequestMethod.POST, value = "/getrandomnum")
    public HttpEntity getRandomNum(@RequestBody GetRandomEntity getRandomEntity) {
        return commonGetRandom(getRandomEntity.getSn(), getRandomEntity);
    }

    private HttpEntity commonGetToken(String sn, LoginEntity loginEntity) {
        // 校验请求报文中的证书颁发机构
        if (!ParameterCheckUtil.isCertAuthorityValid(loginEntity.getAuthority())) {
            String responesJson = JsonHelper
                .generateResponse(-1,
                    String.format("Invalid authority: %s.", loginEntity.getAuthority()),
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 验证随机数是否由系统生成
        LoginInfoEntity loginInfoEntity = loginInfoService
            .QueryLoginInfoByID(loginEntity.getLoginInfoID());
        if (null == loginInfoEntity) {
            String responesJson = JsonHelper
                .generateResponse(-1, "Invalid loginInfoID, can't find the login info in IAS.",
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }
        if (!loginEntity.getSourceData().equals(loginInfoEntity.getRandomNum())) {
            String responesJson = JsonHelper
                .generateResponse(-1, "Invalid sourceData, can't match the random num in IAS.",
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }
        // TODO 判断该登录记录的创建时间是否过期

        // 获取相应的cahelper
        ICaHelper helper = SZWJ_CaHelper.getCaHelperImplByAuthority(loginEntity.getAuthority());

        String boundValue = "";
        // 验证签名数据
        try {
            helper.VerSignData(loginEntity.getSourceData(), loginEntity.getSignedData());
            boundValue = helper.GetCheckKey(loginEntity.getSignCert());
        } catch (CaHelperException e) {
            String responesJson = JsonHelper
                .generateResponse(-2,
                    String.format("failed to verify signed data, error: %s.", e.getMessage()),
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 使用电子认证网关验证证书有效性
        {
            String responesJson = CertHandleUtil.VerifyCertByAuthGw(-3, loginEntity.getSignCert());
            if (!Tools.isStringEmpty(responesJson)) {
                return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
            }
        }

        String encryptedToken = "";
        String employeeNum = "Not found";

        // 根据SN码获取证书信息实体类
        CertInfoEntity certInfoEntity = certInfoService.QueryCertInfoBySn(sn);

        // 根据证书信息实体中的用户ID查询用户表获取用户工号
        if (null == certInfoEntity || null == certInfoEntity.getUserID() || certInfoEntity
            .getUserID().isEmpty()) {
            logger.warn(
                "can't find user ID in cert info, no correlation was found between user info "
                    + "and cert info, sn: {}", sn);
        } else {
            loginInfoEntity.setCertInfoID(certInfoEntity.getID());
            // 获取工号
            employeeNum = userService.QueryEmployeeNumByID(certInfoEntity.getUserID());
        }

        // 组装动态令牌
        StringBuffer sbEncryptedToken = new StringBuffer();
        sbEncryptedToken.append(sn).append("|||").append(loginEntity.getPasswd()).append("|||")
            .append(employeeNum).append("|||").append(loginEntity.getSourceData());

        // 用加密证书公钥加密动态令牌
        encryptedToken = Base64Util.encode(sbEncryptedToken.toString().getBytes())
            .replace("\r\n", "");

        loginInfoEntity.setEncryptedToken(encryptedToken);

        // 将加密后的动态令牌记录到数据库
        loginInfoEntity.setSignedData(loginEntity.getSignedData());

        loginInfoEntity.setExpiredTime(CalendarUtil.GetExpiredUTCTimestamp(defaultExpiredMinute));
        loginInfoService.UpdateEncryptedToken(loginInfoEntity);

        // 生成响应报文
        EventValueEntity eventValueEntity = new EventValueEntity();
        eventValueEntity.setBoundValue(boundValue);
        eventValueEntity.setEncryptedToken(encryptedToken);
        String responesJson = JsonHelper
            .generateResponse(0, "login successfully.", eventValueEntity);
        return new ResponseEntity(responesJson, HttpStatus.OK);
    }

    // 登录验证（旧接口，兼容保留）
    @RequestMapping(method = RequestMethod.POST, value = "/gettoken/{sn}")
    public HttpEntity getToken(@PathVariable("sn") String sn,
        @RequestBody LoginEntity loginEntity) {
        return commonGetToken(sn, loginEntity);
    }

    // 登录验证（新接口）
    @RequestMapping(method = RequestMethod.POST, value = "/gettoken")
    public HttpEntity getToken(@RequestBody LoginEntity loginEntity) {
        return commonGetToken(loginEntity.getSn(), loginEntity);
    }

    // 验证动态口令
    @RequestMapping(method = RequestMethod.POST, value = "/verifytoken")
    public HttpEntity verifyToken(@RequestBody VerifyTokenEntity verifyTokenEntity) {
        LoginInfoEntity loginInfoEntity = loginInfoService
            .QueryLastLoginInfoByEncryptedToken(verifyTokenEntity.getEncryptedToken());
        if (null == loginInfoEntity) {
            String responesJson = JsonHelper
                .generateResponse(-1, "Invalid encrypted token, can't find the login info in IAS.",
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        System.out.println(loginInfoEntity.getExpiredTime());
        Timestamp currentTime = CalendarUtil.GetUTCTimestamp();
        if (currentTime.after(loginInfoEntity.getExpiredTime())) {
            String responesJson = JsonHelper
                .generateResponse(-1, "Invalid encrypted token, the encrypted token has expired.",
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 记录登陆信息，并刷新动态口令的过期时间
        LoginInfoEntity newLoginInfoEntity = new LoginInfoEntity();
        newLoginInfoEntity.setID(UUID.randomUUID().toString());
        newLoginInfoEntity.setBusinessSystemCode(verifyTokenEntity.getBusinessSystemCode());
        newLoginInfoEntity.setBusinessTypeCode(verifyTokenEntity.getBusinessTypeCode());
        newLoginInfoEntity.setAuthority(verifyTokenEntity.getAuthority());
        newLoginInfoEntity.setCreatedTime(CalendarUtil.GetUTCTimestamp());
        newLoginInfoEntity.setEncryptedToken(verifyTokenEntity.getEncryptedToken());
        newLoginInfoEntity
            .setExpiredTime(CalendarUtil.GetExpiredUTCTimestamp(defaultExpiredMinute));
        loginInfoService.InsertEncryptedTokenRecord(newLoginInfoEntity);

        String responesJson = JsonHelper
            .generateResponse(0, "login successfully.", null);
        return new ResponseEntity(responesJson, HttpStatus.OK);
    }
}
