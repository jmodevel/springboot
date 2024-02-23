package com.jmo.devel.tv.shows.model;

import java.util.Date;

import com.jmo.devel.tv.shows.model.converters.DateToStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document( "tvShows" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {

	@Id
	private String identifier;

	private int id;
	private String url;
	private String name;
	private String type;
	private String language;
	private String[] genres;
	private String status;
	private int runtime;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Convert(converter = DateToStringConverter.class)
	private Date premiered;
	private String officialSite;
	
}
