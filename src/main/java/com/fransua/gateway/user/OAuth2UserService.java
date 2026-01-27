package com.fransua.gateway.user;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

  private final UserDao userDao;

  public OAuth2UserService(@Qualifier("jpa") UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String id = oAuth2User.getAttribute("sub");
    String email = oAuth2User.getAttribute("email");
    String name = oAuth2User.getAttribute("name");
    String picture = oAuth2User.getAttribute("picture");

    User user = userDao.findById(id).orElse(new User());
    user.setId(id);
    user.setEmail(email);
    user.setName(name);
    user.setPicture(picture);
    user.setLastLogin(Instant.now());

    userDao.save(user);

    return oAuth2User;
  }
}
