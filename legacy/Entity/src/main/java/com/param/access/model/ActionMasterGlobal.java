package com.param.access.model;

import java.util.Date;

import javax.persistence.Column;
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

@NamedQueries({
					
		/*
		 * @NamedQuery(name="GET_ACTION_MASTER_BY_ID",
		 * query="SELECT actionMaster.actionId AS actionId, actionMaster.actionName AS actionName, actionMaster.actionLink AS actionLink,"
		 * +
		 * " actionMaster.actionIcon AS actionIcon, actionMaster.parentId AS parentId, actionMaster.status AS status"
		 * + " FROM ActionMasterGlobal actionMaster" +
		 * " WHERE actionMaster.actionId= :actionId"),
		 */
					
					
					
					@NamedQuery(name="GET_ACTIVE_ACTION_MASTER",
						query="SELECT actionMaster.actionId AS actionId, actionMaster.actionName AS actionName, actionMaster.actionLink AS actionLink,"
									+ " actionMaster.actionIcon AS actionIcon, actionMaster.parentId AS parentId, actionMaster.status AS status"
									+ " FROM ActionMasterGlobal actionMaster"
									+ " WHERE actionMaster.status= 'A'"),
					
					@NamedQuery(name="GET_ACTION_MASTER_BY_NAME",
						query="SELECT actionMaster.actionId AS actionId, actionMaster.actionName AS actionName, actionMaster.parentId AS parentId"
									+ " FROM ActionMasterGlobal actionMaster"
									+ " WHERE LOWER(actionMaster.actionName)= :actionName"
									+ " AND actionMaster.parentId= :parentId"
									),
					
					@NamedQuery(name="GET_ACTION_ID",
						query="SELECT actionMaster.actionId AS actionId, actionMaster.actionName AS actionName, actionMaster.parentId AS parentId"
								+ " FROM ActionMasterGlobal actionMaster"
								+ " WHERE LOWER(actionMaster.actionName) like :actionName"
								+ "  AND actionMaster.parentId= :parentId"
								
						),
					
					@NamedQuery(name="GET_PARENT_ACTION_NAME",
					query="SELECT actionMaster.actionId AS actionId, actionMaster.actionName AS actionName, actionMaster.parentId AS parentId"
							+ " FROM ActionMasterGlobal actionMaster"
							+ " WHERE LOWER(actionMaster.actionName) like :actionName"
							+ " AND actionMaster.parentId= (SELECT actionMaster.actionId AS actionId FROM ActionMasterGlobal actionMaster "
							+ " WHERE LOWER(actionMaster.actionName) like :grandParentActionName AND actionMaster.parentId= :parentId)"
					),
												
					@NamedQuery(name="GET_ACTION_MASTER_BY_NAME_NOT_ID",
						query="SELECT actionMaster.actionId AS actionId, actionMaster.actionName AS actionName, actionMaster.parentId AS parentId"
								+ " FROM ActionMasterGlobal actionMaster"
								+ " WHERE actionMaster.actionId != :actionId AND LOWER(actionMaster.actionName)= :actionName  AND actionMaster.parentId= :parentId"),			
					
					@NamedQuery(name="GET_PARENT_ID",
					query="SELECT actionMaster.actionId AS actionId, actionMaster.actionName AS actionName, actionMaster.actionLink AS actionLink,"
								+ " actionMaster.actionIcon AS actionIcon, actionMaster.parentId AS parentId, actionMaster.status AS status"
								+ " FROM ActionMasterGlobal actionMaster"
								+ " WHERE actionMaster.parentId= 0 "
								)
									
			})
@NamedNativeQueries({
			@NamedNativeQuery(name="GET_ACTION_MASTER_LIST",
					query="select b.action_name as \"actionName\", b.parent_id as \"parentId\", a.action_name as \"parentActioName\", b.status as \"status\", b.action_id as \"actionId\" "
					+"from  public.m_action_master b left join  public.m_action_master a "
					+" on b.parent_id=a.action_id "
					+ " where b.status != 'D'"
					+ " group by b.action_id, a.action_name ,b.status " 
					+"  order by b.action_id "),
//			@NamedNativeQuery(name="SEARCH_ACTION_MASTER_BY_PARENT_ACTION_NAME",
//			query="select b.action_name as \"actionName\", b.parent_id as \"parentId\", a.action_name as \"parentActioName\", b.status as \"status\", b.action_id as \"actionId\" "
//						+" from  public.m_action_master b left join  public.m_action_master a "
//						+" on b.parent_id=a.action_id "
//						+ " where lower(b.action_name) like :actionName"
//						+ " group by b.action_id, a.action_name ,b.status " 
//						+"  order by b.action_id")
			
			@NamedNativeQuery(name="SEARCH_ACTION_MASTER_BY_PARENT_ACTION_NAME",
					query=" WITH recursive action_mst AS (  SELECT am1.action_id ,"
					+"	cast(am1.action_name AS \"text\")AS \"aname\","
					+"	am1.parent_id  "
					+" FROM   public.m_action_master am1 "
					+" WHERE  am1.parent_id = 0 "
					+" AND    am1.status = 'A' "
					+" UNION  "
					+" SELECT    am2.action_id, "
					+"	   cast(am.aname "
				    +"             || '|' "
				    +"             || am2.action_name AS \"text\")AS \"aname\",  "       
					+" am2.parent_id "
					+" FROM   public.m_action_master am2 "
					+" INNER JOIN action_mst am  ON "
					+"          am2.parent_id = am.action_id  "
					+"  WHERE am2.status = 'A' ) "
					+" SELECT act_mst.action_id AS \"id\", "
				    +"   act_mst.aname     AS \"name\" ,"
				    + " act_mst.action_id AS \"actionId\" "
				       
					+" FROM   action_mst act_mst "
					+" WHERE  lower(act_mst.aname) LIKE :actionName "
					),

			@NamedNativeQuery(name="GET_ACTION_MASTER_BY_ID",
			query="SELECT actionMaster.action_id AS \"actionId\", actionMaster.action_name AS \"actionName\", actionMaster.action_link AS \"actionLink\","
						+ " actionMaster.action_icon AS \"actionIcon\", actionMaster.parent_id AS \"parentId\", actionMaster.status AS \"status\", actionMasterParent.action_name AS \"parentactioname\" "
						+ " FROM public.m_action_master actionMaster left join  public.m_action_master actionMasterParent  on actionMasterParent.action_id=actionMaster.parent_id"
						+ " WHERE actionMaster.action_id= :actionId")
							
			
			
})



@Entity
@Table(name = "m_action_master", schema = "public")
@SequenceGenerator(name = "action_master_seq", sequenceName = "public.action_master_seq", allocationSize = 1)
public class ActionMasterGlobal {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "action_master_seq")
	
	@Column(name = "action_id")
	private Integer actionId;
	
	@Column(name = "action_name")
	private String actionName;
	
	@Column(name = "action_link")
	private String actionLink;
	
	@Column(name = "action_icon")
	private String actionIcon;
	
	@Column(name = "parent_id")
	private Integer parentId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "module_type")
	private Integer moduleType;

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionLink() {
		return actionLink;
	}

	public void setActionLink(String actionLink) {
		this.actionLink = actionLink;
	}

	public String getActionIcon() {
		return actionIcon;
	}

	public void setActionIcon(String actionIcon) {
		this.actionIcon = actionIcon;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getModuleType() {
		return moduleType;
	}

	public void setModuleType(Integer moduleType) {
		this.moduleType = moduleType;
	}
	
	
}
