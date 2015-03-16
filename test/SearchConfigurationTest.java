import org.junit.Test;

import java.io.File;

import static org.junit.Assert.fail;

public class SearchConfigurationTest {

    @Test
    public void fileExistsTest() {
        String file = "~/conf/query.json";
        if (!new File(file).exists()) {
            fail("Configuration File does not exists: " + file);
        }
    }

}
