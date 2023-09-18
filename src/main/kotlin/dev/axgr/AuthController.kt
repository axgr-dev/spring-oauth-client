package dev.axgr

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {

  @GetMapping("/me")
  fun me(): String {
    val auth = SecurityContextHolder
      .getContext()
      .authentication as? OAuth2AuthenticationToken

    return auth?.name ?: "anonymous"
  }
}
