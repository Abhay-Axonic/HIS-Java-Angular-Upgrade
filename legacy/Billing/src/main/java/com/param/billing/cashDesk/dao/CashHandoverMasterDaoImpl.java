package com.param.billing.cashDesk.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.cashDesk.dto.CashHandoverDetailsDto;
import com.param.billing.cashDesk.dto.DenominationMasterDto;
import com.param.billing.global.model.CashHandoverMaster;
import com.param.billing.global.model.DenominationDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DenominationDetailsMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class CashHandoverMasterDaoImpl extends GenericDao<CashHandoverMaster,Integer> implements ICashHandoverMasterDao,ICommonConstants{

	public CashHandoverMasterDaoImpl() {
		super(CashHandoverMaster.class);
	}

	@Autowired
	private SessionFactory SessionFactory;
	
	
	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public Response getListOfDenominations(DenominationMasterDto denominationMasterDto) {
		try {
			List<DenominationMasterDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DENOMINATION_MASTER_LIST")
					.setResultTransformer(Transformers.aliasToBean(DenominationMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	
	@Override
	public Response saveCashHandoverMaster(CashHandoverDetailsDto cashHandoverDetailsDto) {
		
		try {
			
			CashHandoverMaster cashHandoverMaster = new CashHandoverMaster();
			
			cashHandoverMaster.setDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(cashHandoverDetailsDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			cashHandoverMaster.setCashHandoverById(cashHandoverDetailsDto.getCashHandoverById());		
			cashHandoverMaster.setCashHandoverToId(cashHandoverDetailsDto.getCashHandoverToId());		
			cashHandoverMaster.setCashHandoverAmount(cashHandoverDetailsDto.getCashHandoverAmount());
			cashHandoverMaster.setRemarks(cashHandoverDetailsDto.getRemarks());
			cashHandoverMaster.setOrganizationId(cashHandoverDetailsDto.getOrganizationId());
			cashHandoverMaster.setUnitId(cashHandoverDetailsDto.getUnitId());
			cashHandoverMaster.setCreatedDate(GlobalCommonDateUtils.getDate(cashHandoverDetailsDto.getDate(), "dd-M-yyyy HH:mm:ss"));
			cashHandoverMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(cashHandoverDetailsDto.getDate(), "dd-M-yyyy HH:mm:ss"));
			cashHandoverMaster.setCreatedBy(cashHandoverDetailsDto.getCashHandoverById());
			cashHandoverMaster.setUpdatedBy(cashHandoverDetailsDto.getCashHandoverById());
			cashHandoverMaster.setStatus("A");
			cashHandoverMaster.setStatusId(cashHandoverDetailsDto.getStatusId());
			cashHandoverMaster.setShiftClose(false);
			
			if(cashHandoverDetailsDto.getId()!=null) {
				cashHandoverMaster.setId(cashHandoverDetailsDto.getId());
				cashHandoverMaster.setCobNumber(cashHandoverDetailsDto.getCobNumber());
				SessionFactory.getCurrentSession().update(cashHandoverMaster);
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, cashHandoverMaster);
			}else {
				String cobString="COB";
				char[] otp =GlobalCommonDateUtils.generateRandomNumber(5);
				String cob=cobString+String.valueOf(otp);
				//System.out.println("cob"+cob);
				cashHandoverMaster.setCobNumber(cob);
				SessionFactory.getCurrentSession().save(cashHandoverMaster);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, cashHandoverMaster);
			}
			
			
			
		} catch (Exception e) {
			
			throw e;
		}
	}		
	@Override
	public Response validateUser(CashHandoverDetailsDto cashHandoverDetailsDto) {
		try{
			
			String encyption =  GlobalCommonDateUtils.encrypt(cashHandoverDetailsDto.getHandoverByPassword().trim());
			CashHandoverDetailsDto uservali = (CashHandoverDetailsDto) SessionFactory.getCurrentSession().getNamedQuery("VALIDATE_USER")
					.setInteger("userId",cashHandoverDetailsDto.getCashHandoverById() )
					.setString("password",encyption)
					.setInteger("organizationId", cashHandoverDetailsDto.getOrganizationId())
	                .setInteger("unitId", cashHandoverDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CashHandoverDetailsDto.class)).uniqueResult();
			
			if(uservali!= null) {
				
				String encyption1 =  GlobalCommonDateUtils.encrypt(cashHandoverDetailsDto.getHandoverToPassword().trim());
				CashHandoverDetailsDto uservali1 = (CashHandoverDetailsDto) SessionFactory.getCurrentSession().getNamedQuery("VALIDATE_USER")
						.setInteger("userId",cashHandoverDetailsDto.getCashHandoverToId() )
						.setString("password",encyption1)
						.setInteger("organizationId", cashHandoverDetailsDto.getOrganizationId())
		                .setInteger("unitId", cashHandoverDetailsDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(CashHandoverDetailsDto.class)).uniqueResult();
				
				if(uservali1!= null) {
					return new Response(SUCCESS, null, null,null, uservali);
					
				}else {
					
					return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null,null);
				}
			}else {
				return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null,null);
			}
			
		}catch(HibernateException he){
			throw new HibernateException(he.getMessage());
		}catch(Exception e){
			
			throw e;
		}	
		
	}

	@Override
	public Response saveDenominationmaster(List<DenominationDetailsMasterDto> denominationDetailsMasterDto) {
		
		try {
			List<DenominationDetailsMasterDto> list = new ArrayList<DenominationDetailsMasterDto>();
		
			for(DenominationDetailsMasterDto denominationList : denominationDetailsMasterDto) {
				if(denominationList.getQty() != null && denominationList.getQty() !=0) {

				DenominationDetailsMaster denominationDetailsMaster = new DenominationDetailsMaster();
				denominationDetailsMaster.setAmount(denominationList.getAmount());
				denominationDetailsMaster.setDenomination(denominationList.getDenomination());
				denominationDetailsMaster.setCreatedBy(denominationList.getCreatedBy());
				denominationDetailsMaster.setUpdatedBy(denominationList.getCreatedBy());
				denominationDetailsMaster.setQuantity(denominationList.getQty());
				denominationDetailsMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0),"dd-M-yyyy"));
				denominationDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0),"dd-M-yyyy"));
				denominationDetailsMaster.setCashHandoverTransactionId(denominationList.getCashHandoverTransactionId() !=null ? denominationList.getCashHandoverTransactionId() : null);
				denominationDetailsMaster.setCashInoutId(denominationList.getCashInoutId()!= null ? denominationList.getCashInoutId() : null);
				if(denominationList.getId()!=null) {
					denominationDetailsMaster.setStatus("I");
					denominationDetailsMaster.setId(denominationList.getId());
					sessionFactory.getCurrentSession().update(denominationDetailsMaster);
				}else {
					denominationDetailsMaster.setStatus("A");
					sessionFactory.getCurrentSession().save(denominationDetailsMaster);
				
				}
				}
				
			}
					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getListOfDenominationDetails(DenominationDetailsMasterDto denominationDetailsMasterDto) {
		try {
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(
					"select dm.quantity as \"qty\","
					+ " dm.id as \"id\","
					+ " dm.denomination as \"denomination\","
					+ "ch.remarks as \"remarks\","
					+ "to_char(dm.created_date,'dd/mm/yyyy') as \"createdDate\" ,"
					+ "dm.amount as \"amount\","
					+ "ch.cash_handover_amount as \"cashHandoverAmount\" "
					+ "from public.t_cash_handover_details ch "
					+ "right join public.t_denomination_details dm on ch.id = dm.cash_handover_transaction_id "
					+ "where dm.cash_handover_transaction_id =:cashHandoverTransactionId and dm.status='A'"
					//+ "and dm.created_date='"+ GlobalCommonDateUtils.getDate(denominationDetailsMasterDto.getCreatedDate(),"yyyy-M-dd")+ "'"
					)
					.setParameter("cashHandoverTransactionId", denominationDetailsMasterDto.getCashHandoverTransactionId());

			List<DenominationDetailsMasterDto> denominationlist = sqlQuery.setResultTransformer(Transformers.aliasToBean(DenominationDetailsMasterDto.class)).list();
			

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, denominationlist, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getcashOpeningTransactionList(CashHandoverDetailsDto cashHandoverDetailsDto)
			throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPEN_HANDOVER_MASTER_LIST")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();
			
			if(cashHandoverDetailsDto.getCashHandoverToId()!=0){
				subQuery.append(" AND cashHandoverMaster.cashHandoverToId =:cashHandoverToId ");
			}
			
			
			if(cashHandoverDetailsDto.getCobNumber()!="" && cashHandoverDetailsDto.getCobNumber()!=null){
				subQuery.append(" AND cashHandoverMaster.cobNumber =:cobNumber ");
			}
			
			subQuery.append(" order by cashHandoverMaster.id desc");
			query = query.concat(subQuery.toString());
			
			Query hqlQuery = sessionFactory.getCurrentSession()
					.createQuery(query)
					.setParameter("organizationId",  cashHandoverDetailsDto.getOrganizationId())
					.setParameter("unitId",  cashHandoverDetailsDto.getUnitId())
					.setParameter("fromDate",  cashHandoverDetailsDto.getFromDate())
					.setParameter("toDate",cashHandoverDetailsDto.getToDate())
					.setParameter("cashHandoverById",  cashHandoverDetailsDto.getCashHandoverById());

			if(cashHandoverDetailsDto.getCashHandoverToId()!=0)
				hqlQuery.setParameter("cashHandoverToId", cashHandoverDetailsDto.getCashHandoverToId());

			if(cashHandoverDetailsDto.getCobNumber()!="" && cashHandoverDetailsDto.getCobNumber()!=null)
				hqlQuery.setParameter("cobNumber", cashHandoverDetailsDto.getCashHandoverToId());

			List<CashHandoverDetailsDto> list = hqlQuery.setResultTransformer(Transformers.aliasToBean(CashHandoverDetailsDto.class)).list();
			//System.out.println(list);
		return new Response(SUCCESS, SUCCESS_CODE, null,list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response checkShiftClosingStatus(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {
		try {
			List<CashHandoverDetailsDto> list =sessionFactory.getCurrentSession()
					.getNamedQuery("TO_CHECK_SHIFT_CLOSE_OR_NOT")
					.setInteger("handoverTo", cashHandoverDetailsDto.getCashHandoverToId())
					.setInteger("organizationId", cashHandoverDetailsDto.getOrganizationId())
					.setInteger("unitId", cashHandoverDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CashHandoverDetailsDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response updateShiftCloseFlag(Integer handoverBy) throws ApplicationException {
		try {
			Integer denominationlist = sessionFactory.getCurrentSession().createSQLQuery(
					"update public.t_cash_handover_details set shift_close = true where  cash_handover_to_id =:handoverBy")
					.setParameter("handoverBy", handoverBy)
					.setResultTransformer(Transformers.aliasToBean(Integer.class)).executeUpdate();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response updateStatusToComplete(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {
		try {
			CashHandoverMaster mstr = findById(cashHandoverDetailsDto.getId());
			mstr.setStatusId(cashHandoverDetailsDto.getStatusId());
			mstr.setUpdatedBy(cashHandoverDetailsDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(cashHandoverDetailsDto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
			sessionFactory.getCurrentSession().update(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (Exception e) {
			
			throw e;
		}
	}



	@Override
	public Response inActiveAndActiveEntry(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {

		try {
			Integer denominationlist = sessionFactory.getCurrentSession().createSQLQuery(
					"update public.t_cash_in_out set status = 'I' where  id =:id ")
					.setParameter("id", cashHandoverDetailsDto.getId())
					.setResultTransformer(Transformers.aliasToBean(Integer.class)).executeUpdate();

		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	
	}
}
	

	

