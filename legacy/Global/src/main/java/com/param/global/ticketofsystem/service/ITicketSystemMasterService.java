package com.param.global.ticketofsystem.service;


import org.springframework.web.multipart.MultipartFile;
import com.param.global.common.Response;
import com.param.global.ticketofsystem.dto.TicketSystemMasterDto;

@SuppressWarnings({"rawtypes","unchecked"})
public interface ITicketSystemMasterService {
	
	public Response getTicketSystemMasterById(Integer ticketMasterId, Integer unitId, Integer organizationId);
	
	public Response getAllTicketSystemMasterList(Integer unitId, Integer organizationId);
	
	public Response addTicketSystemMaster(TicketSystemMasterDto ticketSystemMasterDto );
	
	public Response updateTicketSystemMaster(TicketSystemMasterDto ticketSystemMasterDto);
	
	public Response changeTicketSystemMasterStatus(Integer ticketMasterId, Integer unitId, Integer organizationId,Character ticketStatus);

	public Response saveTicketSystemMasterfile(Integer ticketMasterId,String organizationName, Integer unitId, Integer organizationId,
			 Integer userId, MultipartFile[] files,Boolean updateFileFlage);
	
	public Response getAllTicketSystemMasterfile(Integer ticketMasterId, Integer unitId, Integer organizationId);
	
	public Response removeTicketSystemMasterfile(Integer fileId, Integer unitId, Integer organizationId);

	public Response removeAllTicketSystemMasterfile(Integer ticketMasterId, Integer unitId, Integer organizationId);

	public Response searchTicketSystem(TicketSystemMasterDto ticketSystemMasterDto);
}
