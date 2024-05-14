package com.jmo.devel.bookstore.admin.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthorDto {

    private Long          id;
    private String        name;
    private String        surnames;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate     birthDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate     death;
    private List<BookDto> books;

}
