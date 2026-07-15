package com.param.global.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.razorpay.AppointmentConfirmationDto;
import com.param.global.razorpay.RazorPayOrderLogDetail;
import com.param.global.razorpay.RazorpayPaymentReqDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Repository
public class RazorPayDaoImpl extends GenericDao<RazorPayOrderLogDetail, Integer> implements IRazorPayDao,ICommonConstants {

	final static Logger logger = Logger.getLogger(RazorPayDaoImpl.class);

	@Autowired
	private SessionFactory sf;


	public RazorPayDaoImpl() {
		super(RazorPayOrderLogDetail.class);
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public Response logRazorPayOrder(RazorpayPaymentReqDto reqDto) throws ApplicationException {
		try {
			RazorPayOrderLogDetail payOrderLogDetail=new RazorPayOrderLogDetail();
			payOrderLogDetail.setAppointmentId(reqDto.getAppointmentId());
			payOrderLogDetail.setOrderId(reqDto.getOrderId());
			payOrderLogDetail.setOrganizationId(reqDto.getOrganizationId());
			payOrderLogDetail.setUnitId(reqDto.getUnitId());
			payOrderLogDetail.setStatus('A');
			payOrderLogDetail.setAmount(Double.valueOf(reqDto.getAmount()));
			payOrderLogDetail.setVisitTypeId(reqDto.getVisitTypeId());
			sf.getCurrentSession().save(payOrderLogDetail);
			return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("logRazorPayOrder failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("logRazorPayOrder failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getAppointmentDataByRazorPayOrderId(String razorPayOrderId) throws ApplicationException {
		try {
			AppointmentConfirmationDto appointmentConfirmationDto=(AppointmentConfirmationDto)sf.getCurrentSession().createSQLQuery("select mrold.organization_id as \"organizationId\",mrold.unit_id as \"unitId\", " + 
					"mrold.amount as \"paidAmount\",mrold.appointment_id as \"appointmentId\", " + 
					"mrold.visit_type_id as \"visitTypeId\",tpr.patient_id as \"patientId\", " + 
					"msm.doctor_id as \"doctorId\",msm.speciality_id as \"specialityId\", " + 
					"to_char(msm.from_time,'HH:MI AM') as \"fromTime\",to_char(msm.from_time,'HH24:MI') as \"fromTimeSlot\",mum.unit_address as \"unitAddress\", " + 
					"concat(tpr.first_name,' ',tpr.last_name) as \"patientName\",tpr.mobile_number as \"mobileNumber\",tpr.email as \"emailId\", " + 
					"concat(mdm.first_name,' ',mdm.last_name) as \"doctorName\",mdm.doctor_image as \"doctorImage\",mam.remark as \"appointmentReason\", " + 
					"msm2.speciality_name as \"consultingSpeciality\",to_char(msm.appointment_date,'DD-MM-YYYY') as \"scheduledDate\", " + 
					"(select array_to_string(array(select docmapp.qualification_desc from doctor.t_doctor_doc_mapper docmapp where docmapp.doctor_id =msm.doctor_id), ', '))  as \"doctorDegrees\", " + 
					"(select array_to_string(array(select msm3.speciality_name from doctor.t_doctor_speciality_mapper tdsm " + 
					"inner join doctor.m_speciality_master msm3 on msm3.speciality_id =tdsm.speciality_id  " + 
					"where tdsm.status ='A' and tdsm.doctor_id=mdm.doctor_id), ', ')) as \"areaOfExperty\",mam.is_teleconsultation_apppointment as \"isTeleConsultApp\","+
				    "to_char(msm.to_time-msm.from_time,'MI') as \"visitDuration\" " + 
					"from public.m_razorpay_order_log_details mrold  " + 
					"inner join opd.m_appointment_master mam on mam.appointment_id =mrold.appointment_id  " + 
					"inner join opd.m_slot_master msm on msm.slot_id =mam.slot_id  " + 
					"inner join doctor.m_doctor_master mdm on mdm.doctor_id =msm.doctor_id  " + 
					"inner join public.m_unit_master mum on mum.unit_id=mrold.unit_id " + 
					"inner join doctor.m_speciality_master msm2 on msm2.speciality_id =msm.speciality_id  " + 
					"inner join patient.t_patient_registration tpr on tpr.patient_id =mam.patient_id " + 
					"where mrold.order_id=:orderId")
			.setParameter("orderId", razorPayOrderId)
			.setResultTransformer(Transformers.aliasToBean(AppointmentConfirmationDto.class)).uniqueResult();
			return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, appointmentConfirmationDto);

		} catch (HibernateException he) {
			logger.error("getAppointmentDataByRazorPayOrderId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAppointmentDataByRazorPayOrderId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getPayementDetailsByMobileNo(String mobileNo) throws ApplicationException {
		try {
			RazorpayPaymentReqDto dto=(RazorpayPaymentReqDto)sf.getCurrentSession().createSQLQuery("select mrold.razorpay_order_det_id as \"razorpayOrderDetId\" ,"
					+ "mrold.is_used as \"isUsed\", mrold.mobile_no as \"mobileNo\","
					+ " mrold.amount as \"razorAmount\","
					+ " mrold.paid as \"paid\","
					+ " mrold.short_url as \"shortUrl\","
					+ "  mrold.unique_id as \"uniqueId\", "
					+ " to_char(mrold.created_date ,'YYYY-MM-DD HH24:mi:ss') as  \"createdDate\"  "
					+ " from m_razorpay_order_log_details mrold where mrold.mobile_no ='"+mobileNo+"' and mrold.status = 'A'  order by 1 desc limit 1 ")
			.setResultTransformer(Transformers.aliasToBean(RazorpayPaymentReqDto.class)).uniqueResult();
			
			return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, dto);

		} catch (HibernateException he) {
			logger.error("getPayementDetailsByMobileNo failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPayementDetailsByMobileNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	public Response savePaymentLinkLog(RazorpayPaymentReqDto reqDto) throws ApplicationException {
		try {
			RazorPayOrderLogDetail payOrderLogDetail=new RazorPayOrderLogDetail();
			payOrderLogDetail.setUniqueId(reqDto.getUniqueId());
			payOrderLogDetail.setMobileNo(reqDto.getMobileNo());
			payOrderLogDetail.setIsUsed(false);
			payOrderLogDetail.setOrganizationId(reqDto.getOrganizationId());
			payOrderLogDetail.setUnitId(reqDto.getUnitId());
			payOrderLogDetail.setStatus('A');
			payOrderLogDetail.setAmount(reqDto.getRazorAmount());
			payOrderLogDetail.setShortUrl(reqDto.getShortUrl());
			payOrderLogDetail.setCreatedDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()));
			sf.getCurrentSession().save(payOrderLogDetail);
			return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("savePaymentLinkLog failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("savePaymentLinkLog failed", e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	public Response updatePaymentLinkLog(String razorpayPaymentLinkStatus, String razorpayPaymentLinkId)
			throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			Integer count = session.createSQLQuery("UPDATE m_razorpay_order_log_details SET paid = :razorpayPaymentLinkStatus WHERE unique_id = :razorpayPaymentLinkId")
					.setParameter("razorpayPaymentLinkStatus", razorpayPaymentLinkStatus)
					.setParameter("razorpayPaymentLinkId", razorpayPaymentLinkId)
					.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		}catch (HibernateException he) {
			logger.error("updatePaymentLinkLog failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePaymentLinkLog failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOrderLogDetailsByOrderId(String orderId) throws ApplicationException {
		try {
			RazorPayOrderLogDetail detail = (RazorPayOrderLogDetail) sf.getCurrentSession()
				.createSQLQuery("select unit_id as \"unitId\", organization_id as \"organizationId\", amount as \"amount\" "
					+ "from m_razorpay_order_log_details where order_id = :orderId")
				.setParameter("orderId", orderId)
				.setResultTransformer(Transformers.aliasToBean(RazorPayOrderLogDetail.class)).uniqueResult();
			return new Response<>(SUCCESS, null, COMMON_SUCCESS, null, detail);
		} catch (HibernateException he) {
			logger.error("getOrderLogDetailsByOrderId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getOrderLogDetailsByOrderId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
