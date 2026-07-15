'use strict';
angular
		.module('myApp')
		.controller(
				'patientTypeDetailsController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService ) {
							
							$(".selectedPageName").text("Patient Type Details ")
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.specialityList=[];
							$scope.subSpecialityList=[];
							$scope.groupDetailList=[];
							$scope.assetProductDetailList=[];
							$scope.tariffMuliSelectList = [];
							$scope.selectedTariffList = [];
							$scope.selectedVisitType = [];
							$scope.selectedPaymentEnt = [];
							$scope.selectedPatientTypeList = [];
							
							$scope.productCategoryList=[];
							$scope.assetCategoryList= [];
							
							$scope.groupDetailsObj = {
									specialityId: '',
									subSpecialityId: '',
									markupStatus: ''
								};
							
							$scope.pharmacyDetailsObj = {
									assetCategoryId: '',
									productCategoryId: '',
									markupStatus: ''
								};
							
							$scope.serviceDetailsObj = {
									specialityId: '',
									subSpecialityId: '',
									markupStatus: '',
									specialityName: '',
									subSpecialityName: '',
									percentage: '',
									serviceId: ''
								};
							$scope.patientTypeObj = {
									code: '',
									description: '',
									patientTypeId: ''
								};
							$scope.patientTypeList = [];
							$scope.specialityList= [];
							$scope.serviceDetailList=[];
							$scope.serviceListBySpeciality=[];
							$scope.getSpecialityList = function () {
								var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
								var obj = { organizationId: $scope.organizationId, unitId: $scope.unitId };
								GenericService.serviceAction(METHOD_POST, URI, obj).then(function (response) {
									if (response.data.status == "success")
										$scope.specialityList = response.data.listObject;
								});
							}
							$scope.saveBtnFlag = true;
							$scope.getSpecialityList();
							
							$scope.init = function(){
								
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
									// for getting active tariff master
									var URI = BASE_URL + ROOT_URL
											+ GETACTIVETARIFFMASTERLIST;
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.tariffList = response.data.listObject;
															console.log("ActiveTariffMasterList",$scope.tariffList);
															
															angular.forEach(response.data.listObject,
																	function(value,index) {
																		var data = {};
																		data.id = value.tariffId;
																		data.label = value.tariffDesc;
																		$scope.tariffMuliSelectList.push(data);
															});
															
														}
													});	
									
								
									
							}
							
								var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/1/"+$scope.unitId+"/"+$scope.organizationId;
								GenericService.serviceAction(METHOD_GET, URI, null)
								.then(function(response){
									$rootScope.stopLoader();
									if(response.status == 200)
										
										$scope.assetCategoryList = response.data;
								});
							
							$scope.init();
							
							$scope.getProductCatByAssestCat = function(obj)
							{
								$rootScope.startLoader();
								
								$scope.pharmacyDetailsObj.assetCategoryName = obj.category;
								$scope.pharmacyDetailsObj.assetCategoryId = obj.id; 
								
								$scope.pharmacyDetailsObj.productCategoryName = null;
								$scope.pharmacyDetailsObj.productCategoryId = null; 
								
								if(obj.id != "" && typeof(obj.id) != "undefined"){
									var URI = BASE_URL + ROOT_URL + "/api/global/products/categories/"+obj.id+"/"+$scope.organizationId;
									GenericService.serviceAction(METHOD_GET, URI, null)
									.then(function(response){
										$rootScope.stopLoader();
										if(response.status == 200)
											$scope.productCategoryList = response.data;
										else
											$scope.productCategoryList = [];
										
									});
								}
								
							}
							
							$scope.selectAssestCat = function(obj)
							{
								$scope.pharmacyDetailsObj.productCategoryName = obj.category;
								$scope.pharmacyDetailsObj.productCategoryId = obj.id; 
							}
							
							$scope.searchServices = function(searchKeyword){
								$scope.servicesList=[];
								searchKeyword=searchKeyword+"---"+0;
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
								
								//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.servicesList = response.data.listObject;
									}
									return $scope.servicesList;
								});
							}
							
						    $scope.addServiceToTempServiceList = function($item, $model, $label)
						    {
						    	$scope.selectedServiceId = $item.serviceMasterId;
						    	$scope.selectedServiceName = $item.serviceStandardName +'/' + $item.serviceStandardCode;
						    	
						    	$scope.groupDetailsObj.specialityName = $item.specialityName;
								$scope.groupDetailsObj.specialityId = $item.specialityId; 
								
								$scope.groupDetailsObj.subSpecialityName = $item.subSpecialityName;
								$scope.groupDetailsObj.subSpecialityId = $item.subSpecialityId; 
								
							}
						    
						    
						    $scope.searchItems = function(searchKeyword){
								$scope.itemList=[];
								var URI = BASE_URL + ROOT_URL + "/global/itemMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.itemList = response.data.listObject;
									}
									return $scope.itemList;
								});
							}
							 
							
							 $scope.addItemToBill = function($item, $model, $label)
							    {
							    	$scope.selectedItemId = $item.itemId;
							    	$scope.selectedItemName = $item.itemName +'/' + $item.itemCode;
							    	
							    	$scope.pharmacyDetailsObj.productCategoryName = $item.productCategoryName;
									$scope.pharmacyDetailsObj.productCategoryId = $item.productCategoryId; 
									
									$scope.pharmacyDetailsObj.assetCategoryName = $item.assetCategoryName;
									$scope.pharmacyDetailsObj.assetCategoryId = $item.assetCategoryId; 
									
								}
							 
							
							$scope.activeVisitType=function(){							
								$scope.visitTypeList = [
									{
										id:3,
										label:"Day Care"
									},
									{
										id:4,
										label:"ER"
									},
									{
										id:2,
										label:"IP"
									},
									{
										id:1,
										label:"OP"
									}
								];
							}
							$scope.activeVisitType();
							

							$scope.activePaymentEntitlementMultiselectList=function(){							
								
								$scope.paymentEntitlementMultiselectList = [
									{
										id:3,
										label:"Company"
									},
									{
										id:2,
										label:"Insurance"
									},
									{
										id:1,
										label:"Self Entitlement"
									},
								];
							}
							$scope.activePaymentEntitlementMultiselectList();
							
							$scope.activePatientType=function(){
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId
									}
								var URI = BASE_URL + ROOT_URL
									+ "/global/patientcategory/"+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(function(response) {
													$scope.patientTypeListMain = [];
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.id;
																			data.label = value.name;
																			$scope.patientTypeListMain.push(data);
																		});
														console.log('patientTypeList',$scope.patientTypeListMain);
													}
												});
							}
							$scope.activePatientType();
							
							
							var isGroupExists;
							function checkGroupExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.specialityId == parseInt(arr.specialityId) && (arr.subSpecialityId == '' || arr.subSpecialityId == null) && (arr.serviceId == '' || arr.serviceId == null || arr.serviceId == undefined))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							
							var isGroupSubgroupExists;
							function checkGroupSubGroupExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.specialityId == parseInt(arr.specialityId) && val.subSpecialityId == parseInt(arr.subSpecialityId)  && (arr.serviceId == '' || arr.serviceId == null || arr.serviceId == undefined))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							
								var isServiceExists;
							function checkSerivceExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.specialityId === parseInt(arr.specialityId) && val.subSpecialityId === parseInt(arr.subSpecialityId)  && val.serviceId === parseInt(arr.serviceId))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							
							//start for Pharmacy
							
							var isAssetCategoryExists;
							function checkAssetCategoryExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.assetCategoryId == parseInt(arr.assetCategoryId)   && (arr.productCategoryId == '' || arr.productCategoryId == null) && (arr.itemId == '' || arr.itemId == null || arr.itemId == undefined))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							
							var isAssetProductExists;
							function checkAssetProductExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.assetCategoryId == parseInt(arr.assetCategoryId) && val.productCategoryId == parseInt(arr.productCategoryId)   && (arr.itemId == '' || arr.itemId == null || arr.itemId == undefined))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							
								var isItemExists;
							function checkItemExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.assetCategoryId === parseInt(arr.assetCategoryId) && val.productCategoryId === parseInt(arr.productCategoryId)  && val.itemId === parseInt(arr.itemId))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							//End For Pharmecy
							
							
							$scope.removeProcedure = function(index){
								$scope.procedureWiseList.splice(index,1);
							}
							
							
							$scope.popupForSaveDetails = function(){
								
								if($scope.patientTypeId == '' || $scope.patientTypeId == null)
								{
								    growl.warning("Please select patient class",
										{
											title : 'Warning!'
										});
								    return false;

								}

							else if($scope.selectedTariffList.length == 0)
								{
								    growl.warning("Please select tariff",
										{
											title : 'Warning!'
										});
								    return false;

								}
							else if($scope.selectedVisitType.length == 0)
							{
							    growl.warning("Please select visit type",
									{
										title : 'Warning!'
									});
							    return false;

							}
							else if($scope.selectedPaymentEnt.length == 0)
							{
							    growl.warning("Please select payment entitlement",
									{
										title : 'Warning!'
									});
							    return false;

							}
							else if($scope.selectedPatientTypeList.length == 0)
							{
							    growl.warning("Please select patient type",
									{
										title : 'Warning!'
									});
							    return false;

							}
							else{
							 if($scope.groupDetailList.length != 0){
								 angular.forEach($scope.groupDetailList,function(value,index) {
									if(value.percentage<0)
									{
										growl.warning("Negative data should not be accepted",
										{
											title : 'Warning!'
										});
									}
									else
									{
										angular.element('#confirmSavePatientTypeDetails').modal('show');
									}									
								})
							}
						    else
							{
									angular.element('#confirmSavePatientTypeDetails').modal('show');
							}	
								
							}
								
								
							}
							
							$scope.getSubSpecialityBySpeciality = function(obj)
							{
								$scope.groupDetailsObj.specialityName = obj.specialityName;
								$scope.groupDetailsObj.specialityId = obj.specialityId; 
								
								$scope.groupDetailsObj.subSpecialityName = null;
								$scope.groupDetailsObj.subSpecialityId = null; 
								/*
								$scope.serviceDetailsObj.specialityName = obj.specialityName;
								$scope.serviceDetailsObj.specialityId = obj.specialityId; */
								
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
									data.specialityId = obj.specialityId;
									var URI = BASE_URL + ROOT_URL
											+ GETSUBSPECIALITYBYSPECIALITYID;
									GenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														$scope.subSpecialityList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.subSpecialityList = response.data.listObject;
													});
								
							}
							
							$scope.getSubSpecialityDetails = function(obj2)
							{
								$scope.groupDetailsObj.subSpecialityName = obj2.subSpecialityName;
								$scope.groupDetailsObj.subSpecialityId = obj2.subSpecialityId; 
								
								/*$scope.serviceDetailsObj.subSpecialityName = obj2.subSpecialityName;
								$scope.serviceDetailsObj.subSpecialityId = obj2.subSpecialityId; */
							}
							
							
							$scope.addProductCategory = function()
							{
								if($scope.patientTypeId == '' || $scope.patientTypeId == null)
								{
								    growl.warning("Please select patient class",
										{
											title : 'Warning!'
										});
								    return false;

								}

								else if($scope.selectedTariffList.length == 0)
									{
									    growl.warning("Please select tariff",
											{
												title : 'Warning!'
											});
									    return false;
	
									}
								else if($scope.selectedVisitType.length == 0)
								{
								    growl.warning("Please select visit type",
										{
											title : 'Warning!'
										});
								    return false;
	
								}
								else if($scope.selectedPaymentEnt.length == 0)
								{
								    growl.warning("Please select payment entitlement",
										{
											title : 'Warning!'
										});
								    return false;
	
								}
								else if($scope.selectedPatientTypeList.length == 0)
								{
								    growl.warning("Please select patient type",
										{
											title : 'Warning!'
										});
								    return false;
	
								}
								
								else if(($scope.pharmacyDetailsObj.assetCategoryId == '' || $scope.pharmacyDetailsObj.assetCategoryId == null) &&  ($scope.selectedItemId == '' || $scope.selectedItemId == null || $scope.selectedItemId == undefined))
									{
									    growl.warning("Please select asset category / item",
											{
												title : 'Warning!'
											});
									    return false;

									}
								else if($scope.pharmacyDetailsObj.markupStatus == '' || $scope.pharmacyDetailsObj.markupStatus == null || $scope.pharmacyDetailsObj.markupStatus == undefined){
									growl.warning("Please select mark status",
											{
												title : 'Warning!'
											});
									    return false;
								}
								else
								{
								
								var data={
										markupStatus : $scope.pharmacyDetailsObj.markupStatus,
										assetCategoryId : $scope.pharmacyDetailsObj.assetCategoryId,
										assetCategoryName : $scope.pharmacyDetailsObj.assetCategoryName,
										productCategoryId : $scope.pharmacyDetailsObj.productCategoryId,
										productCategoryName : $scope.pharmacyDetailsObj.productCategoryName,
										itemId : $scope.selectedItemId,
								    	itemName : $scope.selectedItemName,
										percentage: 0,
										organizationId : $scope.organizationId,
										unitId: $scope.unitId,
										createdBy: $scope.userId,
										updatedBy: $scope.userId,
										patientTypeId:$scope.patientTypeId
								};
								
								
								if($scope.pharmacyDetailsObj.assetCategoryId > 0 && ($scope.pharmacyDetailsObj.productCategoryId == '' || $scope.pharmacyDetailsObj.productCategoryId == null) && ($scope.selectedItemId == '' || $scope.selectedItemId == null || $scope.selectedItemId == undefined)){
									
									 isAssetCategoryExists=checkAssetCategoryExistsOrNot($scope.assetProductDetailList,data)
									if (!isAssetCategoryExists) {
										$scope.assetProductDetailList.push(data);
										console.log($scope.assetProductDetailList);
										$scope.pharmacyDetailsObj = {};
										$scope.selectedItemId = null;
								    	$scope.selectedItemName = null;
								    	$scope.searchItem = '';
								    	$scope.pharmacyobj = null;
								    	$scope.pharmacyProductobj = null;
										
									}
									else
										{
											growl.warning("Asset Category already added in the list",{
												title : "WARNING"
											});
											return;
										}
									
								}
								
								if($scope.pharmacyDetailsObj.assetCategoryId > 0  && $scope.pharmacyDetailsObj.productCategoryId > 0 &&  ($scope.selectedItemId == '' || $scope.selectedItemId == null || $scope.selectedItemId == undefined)){
									
									isAssetProductExists=checkAssetProductExistsOrNot($scope.assetProductDetailList,data)
									if (!isAssetProductExists) {
										$scope.assetProductDetailList.push(data);
										console.log($scope.assetProductDetailList);
										$scope.pharmacyDetailsObj = {};
										$scope.selectedItemId = null;
								    	$scope.selectedItemName = null;
								    	$scope.searchItem = '';
								    	$scope.pharmacyobj = null;
								    	$scope.pharmacyProductobj = null;
										
									}
									else
										{
											growl.warning("Product category already added in the list",{
												title : "WARNING"
											});
											return;
										}
									
								}
								
								if($scope.selectedItemId > 0){
									
									isItemExists=checkItemExistsOrNot($scope.assetProductDetailList,data)
									if (!isItemExists) {
										$scope.assetProductDetailList.push(data);
										console.log($scope.assetProductDetailList);
										$scope.pharmacyDetailsObj = {};
										$scope.selectedItemId = null;
								    	$scope.selectedItemName = null;
								    	$scope.searchItem = '';
								    	$scope.pharmacyobj = null;
								    	$scope.pharmacyProductobj = null;
										
									}
									else
										{
										$scope.pharmacyDetailsObj = {};
										$scope.selectedItemId = null;
								    	$scope.selectedItemName = null;
								    	$scope.searchItem = '';
								    	$scope.pharmacyobj = null;
								    	$scope.pharmacyProductobj = null;
											growl.warning("Item already added in the list",{
												title : "WARNING"
											});
											return false;
										}
									
								}
								
							}
								
								
								
							}
							
							$scope.addGroupDetailsToList = function()
							{
								

								if($scope.patientTypeId == '' || $scope.patientTypeId == null)
									{
									    growl.warning("Please select patient class",
											{
												title : 'Warning!'
											});
									    return false;

									}

								else if($scope.selectedTariffList.length == 0)
									{
									    growl.warning("Please select tariff",
											{
												title : 'Warning!'
											});
									    return false;

									}
								else if($scope.selectedVisitType.length == 0)
								{
								    growl.warning("Please select visit type",
										{
											title : 'Warning!'
										});
								    return false;

								}
								else if($scope.selectedPaymentEnt.length == 0)
								{
								    growl.warning("Please select payment entitlement",
										{
											title : 'Warning!'
										});
								    return false;

								}
								else if($scope.selectedPatientTypeList.length == 0)
								{
								    growl.warning("Please select patient type",
										{
											title : 'Warning!'
										});
								    return false;

								}
								
								else if(($scope.groupDetailsObj.specialityId == '' || $scope.groupDetailsObj.specialityId == null) &&  ($scope.selectedServiceId == '' || $scope.selectedServiceId == null || $scope.selectedServiceId == undefined))
									{
									    growl.warning("Please select specialty/service",
											{
												title : 'Warning!'
											});
									    return false;

									}
								else if($scope.groupDetailsObj.markupStatus == '' || $scope.groupDetailsObj.markupStatus == null || $scope.groupDetailsObj.markupStatus == undefined){
									growl.warning("Please select mark status",
											{
												title : 'Warning!'
											});
									    return false;
								}
								else
									{
									
									var data={
											markupStatus : $scope.groupDetailsObj.markupStatus,
											specialityId : $scope.groupDetailsObj.specialityId,
											specialityName : $scope.groupDetailsObj.specialityName,
											subSpecialityId : $scope.groupDetailsObj.subSpecialityId,
											subSpecialityName : $scope.groupDetailsObj.subSpecialityName,
											serviceId : $scope.selectedServiceId,
									    	serviceName : $scope.selectedServiceName,
											percentage: 0,
											organizationId : $scope.organizationId,
											unitId: $scope.unitId,
											createdBy: $scope.userId,
											updatedBy: $scope.userId,
											patientTypeId:$scope.patientTypeId
									};
									
									
									if($scope.groupDetailsObj.specialityId > 0 && ($scope.groupDetailsObj.subSpecialityId == '' || $scope.groupDetailsObj.subSpecialityId == null) && ($scope.selectedServiceId == '' || $scope.selectedServiceId == null || $scope.selectedServiceId == undefined)){
										
										 isGroupExists=checkGroupExistsOrNot($scope.groupDetailList,data)
										if (!isGroupExists) {
											$scope.groupDetailList.push(data);
											console.log($scope.groupDetailList);
											$scope.groupDetailsObj = {};
											$scope.selectedServiceId = null;
									    	$scope.selectedServiceName = null;
									    	$scope.search = '';
									    	$scope.obj = null;
									    	$scope.obj1 = null;
											
										}
										else
											{
												growl.warning("Group already added in the list",{
													title : "WARNING"
												});
												return;
											}
										
									}
									
									if($scope.groupDetailsObj.specialityId > 0  && $scope.groupDetailsObj.subSpecialityId > 0 &&  ($scope.selectedServiceId == '' || $scope.selectedServiceId == null || $scope.selectedServiceId == undefined)){
										
										 isGroupSubgroupExists=checkGroupSubGroupExistsOrNot($scope.groupDetailList,data)
										if (!isGroupSubgroupExists) {
											$scope.groupDetailList.push(data);
											console.log($scope.groupDetailList);
											$scope.groupDetailsObj = {};
											$scope.selectedServiceId = null;
									    	$scope.selectedServiceName = null;
									    	$scope.search = '';
									    	$scope.obj = null;
									    	$scope.obj1 = null;
											
										}
										else
											{
												growl.warning("Sub group already added in the list",{
													title : "WARNING"
												});
												return;
											}
										
									}
									
									if($scope.selectedServiceId > 0){
										
										isServiceExists=checkSerivceExistsOrNot($scope.groupDetailList,data)
										if (!isServiceExists) {
											$scope.groupDetailList.push(data);
											console.log($scope.groupDetailList);
											$scope.groupDetailsObj = {};
											$scope.selectedServiceId = null;
									    	$scope.selectedServiceName = null;
									    	$scope.search = '';
									    	$scope.obj = null;
									    	$scope.obj1 = null;
											
										}
										else
											{
											$scope.groupDetailsObj = {};
											$scope.selectedServiceId = null;
									    	$scope.selectedServiceName = null;
									    	$scope.search = '';
									    	$scope.obj = null;
									    	$scope.obj1 = null;
												growl.warning("Service already added in the list",{
													title : "WARNING"
												});
												return false;
											}
										
									}
									
								}
								
							}
							
							
							$scope.addServiceDetailsToList = function()
							{
								
								var data={
										markupStatus : $scope.groupDetailsObj.markupStatus	,
										specialityId : $scope.groupDetailsObj.specialityId,
										specialityName : $scope.groupDetailsObj.specialityName,
										subSpecialityId : $scope.groupDetailsObj.subSpecialityId,
										subSpecialityName : $scope.groupDetailsObj.subSpecialityName,
										serviceId: '',
										percentage: '',
										markupStatus: '',
										organizationId : $scope.organizationId,
										unitId: $scope.unitId,
										createdBy: $scope.userId,
										updatedBy: $scope.userId,
										patientTypeId:''
								};
								
								$scope.serviceDetailList.push(data);
								console.log($scope.serviceDetailList);
							}
							
							
							
						$scope.savePatientType = function(patientTypeObj)
						{
							if($scope.patientTypeObj.code=="" ){
								
								growl.warning("Please Enter Code",
										{
											title : 'warning!'
										});
								return false;
							}else if($scope.patientTypeObj.description=="" ){
								
								growl.warning("Please Enter Description",
										{
											title : 'warning!'
										});
								return false;
							}else{
							console.log(patientTypeObj);
							var dataForPatientType = {
								createdBy : $scope.userId,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								updatedBy: $scope.userId,
								code : patientTypeObj.code,
								description: patientTypeObj.description
							};
							
							var URI = BASE_URL + ROOT_URL +"/api/billing/savePatientType";
						GenericService
							.serviceAction(METHOD_POST, URI, dataForPatientType,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.startLoader();
										if (response.data.status == "success"){
											$scope.stopLoader();
											$scope.saveBtnFlag = true;
											$scope.updateBtnFlag = false;
											$scope.patientTypeObj = {};
											$scope.getActivePatientTypeList();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										}
										else{
											$scope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'error!'
													});
										}
									});
							}
						}
						
						
						$scope.getActivePatientTypeList = function()
						{
							var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
						GenericService
							.serviceAction(METHOD_GET, URI,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										if (response.data.status == "success"){
											
											$scope.patientTypeList = response.data.listObject;
											console.log("list",$scope.patientTypeList);
										}
										
									});
							
						}
						
						$scope.getActivePatientTypeList();
						
						$scope.getServicesBySpecialityAndSubSpeciality = function()
						{
							console.log($scope.serviceDetailsObj);
							
							 $scope.data={

										"subSpecialityIdList":[$scope.serviceDetailsObj.subSpecialityId],
										"orgnisationId":$scope.organizationId,
										"unitId":$scope.unitId
										
							 }
							 var URI = BASE_URL + ROOT_URL+ "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
							GenericService
							.serviceAction(METHOD_POST, URI, $scope.data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.startLoader();
										if (response.data.status == "success")
											$scope.stopLoader();
									//	$scope.serviceListBySpeciality = response.data.listObject;
										console.log("spl List",response.data.listObject);
										
										angular
										.forEach(response.data.listObject,function(value,index) {
											var newArr = {};
											newArr.serviceId = value.serviceId;
											newArr.serviceName = value.serviceStandardName;
											newArr.specialityId = value.specialityId,
											newArr.specialityName = value.specialityName,
											newArr.percentage = '',
											newArr.markupStatus = $scope.serviceDetailsObj.markupStatus,
											newArr.subSpecialityId = value.subSpecialityId,
											newArr.subSpecialityName = value.subSpecialityName,
											newArr.organizationId = $scope.organizationId,
											newArr.unitId= $scope.unitId,
											newArr.createdBy= $scope.userId,
											newArr.updatedBy= $scope.userId,
											newArr.patientTypeId= $scope.groupDetailsObj.patientTypeId
											$scope.serviceListBySpeciality.push(newArr);
											});
									
											console.log($scope.serviceListBySpeciality);
										
									});
						}
						
						$scope.savePatientTypeDetails = function()
						{
							
							if($scope.groupDetailList.length > 0 || $scope.assetProductDetailList.length > 0)
							{
								console.log
							 $rootScope.startLoaderPopup();
							 $scope.serviceListBySpeciality = [];
							 var arra=cartesian($scope.selectedTariffList,$scope.selectedVisitType,$scope.selectedPaymentEnt,$scope.selectedPatientTypeList);
							 console.log(arra);
							 
							 for(var i=0;i<arra.length;i++){ //loop for service
								 var tempArr=arra[i];
									
									angular.forEach($scope.groupDetailList,function(value,index) {
											
										var tempObj={
												tariffId:tempArr[0].id,
												visitTypeId:tempArr[1].id,
												paymentEntitlementId:tempArr[2].id,
												patientCategoryId:tempArr[3].id,
										}
										
										    tempObj.markupStatus=value.markupStatus;
											tempObj.specialityId=value.specialityId;
											tempObj.subSpecialityId=value.subSpecialityId;
											tempObj.serviceId=(value.serviceId == undefined || value.serviceId == null) ? null : value.serviceId;
											tempObj.percentage=value.percentage;
											  
											tempObj.organizationId=$scope.organizationId;
											tempObj.unitId=$scope.unitId;
											tempObj.createdBy=$scope.userId;
											tempObj.updatedBy= $scope.userId;
											tempObj.patientTypeId=value.patientTypeId;
											tempObj.assetCategoryId= null;
											tempObj.productCategoryId = null;
											tempObj.itemId=null;
											$scope.serviceListBySpeciality.push(tempObj);
										});
									
									angular.forEach($scope.assetProductDetailList,function(value,index) {
										
										var tempObj1={
												tariffId:tempArr[0].id,
												visitTypeId:tempArr[1].id,
												paymentEntitlementId:tempArr[2].id,
												patientCategoryId:tempArr[3].id,
										}
										
										    tempObj1.markupStatus=value.markupStatus;
											tempObj1.assetCategoryId=value.assetCategoryId;
											tempObj1.productCategoryId=value.productCategoryId;
											tempObj1.itemId=(value.itemId == undefined || value.itemId == null) ? null : value.itemId;
											tempObj1.percentage=value.percentage;
											tempObj1.organizationId=$scope.organizationId;
											tempObj1.unitId=$scope.unitId;
											tempObj1.createdBy=$scope.userId;
											tempObj1.updatedBy= $scope.userId;
											tempObj1.patientTypeId=value.patientTypeId;
											tempObj1.specialityId=null;
											tempObj1.subSpecialityId=null;
											tempObj1.serviceId=null;
											$scope.serviceListBySpeciality.push(tempObj1);
										});
									
						         
									}//For Loop Service
							 
							 /*for(var i=0;i<arra.length;i++){ //loop for Pharmacy
								 var tempArr=arra[i];
								
									
									
									angular.forEach($scope.assetProductDetailList,function(value,index) {
											
										var tempObj={
												tariffId:tempArr[0].id,
												visitTypeId:tempArr[1].id,
												paymentEntitlementId:tempArr[2].id,
												patientCategotyId:tempArr[3].id,
										}
										
										    tempObj.markupStatus=value.markupStatus;
											tempObj.assetCategoryId=value.assetCategoryId;
											tempObj.productCategoryId=value.productCategoryId;
											tempObj.itemId=(value.itemId == undefined || value.itemId == null) ? null : value.itemId;
											tempObj.percentage=value.percentage;
											tempObj.organizationId=$scope.organizationId;
											tempObj.unitId=$scope.unitId;
											tempObj.createdBy=$scope.userId;
											tempObj.updatedBy= $scope.userId;
											tempObj.patientTypeId=value.patientTypeId;
											$scope.serviceListBySpeciality.push(tempObj);
										});
									
						         
									}//For Loop Pharmacy
*/							 
							 console.log('$scope.serviceListBySpeciality',$scope.serviceListBySpeciality);
							// return false;
							 
							var dataForSave = {
									patientTypeGroupWiseDetails:$scope.serviceListBySpeciality,
							};
							
							console.log("datatttttt",dataForSave)
							var URI = BASE_URL + ROOT_URL +"/api/billing/savePatientTypeWiseDetails";
						GenericService
							.serviceAction(METHOD_POST, URI, dataForSave,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										
										if (response.data.status == "success"){
											
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										angular.element('#confirmSavePatientTypeDetails').modal('hide');
										$scope.serviceListBySpeciality = [];
										$scope.groupDetailList = [];
										$scope.tariffMuliSelectList = [];
										$scope.selectedTariffList = [];
										$scope.selectedVisitType = [];
										$scope.selectedPaymentEnt = [];
										$scope.selectedPatientTypeList = [];
										$scope.assetProductDetailList = [];
										$scope.patientTypeId = '';
										$scope.init();
										}
										else{
											growl.success(response.data.message,
													{
														title : 'error!'
													});
										}
										$rootScope.stopLoaderPopup();
									});
							}
							else
								{
									growl.warning("Please add atleast one record on service or pharmacy tab. ",
										{
											title : 'Warning!'
										});
									return false;
								}
						}
						
						$scope.patientTypeWiseDetails={};
						$scope.patientTypeServiceWiseDetailsList=[];
						$scope.patientTypeGroupWiseDetailsList=[];
						
						$scope.getPatientTypeDetailsById = function(id)
						{
							
						
							var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientTypeServiceWiseDetails/"+id+"/"+$scope.organizationId+"/"+$scope.unitId;
						GenericService
							.serviceAction(METHOD_GET, URI,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
									//	$scope.startLoader();
										if (response.data.status == "success"){
										//	$scope.stopLoader();
											
											$scope.patientTypeWiseDetails = response.data.object;
											$scope.patientTypeServiceWiseDetailsList = $scope.patientTypeWiseDetails.patientTypeServiceWiseDetails;
											$scope.patientTypeGroupWiseDetailsList = $scope.patientTypeWiseDetails.patientTypeGroupWiseDetails;
											console.log("srv",$scope.patientTypeWiseDetails);
											/*growl.success(response.data.message,
												{
													title : 'Success!'
												});*/
										}
										else{
											growl.success(response.data.message,
													{
														title : 'error!'
													});
										}
									});
							
						}
						
						
						$scope.removeAddedGroup = function(groupDetailList,index) {
							$scope.groupDetailList.splice(index,1)
						}
						
						$scope.removeAddedAssetProduct = function(assetProductDetailList,index) {
							$scope.assetProductDetailList.splice(index,1)
						}
						
						$scope.removeAddedServices = function(serviceListBySpeciality,index) {
							$scope.serviceListBySpeciality.splice(index,1)
						}
						
						
						$scope.updateStatus = function(Id,status){
							try{
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								var data={
										updatedBy:$scope.userId,
										updatedDate:$scope.dt,
										status:status,
										patientTypeId:Id
								};
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing//updatePatientTypeStatus";
								console.log("update status",data);
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
//										$scope.$broadcast('show-errors-reset');
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							}catch(e){
								console.log("Exception",e);
							}
						}
						
						$scope.showUpdateBtn = function(info){
							try{
								
								$scope.patientTypeObj.code = info.code;
								$scope.patientTypeObj.description = info.description;
								$scope.patientTypeObj.patientTypeId = info.patientTypeId;
 								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
							
							}catch(e){
								console.log("Exception-",e);
							}
						}
						
						$scope.updatePatientType = function(){
							try{
								if($scope.patientTypeObj.code=="" || $scope.patientTypeObj.code==undefined){
									
									growl.warning("Please Enter Code",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.patientTypeObj.description=="" || $scope.patientTypeObj.description==undefined){
									
									growl.warning("Please Enter Description",
											{
												title : 'warning!'
											});
									return false;
								}else{
							
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');			
								var dataForPatientType = {
										createdBy : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy: $scope.userId,
										code : $scope.patientTypeObj.code,
										description: $scope.patientTypeObj.description,
										patientTypeId : $scope.patientTypeObj.patientTypeId
									};
									var URI = BASE_URL + ROOT_URL +"/api/billing/updatePatientType";
								GenericService
									.serviceAction(METHOD_POST, URI, dataForPatientType,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.startLoader();
												if (response.data.status == "success"){
													$scope.stopLoader();
													$scope.getActivePatientTypeList();
													$scope.patientTypeObj.description='';
													$scope.patientTypeObj.patientTypeId='';
													$scope.patientTypeObj.code='';
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												
												$scope.saveBtnFlag = true;
												$scope.updateBtnFlag = false;
												}
												else{
													$scope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'error!'
															});
												}
											});
									
								}
							}catch(e){
								console.log("Exception-",e);
							}
						}
						
						$scope.updateGroupStatus = function(Id,status){
							try{
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								var data={
										updatedBy:$scope.userId,
										updatedDate:$scope.dt,
										status:status,
										patientTypeGroupId:Id
								};
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing//updatePatientTypeGroupStatus";
								console.log("update status",data);
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
//										$scope.$broadcast('show-errors-reset');
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							}catch(e){
								console.log("Exception",e);
							}
						}
						
						$scope.updateServiceStatus = function(Id,status){
							try{
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								var data={
										updatedBy:$scope.userId,
										updatedDate:$scope.dt,
										status:status,
										patientTypeServiceId:Id
								};
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing//updatePatientTypeServiceStatus";
								console.log("update status",data);
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
//										$scope.$broadcast('show-errors-reset');
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							}catch(e){
								console.log("Exception",e);
							}
						}
						
						
						/*First Level*/
				    	$scope.firstTabLevel = 1 ;
						
						$scope.setFirstTabLevel= function(newTab)
						{
							$scope.firstTabLevel = newTab;
							if(newTab == 2)
								{
								
								/*$rootScope.startLoader();
								
								var subGroupIds = [];
								angular.forEach($scope.groupDetailList,function(value,index){
									subGroupIds.push(value.subSpecialityId);
								});
									var data = 
									{
										"orgnisationId":$scope.organizationId,
										"unitId":$scope.unitId,
										"subSpecialityIdList":subGroupIds
									}
							
									var URI = BASE_URL + ROOT_URL + "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.serviceListBySpeciality = [];
											angular.forEach(response.data.listObject,function(value,index){
												angular.forEach($scope.groupDetailList,function(value1,index1){
													if(value.specialityId == value1.specialityId && value.subSpecialityId == value1.subSpecialityId)
														{
														var data={
																markupStatus : value1.markupStatus	,
																specialityId : value.specialityId,
																specialityName : value.specialityName,
																subSpecialityId : value.subSpecialityId,
																subSpecialityName : value.subSpecialityName,
																serviceId: value.serviceId,
																percentage: value1.percentage,
																organizationId : $scope.organizationId,
																unitId: $scope.unitId,
																createdBy: $scope.userId,
																updatedBy: $scope.userId,
																patientTypeId:$scope.patientTypeId,
																serviceName : value.serviceStandardName
														};
														$scope.serviceListBySpeciality.push(data);
														
														}
												});
											});
											
										}
										$rootScope.stopLoader();
									});*/
							
							
								}
						};
						
						$scope.isSetFirstTabLevel = function(tabNum)
						{
							return $scope.firstTabLevel === tabNum;
						};
						  
						  function cartesian() {
							    var r = [], arg = arguments, max = arg.length-1;
							    function helper(arr, i) {
							        for (var j=0, l=arg[i].length; j<l; j++) {
							            var a = arr.slice(0); // clone arr
							            a.push(arg[i][j]);
							            if (i==max)
							                r.push(a);
							            else
							                helper(a, i+1);
							        }
							    }
							    helper([], 0);
							    return r;
							}
			}]);
							