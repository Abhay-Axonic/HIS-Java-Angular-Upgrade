package com.param.opd.coversheet.dao;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.model.VaccineImmunizationMapper;
import com.param.opd.coversheet.dto.VaccineImmunizationMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VaccineImmunizationMapperDAOImpl extends GenericDao<VaccineImmunizationMapper, Integer> implements IVaccineImmunizationDAO,ICommonConstants {
	final static Logger logger = Logger.getLogger(VaccineImmunizationMapperDAOImpl.class);


	public VaccineImmunizationMapperDAOImpl() {
		super(VaccineImmunizationMapper.class);
	}
	

	@Autowired
	SessionFactory sessionFactory;
    @Override
	public Response getListOfVaccineImmunizationMapper(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException {
		try
		{
			List<VaccineImmunizationMapperDto> list2=new ArrayList<>();
			List<VaccineImmunizationMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VACCINE_IMMUNIZATION_MAPPER")
					.setParameter("organizationId", vaccineImmunizationMapperDto.getOrganizationId())
					.setParameter("patientId", vaccineImmunizationMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapperDto.class)).list();
			
			for(VaccineImmunizationMapperDto dto : list) {
                if(dto.getGenericId() != null) {
		         String query = " select itm.id as \"itemId\", itm.name as \"name\", " + 
		         		" gen.name as \"genericName\" " + 
		         		" from public.m_item itm " + 
		         		" inner join public.m_generic gen on gen.id=itm.generic_id " + 
		         		" where itm.generic_id=:genericId " +
		         		" ORDER BY gen.name ASC";
			
		         List<VaccineImmunizationMapperDto> list3  = sessionFactory.getCurrentSession()
					    .createSQLQuery(query)
						.setParameter("genericId", dto.getGenericId())
						.setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapperDto.class)).list();
				dto.setListOfGenericIdWiseList(list3);
				list2.add(dto);
				}
			}
			
			//System.out.println(listVaccineMapperDto.size());
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfVaccineImmunizationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response saveVaccineImmunizationMapper(List<VaccineImmunizationMapperDto> vaccineImmunizationMapperDto)
			throws ApplicationException {
		try {
			VaccineImmunizationMapperDto resDto=new VaccineImmunizationMapperDto();
			VaccineImmunizationMapper vaccineImmunizationMapper=new VaccineImmunizationMapper();
			//System.out.println(vaccineImmunizationMapperDto.size());
			for(VaccineImmunizationMapperDto l:vaccineImmunizationMapperDto)
			{
				if(l.getVaccinationMasterId()!=null) {
				StringBuilder listQuery=new StringBuilder(sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VACCINE_MAPPER_BY_PATIENTID_VACCINATIONID").getQueryString());
				Query query=sessionFactory.getCurrentSession().createQuery(listQuery.toString());
				vaccineImmunizationMapper= (VaccineImmunizationMapper)query
					.setParameter("vaccinationMasterId", l.getVaccinationMasterId())
						.setParameter("patientId", l.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapper.class)).uniqueResult();
				
				if(vaccineImmunizationMapper!=null)
				{
					
					vaccineImmunizationMapper.setVaccinationMasterId(l.getVaccinationMasterId());
					if(l.getActualDate() != null ) {
					vaccineImmunizationMapper.setActualDate(GlobalCommonDateUtils.getDate(l.getActualDate(), "dd-M-yyyy HH:mm:ss"));
					}else {
						vaccineImmunizationMapper.setActualDate(null);
					}
					if(l.getPlannedDate() != null && l.getPlannedDate() != "Invalid date") {
					vaccineImmunizationMapper.setPlannedDate(GlobalCommonDateUtils.getDate(l.getPlannedDate(), "dd-M-yyyy HH:mm:ss"));
					}else {
						vaccineImmunizationMapper.setPlannedDate(null);
					}
					vaccineImmunizationMapper.setPatientId(l.getPatientId());
					vaccineImmunizationMapper.setIsNoSignificantVaccination(l.getIsNoSignificantVaccination());
					vaccineImmunizationMapper.setVaccineFlagStatus(l.getVaccineFlagStatus());
					if(l.getAdmissionId() != null) {
						vaccineImmunizationMapper.setAdmissionId(l.getAdmissionId());
					}else {
						vaccineImmunizationMapper.setEncounterId(l.getEncounterId());
					}
					if(l.getItemId()!=null) {
						vaccineImmunizationMapper.setItemId(l.getItemId());
					}
					vaccineImmunizationMapper.setRoleId(l.getRoleId());
					vaccineImmunizationMapper.setOrganizationId(l.getOrganizationId());
					vaccineImmunizationMapper.setRemarks(l.getRemarks());
					vaccineImmunizationMapper.setUnitId(l.getUnitId());
					vaccineImmunizationMapper.setStatus(l.getStatus());
					vaccineImmunizationMapper.setVaccinationNote(l.getVaccinationNote() != null ? l.getVaccinationNote():null);
					vaccineImmunizationMapper.setLocation(l.getLocation());
					if(l.getPlannedDate() == null) {
						vaccineImmunizationMapper.setIsForceClosed(true);
						vaccineImmunizationMapper.setPlannedDate(null);
					}else {
						vaccineImmunizationMapper.setIsForceClosed(false);
					}
					
					vaccineImmunizationMapper.setCreatedBy(l.getCreatedBy());
					if(l.getCalculatedDate() != null) {
					vaccineImmunizationMapper.setCalculatedDate(GlobalCommonDateUtils.getDate(l.getCalculatedDate(), "dd-M-yyyy HH:mm:ss"));
					}else {
						vaccineImmunizationMapper.setCalculatedDate(null);
					}
					vaccineImmunizationMapper.setCreatedDate(GlobalCommonDateUtils.getDate(l.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					vaccineImmunizationMapper.setUpdatedBy(l.getUpdatedBy());
					vaccineImmunizationMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(l.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					vaccineImmunizationMapper.setIsVaccinationComplete(l.getIsVaccinationComplete());
					vaccineImmunizationMapper.setIsMarkAllVaccineComplete(l.getIsMarkAllVaccineComplete());

					sessionFactory.getCurrentSession().update(vaccineImmunizationMapper);
					
					resDto.setIsVaccinationComplete(l.getIsVaccinationComplete());
					resDto.setCalculatedPlannedDate(l.getCalculatedPlannedDate());
					resDto.setPatientId(l.getPatientId());

				}
				
				else {
						if(l.getVaccineFlagStatus().equals('Y'))	{
						VaccineImmunizationMapper vaccineMapper=new VaccineImmunizationMapper();
						vaccineMapper.setVaccinationMasterId(l.getVaccinationMasterId());
	                    vaccineMapper.setVaccineNameText(l.getVaccineNameText());
						if(l.getActualDate() != null) {
						vaccineMapper.setActualDate(GlobalCommonDateUtils.getDate(l.getActualDate(), "dd-M-yyyy HH:mm:ss"));
						}else {
							vaccineMapper.setActualDate(null);
						}
						if(l.getPlannedDate() != null) {
						vaccineMapper.setPlannedDate(GlobalCommonDateUtils.getDate(l.getPlannedDate(), "dd-M-yyyy HH:mm:ss"));
						}
						else {
							vaccineMapper.setPlannedDate(null);
						}
						if(l.getItemId()!=null) {
						vaccineMapper.setItemId(l.getItemId());
						}
						vaccineMapper.setPatientId(l.getPatientId());
						vaccineMapper.setIsNoSignificantVaccination(l.getIsNoSignificantVaccination());
						vaccineMapper.setVaccineFlagStatus(l.getVaccineFlagStatus());
						if(l.getAdmissionId() != null) {
							vaccineMapper.setAdmissionId(l.getAdmissionId());
						}else {
							vaccineMapper.setEncounterId(l.getEncounterId());
						}
						vaccineMapper.setRoleId(l.getRoleId());
						vaccineMapper.setOrganizationId(l.getOrganizationId());
						vaccineMapper.setRemarks(l.getRemarks());
						vaccineMapper.setUnitId(l.getUnitId());
						vaccineMapper.setStatus(l.getStatus());
						vaccineMapper.setLocation(l.getLocation());
						if(l.getActualDate() == null) {
							vaccineMapper.setIsForceClosed(true);
							vaccineMapper.setActualDate(null);
						}else {
							vaccineMapper.setIsForceClosed(false);
						}
						vaccineMapper.setVaccinationNote(l.getVaccinationNote() != null ? l.getVaccinationNote():null);
						vaccineMapper.setCreatedBy(l.getCreatedBy());
						if(l.getCalculatedDate() != null) {
						vaccineMapper.setCalculatedDate(GlobalCommonDateUtils.getDate(l.getCalculatedDate(), "dd-M-yyyy HH:mm:ss"));
						}else {
							
							vaccineMapper.setCalculatedDate(null);
						}
						vaccineMapper.setCreatedDate(GlobalCommonDateUtils.getDate(l.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
						vaccineMapper.setUpdatedBy(l.getUpdatedBy());
						vaccineMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(l.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
						vaccineMapper.setIsVaccinationComplete(l.getIsVaccinationComplete());
						vaccineMapper.setIsMarkAllVaccineComplete(l.getIsMarkAllVaccineComplete());
//						resDto.setIsVaccinationComplete(l.getIsVaccinationComplete());
						resDto.setCalculatedPlannedDate(l.getCalculatedPlannedDate());
						resDto.setPatientId(l.getPatientId());
						sessionFactory.getCurrentSession().save(vaccineMapper);
						
						resDto.setIsVaccinationComplete(vaccineMapper.getIsVaccinationComplete());
						resDto.setCalculatedDate(GlobalCommonDateUtils.getAvalDate(vaccineMapper.getCalculatedDate(), "dd-M-yyyy"));

						}
				}
				}else {
					if(l.getVaccineFlagStatus().equals('Y'))	{
						VaccineImmunizationMapper vaccineMapper=new VaccineImmunizationMapper();
//						vaccineMapper.setVaccinationMasterId(l.getVaccinationMasterId());
						vaccineMapper.setVaccineNameText(l.getVaccineNameText());
						if(l.getActualDate() != null) {
						vaccineMapper.setActualDate(GlobalCommonDateUtils.getDate(l.getActualDate(), "dd-M-yyyy HH:mm:ss"));
						}else {
							vaccineMapper.setActualDate(null);
						}
						if(l.getPlannedDate() != null) {
						vaccineMapper.setPlannedDate(GlobalCommonDateUtils.getDate(l.getPlannedDate(), "dd-M-yyyy HH:mm:ss"));
						}
						else {
							vaccineMapper.setPlannedDate(null);
						}
						if(l.getItemId()!=null) {
						vaccineMapper.setItemId(l.getItemId());
						}
						vaccineMapper.setPatientId(l.getPatientId());
						vaccineMapper.setIsNoSignificantVaccination(l.getIsNoSignificantVaccination());
						vaccineMapper.setVaccineFlagStatus(l.getVaccineFlagStatus());
						if(l.getAdmissionId() != null) {
							vaccineMapper.setAdmissionId(l.getAdmissionId());
						}else {
							vaccineMapper.setEncounterId(l.getEncounterId());
						}
						vaccineMapper.setRoleId(l.getRoleId());
						vaccineMapper.setOrganizationId(l.getOrganizationId());
						vaccineMapper.setRemarks(l.getRemarks());
						vaccineMapper.setUnitId(l.getUnitId());
						vaccineMapper.setStatus(l.getStatus());
						vaccineMapper.setLocation(l.getLocation());
						vaccineMapper.setCreatedBy(l.getCreatedBy());
						if(l.getCalculatedDate() != null) {
						vaccineMapper.setCalculatedDate(GlobalCommonDateUtils.getDate(l.getCalculatedDate(), "dd-M-yyyy HH:mm:ss"));
						}else {
							
							vaccineMapper.setCalculatedDate(null);
						}
						vaccineMapper.setCreatedDate(GlobalCommonDateUtils.getDate(l.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
						vaccineMapper.setUpdatedBy(l.getUpdatedBy());
						vaccineMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(l.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
						save(vaccineMapper);
						}
				}

			}
			return new Response(SUCCESS, SUCCESS_CODE, "Vaccination Details Recorded Successfully", null, null);
		} catch (HibernateException he) {
		logger.error("saveVaccineImmunizationMapper failed", he);
		} catch (Exception e) {
		logger.error("saveVaccineImmunizationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response updateVaccineImmunizationMapper(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException {

		try {
			String date=vaccineImmunizationMapperDto.getCalculatedPlannedDate() != null ? vaccineImmunizationMapperDto.getCalculatedPlannedDate() : GlobalCommonDateUtils.getAvalDate(new Date(), "yyyy-M-dd");
				if(vaccineImmunizationMapperDto.getIsVaccinationComplete() && (date!= null || date!= "" || date!= "Invalid date")) {
					Session session=sessionFactory.getCurrentSession();
					session.createSQLQuery("update opd.t_vaccine_immunization_mapper set is_vaccination_complete = true "
							+ " where patient_id =:patientId  and to_char(calculated_date , 'yyyy-mm-dd') <=:date "
							)
							.setParameter("patientId",vaccineImmunizationMapperDto.getPatientId())
							.setParameter("date",date)
					.executeUpdate();
				}
		return new Response(SUCCESS, SUCCESS_CODE, "Vaccination Details Recorded Successfully", null, null);
		} catch (HibernateException he) {
		logger.error("updateVaccineImmunizationMapper failed", he);
		} catch (Exception e) {
		logger.error("updateVaccineImmunizationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}



	@Override
	public Response getVaccineDueDatePatientsForSendingMailAndMsgs(int iteration,List<Integer> unitIds) throws ApplicationException {
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            // java 8+ Date/time API
            LocalDate today = LocalDate.now();
            
            LocalDate tomorrow = null ;
            if(iteration==0) {
            	tomorrow =  today.plusDays(3);
            }else if(iteration==1) {
            	tomorrow =  today.plusDays(1);
            }
            // Java8+ tomorrow to Util tomorrow if you want
            java.util.Date tomorrowUtilDate = java.sql.Date.valueOf(tomorrow);
            String currentDate = simpleDateFormat.format(tomorrowUtilDate);
            
            String query ="select  tvim.vaccine_immunization_mapper_id  as \"vaccineImmunMapperId\", " + 
            		"    to_char(tvim.calculated_date ,'yyyy-mm-dd')  as \"calculatedDate\"," + 
            		"   concat(item.name,' | ', item.code) as \"vaccineName\" ," + 
            		"   tvim.patient_id as \"patientId\"," + 
            		"   tvim.organization_id  as \"organizationId\"," + 
            		"   tvim.unit_id  as \"unitId\"," + 
            		"   tvim.admission_id  as \"admissionId\"," + 
            		"   tvim.encounter_id as \"encounterId\"," + 
            		"   tvim.vaccination_id as \"vaccinationMasterId\", " + 
            		"   site.site_name  as \"siteName\", " + 
            		"	mstr.series as \"series\" " + 
            		"   from  opd.t_vaccine_immunization_mapper tvim " + 
            		"   inner join public.m_item item on item.id = tvim.item_id  " + 
            		"   inner join lab.m_vaccination_master mstr on mstr.vaccination_id  = tvim.vaccination_id " + 
            		"   inner join patient.t_patient_registration tpr  on tpr.patient_id = tvim.patient_id " + 
            		"   inner join m_site_of_admininstration site on site.site_id = mstr.site_id " +
            		"   where to_char(tvim.calculated_date ,'yyyy-MM-dd') = :currentDate " +
            		"   and tvim.status ='A' " + 
            		"	and tpr.gender_id = mstr.gender_id " + 
            		"	and mstr.status ='A' " +
            		"   and tvim.patient_id = tvim.patient_id "
            		+ " and tvim.unit_id  in (:unitIds) ";

            List<VaccineImmunizationMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameter("currentDate", currentDate)
                    .setParameterList("unitIds", unitIds)
                    .setResultTransformer(Transformers.aliasToBean(VaccineImmunizationMapperDto.class)).list();

            if(list!=null && list.size()>0) {
            	for (VaccineImmunizationMapperDto vaccineImmunizationMapperDto : list) {
            		if(vaccineImmunizationMapperDto.getSeries()!=null) {
            			if(vaccineImmunizationMapperDto.getSeries().equals(13)) {
            				vaccineImmunizationMapperDto.setSeriesName("Booster 1");
            			}else if(vaccineImmunizationMapperDto.getSeries().equals(14)) {
            				vaccineImmunizationMapperDto.setSeriesName("Booster 2");
            			}else {
            				vaccineImmunizationMapperDto.setSeriesName(vaccineImmunizationMapperDto.getSeries().toString());
            			}
            		} 
				}
            }
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

        } catch (HibernateException he) {
        logger.error("getVaccineDueDatePatientsForSendingMailAndMsgs failed", he);
        } catch (Exception e) {
        logger.error("getVaccineDueDatePatientsForSendingMailAndMsgs failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response updateVaccineImmunizationMapperForNewCov(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException {
		try
		{
			Session session = sessionFactory.getCurrentSession();
			String sqlQuery = "UPDATE opd.t_vaccine_immunization_mapper SET "
					+ "remarks = :remarks, "
					+ "planned_date = :plannedDate, "
					+ "location = :location "
					+ "WHERE vaccine_immunization_mapper_id = :vaccineImmunMapperId "
					+ "AND status = 'A'";
			int rowsUpdated = session.createSQLQuery(sqlQuery)
					.setParameter("remarks", vaccineImmunizationMapperDto.getRemark())
					.setParameter("plannedDate", vaccineImmunizationMapperDto.getPlannedDate())
					.setParameter("location", vaccineImmunizationMapperDto.getLocation())
					.setParameter("vaccineImmunMapperId", vaccineImmunizationMapperDto.getVaccineImmunMapperId())
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}
		catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateVaccineImmunizationMapperForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@SuppressWarnings("unused")
	@Override
	public Response removeFreeTextVaccineForNewCov(VaccineImmunizationMapperDto vaccineImmunizationMapperDto)
			throws ApplicationException {
		try
		{
			Session session = sessionFactory.getCurrentSession();
			String sqlQuery = "UPDATE opd.t_vaccine_immunization_mapper SET "
					+ "status = :status "
					+ "WHERE vaccine_immunization_mapper_id = :vaccineImmunMapperId "
					+ "AND status = 'A' "
					+ "AND vaccine_name_text IS NOT NULL";
			int rowsUpdated = session.createSQLQuery(sqlQuery)
					.setParameter("status", vaccineImmunizationMapperDto.getStatus())
					.setParameter("vaccineImmunMapperId", vaccineImmunizationMapperDto.getVaccineImmunMapperId())
					.executeUpdate();
			if (rowsUpdated > 0) {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_CODE, "This vaccine can not be deleted", null, null);
			}
		}
		catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("removeFreeTextVaccineForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}



	@Override
	public Response batchJobToSendMessageForCycliceAppointmentDueDate(List<Integer> unitIds)
			throws ApplicationException {
		try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            // java 8+ Date/time API
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = null ;
            tomorrow =  today.plusDays(1);
            java.util.Date tomorrowUtilDate = java.sql.Date.valueOf(tomorrow);
            String currentDate = simpleDateFormat.format(tomorrowUtilDate);
            
            String query ="  select mam.organization_id as \"organizationId\", " + 
            		" mam.unit_id  as \"unitId\", " + 
            		" mam.patient_id as \"patientId\", " + 
            		" msm.doctor_id as \"doctorId\", " + 
            		" to_char(mam.appointment_date  ,'yyyy-MM-dd') as \"cyclicOrdScheduledDate\", " + 
            		"  to_char( msm.from_time  ,'HH:mm') as \"appointmentTime\"   " + 
            		"  from opd.m_appointment_master mam  " + 
            		" inner join opd.m_slot_master msm on  msm.slot_id = mam.slot_id  " +
            		"   where to_char(mam.appointment_date ,'yyyy-MM-dd') = :currentDate and  mam.is_cyclic_appointment = 'Y' "
            		+ " and mam.unit_id  in (:unitIds) ";

            List<SlotMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameter("currentDate", currentDate)
                    .setParameterList("unitIds", unitIds)
                    .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
         
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

        } catch (HibernateException he) {
        logger.error("batchJobToSendMessageForCycliceAppointmentDueDate failed", he);
        } catch (Exception e) {
        logger.error("batchJobToSendMessageForCycliceAppointmentDueDate failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response batchJobToSendMessageForCycliceAppointmentServiceDueDate(List<Integer> unitIds)
			throws ApplicationException {

		try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            // java 8+ Date/time API
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = null ;
            tomorrow =  today.plusDays(1);
            java.util.Date tomorrowUtilDate = java.sql.Date.valueOf(tomorrow);
            String currentDate = simpleDateFormat.format(tomorrowUtilDate);
            
            String query ="  select  " + 
            		"	to_char(tod.order_sch_date  ,'yyyy-MM-dd') as \"cyclicOrdScheduledDate\",  " + 
            		"	tom.patient_id as \"patientId\", " + 
            		"	tod.org_id as \"organizationId\", " + 
            		"	tod.org_unit_id as \"unitId\", " + 
            		"	msm.service_standard_name as \"serviceStandardName\" " + 
            		" from " + 
            		"	t_order_details tod " + 
            		"inner join t_order_master tom on tom.order_id = tod .order_id " + 
            		"inner join service.m_service_master msm  on tod.service_id  = msm.service_master_id " +
            		"   where to_char(tod.order_sch_date ,'yyyy-MM-dd') = :currentDate  "
            		+ " and tod.org_unit_id in (:unitIds) ";

            List<SlotMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameter("currentDate", currentDate)
                    .setParameterList("unitIds", unitIds)
                    .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
         
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

        } catch (HibernateException he) {
        logger.error("batchJobToSendMessageForCycliceAppointmentServiceDueDate failed", he);
        } catch (Exception e) {
        logger.error("batchJobToSendMessageForCycliceAppointmentServiceDueDate failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}



	@Override
	public Response dailyBasisPendingAmountIpPatientSMS(List<Integer> unitIds) throws ApplicationException {
		try {
            
            String query ="select sum(tod.self_payable) \"pendingSelfPayable\" , tom.patient_id as \"patientId\", tod.org_unit_id as \"orgUnitId\" , tod.org_id as \"orgId\" "   + 
            		" from t_order_details tod  "   + 
            		" inner join t_order_master tom on tod.order_id  = tom.order_id "   + 
            		" inner join service.m_service_master ser on tod.service_id = ser.service_master_id " + 
            		" inner join service.t_unit_service_master_details unitSer on ser.service_master_id = unitSer.service_master_id " + 
            		" left outer join billing.t_package_consumption_master pack on tod.order_details_id = pack.order_detail_id and (pack.status = 'A') " + 
            		" where tod.service_is_billed = 0 and tod.status = 'A'  and tom.visit_type_id != 1 " + 
            		" and unitSer.unit_id = tod.org_unit_id " + 
            		"	and tod.status = 'A' " + 
            		"	and unitSer.status = 'A' and tom.order_status = 'A'  and tod.debit_note_service = false and tod.org_unit_id in (:unitIds)" + 
            		" group by tom.patient_id  ,tod.org_unit_id ,tod.org_id ";

            List<OrderMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameterList("unitIds", unitIds)
                    .setResultTransformer(Transformers.aliasToBean(OrderMasterDto.class)).list();
         
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

        } catch (HibernateException he) {
        logger.error("dailyBasisPendingAmountIpPatientSMS failed", he);
        } catch (Exception e) {
        logger.error("dailyBasisPendingAmountIpPatientSMS failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	
	}



	@Override
	public Response dailyBasisDepositAmountIpPatientSMS(List<Integer> unitIds) throws ApplicationException {
		try {
            
            String query ="select sum(available_deposit)  \"depositAmount\", patient_id as \"patientId\"   from billing.m_deposit_master mdm  where deposit_type_id = 2 and unit_id in (:unitIds)" + 
            		" group by patient_id  ";
            List<OrderMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
                    .setParameterList("unitIds", unitIds)
                    .setResultTransformer(Transformers.aliasToBean(OrderMasterDto.class)).list();
         
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

        } catch (HibernateException he) {
        logger.error("dailyBasisDepositAmountIpPatientSMS failed", he);
        } catch (Exception e) {
        logger.error("dailyBasisDepositAmountIpPatientSMS failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

}
