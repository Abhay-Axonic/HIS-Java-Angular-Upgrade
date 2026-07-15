package com.param.mrd.dao;

import com.param.adt.mrd.MrdFileAllocationStatus;

import java.util.List;

public interface IMrdFileStatusDao {
    void saveMrdFileStatus(List<MrdFileAllocationStatus> fileAllocationStatus);
}
