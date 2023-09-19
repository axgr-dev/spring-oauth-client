package dev.axgr

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
}
