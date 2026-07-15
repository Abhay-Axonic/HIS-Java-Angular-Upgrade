package com.param.billing.unit.dao;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.global.transaction.config.dto.ChargeConversionMasterDto;
import com.param.billing.global.transaction.model.ChargeConversionMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeConversionMasterDaoImpl extends GenericDao<ChargeConversionMaster, Integer> implements IChargeConversionMasterDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(ChargeConversionMasterDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public ChargeConversionMasterDaoImpl() {
		super(ChargeConversionMaster.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Response saveChargeConversionMasterList(ChargeConversionMasterDto masterDto)
			throws ApplicationException {
		try{
			ChargeConversionMaster chargeConversionMst = new ChargeConversionMaster();
			chargeConversionMst.setCreatedBy(masterDto.getCreatedBy());
			chargeConversionMst.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
			chargeConversionMst.setOrgId(masterDto.getOrgId());
			chargeConversionMst.setOrgUnitId(masterDto.getOrgUnitId());
			chargeConversionMst.setAdmissionId(masterDto.getAdmissionId());
			chargeConversionMst.setEncounterId(masterDto.getEncounterId());
			chargeConversionMst.setFromContractId(masterDto.getFromContractId());
			chargeConversionMst.setFromPayeeId(masterDto.getFromPayeeId());
			chargeConversionMst.setPatinetId(masterDto.getPatinetId());
			chargeConversionMst.setRemarks(masterDto.getRemarks());
			chargeConversionMst.setVisitTypeId(masterDto.getVisitTypeId());
			chargeConversionMst.setToContractId(masterDto.getFromContractId());
			chargeConversionMst.setToPayeeId(masterDto.getToPayeeId());
			chargeConversionMst.setUpdatedDate(TimeZoneComponent.getDateByzone(masterDto.getOrgUnitId()).getTime());
			chargeConversionMst.setUpdatedBy(masterDto.getUpdatedBy());
			chargeConversionMst.setStatus(ACTIVE);
			chargeConversionMst = save(chargeConversionMst);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, chargeConversionMst);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}



	
}
