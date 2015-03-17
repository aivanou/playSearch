package controllers;

import akka.dispatch.Futures;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import model.input.InputDTO;
import model.request.SearchRequest;
import model.response.SearchResponse;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import services.cache.Cache;
import services.cache.impl.CacheFactory;
import services.search.SearchService;
import services.search.SearchServiceFactory;

import javax.validation.ValidationException;

/**
 * Represents the search entry point
 * Request is the HTTP Post with json body
 * Translates the json into @see model.input.InputDTO and builds a @see model.request.SearchRequest from it
 *
 * @see model.request.SearchRequest is used by @see services.search.SearchService
 */
public class SearchController extends Controller {

    private final static SearchService service = SearchServiceFactory.getInstance();
    private final static Cache cache = CacheFactory.getInstance().getCache();

    @BodyParser.Of(BodyParser.Json.class)
    public static Result search() {
        InputDTO inputDTO;
        JsonNode jsonBody = request().body().asJson();
        try {
            inputDTO = Json.fromJson(jsonBody, InputDTO.class);
        } catch (Exception ex) {
            Logger.error("Wrong Query string " + jsonBody.toString());
            ex.fillInStackTrace();
            return badRequest("Wrong query request: " + jsonBody.toString());
        }
        Logger.debug("Application: got request : " + request().body().asJson());
        final SearchRequest request;
        try {
            request = SearchRequest.fromDTO(inputDTO);
        } catch (ValidationException ex) {
            return badRequest(ex.getMessage());
        }
        Promise<Result> promise = processRequest(request, cache.get(jsonBody.toString()));

        return async(Akka.asPromise(promise.future()));
    }

    private static Promise<Result> processRequest(final SearchRequest request, Future<String> cacheResponse) {
        final Promise<Result> promise = Futures.promise();

        cacheResponse.onSuccess(new OnSuccess<String>() {
            @Override
            public void onSuccess(String result) throws Throwable {
                Logger.debug("Invoking cache success with : " + result);
                if (result != null) { //if cache doesn't have an object it will return null
                    promise.success(ok(result));
                } else {
                    executeRequest(request, promise);
                }
            }

        }, Akka.system().dispatcher().prepare());

        cacheResponse.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable failure) throws Throwable {
                Logger.debug("ERROR: " + failure.getMessage());
                executeRequest(request, promise);
            }

        }, Akka.system().dispatcher().prepare());
        return promise;
    }

    private static void executeRequest(final SearchRequest request, final Promise<Result> resultPromise) {
        Logger.debug("Executing search request with : " + request.getQuery());
        F.Promise<SearchResponse> searchResponsePromise = service.search(request);
        searchResponsePromise.recover(new F.Function<Throwable, SearchResponse>() {
            @Override
            public SearchResponse apply(Throwable throwable) throws Throwable {
                //TODO log search exception
                return null;
            }
        });
        searchResponsePromise.onRedeem(new F.Callback<SearchResponse>() {
            @Override
            public void invoke(SearchResponse searchResponse) throws Throwable {
                if (searchResponse == null) {
                    resultPromise.success(badRequest("Internal server error, please contat administrator if possible"));
                } else {
                    String json = searchResponse.toJson();
                    //TODO move to the play configuration file
                    //30 -- cached object TTL in seconds
                    cache.set(request.getQuery(), json, 30).onFailure(new OnFailure() {
                        @Override
                        public void onFailure(Throwable failure) throws Throwable {
                            Logger.debug("Error while writing data in cache: " + failure.getMessage());
                        }
                    }, Akka.system().dispatcher().prepare());
                    if (Logger.isDebugEnabled())
                        Logger.debug("Application: Setting response as: " + json);
                    resultPromise.success(ok(json));
                }
            }
        });
    }
}