package services.search.shuffler;

import model.response.ContentResponse;
import model.response.ResponseItem;

import java.util.Collection;

//TODO implement the response shuffler

/**
 * Merges responses from different sources into one and changes
 * the priorities of documents
 */
public class DefaultResponseShuffler implements IResponseShuffler {
    /**
     * @param input -- the collection of responses
     *              returns the collection of merged @see model.response.ResponseItem
     */

    @Override
    public Collection<ResponseItem> shuffle(Collection<ContentResponse> input) {
        return null;
    }
}
