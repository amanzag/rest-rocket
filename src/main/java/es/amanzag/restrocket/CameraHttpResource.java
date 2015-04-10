package es.amanzag.restrocket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jvnet.hk2.annotations.Optional;
//import org.glassfish.grizzly.http.server.Response;

/**
 * @author amanzaneque
 *
 */
@Path("/camera/frame")
@Singleton
public class CameraHttpResource {
    
    @Inject @Optional private Camera camera;
    private ScheduledExecutorService executor;
    private volatile byte[] currentFrame;
    
    public CameraHttpResource() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::refreshFrame, 1000, 300, TimeUnit.MILLISECONDS);
    }
    
    @Produces("image/jpeg")
    @GET
    public Response getFrame() {
        if (camera != null || currentFrame != null) {
            return Response.ok(currentFrame).build();
        } else {
            return Response.status(Status.SERVICE_UNAVAILABLE).build();
        }
    }
    
    @HEAD
    public Response head() {
        if (camera == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
    
    private void refreshFrame() {
        currentFrame = camera.grabFrame();
    }

}
