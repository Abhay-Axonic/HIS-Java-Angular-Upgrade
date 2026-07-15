package com.param.adt.admission.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.admission.dao.IAdmissionRequestDao;
import com.param.adt.common.IADTConstants;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitMasterDto;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.service.IPatientRegistrationService;

import in.param.exception.ApplicationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdmissionRequestServiceImpl implements IAdmissionRequestService, ICommonConstants,IADTConstants {
	@Autowired
	IAdmissionRequestDao iAdmissionRequestDao;
	
	@Autowired
	IPatientRegistrationService patientRegistrationServiceImpl;

	@Override
	public Response saveAdmissionRequest(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			//If patient is not registered then there will be a pre-registration of a patient who's patientTypeId == 5
			if(admissionNoteDto.getPatientId()==0)
			{
				PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
					patientRegistrationDto.setOrganizationId(admissionNoteDto.getOrganizationId());
					patientRegistrationDto.setUnitId(admissionNoteDto.getUnitId());
					patientRegistrationDto.setPrefixId(admissionNoteDto.getPrefixId());
					patientRegistrationDto.setFirstName(admissionNoteDto.getpFirstName());
					patientRegistrationDto.setMiddleName(admissionNoteDto.getpMiddleName());
					patientRegistrationDto.setLastName(admissionNoteDto.getpLastName());
					patientRegistrationDto.setDob(admissionNoteDto.getDob());
					patientRegistrationDto.setGenderId(admissionNoteDto.getGenderId());
					patientRegistrationDto.setMobileNumber(admissionNoteDto.getMobileNumber());
					patientRegistrationDto.setEmail(admissionNoteDto.getEmail());
					patientRegistrationDto.setRegistrationTypeId(admissionNoteDto.getRegistrationTypeId());
					patientRegistrationDto.setCreatedBy(admissionNoteDto.getCreatedBy());
					patientRegistrationDto.setCreatedDate(admissionNoteDto.getCreatedDate());
					patientRegistrationDto.setUpdatedBy(admissionNoteDto.getUpdatedBy());
					patientRegistrationDto.setUpdatedDate(admissionNoteDto.getUpdatedDate());
					patientRegistrationDto.setPatientCategoryId(admissionNoteDto.getPatientCategoryId());
					patientRegistrationDto.setKinList(admissionNoteDto.getKinList());
					patientRegistrationDto.setStatus('A');
					patientRegistrationDto.setBloodGroup(admissionNoteDto.getBloodGroup()!=null ? admissionNoteDto.getBloodGroup()  : null);
				com.param.global.common.Response patientRegistraion = patientRegistrationServiceImpl.savePatientRegistration(patientRegistrationDto);
				if(patientRegistraion.getStatus().equals(ERROR))
					return new Response(ERROR,null,patientRegistraion.getMessage(),null,null);
				else
					admissionNoteDto.setPatientId((Integer)patientRegistraion.getObject());
			}
			Response existRes = iAdmissionRequestDao.isAlreadyRequestExist(admissionNoteDto);	
			if(existRes.getStatus().equals(SUCCESS))
			{
				admissionNoteDto.setAdmissionNoteNumber(generateAdmissionNoteNo());
				Response res = iAdmissionRequestDao.saveAdmissionRequest(admissionNoteDto);
				AdmissionNoteDto admissionNoteDto2 = (AdmissionNoteDto) res.getObject();
				if (res.getStatus().equals(SUCCESS)) {
					Response res2 = iAdmissionRequestDao.saveAdmissionPatientMapper(admissionNoteDto2);
					AdmissionNoteDto admissionNoteDto3 = (AdmissionNoteDto) res.getObject();
					if(!(admissionNoteDto.getDoa().substring(0,10).equals(admissionNoteDto.getUpdatedDate().substring(0,10))))
					{
						if(res2.getStatus().equals(SUCCESS))
							return saveWaitingListNumber(admissionNoteDto3);
					}else
					{
						if(res2.getStatus().equals(SUCCESS))
							return new Response(SUCCESS, null, NOTE_GENERATED, null, null);
					}
					
				}
			}
			else{
				return existRes;
			}

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientListLike(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			return iAdmissionRequestDao.getPatientListLike(patientRegistrationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientVisitDetailsById(VisitMasterDto visitMasterDto) throws ApplicationException {
		try {
			return iAdmissionRequestDao.getPatientVisitDetailsById(visitMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorListAganistSpeciality(DoctorSpecialityMapperDto doctorSpecialityMapperdto)
			throws ApplicationException {
		try {
			return iAdmissionRequestDao.getDoctorListAganistSpeciality(doctorSpecialityMapperdto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveAdmissionPatientMapper(AdmissionNoteDto admissionNoteDto) {
		try {
			return iAdmissionRequestDao.saveAdmissionPatientMapper(admissionNoteDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveWaitingListNumber(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			
			Response res= iAdmissionRequestDao.getMaxWaitingListNumber(admissionNoteDto);
			if(res.getStatus().equals(SUCCESS))
			{
				if(!res.getObject().equals(null))
				{
					Integer i=(Integer)res.getObject();
					i++;
					admissionNoteDto.setWaitListNumber(i);
					admissionNoteDto.setStatus('A');
					admissionNoteDto.setCreatedBy(admissionNoteDto.getUpdatedBy());
					return iAdmissionRequestDao.saveWaitingListNumber(admissionNoteDto);
				}
				else
					return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			else
				return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorsListAganistSpecialitys(DoctorSpecialityMapperDto doctorSpecialityMapper)
			throws ApplicationException {
		try {
			return iAdmissionRequestDao.getDoctorsListAganistSpecialitys(doctorSpecialityMapper);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Transactional
	private String generateAdmissionNoteNo() throws ApplicationException {
		try {
			Long incrementedId = iAdmissionRequestDao.getNextValAdmissionNoteId();
			String encouterPrefix="AN-18-";
			
			StringBuilder encounter=new StringBuilder(encouterPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				encounter = length == 1 ? (encounter.append("000000").append(incrementedId))
						: length == 2 ? (encounter.append("00000").append(incrementedId))
								: length == 3 ? (encounter.append("0000").append(incrementedId))
										: length == 4 ? (encounter.append("000").append(incrementedId))
												: length == 5 ? (encounter.append("00").append(incrementedId))
														: length == 6 ? (encounter.append("0").append(incrementedId))
																: length == 7 ? (encounter.append(incrementedId)) : encounter.append("");
				return encounter.toString();
			} catch (Exception e) {
				throw new ApplicationException(e.getMessage(), e);
			}
	}

	@Override
	public Response getNephrologyDoctorsList(DoctorSpecialityMapperDto doctorSpecialityMapper)
			throws ApplicationException {
		try {
			return iAdmissionRequestDao.getNephrologyDoctorsList(doctorSpecialityMapper);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPastAdmissionDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException {
		try {
			return iAdmissionRequestDao.getPastAdmissionDetails(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientListLikeForERMapOfBed(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {
			return iAdmissionRequestDao.getPatientListLikeForERMapOfBed(patientRegistrationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmissionForIPDCoversheetByDate(AdmissionNoteDto dto) throws ApplicationException {
		try{
			List<AdmissionNoteDto> resDtoList = new ArrayList<>();

			for(String date : dto.getDates()){
				List<AdmissionNoteDto> encounterList = new ArrayList<>();
				AdmissionNoteDto resDto = new AdmissionNoteDto();

				resDto.setAddedDate(date);
				if(dto.getEncounterList() != null && dto.getEncounterList().size()>0){
					for (AdmissionNoteDto item : dto.getEncounterList()) {

						if (date.equals(item.getCreatedDate())) {
							item.setAddedDate(date);
							encounterList.add(item);
						}
					}
					resDto.setEncounterList(encounterList);
				}
				resDtoList.add(resDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){

		}

		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUniqueDateForIPDCoversheet(AdmissionNoteDto dto) throws ApplicationException {
		try{
			List<String>distinctDates = new ArrayList<>();
			AdmissionNoteDto resDto = dto;

			if(dto.getEncounterList() != null && dto.getEncounterList().size()>0){
				for (AdmissionNoteDto complaint : dto.getEncounterList()) {

					if (!distinctDates.contains(complaint.getCreatedDate())) {
						distinctDates.add(complaint.getCreatedDate());
					}
				}

				if(distinctDates != null && distinctDates.size()>0){
					Collections.sort(distinctDates, Comparator.reverseOrder());
					resDto.setDates(distinctDates);
					return getAdmissionForIPDCoversheetByDate(resDto);
				}

			}
			else{
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, null, null);
			}
		}
		catch (Exception e){
			System.out.println(e);
		}

		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

}
