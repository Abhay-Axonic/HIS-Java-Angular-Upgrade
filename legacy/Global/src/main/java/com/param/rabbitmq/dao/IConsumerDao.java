package com.param.rabbitmq.dao;

import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;

public interface IConsumerDao {

	Item getById(Integer id);

	Store getNarcoticStores();

	Store getIPStore();

	Long create(Prescription prescription);

	Integer create(PrescriptionFrequencyDetail prescriptionFrequencyDetail);

}
