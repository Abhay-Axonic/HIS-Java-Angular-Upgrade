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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.org.common.model.DegreeMaster;


@NamedQuery(name="GET_LIST_OF_DOCTOR_DEGREE_DOCCUEMENT",
            query="SELECT ddm.doctorId as doctorId, "
            		+ "ddm.doctorDocId as doctorDocId, "
            		+ "ddm.registrationNumber as  registrationNumber, "
            		+ "ddm.qualificationId as qualificationId, "
            		+ "ddm.qualificationDesc as  qualificationDesc, "
            		+ "ddm.degreeId as degreeId, "
            		+ "degreeMaster.degreeDesc as degreeDesc, "
            		+ "ddm.fileName as fileName, "
            		+ "ddm.file as file, "
            		+ "ddm.documentFilePath as documentFilePath "
            		+ "FROM DoctorDocumentMapper ddm "
            		+ "INNER JOIN ddm.degreeMaster degreeMaster "
            		+ "WHERE ddm.organizationId=:organizationId "
            		+ "AND ddm.unitId=:unitId "
            		+ "AND ddm.status='A' "
            		+ "AND ddm.doctorId=:doctorId ")

@Entity
@Table(name = "t_doctor_doc_mapper", schema = "doctor")
@SequenceGenerator(name = "t_doctor_doc_mapper_seq", sequenceName = "doctor.t_doctor_doc_mapper_seq", allocationSize = 1)
public class DoctorDocumentMapper {
	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_doctor_doc_mapper_seq")

	@Column(name = "doctor_doc_id")
	private Integer doctorDocId;

	@Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "registration_number")
	private String registrationNumber;

	@Column(name = "qualification_id")
	private Integer qualificationId;

	@Column(name = "qualification_desc")
	private String qualificationDesc;

	@Column(name = "degree_id")
	private Integer degreeId;

	@Column(name = "file")
	private byte[] file;

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

	@Column(name = "document_file_path")
	private String documentFilePath;
	
	
	@Column(name = "file_name")
	private String fileName ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private DoctorMaster doctorDocumentMapper;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "degree_id", insertable = false, updatable = false)
	private DegreeMaster  degreeMaster ;
	

	public Integer getDoctorDocId() {
		return doctorDocId;
	}

	public void setDoctorDocId(Integer doctorDocId) {
		this.doctorDocId = doctorDocId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Integer getQualificationId() {
		return qualificationId;
	}

	public void setQualificationId(Integer qualificationId) {
		this.qualificationId = qualificationId;
	}

	public String getQualificationDesc() {
		return qualificationDesc;
	}

	public void setQualificationDesc(String qualificationDesc) {
		this.qualificationDesc = qualificationDesc;
	}

	public Integer getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Integer degreeId) {
		this.degreeId = degreeId;
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

	public String getDocumentFilePath() {
		return documentFilePath;
	}

	public void setDocumentFilePath(String documentFilePath) {
		this.documentFilePath = documentFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
