package com.param.global.model;

import java.util.Date;


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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.opd.coversheet.model.Icpc;
import com.param.opd.coversheet.model.IcpcSystemMaster;
@NamedQueries({
		/*@NamedQuery(name="GET_LIST_OF_ICPC_SYSTEMS",
				 					query="SELECT ism.icpcSystemId AS icpcSystemId , ism.icpcSystemDesc AS icpcSystemDesc"
				 							+ " FROM ICPCTemplateMapping iCPCTemplateMapping "
				 							+ " INNER JOIN iCPCTemplateMapping.icpcSystemMaster ism"
				 							+ " WHERE iCPCTemplateMapping.orgId=:organisationId "
				 							+ " AND iCPCTemplateMapping.orgUnitId=:unitId") */
	@NamedQuery(name="GET_DETAILED_LIST_OF_ICPC_SYSTEMS",
			query=" SELECT  distinct(iCPCTemplateMapping.templateId) AS templateId, "
					+ " icpcSystemMaster.icpcSystemDesc AS icpcSystemDesc, "
					+ " iCPCTemplateMapping.mIcpcSystemId AS mIcpcSystemId "
					+ " FROM ICPCTemplateMapping iCPCTemplateMapping "
					+ " INNER JOIN iCPCTemplateMapping.templateMaster templateMaster"
					+ " INNER JOIN iCPCTemplateMapping.icpcSystemMaster icpcSystemMaster"
					+ " WHERE iCPCTemplateMapping.orgId=:orgId"
					+ " AND iCPCTemplateMapping.orgUnitId=:orgUnitId"
					+ " AND iCPCTemplateMapping.templateId=:templateId"
					+ " AND iCPCTemplateMapping.isDeleted !='Y'"	
					+ " AND iCPCTemplateMapping.status!='D'"
				),
	@NamedQuery(name="GET_DETAILED_LIST_OF_ICPC_SYSTEMS_DESCRIPTION",
	query=" SELECT  distinct(iCPCTemplateMapping.templateId) AS templateId,"
			+ " icpcSystemMaster.icpcSystemDesc AS icpcSystemDesc, icpcMaster.icpcDesc AS icpcDesc"
			+ " FROM ICPCTemplateMapping iCPCTemplateMapping "
			+ " INNER JOIN iCPCTemplateMapping.templateMaster templateMaster"
			+ " INNER JOIN iCPCTemplateMapping.icpcSystemMaster icpcSystemMaster"
			+ " INNER JOIN iCPCTemplateMapping.icpcMaster icpcMaster"
			+ " WHERE iCPCTemplateMapping.orgId=:orgId"
			+ " AND iCPCTemplateMapping.orgUnitId=:orgUnitId"
			+ " AND iCPCTemplateMapping.templateId=:templateId"
			+ " AND iCPCTemplateMapping.status!='D'"
			+ " AND iCPCTemplateMapping.isDeleted !='Y'"	
		),
	@NamedQuery(name="GET_ICPC_LIST_BY_TEMPLATE_ID",
	query=" SELECT icpcMaster.icpcDesc AS icpcDesc,icpcMaster.icpcId as mIcpcId"
			+ " FROM ICPCTemplateMapping iCPCTemplateMapping "
			+ " INNER JOIN iCPCTemplateMapping.templateMaster templateMaster"
			+ " INNER JOIN iCPCTemplateMapping.icpcSystemMaster icpcSystemMaster"
			+ " INNER JOIN iCPCTemplateMapping.icpcMaster icpcMaster"
			+ " WHERE iCPCTemplateMapping.orgId=:orgId"
			+ " AND iCPCTemplateMapping.orgUnitId=:orgUnitId"
			+ " AND iCPCTemplateMapping.templateId=:templateId"
			+ " AND iCPCTemplateMapping.status!='D'"
			+ " AND iCPCTemplateMapping.isDeleted !='Y'"	
		),
	
	@NamedQuery(name="ICPC_TEMPLATE_MAPPING_LIST",
	query=" SELECT  distinct(iCPCTemplateMapping.templateId) AS templateId, "
			+ " templateMaster.title AS title, "
			+ " iCPCTemplateMapping.status AS status"
			+ " FROM ICPCTemplateMapping iCPCTemplateMapping "
			+ " INNER JOIN iCPCTemplateMapping.templateMaster templateMaster"
			+ " WHERE iCPCTemplateMapping.orgId=:orgId"
			+ " AND iCPCTemplateMapping.orgUnitId=:orgUnitId"
			+ " AND iCPCTemplateMapping.status!='D'"
			+ " AND iCPCTemplateMapping.isDeleted !='Y'"	
		),
	
	@NamedQuery(name="GET_ICPC_TEMPLATE_MAPPING_BY_ID",
	query=" SELECT "
			+ " distinct (iCPCTemplateMapping.mIcpcSystemId) AS mIcpcSystemId, "
			+ "iCPCTemplateMapping.templateId AS templateId, "
			+ " templateMaster.title AS title, "
			
			+ "  iCPCTemplateMapping.mIcpcId AS mIcpcId, "
			+ " iCPCTemplateMapping.status AS status"
			+ " FROM ICPCTemplateMapping iCPCTemplateMapping "
			+ " INNER JOIN iCPCTemplateMapping.templateMaster templateMaster"
			+ " WHERE iCPCTemplateMapping.orgId=:orgId"
			+ " AND iCPCTemplateMapping.orgUnitId=:orgUnitId"
			+ " AND iCPCTemplateMapping.templateId=:templateId"
			+ " AND iCPCTemplateMapping.status!='D'"
			+ " AND iCPCTemplateMapping.isDeleted !='Y'"	
		),
	
	@NamedQuery(name="CHECK_ICPC_TEMPLATE_MAPPING_AVAILABLE",
	query=" SELECT iCPCTemplateMapping.icpcTemplateId AS icpcTemplateId, iCPCTemplateMapping.templateId AS templateId,  "
			+ "  iCPCTemplateMapping.mIcpcSystemId AS mIcpcSystemId "
			+ " FROM ICPCTemplateMapping iCPCTemplateMapping "
			+ " WHERE iCPCTemplateMapping.orgId=:orgId"
			+ " AND iCPCTemplateMapping.orgUnitId=:orgUnitId"
			+ " AND iCPCTemplateMapping.templateId=:templateId"
            + " AND iCPCTemplateMapping.status!='D' "
//			+ " AND iCPCTemplateMapping.mIcpcSystemId=:mIcpcSystemId"
//			+ " AND iCPCTemplateMapping.mIcpcId=:mIcpcId"
		),
	
	
	@NamedQuery(name = "UPDATE_ICPC_TEMPLATE_MAPPING_STATUS",
	query = " UPDATE ICPCTemplateMapping iCPCTemplateMapping "
			+ " SET iCPCTemplateMapping.status = :status"
			+ " WHERE iCPCTemplateMapping.orgId= :orgId "
			+ " AND iCPCTemplateMapping.orgUnitId= :orgUnitId "
			+ " AND iCPCTemplateMapping.templateId = :templateId"),
	
	
	@NamedQuery(name = "UPDATE_ICPC_TEMPLATE_MAPPING",
	query = "UPDATE ICPCTemplateMapping iCPCTemplateMapping "
			+ "  SET iCPCTemplateMapping.isDeleted = :isDeleted "
			+ " WHERE iCPCTemplateMapping.orgId= :orgId "
			+ " AND iCPCTemplateMapping.orgUnitId= :orgUnitId "
			+ " AND iCPCTemplateMapping.templateId = :templateId"),
	
//	@NamedQuery(name="GET_ICPC_TEMPLATE_MAPPING_COUNT",
//	query=" SELECT count(distinct(iCPCTemplateMapping.templateId)) AS templateId  "
//			+ " FROM ICPCTemplateMapping iCPCTemplateMapping "
//			+ " INNER JOIN iCPCTemplateMapping.templateMaster templateMaster "
////			+ " WHERE iCPCTemplateMapping.orgId=:orgId"
////			+ " AND iCPCTemplateMapping.orgUnitId=:orgUnitId"
//		),
})
@NamedNativeQueries({
	@NamedNativeQuery(name="GET_LIST_OF_ICPC_SYSTEMS",
				query="SELECT icpcSystem.icpc_system_id AS \"icpcSystemId\" ,"
						+ "  icpcSystem.icpc_system_desc AS \"icpcSystemDesc\" "
						+ " FROM emr.m_icpc_system icpcSystem "
						+ " ORDER BY icpcSystem.icpc_system_desc ASC "
					),
	
	@NamedNativeQuery(name="GET_ICPC_TEMPLATE_MAPPING_COUNT",
	query="SELECT count(distinct(ism.template_id)) AS \"templateId\" "
			+ " FROM public.t_icpc_template_mapping ism "
			+ " INNER JOIN public.t_template_master tm on ism.template_id=tm.template_id "
			+ " WHERE ism.org_id=:orgId "
			+ " AND ism.org_unit_id=:orgUnitId "
			+ " AND ism.status!='D'"
		),
	
	@NamedNativeQuery(name="GET_ICPC_SYSTEMS_CODES_BY_SYSTEM_ID",
	query="SELECT  icpc_mst.icpc_id AS \"mIcpcId\", icpc_mst.icpc_system_id AS \"mIcpcSystemId\",  "
			+ " icpc_mst.icpc_desc AS \"icpcDesc\", icpc_syst.icpc_system_desc AS \"icpcSystemDesc\"  "
			+ " FROM emr.m_icpc icpc_mst  "
			+ " inner join emr.m_icpc_system icpc_syst on icpc_syst.icpc_system_id=icpc_mst.icpc_system_id  "
			+  " where icpc_mst.status = 'A'  "
			+ " and icpc_mst.icpc_system_id IN (:systemIdList)  "
			+ " ORDER BY icpc_mst.icpc_desc ASC " 	
		),
	
	@NamedNativeQuery(name="GET_TEMPLATE_LIST_FOR_ICPC", 
	query=" select distinct (tmp_mst.template_id) as \"templateId\", "
		+ " tmp_mst.title as \"title\" "
		+ " from public.t_template_master tmp_mst "
		+ " where tmp_mst.template_type_id =4"
		+ " and tmp_mst.org_id= :orgId"
		+ " and tmp_mst.unit_id= :orgUnitId"
		+ " and tmp_mst.status='A' "
		+ " ORDER BY tmp_mst.title ASC "
		),
	
	
})
@Entity
@Table(name = "t_icpc_template_mapping", schema = "public")
@SequenceGenerator(name = "seq_t_icpc_template_mapping", sequenceName = "public.seq_t_icpc_template_mapping", allocationSize = 1)
public class ICPCTemplateMapping {
	@Id
	@Column(name = "icpc_template_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_icpc_template_mapping")
	private Integer icpcTemplateId;
	
	@Column(name = "template_id")
	private Integer templateId;
	
	@Column(name = "m_icpc_system_id")
	private Integer mIcpcSystemId;
	
	@Column(name = "m_icpc_id")
	private Integer mIcpcId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "m_icpc_system_id", insertable = false, updatable = false)
//	private IcpcSystemMaster icpcSystemMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "template_id", insertable = false, updatable = false)
	private TemplateMaster templateMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "m_icpc_system_id", insertable = false, updatable = false)
	private IcpcSystemMaster icpcSystemMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "m_icpc_id", insertable = false, updatable = false)
	private Icpc icpcMaster;

	public Integer getIcpcTemplateId() {
		return icpcTemplateId;
	}

	public void setIcpcTemplateId(Integer icpcTemplateId) {
		this.icpcTemplateId = icpcTemplateId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getmIcpcSystemId() {
		return mIcpcSystemId;
	}

	public void setmIcpcSystemId(Integer mIcpcSystemId) {
		this.mIcpcSystemId = mIcpcSystemId;
	}

	public Integer getmIcpcId() {
		return mIcpcId;
	}

	public void setmIcpcId(Integer mIcpcId) {
		this.mIcpcId = mIcpcId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
