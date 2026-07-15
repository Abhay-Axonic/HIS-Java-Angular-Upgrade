package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Frequency;
import com.param.global.model.TubeFeedingProductsMaster;
import com.param.opd.model.FrequencyMaster;


@NamedQueries({
	
	@NamedQuery(name="GET_IPD_RYLES_TUBE_ORDER_LIST",
		query=" SELECT  ipdRylesTubeMaster.ipdRylesTubeId AS ipdRylesTubeId,"
				+ " ipdRylesTubeMaster.tubeFeedingProductId AS tubeFeedingProductId,"
				+ " ipdRylesTubeMaster.doctorId AS doctorId, "
				+ " ipdRylesTubeMaster.admissionId AS admissionId, "
				+ " ipdRylesTubeMaster.quantity AS quantity, "
				+ " ipdRylesTubeMaster.patientId as patientId, "
				+ " ipdRylesTubeMaster.frequencyId as frequencyId, "
				+ " ipdRylesTubeMaster.rylesTubeInstruction AS rylesTubeInstruction, "
				+ " ipdRylesTubeMaster.organizationId AS organizationId,"
				+ " ipdRylesTubeMaster.unitId AS unitId,"
				+ " ipdRylesTubeMaster.status AS status,"
				+ " frequencyMaster.description AS frequency, "
				+ " tubeFeedingProductsMaster.productsDesc AS tubeFeedingProductsDesc"
				+ " FROM IpdRylesTubeMaster ipdRylesTubeMaster "
				+ " LEFT JOIN ipdRylesTubeMaster.frequencyMaster frequencyMaster"
				+ " INNER JOIN ipdRylesTubeMaster.tubeFeedingProductsMaster tubeFeedingProductsMaster"
				+ " WHERE ipdRylesTubeMaster.organizationId= :organizationId"
				+ " AND ipdRylesTubeMaster.unitId= :unitId"
				+ " AND ipdRylesTubeMaster.patientId= :patientId"
				+ " ORDER BY ipdRylesTubeMaster.ipdRylesTubeId asc "
				)
})
@Entity
@Table(name = "t_ipd_ryles_tube", schema = "adt")
@SequenceGenerator(name = "t_ipd_ryles_tube_seq", sequenceName = "adt.t_ipd_ryles_tube_seq", allocationSize = 1)
public class IpdRylesTubeMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_ipd_ryles_tube_seq")
	
	@Column(name = "ipd_ryles_tube_id")
	private Integer ipdRylesTubeId;
	
	@Column(name = "tube_feeding_product_id")
	private Integer tubeFeedingProductId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "frequency_id")
	private Integer frequencyId;
	
	@Column(name = "ryles_tube_instruction")
	private String rylesTubeInstruction;
	
	@Column(name = "organisation_id")
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="frequency_id", insertable=false, updatable=false)
	private Frequency frequencyMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tube_feeding_product_id", insertable=false, updatable=false)
	private TubeFeedingProductsMaster tubeFeedingProductsMaster;

	public Integer getIpdRylesTubeId() {
		return ipdRylesTubeId;
	}

	public void setIpdRylesTubeId(Integer ipdRylesTubeId) {
		this.ipdRylesTubeId = ipdRylesTubeId;
	}

	public Integer getTubeFeedingProductId() {
		return tubeFeedingProductId;
	}

	public void setTubeFeedingProductId(Integer tubeFeedingProductId) {
		this.tubeFeedingProductId = tubeFeedingProductId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public String getRylesTubeInstruction() {
		return rylesTubeInstruction;
	}

	public void setRylesTubeInstruction(String rylesTubeInstruction) {
		this.rylesTubeInstruction = rylesTubeInstruction;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Frequency getFrequencyMaster() {
		return frequencyMaster;
	}

	public void setFrequencyMaster(Frequency frequencyMaster) {
		this.frequencyMaster = frequencyMaster;
	}

	public TubeFeedingProductsMaster getTubeFeedingProductsMaster() {
		return tubeFeedingProductsMaster;
	}

	public void setTubeFeedingProductsMaster(TubeFeedingProductsMaster tubeFeedingProductsMaster) {
		this.tubeFeedingProductsMaster = tubeFeedingProductsMaster;
	}
	
	

}
