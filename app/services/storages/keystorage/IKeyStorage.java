package services.storages.keystorage;

import model.SearchEngineType;


/**
 * @see services.storages.keystorage.KeyStorage
 */
@Deprecated
public interface IKeyStorage {

    String getKey(SearchEngineType provider);
}
