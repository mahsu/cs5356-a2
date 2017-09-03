package controllers;


import io.dropwizard.jersey.sessions.Session;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class NetidController {

    @GET
    @Path("/netid")
    public String netid(@Session HttpSession session) {
        return "mdh267";
    }
}
