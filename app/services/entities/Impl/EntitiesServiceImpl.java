package services.entities.Impl;

import datasources.dao.entities.EntitiesDAO;
import model.EntityResponse;
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
     * @param id is used to generate necessary url
     *           <p/>
     *           (maybe I should make an interface with method that will return id and Catalog class will implement it?)
     * @return null if there was an error during converting body to  byte stream
     *         F.Promise<String> in another case
     */
    @Override
    public F.Promise<EntityResponse<String>> insert(T entity, String id) {
        String url = getURL(id);
        String body = generateGetQuery(entity);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Insert Operation:   URL : " + id + "  using body:  " + body);
        }
        InputStream bodyStream;
        try {
            bodyStream = new ByteArrayInputStream(body.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Logger.error("ElasticSearchProvider: query [" + body + "] can't be uncoded with UTF-8", e);
            return null;
        }
        return catalogDAO.insert(url, bodyStream);
    }

    @Override
    public F.Promise<EntityResponse<String>> delete(String id) {
        String url = getURL(id);
        if (Logger.isDebugEnabled()) {
            Logger.debug("Delete Operation:   URL: " + id);
        }
        return catalogDAO.delete(url);
    }

    @Override
    public F.Promise<EntityResponse<T>> get(String id) {
        final String url = getURL(id);
        return catalogDAO.get(url);
    }

    protected abstract String getURL(String id);

    protected abstract String generateGetQuery(T entity);

}
