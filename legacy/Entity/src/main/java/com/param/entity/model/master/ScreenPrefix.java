package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.CommonEntity;

@Entity(name = "ScreenPrefix")
@Table(name = "m_screen_prefix", schema = "public")
public class ScreenPrefix extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@Column(length = 10, name = "default_prefix")
	private String defaultPrefix;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="screen_id", nullable=false)
	private Screen screen;
	
}
