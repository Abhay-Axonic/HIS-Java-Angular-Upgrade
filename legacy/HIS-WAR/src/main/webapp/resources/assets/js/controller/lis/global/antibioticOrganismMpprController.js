/**
 * @author  Suraj  Kumbhoje
 */

angular.module('myApp').controller(
		"antibioticOrganismMpprController",
		[ '$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage',
			'growl',
				function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl)
				{
			       
			        $rootScope.loginpage = true; 
					$scope.LISDynamicLabel = "Antibiotic Organism Mapprer";
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.antibio = [];
					
					
					/**Variables for pagination */
					$scope.offset = 0;
					$scope.noOfRecordsPerPage = 10;
					$scope.commonList = [];
					$scope.totalNoOfRecords;
					$scope.searchKeyword;
					$scope.commonListCount;
					
					/*** Check user is Loggedin */
					var cookieObject,cookieObjectAppConfig;
					[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
					$scope.unitId = cookieObject.unitId;
					$scope.orgId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					
					
					$scope.antibioticGroupList = [];
					$scope.getAntibioticByAntibioticGroupIdList =[];
					$scope.selectedAntibioticList = [];
					
					/* code for setting Label */
					$(".selectedPageName").text("Mapping Of Micro Organism ");
					
					$scope.initCommon = function()
					{
						$scope.saveBtnFlag = true; 
						$scope.updateBtnFlag = false;
						$scope.common =
						{
							 "antiboiticOrganismMpprId":"",
							 "antiboiticId":"",
							 "organismId":"",
						     "status":"A",
							 "createdBy":$scope.userId,
							 "createdDate":"",
							 "updatedBy":$scope.userId,
							 "updatedDate":"",
						     "orgId":$scope.orgId,
						     "antibioticName":$scope.antibioticName, 
						     "antibioticClassDesc":$scope.antibioticClassDesc 
						     
						}
						
						$scope.updateAntibioticSelectedList = [];
						$scope.updateFinalAntibiotic = [];
						$scope.finalAntibiotic = [];
						$scope.tempSelectAnti = [];
						$scope.antibioticSelectedList = [];
						$scope.removeAntibioticSelectedList = [];
					}
					$scope.initCommon();
					
					$scope.initAntibioticAdditionMaster = function(){
					$scope.antibioticAdditionMaster =
					{
						 "antiboiticOrganismMpprId":$scope.common.antiboiticOrganismMpprId,
						 "antiboiticId":$scope.common.antiboiticId,
						 "organismId":$scope.common.organismId,
					     "status":$scope.common.status,
						 "createdBy":$scope.common.createdBy,
						 "createdDate":$scope.common.createdDate,
						 "updatedBy":$scope.common.updatedBy,
						 "updatedDate":$scope.common.updatedDate,
					     "orgId":$scope.common.orgId,
					     "antibioticName":$scope.antibioticName, 
					     "antibioticClassDesc":$scope.antibioticClassDesc,
					     "isDeleted": "N"
					 }
					
					}
					
					// ====================================================CODE START FORFOR AGE_GROUP MASTER LIST===========================================================

					
					
					/*
					 *  Get Antibiotic Group Master List
					 */
					$scope.getAntibioticGroupList = function() {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL
							        + LIS_COMMON
									+ GET_ANTIBIOTIC_GROUP_LIST
									+ S
									+ $scope.orgId;
							console.log("URI", URI);
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.antibioticGroupList  = response.data.listObject;
												console.log("antibioticGroupList",JSON.stringify($scope.antibioticGroupList));
											});
						} catch (e) {
							console.log(e.message)
						}

					}
					$scope.getAntibioticGroupList();
					
					
					
					/*
					 *  Get Antibiotic By  Antibiotic Group Id List
					 */
					$scope.getAntibioticByAntibioticGroupIdList = function(antibioticGroupId) {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL
							        + LIS_COMMON
									+ GET_ANTIBIOTIC_LIST_BY_ANTIBIOTIC_GROUP_ID
									+ S +"antibioticGroupId"+ S
									+ antibioticGroupId
									+ S +"orgId"+ S
									+ $scope.orgId;
							console.log("URI", URI);
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.getAntibioticByAntibioticGroupIdList  = response.data.listObject;
												console.log("getAntibioticByAntibioticGroupIdList",JSON.stringify($scope.getAntibioticByAntibioticGroupIdList));
											});
						} catch (e) {
							console.log(e.message)
						}

					}
					
					
					/*
					 *  Get OrganismList Master List
					 */
					$scope.getOrganismListList = function() {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL
							        + LIS_COMMON
									+ GET_ORGANISM_LIST 
									+ S
									+ $scope.orgId;
							console.log("URI", URI);
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.organismList  = response.data.listObject;
											});
						} catch (e) {
							console.log(e.message)
						}

					}
					$scope.getOrganismListList();
					
					$scope.setNoOfRecords = function() {
						$scope.initAntibioticAdditionMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
					};
					$scope.initAntibioticAdditionMasterList = function(orgId, offset, noOfRecordsPerPage)
					{
						$rootScope.startLoader();
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI1 = BASE_URL + ROOT_URL + LIS_GLOBAL + GET_TOTAL_ANTIBIOTIC_ORGANISM_LIST_MAPPER_MASTER + S + orgId + S + offset
								+ S + noOfRecordsPerPage;
						var URI2 = BASE_URL + ROOT_URL + LIS_GLOBAL + GET_COUNT_ANTIBIOTIC_ORGANISM_LIST_MAPPER_MASTER_LIST + S + orgId;
						promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
						{
							$rootScope.stopLoader();
							$scope.commonList = response[0].listObject;
							$scope.commonListCount = response[1].object;
							$scope.setPage(1, false);
							console.log($scope.commonList);
						});
					}

					$scope.getAntibioticAdditionMasterList = function(orgId, offset, noOfRecordsPerPage) {
						$rootScope.startLoader();
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						/*var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + GET_ANTIBIOTIC_ADDTION_ANTIBIOTIC_CLASS_LIST + S + orgId + S + offset
								+ S + noOfRecordsPerPage;*/
						var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + GET_TOTAL_ANTIBIOTIC_ORGANISM_LIST_MAPPER_MASTER + S + orgId + S + offset
						+ S + noOfRecordsPerPage;
						console.log("URI", URI);
						GenericService.serviceAction("GET", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.commonList = response.data.listObject;
						});
					}

					$scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						if (page < 1 || page > $scope.pager.totalPages)
						{
							return;
						}
						$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
						if (flag)
						{
							$scope.getAntibioticAdditionMasterList($scope.orgId, $scope.pager.startIndex, $scope.pager.pageSize);
						}
					}

					$scope.initAntibioticAdditionMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);

					// ====================================================CODE END FOR AGE_GROUP MASTER LIST===========================================================
					
					
					
					
					
					//code for Activating Inactivating Sample
					$scope.updateStatus = function(commonId,status)
					{
						try {
							$rootScope.startLoader();
							$scope.initAntibioticAdditionMaster();
							var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+ACTIVE_INACTIVE_ANTIBIOTIC_ORGANISM_LIST_MAPPER_MASTER+S+$scope.orgId+S+commonId+S+status;
							console.log("ACT_INACT_URI",URI);
							GenericService.serviceAction("GET", URI, $scope.antibioticAdditionMaster).then(function(response) 
							{
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
									$scope.$broadcast('show-errors-reset');
									$scope.initCommon();
									$scope.initAntibioticAdditionMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
							});
						} catch (e) {
							console.log("Exception",e.message);
						}
						
					}
					
					
					$scope.showUpdateBtn = function(commonId)
					{
						try {
							$rootScope.startLoader();
							$scope.saveBtnFlag = false;
							$scope.updateBtnFlag = true;
						//	var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+EDIT_ANTIBIOTIC_ADDTION_MASTER+S+$scope.orgId+S+commonId;
							var URI = BASE_URL + ROOT_URL
					        + LIS_GLOBAL
					        +"/getAntibioticByOrganismId/update/"
							+ commonId;
							console.log("EDIT",URI);
							GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
							{
								console.log("data",response.data);
								
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									$scope.common.organismId = commonId+ "";
									$scope.selectedAntibioticList = [];
									$scope.tempAntibiotics = [];
									$scope.antibioticSelectedList = [];
									$scope.finalAntibiotic = [];
									angular.forEach($scope.antibio,function(value,index){
										$scope.antibio[index] = false;
										var i = $scope.tempSelectAnti.indexOf(value);
										$scope.tempSelectAnti.splice(i,1);
									})
									angular.forEach(response.data.listObject,function(value,index){
									
										angular.forEach($scope.antibioticGroupList,function(val,idx){
											if(val.id == value.antiboiticGroupId){
												$scope.antibio[value.antiboiticGroupId] = true;
											}
										});
										
										var newArr = {id:value.antiboiticId,name:value.antibioticName,antibioticGroupId:value.antiboiticGroupId,antiboiticOrganismMpprId:value.antiboiticOrganismMpprId};
										if(value.status =='Y'){
											newArr[newArr.id] = true;
											var temp = {antibioticId:value.antiboiticId,antibioticName:value.antibioticName,antibioticGroupId:value.antiboiticGroupId,antiboiticOrganismMpprId:value.antiboiticOrganismMpprId};
												$scope.finalAntibiotic.push(temp);
										}else{
											newArr[newArr.id]  = false;
										}
										$scope.antibioticSelectedList.push(newArr);
										//$scope.selectedAntibioticList.push(temp);
									});
									
									console.log("$scope.common",$scope.common);
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									alert("Error In Fetching Data");
								}
							});
						} catch (e) {
							console.log("Exception",e.message);
						}
						
					}
					
					
					$scope.save = function() 
					{
						$scope.$broadcast('show-errors-check-validity');
						if ($scope.commonform.$valid)
						{
							try {
								$rootScope.startLoader();
								$scope.initAntibioticAdditionMaster();
								$scope.listAntibioticAdditionMaster =
								{
									 "organismId":$scope.common.organismId,
								     "orgId":$scope.common.orgId,
								     "status":"A",
									 "createdBy":$scope.common.createdBy,
									 "createdDate":$scope.common.createdDate,
									 "updatedBy":$scope.common.updatedBy,
									 "updatedDate":$scope.common.updatedDate,
								     "selectAntiboiticOrganismMppList":[]
								     
								 }
								angular.forEach($scope.finalAntibiotic,function(value,index){
									var newArr = {};
									newArr.antiboiticId = value.antibioticId;
									newArr.antiboiticGroupId = value.antibioticGroupId;
										$scope.listAntibioticAdditionMaster.selectAntiboiticOrganismMppList.push(newArr);
								})
									$scope.listAntibioticAdditionMaster.isUpdate = false;
								console.log("AntibioticAdditionMaster",JSON.stringify($scope.listAntibioticAdditionMaster));
								
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+ADD_ANTIBIOTIC_ORGANISM_LIST_MAPPER_MASTER;
								GenericService.serviceAction("POST", URI, $scope.listAntibioticAdditionMaster).then(function(response) 
								{	
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.antibio = [];
										$scope.antibioticGroupList = [];
										$scope.finalAntibiotic = [];
										$scope.initCommon();
										$scope.getAntibioticGroupList();
										$scope.initAntibioticAdditionMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									
								});
							} catch (e) {
								console.log("Exception",e.message);
							}
							
						}
					}
					$scope.update = function() 
					{
						$scope.$broadcast('show-errors-check-validity');
						if ($scope.commonform.$valid)
						{
							try {
								$rootScope.startLoader();
								$scope.initAntibioticAdditionMaster();
								$scope.listAntibioticAdditionMaster =
								{
									"organismId":$scope.common.organismId,
								     "orgId":$scope.common.orgId,
								     "status":"A",
									 "createdBy":$scope.common.createdBy,
									 "createdDate":$scope.common.createdDate,
									 "updatedBy":$scope.common.updatedBy,
									 "updatedDate":$scope.common.updatedDate,
								     "selectAntiboiticOrganismMppList":[]
								     
								 }
								
								angular.forEach($scope.finalAntibiotic,function(value,index){
									var newArr = {};
									newArr.antiboiticId = value.antibioticId;
									newArr.antiboiticGroupId = value.antibioticGroupId;
									newArr.antiboiticOrganismMpprId = value.antiboiticOrganismMpprId;
										$scope.listAntibioticAdditionMaster.selectAntiboiticOrganismMppList.push(newArr);
								})
								
								if($scope.removeAntibioticSelectedList.length>0){
									var tempList = [];
									if($scope.removeSelectedAntibioticList.length>0){
										angular.forEach($scope.removeAntibioticSelectedList,function(value,index){
											var count = 0;
											angular.forEach($scope.removeSelectedAntibioticList,function(value1,index1){
												if(value.antibioticGroupId == value1.antibioticGroupId && value.id == value1.id){
													var newArr = {};
													newArr.antiboiticId = value.id;
													newArr.antiboiticGroupId = value.antibioticGroupId;
													newArr.antiboiticOrganismMpprId = value.antiboiticOrganismMpprId;
													newArr.status = value.status;
													tempList.push(newArr);
													count = 1;
												}
											});	
											if(count == 0){
												var newArr = {};
												newArr.antiboiticId = value.id;
												newArr.antiboiticGroupId = value.antibioticGroupId;
												newArr.antiboiticOrganismMpprId = value.antiboiticOrganismMpprId;
												newArr.status = value.status;
												tempList.push(newArr);
											}
										});	
										
										angular.forEach($scope.removeSelectedAntibioticList,function(value,index){
											var count = 0;
											angular.forEach(tempList,function(value1,index1){
												if(value.antibioticGroupId == value1.antibioticGroupId && value.id == value1.antibioticId){
													count = 1;
												}
											});	
											if(count == 0){
												var newArr = {};
												newArr.antiboiticId = value.id;
												newArr.antiboiticGroupId = value.antibioticGroupId;
												newArr.antiboiticOrganismMpprId = value.antiboiticOrganismMpprId;
												newArr.status = value.status;
												tempList.push(newArr);
											}
										});	
										
										$scope.listAntibioticAdditionMaster.selectAntiboiticOrganismMppList = 	$scope.listAntibioticAdditionMaster.selectAntiboiticOrganismMppList.concat(tempList);
									}else{
										var list1 = [];
										if($scope.removeAntibioticSelectedList.length>0){
											angular.forEach($scope.removeAntibioticSelectedList,function(value,index){
												var newArr = {};
												newArr.antiboiticId = value.id;
												newArr.antiboiticGroupId = value.antibioticGroupId;
												newArr.antiboiticOrganismMpprId = value.antiboiticOrganismMpprId;
												newArr.status = value.status;
												list1.push(newArr);
											});
										}
										
										$scope.listAntibioticAdditionMaster.selectAntiboiticOrganismMppList = 	$scope.listAntibioticAdditionMaster.selectAntiboiticOrganismMppList.concat(list1);
									}
								}else if($scope.removeSelectedAntibioticList.length>0){
									
									var list2 = [];
									
										angular.forEach($scope.removeSelectedAntibioticList,function(value,index){
											var newArr = {};
											newArr.antiboiticId = value.id;
											newArr.antiboiticGroupId = value.antibioticGroupId;
											newArr.antiboiticOrganismMpprId = value.antiboiticOrganismMpprId;
											newArr.status = value.status;
											list2.push(newArr);
										});
									
									$scope.listAntibioticAdditionMaster.selectAntiboiticOrganismMppList = 	$scope.listAntibioticAdditionMaster.selectAntiboiticOrganismMppList.concat(list2);
								}
								console.log("AntibioticAdditionMaster",JSON.stringify($scope.listAntibioticAdditionMaster));
								$scope.listAntibioticAdditionMaster.isUpdate = true;
								var URI = BASE_URL + ROOT_URL +"/LIS/global/addAntibioticOrganismMpprMaster" ;
										///updateAntibioticOrganismMpprMaster"//UPDATE_ANTIBIOTIC_ADDTION_MASTER;
							    GenericService.serviceAction("POST", URI, $scope.listAntibioticAdditionMaster).then(function(response) 
								{
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();				
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.antibio = [];
										$scope.antibioticGroupList = [];
										$scope.finalAntibiotic = [];
										$scope.removedData = [];
										$scope.getAntibioticGroupList();
										$scope.initCommon();
										$scope.initAntibioticAdditionMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							} catch (e) 
							{
								console.log("Exception",e.message);
							}
							
						}
					}
					
					
					
					$scope.updateAntibioticSelectedList = [];
					$scope.updateFinalAntibiotic = [];
					$scope.finalAntibiotic = [];
					$scope.tempSelectAnti = [];
					$scope.antibioticSelectedList = [];
					$scope.removeAntibioticSelectedList = [];
					$scope.addTempAntibiotic = function(antibioticId,desc,checkStatus){
						$scope.tempSelectAnti = [];
						$scope.updateAntibioticSelectedList =  angular.copy($scope.antibioticSelectedList);
						$scope.updateFinalAntibiotic =  angular.copy($scope.finalAntibiotic);
					
							if(checkStatus){
								var temp = {antibioticId:antibioticId,desc:desc};
								$scope.tempSelectAnti.push(temp);
							}
						
						if(!checkStatus){
							
							angular.forEach($scope.updateAntibioticSelectedList ,function(value,index){
								if(value.antibioticGroupId == antibioticId && value.antiboiticOrganismMpprId != undefined && value.antiboiticOrganismMpprId != null){
									value.status='I';
									$scope.removeAntibioticSelectedList.push(value);
								}
							});
							
							var tempAntibioticSelectedList = [];
							angular.forEach($scope.antibioticSelectedList,function(value,index){
								
								if(value.antibioticGroupId != antibioticId){
									tempAntibioticSelectedList.push(value);
								}
	
							});
					        	var tempFinalAntibiotic = [];
				                angular.forEach(tempAntibioticSelectedList,function(value,index){
									
									 angular.forEach($scope.finalAntibiotic,function(value1,index1){
									
									if(value.id == value1.antibioticId  && value.antibioticGroupId == value1.antibioticGroupId){
										tempFinalAntibiotic.push(value1);
									}
									
								    });
								});
	
								$scope.antibioticSelectedList = tempAntibioticSelectedList;	
	                            $scope.finalAntibiotic = tempFinalAntibiotic;
						}
						$scope.selectedAntibioticList = angular.copy($scope.tempSelectAnti);
						$scope.listAntibioticMaster =
						{
						     "orgId":$scope.common.orgId,
						     "selectAntibioticList":[]
						 }
						
						angular.forEach($scope.selectedAntibioticList,function(value,index){
							$scope.listAntibioticMaster.selectAntibioticList.push(value.antibioticId);
				     	})
				     	
				     	if($scope.selectedAntibioticList.length == 0){
				     		$scope.listAntibioticMaster.selectAntibioticList.push(0);
				     	}
					
						try {
							
							if(checkStatus){
								
								
									$rootScope.startLoader();
									
									console.log("listAntibioticMaster",JSON.stringify($scope.listAntibioticMaster));
									var URI = BASE_URL + ROOT_URL + LIS_COMMON + GET_ANTIBIOTIC_LIST_BY_ANTIBIOTIC_GROUP_ID;
									GenericService.serviceAction("POST", URI, $scope.listAntibioticMaster).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											/*growl.success(response.data.message,
													{
														title : 'Success!'
													});*/
											$scope.$broadcast('show-errors-reset');
													if(response.data.listObject.length>0){
														angular.forEach(response.data.listObject,function(val,idx){
															var newArr = {};
															newArr.id = val.antiboiticId;
															newArr.name = val.antibioticName;
															newArr.antibioticGroupId = val.antiboiticGroupId;
															newArr.antiboiticOrganismMpprId = null;
															$scope.antibioticSelectedList.push(newArr);
														});
													}
										//	$scope.antibioticSelectedList  = response.data.listObject;
											console.log("antibioticSelectedList",JSON.stringify($scope.antibioticSelectedList ));
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
							} catch (e) {
								console.log("Exception",e.message);
							}
					}
					
					$scope.selectAntibiotic = function()
					{
						$scope.selectedAntibioticList = angular.copy($scope.tempSelectAnti);
						$scope.listAntibioticMaster =
						{
						     "orgId":$scope.common.orgId,
						     "selectAntibioticList":[]
						     
						 }
						angular.forEach($scope.selectedAntibioticList,function(value,index){
							$scope.listAntibioticMaster.selectAntibioticList.push(value.antibioticId);
				     	})
					
						try {
								$rootScope.startLoader();
								
								console.log("listAntibioticMaster",JSON.stringify($scope.listAntibioticMaster));
								var URI = BASE_URL + ROOT_URL + LIS_COMMON + GET_ANTIBIOTIC_LIST_BY_ANTIBIOTIC_GROUP_ID;
								GenericService.serviceAction("POST", URI, $scope.listAntibioticMaster).then(function(response) 
								{	
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										if(response.data.listObject.length>0){
											$scope.antibioticSelectedList = [];
											angular.forEach(response.data.listObject,function(val,idx){
												var newArr = {};
												newArr.id = val.antiboiticId;
												newArr.name = val.antibioticName;
												newArr.antibioticGroupId = val.antibioticGroupId;
												$scope.antibioticSelectedList.push(newArr);
											});
										}
									//	$scope.antibioticSelectedList  = response.data.listObject;
										console.log("antibioticSelectedList",JSON.stringify($scope.antibioticSelectedList ));
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									
								});
							} catch (e) {
								console.log("Exception",e.message);
							}
						
					}
					
				
					
					/*
					 *  Get Antibiotics List By Organism Id
					 */
					$scope.getAntibioticsListByorganismId = function(organismId) {
						try {
							$scope.antibioticsListByorganismId  = [];
							var data = "";
							var URI = BASE_URL + ROOT_URL
							        + LIS_GLOBAL
									+ GET_ANTIBIOTICS_LIST_BY_ORGANISM_ID
									+ SLASH
									+ organismId;
							console.log("URI", URI);
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.antibioticsListByorganismId  = response.data.listObject;
												console.log("antibioticsListByorganismId",JSON.stringify($scope.antibioticsListByorganismId));
											});
						} catch (e) {
							console.log(e.message)
						}
						

					}
					$scope.removeSelectedAntibioticList = [];
					$scope.addFinalAnti = function(selectedAntibiotic,flag){
						
						   var temp = {antibioticId:selectedAntibiotic.id,antibioticName:selectedAntibiotic.name,antibioticGroupId:selectedAntibiotic.antibioticGroupId,antiboiticOrganismMpprId :selectedAntibiotic.antiboiticOrganismMpprId};
						   var index = $scope.finalAntibiotic.findIndex(item => {return item.antibioticId == selectedAntibiotic.id && item.antibioticGroupId == selectedAntibiotic.antibioticGroupId});
							
						if(index == -1){
							$scope.finalAntibiotic.push(temp);
						}else{
							if(selectedAntibiotic.antiboiticOrganismMpprId != null){
								var newArr = {};
								newArr.id = selectedAntibiotic.id;
								newArr.name = selectedAntibiotic.name;
								newArr.antibioticGroupId = selectedAntibiotic.antibioticGroupId;
								newArr.antiboiticOrganismMpprId = selectedAntibiotic.antiboiticOrganismMpprId;
								newArr.status = 'I';
								$scope.removeSelectedAntibioticList.push(newArr);
							}
							$scope.finalAntibiotic.splice(index,1);
						}
						
					}
					
					
					/**=============================>Errors Messages  <=======================================**/
					
					$scope.plzSelectAntibioticClass= 'Please Select Antibiotic Class.';
					
				} ]);