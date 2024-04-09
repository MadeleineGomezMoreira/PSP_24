package jakarta.filters;

import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Context;

import java.io.IOException;

//TODO: check the URLS
@WebFilter(filterName = "FilterRoleUser", urlPatterns = {"/api/*"})
public class FilterRoleUser implements Filter {

    private final SecurityContext securityContext;

    @Inject
    public FilterRoleUser(@Context SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        //here I'll check if the user has the required role for the requested path
        if (securityContext.isCallerInRole(getRequiredRole(httpRequest))) {
            chain.doFilter(request, response); //if the caller is in role, continue with the request
        } else {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN"); //else return 403 error
        }
    }

    //this method will determine the required role based on the request
    private String getRequiredRole(HttpServletRequest request) {
        String requiredRole = "";

        //there will be a requestedRole parameter in the request
        //if the parameter is not null, then the required role will be the value of the parameter
        if (request.getParameter("requiredRole") != null) {
            requiredRole = request.getParameter("requiredRole");
        } else {
            //if the requestedRole parameter is null, then the required role will be "NONE"
            requiredRole = "NONE";
        }

        return requiredRole;
    }
}
