package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_GROWTH_CHART_TRANSACTION_BY_PATIENT",
			query="select "
					+ "  growth_chart.growth_chart_id as \"growthChartId\", "
					+ "  gw_chart.t_growth_chart_id as \"tGrowthChartId\", "
					+ "  growth_chart.age_in_months as \"ageInMonths\", "
					+ "  to_char( "
					+ "    gw_chart.growth_chart_date, "
					+ "    'MM-DD-YYYY HH:MI:SS' "
					+ "  ) as \"growthChartDate\", "
					+ "  gw_chart.weight as \"weight\", "
					+ "  gw_chart.height as \"height\", "
					+ "  gw_chart.circumference as \"circumference\", "
					+ "  growth_chart.weight_from as \"weightFrom\", "
					+ "  growth_chart.weight_to as \"weightTo\", "
					+ "  wt_unit.unit_name as \"weightUnit\", "
					+ "  growth_chart.height_from as \"heightFrom\", "
					+ "  growth_chart.height_to as \"heightTo\", "
					+ "  ht_unit.unit_name as \"heightUnitString\", "
					+ "  growth_chart.circumference_from as \"circumferenceFrom\", "
					+ "  growth_chart.circumference_to as \"circumferenceTo\", "
					+ "  cf_unit.unit_name as \"circumferenceUnitString\" "
					+ "from "
					+ "  public.m_growth_chart_master growth_chart "
					+ "  inner join public.m_height_unit ht_unit on growth_chart.height_unit = ht_unit.height_unit_id "
					+ "  inner join public.m_weight_unit wt_unit on growth_chart.weight_unit_id = wt_unit.weight_unit_id "
					+ "  inner join public.m_circufereance_unit cf_unit on growth_chart.circumference_unit = cf_unit.circufereance_unit_id "
					+ "  inner join public.t_growth_chart gw_chart on growth_chart.growth_chart_id = gw_chart.growth_chart_id "
					+ "where "
					+ "  growth_chart.organisation_id = :orgId "
					+ "  and growth_chart.unit_id = :unitId "
					+ "  and growth_chart.status <> 'D' "
					+ "  and gw_chart.patient_id = :patientId "
					+ "union "
					+ "select "
					+ "  growth_chart.growth_chart_id as \"growthChartId\", "
					+ "  null as \"tGrowthChartId\", "
					+ "  growth_chart.age_in_months as \"ageInMonths\", "
					+ "  null as \"growthChartDate\", "
					+ "  null as \"weight\", "
					+ "  null as \"height\", "
					+ "  null as \"circumference\", "
					+ "  growth_chart.weight_from as \"weightFrom\", "
					+ "  growth_chart.weight_to as \"weightTo\", "
					+ "  wt_unit.unit_name as \"weightUnit\", "
					+ "  growth_chart.height_from as \"heightFrom\", "
					+ "  growth_chart.height_to as \"heightTo\", "
					+ "  ht_unit.unit_name as \"heightUnitString\", "
					+ "  growth_chart.circumference_from as \"circumferenceFrom\", "
					+ "  growth_chart.circumference_to as \"circumferenceTo\", "
					+ "  cf_unit.unit_name as \"circumferenceUnitString\" "
					+ "from "
					+ "  public.m_growth_chart_master growth_chart "
					+ "  inner join public.m_height_unit ht_unit on growth_chart.height_unit = ht_unit.height_unit_id "
					+ "  inner join public.m_weight_unit wt_unit on growth_chart.weight_unit_id = wt_unit.weight_unit_id "
					+ "  inner join public.m_circufereance_unit cf_unit on growth_chart.circumference_unit = cf_unit.circufereance_unit_id "
					+ "where "
					+ "  growth_chart.organisation_id = :orgId "
					+ "  and growth_chart.unit_id = :unitId "
					+ "  and growth_chart.status <> 'D' "
                    +"  and growth_chart.growth_chart_id not in ( "
                    +"    select "
					+ "      gc.growth_chart_id "
					+ "    from "
					+ "      public.t_growth_chart gc "
					+ "    where "
					+ "      gc.organisation_id = :orgId "
					+ "      and gc.unit_id = :unitId "
					+ "      and gc.patient_id = :patientId )"
			) })
@Entity
@Table(name = "t_growth_chart" , schema = "public")
@SequenceGenerator(name = "seq_t_chart_master" , sequenceName = "public.seq_t_chart_master" , allocationSize = 1)
public class TGrowthChart {
	
	@Id
	@Column(name="t_growth_chart_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_t_chart_master")
	private int tGrowthChartId;
	
	@Column(name="growth_chart_id")
	private Integer growthChartId;
	
	@Column(name="growth_chart_date")
	private Date growthChartDate;
	
	@Column(name="weight")
	private Double weight;
	
	@Column(name="height")
	private Double height;
	
	@Column(name="circumference")
	private Double circumference;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status")
	private char status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;

	public int gettGrowthChartId() {
		return tGrowthChartId;
	}

	public void settGrowthChartId(int tGrowthChartId) {
		this.tGrowthChartId = tGrowthChartId;
	}

	public Integer getGrowthChartId() {
		return growthChartId;
	}

	public void setGrowthChartId(Integer growthChartId) {
		this.growthChartId = growthChartId;
	}

	public Date getGrowthChartDate() {
		return growthChartDate;
	}

	public void setGrowthChartDate(Date growthChartDate) {
		this.growthChartDate = growthChartDate;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getCircumference() {
		return circumference;
	}

	public void setCircumference(Double circumference) {
		this.circumference = circumference;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	

}
