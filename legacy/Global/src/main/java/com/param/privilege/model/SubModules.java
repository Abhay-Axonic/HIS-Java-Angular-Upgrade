package com.param.privilege.model;

import java.io.Serial;
import java.io.Serializable;

public class SubModules implements Serializable {

	@Serial
	private static final long serialVersionUID = 3603409819181147379L;
	String parent;
	String permission;

	public SubModules() {

	}

	public SubModules(String parent, String permission) {
		this.parent = parent;
		this.permission = permission;
	}

}
