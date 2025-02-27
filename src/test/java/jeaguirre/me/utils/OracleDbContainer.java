package jeaguirre.me.utils;

import org.testcontainers.containers.OracleContainer;

public class OracleDbContainer extends OracleContainer {

    private static final String IMAGE_VERSION = "gvenzl/oracle-xe:21-slim-faststart";
    private static OracleDbContainer container;

    private OracleDbContainer(String imageVersion) {
        super(imageVersion);
    }

    public static OracleDbContainer getInstance() {
        if (container == null) {
            container = new OracleDbContainer(IMAGE_VERSION);
            container.withInitScript("init_script.sql");
        }
        return container;
    }

}
