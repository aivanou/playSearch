package datasources.dao.entities.impl;

import model.input.Catalog;
import org.codehaus.jackson.JsonNode;

/**
 * implementation of ElasticSearchDAO that works with Catalog
 */
public class CatalogElasticSearchDAOImpl extends ElasticEntityOperationsDAO<Catalog> {

    @Override
    protected Catalog convertFromJson(JsonNode json) {
        String title = getElement(json, "title");
        String description = getElement(json, "description");
        String url = getElement(json, "url");
        return new Catalog(url, description, title);
    }

    private String getElement(JsonNode node, String el) {
        return node.get(el) == null ? "" : node.get(el).asText();
    }
}
