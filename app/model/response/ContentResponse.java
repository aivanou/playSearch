package model.response;

import model.Jsonable;
import model.SearchType;

/**
 * The Resposne from search
 */
public abstract class ContentResponse implements Jsonable {

    protected SearchType searchType;

    public ContentResponse(SearchType searchType) {
        this.searchType = searchType;
    }

    public SearchType getSearchType() {
        return searchType;
    }


}
