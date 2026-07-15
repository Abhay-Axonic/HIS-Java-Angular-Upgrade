package com.param.billing.unit.dao;

import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dto.TContractProcedureDetailsDto;
import com.param.service.global.model.ContractProcedureDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes"})
public class TContractProcedureDetailsDaoImpl extends GenericDao<ContractProcedureDetails, Integer> implements ITContractProcedureDetailsDao,ICommonConstants, IError{

	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public TContractProcedureDetailsDaoImpl() {
		super(ContractProcedureDetails.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Response saveContractProcedureDetails(TContractProcedureDetailsDto masterDto)
			throws ApplicationException {
		try{
			ContractProcedureDetails contractProcedureDetails = new ContractProcedureDetails();
				contractProcedureDetails.setCreatedBy(masterDto.getCreatedBy());
				contractProcedureDetails.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()));
				contractProcedureDetails.setUpdatedBy(masterDto.getCreatedBy());
				contractProcedureDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()));
				contractProcedureDetails.setUnitId(masterDto.getUnitId());
				contractProcedureDetails.setOrgId(masterDto.getOrgId());
				contractProcedureDetails.setDepartmentId(masterDto.getDepartmentId());
				contractProcedureDetails.setContractId(masterDto.getContractId());
				contractProcedureDetails.setProcedureId(masterDto.getProcedureId());
				contractProcedureDetails.setCapAmount(masterDto.getCapAmount());
				contractProcedureDetails.setStatus('A');
			contractProcedureDetails = save(contractProcedureDetails);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}





	
}
