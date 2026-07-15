package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.CrossMatchRegisterDetailsDto;
import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.bloodBank.dto.IssueBloodBagDto;
import com.param.bloodBank.model.CrossMatchRegister;
import com.param.bloodBank.model.CrossMatchRegisterDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class CrossMatchRegisterDaoImpl extends GenericDao<CrossMatchRegister, Integer> implements ICrossMatchRegisterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(CrossMatchRegisterDaoImpl.class);


	public CrossMatchRegisterDaoImpl() {
		super(CrossMatchRegister.class);
	}

	@Override
	public Response saveCrossMatchRegister(CrossMatchRegisterDto crossMatchRegisterDto) throws ApplicationException {
		try {
			
			CrossMatchRegister mstr = new CrossMatchRegister();
			mstr.setBtoId(crossMatchRegisterDto.getBtoId());
			mstr.setComponentId(crossMatchRegisterDto.getComponentId());
			mstr.setCreatedBy(crossMatchRegisterDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(crossMatchRegisterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setCrossMatchStatus(crossMatchRegisterDto.getCrossMatchStatus());
			mstr.setOrganizationId(crossMatchRegisterDto.getOrganizationId());
			mstr.setPatientBloodGroup(crossMatchRegisterDto.getPatientBloodGroup());
			mstr.setPatientRequisitionId(crossMatchRegisterDto.getPatientRequisitionId());
			mstr.setPatientId(crossMatchRegisterDto.getPatientId());
			mstr.setPendingQty(crossMatchRegisterDto.getPendingQty());
			mstr.setRemark(crossMatchRegisterDto.getRemark());
			mstr.setRequiredBloodGroup(crossMatchRegisterDto.getRequiredBloodGroup());
			mstr.setRequiredQty(crossMatchRegisterDto.getRequiredQty());
			mstr.setRequisitionDate(GlobalCommonDateUtils.getDate(crossMatchRegisterDto.getRequisitionDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setIssueDate(GlobalCommonDateUtils.getDate(crossMatchRegisterDto.getIssueDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setBloodComponentOrderId(crossMatchRegisterDto.getBloodComponentOrderId());
			mstr.setStatus(crossMatchRegisterDto.getStatus());
			mstr.setTechnicianId(crossMatchRegisterDto.getTechnicianId());
			mstr.setUnitId(crossMatchRegisterDto.getUnitId());
			mstr.setUpdatedBy(crossMatchRegisterDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(crossMatchRegisterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			mstr = save(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
		} catch (HibernateException he) {
			logger.error("saveCrossMatchRegister failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveCrossMatchRegister failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveCrossMatchRegisterDetails(CrossMatchRegisterDto crossMatchRegisterDto)
			throws ApplicationException {
		try {
			List<CrossMatchRegisterDetailsDto> list = crossMatchRegisterDto.getCrossMatchRegisterDetails();
			Session session=sessionFactory.getCurrentSession();
				if(list.size()>0) {
					for (CrossMatchRegisterDetailsDto dto : list) {
						
						CrossMatchRegisterDetails dtls = new CrossMatchRegisterDetails();
						dtls.setAlbuminMajor(dto.getAlbuminMajor()!=null?dto.getAlbuminMajor():0);
						dtls.setAlbuminMinor(dto.getAlbuminMinor()!=null?dto.getAlbuminMinor():0);
						dtls.setBloodGroupId(dto.getBloodGroupId());
						dtls.setComponentId(dto.getComponentId());
						dtls.setBloodBagNo(dto.getBloodBagNo());
						dtls.setCoombsMajor(dto.getCoombsMajor()!=null?dto.getCoombsMajor():0);
						dtls.setCoombsMinor(dto.getCoombsMinor()!=null?dto.getCoombsMinor():0);
						dtls.setCreatedBy(dto.getCreatedBy());
						dtls.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
						dtls.setCrossMatchRegisterDetailsStatus(dto.getCrossMatchRegisterDetailsStatus());
						dtls.setCrossMatchRegisterId(crossMatchRegisterDto.getCrossMatchRegisterId());
						dtls.setCrossMatchReportResult(dto.getCrossMatchReportResult());
						dtls.setOrganizationId(dto.getOrganizationId());
						dtls.setSalineMajor(dto.getSalineMajor()!=null?dto.getSalineMajor():0);
						dtls.setSalineMinor(dto.getSalineMinor()!=null?dto.getSalineMinor():0);
						dtls.setSegmentNo(dto.getSegmentNo());
						dtls.setStatus(dto.getStatus());
						dtls.setUnitId(dto.getUnitId());
						dtls.setUpdatedBy(dto.getUpdatedBy());
						dtls.setBloodBankStockId(dto.getBloodBankStockId());
						dtls.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
						session.save(dtls);
					}
					
				}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveCrossMatchRegisterDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveCrossMatchRegisterDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCrossMatchRegisteredOnBagReservation(CrossMatchRegisterDto dto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_CROSS_MATCH_REGISTERED_DETAILS_ON_RESERVATION").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				subQuery.append(" AND to_char(mstr.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId());

			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				actualQuery.setParameter("fromDate",dto.getFromDate());
				actualQuery.setParameter("toDate",dto.getToDate());
			}
			List<CrossMatchRegisterDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(CrossMatchRegisterDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){
			logger.error("getCrossMatchRegisteredOnBagReservation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCrossMatchRegister(List<CrossMatchRegisterDto> dto) throws ApplicationException {
		try {
			List<CrossMatchRegisterDto> list = dto;
			if(list.size()>0) {
				for (CrossMatchRegisterDto crossMatchRegisterDto : list) {
					
					CrossMatchRegister crossMatchRegister = findById(crossMatchRegisterDto.getCrossMatchRegisterId());
					crossMatchRegister.setCrossMatchStatus(12);
					crossMatchRegister=update(crossMatchRegister);
					
					@SuppressWarnings("unused")
					int count = sessionFactory.getCurrentSession().createQuery(" Update CrossMatchRegisterDetails  crossDetail set crossDetail.isExported=true where crossDetail.crossMatchRegisterDetailsId="+crossMatchRegisterDto.getCrossMatchRegisterDetailsId()).executeUpdate();
					
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("updateCrossMatchRegister failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCrossMatchRegisteredOnIssue(CrossMatchRegisterDto dto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_CROSS_MATCH_REGISTERED_DETAILS_ON_ISSUE").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				subQuery.append(" AND to_char(mstr.issueDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId());
			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				actualQuery.setParameter("fromDate",dto.getFromDate());
				actualQuery.setParameter("toDate",dto.getToDate());
			}


				//	.setString("reserveDate", dto.getRequisitionDate())
			List<CrossMatchRegisterDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(CrossMatchRegisterDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){
			logger.error("getCrossMatchRegisteredOnIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCrossMatchRegisterDetailsByCrossMatchID(CrossMatchRegisterDto dto) throws ApplicationException {
		try {
			List<CrossMatchRegisterDetailsDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CROSS_MATCH_REGISTER_DETAILS_BY_CROSS_MATCH_ID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("crossMatchRegisterId", dto.getCrossMatchRegisterId())
					.setResultTransformer(Transformers.aliasToBean(CrossMatchRegisterDetailsDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
				
			} catch (HibernateException he) {
				logger.error("getCrossMatchRegisterDetailsByCrossMatchID failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getCrossMatchRegisterDetailsByCrossMatchID failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateCrossMatchingReqQtyByCrossMatchId(IssueBloodBagDto issueBloodBagDto)
			throws ApplicationException {
		try {
				CrossMatchRegister crossMatchRegister = findById(issueBloodBagDto.getCrossMatchRegisterId());
				int Qty = crossMatchRegister.getRequiredQty();
				Qty -=issueBloodBagDto.getBloodUnitQty();
				crossMatchRegister.setRequiredQty(Qty);
				crossMatchRegister=update(crossMatchRegister);
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateCrossMatchingReqQtyByCrossMatchId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateCrossMatchingReqQtyByCrossMatchId failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

}
