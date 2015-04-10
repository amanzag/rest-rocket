package es.amanzag.restrocket;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.glassfish.grizzly.http.server.Request;

import es.amanzag.restrocket.RocketDevice.Command;

/**
 * @author amanzaneque
 *
 */
@Path("/rocket")
public class RocketHttpResource {
    
    private final long DEFAULT_DURATION = 200;
    
    @Inject private RocketDevice device;
    
    @Inject private ExecutorService executorService;
    
    @Context private Request request;
    
    @POST @Path("/{command}") 
    public void runCommand(@PathParam("command") Command command) {
        System.out.printf("[%s] Request %s %s received from %s\n", LocalDateTime.now(), request.getMethod(), request.getRequestURL(), request.getRemoteAddr());
        executorService.submit(() -> {
            try {
                System.out.printf("[%s] Running command %s\n", LocalDateTime.now(), command);
                device.sendCommand(command, DEFAULT_DURATION);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
