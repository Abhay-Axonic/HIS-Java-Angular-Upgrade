package com.param.global.ticketofsystem.util;

import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.Response;
import com.param.global.ticketofsystem.dto.TicketSystemMasterDto;

public interface IProductionSupportAppService {

	public  Response addTicketToProductionSupport(TicketSystemMasterDto ticketSystemMasterDto );
	
	public Response uploadFile(MultipartFile[] file);
	
}
