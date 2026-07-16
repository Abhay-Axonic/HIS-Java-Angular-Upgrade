package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.opd.coversheet.model.InfusionSolutionMaster;
import com.param.opd.coversheet.model.IpdRylesTubeMaster;
import com.param.opd.coversheet.model.NursingOrderMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Frequency")
@Table(name="m_frequency",schema="public")
public class Frequency extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String code;

	@Column(name="description")
	private String description;

	@Column(name="no_of_administration_time")
	private Integer noOfAdministrationTime;

	@OneToMany(fetch = FetchType.LAZY,mappedBy="frequency",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FrequencyDetail> frequencyDetailList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy="frequencyMaster",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InfusionSolutionMaster> frequencyMasterList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "frequencyMaster")
	private List<IpdRylesTubeMaster> listfrequencyMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "frequencyMaster")
	private List<NursingOrderMapper> listNursingOrderMapper;

	public Frequency() {
		super();
	}
	
	public Frequency(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getNoOfAdministrationTime() {
		return this.noOfAdministrationTime;
	}

	public void setNoOfAdministrationTime(Integer noOfAdministrationTime) {
		this.noOfAdministrationTime = noOfAdministrationTime;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Frequency(String code, String description, Integer noOfAdministrationTime) {
		super();
		this.code = code;
		this.description = description;
		this.noOfAdministrationTime = noOfAdministrationTime;
	}
	
	public void updateFrequency(String code, String description, Integer noOfAdministrationTime, Boolean isActive) {
		this.code = code;
		this.description = description;
		this.noOfAdministrationTime = noOfAdministrationTime;
		this.isActive = isActive;
	}

	public List<FrequencyDetail> getFrequencyDetailList() {
		return frequencyDetailList;
	}

	public void setFrequencyDetailsList(List<FrequencyDetail> frequencyDetailsList) {
		this.frequencyDetailList = frequencyDetailsList;
	}

	public FrequencyDetail addFrequencyDetailList(FrequencyDetail frequencyDetail) {
		if (getFrequencyDetailList() == null)
			this.frequencyDetailList = new ArrayList<FrequencyDetail>();
		
		getFrequencyDetailList().add(frequencyDetail);
		frequencyDetail.setMFrequency(this);

		return frequencyDetail;
	}

	public FrequencyDetail removeFrequencyDetail(FrequencyDetail frequencyDetail) {
		getFrequencyDetailList().remove(frequencyDetail);
		frequencyDetail.setMFrequency(this);

		return frequencyDetail;
	}
	

}