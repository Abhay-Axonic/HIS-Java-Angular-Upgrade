package com.param.bloodBank.dao;

import java.util.List;

import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.IntraTransfusionMonitoringMapper;
import com.param.bloodBank.dto.TransfusionDetailsMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBloodBagIssueAcceptanceDao {

	Response saveBloodBagIssueAcceptance(List<BloodBagIssueAcceptanceDto> bloodBagIssueAcceptanceDto)throws ApplicationException;

	Response getListOnPendingTransfusion(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto) throws ApplicationException;

	Response saveTransfusionDetails(TransfusionDetailsMasterDto transfusionDetailsMasterDto)throws ApplicationException;

	Response saveIntraTransfusionMonitoringMapper(TransfusionDetailsMasterDto transfusionDetailsMasterDto) throws ApplicationException;

	TransfusionDetailsMasterDto getTransfusionDetailsList(Integer patientId, Integer organizationId, Integer unitId)throws ApplicationException;

	List<IntraTransfusionMonitoringMapper> getintraTransfusionDetailsList(Integer patientId, Integer organizationId,Integer unitId)throws ApplicationException;

	Response updateTransfusionDetailsStatus(IntraTransfusionMonitoringMapper intraTransfusionMonitoringMapper)throws ApplicationException;


}
