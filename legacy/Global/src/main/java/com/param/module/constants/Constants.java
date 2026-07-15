package com.param.module.constants;

public class Constants {

	public static final String MODULE_ACCESS_QUERY = "from ActionMaster where parent_id= 0";
	public static final String MODULE_ACCESS_QUERY_BY_TYPE = "select actionMaster.action_id, actionMaster.action_name from ActionMaster actionMaster where type='Module'";

}
