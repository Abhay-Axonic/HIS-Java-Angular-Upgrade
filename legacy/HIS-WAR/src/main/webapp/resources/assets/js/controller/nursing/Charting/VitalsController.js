

angular.module("myApp").controller("vitalsController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl){
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
	

	
	
	
	
	//For Get Active Gender List
	$scope.getSexMasterList = function() {
		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.sexMasterList = response.data.listObject;
		});
	}
	
	$scope.getIdentificationTypeList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.identificationTypeList = response.data.listObject;
			}
		});
	}
	
	
	
	
	
	
	
	
	$scope.init = function(){
		$scope.identificationTypeList = [];
		$scope.getIdentificationTypeList();
		$scope.sexMasterList = [];
		$scope.getSexMasterList();
		$scope.patientDetailsList = [];
		$scope.patientDetails = {};
		$scope.billing = true;
		$scope.visitNoList = [];
	}
	
	$scope.init();
	


	

	
	$scope.searchPatient = function(patientSearchObj)
	{
		
		$rootScope.startLoader();
		$scope.patientSearchObj.unitId = cookieObject.unitId;
		$scope.patientSearchObj.organizationId = cookieObject.organizationId;
		if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			$rootScope.stopLoader();
			return false;
		}	
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
		var patSearchObj = angular.copy($scope.patientSearchObj);
		if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
			var dt = new Date($scope.patientSearchObj.birthDate);
			patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
		} 
		BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.searchPatientList = [];
			console.log("response",response);
			if (response.data.status == "success")
				{
				$scope.patientList = response.data.listObject;
				patSearchObj = {};
				$rootScope.stopLoader();
				}
			else if(response.data.listObject==null){
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				$scope.patientSearchObj.birthDate="";
				$scope.patientList=[];
				return false;
			}
			else if (response.data.status == "error")
			{
				$rootScope.stopLoader();
			}
			$scope.patientSearchObj.birthDate="";
		});
		
	}
	
	$scope.getPatientDetailsById = function(patientId){
		$rootScope.startLoader();
		$scope.patientDetailsList = [];
		$scope.patientDetails = {}
		
	
		var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.patientDetailsList = response.data.listObject;
				$scope.patientDetails = response.data.listObject[0];
			}
			else if (response.data.status == "error"){
				$rootScope.stopLoader();
			}
		});
		
	}
		
  $scope.addVital  = function()
  {
		  if($scope.patientDetails!=null && !angular.equals({},$scope.patientDetails) )
			  {
							$state.go('addNewVitals',
								{
								patientObj : $scope.patientDetails,
								});
			  }
		  else
		     {
			    growl.error("Please select Patient.",{title : 'Error!'});
		     }
		  
  }
  
  $scope.fromDate;
  $scope.toDate;
  $scope.vitalSearchObj ={
		  "fromDate" : ($scope.fromDate!=""&&$scope.fromDate!=undefined)?moment($scope.fromDate).format('DD-MM-YYYY HH:mm:ss'):"",
		  "toDate" : ($scope.toDate!=""&&$scope.toDate!=undefined)?moment($scope.toDate).format('DD-MM-YYYY HH:mm:ss'):"",
		  "fromTime" : "",
		  "toTime" : "",
		  "patientId" : $scope.patientDetails.patientId,
		  "visitTypeId" : "",
		  "admissionId" : "",
		  "encounterId" : "",
		  "organisationId" : $scope.organizationId,
		  "unitId" : $scope.unitId,
		  "vitalId" : ""
		}

	$scope.vitalDetails ={
		  "tVitalDetailsId" : "",
		  "tVitalId" : "",
		  "vitalId" : "",
		  "observation1" : "",
		  "typeId" : "",
		  "observation" : ""
		}
  
  
  console.log('$scope.vitalSearchObj',JSON.stringify($scope.vitalSearchObj));
  $scope.filterdVitalList = [];
  $scope.searchVitals = function()
	{
	  $rootScope.startLoader();
	  $scope.vitalSearchObj.patientId = $scope.patientDetails.patientId;
	  var URI = BASE_URL+ROOT_URL+GLOBAL+GET_VITALS;
	  BillingGenericService.serviceAction(METHOD_POST,URI,$scope.vitalSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
				$scope.vitalList = response.data.listObject[0];
				$scope.vitalDetailsList = response.data.listObject[1];
				$rootScope.stopLoader();
			
				angular.forEach($scope.vitalList, function(vital, key) 
							{
						vital.detailsList = [];  
						angular.forEach($scope.vitalDetailsList, function(vitalDetails, key) 
								{
						         angular.forEach(vitalDetails.vitalList, function(vitalDetails, key) 
								       {
						        	    if(vital.vitalId==vitalDetails.vitalId)
							    	    {
						        		  vital.detailsList.push(vitalDetails);
							    	    }
									   });
							   });
						   });
				console.log('$scope.vitalList',JSON.stringify($scope.vitalList));
			}
			else if (response.data.status == "error"){
				$rootScope.stopLoader();
			}
		});
	  
	}
  
	
  $scope.vitalSortFunction = function(list) {
	  return function(vitalsDtls) {
		  	angular.forEach(list, function(value, key) 
		  			{
		  		   if(value.vitalTime==vitalsDtls.time && value.createdDate==vitalsDtls.date && value.vitalId ==vitalsDtls.vitalId)
		  			   {
		                    return vitalsDtls;    			   
		  			   }
		  		   else {
		  			   return angular.copy($scope.vitalObj);  
		  		   }
		  		});
	    }
	
	}
  
	}catch(e){
		console.log(e.message);
	}
}]);