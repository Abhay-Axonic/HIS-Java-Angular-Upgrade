package com.param.global.common;

public interface ScheduleActions {

	public static final String INSERT = "INSERT";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	public static final String ACTIVE = "ACTIVE";

	public static final String HIS = "HIS";

	public static final String PRIORITY_HIGH = "HIGH";
	public static final String PRIORITY_NORM = "NORMAL";
	public static final String PRIORITY_LOW = "LOW";

	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_INPROGRESS = "IN_PROGRESS";
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_ERROR = "ERROR";
	
	//for Local Testhttp://192.168.0.133:9090/MyLifeGlobal/api
/*	public static final String BASE_URL = "http://localhost:8082/GlobalScheduling/api";
	public static final String MYLIFE_GLOBAL_URI = "http://13.126.135.186:8102/MyLifeGlobal/api";*/
	
	
	//mylife test url
	/*public static final String MYLIFE_GLOBAL_URI = "http://13.126.135.186:8102/MyLifeGlobal/api";
	public static final String BASE_URL = "http://52.88.65.181:8080/GlobalScheduling/api";*/
	
	//mylife demo url
	/*public static final String MYLIFE_GLOBAL_URI = "http://13.126.135.186:8104/MyLifeGlobalDemo/api";
	public static final String BASE_URL = "http://34.212.43.230:8080/GlobalSchedulingDemo/api";*/
	
	//public static final String MYLIFE_GLOBAL_URI = "http://34.212.43.230:8080/MyLifeGlobal/api";
	//Hearth-Huddle
	public static final String HH_URI = "http://ec2-35-154-13-63.ap-south-1.compute.amazonaws.com:4002/api";
	public static final String HH_AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjpudWxsLCJyb2xlX2lkIjpudWxsLCJvcmdfaWQiOm51bGx9.EwwhaYaKZpiU_N0FzU4_hGa2IW7fBRVaQiZESxvp0bA";

}
