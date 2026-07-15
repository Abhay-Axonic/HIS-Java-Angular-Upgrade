package com.param.global.dto;

import java.util.List;

public class DoctorSpecialityMapperDto {
	private  Character aiDoctor;
	private int doctorSpecialityId;

	private Integer doctorId;

	private Integer unitId;

	private Integer specialityId;
	
	private Integer subSpecialityId;

	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;

	private String firstName;

	private String lastName;

	private String doctorName;

	private Integer organizationId;

	private List<Integer> specialityArray;

	private String specialityName;

	private Integer classId;

	private Integer allowedOverBookingSlots;

	private String doctorImage;
	
	private String mobileNumber;
	
	private Integer userId;
	
    private String appointmentDate;
    
    private Boolean flag;
    
    private Character deptWiseAppointment;

    private Integer doctorSubSpecialityId;

    private String subSpecialityName;

	private String dateFromAI;

	private Integer slotTypeId;

    
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getAllowedOverBookingSlots() {
		return allowedOverBookingSlots;
	}

	public void setAllowedOverBookingSlots(Integer allowedOverBookingSlots) {
		this.allowedOverBookingSlots = allowedOverBookingSlots;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public List<Integer> getSpecialityArray() {
		return specialityArray;
	}

	public void setSpecialityArray(List<Integer> specialityArray) {
		this.specialityArray = specialityArray;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getDoctorSpecialityId() {
		return doctorSpecialityId;
	}

	public void setDoctorSpecialityId(int doctorSpecialityId) {
		this.doctorSpecialityId = doctorSpecialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public String getDoctorImage() {
		return doctorImage;
	}

	public void setDoctorImage(String doctorImage) {
		this.doctorImage = doctorImage;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Character getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(Character deptWiseAppointment) {
		this.deptWiseAppointment = deptWiseAppointment;
	}

    public Integer getDoctorSubSpecialityId() {
        return doctorSubSpecialityId;
    }

    public void setDoctorSubSpecialityId(Integer doctorSubSpecialityId) {
        this.doctorSubSpecialityId = doctorSubSpecialityId;
    }

    public String getSubSpecialityName() {
        return subSpecialityName;
    }

    public void setSubSpecialityName(String subSpecialityName) {
        this.subSpecialityName = subSpecialityName;
    }

	public String getDateFromAI() {
		return dateFromAI;
	}

	public void setDateFromAI(String dateFromAI) {
		this.dateFromAI = dateFromAI;
	}

	public Integer getSlotTypeId() {
		return slotTypeId;
	}

	public void setSlotTypeId(Integer slotTypeId) {
		this.slotTypeId = slotTypeId;
	}

	public Character getAiDoctor() {
		return aiDoctor;
	}

	public void setAiDoctor(Character aiDoctor) {
		this.aiDoctor = aiDoctor;
	}
}
