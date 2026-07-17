package com.param.fhir.model;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class IdentifierSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof String string) {
            gen.writeString(string);
        } else if (value instanceof List) {
            gen.writeStartArray();
            for (String identifier : (List<String>) value) {
                gen.writeString(identifier);
            }
            gen.writeEndArray();
        }
    }
}