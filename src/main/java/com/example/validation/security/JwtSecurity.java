package com.example.validation.security;

import com.example.validation.repository.UserAccessRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtSecurity extends OncePerRequestFilter {
    private final UserAccessRepository userAccessRepository;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            if (this.jwtUtils.isValid(token)) {
                this.userAccessRepository.findById(this.jwtUtils.getClaims(token, "sub", String.class))
                        .ifPresent(userAccessSession -> {
                            SecurityContextHolder.getContext().setAuthentication(
                                    new UsernamePasswordAuthenticationToken(
                                            userAccessSession.getUserDto(),
                                            userAccessSession.getUserDto().getPassword(),
                                            userAccessSession.getUserDto().getAuthorities()));
                        });
            }
        }
    }
}
