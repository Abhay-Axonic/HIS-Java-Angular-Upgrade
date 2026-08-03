package com.param.equipmentchecklist.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.equipmentchecklist.Dto.EquipmentChecklistMapperDto;
import com.param.equipmentchecklist.model.EquipmentChecklistMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.service.VerifiedIdentityService;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class EquipmentChecklistMapperDaoImpl extends GenericDao<EquipmentChecklistMapper, Integer> implements IEquipmentChecklistMapperDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(EquipmentChecklistMapperDaoImpl.class);

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;


	public EquipmentChecklistMapperDaoImpl() {
		super(EquipmentChecklistMapper.class);
	}

	@Override
	public Response saveChecklistEquipmentMapperMaster(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException {
		try {
			
			EquipmentChecklistMapperDto dto = new EquipmentChecklistMapperDto();
			// uncomment by minesh
			if(equipmentChecklistMapperDto.getItemCode()!=null && equipmentChecklistMapperDto.getItemName()!=null 
					 && equipmentChecklistMapperDto.getChecklistEquipmentMapperMasterId() == null) {//added condition by minesh for update EquipmentChecklistMapper.
				dto = (EquipmentChecklistMapperDto) sessionFactory.getCurrentSession()
							.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MAPPER_MASTER")
							.setParameter("organizationId", equipmentChecklistMapperDto.getOrganizationId())
							.setParameter("unitId", equipmentChecklistMapperDto.getUnitId())
							.setParameter("itemCode", equipmentChecklistMapperDto.getItemCode().toLowerCase())
							.setParameter("itemName", equipmentChecklistMapperDto.getItemName().toLowerCase())
							.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistMapperDto.class)).uniqueResult();
			} else {//added by minesh for update EquipmentChecklistMapper.
				dto = (EquipmentChecklistMapperDto) sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MAPPER_MASTER_NOT_ID")
						.setParameter("organizationId", equipmentChecklistMapperDto.getOrganizationId())
						.setParameter("unitId", equipmentChecklistMapperDto.getUnitId())
						.setParameter("checklistEquipmentMapperMasterId", equipmentChecklistMapperDto.getChecklistEquipmentMapperMasterId())
						.setParameter("itemCode", equipmentChecklistMapperDto.getItemCode().toLowerCase())
						.setParameter("itemName", equipmentChecklistMapperDto.getItemName().toLowerCase())
						.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistMapperDto.class)).uniqueResult();
			}//end update EquipmentChecklistMapper.
			
			
			if(dto!=null)
			{
				return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
			} // uncomment by minesh
			
			EquipmentChecklistMapper mstr = null;
			if (equipmentChecklistMapperDto.getChecklistEquipmentMapperMasterId() != null) {
				// BR-151: cross-tenant IDOR fix -- load the existing row (findById also fixes the
				// pre-existing detached-entity 500: the old code built a NEW entity and called
				// update(mstr), silently throwing after the dup-check) and verify ownership.
				mstr = findById(equipmentChecklistMapperDto.getChecklistEquipmentMapperMasterId());
				if (mstr == null || mstr.getOrganizationId() == null
						|| !mstr.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())
						|| mstr.getUnitId() == null
						|| !mstr.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
					return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}
			} else {
				// create binds the JWT-verified org/unit, never a client-supplied value
				mstr = new EquipmentChecklistMapper();
				mstr.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
				mstr.setUnitId(verifiedIdentityService.getVerifiedUnitId());
				mstr.setCreatedBy(equipmentChecklistMapperDto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(equipmentChecklistMapperDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			}
			mstr.setChecklistEquipmentMasterId(equipmentChecklistMapperDto.getChecklistEquipmentMasterId());
			mstr.setAssetCategoryId(equipmentChecklistMapperDto.getAssetCategoryId());
			mstr.setProductCategoryId(equipmentChecklistMapperDto.getProductCategoryId());
			mstr.setItemCode(equipmentChecklistMapperDto.getItemCode());
			mstr.setItemName(equipmentChecklistMapperDto.getItemName());
			mstr.setStatus(equipmentChecklistMapperDto.getStatus());
			mstr.setUpdatedBy(equipmentChecklistMapperDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(equipmentChecklistMapperDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			if(equipmentChecklistMapperDto.getChecklistEquipmentMapperMasterId()!=null) {
				update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, mstr);
			}else {
				save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);
			}
				} catch (HibernateException he) {
				logger.error("saveChecklistEquipmentMapperMaster failed", he);
				} catch (Exception e) {
				logger.error("saveChecklistEquipmentMapperMaster failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllEquipmentChecklistMapperMasterList(EquipmentChecklistMapperDto equipmentChecklistMapperDto)
			throws ApplicationException {
		try {
			List<EquipmentChecklistMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_EQUIPMENT_MAPPER_MASTER_LIST")
					.setParameter("organizationId", equipmentChecklistMapperDto.getOrganizationId())
					.setParameter("unitId", equipmentChecklistMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
		logger.error("getAllEquipmentChecklistMapperMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateChecklistEquipmentMapperStatus(EquipmentChecklistMapperDto equipmentChecklistMapperDto)
			throws ApplicationException {
		try {
			EquipmentChecklistMapper mstr = findById(equipmentChecklistMapperDto.getChecklistEquipmentMapperMasterId());

			// BR-151: cross-tenant IDOR fix -- same ownership check as the save/update path
			if (mstr == null || mstr.getOrganizationId() == null
					|| !mstr.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())
					|| mstr.getUnitId() == null
					|| !mstr.getUnitId().equals(verifiedIdentityService.getVerifiedUnitId())) {
				return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			mstr.setStatus(equipmentChecklistMapperDto.getStatus());
			mstr.setUpdatedBy(equipmentChecklistMapperDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(equipmentChecklistMapperDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("updateChecklistEquipmentMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/*@Override
	public Response getEquipmentChecklistNameListById(EquipmentChecklistMapperDto equipmentChecklistMapperDto)
			throws ApplicationException {
		try {
			List<EquipmentChecklistMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_EQUIPMENT_MASTER_DETAILS_LIST_BY_MASTER_ID")
					.setParameter("organizationId", equipmentChecklistMapperDto.getOrganizationId())
					.setParameter("unitId", equipmentChecklistMapperDto.getUnitId())
					.setParameter("checklistMasterId", equipmentChecklistMapperDto.getChecklistEquipmentMasterId())
					.setResultTransformer(Transformers.aliasToBean(EquipmentChecklistMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
}
