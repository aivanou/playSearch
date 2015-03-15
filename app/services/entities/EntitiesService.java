package services.entities;


import play.libs.F;
import services.entities.Impl.EntityResponse;

/**
 * operations on entities(entities,video,news, etc..)
 * implies working with database object
 */
public interface EntitiesService<T> {

    F.Promise<EntityResponse<String>> insert(T entity, String id);

    F.Promise<EntityResponse<String>> delete(String url);

    F.Promise<EntityResponse<T>> get(String url);

}
