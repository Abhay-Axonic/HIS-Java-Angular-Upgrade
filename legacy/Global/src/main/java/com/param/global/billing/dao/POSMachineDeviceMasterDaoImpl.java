package com.param.global.billing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.POSMachineDeviceMaster;
import com.param.global.billing.dto.POSMachineDeviceMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class POSMachineDeviceMasterDaoImpl extends GenericDao<POSMachineDeviceMaster, Integer> implements IPOSMachineDeviceMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(POSMachineDeviceMasterDaoImpl.class);

	public POSMachineDeviceMasterDaoImpl() {
		super(POSMachineDeviceMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getPOSMachineDeviceMasterList(POSMachineDeviceMasterDto pOSMachineDeviceMasterDto) {
		try {
			List<POSMachineDeviceMasterDto> list = sessionFactory.getCurrentSession()

					.createSQLQuery("select pos.pos_machine_device_id as \"posMachineDeviceId\","
							+ " pos.device_name as \"deviceName\", "
							+ " pos.upload_link as \"uploadLink\", "
							+ " pos.machine_config_details as \"machineConfigDetails\", "
							+ " pos.organization_id as \"organizationId\" "
							+ " from billing.m_pos_machine_device_master pos" 
							+ " where pos.status='A'  and pos.organization_id=:organizationId"
							+ " ORDER BY pos.device_name ASC")
					.setParameter("" + "organizationId", pOSMachineDeviceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(POSMachineDeviceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		} catch (HibernateException he) {
			logger.error("getPOSMachineDeviceMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPOSMachineDeviceMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
