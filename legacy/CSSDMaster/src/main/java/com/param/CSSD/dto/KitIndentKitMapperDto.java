package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;

@JsonInclude(Include.NON_NULL)
public class KitIndentKitMapperDto {

	private Integer id;

	private Integer indentNo;
	
	private Integer kitId;
	
	private Integer quantity;
	
	private Boolean isInstrument;
	
	private String kitCode;
	
	private String kitName;
	
	private List<KitInstrumentMapperDto> kitInstrumentList;
	
	private String code;
	
	private String name;
	
	private Integer surgeon;
	
	private String surgeonName;
	
	private Boolean is_active;
	
	private Boolean is_deleted;
	
	private Integer created_by;
	
	private Integer updated_by;
	
	private String created_date;
	
	private String updated_date;
	
	private String rejectReasonNote;
	
	private Integer rejectReasonId;
	
	private String reason;
	
	private String status;
	
	private String reasonDesc;
	
	
	public Boolean getIsInstrument() {
		return isInstrument;
	}

	public void setIsInstrument(Boolean isInstrument) {
		this.isInstrument = isInstrument;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSurgeon() {
		return surgeon;
	}

	public void setSurgeon(Integer surgeon) {
		this.surgeon = surgeon;
	}

	public String getSurgeonName() {
		return surgeonName;
	}

	public void setSurgeonName(String surgeonName) {
		this.surgeonName = surgeonName;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}

	public List<KitInstrumentMapperDto> getKitInstrumentList() {
		return kitInstrumentList;
	}

	public void setKitInstrumentList(List<KitInstrumentMapperDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(Integer indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getKitCode() {
		return kitCode;
	}

	public void setKitCode(String kitCode) {
		this.kitCode = kitCode;
	}

	public String getKitName() {
		return kitName;
	}

	public void setKitName(String kitName) {
		this.kitName = kitName;
	}

	public String getRejectReasonNote() {
		return rejectReasonNote;
	}

	public void setRejectReasonNote(String rejectReasonNote) {
		this.rejectReasonNote = rejectReasonNote;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

		
		
}
