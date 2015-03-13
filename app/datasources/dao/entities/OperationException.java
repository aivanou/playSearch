package datasources.dao.entities;

/**
 * error describes problems, that occur during entity operations (EntitiesDAO)
 */
public class OperationException extends Exception {

    public OperationException(String message) {
        super(message);
    }
}
