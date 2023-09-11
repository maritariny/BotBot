package ru.maritariny.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maritariny.entity.AppUser;

import java.util.Optional;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByTelegramUserId(Long id); // Реализацию метода берет на себя Spring
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByEmail(String email);
}
