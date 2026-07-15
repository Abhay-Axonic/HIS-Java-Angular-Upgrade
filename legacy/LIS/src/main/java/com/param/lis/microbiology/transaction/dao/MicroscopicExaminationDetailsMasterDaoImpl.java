package com.param.lis.microbiology.transaction.dao;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.micro.MicroscopicExaminationDetailsMaster;
import com.param.entity.lis.micro.MicroscopicExaminationSubDetailsMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MicroscopicExaminationDetailsMasterDaoImpl extends GenericDao<MicroscopicExaminationDetailsMaster, Integer>
		implements ImicroscopicExaminationDetailsMaster, ICommonConstants, ITransactionConstants, IError {

	final static Logger logger = Logger.getLogger(MicroscopicExaminationDetailsMasterDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	public MicroscopicExaminationDetailsMasterDaoImpl() {
		super(MicroscopicExaminationDetailsMaster.class);
	}

	@Override
	public Response savemicroscopicExaminationDetailsMaster(
			MicroscopicExaminationDetailsMaster microscopicExaminationDetailsMaster) throws ApplicationException {

		try {

			if (microscopicExaminationDetailsMaster != null) {
					MicroscopicExaminationDetailsMaster microscopicExaDetailsMst = save(
							microscopicExaminationDetailsMaster);
					if (microscopicExaDetailsMst.getListMicroscopicExaminationSubDetailsMaster() != null) 
					{
						for (Iterator iterator = microscopicExaDetailsMst
								.getListMicroscopicExaminationSubDetailsMaster().iterator(); iterator.hasNext();) {
							MicroscopicExaminationSubDetailsMaster microscopicExaminationSubDetailsMaster = (MicroscopicExaminationSubDetailsMaster) iterator
									.next();
								microscopicExaminationSubDetailsMaster
										.setExaminationDetailsId(microscopicExaDetailsMst.getExaminationDetailsId());
								if(microscopicExaminationSubDetailsMaster.getMicroexaSubDetailsId()==0) 
								{
								    microscopicExaminationSubDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(microscopicExaminationSubDetailsMaster.getOrgUnitId()).getTime());
									sessionFactory.getCurrentSession().save(microscopicExaminationSubDetailsMaster);
								}else {
								     microscopicExaminationSubDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(microscopicExaminationSubDetailsMaster.getOrgUnitId()).getTime());
									sessionFactory.getCurrentSession().update(microscopicExaminationSubDetailsMaster);
								}
						}
							return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
					} else {
						return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
					}

			} else {
				return new Response(ERROR, ERR_500, "Examination Details Result list is empty", null, null);

			}
		} catch (HibernateException er) {
			logger.error("Exection", er);throw er;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}

	}

	@Override
	public Response updatemicroscopicExaminationDetailsMaster(
			MicroscopicExaminationDetailsMaster microscopicExaminationDetailsMaster) throws ApplicationException {
		try {

			if (microscopicExaminationDetailsMaster != null) {
					if (microscopicExaminationDetailsMaster.getListMicroscopicExaminationSubDetailsMaster() != null) 
					{
						for (Iterator iterator = microscopicExaminationDetailsMaster
								.getListMicroscopicExaminationSubDetailsMaster().iterator(); iterator.hasNext();) 
						{
							MicroscopicExaminationSubDetailsMaster microscopicExaminationSubDetailsMaster = (MicroscopicExaminationSubDetailsMaster) iterator
									.next();
							if (microscopicExaminationSubDetailsMaster.getMicroexaSubDetailsId() == 0) {
								microscopicExaminationSubDetailsMaster.setExaminationDetailsId(
										microscopicExaminationDetailsMaster.getExaminationDetailsId());
								microscopicExaminationSubDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(microscopicExaminationSubDetailsMaster.getOrgUnitId()).getTime());
								sessionFactory.getCurrentSession().save(microscopicExaminationSubDetailsMaster);
							} else {
							     microscopicExaminationSubDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(microscopicExaminationSubDetailsMaster.getOrgUnitId()).getTime());
								sessionFactory.getCurrentSession().update(microscopicExaminationSubDetailsMaster);
							}

						}
							return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
					} else {
						return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
					}

			} else {
				return new Response(ERROR, ERR_500, "Examination Details Result list is empty", null, null);

			}
		} catch (HibernateException er) {
			logger.error("Exection", er);throw er;
		} catch (Exception e) {
			logger.error("Exception", e);throw e;
		}
	}

}
