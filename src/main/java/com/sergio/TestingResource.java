package com.sergio;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class TestingResource {
    /**
     * Handles HTTP GET requests to the /hello endpoint and returns a plain text greeting.
     *
     * @return a plain text message "Hello from Quarkus REST"
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }
}
