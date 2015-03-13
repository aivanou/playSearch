package services.search.after;

import model.ResponseItem;
import model.SearchResponse;
import model.SearchType;
import play.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class LinkyAfterSearchGroups implements AfterSearch {

    /**
     * remains items only with the unique domains
     * applicable only to SearchType.DOCS
     * changes items in @param searchResponse
     * if item does not have a domain field, gets domain from item.getURL()
     * <p/>
     * maybe it should be threadSafe?
     */
    @Override
    public SearchResponse process(SearchResponse searchResponse) {
        if (searchResponse == null) return null;
        if (searchResponse.getItems().isEmpty() || !SearchType.DOCS.equals(searchResponse.getSearchType())) {
            return searchResponse;
        }
        List<ResponseItem> rItems = new ArrayList<>(searchResponse.getItems().size());
        for (ResponseItem item : searchResponse.getItems()) {
            String domain;
            try {
                domain = item.getParams().containsKey("domain") ? item.getParams().get("domain") : getHost(item.getUrl());
            } catch (MalformedURLException e) {
                Logger.error("LinkyAfterSearchGroups: Cannot get domain from url:   " + item.getUrl());
                continue;
            }
            if (!containsDomain(rItems, domain)) {
                rItems.add(item);
            }
        }
        searchResponse.getItems().clear();
        searchResponse.getItems().addAll(rItems);
        return searchResponse;
    }


    private boolean containsDomain(List<ResponseItem> items, String domain) {
        for (ResponseItem item : items) {
            String rDomain;
            try {
                rDomain = item.getParams().containsKey("domain") ? item.getParams().get("domain") : getHost(item.getUrl());
            } catch (MalformedURLException ex) {
                Logger.error("Cannot get domain from url:   " + item.getUrl());
                continue;
            }
            if (rDomain.trim().equalsIgnoreCase(domain.trim()))
                return true;
        }
        return false;
    }

    private String getHost(String strUrl) throws MalformedURLException {
        URL url = new URL(strUrl);
        return url.getHost();
    }

}
