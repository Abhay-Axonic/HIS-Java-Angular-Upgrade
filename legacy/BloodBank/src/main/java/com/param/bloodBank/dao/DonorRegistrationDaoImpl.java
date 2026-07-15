package com.param.bloodBank.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.DonorRegistrationDto;
import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.bloodBank.model.DonorDetail;
import com.param.bloodBank.model.DonorRegistrationMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.DonorFingerPrintInterface;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class DonorRegistrationDaoImpl extends GenericDao<DonorRegistrationMaster, Integer> implements IDonorRegistrationDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorRegistrationDaoImpl.class);

	
	public DonorRegistrationDaoImpl() {
		super(DonorRegistrationMaster.class);
	}

	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response checkUniqueDonor(DonorRegistrationDto donorRegistrationDto) {
		try {
			List<DonorRegistrationDto> getDonorDetaisList;
			
			getDonorDetaisList = sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_UNIQUE_DONOR")
						.setParameter("orgId", donorRegistrationDto.getOrganizationId())
						.setParameter("unitId", donorRegistrationDto.getUnitId())
						.setParameter("genderId", donorRegistrationDto.getGenderId())
						.setParameter("dob", GlobalCommonDateUtils.getDate(donorRegistrationDto.getDob(),"dd-M-yyyy"))
						.setParameter("mobileNumber", donorRegistrationDto.getMobileNumber())
						.setResultTransformer(Transformers.aliasToBean(DonorRegistrationDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, getDonorDetaisList, null);
			
		}catch (HibernateException he) {
			logger.error("checkUniqueDonor failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("checkUniqueDonor failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDonorRegistration(DonorRegistrationDto donorRegistrationDto) {
		try{	
			Session session=sessionFactory.getCurrentSession();
					DonorRegistrationMaster donorRegistrationMaster = mapper.map(donorRegistrationDto,DonorRegistrationMaster.class,
								"DonorRegistrationDto_to_DonorRegistrationMaster");
					donorRegistrationMaster.setStatus('A');
					donorRegistrationDto.setDonorId((Integer) session.save(donorRegistrationMaster));

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, donorRegistrationDto);
		}catch (HibernateException he) {
			logger.error("saveDonorRegistration failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveDonorRegistration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDonorDetails(DonorRegistrationDto donorRegistrationDto) {
		try{	
			Session session=sessionFactory.getCurrentSession();
			//Transaction tr=session.beginTransaction();
				DonorDetail donorDetail = mapper.map(donorRegistrationDto,DonorDetail.class,
						"DonorRegistrationDto_to_DonorDetails");
			donorDetail.setStatus('A');
			session.save(donorDetail);
			//tr.commit();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
			logger.error("saveDonorDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveDonorDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonor(DonorRegistrationDto donorRegistrationDto) {
		try{
			Session session=sessionFactory.getCurrentSession();
				
				DonorRegistrationMaster donorRegistrationMaster = mapper.map(donorRegistrationDto,DonorRegistrationMaster.class,
						"DonorRegistrationDto_to_DonorRegistrationMaster");
			donorRegistrationMaster.setStatus('A');
				session.update(donorRegistrationMaster);
				
				DonorDetail donorDetail = mapper.map(donorRegistrationDto,DonorDetail.class,
							"DonorRegistrationDto_to_DonorDetails");
				donorDetail.setDonorDetailsId(donorRegistrationDto.getDonorDetailsId());
			donorDetail.setStatus('A');
				session.update(donorDetail);
				
			return new Response(SUCCESS, donorRegistrationMaster.getDonorNumber(), "Donor "+ donorRegistrationMaster.getDonorNumber()+" "+ COMMON_UPDATE, null, donorRegistrationMaster.getDonorId());
			} catch (HibernateException he) {
				logger.error("updateDonor failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateDonor failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response donorSearchNormalByDonorId(Integer donorId, Integer organizationId, Integer unitId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder(session.getNamedQuery("GET_DONOR_DATA_FOR_UPDATE").getQueryString());
			//stringQuery.append(" AND patient.registrationTypeId!=2");
			stringQuery.append(" AND donor.donorId=:donorId ");
			Query actualQuery = session.createQuery(stringQuery.toString());
			 actualQuery.setParameter("orgId", organizationId)
					.setParameter("unitId",unitId);
			actualQuery.setParameter("donorId",donorId);

			List<DonorRegistrationDto> donorRegistrationsList =	actualQuery.setResultTransformer(Transformers.aliasToBean(DonorRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, donorRegistrationsList, null);

		} catch (HibernateException he) {
			logger.error("donorSearchNormalByDonorId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("donorSearchNormalByDonorId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonorImagePath(DonorRegistrationDto donorRegistrationDto) {
		 try{
		      Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_DONOR_IMAGE_PATH")
		          .setParameter("orgId", donorRegistrationDto.getOrganizationId())
		          .setParameter("unitId", donorRegistrationDto.getUnitId())
		          .setParameter("donorId", donorRegistrationDto.getDonorId())
		          .setParameter("imagePath", donorRegistrationDto.getImagePath()).executeUpdate();
		      if(result != null){
		          return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		      }
		              
		  }catch(HibernateException he){
		  	logger.error("updateDonorImagePath failed", he);
		  	throw new ServiceException(he.getMessage());
		  }catch(Exception e){
		  	logger.error("updateDonorImagePath failed", e);
		  	throw new ServiceException(e.getMessage());
		  }
		  return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDonorDetails(DonorRegistrationDto donorRegistrationDto) {
		try {
			List<DonorRegistrationDto> getPatientDetaisList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DONOR_DETAILS")
					.setParameter("orgId", donorRegistrationDto.getOrganizationId())
					.setParameter("unitId", donorRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DonorRegistrationDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, getPatientDetaisList, null);
		}catch (HibernateException he) {
			logger.error("getDonorDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDonorDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response donorSearchNormal(DonorRegistrationDto donorRegistrationDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder stringQuery=new StringBuilder(session.getNamedQuery("GET_DONOR_DATA").getQueryString());
			//stringQuery.append(" AND patient.registrationTypeId IN (1,3,4,6) ");
			StringBuilder resQuery = this.generateQueryForDonorSearch(stringQuery,donorRegistrationDto);
			Query actualQuery = session.createQuery(resQuery.toString());
			List<DonorRegistrationDto> donorRegistrationsList = actualQuery
					.setParameter("orgId", donorRegistrationDto.getOrganizationId())
					.setParameter("unitId", donorRegistrationDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DonorRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, donorRegistrationsList, null);

		} catch (HibernateException he) {
			logger.error("donorSearchNormal failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("donorSearchNormal failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	private StringBuilder generateQueryForDonorSearch(StringBuilder stringQuery,
			DonorRegistrationDto donorRegistrationDto) {
		try{
			
			/*if(donorRegistrationDto.getDonorNumber() != null && !donorRegistrationDto.getDonorNumber().isEmpty())
				stringQuery.append(" AND lower(donor.donorNumber) LIKE concat('%',LOWER('"+donorRegistrationDto.getDonorNumber()+"'),'%') ");
			
			if(donorRegistrationDto.getDonorName() != null && !donorRegistrationDto.getDonorName().isEmpty())
				stringQuery.append(" AND lower(concat(coalesce(donor.firstName),coalesce(donor.lastName,''))) LIKE concat('%',lower('"+donorRegistrationDto.getDonorName()+"'),'%') ");
			
			if(donorRegistrationDto.getGenderId() != null)
				stringQuery.append(" AND donor.genderId="+donorRegistrationDto.getGenderId()+" ");
			
			if(donorRegistrationDto.getMobileNumber() != null && !donorRegistrationDto.getMobileNumber().isEmpty())
				stringQuery.append(" AND donor.mobileNumber='"+donorRegistrationDto.getMobileNumber()+"' ");
			
			if(donorRegistrationDto.getDob() != null && !donorRegistrationDto.getDob().isEmpty())
				stringQuery.append(" AND donor.dob='"+GlobalCommonDateUtils.getDate(donorRegistrationDto.getDob(),"dd-M-yyyy")+"' ");
			
			if(donorRegistrationDto.getIdentificationNumber() != null && !donorRegistrationDto.getIdentificationNumber().isEmpty())
				stringQuery.append(" AND donor.identificationNumber='"+donorRegistrationDto.getIdentificationNumber()+"' ");
			
			if(donorRegistrationDto.getIdentificationTypeId() != null)
				stringQuery.append(" AND donor.identificationTypeId='"+donorRegistrationDto.getIdentificationTypeId()+"' ");
			
			if(donorRegistrationDto.getFromDate() != null && !donorRegistrationDto.getFromDate().isEmpty() && donorRegistrationDto.getToDate() != null && !donorRegistrationDto.getToDate().isEmpty() )
				stringQuery.append(" AND to_char(donor.createdDate, 'DD-MM-YYYY') BETWEEN '"+donorRegistrationDto.getFromDate()+"' AND '"+donorRegistrationDto.getToDate()+"'");*/

			Map<String, Object> parameters = new HashMap<>();
			if (donorRegistrationDto.getDonorNumber() != null && !donorRegistrationDto.getDonorNumber().isEmpty()) {
				stringQuery.append(" AND lower(donor.donorNumber) LIKE concat('%', :donorNumber, '%') ");
				parameters.put("donorNumber", donorRegistrationDto.getDonorNumber().toLowerCase());
			}

			if (donorRegistrationDto.getDonorName() != null && !donorRegistrationDto.getDonorName().isEmpty()) {
				stringQuery.append(" AND lower(concat(coalesce(donor.firstName, ''), coalesce(donor.lastName, ''))) LIKE concat('%', :donorName, '%') ");
				parameters.put("donorName", donorRegistrationDto.getDonorName().toLowerCase());
			}

			if (donorRegistrationDto.getGenderId() != null) {
				stringQuery.append(" AND donor.genderId = :genderId ");
				parameters.put("genderId", donorRegistrationDto.getGenderId());
			}

			if (donorRegistrationDto.getMobileNumber() != null && !donorRegistrationDto.getMobileNumber().isEmpty()) {
				stringQuery.append(" AND donor.mobileNumber = :mobileNumber ");
				parameters.put("mobileNumber", donorRegistrationDto.getMobileNumber());
			}

			if (donorRegistrationDto.getDob() != null && !donorRegistrationDto.getDob().isEmpty()) {
				stringQuery.append(" AND donor.dob = :dob ");
				parameters.put("dob", GlobalCommonDateUtils.getDate(donorRegistrationDto.getDob(), "dd-M-yyyy"));
			}

			if (donorRegistrationDto.getIdentificationNumber() != null && !donorRegistrationDto.getIdentificationNumber().isEmpty()) {
				stringQuery.append(" AND donor.identificationNumber = :identificationNumber ");
				parameters.put("identificationNumber", donorRegistrationDto.getIdentificationNumber());
			}

			if (donorRegistrationDto.getIdentificationTypeId() != null) {
				stringQuery.append(" AND donor.identificationTypeId = :identificationTypeId ");
				parameters.put("identificationTypeId", donorRegistrationDto.getIdentificationTypeId());
			}

			if (donorRegistrationDto.getFromDate() != null && !donorRegistrationDto.getFromDate().isEmpty() &&
					donorRegistrationDto.getToDate() != null && !donorRegistrationDto.getToDate().isEmpty()) {
				stringQuery.append(" AND to_char(donor.createdDate, 'DD-MM-YYYY') BETWEEN :fromDate AND :toDate ");
				parameters.put("fromDate", donorRegistrationDto.getFromDate());
				parameters.put("toDate", donorRegistrationDto.getToDate());
			}

			return stringQuery;
		}catch(Exception e){
			logger.error("generateQueryForDonorSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checKPatientAvailability(DonorRegistrationDto donorRegistrationDto) {
		try {
			List<DonorRegistrationDto> getDonorDetaisList;
			
			getDonorDetaisList = sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_UNIQUE_PATIENT_ID")
						.setParameter("orgId", donorRegistrationDto.getOrganizationId())
						.setParameter("unitId", donorRegistrationDto.getUnitId())
						.setParameter("patientId", donorRegistrationDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(DonorRegistrationDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, getDonorDetaisList, null);
			
		}catch (HibernateException he) {
			logger.error("checKPatientAvailability failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("checKPatientAvailability failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveDonorThumb(DonorFingerPrintInterface fingerInterface) {
		try {

			 Integer result = (Integer)sessionFactory.getCurrentSession().save(fingerInterface);
			 return new Response(SUCCESS, SUCCESS_CODE, null, null, result);
			} catch (HibernateException he) {
				logger.error("saveDonorThumb failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveDonorThumb failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Long getNextValDonorId() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery("select nextval('blood_bank.donor_registration_seq')");
				Long id = ((BigInteger) query.uniqueResult()).longValue();

				SQLQuery query2 = session.createSQLQuery("select setval('blood_bank.donor_registration_seq'," + (id-1) + ")");
				query2.uniqueResult();

				return id;
		} catch (HibernateException he) {
			logger.error("getNextValDonorId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getNextValDonorId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDonorRegistrationDetails(DonorScreeningDetailDto donorScreeningDetailDto)throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_REGISTRERED_DONOR_BY_EXPORTED_TO_SCREENING_STATUS").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(donorScreeningDetailDto.getFromDate()!=null && donorScreeningDetailDto.getToDate()!=null && donorScreeningDetailDto.getFromDate()!="" && donorScreeningDetailDto.getToDate()!=""){
				subQuery.append(" AND to_char(donor.createdDate, 'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			
			if(donorScreeningDetailDto.getDonorName()!=null && donorScreeningDetailDto.getDonorName()!=""){
				//subQuery.append(" AND lower(concat(coalesce(donor.firstName),coalesce(donor.lastName,''))) LIKE concat('%',lower(:donorName),'%') ");
				subQuery.append(" AND lower(concat(coalesce(donor.firstName, ''), coalesce(donor.lastName, ''))) LIKE :donorName");
			}
			 
			if(donorScreeningDetailDto.getDonorNumber()!=null && donorScreeningDetailDto.getDonorNumber()!=""){
				subQuery.append(" AND donor.donorNumber =:donorNumber");
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
				//actualQuery.setParameter("donorName",donorScreeningDetailDto.getDonorName());
				actualQuery.setParameter("donorName", "%" + donorScreeningDetailDto.getDonorName().toLowerCase() + "%");
			}

			if(donorScreeningDetailDto.getDonorNumber()!=null && donorScreeningDetailDto.getDonorNumber()!=""){
				actualQuery.setParameter("donorNumber",donorScreeningDetailDto.getDonorNumber());
			}

			List<DonorScreeningDetailDto> list = actualQuery.setResultTransformer(Transformers.aliasToBean(DonorScreeningDetailDto.class)).list();
		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("getDonorRegistrationDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
