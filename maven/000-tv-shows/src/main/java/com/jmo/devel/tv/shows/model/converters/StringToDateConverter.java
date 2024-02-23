package com.jmo.devel.tv.shows.model.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ReadingConverter
public class StringToDateConverter implements Converter<String, Date> {

	private SimpleDateFormat sdf;
	
	public StringToDateConverter() {
		this.sdf = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public Date convert(String source) {
		Date date = null;
    	try {
			date = sdf.parse(source);
		} catch (ParseException e) {
			log.error( "error converting source: " + source );
		}
    	return date;
	}
}