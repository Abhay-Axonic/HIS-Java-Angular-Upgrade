'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:serviceMasterController
 * @description #serviceMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'serviceMasterController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'GenericServiceFileUpload',
						'growl',
						'PagerService',
						'fileReader',
						'$state',
						'$stateParams',
						'$timeout',
						
						
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope,GenericService,BillingGenericService,GenericServiceFileUpload
								,growl,PagerService,fileReader,$state,$stateParams,$timeout) {

							$scope.specialityMasterList = [];
							$scope.subSpecialityMasterList = [];

							// for datePicker
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

							// for datePicker2
							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};
							$scope.openDatePickerPdd = function() {
								$scope.datepickerPdd.opened = true;
							};

							$scope.datepickerPdd = {
								opened : false
							};
							// init function description
							$scope.init = function() {
                                     
								$scope.filepreview = "";
								// file upload object 
								
								$scope.fileObj={
										csvfile:'',
								}
								
								
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.masterObj = {}
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								// ------------ Get Speciality Master List
								// ------------//

								try {
									var URI = BASE_URL + ROOT_URL
											+ GETSPECIALITY;
									var obj = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
									GenericService
											.serviceAction("POST", URI, obj)
											.then(
													function(response) {
														if (response.data.status == "success")
															$scope.specialityMasterList = response.data.listObject;
														console
																.log(
																		"specialityMasterList",
																		$scope.specialityMasterList);
													});
								} catch (e) {
									console.log("Exception", e.message);
								}

								// to get general ledger list
								var URI = BASE_URL + ROOT_URL
										+ GETGENERALLEDGERMASTERACTIVELIST
										+ "/" + $scope.organizationId;
								var data = {

								}
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.ledgerList = [];

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.ledgerList = response.data.listObject;
														console.log("ledgerList",$scope.ledgerList);
													}
												});
								/*// getting service list
								var URI = BASE_URL + ROOT_URL
										+ GET_ORGANIZATION_SERVICE_MASTER_LIST
										+ "/" + $scope.organizationId + "/0/50";
								var data = {

								}
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.serviceList = [];

													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.serviceList = response.data.listObject;
														console
																.log(
																		"serviceList",
																		$scope.serviceList);
													}
												});*/
							};
							$scope.init();
							
							
							$scope.searchItems = function(searchKeyword){

					        	$scope.itemList=[];
					        	
								var data={
										itemName : searchKeyword,
								};
								
								var URI = BASE_URL + ROOT_URL + "/api/globals/items/typeahead/search"
					    		return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					    			if(response.data.status == "success"){
					    				$scope.itemList = response.data.listObject;
					    			//	$scope.filterCountry=itemList;
					    				console.log("$scope.itemList",$scope.itemList);
					    			}
					    			//return $scope.itemList;
					    		});
					    	}
                            $scope.ledgerNameSearch = function(keyword)
                            {
                            	$scope.masterObj.generalLedgerId=null;
                                var URI = BASE_URL + ROOT_URL
                                + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
                                var data = {
                                        "generalLedgerName":keyword
                                    }
                                return GenericService
                                .serviceAction("POST", URI, data)
                                .then(
                                        function(response) {
                                            if (response.data.status == "success") {
                                                $rootScope.stopLoader();
                                                return response.data.listObject;
                                            }
                                        });
                            }
                            $scope.SelectLedgerName =function(ledger)
                            {
                                $scope.masterObj.generalLedgerId=ledger.generalLedgerId;
                            }
                            $scope.clearText = function()
						    {
						    	if($scope.masterObj.generalLedgerId==undefined || $scope.masterObj.generalLedgerId==null || $scope.masterObj.generalLedgerId==0)
						    		{
						    		$scope.masterObj.generalLedgerName="";
						    		}
						    }
							$scope.complete=function(string){
								var URI = BASE_URL + ROOT_URL + "/api/globals/items/item/search/" + string;

								return BillingGenericService.serviceAction("GET", URI).then(
												function(response) {
													if (!!response.data && response.data.length > 0)
														console.log("getListByName",response.data);
													return response.data;

					    		
												});
							}
							
							$scope.fillTextbox=function(string){
								$scope.country=string.itemName;
								$scope.itemId=string.itemId;
								$scope.itemName=string.itemName;
								
								$scope.filterCountry1=null;
							}
							
							
							$scope.qty = 0;
							$scope.itemObj = {
									"itemId":null,
									"itemName":""
							};
							  $scope.addMedicationItemList = function($item, $model, $label,idx){
						    		console.log("itemList",$item);
						    		
						    		 $scope.itemObj.itemId = $item.itemId;
						    		 $scope.itemObj.itemName = $item.itemName;						    		
						    	}
							
							  //add Inventroy Items 
							  $scope.inventoryItems = [];
							  $scope.addInventoryItem = function(){
								  $rootScope.startLoader();
								  if($scope.itemObj.itemId==null){
									  $rootScope.stopLoader();
									  growl.warning("Please Select Item",{
													title : 'warning'
												});
									  return false;
								  }
								  
								  if($scope.qty==0){
									  $rootScope.stopLoader();
									  growl.warning("Please Enter Quantity",{
													title : 'warning'
												});
									  return false;
								  }
								  $scope.isItemExist = false;
								  if($scope.inventoryItems.length>0){
										angular.forEach($scope.inventoryItems,function(value,index){
											if(value.itemId == $scope.itemObj.itemId){
												$scope.isItemExist = true;
											}
										});
									}
								  
								  if($scope.isItemExist){
										growl.warning("Item Already Exist",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
										
									}
								  var newArr = {};
								  newArr.itemId =  $scope.itemObj.itemId;
								  newArr.itemName =  $scope.itemObj.itemName;
								  newArr.assignQty =  $scope.qty;
								  newArr.status =  'A';
								  newArr.serviceItemMapperId =  null;
								  $scope.inventoryItems.push(newArr);
								  $scope.itemObj = {};
								  $scope.qty = 0;
								  $scope.itemName = "";
								  $rootScope.stopLoader();
							  }
							  
							  $scope.removeInventoryItem = function(index){
								  $rootScope.startLoader();
									  if($scope.inventoryItems[index].serviceItemMapperId!=null){
										  $scope.inventoryItems[index].status = 'I';
									  }else{
										  $scope.inventoryItems.splice(index,1);
									  }
								  $rootScope.stopLoader();
							  }
							  $scope.serviceItemMapperList = [];
							  $scope.addServiceItemMapperList = function(){
								  $rootScope.startLoader();
								  $scope.serviceItemMapperList = [];
								  if($scope.inventoryItems.length>0){
									  angular.forEach($scope.inventoryItems,function(value,index){
										  var newArr = {};
										  newArr.serviceId =  null;
										  newArr.itemId =  value.itemId;
										  newArr.serviceItemMapperId =  value.serviceItemMapperId;
										  newArr.assignQty =  parseInt(value.assignQty);
										  newArr.organizationId =  $scope.organizationId;
										  newArr.createdDate =  moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										  newArr.updatedDate =  moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										  newArr.createdBy =  $scope.userId;
										  newArr.updatedBy =  $scope.userId;
										  newArr.status =  value.status;
										  $scope.serviceItemMapperList.push(newArr);  
									  });
									  
									  console.log("$scope.serviceItemMapperList",$scope.serviceItemMapperList);
									  $('#inventoryDetails_modal').modal('hide');
									  $rootScope.stopLoader();
								  }else{
									  growl.warning("Please Add Atleast One item",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
								  }
								  
							  }
							  
							  //get Inventory Item List by serviceMasterId
							  $scope.savedInventoryItems = [];
							  $scope.getInventoryItemList = function(serviceMasterId,flag){
								  $rootScope.startLoader();
								  var data = {
										  serviceId : serviceMasterId,
											organizationId : $scope.organizationId,
										};
										var URI = BASE_URL + ROOT_URL + "/billing/getServiceItemMapperDetails";
								  GenericService.serviceAction("POST", URI, data).then(function(response) {
												$scope.inventoryItems = [];
												 $scope.savedInventoryItems = [];
												if (response.data.status == "success") {
													if(flag){
													  $scope.inventoryItems = response.data.listObject;
													}else{
														 $scope.savedInventoryItems = response.data.listObject;
													}
												}
											});
								  $rootScope.stopLoader();
							  }
							  
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initServiceList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initServiceList = function(
									orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
								+ GET_ORGANIZATION_SERVICE_MASTER_LIST
								+ "/" + $scope.organizationId + "/"+offset+"/"+noOfRecordsPerPage;
								var URI1 = BASE_URL + ROOT_URL
										+ GET_ORGANIZATION_SERVICE_MASTER_COUNT+"/"+$scope.organizationId;

								var data = {
									
								}

								var data1 = {
									
								}

								// For Get Employee Category List Using Offset
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.serviceList = [];

													if (response.data.status == "success") {
														$scope.serviceList = response.data.listObject;
														console.log("serviceList",$scope.serviceList);

													}
												});

								// For Count service List
								GenericService
										.serviceAction("GET", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log(response.data);
														$scope.commonListCount = response.data.object;
														if($scope.updateBtnFlag==true){
															console.log($scope.pageNo)
															$scope.setPage($scope.pageNo,true);
														}
														else{
															console.log("else"+$scope.pageNo)
															$scope.setPage(1,false);

														}
														$scope.saveBtnFlag = true;
														$scope.updateBtnFlag=false;
													}
												});
							}

							var pageOffset='';
							$scope.getServiceList = function(orgId,
									offset, noOfRecordsPerPage) {
								pageOffset=offset;
								console.log("offset", offset);
								console.log(noOfRecordsPerPage)
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
								+ GET_ORGANIZATION_SERVICE_MASTER_LIST
								+ "/" + $scope.organizationId + "/"+offset+"/"+noOfRecordsPerPage;
								var data = {
									
								}
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.serviceList = [];

													if (response.data.status == "success") {
														$scope.serviceList = response.data.listObject;

													}
												});
							}

							$scope.pager = {};
							$scope.page;
							//
							$scope.setPage = function(page, flag) {
							    if($scope.pager.totalPages==0){
                                    $scope.pager.totalPages =  undefined;
                                }
								console.log(page)
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getServiceList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initServiceList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End
							// ------------ Get SubSpeciality By SpecialityId
							// ----------//
							/*$scope.getSubSpecialityBySpecialityId = function(
									specialityId) {
								if (specialityId != null) {
									console.log("chk", specialityId);
									var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/"+specialityId;
									var obj = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										specialityId : specialityId
									}
									GenericService
											.serviceAction("POST", URI, obj)
											.then(
													function(response) {
														if (response.data.status == "success")
															$scope.subSpecialityMasterList = response.data.listObject;
														console
																.log(
																		"subSpecialityMasterList",
																		$scope.subSpecialityMasterList);
													});
								} else {
									console.log("department not selected yet");
								}
							}*/
							$scope.getSubSpecialityBySpecialityId = function(specialityId){
								if(specialityId != null){
								var URI = BASE_URL + ROOT_URL + "/adt/subspeciality/orgId/"+$scope.organizationId+"/specialityId/"+specialityId;
								BillingGenericService.serviceAction(METHOD_GET,URI, null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.subSpecialityMasterList= [];
									if (response.data.status == "success")
										$scope.subSpecialityMasterList = response.data.listObject;
								});
								}
							}
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveServiceMaster();
							}
							// ------------ Save Organization Service Master
							// ---------------//
							$scope.saveServiceMaster = function() {
								  $scope.$broadcast('show-errors-check-validity');
									if ($scope.serviceMasterForm.$valid) {
								try {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
									$rootScope.startLoader();
									$scope.currentDate = new Date();
									$scope.currentDate = moment(
											$scope.currentDate).format(
											'DD-MM-YYYY HH:mm:ss');
									var data = {
										specialityId : $scope.masterObj.specialityId,
										subSpecialityId : $scope.masterObj.subSpecialityId,
										serviceStandardName : $scope.masterObj.serviceName,
										codificationServiceName : $scope.masterObj.codificationServiceName,
										serviceStandardCode : $scope.masterObj.serviceCode,
										generalLedgerId : $scope.masterObj.generalLedgerId,
										/*validFrom : moment($scope.datepicker)
												.format('DD-MM-YYYY HH:mm:ss'),
										validTill : moment($scope.datepickerPdd)
												.format('DD-MM-YYYY HH:mm:ss'),*/
										status : 'A',
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										organizationId : $scope.organizationId,
										createdDate : $scope.currentDate,
										updatedDate : $scope.currentDate,
										serviceItemMapperList : $scope.serviceItemMapperList
									}
//									 console.log("masterObj",data); return
									// false;
									var URI = BASE_URL + ROOT_URL + BILLING
											+ SAVE_ORGANIZATION_SERVICE_MASTER;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.init();
															growl
																	.success(
																			response.data.message,
																			{
																				title : 'Success!'
																			});
															$rootScope.stopLoader();
														} else {
															growl
																	.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}
														angular.element("input[type='file']").val(null);
														 $scope.inventoryItems = [];
														 $scope.savedInventoryItems = [];
													$scope
														.initServiceList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage);
													});
									 }
									} catch (e) {
									console.log("Exception", e.message);
								}
									}
							}
							// Function For get single Service()
							$scope.showUpdateBtn = function(id) {
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								
								var data = {
										serviceMasterId:id,
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
								}
								var URI = BASE_URL + ROOT_URL
										+"/billing"+ GET_ORGANIZATION_SERVICE_MASTER_BY_ID;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
//													console.log("BYId",response);
													// $scope.speciality =[];
													if (response.data.status == "success") {
														console.log("object",response.data.object);
														$scope.masterObj.serviceMasterId=response.data.object.serviceMasterId;
														$scope.masterObj.specialityId=response.data.object.specialityId.toString();
														 $scope.masterObj.subSpecialityId = response.data.object.subSpecialityId.toString();
														 $scope.masterObj.serviceName = response.data.object.serviceStandardName;
														 $scope.masterObj.codificationServiceName = response.data.object.codificationServiceName;
														 $scope.masterObj.serviceCode=response.data.object.serviceStandardCode;
														 if(!response.data.object.generalLedgerId){
                                                            $scope.masterObj.generalLedgerName="";
                                                         }
														 $scope.masterObj.generalLedgerId=response.data.object.generalLedgerId.toString();
	                                                     $scope.masterObj.generalLedgerName = response.data.object.generalLedgerName.toString();
														/* $scope.datepicker=new Date(response.data.object.validFrom);
														 $scope.datepickerPdd=new Date(response.data.object.validTill);*/
														 $scope.getSubSpecialityBySpecialityId($scope.masterObj.specialityId);
//														 alert($scope.masterObj.subSpecialityId);
														 $scope.getInventoryItemList($scope.masterObj.serviceMasterId,true); 
													} 
													else {
														alert("Error!!");
													}
													
												});
							};

							// Function For update service status
							$scope.updateStatus = function(id, type) {
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');

								type = (type == "A") ? "A" : "I";

								var data = {
										serviceMasterId : id,
										organizationId : $scope.organizationId,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL
										+"/billing"+ CHANGE_ORGANIZATION_SERVICE_MASTER_STATUS;
								GenericService
										.serviceAction("PUT", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.init();
														growl
																.success(
																		response.data.message,
																		{
																			title : 'Success!'
																		});
													} else {
														growl
																.error(
																		response.data.message,
																		{
																			title : 'Error!'
																		});
													}

												
												});
							}
							$scope.pageNo='';
							// Function For updateServiceMaster()
							$scope.updateServiceMaster = function() {
								  $scope.$broadcast('show-errors-check-validity');
									if ($scope.serviceMasterForm.$valid) {
								try {
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
//								$scope.$broadcast('show-errors-check-validity');
								var data = {
										specialityId : $scope.masterObj.specialityId,
										subSpecialityId : $scope.masterObj.subSpecialityId,
										serviceStandardName : $scope.masterObj.serviceName,
										codificationServiceName : $scope.masterObj.codificationServiceName,
										serviceStandardCode : $scope.masterObj.serviceCode,
										generalLedgerId : $scope.masterObj.generalLedgerId,
										/*validFrom : moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
										validTill : moment($scope.datepickerPdd).format('DD-MM-YYYY HH:mm:ss'),*/
										status : 'A',
										updatedBy : $scope.userId,
										organizationId : $scope.organizationId,
										updatedDate : $scope.currentDate,
										serviceMasterId:$scope.masterObj.serviceMasterId,
										serviceItemMapperList : $scope.serviceItemMapperList
										
								}
								
								
//								if ($scope.employeeCategoryForm.$valid) {
									console.log("update",data);  
									var URI = BASE_URL + ROOT_URL
											+ UPDATEGLOBALSERVICEMASTER;
									GenericService
											.serviceAction("PUT", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.masterObj = {}
															$scope.datepicker= {};
															$scope.datepickerPdd= {};
															angular.element("input[type='file']").val(null);
															/*$scope.init();*/
															growl.success(
																			"Updated successfully",
																			{
																				title : 'Success!'
																			});
															$rootScope.stopLoader();
															$scope.inventoryItems = [];
															 $scope.savedInventoryItems = [];
															$scope.offset=pageOffset;
															console.log($scope.offset)
															$scope.initServiceList(
																	        $scope.organizationId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
															$scope.pageNo=($scope.offset/10)+1;
														    $scope.setPage($scope.pageNo,true)
														    console.log("pageNo"+$scope.pageNo)
														    
														} else {
															growl.error(
																			response.data.message,
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}
														
														/*$scope.pager.currentPage=7;*/
													});
								} catch (e) {
									console.log("Exception", e.message);
								}
									}
							}
							
							
							/***============================================File Upload and download==============================================*/
						
							$scope.fileObj={
									csvfile:$scope.csvfile,
							}
							
							$scope.filepreview = "";
							$scope.filepreview = $scope.csvfile;
								$scope.csvfile;
								 
							$scope.failureRecordCount=0;		
							$scope.uploadfile = function() {
									try {
									   $rootScope.startLoader();
										var fileName = $scope.csvfile;
								        var data = new FormData();
								        data.append('file',fileName );
								        console.log("listData", data);
								        var config = {
								        	   	transformRequest: angular.identity,
								        	   	transformResponse: angular.identity,
								    	   		headers : {
								    	   			unitId:$scope.unitId,
													organizationId:$scope.organizationId,
													updatedBy : $scope.userId,
													createdBy : $scope.userId,
								    	   			'Content-Type': undefined
								    	   	    }
								           }
									

                                        if($scope.csvfile){
											var url=BASE_URL + ROOT_URL + "/billing/saveServiceMaster1";
									          $http.post(url, data, config)
									           .then(function (response)  
									        	{
									        	   $rootScope.stopLoader();
									        	   var response= JSON.parse(response.data);
									        	   if(response.status == 'success'){
									        		var successCount=response.object.successCount;
									        		var failedCount=response.object.failureCount;
									        		$scope.failureRecordCount=failedCount;
                                                   var message="";
									        		if(failedCount==0){
									        			message="All records saved successfully with count="+successCount;
									        			growl.success(message,
																{
																	title : 'SUCCESS'
																		
																});
									        		}else if(successCount==0){
									        			message="All record failed with count="+failedCount;
									        			growl.warning(message,
																{
																	title : 'Warning!'
																		
																});
									        		}else{
									        			message="Saved record count="+successCount+" failed record count="+failedCount;
									        			growl.warning(message,
																{
																	title : 'Warning!'
																		
																});
									        		}
									        	 
									        	   }
									        	   else if(response.status== 'error'){
									        			growl.error(response.message,
																{
																	title : 'Already Exist'
																		
																});
										           }
											});
								
										
                                        }
                                        else
                                      	  {
                                      	  growl.error("Please choose .CSV file",
														{
															title : 'ERROR'
																
														});
                                      	   $rootScope.stopLoader();
                                      	  }
									} catch (e) {
										console.log("Exception-", e);
									}
								}
							
						    /***============================================Code file upload ==============================================*/

						$scope.serachServiceMaster = function(searchString){
							$scope.serviceList=[];
							$scope.loadingMsg="Loading...";
							if(searchString!=''){
								var data = {
									organizationId : $scope.organizationId,
									serviceStandardName : searchString
								};
							var URI = BASE_URL + ROOT_URL + "/billing/serviceMasterUniversalSearch"
									
							console.log("URI", URI);
							GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
												$rootScope.stopLoader();
												$scope.commonListCount=0;
												$scope.serviceList = response.data.listObject;
												$scope.commonListCount = response.data.listObject.length;
												$scope.setPage(1,false);
												if($scope.serviceList.length==0)
													$scope.loadingMsg="No record found...";
								});
							}else{
								$scope.initServiceList($scope.organizationId,$scope.offset,$scope.noOfRecordsPerPage);
							}
							$rootScope.stopLoader();
						}
						
						$scope.listForClearData = [];
						$scope.clearData = function(){
							$scope.listForClearData = [];
							if($scope.inventoryItems.length>0){
								angular.forEach($scope.inventoryItems,function(value,index){
									if(value.serviceItemMapperId!=null){
										value.status='A';
										$scope.listForClearData.push(value); 
									}
								});
								$scope.inventoryItems = $scope.listForClearData; 
							}
						
						}
						
						} ]);
