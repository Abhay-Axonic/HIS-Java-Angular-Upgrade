package com.param.global.otims.service;


import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.IssueAcceptanceDto;
import com.param.global.otims.dto.ImplantDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
@Transactional
public interface IImplantDetailsService {
	public Response saveImplantdetails( List<ImplantDetailsDto> implantDetailsDto) throws ApplicationException;

	public Response getImplantdetailsList(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

	public Response getImplantdetails(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

	public Response getKitdetails(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

	public Response searchImplantItems(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

	public Response getImplantDetailsByProcedureId(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

	public Response searchImplantKit(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

	public Response getImplantIndentAcceptanceData(IssueAcceptanceDto issueAcceptanceDto) throws ApplicationException;

	public Response updateImplantDetailsIsExported(List<ImplantDetailsDto> list) throws ApplicationException;

	public Response getImplantDetailsByProcedureIdOnOTIMS(ImplantDetailsDto implantDetailsDto) throws ApplicationException;
	
}
