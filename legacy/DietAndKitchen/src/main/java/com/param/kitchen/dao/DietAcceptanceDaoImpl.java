package com.param.kitchen.dao;

import java.util.ArrayList;
import java.util.List;

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
import com.param.kitchen.dto.DietAcceptanceRequestDto;
import com.param.kitchen.dto.PatientDetailsDto;
import com.param.kitchen.model.DietOrder;

import in.param.exception.ApplicationException;

@Component
public class DietAcceptanceDaoImpl implements IDietAcceptanceDao,ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietAcceptanceDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	DozerBeanMapper mapper;
	@Autowired
	IDietPlanningDao iDietPlanningDao;
	@Autowired
	IDietOrderDao iDietOrderDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response acceptDietOrderByDietOrderId(List<PatientDetailsDto> patientDetailsList) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			for (PatientDetailsDto patientDetailsDto : patientDetailsList) {
			
				if(patientDetailsDto.getAccepted()){
					List<DishDto> orderList=patientDetailsDto.getMealDetails().getDishList();
					for (DishDto orderDto : orderList) {
						
						DietOrder dietOrder=(DietOrder) session.load(DietOrder.class, orderDto.getOrderId());
						if(dietOrder!=null)
						{
							if(!dietOrder.getIsAccepted())
							{
								dietOrder.setIsAccepted(true);
								dietOrder.setIsIssued(false);
								session.update(dietOrder);
							}
							
						}
						
					}
				}
				if(!patientDetailsDto.getAccepted())
				{
					
					List<DishDto> orderList=patientDetailsDto.getMealDetails().getDishList();
					for (DishDto orderDto : orderList) {
						
						DietOrder dietOrder=(DietOrder) session.load(DietOrder.class, orderDto.getOrderId());
						if(dietOrder != null)
						{
							if(dietOrder.getIsIssued() && dietOrder.getStatus()=='A' && !dietOrder.getIsRejected()
									&& !dietOrder.getIsAccepted())
							{
								dietOrder.setIsRejected(true);
								dietOrder.setIsIssued(false);
								if(patientDetailsDto.getRemarks() != null)
								{
									dietOrder.setRemarks(patientDetailsDto.getRemarks());
								}
								else
								{
									dietOrder.setRemarks("No Remark Specified.");
								}
								session.update(dietOrder);
								
								DietOrder dietOrderForRejection = new DietOrder();
								
								dietOrderForRejection.setBedId(dietOrder.getBedId());
								dietOrderForRejection.setDayId(dietOrder.getDayId());
								dietOrderForRejection.setDietDate(dietOrder.getDietDate());
								dietOrderForRejection.setDietPlanningId(dietOrder.getDietPlanningId());
								dietOrderForRejection.setDietPreferenceId(dietOrder.getDietPreferenceId());
								dietOrderForRejection.setDietTypeId(dietOrder.getDietTypeId());
								dietOrderForRejection.setDishId(dietOrder.getDishId());
								dietOrderForRejection.setFloorId(dietOrder.getFloorId());
								dietOrderForRejection.setIsAccepted(false);
								dietOrderForRejection.setIsIssued(false);
								dietOrderForRejection.setIsRejected(false);
								dietOrderForRejection.setMealId(dietOrder.getMealId());
								dietOrderForRejection.setPatientId(dietOrder.getPatientId());
								dietOrderForRejection.setQuantity(dietOrder.getQuantity());
								dietOrderForRejection.setRejectionStatus(true);
								dietOrderForRejection.setRemarks(dietOrder.getRemarks());
								dietOrderForRejection.setStatus(dietOrder.getStatus());
								dietOrderForRejection.setWardId(dietOrder.getWardId());

								session.save(dietOrderForRejection);
							}
						}
					}
					
				}
				
				
			}
			
			   return new Response(SUCCESS, null, "Diet Order is accepted successfully.", null, null);
			
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response rejectDietOrderByDietOrderId(List<DishDto> orderList, String remarks) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			for (DishDto orderDto : orderList) {
			
				DietOrder dietOrder=(DietOrder) session.load(DietOrder.class, orderDto.getOrderId());
				if(dietOrder != null)
				{
					if(dietOrder.getIsIssued() && dietOrder.getStatus()=='A' && !dietOrder.getIsRejected()
							&& !dietOrder.getIsAccepted())
					{
						dietOrder.setIsRejected(true);
						dietOrder.setIsIssued(false);
						if(remarks != null)
						{
							dietOrder.setRemarks(remarks);
						}
						else
						{
							dietOrder.setRemarks("No Remark Specified.");
						}
						session.update(dietOrder);
						
						DietOrder dietOrderForRejection = new DietOrder();
						
						dietOrderForRejection.setBedId(dietOrder.getBedId());
						dietOrderForRejection.setDayId(dietOrder.getDayId());
						dietOrderForRejection.setDietDate(dietOrder.getDietDate());
						dietOrderForRejection.setDietPlanningId(dietOrder.getDietPlanningId());
						dietOrderForRejection.setDietPreferenceId(dietOrder.getDietPreferenceId());
						dietOrderForRejection.setDietTypeId(dietOrder.getDietTypeId());
						dietOrderForRejection.setDishId(dietOrder.getDishId());
						dietOrderForRejection.setFloorId(dietOrder.getFloorId());
						dietOrderForRejection.setIsAccepted(false);
						dietOrderForRejection.setIsIssued(false);
						dietOrderForRejection.setIsRejected(false);
						dietOrderForRejection.setMealId(dietOrder.getMealId());
						dietOrderForRejection.setPatientId(dietOrder.getPatientId());
						dietOrderForRejection.setQuantity(dietOrder.getQuantity());
						dietOrderForRejection.setRejectionStatus(true);
						dietOrderForRejection.setRemarks(dietOrder.getRemarks());
						dietOrderForRejection.setStatus(dietOrder.getStatus());
						dietOrderForRejection.setWardId(dietOrder.getWardId());

						session.save(dietOrderForRejection);
					}
				}
			}
			
			return new Response(SUCCESS, null, "Diet order is rejected suuccessfully.", null, null);
			
					
		    } catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getDietAcceptanceByMultipleFields(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId) throws ApplicationException {
try {
			if(dietAcceptanceRequestDto.getTransition() != null)
			{
				if(dietAcceptanceRequestDto.getTransition() == 3)
				{
					StringBuilder condition=new StringBuilder();
					
					if(dietAcceptanceRequestDto.getFloorId()!=null)
					{
						condition.append(" AND dietOrder.floorId=:floorId ");
					}
					if(dietAcceptanceRequestDto.getWardId()!=null)
					{
						condition.append(" AND dietOrder.wardId=:wardId ");
					}
					if(dietAcceptanceRequestDto.getMealId()!=null)
					{
						condition.append(" AND dietOrder.mealId=:mealId ");
					}
					
					
					String query1="SELECT distinct dietOrder.patientId as patientId "
							+ " from DietOrder dietOrder "
							+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
							+ " "+condition+" AND dietOrder.isIssued=TRUE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE "
									+ "AND dietOrder.rejectionStatus=FALSE and dietOrder.organizationId=:organizationId and dietOrder.unitId=:unitId ";
							
					List<Integer> patientIds=new ArrayList<Integer>();
					Query query = sessionFactory.getCurrentSession()
								.createQuery(query1)
								.setParameter("organizationId",organizationId)
								.setParameter("unitId", unitId);

					if (dietAcceptanceRequestDto.getFloorId() != null) {
						query.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
					}
					if (dietAcceptanceRequestDto.getWardId() != null) {
						query.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
					}
					if (dietAcceptanceRequestDto.getMealId() != null) {
						query.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
					}


					patientIds = (List<Integer>)query.list();
						
					List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
					
					for (Integer integer : patientIds) {
						
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
								+ " WHERE dietOrder.patientId="+integer
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
								patientDetailsDto.setTransition("Ready for Acceptance");

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
										.setParameter("mealId", dietAcceptanceRequestDto.getMealId())
										.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
										.uniqueResult();
								
								String queryForStatus="SELECT distinct dietOrder.status as status, dietOrder.orderId as orderId "
										+ " from DietOrder dietOrder "
										+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
										+ " "+condition+" AND dietOrder.isIssued=TRUE AND dietOrder.isRejected=FALSE  AND dietOrder.isAccepted=FALSE AND dietOrder.rejectionStatus=FALSE "
												+ "AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId AND dietOrder.patientId="+patientDetailsDto.getPatientId()
										+" ORDER BY dietOrder.orderId desc ";


								Query queryForStatus1= sessionFactory.getCurrentSession()
											.createQuery(queryForStatus)
											.setParameter("organizationId", organizationId)
											.setParameter("unitId", unitId);


								if (dietAcceptanceRequestDto.getFloorId() != null) {
									queryForStatus1.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
								}
								if (dietAcceptanceRequestDto.getWardId() != null) {
									queryForStatus1.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
								}
								if (dietAcceptanceRequestDto.getMealId() != null) {
									queryForStatus1.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
								}

								queryForStatus1.setMaxResults(1);
								queryForStatus1.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class));

								PatientDetailsDto patientDetailsDtoForStatus = (PatientDetailsDto) queryForStatus1.uniqueResult();
								
								mealDetailsDto.setStatus(patientDetailsDtoForStatus.getStatus());
								
							Query createQuery1= sessionFactory.getCurrentSession()
											.createQuery(" SELECT dietOrder.orderId as orderId,dietOrder.dishId as dishId, dishMaster.dishDescription as dishName,"
													+ " dietOrder.quantity as quantity from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster "
													+ "WHERE dietOrder.patientId=:patientId AND dietOrder.mealId=:mealId "
													+ "AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
													+condition+" AND dietOrder.isIssued=TRUE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE "
															+ "AND dietOrder.rejectionStatus=FALSE "
															+ "AND dietOrder.organizationId=:organizationId "
															+ "AND dietOrder.unitId=:unitId "
													+ "")
											.setParameter("patientId", patientDetailsDto.getPatientId())
											.setParameter("mealId", mealDetailsDto.getMealId())
											.setParameter("organizationId", organizationId)
											.setParameter("unitId", unitId);


								if (dietAcceptanceRequestDto.getFloorId() != null) {
									createQuery1.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
								}
								if (dietAcceptanceRequestDto.getWardId() != null) {
									createQuery1.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
								}
								if (dietAcceptanceRequestDto.getMealId() != null) {
									createQuery1.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
								}

								List<DishDto> dishList = createQuery1.setResultTransformer(Transformers.aliasToBean(DishDto.class))
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
//							return new Response(SUCCESS, null, null, patientDetailsList, null);
						}
						else
						{
							return new Response(SUCCESS, null,"No Diet Order is available to accept.", null, null);
						}
					
				}
				
				else if(dietAcceptanceRequestDto.getTransition() == 1)
					{
						StringBuilder condition=new StringBuilder();
						
						if(dietAcceptanceRequestDto.getFloorId()!=null)
						{
							condition.append(" AND dietOrder.floorId=:floorId ");
						}
						if(dietAcceptanceRequestDto.getWardId()!=null)
						{
							condition.append(" AND dietOrder.wardId=:wardId ");
						}
						if(dietAcceptanceRequestDto.getMealId()!=null)
						{
							condition.append(" AND dietOrder.mealId=:mealId ");
						}
						
						
						String query1="SELECT distinct dietOrder.patientId as patientId "
								+ " from DietOrder dietOrder "
								+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
								+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=TRUE "
										+ "AND dietOrder.rejectionStatus=FALSE AND dietOrder.organizationId=:organizationId "
										+ "AND dietOrder.unitId=:unitId ";
								
						List<Integer> patientIds=new ArrayList<Integer>();
						Query query = sessionFactory.getCurrentSession()
									.createQuery(query1)
									.setParameter("organizationId",organizationId)
									.setParameter("unitId", unitId);

						if (dietAcceptanceRequestDto.getFloorId() != null) {
							query.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
						}
						if (dietAcceptanceRequestDto.getWardId() != null) {
							query.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
						}
						if (dietAcceptanceRequestDto.getMealId() != null) {
							query.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
						}



						patientIds = (List<Integer>)query.list();
							
						
						
						List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
						
						for (Integer integer : patientIds) {
							
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
									+ " WHERE dietOrder.patientId="+integer
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
									patientDetailsDto.setTransition("Accepted");

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
											.setParameter("mealId", dietAcceptanceRequestDto.getMealId())
											.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
											.uniqueResult();
									
									String queryForStatus="SELECT distinct dietOrder.status as status, dietOrder.orderId as orderId "
											+ " from DietOrder dietOrder "
											+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
											+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE  AND dietOrder.isAccepted=TRUE "
													+ "AND dietOrder.rejectionStatus=FALSE "
													+ "AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId "
													+ "AND dietOrder.patientId="+patientDetailsDto.getPatientId()
													+" ORDER BY dietOrder.orderId desc ";
											
									
									Query queryForStatus1 = sessionFactory.getCurrentSession()
												.createQuery(queryForStatus)
												.setParameter("organizationId",organizationId)
												.setParameter("unitId",unitId);

									if (dietAcceptanceRequestDto.getFloorId() != null) {
										queryForStatus1.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
									}
									if (dietAcceptanceRequestDto.getWardId() != null) {
										queryForStatus1.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
									}
									if (dietAcceptanceRequestDto.getMealId() != null) {
										queryForStatus1.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
									}


									queryForStatus1.setMaxResults(1);
									queryForStatus1	.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class));

//									Character statusForOrder = (Character)queryForStatus1.uniqueResult();
									PatientDetailsDto patientDetailsDtoForStatus = (PatientDetailsDto) queryForStatus1.uniqueResult();
									
									mealDetailsDto.setStatus(patientDetailsDtoForStatus.getStatus());
									
										Query cQuery= sessionFactory.getCurrentSession()
												.createQuery(" SELECT dietOrder.orderId as orderId,dietOrder.dishId as dishId, dishMaster.dishDescription as dishName,"
														+ " dietOrder.quantity as quantity from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster "
														+ "WHERE dietOrder.patientId=:patientId AND dietOrder.mealId=:mealId "
														+ "AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
														+condition+" AND dietOrder.isIssued=FALSE "
														+ "AND dietOrder.isRejected=FALSE "
														+ "AND dietOrder.rejectionStatus=FALSE "
														+ "AND dietOrder.isAccepted=TRUE "
														+ "AND dietOrder.organizationId=:organizationId "
														+ "AND dietOrder.unitId=:unitId"
														+ "")
												.setParameter("patientId", patientDetailsDto.getPatientId())
												.setParameter("mealId", mealDetailsDto.getMealId())
												.setParameter("organizationId",organizationId)
												.setParameter("unitId",unitId);


									if (dietAcceptanceRequestDto.getFloorId() != null) {
										cQuery.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
									}
									if (dietAcceptanceRequestDto.getWardId() != null) {
										cQuery.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
									}
									if (dietAcceptanceRequestDto.getMealId() != null) {
										cQuery.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
									}


									List<DishDto> dishList = cQuery.setResultTransformer(Transformers.aliasToBean(DishDto.class))
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
//								return new Response(SUCCESS, null, null, patientDetailsList, null);
							}
							else
							{
								return new Response(SUCCESS, null,"No Diet Order is available to accept.", null, null);
							}
					}
				else if(dietAcceptanceRequestDto.getTransition() == 2)
				{
					StringBuilder condition=new StringBuilder();
					
					if(dietAcceptanceRequestDto.getFloorId()!=null)
					{
						condition.append(" AND dietOrder.floorId=:floorId ");
					}
					if(dietAcceptanceRequestDto.getWardId()!=null)
					{
						condition.append(" AND dietOrder.wardId=:wardId ");
					}
					if(dietAcceptanceRequestDto.getMealId()!=null)
					{
						condition.append(" AND dietOrder.mealId=:mealId ");
					}
					
					
					String query1="SELECT distinct dietOrder.patientId as patientId "
							+ " from DietOrder dietOrder "
							+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
							+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE AND dietOrder.isAccepted=FALSE "
									+ "AND dietOrder.rejectionStatus=TRUE "
									+ "AND dietOrder.organizationId=:organizationId "
									+ "AND dietOrder.unitId=:unitId ";
							
					List<Integer> patientIds=new ArrayList<Integer>();
					Query query = sessionFactory.getCurrentSession()
								.createQuery(query1)
								.setParameter("organizationId", organizationId)
								.setParameter("unitId", unitId);


					if (dietAcceptanceRequestDto.getFloorId() != null) {
						query.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
					}
					if (dietAcceptanceRequestDto.getWardId() != null) {
						query.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
					}
					if (dietAcceptanceRequestDto.getMealId() != null) {
						query.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
					}



					patientIds = (List<Integer>)query.list();
						
					
					
					List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
					
					for (Integer integer : patientIds) {
						
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
								+ " WHERE dietOrder.patientId="+integer
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
								patientDetailsDto.setTransition("Rejected");

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
										.setParameter("mealId", dietAcceptanceRequestDto.getMealId())
										.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
										.uniqueResult();
								
								String queryForStatus="SELECT distinct dietOrder.status as status, dietOrder.orderId as orderId  "
										+ " from DietOrder dietOrder "
										+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
										+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE  AND dietOrder.isAccepted=FALSE "
										+ " AND dietOrder.rejectionStatus=TRUE "
										+ " AND dietOrder.organizationId=:organizationId "
										+ " AND dietOrder.unitId=:unitId "
										+ " AND dietOrder.patientId="+patientDetailsDto.getPatientId()
										+" ORDER BY dietOrder.orderId desc ";
										
								
								Query queryForStatus1 = sessionFactory.getCurrentSession()
											.createQuery(queryForStatus)
											.setParameter("organizationId", organizationId)
											.setParameter("unitId",unitId);


								if (dietAcceptanceRequestDto.getFloorId() != null) {
									queryForStatus1.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
								}
								if (dietAcceptanceRequestDto.getWardId() != null) {
									queryForStatus1.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
								}
								if (dietAcceptanceRequestDto.getMealId() != null) {
									queryForStatus1.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
								}

								queryForStatus1.setMaxResults(1);
								queryForStatus1.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class));

//								Character statusForOrder = (Character)queryForStatus1.uniqueResult();
//								mealDetailsDto.setStatus(statusForOrder);

								PatientDetailsDto patientDetailsDtoForStatus = (PatientDetailsDto) queryForStatus1.uniqueResult();

								mealDetailsDto.setStatus(patientDetailsDtoForStatus.getStatus());
								
									Query cQuery = sessionFactory.getCurrentSession()
											.createQuery(" SELECT dietOrder.orderId as orderId,dietOrder.dishId as dishId, dishMaster.dishDescription as dishName,"
													+ " dietOrder.quantity as quantity from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster "
													+ "WHERE dietOrder.patientId=:patientId AND dietOrder.mealId=:mealId "
													+ "AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
													+condition+" AND dietOrder.isIssued=FALSE AND "
													+ "dietOrder.isRejected=FALSE AND dietOrder.rejectionStatus=TRUE "
													+ "AND dietOrder.isAccepted=FALSE "
													+ " AND dietOrder.organizationId=:organizationId "
													+ " AND dietOrder.unitId=:unitId "
													+ "")
											.setParameter("patientId", patientDetailsDto.getPatientId())
											.setParameter("mealId", mealDetailsDto.getMealId())
											.setParameter("organizationId", organizationId)
											.setParameter("unitId",unitId);
								if (dietAcceptanceRequestDto.getFloorId() != null) {
									cQuery.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
								}
								if (dietAcceptanceRequestDto.getWardId() != null) {
									cQuery.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
								}
								if (dietAcceptanceRequestDto.getMealId() != null) {
									cQuery.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
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
//							return new Response(SUCCESS, null, null, patientDetailsList, null);
						}
						else
						{
							return new Response(SUCCESS, null,"No Diet Order is available to accept.", null, null);
						}
					
					
				}
			}
			else
			{
				StringBuilder condition=new StringBuilder();
				
				if(dietAcceptanceRequestDto.getFloorId()!=null)
				{
					condition.append(" AND dietOrder.floorId=:floorId ");
				}
				if(dietAcceptanceRequestDto.getWardId()!=null)
				{
					condition.append(" AND dietOrder.wardId=:wardId ");
				}
				if(dietAcceptanceRequestDto.getMealId()!=null)
				{
					condition.append(" AND dietOrder.mealId=:mealId ");
				}
				
				
				String query1="SELECT distinct dietOrder.patientId as patientId "
						+ " from DietOrder dietOrder "
						+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
						+ " AND dietOrder.organizationId=:organizationId "
						+ " AND dietOrder.unitId=:unitId "
						+condition;
						
				List<Integer> patientIds=new ArrayList<Integer>();
				Query query = sessionFactory.getCurrentSession()
							.createQuery(query1)
							.setParameter("organizationId", organizationId)
							.setParameter("unitId",unitId);

				if (dietAcceptanceRequestDto.getFloorId() != null) {
					query.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
				}
				if (dietAcceptanceRequestDto.getWardId() != null) {
					query.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
				}
				if (dietAcceptanceRequestDto.getMealId() != null) {
					query.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
				}

				patientIds = (List<Integer>)query.list();

				List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
				
				for (Integer integer : patientIds) {
					
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
							+ " WHERE dietOrder.patientId="+integer
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
							patientDetailsDto.setTransition("Rejected");

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
									.setParameter("mealId", dietAcceptanceRequestDto.getMealId())
									.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
									.uniqueResult();
							
							String queryForStatus="SELECT distinct dietOrder.status as status, dietOrder.orderId as orderId "
									+ " from DietOrder dietOrder "
									+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
									+ " "+condition+" "
									+ " AND dietOrder.organizationId=:organizationId "
									+ " AND dietOrder.unitId=:unitId "
									+ "AND dietOrder.patientId="+patientDetailsDto.getPatientId()
									+" ORDER BY dietOrder.orderId desc ";
									
							
							Query queryForStatus1 = sessionFactory.getCurrentSession()
										.createQuery(queryForStatus)
										.setParameter("organizationId",organizationId)
										.setParameter("unitId", unitId);

							if (dietAcceptanceRequestDto.getFloorId() != null) {
								queryForStatus1.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
							}
							if (dietAcceptanceRequestDto.getWardId() != null) {
								queryForStatus1.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
							}
							if (dietAcceptanceRequestDto.getMealId() != null) {
								queryForStatus1.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
							}

							queryForStatus1.setMaxResults(1);
							queryForStatus1.setResultTransformer(Transformers.aliasToBean(PatientDetailsDto.class));

//							Character statusForOrder = (Character)queryForStatus1.uniqueResult();
//							mealDetailsDto.setStatus(statusForOrder);

							PatientDetailsDto patientDetailsDtoForStatus = (PatientDetailsDto) queryForStatus1.uniqueResult();
							mealDetailsDto.setStatus(patientDetailsDtoForStatus.getStatus());
							
								Query cQuery = sessionFactory.getCurrentSession()
										.createQuery(" SELECT dietOrder.orderId as orderId,dietOrder.dishId as dishId, dishMaster.dishDescription as dishName,"
												+ " dietOrder.quantity as quantity from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster "
												+ "WHERE dietOrder.patientId=:patientId AND dietOrder.mealId=:mealId "
												+ "AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "
												+condition+"  "
												+ " AND dietOrder.organizationId=:organizationId "
												+ " AND dietOrder.unitId=:unitId "
												+ "")
										.setParameter("patientId", patientDetailsDto.getPatientId())
										.setParameter("mealId", mealDetailsDto.getMealId())
										.setParameter("organizationId",organizationId)
										.setParameter("unitId", unitId);


							if (dietAcceptanceRequestDto.getFloorId() != null) {
								cQuery.setParameter("floorId", dietAcceptanceRequestDto.getFloorId());
							}
							if (dietAcceptanceRequestDto.getWardId() != null) {
								cQuery.setParameter("wardId", dietAcceptanceRequestDto.getWardId());
							}
							if (dietAcceptanceRequestDto.getMealId() != null) {
								cQuery.setParameter("mealId", dietAcceptanceRequestDto.getMealId());
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
//						return new Response(SUCCESS, null, null, patientDetailsList, null);
					}
					else
					{
						return new Response(SUCCESS, null,"No Diet Order is available to accept.", null, null);
					}
				
			}
				
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getDietAcceptanceByOnlyDate(DietAcceptanceRequestDto dietAcceptanceRequestDto, Integer organizationId,Integer unitId) throws ApplicationException {	
		try {
			
			List<MealMasterDto> allMealsList = 	sessionFactory.getCurrentSession()
					.createQuery("select mealMaster.id as mealId from MealMaster mealMaster "
							+ "where mealMaster.organizationId=:organizationId AND mealMaster.status='A' "
							//+ "mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
							+ "")
					.setParameter("organizationId",organizationId)
					//.setParameter("unitId", unitId)  //coment by minesh bcoz its only on org level.
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class))
					.list();
			
			List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
			
			for (MealMasterDto mealMasterDto : allMealsList) {
				
				dietAcceptanceRequestDto.setTransition(1);
				dietAcceptanceRequestDto.setMealId(mealMasterDto.getMealId());
				List<PatientDetailsDto> readyList = (List<PatientDetailsDto>) this.getDietAcceptanceByMultipleFields(dietAcceptanceRequestDto,organizationId,unitId).getListObject();
				
				if(readyList != null)
				{
					patientDetailsList.addAll(readyList);
				}
					
				
			}
			
			for (MealMasterDto mealMasterDto : allMealsList) {
				dietAcceptanceRequestDto.setTransition(2);
				dietAcceptanceRequestDto.setMealId(mealMasterDto.getMealId());
				List<PatientDetailsDto> acceptedList = (List<PatientDetailsDto>) this.getDietAcceptanceByMultipleFields(dietAcceptanceRequestDto,organizationId,unitId).getListObject();
				
				if(acceptedList != null)
				{
					patientDetailsList.addAll(acceptedList);
				}
				
			}
			
			for (MealMasterDto mealMasterDto : allMealsList) {
				dietAcceptanceRequestDto.setTransition(3);
				dietAcceptanceRequestDto.setMealId(mealMasterDto.getMealId());
				List<PatientDetailsDto> rejectedList = (List<PatientDetailsDto>) this.getDietAcceptanceByMultipleFields(dietAcceptanceRequestDto,organizationId,unitId).getListObject();
				if(rejectedList != null)
				{
					patientDetailsList.addAll(rejectedList);
				}
		
			}
			return new Response(SUCCESS, null, null, patientDetailsList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getDietAcceptanceByTransitionWithouMealId(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId) throws ApplicationException {	
		try {
			
			List<MealMasterDto> allMealsList = 	sessionFactory.getCurrentSession()
					.createQuery("select mealMaster.id as mealId from MealMaster mealMaster "
							+ " where mealMaster.organizationId=:organizationId "
							//+ "and mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
							+ "")
					.setParameter("organizationId",organizationId)
					//.setParameter("unitId", unitId)  //coment by minesh bcoz its only on org level.
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class))
					.list();
			
			List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
			
			for (MealMasterDto mealMasterDto : allMealsList) {
				
				dietAcceptanceRequestDto.setMealId(mealMasterDto.getMealId());
				List<PatientDetailsDto> readyList = (List<PatientDetailsDto>) this.getDietAcceptanceByMultipleFields(dietAcceptanceRequestDto,organizationId,unitId).getListObject();
				
				if(readyList != null)
				{
					patientDetailsList.addAll(readyList);
				}
					
				
			}
			
						return new Response(SUCCESS, null, null, patientDetailsList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getDietAcceptanceByMealIdWithouTransition(DietAcceptanceRequestDto dietAcceptanceRequestDto,Integer organizationId,Integer unitId) throws ApplicationException {	
		try {
			
			
			List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
			
			for (int i=1;i<4;i++) {
				
				dietAcceptanceRequestDto.setTransition(i);
				List<PatientDetailsDto> readyList = (List<PatientDetailsDto>) this.getDietAcceptanceByMultipleFields(dietAcceptanceRequestDto,organizationId,unitId).getListObject();
				
				if(readyList != null)
				{
					patientDetailsList.addAll(readyList);
				}
			}
			return new Response(SUCCESS, null, null, patientDetailsList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
}





	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getDietAcceptanceByMultipleFields(DietAcceptanceRequestDto dietAcceptanceRequestDto) throws ApplicationException {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(dietAcceptanceRequestDto.getFloorId()!=null)
			{
				condition.append(" AND dietOrder.floorId="+dietAcceptanceRequestDto.getFloorId()+" ");
			}
			if(dietAcceptanceRequestDto.getWardId()!=null)
			{
				condition.append(" AND dietOrder.wardId="+dietAcceptanceRequestDto.getWardId()+" ");
			}
			if(dietAcceptanceRequestDto.getMealId()!=null)
			{
				condition.append(" AND dietOrder.mealId="+dietAcceptanceRequestDto.getMealId()+" ");
			}
			
			
			String query1 = "SELECT dietOrder.orderId as orderId, dietOrder.patientId as patientId,"
					+ " dietOrder.floorId as floorId, dietOrder.wardId as wardId,dietOrder.bedId as bedId, "
					+ " dietOrder.mealId as mealId, dietOrder.dishName as dishName, dietOrder.quantity as quantity,"
					+ " dietOrder.status as status,dietOrder.dietTypeId as dietTypeId,dietOrder.dietPreference as dietPreference"
					+ " from DietOrder dietOrder WHERE dietOrder.status='A' "
					+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+dietAcceptanceRequestDto.getDietDate()+" "+condition
					+ " AND dietOrder.isIssued=TRUE AND dietOrder.isRejected=FALSE ";
			
			List<DietOrderDto> dietOrderDtoList=new ArrayList<>();
				Query query = sessionFactory.getCurrentSession()
							.createQuery(query1)
							.setResultTransformer(Transformers.aliasToBean(DietOrderDto.class));
					dietOrderDtoList = query.list();
					for (DietOrderDto dietOrderDto : dietOrderDtoList) {
						dietOrderDto.setMealId(dietAcceptanceRequestDto.getMealId());
					}
					if( !dietOrderDtoList.isEmpty())
					{
						return new Response(SUCCESS, null, null,dietOrderDtoList, null);
					}
					else
					{
						return new Response(SUCCESS, null,"No Diet order is available for Acceptance",null, null);
					}
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
		
}
