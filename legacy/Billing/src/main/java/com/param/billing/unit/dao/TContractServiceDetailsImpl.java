package com.param.billing.unit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.global.model.TContractServiceDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes" })
public class TContractServiceDetailsImpl extends GenericDao<TContractServiceDetails, Integer>
		implements ITContractServiceDetailsDao, ICommonConstants, IError {

	final static Logger logger = Logger.getLogger(TContractServiceDetailsImpl.class);

	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	HikariDataSource driverManager;

	public TContractServiceDetailsImpl() {
		super(TContractServiceDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveContractServiceDetails(List<TContractServiceDetailsDto> masterDtoList)
			throws ApplicationException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {

			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
			 statement = connection.prepareStatement("INSERT INTO service.t_contract_service_details "
					+ "(org_id, unit_id, contract_id, service_price, final_price, service_id, credit_payable, self_payable, created_by, created_date,updated_by,updated_date,apportioned_price,is_package,package_master_id,billing_bed_category_id,patient_category_id,tax_id,tax_percentage)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)");
			int i = 0;
			for (TContractServiceDetailsDto dto : masterDtoList) 
			{
				if(dto.getIsPackage()==null)
					dto.setIsPackage('N');
				
				statement.setInt(1, dto.getOrgId());
				statement.setInt(2, dto.getUnitId());
				statement.setInt(3, dto.getContractId());
				statement.setDouble(4, dto.getServicePrice());
				statement.setDouble(5, dto.getFinalPrice());
				statement.setInt(6, dto.getServiceId());
				statement.setDouble(7, dto.getCreditPayable());
				statement.setDouble(8, dto.getSelfPayable());
				statement.setInt(9, dto.getCreatedBy());
				statement.setDate(10,new java.sql.Date(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd").getTime()));
				statement.setInt(11, dto.getCreatedBy());
				statement.setDate(12,new java.sql.Date(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd").getTime()));
				statement.setDouble(13, dto.getApportionedPrice());
				statement.setBoolean(14,dto.getIsPackage()!=null ? (dto.getIsPackage()=='Y' ? true : false) : false);
				statement.setInt(15, dto.getPackageMasterId()!=null ? dto.getPackageMasterId() : 0);
				statement.setInt(16, dto.getBillingBedCategoryId() !=null ? dto.getBillingBedCategoryId() : 0);
				statement.setInt(17, dto.getPatientCategoryId() !=null ? dto.getPatientCategoryId() : 0);
				statement.setInt(18, dto.getTaxId() !=null ? dto.getTaxId() :0);
				statement.setDouble(19, dto.getTaxPercentage() !=null ? dto.getTaxPercentage() : 0);
				statement.addBatch();
				i++;
				if (i % 200 == 0 || i == masterDtoList.size()) {
					statement.executeBatch();
					// Execute every 200 items.
					connection.commit();
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (Exception e) {
				
			}

		}
	}

}
