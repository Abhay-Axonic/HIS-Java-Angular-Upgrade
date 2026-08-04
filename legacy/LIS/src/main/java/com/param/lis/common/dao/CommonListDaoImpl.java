package com.param.lis.common.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.common.dto.CommonDto;
import com.param.lis.common.dto.RejectionReasonDto;
import com.param.lis.common.dto.SampleStatusDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.AntibioticOrganismMpprMasterDto;
import com.param.lis.global.dto.SelectedAntibioticListDto;
import com.param.lis.transaction.dto.ParameterHistoryDto;

import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class CommonListDaoImpl  implements ICommonListDao, ICommonConstants, IError
{
	final static Logger logger = Logger.getLogger(CommonListDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response getSampleStatusList(Integer orgId) throws ApplicationException
	{
		List<SampleStatusDto>  listSampleStatusDto  = null; 
		try
		{
			listSampleStatusDto = (List<SampleStatusDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SAMPLE_STATUS_LIST").setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(SampleStatusDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listSampleStatusDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getRejectionReasonList(Integer orgId) throws ApplicationException
	{
		List<RejectionReasonDto>  listRejectionReasonDto  = null; 
		try
		{
			listRejectionReasonDto = (List<RejectionReasonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	rejmst.rejection_id AS \"rejectionId\", "
							+"	rejmst.rejection_name AS \"rejectionName\" "
							+"FROM "
							+"	lab.m_rejection_master rejmst "
							+"WHERE "
							+"	rejmst.status = 'A' "
							+"	AND rejmst.org_id = :orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(RejectionReasonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listRejectionReasonDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getDeparmentList(Integer orgId,Integer unitId) throws ApplicationException
	{
		List<CommonDto>  listDeparmentDto  = null; 
		try
		{
			listDeparmentDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" select  "
							 + "	subspemst.sub_speciality_id as id,  "
							 + "	subspemst.sub_speciality_name as name  "
							 + "from  "
							 + "	doctor.m_sub_speciality_master subspemst  "
							 + "inner join public.t_unit_sub_speciality_mapper mppr on  "
							 + "	mppr.sub_speciality_id = subspemst.sub_speciality_id  "
							 + "inner join public.t_lab_configuration lab_config on  "
							 + "	mppr.unit_id = lab_config.unit_id  "
							 + "	and lab_config.lab_speciality_id = subspemst.speciality_id  "
							 + "where  "
							 + "	subspemst.status = 'A'  "
							 + "	and subspemst.organization_id = :orgId  "
							 + " and lab_config.unit_id = :unitId"
							 + " ORDER BY subspemst.sub_speciality_name ASC ")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listDeparmentDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getSampleTypeList(Integer orgId) throws ApplicationException
	{
		List<CommonDto>  listSampleTypeDto  = null; 
		try
		{
			listSampleTypeDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	smpmst.sample_id AS id, "
							+"	smpmst.sample_desc AS name "
							+"FROM "
							+"	lab.m_sample_master smpmst "
							+"WHERE "
							+"	smpmst.sample_status = 'A' "
							+"	AND smpmst.org_id = :orgId "
							+ "  ORDER BY smpmst.sample_desc ASC ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listSampleTypeDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getUnitList(Integer orgId) throws ApplicationException
	{
		List<CommonDto>  listUnitDto  = null; 
		try
		{
			listUnitDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	lbunitmst.unit_id AS id, "
							+"	lbunitmst.unit_name AS name "
							+"FROM "
							+"	lab.m_unit_master lbunitmst "
							+"WHERE "
							+"	lbunitmst.unit_status = 'A' "
							+"	AND lbunitmst.org_id = :orgId "
							+ " ORDER BY lbunitmst.unit_name ASC ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listUnitDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getContainerList(Integer orgId) throws ApplicationException
	{
		List<CommonDto>  listContainerDto  = null; 
		try
		{
			listContainerDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	containermst.container_id AS id, "
							+"	containermst.container_name AS name "
							+"FROM "
							+"	lab.m_container_master containermst "
							+"WHERE "
							+"	containermst.container_status = 'A' "
							+"	AND containermst.org_id = :orgId "
							+ " ORDER BY containermst.container_name ASC ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listContainerDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;

		}
	}

	@Override
	public Response getReportTypeList(Integer orgId) throws ApplicationException
	{
		List<CommonDto>  listReportTypeDto  = null; 
		try
		{
			listReportTypeDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	reporttypemst.report_type_id AS id, "
							+"	reporttypemst.report_type_name AS name "
							+"FROM "
							+"	lab.m_report_type_master reporttypemst "
							+"WHERE "
							+"	reporttypemst.report_type_status = 'A' "
							+"	AND reporttypemst.org_id = :orgId "
							+ " ORDER BY reporttypemst.report_type_name ASC ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listReportTypeDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getTechniqueList(Integer orgId) throws ApplicationException
	{
		List<CommonDto>  listTechniqueDto  = null; 
		try
		{
			listTechniqueDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	techmst.technique_id AS id, "
							+"	techmst.technique_name AS name, "
							+"	techmst.technique_name AS label "
							+"FROM "
							+"	lab.m_technique_master techmst "
							+"WHERE "
							+"	techmst.technique_status = 'A' "
							+"	AND techmst.org_id = :orgId "
							+ " ORDER BY techmst.technique_name ASC ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTechniqueDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getPrerequisitesList(Integer orgId) throws ApplicationException
	{
		List<CommonDto>  listPrerequisitesDto  = null; 
		try
		{
			listPrerequisitesDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	prereqmst.prerequisites_id AS id, "
							+"	prereqmst.prerequisites_name AS name "
							+"FROM "
							+"	lab.m_prerequisites_master prereqmst "
							+"WHERE "
							+"	prereqmst.prerequisites_status = 'A' "
							+"	AND prereqmst.org_id = :orgId "
							+ " ORDER BY prereqmst.prerequisites_name ASC ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listPrerequisitesDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getHeaderList(Integer orgId,Integer orgUnitId) throws ApplicationException
	{
		List<CommonDto>  listHeaderDto  = null; 
		try
		{
			listHeaderDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	headermst.header_id AS id, "
							+"	headermst.header_desc AS name "
							+"FROM "
							+"	lab.m_header_master headermst "
							+"WHERE "
							+"	headermst.header_status = 'A' "
							+"	AND headermst.org_id = :orgId"
							+"	AND headermst.org_unit_id = :orgUnitId "
							+ " ORDER BY headermst.header_desc ASC ")
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listHeaderDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getParameterList(Integer orgId,Integer orgUnitId) throws ApplicationException
	{
		List<CommonDto>  listParameterDto  = null; 
		try
		{
			listParameterDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	parametermst.parameter_id AS id, "
							+"	parametermst.parameter_name AS name "
							+"FROM "
							+"	lab.m_parameter_master parametermst "
							+"WHERE "
							+"	parametermst.status = 'A' "
							+"  AND parametermst.is_multyparameter = 'Y' "
							+"	AND parametermst.org_id = :orgId "
							+"	AND parametermst.org_unit_id = :orgUnitId ")
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listParameterDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getAgeGroupList(Integer orgId) throws ApplicationException {

		List<CommonDto>  listAgeGroupDto  = null; 
		try
		{
			listAgeGroupDto = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT  "
							+ " ageGroupmst.age_group_id AS id,  "
							+ " ageGroupmst.age_type_grp_name AS name  "
							+ " FROM  "
							+ " lab.m_age_group_master ageGroupmst  "
							+ " WHERE  "
							+ " ageGroupmst.age_group_status = 'A'  "
							+ " AND ageGroupmst.org_id =:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listAgeGroupDto, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getGenderList(Integer orgId) throws ApplicationException {
		List<CommonDto>  listGenderList  = null; 
		try
		{
			listGenderList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT  "
							+ " genderMst.gender_id AS id, "
							+ " genderMst.gender_name AS name "
							+ " FROM "
							+ " m_gender_master genderMst "
							+ " WHERE "
							+ " genderMst.status = 'A' "
							+ " AND genderMst.organization_id=:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listGenderList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getDayList(Integer orgId) throws ApplicationException {
		List<CommonDto>  listDayList  = null; 
		try
		{
			listDayList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT  "
									+ " daymst.day_master_id AS id, "
									+ " daymst.day AS name  "
									+ " FROM "
									+ " lab.m_day_master daymst "
									+ " WHERE "
									+ " daymst.status = 'A' "
									+ " AND daymst.org_id =:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listDayList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getTrunAroundTime(Integer orgId)
			throws ApplicationException {
		List<CommonDto>  listTrunAroundTime  = null; 
		try
		{
			listTrunAroundTime = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(  "SELECT  "    
						    + " tatTimeMst.tat_time_id AS id, "
						    + " tatTimeMst.tat_time_name AS name "
						    + " FROM  "
							+ " lab.m_tat_time_master tatTimeMst "
							+ " WHERE  "
							+ " tatTimeMst.tat_time_status = 'A' "
							+ " AND tatTimeMst.org_id =:orgId "
							+ " ORDER BY tatTimeMst.tat_time_name ASC ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTrunAroundTime, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getAutoCompleteServices(Integer orgId,Integer orgUnitId,Integer deptId,String serviceName)
			throws ApplicationException {
		List<CommonDto>  listAutoComplete  = null; 
		try
		{
			listAutoComplete = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT"
							+" 	service_mst.service_master_id AS id,"
							+" 	service_mst.service_standard_name AS name"
							+" FROM"
							+"	service.m_service_master service_mst"
							+" INNER JOIN service.t_unit_service_mapper unit_service_mapper ON"
							+"	unit_service_mapper.service_id = service_mst.service_master_id"
							+" WHERE"
							+"	unit_service_mapper.status = 'A'"
							+"	AND unit_service_mapper.orgnisation_id = :orgId"
							+"	AND unit_service_mapper.status = 'A'"
							+"	AND unit_service_mapper.unit_id =:orgUnitId"
							+"	AND service_mst.speciality_id = :deptId"
							+"	AND LOWER(service_mst.service_standard_name ) LIKE LOWER(:serviceName )")
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
					.setParameter("deptId", deptId)
					.setParameter("serviceName", "%"+serviceName.trim().toLowerCase().trim()+"%")
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listAutoComplete, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getMediaList(Integer orgId) throws ApplicationException {
		List<CommonDto>  listMediaList  = null; 
		try
		{
			listMediaList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT "
						     + " mediaMst.media_id AS id, "
						     + " mediaMst.media_description AS name "
						     + " FROM "
							 + " lab.m_media_master mediaMst " 
							 + " WHERE   " 
							 + " mediaMst.media_status = 'A' "
							 + " AND mediaMst.org_id =:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listMediaList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getColonyList(Integer orgId) throws ApplicationException {
		List<CommonDto>  listColonyList  = null; 
		try
		{
			listColonyList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT "
						     + " colonyMst.colony_id AS id,  "
						     + " colonyMst.colony_name AS name "
						     + " FROM "
							 + " lab.m_colony_master colonyMst   " 
							 + " WHERE   " 
							 + " colonyMst.colony_status = 'A'  "
							 + " AND colonyMst.org_id =:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listColonyList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getAntibioticGroupList(Integer orgId)
			throws ApplicationException {
		List<CommonDto>  listAntibioticGroupList  = null; 
		try
		{
			listAntibioticGroupList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT "
						     + " antiGroupMst.antibiotic_group_id AS id, "
						     + " antiGroupMst.antibiotic_group_desc AS name "
						     + " FROM "
							 + " lab.m_antibiotic_group_master antiGroupMst  " 
							 + " WHERE   " 
							 + " antiGroupMst.antibiotic_group_status = 'A' "
							 + " AND antiGroupMst.org_id =:orgId "
							 + " ORDER BY antiGroupMst.antibiotic_group_desc ASC ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listAntibioticGroupList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getOrganismGroupList(Integer orgId)
			throws ApplicationException {
		List<CommonDto>  listOrganismGroupList  = null; 
		try
		{
			listOrganismGroupList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery( "SELECT  "
							+ "  organismGroupMst.organism_group_id AS id,"
							+ "  organismGroupMst.organism_group_name AS name "
							+ "  FROM  "
							+ "  lab.m_organism_group_master organismGroupMst "
							+ "  WHERE "
							+ "  organismGroupMst.organism_group_status = 'A'  "
							+ " AND organismGroupMst.org_id =:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listOrganismGroupList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@Override
	public Response getIncubationTimeList(Integer orgId) throws ApplicationException {
		List<CommonDto>  incubationTimeList  = null; 
		try
		{
			incubationTimeList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCUBATION_TIME_LIST")
					/* .setParameter("orgId", orgId) */
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, incubationTimeList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@Override
	public Response getMediaListBySampleType(Integer sampleId, Integer orgId) throws ApplicationException {
		List<CommonDto>  mediaList  = null; 
		try
		{
			mediaList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEDIA_LIST_BY_SAMPLE_ID")
					.setParameter("orgId", orgId)
					.setParameter("sampleId", sampleId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, mediaList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getIncubationPeriodList(Integer orgId) throws ApplicationException {
		List<CommonDto>  incubationPeriodList  = null; 
		try
		{
			incubationPeriodList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCUBATION_PERIOD_LIST")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, incubationPeriodList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getStainingListByOrgId(Integer orgId) throws ApplicationException {
		List<CommonDto>  stainignList  = null; 
		try
		{
			stainignList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STATINING_LIST_BY_ORGID")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, stainignList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getMicroOrganismByGroup(Integer organismGroupId, Integer orgId) throws ApplicationException {
		List<CommonDto>  organismList  = null; 
		try
		{
			organismList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ORGANISM_BY_GROUP_ID")
					.setParameter("orgId", orgId)
					.setParameter("organismGroupId", organismGroupId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, organismList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	
	

	@Override
	public Response getOrganismList(Integer orgId) throws ApplicationException {
		List<CommonDto>  listOrganismList  = null; 
		try
		{
			listOrganismList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery( "SELECT  "
							+ "  organismMst.organism_id AS id,"
							+ "  organismMst.organism_name AS name "
							+ "  FROM  "
							+ "  lab.m_organism_master organismMst "
							+ "  WHERE "
							+ "  organismMst.organism_status = 'A'  "
							+ " AND organismMst.org_id =:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listOrganismList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getAntibioticGroupByGroup(SelectedAntibioticListDto selectedAntibioticListDto) throws ApplicationException {
		List<AntibioticOrganismMpprMasterDto>  organismList  = null; 
		try
		{
			organismList = (List<AntibioticOrganismMpprMasterDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTIC_BY_ANTIBIOTIC_GROUP_ID_IN_ORGANISM")
					.setParameter("orgId", selectedAntibioticListDto.getOrgId())
					.setParameterList("antibioticGroupId", selectedAntibioticListDto.getSelectAntibioticList())
                    .setResultTransformer(Transformers.aliasToBean(AntibioticOrganismMpprMasterDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, organismList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getAutoCompleteServicesForAntibiotic(Integer orgId,
			String antibioticName) throws ApplicationException {
		List<CommonAutoCompleteDto>  listAutoComplete  = null; 
		try
		{
			listAutoComplete = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT"
							+" antiMast.antibiotic_id  AS id,"
							+" antiMast.antibiotic_name AS name"
							+" FROM"
							+"	lab.m_antibiotic_master antiMast"
							+" WHERE"
							+"	antiMast.antibiotic_status= 'A'"
							+"	AND antiMast.org_id = :orgId"
							+"	AND LOWER(antiMast.antibiotic_name ) LIKE LOWER(:antibioticName )")
					.setParameter("orgId", orgId)
					.setParameter("antibioticName", "%"+antibioticName.trim().toLowerCase().trim()+"%")
                    .setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listAutoComplete, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@Override
	public Response getMicrolabPriorityList(Integer orgId)
			throws ApplicationException {
		List<CommonDto>  listMicroPriorityMstList  = null; 
		try
		{
			listMicroPriorityMstList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery( "SELECT  "
							+ "  micro_priority_mst.microlab_priority_id AS id,"
							+ "  micro_priority_mst.priority_desc AS name "
							+ "  FROM  "
							+ "   lab.m_microlab_priority_master micro_priority_mst"
							+ "  WHERE "
							+ "   micro_priority_mst.status = 'A' "
							+ "   AND micro_priority_mst.org_id = :orgId ")
                    .setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listMicroPriorityMstList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getAntibioticsByOrganismIdList(Integer orgId,
			Integer organismId) throws ApplicationException {
		List<CommonDto>  organismList  = null; 
		try
		{
			organismList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ANTIBIOTIC_BY_ORGANISM_ID_IN_MICRO")
					.setParameter("orgId", orgId)
					.setParameter("organismId", organismId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, organismList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getSpecimanTypeList(Integer orgId) throws ApplicationException {
		List<CommonDto>  organismList  = null; 
		try
		{
			organismList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIMAN_TYPE_BY_ORG")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, organismList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getAccountPayableList(Integer orgId) throws ApplicationException {
		List<CommonDto>  listColonyList  = null; 
		try
		{
			listColonyList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT "
						     + " account_payable_master.acc_payable_mst_id AS id,  "
						     + " account_payable_master.acc_desc AS name "
						     + " FROM "
							 + " public.m_account_payable_master account_payable_master " 
							 + " WHERE   " 
							 + " account_payable_master.status = 'A'  "
							 + " AND account_payable_master.org_id =:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listColonyList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getRackList(Integer orgId) throws ApplicationException {
		List<CommonDto>  listColonyList  = null; 
		try
		{
			listColonyList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT "
						     + " rack_master.rack_id AS id,  "
						     + " rack_master.rack_name AS name "
						     + " FROM "
							 + "  lab.m_rack_master rack_master" 
							 + " WHERE   " 
							 + " rack_master.status = 'A'  "
							 + " AND rack_master.org_id =:orgId ")
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listColonyList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	@Override
	public Response getRackListByShelfId(Integer shelfId,Integer orgId) throws ApplicationException {
		List<CommonDto>  listColonyList  = null; 
		try
		{
			listColonyList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT "
						     + " rack_shelf_master.shelf_id AS id,  "
						     + " rack_shelf_master.shelf_name AS name "
						     + " FROM "
							 + "  lab.m_rack_shelf_master rack_shelf_master " 
							 + " WHERE   " 
							 + " rack_shelf_master.status = 'A'  "
							 + " AND rack_shelf_master.org_id =:orgId "
							 + " AND rack_shelf_master.rack_id =:shelfId "
							 + " AND is_deleted ='N' ")
					.setParameter("shelfId", shelfId)
					.setParameter("orgId", orgId)
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listColonyList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getSampleStatus(Integer orgId) throws ApplicationException {
		List<CommonDto>  listColonyList  = null; 
		try
		{
			listColonyList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT "
						     + " test_status_master.test_status_id AS id, "
						     + " test_status_master.status_name AS name "
						     + " FROM "
							 + "  lab.m_test_status_master  test_status_master " 
							 + " WHERE   " 
							 + " test_status_master.status = 'A' "
							 + " ORDER BY test_status_master.status_name ASC "
							 /*+ " AND test_status_master.org_id =:orgId "*/)
				/*	.setParameter("orgId", orgId)*/
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listColonyList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

  @Override
  public Response getTestParameters(Integer testId, Integer orgId, Integer orgUnitId)
      throws ApplicationException {
    try
    {
      List<ParameterHistoryDto>  listParameterList  = null; 
      listParameterList = (List<ParameterHistoryDto>) sessionFactory.getCurrentSession()
                .getNamedQuery("GET_PARAMETER_FOR_HISTORY")
                .setParameter("orgId", orgId)
                .setParameter("orgUnitId", orgUnitId)
                .setParameter("testId", testId)
                .setResultTransformer(Transformers.aliasToBean(ParameterHistoryDto.class)).list();                    
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listParameterList, null);
    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
  }

  @Override
  public Response getRefRangeTypeList(Integer orgId, Integer orgUnitId)
      throws ApplicationException {
    try
    {
      List<CommonDto>  listRefRangeTypeList  = null; 
      listRefRangeTypeList = (List<CommonDto>) sessionFactory.getCurrentSession()
                .getNamedQuery("GET_REF_RENGE_TYPE_LIST")
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();                    
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listRefRangeTypeList, null);

    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
  }

  @Override
  public Response getTextualReferenceRanges(Integer orgId, Integer orgUnitId)
      throws ApplicationException {
    try
    {
      List<CommonDto>  listTextualRefRangeList  = null; 
      listTextualRefRangeList = (List<CommonDto>) sessionFactory.getCurrentSession()
                .getNamedQuery("GET_TEXTUAL_RANGE_LIST")
                .setParameter("orgId", orgId)
               // .setParameter("orgUnitId", orgUnitId)
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();                    
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTextualRefRangeList, null);

    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
  }

  @Override
  public Response getMicrobiologyTestList(Integer orgId, Integer orgUnitId) {
    List<CommonDto>  listMicroTest  = null; 
    try
    {
      listMicroTest = (List<CommonDto>) sessionFactory.getCurrentSession()
                .createQuery(" SELECT "
                         + " microbiologyTestMaster.microTestId AS id,  "
                         + " microbiologyTestMaster.microTestDesc AS name "
                         + " FROM "
                         + " MicrobiologyTestMaster microbiologyTestMaster   " 
                         + " WHERE   " 
                         + " microbiologyTestMaster.status = 'A'  "
                         + " AND microbiologyTestMaster.orgId =:orgId ")
                .setParameter("orgId", orgId)
               // .setParameter("orgUnitId", orgUnitId) + " AND microbiologyTestMaster.orgUnitId =:orgUnitId "
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();                    
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listMicroTest, null);

    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
  }

  @Override
  public Response getMicrobiologyStainingList(Integer orgId) throws ApplicationException {
    List<CommonDto>  stainignList  = null; 
    try
    {
        stainignList = (List<CommonDto>) sessionFactory.getCurrentSession()
                .getNamedQuery("GET_MICRO_STAIN_LIST")
                .setParameter("orgId", orgId)
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();                    
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, stainignList, null);

    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
  }

@Override
public Response getSendThroughList() throws ApplicationException {
	 try
	    {
		 List<CommonDto>   listMicroTest = (List<CommonDto>) sessionFactory.getCurrentSession()
	                .createSQLQuery(" select mst.send_through_id as id, "
	                		+ "mst.send_throug_desc as name "
	                		+ "from lab.m_send_throgh_master mst "
	                		+ "where mst.status = 'A'"
	                		+ "ORDER BY mst.send_throug_desc ASC")
	                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();                    
	        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listMicroTest, null);

	    } catch (Exception e)
	    {
	        logger.error("Exection", e);
	        throw e;
	    }
}

@Override
public Response getRequestTypeList() throws ApplicationException {
	 try
	    {
		 List<CommonDto>   listRequestTypeList = (List<CommonDto>) sessionFactory.getCurrentSession()
	                .createSQLQuery(" select mst.request_type_id as id, "
	                		+ "mst.request_type_desc as name "
	                		+ "from billing.m_request_type_master mst "
	                		+ "where mst.status = 'A'"
	                		+ "ORDER BY mst.request_type_desc asc")
	                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();                    
	        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listRequestTypeList, null);

	    } catch (Exception e)
	    {
	        logger.error("Exection", e);
	        throw e;
	    }
}

@Override
public Response getAuthorizationStatusList() throws ApplicationException {
	try
    {
	 List<CommonDto>   listRequestTypeList = (List<CommonDto>) sessionFactory.getCurrentSession()
                .createSQLQuery(" select mst.auth_status_id as id, "
                		+ "mst.auth_status_desc as name "
                		+ "from billing.m_authorization_status_master mst "
                		+ "where mst.status = 'A'"
                		+ "ORDER BY mst.auth_status_desc ASC")
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();                    
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listRequestTypeList, null);

    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
}

@Override
public Response getSerologyFindings(Integer orgId, Integer orgUnitId) throws ApplicationException {
	List<CommonDto>  list  = null; 
	try
	{
		list = (List<CommonDto>) sessionFactory.getCurrentSession()
				.createSQLQuery("select " + 
						"	mst.serology_id as \"id\" ," + 
						"	mst.serology_test as \"label\" " + 
						" from " + 
						"	lab.m_serology_test_finding_master mst " + 
						" where " + 
						"	mst.org_id = :orgId " + 
						"	and mst.org_unit_id = :orgUnitId")
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId", orgUnitId)
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, null);

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}

@Override
public Response getMultiTestParameterList(Integer orgId, Integer orgUnitId) throws ApplicationException {
	List<CommonDto>  listParameterDto  = null; 
	try
	{
		listParameterDto = (List<CommonDto>) sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT "
						+"	parametermst.parameter_id AS id, "
						+"	parametermst.parameter_name AS name "
						+"FROM "
						+"	lab.m_parameter_master parametermst "
						+"WHERE "
						+"	parametermst.status = 'A' "
						+"  AND parametermst.is_multyparameter = 'Y' "
						+ " AND parametermst.refrange_type_id = 1 "
						+"	AND parametermst.org_id = :orgId "
						+"	AND parametermst.org_unit_id = :orgUnitId ")
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId", orgUnitId)
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listParameterDto, null);

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}

@Override
public Response getTypeHeadSearchHeaderList(Integer orgId, Integer orgUnitId, String label)
		throws ApplicationException {
	List<CommonDto>  listHeaderDto  = null; 
	try
	{
		listHeaderDto = (List<CommonDto>) sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT "
						+"	headermst.header_id AS id, "
						+"	headermst.header_desc AS name "
						+"FROM "
						+"	lab.m_header_master headermst "
						+"WHERE "
						+"	headermst.header_status = 'A' "
						+"	AND headermst.org_id = :orgId"
						+"	AND headermst.org_unit_id = :orgUnitId "
						+ " AND (lower(headermst.header_desc) like :label OR lower(headermst.header_code) like :label)")
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId", orgUnitId)
				.setParameter("label", "%"+ label.trim().toLowerCase()+"%")
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listHeaderDto, null);

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}

@Override
public Response getTypeHeadSearchParameterList(Integer orgId, Integer orgUnitId, String label)
		throws ApplicationException {
	List<CommonDto>  listParameterDto  = null; 
	try
	{
		listParameterDto = (List<CommonDto>) sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT "
						+"	parametermst.parameter_id AS id, "
						+"	parametermst.parameter_name AS name "
						+"FROM "
						+"	lab.m_parameter_master parametermst "
						+"WHERE "
						+"	parametermst.status = 'A' "
						+"  AND parametermst.is_multyparameter = 'Y' "
						+"	AND parametermst.org_id = :orgId "
						+"	AND parametermst.org_unit_id = :orgUnitId "
						+ " AND (lower(parametermst.parameter_name) like :label OR lower(parametermst.parameter_code) like :label) ")
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId", orgUnitId)
				.setParameter("label", "%"+ label.trim().toLowerCase()+"%")
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listParameterDto, null);

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}

}
