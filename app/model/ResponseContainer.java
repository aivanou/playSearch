package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ResponseContainer {

    private Map<String,Integer> metadata;
    private Collection<ResponseItem> items;

    public void buildMetaData(){
        metadata = new HashMap<>();

        for(ResponseItem item: items){
            Integer value = metadata.get(item.getType().reduction());
            if(value != null){
                value++;
            }else{
                value = 1;
            }
            metadata.put(item.getType().reduction(),value);
        }

    }

    public Map<String, Integer> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Integer> metadata) {
        this.metadata = metadata;
    }

    public Collection<ResponseItem> getItems() {
        return items;
    }

    public void setItems(Collection<ResponseItem> items) {
        this.items = items;
    }
}
