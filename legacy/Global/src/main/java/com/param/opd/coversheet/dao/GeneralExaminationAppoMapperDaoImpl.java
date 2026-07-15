package com.param.opd.coversheet.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.SystemObservationMasterDto;
import com.param.global.model.SystemMaster;
import com.param.global.model.SystemObervationMaster;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.ExaminationMapperDto;
import com.param.opd.coversheet.dto.GeneralExaminationAppoMapperDto;
import com.param.opd.coversheet.dto.ListOfGeneralExamSystemMaster;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.model.GeneralExaminationAppoMapper;
import com.param.opd.coversheet.service.IGeneralExaminationAppoMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class GeneralExaminationAppoMapperDaoImpl extends GenericDao<GeneralExaminationAppoMapper, Integer> implements
		IGeneralExaminationAppoMapperDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(GeneralExaminationAppoMapperDaoImpl.class);


	@Autowired
	private IGeneralExaminationAppoMapperService iGeneralExaminationAppoMapperService;
	
	public GeneralExaminationAppoMapperDaoImpl() {
		super(GeneralExaminationAppoMapper.class);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Response getListOfGeneralExaminationMapper(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<GeneralExaminationAppoMapperDto> listGeneralExaminationAppoMapperDto = statlessSession.getNamedQuery("GET_GENERAL_EXAM_LIST")
					.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
					.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
					.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
					.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
					.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listGeneralExaminationAppoMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfGeneralExaminationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	@Transactional
	public Response saveGeneralExamSystemProperty(GeneralExaminationAppoMapper generalExamination)
			throws ApplicationException {
		try
		{
			GeneralExaminationAppoMapper propertyId = save(generalExamination);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}
		catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("saveGeneralExamSystemProperty failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	@Transactional
	public Response saveGeneralExaminationObservationMapper(GeneralExaminationAppoMapper generalExamination)
			throws ApplicationException {
		try
		{
			GeneralExaminationAppoMapper observationId = save(generalExamination);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}
		catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("saveGeneralExaminationObservationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	@Transactional
	public Response saveGeneralExaminationSystem(GeneralExaminationAppoMapper generalExamination)throws ApplicationException {
		// TODO Auto-generated method stub
		try
		{
			GeneralExaminationAppoMapper observationId = save(generalExamination);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}
		catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("saveGeneralExaminationSystem failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getListOfSavedGeneralExaminationMapper(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		try {
				  Session statlessSession = sessionFactory.getCurrentSession();
		
				  List<GeneralExaminationAppoMapperDto> listGeneralExaminationAppoMapperDto =
				  statlessSession.getNamedQuery("GET_SAVED_GENERAL_EXAM_LIST")
//				  .setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
				  .setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
				  .setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
				  .setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
				  .setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class))
				  .list();
			  
				  List<SystemMaster> listSystemMaster = statlessSession
				  .createQuery("from SystemMaster p where unit_id=:unitId AND organization_id=:organizationId AND status = 'A' and (typeId=:typeId OR typeId=3) AND (genderId=:genderId OR genderId=4)")
				  .setParameter("genderId", generalExaminationAppoMapperDto.getGenderId())
				  .setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
				  .setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
				  .setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId()).list();

				  for (int i = 0; i < listSystemMaster.size(); i++) 
				  {
					for(GeneralExaminationAppoMapperDto dto : listGeneralExaminationAppoMapperDto){
						if(dto.getSystemId() == listSystemMaster.get(i).getSystemId())
							{
								listSystemMaster.get(i).setSystemFlag(true);
								listSystemMaster.get(i).setSystemNADFlag(dto.getIsNADValue());
								break;
							}
					}
				
					for (int j = 0; j < listSystemMaster.get(i).getSystemObervationMasterList().size(); j++) 
					{
						for(GeneralExaminationAppoMapperDto dto : listGeneralExaminationAppoMapperDto){
							if (dto.getSystemObervationId() == listSystemMaster.get(i).getSystemObervationMasterList().get(j).getObservationId()) 
								{
									listSystemMaster.get(i).getSystemObervationMasterList().get(j).setSystemObservationFlag(true);
									listSystemMaster.get(i).getSystemObervationMasterList().get(j).setSystemObervationRemark(dto.getRemark());
									break;
								}
						}
						//ONE WAY TRASECTION 
						listSystemMaster.get(i).getSystemObervationMasterList().get(j).setSystemMaster(null);
					
						for (int k = 0; k < listSystemMaster.get(i).getSystemObervationMasterList().get(j).getSystemObervationPropertyMasterList().size(); k++) 
						{
							for(GeneralExaminationAppoMapperDto dto : listGeneralExaminationAppoMapperDto){
								if (dto.getSystemObervationPropertyId() == listSystemMaster.get(i).getSystemObervationMasterList().get(j).getSystemObervationPropertyMasterList().get(k).getPropertyId()) 
									{
										listSystemMaster.get(i).getSystemObervationMasterList().get(j).getSystemObervationPropertyMasterList().get(k).setSystemObservationPropertyFlag(true);
										listSystemMaster.get(i).getSystemObervationMasterList().get(j).getSystemObervationPropertyMasterList().get(k).setSystemObservationPropertyRemark(dto.getRemark());
										break;
									}
							}
							
						}
					}
				  }
			return new Response(SUCCESS, SUCCESS_CODE, null, listSystemMaster, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getListOfSavedGeneralExaminationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getListOfSystemsAndObservation(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		try {
			List<GeneralExaminationAppoMapperDto> list2=new ArrayList<>();
			List<GeneralExaminationAppoMapperDto> list=sessionFactory.getCurrentSession()
				.createSQLQuery(" select" 
						+"	distinct(systemMaster.system_id) as \"systemId\" ,"  
						+"	sysMppr.gender_id as \"genderId\" ,"  
						+"	systemMaster.organization_id as \"organizationId\" ,"  
						+"	systemMaster.status as \"status\" ," 
						+"	systemMaster.system_code as \"systemCode\" ," 
						+"	systemMaster.system_name as \"systemName\" ,"
						+ " to_char(systemMaster.created_date,'DD/MM/YYYY')  as \"createdDate\", "
						+"	systemMaster.type_id as \"typeId\" ," 
						+"	systemMaster.unit_id as \"unitId\" " 
						+" from" 
						+"	emr.m_system_master systemMaster "
						+ " left join emr.m_system_master_gender_mapper sysMppr on sysMppr.system_id=systemMaster.system_id" 
						+" where systemMaster.unit_id =:unitId" 
						+"	and systemMaster.organization_id =:organizationId" 
						+"	and systemMaster.status = 'A'" 
						+"	and(systemMaster.type_id =:typeId "
						+ "or systemMaster.type_id = 3 )"
//						+ ")"  
						+"	and(sysMppr.gender_id =:genderId "
//						+ "or sysMppr.gender_id = 3" 
						+"	) ")
				.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
				.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
				.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
				.setParameter("genderId", generalExaminationAppoMapperDto.getGenderId())
				.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			
			for(GeneralExaminationAppoMapperDto dto : list) 
			{
				
					List<GeneralExaminationAppoMapperDto> list1=sessionFactory.getCurrentSession()
							.createSQLQuery(" select" + 
								"	systemObsMaster.system_id as \"systemId\" ," + 
								"	systemObsMaster.observation_id as \"observationId\" ," + 
								"	systemObsMaster.is_property_required as \"isPropertyRequired\" ," + 
								"	systemObsMaster.observation_code as \"observationCode\" ," + 
								"	systemObsMaster.observation_name as \"observationName\" ," + 
								"	systemObsMaster.speciality_ids as \"specialityId\" ," +
								"	systemObsMaster.organization_id as \"organizationId\" ," +
								"	systemObsMaster.status as \"status\" ," + 
								"	systemObsMaster.unit_id as \"unitId\" " + 
								" from" + 
								"	emr.m_system_observation_master systemObsMaster" + 
								" where" + 
								"	systemObsMaster.system_id =:systemId "
								+ " and systemObsMaster.status ='A'"
								+ " and systemObsMaster.organization_id =:organizationId"
								+ " and systemObsMaster.unit_id =:unitId")
						.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
						.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
						.setParameter("systemId", dto.getSystemId())
						.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();


					List<GeneralExaminationAppoMapperDto> listOfObservationMasterBySpecialityId = new ArrayList<>();

					boolean isSpecialityIdPresent = false;
					for(GeneralExaminationAppoMapperDto item : list1){
						if(item.getSpecialityId()!= null && item.getSpecialityId()!=""){
							String[] strArray = item.getSpecialityId().split(",");

							for(String id: strArray){
								if(id.equals(generalExaminationAppoMapperDto.getSpecialityId())){
									isSpecialityIdPresent = true;
									break;
								}
							}
						}

						if(isSpecialityIdPresent == true){
							listOfObservationMasterBySpecialityId.add(item);
						}
						isSpecialityIdPresent = false;
					}


					for(GeneralExaminationAppoMapperDto dto1 : listOfObservationMasterBySpecialityId)
					{

							List<GeneralExaminationAppoMapperDto> list3=sessionFactory.getCurrentSession()
									.createSQLQuery("select" + 
										"	systemObsMaster.observation_id as \"observationId\" ," + 
										"	systemObsMaster.property_id as \"propertyId\" ," + 
										"	systemObsMaster.organization_id as \"organizationId\" ," + 
										"	systemObsMaster.property_name as \"propertyName\" ," + 
										"	systemObsMaster.status as \"status\" ," + 
										"	systemObsMaster.unit_id as \"unitId\"  " + 
										" from" + 
										"	emr.m_system_observation_property_master systemObsMaster" + 
										" where" + 
										"	systemObsMaster.observation_id =:observationId"
										+ " and systemObsMaster.status ='A'"
										+ " and systemObsMaster.organization_id =:organizationId"
										+ " and systemObsMaster.unit_id =:unitId")
								.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
								.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
								.setParameter("observationId", dto1.getObservationId())
								.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
							dto1.setSystemObervationMasterList(list3);
						//	list1.add(dto1);
						
						
					}
					dto.setGeneralExaminationAppoMapperList(listOfObservationMasterBySpecialityId);
					//list.add(dto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getListOfSystemsAndObservation failed", he);
	} catch (Exception e) {
	logger.error("getListOfSystemsAndObservation failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}

	@Override
	public Response getListOfSystemsAndObservationForCaseSheet(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		try {
			List<GeneralExaminationAppoMapperDto> list2=new ArrayList<>();
			List<GeneralExaminationAppoMapperDto> list=sessionFactory.getCurrentSession()
				.createSQLQuery("select" + 
						"	distinct(generalexam.system_id) as \"systemId\"," + 
						"	systemMst.system_name as \"systemName\"," + 
						"	generalexam.is_nad_status as \"isNADValue\"," + 
						"	generalexam.encounter_id as \"encounterId\"," + 
						"	generalexam.admission_id as \"admissionId\", " +
						"  to_char(generalexam.created_date, 'DD/MM/YYYY') as \"createdDate\", "+
						"	generalexam.status as \"status\""  + 
						" from" + 
						"	patient.t_general_examination_appo_mapper generalexam" + 
						" INNER join emr.m_system_master systemMst on" + 
						"	generalexam.system_id = systemMst.system_id" + 
						" where" + 
						"	generalexam.patient_id =:patientId" + 
						"	and generalexam.organization_id =:organizationId " + 
						" and generalexam.status ='A'"+
						"	and generalexam.type_id =:typeId")
				.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
				.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
				.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
				.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			
			for(GeneralExaminationAppoMapperDto dto : list) 
			{
				
					List<GeneralExaminationAppoMapperDto> list1=sessionFactory.getCurrentSession()
							.createSQLQuery("select" + 
									"	distinct(generalexam.system_obervation_id) as \"systemObervationId\"," + 
									"	syetemObserv.observation_name as \"observationName\"," + 
									"	generalexam.is_nad_status as \"isNADValue\","
									+ " generalexam.system_obervation_property_id as \"systemObervationPropertyId\" ," + 
									"	generalexam.remark as \"remarkObs\","+
									"	generalexam.status as \"status\" " + 
									" from" + 
									"	patient.t_general_examination_appo_mapper generalexam" + 
									" inner join emr.m_system_observation_master syetemObserv on" + 
									"	generalexam.system_obervation_id = syetemObserv.observation_id" + 
									" where" + 
									"	generalexam.patient_id =:patientId" + 
									" and generalexam.status ='A'"+
									"	and generalexam.organization_id =:organizationId" + 
									"	and generalexam.system_id =:systemId")
						.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
						.setParameter("systemId", dto.getSystemId())
						.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
					
				
					for(GeneralExaminationAppoMapperDto dto1 : list1) 
					{
						
							List<GeneralExaminationAppoMapperDto> list3=sessionFactory.getCurrentSession()
									.createSQLQuery("select" + 
											"	distinct(generalexam.system_obervation_property_id) as \"systemObervationPropertyId\"," + 
											"	systemObeserv.property_name as \"propertyName\"," + 
											"	generalexam.is_nad_status as \"isNADValue\"," + 
											"	generalexam.remark as \"remark\","+
//											+ " generalexam.descriptive_examination as \"descriptiveExamination\", " + 
											"	generalexam.status as \"status\" " + 
											" from" + 
											"	patient.t_general_examination_appo_mapper generalexam" + 
											" inner join emr.m_system_observation_property_master systemObeserv on" + 
											"	generalexam.system_obervation_property_id = systemObeserv.property_id" + 
											" where" + 
											"	generalexam.patient_id =:patientId" + 
											" and generalexam.status ='A'"+
											"	and generalexam.organization_id =:organizationId" + 
											"	and generalexam.system_obervation_id =:systemObervationId")
								.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
								.setParameter("systemObervationId", dto1.getSystemObervationId())
								.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
								.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
							dto1.setSystemObervationMasterList(list3);
					}
					dto.setGeneralExaminationAppoMapperList(list1);
					
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getListOfSystemsAndObservationForCaseSheet failed", he);
	} catch (Exception e) {
	logger.error("getListOfSystemsAndObservationForCaseSheet failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}


	@Override
	public Response updateGeneralExaminationMapper(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		 try {
				
				Session session=sessionFactory.getCurrentSession();
				Integer result=session.createSQLQuery(" update patient.t_general_examination_appo_mapper  set status = 'I'"
				+ " where patient_id="+generalExaminationAppoMapperDto.getPatientId()
				+ " and type_id="+generalExaminationAppoMapperDto.getTypeId()
				+ " and status='A'")
				.executeUpdate();
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
				
				} catch (HibernateException he) {
				logger.error("updateGeneralExaminationMapper failed", he);
				} catch (Exception e) {
				logger.error("updateGeneralExaminationMapper failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


	@Override
	public Response checkGeneralExaminationMapperAvailable(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<GeneralExaminationAppoMapperDto> listGeneralExaminationAppoMapperDto = statlessSession.getNamedQuery("CHECK_GENERAL_EXAMINATION_MAPPER_AVAILABLE")
					.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
					.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
					.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
					.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listGeneralExaminationAppoMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("checkGeneralExaminationMapperAvailable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getListOfSystemsAndObservationForBookView(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try {
			List<GeneralExaminationAppoMapperDto> list2=new ArrayList<>();
			List<GeneralExaminationAppoMapperDto> list=sessionFactory.getCurrentSession()
				.createSQLQuery("select" + 
						"	distinct(generalexam.system_id) as \"systemId\"," + 
						"	systemMst.system_name as \"systemName\"," + 
						"	generalexam.is_nad_status as \"isNADValue\"," + 
						" generalexam.descriptive_examination as \"descriptiveExamination\", " + 
						"	generalexam.status as \"status\""  + 
						" from" + 
						"	patient.t_general_examination_appo_mapper generalexam" + 
						" INNER join emr.m_system_master systemMst on" + 
						"	generalexam.system_id = systemMst.system_id" + 
						" where" + 
						"	generalexam.patient_id =:patientId" + 
						"	and generalexam.organization_id =:organizationId " + 
						" and generalexam.status ='A'"+
						"	and generalexam.type_id =:typeId"
						+ " AND to_char(generalexam.created_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
						+ " AND :toDate ")
				.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
				.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
				.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
				.setParameter("fromDate", generalExaminationAppoMapperDto.getFromDate())
				.setParameter("toDate", generalExaminationAppoMapperDto.getToDate())
				.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			
			for(GeneralExaminationAppoMapperDto dto : list) 
			{
				
					List<GeneralExaminationAppoMapperDto> list1=sessionFactory.getCurrentSession()
							.createSQLQuery("select" + 
									"	distinct(generalexam.system_obervation_id) as \"systemObervationId\"," + 
									"	syetemObserv.observation_name as \"observationName\"," + 
									"	generalexam.is_nad_status as \"isNADValue\","
									+ " generalexam.system_obervation_property_id as \"systemObervationPropertyId\" ," + 
									"	generalexam.remark as \"remarkObs\","+
									"	generalexam.status as \"status\" " + 
									" from" + 
									"	patient.t_general_examination_appo_mapper generalexam" + 
									" inner join emr.m_system_observation_master syetemObserv on" + 
									"	generalexam.system_obervation_id = syetemObserv.observation_id" + 
									" where" + 
									"	generalexam.patient_id =:patientId" + 
									" and generalexam.status ='A'"+
									"	and generalexam.organization_id =:organizationId" + 
									"	and generalexam.system_id =:systemId"
									+ " AND to_char(generalexam.created_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
									+ " AND :toDate ")
						.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
						.setParameter("systemId", dto.getSystemId())
						.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
						.setParameter("fromDate", generalExaminationAppoMapperDto.getFromDate())
						.setParameter("toDate", generalExaminationAppoMapperDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
					
				
					for(GeneralExaminationAppoMapperDto dto1 : list1) 
					{
						
							List<GeneralExaminationAppoMapperDto> list3=sessionFactory.getCurrentSession()
									.createSQLQuery("select" + 
											"	distinct(generalexam.system_obervation_property_id) as \"systemObervationPropertyId\"," + 
											"	systemObeserv.property_name as \"propertyName\"," + 
											"	generalexam.is_nad_status as \"isNADValue\"," + 
											"	generalexam.remark as \"remark\","+
//											+ " generalexam.descriptive_examination as \"descriptiveExamination\", " + 
											"	generalexam.status as \"status\" " + 
											" from" + 
											"	patient.t_general_examination_appo_mapper generalexam" + 
											" inner join emr.m_system_observation_property_master systemObeserv on" + 
											"	generalexam.system_obervation_property_id = systemObeserv.property_id" + 
											" where" + 
											"	generalexam.patient_id =:patientId" + 
											" and generalexam.status ='A'"+
											"	and generalexam.organization_id =:organizationId" + 
											"	and generalexam.system_obervation_id =:systemObervationId"
											+ " AND to_char(generalexam.created_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
											+ " AND :toDate ")
								.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
								.setParameter("systemObervationId", dto1.getSystemObervationId())
								.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
								.setParameter("fromDate", generalExaminationAppoMapperDto.getFromDate())
								.setParameter("toDate", generalExaminationAppoMapperDto.getToDate())
								.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
							dto1.setSystemObervationMasterList(list3);
					}
					dto.setGeneralExaminationAppoMapperList(list1);
					
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getListOfSystemsAndObservationForBookView failed", he);
	} catch (Exception e) {
	logger.error("getListOfSystemsAndObservationForBookView failed", e);
	}
	return new Response(ERROR, null, null, null, null);
}


	@Override
	public Response getGeneralAndSystemicExaminationMapperList(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try {
			String query = "	select " 
					+ "	distinct(msm.system_id) as \"systemId\" , "
					+ "	msm.type_id as \"typeId\" , " 
					+ "	msm.system_name as \"systemName\", "
					+ "	systmObsMst.observation_id as \"systemObervationId\" , "
//					+ "	systmObsMst.is_property_required as \"isPropertyRequired\" , "
//					+ "	systmObsMst.observation_code as \"observationCode\" , "
					+ "	systmObsMst.observation_id as \"observationId\"  , "
					+ "	systmObsMst.observation_name as \"observationName\" , "
//					+ "	systmObsMst.organization_id as \"organizationId\" , "
					+ "	systmObsMst.status as \"status\"  " 
//					+ "	systmObsMst.unit_id as \"unitId\", "
//					+ "	sysObsrProMst.property_id as \"systemObervationPropertyId\" , "
//					+ "	sysObsrProMst.property_name as \"propertyName\" " 
					+ " from "
					+ "	emr.m_system_master msm " 
					+ " left join emr.m_system_observation_master systmObsMst on "
					+ "	systmObsMst.system_id = msm.system_id " 
					+ "	and systmObsMst.status = 'A' "
					+ " left join emr.m_system_observation_property_master sysObsrProMst on "
					+ "	sysObsrProMst.observation_id = systmObsMst.observation_id "
					+ "	and sysObsrProMst.status = 'A' " 
					+ " where " 
					+ "	msm.status = 'A' "
					+ "	and msm.organization_id =:organizationId " 
					+ "	and msm.unit_id =:unitId "
					+ "	and msm.type_id =:typeId";
			List<GeneralExaminationAppoMapperDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
					.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
					.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
		logger.error("getGeneralAndSystemicExaminationMapperList failed", he);
		} catch (Exception e) {
		logger.error("getGeneralAndSystemicExaminationMapperList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response saveGeneralAndSystemicExaminationMapperForNewCoversheet(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try
		{if(generalExaminationAppoMapperDto.getGeneralExamAppoMapperId() == null) {
			GeneralExaminationAppoMapper genExam = new GeneralExaminationAppoMapper();
			genExam.setEncounterId(generalExaminationAppoMapperDto.getEncounterId());
			genExam.setAdmissionId(generalExaminationAppoMapperDto.getAdmissionId());
			genExam.setCreatedBy(generalExaminationAppoMapperDto.getCreatedBy());
			genExam.setPatientId(generalExaminationAppoMapperDto.getPatientId());
			genExam.setDescriptiveExamination(generalExaminationAppoMapperDto.getDescriptiveExamination());
			genExam.setIsNADValue(generalExaminationAppoMapperDto.getIsNADValue());
			genExam.setOrganizationId(generalExaminationAppoMapperDto.getOrganizationId());
			genExam.setRemark(generalExaminationAppoMapperDto.getRemark());
			genExam.setStatus(generalExaminationAppoMapperDto.getStatus());
			genExam.setSystemId(generalExaminationAppoMapperDto.getSystemId());
			genExam.setSystemObervationPropertyId(generalExaminationAppoMapperDto.getSystemObervationPropertyId());
			genExam.setSystemObervationId(generalExaminationAppoMapperDto.getSystemObervationId());
			genExam.setTypeId(generalExaminationAppoMapperDto.getTypeId());
			genExam.setUnitId(generalExaminationAppoMapperDto.getUnitId());
			genExam.setUpdatedBy(generalExaminationAppoMapperDto.getUpdatedBy());
			genExam.setUpdatedDate(GlobalCommonDateUtils.getDate(generalExaminationAppoMapperDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			genExam.setCreatedDate(GlobalCommonDateUtils.getDate(generalExaminationAppoMapperDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			save(genExam);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, genExam);
			}else {
			GeneralExaminationAppoMapper genExam = findById(generalExaminationAppoMapperDto.getGeneralExamAppoMapperId());
			genExam.setEncounterId(generalExaminationAppoMapperDto.getEncounterId());
			genExam.setAdmissionId(generalExaminationAppoMapperDto.getAdmissionId());
			genExam.setCreatedBy(generalExaminationAppoMapperDto.getCreatedBy());
			genExam.setPatientId(generalExaminationAppoMapperDto.getPatientId());
			genExam.setDescriptiveExamination(generalExaminationAppoMapperDto.getDescriptiveExamination());
			genExam.setIsNADValue(generalExaminationAppoMapperDto.getIsNADValue());
			genExam.setOrganizationId(generalExaminationAppoMapperDto.getOrganizationId());
			genExam.setRemark(generalExaminationAppoMapperDto.getRemark());
			genExam.setStatus(generalExaminationAppoMapperDto.getStatus());
			genExam.setSystemId(generalExaminationAppoMapperDto.getSystemId());
			genExam.setSystemObervationPropertyId(generalExaminationAppoMapperDto.getSystemObervationPropertyId());
			genExam.setSystemObervationId(generalExaminationAppoMapperDto.getSystemObervationId());
			genExam.setTypeId(generalExaminationAppoMapperDto.getTypeId());
			genExam.setUnitId(generalExaminationAppoMapperDto.getUnitId());
			genExam.setUpdatedBy(generalExaminationAppoMapperDto.getUpdatedBy());
			genExam.setUpdatedDate(GlobalCommonDateUtils.getDate(generalExaminationAppoMapperDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			genExam.setCreatedDate(GlobalCommonDateUtils.getDate(generalExaminationAppoMapperDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			update(genExam);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		}
		catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("saveGeneralAndSystemicExaminationMapperForNewCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getGenAndSysExaminMapperPatDetailstForNewCov(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try {

			String query = "select " + " generalExam.general_exam_appo_mapper_id as \"generalExamAppoMapperId\", "
					+ " generalExam.system_id as \"systemId\", "
					+ " generalExam.descriptive_examination as \"descriptiveExamination\", "
					+ " sysMst.system_name as \"systemName\", "
					+ " generalExam.system_obervation_id as \"systemObervationId\", "
					+ " systmObsMst.observation_name as \"observationName\", "
					+ " generalExam.system_obervation_property_id as \"systemObervationPropertyId\", "
					+ " sysObsrProMst.property_name as \"propertyName\", "
					+ " generalExam.is_nad_status as \"isNADValue\", " + " generalExam.remark as \"remark\", "
					+ " generalExam.status as \"status\", " + " generalExam.encounter_id as \"encounterId\", "
					+ " generalExam.patient_id as \"patientId\", " + " generalExam.unit_id as \"unitId\", "
					+ " generalExam.organization_id as \"organizationId\", "
					+ " generalExam.created_by as \"createdBy\", " + " generalExam.updated_by as \"updatedBy\", "
					+ " to_char(generalExam.created_date , 'DD-MM-YYYY')as \"createdDate\", "
					+ " to_char(generalExam.updated_date , 'DD-MM-YYYY') as \"updatedDate\" " + "from "
					+ " patient.t_general_examination_appo_mapper generalExam "
					+ "left join emr.m_system_observation_master systmObsMst on "
					+ " systmObsMst.observation_id = generalExam.system_obervation_id "
					+ "left join emr.m_system_observation_property_master sysObsrProMst on "
					+ " sysObsrProMst.property_id = generalExam.system_obervation_property_id "
					+ "left join emr.m_system_master sysMst on " + " sysMst.system_id = generalExam.system_id "
					+ "where " + " generalExam.unit_id =:unitId " + " and generalExam.patient_id =:patientId "
					+ " and generalExam.status = 'A' " + " and generalExam.organization_id =:organizationId "
					+ " and generalExam.type_id =:typeId"
					+ " order by generalExam.general_exam_appo_mapper_id desc";
			List<GeneralExaminationAppoMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
					.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
					.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
					.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {
		logger.error("getGenAndSysExaminMapperPatDetailstForNewCov failed", he);
		} catch (Exception e) {
		logger.error("getGenAndSysExaminMapperPatDetailstForNewCov failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response removeGenAndSysExaminMapperPatDetailstForNewCov(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try
		{
				GeneralExaminationAppoMapper genExam = findById(generalExaminationAppoMapperDto.getGeneralExamAppoMapperId());
				genExam.setStatus(generalExaminationAppoMapperDto.getStatus());
				genExam.setUnitId(generalExaminationAppoMapperDto.getUnitId());
				genExam.setUpdatedBy(generalExaminationAppoMapperDto.getUpdatedBy());
				genExam.setUpdatedDate(GlobalCommonDateUtils.getDate(generalExaminationAppoMapperDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				update(genExam);
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			
			
			
		}
		catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("removeGenAndSysExaminMapperPatDetailstForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response updateDescriptiveExamForNewCov(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		try
		{
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update patient.t_general_examination_appo_mapper  set descriptive_examination = '"+generalExaminationAppoMapperDto.getDescriptiveExamination()+"'"
			+ " where patient_id="+generalExaminationAppoMapperDto.getPatientId()
			+ " and status='A'")
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			
		}
		catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateDescriptiveExamForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response checkAvailabelGeneralAndSystemicExam(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		try {
			String query = "	select " + 
					"	generalExam.general_exam_appo_mapper_id as generalExamAppoMapperId, " + 
					"	generalExam.system_id as systemId, " + 
					"	generalExam.descriptive_examination as descriptiveExamination, " + 
					"	sysMst.system_name as systemName, " + 
					"	generalExam.remark as remarkObs , " + 
					"	generalExam.system_obervation_id as systemObervationId, " + 
					"	systmObsMst.observation_name as observationName, " + 
					"	generalExam.system_obervation_property_id as systemObervationPropertyId, " + 
					"	sysObsrProMst.property_name as propertyName, " + 
					"	generalExam.is_nad_status as isNADValue, " + 
					"	generalExam.remark as remark, " + 
					"	generalExam.status as status, " + 
					"	generalExam.encounter_id as encounterId, " + 
					"	generalExam.patient_id as patientId, " + 
					"	generalExam.unit_id as unitId, " + 
					"	generalExam.organization_id as organizationId, " + 
					"	generalExam.created_by as createdBy, " + 
					"	generalExam.updated_by as updatedBy, " + 
					"	to_char(generalExam.created_date , 'DD-MM-YYYY')as createdDate, " + 
					"	to_char(generalExam.updated_date , 'DD-MM-YYYY') as updatedDate " + 
					" from " + 
					"	patient.t_general_examination_appo_mapper generalExam " + 
					" left join emr.m_system_observation_master systmObsMst on " + 
					"	systmObsMst.observation_id = generalExam.system_obervation_id " + 
					" left join emr.m_system_observation_property_master sysObsrProMst on " + 
					"	sysObsrProMst.property_id = generalExam.system_obervation_property_id " + 
					" left join emr.m_system_master sysMst on " + 
					"	sysMst.system_id = generalExam.system_id " + 
					" where " + 
					"	generalExam.unit_id =:unitId " + 
					"	and generalExam.patient_id =:patientId " + 
					"	and generalExam.status = 'A' " + 
					"	and generalExam.organization_id =:organizationId " + 
					"	and generalExam.type_id =:typeId "
					+ " order by generalExam.general_exam_appo_mapper_id desc";
			List<GeneralExaminationAppoMapperDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
					.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
					.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
		logger.error("checkAvailabelGeneralAndSystemicExam failed", he);
		} catch (Exception e) {
		logger.error("checkAvailabelGeneralAndSystemicExam failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response checkGenAndSystExamMapprAvailable(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try {
			String query = "	select " + 
					"	generalExam.general_exam_appo_mapper_id as \"generalExamAppoMapperId\", " + 
					"	generalExam.system_id as \"systemId\", " + 
					"	generalExam.descriptive_examination as \"descriptiveExamination\", " + 
					"	sysMst.system_name as \"systemName\", " + 
					"	generalExam.remark as \"remarkObs\" , " + 
					"	generalExam.system_obervation_id as \"systemObervationId\", " + 
					"	systmObsMst.observation_name as \"observationName\", " + 
					"	generalExam.system_obervation_property_id as \"systemObervationPropertyId\", " + 
					"	sysObsrProMst.property_name as \"propertyName\", " + 
					"	generalExam.is_nad_status as \"isNADValue\", " + 
					"	generalExam.remark as \"remark\", " + 
					"	generalExam.status as \"status\", " + 
					"	generalExam.encounter_id as \"encounterId\", " + 
					"	generalExam.patient_id as \"patientId\", " + 
					"	generalExam.unit_id as \"unitId\", " + 
					"	generalExam.organization_id as \"organizationId\", " + 
					"	generalExam.created_by as \"createdBy\", " + 
					"	generalExam.updated_by as \"updatedBy\", " + 
					"	to_char(generalExam.created_date , 'DD-MM-YYYY')as \"createdDate\", " + 
					"	to_char(generalExam.updated_date , 'DD-MM-YYYY') as \"updatedDate\" " + 
					" from " + 
					"	patient.t_general_examination_appo_mapper generalExam " + 
					" left join emr.m_system_observation_master systmObsMst on " + 
					"	systmObsMst.observation_id = generalExam.system_obervation_id " + 
					" left join emr.m_system_observation_property_master sysObsrProMst on " + 
					"	sysObsrProMst.property_id = generalExam.system_obervation_property_id " + 
					" left join emr.m_system_master sysMst on " + 
					"	sysMst.system_id = generalExam.system_id " + 
					" where " + 
					"	generalExam.unit_id =:unitId " + 
					"	and generalExam.patient_id =:patientId " + 
					"	and generalExam.status = 'A' " + 
					"	and generalExam.organization_id =:organizationId " + 
					"	and generalExam.type_id =:typeId "
					+ " and generalExam.system_obervation_id =:observationId"
					+ " order by generalExam.general_exam_appo_mapper_id desc";
			List<GeneralExaminationAppoMapperDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
					.setParameter("observationId", generalExaminationAppoMapperDto.getObservationId())
					.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
					.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
					.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
		logger.error("checkGenAndSystExamMapprAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkGenAndSystExamMapprAvailable failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getExaminationAppoMapperHistory(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<GeneralExaminationAppoMapperDto> listGeneralExaminationAppoMapperDto = statlessSession.getNamedQuery("GET_EXAMINATION_LIST_HISTORY")
					.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
					.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
					.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
					.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
					.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listGeneralExaminationAppoMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getExaminationAppoMapperHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getExaminationAppoMapperHistoryAdmission(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<GeneralExaminationAppoMapperDto> listGeneralExaminationAppoMapperDto = statlessSession.getNamedQuery("GET_EXAMINATION_LIST_HISTORY_BY_ADMISSION")
					.setParameter("patientId", generalExaminationAppoMapperDto.getPatientId())
					.setParameter("unitId", generalExaminationAppoMapperDto.getUnitId())
					.setParameter("organizationId", generalExaminationAppoMapperDto.getOrganizationId())
					.setParameter("typeId", generalExaminationAppoMapperDto.getTypeId())
					.setResultTransformer(Transformers.aliasToBean(GeneralExaminationAppoMapperDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listGeneralExaminationAppoMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getExaminationAppoMapperHistoryAdmission failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
}
