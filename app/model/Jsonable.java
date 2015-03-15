package model;

import java.io.IOException;

/**
 * Classes who support this interface should return JSON
 */
public interface Jsonable {

    String toJson() throws IOException;
}
