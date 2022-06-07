package com.example.atlasbackend

import com.example.atlasbackend.classes.UserDetails
import com.example.atlasbackend.classes.UserDetailsService
import com.example.atlasbackend.service.AuthFilter
import com.example.atlasbackend.service.AuthenticationService
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(val authenticationService: AuthenticationService): WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .authenticationManager(authenticationService)
            .addFilter(AuthFilter())
            .authorizeRequests()
                .anyRequest()
                .authenticated()
            .and()
            .cors()
                .disable()
    }

}