package com.param.CSSDMaster.model;

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

import com.param.entity.model.master.Store;
import com.param.global.model.DoctorMaster;
import com.param.global.model.OtimsUserMaster;

@NamedQueries({
		@NamedQuery(name = "GET_KIT_LIST", query = "SELECT kitMaster.kitId as kitId, " + "kitMaster.name as name, "
				+ "kitMaster.code as code,kitMaster.status as status, " 
				+ "kitMaster.surgeon as surgeon, "
				+ "concat('Dr. ',dctMst.firstName,' ',dctMst.middleName,' ',dctMst.lastName) as surgeonName, "
				+ "kitMaster.created_by as created_by,"
				+ "to_char(kitMaster.created_date,'dd/MM/yyyy') as created_date,"
				+ "kitMaster.updated_by as updated_by,"
				+ "to_char(kitMaster.updated_date,'dd/MM/yyyy') as updated_date "
				+ "FROM KitMaster as kitMaster "
				+ "LEFT JOIN kitMaster.doctorMaster as dctMst "
				+ "WHERE "
				+ " kitMaster.organizationId=:organizationId and kitMaster.unitId=:unitId "),
		
		@NamedQuery(name = "GET_ACTIVE_KIT_LIST", query = "SELECT kitMaster.kitId as kitId, " + "kitMaster.name as name, "
				+ "kitMaster.code as code,kitMaster.status as status, " 
				+ "kitMaster.surgeon as surgeon, " 
				+ "kitMaster.created_by as created_by,"
				+ "to_char(kitMaster.created_date,'dd/MM/yyyy') as created_date,"
				+ "kitMaster.updated_by as updated_by,"
				+ "to_char(kitMaster.updated_date,'dd/MM/yyyy') as updated_date " 
				+ "FROM KitMaster as kitMaster "
				+ "WHERE kitMaster.status='A'"
				+ "and kitMaster.organizationId=:organizationId and kitMaster.unitId=:unitId "),
		
		@NamedQuery(name = "GET_KIT_BY_KIT_ID", query = "SELECT kitMaster.kitId as kitId, " + "kitMaster.name as name, "
				+ "kitMaster.code as code, "
				+ "kitMaster.created_by as created_by,"
				+ "to_char(kitMaster.created_date,'dd/MM/yyyy') as created_date,"
				+ "kitMaster.updated_by as updated_by,"
				+ "to_char(kitMaster.updated_date,'dd/MM/yyyy') as updated_date "
				+ "FROM KitMaster as kitMaster "
				+ "WHERE kitMaster.kitId=:kitId "
				+ "and kitMaster.organizationId=:organizationId "
				+ "and kitMaster.unitId=:unitId "),
		
		@NamedQuery(name = "SEARCH_KIT_LIST_BY_KIT_NAME", query = "SELECT kitMaster.kitId as kitId, "
				+ "kitMaster.name as name, " 
				+ "kitMaster.code as code, kitMaster.isInstrument as isInstrument, "
				+ "kitMaster.surgeon as surgeon, "
				+ "kitMaster.created_by as created_by,"
				+ "to_char(kitMaster.created_date,'dd/MM/yyyy') as created_date,"
				+ "kitMaster.updated_by as updated_by,"
				+ "to_char(kitMaster.updated_date,'dd/MM/yyyy') as updated_date "
				+ "FROM KitMaster as kitMaster WHERE ( lower(kitMaster.name) LIKE :name or lower(kitMaster.code) LIKE :name ) "
				+ "and kitMaster.organizationId=:organizationId and kitMaster.unitId=:unitId  "), 
		
		@NamedQuery(name = "SEARCH_INSTURMENT_LIST_BY_KIT_NAME", query = "SELECT kitMaster.kitId as kitId, "
				+ "kitMaster.name as name, "
				+ "kitMaster.code as code,"
				+ " kitMaster.isInstrument as isInstrument, " 
				+ "kitMaster.created_by as created_by,"
				+ "to_char(kitMaster.created_date,'dd/MM/yyyy') as created_date,"
				+ "kitMaster.updated_by as updated_by,"
				+ "to_char(kitMaster.updated_date,'dd/MM/yyyy') as updated_date "
				+ "FROM KitMaster as kitMaster WHERE ( lower(kitMaster.name) LIKE :name or lower(kitMaster.code) LIKE :name ) "
				+ "and kitMaster.organizationId=:organizationId and kitMaster.unitId=:unitId  "),
		
		@NamedQuery(name = "SEARCH_KIT_LIST_BY_KIT_NAME_AND_CODE", query = "SELECT kitMaster.kitId as kitId, "
				+ "kitMaster.name as name, " + "kitMaster.code as code, "
				+ "kitMaster.created_by as created_by,"
				+ "to_char(kitMaster.created_date,'dd/MM/yyyy') as created_date,"
				+ "kitMaster.updated_by as updated_by,"
				+ "to_char(kitMaster.updated_date,'dd/MM/yyyy') as updated_date "
				+ "FROM KitMaster as kitMaster WHERE kitMaster.name=:name and kitMaster.code=:code "
				+ "and kitMaster.organizationId=:organizationId and kitMaster.unitId=:unitId  "),
		
		
		
		
		@NamedQuery(name="GET_ALL_SURGEON_LIST",
		query="SELECT dctMst.doctorId as doctorId, dctMst.firstName as firstName, "
				+ "dctMst.lastName as lastName, "
				+ "concat('Dr. ',dctMst.firstName,' ',dctMst.middleName,' ',dctMst.lastName) as doctorName "
				+ "FROM DoctorSpecialityMapper doctSpemppr "
				+ "INNER JOIN doctSpemppr.doctorMaster dctMst "
				+ "INNER JOIN doctSpemppr.specialityMaster spl "
				+ "WHERE spl.isSurgicalCode='Y' "
				+ "and dctMst.organizationId=:organizationId and doctSpemppr.unitId=:unitId "),
		
		/*
		 * @NamedQuery(name = "SEARCH_INSTRUMENT_AS_KIT_BY_NAME", query =
		 * "SELECT inst.id as kitId," + " inst.name as name," + " inst.code as code " +
		 * " FROM Item inst where (isCssd=true or isSterilize=true) AND " +
		 * " ( lower(inst.name) LIKE :name  or lower(inst.code) LIKE :name )" +
		 * " AND ( inst.organizationId=:organizationId and inst.unitId=:unitId  ) " )
		 */
				




})

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_KIT_DETAILS_BY_ID_AND_TYPE",
					query = "  select kitDetails.kit_id as \"kitId\",kitDetails.item_id as \"itemId\", kitDetails.quantity as \"qty\" ," + 
							" item.name as \"itemName\", item.code as \"itemCode\"," + 
							" item.manufacturer_id as \"manufacturerId\", " + 
							" manufacture.name as \"manufacturerName\", " + 
							" item.is_lasa as \"isLasa\", " +
							" tax.tax_percentage as \"taxPercentage\", " +
							" tax.name as \"taxName\", " + 
							" item.p_tax_id as \"taxId\" ," + 
							" ( case when item.generic_id is null " + 
							" then proCat.asset_category_id" + 
							" else proCat1.asset_category_id end ) as \"assetCategoryId\"," + 
							"  ( case when item.product_category_id is null " + 
							" then genric.product_category_id" + 
							" else  item.product_category_id end) as \"productCategoryId\" " + 
							" from public.m_kit kit  " + 
							" inner join public.m_kit_detail  kitDetails on kit.id = kitDetails.kit_id " + 
							" inner join public.m_item item on kitDetails.item_id = item.id  " + 
							" inner join public.m_tax tax on item.s_tax_id=tax.id and (tax.is_deleted=false and tax.is_active=true) " + 
							" inner join public.m_manufacturer manufacture on item.manufacturer_id = manufacture.id " + 
							" left join public.m_generic genric on item.generic_id = genric.id " + 
							" left join public.m_product_category proCat on item.product_category_id  = proCat.id " + 
							" left join public.m_product_category proCat1 on genric.product_category_id  = proCat1.id " + 
							" where kit.kit_type_id= :kitTypeId " + 
							" and kit.id= :kitId " + 
							" and kit.organization_id = :orgId " +
							" and kit.unit_id = :unitId "+
							" and kitDetails.is_active=true " + 
							" and kitDetails.is_deleted = false"),
	
    @NamedNativeQuery(name = "GET_BATCH_DETAILS_BY_ITEM_ID", query = " select  CAST(batch.id as int ) as  \"batchId\", " + 
            " batch.batch_no as\"batchNo\",  batch.expiry_date as \"expiry\", " + 
            " concat(coalesce(batch.batch_no,'') ,' | ' ,TO_CHAR(batch.expiry_date, 'Mon-YYYY')) as \"batchDetails\", " + 
            " batch.selling_price as \"sellingPrice\",  batch.markup as \"markup\",  batch.rate as \"rate\" ," + 
            " stock.available_qty as \"availableStockQuantity\" " + 
            " from inventory.t_batch batch " + 
            " inner join inventory.t_store_stock stock on batch.id = stock.batch_id " + 
            " where batch.item_id= :itemId  and stock.store_id = :storeId " + 
            " and batch.organization_id= :orgId  and batch.unit_id= :unitId " + 
            "     and batch.is_deleted = false " + 
            "    and batch.is_active = true " + 
            "    and batch.is_quarantine = false " + 
           // "    and batch.is_pharmacy = false " + 
            "    and stock.available_qty > 0  and stock.available_qty is not null and stock.total_qty is not null " + 
            "   and (batch.expiry_date > now() or batch.expiry_date is null)  "),

		
		@NamedNativeQuery(name = "GET_UOM_DETAILS_BY_BATCH_ID",
		query = " select  uom.unit as \"uomUnit\", " + 
				" uomType.type as \"uomType\", " + 
				" uom.id as \"uomUnitId\", " + 
				" uomType.id as \"uomTypeId\", " + 
				" concat (uomType.code ,'-',uom.unit)as \"uomDetails\", " + 
				" mppr.conversions as \"conversion\", " + 
				" mppr.ip_uom as \"ipUom\", " + 
				" mppr.op_uom  as \"opUom\", " + 
				" mppr.store_uom  as \"storeUom\" " + 
				" from inventory.t_batch_uom_mapper  mppr " + 
				" inner join public.m_uom_unit uom on uom.id = mppr.uom_unit_id " + 
				" inner join public.m_uom_type uomType on uomType.id = mppr.uom_type_id   "
				+ " where mppr.batch_id = :batchId "
				+ " and mppr.unit_id = :unitId "
				+ " and mppr.organization_id = :orgId order by uomType.id "),
})
@Entity
@Table(name = "m_kit_master", schema = "cssd")
@SequenceGenerator(name = "m_kit_master_kit_id_seq", sequenceName = "cssd.m_kit_master_kit_id_seq", allocationSize = 1)
public class KitMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_kit_master_kit_id_seq")
	@Column(name = "kit_id")
	private Integer kitId;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "surgeon")
	private Integer surgeon;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
   @Column(name = "is_active") 
   private Boolean is_active;
  
   @Column(name = "is_deleted")
   private Boolean is_deleted;
	 

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_date")
	private Date updated_date;
	
	@Column(name = "status")
	private Character status;
	
	/*
	 * @Column(name = "store_id") private Integer storeId;
	 * 
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "store_id", insertable = false, updatable = false) private
	 * Store store;
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "surgeon", insertable = false, updatable = false)
	private DoctorMaster doctorMaster;
	
	@Column(name = "is_instrument")
	private Boolean isInstrument;

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getCreated_date() {
		return created_date;
	}
	

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "surgeon", insertable = false, updatable = false)
	private OtimsUserMaster otimsUserMaster;

	public OtimsUserMaster getOtimsUserMaster() {
		return otimsUserMaster;
	}

	public void setOtimsUserMaster(OtimsUserMaster otimsUserMaster) {
		this.otimsUserMaster = otimsUserMaster;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSurgeon() {
		return surgeon;
	}

	public void setSurgeon(Integer surgeon) {
		this.surgeon = surgeon;
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

	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}

	public Boolean getIsInstrument() {
		return isInstrument;
	}

	public void setIsInstrument(Boolean isInstrument) {
		this.isInstrument = isInstrument;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}
	
	
	

	// public Set<KitEquipmentMapper> getKitEquipmentMappersList() {
	// return kitEquipmentMappersList;
	// }
	//
	// public void setKitEquipmentMappersList(Set<KitEquipmentMapper>
	// kitEquipmentMappersList) {
	// this.kitEquipmentMappersList = kitEquipmentMappersList;
	// }

}
