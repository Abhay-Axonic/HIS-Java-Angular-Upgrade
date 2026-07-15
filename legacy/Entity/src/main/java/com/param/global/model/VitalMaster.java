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

import com.param.opd.coversheet.model.VitalQualifireMapper;
import com.param.opd.coversheet.model.VitalQualifireMaster;

@NamedQueries({
	
	/*@NamedQuery(name="GET_VITAL_MASTER_LIST",
			query=" SELECT 	vitalMst.vitalId as vitalId, vitalMst.vitalName as vitalName, vitalMst.minValue as minValue, "
				+ "			vitalMst.maxValue as maxValue, vitalMst.status as status, vitalMst.unit as unit, vitalMst.remark as remark,"
				+ "			vitalMst.vitalIconPath as vitalIconPath, vitalMst.createdBy as createdBy ,"
				+ "			to_char(vitalMst.createdDate,'DD-MM-YYYY') as createdDate, to_char(vitalMst.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "			vitalMst.updatedBy as updatedBy, vitalMst.unitId as unitId, vitalMst.organizationId as organizationId, vitalMst.vitalOrderOpd as vitalOrderOpd "
				+ " FROM 	VitalMaster vitalMst "
				+ " WHERE 	vitalMst.status = 'A' "
				+ " order by	vitalMst.vitalOrderOpd desc"
			),
	*/
})

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_VITAL_MASTER_LIST_FOR_NURSING",
			query="SELECT vital_mst.vital_name AS \"vitalName\", "
					+ "       vital_mst.unit       AS \"vitalUnit\", "
					+ "       vital_mst.vital_id       AS \"vitalId\" "
					+ "FROM   emr.m_vital_master vital_mst "
					+ "WHERE  vital_mst.is_nursing_vital = true "
					+ "ORDER  BY vital_mst.vital_order ASC"
			),
	
	@NamedNativeQuery(name="GET_VITAL_MASTER_LIST",
				query = "select " 
						+ "	towvm.vital_id as \"vitalId\", " 
						+ "	vitalMst.vital_name as \"vitalName\",  "
						+ "	vitalMst.min_value as \"minValue\",  " 
						+ "	vitalMst.max_value as \"maxValue\",  "
						+ "	vitalMst.status as \"status\",  " 
						+ "	vitalMst.unit as \"unit\",  "
						+ "	vitalMst.remark as \"remark\",  " 
						+ "	vitalMst.vital_icon_path as \"vitalIconPath\",  "
						+ "	vitalMst.created_by as \"createdBy\" ,  "
						+ "	vitalMst.is_icu as \"isIcu\" ,  "
						+ "	towvm.is_required as \"isRequired\" ,  "
						+ "	to_char(vitalMst.created_date, 'DD-MM-YYYY') as \"createdDate\",  "
						+ "	to_char(vitalMst.updated_date, 'DD-MM-YYYY') as \"updatedDate\",  "
						+ "	vitalMst.updated_by as \"updatedBy\",  " 
//						+ "	towvm.unit_id as \"unitId\",  "
						+ "	towvm.organization_id as \"organizationId\",  "
						+ "	towvm.vital_order_opd as \"vitalOrderOpd\"  " 
						+ "from  "
						+ "	emr.m_vital_master vitalMst  " + "inner join emr.t_org_wise_vital_mapper towvm on  "
						+ "	towvm.vital_id = vitalMst.vital_id  " 
						+ "where  " 
						+ "	vitalMst.status = 'A'  "
						+ "	and towvm.organization_id = :organizationId  "
						+ "order by  " 
						+ "	vitalMst.vital_name ASC"
	)
	
})

@Entity
@Table(name = "m_vital_master", schema = "emr")
@SequenceGenerator(name = "m_vital_master_seq", sequenceName = "emr.m_vital_master_seq", allocationSize = 1)
public class VitalMaster {

	@Id
	@Column(name = "vital_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_vital_master_seq")
	private int vitalId;
	
	@Column(name = "vital_name")
	private String vitalName;
	
	@Column(name = "min_value")
	private String minValue;

	@Column(name = "max_value")
	private String maxValue;

	@Column(name = "status")
	private char status;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name="vital_icon_path")
	private String vitalIconPath;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "vital_order")
	private Integer vitalOrder;
	
	@Column(name = "vital_order_opd")
	private Integer vitalOrderOpd;
	
	@Column(name = "is_nursing_vital")
	private boolean isNursingVital;
	
	@Column(name = "is_icu")
	private boolean isIcu;
	
	@Column(name = "my_life_vital_id")
	private int myLifeVitalId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vitalMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VitalQualifireMapper> listVitalQualifireMapper;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vitalMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrgWiseVitalMapper> listOrgWiseVitalMapper;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "vital_id", insertable = false, nullable = false, updatable = false)
//	private OrgWiseVitalMapper orgWiseVitalMapper;
	
	public Integer getVitalOrderOpd() {
		return vitalOrderOpd;
	}

	public void setVitalOrderOpd(Integer vitalOrderOpd) {
		this.vitalOrderOpd = vitalOrderOpd;
	}

	public String getVitalIconPath() {
		return vitalIconPath;
	}

	public void setVitalIconPath(String vitalIconPath) {
		this.vitalIconPath = vitalIconPath;
	}

	public int getVitalId() {
		return vitalId;
	}

	public void setVitalId(int vitalId) {
		this.vitalId = vitalId;
	}

	public String getVitalName() {
		return vitalName;
	}

	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getVitalOrder() {
		return vitalOrder;
	}

	public void setVitalOrder(Integer vitalOrder) {
		this.vitalOrder = vitalOrder;
	}

	public boolean isNursingVital() {
		return isNursingVital;
	}

	public void setNursingVital(boolean isNursingVital) {
		this.isNursingVital = isNursingVital;
	}

	public boolean isIcu() {
		return isIcu;
	}

	public void setIcu(boolean isIcu) {
		this.isIcu = isIcu;
	}

	public int getMyLifeVitalId() {
		return myLifeVitalId;
	}

	public void setMyLifeVitalId(int myLifeVitalId) {
		this.myLifeVitalId = myLifeVitalId;
	}
	
	
}
