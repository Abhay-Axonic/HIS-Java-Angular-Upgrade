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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;


@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_PREFIX_NUMBER",query  ="select * from public.t_generate_prefix_number(:storeId,:unitId,:prefixId)")
})

@Entity(name = "PrefixConfiguration")
@Table(name = "m_prefix_configuration", schema = "public")
public class PrefixConfiguration extends BaseEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="screen_id", nullable=false)
	private Screen screen;
	
	@Column(name="running_number", nullable = false)
	private Integer runningNumber;
	
	@Column(name="reset_id", nullable = false)
	private Integer resetId;
	
	@Column(name="length", nullable = false)
	private Integer length;
	
	@Column(name="format_id")
	private Integer formatId;
	
	@Column(name="code")
	private String code;
	
	@Column(name="starting_month")
	private Integer startingMonth;
	
	@Column(name="duration")
	private Integer duration;
	
	@Column(name="generated_code", nullable = false)
	private String generatedCode;

	public PrefixConfiguration() {
		super();
	}

	public PrefixConfiguration(Screen screen, Integer resetId, Integer runningNumber, Integer length,
			 String generatedCode) {
		super();
		this.screen = screen;
		this.resetId = resetId;
		this.runningNumber = runningNumber;
		this.length = length;
		this.generatedCode = generatedCode;
	}
	
	public void updatePrefixConfiguration( Integer runningNumber, Integer resetId, Integer length, String generatedCode) {
		this.runningNumber = runningNumber;
		this.resetId = resetId;
		this.length = length;
		this.generatedCode = generatedCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public Integer getRunningNumber() {
		return runningNumber;
	}

	public void setRunningNumber(Integer runningNumber) {
		this.runningNumber = runningNumber;
	}

	public Integer getResetId() {
		return resetId;
	}

	public void setResetId(Integer resetId) {
		this.resetId = resetId;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getFormatId() {
		return formatId;
	}

	public void setFormatId(Integer formatId) {
		this.formatId = formatId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getStartingMonth() {
		return startingMonth;
	}

	public void setStartingMonth(Integer startingMonth) {
		this.startingMonth = startingMonth;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getGeneratedCode() {
		return generatedCode;
	}

	public void setGeneratedCode(String generatedCode) {
		this.generatedCode = generatedCode;
	}

}
