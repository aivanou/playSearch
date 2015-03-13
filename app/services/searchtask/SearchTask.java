package services.searchtask;

import com.ning.http.client.PerRequestConfig;
import com.ning.http.client.ProxyServer;
import play.libs.F;
import play.libs.WS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author egor, Date: 12/21/12
 */
public abstract class SearchTask {
    protected static final String METHOD = "GET";
    private int timeout;

    protected SearchTask(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns gzipped response from remote search engine.
     */
    public abstract F.Promise<WS.Response> doSearch();

    protected WS.WSRequest getRequest(String requestURL, Map<String, String> headers) {
        WS.WSRequest request = new WS.WSRequest(METHOD);
        request.setUrl(requestURL);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            request.setHeader(header.getKey(), header.getValue());
        }
        if (timeout > 0) {
            PerRequestConfig config = new PerRequestConfig();
            config.setRequestTimeoutInMs(timeout);
            request.setPerRequestConfig(config);
        }
        return request;
    }

    protected WS.WSRequest getRequest(String requestURL, Map<String, String> headers, ProxyServer proxyServer) {
        return getRequest(requestURL, headers).setProxyServer(proxyServer);
    }

    /**
     * Decompress gzipped response into plain string if needed
     */
    public static String decompress(java.io.InputStream is){
        String content = "";
        try {
            GZIPInputStream gzis = new GZIPInputStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int value = 0; value != -1;) {
                value = gzis.read();
                if (value != -1) {
                    baos.write(value);
                }
            }
            gzis.close();
            baos.close();
            content = new String(baos.toByteArray(), "UTF-8");
            return content;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
