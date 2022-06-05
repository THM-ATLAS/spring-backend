package com.example.atlasbackend

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests().anyRequest().permitAll()
        http.cors().disable()

        http.authorizeRequests()
        //TODO: https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/csrf.html wir sollten drüber reden, ob wir das brauchen
    }

    /*override fun configure(auth: AuthenticationManagerBuilder?) {
        auth.ldapAuthentication()
            .userDnPatterns("uid={0}, ou=People")
            .contextSource()
                .url("ldaps://ldap.fh-giessen.de:636/dc=fh-giessen-friedberg,dc=de")
                //.and()


    }*/

}