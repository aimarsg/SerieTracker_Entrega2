package com.aimarsg.serietracker.model.webclient

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.aimarsg.serietracker.model.entities.SerieCatalogo
import com.aimarsg.serietracker.model.entities.SerieUsuario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * API client for the app
 * It connects with the API to get the data and authenticate the users
 */


// Custom exceptions definition
class AuthenticationException : Exception()
class UserExistsException : Exception()
class NotFoundException : Exception()

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

@Serializable
data class Marcador(
    @SerialName("nombre") val nombre: String,
    @SerialName("latitud") val latitud: Double,
    @SerialName("longitud") val longitud: Double,
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
                    exception is ClientRequestException && exception.response.status == HttpStatusCode.NotFound  -> throw NotFoundException()
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
                sendWithoutRequest { request -> request.url.host == "http://35.246.246.159:8000/" }

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
        Log.d("Token oauth", tokenInfo.accessToken)
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
        httpClient.post("http://35.246.246.159:8000/users/") {
            contentType(ContentType.Application.Json)
            setBody(User(user, password))
        }
    }

    // Methods to access the data
    /** Method to get the locations from the API
     * @return a list of locations
     */
    suspend fun getMarcadores(): List<Marcador> {
        val response: List<Marcador> =
            httpClient.get("http://35.246.246.159:8000/marcadores/").body()
        return response
    }

    // Methods to manage user profile picture

    /**
     * Method to get the user's profile picture
     * @return the user's profile picture as a Bitmap
     * @throws NotFoundException if the user's profile picture is not found
     */
    @Throws(NotFoundException::class, Exception::class)
    suspend fun getFotoDePerfil(): Bitmap {
        Log.d("Token oauth", bearerTokenStorage.last().accessToken)
        val response = httpClient.get("http://35.246.246.159:8000/users/obtenerFoto/"){
            bearerAuth(bearerTokenStorage.last().accessToken)
        }
        val image: ByteArray = response.body()
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }


    /**
     * Method to upload the user's profile picture
     * @param foto: the user's profile picture as a Bitmap
     */
    suspend fun subirFotoDePerfil(foto: File) {

        httpClient.post("http://35.246.246.159:8000/users/subirFoto/") {
            setBody(
                MultiPartFormDataContent(
                formData {
                    append("profile_pic", foto.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=\"profilepic.png\"")
                    })
                }
            )
            )
            bearerAuth(bearerTokenStorage.last().accessToken)
        }
    }

    // Firebase
    /**
     * Method to subscribe a user to the FCM service
     * @param token: the user's FCM token (string)
     */
    suspend fun suscribeUser(token: String) {
        httpClient.post("http://35.246.246.159:8000/suscribir_dispositivo/") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("fcm_client_token" to token))
            bearerAuth(bearerTokenStorage.last().accessToken)
        }
    }

    // Methods to manage users data and synchronize with the API
    /**
     * Method to upload user's local data to the API
     * @param series: list of series to upload
     */
    @Throws(AuthenticationException::class, Exception::class)
    suspend fun uploadUserData(series: List<SerieUsuario>){
        Log.d("Token oauth", bearerTokenStorage.last().accessToken)
        httpClient.post("http://35.246.246.159:8000/users/misSeries/sincronizar/"){
            contentType(ContentType.Application.Json)
            setBody(mapOf("seriesUsuario" to series))
            bearerAuth(bearerTokenStorage.last().accessToken)
        }
    }

    /**
     * Method to download user's data from the API
     * @return a list of SeriesUsuario
     */
    @Throws(AuthenticationException::class, Exception::class)
    suspend fun downloadUserData(): List<SerieUsuario> = httpClient.get("http://35.246.246.159:8000/users/misSeries/"){
        bearerAuth(bearerTokenStorage.last().accessToken)
    }.body()

    /**
     * Method to download the catalogue from the API
     * @return a list of SeriesCatalogo
     */
    @Throws(Exception::class)
    suspend fun downloadCatalogue(): List<SerieCatalogo> = httpClient.get("http://35.246.246.159:8000/series_catalogo/").body()

}

