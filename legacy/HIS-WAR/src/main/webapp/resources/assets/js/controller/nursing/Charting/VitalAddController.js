

angular.module("myApp").controller("VitalAddController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','$stateParams',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,$stateParams){
	try{
	
	$rootScope.loginpage = true;
	
	
	$scope.dateOptions = {
			formatYear : 'yyyy',
			showWeeks : false
		};
	$scope.openDatePicker = function() {
		$scope.datepicker.opened = true;
	};

	$scope.datepicker = {
		opened : false
	};
	
	$scope.openDatePickerPatiSearch = function() {
		$scope.datepicker1.opened = true;
	};

	$scope.datepicker1 = {
			opened : false
		};
	
	
	$scope.openFromDate = function() {
		$scope.fromDatePicker.opened = true;
	};

	$scope.fromDatePicker = {
		opened : false
	};
	
	$scope.openToDate = function() {
		$scope.toDatePicker.opened = true;
	};

	$scope.toDatePicker = {
			opened : false
		};
	
	
	
	var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {
		$state.go('login');
		return;
	}
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	
	$scope.patientSearchObj = {
		      "uhIdNumber"      : "",
		      "patientName"     : "",
		      "genderId"        : "",
		      "mobileNo"        : "",
		      "birthDate"       : "",
		      "identificationNo":"",
		      "patientType"   :"1",
		      "unitId"          :$scope.unitId,
		      "organizationId"  :$scope.organizationId
		    }
	
	
	
	$scope.initVital =function()
	{
		
		$scope.vitalObj = {
			  "tVitalId" : 0,
			  "vitalTime" : "",	
			  "patientId" : '',
			  "visitTypeId" : 2,
			  "admissionId" : "",
			  "encounterId" : "",
			  "organisationId" :$scope.organizationId,
			  "unitId" : $scope.unitId,
			  "status" : "A",
			  "createdDate" : "",
			  "createdBy" : $scope.userId,
			  "updatedBy" : $scope.userId,
			  "updatedDate" : "",
			  "vitalName" : "",
			  "vitalUnit" : "",
              "vitalList":[]
			}
		
		$scope.vitalDetails ={
			  "tVitalDetailsId" : "",
			  "tVitalId" : "",
			  "vitalId" : "",
			  "observation1" : "",
			  "typeId" : "",
			  "observation" : "",
			  "vitalName" : "",
		      "vitalUnit" : "",
		      "vitalTime" : "",
		      "createdDate" : ""	  
			}
	}
	$scope.initVital();
	$scope.formatValue=function(value,scale){
	 if (scale === 'F') 
	   {
		 $scope.vitalDetailsList[0].observation1 = Math.round(value * 9.0 / 5.0 + 32);
       } else if (scale === 'C') 
       {
    	   $scope.vitalDetailsList[0].observation = Math.round((value - 32) * 5.0 / 9.0);
       } else {
         throw new Error('Not a valid scale');
       }
	}
	

	
	$scope.vitalDetailsList = [];
	$scope.getVitalsList = function(){
		var URI = BASE_URL + ROOT_URL + API+GLOBAL+GET_VITAL_LIST+"/"+$scope.organizationId+"/"+$scope.userId;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
			if (response.data.status == "success")
				{
				 $scope.vitalDetailsList = response.data.listObject;
				}
		});
	}
	$scope.saveVitals = function()
	{
		$rootScope.startLoader();
		$scope.finalVitalList=[];
		angular.forEach($scope.vitalDetailsList, function(value, key) {
			  
			 if(value.observation!=null && value.observation!=""&& value.observation!=undefined)
				{
				   $scope.finalVitalList.push(value);
				}
			
			});
		$scope.vitalObj.patientId = $scope.patientObj.patientId;
		$scope.vitalObj.vitalList = $scope.finalVitalList;
		var URI =  BASE_URL + ROOT_URL +GLOBAL+SAVE_VITALS;
		BillingGenericService.serviceAction(METHOD_POST,URI,$scope.vitalObj,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
			if (response.data.status == "success"){
				$rootScope.stopLoader();
				$state.go('vitals');
			}
			if (response.data.status == "error"){
				$rootScope.stopLoader();
			}
			
		});
		console.log("$scope.vitalObj",JSON.stringify($scope.vitalObj));
	}
	
	


	
	/**Redirect Page Code*/
	if(angular.equals({},$stateParams.patientObj))
		{
		$state.go('vitals');
	} else {
		$scope.initVital();
		$scope.patientObj = $stateParams.patientObj;
		$scope.billing = true;
		$scope.patientDetails= $stateParams.patientObj;
		$scope.getVitalsList();
	}


	

	
	
	}catch(e){
		console.log(e.message);
	}
}]);