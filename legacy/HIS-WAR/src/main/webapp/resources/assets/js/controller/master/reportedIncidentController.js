/**
 * 
 * Sneha Dalave
 */
(function () {
	"use strict";
		function reportedIncidentController($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,commonDetailService,GenericServiceParamHeader){
			$rootScope.loginpage = true;
			$scope.LISDynamicLabel = "Antibiotic Addition";
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
		
			$scope.storeList = [];
		
		/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.usersList=[];
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
		
		$scope.selectedRoleList=[];	
		$scope.selectedTemplateList=[];
		$scope.selectedTemplateList1=[];
		$scope.userTemplateList = [];
		
		
		$scope.initCommon = function()
					{
						$scope.saveBtnFlag = true; 
						$scope.updateBtnFlag = false;
						$scope.common =
						{
							 "userStoreMapperId":"",
							 "code":"",
							 "description":"",
						     "isActive":"A",
							 "createdBy":$scope.userId,
							 "createdDate":"",
							 "updatedBy":$scope.userId,
							 "updatedDate":"",
						     "organisationId":$scope.organisationId,

						}
					}
		$scope.initCommon();
	
		/**********from date******************/
		
		$scope.fromDate = new Date();
		$scope.openDatePickerfromDate = function() {
			$scope.datepickerfromDate.opened = true;
		};

		$scope.datepickerfromDate = {
			opened : false
		};
		
		$scope.dateOptionfromDate = {
				formatYear : 'yyyy',
				showWeeks : false,
//				maxDate : new Date(),
			};
		
        /**********to date******************/
		
		$scope.toDate = new Date();
		
		$scope.openDatePickertoDate = function() {
			$scope.datepickertoDate.opened = true;
		};

		$scope.datepickertoDate = {
			opened : false
		};
		
		$scope.dateOptiontoDate = {
				formatYear : 'yyyy',
				showWeeks : false,
//				minDate : new Date(),
			};
		
        $scope.$watch('fromDate', function(newVal) {
            if (newVal) {
                $scope.dateOptiontoDate.minDate = new Date(newVal);
                
                if ($scope.toDate < $scope.dateOptiontoDate.minDate) {
                    $scope.toDate = new Date($scope.dateOptiontoDate.minDate);
                }
            }
        });
		
		 $scope.getPatientDocumentList = function(obj)
	     {
	      $rootScope.startLoader();
	      var data =
	        {
	        "patientId":obj.patientId,
	        "unitId":$scope.unitId,
	        "organisationId":$scope.organizationId,
	        "userId":$scope.userId,
	        "visitTypeId":1,
	        "admissionId":0,
	        "incidentReportingMapperId":obj.incidentReportingMapperId,
	       }
	      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
	      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
	        $scope.patientDocumentList = [];
	        if (response.data.status == "success") 
	        {
	        	$scope.patientDocumentList = response.data.listObject;  
	        	 $rootScope.stopLoader();
	            }
		});    
	           
	        
	     }
			
		  $scope.navigateToEdit = function (info) {
		      commonDetailService.setDataId(info);
		      $state.go('EditReportedIncident');
		    }
		  
		$scope.searchIncidentReportingData = function(){
			try{
				var URI = BASE_URL + ROOT_URL + "/api/unit/getReportedIncidentMapperList";
				var obj = {
						    fromDate : moment($scope.fromDate).format('YYYY-MM-DD'),
						    toDate : moment($scope.toDate).format('YYYY-MM-DD'),
						    specialityId : $scope.specialityId || 0 ,
						    incidentTypeId : $scope.incidentTypeId || 0,
							organizationId : $scope.organizationId,
							unitId :$scope.unitId,
							userId :$scope.userId
					    }
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					console.log(response);
					if(response.data.status == "success"){
						$scope.incidentReportingList = response.data.listObject;
					}
				});
			}catch (e) {
				console.log("Exception",e.message);
			}
		}
		$scope.searchIncidentReportingData();
		
		$scope.specialityMasterList=[];
		//------------ Get Speciality Master List ------------//
		$scope.getSpecialityMasterList = function(){
			try{
				var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
				var obj = {
							organizationId : $scope.organizationId,
							unitId         :$scope.unitId
					    }
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					console.log(response);
					if(response.data.status == "success"){
						$scope.specialityMasterList = response.data.listObject;
					}
				});
			}catch (e) {
				console.log("Exception",e.message);
			}
		}
		$scope.getSpecialityMasterList();
		
		
		$scope.addTemplate = function(info) {
			  $rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/unit/getTemplateByReportedIncidentId";
				var obj = {
						    incidentReportingMapperId : info.incidentReportingMapperId,
							organizationId : $scope.organizationId,
							unitId :$scope.unitId,
					    }
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					console.log(response);
					if(response.data.status == "success"){
						$scope.savedTemplatelistFromReportinIncident=[];
						$scope.savedTemplatelistFromReportinIncident = response.data.listObject[0];
						console.log("jjj",$scope.savedTemplatelistFromReportinIncident);
						var	template =$scope.savedTemplatelistFromReportinIncident;
						$scope.saveTrasactionFlag = false;
						$scope.updateTrasactionFlag = false;
						$scope.value ="";
						$scope.remark = template.incidentRemark
						$scope.templateFormatId =2;
					/*	if(template.templateFormatId==1)
							{
							document.getElementById("htmlContent").innerHTML = template.reportingTemplateObj;
							}
						else if(template.templateFormatId==2)
							{*/
							 $scope.value = template.reportingTemplateObj;
//							}
							  $rootScope.stopLoader();
					}
				});
	
		}
		
		$scope.getPatientDocumentList = function(obj)
	     {
	      
	      $rootScope.startLoader();
	      var data =
	        {
	        "patientId":obj.patientId,
	        "unitId":$scope.unitId,
	        "organisationId":$scope.organizationId,
	        "userId":undefined,
	        "visitTypeId":undefined,
	        "admissionId":undefined,
	        "grnId": $scope.grnId,
	       "incidentReportingMapperId" : obj.incidentReportingMapperId
	       }
	      var URI = BASE_URL + ROOT_URL + "/api/opd/patientDoc/details/incident";
	      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
	        $scope.patientDocumentList = [];
	        if (response.data.status == "success") 
	        {
	        	$scope.patientDocumentList = response.data.listObject;  
	        	angular.forEach($scope.patientDocumentList,function(value, key) {
	        		var filepath = value.documentPath
	        		var arr = filepath.split("/");
	        		var length=arr.length;
	        		var fileName1=arr[length-1];
	        		var arr1 = fileName1.split("_");
	        		var length1=arr1.length;
	        		var fileName=arr1[length1-1];
	        		value.fileName=fileName;
	        	});
	        		
	        	
	           
	            $rootScope.stopLoader();
	        }
	        else if(response.data.status == "error")
	         {
	        	$rootScope.stopLoader();
	         }
	       });
	     }
		
}
function config($stateProvider) {
	$stateProvider.state('ReportedIncident', {
		url: '/ReportedIncident',
		templateUrl: 'views/incidentReporting/reported-incident.html',
		controller: "reportedIncidentController", 
		title: 'Reported Incident',
	})
}

angular
.module("myApp")
.config(config)
.controller("reportedIncidentController", reportedIncidentController);
})();