package com.fransua.gateway.user;

import com.fransua.gateway.user.dto.UserResponse;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserService extends OidcUserService {

  private final UserDao userDao;

  public OAuth2UserService(@Qualifier("jpa") UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);

    System.out.println("!!! OIDC LOAD USER CALLED !!!");

    String id = oidcUser.getAttribute("sub");
    String email = oidcUser.getAttribute("email");
    String name = oidcUser.getAttribute("name");
    String picture = oidcUser.getAttribute("picture");

    User user = userDao.findById(id).orElse(new User());
    user.setId(id);
    user.setEmail(email);
    user.setName(name);
    user.setPicture(picture);
    user.setLastLogin(Instant.now());

    userDao.save(user);

    return oidcUser;
  }

  public UserResponse getUserById(String id) {
    User user =
        userDao
            .findById(id)
            .orElseThrow(
                () ->
                    new AuthenticationCredentialsNotFoundException(
                        "User with id [%s] not found".formatted(id)));
    return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getPicture());
  }
}
