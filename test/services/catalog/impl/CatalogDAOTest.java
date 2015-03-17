package services.catalog.impl;

import datasources.dao.entities.EntitiesDAO;
import datasources.dao.entities.impl.CatalogElasticSearchDAOImpl;
import model.input.Catalog;
import org.junit.Test;
import org.mockito.Mockito;
import services.entities.EntitiesService;
import services.entities.Impl.CatalogServiceImpl;

import java.io.InputStream;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

public class CatalogDAOTest {

    @Test
    public void simpleInsert() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                EntitiesDAO<Catalog> catalogDAO = Mockito.mock(CatalogElasticSearchDAOImpl.class);
                EntitiesService<Catalog> catalogService = new CatalogServiceImpl(catalogDAO);
                Catalog c = new Catalog("testid", "very very very long description", "some english title");
                catalogService.insert(c, "testid");
                Mockito.verify(catalogDAO, Mockito.only()).insert(Mockito.anyString(), Mockito.any(InputStream.class));
            }
        });
    }

    @Test
    public void simpleGet() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                EntitiesDAO<Catalog> catalogDAO = Mockito.mock(CatalogElasticSearchDAOImpl.class);
                EntitiesService<Catalog> catalogService = new CatalogServiceImpl(catalogDAO);
                String url = "testid";
                catalogService.get(url);
                Mockito.verify(catalogDAO, Mockito.only()).get(Mockito.anyString());
            }
        });
    }

    @Test
    public void simpleDelete() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                EntitiesDAO<Catalog> catalogDAO = Mockito.mock(CatalogElasticSearchDAOImpl.class);
                EntitiesService<Catalog> catalogService = new CatalogServiceImpl(catalogDAO);
                String url = "testid";
                catalogService.delete(url);
                Mockito.verify(catalogDAO, Mockito.only()).delete(Mockito.anyString());
            }
        });
    }
}
