package services.search;

import model.SearchEngineType;
import model.request.ContentRequest;
import model.request.SearchRequest;
import model.response.ContentResponse;
import model.response.SearchResponse;
import model.response.SearchResponseMetadata;
import play.Logger;
import play.libs.F;
import services.search.provider.ProviderFactory;
import services.search.provider.SearchProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * The search service, invokes low search API for evey @see ContentRequest
 * Currently, the engines are specified for the whole search, as a result
 * they are invoked for every type of content in the request
 * <p/>
 * Returns the promise of @see SearchResponse
 */
public class SearchServiceImpl implements SearchService<SearchRequest, SearchResponse> {

    public SearchServiceImpl() {
    }

    @Override
    public F.Promise<SearchResponse> search(SearchRequest request) {

        List<F.Promise<? extends ContentResponse>> contentPromises = new ArrayList<>();
        for (final SearchEngineType engineType : request.getSearchEngines()) {

            for (final ContentRequest crequest : request.getContentRequests()) {
                Logger.debug(String.format("SearchService: Searching %s on %s content type: %s", request.getQuery(), engineType.getId(), crequest.getSearchType().getName()));
                final SearchProvider provider = ProviderFactory.getProvider(engineType);
                F.Promise<ContentResponse> contentPromise = provider.doSearch(crequest);
                contentPromises.add(contentPromise);
            }
        }

        F.Promise<List<ContentResponse>> seq = F.Promise.sequence(contentPromises);

        return seq.map(new F.Function<List<ContentResponse>, SearchResponse>() {
            @Override
            public SearchResponse apply(List<ContentResponse> contentResponses) throws Throwable {
                SearchResponseMetadata metadata = new SearchResponseMetadata(1, 1, 1);
                Logger.debug(String.format("SearchService: combining %s responses", contentResponses.size()));
                SearchResponse response = new SearchResponse(contentResponses, metadata);
                return response;
            }
        });
    }
}