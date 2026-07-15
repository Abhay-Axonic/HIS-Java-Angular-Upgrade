package com.param.fhir.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.param.fhir.model.FhirData.Name;

import java.io.IOException;
import java.util.List;

public class NameOrStringSerializer extends JsonSerializer<Object> {

    @Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		/*
		 * if (value instanceof List) { // Serialize as JSON array if it's a List<Name>
		 * List<Name> nameList = (List<Name>) value; ArrayNode arrayNode = (ArrayNode)
		 * gen.getCodec().createArrayNode(); for (Name name : nameList) { if (name !=
		 * null) { ObjectNode node = (ObjectNode) gen.getCodec().createObjectNode();
		 * node.put("name", getFirstName()); node.put("name", getLastName());
		 * arrayNode.add(node); } else { arrayNode.addNull(); } }
		 * gen.writeObject(arrayNode); } else if (value instanceof String) { //
		 * Serialize as JSON string if it's a String gen.writeString((String) value); }
		 * else { throw new IOException("Unsupported type for serialization"); }
		 */}
}
