package org.szwj.ca.identityauthsrv.controller;

import org.szwj.ca.identityauthsrv.config.Config;
import org.szwj.ca.identityauthsrv.entity.dao.CertInfoEntity;
import org.szwj.ca.identityauthsrv.entity.dao.SignedPDFEntity;
import org.szwj.ca.identityauthsrv.entity.httpRequest.GenPDFDigestEntity;
import org.szwj.ca.identityauthsrv.entity.httpRequest.GenSignPDFEntity;
import org.szwj.ca.identityauthsrv.entity.httpRequest.SignTimestampEntity;
import org.szwj.ca.identityauthsrv.entity.httpResponse.EventValueEntity;
import org.szwj.ca.identityauthsrv.service.intfc.CertInfoService;
import org.szwj.ca.identityauthsrv.service.intfc.SignedPDFService;
import org.szwj.ca.identityauthsrv.util.common.CalendarUtil;
import org.szwj.ca.identityauthsrv.util.bussiness.CertHandleUtil;
import org.szwj.ca.identityauthsrv.util.common.FileUtil;
import org.szwj.ca.identityauthsrv.util.common.ParameterCheckUtil;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sglca.helper.SZWJ_CaHelper;
import sglca.helper.ICaHelper;
import sglca.helper.models.CaHelperException;
import sglca.helper.models.EventValue;
import sglca.helper.models.PDFInfo;
import sglca.helper.utils.Base64Util;
import sglca.helper.utils.JsonHelper;
import sglca.helper.utils.Tools;

@RestController
@RequestMapping("/v1.0/sign")
public class SignController {

    private static final Logger logger = LoggerFactory.getLogger(SignController.class);

    private String pdfBasePath;

    private String businessOrgCode;

    @Autowired
    private CertInfoService certInfoService;

    @Autowired
    private SignedPDFService signedPDFService;

    public SignController() {
        pdfBasePath = Config.GetInstance().getPdfBasePath();
        businessOrgCode = Config.GetInstance().getBusinessOrgCode();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/timestamp")
    public HttpEntity signDataTimestampWithTSA(
        @RequestBody SignTimestampEntity signTimestampEntity) {
        // 获取时间戳服务器机构
        ICaHelper helper = SZWJ_CaHelper
            .getCaHelperImplByAuthority(Config.GetInstance().getTsaAuthority());
        if (null == helper) {
            String responesJson = JsonHelper
                .generateResponse(-1, "failed to get tsa authority.", null);
            return new ResponseEntity(responesJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String timestamp = "";
        if (SZWJ_CaHelper.BJCA_AUTHORITY.equals(Config.GetInstance().getTsaAuthority())) {
            if (!Tools.isStringEmpty(signTimestampEntity.getSourceData())) {
                try {
                    timestamp = helper.SignWithTSA(signTimestampEntity.getSignCert(),
                        signTimestampEntity.getSourceData());
                } catch (CaHelperException e) {
                    String responesJson = JsonHelper
                        .generateResponse(-1,
                            String.format("sign data with TSA failed, error: %s.", e.getMessage()),
                            null);
                    return new ResponseEntity(responesJson, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else if (!Tools.isStringEmpty(signTimestampEntity.getFileStream())) {
                try {
                    // 路径命名规则: ${pdfBasePath}/业务机构编码/业务系统编码/年份/月份/${uuid}.pdf
                    String pdfSealRecordID = UUID.randomUUID().toString();
                    String filePath = String
                        .format("%s/%s/%s/%s/%s/%s.pdf", pdfBasePath, businessOrgCode,
                            signTimestampEntity.getBusinessSystemCode(), CalendarUtil.GetMonth(),
                            CalendarUtil.GetDate(), pdfSealRecordID);
                    byte[] fileByteSteam = Base64Util.decode(signTimestampEntity.getFileStream());
                    FileUtil.writeByteArrayToFile(new File(filePath), fileByteSteam);

                    timestamp = helper.SignFileWithTSA(null, null, filePath);
                } catch (CaHelperException e) {
                    String responesJson = JsonHelper.generateResponse(-1,
                        String.format("verify signed PDF failed, error: %s.", e.getMessage()),
                        null);
                    return new ResponseEntity(responesJson, HttpStatus.INTERNAL_SERVER_ERROR);
                } catch (IOException e) {
                    String responesJson = JsonHelper.generateResponse(-1,
                        String.format("verify signed PDF failed, error: %s.", e.getMessage()),
                        null);
                    return new ResponseEntity(responesJson, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            // 添加时间戳签名记录
        }

        // 生成响应报文
        EventValue eventValue = new EventValue();
        eventValue.setTimestamp(timestamp);
        String responesJson = JsonHelper
            .generateResponse(0, "sign data with TSA successfully.", eventValue);
        return new ResponseEntity(responesJson, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/pdf/digest")
    public HttpEntity genPDFDigest(@RequestBody GenPDFDigestEntity genPDFDigestEntity) {
        // 校验请求报文中的证书颁发机构
        if (!ParameterCheckUtil.isCertAuthorityValid(genPDFDigestEntity.getAuthority())) {
            String responesJson = JsonHelper.generateResponse(-1,
                String.format("Invalid authority: %s.", genPDFDigestEntity.getAuthority()), null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 使用电子认证网关验证证书有效性
        {
            String responesJson = CertHandleUtil
                .VerifyCertByAuthGw(-2, genPDFDigestEntity.getSignCert());
            if (!Tools.isStringEmpty(responesJson)) {
                return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
            }
        }

        // NETCA处理流程
        PDFInfo pdfInfo = null;
        String pdfSealRecordID = UUID.randomUUID().toString();
        if (SZWJ_CaHelper.NETCA_AUTHORITY.equals(genPDFDigestEntity.getAuthority())) {
            // 保存PDF文件至服务器本地
            // 路径命名规则: ${pdfBasePath}/业务机构编码/业务系统编码/年份/月份/${uuid}.pdf
            String pdfRelativePath = String
                .format("%s/%s/%s/%s.pdf", genPDFDigestEntity.getBusinessSystemCode(),
                    CalendarUtil.GetYear(), CalendarUtil.GetMonth(), pdfSealRecordID);
            String pdfFullPath = String
                .format("%s/%s/%s", pdfBasePath, businessOrgCode, pdfRelativePath);

            byte[] fileByteSteam = new byte[0];
            try {
                fileByteSteam = Base64Util.decode(genPDFDigestEntity.getPdfByte());
                FileUtil.writeByteArrayToFile(new File(pdfFullPath), fileByteSteam);
            } catch (IOException e) {
                String responesJson = JsonHelper.generateResponse(-3,
                    String.format("gen PDF digest, error: %s.", e.getMessage()),
                    null);
                return new ResponseEntity(responesJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 记录签名信息至DB
            // 根据SN码获取证书信息实体类
            CertInfoEntity certInfoEntity = certInfoService
                .QueryCertInfoBySn(genPDFDigestEntity.getSn());

            // 记录到数据库中
            SignedPDFEntity signedPDFEntity = new SignedPDFEntity();
            signedPDFEntity.setID(pdfSealRecordID);
            signedPDFEntity.setAuthority(SZWJ_CaHelper.NETCA_AUTHORITY);
            signedPDFEntity.setBusinessSystemCode(genPDFDigestEntity.getBusinessSystemCode());
            signedPDFEntity.setBusinessTypeCode(genPDFDigestEntity.getBusinessTypeCode());
            signedPDFEntity.setPdfBasePath(pdfBasePath);
            signedPDFEntity.setBusinessOrgCode(businessOrgCode);
            signedPDFEntity.setPdfRelativePath(pdfRelativePath);
            signedPDFEntity.setFileDigest("");
            if (null != certInfoEntity) {
                signedPDFEntity.setCertInfoID(certInfoEntity.getID());
            }
            signedPDFEntity.setCreatedTime(CalendarUtil.GetUTCTimestamp());

            try {
                signedPDFService.InsertSignedPDFRecord(signedPDFEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // 获取相应的cahelper
            ICaHelper helper = SZWJ_CaHelper
                .getCaHelperImplByAuthority(genPDFDigestEntity.getAuthority());
            byte[] pdfByte = new byte[0];
            try {
                pdfByte = Base64Util.decode(genPDFDigestEntity.getPdfByte());
            } catch (IOException e) {
                String responesJson = JsonHelper.generateResponse(-4, "Invalid pdfByte.", null);
                return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
            }

            try {
                pdfInfo = helper.GenPDFDigest(pdfByte, genPDFDigestEntity.getSignCert(),
                    genPDFDigestEntity.getSealImg(), genPDFDigestEntity.getPosition());
            } catch (CaHelperException e) {
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            // 记录签名信息至DB
            // 根据SN码获取证书信息实体类
            CertInfoEntity certInfoEntity = certInfoService
                .QueryCertInfoBySn(genPDFDigestEntity.getSn());

            // 记录到数据库中
            SignedPDFEntity signedPDFEntity = new SignedPDFEntity();
            signedPDFEntity.setID(pdfSealRecordID);
            signedPDFEntity.setAuthority(SZWJ_CaHelper.BJCA_AUTHORITY);
            signedPDFEntity.setBusinessSystemCode(genPDFDigestEntity.getBusinessSystemCode());
            signedPDFEntity.setBusinessTypeCode(genPDFDigestEntity.getBusinessTypeCode());
            signedPDFEntity.setPdfBasePath(pdfBasePath);
            signedPDFEntity.setBusinessOrgCode(businessOrgCode);
            signedPDFEntity.setFileDigest("");
            if (null != certInfoEntity) {
                signedPDFEntity.setCertInfoID(certInfoEntity.getID());
            }
            signedPDFEntity.setCreatedTime(CalendarUtil.GetUTCTimestamp());

            try {
                signedPDFService.InsertSignedPDFRecord(signedPDFEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        EventValueEntity eventValueEntity = new EventValueEntity();
        if (null != pdfInfo) {
            eventValueEntity.setDigest(pdfInfo.getPdfDigest());
        }
        String responesJson = JsonHelper
            .generateResponse(0, "gen PDF digest successfully.", eventValueEntity);
        return new ResponseEntity(responesJson, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/pdf/genSeal")
    public HttpEntity genSignPDF(@RequestBody GenSignPDFEntity genSignPDFEntity) {
        // 校验请求报文中的证书颁发机构
        if (!ParameterCheckUtil.isCertAuthorityValid(genSignPDFEntity.getAuthority())) {
            String responesJson = JsonHelper
                .generateResponse(-1,
                    String.format("Invalid authority: %s.", genSignPDFEntity.getAuthority()),
                    null);
            return new ResponseEntity(responesJson, HttpStatus.BAD_REQUEST);
        }

        // 获取相应的cahelper
        ICaHelper helper = SZWJ_CaHelper
            .getCaHelperImplByAuthority(genSignPDFEntity.getAuthority());
        String base64SealPdf = "";
        try {
            base64SealPdf = helper.GenSignPDF(genSignPDFEntity.getSignedDigest());
        } catch (CaHelperException e) {
            String responesJson = JsonHelper.generateResponse(-2,
                String.format("gen sign with TSA failed, error: %s.", e.getMessage()), null);
            return new ResponseEntity(responesJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EventValueEntity eventValueEntity = new EventValueEntity();
        eventValueEntity.setBase64SealPdf(base64SealPdf);
        String responesJson = JsonHelper
            .generateResponse(0, "gen sign PDF successfully.", eventValueEntity);
        return new ResponseEntity(responesJson, HttpStatus.OK);
    }
}