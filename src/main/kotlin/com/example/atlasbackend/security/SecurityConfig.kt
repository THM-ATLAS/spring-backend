package com.example.atlasbackend.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .authenticationManager(LDAPAuthenticationManager())
            .addFilterAt(UsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests()
                .antMatchers("/", "/login", "/logout")
                .permitAll()
            .anyRequest()
                .authenticated()
            .and()
            .csrf()
                .disable()
            .cors()
                .disable()
            .httpBasic()

    }
}