package com.fransua.gateway.user;

import java.util.Optional;

public interface UserDao {

  Optional<User> findById(String id);

  void save(User user);
}
