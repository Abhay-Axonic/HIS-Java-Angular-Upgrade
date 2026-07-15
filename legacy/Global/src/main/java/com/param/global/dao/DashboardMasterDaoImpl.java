package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DashboardMasterDto;
import com.param.global.dto.DashboardMasterDto;
import com.param.global.model.DashboardMaster;
import com.param.global.model.IntakeOutputChartMaster;
import com.param.global.unit.dto.UserRoleMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class DashboardMasterDaoImpl extends GenericDao<DashboardMaster, Integer> implements IDashboardMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(DashboardMasterDaoImpl.class);


	public DashboardMasterDaoImpl() {
		super(DashboardMaster.class);
	}
	
	@Autowired
		SessionFactory sessionFactory;

	@Override
	public Response getDashboardMasterList(DashboardMasterDto dashboardMasterDto) {
		try {
			List<DashboardMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DASHBOARD_MASTER_LIST")
					//.setParameter("organizationId",  dashboardMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DashboardMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			} catch (HibernateException he) {
			logger.error("getDashboardMasterList failed", he);
			} catch (Exception e) {
			logger.error("getDashboardMasterList failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getDashboardMasterListByRoleId(DashboardMasterDto dashboardMasterDto) throws ApplicationException {
		try {
			List<UserRoleMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DASHBOARD_MASTER_LIST_BY_ROLE_ID")
//					.setParameterList("roleIdList", dashboardMasterDto.getRoleIdList())
					.setParameter("orgId",  dashboardMasterDto.getOrganizationId())
					.setParameter("unitId",  dashboardMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			} catch (HibernateException he) {
			logger.error("getDashboardMasterListByRoleId failed", he);
			} catch (Exception e) {
			logger.error("getDashboardMasterListByRoleId failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	

}
