package petrov.moneytransfer;

import com.beust.jcommander.JCommander;
import java.io.File;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.logging.Logger;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import petrov.moneytransfer.context.AppConfig;
import petrov.moneytransfer.dao.datasource.DataSourceProvider;
import petrov.moneytransfer.dao.datasource.DefaultDataSourceProvider;
import petrov.moneytransfer.exception.handler.DuplicateTransferMapper;

/**
 *
 * @author petrov
 */
public class Server {
    
    private static final Logger LOGGER = Logger.getLogger(
            DuplicateTransferMapper.class.getCanonicalName());
    private static final ServerParameters SERVER_PARAMETERS = new ServerParameters();

    public static HttpServer startServer() {          
        final ResourceConfig config = new AppConfig().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(DefaultDataSourceProvider.class).to(DataSourceProvider.class)
                    .in(Singleton.class);
            }
        });
        
        URI uri = UriBuilder.fromPath(SERVER_PARAMETERS.getPath())
                .scheme("http")
                .host(SERVER_PARAMETERS.getHost())
                .port(SERVER_PARAMETERS.getPort()).build();
        
        return GrizzlyHttpServerFactory.createHttpServer(uri, config);
    }

    public static void main(String[] args) throws IOException {
        JCommander.newBuilder()
                .addObject(SERVER_PARAMETERS)
                .build()
                .parse(args);
        
        final HttpServer server;
        LOGGER.info("Starting server");
        server = startServer();
        
        if (isRunningInJar()) {                  
            LOGGER.info("Press Ctrl^C to exit...");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Stopping server");
                server.shutdown();
            }));
        }
        else {
            System.out.println("Hit enter to stop...");
            System.in.read();
            LOGGER.info("Stopping server");
            server.shutdown();
        }
    }
    
    private static boolean isRunningInJar()
    {
        ProtectionDomain protectionDomain = Server.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        File source;
        try {
            source = new File(location.toURI());
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        
        return source.isFile();
    }
}

