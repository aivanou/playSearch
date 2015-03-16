package model.response;

import model.Jsonable;

/**
 * The response from the @see services.search.provider.SearchProvider
 * It can be one of two types:
 *
 * @see model.response.SuccessContentResponse or @see model.response.FailedContentResponse
 * but both of them will have one parameter: the search content type
 */
public abstract class ContentResponse implements Jsonable {

    protected String searchType;

    public ContentResponse(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchType() {
        return searchType;
    }


}
