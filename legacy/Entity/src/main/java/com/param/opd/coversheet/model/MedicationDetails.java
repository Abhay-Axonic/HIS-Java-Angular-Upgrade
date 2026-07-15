package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({

		/*
		 * @NamedNativeQuery(name = "GET_MEDICATION_ITEMS",
		 * query="SELECT medication_details.medication_details_id as \"medicationDetailsId\", "
		 * +"medication_details.item_id as \"itemId\", "
		 * +"medication_details.kit_id as \"kitId\", "
		 * +"medication_details.patient_id as \"patientId\", "
		 * +"medication_details.qty as \"qty\"," +"itm.name as \"itemName\", "
		 * +"medication_details.encounter_id as \"encounterId\", "
		 * +"medication_details.admission_id as \"admissionId\","
		 * +"medication_details.procedure_order_id as \"procedureOrderId\","
		 * +"to_char(medication_details.created_date,'YYYY-MM-DD') as \"createdDate\" "
		 * +"FROM otims.t_medication_details medication_details "
		 * +"inner join public.m_item itm on medication_details.item_id=itm.id "),
		 */

		@NamedNativeQuery(name = "GET_MEDICATION_ITEMS", query = "SELECT medication_details.medication_details_id as \"medicationDetailsId\", "
				+ "medication_details.item_id as \"itemId\", " + "medication_details.kit_id as \"kitId\", "
				+ "medication_details.patient_id as \"patientId\", " + "medication_details.qty as \"qty\", "
				+ "itm.name as \"itemName\", " + "itm.is_consignment as \"isConsignment\", "
				+ "itm.is_implant as \"isImplant\", " + "itm.is_kit as \"isKit\", " + "kit.description as \"kitName\", "
				+ "itm.is_consumable as \"isConsumable\", " + "medication_details.status as \"status\", "
				+ "medication_details.organization_id as \"organizationId\", "
				+ "medication_details.unit_id as \"unitId\", " + "medication_details.created_by as \"createdBy\", "
				+ "medication_details.updated_by as \"updatedBy\", "
				+ " to_char(medication_details.created_date, 'dd-MM-yyyy HH:ss:mm')as \"createdDate\", "
				+ " to_char(medication_details.updated_date, 'dd-MM-yyyy HH:ss:mm')as \"updatedDate\", "
				// +"kit.id as \"kitId\", "
				+ "kitDetail.uom_type_id as \"uomTypeId\", " + "kitDetail.uom_unit_id as \"uomUnitId\", "
				+ "kitDetail.is_active as \"isActive\", " + "kitItem.name as \"kitItemName\","
				+ "kitDetail.quantity as \"itemQuantity\"," + "kitDetail.item_id as \"kitItemId\","
				+ "medication_details.encounter_id as \"encounterId\", "
				+ "medication_details.admission_id as \"admissionId\", "
				+ "medication_details.procedure_order_id as \"procedureOrderId\" "
				+ "FROM otims.t_medication_details medication_details "
				+ "left join public.m_item itm on medication_details.item_id=itm.id "
				+ "left join public.m_kit kit on medication_details.kit_id=kit.id "
				+ "left join public.m_kit_detail kitDetail on kit.id = kitDetail.kit_id "
				+ "left join public.m_item kitItem on kitDetail.item_id = kitItem.id "),

		@NamedNativeQuery(name = "GET_MEDICATION_ITEMS_ON_OTIMS_DASHBOARD", query = "SELECT medication_details.medication_details_id as \"medicationDetailsId\", "
				+ "medication_details.item_id as \"itemId\", " + "medication_details.kit_id as \"kitId\", "
				+ "medication_details.patient_id as \"patientId\", " + "medication_details.qty as \"qty\", "
				+ "itm.name as \"itemName\", " + "itm.is_implant as \"isImplant\", " + "'' as \"kitName\", "
				+ "itm.is_consumable as \"isConsumable\", " + "medication_details.status as \"status\", "
				+ "medication_details.organization_id as \"organizationId\", "
				+ "medication_details.unit_id as \"unitId\", " + "medication_details.created_by as \"createdBy\", "
				+ "medication_details.updated_by as \"updatedBy\", "
				+ " to_char(medication_details.created_date, 'dd-MM-yyyy HH:ss:mm')as \"createdDate\", "
				+ " to_char(medication_details.updated_date, 'dd-MM-yyyy HH:ss:mm')as \"updatedDate\",  "
				+ "(select batchuommapper.uom_type_id  from inventory.t_batch_uom_mapper batchuommapper  "
				+ "where batchuommapper.batch_id = (select batchmaster.id from inventory.t_batch batchmaster where batchmaster.item_id =medication_details.item_id  limit 1 )  limit 1) "
				+ " as \"uomTypeId\","
				+ "(select batchuommapper.uom_unit_id  from inventory.t_batch_uom_mapper batchuommapper  "
				+ "where batchuommapper.batch_id = (select batchmaster.id from inventory.t_batch batchmaster where batchmaster.item_id =medication_details.item_id  limit 1 )  limit 1) "
				+ "as \"uomUnitId\"," + "true as \"isActive\", " + "'' as \"kitItemName\"," + "0 as \"itemQuantity\","
				+ "0 as \"kitItemId\"," + "medication_details.encounter_id as \"encounterId\", "
				+ "medication_details.admission_id as \"admissionId\", "
				+ "medication_details.procedure_order_id as \"procedureOrderId\", "
				+ " itm.code as \"itemCode\", "
				+ " tax.id as \"taxId\", "
				+ " tax.name as \"taxName\", "
				+ " tax.tax_percentage as \"taxPercent\", "
				+ " generic.name as \"genericName\", "
				+ " generic.product_category_id as \"productCategoryId\", "
				+ " proct.asset_category_id as \"assetCategoryId\"  "
				+ "FROM otims.t_medication_details medication_details "
				+ "inner join public.m_item itm on medication_details.item_id=itm.id "
				+ "inner join public.m_tax tax on tax.id = itm.p_tax_id "
				+ "inner join public.m_generic generic on generic.id = itm.generic_id "
				+ "inner join public.m_product_category proct on proct.id = generic.product_category_id "
				+ " where medication_details.procedure_order_id = :procedureId  "
				+ " AND medication_details.is_exported = false and medication_details.organization_id = :orgId  and medication_details.unit_id = :unitId "
				+ " union  " 
				+ " SELECT medication_details.medication_details_id as \"medicationDetailsId\", "
				+ "medication_details.item_id as \"itemId\", " + "medication_details.kit_id as \"kitId\", "
				+ "medication_details.patient_id as \"patientId\", " + "medication_details.qty as \"qty\", "
				+ "'' as \"itemName\", " + "kitItem.is_implant as \"isImplant\", " + "kit.description as \"kitName\", "
				+ "kitItem.is_consumable as \"isConsumable\", " + "medication_details.status as \"status\", "
				+ "medication_details.organization_id as \"organizationId\", "
				+ "medication_details.unit_id as \"unitId\", " + "medication_details.created_by as \"createdBy\", "
				+ "medication_details.updated_by as \"updatedBy\", "
				+ " to_char(medication_details.created_date, 'dd-MM-yyyy HH:ss:mm')as \"createdDate\", "
				+ " to_char(medication_details.updated_date, 'dd-MM-yyyy HH:ss:mm')as \"updatedDate\",  "
				+ "(select batchuommapper.uom_type_id  from inventory.t_batch_uom_mapper batchuommapper  "
				+ "where batchuommapper.batch_id = (select batchmaster.id from inventory.t_batch batchmaster where batchmaster.item_id =kitDetail.item_id  limit 1 ) limit 1)"
				+ " as \"uomTypeId\","
				+ "(select batchuommapper.uom_unit_id  from inventory.t_batch_uom_mapper batchuommapper  "
				+ "where batchuommapper.batch_id = (select batchmaster.id from inventory.t_batch batchmaster where batchmaster.item_id =kitDetail.item_id  limit 1 ) limit 1)"
				+ " as \"uomUnitId\"," + "kitDetail.is_active as \"isActive\", " + "kitItem.name as \"kitItemName\","
				+ "kitDetail.quantity as \"itemQuantity\"," + "kitDetail.item_id as \"kitItemId\","
				+ "medication_details.encounter_id as \"encounterId\", "
				+ "medication_details.admission_id as \"admissionId\", "
				+ "medication_details.procedure_order_id as \"procedureOrderId\", "
				+ " kitItem.code as \"itemCode\", "
				+ " tax.id as \"taxId\", "
				+ " tax.name as \"taxName\", "
				+ " tax.tax_percentage as \"taxPercent\", "
				+ " generic.name as \"genericName\", "
				+ " generic.product_category_id as \"productCategoryId\", "
				+ " proct.asset_category_id as \"assetCategoryId\"  "
				+ "FROM otims.t_medication_details medication_details "
				+ "inner join public.m_kit kit on medication_details.kit_id=kit.id "
				+ "inner join public.m_kit_detail kitDetail on kit.id = kitDetail.kit_id "
				+ "inner join public.m_item kitItem on kitDetail.item_id = kitItem.id "
				+ "inner join public.m_tax tax on tax.id = kitItem.p_tax_id "
				+ "inner join public.m_generic generic on generic.id = kitItem.generic_id "
				+ "inner join public.m_product_category proct on proct.id = generic.product_category_id "
				+ "where medication_details.procedure_order_id = :procedureId "
				+ " AND medication_details.is_exported = false   and medication_details.organization_id = :orgId  and medication_details.unit_id = :unitId "),
		
		
		@NamedNativeQuery(name = "GET_MEDICATION_ITEMS_ON_OT_BILLING", query = "SELECT medication_details.medication_details_id as \"medicationDetailsId\", "
				+ "medication_details.item_id as \"itemId\", " + "medication_details.kit_id as \"kitId\", "
				+ "medication_details.patient_id as \"patientId\", " + "medication_details.qty as \"qty\", "
				+ "itm.name as \"itemName\", " + "itm.is_implant as \"isImplant\", " + "'' as \"kitName\", "
				+ "itm.is_consumable as \"isConsumable\", " + "medication_details.status as \"status\", "
				+ "medication_details.organization_id as \"organizationId\", "
				+ "medication_details.unit_id as \"unitId\", " + "medication_details.created_by as \"createdBy\", "
				+ "medication_details.updated_by as \"updatedBy\", "
				+ " to_char(medication_details.created_date, 'dd-MM-yyyy HH:ss:mm')as \"createdDate\", "
				+ " to_char(medication_details.updated_date, 'dd-MM-yyyy HH:ss:mm')as \"updatedDate\",  "
				+ "(select batchuommapper.uom_type_id  from inventory.t_batch_uom_mapper batchuommapper  "
				+ "where batchuommapper.batch_id = (select batchmaster.id from inventory.t_batch batchmaster where batchmaster.item_id =medication_details.item_id  limit 1 )  limit 1) "
				+ " as \"uomTypeId\","
				+ "(select batchuommapper.uom_unit_id  from inventory.t_batch_uom_mapper batchuommapper  "
				+ "where batchuommapper.batch_id = (select batchmaster.id from inventory.t_batch batchmaster where batchmaster.item_id =medication_details.item_id  limit 1 )  limit 1) "
				+ "as \"uomUnitId\"," + "true as \"isActive\", " + "'' as \"kitItemName\"," + "0 as \"itemQuantity\","
				+ "0 as \"kitItemId\"," + "medication_details.encounter_id as \"encounterId\", "
				+ "medication_details.admission_id as \"admissionId\", "
				+ "medication_details.procedure_order_id as \"procedureOrderId\", "
				+ " itm.code as \"itemCode\", "
				+ " tax.id as \"taxId\", "
				+ " tax.name as \"taxName\", "
				+ " tax.tax_percentage as \"taxPercent\", "
				+ " generic.name as \"genericName\", "
				+ " generic.product_category_id as \"productCategoryId\", "
				+ " proct.asset_category_id as \"assetCategoryId\"  "
				+ "FROM otims.t_medication_details medication_details "
				+ "inner join public.m_item itm on medication_details.item_id=itm.id "
				+ "inner join public.m_tax tax on tax.id = itm.p_tax_id "
				+ "inner join public.m_generic generic on generic.id = itm.generic_id "
				+ "inner join public.m_product_category proct on proct.id = generic.product_category_id "
				+ " where medication_details.procedure_order_id = :procedureId  "
				+ " AND medication_details.organization_id = :orgId  and medication_details.unit_id = :unitId "
				+ " union  " 
				+ " SELECT medication_details.medication_details_id as \"medicationDetailsId\", "
				+ "medication_details.item_id as \"itemId\", " + "medication_details.kit_id as \"kitId\", "
				+ "medication_details.patient_id as \"patientId\", " + "medication_details.qty as \"qty\", "
				+ "'' as \"itemName\", " + "kitItem.is_implant as \"isImplant\", " + "kit.description as \"kitName\", "
				+ "kitItem.is_consumable as \"isConsumable\", " + "medication_details.status as \"status\", "
				+ "medication_details.organization_id as \"organizationId\", "
				+ "medication_details.unit_id as \"unitId\", " + "medication_details.created_by as \"createdBy\", "
				+ "medication_details.updated_by as \"updatedBy\", "
				+ " to_char(medication_details.created_date, 'dd-MM-yyyy HH:ss:mm')as \"createdDate\", "
				+ " to_char(medication_details.updated_date, 'dd-MM-yyyy HH:ss:mm')as \"updatedDate\",  "
				+ "(select batchuommapper.uom_type_id  from inventory.t_batch_uom_mapper batchuommapper  "
				+ "where batchuommapper.batch_id = (select batchmaster.id from inventory.t_batch batchmaster where batchmaster.item_id =kitDetail.item_id  limit 1 ) limit 1)"
				+ " as \"uomTypeId\","
				+ "(select batchuommapper.uom_unit_id  from inventory.t_batch_uom_mapper batchuommapper  "
				+ "where batchuommapper.batch_id = (select batchmaster.id from inventory.t_batch batchmaster where batchmaster.item_id =kitDetail.item_id  limit 1 ) limit 1)"
				+ " as \"uomUnitId\"," + "kitDetail.is_active as \"isActive\", " + "kitItem.name as \"kitItemName\","
				+ "kitDetail.quantity as \"itemQuantity\"," + "kitDetail.item_id as \"kitItemId\","
				+ "medication_details.encounter_id as \"encounterId\", "
				+ "medication_details.admission_id as \"admissionId\", "
				+ "medication_details.procedure_order_id as \"procedureOrderId\", "
				+ " kitItem.code as \"itemCode\", "
				+ " tax.id as \"taxId\", "
				+ " tax.name as \"taxName\", "
				+ " tax.tax_percentage as \"taxPercent\", "
				+ " generic.name as \"genericName\", "
				+ " generic.product_category_id as \"productCategoryId\", "
				+ " proct.asset_category_id as \"assetCategoryId\"  "
				+ "FROM otims.t_medication_details medication_details "
				+ "inner join public.m_kit kit on medication_details.kit_id=kit.id "
				+ "inner join public.m_kit_detail kitDetail on kit.id = kitDetail.kit_id "
				+ "inner join public.m_item kitItem on kitDetail.item_id = kitItem.id "
				+ "inner join public.m_tax tax on tax.id = kitItem.p_tax_id "
				+ "inner join public.m_generic generic on generic.id = kitItem.generic_id "
				+ "inner join public.m_product_category proct on proct.id = generic.product_category_id "
				+ "where medication_details.procedure_order_id = :procedureId "
				+ " AND medication_details.organization_id = :orgId  and medication_details.unit_id = :unitId "),


		@NamedNativeQuery(name = "GET_MEDICATION_DETAILS", query = "SELECT medication_details.medication_details_id as \"medicationDetailsId\", "
				+ "medication_details.item_id as \"itemId\", " + "medication_details.kit_id as \"kitId\", "
				+ "medication_details.patient_id as \"patientId\", " + "medication_details.qty as \"qty\", "
				+ "itm.name as \"itemName\", " + "kit.description as \"kitName\", "
				// +"kit.id as \"kitId\", "
				+ "kitItem.name as \"kitItemName\"," + "kitDetail.quantity as \"itemQuantity\","
				+ "medication_details.encounter_id as \"encounterId\", "
				+ "medication_details.admission_id as \"admissionId\", "
				+ "medication_details.procedure_order_id as \"procedureOrderId\", "
				+ "to_char(medication_details.created_date,'YYYY-MM-DD') as \"createdDate\" "
				+ "FROM otims.t_medication_details medication_details "
				+ "left join public.m_item itm on medication_details.item_id=itm.id "
				+ "left join public.m_kit kit on medication_details.kit_id=kit.id "
				+ "left join public.m_kit_detail kitDetail on kit.id = kitDetail.kit_id "
				+ "left join public.m_item kitItem on kitDetail.item_id = kitItem.id ")
		// +"left join public.m_kit_detail kit_dt on itm.id = kit_dt.item_id"),

})

@Entity
@Table(name = "t_medication_details", schema = "otims")
@SequenceGenerator(name = "t_medication_details_seq", sequenceName = "otims.t_medication_details_seq", allocationSize = 1)
public class MedicationDetails {

	@Id
	@Column(name = "medication_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_medication_details_seq")
	private Integer medicationDetailsId;

	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "kit_id")
	private Integer kitId;

	@Column(name = "qty")
	private Integer qty;

	@Column(name = "patient_id")
	private Integer patientId;

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

	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;

	@Column(name = "is_exported")
	private boolean isExported;
	
	@Column(name = "is_extra")
	private Boolean isExtra;

	public Integer getMedicationDetailsId() {
		return medicationDetailsId;
	}

	public void setMedicationDetailsId(Integer medicationDetailsId) {
		this.medicationDetailsId = medicationDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public boolean getIsExported() {
		return isExported;
	}

	public void setIsExported(boolean isExported) {
		this.isExported = isExported;
	}

	public Boolean getIsExtra() {
		return isExtra;
	}

	public void setIsExtra(Boolean isExtra) {
		this.isExtra = isExtra;
	}


}