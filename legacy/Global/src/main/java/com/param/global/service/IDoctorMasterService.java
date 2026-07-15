package com.param.global.service;

import javax.transaction.Transactional;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.global.common.Response;
import com.param.global.dto.DoctorDocumentMapperDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import com.param.global.unit.dto.UserIndetificationDetailsDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.opd.appointment.dto.BookAppointmentDto;

import in.param.exception.ApplicationException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("rawtypes")
public interface IDoctorMasterService {

	@Transactional
	public Response getDoctorMasterList(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response saveDoctorRegistration(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response getDoctorDetails(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response updateDoctorDetails(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response updateDoctorStatus(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response searchDoctorDetailsByCriteria(DoctorMasterDto doctorMasterDto)throws ApplicationException;
	
	@Transactional
	public Response saveDoctorImage(int orgId,int unitId,int doctorId, MultipartFile inputFile)throws ApplicationException;

	@Transactional
	public Response searchPlainDoctorDetailsByCriteria(DoctorMasterDto doctorMasterDto)throws ApplicationException;
	
	@Transactional
	public Response getDoctorNameById(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response getActiveDocList(DoctorMasterDto doctorMasterDto)throws ApplicationException;
	
	
	@Transactional
	public Response saveDoctorDocuments(Integer degreeId, Integer qualificationId, String qualificationDesc,
			String registrationNumber, Integer createdBy, String createdDate, 
			String updatedDate, Integer updatedBy, Integer unitId, Integer organizationId, Integer doctorId,
			String fileName, MultipartFile file);
	
	@Transactional
	public Response getListOfDoctorDegreeDoccument(DoctorDocumentMapperDto doctorDocumentMapperDto)throws ApplicationException;

	@Transactional
	public Response getRMODoctorList(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response validateDoctorPin(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response validateDoctorPinByDoctorId(DoctorMasterDto doctorMasterDto)throws ApplicationException;
	
	@Transactional
	public Response getDocotorListForAppoitments(Integer unitId,Integer specialityId);

	public Response getDoctorCount(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response getStaffListByRoleId(UserRoleMasterDto dto) throws ApplicationException;

	@Transactional
	public Response doctorDetailsUniversalSearch(DoctorMasterDto doctorMasterDto) throws ApplicationException;
	
	@Transactional
	public Response getPlainDocList(DoctorMasterDto doctorMasterDto);
	
	@Transactional
	public Response getDoctorListBySpecialityId(DoctorSpecialityMapperDto doctorSpecialityMapper);

	@Transactional
	public Response storeDoctorEqucationDetails(DoctorMasterDto doctorMasterDto) throws ApplicationException;
	
	@Transactional
	public Response getActiveDocListWithSpeciality(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response checkDoctorRegNoAvailability(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Integer getGlobalDocId(Integer doctorId)throws ApplicationException;

	@Transactional
	public Response updateGlobalDocId(Integer doctorId, Integer globalDocId);

	@Transactional
	public Response getDoctorListForSwapDoctor(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
	public Response updateDoctorEqucationDetails(DoctorDocumentMapperDto doctorDocumentMapperDto)throws ApplicationException;

	@Transactional
	public Response getDoctorsForWebAppointment(Integer unitId, Integer specialityId,Boolean flag)throws ApplicationException;

	@Transactional
	public Response getDetailsForBookAppointment(BookAppointmentDto bookAppointmentDto)throws ApplicationException;

	@Transactional
	public Response getDoctorRegistrationNumber(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	public Response getDoctorMasterList1(DoctorMasterDto doctorMasterDto);

	@Transactional
	public Response doctorIdentificationDocumets(Integer identificationTypeId, String identificationNumber, Integer employeeId, Integer userId,
			String datepickerExpiry, Integer createdBy, String createdDate, String updatedDate, Integer updatedBy,
			Integer unitId, Integer organizationId, Integer doctorId, String fileName, String retrieveFileName, String doctorIndetificationFile, Integer userIndetificationDetailsId, MultipartFile file) throws ServerException,  InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
	@Transactional
	public Response getListOfDoctorIdentificationDocuumentList(UserIndetificationDetailsDto userIndetificationDetailsDto)throws ApplicationException;

	@Transactional
	public Response updateDoctorIdentificationDetails(UserIndetificationDetailsDto userIndetificationDetailsDto)throws ApplicationException;
	
	@Transactional
	public Response getDoctorListByOrgId(DoctorSpecialityMapperDto doctorSpecialityMapper)throws ApplicationException;

	@Transactional
	public Response doctorRegistrationWithGLId(GeneralLedgerMasterDto generalLedgerMasterDto)throws ApplicationException;

	@Transactional
	public Response updateDoctorMasterLedgerId(DoctorMasterDto doctorMasterDto)throws ApplicationException;

	@Transactional
    Response getDoctorDetailsEncrypted(DoctorMasterDto doctorMasterDto);

    @Transactional
    public Response getDoctorListForAppointmentsWebUser(Integer unitId, Integer specialityId);

	@Transactional
	public Response getAvailableDoctorBySpeciality(DoctorSpecialityMapperDto doctorSpecialityMapperDto) throws ApplicationException;
}
