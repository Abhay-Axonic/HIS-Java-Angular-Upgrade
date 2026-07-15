package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.IssueBloodBagDetailMapperDto;
import com.param.bloodBank.dto.IssueBloodBagDto;
import com.param.bloodBank.model.IssueBloodBagDetailMapper;
import com.param.bloodBank.model.IssueBloodBagMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class IssueBloodBagDaoImpl extends GenericDao<IssueBloodBagMapper, Integer> implements IIssueBloodBagDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(IssueBloodBagDaoImpl.class);


	public IssueBloodBagDaoImpl() {
		super(IssueBloodBagMapper.class);
	}

	@Override
	public Response saveIssueBloodBag(IssueBloodBagDto issueBloodBagDto) throws ApplicationException {
		try {
			IssueBloodBagMapper mpper = new IssueBloodBagMapper();
			mpper.setBtoId(issueBloodBagDto.getBtoId());
			mpper.setCreatedBy(issueBloodBagDto.getCreatedBy());
			mpper.setCreatedDate(GlobalCommonDateUtils.getDate(issueBloodBagDto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
			mpper.setRequisitionDate(GlobalCommonDateUtils.getDate(issueBloodBagDto.getRequisitionDate(),"dd-M-yyyy HH:ss:mm"));
			mpper.setIssueDate(GlobalCommonDateUtils.getDate(issueBloodBagDto.getIssueDate(),"dd-M-yyyy HH:ss:mm"));
			mpper.setBloodUnitQty(issueBloodBagDto.getBloodUnitQty());
			mpper.setCrossMatchRegisterId(issueBloodBagDto.getCrossMatchRegisterId());
			//mpper.setIssueStatusId(14);
			mpper.setIssuerId(issueBloodBagDto.getIssuerId());
			mpper.setOrganizationId(issueBloodBagDto.getOrganizationId());
			mpper.setPatientId(issueBloodBagDto.getPatientId());
			mpper.setReceivedId(issueBloodBagDto.getReceivedId());
			mpper.setBloodBagIssueNo(issueBloodBagDto.getBloodBagIssueNo());
			mpper.setRemarks(issueBloodBagDto.getRemarks());
			mpper.setStatus(issueBloodBagDto.getStatus());
			mpper.setUnitId(issueBloodBagDto.getUnitId());
			mpper.setUpdatedBy(issueBloodBagDto.getUpdatedBy());
			mpper.setUpdatedDate(GlobalCommonDateUtils.getDate(issueBloodBagDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
			
			mpper = save(mpper);
			if(mpper.getIssueBloodBagId()!=null)
			{
			if(issueBloodBagDto.getBloodBagIssueList().size()>0 && issueBloodBagDto.getBloodBagIssueList()!=null)
			{
				Session session=sessionFactory.getCurrentSession();
				for (IssueBloodBagDetailMapperDto dto : issueBloodBagDto.getBloodBagIssueList()) {
					
					IssueBloodBagDetailMapper detailMppr = new IssueBloodBagDetailMapper();
					detailMppr.setBagNo(dto.getBagNo());
					detailMppr.setBloodGroupId(dto.getBloodGroupId());
					detailMppr.setCrossMatchRegisterDetailsId(dto.getCrossMatchRegisterDetailsId());
					detailMppr.setCrossMatchReportResultStatus(dto.getCrossMatchReportResult());
					detailMppr.setIssueBloodBagStatusId(dto.getIssueBloodBagStatusId());
					detailMppr.setComponentId(dto.getComponentId());
					detailMppr.setCreatedBy(dto.getCreatedBy());
					detailMppr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
					detailMppr.setIssueBloodBagId(mpper.getIssueBloodBagId());
					detailMppr.setOrganizationId(dto.getOrganizationId());
					detailMppr.setSegmentNo(dto.getSegmentNo());
					detailMppr.setStatus(dto.getStatus());
					detailMppr.setUnitId(dto.getUnitId());
					detailMppr.setUpdatedBy(dto.getUpdatedBy());
					detailMppr.setReasonId(dto.getReasonId());
					detailMppr.setNote(dto.getNote());
					detailMppr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
					session.save(detailMppr);
				}
			}
			}	
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mpper);
			
			} catch (HibernateException he) {
				logger.error("saveIssueBloodBag failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveIssueBloodBag failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateCrossMatchingDetailsStatus(IssueBloodBagDetailMapperDto issueBloodBagDto) throws ApplicationException {
		try {

			int count = sessionFactory.getCurrentSession().createQuery(
					" Update CrossMatchRegisterDetails  crossDetail set crossDetail.isExported=true where crossDetail.crossMatchRegisterDetailsId="
							+ issueBloodBagDto.getCrossMatchRegisterDetailsId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}

		catch (HibernateException he) {
			logger.error("updateCrossMatchingDetailsStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateCrossMatchingDetailsStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getIssueBloodBagOnIssueAcceptanceScreen(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)
			throws ApplicationException {
		try{
			Integer count=0;
			 List<BloodBagIssueAcceptanceDto> list = sessionFactory.getCurrentSession()
						.getNamedQuery("SEARCH_ISSUED_BLOOD_BAG_ON_ISSUE_ACCEPTANCE")
						.setInteger("organizationId", bloodBagIssueAcceptanceDto.getOrganizationId())
						.setInteger("unitId",bloodBagIssueAcceptanceDto.getUnitId())
						.setString("fromDate",bloodBagIssueAcceptanceDto.getFromDate())
						.setString("toDate",bloodBagIssueAcceptanceDto.getToDate())
						.setFirstResult(bloodBagIssueAcceptanceDto.getOffset() != null ? bloodBagIssueAcceptanceDto.getOffset()  : 0)
						.setMaxResults(bloodBagIssueAcceptanceDto.getNoOfRecordsPerPage() != null ? bloodBagIssueAcceptanceDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodBagIssueAcceptanceDto.class)).list();
			
			 List<BloodBagIssueAcceptanceDto> listCount = sessionFactory.getCurrentSession()
						.getNamedQuery("SEARCH_ISSUED_BLOOD_BAG_ON_ISSUE_ACCEPTANCE")
						.setInteger("organizationId", bloodBagIssueAcceptanceDto.getOrganizationId())
						.setInteger("unitId",bloodBagIssueAcceptanceDto.getUnitId())
						.setString("fromDate",bloodBagIssueAcceptanceDto.getFromDate())
						.setString("toDate",bloodBagIssueAcceptanceDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(BloodBagIssueAcceptanceDto.class)).list();
			 if(listCount != null && listCount.size()>0) {
				count= listCount.size();
			 }
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, count);
		}catch(Exception e){
			logger.error("getIssueBloodBagOnIssueAcceptanceScreen failed", e);
			throw new ServiceException(e.getMessage());
		}
	
	}

	@Override
	public Response updateIssuedStatusByIssueBloodBagDetailsId(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)
			throws ApplicationException {
		try {

			int count = sessionFactory.getCurrentSession().createQuery(
					" Update IssueBloodBagDetailMapper  dtls set dtls.issueBloodBagStatusId=15 where dtls.issueBloodBagDetailsId="
							+ bloodBagIssueAcceptanceDto.getIssueBloodBagDetailsId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}

		catch (HibernateException he) {
			logger.error("updateIssuedStatusByIssueBloodBagDetailsId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateIssuedStatusByIssueBloodBagDetailsId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
