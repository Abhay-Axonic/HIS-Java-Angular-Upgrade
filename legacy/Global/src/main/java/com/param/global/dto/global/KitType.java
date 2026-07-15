package com.param.global.dto.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class KitType {
private int id;
private String description;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}

public KitType(Integer id, String description) {
	super();
	this.id = id;
	this.description = description;
}
}
