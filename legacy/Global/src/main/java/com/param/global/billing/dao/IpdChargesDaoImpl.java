package com.param.global.billing.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Repository
@SuppressWarnings({"rawtypes"})
public class IpdChargesDaoImpl implements IpdChargesDao, ICommonConstants{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Response getAdmissionsById(int admissionId) throws BillingException {
		try{
			AdmissionListIpdChargesResDto dto = (AdmissionListIpdChargesResDto)sessionFactory.getCurrentSession().getNamedQuery("GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT").setInteger("admissionId", admissionId)
										.setResultTransformer(Transformers.aliasToBean(AdmissionListIpdChargesResDto.class)).uniqueResult();
			if(dto!=null) {
				List<Integer> tariffIds = getTariffIds(admissionId);
				dto.setTariffIds(tariffIds.size()>0 ? tariffIds: null);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAdmissionsByIdForUpdatePayee(int admissionId) throws BillingException {
		try{
			AdmissionListIpdChargesResDto dto = (AdmissionListIpdChargesResDto)sessionFactory.getCurrentSession().getNamedQuery("GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT_FOR_UPDATE_PAYEE").setInteger("admissionId", admissionId)
										.setResultTransformer(Transformers.aliasToBean(AdmissionListIpdChargesResDto.class)).uniqueResult();
			
			dto.setTariffIds(getTariffIds(admissionId));
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getAdmissionsDetailsByAdmissionIdForCancelledBills(int admissionId) {
		try{
			AdmissionListIpdChargesResDto dto = (AdmissionListIpdChargesResDto)sessionFactory.getCurrentSession().getNamedQuery("GET_ADM_DTLS_CANCELLED_BILLS").setInteger("admissionId", admissionId)
										.setResultTransformer(Transformers.aliasToBean(AdmissionListIpdChargesResDto.class)).uniqueResult();
		
			dto.setTariffIds(getTariffIds(admissionId));
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public List<Integer> getTariffIds(int admissionId) {
		try{
			
			String query = "select "
					+ " 	com.tariff_id "
					+ " from "
					+ " 	service.m_company_contract_master com "
					+ " left join opd.t_patient_sponsor_details tpsd on "
					+ " 	com.contract_id = tpsd.contract_id  "
					+ " where "
					+ " 	tpsd.admission_id =:admissionId "
					+ " and tpsd.status ='A' "; 
			
			List<Integer> ids = sessionFactory.getCurrentSession().createSQLQuery(query).setInteger("admissionId", admissionId).list();
			
			return ids;
		}catch(Exception e){
			
			throw e;
		}
	}

}
