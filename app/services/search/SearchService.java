package services.search;

import model.Jsonable;
import play.libs.F;

/**
 * Represents the search service
 * Response should support
 *
 * @see model.Jsonable
 * Returns the play's promise of Response
 */
public interface SearchService<REQ, RESP extends Jsonable> {

    public F.Promise<RESP> search(REQ request);

}
