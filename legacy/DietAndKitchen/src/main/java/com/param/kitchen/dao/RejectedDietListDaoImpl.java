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
import com.param.kitchen.dto.PatientDetailsDto;
import com.param.kitchen.dto.RejectedDietListRequestDto;
import com.param.kitchen.model.DietOrder;

import in.param.exception.ApplicationException;


@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class RejectedDietListDaoImpl implements IRejectedDietListDao,ICommonConstants {

	private static final Logger logger = Logger.getLogger(RejectedDietListDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	DozerBeanMapper mapper;
	@Autowired
	IDietOrderDao iDietOrderDao;
	
	
	
	@Override
	public Response reIssueRejectedDietOrderByDietOrderId(List<Integer> orderIds) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			if(!orderIds.isEmpty())
			{
				for (Integer orderId : orderIds) {
					
					DietOrder dietOrder=(DietOrder)session.load(DietOrder.class,orderId);
					if(dietOrder!=null)
					{
						if(dietOrder.getIsRejected() && dietOrder.getStatus()=='A' && !dietOrder.getIsIssued() 
								&& !dietOrder.getIsAccepted() && !dietOrder.getRejectionStatus())
						{
							dietOrder.setIsIssued(true);
							dietOrder.setIsRejected(false);
							session.update(dietOrder);
						}
						
					}
					
				}
				
				return new Response(SUCCESS, null, "Diet Order is reissued successfully.", null, null);
			}
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getRejectedDietOrderListByMultipleFields(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(rejectedDietListRequest.getFloorId()!=null)
			{
				condition.append(" AND dietOrder.floorId=:floorId ");
			}
			if(rejectedDietListRequest.getWardId()!=null)
			{
				condition.append(" AND dietOrder.wardId=:wardId ");
			}
			if(rejectedDietListRequest.getMealId()!=null)
			{
				condition.append(" AND dietOrder.mealId=:mealId ");
			}
			
			
			String query1="SELECT distinct dietOrder.patientId as patientId "
					+ " from DietOrder dietOrder "
					+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "
					+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE AND dietOrder.isAccepted=FALSE "
							+ "AND dietOrder.rejectionStatus=FALSE"
							+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId ";
					
			List<Integer> patientIds=new ArrayList<Integer>();
			Query query = sessionFactory.getCurrentSession()
						.createQuery(query1)
						.setParameter("organizationId",organizationId)
						.setParameter("unitId",unitId);


			if(rejectedDietListRequest.getFloorId()!=null) {
				query.setParameter("floorId", rejectedDietListRequest.getFloorId());
			}
			if(rejectedDietListRequest.getWardId()!=null) {
				query.setParameter("wardId", rejectedDietListRequest.getWardId());
			}
			if(rejectedDietListRequest.getMealId()!=null){
				query.setParameter("mealId", rejectedDietListRequest.getMealId());
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
								.setParameter("mealId", rejectedDietListRequest.getMealId())
								.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
								.uniqueResult();
						
						String queryForStatus="SELECT distinct dietOrder.status as status "
								+ " from DietOrder dietOrder "
								+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "
								+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE  AND dietOrder.isAccepted=FALSE "
								+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId "
										+ "AND dietOrder.patientId="+patientDetailsDto.getPatientId();
								
						
						Query queryForStatus1 = sessionFactory.getCurrentSession()
									.createQuery(queryForStatus)
									.setParameter("organizationId",organizationId)
									.setParameter("unitId",unitId);

						if (rejectedDietListRequest.getFloorId() != null) {
							queryForStatus1.setParameter("floorId", rejectedDietListRequest.getFloorId());
						}
						if (rejectedDietListRequest.getWardId() != null) {
							queryForStatus1.setParameter("wardId", rejectedDietListRequest.getWardId());
						}
						if (rejectedDietListRequest.getMealId() != null) {
							queryForStatus1.setParameter("mealId", rejectedDietListRequest.getMealId());
						}

						Character statusForOrder = (Character)queryForStatus1.uniqueResult();
						
						mealDetailsDto.setStatus(statusForOrder);
						
						String queryForRemarks="SELECT distinct dietOrder.remarks as remarks "
								+ " from DietOrder dietOrder "
								+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "
								+ " "+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE  "
								+ "AND dietOrder.isAccepted=FALSE "
								+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId "
								+ "AND dietOrder.patientId="+patientDetailsDto.getPatientId();
								
						
						Query queryForRemarks1 = sessionFactory.getCurrentSession()
									.createQuery(queryForRemarks)
									.setParameter("organizationId",organizationId)
									.setParameter("unitId",unitId);

						if (rejectedDietListRequest.getFloorId() != null) {
							queryForRemarks1.setParameter("floorId", rejectedDietListRequest.getFloorId());
						}
						if (rejectedDietListRequest.getWardId() != null) {
							queryForRemarks1.setParameter("wardId", rejectedDietListRequest.getWardId());
						}
						if (rejectedDietListRequest.getMealId() != null) {
							queryForRemarks1.setParameter("mealId", rejectedDietListRequest.getMealId());
						}

						String remarksForOrder = (String)queryForRemarks1.uniqueResult();
						
						patientDetailsDto.setRemarks(remarksForOrder);
						
							Query queryCreate = sessionFactory.getCurrentSession()
									.createQuery(" SELECT dietOrder.orderId as orderId,dietOrder.dishId as dishId, dishMaster.dishDescription as dishName,"
											+ " dietOrder.quantity as quantity from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster "
											+ "WHERE dietOrder.patientId=:patientId AND dietOrder.mealId=:mealId "
											+ "AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "
											+condition+" AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE AND dietOrder.isAccepted=FALSE "
											+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId "
											+ "")
									.setParameter("patientId", patientDetailsDto.getPatientId())
									.setParameter("mealId", mealDetailsDto.getMealId())
									.setParameter("organizationId",organizationId)
									.setParameter("unitId",unitId);

						if (rejectedDietListRequest.getFloorId() != null) {
							queryCreate.setParameter("floorId", rejectedDietListRequest.getFloorId());
						}
						if (rejectedDietListRequest.getWardId() != null) {
							queryCreate.setParameter("wardId", rejectedDietListRequest.getWardId());
						}
						if (rejectedDietListRequest.getMealId() != null) {
							queryCreate.setParameter("mealId", rejectedDietListRequest.getMealId());
						}


						List<DishDto> dishList = queryCreate.setResultTransformer(Transformers.aliasToBean(DishDto.class))
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
//					return new Response(SUCCESS, null, null, patientDetailsList, null);
				}
				else
				{
					return new Response(SUCCESS, null,"No Diet Order is available to accept.", null, null);
				}
			
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getRejectedDietOrderListByMultipleFieldsWithoutMealId(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			
			List<MealMasterDto> allMealsList = 	sessionFactory.getCurrentSession()
					.createQuery("select mealMaster.id as mealId from MealMaster mealMaster "
							+ " where mealMaster.organizationId=:organizationId "
							//+ "AND mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
							+ "")
					.setParameter("organizationId",organizationId)
					//.setParameter("unitId",unitId)  //coment by minesh bcoz its only on org level.
					.setResultTransformer(Transformers.aliasToBean(MealMasterDto.class))
					.list();
			
			List<PatientDetailsDto> patientDetailsList=new ArrayList<>();
			
			for (MealMasterDto mealMasterDto : allMealsList) {
				
				rejectedDietListRequest.setMealId(mealMasterDto.getMealId());
				List<PatientDetailsDto> readyList = (List<PatientDetailsDto>) this.getRejectedDietOrderListByMultipleFields(rejectedDietListRequest,organizationId,unitId).getListObject();
				
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
					tempPatientDetailsDto.setRemarks(patientDetailsDto.getRemarks());

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
	public Response getRejectedDietListByOnlyDate(RejectedDietListRequestDto rejectedDietListRequest,Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			String query1="SELECT distinct dietOrder.patientId as patientId "
					+ " from DietOrder dietOrder "
					+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "
					+ " AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE AND dietOrder.isAccepted=FALSE "
					+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId ";
					
			List<Integer> patientIds=new ArrayList<Integer>();
			Query query = sessionFactory.getCurrentSession()
						.createQuery(query1)
						.setParameter("organizationId",organizationId)
						.setParameter("unitId",unitId);
			patientIds = (List<Integer>)query.list();
			
			List<MealMasterDto> allMealsList = 	sessionFactory.getCurrentSession()
										.createQuery("select mealMaster.id as mealId from MealMaster mealMaster"
												+ " where mealMaster.organizationId=:organizationId "
												//+ "AND mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
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
//				        patientDetailsDto.setUhidNumber(patientDetailsDtoObj.getUhidNumber());
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
						
							MealDetailsDto mealDetailsDto= (MealDetailsDto)sessionFactory.getCurrentSession()
									.createQuery(" SELECT mealMaster.id as mealId, mealMaster.name as mealName "
											+ " from MealMaster as mealMaster "
											+ "WHERE mealMaster=:mealId")
									.setParameter("mealId", mealMasterDto.getMealId())
									.setResultTransformer(Transformers.aliasToBean(MealDetailsDto.class))
									.uniqueResult();
							
							String queryForStatus="SELECT distinct dietOrder.status as status "
									+ " from DietOrder dietOrder "
									+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "
									+ " AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE AND dietOrder.isAccepted=FALSE "
									+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId "
									+ " AND dietOrder.patientId="+patientDetailsDto.getPatientId();
							
							Query queryForStatus1 = sessionFactory.getCurrentSession()
										.createQuery(queryForStatus)
										.setParameter("organizationId",organizationId)
										.setParameter("unitId",unitId);
							Character statusForOrder = (Character)queryForStatus1.uniqueResult();
							
							mealDetailsDto.setStatus(statusForOrder);
							
							String queryForRemarks="SELECT distinct dietOrder.remarks as remarks "
									+ " from DietOrder dietOrder "
									+ " WHERE to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "
									+ " AND dietOrder.mealId=:mealId AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE  "
									+ " AND dietOrder.isAccepted=FALSE "
									+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId "
									+ " AND dietOrder.patientId="+patientDetailsDto.getPatientId();
									
							
							Query queryForRemarks1 = sessionFactory.getCurrentSession()
										.createQuery(queryForRemarks)
										.setParameter("mealId", mealMasterDto.getMealId())
										.setParameter("organizationId",organizationId)
										.setParameter("unitId",unitId);
							String remarksForOrder = (String)queryForRemarks1.uniqueResult();
							
							patientDetailsDto.setRemarks(remarksForOrder);
							
							
								List<DishDto> dishList = sessionFactory.getCurrentSession()
										.createQuery(" SELECT dietOrder.orderId as orderId,dietOrder.dishId as dishId, dishMaster.dishDescription as dishName,"
												+ " dietOrder.quantity as quantity from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster "
												+ "WHERE dietOrder.patientId=:patientId AND dietOrder.mealId=:mealId "
												+ "AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "
												+ " AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE AND dietOrder.isAccepted=FALSE  "
												+ " AND dietOrder.organizationId=:organizationId AND dietOrder.unitId=:unitId "
												+ "")
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
							    	tempPatientDetailsDto.setRemarks(patientDetailsDto.getRemarks());
							    	
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
//		return new Response(ERROR, null, null, null, null);
	}
	
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response reIssueRejectedDietOrderByDietOrderId(Integer dietOrderId) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			DietOrder dietOrder=(DietOrder)session.load(DietOrder.class,dietOrderId);
			if(dietOrder!=null)
			{
				if(dietOrder.getIsRejected() && dietOrder.getStatus()=='A' && !dietOrder.getIsIssued())
				{
					dietOrder.setIsIssued(true);
					dietOrder.setIsRejected(false);
					session.update(dietOrder);
					return new Response(SUCCESS, null, "Diet Order is reissued successfully.", null, null);
				}
				else if(dietOrder.getIsIssued() && !dietOrder.getIsRejected() && dietOrder.getStatus()=='A')
				{
					return new Response(SUCCESS, null, "Diet Order has already been issued.", null, null);
				}
				else
				{
					return new Response(SUCCESS, null, "No Diet Order is available.", null, null);
				}
			}
			else
			{
				return new Response(SUCCESS, null, "No Diet Order is available.", null, null);
			}
			
			
		} catch (HibernateException he) {
			h
			return new Response(SUCCESS, null, "No Diet Order is available.", null, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getRejectedDietOrderListByMultipleFields(RejectedDietListRequestDto rejectedDietListRequest) throws ApplicationException {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(rejectedDietListRequest.getFloorId()!=null)
			{
				condition.append(" AND dietOrder.floorId="+rejectedDietListRequest.getFloorId()+" ");
			}
			if(rejectedDietListRequest.getWardId()!=null)
			{
				condition.append(" AND dietOrder.wardId="+rejectedDietListRequest.getWardId()+" ");
			}
			if(rejectedDietListRequest.getMealId()!=null)
			{
				condition.append(" AND dietOrder.mealId="+rejectedDietListRequest.getMealId()+" ");
			}
			
			String query1 = "SELECT dietOrder.orderId as orderId, dietOrder.patientId as patientId,"
					+ " dietOrder.floorId as floorId, dietOrder.wardId as wardId,dietOrder.bedId as bedId, "
					+ " dietOrder.mealId as mealId, dietOrder.dishName as dishName, dietOrder.quantity as quantity,"
					+ " dietOrder.status as status,dietOrder.dietTypeId as dietTypeId,dietOrder.dietPreference as dietPreference,"
					+ " dietOrder.remarks as remarks from DietOrder dietOrder WHERE dietOrder.status='A' "
					+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')="+rejectedDietListRequest.getDietDate()+" "+condition
					+ " AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=TRUE ";
			
			List<DietOrderDto> dietOrderDtoList=new ArrayList<>();
			Query query = sessionFactory.getCurrentSession()
						  .createQuery(query1)
					      .setResultTransformer(Transformers.aliasToBean(DietOrderDto.class));
				dietOrderDtoList = query.list();
				if(!dietOrderDtoList.isEmpty())
				{
					return new Response(SUCCESS, null, null,dietOrderDtoList, null);
				}
				else
				{
					return new Response(SUCCESS, null, "No Rejected Diet Order is available.", null, null);
				}
				
			
		} catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
}
