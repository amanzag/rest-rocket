package es.amanzag.restrocket;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import es.amanzag.restrocket.RocketDevice.Command;

/**
 * @author amanzaneque
 *
 */
public class RocketHttpHandler extends HttpHandler {
    
    private final Pattern URL_PATTERN = Pattern.compile("/rocket/(.*)");
    
    private final long DEFAULT_DURATION = 200;
    
    private RocketDevice device;
    
    private ExecutorService executorService;
    
    public RocketHttpHandler(RocketDevice device) {
        this.device = device;
        this.executorService = Executors.newSingleThreadExecutor();
    }
    
    @Override
    public void service(Request request, Response response) throws Exception {
        System.out.printf("[%s] Request %s %s received from %s\n", LocalDateTime.now(), request.getMethod(), request.getRequestURL(), request.getRemoteAddr());
        Matcher urlMatcher = URL_PATTERN.matcher(request.getPathInfo());
        
        if (request.getMethod() == Method.POST && urlMatcher.matches()) {
            String action = urlMatcher.group(1);
            executorService.submit(() -> {
                try {
                    Command c = Command.valueOf(action);
                    System.out.printf("[%s] Running command %s\n", LocalDateTime.now(), c);
                    device.sendCommand(c, DEFAULT_DURATION);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
            response.setStatus(HttpStatus.OK_200);
        } else {
            System.out.println("unknown request: " + request.getMethod() + " " + request.getPathInfo());
            response.setStatus(HttpStatus.NOT_FOUND_404);
        }
    }

}
