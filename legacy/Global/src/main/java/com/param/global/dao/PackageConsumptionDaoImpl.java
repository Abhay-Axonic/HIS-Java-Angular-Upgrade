package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.packages.model.TPackageConsumptionCapDetails;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.PackageSearchRequestDto;
import com.param.global.dto.TPackageConsumptionCapDetailsDto;
import com.param.global.dto.UpdatePayeeDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
public class PackageConsumptionDaoImpl extends GenericDao<TPackageConsumptionCapDetails, Integer> implements IPackageConsumptionDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(PackageConsumptionDaoImpl.class);

	public PackageConsumptionDaoImpl() {
		super(TPackageConsumptionCapDetails.class);
	}

	@Override
	public Response getPackageServiceByPackageType(
			PackageSearchRequestDto packageSearchRequestDto)
			throws ApplicationException {

		try{

		}catch(Exception ex){
			logger.error("getPackageServiceByPackageType failed", ex);
			throw new ServiceException(ex.getMessage());
		}
		return null;
	}

	@Override
	public Response getPackageConsumptionCapDetails(
			Integer packageConsumptionMasterId) throws ApplicationException {
		try{
			List<TPackageConsumptionCapDetailsDto> listTPackageCapDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACKAGE_CONSUMPTION_CAP_DETAILS")
					.setParameter("packageConsumptionMasterId", packageConsumptionMasterId)
					.setResultTransformer(Transformers.aliasToBean(TPackageConsumptionCapDetailsDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageCapDetailsDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPackageConsumptionEODetails(Integer EODtlId,
			Integer packageConsumptionMasterId) throws ApplicationException {
		try{
			List<TPackageConsumptionCapDetailsDto> listTPackageCapDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PACKAGE_CONSUMPTION_CAP_DETAILS")
					.setParameter("packageConsumptionMasterId", packageConsumptionMasterId)
					.setResultTransformer(Transformers.aliasToBean(TPackageConsumptionCapDetailsDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTPackageCapDetailsDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response chageOfPayeeInativatePackage(UpdatePayeeDto billingMasterDto,OrderDetailsByEncounterIdDto dto) throws ApplicationException {
		try{
			String query = "update " + 
					"	billing.t_package_consumption_master " + 
					" set " + 
					"	status = 'I' " + 
					" where " + 
					"	package_id =:packageId ";
						StringBuilder q = new StringBuilder(query);
						q.append((billingMasterDto.getEncounterId() != null && billingMasterDto.getEncounterId()>0) ?" and encounter_id =  "+billingMasterDto.getEncounterId() :"");
						q.append((billingMasterDto.getAdmissionId() != null && billingMasterDto.getAdmissionId()>0) ?" and admission_id = "+billingMasterDto.getAdmissionId() :"");
						Integer result = (Integer)sessionFactory.getCurrentSession().createSQLQuery(q.toString()).setParameter("packageId", dto.getPackageId()).uniqueResult();
						return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
					}catch(Exception e){
						
						throw e;
					}
	}

}
