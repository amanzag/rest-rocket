package es.amanzag.restrocket;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import es.amanzag.restrocket.dto.Target;

import static es.amanzag.restrocket.RocketKeys.PARAM_TARGET_ID;

@Path("/target")
public class TargetHttpResource {
	@POST @Path("/{"+PARAM_TARGET_ID+"}")
	@Consumes("application/json")
	public void saveTarget(@PathParam(value=PARAM_TARGET_ID) String targetId, Target target){
		System.out.println(targetId);
	}
	
	@GET @Path("/helloWorld")
	public String helloWorld() {
		return "hi dude!";
	}
}
