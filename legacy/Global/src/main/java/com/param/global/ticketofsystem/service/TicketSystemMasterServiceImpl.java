package com.param.global.ticketofsystem.service;
import in.param.exception.ApplicationException;
import static com.param.global.common.ICommonConstants.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.model.TicketSystemMasterFiles;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.ticketofsystem.doa.ITicketSystemMasterDao;
import com.param.global.ticketofsystem.doa.ITicketSystemMasterFileDao;
import com.param.global.ticketofsystem.dto.TicketSystemMasterDto;
import com.param.global.ticketofsystem.dto.TicketSystemMasterFilesDto;
import com.param.global.ticketofsystem.util.IProductionSupportAppService;
import com.param.global.ticketofsystem.util.ITicketSystemMasterFileUtilService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
@Transactional
public class TicketSystemMasterServiceImpl implements ITicketSystemMasterService {
	final static Logger logger = Logger.getLogger(TicketSystemMasterServiceImpl.class);


	@Autowired
	ITicketSystemMasterDao iTicketSystemMasterDao;

	@Autowired
	ITicketSystemMasterFileDao iTicketSystemMasterFileDao; 
	
	@Autowired
	ITicketSystemMasterFileUtilService iTicketSystemMasterFileUtilService;
	
    @Autowired
    private IGlobalUnitMasterDao iGlobalUnitMasterDao;
    
    @Autowired
    private IProductionSupportAppService iProductionSupportAppService;

	@Override
	public Response getTicketSystemMasterById(Integer ticketMasterId, Integer unitId, Integer organizationId) {
		try {
			return iTicketSystemMasterDao.getTicketSystemMasterById(ticketMasterId);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}
	
	@Override
	public Response getAllTicketSystemMasterList(Integer unitId,Integer organizationId) {
		try {
			 Response allTicketSystemMasterList = iTicketSystemMasterDao.getAllTicketSystemMasterList(unitId,organizationId);
			List<TicketSystemMasterDto> setTicketSystemMasterissueDiscList = this.setTicketSystemMasterissueDisc(allTicketSystemMasterList.getListObject());
			return new Response<>(SUCCESS, SUCCESS_CODE, null, setTicketSystemMasterissueDiscList, allTicketSystemMasterList.getObject());
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	public Response addTicketSystemMaster(TicketSystemMasterDto ticketSystemMasterDto) {
		try {
			
			ticketSystemMasterDto.setTicketIssueDescription(this.updateTicketIssueDescription(ticketSystemMasterDto));
			return iTicketSystemMasterDao.addTicketSystemMaster(ticketSystemMasterDto);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	public Response updateTicketSystemMaster(TicketSystemMasterDto ticketSystemMasterDto) {
		try {
			String OrignalIssueDisc = ticketSystemMasterDto.getTicketIssueDescription();
			ticketSystemMasterDto.setTicketIssueDescription(this.updateTicketIssueDescription(ticketSystemMasterDto));
		    Response response = iTicketSystemMasterDao.updateTicketSystemMaster(ticketSystemMasterDto);
		    TicketSystemMasterDto updatedTicketSystemMaster = (TicketSystemMasterDto) response.getObject(); 
		    updatedTicketSystemMaster.setTicketIssueDescription(OrignalIssueDisc);
			return new Response(SUCCESS, SUCCESS_MESSAGE, "Ticket Updated Succesfully",null,updatedTicketSystemMaster);	 
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	public Response changeTicketSystemMasterStatus(Integer ticketMasterId, Integer unitId, Integer organizationId,Character ticketStatus) {
		try {
			return iTicketSystemMasterDao.changeTicketSystemMasterStatus(ticketMasterId, unitId, organizationId, ticketStatus);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}
	
	@Override
	public Response saveTicketSystemMasterfile(Integer ticketMasterId, String organizationName, Integer unitId,
			Integer userId, Integer organizationId, MultipartFile[] files,Boolean updateFileFlage) {
		try {
			
			Response unitRes = iGlobalUnitMasterDao.getUnitFolderPath(unitId);
			UnitMasterDto unitMasterDto = (UnitMasterDto) unitRes.getObject();

			if (files.length > 0 && files.length != 0) {
				TicketSystemMasterFilesDto ticketSystemMasterFilesDto = new TicketSystemMasterFilesDto();
				ticketSystemMasterFilesDto.setTicketMasterId(ticketMasterId);
				ticketSystemMasterFilesDto.setUnitId(unitId);
				ticketSystemMasterFilesDto.setOrganizationId(organizationId);
				ticketSystemMasterFilesDto.setUserId(userId);
				List<TicketSystemMasterFiles> ticketSystemMasterFileList = new LinkedList<TicketSystemMasterFiles>();
				Response fileNameResponse = iTicketSystemMasterFileUtilService.uploadTicketSystemMasterFile(files,
						ticketSystemMasterFilesDto, unitMasterDto);
				List<String> uploadedfilesUrl = fileNameResponse.getListObject();
				uploadedfilesUrl.forEach((url) -> {
					TicketSystemMasterFiles ticketSystemMasterFile = new TicketSystemMasterFiles();
					ticketSystemMasterFile.setTicketMasterId(ticketMasterId);
					ticketSystemMasterFile.setFileStatus('A');
					ticketSystemMasterFile.setFileUrl(url);
					ticketSystemMasterFile.setFileCreatedDate(new Date());
					ticketSystemMasterFile.setOrganizationId(organizationId);
					ticketSystemMasterFile.setUnitId(unitId);
					ticketSystemMasterFile.setUserId(userId);
					ticketSystemMasterFile.setFileCreatedBy(userId);
					ticketSystemMasterFileList.add(ticketSystemMasterFile);
				});

				Response savedTicketfileDetails = iTicketSystemMasterFileDao.saveTicketSystemMasterfile(ticketSystemMasterFileList,files);
				TicketSystemMasterDto ticketSystemMasterDto =null;
				if ((savedTicketfileDetails.getStatus() == "success" || savedTicketfileDetails.getStatus() == "Not Found" ) && updateFileFlage==false ) {
					Response response = iTicketSystemMasterDao.getTicketSystemMasterById(ticketMasterId);
					ticketSystemMasterDto =(TicketSystemMasterDto) response.getObject();
					
					Response dataForPdsSave =  iTicketSystemMasterDao.getDataForPdsSave(unitId);
					TicketSystemMasterDto jsonPdsData = (TicketSystemMasterDto) dataForPdsSave.getListObject().get(0);
					
					ticketSystemMasterDto.setTicketSystemJson(jsonPdsData.getTicketSystemJson());
					Response uploadFileRes = iProductionSupportAppService.uploadFile(files);
					Integer [] attachmentIdArray = (Integer[]) uploadFileRes.getObject();
					ticketSystemMasterDto.setAttachmentId(attachmentIdArray);
					Response addTicketToProductionSupport = iProductionSupportAppService.addTicketToProductionSupport(ticketSystemMasterDto); // add PDS saved msg or not...??
					
					ticketSystemMasterDto.setTicketNumber(addTicketToProductionSupport.getMessage());
					iTicketSystemMasterDao.updateTicketNumberInMaster(ticketSystemMasterDto);
					JSONObject jsonObject = new JSONObject(ticketSystemMasterDto.getTicketIssueDescription());
					String displayTicketIssueDescription = jsonObject.getString("Issue Description");
					
					ticketSystemMasterDto.setTicketIssueDescription(displayTicketIssueDescription);
					return new Response<>(SUCCESS, SUCCESS_CODE, "File And Ticket Added Successfully", null, ticketSystemMasterDto);
				}  
				else{
					return new Response<>(SUCCESS, SUCCESS_CODE, "Ticket updated Successfully", null, ticketSystemMasterDto);
				}

			}

			else {
				Response response = iTicketSystemMasterDao.getTicketSystemMasterById(ticketMasterId);
				TicketSystemMasterDto ticketSystemMasterDto =(TicketSystemMasterDto) response.getObject();
				Response dataForPdsSave =  iTicketSystemMasterDao.getDataForPdsSave(unitId);
				TicketSystemMasterDto jsonPdsData = (TicketSystemMasterDto) dataForPdsSave.getListObject().get(0);
				ticketSystemMasterDto.setTicketSystemJson(jsonPdsData.getTicketSystemJson());
				Response addTicketToProductionSupport = iProductionSupportAppService.addTicketToProductionSupport(ticketSystemMasterDto);
				ticketSystemMasterDto.setTicketNumber(addTicketToProductionSupport.getMessage());
				iTicketSystemMasterDao.updateTicketNumberInMaster(ticketSystemMasterDto);
				JSONObject jsonObject = new JSONObject(ticketSystemMasterDto.getTicketIssueDescription());
				String displayTicketIssueDescription = jsonObject.getString("Issue Description");
				ticketSystemMasterDto.setTicketIssueDescription(displayTicketIssueDescription);
				return new Response(NOT_FOUND, NO_DATA_FOUND, "No Files Attached ", null, ticketSystemMasterDto);
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	public Response getAllTicketSystemMasterfile(Integer ticketMasterId, Integer unitId, Integer organizationId) {
		try {
			return iTicketSystemMasterFileDao.getAllTicketSystemMasterfile(ticketMasterId, unitId, organizationId);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	public Response removeTicketSystemMasterfile(Integer fileId, Integer unitId, Integer organizationId) {
		try {
			 
			return iTicketSystemMasterFileDao.removeTicketSystemMasterfile(fileId, unitId, organizationId);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	public Response removeAllTicketSystemMasterfile(Integer ticketMasterId, Integer unitId, Integer organizationId) {
			
		try { 
			
			return iTicketSystemMasterFileDao.removeAllTicketSystemMasterfile(ticketMasterId, unitId, organizationId);
		
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	public Response searchTicketSystem(TicketSystemMasterDto ticketSystemMasterDto) {

		try {

			 Response allTicketSystemMasterList =iTicketSystemMasterDao.searchTicketSystem(ticketSystemMasterDto);
				List<TicketSystemMasterDto> setTicketSystemMasterissueDiscList = this.setTicketSystemMasterissueDisc(allTicketSystemMasterList.getListObject());
				return new Response<>(SUCCESS, SUCCESS_CODE, null, setTicketSystemMasterissueDiscList, allTicketSystemMasterList.getObject());

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}
	 
	private String updateTicketIssueDescription(TicketSystemMasterDto ticketSystemMasterDto) {

		String updatedTicketIssueDescription = "{   " + "  \"Unit\": \""
				+ ticketSystemMasterDto.getTicketReportedUnitName() + "\", " + "  \"Issue Description\": \""
				+ ticketSystemMasterDto.getTicketIssueDescription() + "\",  " + "  \"Reported By\": \""
				+ ticketSystemMasterDto.getTicketReportedBy() + "\"  " + "}   " + "";
		return updatedTicketIssueDescription;
	}
	
	private List<TicketSystemMasterDto> setTicketSystemMasterissueDisc(List<TicketSystemMasterDto> listObject) {

		listObject.forEach((ticketSystemMasterDto) -> {
			try {
				JSONObject jsonObject = new JSONObject(ticketSystemMasterDto.getTicketIssueDescription());
				String displayTicketIssueDescription = jsonObject.getString("Issue Description");
				ticketSystemMasterDto.setTicketIssueDescription(displayTicketIssueDescription);
			} catch (JSONException err) {
			logger.error("setTicketSystemMasterissueDisc failed", err);
			}
		});
		return listObject;
	}
}
