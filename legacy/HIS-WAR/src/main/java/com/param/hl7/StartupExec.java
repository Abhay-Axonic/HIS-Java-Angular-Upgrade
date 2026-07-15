
package com.param.hl7;


import com.param.global.dto.OrderMasterDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.hl7.service.Hl7MessageService;
import com.param.lis.transaction.dto.LabResultMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
public class StartupExec {

    @Autowired
    private Hl7MessageService service;

   // @Scheduled(fixedRate = 15000)
    public void keepAlive() {
        ProcedureOrderMasterDto dto  = new ProcedureOrderMasterDto();
        dto.setAdmissionId(3624);
        dto.setPatientId(12900);
        dto.setProcedureOrderId(1117);
        //service.afterProcedureOrder(dto);
    }
}

