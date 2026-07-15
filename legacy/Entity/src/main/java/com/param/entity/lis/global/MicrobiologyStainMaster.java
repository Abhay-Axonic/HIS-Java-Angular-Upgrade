package com.param.entity.lis.global;



import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.param.entity.lis.common.LocalTimeConverter;

@NamedQueries({

	@NamedQuery(name = "GET_STAINIG_BY_MICROBSTAINIG_ID", query = "SELECT microbiologyStainMaster.microStainId as id,"
			+ " microbiologyStainMaster.code as code,"
			+ " microbiologyStainMaster.desc as desc,"
			+ " microbiologyStainMaster.status as status,"
			+ " microbiologyStainMaster.orgId as orgId,"
			+ " microbiologyStainMaster.createdBy as createdBy,"
			+ " microbiologyStainMaster.createdDate as createdDate,"
			+ " microbiologyStainMaster.updatedBy as updatedBy,"
			+ " microbiologyStainMaster.updatedDate as updatedDate"
			+ " FROM MicrobiologyStainMaster microbiologyStainMaster "
			+ " WHERE microbiologyStainMaster.microStainId = :microStainId "
			+ " AND microbiologyStainMaster.orgId= :orgId"),
			
		@NamedQuery(name = "GET_STAINIG_BY_MICROBSTAINIG_CODE", query = "SELECT microbiologyStainMaster.microStainId as id,"
			+ " microbiologyStainMaster.code as code,"
			+ " microbiologyStainMaster.desc as desc,"
			+ " microbiologyStainMaster.status as status,"
			+ " microbiologyStainMaster.orgId as orgId,"
			+ " microbiologyStainMaster.createdBy as createdBy,"
			+ " microbiologyStainMaster.createdDate as createdDate,"
			+ " microbiologyStainMaster.updatedBy as updatedBy,"
			+ " microbiologyStainMaster.updatedDate as updatedDate"
			+ " FROM MicrobiologyStainMaster microbiologyStainMaster "
			+ " WHERE microbiologyStainMaster.orgId=:orgId "
			+ " AND lower(microbiologyStainMaster.code) = lower(:code)"),
		
		@NamedQuery(name = "GET_STAINIG_BY_MICROBSTAINIG_DESC", query = "SELECT microbiologyStainMaster.microStainId as id,"
				+ " microbiologyStainMaster.code as code,"
				+ " microbiologyStainMaster.desc as desc,"
				+ " microbiologyStainMaster.status as status,"
				+ " microbiologyStainMaster.orgId as orgId,"
				+ " microbiologyStainMaster.createdBy as createdBy,"
				+ " microbiologyStainMaster.createdDate as createdDate,"
				+ " microbiologyStainMaster.updatedBy as updatedBy,"
				+ " microbiologyStainMaster.updatedDate as updatedDate"
				+ " FROM MicrobiologyStainMaster microbiologyStainMaster "
				+ " WHERE microbiologyStainMaster.orgId=:orgId "
				+ " AND lower(microbiologyStainMaster.desc) = lower(:desc)"),
			
			
			@NamedQuery(name = "UPDATE_GET_STAINIG_TYPE_BY_MICROBSTAINIG_CODE", query = "SELECT microbiologyStainMaster.microStainId as id,"
			+ " microbiologyStainMaster.code as code,"
			+ " microbiologyStainMaster.desc as desc,"
			+ " microbiologyStainMaster.status as status,"
			+ " microbiologyStainMaster.orgId as orgId,"
			+ " microbiologyStainMaster.createdBy as createdBy,"
			+ " microbiologyStainMaster.createdDate as createdDate,"
			+ " microbiologyStainMaster.updatedBy as updatedBy,"
			+ " microbiologyStainMaster.updatedDate as updatedDate"
			+ " FROM MicrobiologyStainMaster microbiologyStainMaster "
			+ " WHERE microbiologyStainMaster.orgId=:orgId "
			+ " AND lower(microbiologyStainMaster.code) = lower(:code)"
			+ " AND microbiologyStainMaster.microStainId <> :microStainId"),
			
			@NamedQuery(name = "UPDATE_GET_STAINIG_TYPE_BY_MICROBSTAINIG_DESC", query = "SELECT microbiologyStainMaster.microStainId as id,"
					+ " microbiologyStainMaster.code as code,"
					+ " microbiologyStainMaster.desc as desc,"
					+ " microbiologyStainMaster.status as status,"
					+ " microbiologyStainMaster.orgId as orgId,"
					+ " microbiologyStainMaster.createdBy as createdBy,"
					+ " microbiologyStainMaster.createdDate as createdDate,"
					+ " microbiologyStainMaster.updatedBy as updatedBy,"
					+ " microbiologyStainMaster.updatedDate as updatedDate"
					+ " FROM MicrobiologyStainMaster microbiologyStainMaster "
					+ " WHERE microbiologyStainMaster.orgId=:orgId "
					+ " AND lower(microbiologyStainMaster.desc) = lower(:desc)"
					+ " AND microbiologyStainMaster.microStainId <> :microStainId"),
	

	@NamedQuery(name = "GET_MICROBSTAINIG_MASTER_LIST", query = "SELECT microbiologyStainMaster.microStainId as id,"
			+ " microbiologyStainMaster.code as code,"
			+ " microbiologyStainMaster.desc as desc,"
			+ " microbiologyStainMaster.status as status,"
			+ " microbiologyStainMaster.orgId as orgId,"
			+ " microbiologyStainMaster.createdBy as createdBy,"
			+ " microbiologyStainMaster.createdDate as createdDate,"
			+ " microbiologyStainMaster.updatedBy as updatedBy,"
			+ " microbiologyStainMaster.updatedDate as updatedDate"
			+ " FROM MicrobiologyStainMaster microbiologyStainMaster "
			+ " WHERE microbiologyStainMaster.orgId = :orgId"
			+ " ORDER BY id DESC"),
	
	@NamedQuery(name = "GET_MICRO_STAIN_LIST", query = "SELECT microbiologyStainMaster.microStainId as id,"
			+ " microbiologyStainMaster.desc as name "
			+ " FROM MicrobiologyStainMaster microbiologyStainMaster "
			+ " WHERE microbiologyStainMaster.orgId = :orgId and microbiologyStainMaster.status = 'A' "
			+ " ORDER BY id DESC")

})




@NamedNativeQueries(
{ @NamedNativeQuery(name = "GET_TOTAL_MICROBSTAINIG_RECORD", query = "select count(*) from lab.m_microbiology_stain_master where "
	+ "org_id = :orgId "),
	
	
@NamedNativeQuery(name = "GET_MICROBSTATINING_LIST_BY_ORGID", query =" SELECT "
		 +"	microbiology_stain_master.micro_stain_id AS id , "
		 +"	microbiology_stain_master.stain_code AS name "
		 +"FROM "
		 +"	lab.m_microbiology_stain_master microbiology_stain_master "
		 +"WHERE "
		 +"	microbiology_stain_master.status = 'A' "
		 +"	AND microbiology_stain_master.org_id =:orgId " )

})

@Entity
@Table(name = "m_microbiology_stain_master", schema = "lab")
@SequenceGenerator(name = "m_seq_microbiology_stain_master", sequenceName = "lab.m_seq_microbiology_stain_master", allocationSize = 1)
public class MicrobiologyStainMaster {
    
    
    @Id
    @Column(name = "micro_stain_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_microbiology_stain_master")
    private int microStainId;
    
    @Column(name = "stain_code")
    private String code;
    
    @Column(name = "stain_desc")
    private String desc;
    
    @Column(name = "status")
    private Character status;
    
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

    public int getMicroStainId() {
      return microStainId;
    }

    public void setMicroStainId(int microStainId) {
      this.microStainId = microStainId;
    }

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }

    public Character getStatus() {
      return status;
    }

    public void setStatus(Character status) {
      this.status = (status == '\u0000') ? 'A' : status;
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

    
}
