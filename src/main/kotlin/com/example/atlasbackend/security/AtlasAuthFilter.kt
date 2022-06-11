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
    private var jsonUsername: String? = null
    private var jsonPassword: String? = null

    override fun obtainPassword(request: HttpServletRequest): String? {
        return if ("application/json" == request.getHeader("Content-Type")) {
            jsonPassword
        } else {
            super.obtainPassword(request)
        }
    }

    override fun obtainUsername(request: HttpServletRequest): String? {
        return if ("application/json" == request.getHeader("Content-Type")) {
            jsonUsername
        } else {
            super.obtainUsername(request)
        }
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {

        val reader = request.reader
        val sb = StringBuffer()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        val parsedReq = sb.toString()
        val authReq: AuthReq = jacksonObjectMapper().readValue(parsedReq)
        jsonPassword = authReq.password
        jsonUsername = authReq.username
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(jsonUsername, jsonPassword)
        )
    }
}

class AuthReq(var username: String, var password: String)
