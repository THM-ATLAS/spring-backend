package com.example.atlasbackend.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class SecurityConfig(
    val atlasAuthFilter: AtlasAuthFilter,
    val ldapAuthenticationManager: LDAPAuthenticationManager
): WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .cors().disable()
            .authenticationManager(ldapAuthenticationManager)
            .addFilterAt(atlasAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests()
                .antMatchers("/api/users", "/api/docs", "/api/swagger-ui/**", "/api/docs/raw/**", "/login", "/css/**", "/js/**", "/fonts/**", "/*.ico").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
    }
}