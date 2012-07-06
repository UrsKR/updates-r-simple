package de.idos.updates.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

public class FileServer {

    public void start() throws Exception {
        Server server = new Server(8080);
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);
        ContextHandler updateContext = createUpdateContext();
        ContextHandler rootContext = createRootContext();
        ContextHandlerCollection handlers = new ContextHandlerCollection();
        handlers.setHandlers(new Handler[]{updateContext, rootContext});
        server.setHandler(handlers);
        server.start();
    }

    private ContextHandler createUpdateContext() {
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("./src/test/resources/httpUpdateServerBase");
        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setHandler(resource_handler);
        contextHandler.setContextPath("/updates");
        return contextHandler;
    }

    private ContextHandler createRootContext() {
        ContextHandler rootContext = new ContextHandler("/");
        rootContext.setHandler(new DefaultHandler());
        return rootContext;
    }
}
