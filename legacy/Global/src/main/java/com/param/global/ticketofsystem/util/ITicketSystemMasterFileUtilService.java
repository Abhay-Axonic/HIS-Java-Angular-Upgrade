package com.param.global.ticketofsystem.util;

import org.springframework.web.multipart.MultipartFile;
import com.param.global.common.Response;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.ticketofsystem.dto.TicketSystemMasterFilesDto;
@SuppressWarnings({"rawtypes","unchecked"})
public interface ITicketSystemMasterFileUtilService {
	
	public Response uploadTicketSystemMasterFile(MultipartFile[] files, TicketSystemMasterFilesDto ticketSystemMasterFilesDto, UnitMasterDto unitMasterDto);
	
}
