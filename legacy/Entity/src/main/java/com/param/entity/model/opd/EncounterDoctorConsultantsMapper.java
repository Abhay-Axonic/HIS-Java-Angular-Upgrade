package com.param.entity.model.opd;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name = "GET_ENC_DOC_MAPPER_DATA", query = "SELECT id as id " 
			+ " FROM EncounterDoctorConsultantsMapper as obj "
			+ " WHERE obj.encounterId=:encId "
			+ " AND obj.doctorId=:docId ")
})


@Entity
@Table(name = "t_encounter_doctor_consultants_mapper", schema = "opd")
public class EncounterDoctorConsultantsMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "created_date")
	private Date createdDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
