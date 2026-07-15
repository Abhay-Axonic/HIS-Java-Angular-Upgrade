package com.param.pharmacy.master.global.dto.frequency;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrequencyRabbitDto {

	private Integer id;
	
	private String code;

	private String description;

	private Integer no_of_administration_time;
	
	private Integer organization_id;
	
	private Integer unit_id;
	
	private Integer updated_by;
	
	private Date updated_date;
	
	private Boolean is_active;
	
	private Boolean is_deleted;
	
	private Integer added_by;
	
	private Date added_date;
	
	List<FrequencyDetailRabbitDto> frequencyDetailList;
	
	public FrequencyRabbitDto() {
		super();
	}
	
	public FrequencyRabbitDto(Integer id, String code, String description, Integer no_of_administration_time,
			Integer organization_id, Integer unit_id, Integer added_by, Date added_date,
			Integer updated_by, Date updated_date, Boolean is_active, Boolean is_deleted) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
		this.no_of_administration_time = no_of_administration_time;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNo_of_administration_time() {
		return no_of_administration_time;
	}

	public void setNo_of_administration_time(Integer no_of_administration_time) {
		this.no_of_administration_time = no_of_administration_time;
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

	public List<FrequencyDetailRabbitDto> getFrequencyDetailList() {
		return frequencyDetailList;
	}

	public void setFrequencyDetailList(List<FrequencyDetailRabbitDto> frequencyDetailList) {
		this.frequencyDetailList = frequencyDetailList;
	}
	
	public void addFrequencyDetails(FrequencyDetailRabbitDto frequencyDetailRabbitDto) {
		if (this.frequencyDetailList == null)
			this.frequencyDetailList = new ArrayList<FrequencyDetailRabbitDto>();

		this.frequencyDetailList.add(frequencyDetailRabbitDto);
	}
	
}
