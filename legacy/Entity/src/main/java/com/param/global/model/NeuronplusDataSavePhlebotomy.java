package com.param.global.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.Admission;
import com.param.global.common.DateConverter;
import com.param.opd.coversheet.model.EncounterMaster;

@Entity
@Table(name = "m_neuronplus_data_save_phlebotomy", schema = "public")
@SequenceGenerator(name = "m_neuronplus_data_save_phlebotomy_seq", sequenceName = "public.m_neuronplus_data_save_phlebotomy_seq", allocationSize = 1)

public class NeuronplusDataSavePhlebotomy {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_neuronplus_data_save_phlebotomy_seq")
	private int id;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer orgUnitId;

	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "neuron_plus_url")
	private String neuronPlusUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getNeuronPlusUrl() {
		return neuronPlusUrl;
	}

	public void setNeuronPlusUrl(String neuronPlusUrl) {
		this.neuronPlusUrl = neuronPlusUrl;
	}
	
}
