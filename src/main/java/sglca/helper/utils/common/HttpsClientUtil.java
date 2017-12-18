package sglca.helper.utils.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import sglca.helper.models.CaHelperException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;

public class HttpsClientUtil {

    /**
     * 返回连接失败信息
     **/
    private String strResult = "{\"code\":-1,\"message\":\"服务器无法连接，请稍后再试\"}";
    /**
     * httpClient 对象
     **/
    private static DefaultHttpClient httpClient = null;
    /**
     * 连接连接池的管理器超时参数
     **/
    private int timeOut = 100 * 1000;
    /**
     * 连接超时参数
     **/
    private int connectionTimeOut = 100 * 1000;
    /**
     * Socket超时参数
     **/
    private int soTimeOut = 100 * 1000;

    private final static Object syncObj = new Object();
    private static sglca.helper.utils.common.HttpsClientUtil instance;

    private HttpsClientUtil() {
        GetHttpClient();
    }

    public static sglca.helper.utils.common.HttpsClientUtil GetInstance() {
        if (null == instance) {
            synchronized (syncObj) {
                instance = new sglca.helper.utils.common.HttpsClientUtil();
            }
            return instance;
        }
        return instance;
    }

    /**
     * 得到 apache http HttpClient对象(Https)
     */
    public DefaultHttpClient GetHttpClient() {
        if (null == httpClient) {
            // 初始化工作
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
                trustStore.load(null, null);
                SSLSocketFactory sf = new sglca.helper.utils.common.SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // 允许所有主机的验证

                HttpParams params = new BasicHttpParams();

                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params,
                    HTTP.DEFAULT_CONTENT_CHARSET);
                HttpProtocolParams.setUseExpectContinue(params, true);

                // 设置连接管理器的超时
                ConnManagerParams.setTimeout(params, timeOut);
                // 设置连接超时
                HttpConnectionParams.setConnectionTimeout(params,
                    connectionTimeOut);
                // 设置socket超时
                HttpConnectionParams.setSoTimeout(params, soTimeOut);

                // 设置http https支持
                SchemeRegistry schReg = new SchemeRegistry();
                schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
                schReg.register(new Scheme("https", sf, 443));

                ClientConnectionManager conManager = new ThreadSafeClientConnManager(
                    params, schReg);

                httpClient = new DefaultHttpClient(conManager, params);
            } catch (Exception e) {
                e.printStackTrace();
                return new DefaultHttpClient();
            }
        }
        return httpClient;
    }

    /**
     * @param url 地址
     * @param json jsondata
     */
    public String HttpsPost(String url, String json) throws IOException, CaHelperException {
        HttpPost httpRequest = new HttpPost(url);
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setEntity(new StringEntity(json, "UTF-8"));

        /** 保持会话Session end **/
        HttpResponse response = httpClient.execute(httpRequest);
        HttpEntity entity = response.getEntity();
        String responseEntity = EntityUtils.toString(entity);
        int httpStatusCode = response.getStatusLine().getStatusCode();
        if (HttpURLConnection.HTTP_OK != httpStatusCode) {
            if (null != entity) {
                entity.consumeContent();
            }
            throw new CaHelperException(responseEntity);
        }
        if (null != entity) {
            entity.consumeContent();
        }
        return responseEntity;
    }
}

class SSLSocketFactoryEx extends SSLSocketFactory {

    SSLContext sslContext = SSLContext.getInstance("TLS");

    public SSLSocketFactoryEx(KeyStore truststore)
        throws NoSuchAlgorithmException, KeyManagementException,
        KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {

            }

            public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {

            }
        };

        sslContext.init(null, new TrustManager[]{tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
        boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port,
            autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}