package com.param.privilege.model;

import java.io.Serializable;

public class Modules implements Serializable {

	private static final long serialVersionUID = 5989644066394330944L;

	String permission;

	public Modules() {

	}

	public Modules(String permission) {
		this.permission = permission;
	}
}
