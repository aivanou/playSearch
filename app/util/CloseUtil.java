package util;

import play.Logger;

import java.io.Closeable;

public final class CloseUtil {

    private CloseUtil() {
        // Empty
    }

    /**
     * Close a closeable.
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                Logger.info("Unable to close " + closeable, e);
            }
        }
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                Logger.info("Unable to close " + closeable, e);
            }
        }
    }
}
