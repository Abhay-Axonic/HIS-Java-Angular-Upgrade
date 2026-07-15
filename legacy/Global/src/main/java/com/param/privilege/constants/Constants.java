package com.param.privilege.constants;

public class Constants {

	public static final String DELETE_BY_ID = "{id}";
	public static final String DELETE_BY_ROLE_ID = "/role/{organizationId}/{unitId}/{roleId}";
	public static final String UPDATE_BY_ROLE_ID = "/role";
	public static final String GET_BY_ID = "{id}";
	public static final String GET_BY_ROLE_ID = "/role/{organizationId}/{unitId}/{roleId}";
	public static final String GET_ALL_ROLE_PRIVILLAGE_BY_ROLE_ID = "/role/privillage/{id}";
	public static final String GET_BY_ROLE_ID_LIST = "/role";
	public static final String FAILED_MSG = "failed";
	public static final String SUCCESS_MSG = "success";
	public static final String JSON_PROPERTY_MODULES = "modules";
	public static final String JSON_PROPERTY_SUBMODULES = "subModules";
	public static final String JSON_PROPERTY_ORG_ID = "organisationId";
	public static final String JSON_PROPERTY_UNIT_ID = "unitId";
	public static final String JSON_PROPERTY_ROLEID = "roleId";
	public static final String JSON_PROPERTY_MODULETYPE = "moduleType";
	public static final String PRIVILEGE_ACCESS_BY_ROLE_ID_QUERY="from Privilege where roleId= :roleid and organisationId=:organizationId and unitId=:unitId";
	public static final String ALL_PRIVILEGE_ACCESS_BY_ROLE_ID_QUERY="from Privilege where roleId= :roleid";
	public static final String LOG_FILE = "";
	
	public static final String BASE_URL = "http://localhost:8080";
	public static final String ROOT_URL = "/HIS-WAR";
	public static final String OTIMS_ROOT_URL = "http://13.126.135.186:4567"; 
	
	public static final String BILLING_CANCEL_DISCHARGE = "/api/billing/cancelBillAndAutopostingServices"; 
	
}
