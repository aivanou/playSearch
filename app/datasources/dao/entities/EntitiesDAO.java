package datasources.dao.entities;


import play.libs.F;
import services.entities.Impl.EntityResponse;

import java.io.InputStream;

/**
 * asynchronous DAO layer for entities described in model.SearchType
 */
public interface EntitiesDAO<T> {

    F.Promise<EntityResponse<String>> insert(String url, InputStream bodyStream);

    F.Promise<EntityResponse<String>> delete(String id);

    F.Promise<EntityResponse<T>> get(String id);

}
