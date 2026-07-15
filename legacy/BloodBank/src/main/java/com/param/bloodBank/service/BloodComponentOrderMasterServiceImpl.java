package com.param.bloodBank.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.param.global.otims.dto.ProcedureOrderMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodComponentOrderMasterDao;
import com.param.bloodBank.dto.BloodComponentOrderMasterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.bloodBank.model.BloodComponentOrderMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})	
public class BloodComponentOrderMasterServiceImpl implements IBloodComponentOrderMasterService, ICommonConstants,IBillingCommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentOrderMasterServiceImpl.class);

		@Autowired
		IBloodComponentOrderMasterDao iBloodComponentOrderMasterDao;

		@Autowired
		private IGlobalUnitMasterDao iGlobalUnitMasterDao;
		
		@Override
		public Response saveBloodComponentOrderMaster(BloodComponentOrderMasterDto bloodComponentOrderMasterDto)
				throws ApplicationException {
			try {
//				Response response=iBloodComponentOrderMasterDao.getBloodComponentOrderMasterByName(bloodComponentOrderMasterDto);
//				
//				if(response.getListObject()!=null && response.getListObject().size()>0){
//					
//					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
//				}else{
				String requisitionNumber = iGlobalUnitMasterDao.getGeneratedNo(0, bloodComponentOrderMasterDto.getBloodComponentOrderMasterDtoList().get(0).getUnitId(), REQUISITION_NUMBER);
				bloodComponentOrderMasterDto.setRequisitionNumber(requisitionNumber);
				
				List<BloodComponentOrderMasterDto> list = new ArrayList();
				for (BloodComponentOrderMasterDto  bloodComponentOrderMasterList :  bloodComponentOrderMasterDto.getBloodComponentOrderMasterDtoList()) {
					BloodComponentOrderMasterDto dto = new BloodComponentOrderMasterDto();
					dto.setBloodComponentId(bloodComponentOrderMasterList.getBloodComponentId());
					dto.setProcedureOrderId(bloodComponentOrderMasterList.getProcedureOrderId());
					dto.setPatientId(bloodComponentOrderMasterList.getPatientId());
					dto.setPriority(bloodComponentOrderMasterList.getPriority());
					dto.setAdmissionId(bloodComponentOrderMasterList.getAdmissionId());
					dto.setDoctorId(bloodComponentOrderMasterList.getDoctorId());
					dto.setComments(bloodComponentOrderMasterList.getComments());
					dto.setInstruction(bloodComponentOrderMasterList.getInstruction());
					dto.setUnit(bloodComponentOrderMasterList.getUnit());
					dto.setRequisitionDate(bloodComponentOrderMasterList.getRequisitionDate());
					dto.setRequisitionOnDate(bloodComponentOrderMasterList.getRequisitionOnDate());
					dto.setOrganizationId(bloodComponentOrderMasterList.getOrganizationId());
					dto.setUnitId(bloodComponentOrderMasterList.getUnitId());
					dto.setStatus(bloodComponentOrderMasterList.getStatus());
					dto.setIsExported(bloodComponentOrderMasterList.getIsExported());
					dto.setIsRequisitionClosed(bloodComponentOrderMasterList.getIsRequisitionClosed());
					dto.setIsRequired(bloodComponentOrderMasterList.getIsRequired());
					dto.setRequisitionNumber(requisitionNumber);
					dto.setCreatedBy(bloodComponentOrderMasterList.getCreatedBy());
					dto.setCreatedDate(bloodComponentOrderMasterList.getCreatedDate());
					dto.setUpdatedBy(bloodComponentOrderMasterList.getUpdatedBy());
					dto.setUpdatedDate(bloodComponentOrderMasterList.getUpdatedDate());
					
				list.add(dto);
				}
					return iBloodComponentOrderMasterDao.saveBloodComponentOrderMaster(list);
//				}
			} catch (Exception e) {
				logger.error("saveBloodComponentOrderMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

		@Override
		public Response updateBloodComponentOrderMaster(BloodComponentOrderMasterDto bloodComponentOrderMasterDto)
				throws ApplicationException {
			try {
//				Response response=iBloodComponentOrderMasterDao.getBloodComponentOrderMasterByNameNotId(bloodComponentOrderMasterDto);
//				
//				if(response.getListObject()!=null && response.getListObject().size()>0){
					
//					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
//				}else{
					
					return iBloodComponentOrderMasterDao.updateBloodComponentOrderMaster(bloodComponentOrderMasterDto.getBloodComponentOrderMasterDtoList());
//				}
			} catch (Exception e) {
				logger.error("updateBloodComponentOrderMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


		@Override
		public Response getBloodComponentOrderMasterList(BloodComponentOrderMasterDto bloodComponentOrderMasterDto)
				throws ApplicationException {
			try {

					return iBloodComponentOrderMasterDao.getBloodComponentOrderMasterList(bloodComponentOrderMasterDto);
				
			} catch (Exception e) {
				logger.error("getBloodComponentOrderMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


		@Override
		public Response getBloodComponentOrderMasterById(Integer bloodComponentOrderId, Integer organizationId, Integer unitId)
				throws ApplicationException {
			try {

				return iBloodComponentOrderMasterDao.getBloodComponentOrderMasterById(bloodComponentOrderId, organizationId, unitId);
			
			} catch (Exception e) {
				logger.error("getBloodComponentOrderMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

		@Override
		public Response getActiveBloodComponentOrderMaster(Integer organizationId, Integer unitId) throws ApplicationException {
			try {

				return iBloodComponentOrderMasterDao.getActiveBloodComponentOrderMaster(organizationId, unitId);
			
		} catch (Exception e) {
			logger.error("getActiveBloodComponentOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

		@Override
		public Response getListOfProcedureOrderByPatientId(Integer patientId, Integer organizationId, Integer unitId)
				throws ApplicationException {
			try {

				return iBloodComponentOrderMasterDao.getListOfProcedureOrderByPatientId(patientId, organizationId, unitId);
			
		} catch (Exception e) {
			logger.error("getListOfProcedureOrderByPatientId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

		@Override
		@Transactional
		public Response searchOrderedBloodComponentByDate(BloodComponentOrderMasterDto bloodComponentOrderMasterDto)
				throws ApplicationException {
			try {

				return iBloodComponentOrderMasterDao.searchOrderedBloodComponentByDate(bloodComponentOrderMasterDto);
			
		} catch (Exception e) {
			logger.error("searchOrderedBloodComponentByDate failed", e);
			throw new ServiceException(e.getMessage());
		}
		}

		@Override
		public Response closePatientRequisition(PatientRequisitionDetailsDto dto) throws ApplicationException {
			try {

				return iBloodComponentOrderMasterDao.closePatientRequisition(dto);
			
		} catch (Exception e) {
			logger.error("closePatientRequisition failed", e);
			throw new ServiceException(e.getMessage());
		}
		}

		@Override
		public Response updateIsExportedStatus(BloodComponentOrderMasterDto dto) throws ApplicationException {
			try {

				return iBloodComponentOrderMasterDao.updateIsExportedStatus(dto);
			
		} catch (Exception e) {
			logger.error("updateIsExportedStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
		}

	@Override
	public Response getDataByDateFromBloodComponentOrderMaster(BloodComponentOrderMasterDto dto) throws ApplicationException {
		try{
			List<BloodComponentOrderMasterDto> resDtoList = new ArrayList<>();

			for(String date : dto.getDates()){
				List<BloodComponentOrderMasterDto> getListOfsavedComponents = new ArrayList<>();
				BloodComponentOrderMasterDto resDto = new BloodComponentOrderMasterDto();

				resDto.setAddedDate(date);
				if(dto.getGetListOfsavedComponents() != null && dto.getGetListOfsavedComponents().size()>0){
					for (BloodComponentOrderMasterDto investigation : dto.getGetListOfsavedComponents()) {

						if (date.equals(investigation.getCreatedDate())) {
							investigation.setAddedDate(date);
							getListOfsavedComponents.add(investigation);
						}
					}
					resDto.setGetListOfsavedComponents(getListOfsavedComponents);
				}
				resDtoList.add(resDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){
			logger.error("getDataByDateFromBloodComponentOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getUniqueDateFromBloodComponentOrderMaster(BloodComponentOrderMasterDto dto) throws ApplicationException {
		try{
			List<String>distinctDates = new ArrayList<>();
			BloodComponentOrderMasterDto resDto = dto;

			if(dto.getGetListOfsavedComponents() != null && dto.getGetListOfsavedComponents().size()>0){
				for (BloodComponentOrderMasterDto complaint : dto.getGetListOfsavedComponents()) {

					if (!distinctDates.contains(complaint.getCreatedDate())) {
						distinctDates.add(complaint.getCreatedDate());
					}
				}

				if(distinctDates != null && distinctDates.size()>0){
					Collections.sort(distinctDates, Comparator.reverseOrder());
					resDto.setDates(distinctDates);
					return getDataByDateFromBloodComponentOrderMaster(resDto);
				}

			}
			else{
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, null, null);
			}
		}
		catch (Exception e){
			logger.error("getUniqueDateFromBloodComponentOrderMaster failed", e);
			throw new ServiceException(e.getMessage());
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
