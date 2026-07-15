package com.param.billing.global.transaction.config.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.model.TPatientContractCapDetails;
import com.param.billing.global.transaction.config.dto.TPatientContractCapDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.service.dto.ContarctConsumptionResDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TPatientContarctCapDetailsDaoImpl extends GenericDao<TPatientContractCapDetails, Integer> implements ITPatientContarctCapDetailsDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(TPatientContarctCapDetailsDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public TPatientContarctCapDetailsDaoImpl() {
		super(TPatientContractCapDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public com.param.billing.common.Response savePatientContarctCapDetails(TPatientContractCapDetailsDto masterDto)
			throws ApplicationException {
		try{
			TPatientContractCapDetails tPatientContractCapDetails = new TPatientContractCapDetails();
			tPatientContractCapDetails.setCreatedBy(masterDto.getCreatedBy());
			tPatientContractCapDetails.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()));
			tPatientContractCapDetails.setUnitId(masterDto.getUnitId());
			tPatientContractCapDetails.setOrgnisationId(masterDto.getOrgnisationId());
			tPatientContractCapDetails.setUpdatedBy(masterDto.getUpdatedBy());
			//tPatientContractCapDetails.setUpdatedDate(masterDto.getUpdatedDate() != null && !masterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(masterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(unitId));
			tPatientContractCapDetails.setAdmissionId(masterDto.getAdmissionId());
			tPatientContractCapDetails.setContractId(masterDto.getContractId());
			tPatientContractCapDetails.setDepartmentBalanceCapAmount(masterDto.getDepartmentBalanceCapAmount());
			tPatientContractCapDetails.setDepartmentId(masterDto.getDepartmentId());
			tPatientContractCapDetails.setDepartmentCapAmount(masterDto.getDepartmentCapAmount());
			tPatientContractCapDetails.setEncounterId(masterDto.getEncounterId());
			tPatientContractCapDetails.setIsServiceItem(masterDto.getIsServiceItem());
			tPatientContractCapDetails.setPatientContCapDetlsId(masterDto.getPatientContCapDetlsId());
			tPatientContractCapDetails.setProductCategoryBalanceCapAmount(masterDto.getProductCategoryBalanceCapAmount());
			tPatientContractCapDetails.setProductCategoryId(masterDto.getProductCategoryId());
			tPatientContractCapDetails.setVisitTypeId(masterDto.getVisitTypeId());
			tPatientContractCapDetails.setStatus(ACTIVE);
			tPatientContractCapDetails.setProductCategoryCapAmount(masterDto.getProductCategoryCapAmount());
			tPatientContractCapDetails.setSubDepartmentBalanceCapAmount(masterDto.getSubDepartmentBalanceCapAmount());
			tPatientContractCapDetails.setSubDepartmentId(masterDto.getSubDepartmentId());
			tPatientContractCapDetails.setSubDepartmentCapAmount(masterDto.getSubDepartmentCapAmount());
			tPatientContractCapDetails = save(tPatientContractCapDetails);
			return new com.param.billing.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, tPatientContractCapDetails);
		}catch(Exception e){
			
			throw e;
			//throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	public com.param.billing.common.Response getExistingContarctByPatientEncounter(
			 Integer encounterId, Integer visitTypeId, Integer admissionId, Integer serviceId ,Integer organisationId, Integer unitId)
			throws ServiceException {
		try {
			List<ContarctConsumptionResDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXISITNG_CONTARCT_DETAILS_BY_ENCOUNTERID_VISITTYPE_ADMISSIONID")
					.setParameter("visitTypeId",visitTypeId)
					/*.setParameter("admissionId", admissionId)*/
					.setParameter("encounterId", encounterId)
					/*.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setParameter("serviceId", serviceId)*/
					.setResultTransformer(Transformers.aliasToBean(ContarctConsumptionResDto.class))
					.list();
			return new  com.param.billing.common.Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
			//throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public com.param.billing.common.Response getContacrtConsumptionServiceDetailsByContarctIdServiceId(
			Integer contractId, Integer serviceId) throws ServiceException {
		return null;
	/*	try {
>>>>>>> Stashed changes
			List<ContarctConsumptionResDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTARCT_CONSUMPTION_SERVICE_DETAILS_BY_CONTARCTID_SERVICEID")
					.setInteger("contractId",contractId)
					.setInteger("serviceId", serviceId)
					.setResultTransformer(Transformers.aliasToBean(ContarctConsumptionResDto.class))
					.list();
			return new  com.param.billing.common.Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
<<<<<<< Updated upstream
		}
=======
		}*/


	}
	
}
