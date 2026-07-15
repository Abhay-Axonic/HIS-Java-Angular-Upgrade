package com.param.pharmacy.master.global.dto.frequency;

import java.sql.Timestamp;

public class FrequencyResponseList {

	protected Timestamp administrationTime;

	protected Timestamp preBufferAdministrationTime;

	protected Timestamp postBufferAdministrationTime;

	public Timestamp getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(Timestamp administrationTime) {
		this.administrationTime = administrationTime;
	}

	public Timestamp getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(Timestamp preBufferAdministrationTime) {
		this.preBufferAdministrationTime = preBufferAdministrationTime;
	}

	public Timestamp getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(Timestamp postBufferAdministrationTime) {
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	public FrequencyResponseList(Timestamp administrationTime, Timestamp preBufferAdministrationTime,
			Timestamp postBufferAdministrationTime) {
		super();
		this.administrationTime = administrationTime;
		this.preBufferAdministrationTime = preBufferAdministrationTime;
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}
	
}
