package com.param.global.unit.dao;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.MRoomRentChargesConfigDto;
import com.param.global.model.MRoomRentChargesConfig;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes"})
public class MRoomRentChargesConfigDaoImpl extends GenericDao<MRoomRentChargesConfig, Integer> implements IMRoomRentChargesConfigDao, ICommonConstants{

	public MRoomRentChargesConfigDaoImpl() {
		super(MRoomRentChargesConfig.class);
	}

	@Override
	public Response getRoomRentChargesByOrgUnitBedCat(int orgId, int unitId, int bedCategoryId)throws ApplicationException {
		try{
			MRoomRentChargesConfigDto dto = (MRoomRentChargesConfigDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ROOM_RENT_CONFIG_BY_ORG_UNIT_BEDCATEGORY")
					.setParameter("unitId", unitId).setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(MRoomRentChargesConfigDto.class))
				.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

}
