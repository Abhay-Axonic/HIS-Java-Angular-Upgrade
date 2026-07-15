package com.param.opd.coversheet.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.data.dto.AdministrationWorklistTimeDetailsDto;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAdvisedDateAppointmentDao;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.AdvisedDateAppointmentDto;
import com.param.global.dto.DrugMasterDto;
import com.param.opd.coversheet.dao.IAdministrationWorklistTimeDetailsDao;
import com.param.opd.coversheet.dao.IMedicationDAO;
import com.param.opd.coversheet.dao.IMedicationHistoryDao;
import com.param.opd.coversheet.dao.IMedicationStopDao;
import com.param.opd.coversheet.dto.AdministrationWorklistDto;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.DrugsListDTO;
import com.param.opd.coversheet.dto.GeneralExaminationAppoMapperDto;
import com.param.opd.coversheet.dto.ImmunizationHistoryMapperDto;
import com.param.opd.coversheet.dto.InfusionSolutionDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.MedicationListDto;
import com.param.opd.coversheet.dto.NursingNoteDto;
import com.param.opd.coversheet.dto.NursingOrderMapperDto;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.dto.RoaMasterDto;
import com.param.opd.coversheet.dto.VaccineImmunizationMapperDto;
import com.param.service.global.SessionService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
public class MedicationServiceImpl implements IMedicationService, ICommonConstants, IBillingCommonConstants {
	final static Logger logger = Logger.getLogger(MedicationServiceImpl.class);


	@Autowired
	private IMedicationDAO iMedicationDAO;

	@Autowired
	private IMedicationHistoryDao iMedicationHistoryDao;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	private IMedicationStopDao iMedicationStopDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	private IAdvisedDateAppointmentDao iAdvisedDateAppointmentDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;

	@Autowired
	IAdministrationWorklistTimeDetailsDao iAdministrationWorklistTimeDetailsDao;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	@Transactional
	public Response saveMedication(PrescriptionDto prescriptionDto) throws ApplicationException {
		try {
			Integer prescriptionId = null;
			Response resPres = null;
			Integer addedOrdQty = 0;
			Integer addNewQty = 0;
			Character nonFormulary = 'Y';
			if (prescriptionDto.getEncounterId() != null) {
				resPres = iMedicationDAO.getPrescriptionIdByEncounterId(prescriptionDto.getEncounterId(),
						prescriptionDto.getStatflag());
				if (resPres.getListObject().size() > 0 && resPres.getListObject() != null) {
					List<PrescriptionDto> prescriptionDto12 = resPres.getListObject();
					for (PrescriptionDto dto : prescriptionDto12) {
						prescriptionId = dto.getPrescriptionId();
						/*
						 * if(dto.getStatusId()==4){ resPres=
						 * iMedicationDAO.updatePrescriptionStatusId(prescriptionDto.getEncounterId());
						 * }
						 */
					}
					List<PrescriptionDetailsDto> listPresDtl = prescriptionDto.getPrescriptionDetailsDtosList();
					for (PrescriptionDetailsDto presDtlDto : listPresDtl) {
						if (presDtlDto.getItemId() != null && prescriptionId != null
								&& prescriptionDto.getEncounterId() != null) {
							Response presDtlRes = iMedicationDAO.getOrderedQty(presDtlDto.getItemId(), prescriptionId,
									prescriptionDto.getEncounterId());
							if (presDtlRes.getListObject() != null && presDtlRes.getListObject().size() > 0) {
								List<PrescriptionDetailsDto> newDto = new ArrayList<>();
								newDto = (List<PrescriptionDetailsDto>) presDtlRes.getListObject();
								for (PrescriptionDetailsDto qtyDto : newDto) {
									addedOrdQty += qtyDto.getOrderQty();
								}
								addNewQty = addedOrdQty + presDtlDto.getOrderQty();
								presDtlDto.setOrderQty(addNewQty);
								presDtlDto.setPrescriptionDetailsid(newDto.get(0).getPrescriptionDetailsid());
								presDtlDto.setPrescriptionFrequencyId(newDto.get(0).getPrescriptionFrequencyId());
							}
						}

					}

				} else {
					prescriptionDto.setIsIntervention(FALSE);
					prescriptionDto.setIsActive(TRUE);
					prescriptionDto.setIsDeleted(FALSE);
					prescriptionDto.setIsValidate(FALSE);
					prescriptionDto.setIsDischarge(FALSE);
					// prescriptionDto.setPriorityId(2);
					prescriptionDto.setStatusId(1);
					prescriptionDto.setMedicationType(prescriptionDto.getMedicationType());
					prescriptionDto.setUpdatedBy(prescriptionDto.getAddedBy());
					prescriptionDto.setUpdatedDate(prescriptionDto.getAddedDate());
//					prescriptionDto.setPrescriptionNo(getPrescriptionNumber());	
					prescriptionDto.setIsMedicationStop('N');
//					prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
					String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, prescriptionDto.getUnitId(),
							PRES_NO);
					if(prescriptionNo == null || prescriptionNo.isEmpty()){
						return new Response<>(ERROR, ERROR_CODE, "Prescription Prefix is Not Configured", null, null);
					}

					int count = 0;
					if (prescriptionDto.getPrescriptionDetailsDtosList().size() > 0) {
						for (PrescriptionDetailsDto dto1 : prescriptionDto.getPrescriptionDetailsDtosList()) {
							if (dto1.getIsNonFormulary() == 'Y') {
								count++;
							}
						}

					}
					/*
					 * if(count==0) { prescriptionDto.setIsNonFormulary('N'); }else
					 * if(prescriptionDto.getPrescriptionDetailsDtosList()!=null &&
					 * prescriptionDto.getPrescriptionDetailsDtosList().size()==count) {
					 * prescriptionDto.setIsNonFormulary('Y'); }else {
					 * prescriptionDto.setIsNonFormulary('N'); }
					 */
					prescriptionDto.setIsNonFormulary('N');
					prescriptionDto.setPrescriptionNo(prescriptionNo);
					Response presRes = iMedicationDAO.savePrescription(prescriptionDto);
					prescriptionDto = (PrescriptionDto) presRes.getObject();

				}
			}

			else {

				prescriptionDto.setIsIntervention(FALSE);
				prescriptionDto.setIsActive(TRUE);
				prescriptionDto.setIsDeleted(FALSE);
				prescriptionDto.setIsValidate(TRUE);
				prescriptionDto.setIsDischarge(FALSE);
				// prescriptionDto.setPriorityId(2);
				prescriptionDto.setStatusId(1);
				prescriptionDto.setUpdatedBy(prescriptionDto.getAddedBy());
				prescriptionDto.setUpdatedDate(prescriptionDto.getAddedDate());
				prescriptionDto.setMedicationType(prescriptionDto.getMedicationType());
//				prescriptionDto.setPrescriptionNo(getPrescriptionNumber());	
				prescriptionDto.setIsMedicationStop('N');
//				prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
				String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, prescriptionDto.getUnitId(), PRES_NO);
				if(prescriptionNo == null || prescriptionNo.isEmpty()){
					return new Response<>(ERROR, ERROR_CODE, "Prescription Prefix is Not Configured", null, null);
				}

				int count = 0;
				if (prescriptionDto.getPrescriptionDetailsDtosList().size() > 0) {
					for (PrescriptionDetailsDto dto1 : prescriptionDto.getPrescriptionDetailsDtosList()) {
						if (dto1.getIsNonFormulary() != null && dto1.getIsNonFormulary() == 'Y') {
							count++;
						}
					}

				}
				if (count == 0) {
					prescriptionDto.setIsNonFormulary('N');
				} else if (prescriptionDto.getPrescriptionDetailsDtosList() != null
						&& prescriptionDto.getPrescriptionDetailsDtosList().size() == count) {
					prescriptionDto.setIsNonFormulary('Y');
				} else {
					prescriptionDto.setIsNonFormulary('N');
				}
				prescriptionDto.setIsNonFormulary('N');
				prescriptionDto.setPrescriptionNo(prescriptionNo);
				Response presRes = iMedicationDAO.savePrescription(prescriptionDto);

				prescriptionDto = (PrescriptionDto) presRes.getObject();
			}

			ListIterator<PrescriptionDetailsDto> iterator = prescriptionDto.getPrescriptionDetailsDtosList()
					.listIterator();
			while (iterator.hasNext()) {
				PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
				prescriptionDetailsDto.setOrganizationId(prescriptionDto.getOrganizationId());
				prescriptionDetailsDto.setUnitId(prescriptionDto.getUnitId());
				if (prescriptionId != null) {
					prescriptionDetailsDto.setPrescriptionId(prescriptionId);
				} else {
					prescriptionDetailsDto.setPrescriptionId(prescriptionDto.getPrescriptionId());
				}

				prescriptionDetailsDto.setIsDiscontinued(FALSE);
				prescriptionDetailsDto.setIsMedicationStop('N');
				prescriptionDetailsDto.setIsPreStop(FALSE);
				prescriptionDetailsDto.setIsIntervention(FALSE);
				prescriptionDetailsDto.setDuration(
						prescriptionDetailsDto.getDuration() != null ? prescriptionDetailsDto.getDuration() : null);
				prescriptionDetailsDto.setDurationUnit(
						prescriptionDetailsDto.getDurationUnit() != null ? prescriptionDetailsDto.getDurationUnit()
								: null);
				prescriptionDetailsDto.setAddedBy(prescriptionDto.getAddedBy());
				prescriptionDetailsDto.setAddedDate(prescriptionDto.getAddedDate());
				prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
				prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
				prescriptionDetailsDto.setIsDeleted(FALSE);
				prescriptionDetailsDto.setIsActive(TRUE);
				prescriptionDetailsDto.setStatusId(1);
				prescriptionDetailsDto.setPrnflag(FALSE);
				prescriptionDetailsDto.setItemId(
						prescriptionDetailsDto.getItemId() != null ? prescriptionDetailsDto.getItemId() : null);
				prescriptionDetailsDto.setPrescribedDose(
						prescriptionDetailsDto.getPrescribedDose() != null ? prescriptionDetailsDto.getPrescribedDose()
								: null);
				prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
				prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
				prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
				prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
				prescriptionDetailsDto.setInfusionflag(FALSE);
				prescriptionDetailsDto.setUpdatedBy(prescriptionDetailsDto.getAddedBy());
				prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionText() != null
						? prescriptionDetailsDto.getInstructionText()
						: prescriptionDetailsDto.getInstructionDesc());
				prescriptionDetailsDto.setUpdatedDate(prescriptionDetailsDto.getAddedDate());
				if (prescriptionDetailsDto.getIsDischarge() != null) {
					prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
				} else {
					prescriptionDetailsDto.setIsDischarge(false);
				}

				if (prescriptionDetailsDto.getIsNonFormulary() != null && prescriptionDetailsDto.getIsNonFormulary() == 'N') {
					nonFormulary = prescriptionDetailsDto.getIsNonFormulary();
				}
//						prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
//						prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
				Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);

				prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();

				iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);

				/*
				 * Response responseListData =
				 * iMedicationDAO.getPriorityOfPrescriptionAccordingToPresDtl(
				 * prescriptionDetailsDto); if(responseListData.getListObject() != null &&
				 * responseListData.getListObject().size() > 0) {
				 * iMedicationDAO.updatePriorityOfPrescriptionAccordingToPresDtl(
				 * prescriptionDetailsDto.getPrescriptionId()); }
				 */

//					Integer prescriptionId1=null;
//					if(prescriptionId!=null) {
//						prescriptionId1=prescriptionId;
//					}else {
//						prescriptionId1=prescriptionDto.getPrescriptionId();
//					}

			}
			Integer checckRecordExistOrNot = iMedicationDAO.getFormularyMedicationCount(prescriptionId);
			if (checckRecordExistOrNot > 0 || nonFormulary == 'N') {
				iMedicationDAO.updateIsFormularyFlagForPrescription(prescriptionDto.getEncounterId(), prescriptionId);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getActiveMedication(Integer patientId, String currentDate, Integer unitId)
			throws ApplicationException {
		try {
			return iMedicationDAO.getActiveMedication(patientId, currentDate, unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	public Response getMedicationDetails(PrescriptionDto prescriptionDto) throws ApplicationException {
		try {
			Response response = iMedicationDAO.getMedicationDetails(prescriptionDto);
			if (response.getListObject() != null && response.getListObject().size() > 0) {

				List<MedicationDTO> listDto = response.getListObject();
				List<BigInteger> predetailIds = listDto.stream().map(MedicationDTO::getPrescriptionDetailsid).collect(Collectors.toList());
				Response response1 = iMedicationStopDao.checkParentSubstituteId(predetailIds);
				for (MedicationDTO dto : listDto) {

					if (response1.getListObject() != null && response1.getListObject().size() > 0) {
						
						List<MedicationDTO> listDto1 = response.getListObject();
						
						MedicationDTO dummyserviceCount = listDto1.stream()
								  .filter(order -> dto.getPrescriptionDetailsid().equals(order.getPrescriptionDetailsid()))
								  .findAny()
								  .orElse(null);
						
						if(dummyserviceCount!=null){
							dto.setIsSubstituteAvail(true);
						}else{
							dto.setIsSubstituteAvail(false);
						}
						
					} else {
						dto.setIsSubstituteAvail(false);
					}
				}

				return new Response<>(SUCCESS, null, null, listDto, null);

			} else {

				return response;
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getDrugList(Integer unitId, Integer orgId, String searchDrug) throws ApplicationException {
		try {
			return iMedicationDAO.getDrugListByTypeSearch(unitId, orgId, searchDrug);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getRoaList(Integer unitId, Integer orgId, Integer formulationTypeId, Integer genericId,
			Integer patientId) throws ApplicationException {
		try {

			return iMedicationDAO.getRoaListByFormulationTypeId(unitId, orgId, formulationTypeId, genericId, patientId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getFrequencyList(Integer orgId, Integer unitId, String searchPar) throws ApplicationException {
		try {
			return iMedicationDAO.getFrequencyList(unitId, orgId, searchPar);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateMedication(MedicationDTO medicationDTO) throws ApplicationException {
		return iMedicationDAO.updateMedication(medicationDTO);
	}

	@Override
	@Transactional
	public Response deleteMedication(Integer medicationId) throws ApplicationException {
		return iMedicationDAO.deleteMedication(medicationId);
	}

	@Override
	public Response getMedicIntructions() throws ApplicationException {
		return iMedicationDAO.getMedicIntructions();
	}

	@Override
	public Response getMedicFavorites(Integer doctorId) throws ApplicationException {
		return iMedicationDAO.getMedicFavorites(doctorId);
	}

	private RoaMasterDto fillRoaMasterDto(DrugMasterDto drugMasterDto) {
		RoaMasterDto roaMasterDto = new RoaMasterDto();
		roaMasterDto.setRoa_details(drugMasterDto.getRoa_details());
		roaMasterDto.setRoa_id(drugMasterDto.getRoa_id());
		return roaMasterDto;
	}

	private boolean checkIfDrugExistInList(List<DrugsListDTO> drugsListDTOs, String medName) {
		for (DrugsListDTO drugsListDTO : drugsListDTOs) {
			if (drugsListDTO.getMedicationName().equals(medName)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkIfDoseExistInList(List<RoaMasterDto> roaMasterDtos, String roaName) {
		for (RoaMasterDto roaMasterDto : roaMasterDtos) {
			if (roaMasterDto.getRoa_details().equals(roaName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Response getLastOrder(Integer patientId, Integer encounterId) throws ApplicationException {
		return iMedicationDAO.getLastOrder(patientId, encounterId);
	}

	@Override
	public Response getFrequencyDetails() throws ApplicationException {
		return iMedicationDAO.getFrequencyDetails();
	}

	@Override
	public Response getRoaMasters() throws ApplicationException {
		return iMedicationDAO.getRoaMasters();
	}

	@Override
	@Transactional
	public Response getUOMList(Integer orgId, Integer unitId, Integer itemId) throws ApplicationException {
		try {
			return iMedicationDAO.getUOMList(unitId, orgId, itemId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	public static String getPrescriptionNumber() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder("PRES00");
		Random rnd = new Random();
		while (salt.length() < 10) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	@Override
	@Transactional
	public Response getEncounterMedicationHistory(MedicationDTO medicationDTO) throws ApplicationException {
		try {
			return iMedicationDAO.getEncounterMedicationHistory(medicationDTO);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getMedicationHistoryByEncounterId(MedicationDTO medicationDTO) throws ApplicationException {
		try {
			return iMedicationDAO.getMedicationHistoryByEncounterId(medicationDTO);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getMedicationByAdmissionId(Integer admissionId) throws ApplicationException {
		try {
			return iMedicationDAO.getMedicationByAdmissionId(admissionId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getICPCSYSTEMList() throws ApplicationException {
		try {
			return iMedicationDAO.getICPCSYSTEMList();
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getICPCMasterList(Integer icpcSyatemId, String searchICPC) throws ApplicationException {
		try {
			return iMedicationDAO.getICPCMasterList(icpcSyatemId, searchICPC);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response saveMedicationHistory(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			Iterator itr = medicationHistoryDto.getMedicationHistoryList().iterator();
			while (itr.hasNext()) {
				MedicationHistoryDto dto = new MedicationHistoryDto();
				MedicationHistoryDto dto2 = (MedicationHistoryDto) itr.next();
				dto.setCreatedDate(dto2.getCreatedDate());
				dto.setCreatedBy(dto2.getCreatedBy());
				dto.setPatientId(medicationHistoryDto.getPatientId());
				dto.setItemId(dto2.getItemId());
				dto.setStatus(dto2.getStatus());
				dto.setDoctorId(dto2.getDoctorId());
				dto.setDuration(dto2.getDuration());
				if (medicationHistoryDto.getEncounterId() != null) {
					dto.setEncounterId(medicationHistoryDto.getEncounterId());
				} else {
					dto.setAdmissionId(medicationHistoryDto.getAdmissionId());
				}
				dto.setIsNonFormulary(dto2.getIsNonFormulary());
				dto.setIsNoSignificantMedication(dto2.getIsNoSignificantMedication());
				dto.setDurationUnit(dto2.getDurationUnit());
				dto.setStartDate(dto2.getStartDate());
				dto.setEndDate(dto2.getEndDate());
				dto.setGenericId(dto2.getGenericId());
				dto.setInstructionId(dto2.getInstructionId());
				dto.setRoaId(dto2.getRoaId());
				dto.setGenericName(dto2.getGenericName());
				dto.setItemName(dto2.getItemName());
				dto.setDoseId(dto2.getDoseId());
				dto.setFrequencyId(dto2.getFrequencyId());
				dto.setIsMedicationContinue(dto2.getIsMedicationContinue());
				dto.setIsMedicationStopHis(dto2.getIsMedicationStopHis());
				dto.setJustificationStopHis(dto2.getJustificationStopHis());
				dto.setOrganizationId(medicationHistoryDto.getOrganizationId());
				dto.setUnitId(medicationHistoryDto.getUnitId());
				dto.setCreatedBy(dto2.getCreatedBy());
				dto.setUpdatedBy(dto2.getUpdatedBy());
				dto.setUpdatedDate(dto2.getUpdatedDate());
				dto.setCreatedDate(dto2.getCreatedDate());
				dto.setDoctorId(dto2.getDoctorId());
				dto.setDoseUnitId(dto2.getDoseUnitId());
				dto.setStoreId(dto2.getStoreId());
				dto.setOrderQty(dto2.getOrderQty());
				dto.setAuthorisedDate(dto2.getAuthorisedDate());

				iMedicationHistoryDao.saveMedicationHistory(dto);
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("saveMedicationHistory failed", e);
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response getMedicationHistoryList(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			return iMedicationHistoryDao.getMedicationHistoryList(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getMedicationHistoryList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getAdministrationWorkListList(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getAdministrationWorkListList(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getAdministrationWorkListList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getDetailedListOfMedication(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			return iMedicationHistoryDao.getDetailedListOfMedication(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getDetailedListOfMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getRoaListNonFormulary(Integer unitId, Integer orgId, Integer patientId)
			throws ApplicationException {
		try {
			return iMedicationDAO.getRoaListByFormulationTypeId(unitId, orgId, patientId);
		} catch (Exception e) {
		logger.error("getRoaListNonFormulary failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	@Transactional
	public Response getMasterListOfICPCSystem(Integer icpcSystemId, String searchICPC) throws ApplicationException {
		try {
			return iMedicationDAO.getMasterListOfICPCSystem(icpcSystemId, searchICPC);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getDateWiseListOfPatientMedication(Integer patientId, Integer organizationId, String fromDate,
			String toDate) throws ApplicationException {
		try {
			return iMedicationDAO.getDateWiseListOfPatientMedication(patientId, organizationId, fromDate, toDate);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getMedicationEncountersList(Integer patientId, Integer encounterId, Integer organizationId,
			Integer unitId) throws ApplicationException {
		try {
			return iMedicationHistoryDao.getMedicationEncountersList(patientId, encounterId, organizationId, unitId);
		} catch (Exception e) {
		logger.error("getMedicationEncountersList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	@Transactional
	public Response getMedicationListById(Integer prescriptionId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iMedicationDAO.getMedicationListById(prescriptionId, organizationId, unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getOPDMedicationListById(Integer prescriptionId, Integer prescriptionDetailsid, Integer patientId,
			Integer itemId, Integer unitId, Integer organizationId) throws ApplicationException {
		try {

			Response response = iMedicationStopDao.checkStatusFromIssueDetailsForUpdate(prescriptionId,
					prescriptionDetailsid, patientId, organizationId);
			if (response.getListObject() != null && response.getListObject().size() > 0) {

				return new Response<>(ERROR, ERROR_CODE,
						"The medication can not be edited, as it is already in process.", null, null);
			} else {

				return iMedicationDAO.getOPDMedicationListById(prescriptionId, prescriptionDetailsid, patientId, itemId,
						unitId, organizationId);
			}
		} catch (Exception e) {
		logger.error("getOPDMedicationListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response medicationUpdate(PrescriptionDto prescriptionDto) throws ApplicationException {
		try {

			String sql = " select status_id as \"statusId\" from pharmacy.t_prescription where id =:id ";

			PrescriptionDto dto = (PrescriptionDto) sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setInteger("id", prescriptionDto.getPrescriptionId())
					.setResultTransformer(Transformers.aliasToBean(PrescriptionDto.class)).uniqueResult();

			prescriptionDto.setPrescriptionId(prescriptionDto.getPrescriptionId());
			prescriptionDto.setIsIntervention(FALSE);
			prescriptionDto.setIsActive(TRUE);
			prescriptionDto.setIsDeleted(FALSE);
			if (prescriptionDto.getIsPharmacologicalValidationSkipIp() == true) {
				prescriptionDto.setIsValidate(TRUE);
			} else {
				prescriptionDto.setIsValidate(FALSE);
			}

			prescriptionDto.setIsDischarge(FALSE);
			prescriptionDto.setPrescriptionNo(prescriptionDto.getPrescriptionNo());
			prescriptionDto.setStatusId(dto.getStatusId());
			prescriptionDto.setIsMedicationStop('N');
			prescriptionDto.setMedicationType(prescriptionDto.getMedicationType());
			prescriptionDto.setUpdatedBy(prescriptionDto.getAddedBy());
			prescriptionDto.setUpdatedDate(prescriptionDto.getAddedDate());
			int count = 0;
			if (prescriptionDto.getPrescriptionDetailsDtosList().size() > 0) {
				for (PrescriptionDetailsDto dto1 : prescriptionDto.getPrescriptionDetailsDtosList()) {
					if (dto1.getIsNonFormulary() == 'Y') {
						count++;
					}
				}

			}
			if (count == 0) {
				prescriptionDto.setIsNonFormulary('N');
			} else if (prescriptionDto.getPrescriptionDetailsDtosList() != null
					&& prescriptionDto.getPrescriptionDetailsDtosList().size() == count) {
				prescriptionDto.setIsNonFormulary('Y');
			} else {
				prescriptionDto.setIsNonFormulary('N');
			}
			prescriptionDto.setIsNonFormulary('N');
			prescriptionDto.setPrescriptionNo(prescriptionDto.getPrescriptionNo());
			Response presRes = iMedicationDAO.updatePrescription(prescriptionDto);
			PrescriptionDto prescriptionDto1 = (PrescriptionDto) presRes.getObject();
			ListIterator<PrescriptionDetailsDto> iterator = prescriptionDto.getPrescriptionDetailsDtosList()
					.listIterator();
			while (iterator.hasNext()) {
				PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
				prescriptionDetailsDto.setPrescriptionDetailsid(prescriptionDetailsDto.getPrescriptionDetailsid());
				prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId());
				prescriptionDetailsDto.setDuration(
						prescriptionDetailsDto.getDuration() != null ? prescriptionDetailsDto.getDuration() : null);
				prescriptionDetailsDto.setDurationUnit(
						prescriptionDetailsDto.getDurationUnit() != null ? prescriptionDetailsDto.getDurationUnit()
								: null);
				prescriptionDetailsDto.setFrequencyId(prescriptionDetailsDto.getFrequencyId());
				prescriptionDetailsDto.setInstructionId(prescriptionDetailsDto.getInstructionId());
				prescriptionDetailsDto.setRouteId(prescriptionDetailsDto.getRouteId());
				prescriptionDetailsDto.setPrescriptionFrequencyId(prescriptionDetailsDto.getPrescriptionFrequencyId());
				prescriptionDetailsDto.setOrganizationId(prescriptionDto.getOrganizationId());
				prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
				prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
				prescriptionDetailsDto.setUnitId(prescriptionDto.getUnitId());
				prescriptionDetailsDto.setIsMedicationStop('N');
				prescriptionDetailsDto.setPrescriptionId(prescriptionDetailsDto.getPrescriptionId());
				prescriptionDetailsDto.setIsDiscontinued(FALSE);
				prescriptionDetailsDto.setIsPreStop(FALSE);
				prescriptionDetailsDto.setIsIntervention(FALSE);
				prescriptionDetailsDto.setAddedBy(prescriptionDto.getAddedBy());
				prescriptionDetailsDto.setAddedDate(prescriptionDto.getAddedDate());
				prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
				prescriptionDetailsDto.setIsDeleted(FALSE);
				prescriptionDetailsDto.setIsActive(TRUE);
				prescriptionDetailsDto.setStatusId(1);
				prescriptionDetailsDto.setPrnflag(FALSE);
				prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
				prescriptionDetailsDto.setInfusionflag(FALSE);
				prescriptionDetailsDto.setUpdatedBy(prescriptionDto.getUpdatedBy());
				prescriptionDetailsDto.setUpdatedDate(prescriptionDto.getUpdatedDate());
				if (prescriptionDetailsDto.getIsDischarge() != null) {
					prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
				} else {
					prescriptionDetailsDto.setIsDischarge(false);
				}
				Response resPreDtls = iMedicationDAO.updatePrescriptionDetail(prescriptionDetailsDto);

				prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();

				iMedicationDAO.updatePrescriptionFrequencyDetails(prescriptionDetailsDto);

			}

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("medicationUpdate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDischargeMedication(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			return iMedicationDAO.getDischargeMedication(medicationDTOs);
		} catch (Exception e) {
		logger.error("getDischargeMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMedicationDetailsByDoctorId(PrescriptionDto prescriptionDto) throws ApplicationException {

		try {
			Response response = iMedicationDAO.getMedicationDetailsByDoctorId(prescriptionDto);

//			
			if (response.getListObject() != null && response.getListObject().size() > 0) {

				List<MedicationDTO> listDto = response.getListObject();
				List<BigInteger> predetailIds = listDto.stream().map(MedicationDTO::getPrescriptionDetailsid).collect(Collectors.toList());
				Response response1 = iMedicationStopDao.checkParentSubstituteId(predetailIds);
				for (MedicationDTO dto : listDto) {

					if (response1.getListObject() != null && response1.getListObject().size() > 0) {
						
						List<MedicationDTO> listDto1 = response.getListObject();
						
						MedicationDTO dummyserviceCount = listDto1.stream()
								  .filter(order -> dto.getPrescriptionDetailsid().equals(order.getPrescriptionDetailsid()))
								  .findAny()
								  .orElse(null);
						
						if(dummyserviceCount!=null){
							dto.setIsSubstituteAvail(true);
						}else{
							dto.setIsSubstituteAvail(false);
						}
						
					} else {
						dto.setIsSubstituteAvail(false);
					}
				}

				return new Response<>(SUCCESS, null, null, listDto, null);

			} else {

				return response;
			}
		} catch (Exception e) {
		logger.error("getMedicationDetailsByDoctorId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getStoreIdForNursing(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			return iMedicationDAO.getStoreIdForNursing(medicationDTOs);
		} catch (Exception e) {
		logger.error("getStoreIdForNursing failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getMedicationDetailsForMedicationHistory(PrescriptionDto prescriptionDto)
			throws ApplicationException {
		try {
			return iMedicationDAO.getMedicationDetailsForMedicationHistory(prescriptionDto);
		} catch (Exception e) {
		logger.error("getMedicationDetailsForMedicationHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getListOfLastEncounterAndAdmission(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			return iMedicationDAO.getListOfLastEncounterAndAdmission(medicationDTOs);
		} catch (Exception e) {
		logger.error("getListOfLastEncounterAndAdmission failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveJustificationForMedicationStop(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			return iMedicationStopDao.saveJustificationForMedicationStop(medicationDTOs);
		} catch (Exception e) {
		logger.error("saveJustificationForMedicationStop failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getLastEncounterAndAdmissionList(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			return iMedicationDAO.getLastEncounterAndAdmissionList(medicationDTOs);
		} catch (Exception e) {
		logger.error("getLastEncounterAndAdmissionList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveJustificationForMedicationStopForHistory(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.saveJustificationForMedicationStopForHistory(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("saveJustificationForMedicationStopForHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getContinuedMedicationList(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			return iMedicationHistoryDao.getContinuedMedicationList(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getContinuedMedicationList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMedicationHistoryListForBookView(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getMedicationHistoryListForBookView(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getMedicationHistoryListForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getGenericListForGenericSearch(Integer unitId, Integer orgId, String searchDrug)
			throws ApplicationException {
		try {
			return iMedicationDAO.getGenericListForGenericSearch(unitId, orgId, searchDrug);
		} catch (Exception e) {
		logger.error("getGenericListForGenericSearch failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getGenericListForBrandNameSearch(Integer unitId, Integer orgId, Integer genericId)
			throws ApplicationException {
		try {
			return iMedicationDAO.getGenericListForBrandNameSearch(unitId, orgId, genericId);
		} catch (Exception e) {
		logger.error("getGenericListForBrandNameSearch failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMasterListOfICPCSystemForCoversheet(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getMasterListOfICPCSystemForCoversheet(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getMasterListOfICPCSystemForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMasterListOfICPCSystemForCoversheetForCode(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getMasterListOfICPCSystemForCoversheetForCode(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getMasterListOfICPCSystemForCoversheetForCode failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public Response getDetailedListForAdministrationWorklist(AdministrationWorklistDto administrationWorklistDto)
			throws ApplicationException {
		try {

			List<AdministrationWorklistDto> itemList = new ArrayList<AdministrationWorklistDto>();
			List<AdministrationWorklistDto> presdtlList = new ArrayList<AdministrationWorklistDto>();
			Response details = iMedicationHistoryDao.getPrescriptionDetailsList(
					administrationWorklistDto.getPrescriptionId(), administrationWorklistDto.getStoreId());
			presdtlList = details.getListObject();

			for (AdministrationWorklistDto presDetail : presdtlList) {

				if (presDetail.getPrescriptionDetailId() != null && presDetail.getItemId() != null) {

					AdministrationWorklistDto item = new AdministrationWorklistDto();
					item.setAntibioticTest(presDetail.getAntibioticTest());
					item.setItemId(presDetail.getItemId());
					item.setItemName(presDetail.getItemName());
					item.setPrescriptionDetailId(presDetail.getPrescriptionDetailId());
					item.setGenericId(presDetail.getGenericId());
					item.setGenericName(presDetail.getGenericName());
					item.setProductCategoryId(presDetail.getProductCategoryId());
					item.setUomTypeId(presDetail.getUomTypeId());
					item.setUomUnitId(presDetail.getUomUnitId());
					item.setManufactureId(presDetail.getManufactureId());
					item.setManufacturerName(presDetail.getManufacturerName());
					item.setDose(presDetail.getDose());
					item.setDoseDesc(presDetail.getDoseDesc());
					item.setRoaDesc(presDetail.getRoaDesc());
					item.setTaxPercentage(presDetail.getTaxPercentage());
					item.setTaxId(presDetail.getTaxId());
					item.setStrengthDesc(presDetail.getStrengthDesc());
					item.setConsumableQuantity(presDetail.getConsumableQuantity());
					item.setBatchId(presDetail.getBatchId());
					item.setBatchNo(presDetail.getBatchNo());
					item.setQuantity(presDetail.getQuantity());
					item.setStatusId(presDetail.getStatusId());
					item.setPrescriptionId(presDetail.getPrescriptionId());
					item.setUnitName(presDetail.getUnitName());
					item.setExpiryDate(presDetail.getExpiryDate());
					item.setSaleDetailId(presDetail.getSaleDetailId());
					item.setAdministrationDate(presDetail.getAdministrationDate());
					item.setAdministrationTime(presDetail.getAdministrationTime());
					item.setAdministrationRemark(presDetail.getAdministrationRemark());
					item.setAdministratorQty(
							presDetail.getAdministratorQty() != null ? presDetail.getAdministratorQty() : 0);
					item.setAdministrationWorklistTimeDetailsId(presDetail.getAdministrationWorklistTimeDetailsId());
					item.setPrescribeDose(presDetail.getPrescribeDose());
					item.setPrescribeDoseUnit(presDetail.getPrescribeDoseUnit());
					item.setFactorId(presDetail.getFactorId());
					item.setDosageForm(presDetail.getDosageForm());
					item.setFactorId(presDetail.getFactorId());
					item.setFormulationDosageMappId(presDetail.getFormulationDosageMappId());
					item.setIsMedicationStop(presDetail.getIsMedicationStop());
					itemList.add(item);

				}

			}

			return new Response<>(SUCCESS, null, null, itemList, null);

//			return iMedicationHistoryDao.getDetailedListForAdministrationWorklist(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getDetailedListForAdministrationWorklist failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getBatchListForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getBatchListForAdministrationWorklist(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getBatchListForAdministrationWorklist failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getUomListByBatchIdForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getUomListByBatchIdForAdministrationWorklist(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getUomListByBatchIdForAdministrationWorklist failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getSubstituteListForActiveMedication(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getSubstituteListForActiveMedication(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getSubstituteListForActiveMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getBrandListByGenericIdForVaccinationChart(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getBrandListByGenericIdForVaccinationChart(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getBrandListByGenericIdForVaccinationChart failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response updateAssignPickerAndIssueDetails(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			return iMedicationStopDao.updateAssignPickerAndIssueDetails(medicationDTOs);
		} catch (Exception e) {
		logger.error("updateAssignPickerAndIssueDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveJustificationForMedicationDelete(PrescriptionDto medicationDTOs) throws ApplicationException {
		Response response = iMedicationStopDao.checkStatusFromIssueDetails(medicationDTOs);
		try {
			if (response.getListObject() != null && response.getListObject().size() > 0) {
				Integer checckRecordExistOrNot = iMedicationDAO
						.getFormularyMedicationCount(medicationDTOs.getPrescriptionId());
				if (checckRecordExistOrNot > 0) {
					iMedicationDAO.updateIsFormularyFlagForPrescription(medicationDTOs.getEncounterId(),
							medicationDTOs.getPrescriptionId());
				} else {
					iMedicationDAO.updateIsFormularyFlagForPrescriptionY(medicationDTOs.getEncounterId(),
							medicationDTOs.getPrescriptionId());
				}
				return new Response<>(ERROR, ERROR_CODE,
						"The medication can not be deleted, as it is already in process.", null, null);
			} else {
				if (medicationDTOs.getEncounterId() != null) {
					iAdvisedDateAppointmentDao.updateCancelStatusInAdvisedAppointment(medicationDTOs);
				}
				if (medicationDTOs.getJustificationStop() != null && medicationDTOs.getJustificationStop() != "") {
					Response res = iMedicationStopDao.getCountOfMedicationsByPresciptionId(medicationDTOs);
					BigInteger d1 = null;
					d1 = (BigInteger) res.getObject();
					if (d1.intValue() == 1) {

						Response res1 = iMedicationStopDao.saveJustificationForMedicationDelete(medicationDTOs);
						if (res1.getStatus().equals(SUCCESS)) {
							Response res2 = this.updateAssignPickerAndIssueDetails(medicationDTOs);
							iMedicationStopDao.saveMedicationDeleteForPrescription(medicationDTOs);
							Integer checckRecordExistOrNot = iMedicationDAO
									.getFormularyMedicationCount(medicationDTOs.getPrescriptionId());
							if (checckRecordExistOrNot > 0) {
								iMedicationDAO.updateIsFormularyFlagForPrescription(medicationDTOs.getEncounterId(),
										medicationDTOs.getPrescriptionId());
							} else {
								iMedicationDAO.updateIsFormularyFlagForPrescriptionY(medicationDTOs.getEncounterId(),
										medicationDTOs.getPrescriptionId());
							}
							return res2;
						}

					} else {
						Response res1 = iMedicationStopDao.saveJustificationForMedicationDelete(medicationDTOs);
						if (res1.getStatus().equals(SUCCESS)) {
							Integer checckRecordExistOrNot = iMedicationDAO
									.getFormularyMedicationCount(medicationDTOs.getPrescriptionId());
							if (checckRecordExistOrNot > 0) {
								iMedicationDAO.updateIsFormularyFlagForPrescription(medicationDTOs.getEncounterId(),
										medicationDTOs.getPrescriptionId());
							} else {
								iMedicationDAO.updateIsFormularyFlagForPrescriptionY(medicationDTOs.getEncounterId(),
										medicationDTOs.getPrescriptionId());
							}
							return iMedicationStopDao.updateAssignPickerAndIssueDetailsTest(medicationDTOs);
						}
					}
				} else {
					Integer checckRecordExistOrNot = iMedicationDAO
							.getFormularyMedicationCount(medicationDTOs.getPrescriptionId());
					if (checckRecordExistOrNot > 0) {
						iMedicationDAO.updateIsFormularyFlagForPrescription(medicationDTOs.getEncounterId(),
								medicationDTOs.getPrescriptionId());
					} else {
						iMedicationDAO.updateIsFormularyFlagForPrescriptionY(medicationDTOs.getEncounterId(),
								medicationDTOs.getPrescriptionId());
					}
					return new Response<>(ERROR, ERROR_CODE, "Please enter reason to delete medication.", null, null);
				}
			}
		} catch (Exception e) {
		logger.error("saveJustificationForMedicationDelete failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getAvailableStokeByItemId(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			return iMedicationHistoryDao.getAvailableStokeByItemId(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getAvailableStokeByItemId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response searchMedicationByBrandName(Integer unitId, Integer orgId, String searchKeyword)
			throws ApplicationException {
		try {
			return iMedicationDAO.searchMedicationByBrandName(unitId, orgId, searchKeyword);
		} catch (Exception e) {
		logger.error("searchMedicationByBrandName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getPatientMedicationHistoryForDoctorApp(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			return iMedicationHistoryDao.getPatientMedicationHistoryForDoctorApp(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("getPatientMedicationHistoryForDoctorApp failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getOrderedMedicationsForDoctorApp(PrescriptionDto prescriptionDto) throws ApplicationException {
		try {
			return iMedicationDAO.getOrderedMedicationsForDoctorApp(prescriptionDto);
		} catch (Exception e) {
		logger.error("getOrderedMedicationsForDoctorApp failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getCheckIsNarcoticItem(Integer itemId) throws ApplicationException {
		try {
			return iMedicationDAO.getCheckIsNarcoticItem(itemId);
		} catch (Exception e) {
		logger.error("getCheckIsNarcoticItem failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response saveAdvisedDateAppointment(AdvisedDateAppointmentDto advisedDateAppointmentDto)
			throws ApplicationException {
		try {
			Response res = iAdvisedDateAppointmentDao.checkAppointmentAvailable(advisedDateAppointmentDto);
			if (res.getListObject() != null && res.getListObject().size() > 0) {

				return iAdvisedDateAppointmentDao.updateAdvisedDateAppointment(advisedDateAppointmentDto);

			}
			return iAdvisedDateAppointmentDao.saveAdvisedDateAppointment(advisedDateAppointmentDto);
		} catch (Exception e) {
		logger.error("saveAdvisedDateAppointment failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response deleteMedicationHistory(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			return iMedicationHistoryDao.deleteMedicationHistory(medicationHistoryDto);
		} catch (Exception e) {
		logger.error("deleteMedicationHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getAdvisedDateAppointmentList(AdvisedDateAppointmentDto advisedDateAppointmentDto)
			throws ApplicationException {
		try {
			return iAdvisedDateAppointmentDao.getAdvisedDateAppointmentList(advisedDateAppointmentDto);
		} catch (Exception e) {
		logger.error("getAdvisedDateAppointmentList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getAdvisedDateAppointmentListCount(AdvisedDateAppointmentDto advisedDateAppointmentDto)
			throws ApplicationException {
		try {
			return iAdvisedDateAppointmentDao.getAdvisedDateAppointmentListCount(advisedDateAppointmentDto);
		} catch (Exception e) {
		logger.error("getAdvisedDateAppointmentListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getFrequentlyUsedMedicationList(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			return iMedicationDAO.getFrequentlyUsedMedicationList(medicationDTOs);
		} catch (Exception e) {
		logger.error("getFrequentlyUsedMedicationList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getOPDMedicationListByIdForNonFormulary(Integer prescriptionId, Integer prescriptionDetailsid,
			Integer patientId, Integer unitId, Integer organizationId) throws ApplicationException {
		try {

			Response response = iMedicationStopDao.checkStatusFromIssueDetailsForUpdate(prescriptionId,
					prescriptionDetailsid, patientId, organizationId);
			if (response.getListObject() != null && response.getListObject().size() > 0) {

				return new Response<>(ERROR, ERROR_CODE,
						"The medication can not be edited, as it is already in process.", null, null);
			} else {

				return iMedicationDAO.getOPDMedicationListByIdForNonFormulary(prescriptionId, prescriptionDetailsid,
						patientId, unitId, organizationId);
			}
		} catch (Exception e) {
		logger.error("getOPDMedicationListByIdForNonFormulary failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateMedicationHistory(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			Iterator itr = medicationHistoryDto.getMedicationHistoryList().iterator();
			while (itr.hasNext()) {
				MedicationHistoryDto dto = new MedicationHistoryDto();
				MedicationHistoryDto dto2 = (MedicationHistoryDto) itr.next();
				dto.setCreatedDate(dto2.getCreatedDate());
				dto.setMedicationHistoryId(dto2.getMedicationHistoryId());
				dto.setCreatedBy(dto2.getCreatedBy());
				dto.setPatientId(medicationHistoryDto.getPatientId());
				dto.setItemId(dto2.getItemId());
				dto.setStatus(dto2.getStatus());
				dto.setDoctorId(dto2.getDoctorId());
				dto.setDuration(dto2.getDuration());
				if (medicationHistoryDto.getEncounterId() != null) {
					dto.setEncounterId(medicationHistoryDto.getEncounterId());
				} else {
					dto.setAdmissionId(medicationHistoryDto.getAdmissionId());
				}
				dto.setIsNonFormulary(dto2.getIsNonFormulary());
				dto.setIsNoSignificantMedication(dto2.getIsNoSignificantMedication());
				dto.setDurationUnit(dto2.getDurationUnit());
				dto.setStartDate(dto2.getStartDate());
				dto.setEndDate(dto2.getEndDate());
				dto.setGenericId(dto2.getGenericId());
				dto.setInstructionId(dto2.getInstructionId());
				dto.setRoaId(dto2.getRoaId());
				dto.setGenericName(dto2.getGenericName());
				dto.setItemName(dto2.getItemName());
				dto.setDoseId(dto2.getDoseId());
				dto.setFrequencyId(dto2.getFrequencyId());
				dto.setIsMedicationContinue(dto2.getIsMedicationContinue());
				dto.setIsMedicationStopHis(dto2.getIsMedicationStopHis());
				dto.setJustificationStopHis(dto2.getJustificationStopHis());
				dto.setOrganizationId(medicationHistoryDto.getOrganizationId());
				dto.setUnitId(medicationHistoryDto.getUnitId());
				dto.setCreatedBy(dto2.getCreatedBy());
				dto.setUpdatedBy(dto2.getUpdatedBy());
				dto.setUpdatedDate(dto2.getUpdatedDate());
				dto.setCreatedDate(dto2.getCreatedDate());
				dto.setDoctorId(dto2.getDoctorId());
				dto.setDoseUnitId(dto2.getDoseUnitId());
				dto.setStoreId(dto2.getStoreId());
				dto.setOrderQty(dto2.getOrderQty());
				dto.setAuthorisedDate(dto2.getAuthorisedDate());

				iMedicationHistoryDao.updateMedicationHistory(dto);
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("updateMedicationHistory failed", e);
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response saveCIMMS(PrescriptionDto prescriptionDto) throws ApplicationException {
		try {
			return iMedicationDAO.saveCIMMS(prescriptionDto);
		} catch (Exception e) {
		logger.error("saveCIMMS failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response searchDrugByGenericNameOrItemName(Integer unitId, Integer orgId, String searchDrug)
			throws ApplicationException {
		try {
			return iMedicationDAO.searchDrugByGenericNameOrItemName(unitId, orgId, searchDrug);
		} catch (Exception e) {
		logger.error("searchDrugByGenericNameOrItemName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveAdministratorWorkList(List<AdministrationWorklistTimeDetailsDto> list)
			throws ApplicationException {
		try {

			for (AdministrationWorklistTimeDetailsDto administrationWorklistTimeDetailsDto : list) {
				iAdministrationWorklistTimeDetailsDao.saveAdministratorWorkList(administrationWorklistTimeDetailsDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("saveAdministratorWorkList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getItemListWithSubstituteForNewEMR(MedicationListDto dto) throws ApplicationException {
		try {
			return iMedicationDAO.getItemListWithSubstituteForNewEMR(dto);
		} catch (Exception e) {
		logger.error("getItemListWithSubstituteForNewEMR failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getSubstituteListForNewEMR(MedicationListDto dto) throws ApplicationException {
		try {
			return iMedicationDAO.getSubstituteListForNewEMR(dto);
		} catch (Exception e) {
		logger.error("getSubstituteListForNewEMR failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getNonFormularyItemForWorklistPharmacy(Integer prescriptionId, Integer unitId,
			Integer organizationId) throws ApplicationException {
		try {
			return iMedicationDAO.getNonFormularyItemForWorklistPharmacy(prescriptionId, unitId, organizationId);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response deleteMedicationForPharmacy(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			return iMedicationStopDao.saveJustificationForMedicationDelete(medicationDTOs);
		} catch (Exception e) {
		logger.error("deleteMedicationForPharmacy failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveMedicationPrescriptionDrtailsSkipPrescription(PrescriptionDto medicationDTOs)
			throws ApplicationException {
		try {
			ListIterator<PrescriptionDetailsDto> iterator = medicationDTOs.getPrescriptionDetailsDtosList()
					.listIterator();
			while (iterator.hasNext()) {
				PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
				prescriptionDetailsDto.setOrganizationId(medicationDTOs.getOrganizationId());
				prescriptionDetailsDto.setUnitId(medicationDTOs.getUnitId());
				prescriptionDetailsDto.setPrescriptionId(medicationDTOs.getPrescriptionId());
				prescriptionDetailsDto.setIsDiscontinued(FALSE);
				prescriptionDetailsDto.setIsMedicationStop('N');
				prescriptionDetailsDto.setIsPreStop(FALSE);
				prescriptionDetailsDto.setIsIntervention(FALSE);
				prescriptionDetailsDto.setDuration(
						prescriptionDetailsDto.getDuration() != null ? prescriptionDetailsDto.getDuration() : null);
				prescriptionDetailsDto.setDurationUnit(
						prescriptionDetailsDto.getDurationUnit() != null ? prescriptionDetailsDto.getDurationUnit()
								: null);
				prescriptionDetailsDto.setAddedBy(medicationDTOs.getAddedBy());
				prescriptionDetailsDto.setAddedDate(medicationDTOs.getAddedDate());
				prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
				prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
				prescriptionDetailsDto.setIsDeleted(FALSE);
				prescriptionDetailsDto.setIsActive(TRUE);
				prescriptionDetailsDto.setStatusId(1);
				prescriptionDetailsDto.setPrnflag(FALSE);
				prescriptionDetailsDto.setItemId(
						prescriptionDetailsDto.getItemId() != null ? prescriptionDetailsDto.getItemId() : null);
				prescriptionDetailsDto.setPrescribedDose(
						prescriptionDetailsDto.getPrescribedDose() != null ? prescriptionDetailsDto.getPrescribedDose()
								: null);
				prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
				prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
				prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
				prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
				prescriptionDetailsDto.setInfusionflag(FALSE);
				prescriptionDetailsDto.setUpdatedBy(prescriptionDetailsDto.getAddedBy());
				prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionText() != null
						? prescriptionDetailsDto.getInstructionText()
						: prescriptionDetailsDto.getInstructionDesc());
				prescriptionDetailsDto.setUpdatedDate(prescriptionDetailsDto.getAddedDate());
				if (prescriptionDetailsDto.getIsDischarge() != null) {
					prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
				} else {
					prescriptionDetailsDto.setIsDischarge(false);
				}
				prescriptionDetailsDto.setIsNonFormulary('N');
				Response resPreDtls = iMedicationDAO.savePrescriptionDetaisForPharmacy(prescriptionDetailsDto);

				prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();

				iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);

			}

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, medicationDTOs);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getIssueDetailsId(PrescriptionDetailsDto dto) throws ApplicationException {
		try {
			return iMedicationDAO.getIssueDetailsId(dto);
		} catch (Exception e) {
			throw e;
		}
		// return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveJustificationForMedicationDeleteMultipleMedication(PrescriptionDto medicationDTOs)
			throws ApplicationException {
		Response response = iMedicationStopDao.checkStatusFromIssueDetailsMultipleMedication(medicationDTOs);
		try {
			if (response.getListObject() != null && response.getListObject().size() > 0) {
				return new Response<>(ERROR, ERROR_CODE,
						"The medication can not be deleted, as it is already in process.", null, null);
			} else {
				if (medicationDTOs.getEncounterId() != null) {
					iAdvisedDateAppointmentDao.updateCancelStatusInAdvisedAppointment(medicationDTOs);
				}
				Response res1 = iMedicationStopDao.saveJustificationForMedicationDeleteMultipleMedication(medicationDTOs);
				if (res1.getStatus().equals(SUCCESS)) {
					Response res2 = iMedicationStopDao.updateAssignPickerAndIssueDetailsMultipleMedication(medicationDTOs);
					iMedicationStopDao.saveMedicationDeleteForPrescriptionMultipleMedication(medicationDTOs);
					iMedicationStopDao.updateAssignPickerAndIssueDetailsTestMultipleMedication(medicationDTOs);
					return res2;
				} 
			}
		} catch (Exception e) {
		logger.error("saveJustificationForMedicationDeleteMultipleMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	@Transactional
	public Response getUniqueDates(PrescriptionDto dto) throws ApplicationException {
		try{
			List<String> distinctDates = dto.getDates();
			PrescriptionDto resDto = dto;

			if(dto.getPersonalHistoryCurrentList() != null && dto.getPersonalHistoryCurrentList().size()>0) {
				for (PatientPersonalHistoryDetailsDto personalHistory : dto.getPersonalHistoryCurrentList()) {
					String date = personalHistory.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}
			if(dto.getPersonalHistoryCurrentList() != null && dto.getPersonalHistoryCurrentList().size()>0) {
				for (PatientPersonalHistoryDetailsDto personalHistory : dto.getPersonalHistoryCurrentList()) {
					String date = personalHistory.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}
			if(dto.getPersonalHistoryOldList() != null && dto.getPersonalHistoryOldList().size()>0) {
				for (PatientPersonalHistoryDetailsDto personalHistory : dto.getPersonalHistoryOldList()) {
					String date = personalHistory.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getMedicationList() != null && dto.getMedicationList().size()>0) {
				for (MedicationDTO medication : dto.getMedicationList()) {
					String date = medication.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getFamilyHistoryOldList() != null && dto.getFamilyHistoryOldList().size()>0) {
				for (PatientFamilyHistoryDto item : dto.getFamilyHistoryOldList()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getFamilyHistoryCurrentList() != null && dto.getFamilyHistoryCurrentList().size()>0) {
				for (PatientFamilyHistoryDto item : dto.getFamilyHistoryCurrentList()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getSurgicalHistoryOldList() != null && dto.getSurgicalHistoryOldList().size()>0) {
				for (PatientSurgicalHistoryDto item : dto.getSurgicalHistoryOldList()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getSurgicalHistoryCurrentList() != null && dto.getSurgicalHistoryCurrentList().size()>0) {
				for (PatientSurgicalHistoryDto item : dto.getSurgicalHistoryCurrentList()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getMedicalHistoryOldList() != null && dto.getMedicalHistoryOldList().size()>0) {
				for (PatientMedicalHistoryDto item : dto.getMedicalHistoryOldList()) {
					String date = item.getCreated_date1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getMedicalHistoryCurrentList() != null && dto.getMedicalHistoryCurrentList().size()>0) {
				for (PatientMedicalHistoryDto item : dto.getMedicalHistoryCurrentList()) {
					String date = item.getCreated_date1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getTravelHistoryOldList() != null && dto.getTravelHistoryOldList().size()>0) {
				for (PatientTravelHistoryDetailsDto item : dto.getTravelHistoryOldList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getTravelHistoryCurrentList() != null && dto.getTravelHistoryCurrentList().size()>0) {
				for (PatientTravelHistoryDetailsDto item : dto.getTravelHistoryCurrentList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getImmunizationHistoryOldList() != null && dto.getImmunizationHistoryOldList().size()>0) {
				for (ImmunizationHistoryMapperDto item : dto.getImmunizationHistoryOldList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getImmunizationHistoryCurrentList() != null && dto.getImmunizationHistoryCurrentList().size()>0) {
				for (ImmunizationHistoryMapperDto item : dto.getImmunizationHistoryCurrentList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getPatientHabitOldList() != null && dto.getPatientHabitOldList().size()>0) {
				for (PatientHabitDetailsDto item : dto.getPatientHabitOldList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getPersonalHistoryCurrentList() != null && dto.getPersonalHistoryCurrentList().size()>0) {
				for (PatientPersonalHistoryDetailsDto item : dto.getPersonalHistoryCurrentList()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getNursingNoteList() != null && dto.getNursingNoteList().size()>0) {
				for (NursingNoteDto item : dto.getNursingNoteList()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getDetailedMedications() != null && dto.getDetailedMedications().size()>0) {
				for (MedicationDTO item : dto.getDetailedMedications()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getSugicalProcedureList() != null && dto.getSugicalProcedureList().size()>0) {
				for (PatientSurgicalHistoryDto item : dto.getSugicalProcedureList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getDetailedMedicationsHistory() != null && dto.getDetailedMedicationsHistory().size()>0) {
				for (MedicationHistoryDto item : dto.getDetailedMedicationsHistory()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getPatientDiagnosisListForMedicalHistory() != null && dto.getPatientDiagnosisListForMedicalHistory().size()>0) {
				for (DiagnosisPatientAppointmentMapperDto item : dto.getPatientDiagnosisListForMedicalHistory()) {
					String date = item.getCreated_date();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getTreatmentHistoryCurrentList() != null && dto.getTreatmentHistoryCurrentList().size()>0) {
				for (PatientTreatmentHistoryDetailsDto item : dto.getTreatmentHistoryCurrentList()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getTreatmentHistoryOldList() != null && dto.getTreatmentHistoryOldList().size()>0) {
				for (PatientTreatmentHistoryDetailsDto item : dto.getTreatmentHistoryOldList()) {
					String date = item.getCreatedDate1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getListCompletedMedicalProcedures() != null && dto.getListCompletedMedicalProcedures().size()>0) {
				for (PatientTreatmentHistoryDetailsDto item : dto.getListCompletedMedicalProcedures()) {
					String date = item.getTreatDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getPatientViatalList() != null && dto.getPatientViatalList().size()>0) {
				for (AppointmentVitalMapperDto item : dto.getPatientViatalList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getPatientDiagnosisList() != null && dto.getPatientDiagnosisList().size()>0) {
				for (DiagnosisPatientAppointmentMapperDto item : dto.getPatientDiagnosisList()) {
					String date = item.getCreated_date1();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getInfusionList() != null && dto.getInfusionList().size()>0) {
				for (InfusionSolutionDto item : dto.getInfusionList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getSavedInstructions() != null && dto.getSavedInstructions().size()>0) {
				for (CoversheetInstructionDto item : dto.getSavedInstructions()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getConsultOrdersList() != null && dto.getConsultOrdersList().size()>0) {
				for (ConsultDoctorDto item : dto.getConsultOrdersList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}

			if(dto.getGetSavedListOfGeneralExam() != null && dto.getGetSavedListOfGeneralExam().size()>0) {
				for (GeneralExaminationAppoMapperDto item : dto.getGetSavedListOfGeneralExam()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}
			if(dto.getNursingOrederedListByPatientId() != null && dto.getNursingOrederedListByPatientId().size()>0) {
				for (NursingOrderMapperDto item : dto.getNursingOrederedListByPatientId()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}
			if(dto.getObservationList() != null && dto.getObservationList().size()>0) {
				for (ObservationDto item : dto.getObservationList()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}
			if(dto.getGetSavedListOfSystemicExam() != null && dto.getGetSavedListOfSystemicExam().size()>0) {
				for (GeneralExaminationAppoMapperDto item : dto.getGetSavedListOfSystemicExam()) {
					String date = item.getCreatedDate();
					if (!distinctDates.contains(date)) {
						distinctDates.add(date);
					}
				}
			}
			if(distinctDates != null && distinctDates.size()>0){
				Collections.sort(distinctDates, Comparator.reverseOrder());
				resDto.setDates(distinctDates);
				return getIpdDoctorCoverSheetDataByDate(resDto);
			}
			else{
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS, null, null);
			}
//			resDto.setDates(distinctDates);




		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getIpdDoctorCoverSheetDataByDate(PrescriptionDto dto) throws ApplicationException {
		try{

			List<PrescriptionDto> resDtoList = new ArrayList<>();
			for(String dates : dto.getDates()){

				PrescriptionDto resDto = new PrescriptionDto();
				List<PatientPersonalHistoryDetailsDto> personalHistoryOldList = new ArrayList<>();
				List<PatientPersonalHistoryDetailsDto> personalHistoryCurrentList = new ArrayList<>();
				List<MedicationDTO> medicationList = new ArrayList<>();
				List<PatientFamilyHistoryDto> familyHistoryOldList = new ArrayList<>();
				List<PatientFamilyHistoryDto> familyHistoryCurrentList = new ArrayList<>();
				List<PatientSurgicalHistoryDto> surgicalHistoryOldList = new ArrayList<>();
				List<PatientSurgicalHistoryDto> surgicalHistoryCurrentList = new ArrayList<>();
				List<PatientMedicalHistoryDto> medicalHistoryOldList = new ArrayList<>();
				List<PatientMedicalHistoryDto> medicalHistoryCurrentList = new ArrayList<>();
				List<PatientTravelHistoryDetailsDto> travelHistoryOldList = new ArrayList<>();
				List<PatientTravelHistoryDetailsDto> travelHistoryCurrentList = new ArrayList<>();
				List<ImmunizationHistoryMapperDto> immunizationHistoryOldList = new ArrayList<>();
				List<ImmunizationHistoryMapperDto> immunizationHistoryCurrentList = new ArrayList<>();
				List<PatientHabitDetailsDto> patientHabitOldList = new ArrayList<>();
				List<PatientHabitDetailsDto> patientHabitList = new ArrayList<>();
				List<NursingNoteDto> nursingNoteList = new ArrayList<>();
				List<VaccineImmunizationMapperDto>vaccineMapperList = new ArrayList<>();
				List<MedicationDTO> detailedMedications = new ArrayList<>();
				List<PatientSurgicalHistoryDto> sugicalProcedureList = new ArrayList<>();
				List <MedicationHistoryDto> detailedMedicationsHistory = new ArrayList<>();
				List<DiagnosisPatientAppointmentMapperDto> patientDiagnosisListForMedicalHistory = new ArrayList<>();
				List<PatientTreatmentHistoryDetailsDto> treatmentHistoryCurrentList = new ArrayList<>();
				List<PatientTreatmentHistoryDetailsDto> treatmentHistoryOldList = new ArrayList<>();
				List<PatientTreatmentHistoryDetailsDto> listCompletedMedicalProcedures = new ArrayList<>();
				List<AppointmentVitalMapperDto> patientViatalList = new ArrayList<>();
				List<DiagnosisPatientAppointmentMapperDto> patientDiagnosisList = new ArrayList<>();
				List<InfusionSolutionDto> infusionList = new ArrayList<>();
				List<CoversheetInstructionDto>savedInstructions = new ArrayList<>();
				List<ConsultDoctorDto>consultOrdersList = new ArrayList();
				List<GeneralExaminationAppoMapperDto> savedListOfGeneralExam = new ArrayList<>();
				List<NursingOrderMapperDto> nursingOrederedListByPatientId = new ArrayList<>();
				List<ObservationDto> observationList = new ArrayList<>();
				List<GeneralExaminationAppoMapperDto> getGetSavedListOfSystemicExam = new ArrayList<>();
				String sysExamDesc = "";
				String genExamDesc = "";

				resDto.setAddedDate(dates);
				if(dto.getPersonalHistoryOldList() != null && dto.getPersonalHistoryOldList().size()>0) {
					for (PatientPersonalHistoryDetailsDto item : dto.getPersonalHistoryOldList()) {

						if (dates.equals(item.getCreatedDate1())) {
							personalHistoryOldList.add(item);
						}
					}
					resDto.setPersonalHistoryOldList(personalHistoryOldList);
				}

				if(dto.getGetSavedListOfSystemicExam() != null && dto.getGetSavedListOfSystemicExam().size()>0) {
					for (GeneralExaminationAppoMapperDto item : dto.getGetSavedListOfSystemicExam()) {

						if (dates.equals(item.getCreatedDate())) {
							sysExamDesc = item.getDescriptiveExamination();
							getGetSavedListOfSystemicExam.add(item);
						}
					}
					resDto.setSysDescriptiveExamination(sysExamDesc);
					resDto.setGetSavedListOfSystemicExam(getGetSavedListOfSystemicExam);
				}
				if(dto.getObservationList() != null && dto.getObservationList().size()>0) {
					for (ObservationDto item : dto.getObservationList()) {

						if (dates.equals(item.getCreatedDate())) {
							observationList.add(item);
						}
					}
					resDto.setObservationList(observationList);
				}
				if(dto.getNursingOrederedListByPatientId() != null && dto.getNursingOrederedListByPatientId().size()>0) {
					for (NursingOrderMapperDto item : dto.getNursingOrederedListByPatientId()) {

						if (dates.equals(item.getCreatedDate())) {
							nursingOrederedListByPatientId.add(item);
						}
					}
					resDto.setNursingOrederedListByPatientId(nursingOrederedListByPatientId);
				}

				if(dto.getPersonalHistoryCurrentList() != null && dto.getPersonalHistoryCurrentList().size()>0) {
					for (PatientPersonalHistoryDetailsDto item : dto.getPersonalHistoryCurrentList()) {

						if (dates.equals(item.getCreatedDate1())) {
							personalHistoryCurrentList.add(item);
						}
					}
					resDto.setPersonalHistoryCurrentList(personalHistoryCurrentList);
				}

				if(dto.getMedicationList() != null && dto.getMedicationList().size()>0) {
					for (MedicationDTO medication : dto.getMedicationList()) {
						String date = medication.getCreatedDate1();
						if (dates.equals(medication.getCreatedDate1())) {
							medicationList.add(medication);
						}
					}
					resDto.setMedicationList(medicationList);
				}

				if(dto.getFamilyHistoryOldList() != null && dto.getFamilyHistoryOldList().size()>0) {
					for (PatientFamilyHistoryDto item : dto.getFamilyHistoryOldList()) {

						if (dates.equals(item.getCreatedDate1())) {
							familyHistoryOldList.add(item);
						}
					}
					resDto.setFamilyHistoryOldList(familyHistoryOldList);
				}

				if(dto.getFamilyHistoryCurrentList() != null && dto.getFamilyHistoryCurrentList().size()>0) {
					for (PatientFamilyHistoryDto item : dto.getFamilyHistoryCurrentList()) {

						if (dates.equals(item.getCreatedDate1())) {
							familyHistoryCurrentList.add(item);
						}
					}
					resDto.setFamilyHistoryCurrentList(familyHistoryCurrentList);
				}

				if(dto.getSurgicalHistoryOldList() != null && dto.getSurgicalHistoryOldList().size()>0) {
					for (PatientSurgicalHistoryDto item : dto.getSurgicalHistoryOldList()) {

						if (dates.equals(item.getCreatedDate1())) {
							surgicalHistoryOldList.add(item);
						}
					}
					resDto.setSurgicalHistoryOldList(surgicalHistoryOldList);
				}

				if(dto.getSurgicalHistoryCurrentList() != null && dto.getSurgicalHistoryCurrentList().size()>0) {
					for (PatientSurgicalHistoryDto item : dto.getSurgicalHistoryCurrentList()) {

						if (dates.equals(item.getCreatedDate1())) {
							surgicalHistoryCurrentList.add(item);
						}
					}
					resDto.setSurgicalHistoryCurrentList(surgicalHistoryCurrentList);
				}

				if(dto.getMedicalHistoryOldList() != null && dto.getMedicalHistoryOldList().size()>0) {
					for (PatientMedicalHistoryDto item : dto.getMedicalHistoryOldList()) {

						if (dates.equals(item.getCreated_date1())) {
							medicalHistoryOldList.add(item);
						}
					}
					resDto.setMedicalHistoryOldList(medicalHistoryOldList);
				}

				if(dto.getMedicalHistoryCurrentList() != null && dto.getMedicalHistoryCurrentList().size()>0) {
					for (PatientMedicalHistoryDto item : dto.getMedicalHistoryCurrentList()) {

						if (dates.equals(item.getCreated_date1())) {
							medicalHistoryCurrentList.add(item);
						}
					}
					resDto.setMedicalHistoryCurrentList(medicalHistoryCurrentList);
				}

				if(dto.getTravelHistoryOldList() != null && dto.getTravelHistoryOldList().size()>0) {
					for (PatientTravelHistoryDetailsDto item : dto.getTravelHistoryOldList()) {

						if (dates.equals(item.getCreatedDate())) {
							travelHistoryOldList.add(item);
						}
					}
					resDto.setTravelHistoryOldList(travelHistoryOldList);
				}

				if(dto.getTravelHistoryCurrentList() != null && dto.getTravelHistoryCurrentList().size()>0) {
					for (PatientTravelHistoryDetailsDto item : dto.getTravelHistoryCurrentList()) {

						if (dates.equals(item.getCreatedDate())) {
							travelHistoryCurrentList.add(item);
						}
					}
					resDto.setTravelHistoryCurrentList(travelHistoryCurrentList);
				}

				if(dto.getImmunizationHistoryOldList() != null && dto.getImmunizationHistoryOldList().size()>0) {
					for (ImmunizationHistoryMapperDto item : dto.getImmunizationHistoryOldList()) {

						if (dates.equals(item.getCreatedDate())) {
							immunizationHistoryOldList.add(item);
						}
					}
					resDto.setImmunizationHistoryOldList(immunizationHistoryOldList);
				}

				if(dto.getImmunizationHistoryCurrentList() != null && dto.getImmunizationHistoryCurrentList().size()>0) {
					for (ImmunizationHistoryMapperDto item : dto.getImmunizationHistoryCurrentList()) {

						if (dates.equals(item.getCreatedDate())) {
							immunizationHistoryCurrentList.add(item);
						}
					}
					resDto.setImmunizationHistoryCurrentList(immunizationHistoryCurrentList);
				}

				if(dto.getPatientHabitOldList()!= null && dto.getPatientHabitOldList().size()>0) {
					for (PatientHabitDetailsDto item : dto.getPatientHabitOldList()) {

						if (dates.equals(item.getCreatedDate())) {
							patientHabitOldList.add(item);
						}
					}
					resDto.setPatientHabitOldList(patientHabitOldList);
				}
				if(dto.getPatientHabitList() != null && dto.getPatientHabitList().size()>0) {
					for (PatientHabitDetailsDto item : dto.getPatientHabitList()) {

						if (dates.equals(item.getCreatedDate())) {
							patientHabitList.add(item);
						}
					}
					resDto.setPatientHabitList(patientHabitList);
				}

				if(dto.getNursingNoteList() != null && dto.getNursingNoteList().size()>0) {
					for (NursingNoteDto item : dto.getNursingNoteList()) {

						if (dates.equals(item.getCreatedDate1())) {
							nursingNoteList.add(item);
						}
					}
					resDto.setNursingNoteList(nursingNoteList);
				}

				if(dto.getVaccineMapperList() != null && dto.getVaccineMapperList().size()>0) {
					for (VaccineImmunizationMapperDto item : dto.getVaccineMapperList()) {

						if (dates.equals(item.getCreatedDate())) {
							vaccineMapperList.add(item);
						}
					}
					resDto.setVaccineMapperList(vaccineMapperList);
				}

				if(dto.getDetailedMedications() != null && dto.getDetailedMedications().size()>0) {
					for (MedicationDTO item : dto.getDetailedMedications()) {

						if (dates.equals(item.getCreatedDate1())) {
							detailedMedications.add(item);
						}
					}
					resDto.setDetailedMedications(detailedMedications);
				}

				if(dto.getSugicalProcedureList() != null && dto.getSugicalProcedureList().size()>0) {
					for (PatientSurgicalHistoryDto item : dto.getSugicalProcedureList()) {

						if (dates.equals(item.getCreatedDate())) {
							sugicalProcedureList.add(item);
						}
					}
					resDto.setSugicalProcedureList(sugicalProcedureList);
				}

				if(dto.getDetailedMedicationsHistory() != null && dto.getDetailedMedicationsHistory().size()>0) {
					for (MedicationHistoryDto item : dto.getDetailedMedicationsHistory()) {

						if (dates.equals(item.getCreatedDate())) {
							detailedMedicationsHistory.add(item);
						}
					}
					resDto.setDetailedMedicationsHistory(detailedMedicationsHistory);
				}

				if(dto.getPatientDiagnosisListForMedicalHistory() != null && dto.getPatientDiagnosisListForMedicalHistory().size()>0) {
					for (DiagnosisPatientAppointmentMapperDto item : dto.getPatientDiagnosisListForMedicalHistory()) {

						if (dates.equals(item.getCreated_date())) {
							patientDiagnosisListForMedicalHistory.add(item);
						}
					}
					resDto.setDetailedMedicationsHistory(detailedMedicationsHistory);
				}

				if(dto.getTreatmentHistoryCurrentList() != null && dto.getTreatmentHistoryCurrentList().size()>0) {
					for (PatientTreatmentHistoryDetailsDto item : dto.getTreatmentHistoryCurrentList()) {

						if (dates.equals(item.getCreatedDate1())) {
							treatmentHistoryCurrentList.add(item);
						}
						
					}
					resDto.setTreatmentHistoryCurrentList(treatmentHistoryCurrentList);
				}

				if(dto.getTreatmentHistoryOldList() != null && dto.getTreatmentHistoryOldList().size()>0) {
					for (PatientTreatmentHistoryDetailsDto item : dto.getTreatmentHistoryOldList()) {

						if (dates.equals(item.getCreatedDate1())) {
							treatmentHistoryOldList.add(item);
						}
					}
					resDto.setTreatmentHistoryOldList(treatmentHistoryOldList);
				}

				if(dto.getListCompletedMedicalProcedures() != null && dto.getListCompletedMedicalProcedures().size()>0) {
					for (PatientTreatmentHistoryDetailsDto item : dto.getListCompletedMedicalProcedures()) {

						if (dates.equals(item.getTreatDate())) {
							listCompletedMedicalProcedures.add(item);
						}
					}
					resDto.setListCompletedMedicalProcedures(listCompletedMedicalProcedures);
				}

				if(dto.getPatientViatalList() != null && dto.getPatientViatalList().size()>0) {
					for (AppointmentVitalMapperDto item : dto.getPatientViatalList()) {

						if (dates.equals(item.getCreatedDate())) {
							patientViatalList.add(item);
						}
					}
					resDto.setPatientViatalList(patientViatalList);
				}

				if(dto.getPatientDiagnosisList() != null && dto.getPatientDiagnosisList().size()>0) {
					for (DiagnosisPatientAppointmentMapperDto item : dto.getPatientDiagnosisList()) {

						if (dates.equals(item.getCreated_date1())) {
							patientDiagnosisList.add(item);
						}
					}
					resDto.setPatientDiagnosisList(patientDiagnosisList);
				}

				if(dto.getInfusionList() != null && dto.getInfusionList().size()>0) {
					for (InfusionSolutionDto item : dto.getInfusionList()) {

						if (dates.equals(item.getStartDate())) {
							infusionList.add(item);
						}
					}
					resDto.setInfusionList(infusionList);
				}

				if(dto.getSavedInstructions() != null && dto.getSavedInstructions().size()>0) {
					for (CoversheetInstructionDto item : dto.getSavedInstructions()) {

						if (dates.equals(item.getCreatedDate())) {
							savedInstructions.add(item);
						}
					}
					resDto.setSavedInstructions(savedInstructions);
				}

				if(dto.getConsultOrdersList() != null && dto.getConsultOrdersList().size()>0) {
					for (ConsultDoctorDto item : dto.getConsultOrdersList()) {

						if (dates.equals(item.getCreatedDate())) {
							consultOrdersList.add(item);
						}
					}
					resDto.setConsultOrdersList(consultOrdersList);
				}

				if(dto.getGetSavedListOfGeneralExam() != null && dto.getGetSavedListOfGeneralExam().size()>0) {
					for (GeneralExaminationAppoMapperDto item : dto.getGetSavedListOfGeneralExam()) {

						if (dates.equals(item.getCreatedDate())) {
							genExamDesc = item.getDescriptiveExamination();
							savedListOfGeneralExam.add(item);
						}
					}
					resDto.setDescriptiveExamination(genExamDesc);
					resDto.setGetSavedListOfGeneralExam(savedListOfGeneralExam);
				}

				resDtoList.add(resDto);
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}catch(Exception e){
		logger.error("getIpdDoctorCoverSheetDataByDate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}