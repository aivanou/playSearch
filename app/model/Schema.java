package model;

//Solr/ElasticSearch fields should be match with these constants
public interface Schema {

    String ELASTIC_SCHEMA = "linky";

    //general fields
    String TITLE = "title";
    String URL = "url";
    String CONTENT = "content";
    String INDEXED_TIME = "indexed";


}
