package com.param.assetManagement.pmsfrequency.dao;

import java.util.List;

import com.param.entity.model.inventory.PMSFrequency;

public interface IPMSFrequencyDao {

	void save(PMSFrequency frq);

	List<PMSFrequency> list(Integer unitId);

	PMSFrequency getPmsFrequency(Integer id);

	void update(PMSFrequency frq);

}
