
/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"encounterDataDetailsController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						'$stateParams',
						'BillingGenericService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$stateParams,BillingGenericService) {

							
							$scope.isRecollected = false;
							$rootScope.loginpage = true;
							$scope.thirdLevel = false;
							
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.triageList = [];
							$scope.otherHistoryList = [];
							$scope.physicalExaminationList = [];
							$scope.prescriptionList = [];
							$scope.labOrdersList = [];
							$scope.diagnosisList = [];
							$scope.procedureList = [];
							$scope.radioLogyList = [];
							$scope.otherReviewList = [];
							$scope.privisionalDiagnosis = [];
							$scope.quickSummery = {};
							
							
							//For Get getQuickSummery
							$scope.getQuickSummery = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_QUICH_SUMMERY;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.quickSummery = response.data.object;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							//For Get TRIAGElIST
							$scope.getTriageList = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_TRIAGE_LIST;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.triageList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
						
							
							
							//For Get OTHER_HISTORY
							$scope.getOtherHistory = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_HISTORY;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.otherHistoryList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							
							
							//For Get OTHER_PHYSICAL_EXAMINATION
							$scope.getPhysicalExamination = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_PHYSICAL_EXAMINATION;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.physicalExaminationList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
						
							//For Get OTHER_PRESCRITION
							$scope.getPrescrition = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_PRESCRITION;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.prescriptionList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							
							//For Get OTHER_LOBORATORY
							$scope.getLabOrders = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_LOBORATORY;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.labOrdersList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							//For Get OTHER_DIAGNOSYS
							$scope.getDiagnosys = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_DIAGNOSYS;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.diagnosisList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							
							
							//For Get PROCEDURES
							$scope.getProcedures = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_PROCEDURE;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.procedureList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							//OTHER_RADIOLOGY
							$scope.getRadiology = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_RADIOLOGY;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.radioLogyList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							
							//OTHER_REVIEW_NOTES
							$scope.getReviewNotes = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + OTHER_REVIEW_NOTES;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.otherReviewList = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							
							//PROVISIONAL_DIAGNOSYS
							$scope.getProvisionalDiagnosis = function(encounterId) 
							{
								$rootScope.startLoader();
								var data ={
										"encounterId":encounterId,
									}
								var URI = BASE_URL + ROOT_URL + PROVISIONAL_DIAGNOSYS;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								{
									if (response.data.status == 'success')
									{
									  $scope.privisionalDiagnosis = response.data.listObject;
									  $rootScope.stopLoader();
									}
									else
									 {
										$rootScope.stopLoader();
									 }
									
								});
							}
							
							if($stateParams.encounterId==null)
 							{
								$state.go('doctorDeskController');
							} else {
								$scope.encounterId = $stateParams.encounterId;
							}
							
							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
								
							};
							
							$scope.setFirstTabLevel = function(newTab) {
								$scope.firstTabLevel = newTab;
								if (newTab == '1') //Quick Summery
								{
									$scope.getQuickSummery($scope.encounterId);
								}
								if (newTab == '2') {//Triage
									$scope.getTriageList($scope.encounterId);
								}
								if (newTab == '3') {//History
									$scope.getOtherHistory($scope.encounterId);
								}
								if (newTab == '4') //Physical Examination
								{
									$scope.getPhysicalExamination($scope.encounterId);
								}
								if (newTab == '5') {//Prescriptions
									$scope.getPrescrition($scope.encounterId);
								}
								if (newTab == '6') {//Laboratory
									$scope.getLabOrders($scope.encounterId);
								}
								if (newTab == '7') //Diagnosis
								{
									$scope.getDiagnosys($scope.encounterId);
								}
								if (newTab == '8') {//Procedures
									$scope.getDiagnosys($scope.encounterId);
								}
								if (newTab == '9') {//Radiology
									$scope.getRadiology($scope.encounterId);
								}
								if (newTab == '10') //Review Notes
								{
									$scope.getReviewNotes($scope.encounterId);
								}
								if (newTab == '11') {//Provisional Diagnosis
									$scope.getProvisionalDiagnosis($scope.encounterId);
								}
								
							};
							$scope.firstTabLevel = 1;
							$scope.setFirstTabLevel($scope.firstTabLevel);
							
							
						} ]);