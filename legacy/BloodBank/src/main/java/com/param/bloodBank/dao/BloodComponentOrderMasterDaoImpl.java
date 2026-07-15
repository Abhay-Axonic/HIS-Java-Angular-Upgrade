package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodComponentOrderMasterDto;
import com.param.bloodBank.dto.PatientRequisitionDetailsDto;
import com.param.bloodBank.model.BloodComponentOrderMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodComponentOrderMasterDaoImpl extends GenericDao<BloodComponentOrderMaster, Integer> implements IBloodComponentOrderMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentOrderMasterDaoImpl.class);


	public BloodComponentOrderMasterDaoImpl() {
		super(BloodComponentOrderMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;
		
	@Override
	public Response saveBloodComponentOrderMaster(List<BloodComponentOrderMasterDto> bloodComponentOrderMasterDtoList)
			throws ApplicationException {
		try {
			for (BloodComponentOrderMasterDto  bloodComponentOrderMasterList : bloodComponentOrderMasterDtoList) {
				BloodComponentOrderMaster bloodComponentOrderMaster = new BloodComponentOrderMaster();
			bloodComponentOrderMaster.setBloodComponentId(bloodComponentOrderMasterList.getBloodComponentId());
			bloodComponentOrderMaster.setProcedureOrderId(bloodComponentOrderMasterList.getProcedureOrderId());
			bloodComponentOrderMaster.setPatientId(bloodComponentOrderMasterList.getPatientId());
			bloodComponentOrderMaster.setPriority(bloodComponentOrderMasterList.getPriority());
			bloodComponentOrderMaster.setAdmissionId(bloodComponentOrderMasterList.getAdmissionId());
			bloodComponentOrderMaster.setDoctorId(bloodComponentOrderMasterList.getDoctorId());
			bloodComponentOrderMaster.setComments(bloodComponentOrderMasterList.getComments());
			bloodComponentOrderMaster.setInstruction(bloodComponentOrderMasterList.getInstruction());
			bloodComponentOrderMaster.setUnit(bloodComponentOrderMasterList.getUnit());
			bloodComponentOrderMaster.setRequisitionDate(GlobalCommonDateUtils.getDate(bloodComponentOrderMasterList.getRequisitionDate(), "dd-M-yyyy HH:mm:ss"));
			bloodComponentOrderMaster.setRequisitionOnDate(GlobalCommonDateUtils.getDate(bloodComponentOrderMasterList.getRequisitionOnDate(), "dd-M-yyyy HH:mm:ss"));
			bloodComponentOrderMaster.setOrganizationId(bloodComponentOrderMasterList.getOrganizationId());
			bloodComponentOrderMaster.setUnitId(bloodComponentOrderMasterList.getUnitId());
			bloodComponentOrderMaster.setStatus(bloodComponentOrderMasterList.getStatus());
			bloodComponentOrderMaster.setIsExported(bloodComponentOrderMasterList.getIsExported());
			bloodComponentOrderMaster.setIsRequisitionClosed(bloodComponentOrderMasterList.getIsRequisitionClosed());
			bloodComponentOrderMaster.setIsRequired(bloodComponentOrderMasterList.getIsRequired());
			bloodComponentOrderMaster.setRequisitionNumber(bloodComponentOrderMasterList.getRequisitionNumber());
			bloodComponentOrderMaster.setCreatedBy(bloodComponentOrderMasterList.getCreatedBy());
			bloodComponentOrderMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodComponentOrderMasterList.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodComponentOrderMaster.setUpdatedBy(bloodComponentOrderMasterList.getUpdatedBy());
			bloodComponentOrderMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodComponentOrderMasterList.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(bloodComponentOrderMaster);
			}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodComponentOrderMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodComponentOrderMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	
	
	@Override
	public Response updateBloodComponentOrderMaster(List<BloodComponentOrderMasterDto> bloodComponentOrderMasterDtoList)
			throws ApplicationException {
		try {
			for (BloodComponentOrderMasterDto  bloodComponentOrderMasterList : bloodComponentOrderMasterDtoList) {
				BloodComponentOrderMaster bloodComponentOrderMaster = findById(bloodComponentOrderMasterList.getBloodComponentOrderId());

			bloodComponentOrderMaster.setBloodComponentId(bloodComponentOrderMasterList.getBloodComponentId());
			bloodComponentOrderMaster.setProcedureOrderId(bloodComponentOrderMasterList.getProcedureOrderId());
			bloodComponentOrderMaster.setPatientId(bloodComponentOrderMasterList.getPatientId());
			bloodComponentOrderMaster.setPriority(bloodComponentOrderMasterList.getPriority());
			bloodComponentOrderMaster.setComments(bloodComponentOrderMasterList.getComments());
			bloodComponentOrderMaster.setAdmissionId(bloodComponentOrderMasterList.getAdmissionId());
			bloodComponentOrderMaster.setDoctorId(bloodComponentOrderMasterList.getDoctorId());
			bloodComponentOrderMaster.setInstruction(bloodComponentOrderMasterList.getInstruction());
			bloodComponentOrderMaster.setUnit(bloodComponentOrderMasterList.getUnit());
			bloodComponentOrderMaster.setRequisitionDate(GlobalCommonDateUtils.getDate(bloodComponentOrderMasterList.getRequisitionDate(), "dd-M-yyyy HH:ss:mm"));
			bloodComponentOrderMaster.setRequisitionOnDate(GlobalCommonDateUtils.getDate(bloodComponentOrderMasterList.getRequisitionOnDate(), "dd-M-yyyy HH:ss:mm"));
			bloodComponentOrderMaster.setOrganizationId(bloodComponentOrderMasterList.getOrganizationId());
			bloodComponentOrderMaster.setUnitId(bloodComponentOrderMasterList.getUnitId());
			bloodComponentOrderMaster.setStatus(bloodComponentOrderMasterList.getStatus());
			bloodComponentOrderMaster.setIsExported(bloodComponentOrderMasterList.getIsExported());
			bloodComponentOrderMaster.setIsRequisitionClosed(bloodComponentOrderMasterList.getIsRequisitionClosed());
			bloodComponentOrderMaster.setIsRequired(bloodComponentOrderMasterList.getIsRequired());
			bloodComponentOrderMaster.setRequisitionNumber(bloodComponentOrderMasterList.getRequisitionNumber());
			bloodComponentOrderMaster.setCreatedBy(bloodComponentOrderMasterList.getCreatedBy());
			bloodComponentOrderMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodComponentOrderMasterList.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodComponentOrderMaster.setUpdatedBy(bloodComponentOrderMasterList.getUpdatedBy());
			bloodComponentOrderMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodComponentOrderMasterList.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			update(bloodComponentOrderMaster);
			}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
				logger.error("updateBloodComponentOrderMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodComponentOrderMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getBloodComponentOrderMasterList(BloodComponentOrderMasterDto bloodComponentOrderMasterDto)
			throws ApplicationException {
		try {
			List<BloodComponentOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_COMPONENT_ORDER_MASTER_LIST")
					.setParameter("organizationId", bloodComponentOrderMasterDto.getOrganizationId())
					.setParameter("unitId", bloodComponentOrderMasterDto.getUnitId())
					.setParameter("patientId", bloodComponentOrderMasterDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(BloodComponentOrderMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodComponentOrderMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodComponentOrderMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodComponentOrderMasterById(Integer bloodComponentOrderId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodComponentOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_COMPONENT_ORDER_MASTER_BY_ID")
					.setParameter("bloodComponentOrderId", bloodComponentOrderId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodComponentOrderMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodComponentOrderMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodComponentOrderMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodComponentOrderMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodComponentOrderMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_COMPONENT_ORDER_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(BloodComponentOrderMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodComponentOrderMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodComponentOrderMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getListOfProcedureOrderByPatientId(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodComponentOrderMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_PROCEDURE_BY_PATIENT_ID")
					.setParameter("patientId", patientId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodComponentOrderMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getListOfProcedureOrderByPatientId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getListOfProcedureOrderByPatientId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response searchOrderedBloodComponentByDate(BloodComponentOrderMasterDto bloodComponentOrderMasterDto)
			throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_ORDERED_BLOOD_COMPONENT").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(bloodComponentOrderMasterDto.getFromDate()!=null && bloodComponentOrderMasterDto.getToDate()!=null && bloodComponentOrderMasterDto.getFromDate()!="" && bloodComponentOrderMasterDto.getToDate()!=""){
				subQuery.append(" AND to_char(bloodComponentOrderMaster.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			//	subQuery.append(" GROUP BY bloodComponentOrderMaster.patientId,  ");
				//subQuery.append(" order by debloodComponentOrderMaster.bloodComponentOrderId  ");
			}
			
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery
					.setParameter("organizationId", bloodComponentOrderMasterDto.getOrganizationId())
					.setParameter("unitId", bloodComponentOrderMasterDto.getUnitId());

			if(bloodComponentOrderMasterDto.getFromDate()!=null && bloodComponentOrderMasterDto.getToDate()!=null && bloodComponentOrderMasterDto.getFromDate()!="" && bloodComponentOrderMasterDto.getToDate()!=""){
				actualQuery.setParameter("fromDate",bloodComponentOrderMasterDto.getFromDate());
				actualQuery.setParameter("toDate",bloodComponentOrderMasterDto.getToDate());
			}

			List<BloodComponentOrderMasterDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(BloodComponentOrderMasterDto.class)).list();
			//.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
			
			//criteria.setProjection(Projections.distinct(Projections.property("id")));
				
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("searchOrderedBloodComponentByDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	public Response closePatientRequisition(PatientRequisitionDetailsDto dto) throws ApplicationException {
		try {
			BloodComponentOrderMaster mstr = findById(dto.getBloodComponentOrderId());
			
			if(mstr!=null)
			{
				mstr.setUpdatedBy(dto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setIsRequisitionClosed(dto.getIsRequisitionClosed());
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			} catch (HibernateException he) {
				logger.error("closePatientRequisition failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("closePatientRequisition failed", e);
				throw new ServiceException(e.getMessage());
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}



	@Override
	public Response updateIsExportedStatus(BloodComponentOrderMasterDto dto) throws ApplicationException {
		try {
			BloodComponentOrderMaster mstr = findById(dto.getBloodComponentOrderId());
			
			if(mstr!=null)
			{
				mstr.setUpdatedBy(dto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setIsExported(dto.getIsExported());
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			} catch (HibernateException he) {
				logger.error("updateIsExportedStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateIsExportedStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
