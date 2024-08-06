package com.jmo.devel.accounting.notes.api.configuration;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.spring.core.DefaultGcpProjectIdProvider;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import com.jmo.devel.accounting.notes.api.model.converter.InstantToLocalDateConverter;
import com.jmo.devel.accounting.notes.api.model.converter.LocalDateToInstantConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AccountingNotesConfiguration {

    @Bean
    public ModelMapper getModelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter( new LocalDateToInstantConverter() );
        mapper.addConverter( new InstantToLocalDateConverter() );
        return mapper;
    }

    @Bean
    public GcpProjectIdProvider gcpProjectIdProvider() {
        return new DefaultGcpProjectIdProvider() {
            @Override
            public String getProjectId() {
                return "accounting-notes";
            }
        };
    }

    @Bean
    public CredentialsProvider credentialsProvider(){
        return new CredentialsProvider() {
            @Override
            public Credentials getCredentials() throws IOException {
                return null;
            }
        };

    }

}