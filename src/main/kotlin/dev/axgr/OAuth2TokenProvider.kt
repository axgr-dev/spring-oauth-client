package dev.axgr

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.stereotype.Component

@Component
class OAuth2TokenProvider(private val service: OAuth2AuthorizedClientService, val props: YouTubeProperties) : ClientHttpRequestInterceptor {

  override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
    val token = token()

    if (token != null) {
      request.headers.add(HttpHeaders.AUTHORIZATION, "Bearer $token")
      return execution.execute(request, body)
    }

    throw IllegalStateException("No OAuth2 authentication found.")
  }

  private fun token(): String? {
    val client: OAuth2AuthorizedClient? = service.loadAuthorizedClient("google", props.principal)
    return client?.accessToken?.tokenValue
  }
}
