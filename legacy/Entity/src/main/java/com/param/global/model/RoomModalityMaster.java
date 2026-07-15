package com.param.global.model;

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
	@NamedQuery(name="GET_ROOM_MODALITY_BY_ID",
			query="SELECT mstr.roomModalityMasterId as roomModalityMasterId, mstr.roomModalityId as roomModalityId,"
					+ " mstr.specialityId as specialityId,mstr.subSpecialityId as subSpecialityId,mstr.modalityId as modalityId "
					+ " From RoomModalityMaster mstr "
					+ " WHERE mstr.roomModalityMasterId=:id " 
					+ " AND mstr.organizationId=:orgId "
					+ " AND mstr.unitId=:unitId " ),
	@NamedQuery(name="CHECK_ALREDY_EXIST_OR_NOT",
	query="SELECT mstr.roomModalityMasterId as roomModalityMasterId "
			+ " From RoomModalityMaster mstr "
			+ " WHERE mstr.modalityId=:modalityId " 
			+ " AND mstr.organizationId=:orgId "
			+ " AND mstr.unitId=:unitId "
			+ " AND mstr.roomModalityId=:roomModalityId"
			+ " AND mstr.subSpecialityId=:subSpecialityId" ),
	@NamedQuery(name="CHECK_NOT_BY_ID",
	query="SELECT mstr.roomModalityMasterId as roomModalityMasterId "
			+ " From RoomModalityMaster mstr "
			+ " WHERE mstr.modalityId=:modalityId " 
			+ " AND mstr.organizationId=:orgId "
			+ " AND mstr.unitId=:unitId "
			+ " AND mstr.roomModalityId=:roomModalityId"
			+ " AND mstr.subSpecialityId=:subSpecialityId"
			+ " AND mstr.roomModalityMasterId!=:roomModalityMasterId" )
})
@NamedNativeQueries({
	@NamedNativeQuery(name="GET_ROOM_MODALITY_LIST",
			query=" select rm.room_modality_master_id as \"roomModalityMasterId\",rm.room_modality_id as \"roomModalityId\",rm.speciality_id as \"specialityId\", "
+"  rm.sub_speciality_id as \"subSpecialityId\",rm.modality_id as \"modalityId\", mm.modality_desc as \"modalityDesc\", "
+"  sm.speciality_name as \"specialityName\", ssm.sub_speciality_name as \"subSpecialityName\",roomMstr.room_desc as \"roomName\",rm.status as \"status\" "
+"   "
+"  "
+"  from adt.m_room_modality rm  "
+ "  inner join adt.m_room_master roomMstr on rm.room_modality_id=roomMstr.room_id "

+"  inner join public.m_modality_master mm on rm.modality_id=mm.modality_id "
+"  inner join doctor.m_speciality_master sm on sm.speciality_id=rm.speciality_id "
+"  inner join doctor.m_sub_speciality_master ssm on ssm.sub_speciality_id=rm.sub_speciality_id "
+"  where rm.organization_id=:orgId "
+"  and rm.unit_id=:unitId "),
	@NamedNativeQuery(name="GET_MODALITY_DETAILS_BY_MODALITY_ID",
	query="	select rmstr.room_desc as \"roomName\",mm.modality_desc as \"modalityDesc\"  "
+" 	from adt.m_room_modality rm  "
+" 	inner join adt.m_room_master rmstr on rm.room_modality_id=rmstr.room_id "
+" 	inner join public.m_modality_master mm on mm.modality_id=rm.modality_id "
+" 	where rm.modality_id=:modalityId "
+" 	and rm.unit_id=:unitId "
+" 	and rm.organization_id=:orgId ")
	})
@Entity
@Table(name="m_room_modality",schema="adt")
@SequenceGenerator(name="m_seq_room_modality",sequenceName="adt.m_seq_room_modality",allocationSize=1)
public class RoomModalityMaster {
	@Id
	@Column(name="room_modality_master_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="m_seq_room_modality")
	private Integer roomModalityMasterId;
	
	@Column(name="room_modality_id")
	private Integer roomModalityId;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name="sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name="modality_id")
	private Integer modalityId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_by")
	private Integer createdBy;

	public Integer getRoomModalityMasterId() {
		return roomModalityMasterId;
	}

	public Integer getRoomModalityId() {
		return roomModalityId;
	}


	public Integer getModalityId() {
		return modalityId;
	}

	

	public Integer getOrganizationId() {
		return organizationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setRoomModalityMasterId(Integer roomModalityMasterId) {
		this.roomModalityMasterId = roomModalityMasterId;
	}

	public void setRoomModalityId(Integer roomModalityId) {
		this.roomModalityId = roomModalityId;
	}

	

	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}



	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	
}
