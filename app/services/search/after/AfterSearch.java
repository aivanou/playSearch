package services.search.after;


import model.response.ContentResponse;

/**
 * executes process method after each search;
 * now implemented only in ElasticSearchProvider
 */
public interface AfterSearch {

    ContentResponse process(ContentResponse response);

}
