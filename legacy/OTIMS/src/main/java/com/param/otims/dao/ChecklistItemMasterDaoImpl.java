package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ChecklistItemMasterDto;
import com.param.global.service.VerifiedIdentityService;
import com.param.otims.model.ChecklistItemMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ChecklistItemMasterDaoImpl extends GenericDao<ChecklistItemMaster, Integer> implements IChecklistItemMasterDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ChecklistItemMasterDaoImpl.class);

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	public ChecklistItemMasterDaoImpl() {
		super(ChecklistItemMaster.class);
	}

	@Override
	public Response saveChecklistItemMaster(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException {
		try {
			
			ChecklistItemMasterDto dto = new ChecklistItemMasterDto();
			if (checklistItemMasterdto.getCode() != null && checklistItemMasterdto.getDescription() != null) {
				if (checklistItemMasterdto.getChecklistItemMasterId() == null)// dilip
				{
					dto = (ChecklistItemMasterDto) sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_ITEM_CODE") 
							.setParameter("organizationId", checklistItemMasterdto.getOrganizationId())
							.setParameter("unitId", checklistItemMasterdto.getUnitId())
							.setParameter("code", checklistItemMasterdto.getCode().toLowerCase())
							.setResultTransformer(Transformers.aliasToBean(ChecklistItemMasterDto.class))
							.uniqueResult();
					
					if(dto!=null)
					{
						return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Code Already Exist", null, null);
					}else {
						dto = (ChecklistItemMasterDto) sessionFactory.getCurrentSession()
								.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLISTITEM_MASTER") // dilip
								.setParameter("organizationId", checklistItemMasterdto.getOrganizationId())
								.setParameter("unitId", checklistItemMasterdto.getUnitId())
								.setParameter("description", checklistItemMasterdto.getDescription().toLowerCase())
								.setResultTransformer(Transformers.aliasToBean(ChecklistItemMasterDto.class))
								.uniqueResult();
						if(dto!=null)
						{
							return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Description Already Exist", null, null);
						}
					}
					
				} else {
					
					dto = (ChecklistItemMasterDto) sessionFactory.getCurrentSession()							
							.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLISTITEM_MASTER_BY_CODE_NOT_ID")
							.setParameter("organizationId", checklistItemMasterdto.getOrganizationId())
							.setParameter("unitId", checklistItemMasterdto.getUnitId())
							.setParameter("checklistItemMasterId", checklistItemMasterdto.getChecklistItemMasterId())
							.setParameter("code", checklistItemMasterdto.getCode().toLowerCase())
							.setResultTransformer(Transformers.aliasToBean(ChecklistItemMasterDto.class))
							.uniqueResult();
					
					if(dto!=null)
					{
						return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Code Already Exist", null, null);
					}else {
						dto = (ChecklistItemMasterDto) sessionFactory.getCurrentSession()							
								.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLISTITEM_MASTER_BY_NAME_NOT_ID")
								.setParameter("organizationId", checklistItemMasterdto.getOrganizationId())
								.setParameter("unitId", checklistItemMasterdto.getUnitId())
								.setParameter("checklistItemMasterId", checklistItemMasterdto.getChecklistItemMasterId())
								.setParameter("description", checklistItemMasterdto.getDescription().toLowerCase())
								.setResultTransformer(Transformers.aliasToBean(ChecklistItemMasterDto.class))
								.uniqueResult();
						
						if(dto!=null)
						{
							return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,"Description Already Exist", null, null);
						}
					}
						
				}
			}
			ChecklistItemMaster mstr = null;
			if (checklistItemMasterdto.getChecklistItemMasterId() != null) {
				// BR-149: cross-tenant IDOR fix -- the update path must verify the existing
				// row belongs to the JWT-verified identity BEFORE mutating, and must never
				// accept a client-supplied org/unit (which allowed a full row theft:
				// org/unit repointed from the victim's tenant to the attacker's).
				mstr = findById(checklistItemMasterdto.getChecklistItemMasterId());
				if (mstr == null || mstr.getOrganizationId() == null
						|| !mstr.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())
						|| mstr.getUnitId() == null
						|| !mstr.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
					return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}
			} else {
				mstr = new ChecklistItemMaster();
				// create binds the JWT-verified org/unit, never a client-supplied value
				mstr.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
				mstr.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			}
			mstr.setCode(checklistItemMasterdto.getCode());
			mstr.setDescription(checklistItemMasterdto.getDescription());
			mstr.setCreatedBy(checklistItemMasterdto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(checklistItemMasterdto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setStatus(checklistItemMasterdto.getStatus());
			mstr.setUpdatedBy(checklistItemMasterdto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(checklistItemMasterdto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			if(checklistItemMasterdto.getChecklistItemMasterId()!=null) {
				mstr.setChecklistItemMasterId(checklistItemMasterdto.getChecklistItemMasterId());
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}else {
				save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
				} catch (HibernateException he) {
					logger.error(he.getMessage(), he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage());
				}
	}

	@Override
	public Response updateChecklistItemMasterStatus(ChecklistItemMasterDto checklistItemMasterdto)
			throws ApplicationException {
		try {
			ChecklistItemMaster mstr = findById(checklistItemMasterdto.getChecklistItemMasterId());

			// BR-149: cross-tenant IDOR fix -- same ownership check as the update path.
			if (mstr == null || mstr.getOrganizationId() == null
					|| !mstr.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())
					|| mstr.getUnitId() == null
					|| !mstr.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
				return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			mstr.setStatus(checklistItemMasterdto.getStatus());
			mstr.setUpdatedBy(checklistItemMasterdto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(checklistItemMasterdto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getChecklistItemMasterList(ChecklistItemMasterDto checklistItemMasterdto)
			throws ApplicationException {
		try {
			List<ChecklistItemMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLISTITEM_MASTER")
					.setParameter("organizationId", checklistItemMasterdto.getOrganizationId())
					.setParameter("unitId", checklistItemMasterdto.getUnitId())
					.setFirstResult(checklistItemMasterdto.getOffset() != null ? checklistItemMasterdto.getOffset()  : 0)
					.setMaxResults(checklistItemMasterdto.getNoOfRecordsPerPage() != null ? checklistItemMasterdto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(ChecklistItemMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		}

	@Override
	public Response searchChecklistItemName(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException {
		try {
			List<ChecklistItemMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_CHECKLISTITEM_MASTER_LIST")
					.setParameter("organizationId", checklistItemMasterdto.getOrganizationId())
					.setParameter("unitId", checklistItemMasterdto.getUnitId())
					.setParameter("description", "%"+checklistItemMasterdto.getDescription().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(ChecklistItemMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getChecklistItemMasterListCount(ChecklistItemMasterDto checklistItemMasterdto)
			throws ApplicationException {
		try {
			List<ChecklistItemMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLISTITEM_MASTER")
					.setParameter("organizationId", checklistItemMasterdto.getOrganizationId())
					.setParameter("unitId", checklistItemMasterdto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ChecklistItemMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, list.size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
