package es.amanzag.restrocket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.Header;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.grizzly.http.util.MimeType;

/**
 * @author amanzaneque
 *
 */
public class CameraHttpHandler extends HttpHandler {
    
    private Camera camera;
    private ScheduledExecutorService executor;
    private volatile byte[] currentFrame;
    
    public CameraHttpHandler(Camera camera) {
        this.camera = camera;
        executor = Executors.newScheduledThreadPool(1);
        
        executor.scheduleAtFixedRate(this::refreshFrame, 1000, 300, TimeUnit.MILLISECONDS);
    }

    @Override
    public void service(Request request, Response response) throws Exception {
        if (request.getPathInfo().equals("/frame")) {
            response.setHeader(Header.ContentType, MimeType.get("jpeg"));
            if(request.getMethod() == Method.GET) {
                if (currentFrame != null) {
                    response.getOutputStream().write(currentFrame);
                    response.setStatus(HttpStatus.OK_200);
                } else {
                    response.setStatus(HttpStatus.SERVICE_UNAVAILABLE_503);
                }
            } else if (request.getMethod() == Method.HEAD) {
                response.setStatus(HttpStatus.OK_200);
            } else {
                response.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
            }
        } else {
            response.setStatus(HttpStatus.NOT_FOUND_404);
        }
    }
    
    private void refreshFrame() {
        currentFrame = camera.grabFrame();
    }

}
