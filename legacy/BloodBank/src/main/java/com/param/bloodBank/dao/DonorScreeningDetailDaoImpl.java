package com.param.bloodBank.dao;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.bloodBank.dto.DonorScreeningMapperDto;
import com.param.bloodBank.model.DonorScreeningDetailMapper;
import com.param.bloodBank.model.DonorScreeningDetailMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class DonorScreeningDetailDaoImpl extends GenericDao<DonorScreeningDetailMaster, Integer> implements IDonorScreeningDetailDao,ICommonConstants {

	final static Logger logger = Logger.getLogger(DonorScreeningDetailDaoImpl.class);


	public DonorScreeningDetailDaoImpl() {
		super(DonorScreeningDetailMaster.class);
	}

	@Autowired
	IBloodGroupMasterDao iBloodGroupMasterDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response saveDonorScreeningDetail(DonorScreeningDetailDto donorScreeningDetailDto)
			throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			DonorScreeningDetailMaster donorScreeningDetailMaster = mapper.map(donorScreeningDetailDto,DonorScreeningDetailMaster.class,
								"DonorScreeningDetailDto_to_DonorScreeningDetailMaster");
			
			if(donorScreeningDetailDto.getDonorScreeningId()!=null && donorScreeningDetailDto.getDonorScreeningId()!=0) {
				 session.update(donorScreeningDetailMaster);
			}
			else {
				donorScreeningDetailDto.setDonorScreeningId((Integer) session.save(donorScreeningDetailMaster));
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, donorScreeningDetailDto);
		}catch (HibernateException he) {
			logger.error("saveDonorScreeningDetail failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveDonorScreeningDetail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDonorScreeningMapper(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException {
		try{	
             List<DonorScreeningMapperDto> donorScreeningMapperlist=donorScreeningDetailDto.getDonorScreeningMapperList();
             Session session=sessionFactory.getCurrentSession();
             
             for(DonorScreeningMapperDto donorScreeningMapperDto:donorScreeningMapperlist) {
				
            	donorScreeningMapperDto.setDonorScreeningDetailId(donorScreeningDetailDto.getDonorScreeningId());
				donorScreeningMapperDto.setDonorId(donorScreeningDetailDto.getDonorId());
				DonorScreeningDetailMapper donorScreeningDetailMapper = new  DonorScreeningDetailMapper();
				donorScreeningDetailMapper.setCreatedBy(donorScreeningMapperDto.getCreatedBy());
				donorScreeningDetailMapper.setCreatedDate(GlobalCommonDateUtils.getDate(donorScreeningMapperDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
				donorScreeningDetailMapper.setUnitId(donorScreeningDetailDto.getUnitId());
				donorScreeningDetailMapper.setOrganizationId(donorScreeningDetailDto.getOrganizationId());
				if(donorScreeningMapperDto.getDonorDeferralTillDate()!=null && (!donorScreeningMapperDto.getParameterResultStatus().equals("N"))) {
				donorScreeningDetailMapper.setDonorDeferralTillDate(GlobalCommonDateUtils.getDate(donorScreeningMapperDto.getDonorDeferralTillDate(),"dd-M-yyyy HH:mm:ss"));
				}
				donorScreeningDetailMapper.setDonorId(donorScreeningDetailDto.getDonorId());
				donorScreeningDetailMapper.setDonorScreeningDetailId(donorScreeningDetailDto.getDonorScreeningId());
				donorScreeningDetailMapper.setDonorScreeningParameterId(donorScreeningMapperDto.getDonorScreeningParameterId());
				donorScreeningDetailMapper.setParameterResultStatus(donorScreeningMapperDto.getParameterResultStatus());
				donorScreeningDetailMapper.setStatus(donorScreeningMapperDto.getStatus());
				donorScreeningDetailMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(donorScreeningMapperDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
				donorScreeningDetailMapper.setUpdatedBy(donorScreeningMapperDto.getUpdatedBy());
				
				if(donorScreeningMapperDto.getDonorScreeningDetailMapperId() !=null ) {
					
					donorScreeningDetailMapper.setDonorScreeningDetailMapperId(donorScreeningMapperDto.getDonorScreeningDetailMapperId());
					session.update(donorScreeningDetailMapper);
				}
				else {
					session.save(donorScreeningDetailMapper);
				}
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			logger.error("saveDonorScreeningMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveDonorScreeningMapper failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonorScreeningDeferralStatus(DonorScreeningDetailDto donorScreeningDetailDto)
			throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery(" update t_donor_screening_details set screening_result_status_id="+donorScreeningDetailDto.getScreeningResultStatusId()+" "
					+ " where donor_id='"+donorScreeningDetailDto.getDonorId()+"' ")
					.executeUpdate();
			
			if(count>0) {
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, donorScreeningDetailDto);
			}
			else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}catch (HibernateException he) {
			logger.error("updateDonorScreeningDeferralStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDonorScreeningDeferralStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDonorScreeningDetailByDonorId(Integer donorId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
				
				DonorScreeningDetailDto donorScreeningDetailDto=(DonorScreeningDetailDto)session.getNamedQuery("GET_DONOR_SCREENING_DETAIL_BY_DONOR_ID")
	 					.setParameter("donorId",donorId)
	 					.setParameter("organizationId",organizationId)
	 					.setParameter("unitId", unitId)
	 					.setResultTransformer(Transformers.aliasToBean(DonorScreeningDetailDto.class)).uniqueResult();
				
				if(donorScreeningDetailDto!=null) {
					List<DonorScreeningMapperDto> donorScreeningMapperDto=session.getNamedQuery("GET_DONOR_SCREENING_MAPPER_LIST_BY_DONOR_SCREENING_DETAIL_ID")
		 					.setParameter("donorScreeningDetailId",donorScreeningDetailDto.getDonorScreeningId())
		 					.setParameter("organizationId",organizationId)
		 					.setParameter("unitId", unitId)
		 					.setResultTransformer(Transformers.aliasToBean(DonorScreeningMapperDto.class)).list();
					
					donorScreeningDetailDto.setDonorScreeningMapperList(donorScreeningMapperDto);
				}
				
			return new Response(SUCCESS, SUCCESS_CODE, null, null, donorScreeningDetailDto);
		}catch (HibernateException he) {
			logger.error("getDonorScreeningDetailByDonorId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDonorScreeningDetailByDonorId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchDonor(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_DONOR_BY_MULIPLE_CRITERIA").getQueryString());
			//System.out.println(donorScreeningDetailDto.getFromDate());
			////System.out.println(GlobalCommonDateUtils.getDate(donorScreeningDetailDto.getFromDate(), "yyyy-M-dd"));
			StringBuffer subQuery = new StringBuffer();
			if(donorScreeningDetailDto.getFromDate()!=null && donorScreeningDetailDto.getToDate()!=null && donorScreeningDetailDto.getFromDate()!="" && donorScreeningDetailDto.getToDate()!=""){
				subQuery.append(" AND to_char(donorScreeningDetailMaster.createdDate , 'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			
			if(donorScreeningDetailDto.getDonorName()!=null && donorScreeningDetailDto.getDonorName()!=""){
				//subQuery.append(" AND lower(concat(coalesce(donorRegi.firstName),coalesce(donorRegi.lastName,''))) LIKE concat('%',lower(:donorName),'%') ");
				subQuery.append(" AND lower(concat(coalesce(donorRegi.firstName, ''), coalesce(donorRegi.lastName, ''))) LIKE :donorName");
			}
			 
			if(donorScreeningDetailDto.getDonorNumber()!=null && donorScreeningDetailDto.getDonorNumber()!=""){
				subQuery.append(" AND donorRegi.donorNumber=:donorNumber ");
			}
		
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery.setParameter("organizationId", donorScreeningDetailDto.getOrganizationId())
					.setParameter("unitId", donorScreeningDetailDto.getUnitId());
			if(donorScreeningDetailDto.getFromDate()!=null && donorScreeningDetailDto.getToDate()!=null && donorScreeningDetailDto.getFromDate()!="" && donorScreeningDetailDto.getToDate()!=""){
				actualQuery.setParameter("fromDate",donorScreeningDetailDto.getFromDate());
				actualQuery.setParameter("toDate",donorScreeningDetailDto.getToDate());
			}

			if(donorScreeningDetailDto.getDonorName()!=null && donorScreeningDetailDto.getDonorName()!=""){
			//	actualQuery.setParameter("donorName",donorScreeningDetailDto.getDonorName());
				actualQuery.setParameter("donorName", "%" + donorScreeningDetailDto.getDonorName().toLowerCase() + "%");
			}

			if(donorScreeningDetailDto.getDonorNumber()!=null && donorScreeningDetailDto.getDonorNumber()!=""){
				actualQuery.setParameter("donorNumber",donorScreeningDetailDto.getDonorNumber());
			}

			List<DonorScreeningDetailDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(DonorScreeningDetailDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("searchDonor failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response registreredDonorSearch(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_REGISTRERED_DONOR_BY_MULIPLE_CRITERIA").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(donorScreeningDetailDto.getFromDate()!=null && donorScreeningDetailDto.getToDate()!=null && donorScreeningDetailDto.getFromDate()!="" && donorScreeningDetailDto.getToDate()!=""){
				subQuery.append(" AND to_char(donorRegi.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			
			if(donorScreeningDetailDto.getDonorName()!=null && donorScreeningDetailDto.getDonorName()!=""){
				//subQuery.append(" AND lower(concat(coalesce(donorRegi.firstName),coalesce(donorRegi.lastName,''))) LIKE concat('%',lower(:donorName),'%') ");
				subQuery.append(" AND lower(concat(coalesce(donorRegi.firstName, ''), coalesce(donorRegi.lastName, ''))) LIKE :donorName");
			}
			 
			if(donorScreeningDetailDto.getDonorNumber()!=null && donorScreeningDetailDto.getDonorNumber()!=""){
				subQuery.append(" AND donorRegi.donorNumber=:donorNumber ");
			}

			query.append(subQuery.toString());

			Query actualQuery = session.createQuery(query.toString());

			// Set static parameters
			actualQuery.setParameter("organizationId", donorScreeningDetailDto.getOrganizationId());
			actualQuery.setParameter("unitId", donorScreeningDetailDto.getUnitId());

			// Set dynamic parameters
			if (donorScreeningDetailDto.getFromDate() != null && donorScreeningDetailDto.getToDate() != null
					&& !donorScreeningDetailDto.getFromDate().isEmpty() && !donorScreeningDetailDto.getToDate().isEmpty()) {
				actualQuery.setParameter("fromDate", donorScreeningDetailDto.getFromDate());
				actualQuery.setParameter("toDate", donorScreeningDetailDto.getToDate());
			}

			if (donorScreeningDetailDto.getDonorName() != null && !donorScreeningDetailDto.getDonorName().isEmpty()) {
				//actualQuery.setParameter("donorName", "%" + donorScreeningDetailDto.getDonorName().toLowerCase() + "%");
				actualQuery.setParameter("donorName", "%" + donorScreeningDetailDto.getDonorName().toLowerCase() + "%");
			}

			if (donorScreeningDetailDto.getDonorNumber() != null && !donorScreeningDetailDto.getDonorNumber().isEmpty()) {
				actualQuery.setParameter("donorNumber", donorScreeningDetailDto.getDonorNumber());
			}

			List<DonorScreeningDetailDto> list = actualQuery
					.setResultTransformer(Transformers.aliasToBean(DonorScreeningDetailDto.class))
					.list();

			List<DonorScreeningDetailDto> finalList = new ArrayList<>();
			if (list != null && !list.isEmpty()) {
				for (DonorScreeningDetailDto dto : list) {
					dto.setScreeningResult("Accepted");
					finalList.add(dto);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
		}catch(Exception e){
			logger.error("registreredDonorSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateScreeningFlag(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery(" update blood_bank.t_donor_registration set exported_to_screening = true "
					+ " where donor_id="+donorScreeningDetailDto.getDonorId()+" ")
					.executeUpdate();
			
			if(count>0) {
			   return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, donorScreeningDetailDto);
			}
			else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}catch (HibernateException he) {
			logger.error("updateScreeningFlag failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateScreeningFlag failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOnlyAcceptedDonorscreening(BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("GET_ACCEPTED_DONOR_SCREENING_DETAILS").getQueryString());
			//System.out.println(bloodBagCollectionDto.getFromDate());
		//	//System.out.println(GlobalCommonDateUtils.getDate(bloodBagCollectionDto.getFromDate(), "yyyy-M-dd"));
			StringBuffer subQuery = new StringBuffer();
			if(bloodBagCollectionDto.getFromDate()!=null && bloodBagCollectionDto.getToDate()!=null && bloodBagCollectionDto.getFromDate()!="" && bloodBagCollectionDto.getToDate()!=""){
				subQuery.append(" AND to_char(donorScreeningDetailMaster.createdDate , 'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			
			if(bloodBagCollectionDto.getDonorName()!=null && bloodBagCollectionDto.getDonorName()!=""){
				//subQuery.append(" AND lower(concat(coalesce(donorRegi.firstName),coalesce(donorRegi.lastName,''))) LIKE concat('%',lower(:donorName),'%') ");
				subQuery.append(" AND lower(concat(coalesce(donorRegi.firstName, ''), coalesce(donorRegi.lastName, ''))) LIKE :donorName");

			}
			 
			if(bloodBagCollectionDto.getDonorNumber()!=null && bloodBagCollectionDto.getDonorNumber()!=""){
				subQuery.append(" AND donorRegi.donorNumber=:donorNumber ");
			}
		
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery.setParameter("organizationId", bloodBagCollectionDto.getOrganizationId())
					.setParameter("unitId", bloodBagCollectionDto.getUnitId());

			if(bloodBagCollectionDto.getFromDate()!=null && bloodBagCollectionDto.getToDate()!=null && bloodBagCollectionDto.getFromDate()!="" && bloodBagCollectionDto.getToDate()!=""){
				actualQuery.setParameter("fromDate",bloodBagCollectionDto.getFromDate());
				actualQuery.setParameter("toDate",bloodBagCollectionDto.getToDate());
			}

			if(bloodBagCollectionDto.getDonorName()!=null && bloodBagCollectionDto.getDonorName()!=""){
				//actualQuery.setParameter("donorName",bloodBagCollectionDto.getDonorName());
				actualQuery.setParameter("donorName", "%" + bloodBagCollectionDto.getDonorName().toLowerCase() + "%");
			}

			if(bloodBagCollectionDto.getDonorNumber()!=null && bloodBagCollectionDto.getDonorNumber()!=""){
				actualQuery.setParameter("donorNumber",bloodBagCollectionDto.getDonorNumber());
			}


			List<BloodBagCollectionDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("getOnlyAcceptedDonorscreening failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getRegistreredAutologousDonor(BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_REGISTRERED_DONOR_BY_MULIPLE_CRITERIA").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(bloodBagCollectionDto.getFromDate()!=null && bloodBagCollectionDto.getToDate()!=null && bloodBagCollectionDto.getFromDate()!="" && bloodBagCollectionDto.getToDate()!=""){
				subQuery.append(" AND to_char(donorRegi.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			
			if(bloodBagCollectionDto.getDonorName()!=null && bloodBagCollectionDto.getDonorName()!=""){
				//subQuery.append(" AND lower(concat(coalesce(donorRegi.firstName),coalesce(donorRegi.lastName,''))) LIKE concat('%',lower(:donorName),'%') ");
				subQuery.append(" AND lower(concat(coalesce(donorRegi.firstName, ''), coalesce(donorRegi.lastName, ''))) LIKE :donorName");
			}
			 
			if(bloodBagCollectionDto.getDonorNumber()!=null && bloodBagCollectionDto.getDonorNumber()!=""){
				subQuery.append(" AND donorRegi.donorNumber=:donorNumber ");
			}

			query.append(subQuery.toString());

			Query actualQuery = session.createQuery(query.toString());

			// Set static parameters
			actualQuery.setParameter("organizationId", bloodBagCollectionDto.getOrganizationId());
			actualQuery.setParameter("unitId", bloodBagCollectionDto.getUnitId());

			// Set dynamic parameters
			if (bloodBagCollectionDto.getFromDate() != null && bloodBagCollectionDto.getToDate() != null
					&& !bloodBagCollectionDto.getFromDate().isEmpty() && !bloodBagCollectionDto.getToDate().isEmpty()) {
				actualQuery.setParameter("fromDate", bloodBagCollectionDto.getFromDate());
				actualQuery.setParameter("toDate", bloodBagCollectionDto.getToDate());
			}

			if (bloodBagCollectionDto.getDonorName() != null && !bloodBagCollectionDto.getDonorName().isEmpty()) {
				//actualQuery.setParameter("donorName", "%" + bloodBagCollectionDto.getDonorName().toLowerCase() + "%");
				actualQuery.setParameter("donorName", "%" + bloodBagCollectionDto.getDonorName().toLowerCase() + "%");
			}

			if (bloodBagCollectionDto.getDonorNumber() != null && !bloodBagCollectionDto.getDonorNumber().isEmpty()) {
				actualQuery.setParameter("donorNumber", bloodBagCollectionDto.getDonorNumber());
			}

			List<BloodBagCollectionDto> list = actualQuery
					.setResultTransformer(Transformers.aliasToBean(BloodBagCollectionDto.class))
					.list();

			List<BloodBagCollectionDto> finalList = new ArrayList<>();
			if (list != null && !list.isEmpty()) {
				for (BloodBagCollectionDto dto : list) {
					dto.setStatusName("Accepted");
					finalList.add(dto);
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
		}catch(Exception e){
			logger.error("getRegistreredAutologousDonor failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	

}
