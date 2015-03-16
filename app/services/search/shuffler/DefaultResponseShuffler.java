package services.search.shuffler;

import model.SearchType;
import model.response.ResponseItem;
import model.response.SearchResponse;

import java.util.*;

public class DefaultResponseShuffler implements IResponseShuffler {
    @Override
    public Collection<ResponseItem> shuffle(Collection<SearchResponse> input) {
//        Map<SearchType, Iterator<ResponseItem>> itMap = new EnumMap<SearchType, Iterator<ResponseItem>>(SearchType.class);

//        for (SearchResponse item : input) {
//            if (item.getItems().iterator() != null && item.getItems().iterator().hasNext()) {
//                SearchType type = item.getSearchType();
//                if (itMap.containsKey(type)) {
//                    Iterator<ResponseItem> tempIterator = itMap.get(type);
//                    itMap.put(type, addResults(tempIterator, item.getItems()));
//                } else {
//                    itMap.put(item.getSearchType(), item.getItems().iterator());
//                }
//            }
//        }

//        Collection<ResponseItem> shuffledItems = new ArrayList<ResponseItem>();
//        int curr_priority = 1;
//        int faults = 0;
//        while (true) {
//            SearchType curr_type = SearchType.getByName("name");
//            Iterator<ResponseItem> itemIt = itMap.get(curr_type);
//            if (itemIt == null || !itemIt.hasNext()) {
//                curr_priority = this.changePriority(curr_priority);
//                faults += 1;
//                if (faults == SearchType.values().length) {
//                    break;
//                }
//                continue;
//            }
//            faults = 0;
//            if (curr_priority == 1) {
//                this.addItem(curr_type, itemIt, shuffledItems, 2);
//            } else {
//                this.addItem(curr_type, itemIt, shuffledItems, 1);
//            }
//            curr_priority = this.changePriority(curr_priority);
//        }
        return null;
    }

    private int changePriority(int pr) {
        if (pr >= 5) {
            return 1;
        } else if (pr < 1) {
            return 1;
        } else {
            return pr + 1;
        }
    }

    private void addItem(SearchType type, Iterator<ResponseItem> it, Collection<ResponseItem> items, int number) {
        for (int i = 0; i < number; i++) {
            if (!it.hasNext()) {
                return;
            }
            ResponseItem ri = it.next();
//            ri.setType(type);
            items.add(ri);
        }
    }

    private Iterator<ResponseItem> addResults(Iterator<ResponseItem> itemIterator, List<ResponseItem> subItems) {
        List<ResponseItem> responseItems = new LinkedList<>();
        while (itemIterator.hasNext()) {
            responseItems.add(itemIterator.next());
        }
        responseItems.addAll(subItems);
        return responseItems.iterator();
    }
}
