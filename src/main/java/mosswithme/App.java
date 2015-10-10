package mosswithme;

import java.io.IOException;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class App 
{
	public static void main(String[] args) throws IOException {
        // create jersey-grizzly server
        ResourceConfig rc = new PackagesResourceConfig("my.resources");
        HttpServer httpServer = GrizzlyServerFactory.createHttpServer(UriBuilder.fromUri("http://localhost").port(8080).path("/").build(), new HttpHandler() {

            @Override
            public void service(Request rqst, Response rspns) throws Exception {
                rspns.setStatus(404, "Not found");
                rspns.getWriter().write("404: not found");
            }
        });
        try {
            httpServer.start();
            System.out.println("Press any key to stop the server...");
            System.in.read();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
	
	
}

