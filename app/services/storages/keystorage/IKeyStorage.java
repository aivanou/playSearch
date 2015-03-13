package services.storages.keystorage;

import model.SearchEngineType;

/**
 * Created with IntelliJ IDEA.
 * User: aliaksandrhlinski
 * Date: 11/4/12
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IKeyStorage {

    String getKey(SearchEngineType provider);
}
