package jndi.helper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class JndiHelper {

    private static final String PROVIDER_URL = "file:D:/Faks/3. GODINA/Java 2/GameOfHunt/Config";

    private static final String CONFIGURATION_FILE_NAME = "conf.properties";

    private static InitialContext context;

    private static InitialContext getInitialContext() throws NamingException, IOException {
        if(context == null) {
            Properties confProps = new Properties();
            confProps.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            confProps.setProperty(Context.PROVIDER_URL, PROVIDER_URL);
            context = new InitialContext(confProps);
        }

        return context;
    }

    public static String getConfigurationParameter(ConfigEnum key) throws NamingException, IOException {
        Object configurationFileObject = getInitialContext().lookup(CONFIGURATION_FILE_NAME);
        Properties serverProps = new Properties();
        serverProps.load(new FileReader(configurationFileObject.toString()));
        return serverProps.getProperty(key.getKey());
    }

}
