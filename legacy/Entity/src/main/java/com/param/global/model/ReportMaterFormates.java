package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_mis_report_format", schema = "public")
//@SequenceGenerator(name="m_reports_master_seq", sequenceName="public.m_reports_master_seq", allocationSize=1)

public class ReportMaterFormates {
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_reports_master_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "format")
	private String format;
	
	@Column(name = "file_extension")
	private String fileExtension;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	

	
	
}
