package ru.yandex.practicum.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String name;
    @NonNull
    private String login;
    @NonNull
    private LocalDate birthday;
    private Set<Long> friends;
}
