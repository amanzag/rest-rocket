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
        RocketDevice rocket = new RocketDevice();
        rocket.init();
        
        final HttpServer server = HttpServer.createSimpleServer(
                null,
                Optional.ofNullable(System.getProperty("port")).map(Integer::parseInt).orElse(8080)
                );
        
        server.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(RestRocket.class.getClassLoader(), "static/"));
        server.getServerConfiguration().addHttpHandler(new RocketHttpHandler(rocket), "/api/*");

        boolean cameraEnabled = !Optional.ofNullable(System.getProperty("disableCamera")).map(Boolean::parseBoolean).orElse(false);
        Camera camera = null;
        if (cameraEnabled) {
            int cameraNumber = Optional.ofNullable(System.getProperty("camera")).map(Integer::parseInt).orElse(1);
            System.out.println("Using camera device " + cameraNumber);
            camera = Camera.getDevice(cameraNumber);
            camera.open();
            server.getServerConfiguration().addHttpHandler(new CameraHttpHandler(camera), "/camera/*");
        }
        
        server.start();

        System.out.println("Press any key to exit");
        System.in.read();
        
        server.shutdown();
        rocket.close();
        if (camera != null) {
            camera.close();
        }
        
    }

}
