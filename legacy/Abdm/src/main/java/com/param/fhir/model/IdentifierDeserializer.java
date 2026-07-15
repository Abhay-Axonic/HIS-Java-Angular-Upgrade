package com.param.fhir.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.fhir.model.FhirData.Identifier;

public class IdentifierDeserializer extends JsonDeserializer<Object> {
	 
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) 
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
 
        if (node.isArray()) {
            List<Identifier> identifiers = new ArrayList<>();
            for (JsonNode item : node) {
                identifiers.add(mapper.treeToValue(item, Identifier.class));
            }
            return identifiers;
        } else {
            return mapper.treeToValue(node, Identifier.class);
        }
    }
}