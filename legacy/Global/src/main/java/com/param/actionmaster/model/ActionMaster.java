package com.param.actionmaster.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@NamedQueries({
	
	@NamedQuery(name="GET_MODULE_LIST", query="SELECT am.action_id as moduleId, "
		+ "am.action_name as moduleName, am.parent_id as parentId,am.module_type as moduleType,am.action_link as actionLink "
		+ "FROM ActionMaster am where am.status='A' order by parent_id asc,ord_seq asc  "),

	@NamedQuery(name="GET_MODULE_LIST_ZERO_PARENT", query="SELECT am.action_id as moduleId, "
		+ "am.action_name as moduleName, am.parent_id as parentId,am.module_type as moduleType,am.action_link as actionLink "
		+ "FROM ActionMaster am where am.parent_id=0 and am.status='A' order by parent_id asc,ord_seq asc  "),

	@NamedQuery(name="GET_SUB_MODULE_LIST_BY_MODULE_ID", query="SELECT am.action_id as subModuleId, "
			+ "am.action_name as subModuleName, am.parent_id as parentId "
			+ "FROM ActionMaster am WHERE parent_id=:moduleId and am.status='A'"),
	@NamedQuery(name="GET_ALL_SUB_MODULE_LIST", query="SELECT am.action_id as subModuleId, "
			+ "am.action_name as subModuleName, am.parent_id as parentId "
			+ "FROM ActionMaster am WHERE am.parent_id!=0 and am.status='A' "),
	@NamedQuery(name="GET_SCREEN_LIST_BY_SUB_MODULE_ID", query="SELECT am.action_id as screenId, "
			+ "am.action_name as screenName "
			+ "FROM ActionMaster am WHERE parent_id=:subModuleId and am.status='A' ")
	
	
	
			
})

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "m_action_master", schema = "public")
public class ActionMaster {
	@Id
	@Column
	Integer action_id;

	@Column
	String action_name;

	@Column
	String action_link;
	
	@Column
	Integer parent_id;
	
	@Column
	Integer module_type;
	
	@Column
	Integer created_by;
	
	@Column
	@Temporal(TemporalType.DATE)
	Date created_date;
	
	@Column
	char status;
	
	@Column
	String type;
	
	@Column
	Integer ord_seq;
	
	public ActionMaster() {

	}

	public ActionMaster(Integer action_id, String action_name,String action_link,Integer parent_id, Integer created_by, Date created_date,
			char status) {
		super();
		this.action_id = action_id;
		this.action_name = action_name;
		this.action_link = action_link;
		this.parent_id = parent_id;
		this.created_by = created_by;
		this.created_date = created_date;
		this.status = status;
	}

	public Integer getAction_id() {
		return action_id;
	}

	public void setAction_id(Integer action_id) {
		this.action_id = action_id;
	}

	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getModule_type() {
		return module_type;
	}

	public void setModule_type(Integer module_type) {
		this.module_type = module_type;
	}

	public String getAction_link() {
		return action_link;
	}

	public void setAction_link(String action_link) {
		this.action_link = action_link;
	}

	public Integer getOrd_seq() {
		return ord_seq;
	}

	public void setOrd_seq(Integer ord_seq) {
		this.ord_seq = ord_seq;
	}
	
	

}