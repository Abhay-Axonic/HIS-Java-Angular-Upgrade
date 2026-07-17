package com.param.pharmacy.nursing.workstation.dao.drugAdministration;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.pharmacy.AntibioticTestDose;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.pharmacy.dto.AntibioticTestDoseDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class AntibioticTestDoseDaoImpl extends GenericDao<AntibioticTestDose, Integer> implements IAntibioticTestDoseDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(AntibioticTestDoseDaoImpl.class);

	public AntibioticTestDoseDaoImpl() {
		super(AntibioticTestDose.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;

		@Override
		public Response saveAntibioticTestDose(AntibioticTestDoseDto dto) {

			try {
				
				
					AntibioticTestDose master=new AntibioticTestDose();
					master.setAdminDate(dto.getAdminDate() != null ? GlobalCommonDateUtils.getDate(dto.getAdminDate(), "dd-M-yyyy HH:ss:mm") : null);
					master.setAdminTime(dto.getAdminTime() != null ? dto.getAdminTime() : "");
					master.setObservationDate(dto.getObservationDate() != null ? GlobalCommonDateUtils.getDate(dto.getObservationDate(), "dd-M-yyyy HH:ss:mm"): null);
					master.setObservationTime(dto.getObservationTime() != null ? dto.getObservationTime() : "");
					master.setSaleDetailsId(dto.getSaleDetailsId() != null ? dto.getSaleDetailsId() : null);
					master.setRoa(dto.getRoa() != null ? dto.getRoa() : null);
					master.setOtherEffect(dto.getOtherEffect() != null ? dto.getOtherEffect() : null);
					master.setRedness(dto.getRedness() != null ? dto.getRedness() : false);
					master.setSwelling(dto.getSwelling() != null ? dto.getSwelling() : false);
					master.setOther(dto.getOther() != null ? dto.getOther() : false);
					master.setStopAdministration(dto.getStopAdministration() != null ? dto.getStopAdministration() : null);
					master.setUnEventful(dto.getUnEventful() != null ? dto.getUnEventful() : null);
					master.setOrganizationId(dto.getOrganizationId());
					master.setUnitId(dto.getUnitId());
					master.setStatus(dto.getStatus());
					master.setCreatedBy(dto.getCreatedBy());
					master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master.setUpdatedBy(dto.getUpdatedBy());
					master.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master.setTestDose(dto.getTestDose() != null ? dto.getTestDose() : null);
					master.setAllergyId(dto.getAllergyId() != null ? dto.getAllergyId() : null);
					master.setAdmissionId(dto.getAdmissionId()!= null ? dto.getAdmissionId(): null);
					master.setEncounterId(dto.getEncounterId() != null ? dto.getEncounterId(): null);
					master.setPrescriptionDetailId(dto.getPrescriptionDetailId() != null ? dto.getPrescriptionDetailId() : null);
					if(dto.getAntibioticTestDoseId() == null ) {
						save(master);
						return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
					}else {
						master.setAntibioticTestDoseId(dto.getAntibioticTestDoseId());
						update(master);
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}
					
			
					
				
				} catch (HibernateException he) {
					logger.error(he.getMessage(), he);
					throw new ServiceException(he.getMessage());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage());
				}

		}

		@Override
		public Response getAntibioticTestDose(AntibioticTestDoseDto dto) {


			try {
				
				
				if(dto.getPrescriptionDetailId() == null && dto.getSaleDetailsId() != null) {
						String qry = " select prescription_detail_id  as \"prescriptionDetailId\" from pharmacy.t_sale_detail tsd  where  id =:saleDetailsId ";
	
						StringBuffer sb = new StringBuffer(qry);
						
					 AntibioticTestDoseDto obj = (AntibioticTestDoseDto) sessionFactory.getCurrentSession()
								.createSQLQuery(sb.toString())
							 .setParameter("saleDetailsId", dto.getSaleDetailsId())
								.setResultTransformer(Transformers.aliasToBean(AntibioticTestDoseDto.class)).uniqueResult();
					 
					 if(obj != null && obj.getPrescriptionDetailId() != null ) {
						 dto.setPrescriptionDetailId(obj.getPrescriptionDetailId());
					 }
				}
				
				if(dto.getPrescriptionDetailId() != null) {
					
					String qry = " select  " + 
							" mi.name  as \"itemName\"," + 
							" mg.name as \"genericName\"," + 
							" 	CAST(tpd.id AS INT4) AS \"prescriptionDetailId\", " + 
							" anti.roa as \"roa\"," + 
							" to_char(anti.admin_date, 'YYYY-MM-DD')  as \"adminDate\"," + 
							" anti.admin_time as \"adminTime\"," + 
							" to_char(anti.observation_date, 'YYYY-MM-DD') as \"observationDate\"," + 
							" anti.observation_time as \"observationTime\"," + 
							" anti.un_eventful as \"unEventful\"," + 
							" anti.redness as \"redness\", " + 
							" anti.swelling as \"swelling\", " + 
							" anti.other as \"other\"," + 
							" anti.other_effect as \"otherEffect\"," + 
							" anti.stop_administration as \"stopAdministration\","
							+ " anti.test_dose as \"testDose\" ,"
							+ " mi.formulation_type_id as \"formulationTypeId\" , " + 
							" mi.generic_id  as \"genericId\","
							+ " anti.allergy_id as \"allergyId\" ,"
							+ " anti.antibiotic_test_dose_id as \"antibioticTestDoseId\"  " + 
							" from pharmacy.t_prescription_detail tpd    " + 
							" inner join m_item mi  on mi.id = tpd.item_id" + 
							" inner join m_generic mg on mg.id  = mi.generic_id " + 
							" left join pharmacy.t_antibiotic_test_dose anti on anti.prescription_detail_id = tpd.id " + 
							" where tpd.id =:prescriptionDetailId ";

					StringBuffer sb = new StringBuffer(qry);
					
					List<AntibioticTestDoseDto> list = sessionFactory.getCurrentSession()
							.createSQLQuery(sb.toString())
							.setParameter("prescriptionDetailId", dto.getPrescriptionDetailId())
							.setResultTransformer(Transformers.aliasToBean(AntibioticTestDoseDto.class)).list();
					
					Integer flag =0;
					if(list.getFirst().getAntibioticTestDoseId() != null) {
						
						if(list.getFirst().getObservationDate() != null) {
							flag =1;
						}
						
					}else {
										
					
										String baseQuery = "SELECT antibiotic_test_dose_id AS \"antibioticTestDoseId\" " +
								                   "FROM pharmacy.t_antibiotic_test_dose tatd " +
								                   "INNER JOIN pharmacy.t_prescription_detail tpd ON tpd.id = tatd.prescription_detail_id " +
								                   "INNER JOIN m_item mi ON mi.id = tpd.item_id " +
								                   "WHERE ";
					
								StringBuilder queryBuilder = new StringBuilder(baseQuery);
					
								if (dto.getAdmissionId() != null) {
								    queryBuilder.append(" tatd.admission_id =:admissionId ");
								} else {
								    queryBuilder.append(" tatd.encounter_id =:encounterId ");
								               
								}
								queryBuilder.append(" AND mi.generic_id = (SELECT mi.generic_id FROM pharmacy.t_prescription_detail tpd  INNER JOIN m_item mi ON mi.id = tpd.item_id  WHERE tpd.id =:rescriptionDetailId " +")") ;
								
					
								String qry1 = queryBuilder.toString();
					
										StringBuffer sb1 = new StringBuffer(qry1);
										
									Query sql = sessionFactory.getCurrentSession()
												.createSQLQuery(sb1.toString());
									if (dto.getAdmissionId() != null) {
										sql.setParameter("admissionId", dto.getAdmissionId());
									} else {
										sql.setParameter("encounterId", dto.getEncounterId());

									}
										sql.setParameter("rescriptionDetailId", dto.getPrescriptionDetailId());
									List<AntibioticTestDoseDto> list1 =	sql.setResultTransformer(Transformers.aliasToBean(AntibioticTestDoseDto.class)).list();
										
								if(list1 != null && list1.size()>0) {
									flag =2;
								}		
					}
					
					return new Response(SUCCESS, null, null, list, flag);
				}
				
				
			} catch (Exception e) {
				throw e;
			}
			return null;



		}

		@Override
		public Response StopAdministration(AntibioticTestDoseDto dto) {

			try {
				
				if(dto.getPrescriptionDetailId() != null) {
					

					String sql = "UPDATE pharmacy.t_prescription_detail SET is_medication_stop = 'Y' " +
							"WHERE id IN (SELECT saled.id FROM pharmacy.t_prescription_detail saled " +
							"INNER JOIN m_item mi ON mi.id = saled.item_id " +
							"INNER JOIN pharmacy.t_prescription sa ON sa.id = saled.prescription_id " +
							"WHERE sa.admission_id = :admissionId " +
							"AND mi.generic_id = (SELECT mi.generic_id FROM pharmacy.t_prescription_detail tsd " +
							"INNER JOIN m_item mi ON mi.id = tsd.item_id WHERE tsd.id = :prescriptionDetailId))";
					sessionFactory.getCurrentSession()
						.createSQLQuery(sql)
							.setParameter("admissionId", dto.getAdmissionId())
							.setParameter("prescriptionDetailId", dto.getPrescriptionDetailId())
							.executeUpdate();

				return new Response(SUCCESS,null,null,null,null);
				}
				
				if(dto.getAdmissionId()!= null) {

					String sql = "UPDATE pharmacy.t_prescription_detail SET is_medication_stop = 'Y' " +
							"WHERE id IN (SELECT saled.prescription_detail_id FROM pharmacy.t_sale_detail saled " +
							"INNER JOIN m_item mi ON mi.id = saled.item_id " +
							"INNER JOIN pharmacy.t_sale sa ON sa.id = saled.sale_id " +
							"WHERE sa.admission_id = :admissionId AND mi.generic_id = " +
							"(SELECT mi.generic_id FROM pharmacy.t_sale_detail tsd " +
							"INNER JOIN m_item mi ON mi.id = tsd.item_id WHERE tsd.id = :saleDetailsId))";

					sessionFactory.getCurrentSession()
							.createSQLQuery(sql)
							.setParameter("admissionId", dto.getAdmissionId())
							.setParameter("saleDetailsId", dto.getSaleDetailsId())
							.executeUpdate();

				return new Response(SUCCESS,null,null,null,null);
					
				}else {
					String sql = "UPDATE pharmacy.t_prescription_detail SET is_medication_stop = 'Y' " +
							"WHERE id IN (SELECT saled.prescription_detail_id FROM pharmacy.t_sale_detail saled " +
							"INNER JOIN m_item mi ON mi.id = saled.item_id " +
							"INNER JOIN pharmacy.t_sale sa ON sa.id = saled.sale_id " +
							"WHERE sa.encounter_id = :encounterId AND mi.generic_id = " +
							"(SELECT mi.generic_id FROM pharmacy.t_sale_detail tsd " +
							"INNER JOIN m_item mi ON mi.id = tsd.item_id WHERE tsd.id = :saleDetailsId))";

					sessionFactory.getCurrentSession()
							.createSQLQuery(sql)
							.setParameter("encounterId", dto.getEncounterId())
							.setParameter("saleDetailsId", dto.getSaleDetailsId())
							.executeUpdate();


					return new Response(SUCCESS,null,null,null,null);
				}
				
			
			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}

		}

		@Override
		public Response checkObservation(AntibioticTestDoseDto dto) {



			try {
				
					 if(dto.getPrescriptionDetailId() == null && dto.getSaleDetailsId() != null) {
							String qry = " select prescription_detail_id  as \"prescriptionDetailId\" from pharmacy.t_sale_detail tsd  where  id =:saleDetailsId ";

							StringBuffer sb = new StringBuffer(qry);
							
						 AntibioticTestDoseDto obj = (AntibioticTestDoseDto) sessionFactory.getCurrentSession()
									.createSQLQuery(sb.toString())
								 .setParameter("saleDetailsId", dto.getSaleDetailsId())
									.setResultTransformer(Transformers.aliasToBean(AntibioticTestDoseDto.class)).uniqueResult();
						 
						 if(obj != null && obj.getPrescriptionDetailId() != null) {
							 dto.setPrescriptionDetailId(obj.getPrescriptionDetailId());
						 }
					 }
						 
					 
						if(dto.getPrescriptionDetailId() != null) {
							
						
						 String baseQuery = "SELECT antibiotic_test_dose_id AS \"antibioticTestDoseId\" " +
				                   "FROM pharmacy.t_antibiotic_test_dose tatd " +
				                   "INNER JOIN pharmacy.t_prescription_detail tpd ON tpd.id = tatd.prescription_detail_id " +
				                   "INNER JOIN m_item mi ON mi.id = tpd.item_id " +
				                   "WHERE ";

							StringBuilder queryBuilder = new StringBuilder(baseQuery);
					
							if (dto.getAdmissionId() != null) {
							    queryBuilder.append(" tatd.admission_id =:admissionId ");
							} else {
							    queryBuilder.append(" tatd.encounter_id =:encounterId ");
							               
							}
							queryBuilder.append(" AND mi.generic_id = (SELECT mi.generic_id FROM pharmacy.t_prescription_detail tpd  INNER JOIN m_item mi ON mi.id = tpd.item_id  WHERE tpd.id =:prescriptionDetailId )") ;
							queryBuilder.append(" AND observation_date IS NOT NULL ")
							            .append(" AND (tatd.stop_administration IS  NULL OR tatd.stop_administration = false)");
					
							String qry = queryBuilder.toString();
					
									StringBuffer sb = new StringBuffer(qry);
									
									Query sql= sessionFactory.getCurrentSession()
											.createSQLQuery(sb.toString());
									sql.setParameter("prescriptionDetailId", dto.getPrescriptionDetailId());
							if (dto.getAdmissionId() != null) {
								sql.setParameter("admissionId", dto.getAdmissionId());
							} else {
								sql.setParameter("encounterId", dto.getEncounterId());

							}
							List<AntibioticTestDoseDto> list =	sql.setResultTransformer(Transformers.aliasToBean(AntibioticTestDoseDto.class)).list();
									
									return new Response(SUCCESS, null, null, list, null);
						}
			} catch (Exception e) {
				throw e;
			}
			return null;



		
		}

		@Override
		public Response getAllergyType(AntibioticTestDoseDto dto) {



			try {
				String qry = " select allergy_type_id as \"allergyTypeId\" from emr.m_allergy_type_master matm  where organization_id  =:organizationId " +
						"and is_generic ='Y'  limit 1 ";

				StringBuffer sb = new StringBuffer(qry);
				
				List<AntibioticTestDoseDto> list = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
				.setParameter("organizationId", dto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(AntibioticTestDoseDto.class)).list();
				
				return new Response(SUCCESS, null, null, list, null);
			} catch (Exception e) {
				throw e;
			}



		
		}
		

}
