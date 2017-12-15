package org.szwj.ca.identityauthsrv;

import com.gdca.signofcloud.common.util.Base64;
import com.gdca.signofcloud.common.util.MessageUtil;
import com.google.gson.JsonObject;
import org.szwj.ca.identityauthsrv.config.Config;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CloudSignInfoEntity;
import org.szwj.ca.identityauthsrv.entity.cloudsign.CommonResp;
import org.szwj.ca.identityauthsrv.service.intfc.CloudSignService;
import org.szwj.ca.identityauthsrv.util.common.FileUtil;
import org.szwj.ca.identityauthsrv.util.common.HttpsClientUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import sglca.helper.SZWJ_CaHelper;
import sglca.helper.ICaHelper;
import sglca.helper.models.CaHelperException;
import sglca.helper.utils.ConfigUtil;
import sglca.helper.utils.JsonHelper;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    private static final Logger logger = LoggerFactory.getLogger(SchedulingConfig.class);

    private static Map<String, String> propertiesMap;

    private static String tsaAuthority;

    @Autowired
    private CloudSignService cloudSignService;

    public SchedulingConfig() {
        String userDir = System.getProperty("user.dir");
        try {
            propertiesMap = ConfigUtil.readProperties(userDir + "/config/application.properties");
        } catch (FileNotFoundException e) {
            logger.error("SchedulingConfig(): failed to read properties file, error: {}",
                e.getMessage());
        } catch (IOException e) {
            logger.error("SchedulingConfig(): failed to read properties file, error: {}",
                e.getMessage());
        }
        tsaAuthority = propertiesMap.get("tsa.authority");
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void signTimestamp() {
        logger.debug("[Begin] Sign timestamp with TSA.");
        ICaHelper helper = SZWJ_CaHelper.getCaHelperImplByAuthority(tsaAuthority);
        if (null == helper) {
            logger.error("Failed to get tsa authority, tsaAuthority: {}", tsaAuthority);
            logger.debug("[End] Sign timestamp with TSA.");
            return;
        }
        if (SZWJ_CaHelper.BJCA_AUTHORITY.equals(tsaAuthority)) {
            try {
                helper.SignWithTSA(null, "Test TSA Stability");
            } catch (CaHelperException e) {
                logger.error("Sign data with TSA failed, error: {}.", e.getMessage());
            }
        }
        logger.debug("[End] Sign timestamp with TSA.");
    }

    @Scheduled(cron = "0 0 4 * * ?")
    private void DownloadSignFiles() {
        logger.info("[Begin] Download cloud signed files.");
        List<CloudSignInfoEntity> cloudSignInfoEntityList = cloudSignService
            .QuerySignInfoByStatus(cloudSignService.STATUS_OF_SUCCESS);
        for (CloudSignInfoEntity cloudSignInfoEntity : cloudSignInfoEntityList) {
            String outFileId = cloudSignInfoEntity.getID();
            String batchSignID = cloudSignInfoEntity.getBatchSignID();
            String filedId = cloudSignInfoEntity.getFiledId();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("filedId", filedId);
            jsonObject.addProperty("signuuid", batchSignID);
            Map<String, String> requestMap = MessageUtil
                .getChannelContext(null, Config.GetInstance().getCloudSignAppID(),
                    Config.GetInstance().getCloudSignAppSecret(), jsonObject.toString());
            JsonObject jsonObjectBody = new JsonObject();
            for (Map.Entry<String, String> entry : requestMap.entrySet()) {
                jsonObjectBody.addProperty(entry.getKey(), entry.getValue());
            }
            logger.info("data:" + jsonObject.toString());
            logger.info("requestBody:" + jsonObjectBody.toString());

            String respResult = "";
            try {
                respResult = HttpsClientUtil.GetInstance().HttpsPost(
                    Config.GetInstance().getCloudSignEndpoint() + "/channel/downloadFile",
                    jsonObjectBody.toString());
            } catch (CaHelperException e) {
                logger.warn(
                    "Failed to download cloud signed, filedId:{}, signuuid:{}, catch CaHelperException:{}",
                    filedId, batchSignID, e.getMessage());
                continue;
            } catch (IOException e) {
                logger.warn(
                    "Failed to download cloud signed, filedId:{}, signuuid:{}, catch IOException:{}",
                    outFileId, batchSignID, e.getMessage());
                continue;
            }
            CommonResp commonResp = JsonHelper.parseJsonToClass(respResult, CommonResp.class);
            if (0 != commonResp.getCode() || null == commonResp.getContent()) {
                logger.warn(
                    "Failed to download cloud signed, filedId:{}, signuuid:{}, respResult:{}",
                    outFileId, batchSignID, respResult);
                continue;
            }

            String pdfFullPath = String.format("%s/%s/%s", cloudSignInfoEntity.getPdfBasePath(),
                cloudSignInfoEntity.getBusinessOrgCode(), cloudSignInfoEntity.getPdfRelativePath());
            byte[] fileContent = Base64.decode(commonResp.getContent().getFileBase64Str());
            try {
                FileUtil.writeByteArrayToFile(new File(pdfFullPath), fileContent);
            } catch (IOException e) {
                logger.warn(
                    "Failed to write file content to file, filedId:{}, signuuid:{}, catch IOException:{}",
                    outFileId, batchSignID, e.getMessage());
                continue;
            }

            try {
                cloudSignService
                    .UpdateCloudSignInfoStatus(cloudSignService.STATUS_OF_DOWNLOAD, outFileId);
            } catch (Exception e) {
                logger.warn(
                    "Failed to download cloud signed, filedId:{}, signuuid:{}, catch Exception:{}",
                    outFileId, batchSignID, e.getMessage());
                continue;
            }
        }

        logger.info("[End] Download cloud signed files.");
    }

}
