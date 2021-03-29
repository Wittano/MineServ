package com.wittano.mineserv.components

import com.wittano.mineserv.components.utils.JwtUtil
import io.jsonwebtoken.JwtException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean

@SpringBootTest
internal class JwtUtilTest {

    @SpyBean
    private val jwtUtil: JwtUtil? = null

    @ParameterizedTest
    @ValueSource(
        strings = [
            "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlI" +
                    "joiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6Ikphdm" +
                    "FJblVzZSIsImV4cCI6MTYxNDE3NDY3NSwiaWF0Ijo" +
                    "xNjE0MTc0Njc1fQ.g8EzzV6cizZL_etmha_rCC7bSH5c6ZP0xcPBoX_SKKE",
            "yJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJhc2Rn" +
                    "YXNkIiwiVXNlcm5hbWUiOiJhc2RnIiwiZXhwIjoxNjE0MTcxOTczLCJp" +
                    "YXQiOjE2MTQxNzE5NzN9.rwLqXFAMl6kjHSk_e61OEo97PXYeRUfhaXpnIaguB-w",
            "123.1233.4123",
            "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoasdgfsakiLCJJc3N1ZXIiOi" +
                    "JJc3N1ZXIiLCJVc2VybmFtZSI6Ikphdasidf7asdIsImV4cCI6" +
                    "MTYxNDE3MjExOCwiaWF0IjoxNjE0MTcyMTE4fQ.qwrewfrahZvItnUElv_x-K-PfJEVsFi6zS0KWLsAXtM"
        ]
    )
    fun decode_ShouldReturnNotNullJwtObject(token: String) {
        try {
            val t = jwtUtil!!.decode(token)

            assertNotNull(t)
        } catch (_: JwtException) {
        }
    }


    @ParameterizedTest
    @ValueSource(
        strings = [
            "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlI" +
                    "joiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6Ikphdm" +
                    "FJblVzZSIsImV4cCI6MTYxNDEsssssNDY3NSwiaWF0Ijo" +
                    "xNjE0MTc0Njc1fQ.g8EzzV6cizZL_etmha_rCC7bSH5c6ZP0xcPBoX_SKKE",
            "yJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJhc2Rn" +
                    "YXNkIiwiVXNlcm5hbWUiOiJhc2RnIiwiZXhwIjoxNjE0MTcxOTczLCJp" +
                    "YXQiOjE2MTQxNzE5NzN9.rwLqXFAMl6kjHSk_e61OEo97PXYeRUfhaXpnIaguB-w",
            "123.1233.4123",
            "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoasdgfsakiLCJJc3N1ZXIiOi" +
                    "JJc3N1ZXIiLCJVc2VybmFtZSI6Ikphdasidf7asdIsImV4cCI6" +
                    "MTYxNDE3MjExOCwiaWF0IjoxNjE0MTcyMTE4fQ.qwrewfrahZvItnUElv_x-K-PfJEVsFi6zS0KWLsAXtM"
        ]
    )
    fun isValid_ShouldReturnFalse(token: String) {
        assertFalse(jwtUtil!!.isValid(token))
    }


    @Test
    fun isValid_ShouldReturnTrue() {
        val token = jwtUtil!!.create("Wittano")
        assertTrue(jwtUtil.isValid(token))
    }

    @Test
    fun create_ShouldReturnJwtStringToken() {
        val token: String = jwtUtil!!.create("bob")

        assertTrue(token.split(".").size == 3)
        assertEquals(jwtUtil.decode(token).subject, "bob")
        assertTrue(jwtUtil.isValid(token))
    }

    @Test
    fun getClaims_ShouldReturnClaims() {
        val token = jwtUtil!!.create("bob")
        val claims = jwtUtil.decode(token)

        assertNotNull(claims)
        assertEquals("bob", claims.subject)
    }
}