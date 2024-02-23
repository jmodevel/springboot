package com.jmo.devel.tv.shows.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class ShowDto {

    private int      id;
    private String   url;
    private String   name;
    private String   type;
    private String   language;
    private String[] genres;
    private String   status;
    private int      runtime;
    private Date     premiered;
    private String   officialSite;

}
