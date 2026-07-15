package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	@NamedQuery(name="GET_NABH_PROCESS_MASTER_LIST",
			query="select" + 
					"	nabhprocRept.nabhProcessId as nabhProcessId," + 
					"	nabhprocRept.nabhProcessCode as nabhProcessCode," + 
					"	nabhprocRept.nabhProcessDesc as nabhProcessDesc," +
					"	nabhprocRept.isCapaOff as isCapaOff," +
					"	nabhprocRept.status as status " + 
					" from" + 
					"	NabhProcessMaster nabhprocRept" + 
					" where " + 
					/*"   (( nabhprocRept.nabhProcessId < 100) " + 
					"	OR (nabhprocRept.nabhProcessId >= 100 " +*/
					"   nabhprocRept.organizationId =:organizationId" + 
					"	and nabhprocRept.unitId =:unitId  "+
					" order by nabhprocRept.nabhProcessId desc "
						),
	
	  @NamedQuery(name="GET_NABH_PROCESS_MASTER_LIST_COUNT",
      query="select" + 
              "    nabhprocRept.nabhProcessId as nabhProcessId," + 
              "    nabhprocRept.nabhProcessCode as nabhProcessCode," + 
              "    nabhprocRept.nabhProcessDesc as nabhProcessDesc," +
              "    nabhprocRept.isCapaOff as isCapaOff," +
              "    nabhprocRept.status as status " + 
              " from" + 
              "    NabhProcessMaster nabhprocRept" + 
              " where " + 
              /*"   (( nabhprocRept.nabhProcessId < 100) " + 
              "    OR (nabhprocRept.nabhProcessId >= 100 " +*/
              "   nabhprocRept.organizationId =:organizationId" + 
              "    and nabhprocRept.unitId =:unitId "+
              " order by nabhprocRept.nabhProcessId desc "
          ),
	
	@NamedQuery(name="GET_ACTIVE_NABH_PROCESS_MASTER_LIST",
	query="select" + 
			"	nabhprocRept.nabhProcessId as nabhProcessId," + 
			"	nabhprocRept.nabhProcessCode as nabhProcessCode," + 
			"	nabhprocRept.nabhProcessDesc as nabhProcessDesc," + 
			"	nabhprocRept.isCapaOff as isCapaOff," + 
			"	nabhprocRept.status as status " + 
			" from" + 
			"	NabhProcessMaster nabhprocRept" + 
			" where " + 
			/*"   (( nabhprocRept.nabhProcessId < 100) " + 
			"	OR (nabhprocRept.nabhProcessId >= 100 " +*/
			"   nabhprocRept.organizationId =:organizationId" + 
			"	and nabhprocRept.unitId =:unitId  "+
			"   and nabhprocRept.status ='A' "
				),
})

@Entity
@Table(name = "m_nabh_process_master", schema = "public")
@SequenceGenerator(name = "m_nabh_process_master_seq", sequenceName = "public.m_nabh_process_master_seq", allocationSize = 1)
public class NabhProcessMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_nabh_process_master_seq")
	@Column(name="nabh_process_id")
	private Integer nabhProcessId;
	
	@Column(name="nabh_process_code")
	private String nabhProcessCode;
	
	@Column(name="nabh_process_desc")
	private String nabhProcessDesc;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="is_capa_off")
	private Character isCapaOff;

	public Integer getNabhProcessId() {
		return nabhProcessId;
	}

	public void setNabhProcessId(Integer nabhProcessId) {
		this.nabhProcessId = nabhProcessId;
	}

	public String getNabhProcessCode() {
		return nabhProcessCode;
	}

	public void setNabhProcessCode(String nabhProcessCode) {
		this.nabhProcessCode = nabhProcessCode;
	}

	public String getNabhProcessDesc() {
		return nabhProcessDesc;
	}

	public void setNabhProcessDesc(String nabhProcessDesc) {
		this.nabhProcessDesc = nabhProcessDesc;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getIsCapaOff() {
		return isCapaOff;
	}

	public void setIsCapaOff(Character isCapaOff) {
		this.isCapaOff = isCapaOff;
	}
	
	
}
