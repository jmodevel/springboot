package com.jmo.devel.bookstore.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long      id;

    @Column(unique = true)
    private String    title;
    private String    isbn;
    private int       pages;
    private LocalDate published;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher")
    private Publisher publisher;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author")
    private Author    author;

}
