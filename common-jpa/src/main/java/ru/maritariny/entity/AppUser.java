package ru.maritariny.entity;

import lombok.*;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.maritariny.entity.enums.UserState;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // @Entity - указывает, что класс будет сущностью, которая связана с таблице БД
@Table(name = "app_user") // @Table - позволяет задать имя таблицы
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Задается стратегия генерации первичных ключей. IDENTITY = Позволяем БД самой генерировать значения для первичных ключей
    private Long id;
    private Long telegramUserId; // id юзера из телеграма
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Boolean isActive;
    @Enumerated(EnumType.STRING) // аннотация говорит спрингу, как именно будет транслироваться значение енам в БД
    private UserState state;
}
