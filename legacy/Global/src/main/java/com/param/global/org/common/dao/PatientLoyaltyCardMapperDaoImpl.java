package com.param.global.org.common.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.org.model.PatientLoyaltyCardMapper;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dto.LoyaltyCardResDto;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@SuppressWarnings({"rawtypes", "unchecked"})
@Repository

public class PatientLoyaltyCardMapperDaoImpl  extends GenericDao<PatientLoyaltyCardMapper, Integer> implements IPatientLoyaltyCardMapperDao,ICommonConstants {

	final static Logger logger = Logger.getLogger(PatientLoyaltyCardMapperDaoImpl.class);

	public PatientLoyaltyCardMapperDaoImpl() {
		super(PatientLoyaltyCardMapper.class);
	}

	@Override
	public Response savePatientLoyaltiCardMapper(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			PatientLoyaltyCardMapper master = new PatientLoyaltyCardMapper();
			master.setLoyaltyCardId(dto.getLoyaltyCardId());
			master.setCardConfigurationId(dto.getCardConfigurationId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganisationId(dto.getOrganisationId());
			master.setPatientId(dto.getPatientId());
			master.setStatus(ACTIVE);
			master.setIsDeleted('N');
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setCardAmount(0);
			master.setIsVerified('N');
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	@Override
	public Response getAvailableActiveDepositWithCardNumber(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException {
		try{
			LoyaltyCardResDto loyaltyCardResDto = (LoyaltyCardResDto) sessionFactory.getCurrentSession().getNamedQuery("GET_CARD_DETAILS_BY_MPPER")
					.setInteger("patientLoyaltyCardMpprId", dto.getPatientLoyaltyCardMpprId())
					.setInteger("orgId", dto.getOrganisationId())
					.setInteger("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(LoyaltyCardResDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, loyaltyCardResDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updatePatientByLocaltiCard(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PATIENT_BY_LOYALTI_CARD")
				.setInteger("patientId", dto.getPatientId())
				.setInteger("loyaltyCardId", dto.getLoyaltyCardId())
			    .executeUpdate();
			if(result>0){
			  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updatePatientPatientCagtegory(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PATIENT_CATEGORY_BY_PATIENT_ID")
				.setInteger("patientId", dto.getPatientId())
				.setInteger("patientCategoryId", dto.getPatientCategoryId())
			    .executeUpdate();
			if(result>0){
			  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getLoyaltiCardDetailsByPatient(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			if(dto.getPatientId()!=null)
			{
				List<PatientLoyaltyCardMapperDto> list =sessionFactory.getCurrentSession().getNamedQuery("GET_LOYALTI_CARD_DETAILS_BY_PATIENT_ID")
						.setInteger("patientId", dto.getPatientId())
						.setInteger("orgId", dto.getOrganisationId())
						.setInteger("unitId", dto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(PatientLoyaltyCardMapperDto.class)).list();
					return new Response<>(SUCCESS, SUCCESS_CODE, null,list,null);	
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null,null,null);	
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getPatientDetailsByLoyaltiCardNo(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DETAILS_BY_LOYALTI_CARD_NO").getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append(" AND loyalityMstr.card_no=:cardNo ");
					
			List<PatientLoyaltyCardMapperDto> list =sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
				.setString("cardNo", dto.getCardNo())
				.setInteger("orgId", dto.getOrganisationId())
				.setInteger("unitId", dto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(PatientLoyaltyCardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null,list, null);			
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateOTP(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().createSQLQuery(" UPDATE public.t_patient_loyalty_card_mppr "
					+ " set otp_expired_time='"+GlobalCommonDateUtils.getDate(dto.getOtpExpiredTime(), "dd-M-yyyy HH:mm:ss")+"'"
					+ ", otp="+dto.getOtp() 
					+" Where patient_loyalty_card_mppr_id="+dto.getPatientLoyaltyCardMpprId()
					+" AND organisation_id="+dto.getOrganisationId()
					+" AND unit_id="+dto.getUnitId())
					.executeUpdate();
			if(result>0){
			  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	@Override		
	public Response updateLoyaltyCardAmount(PatientLoyaltyCardMapperDto dto) throws ApplicationException
	{
		try {
			int result = sessionFactory.getCurrentSession()
					.createSQLQuery(" update " + "	public.t_patient_loyalty_card_mppr " + "set "
							+ "	card_amount = coalesce(card_amount,0)+:amount ," + "	updated_by = :credatedBy, "
							+ "	updated_date = :updatedDate " + "where " + "	patient_loyalty_card_mppr_id = :id ")
					.setDouble("amount", dto.getCardAmount()).setInteger("id", dto.getPatientLoyaltyCardMpprId())
					.setInteger("credatedBy", dto.getCreatedBy())
					.setDate("updatedDate", GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"))
					.executeUpdate();
			if (result > 0) {
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			} else {
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@Override
	public Response updateLoyaltyCardAmountByCard(PatientLoyaltyCardMapperDto dto) throws ApplicationException
	{
		try {
			int result = sessionFactory.getCurrentSession()
						.createSQLQuery("update "
								+ "	public.t_patient_loyalty_card_mppr "
								+ "set "
								+ "	card_amount = coalesce(card_amount, 0)+:amount , "
								+ "	updated_by = :credatedBy, "
								+ "	updated_date = :updatedDate "
								+ "where "
								+ "	loyalty_card_id = ( "
								+ "		select loyalty_card.loyalty_card_id "
								+ "	from "
								+ "		public.m_loyalty_card loyalty_card "
								+ "	where "
								+ "		loyalty_card.card_no = :cardNo "
								+ "		and loyalty_card.is_deleted = 'N' ) "
								+ "		and is_deleted = 'N'" )
					.setDouble("amount", -dto.getCardAmount())
					.setString("cardNo", dto.getCardNo())
					.setInteger("credatedBy", dto.getCreatedBy())
					.setDate("updatedDate", GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"))
					.executeUpdate();
			if (result > 0) {
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			} else {
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getLoyaltyCardPatientList(int orgId, int unitId) throws ApplicationException 
	{
		try {
		List<PatientLoyaltyCardMapperDto> list =sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DETAILS_BY_LOYALTI_CARD_NO")
				.setInteger("orgId", orgId)
				.setInteger("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(PatientLoyaltyCardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null,list, null);		
	}catch(Exception e){
		
		throw e;
	}
	}

	@Override
	public Response getLoyaltyCardConfiguration(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			LoyaltyCardResDto loyaltyCardResDto = (LoyaltyCardResDto) sessionFactory.getCurrentSession().getNamedQuery("GET_CARD_CONFIG_BY_UNIT")
					.setInteger("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(LoyaltyCardResDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, loyaltyCardResDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getLoyaltiDetailsByMapprId(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			List<PatientLoyaltyCardMapperDto> list =sessionFactory.getCurrentSession().getNamedQuery("GET_LOYALTI_DETAILS_BY_MAPPR_ID")
				.setInteger("loyaltiMpprId", dto.getPatientLoyaltyCardMpprId())
				.setString("cardNo", dto.getCardNo())				
				.setResultTransformer(Transformers.aliasToBean(PatientLoyaltyCardMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null,list, null);			
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateOTPCount(PatientLoyaltyCardMapperDto dto, int i) throws ApplicationException {
		try{
			int result=0;
			if(i==1){
				 result = sessionFactory.getCurrentSession().createSQLQuery(" UPDATE public.t_patient_loyalty_card_mppr "
							+ " set otp_count = 0,"
							+ " card_block_date='"+GlobalCommonDateUtils.getDate(dto.getCardBlockDate(), "dd-M-yyyy HH:mm:ss")+"'"
							+" Where patient_loyalty_card_mppr_id="+dto.getPatientLoyaltyCardMpprId())
							.executeUpdate();
				
			}else{
				 result = sessionFactory.getCurrentSession().createSQLQuery(" UPDATE public.t_patient_loyalty_card_mppr "
							+ " set otp_count=coalesce(otp_count, 0)+1,"
							+ " card_block_date='"+GlobalCommonDateUtils.getDate(dto.getCardBlockDate(), "dd-M-yyyy HH:mm:ss")+"'"
							+" Where patient_loyalty_card_mppr_id="+dto.getPatientLoyaltyCardMpprId())
							.executeUpdate();
			}
			if(result>0){
			  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response resetOTPDetails(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().createSQLQuery(" UPDATE public.t_patient_loyalty_card_mppr "
					+ " set is_verified='N'"
					+" Where patient_loyalty_card_mppr_id="+dto.getPatientLoyaltyCardMpprId()
					+" AND organisation_id="+dto.getOrganisationId()
					+" AND unit_id="+dto.getUnitId())
					.executeUpdate();
			if(result>0){
			  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response addUpdatedLoyaltyCardAmount(PatientLoyaltyCardMapperDto loyaltyCarddto) throws ApplicationException {
		try {
			int result = sessionFactory.getCurrentSession()
						.createSQLQuery("update "
								+ "	public.t_patient_loyalty_card_mppr "
								+ "set "
								+ "	card_amount = :amount , "
								+ "	updated_by = :credatedBy, "
								+ "	updated_date = :updatedDate "
								+ "where "
								+ "	loyalty_card_id = ( "
								+ "		select loyalty_card.loyalty_card_id "
								+ "	from "
								+ "		public.m_loyalty_card loyalty_card "
								+ "	where "
								+ "		loyalty_card.card_no = :cardNo "
								+ "     and loyalty_card.unit_id =:unitId  and "
								+ "     loyalty_card.organisation_id =:orgId  "
								+ "		and loyalty_card.is_deleted = 'N' ) "
								+ "		and is_deleted = 'N'" )
					.setDouble("amount", loyaltyCarddto.getCardAmount())
					.setString("cardNo", loyaltyCarddto.getCardNo())
					.setInteger("credatedBy", loyaltyCarddto.getCreatedBy())
					.setInteger("orgId", loyaltyCarddto.getOrganisationId())
					.setInteger("unitId", loyaltyCarddto.getUnitId())
					.setDate("updatedDate", GlobalCommonDateUtils.getDate(loyaltyCarddto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"))
					.executeUpdate();
			if (result > 0) {
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			} else {
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		} catch (Exception e) {
			
			throw e;
		}
		
	}

	@Override
	public Response getUnitName(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try
		{
			PatientLoyaltyCardMapperDto containerMaster = (PatientLoyaltyCardMapperDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_NAME_BY_ORG_ID_OR_UNIT_ID").setInteger("orgId", dto.getOrganisationId())
					.setInteger("unitId", dto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(PatientLoyaltyCardMapperDto.class))					
					.uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, containerMaster);

		} catch (Exception e)
		{
			logger.error("getUnitName failed", e);
			return new Response<>(ERROR, ERROR_CODE, null, null, null);
		}

	}

	@Override
	public Response unBlockOTPDetails(PatientLoyaltyCardMapperDto dto) throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().createSQLQuery(" UPDATE public.t_patient_loyalty_card_mppr "
					+ " set otp_count=0, is_verified='N'"
					+" Where patient_loyalty_card_mppr_id="+dto.getPatientLoyaltyCardMpprId()
					+" AND organisation_id="+dto.getOrganisationId()
					+" AND unit_id="+dto.getUnitId())
					.executeUpdate();
			if(result>0){
			  return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			else{
				return new Response<>(ERROR, ERROR_CODE, null, null, null);
			}
		}catch(Exception e){
			
			throw e;
		}
		
	}
}
