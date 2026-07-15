package com.param.billing.global.transaction.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.dto.RefundCnResDto;
import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.transaction.dto.RefundCNMasterDto;
import com.param.billing.global.transaction.model.RefundCNMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class RefundCNMasterDaoImpl extends GenericDao<RefundCNMaster, Integer> implements IRefundCNMasterDao, ICommonConstants{
	
	public RefundCNMasterDaoImpl() {
		super(RefundCNMaster.class);
	}

	@Override
	public Response saveRefundCreditNote(RefundCNMasterDto refundCNMasterDto) throws BillingException {
		try{
			RefundCNMaster master = new RefundCNMaster();
			master.setRefundCnNo(refundCNMasterDto.getRefundCnNo());
			master.setRefundCnDate(refundCNMasterDto.getRefundCnDate() != null && !refundCNMasterDto.getRefundCnDate().isEmpty() ? GlobalCommonDateUtils.getDate(refundCNMasterDto.getRefundCnDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(refundCNMasterDto.getUnitId()));
			master.setPayeeId(refundCNMasterDto.getPayeeId());
			master.setDepositId(refundCNMasterDto.getDepositId());
			master.setRefundAgainstId(refundCNMasterDto.getRefundAgainstId());
			master.setVisitTypeId(refundCNMasterDto.getVisitTypeId());
			master.setEncounterId(refundCNMasterDto.getEncounterId());
			master.setAdmissionId(refundCNMasterDto.getAdmissionId());
			master.setRefundAmount(refundCNMasterDto.getRefundAmount());
			master.setRefundedAmount(java.math.BigDecimal.ZERO);
			master.setBalanceAmount(refundCNMasterDto.getBalanceAmount());
			master.setAccountHolderName(refundCNMasterDto.getAccountHolderName());
			master.setBankId(refundCNMasterDto.getBankId());
			master.setBranchId(refundCNMasterDto.getBranchId());
			master.setIfscCode(refundCNMasterDto.getIfscCode());
			master.setAccountNo(refundCNMasterDto.getAccountNo());
			master.setBankRequestRefundAmount(refundCNMasterDto.getBankRequestRefundAmount());
			master.setRemark(refundCNMasterDto.getRemark());
			master.setOrganizationId(refundCNMasterDto.getOrganizationId());
			master.setUnitId(refundCNMasterDto.getUnitId());
			master.setCreatedBy(refundCNMasterDto.getCreatedBy());
			master.setCreatedDate(refundCNMasterDto.getCreatedDate() != null && !refundCNMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(refundCNMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(refundCNMasterDto.getUnitId()));
			master.setUpdatedBy(refundCNMasterDto.getUpdatedBy());
			master.setUpdatedDate(refundCNMasterDto.getUpdatedDate() != null && !refundCNMasterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(refundCNMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(refundCNMasterDto.getUnitId()));
			master.setCounterId(refundCNMasterDto.getCounterId());
			master.setIsApproved(refundCNMasterDto.getIsApproved());
			master.setRemark(refundCNMasterDto.getRemark());
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public BigInteger generateRefundCreditNoteNo() throws BillingException {
		try{
			BigInteger id = (BigInteger)sessionFactory.getCurrentSession().getNamedQuery("GENERATE_REFUND_CN_NO").uniqueResult();
			return id;
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateRefundCreditNote(RefundCNMasterDto refundCNMasterDto) throws BillingException {
		try{
			sessionFactory.getCurrentSession().createQuery("UPDATE RefundCNMaster refundCNMaster set "
					//+ "refundCNMaster.refundAmount=:refundAmount, "
					+ "refundCNMaster.refundedAmount=:refundedAmount, "
					+ "refundCNMaster.balanceAmount=:balanceAmount, "
					+ "refundCNMaster.updatedBy=:updatedBy, "
					+ "refundCNMaster.updatedDate=:updatedDate "
					+ "WHERE refundCNMaster.tRefundCnId=:tRefundCnId ")
			    .setParameter("tRefundCnId", refundCNMasterDto.gettRefundCnId())
			    .setParameter("balanceAmount", refundCNMasterDto.getBalanceAmount())
				.setParameter("refundedAmount", refundCNMasterDto.getRefundedAmount())
				//.setParameter("refundAmount", refundCNMasterDto.getRefundAmount())
				.setParameter("updatedBy", refundCNMasterDto.getUpdatedBy())
				.setParameter("updatedDate", GlobalCommonDateUtils.getDate(refundCNMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response searchRefundCn(RefundCnSearchDto reqDto) throws BillingException {
		try{
		//	Date fromDate =(reqDto.getFromDate()!= null && !reqDto.getToDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getFromDate(), "dd-M-yyyy HH:mm:ss"):TimeZoneComponent.getDateByzone(unitId);
		//	Date toDate = (reqDto.getFromDate()!= null && !reqDto.getToDate().isEmpty())?GlobalCommonDateUtils.getDate(reqDto.getToDate(), "dd-M-yyyy HH:mm:ss"):GlobalCommonDateUtils.getNextDateInDate(fromDate.getTime());
			
			
			
			if(reqDto.getFromDate() == null || reqDto.getToDate() == null || reqDto.getFromDate().isEmpty() || reqDto.getToDate().isEmpty())
			{
				reqDto.setFromDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "yyyy-MM-dd"));
				reqDto.setToDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "yyyy-MM-dd"));
			}
			
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("SEARCH_REFUND_CN").getQueryString());

			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty())
				query.append(" and to_char(refund_cn.refund_cn_date,'YYYY-MM-DD') >=:fromDate ");

			if(reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
				query.append(" and to_char(refund_cn.refund_cn_date,'YYYY-MM-DD') <=:toDate ");

			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				query.append(" and ( trim(lower(pat.first_name)) like :patientName or trim(lower(pat.middle_name)) like :patientName or trim(lower(pat.last_name)) like :patientName ) ");

			if(reqDto.getUhidNumber() != null && !reqDto.getUhidNumber().isEmpty())
				query.append(" and trim(lower(pat.uhid_number)) like lower(:uhidNumber) ");

			if(reqDto.getRefundCrNo() != null && !reqDto.getRefundCrNo().isEmpty())
				query.append(" and lower(crnotemst.credit_note_no) like :refundCrNo ");

			if(reqDto.getRefundCnNo() != null && !reqDto.getRefundCnNo().isEmpty())
				query.append(" and lower(refund_cn.refund_cn_no) like :refundCnNo ");

			query.append(" order by refund_cn.refund_cn_date desc ");

			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							 .setInteger("orgId", reqDto.getOrgId())
							 .setInteger("unitId", reqDto.getUnitId());

			if(reqDto.getFromDate() != null && !reqDto.getFromDate().isEmpty())
				sqlQuery.setParameter("fromDate", reqDto.getFromDate());

			if(reqDto.getToDate() != null && !reqDto.getToDate().isEmpty())
				sqlQuery.setParameter("toDate", reqDto.getToDate());

			if(reqDto.getPatientName() != null && !reqDto.getPatientName().isEmpty())
				sqlQuery.setParameter("patientName", "%" + reqDto.getPatientName().toLowerCase() + "%");

			if(reqDto.getUhidNumber() != null && !reqDto.getUhidNumber().isEmpty())
				sqlQuery.setParameter("uhidNumber", "%" + reqDto.getUhidNumber().toLowerCase() + "%");

			if(reqDto.getRefundCrNo() != null && !reqDto.getRefundCrNo().isEmpty())
				sqlQuery.setParameter("refundCrNo", "%" + reqDto.getRefundCrNo().toLowerCase() + "%");

			if(reqDto.getRefundCnNo() != null && !reqDto.getRefundCnNo().isEmpty())
				sqlQuery.setParameter("refundCnNo", "%" + reqDto.getRefundCnNo().toLowerCase() + "%");

			List<RefundCnResDto> listRefundCnResDto = sqlQuery.setResultTransformer(Transformers.aliasToBean(RefundCnResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listRefundCnResDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}





}
