package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorDocumentMapperDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.model.DoctorDocumentMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class DoctorDocumentMapperDaoImpl extends GenericDao<DoctorDocumentMapper, Integer> implements ICommonConstants, IDoctorDocumentMapperDao {
	final static Logger logger = Logger.getLogger(DoctorDocumentMapperDaoImpl.class);

	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	public DoctorDocumentMapperDaoImpl() {
		super(DoctorDocumentMapper.class);
	}


@Override
public Response saveDoctorQualificationDetails(DoctorDocumentMapperDto dto) throws ApplicationException {
					try {
						DoctorDocumentMapper doctorDocumentMapper=new DoctorDocumentMapper();
						doctorDocumentMapper.setDegreeId(dto.getDegreeId());
						doctorDocumentMapper.setQualificationId(dto.getQualificationId());
						doctorDocumentMapper.setQualificationDesc(dto.getQualificationDesc());
						doctorDocumentMapper.setDoctorId(dto.getDoctorId());
						doctorDocumentMapper.setRegistrationNumber(dto.getRegistrationNumber());
						doctorDocumentMapper.setFile(dto.getFile()!=null? dto.getFile():null);
						doctorDocumentMapper.setFileName(dto.getFileName()!=null ? dto.getFileName() : null);
						doctorDocumentMapper.setDocumentFilePath(dto.getDocumentFilePath()!=null?dto.getDocumentFilePath():null);
						doctorDocumentMapper.setCreatedBy(dto.getCreatedBy());
						doctorDocumentMapper.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
						doctorDocumentMapper.setUpdatedBy(dto.getUpdatedBy());
						doctorDocumentMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
						doctorDocumentMapper.setStatus('A');
						doctorDocumentMapper.setOrganizationId(dto.getOrganizationId());
						doctorDocumentMapper.setUnitId(dto.getUnitId());
						save(doctorDocumentMapper);
					
						return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				} catch (Exception e) {
				logger.error("saveDoctorQualificationDetails failed", e);
				}
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
				}

@Override
public Response getListOfDoctorDegreeDoccument(DoctorDocumentMapperDto doctorDocumentMapperDto) throws ApplicationException {
	try{
		List<DoctorMasterDto> doctorMasterDtosList = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LIST_OF_DOCTOR_DEGREE_DOCCUEMENT")
				.setParameter("organizationId", doctorDocumentMapperDto.getOrganizationId())
				.setParameter("unitId", doctorDocumentMapperDto.getUnitId())
				.setParameter("doctorId", doctorDocumentMapperDto.getDoctorId())
				.setResultTransformer(Transformers.aliasToBean(DoctorDocumentMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, doctorMasterDtosList, null);
	} catch (Exception e) {
	logger.error("getListOfDoctorDegreeDoccument failed", e);
	}
	return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
}


@Override
public Response updateDoctorEqucationDetails(DoctorDocumentMapperDto dto) throws ApplicationException {
	try {

		String query = "UPDATE doctor.t_doctor_doc_mapper " +
				"SET status = :status " +
				"WHERE doctor_doc_id = :doctorDocId " +
				"AND organization_id = :organizationId " +
				"AND unit_id = :unitId";

		int rowsUpdated = sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.setParameter("status", "I")  // Parameter for the status value
				.setParameter("doctorDocId", dto.getDoctorDocId())
				.setParameter("organizationId", dto.getOrganizationId())
				.setParameter("unitId", dto.getUnitId())
				.executeUpdate();


		return new Response(SUCCESS, SUCCESS_CODE, "Deleted Successfully", null, null);
} catch (Exception e) {
		logger.error("updateDoctorEqucationDetails failed", e);
		}
return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);}

}
