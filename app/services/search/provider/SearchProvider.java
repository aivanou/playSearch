package services.search.provider;

import model.request.ContentRequest;
import model.response.ContentResponse;
import play.libs.F;

public interface SearchProvider {
    //    int TIMEOUT = Play.application().configuration().getInt("service.timeout");
    int TIMEOUT = 2000;

    F.Promise<ContentResponse> doSearch(ContentRequest req) throws IllegalArgumentException, ClassCastException;

}
