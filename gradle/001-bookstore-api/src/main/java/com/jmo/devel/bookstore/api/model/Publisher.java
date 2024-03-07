package com.jmo.devel.bookstore.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "publisher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   id;

    @Column(unique=true)
    private String name;
    private String headquarters;
    private String website;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<Book> books;

}
