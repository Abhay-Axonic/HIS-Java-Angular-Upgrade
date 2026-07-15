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
		@NamedNativeQuery(name = "GET_IMPLANT_DETAILS_LIST", query = "SELECT implant_details_id as \"implantDetailsId\", "
				+ "item_id as \"itemId\", " + "kit_id as \"kitId\", " + "patient_id as \"patientId\", "
				+ "procedure_order_id as \"procedureOrderId\", " + "qty as \"qty\","
				// +"itm.name as \"itemName\", "
				+ "encounter_id as \"encounterId\", " + "admission_id as \"admissionId\","
				+ "to_char(imt_details.created_date,'YYYY-MM-DD') as \"createdDate\" "
				+ "FROM otims.t_implant_details imt_details "),
		// +"inner join public.m_item itm on imt_details.item_id=itm.id "),

		@NamedNativeQuery(name = "GET_IMPLANT_DETAILS", query = "SELECT implant_details_id as \"implantDetailsId\", "
				+ "item_id as \"itemId\", " + "kit_id as \"kitId\", " + "patient_id as \"patientId\", "
				+ "qty as \"qty\"," + "itm.name as \"itemName\", " + "encounter_id as \"encounterId\", "
				+ "admission_id as \"admissionId\","
				+ "to_char(imt_details.created_date,'YYYY-MM-DD') as \"createdDate\" "
				+ "FROM otims.t_implant_details imt_details "
				+ "inner join public.m_item itm on imt_details.item_id=itm.id "),

		@NamedNativeQuery(name = "GET_KIT_DETAILS", query = "SELECT  implant_details_id as \"implantDetailsId\", "
				+ "imt_details.kit_id as \"kitId\", " + "kitDetail.item_id as \"itemId\", "
				+ "kit.description as\"kitName\", " + "patient_id as \"patientId\", qty as \"qty\", "
				+ "itm.name as \"itemName\", encounter_id as \"encounterId\", " + "admission_id as \"admissionId\", "
				+ "to_char(imt_details.created_date,'YYYY-MM-DD') as \"createdDate\" "
				+ "FROM otims.t_implant_details imt_details "
				+ "inner join public.m_kit kit on imt_details.kit_id = kit.id "
				+ "inner join public.m_kit_detail kitDetail on kit.id = kitDetail.kit_id "
				+ "inner join public.m_item itm on kitDetail.item_id=itm.id "),

		@NamedNativeQuery(name = "SEARCH_IMPLANT_ITEMS_BY_NAME", query = "select item.id as \"itemId\", "
				+ "item.code as \"itemCode\", " + "item.name as \"itemName\" " + "from public.m_item item "
				+ "where (lower(item.name) LIKE  :itemName OR ( lower(item.code) LIKE :itemName)) "
				+ "AND item.organization_id= :organizationId " + "AND item.unit_id = :unitId "
				+ "AND item.is_active = true " + "AND item.is_deleted = false " + "AND item.is_implant = true "),

		@NamedNativeQuery(name = "SEARCH_IMPLANT_KIT_BY_NAME", query = "select kit.id as \"kitId\", "
				+ "kit.code as \"kitCode\", " + "kit.description as \"kitName\", " + "kit.kit_type_id as \"kitTypeId\" "
				// +"kit.description as \"description\" "
				+ "from public.m_kit kit "
				+ "where (lower(kit.description) LIKE  :kitName OR ( lower(kit.code) LIKE :kitName)) "
				+ "AND kit.organization_id= :organizationId " + "AND kit.unit_id = :unitId "
				+ "AND kit.is_active = true " + "AND kit.is_deleted = false " + "AND kit.kit_type_id = 2 "),

		@NamedNativeQuery(name = "GET_IMPLANT_ITEMS", query = "SELECT implant_details.implant_details_id as \"implantDetailsId\", "
				+ "implant_details.item_id as \"itemId\", " + "implant_details.kit_id as \"kitId\", "
				+ "implant_details.patient_id as \"patientId\", " + "implant_details.qty as \"qty\", "
				+ "itm.name as \"itemName\", " + "kit.description as \"kitName\", "
				+ "itm.is_consignment as \"isConsignment\", " + "itm.is_implant as \"isImplant\", "
				+ "itm.is_kit as \"isKit\", " + "itm.is_consumable as \"isConsumable\", "
				+ "implant_details.status as \"status\", " + "implant_details.organization_id as \"organizationId\", "
				+ "implant_details.unit_id as \"unitId\", " + "implant_details.created_by as \"createdBy\", "
				+ "implant_details.updated_by as \"updatedBy\", "
				+ " to_char(implant_details.created_date, 'dd-MM-yyyy HH:ss:mm')as \"createdDate\", "
				+ " to_char(implant_details.updated_date, 'dd-MM-yyyy HH:ss:mm')as \"updatedDate\", "
				// +"kit.id as \"kitId\", "
				+ "kitDetail.uom_type_id as \"uomTypeId\", " + "kitDetail.uom_unit_id as \"uomUnitId\", "
				+ "kitDetail.is_active as \"isActive\", " + "kitItem.name as \"kitItemName\","
				+ "kitDetail.quantity as \"itemQuantity\"," + "kitDetail.item_id as \"kitItemId\","
				+ "implant_details.encounter_id as \"encounterId\", "
				+ "implant_details.admission_id as \"admissionId\", "
				+ "implant_details.procedure_order_id as \"procedureOrderId\", "
				+ " itm.code as \"itemCode\", "
				+ " kitItem.code as \"kitItemCode\", "
				+ " itmTax.id as \"itemTaxId\", "
				+ "  (case when itmTax.tax_percentage !=null then "
				+ "  itmTax.tax_percentage "
				+ "    else "
				+ "    0.0 "
				+ "  end) as \"itemTaxPercent\", "
				+ "  (case when kitTax.tax_percentage!=null then "
				+ "  kitTax.tax_percentage "
				+ "    else "
				+ "    0.0 "
				+ "  end) as \"kitTaxPercent\", "
				+ " itmTax.name as \"itemTaxName\", "
				+ " kitTax.id as \"kitTaxId\", "
				+ " kitTax.name as \"kitTaxName\", "
				+ " itemProct.id as \"itemProductCategoryId\", "
				+ " kitProct.id as \"kitProductCategoryId\", "
				+ " itemProct.asset_category_id as \"itemAssetCategoryId\", "
				+ " kitProct.asset_category_id as \"kitAssetCategoryId\"  "
				+ "FROM otims.t_implant_details implant_details "
				+ "left join public.m_item itm on implant_details.item_id=itm.id "
				+ "left join public.m_kit kit on implant_details.kit_id=kit.id "
				+ "left join public.m_kit_detail kitDetail on kit.id = kitDetail.kit_id "
				+ "left join public.m_item kitItem on kitDetail.item_id = kitItem.id "
				+ "left join public.m_tax itmTax on itmTax.id = itm.p_tax_id "
				+ "left join public.m_tax kitTax on kitTax.id = kitItem.p_tax_id "
				+ "left join public.m_product_category itemProct on itemProct.id = itm.product_category_id "
				+ "left join public.m_product_category kitProct on kitProct.id = kitItem.product_category_id "
				), })
@Entity
@Table(name = "t_implant_details", schema = "otims")
@SequenceGenerator(name = "t_implant_details_seq", sequenceName = "otims.t_implant_details_seq", allocationSize = 1)
public class ImplantDetails {

	@Id
	@Column(name = "implant_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_implant_details_seq")
	private Integer implantDetailsId;

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

	public Integer getImplantDetailsId() {
		return implantDetailsId;
	}

	public void setImplantDetailsId(Integer implantDetailsId) {
		this.implantDetailsId = implantDetailsId;
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
