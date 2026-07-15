package com.param.data.display;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.actionmaster.controller.HibernateUtil;
import com.param.data.dto.CommonDiagnosisDto;
import com.param.data.dto.CommonEncounterDto;
import com.param.data.dto.CommonHistoryDto;
import com.param.data.dto.CommonLaboratoryDto;
import com.param.data.dto.CommonPhysicalExaminationDto;
import com.param.data.dto.CommonPrescriptionsDto;
import com.param.data.dto.CommonProcedureDto;
import com.param.data.dto.CommonProvisionDiagonosisDto;
import com.param.data.dto.CommonRadiologyDto;
import com.param.data.dto.CommonReviewNotesDto;
import com.param.data.dto.CommonVitalsDto;
import com.param.data.dto.OtherAppDataReqDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OtherDBDataDisplayDaoImpl implements IOtherDBDataDisplayDao, ICommonConstants,Quries {

	@Override
	public Response encounterList(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			
			StringBuilder sb= new StringBuilder(Quries.GET_ENCOUNTER_LIST);
			sb.append((reqDto.getEncounterId() != null && reqDto.getEncounterId() != null && !reqDto.getEncounterId().isEmpty() && !reqDto.getEncounterId().isEmpty()) ? " and  care_enc.encounter_nr = "+reqDto.getEncounterId().trim():"");
			if((reqDto.getFromDate() != null && reqDto.getToDate() != null && !reqDto.getFromDate().isEmpty() && !reqDto.getToDate().isEmpty()))
			{
				sb.append(" and date_format(care_enc.encounter_date,\"%d-%m-%Y\") >=:FromDate ");
				sb.append(" and  date_format(care_enc.encounter_date,\"%d-%m-%Y\") <=:ToDate ");
			}
			Query SQLqUERY = s
					.createSQLQuery(sb.toString())
					.setParameter("pid", reqDto.getPid());
			
			if((reqDto.getFromDate() != null && reqDto.getToDate() != null && !reqDto.getFromDate().isEmpty() && !reqDto.getToDate().isEmpty())) {
				SQLqUERY.setParameter("FromDate", reqDto.getFromDate());
				SQLqUERY.setParameter("ToDate", reqDto.getToDate());
			}
			List<CommonEncounterDto> list = 	SQLqUERY.setResultTransformer(Transformers.aliasToBean(CommonEncounterDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response quickSummery(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonEncounterDto> list = s
					.createSQLQuery(Quries.GET_ENCOUNTER_LIST)
					.setParameter("pid", reqDto.getPid())
					.setResultTransformer(Transformers.aliasToBean(CommonEncounterDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getTriageList(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonVitalsDto> list = s
					.createSQLQuery(Quries.GET_TRIAGE)
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonVitalsDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getHistory(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonHistoryDto> list = s
					.createSQLQuery(Quries.GET_HISTORY)
					.setParameter("encounterId",reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonHistoryDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getPhysicalExamination(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonPhysicalExaminationDto> list = s
					.createSQLQuery(Quries.GET_PHYSICAL_EXAMINATION)
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonPhysicalExaminationDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getPrecriptionList(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonPrescriptionsDto> list = s
					.createSQLQuery(Quries.GET_PRESCRIPTION)
					.setParameter("encounterId",reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonPrescriptionsDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getLabOrderedsList(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonLaboratoryDto> list = s
					.createSQLQuery(Quries.GET_DIAGNOSYS)
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonLaboratoryDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getDiagnosysList(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonDiagnosisDto> list = s
					.createSQLQuery(Quries.GET_DIAGNOSYS)
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonDiagnosisDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getRadiologyOrdersList(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonRadiologyDto> list = s
					.createSQLQuery(Quries.GET_DIAGNOSYS)
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonRadiologyDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getProceduresList(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonProcedureDto> list = s
					.createSQLQuery(Quries.GET_PROCEDURES)
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonProcedureDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getreviewNotesList(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonReviewNotesDto> list = s
					.createSQLQuery(Quries.GET_REVIEW_NOTES)
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonReviewNotesDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getProvisionalDiagonsys(OtherAppDataReqDto reqDto) throws BillingException {
		try {

			Session s = HibernateUtil.getMySqlSessionFactory().openSession();
			Transaction tx = s.beginTransaction();
			List<CommonProvisionDiagonosisDto> list = s
					.createSQLQuery(Quries.GET_PROVISIONAL_DIAGNOSYS)
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(CommonProvisionDiagonosisDto.class)).list();
			tx.commit();
			s.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

}
