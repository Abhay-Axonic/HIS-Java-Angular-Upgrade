package com.param.global.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.model.CabinMaster;
import com.param.global.model.GlobalServiceMapper;
import com.param.global.model.GlobalServiceMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GlobalServiceMapperServiceDaoImpl extends GenericDao<GlobalServiceMapper, Integer> implements IGlobalServiceMapperServiceDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(GlobalServiceMapperServiceDaoImpl.class);


	public GlobalServiceMapperServiceDaoImpl() {
		super(GlobalServiceMapper.class);
	}
	
	@Autowired
	HikariDataSource driverManager;
	
	@Override
	public Response saveGlobalMasterData(UnitServiceMasterDetailsDto unitServiceMasterDetailsDtotest)
			throws ApplicationException {

		try {
			
			for (UnitServiceMasterDetailsDto unitServiceMasterDetailsDto : unitServiceMasterDetailsDtotest.getGlobalServiceMapperList()) {
				GlobalServiceMapper globalServiceMapper=new GlobalServiceMapper();
			
				Integer serviceId = 0;
				UnitServiceMasterDetailsDto dto = (UnitServiceMasterDetailsDto) sessionFactory.getCurrentSession()
						.createSQLQuery("select sm.service_master_id as \"serviceMasterId\" "
								+ "from service.m_service_master sm where  "
								+ "  sm.organization_id=:organizationId " 
								 + " and   (lower(sm.service_standard_name) LIKE :serviceStandardName "
								 + "  or  lower(sm.service_standard_code) LIKE :serviceStandardName ) limit 1")
						.setParameter("organizationId", unitServiceMasterDetailsDto.getOrganizationId())
						.setParameter("serviceStandardName",'%'+unitServiceMasterDetailsDto.getServiceStandardName().toLowerCase()+'%')
						.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).uniqueResult();
				if(dto != null) {
				globalServiceMapper.setServiceMasterId(dto.getServiceMasterId() != null ? dto.getServiceMasterId():null);
				}
				globalServiceMapper.setServiceStandardName(unitServiceMasterDetailsDto.getServiceStandardName());
				globalServiceMapper.setServiceStandardCode(unitServiceMasterDetailsDto.getServiceStandardCode());
				globalServiceMapper.setSpecialityName(unitServiceMasterDetailsDto.getSpecialityName());
				globalServiceMapper.setSpecialityId(unitServiceMasterDetailsDto.getSpecialityId() != null ? unitServiceMasterDetailsDto.getSpecialityId() :null);
				globalServiceMapper.setSubSpecialityName(unitServiceMasterDetailsDto.getSubSpecialityName());
				globalServiceMapper.setSubSpecialityId(unitServiceMasterDetailsDto.getSubSpecialityId() !=null ? unitServiceMasterDetailsDto.getSubSpecialityId() :null);
				globalServiceMapper.setBillingBedCategoryId(unitServiceMasterDetailsDto.getBillingBedCategoryId());
				globalServiceMapper.setBillingBedDesc(unitServiceMasterDetailsDto.getBillingBedDesc());
				globalServiceMapper.setRate(unitServiceMasterDetailsDto.getRate());
				globalServiceMapper.setTariffId(unitServiceMasterDetailsDto.getTariffId() != null ? unitServiceMasterDetailsDto.getTariffId() :null);
				globalServiceMapper.setIsOutsource(unitServiceMasterDetailsDto.getIsOutsource());
				globalServiceMapper.setIsOutsourceJob(unitServiceMasterDetailsDto.getIsOutsourceJob());
				globalServiceMapper.setIsPackage(unitServiceMasterDetailsDto.getIsPackage());
				globalServiceMapper.setIsRateEditable(unitServiceMasterDetailsDto.getIsRateEditable());
				globalServiceMapper.setRateEditableMaxValue(unitServiceMasterDetailsDto.getRateEditableMaxValue());
				globalServiceMapper.setRateEditbleMinValue(unitServiceMasterDetailsDto.getRateEditbleMinValue());
				globalServiceMapper.setIsTaxApplicable(unitServiceMasterDetailsDto.getIsTaxApplicable());
				globalServiceMapper.setIsRefDoctorShare(unitServiceMasterDetailsDto.getIsRefDoctorShare());
				globalServiceMapper.setIsDiscount(unitServiceMasterDetailsDto.getIsDiscount());
				globalServiceMapper.setDiscountValue(unitServiceMasterDetailsDto.getDiscountValue());
				globalServiceMapper.setGstTypeId(unitServiceMasterDetailsDto.getGstTypeId());
				globalServiceMapper.setOtcGstTypeId(unitServiceMasterDetailsDto.getOtcGstTypeId());
				globalServiceMapper.setOrganizationId(unitServiceMasterDetailsDto.getOrganizationId());
				globalServiceMapper.setUnitId(unitServiceMasterDetailsDto.getUnitId());
				globalServiceMapper.setCreatedBy(unitServiceMasterDetailsDto.getCreatedBy());
				globalServiceMapper.setUpdatedBy(unitServiceMasterDetailsDto.getUpdatedBy());
				globalServiceMapper.setCreatedDate(TimeZoneComponent.getDateByzone(unitServiceMasterDetailsDto.getUnitId()).getTime());
				globalServiceMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitServiceMasterDetailsDto.getUnitId()).getTime());
				globalServiceMapper.setStatus('A');
				globalServiceMapper.setIsPanel(unitServiceMasterDetailsDto.getIsPanel());
				globalServiceMapper.setIsQuantityEditable(unitServiceMasterDetailsDto.getIsQuantityEditable());
				globalServiceMapper.setIsProcedure(unitServiceMasterDetailsDto.getIsProcedure());
				globalServiceMapper.setIsDocReq(unitServiceMasterDetailsDto.getIsDocReq());
				globalServiceMapper.setIsBedSide(unitServiceMasterDetailsDto.getIsBedSide());
				globalServiceMapper.setIsPoc(unitServiceMasterDetailsDto.getIsPoc());
				globalServiceMapper.setIsPeriodicity(unitServiceMasterDetailsDto.getIsPeriodicity());
				globalServiceMapper.setIsAutoRender(unitServiceMasterDetailsDto.getIsAutoRender());
				globalServiceMapper.setIsAppointable(unitServiceMasterDetailsDto.getIsAppointable());
				globalServiceMapper.setIsPacsReq(unitServiceMasterDetailsDto.getIsPacsReq());
				globalServiceMapper.setIsReviewApplicable(unitServiceMasterDetailsDto.getIsReviewApplicable());
				globalServiceMapper.setRefShareAmt(unitServiceMasterDetailsDto.getRefShareAmt());
				//globalServiceMapper.setRefSharePer(unitServiceMasterDetailsDto.getRefSharePer());
				globalServiceMapper.setBillOfMaterial(unitServiceMasterDetailsDto.getBillOfMaterial());
				//globalServiceMapper.setDocRefPer(unitServiceMasterDetailsDto.getDocRefPer());
				globalServiceMapper.setIsCyclicFlag(unitServiceMasterDetailsDto.getIsCyclicFlag());
				globalServiceMapper.setSampleCollection(unitServiceMasterDetailsDto.getSampleCollection());
				globalServiceMapper.setIsDocSharePerWise(unitServiceMasterDetailsDto.getIsDocSharePerWise());
				globalServiceMapper.setIsRefSharePerWise(unitServiceMasterDetailsDto.getIsRefSharePerWise());
				globalServiceMapper.setPreRequisite(unitServiceMasterDetailsDto.getPreRequisite());
				globalServiceMapper.setPaymentEntitlementId(unitServiceMasterDetailsDto.getPaymentEntitlementId());
				globalServiceMapper.setVisitTypeId(unitServiceMasterDetailsDto.getVisitTypeId());
				globalServiceMapper.setPatientTypeId(unitServiceMasterDetailsDto.getPatientTypeId());
				save(globalServiceMapper);
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		}catch (HibernateException he) {
		logger.error("saveGlobalMasterData failed", he);
		} catch (Exception e) {
		logger.error("saveGlobalMasterData failed", e);
		}
		
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response callProcedure(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {

		try {
			
			Connection conn = null;
			conn = driverManager.getConnection();
            Statement stmt = conn.createStatement();
	         String sql = "CALL public.insert_global_service_data()";
	         stmt = conn.createStatement();
	         stmt.executeUpdate(sql);
	         conn.close();
	         
			
			Connection connectionNew = null;
			connectionNew = driverManager.getConnection();
			Statement statementNew = connectionNew.createStatement();
			String sqlNew ="truncate table service.t_global_service_mapper";
			statementNew = connectionNew.createStatement();
			statementNew.executeUpdate(sqlNew);
			connectionNew.close();
            
			

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		}catch (HibernateException he) {
		logger.error("callProcedure failed", he);
		} catch (Exception e) {
		logger.error("callProcedure failed", e);
		}
		
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
