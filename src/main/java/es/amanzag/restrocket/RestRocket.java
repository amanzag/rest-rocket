package es.amanzag.restrocket;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.usb.UsbException;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author amanzaneque
 *
 */
public class RestRocket {

    /**
     * @param args
     * @throws IOException 
     * @throws UsbException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws IOException, UsbException, InterruptedException {
        RocketDevice rocket = new RocketDevice();
        rocket.init();
        
        final HttpServer server = HttpServer.createSimpleServer(
                null,
                Optional.ofNullable(System.getProperty("port")).map(Integer::parseInt).orElse(8080)
                );
        
        server.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(RestRocket.class.getClassLoader(), "static/"));

        boolean cameraEnabled = !Optional.ofNullable(System.getProperty("disableCamera")).map(Boolean::parseBoolean).orElse(false);
        Camera camera = null;
        if (cameraEnabled) {
            int cameraNumber = Optional.ofNullable(System.getProperty("camera")).map(Integer::parseInt).orElse(0);
            System.out.println("Using camera device " + cameraNumber);
            camera = Camera.getDevice(cameraNumber);
            camera.open();
        }
        
        ResourceConfig rc = new ResourceConfig();
        rc.packages(RestRocket.class.getPackage().toString());
        Camera boundCamera = camera; 
        rc.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(rocket).to(RocketDevice.class);
                bind(Executors.newSingleThreadExecutor()).to(ExecutorService.class);
                if (cameraEnabled) {
                    bind(boundCamera).to(Camera.class);
                }
            }
        });
        server.getServerConfiguration().addHttpHandler(ContainerFactory.createContainer(GrizzlyHttpContainer.class, rc), "/api/*");
        
        CountDownLatch latch = new CountDownLatch(1);
        Camera cameraReference = camera;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.shutdown();
                rocket.close();
                if (cameraReference != null) {
                    cameraReference.close();
                }
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        server.start();
        // keep the thread waiting so that the app doesn't exit
        latch.await();
    }

}
