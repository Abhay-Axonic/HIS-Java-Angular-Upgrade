/**
 *  IPD Billing Controller
 *  18-07-2018
 */

(function(){
	"use strict";
	
	function IPDBillingController($scope,$rootScope,$cookies,$state,BillingGenericService,GenericService,$stateParams,$timeout,growl,paymentCollFactory,GenericServiceParamHeader,packageService){
		var self = this;
		
		$scope.init = function(){
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$rootScope.loginpage = true;
			
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
			else
				{
					console.log('cookieObjectAppConfig',cookieObjectAppConfig);
					
				}
			$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
			$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
			
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.visitTypeId=2;
			$rootScope.loginpage = true;
			$scope.currentDate = new Date();
			
			$scope.transferToActiveDeposit = "N";
			
			$scope.patientSearchObj = {
					"uhIdNumber"      : "",
					"patientName"     : "",
					"genderId"        : "",
					"mobileNo"        : "",
					"birthDate"       : "",
					"identificationNo": "",
					"patientType"	  : "1",
					"unitId"          :$rootScope.unitId,
					"organizationId"  :$rootScope.organizationId
			}
		
			$scope.patientDetails = {
					"patientId"	  : "",
					"patientName" : "",
					"genderCode"  : "",
					"age"         : "", 
					"uhIdNumber"  : "",
					"birthDate"	  : "",
					"encounterId" : ""
			}
			
			$scope.searchPatient = searchPatient;
			$scope.getPatientDetailsByIndex = getPatientDetailsByIndex;
		};
		
		var searchPatient = function(patientsearchObj){
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
			BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				$scope.patientList = response.data.listObject;

				$scope.patientAdmissionList = [];
				$scope.patientDetails ={};
				$scope.admissionDetails = {};

				//console.log('$scope.patientList : ', JSON.stringify($scope.patientList));
				$scope.patientAdmissionList = [];
				self.model.patientDetails ={};
				self.model.encounterDetails = {};
			});
		};
		
		var getPatientDetailsByIndex = function(index){
			$rootScope.startLoader();
			
			$scope.patientDetails.age=($scope.patientList[index].age);
			$scope.patientDetails.patientName=($scope.patientList[index].patientName);
			$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
			$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
			$scope.patientDetails.patientId=($scope.patientList[index].patientId);
			$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
			
			$scope.patientFullName=($scope.patientList[index].patientName)+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
			$scope.uhidNum=($scope.patientList[index].uhIdNumber);
			$scope.admissionId = ($scope.patientList[index].admissionId);
			
			//get admission details by id
			if($scope.admissionId != null && typeof($scope.admissionId) != "undefined"){
				$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
				BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.admissionDetails = response.data.object;
						
						$scope.dateOfAdmission = typeof($scope.admissionDetails.doa) != "undefined" ? $scope.admissionDetails.doa : "";
						$scope.patientDischargeDate = typeof($scope.admissionDetails.pdd) != "undefined" ? $scope.admissionDetails.pdd : "";
						$scope.bedNumber = typeof($scope.admissionDetails.bedNo) != "undefined" ? $scope.admissionDetails.bedNo : "";
						$scope.doctorFullName = typeof($scope.admissionDetails.doctorName) != "undefined" ? $scope.admissionDetails.doctorName : "";
						
						getOrderedServices();
						//$scope.getActivePackagesList();
					}
					$rootScope.stopLoader();
				});
				
				
			}
			//Assignment to model (Santosh)
			self.model.patientDetails.patientId=($scope.patientList[index].patientId);//4
			//ADMISSION & ENCOUNTER IDS SAME VARIABLE;
			self.model.encounterDetails.encounterId = ($scope.patientList[index].admissionId); 
			/*self.model.visitTypeId = 1;*/
			
			$(".patienttSearchResult").toggle();
			$rootScope.stopLoader();
		};
		
		$scope.init();
	};
	
	
	
	function config($stateProvider) {
	    $stateProvider.state("IPDBilling", {
	      url: "/IPDBilling",
	      templateUrl: 'views/master/UnitMaster/billingMaster/IPD-billing.html',
	      controller: "IPDBillingController.Controller",
	      resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 40);
	   				 	});
	   					if(arr.length <= 0){
	   						$state.go("accessDenied")
	   						deferred.reject();
	   					 } else {
	   						deferred.resolve();
	   					 }
	   		        });
	   		
	   		        return deferred.promise;
	   		      }
	   		 },
	      controllerAs: "vm"
	    });
	  }
	
	  angular
	    .module("myApp")
	    .config(config)
	    .controller("IPDBillingController.Controller", IPDBillingController);
})();
