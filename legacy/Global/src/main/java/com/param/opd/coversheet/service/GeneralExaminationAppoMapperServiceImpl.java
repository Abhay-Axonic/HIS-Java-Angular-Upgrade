package com.param.opd.coversheet.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CustomHistoryConfigurationDetailsDto;
import com.param.global.dto.CustomHistoryConfigurationDto;
import com.param.global.model.CustomHistoryConfigurationDetails;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dao.IGeneralExaminationAppoMapperDao;
import com.param.opd.coversheet.dto.GeneralExaminationAppoMapperDto;
import com.param.opd.coversheet.model.GeneralExaminationAppoMapper;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GeneralExaminationAppoMapperServiceImpl implements IGeneralExaminationAppoMapperService, ICommonConstants{
	final static Logger logger = Logger.getLogger(GeneralExaminationAppoMapperServiceImpl.class);


	@Autowired
	private IGeneralExaminationAppoMapperDao iGeneralExaminationAppoMapperDao;
	
	@Override
	public Response saveGeneralExaminationMapper(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)throws ApplicationException {
		try
		{
			Response res=iGeneralExaminationAppoMapperDao.checkGeneralExaminationMapperAvailable(generalExaminationAppoMapperDto);
				if(res.getListObject()!=null && res.getListObject().size()>0){
					
					return iGeneralExaminationAppoMapperDao.updateGeneralExaminationMapper(generalExaminationAppoMapperDto);
			}else {
				if(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().size()!= 0) {
					for(int i=0;i<generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().size();i++)
					{	
						if(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation() != null 
								&& generalExaminationAppoMapperDto.getIsNADValue() != YES)
						{
							for(int j=0;j<generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().size();j++)
							{
								if(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getListOfGeneralSystemProperty() != null 
										&& generalExaminationAppoMapperDto.getIsNADValue() != YES
										&& (generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getRemark().equals(null) 
										|| generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getRemark().equals("")))
								{
									for(int k=0;k<generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getListOfGeneralSystemProperty().size();k++)
									{
										
										GeneralExaminationAppoMapper generalExaminationAppoMapper = new GeneralExaminationAppoMapper();
										
										generalExaminationAppoMapper.setSystemId(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getSystemId());
										generalExaminationAppoMapper.setSystemObervationId(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getSystemObervationId());
										generalExaminationAppoMapper.setSystemObervationPropertyId(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getListOfGeneralSystemProperty().get(k).getSystemObervationPropertyId());
										generalExaminationAppoMapper.setIsNADValue(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getIsNADValue());
										generalExaminationAppoMapper.setRemark(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getListOfGeneralSystemProperty().get(k).getRemark());
										if(generalExaminationAppoMapperDto.getEncounterId()!=null){
											generalExaminationAppoMapper.setEncounterId(generalExaminationAppoMapperDto.getEncounterId());
										}else{
											generalExaminationAppoMapper.setAdmissionId(generalExaminationAppoMapperDto.getAdmissionId());

										}
										generalExaminationAppoMapper.setDescriptiveExamination(generalExaminationAppoMapperDto.getDescriptiveExamination());
										generalExaminationAppoMapper.setPatientId(generalExaminationAppoMapperDto.getPatientId());
										generalExaminationAppoMapper.setCreatedBy(generalExaminationAppoMapperDto.getCreatedBy());
										generalExaminationAppoMapper.setCreatedDate(CommonDateUtils.getDate(generalExaminationAppoMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
										generalExaminationAppoMapper.setOrganizationId(generalExaminationAppoMapperDto.getOrganizationId());
										generalExaminationAppoMapper.setStatus(generalExaminationAppoMapperDto.getStatus());
										generalExaminationAppoMapper.setUnitId(generalExaminationAppoMapperDto.getUnitId());
										generalExaminationAppoMapper.setUpdatedBy(generalExaminationAppoMapperDto.getUpdatedBy());
										generalExaminationAppoMapper.setUpdatedDate(CommonDateUtils.getDate(generalExaminationAppoMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
										generalExaminationAppoMapper.setTypeId(generalExaminationAppoMapperDto.getTypeId());
										
										iGeneralExaminationAppoMapperDao.saveGeneralExamSystemProperty(generalExaminationAppoMapper);
										
									}//END SYSTEM PROPERTY FOR.
								
								}//END SYSTEM PROPERTY IF.
								else
								{
									GeneralExaminationAppoMapper generalExaminationAppoMapper = new GeneralExaminationAppoMapper();
									
									generalExaminationAppoMapper.setSystemId(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getSystemId());
									generalExaminationAppoMapper.setSystemObervationId(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getSystemObervationId());
									generalExaminationAppoMapper.setIsNADValue(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getIsNADValue());
									generalExaminationAppoMapper.setRemark(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getListOfGeneralSystemObservation().get(j).getRemark());
									
									if(generalExaminationAppoMapperDto.getEncounterId()!=null){
										generalExaminationAppoMapper.setEncounterId(generalExaminationAppoMapperDto.getEncounterId());
									}else{
										generalExaminationAppoMapper.setAdmissionId(generalExaminationAppoMapperDto.getAdmissionId());

									}
									generalExaminationAppoMapper.setPatientId(generalExaminationAppoMapperDto.getPatientId());
									generalExaminationAppoMapper.setDescriptiveExamination(generalExaminationAppoMapperDto.getDescriptiveExamination());
									generalExaminationAppoMapper.setCreatedBy(generalExaminationAppoMapperDto.getCreatedBy());
									generalExaminationAppoMapper.setCreatedDate(CommonDateUtils.getDate(generalExaminationAppoMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
									generalExaminationAppoMapper.setOrganizationId(generalExaminationAppoMapperDto.getOrganizationId());
									generalExaminationAppoMapper.setStatus(generalExaminationAppoMapperDto.getStatus());
									generalExaminationAppoMapper.setUnitId(generalExaminationAppoMapperDto.getUnitId());
									generalExaminationAppoMapper.setUpdatedBy(generalExaminationAppoMapperDto.getUpdatedBy());
									generalExaminationAppoMapper.setUpdatedDate(CommonDateUtils.getDate(generalExaminationAppoMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
									generalExaminationAppoMapper.setTypeId(generalExaminationAppoMapperDto.getTypeId());
									
									iGeneralExaminationAppoMapperDao.saveGeneralExaminationObservationMapper(generalExaminationAppoMapper);
								}
								
							}//END SYSTEM OBSERVATION FOR LOOP.
						}//END SYSTEM OBSERVATION IF LOOP.
						
						
						//CODE FOR SAVE SYSTEM 
						if(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getIsNADValue() != NO)
						{
							GeneralExaminationAppoMapper generalExaminationAppoMapper = new GeneralExaminationAppoMapper();
							
							generalExaminationAppoMapper.setSystemId(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getSystemId());
							generalExaminationAppoMapper.setIsNADValue(generalExaminationAppoMapperDto.getListOfListOfGeneralExamSystemMaster().get(i).getIsNADValue());
							
//							generalExaminationAppoMapper.setEncounterId(generalExaminationAppoMapperDto.getEncounterId());
							if(generalExaminationAppoMapperDto.getEncounterId()!=null){
								generalExaminationAppoMapper.setEncounterId(generalExaminationAppoMapperDto.getEncounterId());
							}else{
								generalExaminationAppoMapper.setAdmissionId(generalExaminationAppoMapperDto.getAdmissionId());

							}
							generalExaminationAppoMapper.setDescriptiveExamination(generalExaminationAppoMapperDto.getDescriptiveExamination());
							generalExaminationAppoMapper.setPatientId(generalExaminationAppoMapperDto.getPatientId());
							generalExaminationAppoMapper.setCreatedBy(generalExaminationAppoMapperDto.getCreatedBy());
							generalExaminationAppoMapper.setCreatedDate(CommonDateUtils.getDate(generalExaminationAppoMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
							generalExaminationAppoMapper.setOrganizationId(generalExaminationAppoMapperDto.getOrganizationId());
							generalExaminationAppoMapper.setStatus(generalExaminationAppoMapperDto.getStatus());
							generalExaminationAppoMapper.setUnitId(generalExaminationAppoMapperDto.getUnitId());
							generalExaminationAppoMapper.setUpdatedBy(generalExaminationAppoMapperDto.getUpdatedBy());
							generalExaminationAppoMapper.setUpdatedDate(CommonDateUtils.getDate(generalExaminationAppoMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
							generalExaminationAppoMapper.setTypeId(generalExaminationAppoMapperDto.getTypeId());
							iGeneralExaminationAppoMapperDao.saveGeneralExaminationSystem(generalExaminationAppoMapper);
							
						}
						
					}
				}else{
					GeneralExaminationAppoMapper generalExaminationAppoMapper = new GeneralExaminationAppoMapper();
					if(generalExaminationAppoMapperDto.getEncounterId()!=null){
						generalExaminationAppoMapper.setEncounterId(generalExaminationAppoMapperDto.getEncounterId());
					}else{
						generalExaminationAppoMapper.setAdmissionId(generalExaminationAppoMapperDto.getAdmissionId());

					}
					generalExaminationAppoMapper.setDescriptiveExamination(generalExaminationAppoMapperDto.getDescriptiveExamination());
					generalExaminationAppoMapper.setPatientId(generalExaminationAppoMapperDto.getPatientId());
					generalExaminationAppoMapper.setRemark(null);
					generalExaminationAppoMapper.setIsNADValue('N');
					generalExaminationAppoMapper.setSystemId(null);
					generalExaminationAppoMapper.setSystemObervationId(null);
					generalExaminationAppoMapper.setSystemObervationPropertyId(null);
					generalExaminationAppoMapper.setCreatedBy(generalExaminationAppoMapperDto.getCreatedBy());
					generalExaminationAppoMapper.setCreatedDate(CommonDateUtils.getDate(generalExaminationAppoMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					generalExaminationAppoMapper.setOrganizationId(generalExaminationAppoMapperDto.getOrganizationId());
					generalExaminationAppoMapper.setStatus(generalExaminationAppoMapperDto.getStatus());
					generalExaminationAppoMapper.setUnitId(generalExaminationAppoMapperDto.getUnitId());
					generalExaminationAppoMapper.setUpdatedBy(generalExaminationAppoMapperDto.getUpdatedBy());
					generalExaminationAppoMapper.setUpdatedDate(CommonDateUtils.getDate(generalExaminationAppoMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					generalExaminationAppoMapper.setTypeId(generalExaminationAppoMapperDto.getTypeId());
					iGeneralExaminationAppoMapperDao.saveGeneralExaminationSystem(generalExaminationAppoMapper);
				}
			}
			
			
			
			
			//END SYSTEM MASER FOR LOOP.
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("saveGeneralExaminationMapper failed", he);
		} catch (Exception e) {
		logger.error("saveGeneralExaminationMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	@Transactional
	public Response getListOfGeneralExaminationMapper(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iGeneralExaminationAppoMapperDao.getListOfGeneralExaminationMapper(generalExaminationAppoMapperDto);
	}

	@Override
	@Transactional
	public Response getListOfSavedGeneralExaminationMapper(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iGeneralExaminationAppoMapperDao.getListOfSavedGeneralExaminationMapper(generalExaminationAppoMapperDto);
	}

	@Override
	public Response getListOfSystemsAndObservation(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iGeneralExaminationAppoMapperDao.getListOfSystemsAndObservation(generalExaminationAppoMapperDto);
	}

	@Override
	public Response getListOfSystemsAndObservationForCaseSheet(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		// TODO Auto-generated method stub
		return iGeneralExaminationAppoMapperDao.getListOfSystemsAndObservationForCaseSheet(generalExaminationAppoMapperDto);
	}

	@Override
	public Response updateGeneralExaminationMapper(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) {
		// TODO Auto-generated method stub
		return iGeneralExaminationAppoMapperDao.updateGeneralExaminationMapper(generalExaminationAppoMapperDto);
	}

	@Override
	public Response getListOfSystemsAndObservationForBookView(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)throws ApplicationException {
		// TODO Auto-generated method stub
		return iGeneralExaminationAppoMapperDao.getListOfSystemsAndObservationForBookView(generalExaminationAppoMapperDto);
	}
	
	@Override
	public Response getGeneralAndSystemicExaminationMapperList(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try {

//			return iGeneralExaminationAppoMapperDao.getGeneralAndSystemicExaminationMapperList(generalExaminationAppoMapperDto);
			

			Response res =  iGeneralExaminationAppoMapperDao.getGeneralAndSystemicExaminationMapperList(generalExaminationAppoMapperDto);
			List<GeneralExaminationAppoMapperDto> getResList = res.getListObject();
			
			List<GeneralExaminationAppoMapperDto> generalAndSystemicExaminationList = new ArrayList<GeneralExaminationAppoMapperDto>();
			
			List<GeneralExaminationAppoMapperDto> chekedList = new ArrayList<GeneralExaminationAppoMapperDto>();
			
			Response checkRes=iGeneralExaminationAppoMapperDao.getGenAndSysExaminMapperPatDetailstForNewCov(generalExaminationAppoMapperDto);
			List<GeneralExaminationAppoMapperDto> checkResList = checkRes.getListObject();
			
			for (GeneralExaminationAppoMapperDto getDtoListMain : getResList) {
				if(getDtoListMain.getObservationId() != null) {
					GeneralExaminationAppoMapperDto addDto=new GeneralExaminationAppoMapperDto();
					addDto.setSystemId(getDtoListMain.getSystemId());
					addDto.setSystemName(getDtoListMain.getSystemName());
					addDto.setStatus(getDtoListMain.getStatus());
					addDto.setObservationId(getDtoListMain.getObservationId());
					addDto.setObservationName(getDtoListMain.getObservationName());
					addDto.setTypeId(getDtoListMain.getTypeId());
					addDto.setPatientId(generalExaminationAppoMapperDto.getPatientId());
					addDto.setUnitId(generalExaminationAppoMapperDto.getUnitId());
					addDto.setOrganizationId(generalExaminationAppoMapperDto.getOrganizationId());
					chekedList.add(addDto);
				}
				
			}
			
			if(chekedList!=null&& chekedList!=null && chekedList.size()>0) {
				List<GeneralExaminationAppoMapperDto> mappedListOfGenSysMapper = chekedList;
				for(GeneralExaminationAppoMapperDto dto :mappedListOfGenSysMapper) {
					Response rest=iGeneralExaminationAppoMapperDao.checkGenAndSystExamMapprAvailable(dto);
					if(rest.getListObject().size() == 0) {
						
					if(dto.getObservationId() != null) {
					GeneralExaminationAppoMapperDto  genExam = new GeneralExaminationAppoMapperDto();
					genExam.setEncounterId(generalExaminationAppoMapperDto.getEncounterId());
					genExam.setAdmissionId(generalExaminationAppoMapperDto.getAdmissionId());
					genExam.setCreatedBy(generalExaminationAppoMapperDto.getCreatedBy());
					genExam.setPatientId(generalExaminationAppoMapperDto.getPatientId());
					genExam.setDescriptiveExamination("");
					genExam.setIsNADValue('N');
					genExam.setOrganizationId(generalExaminationAppoMapperDto.getOrganizationId());
					genExam.setRemark("");
					genExam.setStatus('A');
					genExam.setSystemId(dto.getSystemId());
//					genExam.setSystemObervationPropertyId(dto.getSystemObervationPropertyId());
					genExam.setSystemObervationId(dto.getObservationId());
					genExam.setTypeId(generalExaminationAppoMapperDto.getTypeId());
					genExam.setUnitId(generalExaminationAppoMapperDto.getUnitId());
					genExam.setUpdatedBy(generalExaminationAppoMapperDto.getUpdatedBy());
					genExam.setCreatedDate(generalExaminationAppoMapperDto.getCreatedDate());
					genExam.setUpdatedDate(generalExaminationAppoMapperDto.getUpdatedDate());
					
					Response detsilSaveRes =  iGeneralExaminationAppoMapperDao.saveGeneralAndSystemicExaminationMapperForNewCoversheet(genExam);
					if(detsilSaveRes!=null && detsilSaveRes.getObject()!=null) {
						GeneralExaminationAppoMapper mstr = (GeneralExaminationAppoMapper)detsilSaveRes.getObject();
						GeneralExaminationAppoMapperDto newDto = new GeneralExaminationAppoMapperDto();
						newDto.setEncounterId(mstr.getEncounterId());
						newDto.setAdmissionId(mstr.getAdmissionId());
						newDto.setGeneralExamAppoMapperId(mstr.getGeneralExamAppoMapperId());
						newDto.setCreatedBy(mstr.getCreatedBy());
						newDto.setPatientId(mstr.getPatientId());
						newDto.setDescriptiveExamination(mstr.getDescriptiveExamination());
						newDto.setIsNADValue(mstr.getIsNADValue());
						newDto.setOrganizationId(mstr.getOrganizationId());
						newDto.setRemark(mstr.getRemark());
						newDto.setStatus(mstr.getStatus());
						newDto.setSystemId(mstr.getSystemId());
						newDto.setSystemObervationPropertyId(mstr.getSystemObervationPropertyId());
						newDto.setSystemObervationId(mstr.getSystemObervationId());
						newDto.setTypeId(mstr.getTypeId());
						newDto.setUnitId(mstr.getUnitId());
						newDto.setUpdatedBy(mstr.getUpdatedBy());
						newDto.setSystemName(dto.getSystemName());
						newDto.setObservationName(dto.getObservationName());
						newDto.setPropertyName(dto.getPropertyName());
						newDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(mstr.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
						newDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(mstr.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
						generalAndSystemicExaminationList.add(newDto);
					}
				}
				}
				}
			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, null, generalAndSystemicExaminationList, null);
		

		} catch (Exception e) {
		logger.error("getGeneralAndSystemicExaminationMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveGeneralAndSystemicExaminationMapperForNewCoversheet(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try {

			return iGeneralExaminationAppoMapperDao.saveGeneralAndSystemicExaminationMapperForNewCoversheet(generalExaminationAppoMapperDto);

		} catch (Exception e) {
		logger.error("saveGeneralAndSystemicExaminationMapperForNewCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getGenAndSysExaminMapperPatDetailstForNewCov(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try {

			return iGeneralExaminationAppoMapperDao.getGenAndSysExaminMapperPatDetailstForNewCov(generalExaminationAppoMapperDto);

		} catch (Exception e) {
		logger.error("getGenAndSysExaminMapperPatDetailstForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response removeGenAndSysExaminMapperPatDetailstForNewCov(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		try {

			return iGeneralExaminationAppoMapperDao.removeGenAndSysExaminMapperPatDetailstForNewCov(generalExaminationAppoMapperDto);

		} catch (Exception e) {
		logger.error("removeGenAndSysExaminMapperPatDetailstForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDescriptiveExamForNewCov(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		try {

			return iGeneralExaminationAppoMapperDao.updateDescriptiveExamForNewCov(generalExaminationAppoMapperDto);

		} catch (Exception e) {
		logger.error("updateDescriptiveExamForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getExaminationAppoMapperHistory(GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iGeneralExaminationAppoMapperDao.getExaminationAppoMapperHistory(generalExaminationAppoMapperDto);
	}

	@Override
	public Response getExaminationAppoMapperHistoryAdmission(
			GeneralExaminationAppoMapperDto generalExaminationAppoMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iGeneralExaminationAppoMapperDao.getExaminationAppoMapperHistoryAdmission(generalExaminationAppoMapperDto);
	}



}
