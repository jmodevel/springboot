package com.jmo.devel.accounting.notes.api.model.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class InstantToLocalDateConverter implements Converter<Instant, LocalDate> {
    @Override
    public LocalDate convert(MappingContext<Instant, LocalDate> context) {
        Instant source = context.getSource();
        return source != null ? source.atOffset( ZoneOffset.UTC ).toLocalDate() : null;
    }
}
