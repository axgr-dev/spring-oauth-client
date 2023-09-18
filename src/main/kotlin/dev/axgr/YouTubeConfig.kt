package dev.axgr

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.web.client.RestClient

@ConfigurationProperties("youtube")
data class YouTubeProperties(val url: String, val video: String, val title: String, val principal: String)

@Configuration
@EnableConfigurationProperties(YouTubeProperties::class)
class YouTubeConfig(private val props: YouTubeProperties) {

  @Bean
  fun client(builder: RestClient.Builder, interceptor: OAuth2TokenProvider): RestClient {
    return builder
      .requestInterceptor(interceptor)
      .baseUrl(props.url)
      .build()
  }

  @Bean
  fun manager(clientRegistrationRepository: ClientRegistrationRepository, authorizedClientRepository: OAuth2AuthorizedClientRepository): OAuth2AuthorizedClientManager {
    val provider = OAuth2AuthorizedClientProviderBuilder.builder()
      .clientCredentials()
      .build()

    val manager = DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository)
    manager.setAuthorizedClientProvider(provider)

    return manager
  }
}
