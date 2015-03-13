package services.statistics;


public interface DBService<INPUT> {

    /**
     * inserts entity into some storage
     * can store @param entity in the temporary collection
     * and then write as Batch process into storage
     *
     * @param entity
     */
    void insert(INPUT entity);

}
