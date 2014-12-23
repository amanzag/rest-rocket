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
    
    private Pattern regexp = Pattern.compile("/rocket/(.*)");
    
    private RocketDevice device;
    
    public RocketHttpHandler(RocketDevice device) {
        this.device = device;
    }
    
    @Override
    public void service(Request request, Response response) throws Exception {
        Matcher urlMatcher = regexp.matcher(request.getPathInfo());
        
        if (request.getMethod() == Method.POST && urlMatcher.matches()) {
            String action = urlMatcher.group(1);
            device.sendCommand(Command.valueOf(action), 1000);
            response.setStatus(HttpStatus.OK_200);
        } else {
            System.out.println("unknown request: " + request.getMethod() + "" + request.getPathInfo());
            response.setStatus(HttpStatus.NOT_FOUND_404);
        }
    }

}
