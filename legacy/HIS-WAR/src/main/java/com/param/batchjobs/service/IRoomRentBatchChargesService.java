package com.param.batchjobs.service;

import java.util.List;

public interface IRoomRentBatchChargesService {
	public void postRoomRentCharges(List<Integer> unitIds);
	//public void postPeriodicityServices();
	public void postPeriodicServices(List<Integer> unitIds);
	//public void postRoomRentCharges(Integer i);
}
