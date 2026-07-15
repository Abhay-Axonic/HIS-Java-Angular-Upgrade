package com.param.global.ticketofsystem.doa;

import com.param.global.common.Response;
import com.param.global.ticketofsystem.dto.TicketSystemMasterDto;

@SuppressWarnings({"rawtypes","unchecked"})
public interface ITicketSystemMasterDao {
	
	public Response getTicketSystemMasterById(Integer ticketMasterId);
	
	public Response getAllTicketSystemMasterList(Integer unitId, Integer organizationId);
	
	public Response addTicketSystemMaster(TicketSystemMasterDto ticketSystemMasterDto );
	
	public Response updateTicketSystemMaster(TicketSystemMasterDto ticketSystemMasterDto);
	
	public Response changeTicketSystemMasterStatus(Integer ticketMasterId, Integer unitId, Integer organizationId,Character ticketStatus);
	
	public Response getDataForPdsSave (Integer unitId);

	public Response searchTicketSystem(TicketSystemMasterDto ticketSystemMasterDto);

	public void updateTicketNumberInMaster(TicketSystemMasterDto ticketSystemMasterDto);
}
