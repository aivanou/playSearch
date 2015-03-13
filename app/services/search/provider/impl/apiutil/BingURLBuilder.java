package services.search.provider.impl.apiutil;

import model.SearchRequest;

/**
 * BingURLBuilder produce method to build URL for Bing API
 *
 * @author vadim
 * @date 12/12/12
 */
public class BingURLBuilder extends URLBuilder {
    public static final String DEFAULT_BASE = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web";
    private static final BingURLBuilder DEFAULT = new BingURLBuilder(DEFAULT_BASE);
    private static final String QUERY_ARG = "&Query=";
    private static final String MARKET_ARG = "&Market=";
    private static final String TOP_ARG = "&$top=";
    private static final String SKIP_ARG = "&$skip=";
    private static final String COMMON_PART = "Adult='Moderate'&$format=JSON&WebFileType='HTML'";
    private static final int MAX_NUM = 50;
    private final String addr;

    public BingURLBuilder(String base) {
        super(base);
        addr = String.format("%s?%s", this.base, COMMON_PART);
    }

    public static BingURLBuilder defaultInstance() {
        return DEFAULT;
    }

    @Override
    public String build(SearchRequest req) throws IllegalArgumentException {
        validate(req);
        String market = market(req.getLang(), req.getRegion());
        int top = top(req.getNumber());
        int skip = skip(req.getPage(), req.getNumber());
        StringBuilder sb = new StringBuilder();
        sb.append(addr).append(MARKET_ARG).append(market).append(TOP_ARG).append(top).append(SKIP_ARG)
                .append(skip).append(QUERY_ARG).append('\'').append(req.getQuery()).append('\'');
        return sb.toString();
    }

    /**
     * Calculate top parameter according to number parameter
     *
     * @param number from {@code SearchRequest}
     * @return top parameter
     * @throws IllegalArgumentException if number is less or equals to zero
     */
    private int top(int number) throws IllegalArgumentException {
        if (number <= 0) {
            throw new IllegalArgumentException(String.format("Invalid number parameter '%s' in request, must be positive", number));
        }
        return number > MAX_NUM ? MAX_NUM : number;
    }

    /**
     * Calculate skip parameter according to given page and number
     *
     * @param page   from {@code SearchRequest}
     * @param number from {@code SearchRequest}
     * @return calculated skip parameter as <p>number * (page - 1)</p>
     * @throws IllegalArgumentException if page or number are equals or less than zero
     */
    private int skip(int page, int number) throws IllegalArgumentException {
        if (page < 0 || number <= 0) {
            throw new IllegalArgumentException(String.format("Number and page parameters expected to be positive, actual: page %s, number %s", page, number));
        }
        return number * (page);
    }

    /**
     * Calculate market parameter according to given lang and region
     *
     * @param lang   from {@code SearchRequest}
     * @param region from {@code SearchRequest}
     * @return market parameter as 'lang-REGION'
     */
    private String market(String lang, String region) {
        //todo maybe it's better to use java.util.Locale?
        StringBuilder sb = new StringBuilder(lang.length() + region.length() + 3);
        sb.append('\'').append(lang.toLowerCase()).append('-').append(region.toUpperCase()).append('\'');
        return sb.toString();
    }
}
