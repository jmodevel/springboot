package com.jmo.devel.tv.shows.model.converters;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@WritingConverter
public class DateToStringConverter implements Converter<Date, String> {

	private SimpleDateFormat sdf;
	
	public DateToStringConverter() {
		log.debug("Building DateToStringConverter");
		this.sdf = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public String convert(Date source) {
		return sdf.format("yyyy-MM-dd");
	}
}
