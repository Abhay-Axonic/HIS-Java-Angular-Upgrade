package com.param.billing.unit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dto.TContractGroupPharmacyDetailsDto;
import com.param.service.global.model.ContractItemDetails;
import com.param.service.global.model.TContractPharmacyExclusion;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes"})
public class TContractGroupPharmacyExclusionDaoImpl extends GenericDao<ContractItemDetails, Integer> implements ITContractGroupPharmacyExclusionDetailsDto,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(TContractGroupPharmacyExclusionDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;
	
	
	public TContractGroupPharmacyExclusionDaoImpl() {
		super(ContractItemDetails.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Response saveContractGroupPharmacyInclusionDetails(
			List<TContractGroupPharmacyDetailsDto> masterDtoList)
			throws ApplicationException {
		Connection connection =null;
		PreparedStatement statement = null;
		try{
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
			 statement = connection.prepareStatement("INSERT INTO service.t_contract_item_details "
	        		+ "(org_id, unit_id, contract_id, item_id, is_equipment, created_by, created_date,updated_by,updated_date,product_category_id)"
	        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?);"); 
			 int i = 0;
			for(TContractGroupPharmacyDetailsDto dto:masterDtoList){
					statement.setInt(1, dto.getOrgId());
					statement.setInt(2, dto.getUnitId());
					statement.setInt(3, dto.getContractId());
					statement.setInt(4, dto.getItemId());
					statement.setString(5, dto.getIsEquipment()+"");
					statement.setInt(6, dto.getCreatedBy());
					statement.setDate(7, new java.sql.Date(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime()));
					statement.setInt(8, dto.getCreatedBy());
					statement.setDate(9, new java.sql.Date(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime()));
					statement.setInt(10, dto.getProductCategoryId());
					statement.addBatch();
				  i++;

		            if (i % 200 == 0 || i == masterDtoList.size()) {
		                statement.executeBatch(); // Execute every 20 items.
		                connection.commit();  
		            }
				
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}finally{
			try{
				if (statement != null) {
					statement.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			}catch(Exception e){
				
			}
		}
	}

	@Override
	public Response saveContractGroupPharmacyExclusionDetails(List<TContractGroupPharmacyDetailsDto> groupPharmacyExclusionDetailsDtoList)
			throws ApplicationException {
		try{
			for(TContractGroupPharmacyDetailsDto groupPharmacyDetailsDto : groupPharmacyExclusionDetailsDtoList)
			{
				TContractPharmacyExclusion contractPharmacyExclusion = new TContractPharmacyExclusion();
					contractPharmacyExclusion.setContractId(groupPharmacyDetailsDto.getContractId());
					contractPharmacyExclusion.setOrgId(groupPharmacyDetailsDto.getOrgId());
					contractPharmacyExclusion.setUnitId(groupPharmacyDetailsDto.getUnitId());
					contractPharmacyExclusion.setProductCategoryId(groupPharmacyDetailsDto.getProductCategoryId());
					contractPharmacyExclusion.setCreatedDate(TimeZoneComponent.getDateByzone(groupPharmacyDetailsDto.getUnitId()));
					contractPharmacyExclusion.setStatus('A');
				sessionFactory.getCurrentSession().save(contractPharmacyExclusion);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException e){
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
}
