package com.param.global.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.MessageQueueDto;
import com.param.global.model.MessageQueue;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MessageQueueDaoImpl extends GenericDao<MessageQueue, Integer>implements IMessageQueueDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(MessageQueueDaoImpl.class);


	public MessageQueueDaoImpl() {
		super(MessageQueue.class);
	}
//
//	@Autowired
//	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
//	
//	@Autowired
//	DocUploadUtility docUploadUtility;
//	
//	@Autowired
//	IwhatsAppServiceImpl iwhatsAppServiceImpl;
//	
//	@Autowired
//	IMessageQueueDao iMessageQueueDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response addMailMessageInQueue(MessageQueueDto smsInfo) throws ApplicationException {
		try
		{	
			MessageQueue dto = new MessageQueue();
				
			 dto.setCategoryId(smsInfo.getCategoryId());
			 dto.setCategoryName(smsInfo.getCategoryName());
//			 dto.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 dto.setMailContent(smsInfo.getMailContent() != null ? smsInfo.getMailContent() : null );
			 dto.setMailFileName(smsInfo.getMailFileName()!= null ? smsInfo.getMailFileName() : null);
			 dto.setMailSubject(smsInfo.getMailSubject() != null ? smsInfo.getMailSubject() : null);
			 dto.setMessageType(SendMail);  //1 for mail 
			 dto.setOrganizationId(smsInfo.getOrganizationId());
			 dto.setReportString(smsInfo.getReportString() != null ? smsInfo.getReportString() : null);
			 dto.setRetryAttemps(RetryAttemps);
			 dto.setToMailId(smsInfo.getToMailId());
			 dto.setTriggerId(smsInfo.getTriggerId());
			 dto.setUniqueId(smsInfo.getUniqueId() != null ? smsInfo.getUniqueId() : null);
			 dto.setUnitId(smsInfo.getUnitId());
			 dto.setStatus('A');
//			 dto.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 dto.setUserId(smsInfo.getUserId());
			 dto.setSelected(false);
			 dto.setCreatedDate(smsInfo.getCyclicOrderDate()!=null ? GlobalCommonDateUtils.getDate(smsInfo.getCyclicOrderDate(), "dd-M-yyyy HH:mm:ss") : 
				 TimeZoneComponent.getDateByzone(smsInfo.getUnitId()));
			 dto.setUpdatedDate(smsInfo.getCyclicOrderDate()!=null ? GlobalCommonDateUtils.getDate(smsInfo.getCyclicOrderDate(), "dd-M-yyyy HH:mm:ss") : 
				 TimeZoneComponent.getDateByzone(smsInfo.getUnitId()));
			 dto = save(dto);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);

		} catch (Exception e)
		{
		logger.error("addMailMessageInQueue failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response addWhatsAppMessageInQueue(MessageQueueDto smsInfo) throws ApplicationException {
		try
		{	
			MessageQueue dto = new MessageQueue();
				
			 dto.setCategoryId(smsInfo.getCategoryId());
			 dto.setCategoryName(smsInfo.getCategoryName());
//			 dto.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 dto.setMailContent(smsInfo.getMailContent());
			 dto.setMailFileName(smsInfo.getMailFileName());
			 dto.setToWhatsappNumber(smsInfo.getToWhatsappNumber());
			 dto.setMessageType(SendWhatsapp);  //2 for whatsapp
			 dto.setOrganizationId(smsInfo.getOrganizationId());
			 dto.setReportString(smsInfo.getReportString());
			 dto.setRetryAttemps(RetryAttemps);
			 dto.setTriggerId(smsInfo.getTriggerId());
			 dto.setUniqueId(smsInfo.getUniqueId() != null ? smsInfo.getUniqueId() : null);
			 dto.setUnitId(smsInfo.getUnitId());
			 dto.setStatus('A');
			 dto.setSelected(false);
//			 dto.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 dto.setCreatedDate(smsInfo.getCyclicOrderDate()!=null ? GlobalCommonDateUtils.getDate(smsInfo.getCyclicOrderDate(), "dd-M-yyyy HH:mm:ss") : 
				 TimeZoneComponent.getDateByzone(smsInfo.getUnitId()));
			 dto.setUpdatedDate(smsInfo.getCyclicOrderDate()!=null ? GlobalCommonDateUtils.getDate(smsInfo.getCyclicOrderDate(), "dd-M-yyyy HH:mm:ss") : 
				 TimeZoneComponent.getDateByzone(smsInfo.getUnitId()));
			 dto.setUserId(smsInfo.getUserId());
			 dto.setWhatsAppTemplateMasterId(smsInfo.getWhatsAppTemplateMasterId() != null ? smsInfo.getWhatsAppTemplateMasterId() : null);
			 dto.setMailFileName(smsInfo.getMailFileName() != null ? smsInfo.getMailFileName() : null);
			 dto.setWhatsappDocumentUrl(smsInfo.getWhatsappDocumentUrl() != null ? smsInfo.getWhatsappDocumentUrl() : null);
			 save(dto);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e)
		{
		logger.error("addWhatsAppMessageInQueue failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response addTextSMSMessageInQueue(MessageQueueDto smsInfo) throws ApplicationException {
		try
		{	
			MessageQueue dto = new MessageQueue();
			 dto.setTextMessageContent(smsInfo.getTextMessageContent());
			 dto.setMobileNumber(smsInfo.getMobileNumber());
			 dto.setCategoryId(smsInfo.getCategoryId());
			 dto.setCategoryName(smsInfo.getCategoryName());
//			 dto.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 dto.setMessageType(SendTextMessage);  //3 for Text SMS
			 dto.setOrganizationId(smsInfo.getOrganizationId());
			 dto.setRetryAttemps(RetryAttemps);
			 dto.setTriggerId(smsInfo.getTriggerId());
			 dto.setUniqueId(smsInfo.getUniqueId() != null ? smsInfo.getUniqueId() : null);
			 dto.setUnitId(smsInfo.getUnitId());
			 dto.setStatus('A');
			 dto.setSelected(false);
//			 dto.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			 dto.setCreatedDate(smsInfo.getCyclicOrderDate()!=null ? GlobalCommonDateUtils.getDate(smsInfo.getCyclicOrderDate(), "dd-M-yyyy HH:mm:ss") : 
				 TimeZoneComponent.getDateByzone(smsInfo.getUnitId()));
			 dto.setUpdatedDate(smsInfo.getCyclicOrderDate()!=null ? GlobalCommonDateUtils.getDate(smsInfo.getCyclicOrderDate(), "dd-M-yyyy HH:mm:ss") : 
				 TimeZoneComponent.getDateByzone(smsInfo.getUnitId()));
			 dto.setUserId(smsInfo.getUserId());
			 save(dto);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e)
		{
		logger.error("addTextSMSMessageInQueue failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMessageFromQueqe(int type) throws ApplicationException {
	try {
			
		 
        Calendar calendar = Calendar.getInstance();
      
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        Date fromDate = calendar.getTime();  
      
        calendar.add(Calendar.DAY_OF_MONTH, 6);  
        Date toDate = calendar.getTime();  
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedFromDate = sdf.format(fromDate);
        String formattedToDate = sdf.format(toDate);
		
		    String query = sessionFactory.getCurrentSession().getNamedQuery("GET_MESSAGE_FROM_QUEQE").getQueryString();
			StringBuffer sb= new StringBuffer(query);
			
		      sb.append(" where mass.selected = false and mass.unit_id is not null and mass.organization_id is not null "
		                + " and mass.message_type = :type" 
		                + " and to_char(mass.created_date,'YYYY-MM-DD') >= :formattedFromDate "
		                + " and to_char(mass.created_date,'YYYY-MM-DD') <= :formattedToDate ");

			List<MessageQueueDto> list = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("type", type)
					.setParameter("formattedFromDate", formattedFromDate)
					.setParameter("formattedToDate", formattedToDate)
					.setResultTransformer(Transformers.aliasToBean(MessageQueueDto.class)).list();
			
			
			if(list!=null &&  list.size()>0) {
				
				List<Integer> queqeIds = new ArrayList<Integer>();
				
				for (MessageQueueDto id : list) {
					queqeIds.add(id.getMessageQueueId());
				}
				
				sessionFactory.getCurrentSession().createSQLQuery(" update t_message_queue set selected = true where message_queue_id in("+queqeIds.toString().replaceAll("\\[","").replaceAll("\\]","")+")").executeUpdate();
				
			}
			
			
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
		logger.error("getMessageFromQueqe failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response deletMessageFromQueqe(Integer messageQueueId) throws ApplicationException {
		try {
			
			sessionFactory.getCurrentSession().createSQLQuery(" DELETE FROM public.t_message_queue WHERE message_queue_id  =:messageQueueId ").setParameter("messageQueueId",messageQueueId).executeUpdate();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	} catch (Exception e) {
	logger.error("deletMessageFromQueqe failed", e);
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	}

	@Override
	public Response updateRetryCount(Integer messageQueueId,String error) throws ApplicationException {
		try {

			String query = "UPDATE t_message_queue SET retry_attemps = (retry_attemps - 1), selected = false, error_message = :error " +
					"WHERE message_queue_id = :messageQueueId";
			sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("error", error)  // Parameter for the error message
					.setParameter("messageQueueId", messageQueueId)  // Parameter for messageQueueId
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("updateRetryCount failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response checkAlreadyExitsOrNot(String uniqueId, Integer messageType ,Integer triggerId) throws ApplicationException {
		try {
			if (uniqueId != null) {
				String query = "SELECT message_queue_id AS \"messageQueueId\" " +
						"FROM public.t_message_queue " +
						"WHERE trigger_id = :triggerId AND message_type = :messageType AND unique_id = :uniqueId";

				List<MessageQueueDto> messageQueueIdList = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("triggerId", triggerId)  // Parameter for triggerId
						.setParameter("messageType", messageType)  // Parameter for messageType
						.setParameter("uniqueId", uniqueId)  // Parameter for uniqueId
						.setResultTransformer(Transformers.aliasToBean(MessageQueueDto.class)) // Transform to DTO
						.list();

				if (messageQueueIdList != null && messageQueueIdList.size() > 0) {

					for (MessageQueueDto messageQueueDto : messageQueueIdList) {
						MessageQueue mstr = findById(messageQueueDto.getMessageQueueId());
						mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
						save(mstr);
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, null, messageQueueIdList, null);
				} else {
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e) {
		logger.error("checkAlreadyExitsOrNot failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response updateBulkMailLogId(Integer bulkMessageQueueId, String errorMessage, Integer logId)
			throws ApplicationException {
		try {
			
			/*Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery("update t_bulk_message_queue set retry_attemps  = (retry_attemps - 1),message_log_id  = "+logId+", error_message = '"+errorMessage+"' where bulk_message_queue_id  = "+bulkMessageQueueId)
					.executeUpdate();*/
			
			
		return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("updateBulkMailLogId failed", e);
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

	/*@Override
	public Response sendWhatsAppNewMethod(MessageQueueDto messageQueueDto) throws ApplicationException {
		try {
		ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		boolean successLogSaveFlag = false;
		boolean faildLogSaveFlag = false;
		String errorMessage = "";
		if (messageQueueDto.getPdfGenerationUrl() != null && messageQueueDto.getMailContent() != null) {
			
			try {
				
				
				
				boolean reportIsNull = false;
				if(messageQueueDto.getReportString()!= null && !messageQueueDto.getReportString().equals("")) {
					
					HttpPost pst = new HttpPost(messageQueueDto.getPdfGenerationUrl());
					pst.addHeader("Content-Type", "application/json");
					pst.setEntity(new StringEntity(messageQueueDto.getReportString()));

					HttpResponse result = httpClient.execute(pst);
					HttpEntity resEntity = result.getEntity();
					resEntity.writeTo(byteOutPutStream);
					
					if(result.getStatusLine().getStatusCode() == 200) {
						reportIsNull = true;
					}
					
				}else {
					reportIsNull = true;
				}
				
				if(reportIsNull) {            // RPF file generated successfully 
					 
					 Response resMsg = null;
				    UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
					unitApplicationConfigurationDto.setOrganizationId(messageQueueDto.getOrganizationId());
					unitApplicationConfigurationDto.setUnitId(messageQueueDto.getUnitId());
			    com.param.global.common.Response appConfigRes= iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				if(unitApplicationConfigurationDtos.getWhatsAppOption() == null) {
					Integer whh = 1;
					unitApplicationConfigurationDtos.setWhatsAppOption(whh);
				}
			if( unitApplicationConfigurationDtos.getWhatsAppOption() == 1) {
						whatsAppRequestDto newDto = new whatsAppRequestDto();
						newDto.setTo_number(messageQueueDto.getToWhatsappNumber());
						SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
						smsInfo.setUnitId(messageQueueDto.getUnitId());
						smsInfo.setOrganizationId(messageQueueDto.getOrganizationId());
						smsInfo.setUnitFolder(messageQueueDto.getUnitFolder());
						
			        String filePath =  docUploadUtility.uploadwhatsAppDocumentFile(byteOutPutStream,messageQueueDto.getMailFileName(),smsInfo);
			        newDto.setUnitId(messageQueueDto.getUnitId());  
			        newDto.setOrganizationId(messageQueueDto.getOrganizationId());
					newDto.setMediaUrl(filePath);
					newDto.setMessage(messageQueueDto.getMailContent());
					newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
					 resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageTwilio(newDto);
					}else if( unitApplicationConfigurationDtos.getWhatsAppOption() == 2) {
						whatsAppRequestDto newDto = new whatsAppRequestDto();
						newDto.setUnitId(messageQueueDto.getUnitId());
						newDto.setOrganizationId(messageQueueDto.getOrganizationId());
						newDto.setTo(messageQueueDto.getToWhatsappNumber());
					    newDto.setText(messageQueueDto.getMailContent());
						newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
					
					 resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageVonage(newDto);
			       }else if( unitApplicationConfigurationDtos.getWhatsAppOption() == 3) {
			    	   
			    		whatsAppRequestDto newDto = new whatsAppRequestDto();
			    	    newDto.setUnitId(messageQueueDto.getUnitId());
						newDto.setOrganizationId(messageQueueDto.getOrganizationId());
						//Authorization
			    	   Response resss = iwhatsAppServiceImpl.authontificationKeyGenerationForHeloAPI(newDto);
			    	   
					   if(resss.getCode().equals("2000")) {
						    newDto.setUnitId(messageQueueDto.getUnitId());
							newDto.setOrganizationId(messageQueueDto.getOrganizationId());
							newDto.setTo(messageQueueDto.getToWhatsappNumber());
						    newDto.setText(messageQueueDto.getMailContent());
							newDto.setTypeOf(unitApplicationConfigurationDtos.getWhatsAppOption());
							newDto.setResponseJson(resss.getObject());
						 resMsg = iwhatsAppServiceImpl.sendWhatsAppMessageHelo(newDto);
					   }
						
			       }
						
					
					if(resMsg.getObject() == null) {
						
						errorMessage =WHATSAPPNOTSENT;
						
						if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {             // if last retry attemps
							
							iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId()); 
							faildLogSaveFlag = true;
							
							
						}else {                                               // minus retry attemps by 1 
							
							iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);
						}
						
					}else {                                                    // whatsapp send  successfully
						errorMessage =WHATSAPPSENT;
						successLogSaveFlag = true;
						iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
					}
					
				}else {                                                        // RPF file Not generated  
					errorMessage =RPTFILLISSUE;
					if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {                 // if last retry attemps
						
						iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
						faildLogSaveFlag = true;
						
					}else {                                                    // minus retry attemps by 1
						
						iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);
					}
				}
			}catch(Exception e) {
				
			}
			
		}else {                                                       // Pdf Generation Url not found 
			errorMessage =PDFURLISSUE;
			if(messageQueueDto.getRetryAttemps().equals(LastRetryAttemps) || messageQueueDto.getRetryAttemps() < LastRetryAttemps ) {                 // if last retry attemps
				
				iMessageQueueDao.deletMessageFromQueqe(messageQueueDto.getMessageQueueId());
				faildLogSaveFlag = true;
				
			}else {                                                    // minus retry attemps by 1
				
				iMessageQueueDao.updateRetryCount(messageQueueDto.getMessageQueueId(),errorMessage);
			}
		}
		return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	} catch (Exception e) {
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
		}
*/
}
