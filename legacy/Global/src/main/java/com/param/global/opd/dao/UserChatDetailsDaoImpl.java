package com.param.global.opd.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.param.global.dto.TwilioUserChatDetailsDto;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UserChatDetailsDto;
import com.param.global.model.CabinMaster;
import com.param.global.model.UserChatDetails;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Repository
	public class UserChatDetailsDaoImpl extends GenericDao<UserChatDetails, Integer> implements IUserChatDetailsDao,ICommonConstants
	{

		private static final Logger logger = Logger.getLogger(UserChatDetailsDaoImpl.class);

		public UserChatDetailsDaoImpl() {
			super(UserChatDetails.class);
		}

		@Autowired
		SessionFactory sessionFactory;

		@Override
		public Response saveUserChatDetails(UserChatDetailsDto userChatDetailsDto) throws ApplicationException {
			try {
				UserChatDetails userChatDetails=new UserChatDetails();
				userChatDetails.setAppointmentIdMyLife(userChatDetailsDto.getAppointmentIdMyLife());
				userChatDetails.setUuid(userChatDetailsDto.getUuid());
				userChatDetails.setUnitId(userChatDetailsDto.getUnitId());
				userChatDetails.setStatus(userChatDetailsDto.getChatStatusId());
				userChatDetails.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(userChatDetailsDto.getUnitId()), "yyy-MM-dd HH:mm:ss"));
				userChatDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(userChatDetailsDto.getUnitId()), "yyy-MM-dd HH:mm:ss"));
				userChatDetails.setDoctorId(userChatDetailsDto.getDoctorId());
				userChatDetails.setChatStatusId(1);
				userChatDetails.setDateTime(GlobalCommonDateUtils.getDate(userChatDetailsDto.getDateTime(), "yyy-MM-dd HH:mm:ss"));
				userChatDetails.setTimeSlot(userChatDetailsDto.getTimeSlot());
				save(userChatDetails);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

			} catch (HibernateException he) {
				logger.error("Error while saving user chat details", he);
			} catch (Exception e) {
				logger.error("Error while saving user chat details", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getUserChatDetails(UserChatDetailsDto userChatDetailsDto) throws ApplicationException {
			try{
				List<UserChatDetails> listUserChatDetailsDto = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_CHATS_BY_DETAILS_TWILIO")
						.setParameter("doctorId", userChatDetailsDto.getDoctorId())
						.setParameter("appointmentId", userChatDetailsDto.getAppointmentIdMyLife())
						.setParameter("unitId", userChatDetailsDto.getUnitId())
						.setParameter("uuid", userChatDetailsDto.getUuid())
						.setResultTransformer(Transformers.aliasToBean(UserChatDetails.class)).list();
				return new Response(SUCCESS, null, null,listUserChatDetailsDto,null );
			}catch(HibernateException he){
				logger.error("Error while getting user chat details", he);
			}catch(Exception e){
				logger.error("Error while getting user chat details", e);
			}finally{

			}
			return new Response(ERROR, null, null, null,null);
		}

		@Override
		public Response updateUserChatDetails(UserChatDetailsDto updatedto) throws ApplicationException {
			try {
				UserChatDetails userChatDetails=new UserChatDetails();
				
				 userChatDetails=findById(updatedto.getUserChatDetailsId());
				 userChatDetails.setStatus(2);
				 userChatDetails.setChatStatusId(1);
			     update(userChatDetails);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

			} catch (HibernateException he) {
				logger.error("Error while updating user chat details", he);
			} catch (Exception e) {
				logger.error("Error while updating user chat details", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getVideoataForNotification(AppointmentStatusSyncDto dto) throws ApplicationException {
			try{
			    
				if(dto.getDoctorId() != null ){
			    Date date = new Date(); 
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        String formattedDate = sdf.format(date);
			
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_CHATS_BY_CHAT_ID_TWILIO_NOTIFICATION").getQueryString().toString());
			stringQuery.append(" AND to_char(usrChatDtls.created_date,'yyyy-mm-dd') Between :formattedDate and  :formattedDate ");
			List<AppointmentStatusSyncDto> listUserChatDetailsDto = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
					.setParameter("doctorId", dto.getDoctorId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("formattedDate",formattedDate)
					.setResultTransformer(Transformers.aliasToBean(AppointmentStatusSyncDto.class)).list();
				return new Response(SUCCESS, null, null,listUserChatDetailsDto,null );
				}
			}catch(HibernateException he){
				logger.error("Error while getting video data for notification", he);
			}catch(Exception e){
				logger.error("Error while getting video data for notification", e);
			}finally{

			}
			return new Response(ERROR, null, null, null,null);
		}

		@Override
		public Response changeStatusofuserChatDetailsId(AppointmentStatusSyncDto dto) throws ApplicationException {
			try{
				 sessionFactory.getCurrentSession()
							.getNamedQuery("UPDATE_CHATS_BY_CHAT_ID_TWILIO")
							.setParameter("chatId", dto.getUserChatDetailsId())
							.setResultTransformer(Transformers.aliasToBean(AppointmentStatusSyncDto.class)).executeUpdate();
					return new Response(SUCCESS, null, null, null,null);
				}catch(HibernateException he){
					logger.error("Error while changing status of user chat details id", he);
				}catch(Exception e){
					logger.error("Error while changing status of user chat details id", e);
				}finally{

				}
				return new Response(ERROR, null, null, null,null);
			}

		@Override
		public Response getChatsByToUserIdChatIdTwilio(UserChatDetailsDto userChatDetailsDto) throws ApplicationException {
			try{
				//Date date = CommonDateUtils.getDate(userChatDetailsDto.getDateTime(), "dd-MM-yyyy");
				List<TwilioUserChatDetailsDto> listUserChatDetailsDto = sessionFactory.getCurrentSession()
						.getNamedQuery("")
						.setInteger("userId", userChatDetailsDto.getToUserId())
						.setInteger("chatId", userChatDetailsDto.getUserChatDetailsId())
						.setString("date", userChatDetailsDto.getDateTime())
						.setResultTransformer(Transformers.aliasToBean(TwilioUserChatDetailsDto.class)).list();
				return new Response(SUCCESS, null, null, listUserChatDetailsDto,null);
			}catch(Exception e){
				logger.error("Error while getting chats by to user id chat id twilio", e);
				return new Response(ERROR, null, null, null,null);
			}
		}

}
