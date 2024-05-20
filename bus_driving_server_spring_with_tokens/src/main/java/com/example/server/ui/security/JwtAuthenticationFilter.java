package com.example.server.ui.security;

import com.example.server.common.Constants;
import com.example.server.domain.exception.TokenInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.apache.logging.log4j.util.Strings.isEmpty;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isEmpty(header) || !header.startsWith(Constants.BEARER_WITH_SPACE)) {
            filterChain.doFilter(request, response);
            return;
        }
        String[] values = header.split(Constants.BLANK_SPACE);
        if (values[0].equalsIgnoreCase(Constants.BEARER) && values.length > 1) {
            try {
                Claims claims = jwtService.extractJwtClaims(values[1]);

                String role = (String) claims.get(Constants.ROLE_LOWER_CASE);
                String username = (String) claims.get(Constants.USERNAME_LOWER_CASE);

                UserDetails userDetails = User.builder()
                        .username(username)
                        .password(Constants.EMPTY_STRING)
                        //TODO: what should I keep here, the authorities or the roles?
                        .roles(role)
                        .authorities(role)
                        .build();

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {
                throw new TokenInvalidException(Constants.INVALID_REFRESH_TOKEN);
            }
        }

    }


}
