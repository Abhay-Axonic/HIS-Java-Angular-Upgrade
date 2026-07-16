package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.asset.ItemAuctionDetail;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.pharmacy.DrugAdministrationDetail;
import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@Entity
@Table(name = "m_frequency_detail")
@NamedQuery(name = "FrequencyDetail.findAll", query = "SELECT f FROM FrequencyDetail f")
public class FrequencyDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="administration_time")
	@Convert(converter = InstantConverter.class)
	private Instant administrationTime;

	@Column(name="post_buffer_administration_time")
	@Convert(converter = InstantConverter.class)
	private Instant postBufferAdministrationTime;

	@Column(name="pre_buffer_administration_time")
	@Convert(converter = InstantConverter.class)
	private Instant preBufferAdministrationTime;
	
	@Column(name="slot", nullable= true)
	private String slot;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="frequency_id")
	private Frequency frequency;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "frequencyDetail", cascade = CascadeType.ALL)
	private List<DrugAdministrationDetail> drugAdministrationDetail;

	public FrequencyDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Instant getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(Instant administrationTime) {
		this.administrationTime = administrationTime;
	}

	public Instant getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(Instant postBufferAdministrationTime) {
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	public Instant getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(Instant preBufferAdministrationTime) {
		this.preBufferAdministrationTime = preBufferAdministrationTime;
	}

	public String getSlot() {
		return this.slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public Frequency getMFrequency() {
		return this.frequency;
	}

	public void setMFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public Frequency getFrequency() {
		return frequency;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public List<DrugAdministrationDetail> getDrugAdministrationDetail() {
		return drugAdministrationDetail;
	}

	public void setDrugAdministrationDetail(List<DrugAdministrationDetail> drugAdministrationDetail) {
		this.drugAdministrationDetail = drugAdministrationDetail;
	}

	public FrequencyDetail(Instant administrationTime, Instant postBufferAdministrationTime, Instant preBufferAdministrationTime) {
		super();
		this.administrationTime = administrationTime;
		this.postBufferAdministrationTime = postBufferAdministrationTime;
		this.preBufferAdministrationTime = preBufferAdministrationTime;
		this.slot = "";
	}

	public FrequencyDetail(Integer id) {
		super();
		this.id = id;
	}

	public void updateFrequencyDetail(Instant administrationTime, Instant postBufferAdministrationTime, Instant preBufferAdministrationTime) {
		this.administrationTime = administrationTime;
		this.postBufferAdministrationTime = postBufferAdministrationTime;
		this.preBufferAdministrationTime = preBufferAdministrationTime;
		this.slot = "";
	}
	
}