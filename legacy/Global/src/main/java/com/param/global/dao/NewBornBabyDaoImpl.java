package com.param.global.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.NewBornBabyDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.model.NewBornBaby;
import com.param.global.service.IPatientRegistrationService;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NewBornBabyDaoImpl extends GenericDao<NewBornBaby, Integer>
		implements INewBornBabyDao, ICommonConstants, IBillingCommonConstants {
	final static Logger logger = Logger.getLogger(NewBornBabyDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	public NewBornBabyDaoImpl() {
		super(NewBornBaby.class);
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private IPatientRegistrationService regService;

	@Autowired
	private IPatientRegistrationDao regDao;
	

	@Override
	public Response saveNewBornBabyReg(NewBornBabyDto newBornBabyDto) throws ApplicationException {
		try {
			
			int noOfBabies = newBornBabyDto.getNumberOfBabies();
			Response res1 = regDao.patientSearchNormalByPatientId(newBornBabyDto.getPatientId(),
					newBornBabyDto.getOrganizationId(),newBornBabyDto.getUnitId());
			PatientRegistrationDto patientRegistrationDto = (PatientRegistrationDto)res1.getListObject().getFirst();
			
			for(int i=0;i<noOfBabies;i++){
				NewBornBaby newBornBaby=new NewBornBaby();
				//System.out.println("----------------------"+noOfBabies+"------------------------"+i);
				newBornBaby.setPatientId(newBornBabyDto.getPatientId());
				newBornBaby.setFatherName(newBornBabyDto.getFatherName());
				newBornBaby.setNumberOfBabies(newBornBabyDto.getNumberOfBabies());
				newBornBaby.setBabyName(newBornBabyDto.getBabyDetails().get(i).getBabyName());
				newBornBaby.setWeight(newBornBabyDto.getBabyDetails().get(i).getWeight());
				newBornBaby.setDob(new SimpleDateFormat("dd-MM-yyyy").parse(newBornBabyDto.getBabyDetails().get(i).getDob()));
				newBornBaby.setTob(newBornBabyDto.getBabyDetails().get(i).getTob());
				newBornBaby.setSex(newBornBabyDto.getBabyDetails().get(i).getSex());
				newBornBaby.setDeliveryType(newBornBabyDto.getBabyDetails().get(i).getDeliveryType());
				newBornBaby.setOutcome(newBornBabyDto.getBabyDetails().get(i).getOutcome());
				newBornBaby.setGestationalAge(newBornBabyDto.getBabyDetails().get(i).getGestationalAge());
				newBornBaby.setBirthTerm(newBornBabyDto.getBabyDetails().get(i).getBirthTerm());
				newBornBaby.setStatus(newBornBabyDto.getStatus());
				newBornBaby.setUnitId(newBornBabyDto.getUnitId());
				newBornBaby.setOrganizationId(newBornBabyDto.getOrganizationId());
				newBornBaby.setCreatedBy(newBornBabyDto.getCreatedBy());
				newBornBaby.setCreatedDate(newBornBabyDto.getCreatedDate().toString());
				newBornBaby.setUpdatedBy(newBornBabyDto.getUpdatedBy());
				newBornBaby.setUpdatedDate(newBornBabyDto.getUpdatedDate().toString());
				newBornBaby.setUhidNumberBaby(iGlobalUnitMasterDao.getGeneratedGlobalNo(0, newBornBabyDto.getOrganizationId(), 0,
						generateUhidConfigBaby(newBornBabyDto.getBabyDetails().get(i).getOutcome().equalsIgnoreCase("live")?1:2)));
				
				// register as normal patient if Live
				if(newBornBabyDto.getBabyDetails().get(i).getOutcome().equalsIgnoreCase("live")) {
					PatientRegistrationDto Dto = new PatientRegistrationDto();	
//					String DOB[] = newBornBabyDto.getBabyDetails().get(i).getDob().toString().split("-");
					patientRegistrationDto.setPatientId(Dto.getPatientId());
					patientRegistrationDto.setPrefixId(newBornBabyDto.getBabyDetails().get(i).getBabyPrefix());
					//"Baby of" Patient Name , 
					if(newBornBabyDto.getBabyDetails().get(i).getBabyName()!=null && newBornBabyDto.getBabyDetails().get(i).getBabyName()!= "") {
					patientRegistrationDto.setFirstName(newBornBabyDto.getBabyDetails().get(i).getBabyName());
					}/*else {
					patientRegistrationDto.setFirstName(patientRegistrationDto.getFirstname());
					patientRegistrationDto.setMiddleName(patientRegistrationDto.getMiddleName());
					patientRegistrationDto.setLastName(patientRegistrationDto.getLastname());
					}*/
					patientRegistrationDto.setUhidNumber(newBornBaby.getUhidNumberBaby());
					patientRegistrationDto.setDob(newBornBabyDto.getBabyDetails().get(i).getDob()+" "+ newBornBabyDto.getBabyDetails().get(i).getTob()+":00");
					patientRegistrationDto.setGenderId((newBornBabyDto.getBabyDetails().get(i).getSex()));
					patientRegistrationDto.setIsPrimary('Y');
					patientRegistrationDto.setOccupationId(null);
					patientRegistrationDto.setMaritalStatusId(1);
					patientRegistrationDto.setCreatedBy(newBornBabyDto.getCreatedBy());
					patientRegistrationDto.setCreatedDate(newBornBabyDto.getCreatedDate().toString());
					patientRegistrationDto.setOrganizationId(newBornBabyDto.getOrganizationId());
					patientRegistrationDto.setUnitId(newBornBabyDto.getUnitId());
					
					regService.saveBabyRegistration(patientRegistrationDto);
				}
				sessionFactory.getCurrentSession().save(newBornBaby);
			}
			
			
			sessionFactory.getCurrentSession().createSQLQuery("UPDATE adt.t_admission SET delivery_flag=1 WHERE patient_id=:patientId")
					.setParameter("patientId", newBornBabyDto.getPatientId()).executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveNewBornBabyReg failed", he);
		} catch (Exception e) {
		logger.error("saveNewBornBabyReg failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Transactional
	private int generateUhidConfigBaby(int registrationTypeId) throws ApplicationException {
		Integer screenId = 0;
		try { // screenId = registrationTypeId == 1 ? BABY_PATIENT_UHID : BABY_PATIENT_UHID_STILL;
				screenId = registrationTypeId == 1 ? NORMAL_PATIENT_UHID : BABY_PATIENT_UHID_STILL;
			} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
			}
		return screenId;
	}

	@Override
	public Response getBabyDetailsByParentId(NewBornBabyDto newBornBabyDto) throws ApplicationException {
		try {
			String GET_BABY_DETAILS_BY_ID = "SELECT newBornBaby.new_born_baby_reg_id as \"newBornBabyRegId\", newBornBaby.patient_id as \"patientId\",  " + 
					"   newBornBaby.uhid_number_baby as \"uhidNumberBaby\", newBornBaby.father_name as \"fatherName\", newBornBaby.delivery_type as \"deliveryType\",  " + 
					"   newBornBaby.number_of_babies as \"numberOfBabies\", newBornBaby.baby_name as \"babyName\",newBornBaby.weight as \"weight\", gen.gender_name as \"gender\",  " + 
					"   newBornBaby.dob as \"dob\", newBornBaby.tob as \"tob\", newBornBaby.sex as \"sex\", newBornBaby.outcome as \"outcome\", " + 
					"   newBornBaby.gestational_age as \"gestationalAge\", newBornBaby.birth_term as \"birthTerm\", newBornBaby.status as \"status\" " + 
					"   FROM public.m_new_born_baby_reg newBornBaby  " + 
					"   inner join public.m_gender_master gen on gen.gender_id=newBornBaby.sex " + 
					"   WHERE newBornBaby.patient_id=:patientId and newBornBaby.status='A'" + 
					"   and newBornBaby.organization_id=:organizationId and newBornBaby.unit_id=:unitId";
			
			List<NewBornBabyDto> babyList = sessionFactory.getCurrentSession()
					.createSQLQuery(GET_BABY_DETAILS_BY_ID)
					.setParameter("patientId", newBornBabyDto.getPatientId())
					.setParameter("organizationId", newBornBabyDto.getOrganizationId())
					.setParameter("unitId", newBornBabyDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NewBornBaby.class)).list();
			return new Response(SUCCESS, null, null, babyList, null);

		} catch (HibernateException he) {
		logger.error("getBabyDetailsByParentId failed", he);
		} catch (Exception e) {
		logger.error("getBabyDetailsByParentId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	

}
