package com.param.global.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.inventory.MaterialInward;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PatientPanCardDocDto;
import com.param.global.model.PatientPanCardDoc;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class PatientPanCardDocDaoDaoImpl  extends GenericDao<PatientPanCardDoc, Integer>implements IPatientPanCardDocDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientPanCardDocDaoDaoImpl.class);


	public PatientPanCardDocDaoDaoImpl() {
		super(PatientPanCardDoc.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	
	@Override
	public Response savePanCardDocument(PatientPanCardDocDto dto) {
		try{
			PatientPanCardDoc master = new PatientPanCardDoc();
			master.setBillId(dto.getBillId());
			master.setDocName(dto.getDocName());
			master.setDocUrl(dto.getDocUrl());
			master.setIsDeposite(dto.getIsDeposite());
			master.setIsPharmacy(dto.getIsPharmacy());
			master.setIsMiscellaneous(dto.getIsMiscellaneous());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setStatus('A');
			master = save(master);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
        logger.error("savePanCardDocument failed", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}


	@Override
	public Response getPanCardDetailsById(int billId, int screenId) throws ApplicationException {
		try{
			
			 StringBuilder sb = new  StringBuilder();
			 			sb.append("select tppcd.patient_pan_card_doc_id as \"id\" , tppcd.doc_url as \"docUrl\"  from public.t_patient_pan_card_doc tppcd where tppcd.bill_id =:billId ");
					       
					        if(screenId == 0)
					        {
					        	sb.append(" AND tppcd.is_pharmacy = false and tppcd.is_deposite = false and tppcd.is_miscellaneous = false order by 1 desc limit 1 ");
					        }
					        if(screenId == 1)
					        {
					        	sb.append(" AND tppcd.is_deposite = true order by 1 desc limit 1 ");
					        }
					        if(screenId == 2) 
					        {
					        	sb.append(" AND tppcd.is_pharmacy = true order by 1 desc limit 1 ");
					        }
					        if(screenId == 3) 
					        {
					        	sb.append(" AND tppcd.is_miscellaneous = true order by 1 desc limit 1 ");
					        }
					       
		        Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		        PatientPanCardDocDto patientPanCardDocDto = (PatientPanCardDocDto) createdQuery
						.setParameter("billId", billId)
		            .setResultTransformer(Transformers.aliasToBean(PatientPanCardDocDto.class)).uniqueResult();
		        return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, patientPanCardDocDto);
		} catch (Exception e)
		{
		logger.error("getPanCardDetailsById failed", e);
		}
		return null;
	}


	@Override
	public Response inwardDataupdate(Double amount, Integer mateinwardid, Integer id) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createSQLQuery("update procurement.t_grn_detail_creditnote_amount set is_deleted=true where id =:id ")
					.setParameter("id", id)
					.executeUpdate();

			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE inventory.t_material_inwards SET credit_note_amount = credit_note_amount + :amount, "
							+ "available_amount = available_amount + :amount, credit_tax_amount = credit_tax_amount - :amount "
							+ "WHERE id = :mateinwardid")
					.setParameter("amount", amount)
					.setParameter("mateinwardid", mateinwardid)
					.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("inwardDataupdate failed", he);
		} catch (Exception e) {
		logger.error("inwardDataupdate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response addConsumeAmount(Double consumenetamount, String rgnumber, String inwordno)
			throws ApplicationException {
		try {

			sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE inventory.t_material_inwards SET credit_note_amount = credit_note_amount - :consumenetamount, "
							+ "available_amount = available_amount - :consumenetamount, credit_tax_amount = credit_tax_amount + :consumenetamount "
							+ "WHERE inword_no = :inwordNo")
					.setParameter("consumenetamount", consumenetamount)
					.setParameter("inwordNo", inwordno)
					.executeUpdate();



			StringBuilder sb = new  StringBuilder();
	 			sb.append("select tppcd.id as \"id\" from  inventory.t_material_inwards tppcd where tppcd.inword_no =:inwordno order by tppcd.id desc limit 1 ");
			       
			       
     Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
     MaterialInward patientPanCardDocDto = (MaterialInward) createdQuery
				.setParameter("inwordno", inwordno)
         .setResultTransformer(Transformers.aliasToBean(MaterialInward.class)).uniqueResult();
     return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, patientPanCardDocDto.getId());
			

				} catch (HibernateException he) {
				logger.error("addConsumeAmount failed", he);
				} catch (Exception e) {
				logger.error("addConsumeAmount failed", e);
				}
				return new Response(ERROR, null, null, null, null);
	}


}
