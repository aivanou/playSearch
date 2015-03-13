package services.search.shuffler;

import model.ResponseItem;
import model.SearchResponse;

import java.util.Collection;

/**
 * This interface represents algorithm for shuffling response results
 */
public interface IResponseShuffler {

    Collection<ResponseItem> shuffle(Collection<SearchResponse> input);

}
