package com.param;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.billing.global.transaction.model.InsuranceMaster;
import com.param.global.model.AreaMaster;
import com.param.global.model.DistrictMaster;
import com.param.global.model.StateMaster;

@Entity
@Table(name = "m_url_master", schema = "abdm")
public class AbdmUrlMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_url_master_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "event_id")
	private Integer eventId;

	@Column(name = "base_url")
	private String baseUrl;

	@Column(name = "base_path")
	private String basePath;

	@Column(name = "api_url")
	private String apiUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	
	
	

	
}
