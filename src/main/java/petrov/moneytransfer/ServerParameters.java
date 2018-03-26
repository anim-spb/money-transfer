package petrov.moneytransfer;

import com.beust.jcommander.Parameter;

/**
 *
 * @author petrov
 */
public class ServerParameters {
    
    private final String host = "localhost";
    private final String path = "/api/"; 
    @Parameter(names = {"-p", "--port"}, description = "Port to listen on.")
    private Integer port = 8080;

    public Integer getPort() {
        return port;
    }    

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }
}
