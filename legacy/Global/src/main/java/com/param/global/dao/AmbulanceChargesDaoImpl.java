package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AmbulanceChargesDto;
import com.param.global.model.AmbulanceCharges;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AmbulanceChargesDaoImpl extends GenericDao<AmbulanceCharges, Integer>
		implements IAmbulanceChargesDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(AmbulanceChargesDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public AmbulanceChargesDaoImpl() {
		super(AmbulanceCharges.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			
			AmbulanceCharges ambulanceCharges =new AmbulanceCharges();
			ambulanceCharges.setPatientId(ambulanceChargesDto.getPatientId());
			ambulanceCharges.setBillNo(ambulanceChargesDto.getBillNo());
			ambulanceCharges.setFromAddress(ambulanceChargesDto.getFromAddress());
			ambulanceCharges.setToAddress(ambulanceChargesDto.getToAddress());
			ambulanceCharges.setAmbulanceType(ambulanceChargesDto.getAmbulanceType());
			ambulanceCharges.setDistance(ambulanceChargesDto.getDistance());
			ambulanceCharges.setAmbulanceProvider(ambulanceChargesDto.getAmbulanceProvider());
			ambulanceCharges.setVehicleNo(ambulanceChargesDto.getVehicleNo());
			ambulanceCharges.setOutSourceVenderName(ambulanceChargesDto.getOutSourceVenderName());
			ambulanceCharges.setLpoNo(ambulanceChargesDto.getLpoNo());
			ambulanceCharges.setLpoDate(ambulanceChargesDto.getLpoDate());
			ambulanceCharges.setLpoVenderName(ambulanceChargesDto.getLpoVenderName());
			ambulanceCharges.setLpoVenderAddress(ambulanceChargesDto.getLpoVenderAddress());
			ambulanceCharges.setLpoAmbulanceProvider(ambulanceChargesDto.getLpoAmbulanceProvider());
			ambulanceCharges.setLpoFromLocation(ambulanceChargesDto.getLpoFromLocation());
			ambulanceCharges.setLpoToLocation(ambulanceChargesDto.getLpoToLocation());
			ambulanceCharges.setLpoDistance(ambulanceChargesDto.getLpoDistance());
			ambulanceCharges.setLpoAmount(ambulanceChargesDto.getLpoAmount());
			ambulanceCharges.setStatus(ambulanceChargesDto.getStatus());
			ambulanceCharges.setOrganizationId(ambulanceChargesDto.getOrganizationId());
			ambulanceCharges.setCreatedBy(ambulanceChargesDto.getCreatedBy());
			ambulanceCharges.setCreatedDate(ambulanceChargesDto.getCreatedDate().toString());
			ambulanceCharges.setUpdatedBy(ambulanceChargesDto.getUpdatedBy());
			ambulanceCharges.setUpdatedDate(ambulanceChargesDto.getUpdatedDate().toString());
			ambulanceCharges.setOrderDetailsId(ambulanceChargesDto.getOrderDetailsId());
		     sessionFactory.getCurrentSession().save(ambulanceCharges);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveAmbulanceCharges failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveAmbulanceCharges failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getAmbulanceChargesList(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException{
		
		try{
			List<AmbulanceChargesDto> ambulanceMasterDataList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_CHARGES_LIST")
					.setParameter("organizationId", ambulanceChargesDto.getOrganizationId())
					.setFirstResult(ambulanceChargesDto.getOffset() != null ? ambulanceChargesDto.getOffset() : 0)
					.setMaxResults(ambulanceChargesDto.getNoOfRecordsPerPage() != null ? ambulanceChargesDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceChargesDto.class)).list();
			
			List<AmbulanceChargesDto> ambulanceMasterDataList1 = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_CHARGES_LIST")
					.setParameter("organizationId", ambulanceChargesDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceChargesDto.class)).list();
			
			Integer count = 0;
			if(ambulanceMasterDataList1 != null && ambulanceMasterDataList1.size() > 0) {
				count = ambulanceMasterDataList1.size();
			}
					
			return new Response(SUCCESS, null, null, ambulanceMasterDataList, count);

		} catch (HibernateException he) {
			logger.error("getAmbulanceChargesList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAmbulanceChargesList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAmbulanceChargesActiveList(AmbulanceChargesDto ambulanceChargesDto)
			throws ApplicationException {
		try {
			List<AmbulanceChargesDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_AMBULANCE_MASTER_LIST")
					.setParameter("organizationId", ambulanceChargesDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceCharges.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
			logger.error("getAmbulanceChargesActiveList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAmbulanceChargesActiveList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			 ;
		}
	}

	@Override
	public Response getSingleAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			List<AmbulanceChargesDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_CHARGES_SINGLE_REC")
					.setParameter("organizationId", ambulanceChargesDto.getOrganizationId())
					.setParameter("ambulanceChargesId", ambulanceChargesDto.getAmbulanceChargesId())
					.setFirstResult(ambulanceChargesDto.getOffset() != null ? ambulanceChargesDto.getOffset() : 0)
					.setMaxResults(ambulanceChargesDto.getNoOfRecordsPerPage() != null ? ambulanceChargesDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceChargesDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
			logger.error("getSingleAmbulanceCharges failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getSingleAmbulanceCharges failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			 ;
		}
	}

	@Override
	public Response updateAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {
			AmbulanceCharges ambulanceCharges = findById(ambulanceChargesDto.getAmbulanceChargesId());
			
			ambulanceCharges.setAmbulanceType(ambulanceChargesDto.getAmbulanceType());
			ambulanceCharges.setDistance(ambulanceChargesDto.getDistance());
			ambulanceCharges.setAmbulanceProvider(ambulanceChargesDto.getAmbulanceProvider());
			ambulanceCharges.setFromAddress(ambulanceChargesDto.getFromAddress());
			ambulanceCharges.setToAddress(ambulanceChargesDto.getToAddress());
			ambulanceCharges.setLpoFromLocation(ambulanceChargesDto.getLpoFromLocation());
			ambulanceCharges.setLpoToLocation(ambulanceChargesDto.getLpoToLocation());
			ambulanceCharges.setLpoNo(ambulanceChargesDto.getLpoNo());
			ambulanceCharges.setLpoDate(ambulanceChargesDto.getLpoDate());
			ambulanceCharges.setLpoVenderName(ambulanceChargesDto.getLpoVenderName());
			ambulanceCharges.setLpoVenderAddress(ambulanceChargesDto.getLpoVenderAddress());
			ambulanceCharges.setLpoAmbulanceProvider(ambulanceChargesDto.getLpoAmbulanceProvider());
			ambulanceCharges.setLpoDistance(ambulanceChargesDto.getLpoDistance());
			ambulanceCharges.setLpoAmount(ambulanceChargesDto.getLpoAmount());
			ambulanceCharges.setOutSourceVenderName(ambulanceChargesDto.getOutSourceVenderName());
			ambulanceCharges.setVehicleNo(ambulanceChargesDto.getVehicleNo());
			ambulanceCharges.setUpdatedBy(ambulanceChargesDto.getUpdatedBy());
			ambulanceCharges.setUpdatedDate(ambulanceChargesDto.getUpdatedDate());
			
			sessionFactory.getCurrentSession().update(ambulanceCharges);
			return new Response<AmbulanceChargesDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateAmbulanceCharges failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateAmbulanceCharges failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			 ;
		}
	}

	@Override
	public Response changeAmbulanceChargesStatus(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {

			AmbulanceCharges ambulanceCharges = findById(ambulanceChargesDto.getAmbulanceChargesId());
			ambulanceCharges.setUpdatedBy(ambulanceChargesDto.getUpdatedBy());
			ambulanceCharges.setStatus(ambulanceChargesDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			ambulanceCharges.setUpdatedDate(ambulanceChargesDto.getUpdatedDate());
			update(ambulanceCharges);
			return new Response<AmbulanceChargesDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("changeAmbulanceChargesStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("changeAmbulanceChargesStatus failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
			 ;
		}
	}

	@Override
	public Response cancelOrder(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException {
		try {	
		
			sessionFactory.getCurrentSession().createQuery(" UPDATE OrderDetailsMaster SET ordCancelled='Y' , status = 'I' "
    		+ " WHERE orderDetailsId=:orderDetailsId")
			.setParameter("orderDetailsId", ambulanceChargesDto.getOrderDetailsId())
			.executeUpdate();
			
			sessionFactory.getCurrentSession().createQuery(" UPDATE AmbulanceCharges SET  status = 'I' "
		    		+ " WHERE orderDetailsId=:orderDetailsId")
					.setParameter("orderDetailsId", ambulanceChargesDto.getOrderDetailsId())
					.executeUpdate();
		
		
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	}catch(Exception e) {
		logger.error("cancelOrder failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	

}
