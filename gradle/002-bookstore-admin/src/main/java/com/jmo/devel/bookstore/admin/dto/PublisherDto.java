package com.jmo.devel.bookstore.admin.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PublisherDto {

    private Long          id;
    private String        name;
    private String        headquarters;
    private String        website;
    private List<BookDto> books;

}
