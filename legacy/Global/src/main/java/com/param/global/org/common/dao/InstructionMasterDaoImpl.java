package com.param.global.org.common.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.global.InstructionMasterDto;
import com.param.global.dto.global.TranslatedInstructionMasterDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.model.InstructionMaster;
import com.param.opd.coversheet.model.InstuctionDeptSpecialityMapperMaster;
import com.param.opd.coversheet.model.TranslatedInstructionMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class InstructionMasterDaoImpl extends GenericDao<InstructionMaster, Integer>
		implements IInstructionMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(InstructionMasterDaoImpl.class);


	public InstructionMasterDaoImpl() {
		super(InstructionMaster.class);
	}
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveInspectionMaster(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			InstructionMaster instructionMaster = new InstructionMaster();

			instructionMaster.setInstructionCode(instructionMasterDto.getInstructionCode());
			instructionMaster.setInstructionDesc(instructionMasterDto.getInstructionDesc());
			instructionMaster.setIsMedication(instructionMasterDto.getIsMedication());
			instructionMaster.setStatus('A');
			instructionMaster.setCreatedBy(instructionMasterDto.getCreatedBy());
			instructionMaster.setUpdatedBy(instructionMasterDto.getUpdatedBy());
			instructionMaster.setOrganizationId(instructionMasterDto.getOrganizationId());
			instructionMaster.setCreatedDate(GlobalCommonDateUtils.getDate(instructionMasterDto.getCreatedDate(), "yyyy-M-dd HH:mm:ss"));
			instructionMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(instructionMasterDto.getUpdatedDate(), "yyyy-M-dd HH:mm:ss"));
			
			save(instructionMaster);
			instructionMasterDto.setInstructionId(instructionMaster.getInstructionId());
			
			   for(InstructionMasterDto visitTypeObj:instructionMasterDto.getDeptSpecilityId()){
				   InstuctionDeptSpecialityMapperMaster instDeptSpecialitympr= new InstuctionDeptSpecialityMapperMaster();
				   instDeptSpecialitympr.setInstuctionDeptSpecialityMapperId(instructionMasterDto.getInstuctionDeptSpecialityMapperId());
				         instDeptSpecialitympr.setDeptSpecialityId(visitTypeObj.getId());
				         instDeptSpecialitympr.setInstructionId(instructionMaster.getInstructionId());
					     instDeptSpecialitympr.setUnitId(instructionMasterDto.getUnitId());
					     instDeptSpecialitympr.setOrganizationId(instructionMasterDto.getOrganizationId());
					     instDeptSpecialitympr.setCreatedBy(instructionMasterDto.getCreatedBy());
					     instDeptSpecialitympr.setStatus('A');
					     instDeptSpecialitympr.setCreatedDate(TimeZoneComponent.getDateByzone(instructionMasterDto.getUnitId()));
					     instDeptSpecialitympr.setUpdatedBy(instructionMasterDto.getCreatedBy());
					     instDeptSpecialitympr.setUpdatedDate(TimeZoneComponent.getDateByzone(instructionMasterDto.getUnitId()));
					     sessionFactory.getCurrentSession().save(instDeptSpecialitympr);

				   }
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, instructionMasterDto);
		} catch (HibernateException he) {
		logger.error("saveInspectionMaster failed", he);
		} catch (Exception e) {
		logger.error("saveInspectionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInspectionMasterList(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSPECTION_MASTER_LIST")
					.setInteger("orgId", instructionMasterDto.getOrganizationId())
					.setFirstResult(instructionMasterDto.getOffset() != null ? instructionMasterDto.getOffset() : 0)
					.setMaxResults(instructionMasterDto.getNoOfRecordsPerPage() != null ? instructionMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getInspectionMasterList failed", he);
		} catch (Exception e) {
		logger.error("getInspectionMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateInspectionMasterStatus(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			InstructionMaster instructionMaster = findById(instructionMasterDto.getInstructionId());
			instructionMaster.setStatus(instructionMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			instructionMaster.setUpdatedBy(instructionMasterDto.getUpdatedBy());
			instructionMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(instructionMasterDto.getUpdatedDate(), "yyyy-M-dd HH:mm:ss"));
			update(instructionMaster);
			return new Response<>(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, instructionMaster.getOrganizationId());
		} catch (HibernateException he) {
		logger.error("updateInspectionMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateInspectionMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInspectionMasterCount(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(InstructionMaster.class); 
			c.add(Restrictions.eq("organizationId", instructionMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getInspectionMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getInspectionMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateInspectionMaster(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			InstructionMaster instructionMaster = new InstructionMaster();
			instructionMaster.setInstructionId(instructionMasterDto.getInstructionId());
			instructionMaster.setInstructionCode(instructionMasterDto.getInstructionCode());
			instructionMaster.setInstructionDesc(instructionMasterDto.getInstructionDesc());
			instructionMaster.setIsMedication(instructionMasterDto.getIsMedication());
			instructionMaster.setStatus('A');
			instructionMaster.setCreatedBy(instructionMasterDto.getCreatedBy());
			instructionMaster.setUpdatedBy(instructionMasterDto.getUpdatedBy());
			instructionMaster.setOrganizationId(instructionMasterDto.getOrganizationId());
			instructionMaster.setCreatedDate(TimeZoneComponent.getDateByzone(instructionMasterDto.getUnitId()));
			instructionMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(instructionMasterDto.getUnitId()));
			update(instructionMaster);
			
			Integer count=sessionFactory.getCurrentSession().createQuery(" update InstuctionDeptSpecialityMapperMaster tvm set status='I' where tvm.instructionId=:instructionId "	)
	     			.setInteger("instructionId", instructionMaster.getInstructionId()).executeUpdate();

			  for(InstructionMasterDto visitTypeObj:instructionMasterDto.getDeptSpecilityId()){
				   InstuctionDeptSpecialityMapperMaster instDeptSpecialitympr= new InstuctionDeptSpecialityMapperMaster();
				         instDeptSpecialitympr.setDeptSpecialityId(visitTypeObj.getId());
				         instDeptSpecialitympr.setInstructionId(instructionMaster.getInstructionId());
					     instDeptSpecialitympr.setUnitId(instructionMasterDto.getUnitId());
					     instDeptSpecialitympr.setOrganizationId(instructionMasterDto.getOrganizationId());
					     instDeptSpecialitympr.setCreatedBy(instructionMasterDto.getCreatedBy());
					     instDeptSpecialitympr.setStatus('A');
					     instDeptSpecialitympr.setCreatedDate(TimeZoneComponent.getDateByzone(instructionMasterDto.getUnitId()));
					     instDeptSpecialitympr.setUpdatedBy(instructionMasterDto.getCreatedBy());
					     instDeptSpecialitympr.setUpdatedDate(TimeZoneComponent.getDateByzone(instructionMasterDto.getUnitId()));
					     sessionFactory.getCurrentSession().save(instDeptSpecialitympr);

				   }
			return new Response<>(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, instructionMasterDto);
		} catch (HibernateException he) {
		logger.error("updateInspectionMaster failed", he);
		} catch (Exception e) {
		logger.error("updateInspectionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInspectionMasterByName(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSPECTION_MASTER_BY_NAME").setInteger("orgId", instructionMasterDto.getOrganizationId())//minesh
					.setString("name", instructionMasterDto.getInstructionDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getInspectionMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getInspectionMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInspectionMasterByNameNotById(InstructionMasterDto instructionMasterDto)
			throws ApplicationException {
		try {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSPECTION_MASTER_BY_NAME_NOT_BY_ID")
					.setInteger("inspectionId", instructionMasterDto.getInstructionId())
					.setInteger("orgId", instructionMasterDto.getOrganizationId())
					.setString("name", instructionMasterDto.getInstructionDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getInspectionMasterByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getInspectionMasterByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInspectionMasterListById(int inspectionId, int orgId) throws ApplicationException {
		try {
			InstructionMasterDto instructionById = (InstructionMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INSPECTION_MASTER_LIST_BY_ID")
					.setInteger("inspectionId", inspectionId)
					.setInteger("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).uniqueResult();
			
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT sm.speciality_id as \"id\", " 
							+" sm.speciality_name as \"specialityName\", "
							+ "dsm.instuction_dept_speciality_mapper_id  as \"instuctionDeptSpecialityMapperId\" "
							+" FROM doctor.m_speciality_master sm "
							+" join public.m_instuction_dept_speciality_mapper dsm on sm.speciality_id= dsm.dept_speciality_id " 
							+" WHERE dsm.status='A'AND dsm.instruction_id=:instructionId "
							+" AND dsm.org_id=:orgId ")
					 .setInteger("orgId", orgId)
					 .setInteger("instructionId",inspectionId)
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).list();
			
			List<TranslatedInstructionMasterDto> translatedInstructionList = sessionFactory.getCurrentSession()
					.createSQLQuery("select tran.instruction_desc as \"desc\", tran.converted_text as \"convertedText\", tran.language_id as \"languageId\" from public.t_translated_instruction tran "
							+" WHERE tran.status='A'AND tran.instruction_id=:instructionId "
							+" AND tran.organization_id=:orgId ")
					 .setInteger("orgId", orgId)
					 .setInteger("instructionId",inspectionId)
					.setResultTransformer(Transformers.aliasToBean(TranslatedInstructionMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, instructionById, translatedInstructionList);
		} catch (HibernateException he) {
		logger.error("getInspectionMasterListById failed", he);
		} catch (Exception e) {
		logger.error("getInspectionMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveInspectionMasterList(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_INSPECTION_MASTER_LIST")
					.setInteger("orgId", instructionMasterDto.getOrganizationId())
					.setCharacter("isMedication", instructionMasterDto.getIsMedication())
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getActiveInspectionMasterList failed", he);
		} catch (Exception e) {
		logger.error("getActiveInspectionMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDeptSpecilityById(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT sm.speciality_id as \"id\", " 
							+" sm.speciality_name as \"specialityName\", "
							+ "dsm.instuction_dept_speciality_mapper_id  as \"instuctionDeptSpecialityMapperId\" "
							+" FROM doctor.m_speciality_master sm "
							+" join public.m_instuction_dept_speciality_mapper dsm on sm.speciality_id= dsm.dept_speciality_id " 
							+" WHERE dsm.status='A'AND dsm.instruction_id=:instructionId "
							+" AND dsm.org_id=:orgId AND dsm.unit_id=:unitId")
					 .setInteger("orgId", instructionMasterDto.getOrganizationId())
					 .setInteger("unitId",instructionMasterDto.getUnitId())
					 .setInteger("instructionId",instructionMasterDto.getInstructionId())
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getDeptSpecilityById failed", he);
		} catch (Exception e) {
		logger.error("getDeptSpecilityById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSpecialityByDept(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			if(instructionMasterDto.getSpecialityId()!=null) {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT distinct sm.speciality_id as \"specialityId\", " 
							+" sm.speciality_name as \"specialityName\","
							+" ins.instruction_id as\"instructionId\",ins.instruction_code as\"instructionCode\", "
							+" ins.instruction_desc as\"instructionDesc\", "
							+" dsm.instuction_dept_speciality_mapper_id  as \"instuctionDeptSpecialityMapperId\" "
							+" from public.m_instruction  ins "
							+" join public.m_instuction_dept_speciality_mapper dsm on ins.instruction_id=dsm.instruction_id"
							+" join doctor.m_speciality_master sm on sm.speciality_id = dsm.dept_speciality_id  " 
							+" WHERE dsm.status='A'AND ins.organization_id=:orgId "
							+" AND dsm.unit_id=:unitId AND sm.speciality_id =:specialityId AND ins.status='A'")
					 .setInteger("orgId", instructionMasterDto.getOrganizationId())
					 .setInteger("unitId",instructionMasterDto.getUnitId())
					 .setInteger("specialityId",instructionMasterDto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
			}
		} catch (HibernateException he) {
		logger.error("getSpecialityByDept failed", he);
		} catch (Exception e) {
		logger.error("getSpecialityByDept failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveTranslatedInstruction(TranslatedInstructionMasterDto dto) throws ApplicationException {
		try {

			TranslatedInstructionMaster master= mapper.map(dto, TranslatedInstructionMaster.class,
					"TranslatedInstructionMasterDto_to_TranslatedInstructionMaster");
	
			sessionFactory.getCurrentSession().save(master);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("saveTranslatedInstruction failed", he);
		} catch (Exception e) {
		logger.error("saveTranslatedInstruction failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTranslatedInstruction(TranslatedInstructionMasterDto dto) throws ApplicationException {
		try{			
						TranslatedInstructionMaster master=new TranslatedInstructionMaster();
						master.setConvertedText(dto.getConvertedText()); 
						master.setDesc(dto.getDesc()); 
						master.setInstructionId(dto.getInstructionId()); 
						master.setLanguageId(dto.getLanguageId()); 
						master.setCreatedBy(dto.getCreatedBy()); 
						master.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"yyyy-M-dd")); 
						master.setUpdatedBy(dto.getUpdatedBy()); 
						master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "yyyy-M-dd")); 
						master.setOrganizationId(dto.getOrganizationId()); 
						master.setUnitId(dto.getUnitId()); 
						master.setStatus('A'); 
				
						sessionFactory.getCurrentSession().save(master);
					
				}catch (HibernateException he) {
				logger.error("updateTranslatedInstruction failed", he);
				} catch (Exception e) {
				logger.error("updateTranslatedInstruction failed", e);
				}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response inactiveTranslatedInstruction(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try{			
			sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_TRANSLATED_INSTRUCTION")
					.setCharacter("status", 'I')
					.setInteger("organizationId", instructionMasterDto.getOrganizationId())
					.setInteger("unitId", instructionMasterDto.getUnitId())
					.setInteger("instructionId", instructionMasterDto.getInstructionId())
					.executeUpdate();
		}catch (HibernateException he) {
		logger.error("inactiveTranslatedInstruction failed", he);
		} catch (Exception e) {
		logger.error("inactiveTranslatedInstruction failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getConvertedLanguageListById(InstructionMasterDto instructionMasterDto)
			throws ApplicationException {
		try {
			List<TranslatedInstructionMasterDto> translatedInstructionList = sessionFactory.getCurrentSession()
					.createSQLQuery("select tran.instruction_desc as \"desc\", tran.converted_text as \"convertedText\" from public.t_translated_instruction tran " 
							+" WHERE tran.status='A'AND tran.instruction_id=:instructionId "
							+" AND tran.organization_id=:orgId ")
					 .setInteger("orgId", instructionMasterDto.getOrganizationId())
					 .setInteger("instructionId",instructionMasterDto.getInstructionId())
					.setResultTransformer(Transformers.aliasToBean(TranslatedInstructionMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, translatedInstructionList, null);
		} catch (HibernateException he) {
		logger.error("getConvertedLanguageListById failed", he);
		} catch (Exception e) {
		logger.error("getConvertedLanguageListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSearchedInstructionList(InstructionMasterDto instructionMasterDto) throws ApplicationException {
		try {
			if(instructionMasterDto.getSpecialityId()!=null) {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT distinct sm.speciality_id as \"specialityId\", " 
							+" sm.speciality_name as \"specialityName\","
							+" ins.instruction_id as\"instructionId\",ins.instruction_code as\"instructionCode\", "
							+" ins.instruction_desc as\"instructionDesc\", "
							+" dsm.instuction_dept_speciality_mapper_id  as \"instuctionDeptSpecialityMapperId\" "
							+" from public.m_instruction  ins "
							+" join public.m_instuction_dept_speciality_mapper dsm on ins.instruction_id=dsm.instruction_id"
							+" join doctor.m_speciality_master sm on sm.speciality_id = dsm.dept_speciality_id  " 
							+" WHERE dsm.status='A' "
							+ " AND ins.organization_id=:orgId "
							+" AND dsm.unit_id=:unitId "
							+ " AND sm.speciality_id =:specialityId "
							+ " AND ins.status='A'"
							+ " and lower(ins.instruction_desc) like '%"+instructionMasterDto.getSearchInstruction()+"%'")
					 .setInteger("orgId", instructionMasterDto.getOrganizationId())
					 .setInteger("unitId",instructionMasterDto.getUnitId())
					 .setInteger("specialityId",instructionMasterDto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(InstructionMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
			}
		} catch (HibernateException he) {
		logger.error("getSearchedInstructionList failed", he);
		} catch (Exception e) {
		logger.error("getSearchedInstructionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMostFrequentlyUsedInstructions(InstructionMasterDto instructionMasterDto)
			throws ApplicationException {
		try {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("select " + 
							"	coversheet0_.instruction_details_id as \"instructionDetailsId\", " + 
							"	instructio1_.instruction_desc as \"instructionDesc\", "
							+ " coversheet0_.adviced_instruction as \"advicedInstruction\"," + 
							"	count(*) as \"adviceCount\" " + 
							" from " + 
							"	opd.t_instruction coversheet0_ " + 
							" left join public.m_instruction instructio1_ on " + 
							"	coversheet0_.instruction_details_id = instructio1_.instruction_id " + 
							" where " + 
							"	coversheet0_.organization_id = :orgId " + 
							"	and coversheet0_.unit_id = :unitId "
							+ "  AND coversheet0_.status!='D'"
							+ "  AND (coversheet0_.instruction_details_id is not null or (coversheet0_.adviced_instruction is not null and coversheet0_.adviced_instruction !='')) " + 
							" group by  " + 
							"	coversheet0_.instruction_details_id , " + 
							"	coversheet0_.adviced_instruction, "
							+ " instructio1_.instruction_desc  "+
							"order by " + 
							"  count(*) desc  limit 10 ")
					 .setInteger("orgId", instructionMasterDto.getOrganizationId())
					 .setInteger("unitId",instructionMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
			
		} catch (HibernateException he) {
		logger.error("getMostFrequentlyUsedInstructions failed", he);
		} catch (Exception e) {
		logger.error("getMostFrequentlyUsedInstructions failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSearchedFreeTextInstructionList(InstructionMasterDto instructionMasterDto)
			throws ApplicationException {
		try {
			List<InstructionMasterDto> inspectionMasterDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("select " + 
//							"	coversheet0_.instruction_id as \"instructionId\", " + 
							"	distinct coversheet0_.adviced_instruction as \"advicedInstruction\" " + 
							"from " + 
							"	opd.t_instruction coversheet0_ " + 
							"left outer join public.m_instruction instructio1_ on " + 
							"	coversheet0_.instruction_details_id = instructio1_.instruction_id " + 
							"where " 
//							"	coversheet0_.status <> 'D' " + 
//							"	and"
							+ " coversheet0_.organization_id =:orgId " + 
							"	and coversheet0_.unit_id =:unitId " + 
							"	and ((lower(coversheet0_.adviced_instruction) like '%"+instructionMasterDto.getSearchInstruction()+"%' ) " + 
							"	or ((coversheet0_.adviced_instruction) like '%"+instructionMasterDto.getSearchInstruction()+"%' )) " 
//							"order by " + 
//							"	coversheet0_.instruction_id asc " + 
							)
					 .setInteger("orgId", instructionMasterDto.getOrganizationId())
					 .setInteger("unitId",instructionMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CoversheetInstructionDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, inspectionMasterDtoList, null);
			
		} catch (HibernateException he) {
		logger.error("getSearchedFreeTextInstructionList failed", he);
		} catch (Exception e) {
		logger.error("getSearchedFreeTextInstructionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
