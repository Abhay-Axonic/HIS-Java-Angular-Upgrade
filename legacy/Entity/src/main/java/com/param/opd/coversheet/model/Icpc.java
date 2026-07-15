package com.param.opd.coversheet.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ICPCTemplateMapping;

@Entity
@Table(name="m_icpc",schema="emr")
@SequenceGenerator(name = "icpc_seq", sequenceName = "emr.icpc_seq", allocationSize = 1)
public class Icpc {

	@Id
	@Column(name="icpc_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "icpc_seq")
	private Integer icpcId;
	
	@Column(name="icpc_system_id")
	private Integer icpcSystemId;
	
	@Column(name="icpc_code")
	private String icpcCode;
	
	@Column(name="icpc_desc")
	private String icpcDesc;
	
	@Column(name="status")
	private Character status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "icpc_system_id", insertable = false, nullable = false, updatable = false)
	private IcpcSystemMaster icpcSystemMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "icpcMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ICPCTemplateMapping> ICPCTemplateMappingList;
	
	public Integer getIcpcId() {
		return icpcId;
	}

	public void setIcpcId(Integer icpcId) {
		this.icpcId = icpcId;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}

	public String getIcpcCode() {
		return icpcCode;
	}

	public void setIcpcCode(String icpcCode) {
		this.icpcCode = icpcCode;
	}

	public String getIcpcDesc() {
		return icpcDesc;
	}

	public void setIcpcDesc(String icpcDesc) {
		this.icpcDesc = icpcDesc;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
}


/*CREATE TABLE emr.m_icpc (
		  icpc_id INTEGER DEFAULT nextval('emr.icpc_seq'::regclass) NOT NULL,
		  icpc_system_id INTEGER,
		  icpc_code VARCHAR,
		  icpc_desc VARCHAR,
		  status CHAR(1) DEFAULT 'A'::"char",
		  CONSTRAINT m_icpc_master_id_pkey PRIMARY KEY(icpc_id)
		) */