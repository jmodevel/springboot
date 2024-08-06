package com.jmo.devel.accounting.notes.api.model.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class LocalDateToInstantConverter implements Converter<LocalDate, Instant> {
    @Override
    public Instant convert(MappingContext<LocalDate, Instant> context) {
        LocalDate source = context.getSource();
        return source != null ? source.atStartOfDay( ZoneOffset.UTC ).toInstant() : null;
    }
}
