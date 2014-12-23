package es.amanzag.restrocket;

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
    
    private final long DEFAULT_DURATION = 500;
    
    private RocketDevice device;
    
    public RocketHttpHandler(RocketDevice device) {
        this.device = device;
    }
    
    @Override
    public void service(Request request, Response response) throws Exception {
        Matcher urlMatcher = URL_PATTERN.matcher(request.getPathInfo());
        
        if (request.getMethod() == Method.POST && urlMatcher.matches()) {
            String action = urlMatcher.group(1);
            device.sendCommand(Command.valueOf(action), DEFAULT_DURATION);
            response.setStatus(HttpStatus.OK_200);
        } else {
            System.out.println("unknown request: " + request.getMethod() + "" + request.getPathInfo());
            response.setStatus(HttpStatus.NOT_FOUND_404);
        }
    }

}
