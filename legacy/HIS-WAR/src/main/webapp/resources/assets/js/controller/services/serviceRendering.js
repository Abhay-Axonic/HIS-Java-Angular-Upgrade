
/**
 * @Author By Vivek Satle
 * @name myApp.controller:patientAcceptanceController
 * @description #patientAcceptanceController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'serviceRendering',
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
						'$timeout',
						'PagerService',
						'BillingGenericService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$timeout,PagerService,BillingGenericService,$window) 
								{
						
							
							 var date = new Date();
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date(),
									minDate : date.setDate((new Date()).getDate() - 90),
								};
								$scope.todateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date(),
									minDate : new Date(),
								};

								//Date Object For Search DOB
								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};

								//Date Object For Reshedule
								$scope.openDatePickerReschedule = function() {
									$scope.datepickerReschedule.opened = true;
								};

								$scope.datepickerReschedule = {
									opened : false
								};

								//Date For From Date on Search

								$scope.openDatePickerFromDate = function() {
									$scope.datepickerFromDate.opened = true;
								};

								$scope.datepickerFromDate = {
									opened : false
								};

								//Date For To Date on Search

								$scope.openDatePickerToDate = function() {
									$scope.datepickerToDate.opened = true;
								};

								$scope.datepickerToDate = {
									opened : false
								};

								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
								 $state.go('applicationConfiguration');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
							$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
							$scope.radiologySpecialtyId = cookieObjectAppConfig.radiologySpecialityId;
							$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
							$scope.casualtySpecialityId = cookieObjectAppConfig.casualtySpecialityId;
							$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
							$scope.nephrologySpecialityId=cookieObjectAppConfig.hemodialysisSpecialityId;
							$scope.physiotherapySpecialityId=cookieObjectAppConfig.physiotherapySpecialityId;
							$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
							$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
						 	$scope.nurseRole = cookieObjectAppConfig.nurseRole;	
							$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
							$scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
							var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
				        	$scope.nursingStationsWardsListId = nursingStationsWardsList;
						    $rootScope.loginpage = true;
						    $scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.patientSearchObj = {};
							$scope.patientSearchObj.uhidNumber = '';
							$scope.patientSearchObj.visitTypeId = '2';
							$scope.patientSearchObj.patientName = '';
							$scope.patientSearchObj.genderId = '';
							$scope.birthDate = '';
							$scope.patientSearchObj.identificationTypeId = '';
							$scope.patientSearchObj.identificationNumber = '';
							
							
						    var labConfig = JSON.parse(localStorage.getItem('labConfig'));
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.labSpecialityId = labConfig==undefined ? 0 : labConfig.labSpecialityId;

							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ PATIENTAUTOFILLSEARCH;

								return BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}
							
							
							$scope.clearSearchObject = function() {
								$scope.patientSearchObj = {};
								$scope.patientSearchObj.uhidNumber = '';
								$scope.patientSearchObj.patientName = '';
								$scope.patientSearchObj.genderId = '';
								$scope.birthDate = '';
								$scope.patientSearchObj.identificationTypeId = '';
								$scope.patientSearchObj.identificationNumber = '';
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
								$scope.getServiceList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
							}
							
							$scope.isWardChanged = function()
					        {      
								$scope.searchPatientList = [];
								$scope.patientSearchObj.uhidNumber = '';
								$scope.patientSearchObj.patientName = '';
								$scope.patientSearchObj.genderId = '';
								$scope.birthDate = '';
								$scope.patientSearchObj.identificationTypeId = '';
								$scope.patientSearchObj.identificationNumber = '';

					            //start for nursing
					            var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
					            GenericService.serviceAction("GET",URI,{}).then(function(response){
					                if (response.data.status == "success") {
					                    $scope.departmentNursingStationMapperDtoslist = [];			  
					                    $scope.departmentNursingStationMapperDtoslist = response.data.listObject;
					                    //alert($scope.roleName);
					                    if($scope.nurseRole == 1){
					                        if($scope.departmentNursingStationMapperDtoslist.length > 0)
					                        {
					                            if($scope.departmentNursingStationMapperDtoslist.length > 1){
					                                angular.element('#nursingStationModal').modal('show');
					                                //$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
					                            }
					                            else
					                            {
					                                var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
					                                GenericService.serviceAction("GET",URI,{}).then(function(response){
					                                    if (response.data.status == "success") {
					                                        $scope.departmentNursingStationListC = {};
					                                        // console.log("fff",response.data.object);
					                                        $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;					  
					                                        sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
					                                        sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
					                                        sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
					                                        sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
					                                        //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
					                                        sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
					                                        $scope.wardListForGetBedList = response.data.object;
					                                    }
					                                });
					                            }
					                        }
					                    }
					                }
					            });        
					        }
							
							$scope.departmentNursing = {};
					        $scope.selectsetNursingStation = function()
					        {
					        	$scope.wardIdFromService = [];
					           
					            if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
					                growl.warning('Please select nursing station..!!');
					                return false;
					            }else{
					            $scope.wardListForGetBedList=[];
					            var data = JSON.parse($scope.departmentNursing);
					            //console.log("$scope.departmentNursing",$scope.departmentNursing);
					            //console.log("$scope.departmentNursing ID",data.nursingStationId);
					            var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
					                GenericService.serviceAction("GET",URI,{}).then(function(response){
					                    if (response.data.status == "success") {
					                        $scope.departmentNursingStationListC = {};
					                        //console.log('nursingStationsWardsList',response.data.object);
					                        $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
					                        sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
					                        sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
					                        sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
					                        sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
					                        $scope.wardListForGetBedList = response.data.object;
					                        $scope.nursingStationId = data.nursingStationId;
											sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
					                        $scope.wardListForGetBedList = response.data.object;
					                        $scope.nursingStationId = JSON.parse(sessionStorage.getItem('nursingStationId'));
					                        $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
					                        $scope.wardIdFromService = $scope.wardListForGetBedList;
					                        angular.element('#nursingStationModal').modal('hide');
					                    }
					                });
					            }
					        }
							
					    	$scope.searchPatient = function (patientSearchData) {
								$scope.searchPatientList = [];
								if (!!$scope.birthDate) {
									var birthDate = moment($scope.birthDate).format('YYYY-MM-DD');
								} else {
									var birthDate = undefined;
								}
								if($scope.wardListForGetBedList == undefined){
									$scope.wardListForGetBedList = $scope.nursingStationsWardsListId;
								}
					        	var URI = BASE_URL + ROOT_URL
								 + '/adt/patientSearchByMultipleCriteria/global';
					            var data = {
									"uhidNumber": patientSearchData.uhidNumber,
									"patientName": patientSearchData.patientName,
									"genderId": patientSearchData.genderId,
									"mobileNumber": patientSearchData.mobileNumber,
									"birthDate": birthDate,
									"identificationNumber": patientSearchData.identificationNumber,
									"identificationTypeId": patientSearchData.identificationTypeId,
									"unitId": $scope.unitId,
									"organizationId": $scope.organizationId,
									"wardList": $scope.wardListForGetBedList,
									"visitTypeId": $scope.patientSearchObj.visitTypeId
								}
					            $rootScope.startLoader();
					            GenericService.serviceAction("POST", URI, data).then(
								function (response) {
									$scope.searchPatientList = response.data.listObject; 
					                $rootScope.stopLoader();
								},
								function errorCallback(response) {
									$rootScope.stopLoader();
								});
					        }
					    	
							$scope.selectPatientId = function(info, model,
									label) {

								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;

								$scope.encounterSearchObject.patientId = info.patientId;
							}
							
							
							$scope.getReasonList = function()
						    {
								var data = {organizationId : $scope.organizationId};
								var URI = BASE_URL + ROOT_URL
									+ GETACTIVEREASONLIST;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$scope.reasonList = [];
				
														if (response.data.status == "success") {
															$scope.reasonList = response.data.listObject;
				
														}
													});
						    }
							$scope.getReasonList();
							/*$scope.getServiceList = function()
						    {
						    
								var deptList = [];
								if($scope.radiologySpecialtyId!=null)
									deptList.push($scope.radiologySpecialtyId);
								if($scope.labSpecialityId!=null)
									deptList.push($scope.labSpecialityId);
								if($scope.nephrologySpecialityId!=null)	
									deptList.push($scope.nephrologySpecialityId);
								if($scope.physiotherapySpecialityId!=null)	
									deptList.push($scope.physiotherapySpecialityId);
								
								$scope.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								
								var data ={
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										deptList : deptList,
										fromDate : $scope.fromDate,
										toDate : $scope.toDate
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +"/api/opd/pending/services/other";
						    
								GenericService
						        .serviceAction(METHOD_POST, URI,data,
						                NOTIFICATION_MSG_STATUS_FALSE)
						        .then(
						                function(response) {
						                    console.log(response);
						                    if (response.data.status == "success"){
						                        
						                        $scope.pendingServiceList = response.data.listObject;
						                        $rootScope.stopLoader();
						                    }else{
						                    	$rootScope.stopLoader();
						                    }
						                    
						                });
						        
						    }
							$scope.getServiceList();*/
							
							//For Paginations Start

							$scope.addPatientDetails = function(item, index) {
								var radio = document.getElementById('patientSelectRadioCommon_' + index);
								$scope.patientId = item.patientId;
								$scope.getServiceList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
								}
							$scope.setNoOfRecords = function() {
								$scope.getServiceList( $scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.getServiceList = function(orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var deptList = [];
								if($scope.radiologySpecialtyId!=null)
									deptList.push($scope.radiologySpecialtyId);
								if($scope.labSpecialityId!=null)
									deptList.push($scope.labSpecialityId);
								if($scope.nephrologySpecialityId!=null)	
									deptList.push($scope.nephrologySpecialityId);
								if($scope.physiotherapySpecialityId!=null)	
									deptList.push($scope.physiotherapySpecialityId);
								
								$scope.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								
								var data ={
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										deptList : deptList,
										patientId : $scope.patientId,
										//fromDate : $scope.fromDate,
										//toDate : $scope.toDate,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +"/api/opd/pending/services/other";
						    
								GenericService
						        .serviceAction(METHOD_POST, URI,data,
						                NOTIFICATION_MSG_STATUS_FALSE)
						        .then(
						                function(response) {
						                    console.log(response);
						                    if (response.data.status == "success"){
						                        
						                        $scope.pendingServiceList = response.data.listObject;
						                        $scope.commonListCount = response.data.object;
						            			$scope.pager = {};
						            			$scope.page;
						            			$scope.setPage(1,false);
						                        $rootScope.stopLoader();
						                    }else{
						                    	$rootScope.stopLoader();
						                    }
						                    
						                });


							}

							$scope.getServiceListMaster = function(orgId, offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								
								var deptList = [];
								if($scope.radiologySpecialtyId!=null)
									deptList.push($scope.radiologySpecialtyId);
								if($scope.labSpecialityId!=null)
									deptList.push($scope.labSpecialityId);
								if($scope.nephrologySpecialityId!=null)	
									deptList.push($scope.nephrologySpecialityId);
								if($scope.physiotherapySpecialityId!=null)	
									deptList.push($scope.physiotherapySpecialityId);
								
								$scope.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								
								var data ={
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										deptList : deptList,
										patientId : $scope.patientId,
										//fromDate : $scope.fromDate,
										//toDate : $scope.toDate,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL +"/api/opd/pending/services/other";
						    
								GenericService
						        .serviceAction(METHOD_POST, URI,data,
						                NOTIFICATION_MSG_STATUS_FALSE)
						        .then(
						                function(response) {
						                    console.log(response);
						                    if (response.data.status == "success"){
						                        
						                        $scope.pendingServiceList = response.data.listObject;
						                        
						                        $rootScope.stopLoader();
						                    }else{
						                    	$rootScope.stopLoader();
						                    }
						                    
						                });
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager( $scope.commonListCount, page, $scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getServiceListMaster($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							//$scope.getServiceList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);

							// For Paginations End
							
							
							$scope.getAccept = function(item)
						    {
						    
								item.unitId=$scope.unitId;
								item.organizationId=$scope.organizationId;
								item.userId=$scope.userId;
								item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
								item.specialityId=cookieObjectAppConfig.radiologySpecialityId;
								item.updatedBy = $scope.userId;
								item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
								//item.isPreOrPostBilling =$scope.isPreOrPostBilling;
								
								$rootScope.startLoader();
								
								var URI = BASE_URL + ROOT_URL +"/api/opd/render/services/other";
						    
								BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE)
						        .then(
						                function(response) {
						                    console.log(response);
						                    if (response.data.status == "success"){
						                        
						                    	$scope.getServiceList();
						                    	$rootScope.stopLoader();
						                    }else{
						                    	$scope.getServiceList();
						                    	$rootScope.stopLoader();
						                    } 
						                    
						                });
						        
						    }
							
							$scope.getInfo=function(info)
							{
								$scope.infoObject = info;
								
								var URI = BASE_URL + ROOT_URL 
								+ "/api/billing/getOrderDetailsPayeeData/"+info.orderDetailsId;
					    		  		
					    		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
								{	
					    			$scope.orderDetailsPayeeMapper = [];
					    			$scope.orderDetailsPackageMapper = [];
					    			if (response.data.status == "success") 
					    				{
												$scope.orderDetailsPayeeMapper = response.data.listObject;
												$scope.orderDetailsPackageMapper = response.data.object;
												
										}
								});
								
								
								
							}
							
							$scope.saveAcceptReject=function(rejectReasonId)
							{
								if(rejectReasonId==undefined || rejectReasonId=="")
								{
									growl.warning('Please select reason...!!',
											{
												title : 'Warning!'
											});
									return false;
								}else
								{
								
									 var data = 
										{
											admissionId :$scope.infoObject.admissionId,
											encounterId :$scope.infoObject.encounterId,
											orderDetailsId:$scope.infoObject.orderDetailsId,
											ordCancelReasonId : rejectReasonId,
											createdBy : $scope.userId,
											specialityId : $scope.infoObject.specialityId,
											serviceId:$scope.infoObject.serviceId,
											orderDetailsPayeeMapperDtosList : $scope.orderDetailsPayeeMapper,
						    				orderDetailsPackageMapperDtosList : $scope.orderDetailsPackageMapper
										}
										console.log('AddCapAmt',data);
										//return false;
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL +  "/global/consumption/service/cancel";
										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, data,
														NOTIFICATION_MSG_STATUS_TRUE)
												.then(
														function(response) 
														{
																$scope.getServiceList();
																$rootScope.stopLoader();
														});
								}
								
								
							}
							$scope.getClassForColor = function(info) {

								if (info.priorityId == 2) {
									return 'td-bg-red';
								}

							}
							
							$scope.patientDocumnetInformation = function(info) {
								$scope.patientDocumentFlipBook ={};
								$scope.patientDocumentFlipBook =info;
								
								
								
									var data =
									{
									"encounterId":$scope.patientDocumentFlipBook.encounterId,
									"patientId":$scope.patientDocumentFlipBook.patientId,
									"unitId":$scope.unitId,
									"organisationId":$scope.organizationId,
									"userId":$scope.userId
									}
									
									
								var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
								BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.patientDocumentListTest = [];
										if (response.data.status == "success") 
										{
											$scope.patientDocumentListTest = response.data.listObject;
											if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
												$('#patientDocumentOption').modal('show');
											}
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											$rootScope.stopLoader();
											}
									});
						
					          };
					          
					          $scope.convertDateToTimestamp = function(dateString) {
					                let parts = dateString.split("-");
					                let day = parseInt(parts[0], 10);
					                let month = parseInt(parts[1], 10) - 1; 
					                let year = parseInt(parts[2], 10);
					                let date = new Date(year, month, day);
					                let timestampInMilliseconds = date.getTime();
					                let timestampInSeconds = timestampInMilliseconds / 1000;
					                return timestampInSeconds;
					            };
					            
						     		$scope.generateFlipbookData = function() {
						                  let flipbookDatalist = [];

							     			var filteredlist=[];
							     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
							     		    	
							     		    	if(filterObj.documentPath != null){
							     				var flipbookDataobj={};
							     				 let createdDate = filterObj.createdDate;
							     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
							     				flipbookDataobj.timestamp=timestamp;
							     				flipbookDataobj.url=filterObj.documentPath;
							     				flipbookDataobj.tags=[];
							     				flipbookDataobj.tags.push("Document");
							     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
							     				flipbookDatalist.push(flipbookDataobj);
							     		    	}
							     				
							     			});

						                let data = {
						                    flipbookData: flipbookDatalist,
						                    logo: $scope.firstImgaa,
						                    privacy: "public"
						                };

						                let baseUrlLocation = $window.location.href;
						                let temp = baseUrlLocation.split('/');
						                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

						                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
						                
						        		try {
						        			var param={};
						        				$.ajax({
						        			        url : generateFlipbookurl,
						        					type : "POST",
						        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
						        					contentType : "application/json",
						        					dataType : "json",
						        					data : JSON.stringify(data),
						        			        //headers:getHeaders(),
						        					success : function(response){
						        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
						        							  window.open(''+response.flipbookUrl, '_blank');
						        						}else{
						        							$.notify("Error occurred","error");
						        						}
						        			    	}
						        			});
						        	     } catch (e) {
						        	 		console.log(e );
						        		}
						            };
							
								} ]);