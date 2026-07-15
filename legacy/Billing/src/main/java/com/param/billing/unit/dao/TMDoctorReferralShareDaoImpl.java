package com.param.billing.unit.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.transaction.dto.DoctorReferralShareMapperDto;
import com.param.billing.global.transaction.dto.MDoctorReferralShareDto;
import com.param.billing.global.transaction.model.DoctorReferalShareCountMapper;
import com.param.billing.global.transaction.model.DoctorReferralShareMapper;
import com.param.billing.global.transaction.model.MDoctorReferralShare;
import com.param.billing.unit.dto.MDoctorReferralShareReqDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;


@Repository
@SuppressWarnings({ "rawtypes","unchecked" })
public class TMDoctorReferralShareDaoImpl extends GenericDao<MDoctorReferralShare, Integer> implements IMDoctorReferralShareDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(TMDoctorReferralShareDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public TMDoctorReferralShareDaoImpl() {
		super(MDoctorReferralShare.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveDoctorReferralShareDao(MDoctorReferralShareReqDto masterDto)
			throws ServiceException {
		try{
			MDoctorReferralShare mDoctorReferralShare = new MDoctorReferralShare();
			mDoctorReferralShare.setCreatedBy(masterDto.getCreatedBy());
			mDoctorReferralShare.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()).getTime());
			mDoctorReferralShare.setOrgUnitId(masterDto.getUnitId());
			mDoctorReferralShare.setOrgId(masterDto.getOrgId());
			mDoctorReferralShare.setUpdatedBy(masterDto.getUpdatedBy());
			mDoctorReferralShare.setEffectiveDate(GlobalCommonDateUtils.getLongDateFromStrigDate(masterDto.getEffectiveDate()));
			mDoctorReferralShare.setVisitTypeId(masterDto.getVisitTypeId());
			mDoctorReferralShare.setDocReferralId(masterDto.getDocReferralId());
			mDoctorReferralShare.setDuration(masterDto.getDuration());
//			mDoctorReferralShare.setNoOfCases(masterDto.getNoOfCases());
//			mDoctorReferralShare.setRevenue(masterDto.getRevenue());
//			mDoctorReferralShare.setShare(masterDto.getShare());
			mDoctorReferralShare.setReferraledDocId(masterDto.getReferraledDocId());
			mDoctorReferralShare.setStatus(ACTIVE);
			mDoctorReferralShare = save(mDoctorReferralShare);
			return new Response<>(SUCCESS, SUCCESS_CODE, DOCTOR_REFERRAL_SHARE, null, mDoctorReferralShare);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
		
	}

	@Override
	public Response saveDoctorReferralMapper(DoctorReferralShareMapper mstMapper) throws ServiceException {
		try{
			sessionFactory.getCurrentSession().save(mstMapper);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDoctorReferralDetails() throws ServiceException {
		try{
			@SuppressWarnings("unchecked")
			List<MDoctorReferralShareReqDto> mDoctorReferralShareDtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_DETAILS")
//					.setInteger("orgId", orgId)
//					.setInteger("unitId",unitId)
					.setParameter("currentDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "yyyy-M-dd"))
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).list();
					return new Response<>(SUCCESS,SUCCESS_CODE,null,mDoctorReferralShareDtoList,null);
			
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Response getReferralDetails(Integer referralDocId,Integer visitTypeId,Integer orgId,Integer unitId) throws ServiceException {
		try{
			LocalDate date = LocalDate.now().minusDays(1);
			Date todayDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			List<MDoctorReferralShareDto> mDoctorReferralShareDtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_REFERRAL_DETAILS")
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(todayDate, "dd-MM-yyyy"))
					.setParameter("referralDocId",referralDocId !=null ? referralDocId : 0)
					.setParameter("visitTypeId",visitTypeId)
					.setParameter("orgId",orgId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareDto.class)).list();
					return new Response<>(SUCCESS,SUCCESS_CODE,null,mDoctorReferralShareDtoList,null);
			
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response saveReferralCount(DoctorReferalShareCountMapper master) throws ServiceException {
		try{
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS , SUCCESS_CODE, null, null, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateReferralByReferralDocId(MDoctorReferralShareReqDto dto) throws ServiceException {
		try{
			
			LocalDate date = LocalDate.now().minusDays(1);
			Date todayDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			Integer cnt=sessionFactory.getCurrentSession().createSQLQuery("UPDATE public.t_referal_details refDtls "
					+ " set status='I'"
					+ " where referral_id=:referralDocId AND to_char(created_date,'DD-MM-YYYY')=:currentDate")
					.setParameter("referralDocId", dto.getReferraledDocId())
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(todayDate, "dd-MM-yyyy"))
					.executeUpdate();	
			if(cnt>0){
				return new Response<>(SUCCESS , SUCCESS_CODE, null, null, null);
			}
			
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateReferralCountMapper(MDoctorReferralShareReqDto dto) throws ServiceException {
		try{
//			LocalDate date = LocalDate.now().minusDays(1);
//			Date todayDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			Integer cnt=sessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_doctor_referal_share_count_mapper refMppr "
					+ " set refMppr.is_read='T'"
					+ " where refMppr.referraled_doc_id=:referralDocId")
					.setParameter("referralDocId", dto.getReferraledDocId())
					.executeUpdate();	
			if(cnt>0){
				return new Response<>(SUCCESS , SUCCESS_CODE, null, null, null);
			}
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getNoOfCount(Integer referralDocId) throws ServiceException {
		try{
			
			MDoctorReferralShareReqDto dtoList=(MDoctorReferralShareReqDto)sessionFactory.getCurrentSession().getNamedQuery("GET_REFERAL_COUNT_DETAILS")
					.setParameter("referralDocId", referralDocId)
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).uniqueResult();
			return new Response<>(SUCCESS , SUCCESS_CODE, null, null, dtoList);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getShareDetails(Integer referralDocId) throws ServiceException {
		try{
		
			List<MDoctorReferralShareReqDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_SHARE_DETAILS")
					.setParameter("referralDocId", referralDocId)
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).list();
			return new Response<>(SUCCESS , SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getAllReferralCountDetails(Integer referralDocId,Integer visitTypeId,Integer orgId,Integer unitId) throws ServiceException {
		try{		
			List<MDoctorReferralShareReqDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_ALL_REFERRAL_COUNT_DETAILS")
					.setParameter("referralDocId", referralDocId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).list();
			return new Response<>(SUCCESS , SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDoctorReferralDetailsByDoctorReferralId(Integer referralDocId,Integer visitTypeId, Integer orgId, Integer unitId) {
		try{		
			
			List<MDoctorReferralShareReqDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_DETAILS_BY_DOCTOR_REFERRAL_ID")
					.setParameter("referralDocId", referralDocId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).list();
			return new Response<>(SUCCESS , SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getEncounterAndAdmissionIds(Integer referralDocId, Date startDate,Integer visitTypeId, Integer orgId, Integer unitId) throws ServiceException {
		try{				
			LocalDate date = LocalDate.now().minusDays(1);
			Date todayDate = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			
			LocalDate todate =GlobalCommonDateUtils.getLocalDate(startDate).minusDays(1);
			Date firstDate = Date.from(todate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			List<MDoctorReferralShareReqDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_ENCOUNTER_AND_ADMISSION_IDS")
					.setParameter("referralDocId", referralDocId)
					.setParameter("fromDate", firstDate)
					.setParameter("toDate", todayDate)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).list();
			return new Response<>(SUCCESS , SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateReferralCountDetails(Integer referraledDocId,Integer visitTypeId, Integer orgId, Integer unitId) throws ServiceException {
		try{
			Date currentDate=TimeZoneComponent.getDateByzone(unitId);
			Integer count=sessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_doctor_referal_share_count_mapper"
					+ " SET is_read='T' "
					+ "where referraled_doc_id =:referraledDocId "
							+ "AND visit_type_id =:visitTypeId AND org_id =:orgId AND org_unit_id =:unitId "
							+ " and  to_char(created_date,'YYYY-MM-DD') <=:currentDate ")
					.setParameter("referraledDocId", referraledDocId)
					.setParameter("visitTypeId", visitTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(currentDate, "yyyy-MM-dd"))
					.executeUpdate();
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDoctorReferralShareDetails(MDoctorReferralShareReqDto mDoctorReferralShareReqDto) {
		try{
			List<MDoctorReferralShareReqDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_SHARE_DETAILS")
//					.setInteger("referralDocId", mDoctorReferralShareReqDto.getReferraledDocId())
					.setParameter("orgId", mDoctorReferralShareReqDto.getOrgId())
//					.setInteger("unitId", mDoctorReferralShareReqDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).list();
			return new Response<>(SUCCESS , SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDoctorReferralShareDetailsByRefDocId(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)
			throws ServiceException {
		try{
			List<MDoctorReferralShareReqDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_DOCTOR_REFERRAL_SHARE_DETAILS_BY_DOC_REF_ID")
					.setParameter("referralDocId", mDoctorReferralShareReqDto.getDocReferralId())
					.setParameter("orgId", mDoctorReferralShareReqDto.getOrgId())
					.setParameter("unitId", mDoctorReferralShareReqDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).list();
			return new Response<>(SUCCESS , SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateStatusDoctorReferralShare(DoctorReferralShareMapperDto dto)
			throws ServiceException {
		try{
			Integer count =sessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_doctor_referral_share_mapper set status=:status"
					+ " where doctor_referal_share_mapper_id=:mapperId")
					.setParameter("mapperId", dto.getDoctorReferalShareMapperId())
					.setParameter("status", dto.getStatus())
					.executeUpdate();
			if(count>0)
				return new Response<>(SUCCESS , SUCCESS_CODE, null, null, null);
			else
				return new Response<>(ERROR , ERROR_CODE, null, null, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response checkALreadyExistOrNot(MDoctorReferralShareReqDto mDoctorReferralShareReqDto)
			throws ServiceException {
		try{

			String arrDate[]=mDoctorReferralShareReqDto.getEffectiveDate().split("-");
			String arrYear[]=arrDate[2].split(" ");
			String effectiveDateString=arrYear[0]+"-"+arrDate[1]+"-"+arrDate[0];
			//System.out.println("date in string="+effectiveDateString);
			List<MDoctorReferralShareReqDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("CHECK_DOCTOR_REFERRAL_ALREADY_EXIST_OR_NOT")
					.setParameter("referralDocId", mDoctorReferralShareReqDto.getReferraledDocId())
					.setParameter("referralId", mDoctorReferralShareReqDto.getDocReferralId())
					.setParameter("visitType",mDoctorReferralShareReqDto.getVisitTypeId())
					.setParameter("duration", mDoctorReferralShareReqDto.getDuration())
					.setParameter("effectiveDate",effectiveDateString)
					.setParameter("orgId", mDoctorReferralShareReqDto.getOrgId())
					.setParameter("unitId", mDoctorReferralShareReqDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MDoctorReferralShareReqDto.class)).list();
			return new Response<>(SUCCESS , SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	


	/*@Override
	public Response saveDoctorReferralShareDao(MDoctorReferralShareDto masterDto)
			throws ApplicationException {
		try{
			MDoctorReferralShare mDoctorReferralShare = new MDoctorReferralShare();
			mDoctorReferralShare.setCreatedBy(masterDto.getCreatedBy());
			mDoctorReferralShare.setCreatedDate(TimeZoneComponent.getDateByzone(unitId).getTime());
			mDoctorReferralShare.setOrgUnitId(masterDto.getOrgUnitId());
			mDoctorReferralShare.setOrgId(masterDto.getOrgId());
			mDoctorReferralShare.setUpdatedBy(masterDto.getUpdatedBy());
			mDoctorReferralShare.setEffectiveDate(masterDto.getEffectiveDate());
			mDoctorReferralShare.setVisitTypeId(masterDto.getVisitTypeId());
			mDoctorReferralShare.setDocReferralId(masterDto.getDocReferralId());
			mDoctorReferralShare.setDuration(masterDto.getDuration());
			mDoctorReferralShare.setNoOfCases(masterDto.getNoOfCases());
			mDoctorReferralShare.setRevenue(masterDto.getRevenue());
			mDoctorReferralShare.setShare(masterDto.getShare());
			mDoctorReferralShare.setReferraledDocId(masterDto.getReferraledDocId());
			mDoctorReferralShare.setStatus(ACTIVE);
			mDoctorReferralShare = save(mDoctorReferralShare);
			return new Response<>(SUCCESS, SUCCESS_CODE, DOCTOR_REFERRAL_SHARE, null, mDoctorReferralShare);
		}catch(Exception e){
			logger.error("Exection", e);
			
			throw new ServiceException(e.getMessage());
		}
	}*/



	/*@Override
	public Response getDoctorGradeList(Integer orgId,Integer orgUnitId) throws ApplicationException
	{
		List<CommonDto>  listDoctorGrade  = null; 
		try
		{
			listDoctorGrade = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	docgrade.doctor_grade_id AS \"id\", "
							+"	docgrade.doc_grade_name AS \"name\" "
							+"FROM "
							+"	doctor.m_doctor_grade docgrade "
							+"WHERE "
							+"	docgrade.status = 'A' "
							+"	AND docgrade.org_id = :orgId "
							+"	AND docgrade.org_unit_id = :orgUnitId ")
					.setInteger("orgId", orgId)
					.setInteger("orgUnitId", orgUnitId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_CODE, listDoctorGrade, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500, null, null);
		}
	}*/

	
}
