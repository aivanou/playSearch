package services.search.provider.impl.apiutil;

import model.request.SearchRequest;

/**
 * GoogleURLBuilder ...
 *
 * @author vadim
 * @date 12/12/12
 */
public class GoogleURLBuilder extends URLBuilder {
    public static final String DEFAULT_BASE = "https://www.googleapis.com/customsearch/v1";
    private static final GoogleURLBuilder DEFAULT_BUILDER = new GoogleURLBuilder(DEFAULT_BASE);
    private static final String QUERY = "&q=";
    private static final String LANG = "&cr=";
    private static final String START = "&start=";
    private static final String SIZE = "&num=";
    public static final String CX = "cx";
    public static final String KEY = "key";
    private static final String REGION = "&gl=";
    private static final String COMMON_PART = "alt=json&prettyPrint=false&fields=items(title,link,snippet)";
    private static final int MAX_NUM = 10;

    private final String addr;

    public GoogleURLBuilder(String base) {
        super(base);
        this.addr = String.format("%s?%s", base, COMMON_PART);
    }

    public static GoogleURLBuilder defaultInstance() {
        return DEFAULT_BUILDER;
    }

    @Override
    public String build(SearchRequest req) throws IllegalArgumentException {
        validate(req);
        int start = start(0, req.getTotalNumber());
        int size = size(req.getTotalNumber());
        StringBuilder sb = new StringBuilder();
        sb.append(addr).append(START).append(start).append(SIZE).append(size).append(LANG).append(req.getLang())
                .append(REGION).append(req.getRegion()).append(QUERY)
                .append(req.getQuery().trim().replaceAll("\\s+", "\\+"));
        return sb.toString();
    }

    private int start(int page, int number) throws IllegalArgumentException {
        if (page < 0 || number <= 0) {
            throw new IllegalArgumentException(String.format("Number and page parameters expected to be positive, actual: page %s, number %s", page, number));
        }
        return number * (page) + 1;
    }

    private int size(int number) throws IllegalArgumentException {
        if (number <= 0) {
            throw new IllegalArgumentException(String.format("Invalid number parameter '%s' in request, must be positive", number));
        }
        return number > MAX_NUM ? MAX_NUM : number;
    }
}
