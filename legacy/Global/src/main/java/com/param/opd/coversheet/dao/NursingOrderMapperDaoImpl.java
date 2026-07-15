package com.param.opd.coversheet.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NamedQuery;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.NursingOrderMapperDto;
import com.param.opd.coversheet.dto.PendingNursingOrderAdministrationDto;
import com.param.opd.coversheet.dto.NursingOrderMapperDto;
import com.param.opd.coversheet.model.NursingOrderMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class NursingOrderMapperDaoImpl extends GenericDao<NursingOrderMapper, Integer> implements INursingOrderMapperDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(NursingOrderMapperDaoImpl.class);


	public NursingOrderMapperDaoImpl() {
		super(NursingOrderMapper.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;

		@Override
		public Response saveNursingOrderMapper(List<NursingOrderMapperDto> nursingOrderMapperDtoList)
				throws ApplicationException {
			try {
				for (NursingOrderMapperDto  nursingOrderMapperList : nursingOrderMapperDtoList) {
					NursingOrderMapper nursingOrderMapper = new NursingOrderMapper();
				nursingOrderMapper.setAdmissionId(nursingOrderMapperList.getAdmissionId());
				nursingOrderMapper.setDoctorId(nursingOrderMapperList.getDoctorId());
				nursingOrderMapper.setFrequencyId(nursingOrderMapperList.getFrequencyId());
				nursingOrderMapper.setInstruction(nursingOrderMapperList.getInstruction());
				nursingOrderMapper.setPatientId(nursingOrderMapperList.getPatientId());
				nursingOrderMapper.setCheckStatus(nursingOrderMapperList.getCheckStatus());
				nursingOrderMapper.setStartDate(GlobalCommonDateUtils.getDate(nursingOrderMapperList.getStartDate(), "dd-M-yyyy HH:ss:mm"));
				nursingOrderMapper.setStopDate(GlobalCommonDateUtils.getDate(nursingOrderMapperList.getStopDate(), "dd-M-yyyy HH:ss:mm"));
				nursingOrderMapper.setNursingOrderId(nursingOrderMapperList.getNursingOrderId());
				nursingOrderMapper.setOrganizationId(nursingOrderMapperList.getOrganizationId());
				nursingOrderMapper.setUnitId(nursingOrderMapperList.getUnitId());
				nursingOrderMapper.setStatus(nursingOrderMapperList.getStatus());
				nursingOrderMapper.setNurseStatus('P');
				nursingOrderMapper.setRemark(nursingOrderMapperList.getRemark());
				nursingOrderMapper.setCreatedBy(nursingOrderMapperList.getCreatedBy());
				nursingOrderMapper.setCreatedDate(GlobalCommonDateUtils.getDate(nursingOrderMapperList.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				nursingOrderMapper.setUpdatedBy(nursingOrderMapperList.getUpdatedBy());
				nursingOrderMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingOrderMapperList.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				
				save(nursingOrderMapper);
				}
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				
				} catch (HibernateException he) {
				logger.error("saveNursingOrderMapper failed", he);
				} catch (Exception e) {
				logger.error("saveNursingOrderMapper failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		@Override
		public Response updateNursingOrderMapper(List<NursingOrderMapperDto> nursingOrderMapperDtoList)
				throws ApplicationException {
			try {
				for (NursingOrderMapperDto  nursingOrderMapperList : nursingOrderMapperDtoList) {
					NursingOrderMapper nursingOrderMapper = findById(nursingOrderMapperList.getNursingOrderMapperId());
				nursingOrderMapper.setAdmissionId(nursingOrderMapperList.getAdmissionId());
				nursingOrderMapper.setDoctorId(nursingOrderMapperList.getDoctorId());
				nursingOrderMapper.setFrequencyId(nursingOrderMapperList.getFrequencyId());
				nursingOrderMapper.setInstruction(nursingOrderMapperList.getInstruction());
				nursingOrderMapper.setPatientId(nursingOrderMapperList.getPatientId());
				nursingOrderMapper.setCheckStatus(nursingOrderMapperList.getCheckStatus());
				nursingOrderMapper.setStartDate(GlobalCommonDateUtils.getDate(nursingOrderMapperList.getStartDate(), "dd-M-yyyy HH:ss:mm"));
				nursingOrderMapper.setStopDate(GlobalCommonDateUtils.getDate(nursingOrderMapperList.getStopDate(), "dd-M-yyyy HH:ss:mm"));
				nursingOrderMapper.setNursingOrderId(nursingOrderMapperList.getNursingOrderId());
				nursingOrderMapper.setOrganizationId(nursingOrderMapperList.getOrganizationId());
				nursingOrderMapper.setUnitId(nursingOrderMapperList.getUnitId());
				nursingOrderMapper.setStatus(nursingOrderMapperList.getStatus());
				nursingOrderMapper.setRemark(nursingOrderMapperList.getRemark());
				nursingOrderMapper.setNurseStatus('P');
				nursingOrderMapper.setUpdatedBy(nursingOrderMapperList.getUpdatedBy());
				nursingOrderMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingOrderMapperList.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				
				update(nursingOrderMapper);
				}
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
				} catch (HibernateException he) {
				logger.error("updateNursingOrderMapper failed", he);
				} catch (Exception e) {
				logger.error("updateNursingOrderMapper failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getNursingOrderMapperById(Integer nursingOrderMapperId, Integer organizationId, Integer unitId)
				throws ApplicationException {
			try {
				List<NursingOrderMapperDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_NURSING_ORDER_MAPPER_BY_ID")
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setParameter("nursingOrderMapperId", nursingOrderMapperId)
						.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
					
				} catch (HibernateException he) {
				logger.error("getNursingOrderMapperById failed", he);
				} catch (Exception e) {
				logger.error("getNursingOrderMapperById failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

		@Override
		public Response getNursingOrderMapperList(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
			try {
				List<NursingOrderMapperDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_NURSING_ORDER_MAPPER_LIST")
						.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
						.setParameter("unitId", nursingOrderMapperDto.getUnitId())
						.setParameter("patientId", nursingOrderMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
					
				} catch (HibernateException he) {
				logger.error("getNursingOrderMapperList failed", he);
				} catch (Exception e) {
				logger.error("getNursingOrderMapperList failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

		@Override
		public Response getActiveNursingOrderMapperList(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
			try {
				List<NursingOrderMapperDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ACTIVE_NURSING_ORDER_MAPPER_LIST")
						.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
						.setParameter("unitId", nursingOrderMapperDto.getUnitId())
						.setParameter("patientId", nursingOrderMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
					
				} catch (HibernateException he) {
				logger.error("getActiveNursingOrderMapperList failed", he);
				} catch (Exception e) {
				logger.error("getActiveNursingOrderMapperList failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		@Override
		public Response getListOfNursingOrders(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException {
			try {
				
				String query = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_LIST_OF_PATIENT_OF_NURSING_ORDERS").getQueryString();
				StringBuffer sb = new StringBuffer(query);
				if(nursingOrderMapperDto.getPatientId()!=null)
					sb.append(" AND nursingOrderMapper.patientId = :patientId");
				Query createQuery=sessionFactory.getCurrentSession()
						.createQuery(sb.toString())
						.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
						.setParameter("unitId", nursingOrderMapperDto.getUnitId());

				if (nursingOrderMapperDto.getPatientId() != null) {
					createQuery.setParameter("patientId", nursingOrderMapperDto.getPatientId());
				}

				List<NursingOrderMapperDto> list=createQuery.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
					
				} catch (HibernateException he) {
				logger.error("getListOfNursingOrders failed", he);
				} catch (Exception e) {
				logger.error("getListOfNursingOrders failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		@Override
		public Response updateNursingOrdersStatusByNurse(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
			try {
				
				Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update adt.t_nursing_order_mapper  set nurse_status = '"+nursingOrderMapperDto.getNurseStatus()
				+"' , remark= '"+nursingOrderMapperDto.getRemark()+"' "
				+ " where patient_id="+nursingOrderMapperDto.getPatientId())
				.executeUpdate();
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
				} catch (HibernateException he) {
				logger.error("updateNursingOrdersStatusByNurse failed", he);
				} catch (Exception e) {
				logger.error("updateNursingOrdersStatusByNurse failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		@Override
		public Response updateNursingOrdersStartOrStop(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
              try {
				
				Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update adt.t_nursing_order_mapper  set check_status = "+nursingOrderMapperDto.getCheckStatus()
				+ " where patient_id="+nursingOrderMapperDto.getPatientId()
				+ " and nursing_order_mapper_id="+nursingOrderMapperDto.getNursingOrderMapperId())
				.executeUpdate();
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
				} catch (HibernateException he) {
				logger.error("updateNursingOrdersStartOrStop failed", he);
				} catch (Exception e) {
				logger.error("updateNursingOrdersStartOrStop failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		@Override
		public Response getNursingOrderMapperListByAdmissionId(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
			try {
				List<NursingOrderMapperDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_NURSING_ORDER_MAPPER_LIST_BY_ADMISSION_ID")
						.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
						.setParameter("unitId", nursingOrderMapperDto.getUnitId())
						.setParameter("patientId", nursingOrderMapperDto.getPatientId())
						.setParameter("admissionId", nursingOrderMapperDto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
					
				} catch (HibernateException he) {
				logger.error("getNursingOrderMapperListByAdmissionId failed", he);
				} catch (Exception e) {
				logger.error("getNursingOrderMapperListByAdmissionId failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		@Override
		public Response getNurseOrdersForPendingOrdersScreen(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
				try {
					List<NursingOrderMapperDto> list2=new ArrayList<>();
				/*	List<NursingOrderMapperDto> list=sessionFactory.getCurrentSession()
						.createSQLQuery("select" + 
								"	distinct(nurseOrdMapper.patient_id) as \"patientId\"," + 
								"	nurseOrdMapper.admission_id as \"admissionId\"," + 
								"	nurseOrdMapper.doctor_id as \"doctorId\"," + 
								"	preg.uhid_number as \"uhidNumber\"," + 
								"	preg.first_name as \"firstName\"," + 
								"	preg.middle_name as \"middleName\"," + 
								"	preg.last_name as \"lastName\"," + 
								"	to_char(" + 
								"		preg.dob," + 
								"		'DD-MM-YYYY'" + 
								"	) as \"dob\"," + 
								"	trim( to_char( extract( year from age( current_timestamp, preg.dob )), '999' )) as \"ageString\"," + 
								"	prfx.prefix_code as \"prefixCode\"," + 
								"	gen.gender_name as \"genderName\"," + 
								"	gen.gender_id as \"genderId\"," + 
								"	gen.gender_code as \"gendercode\"," + 
								"	bm.floor_id as \"floorId\"," + 
								"	bm.bed_number as \"bedNumber\"," + 
								"	bm.bed_id as \"bedId\"," + 
								"	bm.ward_id as \"wardId\"," + 
								"	ward.ward_name as \"wardName\"," + 
								"	concat( coalesce( doc.first_name, '' ), ' ', coalesce( doc.middle_name, '' ), ' ', coalesce( doc.last_name, '' )) as \"dFirstName\"," + 
								"	to_char(" + 
								"		adl.doa," + 
								"		'dd/MM/yyyy'" + 
								"	) as \"doa\" "+
//								" to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\"" + 
								" from" + 
								"	adt.t_nursing_order_mapper nurseOrdMapper" + 
								" inner join adt.m_nursing_order_master nurseOrdeMst on" + 
								"	nurseOrdeMst.nursing_order_id = nurseOrdMapper.nursing_order_id" + 
								" inner join patient.t_patient_registration preg on" + 
								"	preg.patient_id = nurseOrdMapper.patient_id" + 
								" left join public.m_prefix_master prfx on" + 
								"	prfx.prefix_id = preg.prefix_id" + 
								" left join public.m_gender_master gen on" + 
								"	gen.gender_id = preg.gender_id" + 
								" inner join adt.t_admission admission on" + 
								"	admission.admission_id = nurseOrdMapper.admission_id" + 
								" inner join doctor.m_doctor_master doc on" + 
								"	admission.doctor_id = doc.doctor_id" + 
								" inner join doctor.m_speciality_master sp on" + 
								"	admission.speciality_id = sp.speciality_id" + 
								" inner join adt.t_admission_details adl on" + 
								"	adl.admission_id = admission.admission_id" + 
								" inner join adt.m_bed_master bm on" + 
								"	adl.bed_id = bm.bed_id" + 
								" inner join adt.m_ward_master ward on" + 
								"	bm.ward_id = ward.ward_id" + 
								" where" + 
								"	nurseOrdMapper.organization_id = 1")
						.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();*/
					
//					for(NursingOrderMapperDto dto : list) 
//					{
						
							List<NursingOrderMapperDto> list1=sessionFactory.getCurrentSession()
									.createSQLQuery("select" + 
											"	nurseOrdMapper.nursing_order_mapper_id as \"nursingOrderMapperId\"," + 
											"	nurseOrdMapper.nursing_order_id as \"nursingOrderId\"," + 
											"	nurseOrdeMst.nursing_order_desc as \"nursingOrderDesc\"," + 
											"	to_char(" + 
											"		nurseOrdMapper.start_date," + 
											"		'dd/MM/yyyy'" + 
											"	) as \"startDate\"," + 
											"	to_char(" + 
											"		nurseOrdMapper.stop_date," + 
											"		'dd/MM/yyyy'" + 
											"	) as \"stopDate\"," + 
											"	nurseOrdMapper.frequency_id as \"frequencyId\"," + 
											"	freq.description as \"frequencyDesc\"" + 
											" from" + 
											"	adt.t_nursing_order_mapper nurseOrdMapper" + 
											" inner join adt.m_nursing_order_master nurseOrdeMst on" + 
											"	nurseOrdeMst.nursing_order_id = nurseOrdMapper.nursing_order_id" + 
											" inner join public.m_frequency freq on" + 
											"	freq.id = nurseOrdMapper.frequency_id" + 
											" where" + 
											"	nurseOrdMapper.patient_id = :patientId "
											+ " and nurseOrdMapper.organization_id = :organizationId")
								.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
								.setParameter("patientId", nursingOrderMapperDto.getPatientId())
								.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
							
							
						
							for(NursingOrderMapperDto dto1 : list1) 
							{
								
									List<NursingOrderMapperDto> list3=sessionFactory.getCurrentSession()
											.createSQLQuery("select" + 
													"	distinct(nurseOrdMapper.frequency_id) as \"frequencyId\"," + 
													"	to_char(freqdtl.administration_time, 'HH24:MI') as \"administrationTime\"," + 
													"	to_char(freqdtl.pre_buffer_administration_time, 'HH24:MI') as \"preBufferAdministrationTime\"," + 
													"	to_char(freqdtl.post_buffer_administration_time, 'HH24:MI') as \"postBufferAdministrationTime\" " + 
													" from" + 
													"	adt.t_nursing_order_mapper nurseOrdMapper" + 
													" inner join public.m_frequency_detail freqdtl on" + 
													"	nurseOrdMapper.frequency_id = freqdtl.frequency_id" + 
													" where" + 
													"	freqdtl.frequency_id = :frequencyId" + 
													"	and freqdtl.is_active = true" + 
													"	and freqdtl.is_deleted = false "
													+ " and nurseOrdMapper.organization_id = :organizationId ")
										.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
										.setParameter("frequencyId", dto1.getFrequencyId())
										.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
									dto1.setNursingOrderAdministeredTimeList(list3);
//							}
//							dto.setNursingOrderMapperDtoList(list1);
							
					}
					return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
			} catch (HibernateException he) {
			logger.error("getNurseOrdersForPendingOrdersScreen failed", he);
			} catch (Exception e) {
				logger.error("getNurseOrdersForPendingOrdersScreen failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		@Override
		public Response getNurseOrdersForPendingOrdersScreenByDate(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
			try {
				List<NursingOrderMapperDto> list2=new ArrayList<>();
						List<NursingOrderMapperDto> list1=sessionFactory.getCurrentSession()
								.createSQLQuery("select" + 
										"	nurseOrdMapper.nursing_order_mapper_id as \"nursingOrderMapperId\"," + 
										"	nurseOrdMapper.nursing_order_id as \"nursingOrderId\"," + 
										"	nurseOrdeMst.nursing_order_desc as \"nursingOrderDesc\"," + 
										"	nurseOrdMapper.instruction as \"instruction\"," + 
										"	to_char(" + 
										"		nurseOrdMapper.start_date," + 
										"		'dd/MM/yyyy'" + 
										"	) as \"startDate\"," + 
										"	to_char(" + 
										"		nurseOrdMapper.stop_date," + 
										"		'dd/MM/yyyy'" + 
										"	) as \"stopDate\"," + 
										"	nurseOrdMapper.frequency_id as \"frequencyId\"," + 
										"	freq.description as \"frequencyDesc\"" + 
										" from" + 
										"	adt.t_nursing_order_mapper nurseOrdMapper" + 
										" inner join adt.m_nursing_order_master nurseOrdeMst on" + 
										"	nurseOrdeMst.nursing_order_id = nurseOrdMapper.nursing_order_id" + 
										" inner join public.m_frequency freq on" + 
										"	freq.id = nurseOrdMapper.frequency_id" + 
										" where" + 
										"	nurseOrdMapper.patient_id = :patientId "
										+ " and nurseOrdMapper.organization_id = :organizationId"
										+ " and to_char(nurseOrdMapper.start_date, 'YYYY-MM-DD') = :startDate "
										)
							.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
							.setParameter("patientId", nursingOrderMapperDto.getPatientId())
							.setParameter("startDate", nursingOrderMapperDto.getStartDate())
							.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
						
					
						for(NursingOrderMapperDto dto1 : list1) 
						{
							
								List<NursingOrderMapperDto> list3=sessionFactory.getCurrentSession()
										.createSQLQuery("select" + 
												"	distinct(nurseOrdMapper.frequency_id) as \"frequencyId\"," + 
												"	to_char(freqdtl.administration_time, 'HH24:MI') as \"administrationTime\"," + 
												"	to_char(freqdtl.pre_buffer_administration_time, 'HH24:MI') as \"preBufferAdministrationTime\"," + 
												"	to_char(freqdtl.post_buffer_administration_time, 'HH24:MI') as \"postBufferAdministrationTime\" " + 
												" from" + 
												"	adt.t_nursing_order_mapper nurseOrdMapper" + 
												" inner join public.m_frequency_detail freqdtl on" + 
												"	nurseOrdMapper.frequency_id = freqdtl.frequency_id" + 
												" where" + 
												"	freqdtl.frequency_id = :frequencyId" + 
												"	and freqdtl.is_active = true" + 
												"	and freqdtl.is_deleted = false "
												+ " and nurseOrdMapper.organization_id = :organizationId ")
									.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
									.setParameter("frequencyId", dto1.getFrequencyId())
									.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
								dto1.setNursingOrderAdministeredTimeList(list3);
						
				}
				return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
		} catch (HibernateException he) {
		logger.error("getNurseOrdersForPendingOrdersScreenByDate failed", he);
		} catch (Exception e) {
			logger.error("getNurseOrdersForPendingOrdersScreenByDate failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		
		
		@Override
		public Response getNurseOrdersDateWiseList(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
			try {
//				List<NursingOrderMapperDto> list2=new ArrayList<>();
				List<NursingOrderMapperDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery("select" + 
							"	nurseOrdMapper.nursing_order_mapper_id as \"nursingOrderMapperId\"," + 
							"	nurseOrdMapper.nursing_order_id as \"nursingOrderId\"," +
							"	nurseOrdMapper.check_status as \"checkStatus\"," +
							"	nurseOrdeMst.nursing_order_desc as \"nursingOrderDesc\"," + 
							"	nurseOrdeMst.service_id as \"serviceId\"," + 
							"	nurseOrdMapper.instruction as \"instruction\"," + 
							"	to_char(" + 
							"		nurseOrdMapper.start_date," + 
							"		'DD/MM/YYYY'" + 
							"	) as \"startDateParse\"," + 
							"	to_char(" + 
							"		nurseOrdMapper.stop_date," + 
							"		'DD/MM/YYYY'" + 
							"	) as \"stopDateParse\"," + 
							"	to_char(" + 
							"		nurseOrdMapper.start_date," + 
							"		'yyyy-MM-dd'" + 
							"	) as \"startDate\"," + 
							"	to_char(" + 
							"		nurseOrdMapper.stop_date," + 
							"		'yyyy-MM-dd'" + 
							"	) as \"stopDate\"," + 
							"	nurseOrdMapper.frequency_id as \"frequencyId\", " 
							+ "nurseOrdeMst.speciality_id AS \"specialityId\", "
							+ "nurseOrdeMst.sub_speciality_id AS \"subSpecialityId\", "
							+ "serMast.service_standard_name AS \"serviceStandardName\", "
							+ "specMaster.speciality_name AS \"specialityName\", "
							+ "subSpecMaster.sub_speciality_name AS \"subSpecialityName\", "+
							"	freq.description as \"frequencyDesc\" " + 
							" from" + 
							"	adt.t_nursing_order_mapper nurseOrdMapper" + 
							" inner join adt.m_nursing_order_master nurseOrdeMst on" + 
							"	nurseOrdeMst.nursing_order_id = nurseOrdMapper.nursing_order_id" + 
							" inner join public.m_frequency freq on" + 
							"	freq.id = nurseOrdMapper.frequency_id "
							+ " left  join service.m_service_master serMast on "
							+ " serMast.service_master_id=nurseOrdeMst.service_id "
							+ " left join doctor.m_speciality_master specMaster on "
							+ " specMaster.speciality_id = nurseOrdeMst.speciality_id "
							+ " left join doctor.m_sub_speciality_master subSpecMaster on"
							+ " subSpecMaster.sub_speciality_id =nurseOrdeMst.sub_speciality_id " + 
							" where" + 
							"	nurseOrdMapper.patient_id = :patientId "
							+ " and nurseOrdMapper.organization_id = :organizationId " 
							+ " and nurseOrdMapper.check_status = false or nurseOrdMapper.check_status = true ")
					

					.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
					.setParameter("patientId", nursingOrderMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
				for(NursingOrderMapperDto dto : list) 
				{
					org.joda.time.LocalDate startDate = new org.joda.time.LocalDate(dto.getStartDate());
					org.joda.time.LocalDate stopDate = new org.joda.time.LocalDate(dto.getStopDate());
					List<String> dates = new ArrayList<>(); 
					dates.add(startDate.toString());
					while (startDate.isBefore(stopDate)) 
						{
								////System.out.println(startDate.toString());
								startDate = startDate.plusDays(1);
								dates.add(startDate.toString());
						//System.out.println(dates);
					}
					
					List<NursingOrderMapperDto> list3=sessionFactory.getCurrentSession()
							.createSQLQuery("select" + 
									"	distinct(nurseOrdMapper.frequency_id) as \"frequencyId\"," + 
									"	to_char(freqdtl.administration_time, 'HH24:MI') as \"administrationTime\"," + 
									"	to_char(freqdtl.pre_buffer_administration_time, 'HH24:MI') as \"preBufferAdministrationTime\"," + 
									"	to_char(freqdtl.post_buffer_administration_time, 'HH24:MI') as \"postBufferAdministrationTime\" " + 
									" from" + 
									"	adt.t_nursing_order_mapper nurseOrdMapper" + 
									" inner join public.m_frequency_detail freqdtl on" + 
									"	nurseOrdMapper.frequency_id = freqdtl.frequency_id" + 
									" where" + 
									"	freqdtl.frequency_id = :frequencyId" + 
									"	and freqdtl.is_active = true" + 
									"	and freqdtl.is_deleted = false "
									+ " and nurseOrdMapper.organization_id = :organizationId ")
						.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
						.setParameter("frequencyId", dto.getFrequencyId())
						.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
					
					
					List<NursingOrderMapperDto> orderDateList=new ArrayList<>();
					for (String adDates :dates) {
						NursingOrderMapperDto dateDto=new NursingOrderMapperDto();
						dateDto.setAdministrationDate(adDates);
						dateDto.setNursingOrderAdministeredTimeList(list3);
						orderDateList.add(dateDto);
				  }
					dto.setNursingOrderMapperDtoList(orderDateList);
					
				}
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getNurseOrdersDateWiseList failed", he);
		} catch (Exception e) {
			logger.error("getNurseOrdersDateWiseList failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		@Override
		public Response getNurseOrdersDateWiseListWebApp(NursingOrderMapperDto nursingOrderMapperDto)
				throws ApplicationException {
			try {
				
				String fromDate = nursingOrderMapperDto.getStartDate();
						
				List<NursingOrderMapperDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery("select" + 
							"	nurseOrdMapper.nursing_order_mapper_id as \"nursingOrderMapperId\"," + 
							"	nurseOrdMapper.nursing_order_id as \"nursingOrderId\"," + 
							"	nurseOrdeMst.nursing_order_desc as \"nursingOrderDesc\"," + 
							"	nurseOrdeMst.service_id as \"serviceId\"," + 
							"	nurseOrdMapper.instruction as \"instruction\"," + 
							"	to_char(" + 
							"		nurseOrdMapper.start_date," + 
							"		'DD/MM/YYYY'" + 
							"	) as \"startDateParse\"," + 
							"	to_char(" + 
							"		nurseOrdMapper.stop_date," + 
							"		'DD/MM/YYYY'" + 
							"	) as \"stopDateParse\"," + 
							"	to_char(" + 
							"		nurseOrdMapper.start_date," + 
							"		'yyyy-MM-dd'" + 
							"	) as \"startDate\"," + 
							"	to_char(" + 
							"		nurseOrdMapper.stop_date," + 
							"		'yyyy-MM-dd'" + 
							"	) as \"stopDate\"," + 
							"	nurseOrdMapper.frequency_id as \"frequencyId\", " 
							+ "nurseOrdeMst.speciality_id AS \"specialityId\", "
							+ "nurseOrdeMst.sub_speciality_id AS \"subSpecialityId\", "
							+ "serMast.service_standard_name AS \"serviceStandardName\", "
							+ "specMaster.speciality_name AS \"specialityName\", "
							+ "subSpecMaster.sub_speciality_name AS \"subSpecialityName\", "+
							"	freq.description as \"frequencyDesc\" " + 
							" from" + 
							"	adt.t_nursing_order_mapper nurseOrdMapper" + 
							" inner join adt.m_nursing_order_master nurseOrdeMst on" + 
							"	nurseOrdeMst.nursing_order_id = nurseOrdMapper.nursing_order_id" + 
							" inner join public.m_frequency freq on" + 
							"	freq.id = nurseOrdMapper.frequency_id "
							+ " left  join service.m_service_master serMast on "
							+ " serMast.service_master_id=nurseOrdeMst.service_id "
							+ " left join doctor.m_speciality_master specMaster on "
							+ " specMaster.speciality_id = nurseOrdeMst.speciality_id "
							+ " left join doctor.m_sub_speciality_master subSpecMaster on"
							+ " subSpecMaster.sub_speciality_id =nurseOrdeMst.sub_speciality_id " + 
							" where" + 
							"	nurseOrdMapper.patient_id = :patientId "
							+ " and nurseOrdMapper.organization_id = :organizationId " 
							+ " and nurseOrdMapper.check_status = false "
							+ " and to_char( nurseOrdMapper.start_date, 'yyyy-MM-dd' )<=  '"+ fromDate +"' and   to_char( nurseOrdMapper.stop_date, 'yyyy-MM-dd' ) >= '"+fromDate+"' ")
					

					.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
					.setParameter("patientId", nursingOrderMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
				for(NursingOrderMapperDto dto : list) 
				{
					
					Date d=new Date();  
					org.joda.time.LocalDate newDate = new org.joda.time.LocalDate(new Date());
					org.joda.time.LocalDate startDate = new org.joda.time.LocalDate(dto.getStartDate());
					org.joda.time.LocalDate stopDate = new org.joda.time.LocalDate(dto.getStopDate());
					List<String> dates = new ArrayList<>(); 
					dates.add(startDate.toString());
					while (startDate.isBefore(stopDate)) 
						{
								startDate = startDate.plusDays(1);
								
								if(startDate.isEqual(newDate)){
									dates.add(startDate.toString());
								}
								
					   }
					
					
					List<NursingOrderMapperDto> list3=sessionFactory.getCurrentSession()
							.createSQLQuery("select" + 
									"	distinct(nurseOrdMapper.frequency_id) as \"frequencyId\"," + 
									"	to_char(freqdtl.administration_time, 'HH24:MI') as \"administrationTime\"," + 
									"	to_char(freqdtl.pre_buffer_administration_time, 'HH24:MI') as \"preBufferAdministrationTime\"," + 
									"	to_char(freqdtl.post_buffer_administration_time, 'HH24:MI') as \"postBufferAdministrationTime\" " + 
									" from" + 
									"	adt.t_nursing_order_mapper nurseOrdMapper" + 
									" inner join public.m_frequency_detail freqdtl on" + 
									"	nurseOrdMapper.frequency_id = freqdtl.frequency_id" + 
									" where" + 
									"	freqdtl.frequency_id = :frequencyId" + 
									"	and freqdtl.is_active = true" + 
									"	and freqdtl.is_deleted = false "
									+ " and nurseOrdMapper.organization_id = :organizationId ")
						.setParameter("organizationId", nursingOrderMapperDto.getOrganizationId())
						.setParameter("frequencyId", dto.getFrequencyId())
						.setResultTransformer(Transformers.aliasToBean(NursingOrderMapperDto.class)).list();
					
					
					List<PendingNursingOrderAdministrationDto> listSavedPatient=sessionFactory.getCurrentSession()
							.createSQLQuery("select" + 
									"	pnd.pending_nur_ord_administration_id as \"pendingNurOrdAdministrationId\"," + 
									"	pnd.nursing_order_mapper_id as \"nursingOrderMapperId\"," + 
									"	to_char(" + 
									"		pnd.pending_nur_ord_administration_date," + 
									"		'YYYY-MM-DD'" + 
									"	) as \"pendingNurOrdAdministrationDate\"," + 
									"	to_char(" + 
									"		pnd.pending_nur_ord_administration_time," + 
									"		'HH24:MI'" + 
									"	) as \"pendingNurOrdAdministrationTime\"," + 
									"	to_char(" + 
									"		pnd.administration_time," + 
									"		'HH24:MI'" + 
									"	) as \"administrationTime\"," + 
									"	pnd.reason_id as \"reasonId\"," + 
									"	rsn.reason_desc as \"reasonDesc\"," + 
									"	pnd.remark as \"remark\"" + 
									" from" + 
									"	adt.t_pending_nursing_order_administration pnd" + 
									" inner join adt.m_reason_master rsn on" + 
									"	rsn.reason_master_id = pnd.reason_id" + 
									" where" + 
									"	pnd.patient_id =:patientId"
									+ " and  pnd.nursing_order_mapper_id =:nursingOrderMapperId " + 
									"   and to_char( pnd.pending_nur_ord_administration_date, 'yyyy-MM-dd' )= '"+ fromDate +"'")
						.setParameter("patientId", nursingOrderMapperDto.getPatientId())
						.setParameter("nursingOrderMapperId", dto.getNursingOrderMapperId())
						.setResultTransformer(Transformers.aliasToBean(PendingNursingOrderAdministrationDto.class)).list();
					
					/*List<PendingNursingOrderAdministrationDto> listSavedPatient = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_PENDING_NUR_ORD_LIST_BY_PATIENT_ID")
							.setParameter("patientId", nursingOrderMapperDto.getPatientId())
							.setResultTransformer(Transformers.aliasToBean(PendingNursingOrderAdministrationDto.class)).list();*/
					
					List<NursingOrderMapperDto> orderDateList=new ArrayList<>();
					
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
					if(listSavedPatient != null && listSavedPatient.size() > 0) {
						for(NursingOrderMapperDto dtofisrt : list3) 
						{
							
							LocalTime cuurentTime = LocalTime.parse(nursingOrderMapperDto.getCurrentTime());
							LocalTime bToTime = LocalTime.parse(dtofisrt.getAdministrationTime());
								for (PendingNursingOrderAdministrationDto adDatessecodn :listSavedPatient) {
									
									if(dtofisrt.getAdministrationTime().equals(adDatessecodn.getAdministrationTime())) {
										dtofisrt.setAdministrationTime(adDatessecodn.getPendingNurOrdAdministrationTime());
										dtofisrt.setPendingNurOrdAdministrationId(adDatessecodn.getPendingNurOrdAdministrationId());
										dtofisrt.setRemark(adDatessecodn.getRemark());
										dtofisrt.setReasonId(adDatessecodn.getReasonId());
										dtofisrt.setReasonDesc(adDatessecodn.getReasonDesc());
										String color = "green";
										dtofisrt.setColor(color);
									}else if(bToTime.isAfter(cuurentTime))
									{
										String color = "yellow";
										dtofisrt.setColor(color);
									}else if(bToTime.isAfter(cuurentTime)) {
										String color = "red";
										dtofisrt.setColor(color);
									}
							  }
						}
					}
					
					
					for (String adDates :dates) {
						NursingOrderMapperDto dateDto=new NursingOrderMapperDto();
						dateDto.setAdministrationDate(adDates);
						dateDto.setNursingOrderAdministeredTimeList(list3);
						orderDateList.add(dateDto);
				  }
					dto.setNursingOrderMapperDtoList(orderDateList);
					
				}
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getNurseOrdersDateWiseListWebApp failed", he);
		} catch (Exception e) {
			logger.error("getNurseOrdersDateWiseListWebApp failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

}
