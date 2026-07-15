
angular.module("myApp").controller("pharmanController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','$timeout','growl','paymentCollFactory','reportService','GenericService','$location','$localStorage','loyaltiCardCallFactory',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,$timeout,growl,paymentCollFactory,reportService,GenericService,$location, $localStorage,loyaltiCardCallFactory){
	/**Variables for pagination */
	$scope.offset = 0;
	$scope.noOfRecordsPerPage = 10;
	$scope.commonList = [];
	$scope.totalNoOfRecords;
	$scope.searchKeyword;
	$scope.commonListCount;


	var cookieObject = $cookies.getObject('cookieObject');
	if(cookieObject == undefined){
		$state.go('login');
		return;
	}
	
	$scope.unitId = cookieObject.unitId;
	$scope.organisationId = cookieObject.organizationId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	$scope.createdBy =cookieObject.userId;
	$scope.updatedBy =cookieObject.userId;
	
	$scope.medicine="";
	//$scope.formulation="";
	$scope.type="";
	$scope.initMaster = function(){
		$scope.saveBtnFlag = true;
		$scope.updateBtnFlag = false;
	
	}
	$scope.initMaster();
	
	$scope.searchPharmanDrugList = function(searchKeyword){
		var data={
				searchKeyword:searchKeyword
		}
		
		var URI = BASE_URL + ROOT_URL
				+ "/api/global/getPharmanDrugList"
		return BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							if (response.data.status == "success") {
								 return $scope.itemList = response.data.listObject;
							}
						});
	}

	      
	$scope.selectDrug=function(info, model,label,ind){
		$scope.medicineName=label;
		$scope.getFormsByName();
	}
	$scope.getFormsByName = function(){
		var data={
				medicine:$scope.medicineName
		}
		
		var URI = BASE_URL + ROOT_URL
				+ "/api/global/getPharmanDrugFormByName"
		return BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							if (response.data.status == "success") {
								$scope.medFormsList = response.data.listObject;

								$scope.formulation=$scope.medFormsList[0].formulation;
							}
						});
	}
	$scope.getMedName=function(medName){
		$scope.medicine=medName;
	}
	$scope.getMedForm=function(medForm, ind){
		$scope.formulation=medForm;
	}
	$scope.setType=function(type){
		if($scope.medicine==undefined || $scope.medicine==null || $scope.medicine==""){
			growl.warning("Please select medicine.");
		}else{
			$scope.type=type;
			$scope.getPopUpData();
		}
		
		}
	$scope.list=[];
	$scope.getPopUpData = function(){
		var data={
				medicine:$scope.medicine,
				medicinalforms:$scope.formulation
		}
		
		var URI = BASE_URL + ROOT_URL
				+ "/api/global/getPopUpData"
		 BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							if (response.data.status == "success") {
								$scope.popUpData = response.data.listObject;
								angular.forEach($scope.popUpData, function(val, key){
									var newArr={};
									newArr.allergyandcrosssensitivity=val.allergyandcrosssensitivity;
									newArr.cautions=val.cautions;
									newArr.hepaticimpairment=val.hepaticimpairment;
									newArr.allergyandcrosssensitivity=val.allergyandcrosssensitivity;
									newArr.indicationsanddoses=val.indicationsanddoses;
									newArr.medicinalforms=val.medicinalforms;
									newArr.medicine=val.medicine;
									newArr.monitoringrequirements=val.monitoringrequirements;
									newArr.patientandcareradvice=val.patientandcareradvice;
									newArr.prescribinganddispensinginformations=val.prescribinganddispensinginformations;
									newArr.pretreatmentscreeninginformation=val.pretreatmentscreeninginformation;
									newArr.sideeffects=val.sideeffects;
									newArr.renalimpairment=val.renalimpairment;

									$scope.list.push(newArr);
								})
							}
						});
	}
	
	$scope.clear=function(){
		$scope.list=[];
		$scope.medicine="";
		$scope.formulation="";
		$scope.type="";
	}
}]);