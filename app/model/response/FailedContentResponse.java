package model.response;

import model.SearchType;

/**
 * If search request has errors, this message will be transferred to the client
 */
public class FailedContentResponse extends ContentResponse {

    private Throwable exception;
    private String errorMessage;

    public FailedContentResponse(SearchType searchType, Throwable exception) {
        super(searchType);
        this.exception = exception;
        this.errorMessage = exception.getMessage();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FailedContentResponse that = (FailedContentResponse) o;

        if (exception != null ? !exception.equals(that.exception) : that.exception != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return exception != null ? exception.hashCode() : 0;
    }

    @Override
    public String toJson() {
        return "{\"error\":\"" + exception.getMessage() + "\"}";
    }
}
