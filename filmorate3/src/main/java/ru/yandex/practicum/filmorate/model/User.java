package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    @NotNull
    @NotBlank
    @Email
    private final String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\s*$")
    private final String login;
    @NotNull
    @JsonFormat
    @PastOrPresent
    private final LocalDate birthday;
    @NotNull(groups = NotNull.class)
    private Integer id;
    private String name;
}
