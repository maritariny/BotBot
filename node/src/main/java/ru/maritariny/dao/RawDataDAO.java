package ru.maritariny.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.maritariny.entity.RawData;

// Наследуется от стандартного spring интерфейса, в качестве параметров (дженерики) указывается
// тип сущности и тип первичного ключа, который используется в нашей сущности
// JpaRepository - механизм спринга, который предоставляет из коробки набор стандартных методов для работы с БД (save, findById)
// + предоставляется удобный способ создания собственных методов (например findByИмяКолонкиAndИмяДругойКолонки - поймет даже без реализации)
public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
