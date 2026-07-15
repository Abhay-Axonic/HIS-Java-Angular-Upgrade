package com.param.global.billing.dto;

import java.util.List;

public class FranchiseDepositReqDto {

    private FranchiseDepositMasterDto franchiseDepositMasterDto;
    private List<FranchiseDepositDetailsDto> franchiseDepositDetailsDtoList;

    public FranchiseDepositMasterDto getFranchiseDepositMasterDto() {
        return franchiseDepositMasterDto;
    }

    public void setFranchiseDepositMasterDto(FranchiseDepositMasterDto franchiseDepositMasterDto) {
        this.franchiseDepositMasterDto = franchiseDepositMasterDto;
    }

    public List<FranchiseDepositDetailsDto> getFranchiseDepositDetailsDtoList() {
        return franchiseDepositDetailsDtoList;
    }

    public void setFranchiseDepositDetailsDtoList(List<FranchiseDepositDetailsDto> franchiseDepositDetailsDtoList) {
        this.franchiseDepositDetailsDtoList = franchiseDepositDetailsDtoList;
    }
}
