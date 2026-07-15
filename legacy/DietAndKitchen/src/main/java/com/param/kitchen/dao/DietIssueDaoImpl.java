package com.param.kitchen.dao;

import java.util.ArrayList;
import java.util.List;

import com.param.kitchen.dto.DietIssueDto;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DishDto;
import com.param.global.master.dto.MealDetailsDto;
import com.param.global.master.dto.MealMasterDto;
import com.param.kitchen.dto.DietIssueRequestDto;
import com.param.kitchen.dto.PatientDetailsDto;
import com.param.kitchen.model.DietOrder;

import in.param.exception.ApplicationException;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietIssueDaoImpl implements IDietIssueDao,ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietIssueDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	DozerBeanMapper mapper;
	@Autowired
	IDietPlanningDao iDietPlanningDao;
	
	@Autowired
	IDietOrderDao iDietOrderDao;
	
	@Override
	public Response issueDietOrderByDietOrderList(List<Integer> dietOrderIdList) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			if(!dietOrderIdList.isEmpty())
			{
			for (Integer dietOrderId : dietOrderIdList) {
				DietOrder dietOrder=(DietOrder)session.load(DietOrder.class,dietOrderId);
				dietOrder.setIsIssued(true);
				session.update(dietOrder);
			}
			return new Response(SUCCESS, null, "Diet Order is issued successfully.", null, null);
			}
			else
			{
				return new Response(SUCCESS, null, "No Diet Order is available.", null, null);
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
	public Response getDietIssueByMultipleFields(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			

			StringBuilder condition=new StringBuilder();
			
			if(dietIssueRequestDto.getFloorId()!=null)
			{
				condition.append(" AND dietOrder.floorId=:floorId ");
			}
			if(dietIssueRequestDto.getWardId()!=null)
			{
				condition.append(" AND dietOrder.wardId=:wardId ");
			}
			if(dietIssueRequestDto.getMealId()!=null)
			{
				condition.append(" AND dietOrder.mealId=:mealId ");
			}
			
			
			String query1="SELECT distinct dietOrder.patientId as patientId "
					+ " from DietOrder dietOrder "
					+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietIssueRequestDto.getDietDate()+" "
					+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE"
					+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId ";
					
			List<Integer> patientIds=new ArrayList<Integer>();
			Query query = sessionFactory.getCurrentSession()
						.createQuery(query1)
						.setParameter("organizationId",organizationId)
						.setParameter("unitId",unitId);

			if (dietIssueRequestDto.getFloorId() != null) {
				query.setParameter("floorId", dietIssueRequestDto.getFloorId());
			}
			if (dietIssueRequestDto.getWardId() != null) {
				query.setParameter("wardId", dietIssueRequestDto.getWardId());
			}
			if (dietIssueRequestDto.getMealId() != null) {
				query.setParameter("mealId", dietIssueRequestDto.getMealId());
			}


			patientIds = (List<Integer>)query.list();
				
			
			
			List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
			
			for (Integer patientId : patientIds) {
				
				PatientDetailsDto patientDetailsDto =new PatientDetailsDto();
				
				String query2="SELECT distinct dietOrder.patientId as patientId,"
						+ " dietOrder.floorId as floorId,floorMaster.floorName as floorName,"
						+ " dietOrder.wardId as wardId,wardMaster.wardName as wardName,"
						+ " dietOrder.bedId as bedId,bedMaster.bedNumber as bedNumber, "
						+ " dietOrder.dietPreferenceId as dietPreferenceId,dietPreferenceMaster.mealPreferenceDesc as dietPreferenceDesc, "
						+ " dietOrder.dietTypeId as dietTypeId,dietTypeMaster.dietTypeDescription as dietTypeDesc, "
						+ " dietOrder.orderId as orderId "
						+ " from DietOrder dietOrder "
						+ " INNER JOIN dietOrder.floorMaster as floorMaster "
						+ " INNER JOIN dietOrder.wardMaster as wardMaster "
						+ " INNER JOIN dietOrder.bedMaster as bedMaster "
						+ " INNER JOIN dietOrder.dietTypeMaster as dietTypeMaster "
						
						+ " INNER JOIN dietOrder.dietPreferenceMaster as dietPreferenceMaster "
						+ " WHERE dietOrder.patientId="+patientId
						+ " AND dietOrder.status='A' "
						+ " ORDER BY dietOrder.orderId desc ";
						
				
				Query query3 = sessionFactory.getCurrentSession()
							.createQuery(query2)
							.setMaxResults(1)
							.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class));
				patientDetailsDto = (PatientDetailsDto) query3.uniqueResult();

				 if(patientDetailsDto!=null) {
					 Response resp = iDietOrderDao.getPatientDetailsByPatientId(patientDetailsDto.getPatientId(), organizationId, unitId);

					 PatientDetailsDto patientDetailsDtoObj = (PatientDetailsDto) resp.getObject();

					 if (patientDetailsDtoObj != null) {
						 patientDetailsDto.setAge(patientDetailsDtoObj.getAge());
						 patientDetailsDto.setDob(patientDetailsDtoObj.getDob());
						 patientDetailsDto.setDoa(patientDetailsDtoObj.getDoa());
						 patientDetailsDto.setPatientName(patientDetailsDtoObj.getPatientName());
						 patientDetailsDto.setdFirstName(patientDetailsDtoObj.getdFirstName());
						 patientDetailsDto.setUhidNumber(patientDetailsDtoObj.getUhidNumber());
						 patientDetailsDto.setGenderName(patientDetailsDtoObj.getGenderName());
						 patientDetailsDto.setGenderId(patientDetailsDtoObj.getGenderId());
						 patientDetailsDto.setImagePath(patientDetailsDtoObj.getImagePath());

						 patientDetailsList.add(patientDetailsDto);
					 }
				 }
			}
			
				
				if(!patientDetailsList.isEmpty())
				{
					List<PatientDetailsDto> finalPatientDetailsList=new ArrayList<>();

					for (PatientDetailsDto patientDetailsDto : patientDetailsList) {

						MealDetailsDto mealDetailsDto= (MealDetailsDto)sessionFactory.getCurrentSession()
								.createQuery(" SELECT mealMaster.id as mealId, mealMaster.name as mealName "
										+ " from MealMaster as mealMaster "
										+ "WHERE mealMaster=:mealId")
								.setParameter("mealId", dietIssueRequestDto.getMealId())
								.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
								.uniqueResult();
						
						String queryForStatus="SELECT distinct dietOrder.status as status "
								+ " from DietOrder dietOrder "
								+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietIssueRequestDto.getDietDate()+" "
								+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE "
										+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId AND dietOrder.status='A'"
										+ " AND dietOrder.patientId="+patientDetailsDto.getPatientId();
								
						
						Query queryForStatus1 = sessionFactory.getCurrentSession()
									.createQuery(queryForStatus)
									.setParameter("organizationId",organizationId)
									.setParameter("unitId",unitId);

						if (dietIssueRequestDto.getFloorId() != null) {
							queryForStatus1.setParameter("floorId", dietIssueRequestDto.getFloorId());
						}
						if (dietIssueRequestDto.getWardId() != null) {
							queryForStatus1.setParameter("wardId", dietIssueRequestDto.getWardId());
						}
						if (dietIssueRequestDto.getMealId() != null) {
							queryForStatus1.setParameter("mealId", dietIssueRequestDto.getMealId());
						}

						Character statusForOrder = (Character)queryForStatus1.uniqueResult();
						
						mealDetailsDto.setStatus(statusForOrder);
						
							Query cQuery = sessionFactory.getCurrentSession()
									.createQuery(" SELECT dietOrder.orderId as orderId,dietOrder.dishId as dishId, dishMaster.dishDescription as dishName,"
											+ " dietOrder.quantity as quantity from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster "
											+ "WHERE dietOrder.patientId=:patientId AND dietOrder.mealId=:mealId "
											+ "AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietIssueRequestDto.getDietDate()+" "
											+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE  "
											+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId ")
									.setParameter("patientId", patientDetailsDto.getPatientId())
									.setParameter("mealId", mealDetailsDto.getMealId())
									.setParameter("organizationId",organizationId)
									.setParameter("unitId",unitId);

						if (dietIssueRequestDto.getFloorId() != null) {
							cQuery.setParameter("floorId", dietIssueRequestDto.getFloorId());
						}
						if (dietIssueRequestDto.getWardId() != null) {
							cQuery.setParameter("wardId", dietIssueRequestDto.getWardId());
						}
						if (dietIssueRequestDto.getMealId() != null) {
							cQuery.setParameter("mealId", dietIssueRequestDto.getMealId());
						}

						List<DishDto> dishList =cQuery.setResultTransformer(Transformers.aliasToBean(DishDto.class))
									.list();

							mealDetailsDto.setDishList(dishList);
						patientDetailsDto.setMealDetails(mealDetailsDto);

						if(!dishList.isEmpty()){
							finalPatientDetailsList.add(patientDetailsDto);
						}
						
					}
					if(finalPatientDetailsList.size()>0) {
						return new Response(SUCCESS, null, "", finalPatientDetailsList, null);
					}else{
						return new Response(SUCCESS, null, "No Diet Order is available to issue.", null, null);
					}
				}
				else
				{
					return new Response(SUCCESS, null,"No Diet Order is available to issue.", null, null);
				}
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}
	
	
	@Override
	public Response getDietIssueByOnlyDate(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId) throws ApplicationException {
		try {

//			StringBuilder condition=new StringBuilder();

			String query1="SELECT distinct dietOrder.patientId as patientId "
					+ " from DietOrder dietOrder "
					+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietIssueRequestDto.getDietDate()+" "
					+ " AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE "
					+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId ";
					
			List<Integer> patientIds=new ArrayList<Integer>();
			Query query = sessionFactory.getCurrentSession()
						.createQuery(query1)
						.setParameter("organizationId",organizationId)
						.setParameter("unitId",unitId);
			patientIds = (List<Integer>)query.list();
			
			List<MealMasterDto> allMealsList = 	sessionFactory.getCurrentSession()
										.createQuery("select mealMaster.id as mealId from MealMaster mealMaster "
												+ "where  mealMaster.organizationId=:organizationId AND mealMaster.status='A' "
												//+ "and mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
												+ "")
										.setParameter("organizationId",organizationId)
										//.setParameter("unitId",unitId)  //coment by minesh bcoz its only on org level.
										.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class))
										.list();
			
			List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
			
			List<PatientDetailsDto> finalPatientDetailsList=new ArrayList<>();
			
			for (Integer patientId : patientIds) {
				
				PatientDetailsDto patientDetailsDto =new PatientDetailsDto();
				
					String query2="SELECT distinct dietOrder.patientId as patientId,"
							+ " dietOrder.floorId as floorId,floorMaster.floorName as floorName,"
							+ " dietOrder.wardId as wardId,wardMaster.wardName as wardName,"
							+ " dietOrder.bedId as bedId,bedMaster.bedNumber as bedNumber, "
							+ " dietOrder.dietPreferenceId as dietPreferenceId,dietPreferenceMaster.mealPreferenceDesc as dietPreferenceDesc, "
							+ " dietOrder.dietTypeId as dietTypeId,dietTypeMaster.dietTypeDescription as dietTypeDesc, "
							+ " dietOrder.orderId as orderId "
							+ " from DietOrder dietOrder "
							+ " INNER JOIN dietOrder.floorMaster as floorMaster "
							+ " INNER JOIN dietOrder.wardMaster as wardMaster "
							+ " INNER JOIN dietOrder.bedMaster as bedMaster "
							+ " INNER JOIN dietOrder.dietTypeMaster as dietTypeMaster "

							+ " INNER JOIN dietOrder.dietPreferenceMaster as dietPreferenceMaster "
							+ " WHERE dietOrder.patientId="+patientId
							+ " AND dietOrder.status='A' "
							+ " ORDER BY dietOrder.orderId desc ";


				Query query3 = sessionFactory.getCurrentSession()
							.createQuery(query2)
							.setMaxResults(1)
							.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class));
				patientDetailsDto = (PatientDetailsDto) query3.uniqueResult();

				if(patientDetailsDto!=null) {
					Response resp = iDietOrderDao.getPatientDetailsByPatientId(patientDetailsDto.getPatientId(), organizationId, unitId);

					PatientDetailsDto patientDetailsDtoObj = (PatientDetailsDto) resp.getObject();

					if (patientDetailsDtoObj != null) {
						patientDetailsDto.setAge(patientDetailsDtoObj.getAge());
						patientDetailsDto.setDob(patientDetailsDtoObj.getDob());
						patientDetailsDto.setDoa(patientDetailsDtoObj.getDoa());
						patientDetailsDto.setPatientName(patientDetailsDtoObj.getPatientName());
						patientDetailsDto.setdFirstName(patientDetailsDtoObj.getdFirstName());
						patientDetailsDto.setUhidNumber(patientDetailsDtoObj.getUHIDNumber());
						patientDetailsDto.setGenderName(patientDetailsDtoObj.getGenderName());
						patientDetailsDto.setGenderId(patientDetailsDtoObj.getGenderId());
						patientDetailsDto.setImagePath(patientDetailsDtoObj.getImagePath());

						patientDetailsList.add(patientDetailsDto);

					}
				}
			}

				if(!patientDetailsList.isEmpty())
				{
					for (PatientDetailsDto patientDetailsDto : patientDetailsList) {
					
						for (MealMasterDto mealMasterDto : allMealsList) {

//							if(mealMasterDto.getMealId()!=null)
//							{
//								condition.append(" AND dietOrder.mealId="+mealMasterDto.getMealId()+" ");
//							}
						
							MealDetailsDto mealDetailsDto= (MealDetailsDto)sessionFactory.getCurrentSession()
									.createQuery(" SELECT mealMaster.id as mealId, mealMaster.name as mealName "
											+ " from MealMaster as mealMaster "
											+ "WHERE mealMaster=:mealId")
									.setParameter("mealId", mealMasterDto.getMealId())
									.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
									.uniqueResult();
							
							String queryForStatus="SELECT distinct dietOrder.status as status "
									+ " from DietOrder dietOrder "
									+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietIssueRequestDto.getDietDate()+" "
									+ " AND dietOrder.mealId=:mealId AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE "
									+ " AND dietOrder.organizationId=:organizationId and dietOrder.unitId=:unitId AND dietOrder.status='A'"
									+ " AND dietOrder.patientId="+patientDetailsDto.getPatientId();
							
							Query queryForStatus1 = sessionFactory.getCurrentSession()
										.createQuery(queryForStatus)
										.setParameter("organizationId",organizationId)
										.setParameter("unitId",unitId)
										.setParameter("mealId",mealDetailsDto.getMealId());
							Character statusForOrder = (Character)queryForStatus1.uniqueResult();
							
							mealDetailsDto.setStatus(statusForOrder);
							
								List<DishDto> dishList = sessionFactory.getCurrentSession()
										.createQuery(" SELECT dietOrder.orderId as orderId,dietOrder.dishId as dishId, dishMaster.dishDescription as dishName,"
												+ " dietOrder.quantity as quantity from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster "
												+ "WHERE dietOrder.patientId=:patientId AND dietOrder.mealId=:mealId "
												+ "AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietIssueRequestDto.getDietDate()+" "
												+ " AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE "
												+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId" )
										.setParameter("patientId", patientDetailsDto.getPatientId())
										.setParameter("mealId", mealMasterDto.getMealId())
										.setParameter("organizationId",organizationId)
										.setParameter("unitId",unitId)
										.setResultTransformer(Transformers.aliasToBean(DishDto.class))
										.list();
								
								mealDetailsDto.setDishList(dishList);

							    patientDetailsDto.setMealDetails(mealDetailsDto);

							    if(!dishList.isEmpty())
							    {
							    	
							    	PatientDetailsDto tempPatientDetailsDto=new PatientDetailsDto();
							    	tempPatientDetailsDto.setPatientId(patientDetailsDto.getPatientId());
							    	tempPatientDetailsDto.setMealDetails(patientDetailsDto.getMealDetails());
							    	
							    	tempPatientDetailsDto.setFloorId(patientDetailsDto.getFloorId());
							    	tempPatientDetailsDto.setFloorName(patientDetailsDto.getFloorName());
							    	tempPatientDetailsDto.setWardId(patientDetailsDto.getWardId());
							    	tempPatientDetailsDto.setWardName(patientDetailsDto.getWardName());
							    	tempPatientDetailsDto.setBedId(patientDetailsDto.getBedId());
							    	tempPatientDetailsDto.setBedNumber(patientDetailsDto.getBedNumber());
							    	tempPatientDetailsDto.setPatientId(patientDetailsDto.getPatientId());
							    	tempPatientDetailsDto.setDietPreferenceId(patientDetailsDto.getDietPreferenceId());
							    	tempPatientDetailsDto.setDietPreferenceDesc(patientDetailsDto.getDietPreferenceDesc());
							    	tempPatientDetailsDto.setPatientId(patientDetailsDto.getPatientId());
							    	tempPatientDetailsDto.setDietTypeId(patientDetailsDto.getDietTypeId());
							    	tempPatientDetailsDto.setDietTypeDesc(patientDetailsDto.getDietTypeDesc());
							    	
							    	tempPatientDetailsDto.setAge(patientDetailsDto.getAge());
							    	tempPatientDetailsDto.setDob(patientDetailsDto.getDob());
							    	tempPatientDetailsDto.setDoa(patientDetailsDto.getDoa());
							    	tempPatientDetailsDto.setPatientName(patientDetailsDto.getPatientName());
							    	tempPatientDetailsDto.setdFirstName(patientDetailsDto.getdFirstName());
							    	tempPatientDetailsDto.setUhidNumber(patientDetailsDto.getUhidNumber());
							    	tempPatientDetailsDto.setGenderName(patientDetailsDto.getGenderName());
									tempPatientDetailsDto.setGenderId(patientDetailsDto.getGenderId());
							    	tempPatientDetailsDto.setImagePath(patientDetailsDto.getImagePath());

							    	finalPatientDetailsList.add(tempPatientDetailsDto);
							    }
				
						}
						
					}
					return new Response(SUCCESS, null, null, finalPatientDetailsList, null);
				}

				else
				{
					return new Response(SUCCESS, null,"No Diet Order is available to issue.", null, null);
				}
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	@Override
	public Response getDietIssueByMultipleFieldsWithoutMealId(DietIssueRequestDto dietIssueRequestDto,Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			
			List<MealMasterDto> allMealsList = 	sessionFactory.getCurrentSession()
					.createQuery("select mealMaster.id as mealId from MealMaster mealMaster "
							+ "where mealMaster.organizationId=:organizationId AND mealMaster.status='A' "
							//+ "and mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
							+ "")
					.setParameter("organizationId",organizationId)
					//.setParameter("unitId",unitId)  //coment by minesh bcoz its only on org level.
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class))
					.list();
			
			List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
			
			for (MealMasterDto mealMasterDto : allMealsList) {
				
				dietIssueRequestDto.setMealId(mealMasterDto.getMealId());
				List<PatientDetailsDto> readyList = (List<PatientDetailsDto>) this.getDietIssueByMultipleFields(dietIssueRequestDto,organizationId,unitId).getListObject();
				
				if(readyList != null)
				{
					patientDetailsList.addAll(readyList);
				}
			}
			
			List<PatientDetailsDto> finalPatientDetailsList=new ArrayList<>();
			
			for (PatientDetailsDto patientDetailsDto : patientDetailsList) {
				
				if(!patientDetailsDto.getMealDetails().getDishList().isEmpty())
			    {
			    	
			    	PatientDetailsDto tempPatientDetailsDto=new PatientDetailsDto();
			    	tempPatientDetailsDto.setPatientId(patientDetailsDto.getPatientId());
			    	tempPatientDetailsDto.setMealDetails(patientDetailsDto.getMealDetails());
			    	
			    	tempPatientDetailsDto.setFloorId(patientDetailsDto.getFloorId());
			    	tempPatientDetailsDto.setFloorName(patientDetailsDto.getFloorName());
			    	tempPatientDetailsDto.setWardId(patientDetailsDto.getWardId());
			    	tempPatientDetailsDto.setWardName(patientDetailsDto.getWardName());
			    	tempPatientDetailsDto.setBedId(patientDetailsDto.getBedId());
			    	tempPatientDetailsDto.setBedNumber(patientDetailsDto.getBedNumber());
			    	tempPatientDetailsDto.setPatientId(patientDetailsDto.getPatientId());
			    	tempPatientDetailsDto.setDietPreferenceId(patientDetailsDto.getDietPreferenceId());
			    	tempPatientDetailsDto.setDietPreferenceDesc(patientDetailsDto.getDietPreferenceDesc());
			    	tempPatientDetailsDto.setPatientId(patientDetailsDto.getPatientId());
			    	tempPatientDetailsDto.setDietTypeId(patientDetailsDto.getDietTypeId());
			    	tempPatientDetailsDto.setDietTypeDesc(patientDetailsDto.getDietTypeDesc());
			    	
			    	tempPatientDetailsDto.setAge(patientDetailsDto.getAge());
			    	tempPatientDetailsDto.setDob(patientDetailsDto.getDob());
			    	tempPatientDetailsDto.setDoa(patientDetailsDto.getDoa());
			    	tempPatientDetailsDto.setPatientName(patientDetailsDto.getPatientName());
			    	tempPatientDetailsDto.setdFirstName(patientDetailsDto.getdFirstName());
			    	tempPatientDetailsDto.setUhidNumber(patientDetailsDto.getUhidNumber());
			    	tempPatientDetailsDto.setGenderName(patientDetailsDto.getGenderName());
					tempPatientDetailsDto.setGenderId(patientDetailsDto.getGenderId());
			    	tempPatientDetailsDto.setImagePath(patientDetailsDto.getImagePath());

			    	finalPatientDetailsList.add(tempPatientDetailsDto);
			    }
				
			}
			
			
			
			return new Response(SUCCESS, null, null, finalPatientDetailsList, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
}

	
	@Override
	public Response updateStatus(DietIssueDto dietIssueDto) {
		try {
			Session session = sessionFactory.getCurrentSession();

			if(dietIssueDto.getStatus() == 'A'){
//				MealDetailsDto mealDetailsDto= (MealDetailsDto)sessionFactory.getCurrentSession()
//						.createQuery(" SELECT mealMaster.id as mealId, mealMaster.name as mealName "
//								+ " from MealMaster as mealMaster "
//								+ "WHERE mealMaster=:mealId")
//						.setParameter("mealId", mealMasterDto.getMealId())
//						.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
//						.uniqueResult();
				for (Integer dietOrderId : dietIssueDto.getDietList()) {

					DietOrder dietOrder = (DietOrder) session.get(DietOrder.class, dietOrderId);
					dietOrder.setStatus('A');
					sessionFactory.getCurrentSession().update(dietOrder);
				}
			}else{
				for (Integer dietOrderId : dietIssueDto.getDietList()) {

					DietOrder dietOrder = (DietOrder) session.get(DietOrder.class, dietOrderId);
					dietOrder.setStatus('I');
					sessionFactory.getCurrentSession().update(dietOrder);
				}
			}
			
//			for (Integer dietOrderId : dietOrderIdList) {
//
//				DietOrder dietOrder = (DietOrder) session.get(DietOrder.class, dietOrderId);
//				dietOrder.setStatus(dietOrder.getStatus()== 'A' ? 'I' : 'A');
//				sessionFactory.getCurrentSession().update(dietOrder);
//			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}
	
}
	
	
	/*@Override
	public Response getDietIssueByMultipleFields(DietIssueRequestDto dietIssueRequestDto) throws ApplicationException {
		try {
			

			StringBuilder condition=new StringBuilder();
			
			if(dietIssueRequestDto.getFloorId()!=null)
			{
				condition.append(" AND dietOrder.floorId="+dietIssueRequestDto.getFloorId()+" ");
			}
			if(dietIssueRequestDto.getWardId()!=null)
			{
				condition.append(" AND dietOrder.wardId="+dietIssueRequestDto.getWardId()+" ");
			}
			if(dietIssueRequestDto.getMealId()!=null)
			{
				condition.append(" AND dietOrder.mealId="+dietIssueRequestDto.getMealId()+" ");
			}
			
			String query1="SELECT dietOrder.orderId as orderId,dietOrder.status as status,dietOrder.patientId as patientId,"
					+ " dietOrder.floorId as floorId,floorMaster.floorName as floorName,"
					+ " dietOrder.wardId as wardId,wardMaster.wardName as wardName,"
					+ " dietOrder.bedId as bedId,bedMaster.bedNumber as bedNumber, "
					+ " dietOrder.dietPreferenceId as dietPreferenceId,dietPreferenceMaster.mealPreferenceDesc as dietPreferenceDesc, "
					+ " dietOrder.dietTypeId as dietTypeId,dietTypeMaster.dietTypeDescription as dietTypeDesc, "
					+ " dietOrder.mealId as mealId,mealMaster.name as mealName,dishMaster.dishDescription as dishName,"
					+ " dietOrder.quantity as quantity from DietOrder dietOrder "
					+ " INNER JOIN dietOrder.dishMaster as dishMaster "
					+ " INNER JOIN dietOrder.floorMaster as floorMaster "
					+ " INNER JOIN dietOrder.wardMaster as wardMaster "
					+ " INNER JOIN dietOrder.bedMaster as bedMaster "
					+ " INNER JOIN dietOrder.dietTypeMaster as dietTypeMaster "
					+ " INNER JOIN dietOrder.mealMaster as mealMaster "
					+ " INNER JOIN dietOrder.dietPreferenceMaster as dietPreferenceMaster "
					+ " WHERE dietOrder.status='A' "
					+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietIssueRequestDto.getDietDate()+" "
					+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE";
					
			List<DietOrderDto> dietOrderDtoList=new ArrayList<>();
			Query query = sessionFactory.getCurrentSession()
						.createQuery(query1)
						.setResultTransformer(Transformers.aliasToBean(DietOrderDto.class));
				dietOrderDtoList = query.list();
				
				if(!dietOrderDtoList.isEmpty())
				{
					for (DietOrderDto dietOrderDto : dietOrderDtoList) {
						PatientDetailsDto patientDetails=(PatientDetailsDto)iDietOrderService.getPatientDetailsByPatientId(dietOrderDto.getPatientId()).getObject();
						dietOrderDto.setPatientDetails(patientDetails);
					}
					return new Response(SUCCESS, null, null, dietOrderDtoList, null);
				}
				else
				{
					return new Response(SUCCESS, null,"No Diet Order is available to issue.", null, null);
				}
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	*/
	
	
