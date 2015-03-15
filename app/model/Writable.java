package model;


import java.io.IOException;
import java.io.OutputStream;

/**
 * The writable interface
 */
public interface Writable {

    void writeTo(OutputStream out) throws IOException;

}
