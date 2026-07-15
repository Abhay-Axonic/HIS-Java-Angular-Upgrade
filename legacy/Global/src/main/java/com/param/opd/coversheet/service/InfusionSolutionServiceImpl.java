package com.param.opd.coversheet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.opd.coversheet.dao.IInfusionSolutionDao;
import com.param.opd.coversheet.dao.IMedicationDAO;
import com.param.opd.coversheet.dto.InfusionAdditiveDto;
import com.param.opd.coversheet.dto.InfusionSolutionDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class InfusionSolutionServiceImpl implements IInfusionSolutionService,ICommonConstants, IBillingCommonConstants{
	final static Logger logger = Logger.getLogger(InfusionSolutionServiceImpl.class);

	
	@Autowired
	IInfusionSolutionDao iInfusionSolutionDao;
	
	@Autowired
	private IMedicationDAO iMedicationDAO;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Override
	public Response saveInfusionSolution(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException {
		try {
			List<PrescriptionDetailsDto> preDtlListForPharma = new ArrayList<>();
					infusionSolutionDto.setItemId(infusionSolutionDto.getItemId());
					infusionSolutionDto.setInfusionRate(infusionSolutionDto.getInfusionRate());
					infusionSolutionDto.setInstructionId(infusionSolutionDto.getInstructionId());
					infusionSolutionDto.setFrequencyId(infusionSolutionDto.getFrequencyId());
					infusionSolutionDto.setDoctorId(infusionSolutionDto.getDoctorId());
					infusionSolutionDto.setGenericId(infusionSolutionDto.getGenericId());
					infusionSolutionDto.setPriority(infusionSolutionDto.getPriority());
					infusionSolutionDto.setRoaId(infusionSolutionDto.getRoaId());
					infusionSolutionDto.setSolutionVolume(infusionSolutionDto.getSolutionVolume());
					infusionSolutionDto.setStrengthUnitId(infusionSolutionDto.getStrengthUnitId());
					infusionSolutionDto.setPatientId(infusionSolutionDto.getPatientId());
					infusionSolutionDto.setAdmissionId(infusionSolutionDto.getAdmissionId());
					infusionSolutionDto.setTotalVolume(infusionSolutionDto.getTotalVolume());
					infusionSolutionDto.setComments(infusionSolutionDto.getComments());
					infusionSolutionDto.setStartDate(infusionSolutionDto.getStartDate());
					infusionSolutionDto.setEndDate(infusionSolutionDto.getEndDate());
					infusionSolutionDto.setOrganizationId(infusionSolutionDto.getOrganizationId());
					infusionSolutionDto.setUnitId(infusionSolutionDto.getUnitId());
					infusionSolutionDto.setCreatedBy(infusionSolutionDto.getCreatedBy());
					infusionSolutionDto.setCreatedDate(infusionSolutionDto.getCreatedDate());
					infusionSolutionDto.setDuration(infusionSolutionDto.getDuration());
					infusionSolutionDto.setUpdatedBy(infusionSolutionDto.getUpdatedBy());
					infusionSolutionDto.setUpdatedDate(infusionSolutionDto.getUpdatedDate());
					infusionSolutionDto.setStatus(infusionSolutionDto.getStatus());
					infusionSolutionDto.setIsStartFlag('Y');

		Response infusionRes = iInfusionSolutionDao.saveInfusionSolution(infusionSolutionDto);
		
		 infusionSolutionDto = (InfusionSolutionDto) infusionRes.getObject();
		ListIterator<InfusionAdditiveDto> iterator1 = infusionSolutionDto.getInfusionAdditiveDtosList().listIterator();
		while(iterator1.hasNext())
		{
			InfusionAdditiveDto infusionAdditiveDto = iterator1.next();
			infusionAdditiveDto.setOrganizationId(infusionSolutionDto.getOrganizationId());
			infusionAdditiveDto.setUnitId(infusionSolutionDto.getUnitId());
			infusionAdditiveDto.setInfusionSolutionId(infusionSolutionDto.getInfusionSolutionId());
			infusionAdditiveDto.setItemId(infusionAdditiveDto.getItemId());
			infusionAdditiveDto.setAdditiveName(infusionAdditiveDto.getAdditiveName());
			infusionAdditiveDto.setGenericId(infusionAdditiveDto.getGenericId());
			infusionAdditiveDto.setRoaId(infusionAdditiveDto.getRoaId());
			infusionAdditiveDto.setStrengthUnitId(infusionAdditiveDto.getStrengthUnitId());
			infusionAdditiveDto.setVolume(infusionAdditiveDto.getVolume());
			infusionAdditiveDto.setCreatedBy(infusionAdditiveDto.getCreatedBy());
			infusionAdditiveDto.setCreatedDate(infusionAdditiveDto.getCreatedDate());
			infusionAdditiveDto.setUpdatedBy(infusionAdditiveDto.getUpdatedBy());
			infusionAdditiveDto.setUpdatedDate(infusionAdditiveDto.getUpdatedDate());
			infusionAdditiveDto.setStatus(infusionAdditiveDto.getStatus());
			 iInfusionSolutionDao.saveInfusionAdditive(infusionAdditiveDto);	
			
		}
		PrescriptionDto prescriptionDto=new PrescriptionDto();
		prescriptionDto.setIsIntervention(FALSE);
		prescriptionDto.setIsActive(TRUE);
		prescriptionDto.setIsDeleted(FALSE);
		prescriptionDto.setIsValidate(FALSE);
		prescriptionDto.setIsDischarge(FALSE);
		prescriptionDto.setIsNonFormulary('N');
		prescriptionDto.setReconciliationDone(FALSE);
		prescriptionDto.setPriorityId((infusionSolutionDto.getPriority()!=null && infusionSolutionDto.getPriority()!=0) ? infusionSolutionDto.getPriority() : 2);
		prescriptionDto.setStatusId(1);
		prescriptionDto.setAdmissionId(infusionSolutionDto.getAdmissionId());
		prescriptionDto.setPatientId(infusionSolutionDto.getPatientId());
		prescriptionDto.setDoctorId(infusionSolutionDto.getDoctorId());
		prescriptionDto.setAuthorisedBy(infusionSolutionDto.getDoctorId());
		prescriptionDto.setAuthorisedDate(infusionSolutionDto.getCreatedDate());
		prescriptionDto.setPrescriptionDate(infusionSolutionDto.getCreatedDate());
		prescriptionDto.setVisitTypeId(infusionSolutionDto.getVisitTypeId());
		prescriptionDto.setAddedBy(infusionSolutionDto.getCreatedBy());
		prescriptionDto.setAddedDate(infusionSolutionDto.getCreatedDate());
		prescriptionDto.setUpdatedBy(infusionSolutionDto.getUpdatedBy());
		prescriptionDto.setUpdatedDate(infusionSolutionDto.getUpdatedDate());
		prescriptionDto.setOrganizationId(infusionSolutionDto.getOrganizationId());	
		prescriptionDto.setUnitId(infusionSolutionDto.getUnitId());	
		prescriptionDto.setDeptId(infusionSolutionDto.getDeptId());
		prescriptionDto.setIsMedicationStop('N');	
//		prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
		String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, infusionSolutionDto.getUnitId(), PRES_NO);
		prescriptionDto.setPrescriptionNo(prescriptionNo);
	Response presRes = iMedicationDAO.savePrescription(prescriptionDto);
	prescriptionDto = (PrescriptionDto) presRes.getObject();
//	ListIterator<PrescriptionDetailsDto> iterator = prescriptionDto.getPrescriptionDetailsDtosList().listIterator();
	if(infusionSolutionDto.getInfusionAdditiveDtosList()!=null) {
//	ListIterator<InfusionAdditiveDto> iterator = infusionSolutionDto.getInfusionAdditiveDtosList().listIterator();
//	while(iterator.hasNext())
//	{
//		InfusionAdditiveDto dto = iterator.next();
		PrescriptionDetailsDto prescriptionDetailsDto1 =new PrescriptionDetailsDto();
			prescriptionDetailsDto1.setOrganizationId(infusionSolutionDto.getOrganizationId());
			prescriptionDetailsDto1.setUnitId(infusionSolutionDto.getUnitId());
			prescriptionDetailsDto1.setPrescriptionDate(infusionSolutionDto.getCreatedDate());
			prescriptionDetailsDto1.setPrescriptionId(prescriptionDto.getPrescriptionId());
			prescriptionDetailsDto1.setFrequencyId(infusionSolutionDto.getFrequencyId());
		    prescriptionDetailsDto1.setInstructionId(infusionSolutionDto.getInstructionId());
			prescriptionDetailsDto1.setRouteId(infusionSolutionDto.getRoaId());
			prescriptionDetailsDto1.setStrength(infusionSolutionDto.getStrength());
			prescriptionDetailsDto1.setStoreId(1);
			prescriptionDetailsDto1.setAuthorisedBy(infusionSolutionDto.getDoctorId());
			prescriptionDetailsDto1.setAuthorisedDate(infusionSolutionDto.getCreatedDate());
			prescriptionDetailsDto1.setOrderQty(infusionSolutionDto.getInfusionQuantity().intValue());
			prescriptionDetailsDto1.setIsDiscontinued(FALSE);
			prescriptionDetailsDto1.setIsMedicationStop('N');
			prescriptionDetailsDto1.setIsPreStop(FALSE);
			prescriptionDetailsDto1.setIsIntervention(FALSE);
			prescriptionDetailsDto1.setAddedBy(infusionSolutionDto.getCreatedBy());
			prescriptionDetailsDto1.setAddedDate(infusionSolutionDto.getCreatedDate());
			prescriptionDetailsDto1.setIsDeleted(FALSE);
			prescriptionDetailsDto1.setIsActive(TRUE);
			prescriptionDetailsDto1.setStatusId(1);
			prescriptionDetailsDto1.setPrnflag(FALSE);
			prescriptionDetailsDto1.setInfusionTotalVolume(infusionSolutionDto.getTotalVolume());
			prescriptionDetailsDto1.setItemId(infusionSolutionDto.getItemId()!=null ? infusionSolutionDto.getItemId():null);
			prescriptionDetailsDto1.setStatflag(FALSE);
			prescriptionDetailsDto1.setIsSolution(true);
			prescriptionDetailsDto1.setGenericName(null);
			prescriptionDetailsDto1.setItemName(null);
			prescriptionDetailsDto1.setIsNonFormulary('N');
			prescriptionDetailsDto1.setInfusionflag(TRUE);
			prescriptionDetailsDto1.setInfusionSolutionId(infusionSolutionDto.getInfusionSolutionId());
			prescriptionDetailsDto1.setDose(infusionSolutionDto.getSolutionVolume());
			prescriptionDetailsDto1.setDoseUnitId(infusionSolutionDto.getStrengthUnitId());
			prescriptionDetailsDto1.setStartDate(infusionSolutionDto.getStartDate());
			prescriptionDetailsDto1.setStopDate(infusionSolutionDto.getEndDate());
			prescriptionDetailsDto1.setUpdatedBy(infusionSolutionDto.getUpdatedBy());
			prescriptionDetailsDto1.setUpdatedDate(infusionSolutionDto.getUpdatedDate());
			if(prescriptionDetailsDto1.getIsDischarge()!=null){
				prescriptionDetailsDto1.setIsDischarge(prescriptionDetailsDto1.getIsDischarge());
			}else{
				prescriptionDetailsDto1.setIsDischarge(false);
			}
			
//			prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
//			prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
		Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto1);	
			
		prescriptionDetailsDto1 = (PrescriptionDetailsDto) resPreDtls.getObject();
		
		iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto1);
		
		
//	}
	ListIterator<InfusionAdditiveDto> iterator11 = infusionSolutionDto.getInfusionAdditiveDtosList().listIterator();
	while(iterator11.hasNext())
	{
		InfusionAdditiveDto dto1 = iterator11.next();
		PrescriptionDetailsDto prescriptionDetailsDto =new PrescriptionDetailsDto();
			prescriptionDetailsDto.setOrganizationId(dto1.getOrganizationId());
			prescriptionDetailsDto.setUnitId(dto1.getUnitId());
			prescriptionDetailsDto.setPrescriptionDate(infusionSolutionDto.getCreatedDate());
			prescriptionDetailsDto.setPrescriptionId(prescriptionDto.getPrescriptionId());
			prescriptionDetailsDto.setFrequencyId(infusionSolutionDto.getFrequencyId());
		    prescriptionDetailsDto.setInstructionId(infusionSolutionDto.getInstructionId());
			prescriptionDetailsDto.setRouteId(infusionSolutionDto.getRoaId());
			prescriptionDetailsDto.setStrength(infusionSolutionDto.getStrength());
			prescriptionDetailsDto.setStoreId(1);
			prescriptionDetailsDto.setAuthorisedBy(infusionSolutionDto.getDoctorId());
			prescriptionDetailsDto.setInfusionTotalVolume(infusionSolutionDto.getTotalVolume());
			prescriptionDetailsDto.setAuthorisedDate(infusionSolutionDto.getCreatedDate());
			prescriptionDetailsDto.setOrderQty(infusionSolutionDto.getInfusionQuantity().intValue());
			prescriptionDetailsDto.setIsDiscontinued(FALSE);
			prescriptionDetailsDto.setIsMedicationStop('N');
			prescriptionDetailsDto.setIsPreStop(FALSE);
			prescriptionDetailsDto.setIsAdditive(true);
			prescriptionDetailsDto.setIsIntervention(FALSE);
			prescriptionDetailsDto.setAddedBy(infusionSolutionDto.getCreatedBy());
			prescriptionDetailsDto.setAddedDate(infusionSolutionDto.getCreatedDate());
			prescriptionDetailsDto.setIsDeleted(FALSE);
			prescriptionDetailsDto.setIsActive(TRUE);
			prescriptionDetailsDto.setStatusId(1);
			prescriptionDetailsDto.setPrnflag(FALSE);
			prescriptionDetailsDto.setItemId(dto1.getItemId()!=null ? dto1.getItemId():null);
			prescriptionDetailsDto.setStatflag(FALSE);
			prescriptionDetailsDto.setGenericName(null);
			prescriptionDetailsDto.setItemName(null);
			prescriptionDetailsDto.setIsNonFormulary('N');
			prescriptionDetailsDto.setInfusionflag(TRUE);
			prescriptionDetailsDto.setInfusionSolutionId(infusionSolutionDto.getInfusionSolutionId());
			prescriptionDetailsDto.setDose(dto1.getVolume());
			prescriptionDetailsDto.setDoseUnitId(dto1.getStrengthUnitId());
			prescriptionDetailsDto.setStartDate(infusionSolutionDto.getStartDate());
			prescriptionDetailsDto.setStopDate(infusionSolutionDto.getEndDate());
			prescriptionDetailsDto.setUpdatedBy(infusionSolutionDto.getUpdatedBy());
			prescriptionDetailsDto.setUpdatedDate(infusionSolutionDto.getUpdatedDate());
			if(prescriptionDetailsDto.getIsDischarge()!=null){
				prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
			}else{
				prescriptionDetailsDto.setIsDischarge(false);
			}
			preDtlListForPharma.add(prescriptionDetailsDto);
//			prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
//			prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
		Response resPreDtls1 = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);	
			
		prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls1.getObject();
		
		iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
		
		infusionSolutionDto.setPrescriptionId(prescriptionDto.getPrescriptionId());
		infusionSolutionDto.setStoreId(prescriptionDto.getStoreId());
		infusionSolutionDto.setPrescriptionDetailsid(prescriptionDetailsDto.getPrescriptionDetailsid());
		infusionSolutionDto.setPrescriptionDetailsDto(preDtlListForPharma);
		
		
		
	}
	
	}
	if(infusionSolutionDto.getInfusionAdditiveDtosList() == null || infusionSolutionDto.getInfusionAdditiveDtosList().isEmpty()){
		infusionSolutionDto.setPrescriptionId(prescriptionDto.getPrescriptionId());
		infusionSolutionDto.setStoreId(prescriptionDto.getStoreId());
	}

	return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, infusionSolutionDto);
} catch (Exception e) {
		logger.error("saveInfusionSolution failed", e);
		}
return new Response<>(ERROR, ERROR_CODE, null, null, null);
}


	@Override
	public Response getInfusionSolutionItemList(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException {
		try {
					return iInfusionSolutionDao.getInfusionSolutionItemList(infusionSolutionDto);
		
				} catch (Exception e) {
				logger.error("getInfusionSolutionItemList failed", e);
				}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	@Override
	public Response getInfusionAdditiveItemList(InfusionSolutionDto infusionSolutionDto)  throws ApplicationException {
		try {
				return iInfusionSolutionDao.getInfusionAdditiveItemList(infusionSolutionDto);

				} catch (Exception e) {
				logger.error("getInfusionAdditiveItemList failed", e);
				}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getListOfInfusion(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
		try {
			return iInfusionSolutionDao.getListOfInfusion(infusionSolutionDto);

			} catch (Exception e) {
			logger.error("getListOfInfusion failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


	@Override
	public Response getAdditiveListById(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
		try {
			return iInfusionSolutionDao.getAdditiveListById(infusionSolutionDto);

		} catch (Exception e) {
		logger.error("getAdditiveListById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


	@Override
	public Response updateInfusionStopFlag(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
		try {
			List<PrescriptionDetailsDto> getPresDetIdAndPresId = new ArrayList<>();
			 getPresDetIdAndPresId = iInfusionSolutionDao.getPresDetIdAndPresId(infusionSolutionDto.getInfusionSolutionId());
			 for (PrescriptionDetailsDto prescriptionDetailsDto : getPresDetIdAndPresId) {
				 PrescriptionDetailsDto updatePresDetIdAndPresId = iInfusionSolutionDao.updatePresDetIdAndPresId(prescriptionDetailsDto);
			}
			return iInfusionSolutionDao.updateInfusionStopFlag(infusionSolutionDto);

	} catch (Exception e) {
	logger.error("updateInfusionStopFlag failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

    @Override
    public Response getPresDtlDataByPresId(Integer prescriptionId, Integer organizationId, Integer unitId) throws ApplicationException {
        try {
            Response res= iMedicationDAO.getPresDtlDataByPresId(prescriptionId, organizationId, unitId );
            return new Response<>(SUCCESS, null, null, null, res);
        } catch (Exception e) {
        logger.error("getPresDtlDataByPresId failed", e);
        }
        return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

    }
    


}
