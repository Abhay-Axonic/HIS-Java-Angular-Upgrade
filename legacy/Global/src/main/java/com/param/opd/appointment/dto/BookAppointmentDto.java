package com.param.opd.appointment.dto;

import java.util.List;

import com.param.global.dto.DoctorDocumentMapperDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.org.dto.UnitMasterDto;

public class BookAppointmentDto {
   
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer doctorId;
	
	private Integer userId;
	
	private Integer specialityId;
	
	private String appointmentDate;
	
	private List<DoctorDocumentMapperDto> qaulificationList;
	
	private DoctorMasterDto doctorDto;
	
	private List<UnitMasterDto> unitDetList;
	
	private String doctorDegrees;
	
	private OrderMasterDto orderMasterDto;
	
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}


	public List<DoctorDocumentMapperDto> getQaulificationList() {
		return qaulificationList;
	}

	public void setQaulificationList(List<DoctorDocumentMapperDto> qaulificationList) {
		this.qaulificationList = qaulificationList;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public DoctorMasterDto getDoctorDto() {
		return doctorDto;
	}

	public void setDoctorDto(DoctorMasterDto doctorDto) {
		this.doctorDto = doctorDto;
	}

	public List<UnitMasterDto> getUnitDetList() {
		return unitDetList;
	}

	public void setUnitDetList(List<UnitMasterDto> unitDetList) {
		this.unitDetList = unitDetList;
	}

	public String getDoctorDegrees() {
		return doctorDegrees;
	}

	public void setDoctorDegrees(String doctorDegrees) {
		this.doctorDegrees = doctorDegrees;
	}

	public OrderMasterDto getOrderMasterDto() {
		return orderMasterDto;
	}

	public void setOrderMasterDto(OrderMasterDto orderMasterDto) {
		this.orderMasterDto = orderMasterDto;
	}
}
