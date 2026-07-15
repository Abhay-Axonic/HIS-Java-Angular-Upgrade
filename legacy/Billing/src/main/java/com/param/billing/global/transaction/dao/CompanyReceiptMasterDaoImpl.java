package com.param.billing.global.transaction.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.dto.CompanyReceiptMasterDto;
import com.param.billing.global.model.CompanyReceiptMaster;
import com.param.dozer.DozerFactory;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes"})
public class CompanyReceiptMasterDaoImpl extends GenericDao<CompanyReceiptMaster, Integer> implements ICompanyReceiptMasterDao, ICommonConstants{

	public CompanyReceiptMasterDaoImpl() {
		super(CompanyReceiptMaster.class);
	}

	@Autowired
	private DozerFactory factory;
	
	@Override
	public Response saveCompanyReceiptMaster(CompanyReceiptMasterDto dto) throws ApplicationException {
		try{
			Date today = TimeZoneComponent.getDateByzone(dto.getUnitId());
			CompanyReceiptMaster master = factory.map(dto, CompanyReceiptMaster.class, null);
			master.setCreatedDate(today);
			master.setCompanyReceiptDate(today);
			master = save(master);
			today = null;
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, master);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response mergeCompanyReceipt(CompanyReceiptMaster master) throws ApplicationException {
		try{
			master = merge(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, master);
		}catch(Exception e){
			
			throw e;
		}
	}

}
