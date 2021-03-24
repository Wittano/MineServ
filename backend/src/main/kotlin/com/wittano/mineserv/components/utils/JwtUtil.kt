package com.wittano.mineserv.components.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

/**
 * Support class for generate and check JWT tokens
 */
@Component
class JwtUtil {
    @Value("\${jwt.expired}")
    private val expired: Int? = null

    @Value("\${project.jwt.secret}")
    private val secret: String? = null

    /**
     * Get claims from token
     * @param token JWT token
     * @throws JwtException throw, when token isn't valid
     * @return Claims object
     * @see Claims
     */
    @Throws(io.jsonwebtoken.JwtException::class)
    private fun getClaims(token: String): Claims = Jwts
        .parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .body

    @Throws(io.jsonwebtoken.JwtException::class)
    fun decode(token: String): Claims = getClaims(token)

    /**
     * Check if token has correct signature and if token isn't expired
     * @param token JWT token
     * @return Return false, when token hasn't correct signature or is expired, otherwise True
     */
    fun isValid(token: String): Boolean {
        val regex = Regex("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*\$")

        return try {
            val jwt = decode(token)

            regex.matches(token) && jwt.expiration.after(Date())
        } catch (e: JwtException) {
            false
        }
    }

    /**
     * Create JWT token
     * @param subject username
     * @param claims other attributes, which will be inside in payload
     */
    fun create(subject: String, claims: Map<String, Any> = HashMap()): String = Jwts
        .builder()
        .setClaims(claims)
        .setExpiration(Date(System.currentTimeMillis() + (expired!! * 1000)))
        .setIssuedAt(Date())
        .setSubject(subject)
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact()
}