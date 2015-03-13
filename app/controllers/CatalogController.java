package controllers;

import services.entities.EntitiesService;

import model.input.Catalog;
import model.EntityResponse;

import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.entities.Impl.CatalogServiceImpl;


public class CatalogController extends Controller {

    private static final EntitiesService<Catalog> catalogService;

    static {
        String searchEngine = Play.application().configuration().getString("catalog.searchEngine", "default");
        if ("elastic".equals(searchEngine.trim().toLowerCase())) {
            catalogService = new CatalogServiceImpl();
        } else {
            String message = "Initialization exception: unsupported entities.searchEngine type : " + searchEngine +
                    "Supported types:  [ elastic ]";
            Logger.error(message);
            catalogService = null;
        }
    }

    public static Result getCatalogue(final String url) {
        if (catalogService == null) {
            return internalServerError("initialization exception. please contact with administrator");
        }
        F.Promise<EntityResponse<Catalog>> pr = catalogService.get(url);
        return async(
                pr.map(
                        new F.Function<EntityResponse<Catalog>, Result>() {
                            @Override
                            public Result apply(EntityResponse<Catalog> rsp) throws Throwable {
                                if (rsp.getError() == null) {
                                    return ok(Json.toJson(rsp.getMessage()));
                                }
                                return internalServerError(rsp.getError().getMessage());
                            }
                        }
                )
        );
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result insertCatalogue() {
        if (catalogService == null) {
            return internalServerError("initialization exception. please contact with administrator");
        }
        if (!request().getHeader("Content-Type").contains("application/json")) {
            return badRequest("Request error, you did not specify: Content-Type: application/json; your header: " + request().getHeader("Content-Type"));
        }
        Catalog catalog;
        try {
            catalog = Json.fromJson(request().body().asJson(), Catalog.class);
        } catch (RuntimeException ex) {
            return badRequest("Request error: request body: " + request().body().asJson().toString());
        }
        F.Promise<EntityResponse<String>> pr = catalogService.insert(catalog, catalog.getUrl());
        return async(
                pr.map(
                        new F.Function<EntityResponse<String>, Result>() {
                            @Override
                            public Result apply(EntityResponse<String> rsp) throws Throwable {
                                if (rsp.getError() == null) {
                                    return ok(rsp.getMessage());
                                }
                                return internalServerError(rsp.getError().getMessage());
                            }
                        }
                )
        );
    }

    public static Result deleteCatalogue(String url) {
        if (catalogService == null) {
            return internalServerError("initialization exception. please contact with administrator");
        }
        F.Promise<EntityResponse<String>> pr = catalogService.delete(url);
        return async(
                pr.map(
                        new F.Function<EntityResponse<String>, Result>() {
                            @Override
                            public Result apply(EntityResponse<String> rsp) throws Throwable {
                                if (rsp.getError() == null) {
                                    return ok(rsp.getMessage());
                                }
                                return internalServerError(rsp.getError().getMessage());
                            }
                        }
                )
        );
    }
}
