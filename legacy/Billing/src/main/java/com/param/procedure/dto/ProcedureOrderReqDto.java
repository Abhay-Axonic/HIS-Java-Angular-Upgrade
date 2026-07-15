package com.param.procedure.dto;

import java.util.List;

import com.param.global.otims.dto.OTCompletionNoteDto;
import com.param.global.otims.dto.OtCompletionInvConsumableDto;
import com.param.global.otims.dto.OtCompletionTimeSheetDto;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureReqMasterDto;
import com.param.global.procedure.dto.BillOfMaterialDto;
import com.param.global.procedure.dto.OTCompletionRequest;
import com.param.global.procedure.dto.OtCompletionMedicalGasDto;

public class ProcedureOrderReqDto {
	
	private Integer payeeId;
	private List<ProcedureOrderMasterDto> procedureOrders;
	private List<OTServiceDto> otServices;
	private List<OtCompletionStaffDto> otCompletionStaff;
	private OtCompletionTimeSheetDto timeSheet;
	private char isCompleted;
	private List<OTCompletionRequest> oTCompletionRequest;
	private List<OtcCompletionInvMedicationDto> otcCompletionInvMedication;
	private List<OtcCompletionInvImplantsDto> otcCompletionInvImplants;
	private List<OtCompletionInvConsumableDto> otCompletionInvConsumable;
	private List<OTCompletionNoteDto> oTCompletionNoteList;
	private List<OtCompletionEquipmentDto> equipmentlist ;
	private List<BillOfMaterialDto> billOfresList ;
	private List<OTCompletionCssdKitDto> otCompletionCssdKitList;
	private OTCompletionDetailsDto otCompletionDetailsDto;
	private List<OtCompletionMedicalGasDto> otCompletionMedicalGasList;
	private ProcedureReqMasterDto procedureReqMasterDto;
	private Boolean otimsStatus;
	private boolean otToWardTransfer;
	private Integer transferRequestId;
	public char getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(char isCompleted) {
		this.isCompleted = (isCompleted != '\u0000' ? isCompleted : 'N');
	}
	public OtCompletionTimeSheetDto getTimeSheet() {
		return timeSheet;
	}
	public void setTimeSheet(OtCompletionTimeSheetDto timeSheet) {
		this.timeSheet = timeSheet;
	}
	public List<ProcedureOrderMasterDto> getProcedureOrders() {
		return procedureOrders;
	}
	public void setProcedureOrders(List<ProcedureOrderMasterDto> procedureOrders) {
		this.procedureOrders = procedureOrders;
	}
	public List<OTServiceDto> getOtServices() {
		return otServices;
	}
	public void setOtServices(List<OTServiceDto> otServices) {
		this.otServices = otServices;
	}
	public List<OTCompletionRequest> getoTCompletionRequest() {
		return oTCompletionRequest;
	}
	public void setoTCompletionRequest(List<OTCompletionRequest> oTCompletionRequest) {
		this.oTCompletionRequest = oTCompletionRequest;
	}
	public List<OtCompletionStaffDto> getOtCompletionStaff() {
		return otCompletionStaff;
	}
	public void setOtCompletionStaff(List<OtCompletionStaffDto> otCompletionStaff) {
		this.otCompletionStaff = otCompletionStaff;
	}
	public List<OtcCompletionInvMedicationDto> getOtcCompletionInvMedication() {
		return otcCompletionInvMedication;
	}
	public void setOtcCompletionInvMedication(List<OtcCompletionInvMedicationDto> otcCompletionInvMedication) {
		this.otcCompletionInvMedication = otcCompletionInvMedication;
	}
	public List<OtcCompletionInvImplantsDto> getOtcCompletionInvImplants() {
		return otcCompletionInvImplants;
	}
	public void setOtcCompletionInvImplants(List<OtcCompletionInvImplantsDto> otcCompletionInvImplants) {
		this.otcCompletionInvImplants = otcCompletionInvImplants;
	}
	public List<OtCompletionInvConsumableDto> getOtCompletionInvConsumable() {
		return otCompletionInvConsumable;
	}
	public void setOtCompletionInvConsumable(List<OtCompletionInvConsumableDto> otCompletionInvConsumable) {
		this.otCompletionInvConsumable = otCompletionInvConsumable;
	}
	public Boolean getOtimsStatus() {
		return otimsStatus;
	}
	public void setOtimsStatus(Boolean otimsStatus) {
		this.otimsStatus = otimsStatus;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	
	public List<BillOfMaterialDto> getBillOfresList() {
		return billOfresList;
	}
	public void setBillOfresList(List<BillOfMaterialDto> billOfresList) {
		this.billOfresList = billOfresList;
	}
	public List<OTCompletionCssdKitDto> getOtCompletionCssdKitList() {
		return otCompletionCssdKitList;
	}
	public void setOtCompletionCssdKitList(List<OTCompletionCssdKitDto> otCompletionCssdKitList) {
		this.otCompletionCssdKitList = otCompletionCssdKitList;
	}
	public OTCompletionDetailsDto getOtCompletionDetailsDto() {
		return otCompletionDetailsDto;
	}
	public void setOtCompletionDetailsDto(OTCompletionDetailsDto otCompletionDetailsDto) {
		this.otCompletionDetailsDto = otCompletionDetailsDto;
	}
	public List<OTCompletionNoteDto> getoTCompletionNoteList() {
		return oTCompletionNoteList;
	}
	public void setoTCompletionNoteList(List<OTCompletionNoteDto> oTCompletionNoteList) {
		this.oTCompletionNoteList = oTCompletionNoteList;
	}
	public List<OtCompletionMedicalGasDto> getOtCompletionMedicalGasList() {
		return otCompletionMedicalGasList;
	}
	public void setOtCompletionMedicalGasList(List<OtCompletionMedicalGasDto> otCompletionMedicalGasList) {
		this.otCompletionMedicalGasList = otCompletionMedicalGasList;
	}
	public ProcedureReqMasterDto getProcedureReqMasterDto() {
		return procedureReqMasterDto;
	}
	public void setProcedureReqMasterDto(ProcedureReqMasterDto procedureReqMasterDto) {
		this.procedureReqMasterDto = procedureReqMasterDto;
	}
	public List<OtCompletionEquipmentDto> getEquipmentlist() {
		return equipmentlist;
	}
	public void setEquipmentlist(List<OtCompletionEquipmentDto> equipmentlist) {
		this.equipmentlist = equipmentlist;
	}
	public boolean getOtToWardTransfer() {
		return otToWardTransfer;
	}
	public void setOtToWardTransfer(boolean otToWardTransfer) {
		this.otToWardTransfer = otToWardTransfer;
	}
	public Integer getTransferRequestId() {
		return transferRequestId;
	}
	public void setTransferRequestId(Integer transferRequestId) {
		this.transferRequestId = transferRequestId;
	}
	
}
