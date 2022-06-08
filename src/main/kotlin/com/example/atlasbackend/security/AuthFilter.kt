package com.example.atlasbackend.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class UsernamePasswordAuthenticationFilter : UsernamePasswordAuthenticationFilter() {
    private var jsonUsername: String? = null
    private var jsonPassword: String? = null
    override fun getAuthenticationManager(): AuthenticationManager {
        return LDAPAuthenticationManager()
    }

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
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line)
        }
        val parsedReq = sb.toString()
        if (parsedReq != null) {
            val authReq: AuthReq = jacksonObjectMapper().readValue(parsedReq)
            jsonPassword = authReq.password
            jsonUsername = authReq.username
            return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(jsonUsername, jsonPassword)
            )
        }

        return null
    }
}

class AuthReq(var username: String, var password: String)