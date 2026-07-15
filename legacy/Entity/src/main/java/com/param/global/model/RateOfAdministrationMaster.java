package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_Vaccination_Master_LIST",
		query="SELECT vaccinationMaster.vaccinationId AS vaccinationId, vaccinationMaster.vaccinationCode AS vaccinationCode,  vaccinationMaster.vaccinationDesc AS vaccinationDesc,"
				+ "vaccinationMaster.administrationId AS administrationId,vaccinationMaster.siteId AS siteId,vaccinationMaster.doseId AS doseId,"
				+ "vaccinationMaster.uomId AS  uomId,vaccinationMaster.age AS age,vaccinationMaster.dayid AS dayid,"
					+ " vaccinationMaster.organizationId AS organizationId,vaccinationMaster.unitId AS unitId,vaccinationMaster.status AS status"
					+ " FROM VaccinationMaster vaccinationMaster"
					
					),

	})

@Entity
@Table(name = "m_roa_master", schema = "lab")
@SequenceGenerator(name = "seq_m_vaccination_master", sequenceName = "lab.m_vaccination_master_seq", allocationSize = 1)
public class RateOfAdministrationMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_vaccination_master")
	
	@Column(name = "roa_id")
	private Integer roaId;
	
	@Column(name = "roa_name")
	private String roaName;
	
}
