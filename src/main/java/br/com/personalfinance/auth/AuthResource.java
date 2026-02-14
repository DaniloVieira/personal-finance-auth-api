package br.com.personalfinance.auth;

import br.com.personalfinance.auth.dto.AuthResponse;
import br.com.personalfinance.auth.dto.LoginRequest;
import br.com.personalfinance.auth.dto.SignupRequest;
import br.com.personalfinance.auth.entity.User;
import br.com.personalfinance.common.exception.BusinessException;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/signup")
    public Response signup(@Valid SignupRequest request) {
        try {
            User user = authService.signup(request);
            String token = authService.generateToken(user);
            return Response.status(Response.Status.CREATED)
                    .entity(new AuthResponse(token, user.name, user.email))
                    .build();
        } catch (BusinessException e) {
//            throw new RuntimeException(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Signup failed: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        try {
            User user = authService.authenticate(request);
            String token = authService.generateToken(user);
            return Response.ok(new AuthResponse(token, user.name, user.email))
                    .build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Login failed: " + e.getMessage())
                    .build();
        }
    }
}
