package jeaguirre.me.utils;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class OracleDbContainerExtension implements BeforeAllCallback {

    public static OracleDbContainer container = OracleDbContainer.getInstance();

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        container.start();

        System.setProperty("DATABASE_URL", container.getJdbcUrl());
        System.setProperty("DATABASE_USERNAME", container.getUsername());
        System.setProperty("DATABASE_PASSWORD", container.getPassword());
    }

}
