package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ChecklistMasterDetailsDto;
import com.param.otims.model.ChecklistMasterDetails;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class ChecklistMasterDetailsDaoImpl extends GenericDao<ChecklistMasterDetails, Integer> implements IChecklistMasterDetailsDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ChecklistMasterDetailsDaoImpl.class);

	public ChecklistMasterDetailsDaoImpl() {
		super(ChecklistMasterDetails.class);
	}

	@Override
	public Response saveChecklistMasterDetails(List<ChecklistMasterDetailsDto> checklistMasterDetails, Integer checklistMasterId)
			throws ApplicationException {
		try {
			for (ChecklistMasterDetailsDto checklistMasterDetailsDto : checklistMasterDetails) {
					
					ChecklistMasterDetails mstr = new ChecklistMasterDetails();
					mstr.setChecklistMasterId(checklistMasterId);
					mstr.setCreatedBy(checklistMasterDetailsDto.getCreatedBy());
					mstr.setCreatedDate(GlobalCommonDateUtils.getDate(checklistMasterDetailsDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
					mstr.setOrganizationId(checklistMasterDetailsDto.getOrganizationId());
					mstr.setStatus(checklistMasterDetailsDto.getStatus());
					mstr.setUnitId(checklistMasterDetailsDto.getUnitId());
					mstr.setUpdatedBy(checklistMasterDetailsDto.getUpdatedBy());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(checklistMasterDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
					mstr.setChecklistItemMasterId(checklistMasterDetailsDto.getChecklistItemMasterId());
					save(mstr);
				}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateChecklistMasterDetails(Integer checklistMasterId) throws ApplicationException {
	try {
		Session session=sessionFactory.getCurrentSession();
		Integer count=session.createSQLQuery(" update otims.m_checklist_master_details set status = 'I' "
				+ " where checklist_master_id="+checklistMasterId)
				.executeUpdate();
		
		   return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getChecklistMasterDetailsByMasterId(ChecklistMasterDetailsDto dto) throws ApplicationException {
		try {
			List<ChecklistMasterDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_MASTER_DETAILS_LIST_BY_MASTER_ID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("checklistMasterId", dto.getChecklistMasterId())
					.setResultTransformer(Transformers.aliasToBean(ChecklistMasterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
