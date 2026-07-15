angular
		.module('myApp')
		.controller(
				'pharmacyTariffMasterController',
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
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService) {
							
							
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
							
							$scope.productCategoryList=[];
							$scope.groupDetailList=[];
							
							$scope.assetCategoryList= [];
							
							$scope.getAssetCategoryList = function () {
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/1/"+$scope.unitId+"/"+$scope.organizationId;;
								GenericService.serviceAction(METHOD_GET, URI, null)
								.then(function(response){
									$rootScope.stopLoader();
									if(response.status == 200)
										
										$scope.assetCategoryList = response.data;
								});
							}
							$scope.saveBtnFlag = true;
							$scope.getAssetCategoryList();
							
							$scope.getpharmacyList = function () {
								/*var data = {
										organizationId:$scope.organizationId,
											unitId:$scope.unitId
								}*/
								var URI = BASE_URL + ROOT_URL + "/api/units/pharmacy/tariff/getAll/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(
										function(response) {
									if (response.data.status == 'success') {
										
										$scope.groupDetailList = response.data.listObject;											
	
									} else{
										growl.error(response.data.message,
											{
												title: 'Error!'
											});
									}
								});
							}
							
							$scope.getpharmacyList();
							
							var isGroupSubgroupExists;
							function checkGroupSubgroupExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.assetCategoryId == parseInt(arr.assetCategoryId) && val.productCategoryId == parseInt(arr.productCategoryId) &&  val.paymentEntitlementId == parseInt(arr.paymentEntitlementId) )
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							$scope.removeProcedure = function(index){
								$scope.procedureWiseList.splice(index,1);
							}
							
							

							$scope.updateStatus = function(id, status) {
								try {

									
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL+ "/api/units/pharmacy/tariff/update/status/"+id+"/"+status;
									// return false;
									GenericService.serviceAction("GET", URI,"").then(
													function(response) {
														if (response.data.status == 'success') {
															$rootScope.stopLoader();
															growl.success("Status Changed Successfully",
																	{
																		title : 'Success!'
																	});
															$scope.getpharmacyList();
														} else if (response.data.status == 'error') {
															$rootScope.stopLoader();
															growl.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
														}
													});
								} catch (e) {
									console.log("Exception", e);
								}
							}
							
							$scope.submitDetails = function()
							{
								$rootScope.startLoader();
								if($scope.markUpDownPerPopup == undefined || $scope.markUpDownPerPopup == null || $scope.markUpDownPerPopup =="" || isNaN($scope.markUpDownPerPopup)){
//									growl.error("Please Select Markupdown Percentage",
//											{
//												title : 'error!'
//											});
									$rootScope.stopLoader();
//									return false;
									$scope.markUpDownPerPopup=0;
								}
								var data = {
								id:$scope.idPopup,
								markUpDown:$scope.markupStatusPopup,
								markUpDownPer:$scope.markUpDownPerPopup
								}
						var URI = BASE_URL + ROOT_URL + "/api/units/pharmacy/tariff/update"
						BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(
								function(response) {
							if (response.data.status == 'success') {
								
								growl.success("Updated Successfully",
										{
											title : 'Success!'
										});
								$scope.getpharmacyList();
								$rootScope.stopLoader();

							} else{
								growl.error(response.data.message,
									{
										title: 'Error!'
									});
								$rootScope.stopLoader();
							}
						});
						
							}
							
							$scope.getProductCatByAssestCat = function(assetId)
							{
								if(assetId != "" && typeof(assetId) != "undefined" && assetId>0){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/global/products/categories/"+assetId+"/"+$scope.organizationId;
									var httpObject = {method : "GET",url : URI,data : null};
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
							
							$scope.getDetails = function(item,idx)
							{
								$scope.listIndex = idx;
								$scope.markupStatusPopup = item.markUpDown;
								$scope.markUpDownPerPopup = item.markUpDownPer;
								$scope.idPopup = item.id;
							}
							
							
						
							
							$scope.addProductCategory = function()
							{
								

								if($scope.assetCategoryId == '' || $scope.assetCategoryId == null)
									{
									    growl.warning("Please select asset category",
											{
												title : 'Warning!'
											});
									    return false;

									}
								
								else if($scope.productCategoryId == '' || $scope.productCategoryId == null)
									{
									    growl.warning("Please select product category",
											{
												title : 'Warning!'
											});
									    return false;

									}
								else if($scope.markupStatus == '' || $scope.markupStatus == null)
									{
										growl.warning("Please select markup status",
												{
													title : 'Warning!'
												});
										    return false;
									}
								else
									{
									
									var assetCatObj = $.grep($scope.assetCategoryList, function (disCat) {
							            return disCat.id == $scope.assetCategoryId;
							        })[0];
									
									var productCatObj = $.grep($scope.productCategoryList, function (disCat) {
							            return disCat.id == $scope.productCategoryId;
							        })[0];
									
									
									
									var data={
											markUpDown : $scope.markupStatus,
											assetCategoryId : $scope.assetCategoryId,
											assetCategoryName : assetCatObj.category,
											productCategoryId : $scope.productCategoryId,
											productCategoryName : productCatObj.category,
											markUpDownPer : 0,
											organizationId : $scope.organizationId,
											unitId: $scope.unitId,
											createdBy: $scope.userId,
											updatedBy: $scope.userId,
											status:'A',
											paymentEntitlementId:$scope.paymentEntitlementId,
											id:''
											
									};
						
									isGroupSubgroupExists=checkGroupSubgroupExistsOrNot($scope.groupDetailList,data)
									if (!isGroupSubgroupExists) {
										$scope.groupDetailList.push(data);
										
										$scope.assetCategoryId = '';
										$scope.markupStatus = '';
										$scope.productCategoryId = '';
										$scope.paymentEntitlementId = '';
										console.log($scope.groupDetailList);
										
									}
									else
										{
											growl.warning("Already added in the list",{
												title : "WARNING"
											});
											return;
										}
								}
								
							}
						
						
						
						
						$scope.savePharmacyTariff = function()
						{
							if($scope.groupDetailList.length > 0)
							{
								var list = [];
								angular.forEach($scope.groupDetailList, function(value, key) {
                                	if(value.id == ""){	list.push(value);	}
                                });
								if(list.length == 0){
									growl.error("No data to save !!!",{title : 'Error!'});
								}else{
									 $rootScope.startLoader();
									var data = {
											pharmacyTariffDtoList:list
									};
									var URI = BASE_URL + ROOT_URL +"/api/units/pharmacy/tariff/save";
									GenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(
										function(response) {
											console.log(response);
											if (response.data.status == "success"){
												growl.success(response.data.message,{title : 'Success!'});
												$scope.getpharmacyList();
											}
											else{
												growl.error(response.data.message,{title : 'error!'});
											}
										$rootScope.stopLoader();
									});
								}
							}
							else{
								growl.warning("Please add product",{title : 'Warning!'});
								return false;
							}
						}
						
						$scope.removeAddedGroup = function(groupDetailList,index) {
							$scope.groupDetailList.splice(index,1)
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
												}
												else{
													$scope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'error!'
															});
												}
											});
									
								
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
						
						
						/*First Level*/
				    	$scope.firstTabLevel = 1 ;
						
						$scope.setFirstTabLevel= function(newTab)
						{
							$scope.firstTabLevel = newTab;
						};
						
						$scope.isSetFirstTabLevel = function(tabNum)
						{
							return $scope.firstTabLevel === tabNum;
						};
						
			}]);
							