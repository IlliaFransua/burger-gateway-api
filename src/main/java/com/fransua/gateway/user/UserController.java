package com.fransua.gateway.user;

import com.fransua.gateway.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final OAuth2UserService oAuth2UserService;

  @GetMapping("/profile")
  public UserResponse getProfile(@AuthenticationPrincipal OAuth2User oAuth2User) {
    if (oAuth2User == null) {
      throw new AuthenticationCredentialsNotFoundException("User not authenticated");
    }

    String id = oAuth2User.getAttribute("sub");
    return oAuth2UserService.getUserById(id);
  }
}
