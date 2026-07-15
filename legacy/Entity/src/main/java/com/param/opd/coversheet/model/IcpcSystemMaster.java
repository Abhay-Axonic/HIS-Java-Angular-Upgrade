package com.param.opd.coversheet.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ICPCTemplateMapping;
import com.param.global.model.UserRoleMapper;

@NamedQueries({
	
	@NamedQuery(name="GET_ICPC_SYSTEM_NAME_BY_SYSTEM_ID",
			query="SELECT icpcSystemId AS icpcSystemId, "
					 + "icpcSystemDesc AS systemName "
					 + "FROM IcpcSystemMaster "
					 + "WHERE icpcSystemId= :icpcSystemId ")
})


@Entity
@Table(name="m_icpc_system",schema="emr")
@SequenceGenerator(name = "icpc_system_master_seq", sequenceName = "emr.icpc_system_master_seq", allocationSize = 1)
public class IcpcSystemMaster {

	@Id
	@Column(name="icpc_system_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "icpc_system_master_seq")
	private Integer icpcSystemId;
	
	@Column(name="icpc_system_code")
	private String icpcSystemCode;
	
	@Column(name="icpc_system_desc")
	private String icpcSystemDesc;
	
	@Column(name="status")
	private Character status;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "icpcSystemMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ICPCTemplateMapping> ICPCTemplateMappingList;

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
	}

	public String getIcpcSystemCode() {
		return icpcSystemCode;
	}

	public void setIcpcSystemCode(String icpcSystemCode) {
		this.icpcSystemCode = icpcSystemCode;
	}

	public String getIcpcSystemDesc() {
		return icpcSystemDesc;
	}

	public void setIcpcSystemDesc(String icpcSystemDesc) {
		this.icpcSystemDesc = icpcSystemDesc;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
}