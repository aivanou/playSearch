package services.entities.Impl;

import datasources.dao.entities.EntitiesDAO;
import play.Logger;
import play.libs.F;
import services.entities.EntitiesService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public abstract class EntitiesServiceImpl<T> implements EntitiesService<T> {

    private EntitiesDAO<T> catalogDAO;

    public EntitiesServiceImpl(EntitiesDAO<T> catalogDAO) {
        this.catalogDAO = catalogDAO;
    }

    /**
     * inserts single entity into elastic search
     *
     * @param id is used to generate necessary id
     *           <p/>
     *           (maybe I should make an interface with method that will return id and Catalog class will implement it?)
     * @return null if there was an error during converting body to  byte stream
     * F.Promise<String> in another case
     */
    @Override
    public F.Promise<EntityResponse<String>> insert(T entity, String id) {
        String body = generateGetQuery(entity);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Insert Operation:   id: " + id + "  using body:  " + body);
        }
        InputStream bodyStream;
        try {
            bodyStream = new ByteArrayInputStream(body.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger.error("ElasticSearchProvider: query [" + body + "] can't be uncoded with UTF-8", e);
            return null;
        }
        return catalogDAO.insert(getUrl(id), bodyStream);
    }

    @Override
    public F.Promise<EntityResponse<String>> delete(String id) {
        if (Logger.isDebugEnabled()) {
            Logger.debug("Delete Operation:   id: " + id);
        }
        return catalogDAO.delete(getUrl(id));
    }

    @Override
    public F.Promise<EntityResponse<T>> get(String id) {
        return catalogDAO.get(getUrl(id));
    }

    protected abstract String generateGetQuery(T entity);

    protected abstract String getUrl(String id);

}
