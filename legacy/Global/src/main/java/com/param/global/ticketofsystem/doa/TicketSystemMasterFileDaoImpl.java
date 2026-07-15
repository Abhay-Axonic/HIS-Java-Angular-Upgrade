package com.param.global.ticketofsystem.doa;
import in.param.exception.ApplicationException;
import com.param.global.common.Response;
import com.param.global.model.TicketSystemMasterFiles;
import com.param.global.ticketofsystem.dto.TicketSystemMasterFilesDto;

import in.param.common.dao.GenericDao;
import static com.param.global.common.ICommonConstants.*;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class TicketSystemMasterFileDaoImpl extends GenericDao<TicketSystemMasterFiles,Integer> implements ITicketSystemMasterFileDao {
	final static Logger logger = Logger.getLogger(TicketSystemMasterFileDaoImpl.class);


	public TicketSystemMasterFileDaoImpl() {
		super(TicketSystemMasterFiles.class);
	}
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	public Response saveTicketSystemMasterfile( List<TicketSystemMasterFiles>  ticketSystemMasterFiles , MultipartFile[] files) {
		
		try {
			if (!ticketSystemMasterFiles.isEmpty()) {
				Integer i = 0;
				for (TicketSystemMasterFiles masterFiles : ticketSystemMasterFiles) {
					masterFiles.setFileName(files[i].getOriginalFilename());
					save(masterFiles);
					i++;
				}
 				return new Response(SUCCESS, SUCCESS_CODE, " File Attached Successfully ", null, null);
			} else {
				return new Response(NOT_FOUND, NO_DATA_FOUND, "No File Attached ", null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	public Response getAllTicketSystemMasterfile(Integer ticketMasterId, Integer unitId, Integer organizationId) {
		try {
			List<TicketSystemMasterFilesDto> list= sessionFactory.getCurrentSession()
	                .getNamedQuery("GET_All_TICKET_FILES_BY_TICKET_ID") 
	                .setParameter("ticketMasterId",ticketMasterId)
	                .setParameter("orgId",organizationId)
	                .setParameter("unitId",unitId)
	                .setResultTransformer(Transformers.aliasToBean(TicketSystemMasterFilesDto.class)).list();
	       if(!list.isEmpty()) {
	    	   return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);   
	       }
			else {
				return new Response<>(NO_DATA_FOUND, NOT_FOUND_CODE, "No File Attached",  null , null);
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	public Response removeTicketSystemMasterfile(Integer fileId,Integer unitId, Integer organizationId) {
		try {
			  Integer updateCount = sessionFactory.getCurrentSession()
			           .getNamedQuery("UPDATE_TICKET_FILES_STATUS_BY_FILE_ID")
			           .setParameter("fileId", fileId)
			           .setParameter("fileUpdatedDate", new Date())
			           .setParameter("orgId", organizationId)
			           .setParameter("unitId", unitId)
			           .executeUpdate();
			if(updateCount>0 && updateCount!=0) {
				
				 return new Response<>(SUCCESS, SUCCESS_CODE, "Attachment Removed Successfully ",null, null); 
			}
			else {
				return new Response<>(NO_DATA_FOUND, NOT_FOUND_CODE, "No File Attached",  null , null);
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	public Response removeAllTicketSystemMasterfile(Integer ticketMasteId, Integer unitId, Integer organizationId) {
		try {
			 Integer updateCount = sessionFactory.getCurrentSession()
			           .getNamedQuery("UPDATE_TICKET_STATUS_BY_TICKET_ID")
			           .setParameter("ticketMasterId", ticketMasteId)
			           .setParameter("fileUpdatedDate", new Date())
			           .setParameter("orgId", organizationId)
			           .setParameter("unitId", unitId)
			           .executeUpdate();
			if(updateCount>0 && updateCount!=0) {
				 return new Response<>(SUCCESS, SUCCESS_CODE, "All Attachments Removed Successfully ",null, null ); 
			}
			else {
				return new Response<>(NO_DATA_FOUND, NOT_FOUND_CODE, "No File Attached",  null , null);
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

}
