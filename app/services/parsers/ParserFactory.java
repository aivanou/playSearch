package services.parsers;

import model.SearchEngineType;

public class ParserFactory {

    private ParserFactory() {
    }

    public static ParserSE newInstance(SearchEngineType engine) throws Exception {
        if (engine.equals("google"))
            return new ParserSEGoogle();
        return new ParserSEBing();
    }
}
