(function(){
	'use strict'
	
	function billEstimationController($scope, $rootScope, BillingGenericService, growl, $cookies, $state){
		try{
		
		var self = this;
		
		function init(){
			$rootScope.loginpage = true;
			self.model = {};
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
				$state.go('login');
				return;
			}
			self.model.unitId = cookieObject.unitId;
			self.model.organizationId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			
			$scope.patientSearchObj = {uhIdNumber: "",patientName: "",genderId: "",mobileNo: "",birthDate: "",identificationNo : "",
										patientType: 1,unitId: self.model.unitId,organizationId : self.model.organizationId};
			$scope.patientDetails = {patientName: "",genderCode: "",age: "",uhIdNumber: "",birthDate: ""};
			$scope.patientList = [];
			
		}
		$scope.openDatePicker = function() {
			// alert("in");
			$scope.datepicker.opened = true;
		};
		$scope.datepicker = {
			opened : false
		};
		$scope.dateOptions = {
				formatYear : 'yyyy',
				todayHighlight:'TRUE',
				maxDate:new Date(),
				showWeeks : false
			};
		$scope.searchPatient = function(searchObj){
			
			var patSearchObj = angular.copy(searchObj);
			if(patSearchObj.patientName!="" && patSearchObj.patientName.length < 3)
			{
				growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
				$scope.patientList = [];
				$rootScope.stopLoader();
				return false;
			}	
			if(searchObj.birthDate != "" && typeof(searchObj.birthDate) != "undefined"){
				var dt = new Date(searchObj.birthDate);
				searchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" + dt.getFullYear();
			} 
			
			var URI = BASE_URL + ROOT_URL + PATIENTSERACHBYMULTIPLECRITERIAUNKNOWN;
			$rootScope.startLoader();
			BillingGenericService.serviceAction(METHOD_POST,URI, searchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					$scope.patientList = response.data.listObject;
					$scope.patientSearchObj.birthDate="";
				}
				if(response.data.listObject==null){
					growl.warning(response.data.message,{
						title : ERROR_MSG
					});
					$scope.patientSearchObj.birthDate="";
					$scope.patientList=[];
					return false;
				}
				$rootScope.stopLoader();
			});
		}
		
		$scope.getPatientDetailsByIndex = function(index) {
			$scope.patientDetails.age = ($scope.patientList[index].age);
			$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
			$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
			$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
			$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
			$('.patienttSearchResult').toggle();
		}	
		
		init();
		
		}catch(e){
			console.log(e.message);
			$rootScope.stopLoader();
		}
	}
	
	function config($stateProvider){
		$stateProvider.state("billEstimation",{
			 url: '/billEstimation',
			 templateUrl: 'views/master/UnitMaster/billingMaster/bill-estimation.html',
			 controller: "BillEstimation.Controller",
			 controllerAs : "vm"
		});
	}
	
	angular
    .module("myApp")
    .config(config)
    .controller("BillEstimation.Controller", billEstimationController);
	
})();