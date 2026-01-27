package com.fransua.gateway.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository("jpa")
@RequiredArgsConstructor
public class UserJPADataAccessService implements UserDao {

  private final UserRepository userRepository;

  @Override
  public Optional<User> findById(String id) {
    return userRepository.findById(id);
  }

  @Override
  public void save(User user) {
    userRepository.save(user);
  }
}
