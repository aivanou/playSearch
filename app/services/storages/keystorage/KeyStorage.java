package services.storages.keystorage;

import model.SearchEngineType;


/**
 * used by external search engines to make search through API keys
 * Currently there is no possibility to use id
 */
@Deprecated
public class KeyStorage {

    public static String getKey(SearchEngineType provider) {
        if (provider.getName().equals("google")) {
            return "AIzaSyBVdMDN9aYUh0CK-qxrEco0SUl1PYMB01s 013262879079796003206:wsd3vnlscza";
        } else if (provider.getName().equals("bing")) {
            return "ebs3OSzZf3EmJEF3/sMvRbDRuJWCNep+5ZO8I3qnXmc=";
        }

        return null;
    }
}
