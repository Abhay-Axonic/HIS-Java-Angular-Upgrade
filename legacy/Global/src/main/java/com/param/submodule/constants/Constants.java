package com.param.submodule.constants;

public class Constants {

	public static final String SUBMODULE_ACCESS_BY_MODULE_NAME_QUERY = "from ActionMaster where parent_id=(Select action_id from ActionMaster where action_name = :module) ";
	public static final String SUBMODULE_ACCESS_BY_MODULE_ID_QUERY = "from ActionMaster where parent_id= :id AND type='SubModule'";
	public static final String SUBMODULE_ACCESS_BY_TYPE = "from ActionMaster where type='SubModule'";
}
