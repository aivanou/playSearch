package controllers;

import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class VersionController extends Controller {
    private static String version;
    static {
        try {
            version = ConfigFactory.load("version.properties").getString("version");
        } catch (ConfigException.Missing e) {
            Logger.warn("version.properties is missing");
        }
    }

    public static Result getVersion() {
        return Results.ok(version != null ? version : "Can't detect version because version.properties wasn't found.");
    }
}
