package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.GrowthDevelopmentalMilestonesTransaction;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesDto;
import com.param.global.unit.dto.GrowthDevelopmentalMilestonesReqDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GrowthDevelopmentalMilestonesTransationDaoImpl extends GenericDao<GrowthDevelopmentalMilestonesTransaction, Integer> implements GrowthDevelopmentalMilestonesTransationDao, ICommonConstants {

	public GrowthDevelopmentalMilestonesTransationDaoImpl() {
		super(GrowthDevelopmentalMilestonesTransaction.class);
	}
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveGrowthDevelopmentalMilestonesTransation(GrowthDevelopmentalMilestonesDto dto) throws ApplicationException {
		try{
			GrowthDevelopmentalMilestonesTransaction transaction = new GrowthDevelopmentalMilestonesTransaction();
			transaction.setGrowthDevelopmentalMilestonesId(dto.getGrowthDevelopmentalMilestonesId());
			transaction.setAchievedDate(GlobalCommonDateUtils.getDate(dto.getAchievedDate(), "dd-M-yyyy HH:mm:ss"));
			transaction.setActualAgeOfMilestoneAchieved(dto.getActualAgeOfMilestoneAchieved());
			transaction.setPatientId(dto.getPatientId());
			transaction.setOrganizationId(dto.getOrganizationId());
			transaction.setUnitId(dto.getUnitId());
			transaction.setCreatedBy(dto.getCreatedBy());
			transaction.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			transaction.setStatus(ACTIVE);
			transaction.setUpdatedBy(dto.getUpdatedBy());
			transaction.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			int id = (int)sessionFactory.getCurrentSession().save(transaction);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}



	@Override
	public Response getGrowthDevelopmentalMilestonesTransation(GrowthDevelopmentalMilestonesReqDto reqDto) throws ApplicationException {
		try {
			List<GrowthDevelopmentalMilestonesDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_GROWTH_DEVELOPMENTAL_MILESTONES_TRANSACTION_BY_PATIENT")
					.setParameter("orgId", reqDto.getOrganizationId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("patientId", reqDto.getPatientId())
				    .setResultTransformer(Transformers.aliasToBean(GrowthDevelopmentalMilestonesDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			System.out.println("*************************" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	

}
