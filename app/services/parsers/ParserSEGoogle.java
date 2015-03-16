package services.parsers;

import model.response.ResponseItem;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;

/**
 * ParserSEGoogle ...
 *
 * @author Alexander Latysh
 * @version 1.0.0
 * @date Nov 14, 2012
 * @email <alatysh@linky.asia>
 */
public class ParserSEGoogle extends ParserSE {

    private static final String PARAM_BEFORE_URL = "url?q=";
    private static final String PARAM_AFTER_URL = "&sa=";
    private static final Logger logger = Logger.getLogger(ParserSEGoogle.class);
    private static final Logger queryLogger = Logger.getLogger("googleQueryLogger");

    @Override
    protected ResponseItem parseElement(Element element) throws MalformedURLException {
        Element titleDiv = element.getElementsByClass("r").first().getElementsByTag("a").first();
        String dirtyUrl = titleDiv.attr("href");
        String title = titleDiv.text();
        String snippet = element.getElementsByClass("st").first().text();
        String url = parseDirtyUrl(dirtyUrl);
        if (url == null) {
            throw new MalformedURLException(String.format("Bad dirty url %s", dirtyUrl));
        }
        ResponseItem item = new ResponseItem(url, title, snippet, "document", 1.0, System.nanoTime());
        return item;
    }

    private String parseDirtyUrl(String dirtyUrl) {
        if (dirtyUrl == null) {
            return null;
        }
        int startCut = dirtyUrl.indexOf(PARAM_BEFORE_URL) + PARAM_BEFORE_URL.length();
        if (startCut <= 6) {
            return dirtyUrl;
        }
        int endCut = dirtyUrl.indexOf(PARAM_AFTER_URL);
        if ((startCut > 0) && (endCut > startCut)) {
            return dirtyUrl.substring(startCut, endCut);
        }
        return null;
    }

    @Override
    protected Element getSERP(Document doc) {
        return doc.getElementById("ires");
    }

    @Override
    protected boolean isEmptySERP(Document doc) {
        return doc.getElementById("topstuff") != null;
    }

    @Override
    protected String getSearchEngineName() {
//        return SearchEngineType.GOOGLE.name();
        return "";
    }

    @Override
    protected Elements getListFromSERP(Element SERP) {
        return SERP.getElementsByClass("g");
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected Element getStatistic(Document doc) {
        return doc.getElementById("resultStats");
    }

    @Override
    protected Logger getQueryLogger() {
        return queryLogger;
    }
}
