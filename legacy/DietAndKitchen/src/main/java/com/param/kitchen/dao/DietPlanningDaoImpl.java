package com.param.kitchen.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DietPrefDto;
import com.param.global.master.dto.DishDto;
import com.param.global.master.dto.MealDetailsDto;
import com.param.global.master.dto.MealsDto;
import com.param.kitchen.dto.DietPlanningDto;
import com.param.kitchen.dto.PatientInfoDto;
import com.param.kitchen.model.DietOrder;
import com.param.kitchen.model.DietPlanning;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Component
public class DietPlanningDaoImpl implements IDietPlanningDao,ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietPlanningDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	DozerBeanMapper mapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response saveDietPlanning(DietPlanningDto dietPlanningDto) throws ApplicationException, ParseException {
		try {
			Session session = sessionFactory.getCurrentSession();
			List<DietOrder> dietOrderByPatientId  = getDietOrderByPatientId(dietPlanningDto.getPatientId());

			if(dietOrderByPatientId.size()>0 && dietOrderByPatientId!= null){
				Query updateQue = sessionFactory.getCurrentSession()
						.createSQLQuery("update  kitchen.diet_order set status ='I' where patient_id = :patientId and diet_preference_id in (:dietPreferenceId)")
						.setParameter("patientId", dietPlanningDto.getPatientId())
						.setParameter("dietPreferenceId", dietOrderByPatientId.getFirst().getDietPreferenceId());

				updateQue.executeUpdate();
			}

			DietPlanning dietPlanning = new DietPlanning();
			dietPlanning.setDietTypeId(dietPlanningDto.getDietTypeId());
			dietPlanning.setPatientId(dietPlanningDto.getPatientId());
			dietPlanning.setCreatedBy(dietPlanningDto.getCreatedBy());
			dietPlanning.setUpdatedBy(dietPlanningDto.getCreatedBy());
			dietPlanning.setOrganizationId(dietPlanningDto.getOrganizationId());
			dietPlanning.setUnitId(dietPlanningDto.getUnitId());
			dietPlanning.setCreatedDate(TimeZoneComponent.getDateByzone(dietPlanningDto.getUnitId()));
			dietPlanning.setUpdatedDate(TimeZoneComponent.getDateByzone(dietPlanningDto.getUnitId()));


			Integer dietPlanningId = (Integer) session.save(dietPlanning);

			Query query1 = sessionFactory.getCurrentSession()
					.createQuery("select admission.admissionId as admissionId"
							+ " from Admission admission where admission.status = 'A' AND admission.patientId=:patientId")
					.setParameter("patientId", dietPlanning.getPatientId());

			List<?> result = query1.list(); // Execute the query and get the result list

			if (result == null || result.isEmpty()) {
				return new Response(SUCCESS, "200", "Patient Discharged", null, null);
			}

			Integer admissionId = (Integer) query1.list().getFirst();

			Query query = sessionFactory.getCurrentSession()
					.createQuery("select admissionDetails.floorId as floorId,"
							+ "admissionDetails.wardId as wardId, admissionDetails.bedId as bedId "
							+ "from AdmissionDetails admissionDetails "
							+ " where admissionDetails.status = 'A' AND admissionDetails.admissionId=:admissionId")
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(PatientInfoDto.class));
			PatientInfoDto patientInfoDto =(PatientInfoDto) query.uniqueResult();
			patientInfoDto.setPatientId(dietPlanning.getPatientId());

			List<MealDetailsDto> mealDetailsList = dietPlanningDto.getMealDetailsList();

			for (MealDetailsDto mealDetailsDto : mealDetailsList) {

				for (MealsDto mealsDto : mealDetailsDto.getMealsList()) {

					for (DishDto dishDto : mealsDto.getDishList()) {

						if (dishDto!= null) {

							DietOrder dietOrder = new DietOrder();
							dietOrder.setPatientId(patientInfoDto.getPatientId());
							dietOrder.setFloorId(patientInfoDto.getFloorId());
							dietOrder.setWardId(patientInfoDto.getWardId());
							dietOrder.setBedId(patientInfoDto.getBedId());
							dietOrder.setMealNotes(dishDto.getMealNotes());
							
							dietOrder.setDietPreferenceId(dietPlanningDto.getDietPreferenceId());
							
							dietOrder.setOrganizationId(dietPlanningDto.getOrganizationId());
							dietOrder.setUnitId(dietPlanningDto.getUnitId());
							
							dietOrder.setDietTypeId(dietPlanningDto.getDietTypeId());
							
							dietOrder.setDietPlanningId(dietPlanningId);
							dietOrder.setMealId(mealDetailsDto.getMealId());
							
							dietOrder.setDayId(mealsDto.getDayId());
							dietOrder.setDietDate(new SimpleDateFormat("yyyy-MM-dd").parse(mealsDto.getDietDate()));
							dietOrder.setDishId(dishDto.getDishId());

							dietOrder.setQuantity(dishDto.getQuantity());

							dietOrder.setStatus('A');
							dietOrder.setIsIssued(false);
							dietOrder.setIsRejected(false);
							dietOrder.setIsAccepted(false);
							dietOrder.setRejectionStatus(false);

							session.save(dietOrder);

						}

					}

				}

			}

			return new Response(SUCCESS, "200", COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}

	private List<DietOrder> getDietOrderByPatientId(Integer patientId) {

		List<DietOrder> listDietOrder = new ArrayList<>();

		// Corrected SQL query to fetch order IDs and diet preference IDs
		Query query1 = sessionFactory.getCurrentSession()
				.createSQLQuery("select orders.order_id as orderId, orders.diet_preference_id as dietPreferenceId from kitchen.diet_order orders where orders.patient_id=:patientId")
				.setParameter("patientId", patientId);

		// Execute the query to get a list of object arrays
		List<Object[]> resultList = query1.list();

		// Iterate through the result array and construct DietOrder objects
		for (Object[] result : resultList) {
			Integer orderId = (Integer) result[0];
			Integer dietPreferenceId = (Integer) result[1];

			// Create a new DietOrder object and set the properties
			DietOrder dietOrder = new DietOrder();
			dietOrder.setOrderId(orderId);
			dietOrder.setDietPreferenceId(dietPreferenceId);

			// Add the DietOrder object to the result list
			listDietOrder.add(dietOrder);
		}

		return listDietOrder;
	}



	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getAllDietPlanning() throws ApplicationException {
		try {
			
			Query query = sessionFactory.getCurrentSession()
					.getNamedQuery("DIET_PLANNING.findAll")
					.setResultTransformer(Transformers.aliasToBean(DietPlanningDto.class));
			List<DietPlanningDto> dietPlanningDtoList = query.list();
			
			for (DietPlanningDto dietPlanningDto : dietPlanningDtoList) {
				
				Integer dietPlanningId=dietPlanningDto.getId();
				if(dietPlanningId != null){
				Query queryForDietOrder = sessionFactory.getCurrentSession()
						.getNamedQuery("DIET_ORDER.GET_BY_DIET_PLANNING_ID")
						.setParameter("dietPlanningId", dietPlanningId)
						.setResultTransformer(Transformers.aliasToBean(DietOrderDto.class));
				List<DietOrderDto> dietOrderDtoList = queryForDietOrder.list();
				dietPlanningDto.setDietOrderDtoList(dietOrderDtoList);
				}
			}
			if(!dietPlanningDtoList.isEmpty())
			{
				return new Response(SUCCESS, null, null, dietPlanningDtoList, null);
			}
			else
			{
				return new Response(SUCCESS, null, "No Diet Planning is available.", null, null);
			}
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getDietPlanningByPatientId(Integer patientId) throws ApplicationException {
		try {
			
			Query query = sessionFactory.getCurrentSession()
					.getNamedQuery("DIET_PLANNING.GET_BY_PATIENT_ID")
					.setParameter("patientId", patientId)
					.setResultTransformer(Transformers.aliasToBean(DietPlanningDto.class));
			List<DietPlanningDto> dietPlanningDtoList = query.list();
			
			for (DietPlanningDto dietPlanningDto : dietPlanningDtoList) {
		
				Integer dietPlanningId=dietPlanningDto.getId();
				if(dietPlanningId != null){
				Query queryForDietOrder = sessionFactory.getCurrentSession()
						.getNamedQuery("DIET_ORDER.GET_BY_DIET_PLANNING_ID")
						.setParameter("dietPlanningId", dietPlanningId)
						.setResultTransformer(Transformers.aliasToBean(DietOrderDto.class));
				List<DietOrderDto> dietOrderDtoList = queryForDietOrder.list();
				dietPlanningDto.setDietOrderDtoList(dietOrderDtoList);
				}
			}
			if(!dietPlanningDtoList.isEmpty())
			{
				return new Response(SUCCESS, null, null, dietPlanningDtoList, null);
			}
			else
			{
				return new Response(SUCCESS, null, "No Diet Planning is available.", null, null);
			}
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response updateDietPlanning(DietPlanningDto dietPlanningDto) throws ApplicationException {
		try {
			
				Session session=sessionFactory.getCurrentSession();
                DietPlanning dietPlanning=(DietPlanning)session.load(DietPlanning.class,dietPlanningDto.getId());
                dietPlanning.setUpdatedBy(dietPlanningDto.getUpdatedBy());
                dietPlanning.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
                session.update(dietPlanning);
				List<DietOrderDto> dietOrderDtoList=dietPlanningDto.getDietOrderDtoList();
				for (DietOrderDto dietOrderDtoObject : dietOrderDtoList) {
					
					if(dietOrderDtoObject.getOrderId()!= null)
					{
						DietOrder dietOrder = (DietOrder)session.load(DietOrder.class, dietOrderDtoObject.getOrderId());
						dietOrder=mapper.map(dietOrderDtoObject, DietOrder.class,"DietOrderDto_to_DietOrder");
						dietOrder.setDishId(dietOrderDtoObject.getDishId());
						dietOrder.setDishName(dietOrderDtoObject.getDishName());
						dietOrder.setQuantity(dietOrderDtoObject.getQuantity());
						session.update(dietOrder);
					}
					else
					{
						Query query1 = sessionFactory.getCurrentSession()
								.createQuery("select admission.admissionId as admissionId"
										+ " from Admission admission where admission.patientId=:patientId")
								.setParameter("patientId", dietPlanning.getPatientId());
						        Integer admissionId=(Integer)query1.list().get(0);
					
						Query query = sessionFactory.getCurrentSession()
								.createQuery("select admissionDetails.floorId as floorId,"
										+ "admissionDetails.wardId as wardId, admissionDetails.bedId as bedId "
										+ "from AdmissionDetails admissionDetails "
										+ " where admissionDetails.admissionId=:admissionId")
								.setParameter("admissionId", admissionId)
								.setResultTransformer(Transformers.aliasToBean(PatientInfoDto.class));
						List<PatientInfoDto> patientInfoDtoList = query.list();
						PatientInfoDto patientInfoDto=patientInfoDtoList.get(0);
						patientInfoDto.setPatientId(dietPlanning.getPatientId());
						
						DietOrderDto dietOrderDto=new DietOrderDto();
						dietOrderDto.setPatientId(patientInfoDto.getPatientId());
						dietOrderDto.setFloorId(patientInfoDto.getFloorId());
						dietOrderDto.setWardId(patientInfoDto.getWardId());
						dietOrderDto.setBedId(patientInfoDto.getBedId());
						dietOrderDto.setMealId(dietPlanning.getMealId());
						dietOrderDto.setDietTypeId(dietPlanning.getDietTypeId());
						dietOrderDto.setDietPreference(dietPlanning.getDietPreference());
						dietOrderDto.setDietDate(dietPlanningDto.getDietDate());
						dietOrderDto.setDishId(dietOrderDtoObject.getDishId());
						dietOrderDto.setDishName(dietOrderDtoObject.getDishName());
						dietOrderDto.setQuantity(dietOrderDtoObject.getQuantity());
						dietOrderDto.setStatus('A');
						dietOrderDto.setIsIssued(false);
						dietOrderDto.setIsRejected(false);
						dietOrderDto.setDietPlanningId(dietPlanningDto.getId());
						
						DietOrder dietOrder=mapper.map(dietOrderDto, DietOrder.class,
								"DietOrderDto_to_DietOrder");
						
						session.save(dietOrder);
						
					}
					
				}
			return new Response(SUCCESS, null,"Diet Planning is updated successfully.", null, null);
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response deleteDietPlanning(Integer patientId) throws ApplicationException {
		try {
			
				Query query = sessionFactory.getCurrentSession()
					.getNamedQuery("DIET_PLANNING.GET_BY_PATIENT_ID")
					.setParameter("patientId", patientId)
					.setResultTransformer(Transformers.aliasToBean(DietPlanningDto.class));
				List<DietPlanningDto> dietPlanningDtoList = query.list();
				if(!dietPlanningDtoList.isEmpty())
				{

					for (DietPlanningDto dietPlanningDto : dietPlanningDtoList) {
						
						    DietPlanning dietPlanning =(DietPlanning)sessionFactory.getCurrentSession().load(DietPlanning.class,dietPlanningDto.getId());	
							dietPlanning.setStatus('D');
							sessionFactory.getCurrentSession().update(dietPlanning);
							List<DietOrderDto> dietOrderDtoList=dietPlanningDto.getDietOrderDtoList();
							if(dietOrderDtoList != null)
							{
								for (DietOrderDto dietOrderDto : dietOrderDtoList) {
									
									DietOrder dietOrder=(DietOrder)sessionFactory.getCurrentSession()
											             .load(DietOrder.class, dietOrderDto.getOrderId());
									dietOrder.setStatus('D');
									sessionFactory.getCurrentSession().update(dietOrder);
								}
							}
							
						}
						
					return new Response(SUCCESS, null,"Diet Planning is deleted successfully.", null, null);
				}
				else
				{
					return new Response(SUCCESS, null,"No Diet Planning is available.", null, null);
				}
				
			
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getDietPreferenceByPatientId(Integer patientId) throws ApplicationException {
		try {
			
			SQLQuery query = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT mealPrefMst.meal_preference_id as \"dietPrefId\"," +
							/*+ "mealPrefMst.meal_preference_code as \"dietPrefCode\","
							+ "mealPrefMst.meal_preference_desc as \"dietPrefDesc\" from adt.m_meal_preference_master mealPrefMst "
							+ "where mealPrefMst.meal_preference_id in ( select admDetails.meal_pref_id from adt.t_admission_details admDetails "
							+ "where admDetails.admission_id in (select admission.admission_id from adt.t_admission admission "
							+ "where admission.patient_id=:patientId ) ) "*/
							 " mealPrefMst.meal_preference_code as \"dietPrefCode\","
								+ " mealPrefMst.meal_preference_desc as \"dietPrefDesc\" "
								+ " from adt.m_meal_preference_master mealPrefMst "
								+ " inner join adt.t_ipd_diet_order ipdiet on ipdiet.diet_meal_preference_id =mealPrefMst.meal_preference_id "
								+ " where mealPrefMst.meal_preference_id in "
								+ " ( select ipdiet.diet_meal_preference_id from adt.t_ipd_diet_order ip "
								+ " where ip.admission_id in "
								+ " (select admission.admission_id from adt.t_admission admission "
								+ "  where admission.patient_id=:patientId) "
								+ " ) "
);
					
					
			List<DietPrefDto> dietPrefList = query
					.setParameter("patientId", patientId)
					.setResultTransformer(Transformers.aliasToBean(DietPrefDto.class))
					.list();
			
			
			if(!dietPrefList.isEmpty())
			{
				return new Response(SUCCESS, null, null, null,dietPrefList.getFirst());
			}
			else
			{
				return new Response(SUCCESS, null, "Something went wrong.", null, null);
			}
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


}



/*-----------------Old Diet Planning Save Logic ---------------------*/

/*for (DietOrderDto dietOrderDtoObject : dietOrderDtoList) {

DietOrderDto dietOrderDto=new DietOrderDto();
dietOrderDto.setPatientId(patientInfoDto.getPatientId());
dietOrderDto.setFloorId(patientInfoDto.getFloorId());
dietOrderDto.setWardId(patientInfoDto.getWardId());
dietOrderDto.setBedId(patientInfoDto.getBedId());
dietOrderDto.setMealId(dietPlanning.getMealId());
dietOrderDto.setDietTypeId(dietPlanning.getDietTypeId());
dietOrderDto.setDietPreference(dietPlanning.getDietPreference());
dietOrderDto.setDietDate(dietPlanningDto.getDietDate());
dietOrderDto.setDishId(dietOrderDtoObject.getDishId());
dietOrderDto.setDishName(dietOrderDtoObject.getDishName());
dietOrderDto.setQuantity(dietOrderDtoObject.getQuantity());
dietOrderDto.setStatus('A');
dietOrderDto.setIsIssued(false);
dietOrderDto.setIsRejected(false);
dietOrderDto.setDietPlanningId(dietPlanningId);

DietOrder dietOrder=mapper.map(dietOrderDto, DietOrder.class,
		"DietOrderDto_to_DietOrder");

session.save(dietOrder);

}*/





