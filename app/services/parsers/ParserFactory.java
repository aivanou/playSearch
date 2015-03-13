package services.parsers;

import model.SearchEngineType;

public class ParserFactory {

    private ParserFactory() {
    }

    public static ParserSE newInstance(SearchEngineType engine) throws Exception {
        if (engine == SearchEngineType.BING) {
            return new ParserSEBing();
        } else if (engine == SearchEngineType.GOOGLE) {
            return new ParserSEGoogle();
        }
        throw new Exception("Parser not found for " + engine);
    }
}
