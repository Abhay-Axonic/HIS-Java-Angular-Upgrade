package com.param.global.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.IntakeOutputChartDetailsDto;
import com.param.global.dto.IntakeOutputChartMasterDto;
import com.param.global.dto.OutputChartDetailsDto;
import com.param.global.model.CabinMaster;
import com.param.global.model.IntakeOutputChartDetailsMaster;
import com.param.global.model.IntakeOutputChartMaster;
import com.param.global.model.OutputChartDetailsMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class IntakeOutputChartDaoImpl extends GenericDao<IntakeOutputChartMaster, Integer> implements IIntakeOutputChartDao, ICommonConstants {

		final static Logger logger = Logger.getLogger(IntakeOutputChartDaoImpl.class);

		public IntakeOutputChartDaoImpl() {
			super(IntakeOutputChartMaster.class);
		}
		
		@Autowired
			SessionFactory sessionFactory;
		
		@Autowired
			DozerBeanMapper mapper;


@Override
public Response saveIntakeOutputChart(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
		throws ApplicationException {
	try {

		IntakeOutputChartMaster intakeOutputChartMaster=new IntakeOutputChartMaster();
		if(intakeOutputChartMasterDto.getIntakeTime()!=null) {
			LocalTime intakeTime1 = LocalTime.parse(intakeOutputChartMasterDto.getIntakeTime());
			LocalDate today = LocalDate.now();
			Instant intakeTime = intakeTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			intakeOutputChartMaster.setIntakeTime(intakeTime);
		}
		if(intakeOutputChartMasterDto.getOutputTime()!=null) {
			LocalTime outputTime1 = LocalTime.parse(intakeOutputChartMasterDto.getOutputTime());
			LocalDate today = LocalDate.now();
			Instant outputTime = outputTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

		intakeOutputChartMaster.setOutputTime(outputTime);
		}
		/*if(intakeOutputChartMasterDto.getTotalOutput()!=null) {
		intakeOutputChartMaster.setTotalOutput(intakeOutputChartMasterDto.getTotalOutput());
		}*/
		/*if(intakeOutputChartMasterDto.getTotalIntake()!=null) {
		intakeOutputChartMaster.setTotalIntake(intakeOutputChartMasterDto.getTotalIntake());
		}*/
		intakeOutputChartMaster.setPatientId(intakeOutputChartMasterDto.getPatientId());
		intakeOutputChartMaster.setIsIoStart(intakeOutputChartMasterDto.getIsIoStart());
		intakeOutputChartMaster.setAdmissionId(intakeOutputChartMasterDto.getAdmissionId());
		if(intakeOutputChartMasterDto.getBalance()!=null) {
		intakeOutputChartMaster.setBalance(intakeOutputChartMasterDto.getBalance());
		}
		intakeOutputChartMaster.setUserId(intakeOutputChartMasterDto.getUserId());
		intakeOutputChartMaster.setStatus(intakeOutputChartMasterDto.getStatus());
		intakeOutputChartMaster.setOrganizationId(intakeOutputChartMasterDto.getOrganizationId());
		intakeOutputChartMaster.setUnitId(intakeOutputChartMasterDto.getUnitId());
		if(intakeOutputChartMasterDto.getIntakeDate()!=null) {
		intakeOutputChartMaster.setIntakeDate(GlobalCommonDateUtils.getDate(intakeOutputChartMasterDto.getIntakeDate(), "dd-M-yyyy HH:mm:ss"));
		}
		if(intakeOutputChartMasterDto.getOutputDate()!=null) {
		intakeOutputChartMaster.setOutputDate(GlobalCommonDateUtils.getDate(intakeOutputChartMasterDto.getOutputDate(), "dd-M-yyyy HH:mm:ss"));
		}
		intakeOutputChartMaster.setCreatedBy(intakeOutputChartMasterDto.getCreatedBy());
		intakeOutputChartMaster.setCreatedDate(GlobalCommonDateUtils.getDate(intakeOutputChartMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
		intakeOutputChartMaster.setUpdatedBy(intakeOutputChartMasterDto.getCreatedBy());
		intakeOutputChartMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(intakeOutputChartMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		save(intakeOutputChartMaster);
		intakeOutputChartMasterDto.setIntakeOutputChartId(intakeOutputChartMaster.getIntakeOutputChartId());
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, intakeOutputChartMasterDto);
	} catch (HibernateException he) {
		logger.error("saveIntakeOutputChart failed", he);
	} catch (Exception e) {
		logger.error("saveIntakeOutputChart failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

@Override
public Response saveIntakeOutputChartDetails(IntakeOutputChartDetailsDto intakeOutputChartDetailsDto)
		throws ApplicationException {
			try {

				LocalTime startIntakeTime1 = LocalTime.parse(intakeOutputChartDetailsDto.getStartIntakeTime());
				LocalDate today = LocalDate.now();
				Instant startIntakeTime = startIntakeTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				IntakeOutputChartDetailsMaster intakeOutputChartDetailsMaster=new IntakeOutputChartDetailsMaster();
				intakeOutputChartDetailsMaster.setStartIntakeTime(startIntakeTime);
				intakeOutputChartDetailsMaster.setShiftId(intakeOutputChartDetailsDto.getShiftId());
				intakeOutputChartDetailsMaster.setTotalIntake(intakeOutputChartDetailsDto.getTotalIntake());
				intakeOutputChartDetailsMaster.setOrganizationId(intakeOutputChartDetailsDto.getOrganizationId());
				intakeOutputChartDetailsMaster.setUnitId(intakeOutputChartDetailsDto.getUnitId());
				intakeOutputChartDetailsMaster.setIntakeOutputChartId(intakeOutputChartDetailsDto.getIntakeOutputChartId());
				intakeOutputChartDetailsMaster.setCreatedBy(intakeOutputChartDetailsDto.getCreatedBy());
				intakeOutputChartDetailsMaster.setIntakeFluidId(intakeOutputChartDetailsDto.getIntakeFluidId());
				intakeOutputChartDetailsMaster.setIntakeTypeFluidId(intakeOutputChartDetailsDto.getIntakeTypeFluidId());
				intakeOutputChartDetailsMaster.setStartIntakeDate(GlobalCommonDateUtils.getDate(intakeOutputChartDetailsDto.getStartIntakeDate(), "dd-M-yyyy HH:mm:ss"));
				intakeOutputChartDetailsMaster.setStatus(intakeOutputChartDetailsDto.getStatus());
				intakeOutputChartDetailsMaster.setStartIntakeQuantity(intakeOutputChartDetailsDto.getStartIntakeQuantity());
				intakeOutputChartDetailsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(intakeOutputChartDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				intakeOutputChartDetailsMaster.setUpdatedBy(intakeOutputChartDetailsDto.getUpdatedBy());
				intakeOutputChartDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(intakeOutputChartDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				sessionFactory.getCurrentSession().save(intakeOutputChartDetailsMaster);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
					} catch (HibernateException he) {
				logger.error("saveIntakeOutputChartDetails failed", he);
					} catch (Exception e) {
				logger.error("saveIntakeOutputChartDetails failed", e);
				}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}

@Override
public Response saveOutputChartDetails(OutputChartDetailsDto outputChartDetailsDto)
		throws ApplicationException {
	try {

		LocalTime startOutputTime1 = LocalTime.parse(outputChartDetailsDto.getStartOutputTime());
		LocalDate today = LocalDate.now();
		Instant startOutputTime = startOutputTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

		OutputChartDetailsMaster outputChartDetailsMaster=new OutputChartDetailsMaster();
		outputChartDetailsMaster.setStartOutputTime(startOutputTime);
		outputChartDetailsMaster.setShiftId(outputChartDetailsDto.getShiftId());
		outputChartDetailsMaster.setTotalOutput(outputChartDetailsDto.getTotalOutput());
		outputChartDetailsMaster.setOrganizationId(outputChartDetailsDto.getOrganizationId());
		outputChartDetailsMaster.setUnitId(outputChartDetailsDto.getUnitId());
		outputChartDetailsMaster.setIntakeOutputChartId(outputChartDetailsDto.getIntakeOutputChartId());
		outputChartDetailsMaster.setCreatedBy(outputChartDetailsDto.getCreatedBy());
		outputChartDetailsMaster.setOutputTypeId(outputChartDetailsDto.getOutputTypeId());
		outputChartDetailsMaster.setStartOutputDate(GlobalCommonDateUtils.getDate(outputChartDetailsDto.getStartOutputDate(), "dd-M-yyyy HH:mm:ss"));
		outputChartDetailsMaster.setStatus(outputChartDetailsDto.getStatus());
		outputChartDetailsMaster.setStartOutputQuantity(outputChartDetailsDto.getStartOutputQuantity());
		outputChartDetailsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(outputChartDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
		outputChartDetailsMaster.setUpdatedBy(outputChartDetailsDto.getUpdatedBy());
		outputChartDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(outputChartDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		sessionFactory.getCurrentSession().save(outputChartDetailsMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			} catch (HibernateException he) {
		logger.error("saveOutputChartDetails failed", he);
			} catch (Exception e) {
		logger.error("saveOutputChartDetails failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

	@Override
	public Response getIntakeOutputChartListByPatientId(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
		throws ApplicationException {
	try {
		List<IntakeOutputChartMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_INTAKE_OUTPUT_CHART_LIST_BY_PATIENT_ID")
				.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
				.setParameter("unitId",  intakeOutputChartMasterDto.getUnitId())
				.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
				.setParameter("createdDate",  intakeOutputChartMasterDto.getCreatedDate())
				.setResultTransformer(Transformers.aliasToBean(IntakeOutputChartMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getIntakeOutputChartListByPatientId failed", he);
		} catch (Exception e) {
			logger.error("getIntakeOutputChartListByPatientId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIntakeOutputChartHistoryListByDate(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
		throws ApplicationException {
	try {
		List<IntakeOutputChartMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_INTAKE_OUTPUT_CHART_HISTORY_LIST_BY_DATE")
				.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
				.setParameter("unitId",  intakeOutputChartMasterDto.getUnitId())
				.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
				.setParameter("fromDate",  intakeOutputChartMasterDto.getFromDate())
				.setParameter("todate",  intakeOutputChartMasterDto.getToDate())
				.setResultTransformer(Transformers.aliasToBean(IntakeOutputChartMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getIntakeOutputChartHistoryListByDate failed", he);
		} catch (Exception e) {
			logger.error("getIntakeOutputChartHistoryListByDate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIntakeHistoryList(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			List<IntakeOutputChartDetailsDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INTAKE_HISTORY_LIST")
					.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
					.setParameter("unitId",  intakeOutputChartMasterDto.getUnitId())
					.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
					.setParameter("shiftId",  intakeOutputChartMasterDto.getShiftId())
					.setParameter("intakeDate",  intakeOutputChartMasterDto.getIntakeDate())
//					.setParameter("startIntakeTime",  intakeOutputChartMasterDto.getStartIntakeTime())
					.setResultTransformer(Transformers.aliasToBean(IntakeOutputChartDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getIntakeHistoryList failed", he);
			} catch (Exception e) {
				logger.error("getIntakeHistoryList failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getOutputHistoryList(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			List<OutputChartDetailsDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OUTPUT_HISTORY_LIST")
					.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
					.setParameter("unitId",  intakeOutputChartMasterDto.getUnitId())
					.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
					.setParameter("shiftId",  intakeOutputChartMasterDto.getShiftId())
					.setParameter("intakeDate",  intakeOutputChartMasterDto.getIntakeDate())
//					.setParameter("startOutputTime",  intakeOutputChartMasterDto.getStartOutputTime())
					.setResultTransformer(Transformers.aliasToBean(OutputChartDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getOutputHistoryList failed", he);
			} catch (Exception e) {
				logger.error("getOutputHistoryList failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getInputOutputChartHistoryDetailsList(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			List<IntakeOutputChartMasterDto> list=new ArrayList<>();
			List<IntakeOutputChartMasterDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INPUT_OUTPUT_CHART_HISTORY_BY_DATE_FOR_INPUT")
					.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
					.setParameter("unitId",  intakeOutputChartMasterDto.getUnitId())
					.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
					.setParameter("fromDate",  intakeOutputChartMasterDto.getFromDate())
					.setParameter("todate",  intakeOutputChartMasterDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(IntakeOutputChartMasterDto.class)).list();

			for(IntakeOutputChartMasterDto dto : list1) 
			{			
				List<IntakeOutputChartMasterDto> list2=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_INPUT_OUTPUT_CHART_HISTORY_FOR_SHIFT")
						.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
						.setParameter("unitId",  intakeOutputChartMasterDto.getUnitId())
						.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
//						.setParameter("fromDate",  intakeOutputChartMasterDto.getFromDate())
//						.setParameter("todate",  intakeOutputChartMasterDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(IntakeOutputChartMasterDto.class)).list();
				
				dto.setIntakeDateList(list2);
				list.add(dto);
				
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
	} catch (HibernateException he) {
		logger.error("getInputOutputChartHistoryDetailsList failed", he);
	} catch (Exception e) {
		logger.error("getInputOutputChartHistoryDetailsList failed", e);
	}
	return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response getInputOutputChartHistoryByDate(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
//			List<IntakeOutputChartMasterDto> list =new ArrayList();
			List<IntakeOutputChartMasterDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("")
				.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
				.setParameter("unitId",  intakeOutputChartMasterDto.getUnitId())
				.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
				.setParameter("fromDate",  intakeOutputChartMasterDto.getFromDate())
				.setParameter("todate",  intakeOutputChartMasterDto.getToDate())
				.setResultTransformer(Transformers.aliasToBean(IntakeOutputChartMasterDto.class)).list();
			
			/*List<IntakeOutputChartMasterDto> list2=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INPUT_OUTPUT_CHART_HISTORY_BY_DATE_FOR_OUTPUT")
					.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
					.setParameter("unitId",  intakeOutputChartMasterDto.getUnitId())
					.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
					.setParameter("fromDate",  intakeOutputChartMasterDto.getFromDate())
					.setParameter("todate",  intakeOutputChartMasterDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(IntakeOutputChartMasterDto.class)).list();
			
			for (IntakeOutputChartMasterDto intakeOutputChartMasterDto2 : list1) {
				for (IntakeOutputChartMasterDto intakeOutputChartMasterDto3 : list2) {
				if(intakeOutputChartMasterDto2.getIntakeDate()==intakeOutputChartMasterDto3.getOutputDate()) {
					list.add(intakeOutputChartMasterDto2);
				}
				else {
					list.add(intakeOutputChartMasterDto3);
				}
				}
			}*/
			
			/*for (IntakeOutputChartMasterDto intakeOutputChartMasterDto3 : list2) {
				if(intakeOutputChartMasterDto3.getOutputDate()!=intakeOutputChartMasterDto.getIntakeDate()) {
				
				}*/
//			}
			
			
				return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
	} catch (HibernateException he) {
		logger.error("getInputOutputChartHistoryByDate failed", he);
	} catch (Exception e) {
		logger.error("getInputOutputChartHistoryByDate failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateInputOutputStartStatus(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
		
			LocalTime outputTime = LocalTime.parse( intakeOutputChartMasterDto.getOutputTime());
			String date=GlobalCommonDateUtils.getAvalDate(TimeZoneComponent.getDateByzone(intakeOutputChartMasterDto.getUnitId()), "yyyy-M-dd");
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update adt.t_intake_output_chart set is_io_start ="+intakeOutputChartMasterDto.getIsIoStart()
			+" , output_date="+"'"+intakeOutputChartMasterDto.getOutputDate()+"'"
			+" , output_time="+"'"+date+" "+outputTime+"'"
			+ " where patient_id="+intakeOutputChartMasterDto.getPatientId())
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, "I/O chart stopped successfully", null, null);

			} catch (HibernateException he) {
				logger.error("updateInputOutputStartStatus failed", he);
			} catch (Exception e) {
				logger.error("updateInputOutputStartStatus failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

	@Override
	public Response updateStatusForStartIOChart(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
				Session session=sessionFactory.getCurrentSession();
					session.createSQLQuery(" update adt.t_intake_output_chart set is_io_start = "+intakeOutputChartMasterDto.getIsIoStart()
											+ " where patient_id="+intakeOutputChartMasterDto.getPatientId())
				.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, "I/O chart has started", null, null);

			} catch (HibernateException he) {
				logger.error("updateStatusForStartIOChart failed", he);
			} catch (Exception e) {
				logger.error("updateStatusForStartIOChart failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

	@Override
	public Response getIntakeAndOutputDateByPatientId(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			List<IntakeOutputChartMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INTAKE_AND_OUTPUT_DATE_BY_PATIENT_ID")
					.setParameter("organizationId",  intakeOutputChartMasterDto.getOrganizationId())
					.setParameter("patientId",  intakeOutputChartMasterDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(IntakeOutputChartMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getIntakeAndOutputDateByPatientId failed", he);
			} catch (Exception e) {
				logger.error("getIntakeAndOutputDateByPatientId failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateIntakeOutputChart(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {





			IntakeOutputChartMaster intakeOutputChartMaster=findById(intakeOutputChartMasterDto.getIntakeOutputChartId());
			if(intakeOutputChartMasterDto.getIntakeTime()!=null) {
				LocalTime intakeTime1 = LocalTime.parse(intakeOutputChartMasterDto.getIntakeTime());
				LocalDate today = LocalDate.now();
				Instant intakeTime = intakeTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
				intakeOutputChartMaster.setIntakeTime(intakeTime);
			}
			if(intakeOutputChartMasterDto.getOutputTime()!=null) {
				LocalTime outputTime1 = LocalTime.parse(intakeOutputChartMasterDto.getOutputTime());
				LocalDate today = LocalDate.now();
				Instant outputTime = outputTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
				intakeOutputChartMaster.setOutputTime(outputTime);
			}
			intakeOutputChartMaster.setPatientId(intakeOutputChartMasterDto.getPatientId());
			intakeOutputChartMaster.setIsIoStart(intakeOutputChartMasterDto.getIsIoStart());
			intakeOutputChartMaster.setAdmissionId(intakeOutputChartMasterDto.getAdmissionId());
			if(intakeOutputChartMasterDto.getBalance()!=null) {
			intakeOutputChartMaster.setBalance(intakeOutputChartMasterDto.getBalance());
			}
			intakeOutputChartMaster.setUserId(intakeOutputChartMasterDto.getUserId());
			intakeOutputChartMaster.setStatus(intakeOutputChartMasterDto.getStatus());
			intakeOutputChartMaster.setOrganizationId(intakeOutputChartMasterDto.getOrganizationId());
			intakeOutputChartMaster.setUnitId(intakeOutputChartMasterDto.getUnitId());
			if(intakeOutputChartMasterDto.getIntakeDate()!=null) {
			intakeOutputChartMaster.setIntakeDate(GlobalCommonDateUtils.getDate(intakeOutputChartMasterDto.getIntakeDate(), "dd-M-yyyy HH:mm:ss"));
			}
			if(intakeOutputChartMasterDto.getOutputDate()!=null) {
			intakeOutputChartMaster.setOutputDate(GlobalCommonDateUtils.getDate(intakeOutputChartMasterDto.getOutputDate(), "dd-M-yyyy HH:mm:ss"));
			}
			intakeOutputChartMaster.setCreatedBy(intakeOutputChartMasterDto.getCreatedBy());
			intakeOutputChartMaster.setCreatedDate(GlobalCommonDateUtils.getDate(intakeOutputChartMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			intakeOutputChartMaster.setUpdatedBy(intakeOutputChartMasterDto.getCreatedBy());
			intakeOutputChartMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(intakeOutputChartMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			intakeOutputChartMaster.setIntakeOutputChartId(intakeOutputChartMasterDto.getIntakeOutputChartId());
			update(intakeOutputChartMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, intakeOutputChartMasterDto);
		} catch (HibernateException he) {
		logger.error("updateIntakeOutputChart failed", he);
		} catch (Exception e) {
		logger.error("updateIntakeOutputChart failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

	@Override
	public Response updateIntakeOutputChartDetails(IntakeOutputChartDetailsDto intakeOutputChartDetailsDto)
			throws ApplicationException {
		try {
			LocalTime startIntakeTime1 = LocalTime.parse(intakeOutputChartDetailsDto.getStartIntakeTime());
			LocalDate today = LocalDate.now();
			Instant startIntakeTime = startIntakeTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			IntakeOutputChartDetailsMaster intakeOutputChartDetailsMaster=new IntakeOutputChartDetailsMaster();
			intakeOutputChartDetailsMaster.setStartIntakeTime(startIntakeTime);
			intakeOutputChartDetailsMaster.setShiftId(intakeOutputChartDetailsDto.getShiftId());
			intakeOutputChartDetailsMaster.setTotalIntake(intakeOutputChartDetailsDto.getTotalIntake());
			intakeOutputChartDetailsMaster.setOrganizationId(intakeOutputChartDetailsDto.getOrganizationId());
			intakeOutputChartDetailsMaster.setUnitId(intakeOutputChartDetailsDto.getUnitId());
			intakeOutputChartDetailsMaster.setIntakeOutputChartId(intakeOutputChartDetailsDto.getIntakeOutputChartId());
			intakeOutputChartDetailsMaster.setCreatedBy(intakeOutputChartDetailsDto.getCreatedBy());
			intakeOutputChartDetailsMaster.setIntakeFluidId(intakeOutputChartDetailsDto.getIntakeFluidId());
			intakeOutputChartDetailsMaster.setIntakeTypeFluidId(intakeOutputChartDetailsDto.getIntakeTypeFluidId());
			intakeOutputChartDetailsMaster.setStartIntakeDate(GlobalCommonDateUtils.getDate(intakeOutputChartDetailsDto.getStartIntakeDate(), "dd-M-yyyy HH:mm:ss"));
			intakeOutputChartDetailsMaster.setStatus(intakeOutputChartDetailsDto.getStatus());
			intakeOutputChartDetailsMaster.setStartIntakeQuantity(intakeOutputChartDetailsDto.getStartIntakeQuantity());
			intakeOutputChartDetailsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(intakeOutputChartDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			intakeOutputChartDetailsMaster.setUpdatedBy(intakeOutputChartDetailsDto.getUpdatedBy());
			intakeOutputChartDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(intakeOutputChartDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			intakeOutputChartDetailsMaster.setIntakeOutputChartDetailsId(intakeOutputChartDetailsDto.getIntakeOutputChartDetailsId());
			sessionFactory.getCurrentSession().saveOrUpdate(intakeOutputChartDetailsMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				} catch (HibernateException he) {
				logger.error("updateIntakeOutputChartDetails failed", he);
				} catch (Exception e) {
				logger.error("updateIntakeOutputChartDetails failed", e);
				}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateOutputChartDetails(OutputChartDetailsDto outputChartDetailsDto) throws ApplicationException {
		try {
			LocalTime startOutputTime1 = LocalTime.parse(outputChartDetailsDto.getStartOutputTime());
			LocalDate today = LocalDate.now();
			Instant startOutputTime = startOutputTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			OutputChartDetailsMaster outputChartDetailsMaster=new OutputChartDetailsMaster();
			outputChartDetailsMaster.setStartOutputTime(startOutputTime);
			outputChartDetailsMaster.setShiftId(outputChartDetailsDto.getShiftId());
			outputChartDetailsMaster.setTotalOutput(outputChartDetailsDto.getTotalOutput());
			outputChartDetailsMaster.setOrganizationId(outputChartDetailsDto.getOrganizationId());
			outputChartDetailsMaster.setUnitId(outputChartDetailsDto.getUnitId());
			outputChartDetailsMaster.setIntakeOutputChartId(outputChartDetailsDto.getIntakeOutputChartId());
			outputChartDetailsMaster.setCreatedBy(outputChartDetailsDto.getCreatedBy());
			outputChartDetailsMaster.setOutputTypeId(outputChartDetailsDto.getOutputTypeId());
			outputChartDetailsMaster.setStartOutputDate(GlobalCommonDateUtils.getDate(outputChartDetailsDto.getStartOutputDate(), "dd-M-yyyy HH:mm:ss"));
			outputChartDetailsMaster.setStatus(outputChartDetailsDto.getStatus());
			outputChartDetailsMaster.setStartOutputQuantity(outputChartDetailsDto.getStartOutputQuantity());
			outputChartDetailsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(outputChartDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			outputChartDetailsMaster.setUpdatedBy(outputChartDetailsDto.getUpdatedBy());
			outputChartDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(outputChartDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			outputChartDetailsMaster.setOutputChartDetailsId(outputChartDetailsDto.getOutputChartDetailsId());
			sessionFactory.getCurrentSession().saveOrUpdate(outputChartDetailsMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				} catch (HibernateException he) {
				logger.error("updateOutputChartDetails failed", he);
				} catch (Exception e) {
				logger.error("updateOutputChartDetails failed", e);
				}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

	@Override
	public Response deleteStatusForStartIOChart(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update adt.t_intake_output_chart_details set status='I' "
										+ " where intake_output_chart_details_id="+intakeOutputChartMasterDto.getIntakeOutputChartDetailsId())
			.executeUpdate();
		
		return new Response(SUCCESS, SUCCESS_CODE, "I/O chart deleted successfully", null, null);
		
		} catch (HibernateException he) {
		logger.error("deleteStatusForStartIOChart failed", he);
		} catch (Exception e) {
		logger.error("deleteStatusForStartIOChart failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response deleteStatusForOutputChart(OutputChartDetailsDto outputChartDetailsDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update adt.t_output_chart_details set status='I' "
										+ " where output_chart_details_id="+outputChartDetailsDto.getOutputChartDetailsId())
			.executeUpdate();
		
		return new Response(SUCCESS, SUCCESS_CODE, "I/O chart deleted successfully", null, null);
		
		} catch (HibernateException he) {
		logger.error("deleteStatusForOutputChart failed", he);
		} catch (Exception e) {
		logger.error("deleteStatusForOutputChart failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

}
