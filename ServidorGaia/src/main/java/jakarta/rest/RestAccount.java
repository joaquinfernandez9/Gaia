package jakarta.rest;

import dao.DaoLogin;
import domain.model.Account;
import domain.services.ServicesLogin;
import domain.services.mail.SendEmail;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import utils.Utils;

import java.time.LocalDateTime;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestAccount {
    @Context
    HttpServletRequest req;

    private final SendEmail sendEmail;
    private final Utils utils;
    private final ServicesLogin log;


    @Inject
    public RestAccount(SendEmail sendEmail, ServicesLogin log, Utils utils) {
        this.sendEmail = sendEmail;
        this.log = log;
        this.utils = utils;
    }

    @GET
    @Path("/getUsers")
    public Response getUsers() {
        return Response.ok(log.get()).build();
    }

    @POST
    @Path("/log")
    public Response login(Account acc) {
        Account account = log.login(acc.getUsername(), acc.getPassword());
        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.accepted(log.login(acc.getUsername(), acc.getPassword())).build();
        }
    }

    @POST
    @Path("/register")
    public Response register(Account account) throws MessagingException {
        String activationCode = utils.randomBytes();
        account.setActivated(0);
        LocalDateTime time = LocalDateTime.now().plusMinutes(5);
        if (log.register(account, activationCode, time)) {
            sendEmail.generateAndSendEmail(
                    account.getEmail(),
                    "Para activar su cuenta haga click en el siguiente enlace: " +
                            "<a href=\"http://192.168.1.103:8080/ServidorGaia-1.0-SNAPSHOT/activate?codigo=" + activationCode+ "\"> Activar cuenta </a>",
                    "Activacion de cuenta"
            );
            return Response.status(Response.Status.CREATED).entity(account).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

}
