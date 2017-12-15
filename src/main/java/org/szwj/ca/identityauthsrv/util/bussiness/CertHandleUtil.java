package org.szwj.ca.identityauthsrv.util.bussiness;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import net.netca.pki.PkiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sglca.helper.netca.NetcaPKI;
import sglca.helper.netca.secuauth.ClientInterface;
import sglca.helper.utils.ConfigUtil;
import sglca.helper.utils.JsonHelper;

public class CertHandleUtil {

    private static final Logger logger = LoggerFactory.getLogger(CertHandleUtil.class);

    private static CertHandleUtil certHandleUtil = new CertHandleUtil();

    private static Boolean startCertAuth;

    private static String certAuthUrl;

    private static String serverCert;

    private CertHandleUtil() {
        try {
            // begin--从配置文件中读取配置参数电子认证网关和电子认证网关证书--
            String userDir = System.getProperty("user.dir");
            Map<String, String> propertiesMap = ConfigUtil
                .readProperties(userDir + "/config/application.properties");
            startCertAuth = Boolean.valueOf(propertiesMap.get("start.cert.auth"));
            certAuthUrl = propertiesMap.get("cert.auth.url");
            serverCert = propertiesMap.get("server.cert");
            // end--从配置文件中读取配置参数电子认证网关和电子认证网关证书--
        } catch (FileNotFoundException e) {
            logger.error("CertHandleUtil(): failed to read properties file, error: {}",
                e.getMessage());
        } catch (IOException e) {
            logger.error("CertHandleUtil(): failed to read properties file, error: {}",
                e.getMessage());
        }
    }

    /**
     * 返回单例CaHelper实例
     */
    public static CertHandleUtil getCertHandleUtil() {
        return certHandleUtil;
    }

    public static String VerifyCertByAuthGw(int statusCode, String signCert) {
        // 不需要进行认证网关验证
        if (!startCertAuth) {
            return null;
        }

        try {
            byte[] bSevCert = NetcaPKI.base64Decode(serverCert);
            ClientInterface inf = new ClientInterface(certAuthUrl, bSevCert);
            // 返回int[2],第一个为响应码,第二个为证书状态码
            int[] ret = inf.checkCert(signCert);
            // ret[0]和ret[1]都为0时证书验证通过，否则验证失败
            if (ret[0] != 0) {
                String responesJson = JsonHelper.generateResponse(statusCode, String
                    .format("failed to verify cert by authentication gateway, error: %s.",
                        ClientInterface.parseEchoCode(ret[0])), null);
                return responesJson;
            } else if (ret[1] != 0) {
                String responesJson = JsonHelper.generateResponse(statusCode, String
                    .format("failed to verify cert by authentication gateway, error: %s.",
                        ClientInterface.parseEchoCode(ret[1])), null);
                return responesJson;
            }
        } catch (PkiException e) {
            String responesJson = JsonHelper.generateResponse(statusCode, String
                .format("failed to verify cert by authentication gateway, error: %s.",
                    e.getMessage()), null);
            return responesJson;
        } catch (Exception e) {
            String responesJson = JsonHelper.generateResponse(statusCode, String
                .format("failed to verify cert by authentication gateway, error: %s.",
                    e.getMessage()), null);
            return responesJson;
        }
        return null;
    }
}
