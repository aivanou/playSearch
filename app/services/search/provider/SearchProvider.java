package services.search.provider;

import model.request.ContentRequest;
import model.response.ContentResponse;
import play.Play;
import play.libs.F;

public interface SearchProvider {
    int TIMEOUT = Play.application().configuration().getInt("service.timeout");

    F.Promise<ContentResponse> doSearch(ContentRequest req) throws IllegalArgumentException, ClassCastException;

}
