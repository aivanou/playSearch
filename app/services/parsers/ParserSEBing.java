/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.parsers;

import model.ResponseItem;
import model.SearchEngineType;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;

/**
 *
 * @author alex
 */
public class ParserSEBing extends ParserSE {

    private static final Logger logger = Logger.getLogger(ParserSEBing.class);

    @Override
    protected ResponseItem parseElement(Element el) throws MalformedURLException {
        Element mc_el = el.getElementsByClass("sa_mc").first();
        if (mc_el != null) {
            String title = mc_el.getElementsByClass("sb_tlst").get(0).getElementsByTag("h3").get(0).getElementsByAttribute("href").get(0).text();
            String snippet = mc_el.getElementsByTag("p").text();
            String url = mc_el.getElementsByClass("sb_meta").get(0).getElementsByTag("cite").text();
            ResponseItem item = new ResponseItem();
            item.setUrl(url);
            item.setTitle(title);
            item.setSnippet(snippet);
            return item;
        }
        return null;
    }

    @Override
    protected Element getSERP(Document document) {
        Element serp = document.getElementById("results");
        if (serp == null) {
            return null;
        } else {
            return serp.getElementById("no_results") == null ? serp : null;
        }
    }

    @Override
    protected Elements getListFromSERP(Element SERP) {
        return SERP.getElementsByClass("sa_wr");
    }

    @Override
    protected boolean isEmptySERP(Document document) {
        return document.getElementById("no_results") != null;
    }

    @Override
    protected String getSearchEngineName() {
        return SearchEngineType.BING.name();
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected Element getStatistic(Document document) {
        return document.getElementById("count");
    }

    @Override
    protected Logger getQueryLogger() {
        return null;
    }
}
