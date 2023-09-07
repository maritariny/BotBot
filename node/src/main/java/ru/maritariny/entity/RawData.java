package ru.maritariny.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.*;

// В этот класс будут сохраняться все входящие update,
// им будет присваиваться уникальный айди (первичный ключ)

// @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor - аннотации lombok
// Позволяют сгенерировать геттеры, сеттеры. Внутренний клас Builder для удобного создания объекта класса (паттерн)
// Аннотации с args позволяют сгенерировать конструкторы по умолчанию (без параметров и со всеми параметрами)
//@Data
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // @Entity - указывает, что класс будет сущностью, которая связана с таблице БД
@Table(name = "raw_data") // @Table - позволяет задать имя таблицы
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) // Для возможности работы с полями с типом jsonb
public class RawData {
    // Первичный ключ обязательно типа long
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Задается стратегия генерации первичных ключей. IDENTITY = Позволяем БД самой генерировать значения для первичных ключей
    private Long id;
    @Type(type="jsonb") // jsonb - фича postrgesql. Оптимизированная двоичная разновидность json (удаляются пробелы, нарушается порядок, удаляются ключи-дубликаты)
    // В базе хранятся не в виде строки, а в виде полноценного json, по которому можно навигироваться с помощью спец. запросов
    @Column(columnDefinition = "jsonb")
    private Update event;

}
