package com.param.opd.coversheet.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.model.PrescriptionDetailsMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class MedicationStopDaoImpl extends GenericDao<PrescriptionDetailsMaster, Integer>
implements IMedicationStopDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(MedicationStopDaoImpl.class);


public MedicationStopDaoImpl() {
super(PrescriptionDetailsMaster.class);
// TODO Auto-generated constructor stub
}

@Autowired
SessionFactory sessionFactory;

@Override
public Response saveJustificationForMedicationStop(PrescriptionDto medicationDTOs) throws ApplicationException {
	try {
		PrescriptionDetailsMaster prescriptionDetailsMaster=findById(medicationDTOs.getPrescriptionDetailsid());
		prescriptionDetailsMaster.setIsMedicationStop(medicationDTOs.getIsMedicationStop());
		prescriptionDetailsMaster.setJustificationStop(medicationDTOs.getJustificationStop());
		prescriptionDetailsMaster.setUpdatedBy(medicationDTOs.getUpdatedBy());
		prescriptionDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(medicationDTOs.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
	 
	update(prescriptionDetailsMaster);

    return new Response(SUCCESS, SUCCESS_CODE, "Medication discontinued successfully.", null, null);
	
	} catch (HibernateException he) {
	logger.error("saveJustificationForMedicationStop failed", he);
	} catch (Exception e) {
	logger.error("saveJustificationForMedicationStop failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response saveJustificationForMedicationDelete(PrescriptionDto medicationDTOs) throws ApplicationException {
	try {
		/*PrescriptionDetailsMaster prescriptionDetailsMaster=findById(medicationDTOs.getPrescriptionDetailsid());
		prescriptionDetailsMaster.setStatusId(9);
		prescriptionDetailsMaster.setJustificationStop(medicationDTOs.getJustificationStop());
		prescriptionDetailsMaster.setUpdatedBy(medicationDTOs.getUpdatedBy());
		prescriptionDetailsMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(medicationDTOs.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));*/
		
		Session session=sessionFactory.getCurrentSession();
		Date date= TimeZoneComponent.getDateByzone(medicationDTOs.getUnitId());
		session.createSQLQuery("UPDATE pharmacy.t_prescription_detail " +
		" SET status_id=9," +
		" justification_stop='"+medicationDTOs.getJustificationStop()+"' ,"+
		" updated_by="+medicationDTOs.getUpdatedBy()+","+
		" updated_date='" + date +"'"+
		" WHERE id= "+medicationDTOs.getPrescriptionDetailsid())
		.executeUpdate();
	 
//	update(prescriptionDetailsMaster);

    return new Response(SUCCESS, SUCCESS_CODE, "Medication deleted successfully.", null, null);
	
	} catch (HibernateException he) {
	logger.error("saveJustificationForMedicationDelete failed", he);
	} catch (Exception e) {
	logger.error("saveJustificationForMedicationDelete failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getCountOfMedicationsByPresciptionId(PrescriptionDto medicationDTOs) throws ApplicationException {
	try {
		BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COUNT_OF_MEDICATIONS_BY_PRESCIPTION_ID")
				.setParameter("prescriptionId", medicationDTOs.getPrescriptionId())
				.setParameter("organizationId", medicationDTOs.getOrganizationId())
				.uniqueResult();
			if (count.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
			}
	}catch (Exception e) {
	logger.error("getCountOfMedicationsByPresciptionId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response saveMedicationDeleteForPrescription(PrescriptionDto medicationDTOs) throws ApplicationException {
	try {
		Integer count = null;

		String Query1 =  "update pharmacy.t_prescription set status_id = case when (select "
				+ "	count(tpd.id) "
				+ "from "
				+ "	pharmacy.t_prescription_detail tpd "
				+ "where "
				+ "	tpd.prescription_id = "+medicationDTOs.getPrescriptionId()
				+ "	and tpd.status_id <> 9) = 0 then 9 else status_id  end  where id = "+medicationDTOs.getPrescriptionId();


		String Query2 = "update pharmacy.t_issue set status_id = case when (select "
				+ "	count(i.id) "
				+ "from "
				+ "	pharmacy.t_issue i "
				+ "where "
				+ "	i.prescription_id =  "+medicationDTOs.getPrescriptionId()
				+ "	and i.status_id <> 9) = 0 then 9 else status_id  end  where prescription_id =" +medicationDTOs.getPrescriptionId();

		String Query3 = "update pharmacy.t_assign_picker set status_id = case when (select "
				+ "	count(ap.id) "
				+ "from "
				+ "	pharmacy.t_assign_picker ap "
				+ "where "
				+ "	ap.prescription_id =  "+medicationDTOs.getPrescriptionId()
				+ "	and ap.status_id <> 9) = 0 then 9 else status_id  end  where prescription_id =" +medicationDTOs.getPrescriptionId();


//		" update pharmacy.t_prescription set status_id = 9 where id="+medicationDTOs.getPrescriptionId()
//		" update pharmacy.t_issue set status_id = 9 where prescription_id ="+medicationDTOs.getPrescriptionId()
//		" update pharmacy.t_assign_picker set status_id = 9 where prescription_id ="+medicationDTOs.getPrescriptionId()
		count =  sessionFactory.getCurrentSession().createSQLQuery(Query1)
		.executeUpdate();
		
		 if(count>0) {
			 
			 sessionFactory.getCurrentSession().createSQLQuery(Query2)
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(Query3)
				.executeUpdate();
		 }
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveMedicationDeleteForPrescription failed", he);
		} catch (Exception e) {
		logger.error("saveMedicationDeleteForPrescription failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response updateAssignPickerAndIssueDetails(PrescriptionDto medicationDTOs) throws ApplicationException {
	try {
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_assign_picker_detail set status_id = 9 where prescription_detail_id ="+medicationDTOs.getPrescriptionDetailsid())
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue_detail set status_id = 9 where prescription_detail_id ="+medicationDTOs.getPrescriptionDetailsid())
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_sale_detail set status_id = 9 where prescription_detail_id ="+medicationDTOs.getPrescriptionDetailsid())
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue set status_id = 9 where prescription_id ="+medicationDTOs.getPrescriptionId())
				.executeUpdate();
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateAssignPickerAndIssueDetails failed", he);
		} catch (Exception e) {
		logger.error("updateAssignPickerAndIssueDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public Response checkStatusFromIssueDetails(PrescriptionDto medicationDTOs) throws ApplicationException {
	try {
		
		String query = "select " + 
				"	pres.id as \"prescriptionId\", " + 
				"	issue.status_id as \"statusIdIssue\", " + 
				"	presDtl.id as \"prescriptionDetailsid\" " + 
				" from " + 
				"	pharmacy.t_prescription pres " + 
				" left join pharmacy.t_prescription_detail presDtl on " + 
				"	presDtl.prescription_id = pres.id " + 
				" left join pharmacy.t_issue_detail issue on " + 
				"	issue.prescription_detail_id = presDtl.id " + 
				" where " + 
				"	presDtl.is_medication_stop = 'N' " + 
			//	"	and presDtl.status_id != 9 " + 
				"	and presDtl.id = :prescriptionDetailsid " + 
				"	and issue.status_id!= 2"
				;
		
		StringBuffer sb = new StringBuffer(query);

		List<MedicationDTO> list= getSession().createSQLQuery(sb.toString())
				.setParameter("prescriptionDetailsid", medicationDTOs.getPrescriptionDetailsid())
		.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list ,null);

	} catch (HibernateException he) {
		throw new HibernateException(he.getMessage());
	} catch (Exception e) {
	logger.error("checkStatusFromIssueDetails failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

}



	@Override
	public Response checkStatusFromIssueDetailsForUpdate(Integer prescriptionId,Integer prescriptionDetailsid ,Integer patientId,
	Integer organizationId) {
	try {

		String query = "select " +
		" pres.id as \"prescriptionId\", " +
		" issue.status_id as \"statusIdIssue\", " +
		" presDtl.id as \"prescriptionDetailsid\" " +
		" from " +
		" pharmacy.t_prescription pres " +
		" left join pharmacy.t_prescription_detail presDtl on " +
		" presDtl.prescription_id = pres.id " +
		" left join pharmacy.t_issue_detail issue on " +
		" issue.prescription_detail_id = presDtl.id " +
		" where " +
		" presDtl.is_medication_stop = 'N' " +
		" and presDtl.status_id != 9 " +
		" and presDtl.id = :prescriptionDetailsid " +
		" and issue.status_id!= 2"
		;
		
		StringBuffer sb = new StringBuffer(query);
		
		List<MedicationDTO> list= getSession().createSQLQuery(sb.toString())
		.setParameter("prescriptionDetailsid", prescriptionDetailsid)
		.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list ,null);
		
		} catch (HibernateException he) {
		throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("checkStatusFromIssueDetailsForUpdate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		
		}

	@Override
	public Response checkParentSubstituteId(List<BigInteger> predetailIds) throws ApplicationException {
		try {

			String sql = "select " + 
					"	tpd.id as \"prescriptionDetailsid\" " + 
					"from " + 
					"	pharmacy.t_prescription_detail tpd " + 
					"where " + 
					"	tpd.parent_substitute_id in(:predetailIds)" ;
			
				StringBuffer sb = new StringBuffer(sql);
				List<MedicationDTO> list = getSession().createSQLQuery(sb.toString())
				.setParameterList("predetailIds", predetailIds)
				.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list ,null);


		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("checkParentSubstituteId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response updateAssignPickerAndIssueDetailsTest(PrescriptionDto medicationDTOs) throws ApplicationException {
		try {
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_assign_picker_detail set status_id = 9 where prescription_detail_id ="+medicationDTOs.getPrescriptionDetailsid())
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue_detail set status_id = 9 where prescription_detail_id ="+medicationDTOs.getPrescriptionDetailsid())
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_sale_detail set status_id = 9 where prescription_detail_id ="+medicationDTOs.getPrescriptionDetailsid())
				.executeUpdate();
			 
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateAssignPickerAndIssueDetailsTest failed", he);
		} catch (Exception e) {
		logger.error("updateAssignPickerAndIssueDetailsTest failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkStatusFromIssueDetailsMultipleMedication(PrescriptionDto medicationDTOs)
			throws ApplicationException {
		try {
			
			String query = "select " + 
					"	pres.id as \"prescriptionId\", " + 
					"	issue.status_id as \"statusIdIssue\", " + 
					"	presDtl.id as \"prescriptionDetailsid\" " + 
					" from " + 
					"	pharmacy.t_prescription pres " + 
					" left join pharmacy.t_prescription_detail presDtl on " + 
					"	presDtl.prescription_id = pres.id " + 
					" left join pharmacy.t_issue_detail issue on " + 
					"	issue.prescription_detail_id = presDtl.id " + 
					" where " + 
					"	presDtl.is_medication_stop = 'N' " + 
					"	and presDtl.id in (:ids) " + 
					"	and issue.status_id!= 2"
					;
			
			StringBuffer sb = new StringBuffer(query);

			List<MedicationDTO> list= getSession().createSQLQuery(sb.toString())
					.setParameterList("ids", medicationDTOs.getOrderDetailsIds())
			.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list ,null);

		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("checkStatusFromIssueDetailsMultipleMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

   }

	@Override
	public Response saveJustificationForMedicationDeleteMultipleMedication(PrescriptionDto medicationDTOs)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Date date= TimeZoneComponent.getDateByzone(medicationDTOs.getUnitId());
			session.createSQLQuery("UPDATE pharmacy.t_prescription_detail " +
			" SET status_id=9," +
			" justification_stop='"+medicationDTOs.getJustificationStop()+"' ,"+
			" updated_by="+medicationDTOs.getUpdatedBy()+","+
			" updated_date='" + date +"'"+
			" WHERE id in ("+medicationDTOs.getOrderDetailsIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +")")
			.executeUpdate();
	    return new Response(SUCCESS, SUCCESS_CODE, "Medication deleted successfully.", null, null);
		
		} catch (HibernateException he) {
		logger.error("saveJustificationForMedicationDeleteMultipleMedication failed", he);
		} catch (Exception e) {
		logger.error("saveJustificationForMedicationDeleteMultipleMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateAssignPickerAndIssueDetailsMultipleMedication(PrescriptionDto medicationDTOs)
			throws ApplicationException {
		try {
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_assign_picker_detail set status_id = 9 where prescription_detail_id in ("+medicationDTOs.getOrderDetailsIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +")")
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue_detail set status_id = 9 where prescription_detail_id in ("+medicationDTOs.getOrderDetailsIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +")")
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_sale_detail set status_id = 9 where prescription_detail_id in ("+medicationDTOs.getOrderDetailsIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +")")
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue set status_id = 9 where prescription_id in ("+medicationDTOs.getOrderDetailsIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +")")
				.executeUpdate();
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateAssignPickerAndIssueDetailsMultipleMedication failed", he);
		} catch (Exception e) {
		logger.error("updateAssignPickerAndIssueDetailsMultipleMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveMedicationDeleteForPrescriptionMultipleMedication(PrescriptionDto medicationDTOs)
			throws ApplicationException {
		try {
			Integer count = null;

			String Query1 =  "update pharmacy.t_prescription set status_id = case when (select "
					+ "	count(tpd.id) "
					+ "from "
					+ "	pharmacy.t_prescription_detail tpd "
					+ "where "
					+ "	tpd.prescription_id in ( "+medicationDTOs.getOrderPrescriptionIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") "
					+ "	and tpd.status_id <> 9) = 0 then 9 else status_id  end  where id in ( "+medicationDTOs.getOrderPrescriptionIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") ";


			String Query2 = "update pharmacy.t_issue set status_id = case when (select "
					+ "	count(i.id) "
					+ "from "
					+ "	pharmacy.t_issue i "
					+ "where "
					+ "	i.prescription_id in (  "+medicationDTOs.getOrderPrescriptionIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") "
					+ "	and i.status_id <> 9) = 0 then 9 else status_id  end  where prescription_id in ( " +medicationDTOs.getOrderPrescriptionIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") ";

			String Query3 = "update pharmacy.t_assign_picker set status_id = case when (select "
					+ "	count(ap.id) "
					+ "from "
					+ "	pharmacy.t_assign_picker ap "
					+ "where "
					+ "	ap.prescription_id in ("+medicationDTOs.getOrderPrescriptionIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") "
					+ "	and ap.status_id <> 9) = 0 then 9 else status_id  end  where prescription_id in (" +medicationDTOs.getOrderPrescriptionIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") ";


			count =  sessionFactory.getCurrentSession().createSQLQuery(Query1)
			.executeUpdate();
			
			 if(count>0) {
				 
				 sessionFactory.getCurrentSession().createSQLQuery(Query2)
					.executeUpdate();
				 
				 sessionFactory.getCurrentSession().createSQLQuery(Query3)
					.executeUpdate();
			 }
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("saveMedicationDeleteForPrescriptionMultipleMedication failed", he);
			} catch (Exception e) {
			logger.error("saveMedicationDeleteForPrescriptionMultipleMedication failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateAssignPickerAndIssueDetailsTestMultipleMedication(PrescriptionDto medicationDTOs)
			throws ApplicationException {
		try {
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_assign_picker_detail set status_id = 9 where prescription_detail_id in ("+medicationDTOs.getOrderDetailsIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") ")
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue_detail set status_id = 9 where prescription_detail_id in ("+medicationDTOs.getOrderDetailsIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") ")
				.executeUpdate();
			 
			 sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_sale_detail set status_id = 9 where prescription_detail_id in ("+medicationDTOs.getOrderDetailsIds().toString().replaceAll("\\[", "").replaceAll("\\]", "") +") ")
				.executeUpdate();
			 
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateAssignPickerAndIssueDetailsTestMultipleMedication failed", he);
		} catch (Exception e) {
		logger.error("updateAssignPickerAndIssueDetailsTestMultipleMedication failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
