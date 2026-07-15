package com.param.global.unit.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.EmployeeVaccination;
import com.param.global.unit.dto.EmployeeVaccinationDto;
import com.param.global.unit.dto.VaccinationDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmployeeVaccinationDaoImp extends GenericDao<EmployeeVaccination, Integer>
		implements IEmployeeVaccinationDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(EmployeeVaccinationDaoImp.class);

	public EmployeeVaccinationDaoImp() {
		super(EmployeeVaccination.class);

	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveEmployeeVaccination(EmployeeVaccinationDto employeeVaccinationDto) throws ApplicationException {

		try {

			for (VaccinationDto vaccinationDto : employeeVaccinationDto.getVaccinationDto()) {
				
				if(vaccinationDto.getEmployeeVaccinationId() == null && vaccinationDto.getSelectionDate() != null){
				EmployeeVaccination employeeVaccination = new EmployeeVaccination();
				employeeVaccination.setRoleId(employeeVaccinationDto.getRoleId());
				employeeVaccination.setUserId(employeeVaccinationDto.getUserId());
				employeeVaccination.setOrganisationId(employeeVaccinationDto.getOrganisationId());
				employeeVaccination.setUnitId(employeeVaccinationDto.getUnitId());
				employeeVaccination.setCreatedBy(employeeVaccinationDto.getCreatedBy());
				employeeVaccination.setCreatedDate(TimeZoneComponent.getDateByzone(employeeVaccinationDto.getUnitId()));
				employeeVaccination.setUpdatedBy(employeeVaccinationDto.getUpdatedBy());
				employeeVaccination.setUpdatedDate(TimeZoneComponent.getDateByzone(employeeVaccinationDto.getUnitId()));
				employeeVaccination.setStatus(employeeVaccinationDto.getStatus());

				employeeVaccination.setSelectionDate(vaccinationDto.getSelectionDate());
				
				employeeVaccination.setVaccinationId(vaccinationDto.getVaccinationId());
				
				sessionFactory.getCurrentSession().save(employeeVaccination);
				}

			}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (Exception e) {
			logger.error("saveEmployeeVaccination failed", e);
		}

		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response searchEmployeeVaccinationByRole(EmployeeVaccinationDto employeeVaccinationDto) {
		try {
			List<EmployeeVaccinationDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT " + "  mvrm.vaccination_id AS \"vaccinationId\", "
							+ "  mvrm.role_id AS \"roleId\", "
							+ "  tev.user_id AS \"userId\", "
							+ "  tev.selection_date AS \"selectionDate\", " + "  vm.name AS \"vaccinationName\", " + " tev.employee_vaccination_id  as \"employeeVaccinationId\" " + "FROM "
							+ "  m_vaccination_role_mapper mvrm "
							+ "  INNER JOIN m_item vm ON vm.id = mvrm.vaccination_id "
							+ "  LEFT JOIN t_employee_vaccination tev ON tev.vaccination_id = mvrm.vaccination_id "
							+ "  AND tev.user_id = :userId" + "  AND tev.organisation_id = :organizationId"
							+ "  AND tev.unit_id = :unitId "
							+ "WHERE " + "  mvrm.role_id = :roleId " + "  AND mvrm.status = 'A' "
							+ "  AND vm.is_active = true " + "  AND vm.organization_id = :organizationId "
							+ "  AND vm.unit_id = :unitId")
					.setParameter("userId", +employeeVaccinationDto.getUserId())
					.setParameter("roleId", +employeeVaccinationDto.getRoleId())
					.setParameter("unitId", employeeVaccinationDto.getUnitId())
					.setParameter("organizationId", employeeVaccinationDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(EmployeeVaccinationDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("searchEmployeeVaccinationByRole failed", he);
		} catch (Exception e) {
			logger.error("searchEmployeeVaccinationByRole failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVaccination(EmployeeVaccinationDto dto) throws ApplicationException {

		try {
			List<EmployeeVaccinationDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(
							"select mi.id as \"vaccinationId\" ,mi.name  as \"vaccinationName\" from m_item mi  "
									+ " where mi.organization_id =:organizationId   and mi.unit_id  =:unitId "
									+ " and mi.is_active  = true and mi.is_vaccin  = true")
					.setParameter("organizationId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EmployeeVaccinationDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
			throw e;
		}

	}

}
