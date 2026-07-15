package com.param.adt.admission.service;

import com.param.adt.admission.dao.IAdmissionTriageHistoryDao;
import com.param.adt.admission.dao.IAdmittedPatientOrdersWorkbench;
import com.param.adt.admission.model.AdmittedPatientOrdersWorkbench;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dao.IAdmittedPatientListDao;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.unit.dto.WardMasterDto;
import com.param.global.dto.AdmissionDto;

import in.param.exception.ApplicationException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class AdmittedPatientListServiceImpl implements IAdmittedPatientListService,ICommonConstants
{
	@Autowired
	IAdmittedPatientListDao iAdmittedPatientListDao; 

	@Autowired
	IAdmittedPatientOrdersWorkbench iAdmittedPatientOrdersWorkbench;

	@Autowired
	IAdmissionTriageHistoryDao iAdmissionTriageHistoryDao;


	public Response getAdmittedPatientList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientList(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveWardListByFloorId(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getActiveWardListByFloorId(wardMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getKinDetailsByAdmissionId(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getKinDetailsByAdmissionId(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAdmittedPatientByFloorWard(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientByFloorWard(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response serachPatientForMapOfBed(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.serachPatientForMapOfBed(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getIpPatientListByDocId(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getIpPatientListByDocId(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAdmittedPatientListForEMR(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListForEMR(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAdmittedPatientListForIPDNursingWorklist(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListForIPDNursingWorklist(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getListOfERPatients(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getListOfERPatients(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


	@Override
	public Response getListOfDayCarePatientsForNursing(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getListOfDayCarePatientsForNursing(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


	@Override
	public Response getListOfDayCareNursingAccepted(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getListOfDayCareNursingAccepted(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response ipPatientListByDocIdForCancelDischarge(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.ipPatientListByDocIdForCancelDischarge(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getHemodialysisByDoctorList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getHemodialysisByDoctorList(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAdmittedPatientListForIPDNursingWorklistForNursingPage(AdmissionDto admissionDto)
			throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListForIPDNursingWorklistForNursingPage(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAdmittedPatientListForIPDNursingWorklistForNursingPageCount(AdmissionDto admissionDto)
			throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListForIPDNursingWorklistForNursingPageCount(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getLoginDoctorSpecialityId(AdmissionDto admissionDto) throws ApplicationException { 
			try {
				return iAdmittedPatientListDao.getLoginDoctorSpecialityId(admissionDto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}


	@Override
	public Response getAdmittedPatientListPagination(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListPagination(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAdmittedPatientListPaginationCount(AdmissionDto admissionDto) {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListPaginationCount(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response admittedPatientListUniversalSearch(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.admittedPatientListUniversalSearch(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response admittedPatientListUniversalSearchForDietOrder(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.admittedPatientListUniversalSearchForDietOrder(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response ipPatientListByDocIdBySearch(AdmissionDto admissionDto) {
		try {
			return iAdmittedPatientListDao.ipPatientListByDocIdBySearch(admissionDto);
		} catch (Exception e) {

		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListForVitalUploader(AdmissionDto admissionDto) {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListForVitalUploader(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getIpPatientListForDoctorApp(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getIpPatientListForDoctorApp(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	
	}


	@Override
	public Response getAdmittedPatientListForNewBorn(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListForNewBorn(admissionDto);
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response editAdmissionDetailsGetById(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.editAdmissionDetailsGetById(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response admissionCancellation(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.admissionCancellation(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getPatientDetailsForWorklistForWebPage(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getPatientDetailsForWorklistForWebPage(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAdmittedPatientListForDietOrder(AdmissionDto admissionDto) throws ApplicationException {
		try {
			return iAdmittedPatientListDao.getAdmittedPatientListForDietOrder(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savePatientOrdersWorkbench(List<AdmissionDto> admissionDtoList) throws ApplicationException {
		try {
			List<AdmittedPatientOrdersWorkbench> res = new LinkedList<>();
			for(AdmissionDto admissionDto: admissionDtoList) {
				        Response checkResponse= iAdmittedPatientOrdersWorkbench.checkPatientOrdersWorkbenchExist(admissionDto);
						if(checkResponse!=null && checkResponse.getCode()==null) {
							AdmittedPatientOrdersWorkbench admittedPatientOrdersWorkbench = new AdmittedPatientOrdersWorkbench();
							admittedPatientOrdersWorkbench.setPatientId(admissionDto.getPatientId());
							admittedPatientOrdersWorkbench.setCreatedBy(admissionDto.getCreatedBy());
							admittedPatientOrdersWorkbench.setCreatedDate(new Date());
							admittedPatientOrdersWorkbench.setTabId(admissionDto.getTabId());
							admittedPatientOrdersWorkbench.setRecordId(admissionDto.getRecordId());
							admittedPatientOrdersWorkbench.setRemark(admissionDto.getWorkbenchRemark());
							admittedPatientOrdersWorkbench.setResultId(admissionDto.getResultId());
							admittedPatientOrdersWorkbench.setWorkbenchAddedDate(admissionDto.getWorkbenchAddedDate());
							admittedPatientOrdersWorkbench.setWorkbenchEndDate(admissionDto.getWorkbenchEndDate());
							admittedPatientOrdersWorkbench.setOrganizationId(admissionDto.getOrganizationId());
							admittedPatientOrdersWorkbench.setUnitId(admissionDto.getUnitId());
							AdmittedPatientOrdersWorkbench response = iAdmittedPatientOrdersWorkbench.savePatientOrdersWorkbench(admittedPatientOrdersWorkbench);
							res.add(response);
						}else{
							AdmittedPatientOrdersWorkbench admittedPatientOrdersWorkbench = new AdmittedPatientOrdersWorkbench();
							Integer id =(Integer)checkResponse.getObject();
							admittedPatientOrdersWorkbench.setPatientWorkbenchId(id);
							admittedPatientOrdersWorkbench.setRemark(admissionDto.getWorkbenchRemark());
							admittedPatientOrdersWorkbench.setResultId(admissionDto.getResultId());
							admittedPatientOrdersWorkbench.setWorkbenchAddedDate(admissionDto.getWorkbenchAddedDate());
							admittedPatientOrdersWorkbench.setWorkbenchEndDate(admissionDto.getWorkbenchEndDate());
							admittedPatientOrdersWorkbench.setUpdatedBy(admissionDto.getUpdatedBy());
							admittedPatientOrdersWorkbench.setUpdatedDate(new Date());
							iAdmittedPatientOrdersWorkbench.updatePatientOrdersWorkbench(admittedPatientOrdersWorkbench);
						}
			}
			return new Response(SUCCESS, null, null, res,null);
		} catch (Exception e) {
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response saveTriageCurrentDetails(AdmissionDto admissionDto) throws ApplicationException {
		return iAdmissionTriageHistoryDao.saveTriageHistory(admissionDto);
	}

}
