package dev.lebenkov.finly.security.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtService jwtService;
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                authenticateToken(token, request);
            } catch (JwtException | IllegalArgumentException
                     | AuthenticationException e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7);
    }

    private void authenticateToken(
            String token,
            HttpServletRequest request) {
        String username = jwtService.extractUsernameFromAccessToken(token);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        boolean accountIsValid = userDetails.isEnabled()
                && userDetails.isAccountNonExpired()
                && userDetails.isAccountNonLocked()
                && userDetails.isCredentialsNonExpired();

        if (accountIsValid) {
            setAuthentication(userDetails, request);
        }

    }

    private void setAuthentication(
            UserDetails userDetails,
            HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}