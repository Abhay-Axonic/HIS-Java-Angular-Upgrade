package com.param.pharmacy.master.global.dto.frequency;

import java.sql.Timestamp;
import java.time.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.common.GlobalCommonDateUtils;

public class FrequencyRequestItem {

	@JsonInclude(Include.NON_NULL)
	protected Integer id;
	
//	protected Integer slot;

	protected Instant administrationTime;

	protected Instant preBufferAdministrationTime;

	protected Instant postBufferAdministrationTime;
	
	protected String administrationTimeString;

	protected String preBufferAdministrationTimeString;

	protected String postBufferAdministrationTimeString;
	

	public FrequencyRequestItem() {
		super();
		
	}
	
	public FrequencyRequestItem(Integer id, Instant administrationTime,
								Instant preBufferAdministrationTime, Instant postBufferAdministrationTime) {
		super();
		this.id = id;
//		this.slot = slot;
		this.administrationTime = administrationTime;
		this.preBufferAdministrationTime = preBufferAdministrationTime;
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

/*
	public Integer getSlot() {
		return slot;
	}


	public void setSlot(Integer slot) {
		this.slot = slot;
	}
*/

	public Instant getAdministrationTime() {
		return administrationTime;
	}

	
	public void setAdministrationTime(String administrationTime) {
		if (administrationTime!=null && !administrationTime.equals(""))
		{
			LocalTime time = LocalTime.parse(administrationTime);
			LocalDate today = LocalDate.now();
			this.administrationTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.administrationTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}
	

	public Instant getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(String preBufferAdministrationTime) {
		if (preBufferAdministrationTime!=null && !preBufferAdministrationTime.equals(""))
		{
			LocalTime time = LocalTime.parse(preBufferAdministrationTime);
			LocalDate today = LocalDate.now();
			this.preBufferAdministrationTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.preBufferAdministrationTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}
	

	public Instant getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(String postBufferAdministrationTime) {
		if (postBufferAdministrationTime!=null && !postBufferAdministrationTime.equals(""))
		{
			LocalTime time = LocalTime.parse(postBufferAdministrationTime);
			LocalDate today = LocalDate.now();
			this.postBufferAdministrationTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.postBufferAdministrationTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public String getAdministrationTimeString() {
		return administrationTimeString;
	}

	public void setAdministrationTimeString(String administrationTimeString) {
		this.administrationTimeString = administrationTimeString;
	}

	public String getPreBufferAdministrationTimeString() {
		return preBufferAdministrationTimeString;
	}

	public void setPreBufferAdministrationTimeString(String preBufferAdministrationTimeString) {
		this.preBufferAdministrationTimeString = preBufferAdministrationTimeString;
	}

	public String getPostBufferAdministrationTimeString() {
		return postBufferAdministrationTimeString;
	}

	public void setPostBufferAdministrationTimeString(String postBufferAdministrationTimeString) {
		this.postBufferAdministrationTimeString = postBufferAdministrationTimeString;
	}
	
	
}

