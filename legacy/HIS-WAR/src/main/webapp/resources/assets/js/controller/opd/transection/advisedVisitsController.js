/**
 * @author sneha d
 */

angular
		.module('myApp')
		.controller(
				"advisedVisitsController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						'$interval',
						'$stateParams',
						'GenericServiceParamHeader',
						'BillingGenericService',
						'reportService',
						'$window',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl,$interval,$stateParams,GenericServiceParamHeader,BillingGenericService,reportService,$window) {
							
							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.sampleCollectionList = [];
							$scope.admissionObj = {};
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.advisedDateAppointmentListCount;

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.fromDate=new Date();
							$scope.toDate=new Date();
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							 
							 $scope.initMaster = function(){
									$scope.saveBtnFlag = true;
									$scope.updateBtnFlag = false;
									$scope.common = {
												
									};
									console.log("Common-init",$scope.common);
								}
								$scope.initMaster();
								
								/**for from date*/
								$scope.openFromDatePicker = function() {
    								$scope.fromDatePicker.opened = true;
    							};

    							$scope.fromDatePicker = {
    								opened : false
    							};
                                $scope.fromDateOptionsPicker = {
                                		formatYear : 'yyyy',
        								showWeeks : false,
        								minDate:new Date(), //max 
        							};
                                
                              //for to date
                                $scope.openToDatePicker = function() {
    								$scope.toDatePicker.opened = true;
    							};

    							$scope.toDatePicker = {
    								opened : false
    							};
                                $scope.toDateOptionsPicker = {
                                		formatYear : 'yyyy',
        								showWeeks : false,
        								minDate:new Date(), //max 
        							};
                                
                                $scope.selectedDoctor= function($item, $model, $label,idx){
									 $scope.doctorId=$item.doctorId;
								 }
                                
                                /**For pagination */
								$scope.setNoOfRecords = function() {
									$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								};
								
								$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
								{
									$rootScope.startLoader();
									
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
									var data1 = {
										      organizationId:$scope.organizationId,
												offset:offset,
												noOfRecordsPerPage:noOfRecordsPerPage,
												unitId:$scope.unitId,
												fromDate:moment($scope.fromDate).format("YYYY-MM-DD"),
												toDate : moment($scope.toDate).format("YYYY-MM-DD"),
												doctorId: $scope.doctorId!=undefined ? $scope.doctorId : null 
												};
									
									var data2 = {
											      organizationId:organizationId,
											      unitId:unitId
												};
												var URI1 = BASE_URL + ROOT_URL + "/api/opd/getAdvisedDateAppointmentList"; 
												var URI2 = BASE_URL + ROOT_URL + "/api/opd/getAdvisedDateAppointmentListCount";
										
									
									GenericService.serviceAction("POST",URI1,data1).then(function(response){
										if(response.data.status == "success"){
										$rootScope.stopLoader();					
										$scope.advisedDateAppointmentList = response.data.listObject;
										}
									});
									
									GenericService.serviceAction("POST",URI2,data2).then(function(response){
										if(response.data.status == "success"){					
											$rootScope.stopLoader();
											$scope.advisedDateAppointmentListCount = response.data.object;
											$scope.setPage(1,false);
										}
									});
									
								}		
								
								/**For master list*/
								$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
									$rootScope.startLoader();
									console.log("offset", offset);
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
									
									var data = {
											organizationId:$scope.organizationId,
											offset:offset,
											noOfRecordsPerPage:noOfRecordsPerPage,
											unitId:$scope.unitId,
											fromDate:moment($scope.fromDate).format("YYYY-MM-DD"),
											toDate : moment($scope.toDate).format("YYYY-MM-DD"),
											doctorId: $scope.doctorId!=undefined ? $scope.doctorId : null 
											};
									
									var URI = BASE_URL + ROOT_URL +  "/api/opd/getAdvisedDateAppointmentList";
									console.log("URI", URI);
									GenericService.serviceAction("POST", URI,data).then(function(response) {
										$rootScope.stopLoader();
										$scope.advisedDateAppointmentList = response.data.listObject;
										console.log("$scope.advisedDateAppointmentList", $scope.advisedDateAppointmentList);
									});
								}

								$scope.pager = {};
								$scope.page;

								$scope.setPage = function(page, flag) {
									if (page < 1 || page > $scope.pager.totalPages)
									{
										return;
									}
									$scope.pager = PagerService.GetPager($scope.advisedDateAppointmentListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
									if (flag)
									{
										$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
									}
								}

								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								
								/**For Doctor list*/
								$scope.searchGroupSurgen = function(keyword) {
									
									var data = {};
									var paramObj = {
											doctor_name : keyword
									};
									var headerObj = {
										unit_id : $scope.unitId,
										organization_id : $scope.organizationId
									}
									console.log(data);

									var URI = BASE_URL + ROOT_URL
											+ GETDOCTORMASTERLISTAUTOFILLSERACH;
									
									return GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															return response.data.listObject;
													});

								}
								
								/**For print advised date appointment report*/
								$scope.printAdvisedDateAppointment = function() {
									var data = {
										"appName" : "HIS",
										"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
										"reportDesignFilePath" : "PatientAdvised.rpt",
										"reportFormat" : "PDF",
										"exportFileName" : "PatientAdvised",
										"reportParams" : [ {
											 "paramName": "FromDate",
												"paramType": "string",
											"paramValue" : moment($scope.fromDate).format("YYYY-MM-DD")
	
											},
											{"paramName": "ToDate",
												"paramType": "string",
												"paramValue" : moment($scope.toDate).format("YYYY-MM-DD")
		
											},
											{
												"paramName": "organization_id",
												"paramType": "integer",
												"paramValue" : $scope.organizationId
		
											},
											{
												"paramName": "unit_id",
												"paramType": "integer",
												"paramValue" : $scope.unitId
		
											},
											{
												"paramName": "doctor_id",
												"paramType": "integer",
												"paramValue" : $scope.doctorId!=undefined ? $scope.doctorId:0
		
											}]
								};
								reportService.generate(REPORT_API_URI1, data);

							}
								
							/**For download csv file*/	
								$scope.obj={
										id:"",
										format:"",
										extension:""
										
								};
								$scope.getReportFormatesList = function() {
									var data = {};
									var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/getMISReportFormattesMasterforShowReports";

									GenericService.serviceAction("POST", URI, data).then(function(response) {
												
												$scope.getReportFormatesToShow = [];
												$scope.getReportFormatesToShow = response.data.listObject;
												
												$scope.obj.id = response.data.listObject[1].id;
												$scope.obj.format = response.data.listObject[1].format;
												$scope.obj.extension = response.data.listObject[1].fileExtension;
												console.log("formate data list",$scope.getReportFormatesToShow);
											});
								}
				           $scope.getReportFormatesList();
				           
				           $scope.getExcel = function(extension, format)
							{
								
								$rootScope.startLoader();
								
//										$scope.Obj.format=$scope.Obj.format;
//										$scope.Obj.extension=$scope.Obj.extension;
									var data =  { 
											 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath":"PatientAdvised.rpt",
							                 "reportFormat": $scope.obj.format,
							                 "exportFileName": "PatientAdvised",
							                 "reportParams": [{
												 "paramName": "FromDate",
													"paramType": "string",
												"paramValue" : moment($scope.fromDate).format("YYYY-MM-DD")
		
												},
												{"paramName": "ToDate",
													"paramType": "string",
													"paramValue" : moment($scope.toDate).format("YYYY-MM-DD")
			
												},
												{
													"paramName": "organization_id",
													"paramType": "integer",
													"paramValue" : $scope.organizationId
			
												},
												{
													"paramName": "unit_id",
													"paramType": "integer",
													"paramValue" : $scope.unitId
			
												},
												{
													"paramName": "doctor_id",
													"paramType": "integer",
													"paramValue" : $scope.doctorId!=undefined ? $scope.doctorId:0
			
												}]
								       };
									var URI =REPORT_DOWNLOAD_URI+"/"+$scope.organizationId+"/"+$scope.unitId;
									BillingGenericService.serviceAction("POST",URI,data,NOTIFICATION_MSG_STATUS_FALSE, 'blob').then(function(response) {
										if (response.data!=null)
											{ 
											 var blob = new Blob([response.data], {type: "octet-stream"});
											 var filename = moment().format('DD_MM_YYYY_HH_MM_SS');
											 saveAs(blob, filename+$scope.obj.extension);
											 $rootScope.stopLoader();
											}
										else {
											$rootScope.stopLoader();
										  }
									});
//								}	
							}
								
						} ]);