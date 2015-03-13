package services.search.after;


import model.SearchResponse;

/**
 * executes process method after each search;
 * now implemented only in ElasticSearchProvider
 */
public interface AfterSearch {

    SearchResponse process(SearchResponse response);

}
