package com.param.otims.service;

import com.param.global.dto.RadiologyOrdersListDto;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.mail.IMailingService;
import com.param.global.otims.dao.IImplantDetailsDao;
import com.param.global.otims.dao.IMedicationDetailsDao;
import com.param.global.otims.dao.IOtCompletionTimeSheetDao;
import com.param.global.otims.dao.IProcedureOTScheduleDao;
import com.param.global.otims.dto.OtCompletionTimeSheetDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.WhoChecklistDto;
import com.param.otims.dao.IPreOperativeDetailsDao;
import com.param.otims.dao.IWhoChecklistDao;
import com.param.otims.model.OtCompletionTimeSheet;
import com.param.otims.model.PreOperativeDetails;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@Service
@SuppressWarnings({ "rawtypes","unchecked","unused"})
public class PreOperativeDetailsServiceImpl  implements IPreOperativeDetailsService, ICommonConstants ,IBillingCommonConstants{

	private static final Logger logger = Logger.getLogger(PreOperativeDetailsServiceImpl.class);

	@Autowired IPreOperativeDetailsDao iPreOperativeDetailsDao;
	
	@Autowired IWhoChecklistDao oWhoChecklistDao;
	
	@Autowired
	private IOtCompletionTimeSheetDao iOtCompletionTimeSheetDao;
	
	@Autowired
	private IProcedureOTScheduleDao iProcedureOTScheduleDao;

	@Autowired 
	IMailingService iMailingService;
	
	@Autowired
	IMedicationDetailsDao iMedicationDetailsDao;
	
	@Autowired
	private IImplantDetailsDao iImplantDetailsDao;
	
	@Override
	public Response savePreOperativeDetails(PreOperativeDetailsDto preOperativeDetailsDto) throws ApplicationException {
		try {
			Response res =  iPreOperativeDetailsDao.savePreOperativeDetails(preOperativeDetailsDto);
			OtCompletionTimeSheet timesheetDto   = new OtCompletionTimeSheet();
			if(res.getObject() != null ) {
				
				PreOperativeDetails mstrDto = (PreOperativeDetails) res.getObject();
				
				for (WhoChecklistDto dto : preOperativeDetailsDto.getWhoChecklistDtoList()) {
					dto.setPreOperativeId(mstrDto.getPreOperativeId());
					oWhoChecklistDao.saveWhoChecklist(dto);
				}
				preOperativeDetailsDto.getOtCompletionTimeSheetDto().setProReqId(preOperativeDetailsDto.getProReqId());
				Response timesheetRes =	iOtCompletionTimeSheetDao.saveOrUpdateOtCompletionTimeSheet(preOperativeDetailsDto.getOtCompletionTimeSheetDto());
				
				if(timesheetRes.getObject()!=null) {
					 timesheetDto = (OtCompletionTimeSheet)timesheetRes.getObject();
				}
				
				if(preOperativeDetailsDto.getImplantDetailsList()!=null && preOperativeDetailsDto.getImplantDetailsList().size()>0) {
					
					iImplantDetailsDao.saveImplantdetails(preOperativeDetailsDto.getImplantDetailsList(),preOperativeDetailsDto.getProReqId(),preOperativeDetailsDto.getProcedureOrderId());
					
				}
				
				if(preOperativeDetailsDto.getMedicationDetailsList()!=null && preOperativeDetailsDto.getMedicationDetailsList().size()>0) {
					
					iMedicationDetailsDao.saveMedicationDetails(preOperativeDetailsDto.getMedicationDetailsList(),preOperativeDetailsDto.getProcedureOrderId());
					
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, timesheetDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getPreOperativeDetailsByProReqId(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try {
			Response res =  iPreOperativeDetailsDao.getPreOperativeDetailsByProReqId(preOperativeDetailsDto);
			if(res.getObject()!=null) {
				PreOperativeDetailsDto dto = (PreOperativeDetailsDto)res.getObject();
				Response whoChecklistRes =  oWhoChecklistDao.getWhoChecklistByPreOperativeId(dto);
				if(whoChecklistRes.getListObject()!=null && whoChecklistRes.getListObject().size()>0) {
					dto.setWhoChecklistDtoList(whoChecklistRes.getListObject());
				}
				Response wheelInTimeSheetRes  =   iProcedureOTScheduleDao.getWheelInDataByProReqId(dto.getProReqId());
				if(wheelInTimeSheetRes.getStatus().equals(SUCCESS) && wheelInTimeSheetRes.getObject()!=null) {
					ProcedureOrderMasterDto wheelInTimeSheetObj = (ProcedureOrderMasterDto)wheelInTimeSheetRes.getObject();
					OtCompletionTimeSheetDto timesheet = new OtCompletionTimeSheetDto() ;
					timesheet.setOtCompletionTimeSheetId(wheelInTimeSheetObj.getOtCompletionTimeSheetId());
					dto.setOtCompletionTimeSheetDto(timesheet);
				}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getActiveDoctorList(PreOperativeDetailsDto preOperativeDetailsDto) throws ApplicationException {
		try {
			
			return iPreOperativeDetailsDao.getActiveDoctorList(preOperativeDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response sendNotificationToDoctor(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try {
			
			return iMailingService.sendNotificationToDoctor(preOperativeDetailsDto);
		
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getAnaesthesiaDoctorList(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try {
			
			return iPreOperativeDetailsDao.getAnaesthesiaDoctorList(preOperativeDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getProcedureOrderIdByProReqId(PreOperativeDetailsDto preOperativeDetailsDto)
			throws ApplicationException {
		try {
			
			return iPreOperativeDetailsDao.getProcedureOrderIdByProReqId(preOperativeDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getAiTranscriptionCreds(PreOperativeDetailsDto preOperativeDetailsDto) throws ApplicationException {
		try {

//			String prompt = "You are a medical language model. Generate a structured and clinically accurate radiology report using the " +
//					" following template: {template} Use appropriate radiological terminology. " +
//					" Fill each section of the template based on findings and context from the " +
//					" conversation.Keep the report concise, objective, and professional.If any " +
//					" sections are not mentioned in the conversation, leave them empty or write " +
//					" \"Not mentioned.\" Do it for following radiologist conversation: ";

			return iPreOperativeDetailsDao.getAiTranscriptionCreds(preOperativeDetailsDto);

		}catch (Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
