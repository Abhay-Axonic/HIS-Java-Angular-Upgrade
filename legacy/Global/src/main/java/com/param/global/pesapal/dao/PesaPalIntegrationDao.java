package com.param.global.pesapal.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.pesapal.dto.PesapalPaymentDto;
import com.param.global.pesapal.model.PesaPalTransactionDetail;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class PesaPalIntegrationDao extends GenericDao<PesaPalTransactionDetail, Integer>
		implements IPesaPalIntegrationDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(PesaPalIntegrationDao.class);

	public PesaPalIntegrationDao() {
		super(PesaPalTransactionDetail.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getPesaPalAuthorizationData(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			PesapalPaymentDto dto = (PesapalPaymentDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select appConfig.pesapal_consumer_key as \"consumerKey\","
							+ " appConfig.pesapal_consumer_secret_key as \"consumerSecretKey\"  "
							+ " from public.t_unit_application_billing_configuration appConfig where appConfig.unit_id=:unitId "
							+ " and appConfig.organization_id=:orgId and appConfig.status='A' ")
					.setParameter("orgId", reqDto.getOrganizationId()).setParameter("unitId", reqDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PesapalPaymentDto.class)).uniqueResult();

			if (dto != null) {
				return new Response<>(SUCCESS, null, null, null, dto);
			} else {
				return new Response<>(ERROR, ERROR_CODE, "Consumer key and secret key not set for unit", null, null);
			}

		} catch (HibernateException he) {
			logger.error("getPesaPalAuthorizationData failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPesaPalAuthorizationData failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savePesaPalPaymentRequestDetails(PesapalPaymentDto reqDto) {
		try {
			PesaPalTransactionDetail pesaPalTransactionDetail=new PesaPalTransactionDetail();
			pesaPalTransactionDetail.setAmount(Integer.parseInt(reqDto.getAmount()));
			pesaPalTransactionDetail.setPhoneNumber(reqDto.getPhoneNumber());
			pesaPalTransactionDetail.setReferenceId(reqDto.getTransactionReferenceId());
			if(reqDto.getEmailId()!=null)
			pesaPalTransactionDetail.setEmailId(reqDto.getEmailId());
			pesaPalTransactionDetail.setFirstName(reqDto.getFirstName());
			pesaPalTransactionDetail.setLastName(reqDto.getLastName());
			pesaPalTransactionDetail.setTransactionStatus("POSTED");
			pesaPalTransactionDetail.setOrganizationId(reqDto.getOrganizationId());
			pesaPalTransactionDetail.setUnitId(reqDto.getUnitId());
			pesaPalTransactionDetail.setCreatedBy(reqDto.getCreatedBy());
			pesaPalTransactionDetail.setUpdatedBy(reqDto.getUpdatedBy());
			pesaPalTransactionDetail.setCreatedDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()));
			pesaPalTransactionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()));
			pesaPalTransactionDetail.setStatus('A');
			sessionFactory.getCurrentSession().save(pesaPalTransactionDetail);
		} catch (HibernateException he) {
			logger.error("savePesaPalPaymentRequestDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("savePesaPalPaymentRequestDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePesapalTransactionStatus(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			if(reqDto.getTransactionStatus().equals("CONSUMED")) {
			sessionFactory.getCurrentSession().createQuery("Update PesaPalTransactionDetail transactionDet "
					+ " set transactionDet.transactionStatus=:status,"
					+ " transactionDet.transactionId=:transactionId,transactionDet.updatedBy=:userId,transactionDet.updatedDate='"+TimeZoneComponent.getDateByzone(reqDto.getUnitId())+"' "
					+ " where transactionDet.transactionId=:transactionId" )
			.setParameter("status", reqDto.getTransactionStatus())
			.setParameter("transactionId", reqDto.getTransactionId())
			.setParameter("userId", reqDto.getUpdatedBy())
			.executeUpdate();
			}else {
				sessionFactory.getCurrentSession().createQuery("Update PesaPalTransactionDetail transactionDet "
						+ " set transactionDet.transactionStatus=:status,transactionDet.paymentMode=:payMode,"
						+ " transactionDet.transactionId=:transactionId "
						+ " where transactionDet.referenceId=:transReferenceId" )
				.setParameter("status", reqDto.getTransactionStatus())
				.setParameter("transReferenceId", reqDto.getTransactionReferenceId())
				.setParameter("transactionId", reqDto.getTransactionId())
				.setParameter("payMode", reqDto.getTransactionType())
				.executeUpdate();
			}
			return new Response<>(SUCCESS,SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			logger.error("updatePesapalTransactionStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePesapalTransactionStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTransactionReferenceIdByTransactionId(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			PesaPalTransactionDetail detailDto;
			if(reqDto.getTransactionReferenceId()==null) {
			 detailDto=(PesaPalTransactionDetail)sessionFactory.getCurrentSession().createQuery("select transactionDet.referenceId as referenceId, "
					+ " transactionDet.transactionStatus as transactionStatus From PesaPalTransactionDetail transactionDet "
					+ " where transactionDet.transactionId=:transactionId" )
			.setParameter("transactionId", reqDto.getTransactionId())
			.setResultTransformer(Transformers.aliasToBean(PesaPalTransactionDetail.class))
			.uniqueResult();
			}else {
				detailDto=(PesaPalTransactionDetail)sessionFactory.getCurrentSession().createQuery("select transactionDet.referenceId as referenceId, "
						+ " transactionDet.transactionStatus as transactionStatus From PesaPalTransactionDetail transactionDet "
						+ " where transactionDet.referenceId=:referenceId" )
				.setParameter("referenceId", reqDto.getTransactionReferenceId())
				.setResultTransformer(Transformers.aliasToBean(PesaPalTransactionDetail.class))
				.uniqueResult();
			}
			if(detailDto!=null) {
			return new Response<>(SUCCESS,SUCCESS_CODE, null, null, detailDto);
			}else {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		} catch (HibernateException he) {
			logger.error("getTransactionReferenceIdByTransactionId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getTransactionReferenceIdByTransactionId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTransactionDetailByTransactionId(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			PesaPalTransactionDetail detailDto=(PesaPalTransactionDetail)sessionFactory.getCurrentSession().createQuery("select transactionDet.referenceId as  referenceId,"
					+ " transactionDet.amount as amount,transactionDet.paymentMode as paymentMode,transactionDet.transactionStatus as transactionStatus"
					+ " From PesaPalTransactionDetail transactionDet "
					+ " where transactionDet.transactionId=:transactionId" )
			.setParameter("transactionId", reqDto.getTransactionId())
			.setResultTransformer(Transformers.aliasToBean(PesaPalTransactionDetail.class))
			.uniqueResult();
			return new Response<>(SUCCESS,SUCCESS_CODE, null, null, detailDto);
		} catch (HibernateException he) {
			logger.error("getTransactionDetailByTransactionId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getTransactionDetailByTransactionId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
