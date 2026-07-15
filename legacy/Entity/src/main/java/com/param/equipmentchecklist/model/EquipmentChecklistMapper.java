package com.param.equipmentchecklist.model;

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

import com.param.adt.master.global.model.OtimsRoleMaster;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.ProductCategory;


@NamedQueries({
	
	/*@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MASTER",
		query="SELECT mstr.checklistEquipmentMasterId as checklistEquipmentMasterId "
					+ " FROM EquipmentChecklist mstr"
					+ " WHERE  mstr.organizationId= :organizationId"
					+ " AND mstr.unitId= :unitId"
					+ " AND lower(mstr.checklistCode)= :checklistCode"
					+ " AND lower(mstr.checklistName)= :checklistName"
					),*/
	
	//added by minesh for save EquipmentChecklistMapper.
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MAPPER_MASTER",
			query="SELECT mstr.checklistEquipmentMapperMasterId as checklistEquipmentMapperMasterId "
						+ " FROM EquipmentChecklistMapper mstr"
						+ " WHERE  mstr.organizationId= :organizationId"
						+ " AND mstr.unitId= :unitId"
						+ " AND lower(mstr.itemCode)= :itemCode"
						+ " AND lower(mstr.itemName)= :itemName"
						),//end EquipmentChecklistMapper.
	
	//added by minesh for update EquipmentChecklistMapper.
		@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MAPPER_MASTER_NOT_ID",
				query="SELECT mstr.checklistEquipmentMapperMasterId as checklistEquipmentMapperMasterId "
							+ " FROM EquipmentChecklistMapper mstr"
							+ " WHERE  mstr.organizationId= :organizationId"
							+ " AND mstr.unitId= :unitId"
							+ " AND mstr.checklistEquipmentMapperMasterId != :checklistEquipmentMapperMasterId"
							+ " AND lower(mstr.itemCode)= :itemCode"
							+ " AND lower(mstr.itemName)= :itemName"
							),//end EquipmentChecklistMapper.
	
	@NamedQuery(name="GET_CHECKLIST_EQUIPMENT_MAPPER_MASTER_LIST",
	query="SELECT mstr.checklistEquipmentMapperMasterId as checklistEquipmentMapperMasterId , " 
				+ " mstr.checklistEquipmentMasterId as checklistEquipmentMasterId , "	
				+ " equipmentChecklist.checklistName as checklistEquipmentName, "
				+ " mstr.assetCategoryId as assetCategoryId , "
				+ " assetCategory.category as assetCategoryName, "
				+ " mstr.productCategoryId as productCategoryId, "
				+ " productCategory.category as productCategoryName, "
				+ " mstr.itemCode as itemCode, "
				+ " mstr.itemName as itemName, "
				+ " mstr.status as status "
				+ " FROM EquipmentChecklistMapper mstr "
				+ " INNER JOIN  mstr.equipmentChecklist equipmentChecklist "
				+ " INNER JOIN  mstr.assetCategory assetCategory "
				+ " INNER JOIN  mstr.productCategory productCategory "
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				)/*,
	
	@NamedQuery(name="GET_CHECKLIST_MASTER_LIST_BY_STAGE_ID",
	query="SELECT mstr.checklistMasterId as checklistMasterId , " 
				+ " mstr.code as code , "
				+ " mstr.roleId as roleId , "
				+ " mstr.stageId as stageId , "
				+ " mstr.isWho as isWho , "
				+ " mstr.description as description, "
				+ " otimsRoleMaster.roleName as roleName, "
				+ " mstr.status as status "
				+ " FROM ChecklistMaster mstr "
				+ " INNER JOIN  mstr.otimsRoleMaster otimsRoleMaster  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.stageId= :stageId "
				+ " AND mstr.status= 'A'"
				)*/
})

@Entity
@Table(name = "m_checklist_equipment_master_mapper", schema = "equipment")
@SequenceGenerator(name = "checklist_equipment_mapper_master_seq", sequenceName = "equipment.checklist_equipment_mapper_master_seq", allocationSize = 1)
public class EquipmentChecklistMapper {

	@Id
	@Column(name = "checklist_equipment_mapper_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_equipment_mapper_master_seq")
	private Integer checklistEquipmentMapperMasterId ;
	
	@Column(name = "checklist_equipment_master_id")
	private Integer checklistEquipmentMasterId;

	@Column(name = "asset_category_id")
	private Integer assetCategoryId;

	@Column(name = "product_category_id")
	private Integer productCategoryId;
	
	@Column(name = "item_code")
	private String itemCode;
	
	@Column(name = "item_name")
	private String itemName;

	@Column(name = "status")
	private Character status;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "checklist_equipment_master_id", insertable = false, updatable = false)
	private EquipmentChecklist equipmentChecklist;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_category_id", insertable = false, updatable = false)
	private AssetCategory assetCategory;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_category_id", insertable = false, updatable = false)
	private ProductCategory productCategory;
	

	public Integer getChecklistEquipmentMapperMasterId() {
		return checklistEquipmentMapperMasterId;
	}

	public void setChecklistEquipmentMapperMasterId(Integer checklistEquipmentMapperMasterId) {
		this.checklistEquipmentMapperMasterId = checklistEquipmentMapperMasterId;
	}

	public Integer getChecklistEquipmentMasterId() {
		return checklistEquipmentMasterId;
	}

	public void setChecklistEquipmentMasterId(Integer checklistEquipmentMasterId) {
		this.checklistEquipmentMasterId = checklistEquipmentMasterId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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
