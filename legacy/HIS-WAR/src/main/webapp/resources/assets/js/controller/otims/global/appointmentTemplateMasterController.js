(function () {
  "use strict";
						function appointmentTemplateMasterController($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$timeout,$stateParams,$filter) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
						
					       
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 $scope.consultationSpecialityId = cookieObjectAppConfig.consultationSpecialityId;
							
							$scope.init = function(){
								
								 $scope.templateObj = {
										 
										     	appointmentTemplateMasterId: null,

											    templateName: "",

											    morningMond: 'N',

											    morningTues: 'N',
											
											    morningWedn: 'N',

											    morningThus: 'N',
											
											    morningFrid: 'N',

											    morningSatur: 'N',
											
											    morningSund: 'N',
											
											    eveningMond: 'N',

											    eveningTues: 'N',
											
											    eveningWedn: 'N',

											    eveningThus: 'N',
											
											    eveningFrid: 'N',

											    eveningSatur: 'N',
											
											    eveningSund: 'N',

											    remark: "",

											    unitId: $scope.unitId ,

											    organizationId: $scope.organizationId,

											    createdBy: $scope.userId,

											    status: "A",
											    
											    appointmentTemplateMasterMapperList : []
										};	
								
								 $scope.appointmentTemplateMasterMapperList = [];
								 
								 $scope.templateMapperObj = {
										 appointmentTemplateMasterMapperId : null,
										 appointmentTemplateMasterId : null,
										 srNo: 1,
										 days : null,
										 unitId: $scope.unitId ,
										 organizationId: $scope.organizationId,
										 createdBy: $scope.userId,
										 status: "A",
										 appointmentTemplateServiceList : []
								 }
								 $scope.appointmentTemplateMasterMapperList.push($scope.templateMapperObj);
								 
								 
							}
							
							$scope.init();
							
							$scope.selectedIndex = '';
							$scope.openAddServicesPopup = function(index){
								$scope.selectedIndex = index;
								angular.element('#templagteService').modal('show');
							}
							
							$scope.AddTemplate = function(index){
								
								if( $scope.appointmentTemplateMasterMapperList[index].days == null ||  $scope.appointmentTemplateMasterMapperList[index].days == ""){
									growl.warning("Please enter days..!!",{
										title : WARNING_MSG
									});
									
								}else{
									
									$scope.templateMapperObj = {
											 appointmentTemplateMasterMapperId : null,
											 appointmentTemplateMasterId : null,
											 srNo: (index+1)+1,
											 days : "",
											 unitId: $scope.unitId ,
											 organizationId: $scope.organizationId,
											 createdBy: $scope.userId,
											 status: "A",
											 appointmentTemplateServiceList : []
									 }
									 $scope.appointmentTemplateMasterMapperList.push($scope.templateMapperObj);
								}
								 
							}
							 $scope.tempappointmentTemplateMasterMapperList = [];
							$scope.removeTemplate = function(index){
								
								if($scope.appointmentTemplateMasterMapperList[index].appointmentTemplateMasterMapperId!= null){
									
									$scope.appointmentTemplateMasterMapperList[index].status = "I" ;
									 var obj = angular.copy($scope.appointmentTemplateMasterMapperList[index]);
									   $scope.tempappointmentTemplateMasterMapperList.push(obj);
									   $scope.appointmentTemplateMasterMapperList.splice(index, 1);
									
								}else{
									$scope.appointmentTemplateMasterMapperList.splice(index, 1);
								}
								
							}
							
							$scope.search='';	
							$scope.searchServices = function(searchKeyword){
								$scope.servicesList=[];
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
								
								return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.servicesList = response.data.listObject;
									}
									return $scope.servicesList;
								});
							}
							
							$scope.addServiceToTempServiceList = function($item, $model, $label){
							
									var newdata ={}
									newdata.serviceId = $item.serviceMasterId;
									newdata.serviceName = $item.serviceStandardName;
									newdata.status = "A";
									newdata.unitId= $scope.unitId ;
									newdata.organizationId = $scope.organizationId;
									newdata.createdBy = $scope.userId;
									newdata.specialityId = $item.specialityId;
									$scope.appointmentTemplateMasterMapperList[$scope.selectedIndex].appointmentTemplateServiceList.push(newdata);
							
							 }
							
							 $scope.tempAppointmentTemplateServiceList = [];
							 
							$scope.removeService = function(index){
								
								if($scope.appointmentTemplateMasterMapperList[$scope.selectedIndex].appointmentTemplateServiceList[index].appointmentTemplateServiceId!= null){
									
									$scope.appointmentTemplateMasterMapperList[$scope.selectedIndex].appointmentTemplateServiceList[index].status = "I" ;
									 var obj = angular.copy($scope.appointmentTemplateMasterMapperList[$scope.selectedIndex].appointmentTemplateServiceList[index]);
									   $scope.tempAppointmentTemplateServiceList.push(obj);
									   
									   $scope.appointmentTemplateMasterMapperList[$scope.selectedIndex].appointmentTemplateServiceList.splice(index, 1);
									
								}else{
									$scope.appointmentTemplateMasterMapperList[$scope.selectedIndex].appointmentTemplateServiceList.splice(index, 1);
								}
								
							}
							
							
							$scope.save = function(){
								
								
								if($scope.templateObj.templateName == undefined || $scope.templateObj.templateName == null || $scope.templateObj.templateName == ""){
									
									growl.warning("Please enter template name",{
										title : WARNING_MSG
									});
									return false;
									
								}
								
								var checkDays = false;
								var checkServiceList = false;
								var srNo = 0;
								angular.forEach($scope.appointmentTemplateMasterMapperList,function(value,key){
						          
										if(value.days == null ||  value.days == ""){
											checkDays = true;
										}
										
										if(value.appointmentTemplateServiceList == undefined || value.appointmentTemplateServiceList == null || value.appointmentTemplateServiceList.length == 0)
										{
											checkServiceList = true;
											srNo = value.srNo;
										}
						            });
								
								if(checkDays == true ){
									growl.warning("Please enter days..!!",{
										title : WARNING_MSG
									});
									return false;
								}
								
								if(checkServiceList == true ){
									growl.warning("Please enter Services in Sr No. "+srNo,{
										title : WARNING_MSG
									});
									return false;
								}
								
								
								$rootScope.startLoader();
								
								
								angular.forEach($scope.tempAppointmentTemplateServiceList,function(value,key){
							          
									angular.forEach($scope.appointmentTemplateMasterMapperList,function(value1,key1){
								          
										if(value1.appointmentTemplateMasterMapperId != undefined && value1.appointmentTemplateMasterMapperId != null && value1.appointmentTemplateMasterMapperId == value.appointmentTemplateMasterMapperId){
											value1.appointmentTemplateServiceList.push(value);
										}
										
						            });
								
					            });
								
								
								$scope.appointmentTemplateMasterMapperList = $scope.appointmentTemplateMasterMapperList.concat($scope.tempappointmentTemplateMasterMapperList); 
										$scope.templateObj.appointmentTemplateMasterMapperList = $scope.appointmentTemplateMasterMapperList;
										$scope.templateObj.createdBy = $scope.userId;
										
										
									var URI = BASE_URL + ROOT_URL + "/api/saveAppointmentTemplate";
									BillingGenericService.serviceAction(METHOD_POST,URI,$scope.templateObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
											 $scope.tempappointmentTemplateMasterMapperList = [];
											 $scope.tempAppointmentTemplateServiceList = [];
											 $scope.init();
											 
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.getAppointmentSavedMasterList();
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
										}
							
									});
							}
							
							$scope.appointmentSavedMasterList = [];
							$scope.getAppointmentSavedMasterList = function(){
								$scope.appointmentSavedMasterList = [];
								$rootScope.startLoader();
								
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
								}
										
									var URI = BASE_URL + ROOT_URL + "/api/getAppointmentTemplate";
									BillingGenericService.serviceAction(METHOD_POST,URI,serData,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
												$rootScope.stopLoader();
												$scope.init();
												$scope.appointmentSavedMasterList = response.data.listObject; 
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
										}
							
									});
							}
							
							$scope.getAppointmentSavedMasterList();
							
							
							$scope.updateStatus= function(appointmentTemplateMasterId,status) {
							
								$rootScope.startLoader();
								
								var serData = {
										appointmentTemplateMasterId : appointmentTemplateMasterId,
										status : status
								}
										
									var URI = BASE_URL + ROOT_URL + "/api/updateStatus";
									BillingGenericService.serviceAction(METHOD_POST,URI,serData,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
												$rootScope.stopLoader();
												
												growl.success("Updated Successfully",
														{
															title : 'Success!'
														});
												
												$scope.getAppointmentSavedMasterList();
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
										}
							
									});
							}
							

							$scope.getAppointmentTemplateMasterMapperData= function(info) {
							
								$rootScope.startLoader();
								
								var serData = {
										appointmentTemplateMasterId : info.appointmentTemplateMasterId,
								}
										
									var URI = BASE_URL + ROOT_URL + "/api/getAppointmentTemplateMapperData";
									BillingGenericService.serviceAction(METHOD_POST,URI,serData,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
												$rootScope.stopLoader();
												 $scope.appointmentTemplateMasterMapperList = [];
												 $scope.templateObj = info;
												 $scope.tempappointmentTemplateMasterMapperList = [];
												 $scope.tempAppointmentTemplateServiceList = [];
												 $scope.appointmentTemplateMasterMapperList = response.data.listObject; 
													$scope.templateMapperObj = {
															 appointmentTemplateMasterMapperId : null,
															 appointmentTemplateMasterId : null,
															 srNo:  $scope.appointmentTemplateMasterMapperList.length +1,
															 days : "",
															 unitId: $scope.unitId ,
															 organizationId: $scope.organizationId,
															 createdBy: $scope.userId,
															 status: "A",
															 appointmentTemplateServiceList : []
													 }
													 $scope.appointmentTemplateMasterMapperList.push($scope.templateMapperObj);
												 
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
										}
							
									});
							}
							
							$scope.getAppointmentMapperDataViewList = [];
							$scope.getAppointmentMapperDataView= function(info) {
								
								$rootScope.startLoader();
								
								var serData = {
										appointmentTemplateMasterId : info.appointmentTemplateMasterId,
								}
										
									var URI = BASE_URL + ROOT_URL + "/api/getAppointmentTemplateMapperData";
									BillingGenericService.serviceAction(METHOD_POST,URI,serData,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
										{
												$rootScope.stopLoader();
												$scope.getAppointmentMapperDataViewList = [];
												 $scope.getAppointmentMapperDataViewList = response.data.listObject; 
												 $("#templateDefinition").modal('show');
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
										}
							
									});
							}
							
							
							
							$scope.index=null;
							$scope.getDepartmentBySpeacialityId=function(index)
							{
								$scope.consultantSpecialityId="";
								$scope.consultationServices=[];
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/billing/service/getConsultationDeptByServiceId/"+0+"/"+$scope.unitId;
									return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.consultationServices = response.data.listObject;
											
											$scope.index=index;
											$scope.consultantSpecialityId = $scope.appointmentTemplateMasterMapperList[$scope.selectedIndex].appointmentTemplateServiceList[$scope.index].consultantSpecialityId;
											$("#departmentModal1").modal('show');
											$rootScope.stopLoaderPopup();
										}else{
											$rootScope.stopLoaderPopup();
										}
									});
							}
							
							$scope.getSelectedConsultSpeciality=function(specialityId){
								
								if(specialityId!=undefined && specialityId!="")
								{
									$scope.appointmentTemplateMasterMapperList[$scope.selectedIndex].appointmentTemplateServiceList[$scope.index].consultantSpecialityId = specialityId;
								}
								
							 }
							
							
							
			}
						function config($stateProvider) {
    $stateProvider.state('appointmentTemplateMaster', {
		 url: '/appointmentTemplateMaster',
		 templateUrl: 'views/master/TheatreManagement/appointment-template-master.html',
		 controller: "appointmentTemplateMasterController",
		 title: 'Appointment Template Master (Recurring/Cyclic)',
	
		})
  }

  angular
    .module("myApp")
    .config(config)
    .controller("appointmentTemplateMasterController", appointmentTemplateMasterController);
})();