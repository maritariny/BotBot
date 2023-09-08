package ru.maritariny.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maritariny.entity.BinaryContent;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {

}
