package com.param.billing.global;

public class CommonGSTCalculation {
    
    public static Double calculateServiceRateForGST(Double serviceReteForTax, Double concessionPer, Double discountPer){
        
        Double concessionAfterCalculaion  =  0.00;
        Double discountAmtAfterCalculaion  =  0.00;
        if(concessionPer>0.00)
            concessionAfterCalculaion = serviceReteForTax*(concessionPer*0.01);
        else
            concessionAfterCalculaion = 0.00;
        
        if(discountPer>0)
        {
            Double rateMinusConcessionAfterCalculaion = (serviceReteForTax) - concessionAfterCalculaion;
            discountAmtAfterCalculaion = rateMinusConcessionAfterCalculaion*(discountPer*0.01);
        }else{
            discountAmtAfterCalculaion = 0.00;
        }
        
        Double serviceReteAfterCalculaion  = serviceReteForTax - (concessionAfterCalculaion + discountAmtAfterCalculaion);
            
        return serviceReteAfterCalculaion;
        
    };

}