package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.IssueBloodBagDetailMapperDto;
import com.param.bloodBank.dto.IssueBloodBagDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IIssueBloodBagDao {

	public Response saveIssueBloodBag(IssueBloodBagDto issueBloodBagDto) throws ApplicationException;

	public Response updateCrossMatchingDetailsStatus(IssueBloodBagDetailMapperDto dto)throws ApplicationException;

	public Response getIssueBloodBagOnIssueAcceptanceScreen(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)throws ApplicationException;

	public Response updateIssuedStatusByIssueBloodBagDetailsId(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)throws ApplicationException;


}
