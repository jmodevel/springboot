package com.jmo.devel.bookstore.admin.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookDto {

    private Long         id;
    private String       title;
    private String       isbn;
    private int          pages;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate    published;
    private PublisherDto publisher;
    private AuthorDto    author;

}
