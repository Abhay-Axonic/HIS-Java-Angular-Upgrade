package com.param.entity.lis.unit;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import com.param.entity.lis.common.LocalTimeConverter;
import com.param.entity.lis.global.LabDayMaster;
import com.param.entity.lis.global.LabUnitMaster;
import com.param.entity.lis.global.RefRangeTypeMaster;
import com.param.entity.lis.global.TechniqueMaster;

@NamedQueries({



    @NamedQuery(name = "GET_PARAMETER_BY_PARAMETER_ID",
        query = " SELECT  "
            +"  parameterMaster.parameterId AS parameterId,  "
            +"  parameterMaster.parameterName AS parameterName,  "
            +"  parameterMaster.aliesName AS aliesName,  "
            +"  parameterMaster.parameterCode AS parameterCode,  "
            +"  parameterMaster.unitId AS unitId,  "
            +"  unitMst.desc AS unitName,  "
            +"  parameterMaster.deltaDaysId AS deltaDaysId,  "
            +"  parameterMaster.deltaPer AS deltaPer,  "
            +"  parameterMaster.testType AS testType,  "
            +"  parameterMaster.status AS status,  "
            +"  parameterMaster.isMultyparameter AS isMultyparameter,  "
            +"  parameterMaster.createdBy AS createdBy,  "
            +"  parameterMaster.createdDate AS createdDate,  "
            +"  parameterMaster.updatedBy AS updatedBy,  "
            +"  parameterMaster.updatedDate AS updatedDate,  "
            +"  parameterMaster.orgId AS orgId,  "
            +"  parameterMaster.orgUnitId AS orgUnitId,  "
            +"  parameterMaster.refrangeTypeId AS refrangeTypeId,  "
            +"  parameterMaster.isFormula AS isFormula,  "
            +"  parameterMaster.isEditable AS isEditable,  "
            +"  parameterMaster.isMachineWisePanicValues AS isMachineWisePanicValues,  "
            +"  parameterMaster.paramPrecision AS paramPrecision,  "
            +"  parameterMaster.techniqueId AS techniqueId , "
            + " techniqueMaster.desc AS techniqueName,"
            + " parameterMaster.ageGroupReqiured as ageGroupReqiured,"
            + " parameterMaster.genderReqiured as genderReqiured "
            +"FROM  "
            +"  ParameterMaster parameterMaster  "
            +"  LEFT JOIN parameterMaster.labUnitMaster unitMst  "
            +"  LEFT JOIN parameterMaster.techniqueMaster techniqueMaster  "
            +"WHERE  "
            +"  parameterMaster.orgId =:orgId  "
           // +"  AND parameterMaster.orgUnitId =:orgUnitId  "
            +"  AND parameterMaster.parameterId =:parameterId  "
),

    @NamedQuery(name = "GET_PARAMETER_BY_CODE",
        query = "SELECT parameterMaster.parameterId AS parameterId,"
            + " parameterMaster.parameterName as parameterName,"
            + " parameterMaster.aliesName as aliesName,"
            + " parameterMaster.parameterCode as parameterCode,"
            + " parameterMaster.unitId as unitId," 
            + " parameterMaster.deltaDaysId as deltaDaysId,"
            + " parameterMaster.deltaPer as deltaPer," 
            + " parameterMaster.testType as testType,"
            + " parameterMaster.isMultyparameter as isMultyparameter,"
            + " parameterMaster.createdBy as createdBy,"
            + " parameterMaster.createdDate as createdDate,"
            + " parameterMaster.updatedBy as updatedBy,"
            + " parameterMaster.updatedDate as updatedDate,"
            + " parameterMaster.orgUnitId as orgUnitId," 
            + " parameterMaster.orgId as orgId"
            + " FROM ParameterMaster parameterMaster " 
            + " WHERE parameterMaster.orgId=:orgId "
            + " AND parameterMaster.orgUnitId = :orgUnitId "
            + " AND lower(parameterMaster.parameterCode) = lower(:parameterCode)"),

    @NamedQuery(name = "UPDATE_GET_PARAMETER_BY_CODE",
        query = "SELECT parameterMaster.parameterId AS parameterId,"
            + " parameterMaster.parameterName as parameterName,"
            + " parameterMaster.aliesName as aliesName,"
            + " parameterMaster.parameterCode as parameterCode,"
            + " parameterMaster.unitId as unitId," + " parameterMaster.deltaDaysId as deltaDaysId,"
            + " parameterMaster.deltaPer as deltaPer," + " parameterMaster.testType as testType,"
            + " parameterMaster.isMultyparameter as isMultyparameter,"
            + " parameterMaster.createdBy as createdBy,"
            + " parameterMaster.createdDate as createdDate,"
            + " parameterMaster.updatedBy as updatedBy,"
            + " parameterMaster.updatedDate as updatedDate,"
            + " parameterMaster.orgUnitId as orgUnitId," + " parameterMaster.orgId as orgId"
            + " FROM ParameterMaster parameterMaster " + " WHERE parameterMaster.orgId=:orgId "
            + " AND parameterMaster.orgUnitId = :orgUnitId "
            + " AND lower(parameterMaster.parameterCode) = lower(:parameterCode)"
            + " AND parameterMaster.parameterId <> :parameterId"),

    @NamedQuery(name = "GET_PAGINATED_PARAMETER_MASTER_LIST",
        query = " SELECT  "
            +"  parameterMaster.parameterId AS parameterId,  "
            +"  parameterMaster.parameterName AS parameterName,  "
            +"  parameterMaster.aliesName AS aliesName,  "
            +"  parameterMaster.parameterCode AS parameterCode,  "
            +"  parameterMaster.unitId AS unitId,  "
            +"  unitMst.desc AS unitName,  "
            +"  parameterMaster.deltaDaysId AS deltaDaysId,  "
            +"  parameterMaster.deltaPer AS deltaPer,  "
            +"  parameterMaster.testType AS testType,  "
            +"  parameterMaster.status AS status,  "
            +"  parameterMaster.isMultyparameter AS isMultyparameter,  "
            +"  parameterMaster.createdBy AS createdBy,  "
            +"  parameterMaster.createdDate AS createdDate,  "
            +"  parameterMaster.updatedBy AS updatedBy,  "
            +"  parameterMaster.updatedDate AS updatedDate,  "
            +"  parameterMaster.orgId AS orgId,  "
            +"  parameterMaster.orgUnitId AS orgUnitId,  "
            +"  parameterMaster.refrangeTypeId AS refrangeTypeId,  "
            +"  parameterMaster.isFormula AS isFormula,  "
            +"  parameterMaster.isEditable AS isEditable,  "
            +"  parameterMaster.isMachineWisePanicValues AS isMachineWisePanicValues,  "
            +"  parameterMaster.paramPrecision AS paramPrecision,  "
            +"  parameterMaster.techniqueId AS techniqueId , "
            +"  parameterMaster.paramPrecision AS paramPrecision  "
            +"FROM  "
            +"  ParameterMaster parameterMaster  "
            +"  LEFT JOIN parameterMaster.labUnitMaster unitMst  "
            +"WHERE  "
            +"  parameterMaster.orgId =:orgId  "
            +"  AND parameterMaster.orgUnitId =:orgUnitId  "
            + " ORDER BY parameterMaster.parameterId DESC"),
    
    @NamedQuery(name = "UPDATE_PARAMETER_STATUS", query ="UPDATE ParameterMaster SET status =:status  "
        +"WHERE parameterId =:parameterId  " ),
    
    @NamedQuery(name = "GET_PARAMETER_FOR_FORMULA",
    query = " SELECT  "
        +"  parameterMaster.parameterId AS id,  "
        +"  parameterMaster.parameterName AS name  "
        +"FROM  "
        +"  ParameterMaster parameterMaster  "
        +"WHERE  "
        +"  parameterMaster.orgId =:orgId  "
        +"  AND parameterMaster.orgUnitId =:orgUnitId  "
        +"  AND parameterMaster.status ='A' "),
    
    

    @NamedQuery(name = "ACTIVE_PARAMETER_LIST",
        query = " SELECT parameterMaster.parameterId as parameterId,"
            + " parameterMaster.parameterName as parameterName,"
            + " parameterMaster.aliesName as aliesName,"
            + " parameterMaster.parameterCode as parameterCode,"
            + " parameterMaster.orgUnitId as orgUnitId," 
            + " parameterMaster.orgId as orgId "
            + " FROM ParameterMaster parameterMaster " 
            + " WHERE parameterMaster.orgId=:orgId "
            + " AND parameterMaster.status ='A' "
            + " AND parameterMaster.orgUnitId = :unitId "
            + " ORDER BY parameterMaster.parameterName ASC " ),
          
    @NamedQuery(name = "GET_PAGINATED_PARAMETER_MASTER_DETAILS_LIST_SEARCH",
    query = " SELECT  "
        +"  parameterMaster.parameterId AS parameterId,  "
        +"  parameterMaster.parameterName AS parameterName,  "
        +"  parameterMaster.aliesName AS aliesName,  "
        +"  parameterMaster.parameterCode AS parameterCode,  "
        +"  parameterMaster.unitId AS unitId,  "
        +"  unitMst.desc AS unitName,  "
        +"  parameterMaster.deltaDaysId AS deltaDaysId,  "
        +"  parameterMaster.deltaPer AS deltaPer,  "
        +"  parameterMaster.testType AS testType,  "
        +"  parameterMaster.status AS status,  "
        +"  parameterMaster.isMultyparameter AS isMultyparameter,  "
        +"  parameterMaster.createdBy AS createdBy,  "
        +"  parameterMaster.createdDate AS createdDate,  "
        +"  parameterMaster.updatedBy AS updatedBy,  "
        +"  parameterMaster.updatedDate AS updatedDate,  "
        +"  parameterMaster.orgId AS orgId,  "
        +"  parameterMaster.orgUnitId AS orgUnitId,  "
        +"  parameterMaster.refrangeTypeId AS refrangeTypeId,  "
        +"  parameterMaster.isFormula AS isFormula,  "
        +"  parameterMaster.isEditable AS isEditable,  "
        +"  parameterMaster.isMachineWisePanicValues AS isMachineWisePanicValues,  "
        +"  parameterMaster.paramPrecision AS paramPrecision  "
        +"FROM  "
        +"  ParameterMaster parameterMaster  "
        +"  LEFT JOIN parameterMaster.labUnitMaster unitMst  "
        +"WHERE  "
        +"  parameterMaster.orgId =:orgId  "
        +"  AND parameterMaster.orgUnitId =:orgUnitId "
        + " AND (lower(parameterMaster.parameterName) like :parameterName "
        + " OR lower(parameterMaster.aliesName) like :parameterName "
        + " OR lower(parameterMaster.parameterCode) like :parameterName "
        + " OR lower(unitMst.desc) like :parameterName )"
        + " ORDER BY parameterMaster.parameterId DESC"),
    
    })

@NamedNativeQueries({@NamedNativeQuery(name = "GET_TOTAL_PARAMETER_RECORD",
    query = "select count(*) from lab.m_parameter_master parammst where "
        + "parammst.org_id = :orgId " + " AND parammst.org_unit_id = :orgUnitId ")


})








@Entity
@Table(name = "m_parameter_master", schema = "lab")
@SequenceGenerator(name = "m_seq_parameter_master", sequenceName = "lab.m_seq_parameter_master",
    allocationSize = 1)
public class ParameterMaster {
  @Id
  @Column(name = "parameter_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_parameter_master")
  private int parameterId;

  @Column(name = "parameter_name")
  private String parameterName;

  @Column(name = "alies_name")
  private String aliesName;

  @Column(name = "parameter_code")
  private String parameterCode;

  @Column(name = "unit_id")
  private Integer unitId;

  @Column(name = "delta_days_id")
  private Integer deltaDaysId;

  @Column(name = "delta_per")
  private Double deltaPer;

  @Column(name = "test_type")
  private Integer testType;

  @Column(name = "status")
  private Character status;

  @Column(name = "is_multyparameter")
  private Character isMultyparameter;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_date")
  @Convert(converter = LocalTimeConverter.class)
  private Long createdDate;

  @Column(name = "updated_by")
  private Integer updatedBy;

  @Column(name = "updated_date")
  @Convert(converter = LocalTimeConverter.class)
  private Long updatedDate;

  @Column(name = "org_id")
  private Integer orgId;

  @Column(name = "org_unit_id")
  private Integer orgUnitId;

  @Column(name = "refrange_type_id")
  private Integer refrangeTypeId;
  
  @Column(name = "is_formula")
  private Character isFormula;
  
  @Column(name = "is_editable")
  private Character isEditable;
  
  @Column(name = "is_machine_wise_panic_values")
  private Character isMachineWisePanicValues;
  
  @Column(name = "param_precision")
  private Integer paramPrecision;
  
  @Column(name = "age_group_reqiured")
  private Character ageGroupReqiured;
  
  @Column(name = "gender_reqiured")
  private Character genderReqiured;

  @OneToMany( mappedBy = "parameterMaster")
  private List<ParamRefrengMaster> listParamRefrengMaster;

  @OneToMany(mappedBy = "parameterMaster")
  private List<HelpValueMaster> listHelpValueMaster;

  @OneToMany( mappedBy = "parameterMaster")
  private List<ParamTextualRanageMaster> listParamTextualRanageMaster;

  @OneToMany( mappedBy = "parameterMaster")
  private List<ParamMultiTextualRangeMaster> listParamMultiTextualRangeMaster;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
  private LabUnitMaster labUnitMaster;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delta_days_id", insertable = false, nullable = false, updatable = false)
  private LabDayMaster labDayMaster;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "refrange_type_id", insertable = false, nullable = false, updatable = false)
  private RefRangeTypeMaster refRangeTypeMaster;

  @Column(name = "technique_id")
  private Integer techniqueId;
  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "technique_id", insertable = false, nullable = false, updatable = false)
	private TechniqueMaster techniqueMaster;
  
  public int getParameterId() {
    return parameterId;
  }

  public void setParameterId(int parameterId) {
    this.parameterId = parameterId;
  }

  public String getParameterName() {
    return parameterName;
  }

  public void setParameterName(String parameterName) {
    this.parameterName = parameterName;
  }

  public String getParameterCode() {
    return parameterCode;
  }

  public void setParameterCode(String parameterCode) {
    this.parameterCode = parameterCode;
  }

  public Integer getUnitId() {
    return unitId;
  }

  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }

  public Integer getDeltaDaysId() {
    return deltaDaysId;
  }

  public void setDeltaDaysId(Integer deltaDaysId) {
    this.deltaDaysId = deltaDaysId;
  }

  public Double getDeltaPer() {
    return deltaPer;
  }

  public void setDeltaPer(Double deltaPer) {
    this.deltaPer = deltaPer;
  }

  public Integer getTestType() {
    return testType;
  }

  public void setTestType(Integer testType) {
    this.testType = testType;
  }

  public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  public Long getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Long createdDate) {
    this.createdDate = createdDate;
  }

  public Integer getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(Integer updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Long getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Long updatedDate) {
    this.updatedDate = updatedDate;
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

  /*
   * public List<TestParamMppr> getListTestParamMppr() { return listTestParamMppr; }
   * 
   * public void setListTestParamMppr(List<TestParamMppr> listTestParamMppr) {
   * this.listTestParamMppr = listTestParamMppr; }
   */

  public List<ParamRefrengMaster> getListParamRefrengMaster() {
    return listParamRefrengMaster;
  }

  public void setListParamRefrengMaster(List<ParamRefrengMaster> listParamRefrengMaster) {
    this.listParamRefrengMaster = listParamRefrengMaster;
  }

  public List<HelpValueMaster> getListHelpValueMaster() {
    return listHelpValueMaster;
  }

  public void setListHelpValueMaster(List<HelpValueMaster> listHelpValueMaster) {
    this.listHelpValueMaster = listHelpValueMaster;
  }

  public String getAliesName() {
    return aliesName;
  }

  public void setAliesName(String aliesName) {
    this.aliesName = aliesName;
  }

  public Character getIsMultyparameter() {
    return isMultyparameter;
  }

  public void setIsMultyparameter(Character isMultyparameter) 
  {
    this.isMultyparameter = (isMultyparameter == '\u0000') ? 'N' : isMultyparameter;
  }

  public Character getStatus() {
    return status;
  }

  public void setStatus(Character status) {
    this.status = (status == '\u0000') ? 'A' : status;
  }

  public LabUnitMaster getLabUnitMaster() {
    return labUnitMaster;
  }

  public void setLabUnitMaster(LabUnitMaster labUnitMaster) {
    this.labUnitMaster = labUnitMaster;
  }

  public LabDayMaster getLabDayMaster() {
    return labDayMaster;
  }

  public void setLabDayMaster(LabDayMaster labDayMaster) {
    this.labDayMaster = labDayMaster;
  }

  public Integer getRefrangeTypeId() {
    return refrangeTypeId;
  }

  public void setRefrangeTypeId(Integer refrangeTypeId) {
    this.refrangeTypeId = refrangeTypeId;
  }

  public RefRangeTypeMaster getRefRangeTypeMaster() {
    return refRangeTypeMaster;
  }

  public void setRefRangeTypeMaster(RefRangeTypeMaster refRangeTypeMaster) {
    this.refRangeTypeMaster = refRangeTypeMaster;
  }

  public List<ParamTextualRanageMaster> getListParamTextualRanageMaster() {
    return listParamTextualRanageMaster;
  }

  public void setListParamTextualRanageMaster(
      List<ParamTextualRanageMaster> listParamTextualRanageMaster) {
    this.listParamTextualRanageMaster = listParamTextualRanageMaster;
  }

  public List<ParamMultiTextualRangeMaster> getListParamMultiTextualRangeMaster() {
    return listParamMultiTextualRangeMaster;
  }

  public void setListParamMultiTextualRangeMaster(
      List<ParamMultiTextualRangeMaster> listParamMultiTextualRangeMaster) {
    this.listParamMultiTextualRangeMaster = listParamMultiTextualRangeMaster;
  }

  public Character getIsFormula() {
    return isFormula;
  }

  public void setIsFormula(Character isFormula) {
    this.isFormula = (isFormula == '\u0000') ? 'N' : isFormula;
  }

  public Integer getParamPrecision() {
    return paramPrecision;
  }

  public void setParamPrecision(Integer paramPrecision) {
    this.paramPrecision = (paramPrecision == null) ? 0 : paramPrecision;
  }

public Character getIsEditable() {
	return isEditable;
}

public void setIsEditable(Character isEditable) {
	this.isEditable = isEditable;
}

public Character getIsMachineWisePanicValues() {
	return isMachineWisePanicValues;
}

public void setIsMachineWisePanicValues(Character isMachineWisePanicValues) {
	this.isMachineWisePanicValues = isMachineWisePanicValues;
}

public Integer getTechniqueId() {
	return techniqueId;
}

public void setTechniqueId(Integer techniqueId) {
	this.techniqueId = techniqueId;
}

public Character getAgeGroupReqiured() {
	return ageGroupReqiured;
}

public void setAgeGroupReqiured(Character ageGroupReqiured) {
	this.ageGroupReqiured = ageGroupReqiured;
}

public Character getGenderReqiured() {
	return genderReqiured;
}

public void setGenderReqiured(Character genderReqiured) {
	this.genderReqiured = genderReqiured;
}


}
