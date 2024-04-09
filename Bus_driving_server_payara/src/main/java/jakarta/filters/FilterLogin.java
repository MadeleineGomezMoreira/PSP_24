package jakarta.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//TODO: check the URLS
@WebFilter(filterName = "FilterLogin",urlPatterns = {"/private/api/*"})
public class FilterLogin  implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("LOGIN") == null) {
            ((HttpServletResponse)servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN,"FORBIDDEN");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
