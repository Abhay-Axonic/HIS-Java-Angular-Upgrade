'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:cleanedBedAcceptController
 * @description #cleanedBedAcceptController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'patientDischargedMasterController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'$stateParams',
						'reportService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, $stateParams,reportService) {

						$scope.init = function() {

							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.searchtxt = '';
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;

							$rootScope.loginpage = true;
							$scope.saveBtnFlag = false;
							$scope.updateBtnFlag = true;
							
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									//minDate : new Date()
								};
							$scope.openDatePicker = function() { 
								$scope.datepicker.opened = true;
							};
							$scope.openDatePickerTo = function() {
								$scope.datepickerTo.opened = true;
							};

							$scope.datepicker = {
									opened : false
								};
								$scope.datepickerTo = {
										opened : false
									};
							$scope.search = {
									fromDate : "",
									toDate : "",
									patientName : "",
									bedNumber : "",
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,

							}

							// for popup
							$scope.showModal = false;
							$scope.buttonClicked = "";
							$scope.popUpFlag = true;
							// end for popup
							
							$scope.bedHousekeeping = {}
							$scope.itemSelecteds = {};
							var data ={};
						}

						$scope.init();
							
							$scope.searchDischargedPatient=function(){
								
									console.log("$scope.search",$scope.search);
									var URI = BASE_URL + ROOT_URL+ "/adt/getPatientDischargedList";
									GenericService.serviceAction("POST", URI, $scope.search)
											.then(
													function(response) {
														$scope.dischargedPatientList = [];

														if (response.data.status == "success") {
															$scope.dischargedPatientList = response.data.listObject;
															
															console.log("dischargedPatientList",$scope.dischargedPatientList);
															
															
														}
													});
							}
							
							$scope.searchDischargedPatient();

		
							/***************Discharge Summary Report***********************/
							$scope.printDischargeSumReport=function(dischargeSummaryId)
							{
								//$scope.save();
								if(dischargeSummaryId!=null && dischargeSummaryId!=undefined){
									var data =  {
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "Dischargesummary.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "DischargeSummary",
								            "reportParams": [
								              {
									             "paramName": "Discharge_summary_Id",
									             "paramType": "Integer",
									             "paramValue": dischargeSummaryId
								              },
								              {
										             "paramName": "selectedHeader",
										             "paramType": "Integer",
										             "paramValue":  1
									              }
								            ]
								 };
									reportService.generate(REPORT_API_URI1,data);
								}
								else{
									growl.error("can not print the report",
											{
												title : 'Error!'
											});
									}
								
						}
						

						} ]);
