package com.param.opd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * 
 * Created by Tushar
 */

@Entity
@Table(name="m_roa_master",schema="opd")
@SequenceGenerator(name = "m_seq_roa_master", sequenceName = "opd.m_seq_roa_master", allocationSize = 1)
public class RoaMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_roa_master")
	@Column(name = "roa_id", unique = true, nullable = false)	
	private int roa_id;
	
	@Column(name = "roa_details", nullable = false)
	private String roa_details;
	
	public int getRoa_id() {
		return roa_id;
	}
	public void setRoa_id(int roa_id) {
		this.roa_id = roa_id;
	}
	public String getRoa_details() {
		return roa_details;
	}
	public void setRoa_details(String roa_details) {
		this.roa_details = roa_details;
	}

}
