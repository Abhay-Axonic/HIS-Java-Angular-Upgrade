package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PharmanDrugDto;
import com.param.global.model.CabinMaster;
import com.param.global.model.PharmanDrugModel;
import com.param.global.unit.dto.CabinMasterDto;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class PharmanDaoImpl  extends GenericDao<PharmanDrugModel, Integer>implements IPharmanDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(PharmanDaoImpl.class);


	public PharmanDaoImpl() {
		super(PharmanDrugModel.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response getPharmanDrugList(PharmanDrugDto pharmanDrugDto) {
		try {
			String query="select" + 
					"	drg.id as \"id\"," + 
					"	drg.medicine as \"medicine\" " + 
					"from" + 
					"	pharman.m_drug_db drg " + 
					"where drg.medicine like '%"+pharmanDrugDto.getSearchKeyword()+"%'";
			List<PharmanDrugDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(PharmanDrugDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getPharmanDrugList failed", he);
		} catch (Exception e) {
		logger.error("getPharmanDrugList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPharmanDrugFormByName(PharmanDrugDto pharmanDrugDto) {
		try {
			String query="select distinct mppr.formulation as \"formulation\" from pharman.m_drug_formulation_mapper mppr where mppr.drug_name  = '"
		+pharmanDrugDto.getMedicine()+"'";
			List<PharmanDrugDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(PharmanDrugDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getPharmanDrugFormByName failed", he);
		} catch (Exception e) {
		logger.error("getPharmanDrugFormByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPopUpData(PharmanDrugDto pharmanDrugDto) {
		try {
			String query = "select drg.id as \"id\", " 
					+ "	drg.indicationsanddoses as \"indicationsanddoses\", "
					+ "	drg.cautions as \"cautions\", " 
					+ "	drg.interactions as \"interactions\", "
					+ "	drg.sideeffects as \"sideeffects\", "
					+ "	drg.allergyandcrosssensitivity as \"allergyandcrosssensitivity\", "
					+ "	drg.pregnancy as \"pregnancy\", "
					+ "	drg.hepaticimpairment as \"hepaticimpairment\", "
					+ "	drg.renalimpairment as \"renalimpairment\", "
					+ "	drg.pretreatmentscreeninginformation as \"pretreatmentscreeninginformation\", "
					+ "	drg.monitoringrequirements as \"monitoringrequirements\", "
					+ "	drg.prescribinganddispensinginformations as \"prescribinganddispensinginformations\", "
					+ "	drg.patientandcareradvice as \"patientandcareradvice\", "
					+ "	drg.medicinalforms as \"medicinalforms\", " + "	drg.medicine as \"medicine\", "
					+ "	drg.contraindications as \"contraindications\", "
					+ "	drg.conceptionandcontraception as \"conceptionandcontraception\", "
					+ "	drg.breastfeeding as \"breastfeeding\", "
					+ "	drg.directionsforadministration as \"directionsforadministration\", "
					+ "	drg.nationalfunding as \"nationalfunding\", " + "	drg.drugaction as \"drugaction\", "
					+ "	drg.importantsafetyinformations as \"importantsafetyinformations\", "
					+ "	drg.treatmentcessationinformation as \"treatmentcessationinformation\", "
					+ "	drg.unlicenseduse as \"unlicenseduse\", "
					+ "	drg.professionspecificinformation as \"professionspecificinformation\", "
					+ "	drg.exceptionstolegalcategory as \"exceptionstolegalcategory\", "
					+ "	drg.handlingandstorages as \"handlingandstorages\", "
					+ "	drg.howtotake as \"howtotake\", "
					+ "	drg.whentostop as \"whentostop\", "
					+ "	drg.renalimpairmentcolour as \"renalimpairmentcolour\", "
					+ "	drg.hepaticimpairmentcolour as \"hepaticimpairmentcolour\", "
					+ "	drg.pregnancycolour as \"pregnancycolour\", "
					+ "	drg.breastfeedingcolour as \"breastfeedingcolour\", "
					+ "	drg.lesssuitableforprescribings as \"lesssuitableforprescribings\", "
					+ "	drg.medicationimagelink as \"medicationimagelink\", "
					+ "	drg.effectsonlaboratorytests as \"effectsonlaboratorytests\" "
					+ " from pharman.m_drug_db drg where medicine =:medicine  and drg.medicinalforms like :medicinalforms ";
			List<PharmanDrugDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("medicine",pharmanDrugDto.getMedicine())
					.setParameter("medicinalforms",'%' + pharmanDrugDto.getMedicinalforms()+ '%')
					.setResultTransformer(Transformers.aliasToBean(PharmanDrugDto.class)).list();
			
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he){
		logger.error("getPopUpData failed", he);
		} catch (Exception e) {
		logger.error("getPopUpData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPopUpDataWithId(PharmanDrugDto pharmanDrugDto) {
		try {
			String query = "select drg.id as \"id\", " 
					+ "	drg.indicationsanddoses as \"indicationsanddoses\", "
					+ "	drg.cautions as \"cautions\", " 
					+ "	drg.interactions as \"interactions\", "
					+ "	drg.sideeffects as \"sideeffects\", "
					+ "	drg.allergyandcrosssensitivity as \"allergyandcrosssensitivity\", "
					+ "	drg.pregnancy as \"pregnancy\", "
					+ "	drg.hepaticimpairment as \"hepaticimpairment\", "
					+ "	drg.renalimpairment as \"renalimpairment\", "
					+ "	drg.pretreatmentscreeninginformation as \"pretreatmentscreeninginformation\", "
					+ "	drg.monitoringrequirements as \"monitoringrequirements\", "
					+ "	drg.prescribinganddispensinginformations as \"prescribinganddispensinginformations\", "
					+ "	drg.patientandcareradvice as \"patientandcareradvice\", "
					+ "	drg.medicinalforms as \"medicinalforms\", " + "	drg.medicine as \"medicine\", "
					+ "	drg.contraindications as \"contraindications\", "
					+ "	drg.conceptionandcontraception as \"conceptionandcontraception\", "
					+ "	drg.breastfeeding as \"breastfeeding\", "
					+ "	drg.directionsforadministration as \"directionsforadministration\", "
					+ "	drg.nationalfunding as \"nationalfunding\", " + "	drg.drugaction as \"drugaction\", "
					+ "	drg.importantsafetyinformations as \"importantsafetyinformations\", "
					+ "	drg.treatmentcessationinformation as \"treatmentcessationinformation\", "
					+ "	drg.unlicenseduse as \"unlicenseduse\", "
					+ "	drg.professionspecificinformation as \"professionspecificinformation\", "
					+ "	drg.exceptionstolegalcategory as \"exceptionstolegalcategory\", "
					+ "	drg.handlingandstorages as \"handlingandstorages\", "
					+ "	drg.howtotake as \"howtotake\", "
					+ "	drg.whentostop as \"whentostop\", "
					+ "	drg.renalimpairmentcolour as \"renalimpairmentcolour\", "
					+ "	drg.hepaticimpairmentcolour as \"hepaticimpairmentcolour\", "
					+ "	drg.pregnancycolour as \"pregnancycolour\", "
					+ "	drg.breastfeedingcolour as \"breastfeedingcolour\", "
					+ "	drg.lesssuitableforprescribings as \"lesssuitableforprescribings\", "
					+ "	drg.medicationimagelink as \"medicationimagelink\", "
					+ "	drg.effectsonlaboratorytests as \"effectsonlaboratorytests\" "
					+ " from pharman.m_drug_db drg where drg.id =:id ";
			List<PharmanDrugDto> list= sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("id", pharmanDrugDto.getId())
					.setResultTransformer(Transformers.aliasToBean(PharmanDrugDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he){
		logger.error("getPopUpDataWithId failed", he);
		} catch (Exception e) {
		logger.error("getPopUpDataWithId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
