package org.szwj.ca.identityauthsrv.controller;

import com.gdca.signofcloud.common.util.Base64;
import com.gdca.signofcloud.common.util.MessageUtil;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;
import org.szwj.ca.identityauthsrv.config.Config;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignBatchTaskEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignInfoEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.BatchSignBackendReqEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.BatchSignEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.Files;
import org.szwj.ca.identityauthsrv.entity.cloudsign.PreSignEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.SignNotifyEntity;
import org.szwj.ca.identityauthsrv.entity.httpResponse.EventValueEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CommonResp;
import org.szwj.ca.identityauthsrv.service.intfc.CloudSignService;
import org.szwj.ca.identityauthsrv.util.common.CalendarUtil;
import org.szwj.ca.identityauthsrv.util.common.GenPDFUtil;
import org.szwj.ca.identityauthsrv.util.common.HttpsClientUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sglca.helper.models.CaHelperException;
import sglca.helper.models.ResponseBody;
import sglca.helper.utils.Base64Util;
import sglca.helper.utils.JsonHelper;
import sglca.helper.utils.Tools;

@RestController
@RequestMapping("/v1.0/cloudsign")
public class CloudSignControl {

    private static final Logger logger = LoggerFactory.getLogger(CloudSignControl.class);

    @Autowired
    private CloudSignService cloudSignService;

    public CloudSignControl() {
    }

    @RequestMapping(method = RequestMethod.POST, value = "/presign")
    public HttpEntity preSign(@RequestBody PreSignEntity preSignEntity) {

        // step1: 参数校验
        logger.info("[Begin] Receives a pre sign request from bussiness system.");
        String errMsg = "";
        if (Tools.isStringEmpty(preSignEntity.getName())) {
            String responseJson = JsonHelper
                .generateResponse(-11, "failed to upload pre sign info: incorrect param 'name'",
                    null);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }
        if (Tools.isStringEmpty(preSignEntity.getData())) {
            String responseJson = JsonHelper
                .generateResponse(-12, "failed to upload pre sign info: incorrect param 'data'",
                    null);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }
        byte[] preSignData = new byte[0];
        try {
            preSignData = Base64Util.decode(preSignEntity.getData());
        } catch (IOException e) {
            errMsg = String
                .format("Failed to upload pre sign info: incorrect param 'data', error:%s",
                    e.getMessage());
            logger.warn(errMsg);
            String responseJson = JsonHelper.generateResponse(-13, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }

        // step2: 将data数据转换为PDF文件
        String preSignRecordID = UUID.randomUUID().toString().replaceAll("-", "");
        // 保存PDF文件至服务器本地
        // 路径命名规则: ${pdfBasePath}/业务机构编码/业务系统编码/年份/月份/${uuid}/${name}.pdf
        String pdfRelativePath = String
            .format("%s/%s/%s/%s/%s.pdf", preSignEntity.getBusinessSystemCode(),
                CalendarUtil.GetYear(), CalendarUtil.GetMonth(), preSignRecordID,
                preSignEntity.getName());
        String pdfFullPath = String.format("%s/%s/%s", Config.GetInstance().getPdfBasePath(),
            Config.GetInstance().getBusinessOrgCode(), pdfRelativePath);
        String pdfContent = new String(preSignData);
        logger.info("pdfContent:" + pdfContent);
        if (Tools.isStringEmpty(pdfContent) || "{}".equals(pdfContent.trim())) {
            errMsg = "Failed to upload pre sign info: incorrect param 'data', error: pdf content is Empty!";
            logger.warn(errMsg);
            String responseJson = JsonHelper.generateResponse(-14, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }

        try {
            GenPDFUtil.writeDataToPDF(pdfFullPath, pdfContent);
        } catch (FileNotFoundException e) {
            errMsg = String.format(
                "Failed to upload pre sign info, writeDataToPDF() catch FileNotFoundException:%s",
                e.getMessage());
            logger.warn(errMsg);
            String responseJson = JsonHelper.generateResponse(11, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DocumentException e) {
            errMsg = String.format(
                "Failed to upload pre sign info, writeDataToPDF() catch DocumentException:%s",
                e.getMessage());
            logger.warn(errMsg);
            String responseJson = JsonHelper.generateResponse(12, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            errMsg = String
                .format("Failed to upload pre sign info, writeDataToPDF() catch IOException:%s",
                    e.getMessage());
            logger.warn(errMsg);
            String responseJson = JsonHelper.generateResponse(13, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // step3: 生成请求报文
        String outFileId = preSignRecordID; //暂用数据库索引ID表示外部文件ID
        String fileMd5 = "";
        String pdfBase64 = "";
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(pdfFullPath));
            byte[] fileData = IOUtils.toByteArray(fileInputStream);
            fileMd5 = DigestUtils.md5Hex(fileData);
            pdfBase64 = Base64.encode(fileData);
        } catch (FileNotFoundException e) {
            errMsg = String.format(
                "Failed to upload pre sign info, get file's md5 catch FileNotFoundException:%s",
                e.getMessage());
            logger.warn(errMsg);
            String responseJson = JsonHelper.generateResponse(21, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            errMsg = String.format(
                "Failed to upload pre sign info, get file's md5 catch IOException:%s",
                e.getMessage());
            logger.warn(errMsg);
            String responseJson = JsonHelper.generateResponse(22, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger
                        .warn("Failed to close InputStream, catch IOException:{}", e.getMessage());
                }
            }
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fileName", String.format("%s.pdf", preSignEntity.getName()));
        jsonObject.addProperty("outFileId", outFileId);
        jsonObject.addProperty("fileMd5", fileMd5);
        Map<String, String> requestMap = MessageUtil
            .getChannelContext(pdfBase64, Config.GetInstance().getCloudSignAppID(),
                Config.GetInstance().getCloudSignAppSecret(), jsonObject.toString());

        JsonObject jsonObjectBody = new JsonObject();
        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            jsonObjectBody.addProperty(entry.getKey(), entry.getValue());
        }
        logger.info("data:" + jsonObject.toString());

        // step4: 将请求转发到真宜签后台
        String respResult = "";
        String url = Config.GetInstance().getCloudSignEndpoint() + "/filesign/pushPdfFile";
        try {
            respResult = HttpsClientUtil.GetInstance().HttpsPost(url, jsonObjectBody.toString());
        } catch (IOException e) {
            errMsg = String.format(
                "Failed to upload pre sign info, send requests to backend of GDCA catch IOException:%s",
                e.getMessage());
            logger.error(errMsg);
            String responseJson = JsonHelper.generateResponse(31, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CaHelperException e) {
            errMsg = String.format(
                "Failed to upload pre sign info, send requests to backend of GDCA catch CaHelperException:%s",
                e.getMessage());
            logger.error(errMsg);
            String responseJson = JsonHelper.generateResponse(32, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("respResult:" + respResult);
        CommonResp commonResp = JsonHelper.parseJsonToClass(respResult, CommonResp.class);
        if (0 != commonResp.getCode()) {
            errMsg = String.format("Failed to upload pre sign info, get GDCA abnormal response: %s",
                commonResp.getMessage());
            logger.error(errMsg);
            String responseJson = JsonHelper.generateResponse(33, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String preSignID = commonResp.getContent().getUuid();

        // step5: 将真宜签签名结果保存到数据库中
        CloudSignInfoEntity cloudSignInfoEntity = new CloudSignInfoEntity();
        cloudSignInfoEntity.setID(preSignRecordID);
        cloudSignInfoEntity.setBusinessSystemCode(preSignEntity.getBusinessSystemCode());
        cloudSignInfoEntity.setData(preSignEntity.getData());
        cloudSignInfoEntity.setPdfBasePath(Config.GetInstance().getPdfBasePath());
        cloudSignInfoEntity.setBusinessOrgCode(Config.GetInstance().getBusinessOrgCode());
        cloudSignInfoEntity.setPdfRelativePath(pdfRelativePath);
        cloudSignInfoEntity.setPreSignID(preSignID);
        try {
            cloudSignService.InsertCloudSignInfo(cloudSignInfoEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventValueEntity eventValueEntity = new EventValueEntity();
        eventValueEntity.setPreSignID(preSignID);
        String responseJson = JsonHelper
            .generateResponse(0, "upload pre sign info successfully.", eventValueEntity);
        return new ResponseEntity(responseJson, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/batchsign")
    public HttpEntity batchSign(@RequestBody BatchSignEntity batchSignEntity) {

        // step1: 参数校验
        logger.info("[Begin] Receives a batch sign request from bussiness system.");
        String errMsg = "";
        if (Tools.isStringEmpty(batchSignEntity.getAccount())) {
            String responseJson = JsonHelper
                .generateResponse(-11, "failed to batch sign: incorrect param 'account'",
                    null);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }
        if (0 == batchSignEntity.getPreSignIDs().size()) {
            String responseJson = JsonHelper
                .generateResponse(-12, "failed to batch sign: incorrect param 'preSignIDs'",
                    null);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }
        if (Tools.isStringEmpty(batchSignEntity.getSignTitle())) {
            String responseJson = JsonHelper
                .generateResponse(-13, "failed to batch sign: incorrect param 'signTitle'",
                    null);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }
        if (Tools.isStringEmpty(batchSignEntity.getNotifyUrl())) {
            String responseJson = JsonHelper
                .generateResponse(-14, "failed to batch sign: incorrect param 'notifyUrl'",
                    null);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }

        // step2: 生成请求报文
        String batchSignRecordID = UUID.randomUUID().toString().replaceAll("-", "");
        BatchSignBackendReqEntity batchSignBackendReqEntity = new BatchSignBackendReqEntity();
        batchSignBackendReqEntity.setOutSignId(batchSignRecordID);
        batchSignBackendReqEntity.setPhoneNo(batchSignEntity.getAccount());
        batchSignBackendReqEntity.setFileUuids(batchSignEntity.getPreSignIDs());
        batchSignBackendReqEntity.setSignTitle(batchSignEntity.getSignTitle());
        batchSignBackendReqEntity.setRemark(batchSignEntity.getRemark());
        batchSignBackendReqEntity.setNotifyUrl(
            Config.GetInstance().getCloudSignCallbackEndpoint() + "/v1.0/cloudsign/signnotify");
        String data = JsonHelper.generateJsonString(batchSignBackendReqEntity);
        Map<String, String> requestMap = MessageUtil
            .getChannelContext(null, Config.GetInstance().getCloudSignAppID(),
                Config.GetInstance().getCloudSignAppSecret(), data);
        JsonObject jsonObjectBody = new JsonObject();
        for (Map.Entry<String, String> entry : requestMap.entrySet()) {
            jsonObjectBody.addProperty(entry.getKey(), entry.getValue());
        }
        logger.info("data:" + data);

        // step3: 将请求转发到真宜签后台
        String respResult = "";
        try {
            respResult = HttpsClientUtil.GetInstance()
                .HttpsPost(Config.GetInstance().getCloudSignEndpoint() + "/filesign/signInitiate",
                    jsonObjectBody.toString());
        } catch (IOException e) {
            errMsg = String.format(
                "Failed to batch sign, send requests to backend of GDCA catch IOException:%s",
                e.getMessage());
            logger.error(errMsg);
            String responseJson = JsonHelper.generateResponse(21, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CaHelperException e) {
            errMsg = String.format(
                "Failed to batch sign, send requests to backend of GDCA catch CaHelperException:%s",
                e.getMessage());
            logger.error(errMsg);
            String responseJson = JsonHelper.generateResponse(22, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommonResp commonResp = JsonHelper.parseJsonToClass(respResult, CommonResp.class);
        if (0 != commonResp.getCode()) {
            errMsg = String.format("Failed to batch sign, get GDCA abnormal response: %s",
                commonResp.getMessage());
            logger.error(errMsg);
            String responseJson = JsonHelper.generateResponse(23, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String batchSignID = commonResp.getContent().getUuid();

        // step4: 将批量签署任务信息保存到数据库中
        CloudSignBatchTaskEntity cloudSignBatchTaskEntity = new CloudSignBatchTaskEntity();
        cloudSignBatchTaskEntity.setID(batchSignRecordID);
        cloudSignBatchTaskEntity.setBusinessSystemCode(batchSignEntity.getBusinessSystemCode());
        cloudSignBatchTaskEntity.setAccount(batchSignEntity.getAccount());
        StringBuilder stringBuilder = new StringBuilder();
        List<String> preSignIDs = batchSignEntity.getPreSignIDs();
        for (String preSignID : preSignIDs) {
            stringBuilder.append(preSignID).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        cloudSignBatchTaskEntity.setPreSignIDs(stringBuilder.toString());
        cloudSignBatchTaskEntity.setSignTitle(batchSignEntity.getSignTitle());
        cloudSignBatchTaskEntity.setRemark(batchSignEntity.getRemark());
        cloudSignBatchTaskEntity.setNotifyUrl(batchSignEntity.getNotifyUrl());
        cloudSignBatchTaskEntity.setBatchSignID(batchSignID);
        try {
            cloudSignService.InsertBatchTaskRecord(cloudSignBatchTaskEntity);
        } catch (Exception e) {
            errMsg = String.format(
                "Failed to batch sign, insert batch task record to DB catch Exception:%s",
                e.getMessage());
            logger.error(errMsg);
            String responseJson = JsonHelper.generateResponse(31, errMsg, null);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        EventValueEntity eventValueEntity = new EventValueEntity();
        eventValueEntity.setBatchSignID(batchSignID);
        String responseJson = JsonHelper
            .generateResponse(0, "batch sign successfully.", eventValueEntity);
        return new ResponseEntity(responseJson, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/signnotify")
    public HttpEntity signNotify(@RequestBody String requestBody) {
        logger.info("Receives an asynchronous callback from the backend of GDCA, requestBody:{}",
            requestBody);

        String errMsg = "";
        CommonResp commonResp = new CommonResp();

        // step1: 校验请求参数
        SignNotifyEntity signNotifyEntity = null;
        String batchSignID = "";
        List<Files> filesList = null;
        try {
            signNotifyEntity = JsonHelper.parseJsonToClass(requestBody, SignNotifyEntity.class);
            filesList = JsonHelper
                .parseJsonToList(signNotifyEntity.getFiles(), Files.class);
            batchSignID = signNotifyEntity.getUuid();
            logger.info("Parse request body successfully, batchSignID:{}", batchSignID);
        } catch (Exception e) {
            errMsg = String.format("Failed to parse request body, error:%s", e.getMessage());
            logger.error(errMsg);
            commonResp.setCode(-11);
            commonResp.setMessage(errMsg);
            String responseJson = JsonHelper.generateJsonString(commonResp);
            return new ResponseEntity(responseJson, HttpStatus.BAD_REQUEST);
        }

        // step2: 状态检查
        final Integer signStatusFinish = 2;
        if (!signNotifyEntity.getSignStatus().equals(signStatusFinish)) {
            // 未办结状态，不做判断
            logger.info(
                "The status of callback request is {}, don't need do anything, batchSignID:{}",
                signNotifyEntity.getStatusStr(), batchSignID);
            commonResp.setCode(0);
            String responseJson = JsonHelper.generateJsonString(commonResp);
            return new ResponseEntity(responseJson, HttpStatus.OK);
        }

        // step3: 查询接入系统回调地址
        String notifyUrl = "";
        try {
            notifyUrl = cloudSignService.QueryNotifyUrlByBatchSignID(batchSignID);
            if (Tools.isStringEmpty(notifyUrl)) {
                commonResp.setCode(0);
                commonResp.setMessage("Expired data, ignore");
                String responseJson = JsonHelper.generateJsonString(commonResp);
                return new ResponseEntity(responseJson, HttpStatus.OK);
            }
            logger.info("Query notify url from DB successfully, batchSignID:{}, notifyUrl:{}",
                batchSignID, notifyUrl);
        } catch (Exception e) {
            errMsg = String.format("Failed to query notify url from DB, batchSignID:%s, error:%s",
                batchSignID, e.getMessage());
            logger.error(errMsg);
            commonResp.setCode(11);
            commonResp.setMessage(errMsg);
            String responseJson = JsonHelper.generateJsonString(commonResp);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // step4: 回调接入系统
        String respResult = "";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("batchSignID", batchSignID);
        jsonObject.addProperty("signResult", signNotifyEntity.getSignResult());
        try {
            respResult = HttpsClientUtil.GetInstance().HttpsPost(notifyUrl, jsonObject.toString());
            logger.info(
                "Send requests to bussiness system successfully, batchSignID:{}, respResult:{}",
                batchSignID, respResult);
            ResponseBody responseBody = JsonHelper.parseResponse(respResult);
            if (0 != responseBody.getStatusCode()) {
                throw new CaHelperException(responseBody.getEventMsg());
            }

        } catch (IOException e) {
            errMsg = String.format(
                "Failed to send requests to bussiness system, catch IOException:%s",
                e.getMessage());
            logger.error(errMsg);

            commonResp.setCode(21);
            commonResp.setMessage(errMsg);
            String responseJson = JsonHelper.generateJsonString(commonResp);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (CaHelperException e) {
            errMsg = String.format(
                "Failed to send requests to bussiness system, catch CaHelperException:%s",
                e.getMessage());
            logger.error(errMsg);

            commonResp.setCode(22);
            commonResp.setMessage(errMsg);
            String responseJson = JsonHelper.generateJsonString(commonResp);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // step5: 更新数据库记录
        try {
            cloudSignService.UpdateCloudSignResult(signNotifyEntity.getFinishTimeStr(),
                signNotifyEntity.getSignStatus(), signNotifyEntity.getSignResult(), batchSignID,
                filesList);
            logger.info("Update batch result to DB successfully, batchSignID:{}", batchSignID);
        } catch (Exception e) {
            errMsg = String.format("Failed to update batch result to DB, batchSignID:%s, error:%s",
                batchSignID, e.getMessage());
            logger.error(errMsg);
            commonResp.setCode(31);
            commonResp.setMessage(errMsg);
            String responseJson = JsonHelper.generateJsonString(commonResp);
            return new ResponseEntity(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // step6：返回响应结果
        commonResp.setCode(0);
        commonResp.setMessage("Sign notify successfully");
        String responseJson = JsonHelper.generateJsonString(commonResp);
        return new ResponseEntity(responseJson, HttpStatus.OK);

    }

}
