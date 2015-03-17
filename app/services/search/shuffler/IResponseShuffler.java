package services.search.shuffler;

import model.response.ContentResponse;
import model.response.ResponseItem;

import java.util.Collection;

/**
 * The interface represents algorithm for shuffling response results
 * Merges responses from different sources into one and changes
 * the priorities of documents
 */
public interface IResponseShuffler {

    Collection<ResponseItem> shuffle(Collection<ContentResponse> input);

}
