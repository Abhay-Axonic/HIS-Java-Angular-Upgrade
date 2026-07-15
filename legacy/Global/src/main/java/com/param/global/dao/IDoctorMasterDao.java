package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.model.DoctorMaster;
import com.param.global.unit.dto.UserRoleMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorMasterDao extends IGenericDao<DoctorMaster, Integer>{

	public Response getDoctorMasterList(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response checkUniqueDoctor(DoctorMasterDto doctorMasterDto) throws ApplicationException;

	public Response saveDoctorRegistration(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response saveDoctorDetails(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getDoctorDetails(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response updateDoctorDetails(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response updateDoctorStatus(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response saveDoctorSpecialityMapper(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response searchDoctorDetailsByCriteria(DoctorMasterDto doctorMasterDto)throws ApplicationException;
	
	public Response getDoctorForSyncById(Integer docId) throws ApplicationException;
	
	public Response updateDoctorImage(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response updateDoctorSpecialityMapper(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getDoctorNameById(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getActiveDocList(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getRMODoctorList(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response validateDoctorPin(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response validateDoctorPinByDoctorId(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getDocotorListForAppoitments(Integer unitId, Integer specialityId)throws ApplicationException;

	public Response getDoctorCount(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getStaffListByRoleId(UserRoleMasterDto dto) throws ApplicationException;

	public Response doctorDetailsUniversalSearch(DoctorMasterDto doctorMasterDto) throws ApplicationException;

	public Response inactiveDoctorSpecialityMapper(Integer doctorId);

	public Response getPlainDocList(DoctorMasterDto doctorMasterDto);

	public Response saveDoctorSubSpecialityMapper(DoctorMasterDto doctorMasterDto);

	public Response getDoctorListBySpecialityId(DoctorSpecialityMapperDto doctorSpecialityMapper);

	public Response getActiveDocListWithSpeciality(DoctorMasterDto doctorMasterDto) throws ApplicationException;

	public Response updateGlobalDocId(Integer doctorId, int globalDocId);

	Integer getGlobalDocId(Integer doctorId);

	public Response checkDoctorRegNoAvailability(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getDoctorListForSwapDoctor(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getDoctorsForWebAppointment(Integer unitId, Integer specialityId, Boolean flag)throws ApplicationException;

	public Response getDoctorRegistrationNumber(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getDoctorMasterList1(DoctorMasterDto doctorMasterDto);

	public Response getDoctorListByOrgId(DoctorSpecialityMapperDto doctorSpecialityMapper)throws ApplicationException;

	public Response updateDoctorMasterLedgerId(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getDoctorDetailsEncrypted(DoctorMasterDto doctorMasterDto);

    public Response getDoctorListForAppointmentsWebUser(Integer unitId, Integer specialityId);

	public Response getAvailableDoctorBySpeciality(DoctorSpecialityMapperDto doctorSpecialityMapperDto);
}
