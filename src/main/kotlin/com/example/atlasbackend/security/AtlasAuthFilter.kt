package com.example.atlasbackend.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class AtlasAuthFilter(ldapAuthenticationManager: LDAPAuthenticationManager) : UsernamePasswordAuthenticationFilter(ldapAuthenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {

        val reader = request.reader
        val sb = StringBuffer()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        val parsedReq = sb.toString()
        val authReq: AuthReq = jacksonObjectMapper().readValue(parsedReq)
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authReq.username, authReq.password)
        )
    }
}

class AuthReq(var username: String, var password: String)
