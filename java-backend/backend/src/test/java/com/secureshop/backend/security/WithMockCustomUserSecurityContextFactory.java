package com.secureshop.backend.security;

import com.secureshop.backend.user.Role;
import com.secureshop.backend.user.User;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(
            WithMockCustomUser annotation) {

        User user = User.builder()
                .id(annotation.id())
                .firstName(annotation.firstName())
                .lastName(annotation.lastName())
                .email(annotation.email())
                .password(annotation.password())
                .role(Role.valueOf(annotation.role()))
                .enabled(true)
                .build();

        UserDetailsImpl principal =
                new UserDetailsImpl(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities());

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        return context;
    }
}