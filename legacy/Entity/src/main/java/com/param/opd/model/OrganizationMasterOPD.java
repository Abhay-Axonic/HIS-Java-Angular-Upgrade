package com.param.opd.model;

/*
 * Created by Tushar
 */
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name = "GET_ORGANIZATION_LIST",
	query= "select organization_id as organization_id,organization_name as organization_name from OrganizationMasterOPD "
			+ "WHERE status = 'A' "
			+ "AND LOWER(organization_name) LIKE LOWER(:NAME)")
	
})

@Entity
@Table(name="m_organization_master",schema="public")
public class OrganizationMasterOPD 
{
	@Id
	@Column(name="organization_id")
    private Integer organization_id;
	
	@Column(name="organization_code")
    private String organization_code;
	
	@Column(name="organization_name")
    private String organization_name;
	
	@Column(name="organization_country_id")
    private Integer organization_country_id;
	
	@Column(name="organization_state_id")
    private Integer organization_state_id;
	
	@Column(name="organization_city_id")
    private Integer organization_city_id;
	
	@Column(name="organization_district_id")
    private Integer organization_district_id;
	
	@Column(name="organization_area_id")
    private Integer organization_area_id;
	
	@Column(name="organization_address")
    private String organization_address;
	
	@Column(name="organization_contact")
    private String organization_contact;
	
	@Column(name="organization_email_id")
    private String organization_email_id;
	
	@Column(name="organization_logo")
    private String organization_logo;
	
	@Column(name="created_by")
    private Integer created_by;
	
	@Column(name="created_date")
    private Date created_date;
	
	@Column(name="updated_by")
    private Integer updated_by;
	
	@Column(name="updated_date")
    private Date updated_date;
	
	@Column(name="status")
    private Character status;
	
	@Column(name="organization_desc")
    private String organization_desc;
	
	@Column(name="area_id")
    private Integer area_id;
	
	@Column(name="city_id")
    private Integer city_id;
	
	@Column(name="country_id")
    private Integer country_id;
	
	@Column(name="district_id")
    private Integer  district_id;
	
	@Column(name="post_code")
    private Integer post_code;
	
	@Column(name="state_id")
    private Integer state_id;
	
}
