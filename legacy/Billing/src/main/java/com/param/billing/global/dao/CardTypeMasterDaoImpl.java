package com.param.billing.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.dto.CardTypeMasterDto;
import com.param.billing.global.model.CardTypeMaster;
import com.param.global.common.GlobalCommonDateUtils;


import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class CardTypeMasterDaoImpl extends GenericDao<CardTypeMaster, Integer> implements ICardTypeMasterDao,ICommonConstants{

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;
	
	public CardTypeMasterDaoImpl() {
		super(CardTypeMaster.class);
	}
	
	@Override
	public Response gerActiveCardTypeMaster(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			List cardTypeList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_CARD_TYPE_NAME")
					//.setInteger("unitId", cardTypeMasterDto.getUnitId())
					.setParameter("orgId", cardTypeMasterDto.getOrgnisationId())
					.setResultTransformer(Transformers.aliasToBean(CardTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, cardTypeList, null);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCardTypeByName(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			List<CardTypeMasterDto> cardTypeMasterDtos = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CARD_TYPE_LIST_BY_NAME")
					.setParameter("cardTypeName", cardTypeMasterDto.getCardTypeName().toLowerCase())
					.setParameter("orgId", cardTypeMasterDto.getOrgnisationId())
					.setResultTransformer(Transformers.aliasToBean(CardTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cardTypeMasterDtos, null);

		} catch (HibernateException he) {
			throw he;

		} catch (Exception e) {
			
			throw e;
		} finally {
			 ;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addCardTypeMaster(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			CardTypeMaster cardTypeMaster = mapper.map(cardTypeMasterDto, CardTypeMaster.class,
					"cardTypeMasterDto_to_cardTypeMaster");
			cardTypeMaster = save(cardTypeMaster);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, cardTypeMaster);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCardTypeMasterList(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			List<CardTypeMasterDto> cardTypeMasterDtos = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CARD_TYPE_LIST")
					.setParameter("orgId", cardTypeMasterDto.getOrgnisationId())
					.setResultTransformer(Transformers.aliasToBean(CardTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cardTypeMasterDtos, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		} finally {
			 ;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCardTypeStatus(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {

			CardTypeMaster cardTypeMaster = findById(cardTypeMasterDto.getCardTypeId());
			cardTypeMaster.setUpdatedBy(cardTypeMasterDto.getUpdatedBy());
			cardTypeMaster.setStatus(cardTypeMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			cardTypeMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(cardTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(cardTypeMaster);
			return new Response<CardTypeMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, cardTypeMaster.getOrgnisationId());

		} catch (HibernateException he) {
			throw he;
			//return new Response<CardTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			throw e;
			//return new Response<CardTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response getCardTypeByNameNotId(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			List<CardTypeMasterDto> cardTypeMasterDtos = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CARD_TYPE_LIST_BY_NAME_NOT_ID")
					.setParameter("cardTypeId", cardTypeMasterDto.getCardTypeId())
					.setParameter("cardTypeName", cardTypeMasterDto.getCardTypeName().toLowerCase())
					.setParameter("orgId", cardTypeMasterDto.getOrgnisationId())
					.setResultTransformer(Transformers.aliasToBean(CardTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cardTypeMasterDtos, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		} finally {
			 ;
		}
		//return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCardTypeMaster(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException {
		try {
			CardTypeMaster cardTypeMaster = findById(cardTypeMasterDto.getCardTypeId());
			cardTypeMaster.setCardTypeName(cardTypeMasterDto.getCardTypeName());
			cardTypeMaster.setCardTypeCode(cardTypeMasterDto.getCardTypeCode());
			cardTypeMaster.setUpdatedBy(cardTypeMasterDto.getUpdatedBy());
			cardTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(cardTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(cardTypeMaster);
			return new Response<CardTypeMasterDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, cardTypeMaster);

		} catch (HibernateException he) {
			throw he;
			//return new Response<CardTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			throw e;
		//	return new Response<CardTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	
}
