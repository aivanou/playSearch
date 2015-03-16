package services.entities.Impl;

import datasources.dao.entities.EntitiesDAO;
import datasources.dao.entities.impl.CatalogElasticSearchDAOImpl;
import model.Schema;
import model.input.Catalog;
import play.Logger;
import play.Play;
import play.libs.Json;

public class CatalogServiceImpl extends EntitiesServiceImpl<Catalog> {

    private final String url = Play.application().configuration().getString("elastic.catalog") + "/" + Schema.ELASTIC_SCHEMA + "/catalog";

    public CatalogServiceImpl() {
        super(new CatalogElasticSearchDAOImpl());
    }

    public CatalogServiceImpl(EntitiesDAO<Catalog> catalogDAO) {
        super(catalogDAO);
    }

    @Override
    protected String getURL(String id) {
        Logger.info("using url: " + url + "/" + id);
        return url + "/" + id;
    }

    @Override
    protected String generateGetQuery(Catalog entity) {
        return Json.toJson(entity).toString();
    }

}
