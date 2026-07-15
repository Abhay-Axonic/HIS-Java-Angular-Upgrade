package com.param.pharmacy.prescription.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.pharmacy.prescription.dao.IPrescriptionDao;
import com.param.pharmacy.prescription.dto.IsInterventionDto;

@Service("PrescriptionService")
@Transactional
public class PrescriptionService implements IPrescriptionService{

	@Autowired
	IPrescriptionDao PrescriptionDao;

	@Override
	public IsInterventionDto getPrescriptionIsIntervention() {
		return null;
	}
}
