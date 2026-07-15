package com.param.otims.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.otims.dao.IOTCompletionNoteDao;
import com.param.global.otims.dto.OTCompletionNoteDto;
import com.param.global.otims.dto.OtDishDto;
import com.param.global.otims.dto.OtMealDto;
import com.param.global.otims.dto.PostOperativeCareDto;
import com.param.opd.coversheet.dao.ICoversheetInstructionDao;
import com.param.opd.coversheet.dao.IMedicationDAO;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.otims.dao.IOtDishDao;
import com.param.otims.dao.IOtMealDao;
import com.param.otims.dao.IPostOperativeCareDao;
import com.param.otims.model.OtMeal;
import com.param.otims.model.PostOperativeCare;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes","unchecked"})
public class PostOperativeCareServiceImpl  implements IPostOperativeCareService, ICommonConstants ,IBillingCommonConstants{

	@Autowired
	IPostOperativeCareDao  iPostOperativeCareDao;
	
	@Autowired
	private IMedicationDAO iMedicationDAO;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	IOTCompletionNoteDao iOTCompletionNoteDao;
	
	@Autowired
	IOtDishDao iOtDishDao;
	
	@Autowired
	IOtMealDao iOtMealDao;
	
	@Autowired
	ICoversheetInstructionDao iCoversheetInstructionDao;
	
	@Override
	public Response savePostOperativeCare(PostOperativeCareDto postOperativeCareDto) throws ApplicationException {
		try {
			Response res =  iPostOperativeCareDao.savePostOperativeCare(postOperativeCareDto);
			if(res.getObject() != null ) {
				if(postOperativeCareDto.getoTCompletionNoteDtoList()!=null && postOperativeCareDto.getoTCompletionNoteDtoList().size()>0) {
					for (OTCompletionNoteDto dto : postOperativeCareDto.getoTCompletionNoteDtoList()) {
						dto.setProReqId(postOperativeCareDto.getProReqId());
						iOTCompletionNoteDao.saveOtCompletionNote(dto);
					}
				}
				if(postOperativeCareDto.getCoversheetInstructionDto()!=null) {
				if(postOperativeCareDto.getCoversheetInstructionDto().getCoversheetInstructionDtoslist()!=null && postOperativeCareDto.getCoversheetInstructionDto().getCoversheetInstructionDtoslist().size() > 0) {
					ListIterator<CoversheetInstructionDto> iterator = postOperativeCareDto.getCoversheetInstructionDto().getCoversheetInstructionDtoslist().listIterator();
					while(iterator.hasNext())
					{
						CoversheetInstructionDto instructionDto = iterator.next();
						instructionDto.setIsOtims(true);
						iCoversheetInstructionDao.saveCoversheetInstruction(instructionDto);
					}
					}else {
						
							iCoversheetInstructionDao.saveCoversheetInstruction(postOperativeCareDto.getCoversheetInstructionDto());
					}
			}
				
				
				if(postOperativeCareDto.getOtMealList() != null && postOperativeCareDto.getOtMealList().size()>0) {
					PostOperativeCare mster  = (PostOperativeCare) res.getObject();
					for (OtMealDto mealDto : postOperativeCareDto.getOtMealList()) {
						mealDto.setPostOperativeCareId(mster.getPostOperativeCareId());
						Response mealres =  iOtMealDao.saveOtMeal(mealDto);
						if(mealres.getObject()!= null ) {
							OtMeal  meal = (OtMeal)mealres.getObject();
							if(mealDto.getOtDishList()!= null && mealDto.getOtDishList().size()>0) {
								for (OtDishDto dishdto : mealDto.getOtDishList()) {
									dishdto.setOtMealId(meal.getOtMealId());
									iOtDishDao.saveOtDish(dishdto);
								}
							}
						}
					}
				}
				
			if(postOperativeCareDto.getPrescriptionDto()!=null) {
				PrescriptionDto prescriptionDto = postOperativeCareDto.getPrescriptionDto();
				
				prescriptionDto.setIsIntervention(FALSE);
				prescriptionDto.setIsActive(TRUE);
				prescriptionDto.setIsDeleted(FALSE);
				prescriptionDto.setIsValidate(FALSE);
				prescriptionDto.setIsDischarge(FALSE);
				prescriptionDto.setStatusId(1);
				prescriptionDto.setIsOtims(TRUE);
				prescriptionDto.setUpdatedBy(prescriptionDto.getAddedBy());
				prescriptionDto.setUpdatedDate(prescriptionDto.getAddedDate());
				prescriptionDto.setIsMedicationStop('N');	
				String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, prescriptionDto.getUnitId(), PRES_NO);
				
				int count=0;
				if(prescriptionDto.getPrescriptionDetailsDtosList().size()>0) {
				for (PrescriptionDetailsDto dto1 : prescriptionDto.getPrescriptionDetailsDtosList()) {
					if(dto1.getIsNonFormulary()=='Y') {
						count++;
					}
				}
				
				}
				if(count==0) {
					prescriptionDto.setIsNonFormulary('N');
				}else if(prescriptionDto.getPrescriptionDetailsDtosList()!=null && prescriptionDto.getPrescriptionDetailsDtosList().size()==count) {
					prescriptionDto.setIsNonFormulary('Y');
				}else {
					prescriptionDto.setIsNonFormulary('N');
				}
				prescriptionDto.setPrescriptionNo(prescriptionNo);
			Response presRes = iMedicationDAO.savePrescription(prescriptionDto);
			prescriptionDto = (PrescriptionDto) presRes.getObject();
			ListIterator<PrescriptionDetailsDto> iterator = prescriptionDto.getPrescriptionDetailsDtosList().listIterator();
			while(iterator.hasNext())
			{
				PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
					prescriptionDetailsDto.setOrganizationId(prescriptionDto.getOrganizationId());
					prescriptionDetailsDto.setUnitId(prescriptionDto.getUnitId());
					prescriptionDetailsDto.setPrescriptionId(prescriptionDto.getPrescriptionId());
					prescriptionDetailsDto.setIsDiscontinued(FALSE);
					prescriptionDetailsDto.setIsMedicationStop('N');
					prescriptionDetailsDto.setIsPreStop(FALSE);
					prescriptionDetailsDto.setIsIntervention(FALSE);
					prescriptionDetailsDto.setAddedBy(prescriptionDto.getAddedBy());
					prescriptionDetailsDto.setAddedDate(prescriptionDto.getAddedDate());
					prescriptionDetailsDto.setIsDeleted(FALSE);
					prescriptionDetailsDto.setIsActive(TRUE);
					prescriptionDetailsDto.setStatusId(1);
					prescriptionDetailsDto.setPrnflag(FALSE);
					prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
					prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
					prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
					prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
					prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
					prescriptionDetailsDto.setInfusionflag(FALSE);
					prescriptionDetailsDto.setUpdatedBy(prescriptionDetailsDto.getAddedBy());
					prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
					prescriptionDetailsDto.setUpdatedDate(prescriptionDetailsDto.getAddedDate());
					if(prescriptionDetailsDto.getIsDischarge()!=null){
						prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
					}else{
						prescriptionDetailsDto.setIsDischarge(false);
					}
					
				Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);	
					
				prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();
				
				iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
				}
			}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, res.getObject());
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getNotesListByProConfigId(Integer orgId, Integer unitId, Integer proReqId)
			throws ApplicationException {
		try {
			Response res = iPostOperativeCareDao.getPostOperativeCare(proReqId);
			
			PostOperativeCareDto dto =  new PostOperativeCareDto();
			
			if(res.getObject() != null ) {
				dto =(PostOperativeCareDto) res.getObject();
				Response notes =  iOTCompletionNoteDao.getOtNoteList(proReqId);
				if(notes.getListObject() != null && notes.getListObject().size()>0) {
					dto.setoTCompletionNoteDtoList(notes.getListObject());
				}
				Response mealres =  iOtMealDao.getOtMeals(dto.getPostOperativeCareId());
				 List<OtMealDto> otMealList = new ArrayList<OtMealDto>();  
				if(mealres.getListObject()!= null && mealres.getListObject().size()>0) {
					
					 otMealList.addAll(mealres.getListObject());
					 
					 for (OtMealDto otMealDto : otMealList) {
						 Response dishRes = iOtDishDao.getOtDishData(otMealDto.getOtMealId());
						 String dishess = "";
						 if(dishRes.getListObject()!= null && dishRes.getListObject().size()>0) {
							 otMealDto.setOtDishList(dishRes.getListObject());
							 List<OtDishDto> disList = new ArrayList<>();
							 disList.addAll(dishRes.getListObject());
							 int i = 0 ;
							
							 for (OtDishDto otDishDto : disList) {
								if(i == 0 ) {
									dishess = otDishDto.getDishDescription();
									i = 1;
								}else {
									dishess = dishess +", "+otDishDto.getDishDescription();
								}
							 }
						 }
						 otMealDto.setSelectedDishes(dishess);
					}
				}
				dto.setOtMealList(otMealList);
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
				
			}else {
				return iPostOperativeCareDao.getNotesListByProConfigId( orgId,  unitId,  proReqId);
			}
			
		} catch (Exception e) {
			
			throw e;
		}
	}

}
