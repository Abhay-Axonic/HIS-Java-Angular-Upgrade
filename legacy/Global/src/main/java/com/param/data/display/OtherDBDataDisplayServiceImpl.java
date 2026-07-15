package com.param.data.display;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.data.dto.OtherAppDataReqDto;
import com.param.data.dto.QuickSummeryDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OtherDBDataDisplayServiceImpl  implements IOtherDBDataDisplayService, ICommonConstants,IBillingCommonConstants{

	@Autowired
	private IOtherDBDataDisplayDao iOtherDBDataDisplayDao;
	
	@Override
	@Transactional
	public Response encounterList(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.encounterList(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	
	@Override
	@Transactional
	public Response quickSummery(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			QuickSummeryDto dto =new QuickSummeryDto();
			dto.setDiagnosis(iOtherDBDataDisplayDao.getDiagnosysList(reqDto).getListObject());
			dto.setTriage(iOtherDBDataDisplayDao.getTriageList(reqDto).getListObject());
			dto.setHistory(iOtherDBDataDisplayDao.getHistory(reqDto).getListObject());
			dto.setPhysicalExamination(iOtherDBDataDisplayDao.getPhysicalExamination(reqDto).getListObject());
			dto.setPrescriptions(iOtherDBDataDisplayDao.getPrecriptionList(reqDto).getListObject());
			dto.setLoboratory(iOtherDBDataDisplayDao.getLabOrderedsList(reqDto).getListObject());
			dto.setDiagnosis(iOtherDBDataDisplayDao.getDiagnosysList(reqDto).getListObject());
			dto.setProcedure(iOtherDBDataDisplayDao.getProceduresList(reqDto).getListObject());
			dto.setReviewNotes(iOtherDBDataDisplayDao.getreviewNotesList(reqDto).getListObject());
			dto.setRadiologyorders(iOtherDBDataDisplayDao.getRadiologyOrdersList(reqDto).getListObject());
			dto.setProvisionalDiagnosys(iOtherDBDataDisplayDao.getProvisionalDiagonsys(reqDto).getListObject());;
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getTriageList(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getTriageList(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getHistory(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getHistory(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPhysicalExamination(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getPhysicalExamination(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPrecriptionList(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getPrecriptionList(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getLabOrderedsList(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getLabOrderedsList(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getDiagnosysList(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getDiagnosysList(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getRadiologyOrdersList(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getRadiologyOrdersList(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getProceduresList(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getProceduresList(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getreviewNotesList(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getreviewNotesList(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getProvisionalDiagonsys(OtherAppDataReqDto reqDto) throws BillingException {
		try{
			return iOtherDBDataDisplayDao.getProvisionalDiagonsys(reqDto);
		}catch(BillingException be){
			throw be;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

}
