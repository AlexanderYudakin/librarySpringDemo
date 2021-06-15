package com.example.library.security;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String tokenHeaderValue = request.getHeader(AUTHORIZATION);
        String username = null;
        String jwtToken = null;
        if (tokenHeaderValue != null && tokenHeaderValue.startsWith("Bearer ")) {
            jwtToken = tokenHeaderValue.substring(7);
            username = jwtTokenService.getUsernameFromToken(jwtToken);
            if (username != null) {
                val context = SecurityContextHolder.getContext();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                val usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                context.setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
