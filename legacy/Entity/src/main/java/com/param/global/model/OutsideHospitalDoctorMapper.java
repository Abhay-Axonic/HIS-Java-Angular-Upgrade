package com.param.global.model;

import java.util.Date;
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

import com.param.opd.model.ConsultDoctor;


@NamedQueries({
	
	@NamedQuery(name="GET_OUTSIDE_HOSPITAL_DOCTOR_MAPPER_BY_NAME",
			query="SELECT outsideHospitalDoctorMapper.outsideHosDocId AS outsideHosDocId "
						+ "	FROM OutsideHospitalDoctorMapper outsideHospitalDoctorMapper"
						+ " WHERE (LOWER(outsideHospitalDoctorMapper.hospDoctorName)=:hospDoctorName OR outsideHospitalDoctorMapper.hospDoctorName=:hospDoctorName) "
						+ " AND outsideHospitalDoctorMapper.unitId= :unitId"
						),
})
@Entity
@Table(name = "t_outside_hospital_doctor_mapper", schema = "public")
@SequenceGenerator(name = "t_outside_hospital_doctor_mapper_seq", sequenceName = "public.t_outside_hospital_doctor_mapper_seq", allocationSize = 1)
public class OutsideHospitalDoctorMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_outside_hospital_doctor_mapper_seq")
	@Column(name = "outside_hos_doc_id")
	private Integer outsideHosDocId;
	
	@Column(name = "outside_hospital_id")
	private Integer outsideHospitalId;
	
	@Column(name = "hosp_doctor_name")
	private String hospDoctorName;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "outsideHospitalDoctorMapper", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ConsultDoctor> OutsideHospitalDoctorMapperList;
	
	public Integer getOutsideHosDocId() {
		return outsideHosDocId;
	}

	public void setOutsideHosDocId(Integer outsideHosDocId) {
		this.outsideHosDocId = outsideHosDocId;
	}

	public Integer getOutsideHospitalId() {
		return outsideHospitalId;
	}

	public void setOutsideHospitalId(Integer outsideHospitalId) {
		this.outsideHospitalId = outsideHospitalId;
	}

	public String getHospDoctorName() {
		return hospDoctorName;
	}

	public void setHospDoctorName(String hospDoctorName) {
		this.hospDoctorName = hospDoctorName;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
	
	
	
}
