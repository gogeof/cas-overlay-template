package org.szwj.ca.identityauthsrv.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sglca.helper.SZWJ_CaHelper;
import sglca.helper.utils.ConfigUtil;

public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static Config config = new Config();

    public static Config GetInstance() {
        return config;
    }

    // TSA
    private String tsaAuthority;

    // PDF
    private String pdfBasePath;

    private String businessOrgCode;

    // CloudSign
    private String cloudSignEndpoint;

    private String cloudSignAppID;

    private String cloudSignAppSecret;

    private String cloudSignAppSecretName;

    private String cloudSignCallbackEndpoint;

    private Config() {
        try {
            String userDir = System.getProperty("user.dir");
            Map<String, String> propertiesMap = ConfigUtil
                .readProperties(userDir + "/config/application.properties");

            // TSA
            tsaAuthority = propertiesMap.get("tsa.authority");
            if (null == SZWJ_CaHelper.getCaHelperImplByAuthority(tsaAuthority)) {
                logger.error("Config(): failed to get tsa authority");
            } else {
                logger.info("Config(): init successfully, tsa.authority: {}", tsaAuthority);
            }

            // PDF
            pdfBasePath = propertiesMap.get("pdf.base.path");
            businessOrgCode = propertiesMap.get("business.organization.code");

            // CloudSign
            cloudSignEndpoint = propertiesMap.get("cloudsign.remote.endpoint");
            cloudSignAppID = propertiesMap.get("cloudsign.app.id");
            cloudSignAppSecret = propertiesMap.get("cloudsign.app.secret");
            cloudSignAppSecretName = propertiesMap.get("cloudsign.app.secret.name");
            cloudSignCallbackEndpoint = propertiesMap.get("cloudsign.callback.endpoint");
        } catch (FileNotFoundException e) {
            logger.error("Config(): failed to read properties file, error: {}",
                e.getMessage());
        } catch (IOException e) {
            logger.error("Config(): failed to read properties file, error: {}",
                e.getMessage());
        }
    }

    public String getPdfBasePath() {
        return pdfBasePath;
    }

    public String getBusinessOrgCode() {
        return businessOrgCode;
    }

    public String getTsaAuthority() {
        return tsaAuthority;
    }

    public String getCloudSignEndpoint() {
        return cloudSignEndpoint;
    }

    public String getCloudSignAppID() {
        return cloudSignAppID;
    }

    public String getCloudSignAppSecret() {
        return cloudSignAppSecret;
    }

    public String getCloudSignAppSecretName() {
        return cloudSignAppSecretName;
    }

    public String getCloudSignCallbackEndpoint() {
        return cloudSignCallbackEndpoint;
    }
}
