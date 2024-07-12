package com.l0mtick.mgkcttimetable.data.remote.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun getHttpClient(engine: HttpClientEngine, token: String): HttpClient {
    return HttpClient(engine) {
        defaultRequest {
            url("https://mgke.keller.by/api/")
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            bearerAuth(token)
        }
        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        install(Auth) {
            bearer {
                BearerTokens(token, token)
            }
        }
    }
}