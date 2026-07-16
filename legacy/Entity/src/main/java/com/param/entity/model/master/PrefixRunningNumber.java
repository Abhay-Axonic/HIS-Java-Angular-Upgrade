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

import com.param.entity.model.base.BaseEntity;

@Entity(name = "PrefixRunningNumber")
@Table(name = "t_prefix_running_number", schema = "public")
public class PrefixRunningNumber extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(name = "current_running")
	private Integer currentRunning;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "screen_id", nullable = false)
	private Screen screen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	public PrefixRunningNumber() {
		super();
	}

	public PrefixRunningNumber(Integer currentRunning, Screen screen, Store store) {
		super();
		this.currentRunning = currentRunning;
		this.screen = screen;
		this.store = store;
	}

	public PrefixRunningNumber(Integer currentRunning, Screen screen) {
		super();
		this.currentRunning = currentRunning;
		this.screen = screen;
	}

	public void updatePrefixRunningNumber(Integer currentRunning){
		this.currentRunning = currentRunning;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCurrentRunning() {
		return currentRunning;
	}

	public void setCurrentRunning(Integer currentRunning) {
		this.currentRunning = currentRunning;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}
