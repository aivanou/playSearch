package datasources.dao.entities;


import model.EntityResponse;
import play.libs.F;

import java.io.InputStream;

/**
 * asynchronous DAO layer for entities described in model.SearchType
 */
public interface EntitiesDAO<T> {

    F.Promise<EntityResponse<String>> insert(String url, InputStream bodyStream);

    F.Promise<EntityResponse<String>> delete(String id);

    F.Promise<EntityResponse<T>> get(String id);

}
