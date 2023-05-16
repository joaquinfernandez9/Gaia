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
    @Path("/login")
    public Account login() {
        String header = req.getHeader("Authorization");
        String[] values = header.split("\\s+");
        String base64Credentials = values[1];
        String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
        String[] userPass = credentials.split(":", 2);
        Account credentials1 = new Account(userPass[0], userPass[1]);
        return log.login(credentials1.getUsername(), credentials1.getPassword());
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
                            "<a href=\"http://192.168.1.102:8080/ServidorGaia-1.0-SNAPSHOT/activate?codigo=" + activationCode+ "\"> Activar cuenta </a>",
                    "Activacion de cuenta"
            );
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }

    }


}
