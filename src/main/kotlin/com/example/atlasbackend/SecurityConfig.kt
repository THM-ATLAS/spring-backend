package com.example.atlasbackend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        /*http.csrf().disable()
            .authorizeRequests().anyRequest().permitAll()
        http.cors().disable()*/

        //authorizing mapping requests based on roles
        http.authorizeRequests()
            .antMatchers("/admin")
            .hasRole("ADMIN")
            .antMatchers( "/users/","/users/{id}","/tasks/","/tasks/{taskID}","/tasks/user/{userID}")
            .hasAnyRole("USER", "ADMIN")
            .antMatchers("/").permitAll()
            .and()
            .formLogin()
    }

//TODO: https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/csrf.html wir sollten drüber reden, ob wir das brauchen

    @Autowired
    lateinit var dataSource: DataSource

    override fun configure(auth: AuthenticationManagerBuilder) {
        //defining authentication (username/password) for different Roles
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .withDefaultSchema()
            .withUser(
            User.withUsername("user")
                .password("user")
                .roles("USER")
            )
            .withUser(
                User.withUsername("admin")
                    .password("admin")
                    .roles("ADMIN")
            )
    }

    // hash code generator: the function is yet to be changed, it returns nothing for now
    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance();
    }
}