/**
 * 
 * Sneha Dalave
 */
(function () {
	"use strict";
	function incidentReportingController($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,commonDetailService,GenericServiceParamHeader)
	{
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
		commonDetailService.setDataId(null);
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
	
		$scope.openDatePickerfromDate = function() {
			$scope.datepickerfromDate.opened = true;
		};

		$scope.datepickerfromDate = {
			opened : false
		};
	
		$scope.dateOptionfromDate = {
			formatYear : 'yyyy',
			showWeeks : false,
//			maxDate : new Date(),
		};
	
		$scope.fromDate=new Date();
		$scope.toDate=new Date();
	
		/**********to date******************/
	
		$scope.openDatePickertoDate = function() {
			$scope.datepickertoDate.opened = true;
		};

		$scope.datepickertoDate = {
			opened : false
		};
	
		$scope.dateOptiontoDate = {
			formatYear : 'yyyy',
			showWeeks : false,
//			minDate : new Date(),
		};
        $scope.$watch('fromDate', function(newVal) {
            if (newVal) {
                $scope.dateOptiontoDate.minDate = new Date(newVal);
                
                if ($scope.toDate < $scope.dateOptiontoDate.minDate) {
                    $scope.toDate = new Date($scope.dateOptiontoDate.minDate);
                }
            }
        });
		
		$scope.specialityMasterList=[];
		//------------ Get Speciality Master List ------------//
		$scope.getSpecialityMasterList = function()
		{
			try
			{
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
		
		$scope.incidentReportingList=[];
		$scope.searchIncidentReportingData = function()
		{
			try
			{
				var URI = BASE_URL + ROOT_URL + "/api/unit/getIncidentReportingMapperList";
				var obj = {
					fromDate : moment($scope.fromDate).format('YYYY-MM-DD'),
					toDate : moment($scope.toDate).format('YYYY-MM-DD'),
					specialityId : $scope.specialityId || 0 ,
					incidentTypeId :  $scope.incidentTypeId || 0,
					organizationId : $scope.organizationId,
					unitId         :$scope.unitId,
					userId         :0
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
	
		$scope.addTemplate = function(template) {
			$scope.saveTrasactionFlag = false;
			$scope.updateTrasactionFlag = false;
			$scope.value ="";
			$scope.remark = template.incidentRemark
			$scope.templateFormatId =template.templateFormatId;
			if(template.templateFormatId==1)
			{
				document.getElementById("htmlContent").innerHTML = template.reportingTemplateObj;
			}
			else if(template.templateFormatId==2 || template.templateFormatId==3)
			{
				$scope.value = template.reportingTemplateObj;
			}
			$('#TemplateInfoModal').modal('hide');
		}
		
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
			 var URI = BASE_URL + ROOT_URL + "/api/opd/patientDoc/details/incident";
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
			commonDetailService.setDataId(info.incidentReportingMapperId);
			$state.go('addNewIncidentReporting');
		}
	}
	function config($stateProvider) {
		$stateProvider.state('incidentReporting', {
			url: '/incidentReporting',
			templateUrl: 'views/incidentReporting/incident-reporting.html',
			controller: "incidentReportingController", 
			title: 'Incident Reporting',
		})
	}

angular
	.module("myApp")
	.config(config)
	.controller("incidentReportingController", incidentReportingController);
})();