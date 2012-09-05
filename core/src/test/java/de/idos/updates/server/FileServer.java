package de.idos.updates.server;

import de.idos.updates.RootFolderSelector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import java.io.File;

public class FileServer {
  Server server = new Server(8080);

  public static void main(String[] args) throws Exception {
    new FileServer().start();
  }

  public void start() throws Exception {
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
    File rootFolder = new RootFolderSelector().getRootFolder();
    resource_handler.setResourceBase(new File(rootFolder, "src/test/resources/httpUpdateServerBase").getAbsolutePath());
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

  public void stop() throws Exception {
    server.stop();
  }
}
