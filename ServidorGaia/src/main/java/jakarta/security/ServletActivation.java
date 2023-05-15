package jakarta.security;

import domain.services.ServicesLogin;
import jakarta.inject.Inject;
import jakarta.listener.ThymeLeafListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "ServletActivation", urlPatterns = {"/activate"})
public class ServletActivation extends HttpServlet {
    private final ServicesLogin servicesLogin;

    @Inject
    public ServletActivation(ServicesLogin servicesLogin) {
        this.servicesLogin = servicesLogin;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException   {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeLeafListener.TEMPLATE_ENGINE_ATTR);

        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(request, response);
        WebContext context = new WebContext(webExchange);

        String template = "activation";
        String mensaje;
        String codigo = request.getParameter("codigo");

        try {
            servicesLogin.activate(codigo, LocalDateTime.now());
            request.getSession().setAttribute("login", true);
            mensaje = "Usuario activado. Vuelva a la aplicación para iniciar sesión";
            request.setAttribute("mensaje", mensaje);
        } catch (Exception e) {
            template = "error";
            mensaje = e.getMessage();
            request.setAttribute("mensaje", mensaje);
        }

        templateEngine.process(template, context, response.getWriter());

    }
}
