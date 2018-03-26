package petrov.moneytransfer.context;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import petrov.moneytransfer.dao.AccountDao;
import petrov.moneytransfer.dao.AccountDaoImpl;
import petrov.moneytransfer.dao.TransferDao;
import petrov.moneytransfer.dao.TransferDaoImpl;
import petrov.moneytransfer.service.AccountService;
import petrov.moneytransfer.service.AccountServiceImpl;
import petrov.moneytransfer.service.TransferService;
import petrov.moneytransfer.service.TransferServiceImpl;

/**
 *
 * @author petrov
 */
public class AppConfig extends ResourceConfig {
    
    private static final String TRACING = "OFF";
    
    private static final String [] PACKAGES_TO_SCAN = {
        "petrov.moneytransfer.resource",
        "petrov.moneytransfer.exception.handler",      
    };
    
    public AppConfig() {   
        property("jersey.config.server.tracing.type", TRACING);      
        registerInstances(new LoggingFeature(
                Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), 
                Level.SEVERE, 
                LoggingFeature.Verbosity.PAYLOAD_ANY, 
                LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));
        
        packages(PACKAGES_TO_SCAN);

        register(new AbstractBinder() {      
            @Override
            protected final void configure() {
                // service
                bind(AccountServiceImpl.class).to(AccountService.class)
                        .in(Singleton.class);
                bind(TransferServiceImpl.class).to(TransferService.class)
                        .in(Singleton.class);
                // dao
                bind(AccountDaoImpl.class).to(AccountDao.class)
                        .in(Singleton.class);
                bind(TransferDaoImpl.class).to(TransferDao.class)
                        .in(Singleton.class);
            }
        });
    }
}