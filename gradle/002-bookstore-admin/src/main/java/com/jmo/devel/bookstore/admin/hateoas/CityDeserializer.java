package com.jmo.devel.bookstore.admin.hateoas;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CityDeserializer extends JsonDeserializer<City> {

    @Override
    public City deserialize( JsonParser p, DeserializationContext ctxt ) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String content = node.get("content").asText();
        return new City( content );
    }

}