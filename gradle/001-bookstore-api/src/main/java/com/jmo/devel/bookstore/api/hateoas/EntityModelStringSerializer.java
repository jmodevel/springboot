package com.jmo.devel.bookstore.api.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.hateoas.EntityModel;

import java.io.IOException;

public class EntityModelStringSerializer extends JsonSerializer<EntityModel<String>> {

    @Override
    public void serialize( EntityModel<String> value, JsonGenerator gen, SerializerProvider serializers ) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("content", value.getContent());
        gen.writeObjectField("_links", value.getLinks());
        gen.writeEndObject();
    }

}
