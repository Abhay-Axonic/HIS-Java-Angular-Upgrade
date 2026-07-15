package com.param.pharmacy.master.global.dto.frequency;

import java.sql.Timestamp;
import java.time.*;
import java.util.Date;

import com.param.global.common.GlobalCommonDateUtils;

public class FrequencyDetailRabbitDto {

	private Integer id;
	
	private Instant administration_time;
	
	private Instant post_buffer_administration_time;
	
	private Instant pre_buffer_administration_time;
	
	private Integer frequency_id;
	
	private Integer organization_id;

	private Integer unit_id;
	
	private Integer updated_by;
	
	private Date updated_date;
	
	private Boolean is_active;
	
	private Boolean is_deleted;
	
	private Integer added_by;
	
	private Date added_date;
	
	public FrequencyDetailRabbitDto() {
		super();
	}
	
	public FrequencyDetailRabbitDto(Integer id, Instant administration_time,
									Instant post_buffer_administration_time, Instant pre_buffer_administration_time, Integer frequency_id,
			Integer organization_id, Integer unit_id, Integer added_by, Date added_date, Integer updated_by, Date updated_date,
			Boolean is_active, Boolean is_deleted) {
		super();
		this.id = id;
		this.administration_time = administration_time;
		this.post_buffer_administration_time = post_buffer_administration_time;
		this.pre_buffer_administration_time = pre_buffer_administration_time;
		this.frequency_id = frequency_id;
		this.organization_id = organization_id;
		this.unit_id = unit_id;
		this.updated_by = updated_by;
		this.updated_date = updated_date;
		this.is_active = is_active;
		this.is_deleted = is_deleted;
		this.added_by = added_by;
		this.added_date = added_date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Instant getAdministration_time() {
		return administration_time;
	}

	
	public void setAdministration_time(String administration_time) {
		if (administration_time!=null && !administration_time.equals(""))
		{
			LocalTime time = LocalTime.parse(administration_time);
			LocalDate today = LocalDate.now();
			this.administration_time = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.administration_time = midnight.toInstant(ZoneOffset.UTC);

		}
	}
	

	public Instant getPre_buffer_administration_time() {
		return pre_buffer_administration_time;
	}

	public void setPre_buffer_administration_time(String pre_buffer_administration_time) {
		if (pre_buffer_administration_time!=null && !pre_buffer_administration_time.equals(""))
		{
			LocalTime time = LocalTime.parse(pre_buffer_administration_time);
			LocalDate today = LocalDate.now();
			this.pre_buffer_administration_time = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.pre_buffer_administration_time = midnight.toInstant(ZoneOffset.UTC);

		}
	}
	

	public Instant getPost_buffer_administration_time() {
		return post_buffer_administration_time;
	}

	public void setPost_buffer_administration_time(String post_buffer_administration_time) {
		if (post_buffer_administration_time!=null && !post_buffer_administration_time.equals(""))
		{
			LocalTime time = LocalTime.parse(post_buffer_administration_time);
			LocalDate today = LocalDate.now();
			this.post_buffer_administration_time = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.post_buffer_administration_time = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public Integer getFrequency_id() {
		return frequency_id;
	}

	public void setFrequency_id(Integer frequency_id) {
		this.frequency_id = frequency_id;
	}

	public Integer getOrganization_id() {
		return organization_id;
	}

	public void setOrganization_id(Integer organization_id) {
		this.organization_id = organization_id;
	}

	public Integer getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	public Integer getAdded_by() {
		return added_by;
	}

	public void setAdded_by(Integer added_by) {
		this.added_by = added_by;
	}

	public Date getAdded_date() {
		return added_date;
	}

	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
	}
	
}
