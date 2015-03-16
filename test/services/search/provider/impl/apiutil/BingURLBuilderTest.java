package services.search.provider.impl.apiutil;

import model.request.ExternalContentRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class BingURLBuilderTest {

    private URLBuilder builder = BingURLBuilder.defaultInstance();
    private ExternalContentRequest request = null;

    @Before
    public void setUp() throws Exception {
        request = new ExternalContentRequest("test", 10, 0, "UTF-8", "th", "TH");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBuild() {
        String expected = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult='Moderate'&$format=JSON&WebFileType='HTML'&Market='th-TH'&$top=10&$skip=0&Query='test'";
        String actual = builder.build(request);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSkipTop() {
        String expected = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Adult='Moderate'&$format=JSON&WebFileType='HTML'&Market='th-TH'&$top=3&$skip=6&Query='test'";
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
