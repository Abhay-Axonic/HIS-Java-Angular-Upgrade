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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.org.model.GeneralLedgerMaster;
import com.param.service.global.model.TContractCapDetails;
import com.param.service.global.model.TPackageCapDetails;
@NamedQueries({
	@NamedQuery(name = "GET_SUB_SPECIALITY_LIST", query = "SELECT subSpecialityMaster.subSpecialityMasterId as subSpecialityMasterId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName, "
			+ "subSpecialityMaster.subSpecialityMasterCode as subSpecialityMasterCode, "
			+ "subSpecialityMaster.specialityId as specialityId, "
			+ "subSpecialityMaster.status as status, "
			+ "subSpecialityMaster.isModality as isModality, "
			+ "subSpecialityMaster.isMriModality as isMriModality, "
			+ "subSpecialityMaster.isCTModality as isCTModality, "
			+ "sm.specialityName as specialityName, "
			+ "glm.generalLedgerName as generalLedgerName "
			+ "FROM SubSpecialityMaster as subSpecialityMaster "
			+ "INNER JOIN subSpecialityMaster.specialityMaster sm "
			+ "LEFT JOIN subSpecialityMaster.generalLedgerMaster glm "
			+ "WHERE subSpecialityMaster.organizationId=:orgId "
			+ " ORDER BY subSpecialityMaster.subSpecialityMasterName ASC "),
	
	@NamedQuery(name = "GET_SUB_SPECIALITY_LIST_BY_NAME", query = "SELECT subSpecialityMaster.subSpecialityMasterId as subSpecialityMasterId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName "
			+ "FROM SubSpecialityMaster as subSpecialityMaster "
			+ "WHERE (LOWER(subSpecialityMaster.subSpecialityMasterName) =:subSpecialityName "
			+ "OR subSpecialityMaster.subSpecialityMasterName=:subSpecialityName) AND subSpecialityMaster.organizationId=:orgId "),
	
	@NamedQuery(name = "GET_SUB_SPECIALITY_LIST_BY_ID", query = "SELECT subSpecialityMaster.subSpecialityMasterId as subSpecialityMasterId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName, "
			+ "subSpecialityMaster.subSpecialityMasterCode as subSpecialityMasterCode, "
			+ "subSpecialityMaster.specialityId as specialityId, "
			+ "subSpecialityMaster.generalLedgerId as generalLedgerId, "
			+ "subSpecialityMaster.status as status, "
			+ "subSpecialityMaster.isModality as isModality,"
			+ "subSpecialityMaster.isMriModality as isMriModality,"
			+ "subSpecialityMaster.isCTModality as isCTModality,"
            + "ledgerMster.generalLedgerName as generalLedgerName,"
			+ "subSpecialityMaster.subHeaderName as subHeaderName "
			+ "FROM SubSpecialityMaster as subSpecialityMaster "
            + " INNER JOIN subSpecialityMaster.generalLedgerMaster ledgerMster  "
			+ "WHERE subSpecialityMaster.subSpecialityMasterId =:subSpecialityId "),
	
	@NamedQuery(name = "GET_SUB_SPECIALITY_LIST_BY_NAME_NOT_BY_ID", query = "SELECT subSpecialityMaster.subSpecialityMasterId as subSpecialityMasterId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName "
			+ "FROM SubSpecialityMaster as subSpecialityMaster "
			+ "WHERE (LOWER(subSpecialityMaster.subSpecialityMasterName) =:subSpecialityName OR subSpecialityMaster.subSpecialityMasterName=:subSpecialityName)"
			+ "AND subSpecialityMaster.subSpecialityMasterId !=:subSpecialityMasterId AND subSpecialityMaster.organizationId =:orgId "),
	
	@NamedQuery(name = "GET_SUBSPECIALITY_BY_SPECIALITY_ID", query = "SELECT subSpecialityMaster.subSpecialityMasterId as subSpecialityMasterId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName, "
			+ "subSpecialityMaster.subSpecialityMasterCode as subSpecialityMasterCode, "
			+ "subSpecialityMaster.specialityId as specialityId, "
			+ "subSpecialityMaster.status as status "
			+ "FROM SubSpecialityMaster as subSpecialityMaster WHERE subSpecialityMaster.status = 'A'"
			+ " AND subSpecialityMaster.specialityId =:specialityId"
			+ " AND subSpecialityMaster.organizationId =:orgId"
			+ " ORDER BY subSpecialityMaster.subSpecialityMasterName ASC "
			),
	
	@NamedQuery(name = "GET_SUBSPECIALITY_BY_SPECIALITY_IDS", query = "SELECT subSpecialityMaster.subSpecialityMasterId as subSpecialityMasterId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName, "
			+ "subSpecialityMaster.subSpecialityMasterCode as subSpecialityMasterCode, "
			+ "subSpecialityMaster.specialityId as specialityId, "
			+ "subSpecialityMaster.status as status "
			+ "FROM SubSpecialityMaster as subSpecialityMaster WHERE subSpecialityMaster.status = 'A'"
			+ " AND subSpecialityMaster.specialityId IN (:specialityIds) "
			+ " AND subSpecialityMaster.organizationId =:orgId"
			),
	
	@NamedQuery(name = "GET_SUB_SPECIALITY_LIST_FOR_CONFIG", query = "SELECT subSpecialityMaster.subSpecialityMasterId as subSpecialityMasterId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName, "
			+ "subSpecialityMaster.subSpecialityMasterCode as subSpecialityMasterCode, "
			+ "subSpecialityMaster.specialityId as specialityId, "
			+ "subSpecialityMaster.status as status, "
			+ "subSpecialityMaster.isModality as isModality, "
			+ "sm.specialityName as specialityName, "
			+ "glm.generalLedgerName as generalLedgerName "
			+ "FROM SubSpecialityMaster as subSpecialityMaster "
			+ "INNER JOIN subSpecialityMaster.specialityMaster sm "
			+ "LEFT JOIN subSpecialityMaster.generalLedgerMaster glm "
			+ "WHERE subSpecialityMaster.organizationId=:orgId "
			+ "ORDER BY subSpecialityMaster.subSpecialityMasterId DESC"),
	
	@NamedQuery(name = "GET_PAGINATED_SUBSPECIALITY_MASTER_DETAILS_LIST_SEARCH", 
	query = "SELECT subSpecialityMaster.subSpecialityMasterId as subSpecialityMasterId, "
			+ "subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName, "
			+ "subSpecialityMaster.subSpecialityMasterCode as subSpecialityMasterCode, "
			+ "subSpecialityMaster.specialityId as specialityId, "
			+ "subSpecialityMaster.status as status, "
			+ "subSpecialityMaster.isModality as isModality, "
			+ "subSpecialityMaster.isMriModality as isMriModality, "
			+ "subSpecialityMaster.isCTModality as isCTModality, "
			+ "sm.specialityName as specialityName, "
			+ "glm.generalLedgerName as generalLedgerName "
			+ "FROM SubSpecialityMaster as subSpecialityMaster "
			+ "INNER JOIN subSpecialityMaster.specialityMaster sm "
			+ "LEFT JOIN subSpecialityMaster.generalLedgerMaster glm "
			+ "WHERE subSpecialityMaster.organizationId=:orgId "
			+ " AND ( lower(subSpecialityMaster.subSpecialityMasterName) like :subSpecialityMasterName "
			+ " OR lower(subSpecialityMaster.subSpecialityMasterCode) like :subSpecialityMasterName "
			+ " OR lower(sm.specialityName) like :subSpecialityMasterName "
			+ " OR lower(glm.generalLedgerName) like :subSpecialityMasterName "
			+ " OR lower(subSpecialityMaster.isModality) like :subSpecialityMasterName ) "
			+ " ORDER BY subSpecialityMaster.subSpecialityMasterId DESC"),
	
})
@NamedNativeQueries({
@NamedNativeQuery(name="GET_SUBSPECIALITY_BY_SPECIALITY_ID_NOT_MAPPED_TO_UNIT",
           query="SELECT sbpl.sub_speciality_id AS \"subSpecialityMasterId\","
           		+ "sbpl.sub_speciality_name AS \"subSpecialityMasterName\","
           		+ "sbpl.sub_speciality_code AS \"subSpecialityMasterCode\","
           		+ "sbpl.speciality_id AS \"specialityId\" "
           		+ " FROM doctor.m_sub_speciality_master sbpl "
           		+ "WHERE sbpl.sub_speciality_id "
           		+ " NOT IN (SELECT uspmppr.sub_speciality_id "
           		+ " FROM public.t_unit_sub_speciality_mapper uspmppr "
           		+ "	WHERE uspmppr.organization_id=:orgId AND uspmppr.unit_id=:unitId and uspmppr.status = 'A' ) "
           		+ " AND sbpl.speciality_id=:specialityId "
           		+ " AND sbpl.status = 'A' "
           		+ " AND sbpl.organization_id=:orgId"),

@NamedNativeQuery(name = "GET_UNIT_SUBSPECIALITY_BY_SPECIALITY_ID", query = "SELECT sbpl.sub_speciality_id AS \"subSpecialityMasterId\","
		+ "sbpl.sub_speciality_name AS \"subSpecialityMasterName\","
   		+ "sbpl.sub_speciality_code AS \"subSpecialityMasterCode\","
   		+ "sbpl.speciality_id AS \"specialityId\", "
   		+ "sbpl.status AS \"status\" "
   		+ " FROM public.t_unit_sub_speciality_mapper uspmppr "
		+ "INNER JOIN  doctor.m_sub_speciality_master sbpl on uspmppr.sub_speciality_id = sbpl.sub_speciality_id "
		+ " WHERE sbpl.status = 'A'"
		+ " AND uspmppr.speciality_id=sbpl.speciality_id"
		+ " AND sbpl.speciality_id =:specialityId"
		+ " AND sbpl.organization_id =:orgId"
		+ " AND uspmppr.unit_id =:unitId "
		+ " ORDER BY sbpl.sub_speciality_name ASC "	),
})
@Entity
@Table(name = "m_sub_speciality_master", schema = "doctor")
@SequenceGenerator(name="sub_speciality_master_seq", sequenceName="doctor.sub_speciality_master_seq", allocationSize=1)
public class SubSpecialityMaster 
{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="sub_speciality_master_seq")
	@Column(name="sub_speciality_id")
	private int subSpecialityMasterId;
	
	@Column(name="sub_speciality_name")
	private String subSpecialityMasterName;
	
	@Column(name="sub_speciality_code")
	private String subSpecialityMasterCode;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name="is_modality")
	private char isModality;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;

	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="general_ledger_id")
	private Integer generalLedgerId;
	
	@Column(name="sub_header_name")
	private String subHeaderName;
	
	@Column(name="is_mri_modality")
	private char isMriModality;
	
	@Column(name="is_ct_modality")
	private char isCTModality; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "general_ledger_id" , insertable = false , updatable = false , nullable = false)
	private GeneralLedgerMaster generalLedgerMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id" , insertable = false , updatable = false , nullable = false)
	private SpecialityMaster specialityMaster;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "subSpecialityMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ModalityMaster> modalityMastersList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "subSpecialityMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TPackageCapDetails> listTPackageCapDetails;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "subSpecialityMaster")
	private List<TContractCapDetails> listTContractCapDetails;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "subSpecialityMaster")
	private List<BedsideImagingMaster> listBedsideImagingMaster;

	public List<TContractCapDetails> getListTContractCapDetails() {
		return listTContractCapDetails;
	}

	public void setListTContractCapDetails(List<TContractCapDetails> listTContractCapDetails) {
		this.listTContractCapDetails = listTContractCapDetails;
	}

	public List<TPackageCapDetails> getListTPackageCapDetails() {
		return listTPackageCapDetails;
	}

	public void setListTPackageCapDetails(List<TPackageCapDetails> listTPackageCapDetails) {
		this.listTPackageCapDetails = listTPackageCapDetails;
	}

	public void setSubSpecialityMasterId(int subSpecialityMasterId) {
		this.subSpecialityMasterId = subSpecialityMasterId;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public char getIsModality() {
		return isModality;
	}

	public void setIsModality(char isModality) {
		this.isModality = (isModality == '\u0000') ? 'N' : isModality;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}


	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public Integer getSubSpecialityMasterId() {
		return subSpecialityMasterId;
	}

	public String getSubSpecialityMasterName() {
		return subSpecialityMasterName;
	}

	public void setSubSpecialityMasterName(String subSpecialityMasterName) {
		this.subSpecialityMasterName = subSpecialityMasterName;
	}

	public String getSubSpecialityMasterCode() {
		return subSpecialityMasterCode;
	}

	public void setSubSpecialityMasterCode(String subSpecialityMasterCode) {
		this.subSpecialityMasterCode = subSpecialityMasterCode;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public List<ModalityMaster> getModalityMastersList() {
		return modalityMastersList;
	}

	public void setModalityMastersList(List<ModalityMaster> modalityMastersList) {
		this.modalityMastersList = modalityMastersList;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

	public GeneralLedgerMaster getGeneralLedgerMaster() {
		return generalLedgerMaster;
	}

	public void setGeneralLedgerMaster(GeneralLedgerMaster generalLedgerMaster) {
		this.generalLedgerMaster = generalLedgerMaster;
	}

	public String getSubHeaderName() {
		return subHeaderName;
	}

	public void setSubHeaderName(String subHeaderName) {
		this.subHeaderName = subHeaderName;
	}

	public char getIsMriModality() {
		return isMriModality;
	}

	public void setIsMriModality(char isMriModality) {
		this.isMriModality = (isMriModality == '\u0000') ? 'N' : isMriModality;
	}

	public char getIsCTModality() {
		return isCTModality;
	}

	public void setIsCTModality(char isCTModality) {
		this.isCTModality = (isCTModality == '\u0000') ? 'N' : isCTModality;
	}
	
	
}
