package com.param.global.org.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.org.model.OrganizationUnitLicenceDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.dto.OrganizationUnitLicenceDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OrganizationUnitLicenceDetailsDaoImpl extends GenericDao<OrganizationUnitLicenceDetails, Integer> implements IOrganizationUnitLicenceDetailsDao ,ICommonConstants{
	final static Logger logger = Logger.getLogger(OrganizationUnitLicenceDetailsDaoImpl.class);


	public OrganizationUnitLicenceDetailsDaoImpl() {
		super(OrganizationUnitLicenceDetails.class);
	}

	
	@Override
	@Transactional
	public Response saveOrganizationUnitLicenceDetails(OrganizationUnitLicenceDetailsDto organizationUnitLicenceDetailsDto)throws ApplicationException {
		try{
			OrganizationUnitLicenceDetails organizationUnitLicenceDetails = new OrganizationUnitLicenceDetails();
			organizationUnitLicenceDetails.setExpiryDate(GlobalCommonDateUtils.getDate(organizationUnitLicenceDetailsDto.getExpiryDate(), "dd-M-yyyy"));
			organizationUnitLicenceDetails.setCreatedBy(1);
			organizationUnitLicenceDetails.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(organizationUnitLicenceDetailsDto.getUnitId()), "dd-M-yyyy"));
			organizationUnitLicenceDetails.setLicenceTypeId(organizationUnitLicenceDetailsDto.getLicenceTypeId());
			organizationUnitLicenceDetails.setLicenceNumber(organizationUnitLicenceDetailsDto.getLicenceNumber());
			organizationUnitLicenceDetails.setIsUnit(organizationUnitLicenceDetailsDto.getIsUnit());
			organizationUnitLicenceDetails.setOrganizationId(organizationUnitLicenceDetailsDto.getOrganizationId());
			organizationUnitLicenceDetails.setUnitId(organizationUnitLicenceDetailsDto.getUnitId());
			if(organizationUnitLicenceDetailsDto.getLicenceDetailsId()!=null) {
				organizationUnitLicenceDetails.setLicenceDetailsId(organizationUnitLicenceDetailsDto.getLicenceDetailsId());
				organizationUnitLicenceDetails = update(organizationUnitLicenceDetails);
			}else {
			organizationUnitLicenceDetails = save(organizationUnitLicenceDetails);
			}
			return new Response<>(SUCCESS, null, null, null, organizationUnitLicenceDetails);
			
		}catch(HibernateException he){
		throw new ApplicationException(he.getMessage(), he);
	}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	public Response getLicenceDetailsByOrgUnitId(OrganizationUnitLicenceDetailsDto organizationUnitLicenceDetailsDto)throws ApplicationException {
	try{
		List<OrganizationUnitLicenceDetailsDto> listLicenceDetailsDtos = null;
		String query = sessionFactory.getCurrentSession().getNamedQuery("GET_LICENCE_DETAILS").getQueryString();

// Using StringBuilder to build query dynamically and securely
		StringBuilder queryBuilder = new StringBuilder(query);

		if (organizationUnitLicenceDetailsDto.getOrganizationId() != null) {
			queryBuilder.append(" WHERE licence.organization_id = :organizationId");
		} else if (organizationUnitLicenceDetailsDto.getUnitId() != null) {
			queryBuilder.append(" WHERE licence.unit_id = :unitId AND licence.is_unit = 'A'");
		}

		Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(queryBuilder.toString());

// Setting parameters securely
		if (organizationUnitLicenceDetailsDto.getOrganizationId() != null) {
			sqlQuery.setParameter("organizationId", organizationUnitLicenceDetailsDto.getOrganizationId());
		} else if (organizationUnitLicenceDetailsDto.getUnitId() != null) {
			sqlQuery.setParameter("unitId", organizationUnitLicenceDetailsDto.getUnitId());
		}

		listLicenceDetailsDtos = sqlQuery.setResultTransformer(Transformers.aliasToBean(OrganizationUnitLicenceDetailsDto.class)).list();

		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listLicenceDetailsDtos, null);
	}catch(Exception e){
	logger.error("getLicenceDetailsByOrgUnitId failed", e);
	}
	return new Response<>(ERROR, null, null, null, null);
	}


	@Override
	public Response updatePreviousRecord(int unitId) throws ApplicationException {
		try {
			int count = sessionFactory.getCurrentSession().createQuery("UPDATE OrganizationUnitLicenceDetails set isUnit='I' where unitId=:unitId ").setParameter("unitId",unitId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);
		}catch(HibernateException he) {
		logger.error("updatePreviousRecord failed", he);
		}catch(Exception e) {
		logger.error("updatePreviousRecord failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

}
