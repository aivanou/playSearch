package services.parsers;

import model.ResponseItem;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 *
 * @author Alexander Latysh
 * @version 1.0.0
 * @date Nov 14, 2012
 * @email <alatysh@linky.asia>
 */
public abstract class ParserSE {
    /**
     * Parsing result of search engine's response
     *
     * @param html to parse
     * @return null, if some error occurs while parsing; empty collection if no result was found; results otherwise
     */
    public Collection<ResponseItem> parse(String html) {
        return parse(html, null);
    }

    /**
     * Parses the result page and checks the number of items to greater or equals 10.
     * Otherwise, writes a message to log with the Search Engine name and query, for which was obtained an incomplete search results,
     * and returns empty list.
     *
     * @param html to parse
     * @param query that obtained the search engine result page
     * @return @return null, if some error occurs while parsing; empty collection if no
     * result was found or number of results less than 10; results otherwise
     */
    public Collection<ResponseItem> parse(String html, String query) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.format("Parsing [%s]", html));
        }
        if (html == null) {
            return null;
        }
        Document doc = Jsoup.parse(html);
        //Here I try to find block-element with statistics about search results
        Element statistic = getStatistic(doc);
        //If such element is not present, this means that the search engine does not returns results.
        //Here I handle both possible cases at which the results have not come.
        if (statistic == null) {
            if (isEmptySERP(doc)) { //This is a case at which Search Engine just didn't find results for a query. I return an empty list.
                if (query != null && !query.isEmpty()) {
                    getLogger().warn(String.format("%s didn't return results for query {%s}", getSearchEngineName(), query));
                    logQuery(query);
                }
                return Collections.<ResponseItem> emptyList();
            } else { //This is a case at which Search Engine returned any other page instead of SERP.
                getLogger().warn(String.format("%s didn't return any results", getSearchEngineName()));
                logQuery(query);
                return null;
            }
        }
        //Here I check whether the number of results exceed or equals 10. Otherwise, I return an empty list.
        if (!isEnoughResults(statistic)) {
            getLogger().warn(String.format("%s returned not enough results for query {%s}", getSearchEngineName(), query));
            return Collections.<ResponseItem> emptyList();
        }
        //Here I find element that contains entry SERP (the listing of results returned by a search engine in response to a query)
        Element SERP = getSERP(doc);
        //Search results in the form of a collection of distinct items
        Elements results = getListFromSERP(SERP);
        return getResults(results);
    }

    /**
     * Parses a single entry from the list of search results.
     *
     * @param element that contains a single entry (block with url, title, snippet) from the search results.
     * @return an object of class {@link ResponseItem} obtained by parsing the element.
     */
    protected abstract ResponseItem parseElement(Element element) throws MalformedURLException;


    /**
     * Takes an element consisting of the whole SERP, parses it, and returns a list of distinct items {@link ResponseItem}
     *
     * @param results element consisting of the whole SERP
     * @return collection of distinct items {@link ResponseItem}
     */
    protected Collection<ResponseItem> getResults(Elements results) {
        Collection<ResponseItem> items = new ArrayList<ResponseItem>();
        if (results != null && !results.isEmpty()) {
            for (Element element : results) {
                try {
                    ResponseItem item = parseElement(element);
                    items.add(item);
                } catch (Throwable ex) {
                    //html may vary, so let's skip errors
                    continue;
                }
            }
        }
        if (items.isEmpty() && results != null && !results.isEmpty()) {
            //html much differs from what we've expected
            return null;
        }
        return items;
    }

    /**
     * Returns the markup element that contains the entire SERP.
     *
     * @param document that contain entire parsed page.
     * @return element that contains the entire SERP
     */
    protected abstract Element getSERP(Document document);

    /**
     * Returns SERP as a list of distinct items
     *
     * @param SERP markup element that contains entire search results page
     * @return
     */
    protected abstract Elements getListFromSERP(Element SERP);

    /**
     * Returns true in the case when the SERP is empty because of the search engine did not find anything
     *
     * @param document that contain entire parsed page
     * @return true if search engine just didn't find anything
     */
    protected abstract boolean isEmptySERP(Document document);

    /**
     * Returns name of search engine
     *
     * @return
     */
    protected abstract String getSearchEngineName();

    protected abstract Logger getLogger();

    /**
    * Check whether number of results from Search Engine exceed or equals 10.
    *
    * @param element with statistic about search results
    * @return true if the the number of results exceeds or equals 10 and false otherwise..
    */
    protected boolean isEnoughResults(Element element) {
        int count = 0;
        //Here I check whether block-element is not empty
        if (element.hasText()) {
            String statistic = element.text();
            //Number of results may be at the beginning or middle of a sentence.
            //So I therefore split sentence into words and looking for a number.
            String[] words = statistic.split(" ");
            for (String word : words) {
                if (Character.isDigit(word.charAt(0))) {
                    //Parse integer from string with removed commas (usually search engines return number of results in the form of 8,670,000,000)
                    count = Integer.parseInt(word.replace(",", ""));
                    break;
                }
            }
        }
        return count >= 10;
    }

    /**
     * Returns element that contains statistic about number of results returned by Search Engine
     *
     * @param document root element that contains entry page
     * @return
     */
    protected abstract Element getStatistic(Document document);

    /**
     * Returns the Logger for writing queries, for which search engine returned not enough results
     * Returns null if such an option is not implemented by certain search engine
     */
    protected abstract Logger getQueryLogger();

    /**
     * Writes to log a query, for which search engine returned not enough results
     */
    protected void logQuery(String query) {
        if (getQueryLogger() != null) {
            getQueryLogger().info(query);
        }
    }
}
