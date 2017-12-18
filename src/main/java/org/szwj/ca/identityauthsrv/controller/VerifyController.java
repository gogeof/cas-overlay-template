package org.szwj.ca.identityauthsrv.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.szwj.ca.identityauthsrv.config.Config;
import org.szwj.ca.identityauthsrv.entity.dao.CertInfoEntity;
import org.szwj.ca.identityauthsrv.entity.dao.SignedDataEntity;
import org.szwj.ca.identityauthsrv.entity.httpRequest.VerifyDataEntity;
import org.szwj.ca.identityauthsrv.entity.httpRequest.VerifyPDFEntity;
import org.szwj.ca.identityauthsrv.service.intfc.CertInfoService;
import org.szwj.ca.identityauthsrv.service.intfc.SignedDataService;
import org.szwj.ca.identityauthsrv.util.bussiness.CertHandleUtil;
import org.szwj.ca.identityauthsrv.util.common.CalendarUtil;
import org.szwj.ca.identityauthsrv.util.common.ParameterCheckUtil;
import sglca.helper.ICaHelper;
import sglca.helper.SZWJ_CaHelper;
import sglca.helper.models.*;
import sglca.helper.netca.*;
import sglca.helper.JsonHelper;
import sglca.helper.Tools;

import java.util.UUID;

@RestController
@RequestMapping("/v1.0/verify")
public class VerifyController {

    private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);

    @Autowired
    private SignedDataService signedDataService;

    @Autowired
    private CertInfoService certInfoService;

    public VerifyController() {
    }

    private HttpEntity commonVerifyData(String sn, VerifyDataEntity verifyDataEntity) {
        // 校验请求报文中的证书颁发机构
        if (!ParameterCheckUtil.isCertAuthorityValid(verifyDataEntity.getAuthority())) {
            String responesJson = JsonHelper
                .generateResponse(-1,
                    String.format("Invalid authority: %s.", verifyDataEntity.getAuthority()),
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 获取相应的cahelper
        ICaHelper helper = SZWJ_CaHelper
            .getCaHelperImplByAuthority(verifyDataEntity.getAuthority());

        // 验证签名数据
        try {
            helper.VerSignData(verifyDataEntity.getSourceData(), verifyDataEntity.getSignedData());
        } catch (CaHelperException e) {
            String responesJson = JsonHelper
                .generateResponse(-2,
                    String.format("verify signed data failed, error: %s.", e.getMessage()),
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 使用电子认证网关验证证书有效性
        String responesJson = CertHandleUtil.VerifyCertByAuthGw(-3, verifyDataEntity.getSignCert());
        if (!Tools.isStringEmpty(responesJson)) {
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        try {
            // 记录数据签名信息
            // 根据SN码获取证书信息实体类
            CertInfoEntity certInfoEntity = certInfoService.QueryCertInfoBySn(sn);

            SignedDataEntity signedDataEntity = new SignedDataEntity();
            signedDataEntity.setID(UUID.randomUUID().toString());
            signedDataEntity.setAuthority(verifyDataEntity.getAuthority());
            signedDataEntity.setSourceData(verifyDataEntity.getSourceData());
            signedDataEntity.setSignedData(verifyDataEntity.getSignedData());
            signedDataEntity.setDetach(verifyDataEntity.getDetach());
            if (null != certInfoEntity) {
                signedDataEntity.setCertInfoID(certInfoEntity.getID());
            }
            signedDataEntity.setCreatedTime(CalendarUtil.GetUTCTimestamp());
            signedDataService.InsertSignedDataRecord(signedDataEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 生成响应报文
        responesJson = JsonHelper.generateResponse(0, "verify data successfully.", null);
        return new ResponseEntity(responesJson, HttpStatus.OK);
    }

    // 数据签名验证（旧接口，兼容保留）
    @RequestMapping(method = RequestMethod.POST, value = "/data/{sn}")
    public HttpEntity verifyData(@PathVariable("sn") String sn,
                                 @RequestBody VerifyDataEntity verifyDataEntity) {
        return commonVerifyData(sn, verifyDataEntity);
    }

    // 数据签名验证（新接口）
    @RequestMapping(method = RequestMethod.POST, value = "/data")
    public HttpEntity verifyData(@RequestBody VerifyDataEntity verifyDataEntity) {
        return commonVerifyData(verifyDataEntity.getSn(), verifyDataEntity);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/pdf/{sn}")
    public HttpEntity verifyPDF(@PathVariable("sn") String sn,
                                @RequestBody VerifyPDFEntity verifyPDFEntity) {
        // 校验请求报文中的证书颁发机构
        if (!ParameterCheckUtil.isCertAuthorityValid(verifyPDFEntity.getAuthority())) {
            String responesJson = JsonHelper
                .generateResponse(-1,
                    String.format("Invalid authority: %s.", verifyPDFEntity.getAuthority()),
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 获取相应的cahelper
        ICaHelper helper = SZWJ_CaHelper.getCaHelperImplByAuthority(verifyPDFEntity.getAuthority());

        // 验证PDF签章
        try {
            String pdfPath = String
                .format("%s/%s_%s_%s_%d.pdf", Config.GetInstance().getPdfBasePath(),
                    verifyPDFEntity.getBusinessSystemCode(), verifyPDFEntity.getBusinessTypeCode(),
                    verifyPDFEntity.getPdfTypeCode(), System.currentTimeMillis());

            Netca netca = new Netca();
            netca.ConvertBase64StreamToPDF(pdfPath, verifyPDFEntity.getSignedPdfStream());
            helper.VerSignPDF(pdfPath);
        } catch (CaHelperException e) {
            String responesJson = JsonHelper
                .generateResponse(-2,
                    String.format("verify signed PDF failed, error: %s.", e.getMessage()),
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 使用电子认证网关验证证书有效性
        String responesJson = CertHandleUtil.VerifyCertByAuthGw(-3, verifyPDFEntity.getSignCert());
        if (!Tools.isStringEmpty(responesJson)) {
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 生成响应报文
        responesJson = JsonHelper
            .generateResponse(0, "verify signed PDF successfully.", null);
        return new ResponseEntity(responesJson, HttpStatus.OK);
    }


}
