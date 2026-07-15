package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.IpdDietOrderDto;
import com.param.opd.coversheet.dto.IpdRylesTubeDto;
import com.param.opd.coversheet.model.IpdRylesTubeMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class IpdRylesTubeDaoImpl extends GenericDao<IpdRylesTubeMaster, Integer> implements IIpdRylesTubeDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(IpdRylesTubeDaoImpl.class);


public IpdRylesTubeDaoImpl() {
	super(IpdRylesTubeMaster.class);
}

@Autowired
SessionFactory sessionFactory;

@Override
public Response saveIpdRylesTubeOrder(List<IpdRylesTubeDto> ipdRylesTubeOrderDtoList) throws ApplicationException {

	try {
		for (IpdRylesTubeDto  ipdRylesTubeOrderList : ipdRylesTubeOrderDtoList) {
			IpdRylesTubeMaster ipdRylesTubeMaster = new IpdRylesTubeMaster();
			ipdRylesTubeMaster.setTubeFeedingProductId(ipdRylesTubeOrderList.getTubeFeedingProductId());
			ipdRylesTubeMaster.setAdmissionId(ipdRylesTubeOrderList.getAdmissionId());
			ipdRylesTubeMaster.setDoctorId(ipdRylesTubeOrderList.getDoctorId());
			ipdRylesTubeMaster.setQuantity(ipdRylesTubeOrderList.getQuantity());
			ipdRylesTubeMaster.setPatientId(ipdRylesTubeOrderList.getPatientId());
			ipdRylesTubeMaster.setFrequencyId(ipdRylesTubeOrderList.getFrequencyId());
			ipdRylesTubeMaster.setIpdRylesTubeId(ipdRylesTubeOrderList.getIpdRylesTubeId());
			ipdRylesTubeMaster.setDoctorId(ipdRylesTubeOrderList.getDoctorId());
			ipdRylesTubeMaster.setRylesTubeInstruction(ipdRylesTubeOrderList.getRylesTubeInstruction());
			ipdRylesTubeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(ipdRylesTubeOrderList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			ipdRylesTubeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(ipdRylesTubeOrderList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			ipdRylesTubeMaster.setCreatedBy(ipdRylesTubeOrderList.getCreatedBy());
			ipdRylesTubeMaster.setUpdatedBy(ipdRylesTubeOrderList.getUpdatedBy());
			ipdRylesTubeMaster.setStatus(ipdRylesTubeOrderList.getStatus());
			ipdRylesTubeMaster.setOrganizationId(ipdRylesTubeOrderList.getOrganizationId());
			ipdRylesTubeMaster.setUnitId(ipdRylesTubeOrderList.getUnitId());
		
		save(ipdRylesTubeMaster);
		}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveIpdRylesTubeOrder failed", he);
		} catch (Exception e) {
		logger.error("saveIpdRylesTubeOrder failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
@Override
public Response getIpdRylesTubeOrderList(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException {
	try {
	List<IpdRylesTubeDto> list=sessionFactory.getCurrentSession()
			.getNamedQuery("GET_IPD_RYLES_TUBE_ORDER_LIST")
			.setParameter("organizationId", ipdRylesTubeDto.getOrganizationId())
			.setParameter("unitId", ipdRylesTubeDto.getUnitId())
			.setParameter("patientId", ipdRylesTubeDto.getPatientId())
			.setResultTransformer(Transformers.aliasToBean(IpdRylesTubeDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
	} catch (HibernateException he) {
	logger.error("getIpdRylesTubeOrderList failed", he);
	} catch (Exception e) {
	logger.error("getIpdRylesTubeOrderList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
@Override
public Response updateIpdRylesTubeOrder(List<IpdRylesTubeDto> ipdRylesTubeOrderDtoList) throws ApplicationException {
	try {
		for (IpdRylesTubeDto  ipdRylesTubeOrderList : ipdRylesTubeOrderDtoList) {
			IpdRylesTubeMaster ipdRylesTubeMaster = findById(ipdRylesTubeOrderList.getIpdRylesTubeId());
			ipdRylesTubeMaster.setTubeFeedingProductId(ipdRylesTubeOrderList.getTubeFeedingProductId());
			ipdRylesTubeMaster.setAdmissionId(ipdRylesTubeOrderList.getAdmissionId());
			ipdRylesTubeMaster.setDoctorId(ipdRylesTubeOrderList.getDoctorId());
			ipdRylesTubeMaster.setQuantity(ipdRylesTubeOrderList.getQuantity());
			ipdRylesTubeMaster.setPatientId(ipdRylesTubeOrderList.getPatientId());
			ipdRylesTubeMaster.setFrequencyId(ipdRylesTubeOrderList.getFrequencyId());
			ipdRylesTubeMaster.setIpdRylesTubeId(ipdRylesTubeOrderList.getIpdRylesTubeId());
			ipdRylesTubeMaster.setDoctorId(ipdRylesTubeOrderList.getDoctorId());
			ipdRylesTubeMaster.setRylesTubeInstruction(ipdRylesTubeOrderList.getRylesTubeInstruction());
			ipdRylesTubeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(ipdRylesTubeOrderList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			ipdRylesTubeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(ipdRylesTubeOrderList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			ipdRylesTubeMaster.setCreatedBy(ipdRylesTubeOrderList.getCreatedBy());
			ipdRylesTubeMaster.setUpdatedBy(ipdRylesTubeOrderList.getUpdatedBy());
			ipdRylesTubeMaster.setStatus(ipdRylesTubeOrderList.getStatus());
			ipdRylesTubeMaster.setOrganizationId(ipdRylesTubeOrderList.getOrganizationId());
			ipdRylesTubeMaster.setUnitId(ipdRylesTubeOrderList.getUnitId());
		
		save(ipdRylesTubeMaster);
		}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateIpdRylesTubeOrder failed", he);
		} catch (Exception e) {
		logger.error("updateIpdRylesTubeOrder failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE,COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response cancelRylesTubeOrder(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException {
	try {
			IpdRylesTubeMaster ipdRylesTubeMaster = findById(ipdRylesTubeDto.getIpdRylesTubeId());
			ipdRylesTubeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(ipdRylesTubeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			ipdRylesTubeMaster.setUpdatedBy(ipdRylesTubeDto.getUpdatedBy());
			ipdRylesTubeMaster.setStatus('I');
			ipdRylesTubeMaster.setOrganizationId(ipdRylesTubeDto.getOrganizationId());
			ipdRylesTubeMaster.setUnitId(ipdRylesTubeDto.getUnitId());
		
		update(ipdRylesTubeMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("cancelRylesTubeOrder failed", he);
		} catch (Exception e) {
		logger.error("cancelRylesTubeOrder failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE,COMMON_ERROR_MESSAGE, null, null);
}
}