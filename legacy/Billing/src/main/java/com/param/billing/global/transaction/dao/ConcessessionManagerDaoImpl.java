package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.billing.global.transaction.model.ConcessionManagerBillLog;
import com.param.global.common.ICommonConstants;
import com.param.utility.TimeZoneComponent;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class ConcessessionManagerDaoImpl implements IConcessessionManagerDao,ICommonConstants{

	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveConcessessionManagerLog(BillingMaster billObj, List<BillingDetails> billingDetailsList,
			List<BillPayeeDetails> billPayeeDetailsList) 
	{
		try {

				ConcessionManagerBillLog billLog = new ConcessionManagerBillLog();
				
					billLog.setBillingMasterId(billObj.getBillingMasterId());
					billLog.setEncounterId(billObj.getEncounterId());
					billLog.setAdmissionId(billObj.getAdmissionId());
					billLog.setVisitTypeId(billObj.getVisitTypeId());
					billLog.setPatientId(billObj.getPatientId());
					billLog.setBillNumber(billObj.getBillNumber());
					billLog.setBillDateTime(billObj.getBillDateTime());
					billLog.setNetAmount(billObj.getNetAmount());
					billLog.setDiscountAmount(billObj.getDisallowedAmount());
					billLog.setConcessionAmount(billObj.getConcessionAmount());
					billLog.setGst(billObj.getGst());
					billLog.setTotalBillAmount(billObj.getTotalBillAmount());
					billLog.setOutstanding(billObj.getOutstanding());
					billLog.setRoundOff(billObj.getRoundOff());
					billLog.setIsCreditBill(billObj.getIsCreditBill());
					billLog.setIsInvoiceCreation(billObj.getIsInvoiceCreation());
					billLog.setUnitId(billObj.getUnitId());
					billLog.setOrganisationId(billObj.getOrganisationId());
					billLog.setStatus(billObj.getStatus());
					billLog.setCreatedDate(TimeZoneComponent.getDateByzone(billObj.getUnitId()).getTime());
					billLog.setCreatedBy(billObj.getCreatedBy());
					billLog.setBillStatusId(billObj.getBillStatusId());
					billLog.setCreditPayable(billObj.getCreditPayable());
					billLog.setSelfPayable(billObj.getSelfPayable());
					billLog.setDisallowedAmount(billObj.getDisallowedAmount());
					billLog.setCrAmount(billObj.getCrAmount());
					billLog.setIsInterimOrFinalBill(billObj.getIsInterimOrFinalBill());
				
				sessionFactory.getCurrentSession().save(billLog);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

}
