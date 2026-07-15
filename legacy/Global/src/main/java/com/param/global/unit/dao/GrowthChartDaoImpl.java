package com.param.global.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CommonDto;
import com.param.global.model.GrowthChartMaster;
import com.param.global.model.TGrowthChart;
import com.param.global.unit.dto.GrowthChartMasterDto;
import com.param.global.unit.dto.GrowthChartReqDto;
import com.param.global.unit.dto.TGrowthChartDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GrowthChartDaoImpl extends GenericDao<GrowthChartMaster, Integer> implements IGrowthChartDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(GrowthChartDaoImpl.class);

	public GrowthChartDaoImpl() {
		super(GrowthChartMaster.class);
	}
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveGrowthMaster(GrowthChartMasterDto dto) throws ApplicationException {
		
		try{
			GrowthChartMaster master = new GrowthChartMaster();
			master.setAgeInMonths(dto.getAgeInMonths());
			master.setAgeInDays(dto.getAgeInMonths()*30);
			master.setWeightFrom(dto.getWeightFrom());
			master.setWeightTo(dto.getWeightTo());
			master.setWeightUnitId(dto.getWeightUnitId());
			master.setHeightFrom(dto.getHeightFrom());
			master.setHeightFrom(dto.getHeightFrom());
			master.setHeightTo(dto.getHeightTo());
			master.setHeightUnit(dto.getHeightUnit());
			master.setCircumferenceFrom(dto.getCircumferenceFrom());
			master.setCircumferenceTo(dto.getCircumferenceTo());
			master.setCircumferenceUnit(dto.getCircumferenceUnit());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganisationId(dto.getOrganisationId());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setGenderId(dto.getGenderId());
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getGrowthMaster(int orgId, int unitId, int offset, int recordPerPage) throws ApplicationException {
		try{
			List<GrowthChartMasterDto> listGrowthChartMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_GROWTH_CHART_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setFirstResult(offset)
					.setMaxResults(recordPerPage != 0 ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(GrowthChartMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listGrowthChartMasterDto, null);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public Response getGrowthMasterById(int orgId, int unitId, int growthChartId) throws ApplicationException {
		try{
			GrowthChartMasterDto growthChartMasterDto = (GrowthChartMasterDto)sessionFactory.getCurrentSession().getNamedQuery("GET_GROWTH_CHART_BY_ID")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("growthChartId", growthChartId)
					.setResultTransformer(Transformers.aliasToBean(GrowthChartMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, growthChartMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getHeightUnit(int orgId, int unitId) throws ApplicationException {
		List<CommonDto>  listDeparmentDto  = null; 
		try
		{
			listDeparmentDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("select ht_unit.height_unit_id as \"id\" , "
							+ "ht_unit.unit_name as \"name\" "
							+ "from public.m_height_unit ht_unit")
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listDeparmentDto, null);

		} catch (Exception e)
		{
			logger.error("getHeightUnit failed", e);
			return new Response(ERROR, ERROR, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response getWeightUnit(int orgId, int unitId) throws ApplicationException {
		List<CommonDto>  listDeparmentDto  = null; 
		try
		{
			listDeparmentDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("select wt_unit.weight_unit_id as \"id\" , "
							+ "wt_unit.unit_name as \"name\" from public.m_weight_unit wt_unit")
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listDeparmentDto, null);

		} catch (Exception e)
		{
			logger.error("getWeightUnit failed", e);
			return new Response(ERROR, ERROR, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response getCircumferenceUnit(int orgId, int unitId) throws ApplicationException {
		List<CommonDto>  listDeparmentDto  = null; 
		try
		{
			listDeparmentDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("select cf_unit.circufereance_unit_id as \"id\" , "
							+ "cf_unit.unit_name as \"name\" from public.m_circufereance_unit cf_unit")
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listDeparmentDto, null);

		} catch (Exception e)
		{
			logger.error("getCircumferenceUnit failed", e);
			return new Response(ERROR, ERROR, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response upadateGrowthMaster(GrowthChartMasterDto dto) throws ApplicationException {
		try{
			GrowthChartMaster master = new GrowthChartMaster();
			master.setGrowthChartId(dto.getGrowthChartId());
			master.setAgeInMonths(dto.getAgeInMonths());
			master.setAgeInDays(dto.getAgeInMonths()*30);
			master.setWeightFrom(dto.getWeightFrom());
			master.setWeightTo(dto.getWeightTo());
			master.setWeightUnitId(dto.getWeightUnitId());
			master.setHeightFrom(dto.getHeightFrom());
			master.setHeightFrom(dto.getHeightFrom());
			master.setHeightTo(dto.getHeightTo());
			master.setHeightUnit(dto.getHeightUnit());
			master.setCircumferenceFrom(dto.getCircumferenceFrom());
			master.setCircumferenceTo(dto.getCircumferenceTo());
			master.setCircumferenceUnit(dto.getCircumferenceUnit());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganisationId(dto.getOrganisationId());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setGenderId(dto.getGenderId());
			master = update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response checkAgeExistOrNotAdd(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException {
		try
		{
			BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_GROWTH_CHART_EXIST_ADD")
					.setParameter("orgId", growthChartMasterDto.getOrganisationId())
					.setParameter("unitId", growthChartMasterDto.getUnitId())
					.setParameter("ageInMonths", growthChartMasterDto.getAgeInMonths())
					.setParameter("genderId", growthChartMasterDto.getGenderId())
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);

		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response checkAgeExistOrNotUpdate(GrowthChartMasterDto growthChartMasterDto) throws ApplicationException {
		try
		{
			BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_GROWTH_CHART_EXIST_UPDATE")
					.setParameter("orgId", growthChartMasterDto.getOrganisationId())
					.setParameter("unitId", growthChartMasterDto.getUnitId())
					.setParameter("ageInMonths", growthChartMasterDto.getAgeInMonths())
					.setParameter("growthChartId", growthChartMasterDto.getGrowthChartId())
					.setParameter("genderId", growthChartMasterDto.getGenderId())
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);

		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response deleteGrowthChartMaster(int orgId, int unitId, int growthChartId) throws ApplicationException {
		try
		{
			int count = (int) sessionFactory.getCurrentSession()
					.getNamedQuery("DELETE_GROWTH_CHART_MASTER")
					.setParameter("growthChartId", growthChartId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);

		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveGrowthChartTransaction(TGrowthChartDto dto) throws ApplicationException {
		
		try{
			TGrowthChart master = new TGrowthChart();
			master.setGrowthChartId(dto.getGrowthChartId());
			master.setGrowthChartDate( GlobalCommonDateUtils.getDate(dto.getGrowthChartDate(), "dd-M-yyyy HH:mm:ss"));
			master.setWeight(dto.getWeight());
			master.setHeight(dto.getHeight());
			master.setCircumference(dto.getCircumference());
			master.setPatientId(dto.getPatientId());
			master.setVisitTypeId(dto.getVisitTypeId());
			master.setAdmissionId(dto.getAdmissionId());
			master.setEncounterId(dto.getEncounterId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganisationId(dto.getOrganisationId());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			int id = (int)sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);
		}catch(Exception e){
			
			throw e;
		}
	}

	
	
	@Override
	public Response getGrowthChartTransaction(GrowthChartReqDto growthChartReqDto) throws ApplicationException {
		try{
			
			
			String query1 = " select  "
					+ " 	growth_chart.growth_chart_id as \"growthChartId\",  "
					+ " 	gw_chart.t_growth_chart_id as \"tGrowthChartId\",  "
					+ " 	growth_chart.age_in_months as \"ageInMonths\",  "
					+ " 	to_char( gw_chart.growth_chart_date,  "
					+ " 	'MM-DD-YYYY HH:MI:SS' ) as \"growthChartDate\",  "
					+ " 	gw_chart.weight as \"weight\",  "
					+ " 	gw_chart.height as \"height\",  "
					+ " 	gw_chart.circumference as \"circumference\",  "
					+ " 	growth_chart.weight_from as \"weightFrom\",  "
					+ " 	growth_chart.weight_to as \"weightTo\",  "
					+ " 	wt_unit.unit_name as \"weightUnit\",  "
					+ " 	growth_chart.height_from as \"heightFrom\",  "
					+ " 	growth_chart.height_to as \"heightTo\",  "
					+ " 	ht_unit.unit_name as \"heightUnitString\",  "
					+ " 	growth_chart.circumference_from as \"circumferenceFrom\",  "
					+ " 	growth_chart.circumference_to as \"circumferenceTo\",  "
					+ "		gender_master.gender_name as \"gender\", "
					+ " 	cf_unit.unit_name as \"circumferenceUnitString\"  "
					+ " from  "
					+ " 	public.m_growth_chart_master growth_chart  "
					+ " inner join public.m_height_unit ht_unit on  "
					+ " 	growth_chart.height_unit = ht_unit.height_unit_id  "
					+ " inner join public.m_weight_unit wt_unit on  "
					+ " 	growth_chart.weight_unit_id = wt_unit.weight_unit_id  "
					+ " inner join public.m_circufereance_unit cf_unit on  "
					+ " 	growth_chart.circumference_unit = cf_unit.circufereance_unit_id   "
					+ " inner join public.t_growth_chart gw_chart on  "
					+ " 	growth_chart.growth_chart_id = gw_chart.growth_chart_id "
					+ "	inner join public.m_gender_master gender_master on  "
					+ " 	growth_chart.gender_id = gender_master.gender_id "
					+ " where  "
					+ " 	growth_chart.organisation_id = :orgId  "
					+ " 	and growth_chart.unit_id = :unitId  "
					+ " 	and growth_chart.status <> 'D'  "
					+ " 	 and gw_chart.patient_id = :patientId   "
					+ " 	and growth_chart.gender_id = :genderId  ";
			
			
			
			String query2 = " union select  "
					+ " 	growth_chart.growth_chart_id as \"growthChartId\",  "
					+ " 	null as \"tGrowthChartId\",  "
					+ " 	growth_chart.age_in_months as \"ageInMonths\",  "
					+ " 	null as \"growthChartDate\",  "
					+ " 	null as \"weight\",  "
					+ " 	null as \"height\",  "
					+ " 	null as \"circumference\",  "
					+ " 	growth_chart.weight_from as \"weightFrom\",  "
					+ " 	growth_chart.weight_to as \"weightT\",  "
					+ " 	wt_unit.unit_name as \"weightUnit\",  "
					+ " 	growth_chart.height_from as \"heightFrom\",  "
					+ " 	growth_chart.height_to as \"heightTo\",  "
					+ " 	ht_unit.unit_name as \"heightUnitString\",  "
					+ " 	growth_chart.circumference_from as \"circumferenceFrom\",  "
					+ " 	growth_chart.circumference_to as \"circumferenceTo\",  "
					+ "		gender_master.gender_name as \"gender\", "
					+ " 	cf_unit.unit_name as \"circumferenceUnitString\"  "
					+ " from  "
					+ " 	public.m_growth_chart_master growth_chart  "
					+ " inner join public.m_height_unit ht_unit on  "
					+ " 	growth_chart.height_unit = ht_unit.height_unit_id  "
					+ " inner join public.m_weight_unit wt_unit on  "
					+ " 	growth_chart.weight_unit_id = wt_unit.weight_unit_id  "
					+ " inner join public.m_circufereance_unit cf_unit on  "
					+ " 	growth_chart.circumference_unit = cf_unit.circufereance_unit_id  "
					+ "	inner join public.m_gender_master gender_master on  "
					+ " 	growth_chart.gender_id = gender_master.gender_id "
					+ " where  "
					+ " 	growth_chart.organisation_id = :orgId  "
					+ " 	and growth_chart.unit_id = :unitId  "
					+ " 	and growth_chart.status <> 'D'  "
					+ " 	and growth_chart.gender_id = :genderId  ";

		
			StringBuilder sb  = new StringBuilder(query1);
			StringBuilder sb1  = new StringBuilder(query2);
			Boolean checkEncounter = true;
			if(growthChartReqDto.getAdmissionId()!=null &&growthChartReqDto.getAdmissionId()>0)
			{
				sb.append("	and gw_chart.admission_id =:admissionId ");
				sb1.append("  and growth_chart.growth_chart_id not in ( "
						+ "    select "
						+ "      gc.growth_chart_id "
						+ "    from "
						+ "      public.t_growth_chart gc "
						+ "    where "
						+ "      gc.organisation_id = :orgId "
						+ "      and gc.unit_id = :unitId "
						+ "      and gc.patient_id = :patientId "
						+ "      and gc.admission_id =:admissionId "
						+ "  )");
				checkEncounter = false;
			 }
			else if(growthChartReqDto.getEncounterId()!=null &&growthChartReqDto.getEncounterId()>0)
			{
				sb.append("	and gw_chart.encounter_id =:encounterId ");
				sb1.append("  and growth_chart.growth_chart_id not in ( "
						+ "    select "
						+ "      gc.growth_chart_id "
						+ "    from "
						+ "      public.t_growth_chart gc "
						+ "    where "
						+ "      gc.organisation_id = :orgId "
						+ "      and gc.unit_id = :unitId "
						+ "      and gc.patient_id = :patientId "
						+ "      and gc.encounter_id =:encounterId"
						+ "  )");
				checkEncounter = false;
			}

			if(checkEncounter){
				sb1.append("  and growth_chart.growth_chart_id not in ( "
						+ "    select "
						+ "      gc.growth_chart_id "
						+ "    from "
						+ "      public.t_growth_chart gc "
						+ "    where "
						+ "      gc.organisation_id = :orgId "
						+ "      and gc.unit_id = :unitId "
						+ "      and gc.patient_id = :patientId "
						+ "  )");
			}

			sb.append(sb1);
			Query createQury = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("orgId", growthChartReqDto.getOrganisationId())
					.setParameter("unitId", growthChartReqDto.getUnitId())
					.setParameter("patientId", growthChartReqDto.getPatientId())
					.setParameter("genderId", growthChartReqDto.getGenderId());

			if(growthChartReqDto.getAdmissionId()!=null &&growthChartReqDto.getAdmissionId()>0)
			{
				createQury.setParameter("admissionId",growthChartReqDto.getAdmissionId());
			}else if(growthChartReqDto.getEncounterId()!=null &&growthChartReqDto.getEncounterId()>0)
			{
				createQury.setParameter("encounterId",growthChartReqDto.getEncounterId());
			}
			List<TGrowthChartDto> list =createQury.setResultTransformer(Transformers.aliasToBean(TGrowthChartDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, list);
		}catch(Exception e){
			
			throw e;
		}
	}

	

}
