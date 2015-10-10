package mosswithme;

import java.io.IOException;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class App 
{
	public static void main(String[] args) throws IOException {
        // create jersey-grizzly server
//        ResourceConfig rc = new ResourceConfig(ApiRouter.class);
		
		StaticHttpHandler httpHandler = new StaticHttpHandler("moss/src/main/java/mosswithme/ApiRouter");
		
        HttpServer httpServer = GrizzlyServerFactory.createHttpServer(UriBuilder.fromUri("http://localhost").port(8080).path("/").build());
        httpServer.getServerConfiguration().addHttpHandler(httpHandler, "http://localhost:8080/");
        
        try {
            httpServer.start();
            System.out.println("Press any key to stop the server...");
            System.in.read();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
	
	
}

