package es.amanzag.restrocket;

import java.io.IOException;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

/**
 * @author amanzaneque
 *
 */
public class RestRocket {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = HttpServer.createSimpleServer();
        
        server.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(RestRocket.class.getClassLoader(), "static/"));
        
        server.getServerConfiguration().addHttpHandler(new HttpHandler() {
            @Override
            public void service(Request request, Response response) throws Exception {
                response.setStatus(HttpStatus.OK_200);
            }
        }, "/rocket");
        
        server.start();
        
        System.out.println("Press any key to exit");
        System.in.read();
        
        server.shutdown();
    }

}
