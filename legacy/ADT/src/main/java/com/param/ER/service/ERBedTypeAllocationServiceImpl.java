package com.param.ER.service;

import com.param.adt.admission.dao.ISpecialityTriageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.ER.dao.IERBedTypeAllocationDao;
import com.param.ER.dto.ERBedTypeAllocationDto;
import com.param.adt.admission.model.Admission;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.transfer.dto.TransferDto;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.UnknownPatientRegistrationDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ERBedTypeAllocationServiceImpl implements IERBedTypeAllocationService, ICommonConstants {

	@Autowired
	IERBedTypeAllocationDao iBedTypeAllocationDao;

	@Autowired
	ISpecialityTriageDao iSpecialityTriageDao;

	@Override
	public Response getUnknownPatientsList(UnknownPatientRegistrationDto unknownPatientRegistrationDto)
			throws ApplicationException {
		try {

			return iBedTypeAllocationDao.getUnknownPatientsList(unknownPatientRegistrationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getERBedTypeAllocationList(ERBedTypeAllocationDto erBedTypeAllocationDto) {
		try {

			return iBedTypeAllocationDao.getERBedTypeAllocationList(erBedTypeAllocationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getERBedTypeAllocationCount(ERBedTypeAllocationDto erBedTypeAllocationDto)
			throws ApplicationException {
		try {

			return iBedTypeAllocationDao.getCount(erBedTypeAllocationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getERBedList(ERBedTypeAllocationDto erBedTypeAllocationDto) throws ApplicationException {
		try {

			return iBedTypeAllocationDao.getERBedList(erBedTypeAllocationDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateERPatientAdmisson(AdmissionDto admissionDto) {
		try {

			Response res = iBedTypeAllocationDao.updateERPatientAdmission(admissionDto);
			Admission admission = (Admission) res.getObject();
			
			admissionDto.setActiveStatus('B');	//B----> Booked
			admissionDto.setBedStatusId(5); // 5----> Booked
		
			if(res.getStatus().equals(SUCCESS))
			{
				Response res3= iBedTypeAllocationDao.updateERPatientDetails(admissionDto);
				if(res3.getStatus().equals(SUCCESS)&&admissionDto.getErBedTypeAllocation()!=null)
				{
					if(admissionDto.getErBedTypeAllocation()==1)
					{
						TransferDto transferDto = new TransferDto();
							transferDto.setOrganizationId(admissionDto.getOrganizationId());
							transferDto.setUnitId(admissionDto.getUnitId());
							transferDto.setAdmissionId(admissionDto.getAdmissionId());
							transferDto.setFromBedCategoryId(admissionDto.getBedCategoryId());
							transferDto.setFromBillingBedCategoryId(admissionDto.getBillingBedCategoryId());
							transferDto.setFromWardId(admissionDto.getWardId());
							transferDto.setFromRoomId(admissionDto.getRoomId());
							transferDto.setFromBedId(admissionDto.getBedId());
							transferDto.setFromDate(admissionDto.getDoa());
							transferDto.setFromTime(admissionDto.getDoa());
							transferDto.setCreatedBy(admissionDto.getCreatedBy());
							transferDto.setCreatedDate(admissionDto.getDoa());
							transferDto.setUpdatedBy(admissionDto.getUpdatedBy());
							transferDto.setUpdatedDate(admissionDto.getDoa());
							transferDto.setStatus('A');
							transferDto.setTransferStatusId(1);
						return iBedTypeAllocationDao.saveTransfer(transferDto);
					}
				}
				{
					return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
				}
			}
			
			return new Response(SUCCESS, null, null, null, admission);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateERsavePatientDetails(AdmissionDto admissionDto) {
		try {
			
			return iBedTypeAllocationDao.updateERPatientDetails(admissionDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveDoctorList(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			return iBedTypeAllocationDao.getActiveDoctorList(doctorMasterDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveTransfer(AdmissionDto unAdmissionDto) throws ApplicationException {
		try {

			

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getDoctorListForOPDBilling(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {

			return iBedTypeAllocationDao.getDoctorListForOPDBilling(doctorMasterDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorShareWiseDocList(DoctorMasterDto doctorMasterDto) throws ApplicationException {
		try {
			Response res1 =iBedTypeAllocationDao.getDoctorShareWiseDocList(doctorMasterDto);
			if(res1.getListObject()!= null && res1.getListObject().size() > 0) {
				return new Response(SUCCESS, null, null, res1.getListObject(), null);

			}else {
				return iBedTypeAllocationDao.getDoctorListForOPDBilling(doctorMasterDto);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveTriageDetails(AdmissionDto admissionDto) throws ApplicationException {
		return iSpecialityTriageDao.saveTriageDetails(admissionDto);
	}
}
