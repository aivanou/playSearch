package services.search.provider;

import model.SearchRequest;
import model.SearchResponse;
import model.SearchType;
import play.Play;
import play.libs.F;

public interface SearchProvider {
    int TIMEOUT = Play.application().configuration().getInt("service.timeout");

    F.Promise<SearchResponse> doSearch(SearchType type, SearchRequest req);

}
