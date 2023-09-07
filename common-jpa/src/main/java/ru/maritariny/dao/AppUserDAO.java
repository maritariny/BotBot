package ru.maritariny.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maritariny.entity.AppUser;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(Long id); // Реализацию метода берет на себя Spring
}
