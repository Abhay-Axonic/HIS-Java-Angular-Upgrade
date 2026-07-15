package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.IssueBloodBagDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IIssueBloodBagService {

	public Response saveIssueBloodBag(IssueBloodBagDto issueBloodBagDto) throws ApplicationException;

	public Response getIssueBloodBagOnIssueAcceptanceScreen(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)throws ApplicationException;

	public Response updateIssuedStatusByIssueBloodBagDetailsId(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)throws ApplicationException;


}
