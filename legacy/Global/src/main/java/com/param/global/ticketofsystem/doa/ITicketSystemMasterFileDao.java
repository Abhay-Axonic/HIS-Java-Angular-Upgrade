package com.param.global.ticketofsystem.doa;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.Response;
import com.param.global.model.TicketSystemMasterFiles;

@SuppressWarnings({"rawtypes","unchecked"})
public interface ITicketSystemMasterFileDao {

	public Response saveTicketSystemMasterfile(List<TicketSystemMasterFiles> ticketSystemMasterFiles, MultipartFile[] files);
	
	public Response getAllTicketSystemMasterfile(Integer ticketMasterId, Integer unitId, Integer organizationId);
	
	public Response removeTicketSystemMasterfile(Integer fileId,Integer unitId, Integer organizationId);

	public Response removeAllTicketSystemMasterfile(Integer ticketMasterId, Integer unitId, Integer organizationId);
	
}
