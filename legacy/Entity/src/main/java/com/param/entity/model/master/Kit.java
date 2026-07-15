package com.param.entity.model.master;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;


@Entity(name = "Kit")
@Table(name = "m_kit", schema = "public")
public class Kit extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_type_id")
	private KitType kitType;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "kitDetail", cascade = CascadeType.ALL)
	private List<KitDetail> kitDetailList;
	
	public Kit()
	{
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public KitType getKitType() {
		return kitType;
	}

	public void setKitType(KitType kitType) {
		this.kitType = kitType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<KitDetail> getKitDetailList() {
		return kitDetailList;
	}

	public void setKitDetailList(List<KitDetail> kitDetailList) {
		this.kitDetailList = kitDetailList;
	}
	
	public Kit(KitType kitType, String code, String description) {
		super();
		this.kitType = kitType;
		this.code=code;
		this.description = description;
		
	}

	
	public KitDetail addKitDetailList(KitDetail kitDetail) {
		if (getKitDetailList() == null)
			this.kitDetailList = new ArrayList<KitDetail>();

		getKitDetailList().add(kitDetail);
		kitDetail.setKitDetail(this);

		return kitDetail;
	}

	public void updateKitMaster(String code, String description) {
		this.code=code;
		this.description=description;
		
	}

	public KitDetail updateKitDetailList(KitDetail kitDetail) {
		if (getKitDetailList() == null)
			this.kitDetailList = new ArrayList<KitDetail>();

		getKitDetailList().add(kitDetail);
		kitDetail.setKitDetail(this);

		return kitDetail;
	}

}
