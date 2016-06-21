package com.estafet.bankx.camel.base;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;

/**
 * Created by estafet.
 */
public abstract class ServerTestSupport extends CamelTestSupport {

    protected static int port;


    public static void initPort() throws Exception {
        port = AvailablePortFinder.getNextAvailable(21000);
    }

    protected int getPort() {
        return port;
    }

    @SuppressWarnings("unchecked")
    protected <T> T lookup(String name) {
        return (T) context.getRegistry().lookupByName(name);
    }

    @SuppressWarnings("unchecked")
    protected <T> T lookup(String name, Class<T> clazz) {
        return (T) context.getRegistry().lookupByNameAndType(name, clazz);
    }

    /**
     * Creating Camel Context with `test` properties.
     *
     * @return The newly created Camel context.
     * @throws Exception On any setup and configuration problem.
     */
    @Override
    protected CamelContext createCamelContext() throws Exception {
        // Generally this is not needed for this example but I wold like to remember to test using properties as route configuration.
        CamelContext context = super.createCamelContext();

        PropertiesComponent properties = context.getComponent("properties", PropertiesComponent.class);
        properties.setLocation("classpath:etc/test.properties");

        return context;
    }
}
