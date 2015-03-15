package services.search.provider.impl.apiutil;

import model.request.SearchRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * GoogleURLBuilderTest ...
 *
 * @author vadim
 * @date 12/14/12
 */
public class GoogleURLBuilderTest {

    private URLBuilder builder = GoogleURLBuilder.defaultInstance();
    private SearchRequest request = null;

    @Before
    public void setUp() throws Exception {
//        request.setQuery("test");
//        request.setLang("th");
//        request.setNumber(10);
//        request.setPage(0);
//        request.setRegion("TH");
//        request.addSearchTypes(SearchType.DOCS);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBuild() {
        String expected = "https://www.googleapis.com/customsearch/v1?alt=json&prettyPrint=false&fields=items(title,link,snippet)&start=1&num=10&cr=th&gl=TH&q=test";
        String actual = builder.build(request);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSkipTop() {
        String expected = "https://www.googleapis.com/customsearch/v1?alt=json&prettyPrint=false&fields=items(title,link,snippet)&start=7&num=3&cr=th&gl=TH&q=test";
        String actual = builder.build(request);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLang() {
        builder.build(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLang() {
        builder.build(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRegion() {
        builder.build(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyRegion() {
        builder.build(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroNumber() {
        builder.build(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadNumber() {
        builder.build(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadPage() {
        builder.build(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullQuery() {
        builder.build(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQuery() {
        builder.build(request);
    }
}
