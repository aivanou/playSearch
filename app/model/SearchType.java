package model;

public enum SearchType {

    VIDEO("video"),
    NEWS("news"),
    DOCS("document"),
    PICTURE("picture"),
    CATALOG("category"),
    ADDRESS("address"),
    UNDEFINED("undefined");

    String name;

    private SearchType(String name) {
        this.name = name;
    }

    public static SearchType getByName(String name) {
        for (SearchType type : SearchType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return SearchType.UNDEFINED;
    }

    public String getName() {
        return name;
    }
}

