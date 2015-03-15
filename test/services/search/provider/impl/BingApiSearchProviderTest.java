package services.search.provider.impl;

import com.akavita.metasearch.keystorage.dao.mysql.MySqlKeyDaoImpl;
import com.akavita.metasearch.keystorage.dao.mysql.MysqlStringKeyDao;
import com.akavita.metasearch.keystorage.domain.StringKey;
import com.akavita.metasearch.keystorage.domain.StringKeyValue;
import com.akavita.metasearch.keystorage.service.KeyProvider;
import model.response.ResponseItem;
import model.request.SearchRequest;
import model.SearchType;
import org.codehaus.jackson.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ReflectionUtils;
import play.libs.Json;
import services.spring.SpringContextLoader;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 * BingApiSearchProviderTest ...
 *
 * @author vadim
 * @date 12/14/12
 */
public class BingApiSearchProviderTest {

    private BingApiSearchProvider provider;
    private MysqlStringKeyDao dao;
    private StringKey key;

    public BingApiSearchProviderTest() throws NoSuchFieldException {
        ClassPathXmlApplicationContext context = SpringContextLoader.getContext("keyProviderTestBeans.xml");
        provider = new BingApiSearchProvider(context.getBean("bingMysqlKeyProvider", KeyProvider.class));
        dao = context.getBean("stringMySqlKeyDao", MysqlStringKeyDao.class);
        Field field = MySqlKeyDaoImpl.class.getDeclaredField("timestampDiffStatement");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, dao, "DATEDIFF('ms', current_timestamp, start_using)");
    }

    @Before
    public void before() {
        if (key == null) {
            key = new StringKey();
            key.setMaxUsages(5000);
            key.setUsagePeriod(1000 * 60 * 60 * 24L * 30);
            key.setStartUsing(new Date());
            key.setProvider("bing");
            key.setValue(new StringKeyValue("JXG7B4BB821sg4/qXSn5XdGB3Z+XPlcGiIX9gLG3nX0="));
            dao.save(key);
        }
    }

    @Test
    public void testParse() {
        String jsonStr = "{\"d\":{\"results\":[{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=0&$top=1\",\"type\":\"WebResult\"},\"ID\":\"17ed1060-3177-46ab-86ea-8e3a6aa2b71a\",\"Title\":\"Test Central\",\"Description\":\"Provides extranet privacy to clients making a range of tests and surveys available to their human resources departments. Companies can test prospective and current ...\",\"DisplayUrl\":\"www.test.com\",\"Url\":\"http://www.test.com/\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=1&$top=1\",\"type\":\"WebResult\"},\"ID\":\"f5415204-2fa7-4555-90a8-8c1f8cab54bd\",\"Title\":\"Speakeasy Speed Test\",\"Description\":\"Test your Internet Connection with Speakeasy\\u0027s reliable and accurate broadband speed test. What\\u0027s your speed?\",\"DisplayUrl\":\"www.speakeasy.net/speedtest\",\"Url\":\"http://www.speakeasy.net/speedtest/\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=2&$top=1\",\"type\":\"WebResult\"},\"ID\":\"20645b27-3c14-4bbe-9dd3-62b1fd1c8f3a\",\"Title\":\"Test - Wikipedia, the free encyclopedia\",\"Description\":\"Test, TEST or Tester may refer to: Test (assessment), an assessment intended to measure the respondents\\u0027 knowledge or other abilities Physical fitness test Driving ...\",\"DisplayUrl\":\"en.wikipedia.org/wiki/Test\",\"Url\":\"http://en.wikipedia.org/wiki/Test\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=3&$top=1\",\"type\":\"WebResult\"},\"ID\":\"59e5ba8f-e9f1-46b5-8c48-de4c1f4f10f2\",\"Title\":\"Speedtest.net\",\"Description\":\"Bandwidth test where one can choose among hundreds of geographically dispersed servers around the world. Also shows a summary of one\\u0027s tests and also the tests by ...\",\"DisplayUrl\":\"www.speedtest.net\",\"Url\":\"http://www.speedtest.net/\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=4&$top=1\",\"type\":\"WebResult\"},\"ID\":\"dfd0d30a-e337-44d3-88bb-4790936ec2e9\",\"Title\":\"test - definition of test by the Free Online Dictionary, Thesaurus ...\",\"Description\":\"test 1 (t st) n. 1. A procedure for critical evaluation; a means of determining the presence, quality, or truth of something; a trial: a test of one\\u0027s eyesight ...\",\"DisplayUrl\":\"www.thefreedictionary.com/test\",\"Url\":\"http://www.thefreedictionary.com/test\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=5&$top=1\",\"type\":\"WebResult\"},\"ID\":\"9927311a-136f-404d-8835-e2ab58d16585\",\"Title\":\"Test | Define Test at Dictionary.com\",\"Description\":\"noun 1. the means by which the presence, quality, or genuineness of anything is determined; a means of trial. 2. the trial of the quality of something: to put to the ...\",\"DisplayUrl\":\"dictionary.reference.com/browse/test\",\"Url\":\"http://dictionary.reference.com/browse/test\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=6&$top=1\",\"type\":\"WebResult\"},\"ID\":\"746d08d1-0f82-4283-b6fd-2c63a032e7d8\",\"Title\":\"Test (assessment) - Wikipedia, the free encyclopedia\",\"Description\":\"A test or exam(ination) is an assessment intended to measure a test-taker\\u0027s knowledge, skill, aptitude, physical fitness, or classification in many other topics (e.g ...\",\"DisplayUrl\":\"en.wikipedia.org/wiki/Test_(assessment)\",\"Url\":\"http://en.wikipedia.org/wiki/Test_(assessment)\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=7&$top=1\",\"type\":\"WebResult\"},\"ID\":\"bb59b1c5-8ac5-476e-9037-264d6b26b3f5\",\"Title\":\"For North Korea, next step is a nuclear test | Reuters\",\"Description\":\"A nuclear test would be the logical follow-up to Wednesday\\u0027s successful rocket launch, analysts said. The North\\u0027s 2009 test came on May 25, a month after a ...\",\"DisplayUrl\":\"www.reuters.com/article/2012/12/13/us-korea-north-rocket-idUSBRE8...\",\"Url\":\"http://www.reuters.com/article/2012/12/13/us-korea-north-rocket-idUSBRE8BB02K20121213\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=8&$top=1\",\"type\":\"WebResult\"},\"ID\":\"fba0d776-ef0e-4187-9b84-dbed3c9a8b0a\",\"Title\":\"Test your IPv6.\",\"Description\":\"This will test your browser and connection for IPv6 readiness, as well as show you your current IPV4 and IPv6 address.\",\"DisplayUrl\":\"test-ipv6.com\",\"Url\":\"http://test-ipv6.com/\"},{\"__metadata\":{\"uri\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=9&$top=1\",\"type\":\"WebResult\"},\"ID\":\"a52c1053-c10d-4ad4-b4e1-50871b9003c6\",\"Title\":\"For North Korea, next step is a nuclear test\",\"Description\":\"SEOUL (Reuters) - North Korea\\u0027s next step after rattling the world by putting a satellite into orbit for the first time will likely be a nuclear test, the third ...\",\"DisplayUrl\":\"news.yahoo.com/north-korea-next-step-nuclear-test-041048723.html\",\"Url\":\"http://news.yahoo.com/north-korea-next-step-nuclear-test-041048723.html\"}],\"__next\":\"https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult=\\u0027Moderate\\u0027&WebFileType=\\u0027HTML\\u0027&Market=\\u0027th-TH\\u0027&Query=\\u0027test\\u0027&$skip=10&$top=10\"}}";
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
                SearchRequest request = null;
//                SearchRequest request = new SearchRequest();
//                request.setQuery("test");
//                request.setCh("UTF-8");
//                request.setLang("th");
//                request.setNumber(10);
//                request.setPage(1);
//                request.setRegion("TH");
//                request.addSearchTypes(SearchType.DOCS);
//                F.Promise<SearchResponse> promise = provider.doSearch(SearchType.DOCS, request);
//                SearchResponse resp = promise.get();
//                Assert.assertNotNull(resp);
//                Assert.assertEquals(10, resp.getItems().size());
//                Assert.assertEquals(SearchType.DOCS, resp.getSearchType());
            }
        });
    }
}
