package com.wutsi.security.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.security.dto.VerifyPasswordRequest
import com.wutsi.security.error.ErrorURN
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/VerifyPasswordController.sql"])
class VerifyPasswordControllerTest {
    @LocalServerPort
    val port: Int = 0

    protected val rest = RestTemplate()

    @Test
    fun verify() {
        // WHEN
        val request = VerifyPasswordRequest(
            value = "123"
        )
        val response = rest.postForEntity(url(100), request, Any::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun mismatch() {
        // WHEN
        val request = VerifyPasswordRequest(
            value = "this is an invalid password"
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(100), request, Any::class.java)
        }

        // THEN
        assertEquals(HttpStatus.CONFLICT, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PASSWORD_MISMATCH.urn, response.error.code)
    }

    private fun url(id: Long) = "http://localhost:$port/v1/passwords/$id/verify"
}
