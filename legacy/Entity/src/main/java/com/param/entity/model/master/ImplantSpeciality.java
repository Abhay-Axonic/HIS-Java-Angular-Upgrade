package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.CascadeType;
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
import com.param.global.model.SpecialityMaster;

@Entity(name = "ImplantSpeciality")
@Table(name = "m_implant_speciality", schema = "public")
public class ImplantSpeciality extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	/*
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "implantspeciality", cascade =
	 * CascadeType.ALL) private Speciality speciality;
	 */

	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "speciality_id")
	private SpecialityMaster specialityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	public ImplantSpeciality() {
		super();
	}

	public ImplantSpeciality(Integer id) {
		super();
		this.id = id;
	}

	public ImplantSpeciality(SpecialityMaster specialityMaster, Item item) {
		this.specialityMaster = specialityMaster;
		this.item = item;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public void updateImplantSpeciality(SpecialityMaster specialityMaster, Item item)
	{
		this.specialityMaster=specialityMaster;
		this.item=item;
		
	}

}
