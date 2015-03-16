package datasources.dao.entities.impl;

import datasources.dao.entities.EntitiesDAO;
import datasources.dao.entities.OperationException;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.F;
import play.libs.WS;
import services.entities.Impl.EntityResponse;
import services.search.provider.SearchProvider;

import java.io.InputStream;

/**
 * Represents methods that interact with elastic search web service
 */
public abstract class ElasticEntityOperationsDAO<T> implements EntitiesDAO<T> {


    @Override
    public F.Promise<EntityResponse<String>> insert(String url, InputStream bodyStream) {

        return WS.url(url).setTimeout(SearchProvider.TIMEOUT).post(bodyStream).map(
                new F.Function<WS.Response, EntityResponse<String>>() {
                    @Override
                    public EntityResponse<String> apply(WS.Response response) throws Throwable {
                        return new EntityResponse("EntityResponse: Insert operation finished with OK status");
                    }
                }
        ).recover(
                new F.Function<Throwable, EntityResponse<String>>() {
                    @Override
                    public EntityResponse<String> apply(Throwable throwable) throws Throwable {
                        Logger.error(throwable.getMessage());
                        return new EntityResponse(throwable);
                    }
                }
        );
    }

    @Override
    public F.Promise<EntityResponse<T>> get(final String url) {
        return WS.url(url).setTimeout(SearchProvider.TIMEOUT).get().map(
                new F.Function<WS.Response, EntityResponse<T>>() {
                    @Override
                    public EntityResponse<T> apply(WS.Response response) throws Throwable {
                        if (response.asJson().get("_source") == null) {
                            throw new OperationException("Get Operation: There is no entity with url:  " + url);
                        }
                        if (Logger.isDebugEnabled()) {
                            Logger.debug("Json:: " + response.asJson().get("_source"));
                        }
                        JsonNode src = response.asJson().get("_source");
                        T ct = convertFromJson(src);
                        if (ct == null) {
                            throw new OperationException("cannot parse response from elastic;  response: " + src.toString());
                        }
                        if (Logger.isDebugEnabled()) {
                            Logger.debug("entities: " + ct.toString());
                        }
                        return new EntityResponse(ct);
                    }
                }
        ).recover(
                new F.Function<Throwable, EntityResponse<T>>() {
                    @Override
                    public EntityResponse<T> apply(Throwable throwable) throws Throwable {
                        Logger.error(throwable.getMessage());
                        return new EntityResponse(throwable);
                    }
                }
        );
    }

    @Override
    public F.Promise<EntityResponse<String>> delete(final String url) {

        return WS.url(url).setTimeout(SearchProvider.TIMEOUT).delete().map(
                new F.Function<WS.Response, EntityResponse<String>>() {
                    @Override
                    public EntityResponse<String> apply(WS.Response response) throws Throwable {
                        return new EntityResponse("Delete Operation: Ok");
                    }
                }
        ).recover(
                new F.Function<Throwable, EntityResponse<String>>() {
                    @Override
                    public EntityResponse<String> apply(Throwable throwable) throws Throwable {
                        Logger.error(throwable.getMessage());
                        return new EntityResponse(throwable.getMessage());
                    }
                }
        );
    }


    protected abstract T convertFromJson(JsonNode json);

}
