package com.param.bloodBank.dao;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
import com.param.bloodBank.dto.BloodBagCollectionExaminationDetailsDto;
import com.param.bloodBank.model.BloodBagCollection;
import com.param.bloodBank.model.BloodBagCollectionExaminationDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBagCollectionDaoImpl extends GenericDao<BloodBagCollection, Integer> implements IBloodBagCollectionDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagCollectionDaoImpl.class);


	public BloodBagCollectionDaoImpl() {
		super(BloodBagCollection.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveBloodBagCollection(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException {
		try {
			BloodBagCollection mstr = new BloodBagCollection(); 
			BloodBagCollectionDto collectionDto  = new BloodBagCollectionDto();
			if(bloodBagCollectionDto.getCampId()!=null)
			{
				//System.out.println("dd"+GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(bloodBagCollectionDto.getCollectionDate(), "dd-MM-yyyy HH:ss:mm"),"dd-MM-yyyy"));
				collectionDto = (BloodBagCollectionDto)sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_FOR_UNIQUE_BY_CAMP")
					.setParameter("organizationId", bloodBagCollectionDto.getOrganizationId())
					.setParameter("unitId", bloodBagCollectionDto.getUnitId())
					.setParameter("campId", bloodBagCollectionDto.getCampId())
					.setParameter("donorId",bloodBagCollectionDto.getDonorId())
					.setParameter("collectionDate",GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(bloodBagCollectionDto.getCollectionDate(), "dd-MM-yyyy HH:ss:mm"),"dd-MM-yyyy"))
					.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionDto.class)).uniqueResult();
			
			}
			else {
				collectionDto = (BloodBagCollectionDto)sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_FOR_UNIQUE_BY_BLOOD_BANK")
						.setParameter("organizationId", bloodBagCollectionDto.getOrganizationId())
						.setParameter("unitId", bloodBagCollectionDto.getUnitId())
						.setParameter("bloodBankId", bloodBagCollectionDto.getBloodBankId())
						.setParameter("donorId",bloodBagCollectionDto.getDonorId())
						.setParameter("collectionDate",GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(bloodBagCollectionDto.getCollectionDate(), "dd-M-yyyy HH:ss:mm"),"dd-MM-yyyy"))
						.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionDto.class)).uniqueResult();
			}
			
			if(collectionDto!=null && collectionDto.getBloodBagCollectionId()!=null)
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			
			}

			LocalTime fromTime1 = LocalTime.parse(bloodBagCollectionDto.getFromTime());
			LocalDate today = LocalDate.now();
			Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime toTime1 = LocalTime.parse(bloodBagCollectionDto.getToTime());
			Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			mstr.setBagCapacityId(bloodBagCollectionDto.getBagCapacityId());
			mstr.setBloodBankId(bloodBagCollectionDto.getBloodBankId());
			mstr.setBarCode(bloodBagCollectionDto.getBarCode());
			mstr.setBatchNo(bloodBagCollectionDto.getBatchNo());
			mstr.setBloodBagCollectionStatusId(bloodBagCollectionDto.getBloodBagCollectionStatusId());
			mstr.setBloodBagTypeId(bloodBagCollectionDto.getBloodBagTypeId());
			mstr.setBloodGroupId(bloodBagCollectionDto.getBloodGroupId());
			mstr.setBloodUnitNumber(bloodBagCollectionDto.getBloodUnitNumber());
			mstr.setCampId(bloodBagCollectionDto.getCampId());
			mstr.setCollectedFrom(bloodBagCollectionDto.getCollectedFrom());
			mstr.setCollectionDate(GlobalCommonDateUtils.getDate(bloodBagCollectionDto.getCollectionDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setCreatedBy(bloodBagCollectionDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(bloodBagCollectionDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setDonorId(bloodBagCollectionDto.getDonorId());
			mstr.setFromTime(fromTime);
			mstr.setOrganizationId(bloodBagCollectionDto.getOrganizationId());
			mstr.setSegmentNumber(bloodBagCollectionDto.getSegmentNumber());
			mstr.setStatus(bloodBagCollectionDto.getStatus());
			mstr.setTerminationReasonId(bloodBagCollectionDto.getTerminationReasonId());
			mstr.setToTime(toTime);
			mstr.setUnitId(bloodBagCollectionDto.getUnitId());
			mstr.setUpdatedBy(bloodBagCollectionDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBagCollectionDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setBloodBagNo(bloodBagCollectionDto.getBloodBagNo());
			mstr.setForwardedToTtdTest(false);
			mstr = save(mstr);
			
			if(mstr!=null)
			{
				if(bloodBagCollectionDto.getExaminationDetailsList()!=null && bloodBagCollectionDto.getExaminationDetailsList().size()>0)
				{
					for(BloodBagCollectionExaminationDetailsDto dto : bloodBagCollectionDto.getExaminationDetailsList())
					{
						BloodBagCollectionExaminationDetails detls = new BloodBagCollectionExaminationDetails();
						detls.setBloodBagCollectionId(mstr.getBloodBagCollectionId());
						detls.setBpDiastolic(dto.getBpDiastolic());
						detls.setBpSystolic(dto.getBpSystolic());
						detls.setCreatedBy(dto.getCreatedBy());
						detls.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
						detls.setExaminationId(dto.getExaminationId());
						detls.setExaminationDate(GlobalCommonDateUtils.getDate(dto.getExaminationDate(),"dd-M-yyyy HH:ss:mm"));
						detls.setOrganizationId(dto.getOrganizationId());
						detls.setPulse(dto.getPulse());
						detls.setStatus(dto.getStatus());
						detls.setUnitId(dto.getUnitId());
						detls.setUpdatedBy(dto.getUpdatedBy());
						detls.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
						
						sessionFactory.getCurrentSession().save(detls);
					}
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			}
			
			} catch (HibernateException he) {
				logger.error("saveBloodBagCollection failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodBagCollection failed", e);
				throw new ServiceException(e.getMessage());
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchBloodBag(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_BLOOD_BAG_BY_MULIPLE_CRITERIA").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(bloodBagCollectionDto.getFromDate()!=null && bloodBagCollectionDto.getToDate()!=null && bloodBagCollectionDto.getFromDate()!="" && bloodBagCollectionDto.getToDate()!=""){
				subQuery.append(" AND to_char(mstr.collectionDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}

			if(bloodBagCollectionDto.getBloodBagNo()!=null && bloodBagCollectionDto.getBloodBagNo()!=""){
				subQuery.append(" AND mstr.bloodBagNo=:bloodBagNo ");
			}
		
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			actualQuery.setParameter("organizationId", bloodBagCollectionDto.getOrganizationId())
					.setParameter("unitId", bloodBagCollectionDto.getUnitId());

			if(bloodBagCollectionDto.getFromDate()!=null && bloodBagCollectionDto.getToDate()!=null && bloodBagCollectionDto.getFromDate()!="" && bloodBagCollectionDto.getToDate()!=""){
				actualQuery.setParameter("fromDate",bloodBagCollectionDto.getFromDate());
				actualQuery.setParameter("toDate",bloodBagCollectionDto.getToDate());
			}

			if(bloodBagCollectionDto.getBloodBagNo()!=null && bloodBagCollectionDto.getBloodBagNo()!=""){
				actualQuery.setParameter("bloodBagNo",bloodBagCollectionDto.getBloodBagNo());
			}


			List<BloodBagCollectionDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("searchBloodBag failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Long getNextValBagCollectionId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery("select nextval('blood_bank.blood_bag_collection_seq')");
				Long id = ((BigInteger) query.uniqueResult()).longValue();

				SQLQuery query2 = session.createSQLQuery("select setval('blood_bank.blood_bag_collection_seq'," + (id-1) + ")");
				query2.uniqueResult();

				return id;
		} catch (HibernateException he) {
			logger.error("getNextValBagCollectionId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getNextValBagCollectionId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void updateForwordedTo(int bloodBagCollectionId) throws ApplicationException {
		try {
			BloodBagCollection mpper = findById(bloodBagCollectionId);
			mpper.setForwardedToTtdTest(true);
			update(mpper);
			
		} catch (HibernateException he) {
			logger.error("updateForwordedTo failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateForwordedTo failed", e);
			throw new ServiceException(e.getMessage());
		}
	
	}

	@Override
	public Response getSavedBloodBagDetailList(BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("GET_SAVED_BLOOD_BAG_DETAIL_LIST").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(bloodBagCollectionDto.getFromDate()!=null && bloodBagCollectionDto.getToDate()!=null && bloodBagCollectionDto.getFromDate()!="" && bloodBagCollectionDto.getToDate()!=""){
				subQuery.append(" AND to_char(mstr.collectionDate,'YYYY-MM-DD') between :fromDate and :toDate");
			}

			if(bloodBagCollectionDto.getDonorName()!=null && bloodBagCollectionDto.getDonorName()!=""){
				//subQuery.append(" AND lower(concat(coalesce(donor.firstName),coalesce(donor.lastName,''))) LIKE concat('%',lower(:donorName),'%') ");
				subQuery.append(" AND lower(concat(coalesce(donor.firstName, ''), coalesce(donor.lastName, ''))) LIKE :donorName");
			}
			 
			if(bloodBagCollectionDto.getDonorNumber()!=null && bloodBagCollectionDto.getDonorNumber()!=""){
				subQuery.append(" AND donor.donorNumber=:donorNumber ");
			}
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery.setParameter("organizationId", bloodBagCollectionDto.getOrganizationId())
					.setParameter("unitId", bloodBagCollectionDto.getUnitId());

			if(bloodBagCollectionDto.getFromDate()!=null && bloodBagCollectionDto.getToDate()!=null && bloodBagCollectionDto.getFromDate()!="" && bloodBagCollectionDto.getToDate()!=""){
				actualQuery.setParameter("fromDate",bloodBagCollectionDto.getFromDate());
				actualQuery.setParameter("toDate",bloodBagCollectionDto.getToDate());}

			if(bloodBagCollectionDto.getDonorName()!=null && bloodBagCollectionDto.getDonorName()!=""){
				actualQuery.setParameter("donorName", "%" + bloodBagCollectionDto.getDonorName().toLowerCase() + "%");
			}

			if(bloodBagCollectionDto.getDonorNumber()!=null && bloodBagCollectionDto.getDonorNumber()!=""){
				actualQuery.setParameter("donorNumber",bloodBagCollectionDto.getDonorNumber());
			}


			List<BloodBagCollectionDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("getSavedBloodBagDetailList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBagCollectionDetailById(BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try {
			BloodBagCollectionDto dto = (BloodBagCollectionDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BAG_COLLECTION_DETAILS_BY_ID")
					.setParameter("bloodBagCollectionId", bloodBagCollectionDto.getBloodBagCollectionId())
					.setParameter("organizationId", bloodBagCollectionDto.getOrganizationId())
					.setParameter("unitId", bloodBagCollectionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionDto.class)).uniqueResult();
				return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
				
			} catch (HibernateException he) {
				logger.error("getBagCollectionDetailById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBagCollectionDetailById failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response getExaminationListByCollectionId(BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try {
			List<BloodBagCollectionExaminationDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXAMINATION_DETAILS_BY_COLLECTION_ID")
					.setParameter("bloodBagCollectionId", bloodBagCollectionDto.getBloodBagCollectionId())
					.setParameter("organizationId", bloodBagCollectionDto.getOrganizationId())
					.setParameter("unitId", bloodBagCollectionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionExaminationDetailsDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getExaminationListByCollectionId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getExaminationListByCollectionId failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	
	@Override
	public Response updateScreeningStatus(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery(" update blood_bank.t_donor_screening_details set exported_to_bag_collection = true "
					+ " where donor_id="+bloodBagCollectionDto.getDonorId())
					.executeUpdate();
			
			if(count>0) {
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, bloodBagCollectionDto);
			}
			else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}catch (HibernateException he) {
			logger.error("updateScreeningStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateScreeningStatus failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	
}
