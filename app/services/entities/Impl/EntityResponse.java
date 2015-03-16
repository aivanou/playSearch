package services.entities.Impl;

public class EntityResponse<T> {

    private T message;
    private Throwable error;

    public EntityResponse(T message) {
        this.message = message;
        this.error = null;
    }

    public EntityResponse(Throwable error) {
        this.error = error;
    }


    public Throwable getError() {
        return error;
    }

    public T getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityResponse that = (EntityResponse) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }
}
