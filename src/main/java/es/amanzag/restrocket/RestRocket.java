package es.amanzag.restrocket;

import java.io.IOException;
import java.util.Optional;

import javax.usb.UsbException;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;

/**
 * @author amanzaneque
 *
 */
public class RestRocket {

    /**
     * @param args
     * @throws IOException 
     * @throws UsbException 
     */
    public static void main(String[] args) throws IOException, UsbException {
        RocketDevice device = new RocketDevice();
        device.init();
        
        final HttpServer server = HttpServer.createSimpleServer(
                null,
                Integer.parseInt(Optional.ofNullable(System.getProperty("port")).orElse("8080")));
        
        server.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(RestRocket.class.getClassLoader(), "static/"));
        
        server.getServerConfiguration().addHttpHandler(new RocketHttpHandler(device), "/api/*");
        
        server.start();
        
        System.out.println("Press any key to exit");
        System.in.read();
        
        server.shutdown();
    }

}
