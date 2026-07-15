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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.opd.coversheet.model.NursingOrderMapper;


@NamedQueries({
	
	@NamedQuery(name="GET_NURSING_ORDER_MASTER_LIST",
			query="SELECT nursingOrderMaster.nursingOrderId AS nursingOrderId, "
					+ "nursingOrderMaster.nursingOrderCode AS nursingOrderCode, "
					+ "nursingOrderMaster.nursingOrderDesc AS nursingOrderDesc,"
					+ "nursingOrderMaster.serviceId AS serviceId,"
					+ "nursingOrderMaster.specialityId AS specialityId,"
					+ "nursingOrderMaster.subSpecialityId AS subSpecialityId,"
					+ "sm.serviceStandardName AS serviceStandardName,"
					+ "spm.specialityName AS specialityName,"
					+ "sbpm.subSpecialityMasterName AS subSpecialityName,"
					+ " nursingOrderMaster.organizationId AS organizationId, "
					+ "nursingOrderMaster.unitId AS unitId,"
					+ " nursingOrderMaster.status AS status"
					+ " FROM NursingOrderMaster nursingOrderMaster"
					+ " LEFT JOIN nursingOrderMaster.serviceMaster sm "
					+ " LEFT JOIN nursingOrderMaster.specialityMaster spm "
					+ " LEFT JOIN nursingOrderMaster.subSpecialityMaster sbpm "
					+ " WHERE  nursingOrderMaster.organizationId= :organizationId "
					+ " AND nursingOrderMaster.unitId= :unitId"
					+ " ORDER BY nursingOrderMaster.nursingOrderId asc"
					),
	
	@NamedQuery(name="GET_NURSING_ORDER_MASTER_BY_ID",
			query="SELECT nursingOrderMaster.nursingOrderId AS nursingOrderId,"
					+ " nursingOrderMaster.nursingOrderCode AS nursingOrderCode, "
					+ "nursingOrderMaster.nursingOrderDesc AS nursingOrderDesc,"
					+ "nursingOrderMaster.serviceId AS serviceId,"
					+ "nursingOrderMaster.specialityId AS specialityId,"
					+ "nursingOrderMaster.subSpecialityId AS subSpecialityId,"
					+ "sm.serviceStandardName AS serviceStandardName,"
					+ "spm.specialityName AS specialityName,"
					+ "sbpm.subSpecialityMasterName AS subSpecialityName,"
					+ " nursingOrderMaster.organizationId AS organizationId,"
					+ " nursingOrderMaster.unitId AS unitId, "
					+ "nursingOrderMaster.status AS status"
					+ " FROM NursingOrderMaster nursingOrderMaster"
					+ " LEFT JOIN nursingOrderMaster.serviceMaster sm "
					+ " LEFT JOIN nursingOrderMaster.specialityMaster spm "
					+ " LEFT JOIN nursingOrderMaster.subSpecialityMaster sbpm "
					+ " WHERE nursingOrderMaster.nursingOrderId= :nursingOrderId"
					+ " AND nursingOrderMaster.organizationId= :organizationId"
					+ " AND nursingOrderMaster.unitId= :unitId"
					),
	
		@NamedQuery(name="GET_ACTIVE_NURSING_ORDER_MASTER",
			query="SELECT nursingOrderMaster.nursingOrderId AS nursingOrderId, "
					+ "nursingOrderMaster.nursingOrderCode AS nursingOrderCode,"
					+ " nursingOrderMaster.nursingOrderDesc AS nursingOrderDesc,"
					+ "nursingOrderMaster.serviceId AS serviceId,"
					+ "nursingOrderMaster.specialityId AS specialityId,"
					+ "nursingOrderMaster.subSpecialityId AS subSpecialityId,"
					+ "sm.serviceStandardName AS serviceStandardName,"
					+ "spm.specialityName AS specialityName,"
					+ "sbpm.subSpecialityMasterName AS subSpecialityName,"
					+ " nursingOrderMaster.organizationId AS organizationId, "
					+ "nursingOrderMaster.unitId AS unitId, "
					+ "nursingOrderMaster.status AS status"
					+ " FROM NursingOrderMaster nursingOrderMaster"
					+ " LEFT JOIN nursingOrderMaster.serviceMaster sm "
					+ " LEFT JOIN nursingOrderMaster.specialityMaster spm "
					+ " LEFT JOIN nursingOrderMaster.subSpecialityMaster sbpm "
					+ " WHERE nursingOrderMaster.status= 'A'"
					+ " AND nursingOrderMaster.organizationId= :organizationId"
					+ " AND nursingOrderMaster.unitId= :unitId"
					+ " ORDER BY nursingOrderMaster.nursingOrderDesc ASC"
					),
	
	@NamedQuery(name="GET_NURSING_ORDER_MASTER_BY_NAME",
		query="SELECT nursingOrderMaster.nursingOrderId AS nursingOrderId "
					+ "	FROM NursingOrderMaster nursingOrderMaster"
					+ " WHERE LOWER(nursingOrderMaster.nursingOrderDesc)= :nursingOrderDesc "
					+ " AND nursingOrderMaster.organizationId= :organizationId "
					+ " AND nursingOrderMaster.unitId= :unitId "
					),
	
	@NamedQuery(name="GET_NURSING_ORDER_MASTER_BY_NAME_NOT_ID",
		query="SELECT nursingOrderMaster.nursingOrderId AS nursingOrderId"
					+ " FROM NursingOrderMaster nursingOrderMaster"
					+ " WHERE LOWER(nursingOrderMaster.nursingOrderDesc)= :nursingOrderDesc"
					+ " AND nursingOrderMaster.nursingOrderId != :nursingOrderId"
					+ " AND nursingOrderMaster.organizationId= :organizationId "
					+ " AND nursingOrderMaster.unitId= :unitId "
					),
		@NamedQuery(name="GET_NURSING_ORDER_MASTER_LIST_SERARCH",
		query="SELECT nursingOrderMaster.nursingOrderId AS nursingOrderId, "
				+ "nursingOrderMaster.nursingOrderCode AS nursingOrderCode, "
				+ "nursingOrderMaster.nursingOrderDesc AS nursingOrderDesc,"
				+ "nursingOrderMaster.serviceId AS serviceId,"
				+ "nursingOrderMaster.specialityId AS specialityId,"
				+ "nursingOrderMaster.subSpecialityId AS subSpecialityId,"
				+ "sm.serviceStandardName AS serviceStandardName,"
				+ "spm.specialityName AS specialityName,"
				+ "sbpm.subSpecialityMasterName AS subSpecialityName,"
				+ " nursingOrderMaster.organizationId AS organizationId, "
				+ "nursingOrderMaster.unitId AS unitId, "
				+ "nursingOrderMaster.status AS status"
				+ " FROM NursingOrderMaster nursingOrderMaster"
				+ " LEFT JOIN nursingOrderMaster.serviceMaster sm "
				+ " LEFT JOIN nursingOrderMaster.specialityMaster spm "
				+ " LEFT JOIN nursingOrderMaster.subSpecialityMaster sbpm "
				+ " WHERE  nursingOrderMaster.organizationId= :organizationId "
				+ " AND nursingOrderMaster.unitId= :unitId"
				+ " AND  LOWER(nursingOrderMaster.nursingOrderDesc) like :nursingOrderCode"
				+ " OR LOWER(nursingOrderMaster.nursingOrderCode) like :nursingOrderCode "
				+ " ORDER BY nursingOrderMaster.nursingOrderId asc"
				),
	})


@Entity
@Table(name = "m_nursing_Order_master", schema = "adt")
@SequenceGenerator(name = "nursing_Order_seq", sequenceName = "adt.nursing_Order_seq", allocationSize = 1)
public class NursingOrderMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nursing_Order_seq")
	
	@Column(name = "nursing_order_id")
	private Integer nursingOrderId;
	
	@Column(name = "nursing_order_code")
	private String nursingOrderCode;
	
	@Column(name = "nursing_order_desc")
	private String nursingOrderDesc;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "sub_speciality_id")
	private Integer subSpecialityId;
	
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nursingOrderMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NursingOrderMapper> listNursingOrderMapper;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false, nullable = false)
	private SpecialityMaster specialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_speciality_id", insertable = false, updatable = false, nullable = false)
	private SubSpecialityMaster subSpecialityMaster;
	
	
	public Integer getNursingOrderId() {
		return nursingOrderId;
	}

	public void setNursingOrderId(Integer nursingOrderId) {
		this.nursingOrderId = nursingOrderId;
	}

	public String getNursingOrderCode() {
		return nursingOrderCode;
	}

	public void setNursingOrderCode(String nursingOrderCode) {
		this.nursingOrderCode = nursingOrderCode;
	}

	public String getNursingOrderDesc() {
		return nursingOrderDesc;
	}

	public void setNursingOrderDesc(String nursingOrderDesc) {
		this.nursingOrderDesc = nursingOrderDesc;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	
	

}