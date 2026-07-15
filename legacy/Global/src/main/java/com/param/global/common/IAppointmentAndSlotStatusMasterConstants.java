package com.param.global.common;

public interface IAppointmentAndSlotStatusMasterConstants {
	
	Integer All=0;
	Integer Pending=1;
	Integer CheckedIn=5;
	Integer Vaccant=1;
	Integer Cancelled=2;
	Integer Rescheduled=3;
	Integer Booked=4;
	Integer Admitted = 8 ;
	Integer Completed = 6;
	
	Integer SlotTypeDoctor = 1;
	Integer SlotTypeModality = 2;
	Integer SlotTypeECH = 3;
	Integer SlotTypeHemodialysis = 8;
	Integer SlotForCyclic = 10;
	
	Integer Blocked=1;
	Integer Unblocked=2;
	
}
