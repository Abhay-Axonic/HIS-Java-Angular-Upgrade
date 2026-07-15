package com.param.entity.model.pharmacy;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.master.User;
import com.param.entity.model.opd.MedicationHistory;
import com.param.utility.TimeZoneComponent;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "Instruction")
@Table(name = "m_instruction", schema = "public")
public class Instruction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, name = "instruction_id", nullable = false)
	private Integer id;

	@Column(nullable = false, name = "instruction_code", length = 100)
	private String instructionCode;

	@Column(nullable = false, name = "instruction_desc", length = 200)
	private String instructionDesc;

	@Column(nullable = false, name = "is_medication", length = 100)
	private Character isMedication;

	@Column(name = "status")
	@ApiModelProperty(value = "IsDeleted")
	protected Character status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	@ApiModelProperty(value = "Added By")
	protected User addedBy = new User();

	@Column(name = "created_date")
	@ApiModelProperty(value = "Added Date")
	protected Date addedDate = TimeZoneComponent.getDateByzone(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	@ApiModelProperty(value = "Updated By")
	protected User updatedBy = new User();

	@Column(name = "updated_date")
	@ApiModelProperty(value = "Update Date")
	protected Date updatedDate = TimeZoneComponent.getDateByzone(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "instruction")
	private List<MedicationHistory> instructionList;

	public Instruction() {
		super();

		/*
		 * SessionService sessionService = getSessionService();
		 * 
		 * if (sessionService != null) { Integer userId =
		 * sessionService.getCookieObjectDto().getUserId();
		 * this.addedBy.setUserId(userId); this.addedDate = TimeZoneComponent.getDateByzone(unitId);
		 * this.updatedBy.setUserId(userId); this.updatedDate = TimeZoneComponent.getDateByzone(unitId); }
		 */

	}

	public Instruction(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInstructionCode() {
		return instructionCode;
	}

	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public Character getIsMedication() {
		return isMedication;
	}

	public void setIsMedication(Character isMedication) {
		this.isMedication = isMedication;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public User getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(User addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/*
	 * public User getUser() { SessionService sessionService = getSessionService();
	 * 
	 * if (sessionService != null) { Integer userId =
	 * sessionService.getCookieObjectDto().getUserId(); User user = new
	 * User(userId); return user; }
	 * 
	 * return null; }
	 */

//	private SessionService getSessionService() {
//		return SpringContext.getBean(SessionService.class);
//	}

}
