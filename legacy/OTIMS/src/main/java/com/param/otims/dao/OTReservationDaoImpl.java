package com.param.otims.dao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureReqMasterDto;
import com.param.global.procedure.VO.AssetVO;
import com.param.otims.model.OTReservation;
import com.param.otims.model.ProcedureOrderMaster;
import com.param.otims.model.ProcedureReqMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class OTReservationDaoImpl  extends GenericDao<OTReservation, Integer> implements IOTReservationDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(OTReservationDaoImpl.class);

	public OTReservationDaoImpl() {
		super( OTReservation.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveOTReservation(OTReservationDto oTReservationdto) throws ApplicationException {
		try{
			OTReservation master = new OTReservation();
			master.setPatientId(oTReservationdto.getPatientId());
			master.setOtReservationNo(oTReservationdto.getOtReservationNo());
			master.setAdmissionId(oTReservationdto.getAdmissionId()==null?null:oTReservationdto.getAdmissionId());
			master.setEncounterId(oTReservationdto.getEncounterId()==null?null:oTReservationdto.getEncounterId());
			master.setVisitTypeId(oTReservationdto.getVisitTypeId()==null?null:oTReservationdto.getVisitTypeId());
			master.setOrganizationId(oTReservationdto.getOrganizationId());
			master.setUnitId(oTReservationdto.getUnitId());
			master.setCreatedBy(oTReservationdto.getCreatedBy());
			master.setCreatedDate(oTReservationdto.getCreatedDate() != null && !oTReservationdto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(oTReservationdto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(oTReservationdto.getUnitId()));
			master.setUpdatedBy(oTReservationdto.getUpdatedBy());
			master.setProReqId(oTReservationdto.getProReqId());
			master.setScheduleId(oTReservationdto.getScheduleId());
			master.setReservationStatusId(oTReservationdto.getReservationStatusId()); // 1 for reserve and 2 for cancellation
			master.setStatus('A');
			master = save(master);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BillingException(e.getMessage());
        }
	}

	@Override
	public Response getEquipmentTyoeAndAssetItem(int orgId, int unitId) throws ApplicationException {
		try
        {
			List<AssetVO> assetList = sessionFactory.getCurrentSession().createSQLQuery(
					"select asse.asset_type_id as \"assetTypeId\" ,asse.id as \"assetCategoryId\"  "
							+ " from m_asset_category asse  " + 
							"	where asse.asset_type_id =2 and asse.is_active = true and is_deleted = false " + 
							"	and asse.organization_id = "+orgId
						  + " and asse.unit_id ="+unitId)
				.setResultTransformer(Transformers.aliasToBean(AssetVO.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, assetList, null);

        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BillingException(e.getMessage());
        }
	}

	@Override
	public Response getAssetAndBatch(AssetVO asstItem) throws ApplicationException {
		try
        {
			List<AssetVO> assetList = sessionFactory.getCurrentSession().createSQLQuery(
					 "select  bth.id  as \"batchId\", " 
							 	+ "	concat(itm.name ,' || ',bth.batch_no) as \"itemName\","
							 	+ "	itm.name  as \"equipmentName\","
								+ "	itm.id as \"itemId\" " 
								+ "	from inventory.t_batch bth "
								+ "	inner join public.m_item itm on bth. item_id = itm.id "
								+ "	inner join public.m_product_category produc on itm.product_category_id = produc.id "
								+ "	inner join public.m_asset_category assetcateg on produc.asset_category_id = assetcateg.id "
								+ "	inner join public.m_asset_type assettype on	assetcateg.asset_type_id = assettype.id"
								+ "	where produc.is_deleted =false " 
								+ "	and assetcateg.is_deleted =false "
								+ "	and assetcateg.id = "+ asstItem.getAssetCategoryId()
								+ "	and assettype.is_deleted =false "
								+ "	and assettype.id = 2 "
								+ "	order by bth.id asc ")
				.setResultTransformer(Transformers.aliasToBean(AssetVO.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, assetList, null);

        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new BillingException(e.getMessage());
        }
	}

	@Override
	public Response saveProcedureReq(ProcedureReqMasterDto procedureReqMasterDto) throws ApplicationException {
		try{
			ProcedureReqMaster master = new ProcedureReqMaster();
			master.setPrimaryProcedureId(procedureReqMasterDto.getPrimaryProcedureId());
			master.setPatientId(procedureReqMasterDto.getPatientId());
			master.setAdmissionId(procedureReqMasterDto.getAdmissionId()==null?null:procedureReqMasterDto.getAdmissionId());
			master.setEncounterId(procedureReqMasterDto.getEncounterId()==null?null:procedureReqMasterDto.getEncounterId());
			master.setVisitTypeId(procedureReqMasterDto.getVisitTypeId()==null?null:procedureReqMasterDto.getVisitTypeId());
			master.setPreferedDateTime(procedureReqMasterDto.getPreferedDateTime() != null && !procedureReqMasterDto.getPreferedDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getPreferedDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setRequestDate(procedureReqMasterDto.getRequestDate() != null && !procedureReqMasterDto.getRequestDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getRequestDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setRequestBy(procedureReqMasterDto.getRequestBy());
			master.setProcedureStatusId(procedureReqMasterDto.getProcedureStatusId());
			master.setOtBillCompletionDateTime(procedureReqMasterDto.getOtBillCompletionDateTime() != null && !procedureReqMasterDto.getOtBillCompletionDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getOtBillCompletionDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setOtBillCompletionBy(procedureReqMasterDto.getOtCompletionBy());
			master.setOtBillCompletionDateTime(procedureReqMasterDto.getOtBillCompletionDateTime() != null && !procedureReqMasterDto.getOtBillCompletionDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getOtBillCompletionDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setOtBillCompletionBy(procedureReqMasterDto.getOtBillCompletionBy());
			master.setOrgId(procedureReqMasterDto.getOrgId());
			master.setUnitId(procedureReqMasterDto.getUnitId());
			master.setCreatedBy(procedureReqMasterDto.getCreatedBy());
			master.setCreatedDate(procedureReqMasterDto.getCreatedDate() != null && !procedureReqMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setUpdatedBy(procedureReqMasterDto.getUpdatedBy());
			master.setUpdatedDate(procedureReqMasterDto.getUpdatedDate() != null && !procedureReqMasterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureReqMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureReqMasterDto.getUnitId()));
			master.setEmergency(procedureReqMasterDto.getEmergency() != null?procedureReqMasterDto.getEmergency():false);
			if(procedureReqMasterDto.getProReqId()!=null) {
				master.setProReqId(procedureReqMasterDto.getProReqId());
				 sessionFactory.getCurrentSession().update(master);
			}else {
				sessionFactory.getCurrentSession().save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response saveProcedureOrder(ProcedureOrderMasterDto procedureOrderMasterDto) throws ApplicationException {
		try{
			if(procedureOrderMasterDto.getProcedureOrderId() !=null) {
			
				Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_master set status='I'  where procedure_order_id='"+procedureOrderMasterDto.getProcedureOrderId()+"' ")
						.executeUpdate();
			}
			LocalTime preferedTime1 = LocalTime.parse( procedureOrderMasterDto.getPreferedTime());
			LocalDate today = LocalDate.now();
			Instant preferedTime = preferedTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			String preDate = procedureOrderMasterDto.getPreferedDateTime()+" 00:00:00";
			procedureOrderMasterDto.setPreferedDateTime(preDate);
			ProcedureOrderMaster master = new ProcedureOrderMaster();
			master.setServiceId(procedureOrderMasterDto.getServiceId());
			master.setPreferedDateTime(procedureOrderMasterDto.getPreferedDateTime() != null && !procedureOrderMasterDto.getPreferedDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getPreferedDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			master.setIsPrimarySecondary(procedureOrderMasterDto.getIsPrimarySecondary());
			master.setAmount(procedureOrderMasterDto.getAmount()==null?0:procedureOrderMasterDto.getAmount());
			master.setUrgency(procedureOrderMasterDto.getUrgency());
			master.setRequestReason(procedureOrderMasterDto.getRequestReason());
			master.setStatus('A');
			master.setIsIcuRequired(procedureOrderMasterDto.getIsIcuRequired());
			master.setIsBloodComponentRequired(procedureOrderMasterDto.getIsBloodComponentRequired());
			master.setIsPacRequired(procedureOrderMasterDto.getIsPacRequired());
			master.setOrgId(procedureOrderMasterDto.getOrgId());
			master.setUnitId(procedureOrderMasterDto.getUnitId());
			master.setCreatedBy(procedureOrderMasterDto.getCreatedBy());
			master.setCreatedDate(procedureOrderMasterDto.getCreatedDate() != null && !procedureOrderMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			master.setUpdatedBy(procedureOrderMasterDto.getUpdatedBy());
			master.setUpdatedDate(procedureOrderMasterDto.getUpdatedDate() != null && !procedureOrderMasterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			master.setProReqId(procedureOrderMasterDto.getProReqId());
			master.setExportedToOtims(false);
			master.setEmergency(false);
			master.setPreferedTime(preferedTime);
			master.setProcedureSequence(procedureOrderMasterDto.getProcedureSequence()!=null ? procedureOrderMasterDto.getProcedureSequence(): null);
			sessionFactory.getCurrentSession().save(master);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response updateProReqMasterStatus(Integer proReqId) throws ApplicationException {
		try{	
			
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_req_mst  set procedure_status_id =3 "
					+ " where pro_req_id="+proReqId+" ")
					.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		}catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response searchOTReservation(ProcedureOrderMasterDto procedureOrderMasterDto) throws ApplicationException {
		try
		{
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_OT_RESERVATION_LIST_BY_CRITERIA").getQueryString());
			if(procedureOrderMasterDto.getServiceId() != null ) {
				query.append(" and proOrdrMast.service_id =:serviceId ");
			}
			
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("unitId", procedureOrderMasterDto.getUnitId())
					.setParameter("organizationId", procedureOrderMasterDto.getOrganizationId())
					.setParameter("fromDate", procedureOrderMasterDto.getFromDate())
					.setParameter("toDate", procedureOrderMasterDto.getToDate());

			if(procedureOrderMasterDto.getServiceId() != null ) {
				createQuery.setParameter("serviceId", procedureOrderMasterDto.getServiceId());
			}

			List<OTReservationDto> list = createQuery.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response searchReservedProcedureName(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try
		{
			String query=sessionFactory.getCurrentSession().getNamedQuery("SEARCH_RESERVED_PROCEDURES").getQueryString();
			List<ServiceSearchResDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", procedureOrderMasterDto.getUnitId())
					.setParameter("organizationId", procedureOrderMasterDto.getOrganizationId())
					.setParameter("searchKeyword", "%"+ procedureOrderMasterDto.getServiceName().trim().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response cancelProcedureOrder(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_master set status = 'I' "
					+ " where pro_req_id="+oTReservationdto.getProReqId()+" ")
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

		}catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response cancelOTReservation(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_ot_reservation set status = 'I' , reservation_status_id = 2, reason_id =" +oTReservationdto.getReasonId()+
					",reason_note= '"+ oTReservationdto.getReasonNote()+"' "
					+ " where pro_req_id ="+oTReservationdto.getProReqId()+" ")
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

		}catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getOTReservationListByPatientId(OTReservationDto oTReservationdto) throws ApplicationException {
		try
		{
		    String query=sessionFactory.getCurrentSession().getNamedQuery("GET_RESERVED_PROCEDURES_BY_PATIENT_ID").getQueryString();
			List<OTReservationDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", oTReservationdto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getVisitTypeListByPatientId(OTReservationDto dto) throws ApplicationException {
		try
		{
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_VISIT_TYPE_LIST_BY_PATIENT_ID").getQueryString();
			List<OTReservationDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", dto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getOTReservationListByPatientIdForDeposite(OTReservationDto oTReservationdto)
			throws ApplicationException {
		try
		{
			Date date = TimeZoneComponent.getDateByzone(oTReservationdto.getUnitId());  
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		    String currentDate = formatter.format(date);  

		    String query=sessionFactory.getCurrentSession().getNamedQuery("GET_RESERVED_PROCEDURES_BY_PATIENT_ID_FOR_DEPOSITE").getQueryString();
			List<OTReservationDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", oTReservationdto.getPatientId())
					.setParameter("currentDate", currentDate)
					.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateOTReservation(OTReservationDto oTReservationdto) throws ApplicationException {
		try
		{
			OTReservation obj  = (OTReservation)findById(oTReservationdto.getOtReservationId());
			obj.setReservationStatusId(oTReservationdto.getReservationStatusId());
			obj.setCreatedDate(TimeZoneComponent.getDateByzone(oTReservationdto.getUnitId()!=null? oTReservationdto.getUnitId():null));
			update(obj);
			sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_ot_reservation set reservation_status_id = "+oTReservationdto.getReservationStatusId()+
					 " where ot_reservation_id ="+oTReservationdto.getOtReservationId()+" ")
					.executeUpdate();
			
		return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateAllProcedureOrderDetailsStatus(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try{	
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_master set status='I' , reason_id="+procedureOrderMasterDto.getReasonId()+" , reason_note= '"
					+procedureOrderMasterDto.getReasonNote()+"' where procedure_order_id='"+procedureOrderMasterDto.getProcedureOrderId()+"' ")
					.executeUpdate();
			if(count>0) {
				 return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			}else {
				 return new Response(SUCCESS, SUCCESS_CODE, null, null, 0);
			}

		}catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new BillingException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getOTReservationForOtCalender(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try
		{
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_OT_RESERVATION_LIST_BY_CRITERIA_FOR_CALENDER").getQueryString());
			
			if(procedureOrderMasterDto.getServiceId() != null ) {
				query.append(" and proOrdrMast.service_id =:serviceId ");
			}
			if(procedureOrderMasterDto.getReservationStatusId() != null ) {
				query.append(" and otreservat.reservation_status_id =:reservationStatusId ");
			}
			
			if(procedureOrderMasterDto.getDoctorId() != null ) {
				query.append(" and proStaffSed.user_id =userId ");
			}
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("unitId", procedureOrderMasterDto.getUnitId())
					.setParameter("organizationId", procedureOrderMasterDto.getOrganizationId())
					.setParameter("fromDate", procedureOrderMasterDto.getFromDate())
					.setParameter("toDate", procedureOrderMasterDto.getToDate());

			if(procedureOrderMasterDto.getServiceId() != null ) {
				createQuery.setParameter("serviceId", procedureOrderMasterDto.getServiceId());
			}
			if(procedureOrderMasterDto.getReservationStatusId() != null ) {
				createQuery.setParameter("reservationStatusId", procedureOrderMasterDto.getReservationStatusId());
			}

			if(procedureOrderMasterDto.getDoctorId() != null ) {
				createQuery.setParameter("userId", procedureOrderMasterDto.getDoctorId());

			}

			List<OTReservationDto> list = createQuery.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}

}
