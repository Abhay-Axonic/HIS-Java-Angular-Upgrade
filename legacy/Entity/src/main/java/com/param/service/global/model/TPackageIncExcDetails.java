package com.param.service.global.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_PACKAGE_INC_EXC_DETAILS_FOR_BILLING_BY_PACKAGE",
					 query = "select "
							 + "	pix.package_inc_exc_id as \"packageIncExcId\", "
							 + "	pix.package_id as \"packageId\", "
							 + "	pix.service_id as \"serviceId\", "
							 + "	pix.is_inclusive_service as \"isInclusiveService\", "
							 + "	pix.is_service_item as \"isServiceItem\", "
							 + "	pix.item_id as \"itemId\", "
							 + "	item.\"name\" as \"itemName\", "
							 + "	item.code  as \"itemCode\", "
							 + "	pix.package_category_consumable_id as \"packageCategoryConsumableId\", "
							 + "	srvtrf.rate as \"apportionedPrice\", "
							 + "	srv.service_standard_name as \"serviceName\", "
							 + "	srv.service_standard_code as \"serviceCode\", "
							 + "	srv.speciality_id as \"specialityId\", "
							 + "	srv.sub_speciality_id as \"subSpecialityId\", "
							 + "	spc.speciality_name as \"specialityName\", "
							 + "	subspc.sub_speciality_name as \"subSpecialityName\","
							 + "    pix.is_groupwise_exlusion as \"isGroupwiseExlusion\","
							 + "	pix.dept_id as \"deptId\", "
							 + "	pix.sub_dept_id as \"subDeptId\" "
							 + "from "
							 + "	service.t_package_inc_exc_details pix "
							 + "left join service.m_service_master srv on "
							 + "	srv.service_master_id = pix.service_id "
							 + "left join doctor.m_speciality_master spc on "
							 + "	spc.speciality_id = srv.speciality_id "
							 + "left join doctor.m_sub_speciality_master subspc on "
							 + "	subspc.sub_speciality_id = srv.sub_speciality_id "
							 + "left join service.m_service_tarrif_master srvtrf on "
							 + "	srvtrf.service_master_id = pix.service_id "
							 + "left join public.m_item item on "
							 + "	pix.item_id = item.id "
							 + "where "
							 + "	pix.package_id = :packageId order by pix.package_inc_exc_id asc"),
	
	
	
	@NamedNativeQuery(name = "GET_PACKAGE_CONSUMPTION_INC_EXC_DETAILS_FOR_BILLING",
	 query =  "select "
			 + "	pcsd.service_id as \"serviceId\", "
			 + "    pcsd.item_id as \"itemId\" , "
			 + "	pcsd.is_inclusive_exclusive as \"isInclusiveService\" , "
			 + "    pcsd.dept_id as \"deptId\", "
			 + "	pcsd.sub_dept_id as \"subDeptId\", "
			 + "	pcsd.product_category_id as \"productCategoryId\" "
			 + " from "
			 + "	billing.t_package_consumption_service_detail pcsd "
			 + " where "
			 + "	pcsd.package_consumption_master_id = :packageConsumptionMasterId "
			 + "	and pcsd.is_inclusive_exclusive = 2 and pcsd.status = 'A' "),

	
	 
	
	
	
	@NamedNativeQuery(name = "GET_PACKAGE_INC_EXC_SERIVCE_BY_PACKAGE_ID",
	 query = "select "
			 + "	pix.package_inc_exc_id as \"packageIncExcId\", "
			 + "	pix.package_id as \"packageId\", "
			 + "	pix.service_id as \"serviceId\", "
			 + "	pix.is_inclusive_service as \"isInclusiveService\", "
			 + "	pix.is_service_item as \"isServiceItem\", "
			 + "	pix.package_category_consumable_id as \"packageCategoryConsumableId\", "
			 + "	srvtrf.rate as \"apportionedPrice\", "
			 + "	pix.service_price as \"servicePrice\", "
			 + "	srv.service_standard_name as \"serviceName\", "
			 + "	srv.service_standard_code as \"serviceCode\", "
			 + "	coalesce(srv.speciality_id,dept_id) as \"specialityId\", "
			 + "	coalesce(srv.sub_speciality_id,sub_dept_id) as \"subSpecialityId\", "
			 + "	spc.speciality_name as \"specialityName\", "
			 + "	subspc.sub_speciality_name as \"subSpecialityName\" ,pix.is_groupwise_exlusion as \"isGroupwiseExlusion\","
			 + "	pix.is_excluded AS \"isExcluded\" "
			 + " from "
			 + "	service.t_package_inc_exc_details pix "
			 + " left join service.m_service_master srv on "
			 + "	srv.service_master_id = pix.service_id "
			 + " left join doctor.m_speciality_master spc on spc.speciality_id = srv.speciality_id  or spc.speciality_id = pix.dept_id "
			 + " left join doctor.m_sub_speciality_master subspc on subspc.sub_speciality_id = srv.sub_speciality_id  or subspc.sub_speciality_id =pix.sub_dept_id "
			 + " left join service.m_service_tarrif_master srvtrf on "
			 + "	srvtrf.service_master_id = pix.service_id "
			 + " where "
			 + "	pix.package_id = :packageId order by pix.package_inc_exc_id asc "),
	
	
	
	@NamedNativeQuery(name = "GET_PACKAGE_INC_EXC_ITEM_BY_PACKAGE_ID",
	 query = "select "
			 + "	pix.package_inc_exc_id as \"packageIncExcId\", "
			 + "	pix.package_id as \"packageId\", "
			 + "	pix.service_id as \"serviceId\", "
			 + "	pix.is_inclusive_service as \"isInclusiveService\", "
			 + "	pix.is_service_item as \"isServiceItem\", "
			 + "	pix.item_id as \"itemId\", "
			 + "	item.\"name\" as \"itemName\", "
			 + "	item.code  as \"itemCode\", "
			 + "	pix.package_category_consumable_id as \"packageCategoryConsumableId\",pix.is_excluded AS \"isExcluded\"  "
			 + "from "
			 + "	service.t_package_inc_exc_details pix "
						/*
						 * + "left join service.m_service_master srv on " +
						 * "	srv.service_master_id = pix.service_id "
						 */
			 + " inner join public.m_item item on "
			 + "	pix.item_id = item.id "
			 + " where "
			 + "	pix.package_id = :packageId order by pix.package_inc_exc_id asc ")
	
})
@Entity
@Table(name="t_package_inc_exc_details",schema="service")
@SequenceGenerator(name="t_seq_package_inc_exc_details",sequenceName="service.t_seq_package_inc_exc_details",allocationSize=1)
public class TPackageIncExcDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_seq_package_inc_exc_details")
	@Column(name="package_inc_exc_id")
	private Integer packageIncExcId;
	
	@Column(name="package_id")
	private Integer packageId;
	
	@Column(name="service_id")
	private Integer serviceId;

	@Column(name="is_inclusive_service")
	private Integer isInclusiveService;
	
	@Column(name="is_service_item")
	private Integer isServiceItem;
	
	@Column(name="item_id")
	private Integer itemId;
	
	@Column(name="package_category_consumable_id")
	private Integer packageCategoryConsumableId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="org_unit_id")
	private Integer orgUnitId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="status")
	private char status;
	
	@Column(name="service_price")
	private Double servicePrice;
	
	@Column(name = "parent_package_id")
	private Integer parentPackageId;
	
	@Column(name = "is_groupwise_exlusion")
	private Integer isGroupwiseExlusion;
	
	@Column(name = "dept_id")
	private Integer deptId;
	
	@Column(name = "sub_dept_id")
	private Integer subDeptId;
	
	// 0 as selected and 1 as de-selected
	@Column(name = "is_excluded")
	private Integer isExcluded;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_category_consumable_id" , insertable = false , updatable = false , nullable = false)
	private TPackageCategoryWiseConsumableDetails tPackageCategoryWiseConsumableDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id" , insertable = false , updatable = false , nullable = false)
	private MPackageMaster mPackageMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id" , insertable = false , updatable = false , nullable = false)
	private ServiceMaster serviceMaster;

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public MPackageMaster getmPackageMaster() {
		return mPackageMaster;
	}

	public void setmPackageMaster(MPackageMaster mPackageMaster) {
		this.mPackageMaster = mPackageMaster;
	}

	public TPackageCategoryWiseConsumableDetails gettPackageCategoryWiseConsumableDetails() {
		return tPackageCategoryWiseConsumableDetails;
	}

	public void settPackageCategoryWiseConsumableDetails(
			TPackageCategoryWiseConsumableDetails tPackageCategoryWiseConsumableDetails) {
		this.tPackageCategoryWiseConsumableDetails = tPackageCategoryWiseConsumableDetails;
	}

	public Integer getPackageIncExcId() {
		return packageIncExcId;
	}

	public void setPackageIncExcId(Integer packageIncExcId) {
		this.packageIncExcId = packageIncExcId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getIsInclusiveService() {
		return isInclusiveService;
	}

	public void setIsInclusiveService(Integer isInclusiveService) {
		this.isInclusiveService = isInclusiveService;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getPackageCategoryConsumableId() {
		return packageCategoryConsumableId;
	}

	public void setPackageCategoryConsumableId(Integer packageCategoryConsumableId) {
		this.packageCategoryConsumableId = packageCategoryConsumableId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'A');
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public Double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(Double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public Integer getIsGroupwiseExlusion() {
		return isGroupwiseExlusion;
	}

	public void setIsGroupwiseExlusion(Integer isGroupwiseExlusion) {
		this.isGroupwiseExlusion = isGroupwiseExlusion;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getIsExcluded() {
		return isExcluded;
	}

	public void setIsExcluded(Integer isExcluded) {
		this.isExcluded = isExcluded;
	}
	

}
