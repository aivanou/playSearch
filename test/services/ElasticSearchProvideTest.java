package services;

import model.SearchEngineType;
import model.SearchType;
import model.request.InternalContentRequest;
import model.response.ContentResponse;
import model.response.SuccessContentResponse;
import org.elasticsearch.common.netty.handler.codec.http.HttpHeaders;
import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import play.libs.F;
import play.libs.WS;
import services.search.provider.SearchProvider;
import services.search.provider.impl.ElasticSearchProvider;
import util.SearchConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

public class ElasticSearchProvideTest {


    static final String url = "http://localhost:9200";
    static final String index = "linky";

    @BeforeClass
    public static void addDocs() throws IOException, JSONException {
        SearchConfiguration.fromFile("/home/aliaksandr/study/code/search_ok/guppy-play/conf/query.json");
        createIndex(url, index).get();
        addRandomDocuments(url + "/" + index + "/document", 20);
    }


    @AfterClass
    public static void deleteIndex() {
//        deleteIndex(url + "/" + index).get();
    }

    @Test
    public void searchContentTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                SearchProvider provider = new ElasticSearchProvider(new SearchEngineType("elastic"));
                List<String> searchFields = Arrays.asList("title", "content");
                Map<SearchEngineType, List<String>> hosts = new HashMap<>();
                hosts.put(new SearchEngineType("elastic"), Arrays.asList("http://localhost:9200"));
                SearchType stype = new SearchType("document", searchFields, hosts);
                try {
                    ContentResponse resp = provider.doSearch(new InternalContentRequest("testtest", stype, 10, 0)).get();
                    Assert.assertTrue(resp instanceof SuccessContentResponse);
                    SuccessContentResponse sresp = (SuccessContentResponse) resp;
                    Assert.assertEquals(10, sresp.getItems().size());
                } catch (Throwable th) {
                    Assert.fail(th.getMessage());
                }
            }

        });
    }

    @Test
    public void searchTitleTest() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                SearchProvider provider = new ElasticSearchProvider(new SearchEngineType("elastic"));
                List<String> searchFields = Arrays.asList("title", "content");
                Map<SearchEngineType, List<String>> hosts = new HashMap<>();
                hosts.put(new SearchEngineType("elastic"), Arrays.asList("http://localhost:9200"));
                SearchType stype = new SearchType("document", searchFields, hosts);
                try {
                    ContentResponse resp = provider.doSearch(new InternalContentRequest("testtitle", stype, 10, 0)).get();
                    Assert.assertTrue(resp instanceof SuccessContentResponse);
                    SuccessContentResponse sresp = (SuccessContentResponse) resp;
                    Assert.assertEquals(10, sresp.getItems().size());
                } catch (Throwable th) {
                    Assert.fail(th.getMessage());
                }
            }

        });
    }

    public static void addRandomDocuments(String url, int number) {
        for (int i = 0; i < number; ++i) {
            try {
                System.out.println(addRandomDocument(url).get().getBody());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static F.Promise<WS.Response> deleteIndex(String url) {
        return WS.url(url).delete();
    }


    public static F.Promise<WS.Response> createIndex(String url, String index) {
        return WS.url(url + "/" + index).put("");
    }

    public static F.Promise<WS.Response> addRandomDocument(String url) throws UnsupportedEncodingException {
        String title = "testtitle " + randomText(2, 4, 5, 8);
        String text = "testtest  " + randomText(8, 20, 3, 8);
        String docUrl = "http://test" + randomString(5, 10) + ".com/";
        return addDocument(title, text, docUrl, System.nanoTime(), url);
    }

    public static F.Promise<WS.Response> addDocument(String title, String content, String documentUrl, long indexed, String url) throws UnsupportedEncodingException {
        String q = "{\n" +
                "        \"content\":\"%s\",\n" +
                "        \"title\":\"%s\",\n" +
                "        \"url\":\"%s\",\n" +
                "        \"indexed\":%s\n" +
                "}";
        String query = String.format(q, content, title, documentUrl, indexed);
        return execute(url, query, 3000);
    }

    public static F.Promise<WS.Response> execute(String url, String query, int timeout) throws UnsupportedEncodingException {
        InputStream queryStream = new ByteArrayInputStream(query.getBytes("UTF-8"));
        return WS.url(url).setTimeout(timeout)
                .setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8")
                .post(queryStream);
    }

    public static String randomText(int min, int max, int minStr, int maxStr) {
        StringBuilder text = new StringBuilder();
        int len = new Random().nextInt(max - min) + min;
        for (int i = 0; i < len; ++i) {
            text.append(randomString(minStr, maxStr));
            text.append(" ");
        }
        return text.toString();
    }

    public static String randomString(int min, int max) {
        StringBuilder str = new StringBuilder();
        int len = new Random().nextInt(max - min) + min;
        for (int i = 0; i < len; ++i) {
            str.append(randomCharacter());
        }
        return str.toString();
    }

    public static char randomCharacter() {
        Random r = new Random();
        return (char) (r.nextInt(122 - 97) + 97);
    }
}
