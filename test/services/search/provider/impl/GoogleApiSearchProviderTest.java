package services.search.provider.impl;

import com.akavita.metasearch.keystorage.dao.mysql.MySqlKeyDaoImpl;
import com.akavita.metasearch.keystorage.dao.mysql.MysqlGoogleKeyDao;
import com.akavita.metasearch.keystorage.domain.GoogleKey;
import com.akavita.metasearch.keystorage.domain.GoogleKeyValue;
import com.akavita.metasearch.keystorage.service.KeyProvider;
import model.ResponseItem;
import model.SearchRequest;
import model.SearchResponse;
import model.SearchType;
import org.codehaus.jackson.JsonNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ReflectionUtils;
import play.libs.F;
import play.libs.Json;
import services.spring.SpringContextLoader;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 * GoogleApiSearchProviderTest ...
 *
 * @author vadim
 * @date 12/14/12
 */
public class GoogleApiSearchProviderTest {

    private GoogleApiSearchProvider provider;
    private MysqlGoogleKeyDao dao;
    private GoogleKey key;

    public GoogleApiSearchProviderTest() throws NoSuchFieldException {
        ClassPathXmlApplicationContext context = SpringContextLoader.getContext("keyProviderTestBeans.xml");
        provider = new GoogleApiSearchProvider(context.getBean("googleMysqlKeyProvider", KeyProvider.class));
        dao = context.getBean("googleMySqlKeyDao", MysqlGoogleKeyDao.class);
        Field field = MySqlKeyDaoImpl.class.getDeclaredField("timestampDiffStatement");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, dao, "DATEDIFF('ms', current_timestamp, start_using)");
    }

    @Before
    public void before() {
        if (key == null) {
            key = new GoogleKey();
            key.setMaxUsages(100);
            key.setUsagePeriod(1000 * 60 * 60 * 24L);
            key.setStartUsing(new Date());
            key.setProvider("google");
            key.setValue(new GoogleKeyValue("013117637915591670905:4dhe-6tg-2w", "AIzaSyA6GYnRYIFj2A1vIYcqbhpCjfDlEbQ1tfs"));
            dao.save(key);
        }
    }

    @After
    public void after() {
    }

    @Test
    public void testParse() {
        String jsonStr = "{\"items\":[{\"title\":\"Samsung Galaxy S3 vs. iPhone 4S Drop Test - YouTube\",\"link\":\"http://www.youtube.com/watch?v=K7e9ebi41Wc\",\"snippet\":\"3 Jun 2012 ... http://www.androidauthority.com/ - Can the brand new Samsung Galaxy S3 pass   the drop test? Find out as we do a drop test comparison of ...\"},{\"title\":\"True Internet Speed Test\",\"link\":\"http://speedtest.trueinternet.co.th/\",\"snippet\":\"The Ookla Speed Test requires at least version 8 of Flash. Please update your   client. 0. 256k. 512k. 1M. 1.5M. 3M. 5M. 10M. 20M+. ถDownload Speed. 00000 ...\"},{\"title\":\"Google Testing Blog\",\"link\":\"http://googletesting.blogspot.com/\",\"snippet\":\"We found that we were struggling with existing tools, so we decided to write our   own test runner. We wanted a test runner that would meet all of our needs for ...\"},{\"title\":\"TOT Bandwidth Speed Test\",\"link\":\"http://speedtest1.totbb.net/\",\"snippet\":\"TOT Bandwidth Speed Test requires at least version 8 of Flash. Please update   your client. 0. 256k. 512k. 1M. 1.5M. 3M. 5M. 10M. 20M+. Download Speed ...\"},{\"title\":\"Thompson Prometric GED Testing\",\"link\":\"https://www.prometric.com/en-us/clients/ged\",\"snippet\":\"Provides testing services. Testing site locator and scheduling service.\"},{\"title\":\"JavaScript Test Console - Facebook Developers\",\"link\":\"http://developers.facebook.com/tools/console/\",\"snippet\":\"Graph API Explorer · JavaScript Test Console · App Dashboard · Insights · Beta   Tier · Test User API · Debugger · Access Token Tool · Ads Manager · Action Spec ...\"},{\"title\":\".test - Wikipedia, the free encyclopedia\",\"link\":\"http://en.wikipedia.org/wiki/.test\",\"snippet\":\"The name test is reserved by the Internet Engineering Task Force (IETF) in RFC   2606 (June 1999) as a domain name that is not intended to be installed as a ...\"},{\"title\":\"Unit testing - Wikipedia, the free encyclopedia\",\"link\":\"http://en.wikipedia.org/wiki/Unit_testing\",\"snippet\":\"In computer programming, unit testing is a method by which individual units of   source code, sets of one or more computer program modules together with ...\"},{\"title\":\"Test method - Wikipedia, the free encyclopedia\",\"link\":\"http://en.wikipedia.org/wiki/Test_method\",\"snippet\":\"A test method is a definitive procedure that produces a test result. A test can be   considered as technical operation that consists of determination of one or more ...\"},{\"title\":\"Statistical hypothesis testing - Wikipedia, the free encyclopedia\",\"link\":\"http://en.wikipedia.org/wiki/Statistical_hypothesis_testing\",\"snippet\":\"A statistical hypothesis test is a method of making decisions using data, whether   from a controlled experiment or an observational study (not controlled).\"}]}";
        JsonNode json = Json.parse(jsonStr);
        List<ResponseItem> items = provider.parse(json, SearchType.DOCS);
        Assert.assertNotNull(items);
        Assert.assertEquals(10, items.size());
        double score = 1d;
        for (ResponseItem item : items) {
            Assert.assertNotNull(item.getUrl());
            Assert.assertNotNull(item.getSnippet());
            Assert.assertNotNull(item.getTitle());
            Assert.assertEquals(SearchType.DOCS, item.getType());
            Assert.assertEquals(score, item.getScore(), 0d);
            score /= 2;
        }
    }

    @Test
    public void testEmptyParse() {
        JsonNode json = Json.parse("{\"test\":1}");
        List<ResponseItem> items = provider.parse(json, SearchType.DOCS);
        Assert.assertNotNull(items);
        Assert.assertEquals(0, items.size());
    }

    @Test
    public void testDoSearch() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                SearchRequest request = new SearchRequest();
                request.setQuery("test");
                request.setCh("UTF-8");
                request.setLang("th");
                request.setNumber(10);
                request.setPage(1);
                request.setRegion("TH");
                request.addSearchTypes(SearchType.DOCS);
                F.Promise<SearchResponse> promise = provider.doSearch(SearchType.DOCS, request);
                SearchResponse resp = promise.get();
                Assert.assertNotNull(resp);
                Assert.assertEquals(10, resp.getItems().size());
                Assert.assertEquals(SearchType.DOCS, resp.getSearchType());
            }
        });
    }
}
