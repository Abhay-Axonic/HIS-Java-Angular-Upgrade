package com.param.bloodBank.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.IntraTransfusionMonitoringMapper;
import com.param.bloodBank.dto.TransfusionDetailsMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodBagIssueAcceptanceService {

	Response saveBloodBagIssueAcceptance(List<BloodBagIssueAcceptanceDto> bloodBagIssueAcceptanceDto)throws ApplicationException;

	Response getListOnPendingTransfusion(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto) throws ApplicationException;

	Response saveTransfusionDetails(TransfusionDetailsMasterDto transfusionDetailsMasterDto) throws ApplicationException;

	Response getTransfusionDetailsList(Integer patientId, Integer organizationId, Integer unitId) throws ApplicationException;


}
