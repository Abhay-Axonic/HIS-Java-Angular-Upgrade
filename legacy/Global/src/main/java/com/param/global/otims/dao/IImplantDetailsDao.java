package com.param.global.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.IssueAcceptanceDto;
import com.param.global.otims.dto.ImplantDetailsDto;
import com.param.global.otims.dto.OTReservationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
public interface IImplantDetailsDao {
	
public Response saveImplantdetails( List<ImplantDetailsDto> implantDetailsDto, int proReqId, Integer procedureOrderId) throws ApplicationException;

public Response getImplantdetailsList(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

public Response getImplantdetails(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

public Response getKitdetails(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

public Response searchImplantItems(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

public Response getImplantDetailsByProcedureId(ImplantDetailsDto implantDetailsDto, int i) throws ApplicationException;

public Response searchImplantKit(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

public Response getImplantIndentAcceptanceData(IssueAcceptanceDto issueAcceptanceDto) throws ApplicationException;

public Response updateImplantDetailsIsExported(List<ImplantDetailsDto> list) throws ApplicationException;

public Response getImplantDetailsByProcedureIdOnOTIMS(ImplantDetailsDto implantDetailsDto) throws ApplicationException;

public Response cancelImplantdetails(OTReservationDto oTReservationdto)throws ApplicationException;

}
