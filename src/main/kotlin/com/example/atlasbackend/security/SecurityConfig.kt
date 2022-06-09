package com.example.atlasbackend.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
class SecurityConfig(
    val atlasAuthFilter: AtlasAuthFilter,
    val ldapAuthenticationManager: LDAPAuthenticationManager
): WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .authenticationManager(ldapAuthenticationManager)
            .addFilterAt(atlasAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests()
                .antMatchers("/login", "/logout")
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