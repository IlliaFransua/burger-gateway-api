package com.fransua.gateway.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {@UniqueConstraint(name = "user_email_unique", columnNames = "email")})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

  @Id @EqualsAndHashCode.Include private String id;

  @Column(nullable = false)
  private String email;

  private String name;
  private String picture;
  private Instant lastLogin;
}
