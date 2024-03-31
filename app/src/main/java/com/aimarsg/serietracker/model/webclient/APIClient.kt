package com.aimarsg.serietracker.model.webclient

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * API client for the app
 * It connects with the API to get the data and authenticate the users
 */


// Custom exceptions definition
class AuthenticationException : Exception()
class UserExistsException : Exception()

// Data class that represents a user to send the register request
@Serializable
data class User(val username: String, val hashed_password: String)
//Data class that represents server response when an [accessToken] is request.
@Serializable
internal data class TokenInfo(
    @SerialName("access_token") val accessToken: String,
    //@SerialName("expires_in") val expiresIn: Int,
    //@SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String,
)

internal val bearerTokenStorage = mutableListOf<BearerTokens>(BearerTokens("", ""))

// Singleton class to create the API client
@Singleton
class APIClient @Inject constructor() {
    private val httpClient = HttpClient(CIO) {

        // If return code is not a 2xx then throw an exception
        expectSuccess = true

        // Install JSON handler (allows to receive and send JSON data)
        install(ContentNegotiation) { json(
            Json {
                prettyPrint = true
                isLenient = true
            }
        ) }

        // Handle non 2xx status responses
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when {
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.Unauthorized -> throw AuthenticationException()
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.Conflict -> throw UserExistsException()
                    else -> {
                        exception.printStackTrace()
                        throw exception
                    }
                }
            }
        }
        install(Auth) {
            bearer {

                loadTokens { bearerTokenStorage.last() }

                // Send always the token, do not  wait for a 401 before adding the token to the header
                sendWithoutRequest { request -> request.url.host == "35.246.246.159" }

            }
        }
    }

    /**
     * Method to authenticate a user
     * @param user: user name
     * @param password: user password
     * @throws AuthenticationException if the user is not authenticated
     */
    @Throws(AuthenticationException::class, Exception::class)
    suspend fun authenticate(user: String, password: String) {
        val tokenInfo: TokenInfo = httpClient.submitForm(
            url = "http://35.246.246.159:8000/token/",
            formParameters = Parameters.build {
                //append("grant_type", "password")
                append("username", user)
                append("password", password)
            }).body()
        bearerTokenStorage.add(BearerTokens(tokenInfo.accessToken, ""))
    }

    /**
     * Method to register a new user
     * @param user: user name
     * @param password: user password
     * @throws UserExistsException if the user already exists
     */
    @Throws(UserExistsException::class, Exception::class)
    suspend fun register(user: String, password: String) {
        /*httpClient.submitForm(
            url = "http://35.246.246.159:8000/users/",
            formParameters = Parameters.build {
                append("username", user)
                append("hashed_password", password)
            }
            /*,
            block = {
                headers {
                    append("Content-Type", "application/json") // Specify content type
                }
            }*/
        )*/

        httpClient.post("http://35.246.246.159:8000/users/") {
            contentType(ContentType.Application.Json)
            setBody(User(user, password))
        }
    }

    // Methods to access the data

    // Firebase

}

