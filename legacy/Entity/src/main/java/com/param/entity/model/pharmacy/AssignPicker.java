package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity(name="AssignPicker")
@Table(name="t_assign_picker",schema="pharmacy")
public class AssignPicker extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="picker_id")
	private User picker;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="prescription_id")
	private Prescription prescription;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;

	@OneToMany(mappedBy="assignPicker",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AssignPickerDetail> assignPickerDetails;
	
	@OneToMany(mappedBy="assignPicker",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AssignPickerDetailAddedItems> assignPickerDetailAddedItems;

	public List<AssignPickerDetailAddedItems> getAssignPickerDetailAddedItems() {
		return assignPickerDetailAddedItems;
	}

	public void setAssignPickerDetailAddedItems(List<AssignPickerDetailAddedItems> assignPickerDetailAddedItems) {
		this.assignPickerDetailAddedItems = assignPickerDetailAddedItems;
	}

	public AssignPicker() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getPicker() {
		return picker;
	}

	public void setPicker(User picker) {
		this.picker = picker;
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<AssignPickerDetail> getAssignPickerDetails() {
		return assignPickerDetails;
	}

	public void setAssignPickerDetails(List<AssignPickerDetail> assignPickerDetails) {
		this.assignPickerDetails = assignPickerDetails;
	}

	public AssignPickerDetail addAssignPickerDetail(AssignPickerDetail assignPickerDetail) {
		if (getAssignPickerDetails() == null)
			this.assignPickerDetails = new ArrayList<AssignPickerDetail>();
		
		getAssignPickerDetails().add(assignPickerDetail);
		assignPickerDetail.setAssignPicker(this);

		return assignPickerDetail;
	}
	
	public void updateAssignPicker(User picker, Status status) {
		this.picker = picker;
		this.status = status;
	}
	
	public AssignPicker(Prescription prescription, Status status) {
		super();
		this.prescription = prescription;
		this.status = status;
	}

	public AssignPicker(Integer id) {
		super();
		this.id = id;
	}

}