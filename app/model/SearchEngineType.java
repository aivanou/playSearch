package model;

public enum SearchEngineType {
    GOOGLE("google"),
    BING("bing"),
    LINKY("linky"),
    Unrecognised("unrecognised");

    private final String id;

    SearchEngineType(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public static SearchEngineType getById(String id) {
        for (SearchEngineType eng : SearchEngineType.values()) {
            if (id.equals(eng.getId()))
                return eng;
        }
        return SearchEngineType.Unrecognised;
    }

}
