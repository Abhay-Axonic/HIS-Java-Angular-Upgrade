package com.param.fhir.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.fhir.model.FhirData.Name;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NameDeserializer extends JsonDeserializer<Object> {
	 
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
 
        if (node.isArray()) {
            List<Name> names = new ArrayList<>();
            for (JsonNode item : node) {
                names.add(mapper.treeToValue(item, Name.class));
            }
            return names;
        } else {
            return mapper.treeToValue(node, Name.class);
        }
    }
}