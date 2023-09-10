package ru.maritariny.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maritariny.entity.AppPhoto;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}
