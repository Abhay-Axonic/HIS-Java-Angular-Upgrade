/**
 * Package service for Package related common functionality
 * 05-07-2018
 */

angular.module('myApp').service("packageService", ['$rootScope', 'BillingGenericService','GenericServiceParamHeader', function($rootscope, BillingGenericService,GenericServiceParamHeader){
	this.getActivePackagesList = function(model, callback){
	    if(model.patientDetails !== undefined && model.patientDetails !== null){
            if(model.patientDetails.patientId === undefined || model.patientDetails.patientId === null || model.patientDetails.patientId ===''){
                model.patientDetails.patientId = 0;
            }
        }
        else{
            model.patientDetails.patientId = 0;
        }
			var activePackagesList;
			var URI =BASE_URL + ROOT_URL + "/api/packages/patientId/"+model.patientDetails.patientId+"/encounterId/"+model.encounterDetails.encounterId+"/visitTypeId/"+ model.visitTypeId;
			var header = {organisationId: model.organizationId,unitId:model.unitId,packageId:0};
			GenericServiceParamHeader.serviceAction("GET", URI, null,NOTIFICATION_MSG_STATUS_FALSE,null,header).then(function(response){
				if(response.data.status == "success")
					activePackagesList = response.data.listObject;
				callback(activePackagesList);
			});
		};
		
	this.getTransferListByAdmissionId = function(admissionId,callback){
		var transferDetailsList;
		var URI = BASE_URL + ROOT_URL + GETTRANSFERLISTBYADMISSIONID + "/"+admissionId;
		BillingGenericService.serviceAction("GET",URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
				transferDetailsList = response.data.listObject;
			callback(transferDetailsList);
		});
	}
	
	this.getTransferOfCareListByAdmissionId = function(admissionId,callback){
		var transferOfCareDetailsList;
		var URI = BASE_URL + ROOT_URL + GETTRANSFEROFCARELISTBYADMISSIONID + "/"+admissionId;
		BillingGenericService.serviceAction("GET",URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
				transferOfCareDetailsList = response.data.listObject;
			callback(transferOfCareDetailsList);
		});
	}
	
	/**
	 * IPD Charge deposit list
	 */
	this.getDepositListByPatientId = function(patientId,unitId,callback){
		var depositList;
		var URI = BASE_URL + ROOT_URL + GETDEPOSITBYPATIENTID+ unitId + "/"+patientId;
		BillingGenericService.serviceAction("GET",URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
				depositList = response.data.listObject;
			callback(depositList);
		});
	}
	
	this.getPeriodicityServicesByAdmissionId = function(admissionId,callback){
		var periodicityServiceList;
		var URI = BASE_URL + ROOT_URL + GETPERIODICITYSERVICESBYADMID + "/"+admissionId;
		BillingGenericService.serviceAction("GET",URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
				periodicityServiceList = response.data.listObject;
			callback(periodicityServiceList);
		});
	}
	
}]);