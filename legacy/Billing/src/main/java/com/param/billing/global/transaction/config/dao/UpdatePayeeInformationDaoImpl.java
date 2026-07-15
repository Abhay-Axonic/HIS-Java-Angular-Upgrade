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
import com.param.billing.global.transaction.config.dto.UpdatePayeeInformationReqDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UpdatePayeeInformationDaoImpl extends GenericDao<TPatientContractCapDetails, Integer> implements IUpdatePayeeInformationDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(UpdatePayeeInformationDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public UpdatePayeeInformationDaoImpl() {
		super(TPatientContractCapDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public com.param.billing.common.Response getPayeeInfoByVisitTypeId(UpdatePayeeInformationReqDto reqDto)
			throws ApplicationException {
		try {
			if(reqDto.getVisitTypeId() !=null || reqDto.getPatientId() !=null) {
				List<UpdatePayeeInformationReqDto> list = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SPONSOR_DETAILS_BY_VISIT_TYPE_ID")
						.setParameter("visitTypeId",reqDto.getVisitTypeId())
						.setParameter("patientId", reqDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(UpdatePayeeInformationReqDto.class))
						.list();
				return new  com.param.billing.common.Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			}
			return new  com.param.billing.common.Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}catch(Exception e){
			
			throw e;
		//	throw new ServiceException(e.getMessage());
		}
	}



	
}
