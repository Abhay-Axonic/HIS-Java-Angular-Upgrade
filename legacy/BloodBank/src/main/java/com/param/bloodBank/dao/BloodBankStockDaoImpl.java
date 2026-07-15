package com.param.bloodBank.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.bloodBank.model.BloodBankStock;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBankStockDaoImpl extends GenericDao<BloodBankStock, Integer> implements IBloodBankStockDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankStockDaoImpl.class);


	public BloodBankStockDaoImpl() {
		super(BloodBankStock.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveBloodBankStock(BloodBankStockDto dto) throws ApplicationException {
		try {
			BloodBankStockDto bloodBankStockDto  = new BloodBankStockDto();
			if(dto!=null)
			{
				 bloodBankStockDto = (BloodBankStockDto)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_BLOOD_BAG_NUMBER")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("bloodBagNo", dto.getBloodBagNo())
					.setResultTransformer(Transformers.aliasToBean(BloodBankStockDto.class)).uniqueResult();
			
			}
				if(bloodBankStockDto!=null) {
					
					return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Blood Bag " + bloodBankStockDto.getBloodBagNo() +"  "+ ALREADY_EXIST, null, bloodBankStockDto);
					
				}else {
					BloodBankStock mstr = new BloodBankStock();
					mstr.setBatchNo(dto.getBatchNo());
					mstr.setExpiryDate(GlobalCommonDateUtils.getDate(dto.getExpiryDate(),"dd-M-yyyy HH:ss:mm"));
					mstr.setBloodBagNo(dto.getBloodBagNo());
					mstr.setBloodBagTypeId(dto.getBloodBagTypeId());
					mstr.setBloodBankId(dto.getBloodBankId());
					mstr.setBloodBagCapacityId(dto.getBloodBagCapacityId());
					mstr.setBloodGroupId(dto.getBloodGroupId());
					mstr.setBloodUnit(dto.getBloodUnit());
					mstr.setCreatedBy(dto.getCreatedBy());
					mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
					mstr.setOrganizationId(dto.getOrganizationId());
					mstr.setSegmentNumber(dto.getSegmentNumber());
					mstr.setShortName(dto.getShortName());
					mstr.setStatus(dto.getStatus());
					mstr.setUnitId(dto.getUnitId());
					mstr.setUpdatedBy(dto.getUpdatedBy());
					mstr.setLoanNumber(dto.getLoanNumber());
					mstr.setLoanInId(dto.getLoanInId());
					mstr.settTDCheck(dto.gettTDCheck());
					mstr.setBloodComponentId(dto.getBloodComponentId());
					mstr.setStockStatusId(dto.getStockStatusId());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
					save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
		} catch (Exception e) {
			logger.error("saveBloodBankStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchBloodBankStock(BloodBankStockDto dto) throws ApplicationException {
		try{
		
				 List<BloodBankStockDto> list = sessionFactory.getCurrentSession()
							.getNamedQuery("SEARCH_BLOOD_BAG_STOCK_BY_MULIPLE_CRITERIA")
							.setParameter("organizationId", dto.getOrganizationId())
							.setParameter("unitId", dto.getUnitId())
							.setParameter("loanInNo", dto.getLoanNumber().toLowerCase())
							.setParameter("loanInID",dto.getLoanInId())
						.setResultTransformer(Transformers.aliasToBean(BloodBankStockDto.class)).list();
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch(Exception e){
				logger.error("searchBloodBankStock failed", e);
				throw new ServiceException(e.getMessage());
			}
		
	}

	@Override
	public Long getNextValBloodBagStockId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery("select nextval('blood_bank.m_blood_bank_stock_seq')");
				Long id = ((BigInteger) query.uniqueResult()).longValue();

				SQLQuery query2 = session.createSQLQuery("select setval('blood_bank.m_blood_bank_stock_seq'," + (id-1) + ")");
				query2.uniqueResult();

				return id;
		} catch (HibernateException he) {
			logger.error("getNextValBloodBagStockId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getNextValBloodBagStockId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBloodBankStockStatus(BloodBankStockDto dto) throws ApplicationException {
		try {
				BloodBankStock bbsMaster = findById(dto.getBloodBankStockId());
				bbsMaster.setStatus(dto.getStatus());
				bbsMaster.setUpdatedBy(dto.getUpdatedBy());
				bbsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				update(bbsMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodBankStockStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodBankStockStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response updateLoanInStatus(BloodBankStockDto dto) throws ApplicationException {
		try {
			BloodBankStock bbsMaster = findById(dto.getBloodBankStockId());
			bbsMaster.setStockStatusId(dto.getStockStatusId());
			bbsMaster.setUpdatedBy(dto.getUpdatedBy());
			bbsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			bbsMaster = update(bbsMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, bbsMaster);
			
		} catch (HibernateException he) {
			logger.error("updateLoanInStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateLoanInStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchLoanInDetails(BloodBankStockDto dto) throws ApplicationException {
		try {
			List<BloodBankStockDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_LOAN_IN_BY_MULIPLE_CRITERIA")
					.setParameter("LoanInId", dto.getLoanInId())
					.setParameter("loanInNO", dto.getLoanNumber())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodBankStockDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("searchLoanInDetails failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("searchLoanInDetails failed", e);
				throw new ServiceException(e.getMessage());
			}

	}

	@Override
	public Response getAvailableBloodGroupByBankID(BloodBankStockDto dto) throws ApplicationException {
		try {
			List<BloodBankStockDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AVAILABLE_BLOOD_BY_BLOOD_BANK_ID")
					.setParameter("bloodBankId", dto.getBloodBankId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodBankStockDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getAvailableBloodGroupByBankID failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getAvailableBloodGroupByBankID failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getBloodBankStockListByTTDStatus(BloodBankStockDto bloodBankStockDto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_BLOOD_BAG_STOCK_DETAILS_BY_TTD_STATUS").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(bloodBankStockDto.getFromDate()!=null && bloodBankStockDto.getToDate()!=null && bloodBankStockDto.getFromDate()!="" && bloodBankStockDto.getToDate()!=""){
				subQuery.append(" AND to_char(mstr.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}

			if(bloodBankStockDto.getBloodBagNo()!=null && bloodBankStockDto.getBloodBagNo()!=""){
				subQuery.append(" AND mstr.bloodBagNo=:bloodBagNo ");
			}
		
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery.setParameter("organizationId", bloodBankStockDto.getOrganizationId())
					.setParameter("unitId", bloodBankStockDto.getUnitId());

			if(bloodBankStockDto.getFromDate()!=null && bloodBankStockDto.getToDate()!=null && bloodBankStockDto.getFromDate()!="" && bloodBankStockDto.getToDate()!=""){
				actualQuery.setParameter("fromDate",bloodBankStockDto.getFromDate());
				actualQuery.setParameter("toDate",bloodBankStockDto.getToDate());			}

			if(bloodBankStockDto.getBloodBagNo()!=null && bloodBankStockDto.getBloodBagNo()!=""){
				actualQuery.setParameter("bloodBagNo",bloodBankStockDto.getBloodBagNo());
			}

			List<BloodBagCollectionDto> list =	actualQuery.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("getBloodBankStockListByTTDStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStockTtdFlag(int bloodBankStockId) throws ApplicationException {
		try {
			BloodBankStock mpper = findById(bloodBankStockId);
			mpper.setExportedToTtd(true);
			update(mpper);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			logger.error("updateStockTtdFlag failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateStockTtdFlag failed", e);
			throw new ServiceException(e.getMessage());
		}
	
	
	}

	@Override
	public Response getBagNoListByComponentAndGroupId(BloodBankStockDto dto) throws ApplicationException {
		try {
			List<BloodBankStockDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AVAILABLE_BLOOD_BAG_BY_COMPONENT_AND_GROUP_ID")
					.setParameter("bloodComponentId", dto.getBloodComponentId())
					.setParameter("bloodBagNo",  dto.getBloodBagNo() != null ? '%' + dto.getBloodBagNo().toLowerCase() + '%' : "")
					.setParameter("bloodGroupId", dto.getBloodGroupId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("currentDate", dto.getCurrentDate())
					.setResultTransformer(Transformers.aliasToBean(BloodBankStockDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBagNoListByComponentAndGroupId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBagNoListByComponentAndGroupId failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateDonorBloodInStock(BloodBankStockDto dto) {
		try {
			BloodBankStockDto bloodBankStockDto  = new BloodBankStockDto();
			if(dto!=null)
			{
				 bloodBankStockDto = (BloodBankStockDto)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_BLOOD_BAG_NUMBER")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("bloodBagNo", dto.getBloodBagNo())
					.setResultTransformer(Transformers.aliasToBean(BloodBankStockDto.class)).uniqueResult();
			
			}
				if(bloodBankStockDto!=null) {
					
					return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Blood Bag " + bloodBankStockDto.getBloodBagNo() +"  "+ ALREADY_EXIST, null, bloodBankStockDto);
					
				}else {
					BloodBankStock mstr = new BloodBankStock();
					mstr.setBatchNo(dto.getBatchNo());
					mstr.setExpiryDate(GlobalCommonDateUtils.getDate(dto.getExpiryDate(),"dd-M-yyyy HH:ss:mm"));
					mstr.setBloodBagNo(dto.getBloodBagNo());
					mstr.setBloodBagTypeId(dto.getBloodBagTypeId());
					mstr.setBloodBankId(dto.getBloodBankId());
					mstr.setBloodBagCapacityId(dto.getBloodBagCapacityId());
					mstr.setBloodGroupId(dto.getBloodGroupId());
					mstr.setBloodUnit(dto.getBloodUnit());
					mstr.setCreatedBy(dto.getCreatedBy());
					mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
					mstr.setOrganizationId(dto.getOrganizationId());
					mstr.setSegmentNumber(dto.getSegmentNumber());
					mstr.setShortName(dto.getShortName());
					mstr.setStatus(dto.getStatus());
					mstr.setUnitId(dto.getUnitId());
					mstr.setUpdatedBy(dto.getUpdatedBy());
					mstr.setDonorId(dto.getDonorId());
					mstr.setDonorNumber(dto.getDonorNumber());
					mstr.settTDCheck(false);
					mstr.setBloodComponentId(dto.getBloodComponentId());
					mstr.setStockStatusId(6);
					mstr.setApprovedBy(dto.getApprovedById()!= null ? dto.getApprovedById() : null);
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
					save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
		} catch (Exception e) {
			logger.error("updateDonorBloodInStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchBagNoListByMultipleCriteria(BloodBankStockDto dto) throws ApplicationException {
		try {
			List<BloodBankStockDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AVAILABLE_BLOOD_BAG_LIST_BY_MULTIPLE_CRITERIA")
					.setParameter("bloodComponentId", dto.getBloodComponentId())
					.setParameter("bloodBagNo",  dto.getBloodBagNo() != null ? dto.getBloodBagNo().toLowerCase() + '%' : "")
					.setParameter("bloodGroupId", dto.getBloodGroupId())
					.setParameter("bloodBagCapacityId", dto.getBloodBagCapacityId())
					.setParameter("bloodBagTypeId", dto.getBloodBagTypeId())
					.setParameter("bloodBankId", dto.getBloodBankId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("currentDate", dto.getCurrentDate())
					.setResultTransformer(Transformers.aliasToBean(BloodBankStockDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("searchBagNoListByMultipleCriteria failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("searchBagNoListByMultipleCriteria failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
}

	

