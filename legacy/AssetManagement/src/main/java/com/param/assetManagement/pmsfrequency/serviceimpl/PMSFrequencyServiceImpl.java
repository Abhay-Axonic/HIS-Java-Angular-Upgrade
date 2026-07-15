package com.param.assetManagement.pmsfrequency.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.assetManagement.pmsfrequency.dao.IPMSFrequencyDao;
import com.param.assetManagement.pmsfrequency.dto.PMSFrequencyDto;
import com.param.assetManagement.pmsfrequency.service.IPMSFrequencyService;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.PMSFrequency;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.AssetType;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

@Service(" PMSFrequencyServiceImpl")
public class PMSFrequencyServiceImpl implements IPMSFrequencyService {
	@Autowired
	IPMSFrequencyDao PMSFrequencyDao;

	@Override
	@Transactional
	public Object save(PMSFrequencyDto request) {

		PMSFrequency frq = new PMSFrequency(request.getCode(), request.getDescription());
		frq.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		frq.setIsActive(true);
		frq.setIsDeleted(false);
		PMSFrequencyDao.save(frq);
		CommonResponse response = new CommonResponse();

		response.setMessage("PMS Frequency Inserted Successfully");

		return response;

	}

	@Override
	@Transactional
	public Object update(PMSFrequencyDto request) {

		PMSFrequency frq = PMSFrequencyDao.getPmsFrequency(request.getId());
		frq.setCode(request.getCode()!=null ? request.getCode() :frq.getCode());
		frq.setDescription(request.getDescription()!=null ? request.getDescription() :frq.getDescription());
		frq.setUpdateDefaults(request.getUserId(), frq.getUnitId());
		PMSFrequencyDao.update(frq);
		CommonResponse response = new CommonResponse();

		response.setMessage("PMS Frequency updated Successfully");

		return response;

	}

	@Override
	@Transactional
	public List<PMSFrequencyDto> list(Integer unitId) {
		List<PMSFrequency> list = PMSFrequencyDao.list(unitId);
		List<PMSFrequencyDto> response = new ArrayList<PMSFrequencyDto>();
		for (PMSFrequency f : list) {
			PMSFrequencyDto fdto = new PMSFrequencyDto();
			fdto.setId(f.getId());
			fdto.setCode(f.getCode());
			fdto.setDescription(f.getDescription());
			response.add(fdto);
		}
		return response;

	}

}
