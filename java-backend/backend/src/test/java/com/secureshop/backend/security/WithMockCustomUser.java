package com.secureshop.backend.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(
        factory = WithMockCustomUserSecurityContextFactory.class
)
public @interface WithMockCustomUser {

    long id() default 1L;

    String firstName() default "John";

    String lastName() default "Doe";

    String email() default "john@example.com";

    String password() default "password";

    String role() default "ROLE_CUSTOMER";
}