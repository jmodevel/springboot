package com.jmo.devel.bookstore.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "author")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long      id;

    private String    name;
    private String    surnames;
    private LocalDate birthDate;
    private LocalDate death;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books;

}
