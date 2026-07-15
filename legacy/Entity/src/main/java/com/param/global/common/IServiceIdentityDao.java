package com.param.global.common;

import in.param.exception.ApplicationException;

public interface IServiceIdentityDao {

    String saveServiceId(String serverId)throws ApplicationException;

    Boolean checkServiceIdAvailable(String serverId)throws ApplicationException;
}
