/**
 * @author  Suraj  Kumbhoje
 */

angular.module('myApp').controller(
		"headerController",
		[ '$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage',
			'growl','GenericServiceFileUpload','reportService',
				function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,GenericServiceFileUpload,reportService)
				{
			       
			        $rootScope.loginpage = true;
					$scope.LISDynamicLabel = "Header ";
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					
					
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
					
					/* code for setting Label */
					$(".selectedPageName").text("Header Master");
					$state.current.title="Header Master";
					$scope.initCommon = function()
					{
						$scope.saveBtnFlag = true; 
						$scope.updateBtnFlag = false;
						$scope.common =
						{
							 "id":"",
							 "code":"",
							 "desc":"",
						     "status":"A",
							 "createdBy":$scope.userId,
							 "createdDate":"",
							 "updatedBy":$scope.userId,
							 "updatedDate":"",
						     "orgId":$scope.orgId,
						     "unitId":$scope.unitId
						}
					}
					$scope.initCommon();
					
					$scope.isParameterCalculation = false;
					$scope.initHeaderMaster = function(){
					$scope.headerMaster =
					{
						 "headerId":$scope.common.id,
						 "code":$scope.common.code,
						 "desc":$scope.common.desc,
					     "status":$scope.common.status,
						 "createdBy":$scope.common.createdBy,
						 "createdDate":$scope.common.createdDate,
						 "updatedBy":$scope.common.updatedBy,
						 "updatedDate":$scope.common.updatedDate,
					     "orgId":$scope.common.orgId,
					     "unitId":$scope.common.unitId,
					     "totalCount":$scope.totalCount,
					     "isParameterCalculation":$scope.isParameterCalculation,
					     "headerParameterMapperDtoList":$scope.parameterDetailList
					 }
					}
					
					// ====================================================CODE START FORFOR HEADER MASTER LIST===========================================================

					$scope.setNoOfRecords = function() {
						$scope.initHeaderMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
					};
					$scope.initHeaderMasterList = function(orgId, offset, noOfRecordsPerPage)
					{
						var data = "";
						$rootScope.startLoader();
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI1 = BASE_URL + ROOT_URL + LIS_GLOBAL + LIST_HEADER_MASTER + S + orgId+S+$scope.unitId+S+offset
								+ S + noOfRecordsPerPage;
						var URI2 = BASE_URL + ROOT_URL + LIS_GLOBAL + COUNT_HEADER_MASTER + S + orgId+S+$scope.unitId;
						promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
						{
							$rootScope.stopLoader();
							$scope.commonList = response[0].listObject;
							$scope.commonListCount = response[1].object;
							$scope.setPage(1, false);
							console.log($scope.commonList);
						});
					}

					$scope.getHeaderMasterList = function(orgId, offset, noOfRecordsPerPage) {
						console.log("offset", offset);
						var data = "";
						$rootScope.startLoader();
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + LIST_HEADER_MASTER + S + orgId +S+$scope.unitId+ S + offset
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
							$scope.getHeaderMasterList($scope.orgId, $scope.pager.startIndex, $scope.pager.pageSize);
						}
					}

					$scope.initHeaderMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);

					// ====================================================CODE END FOR HEADER MASTER LIST===========================================================
					
					
					
					
					
					//code for Activating Inactivating Sample
					$scope.updateStatus = function(commonId,status)
					{
						try {
							$rootScope.startLoader();
							$scope.initHeaderMaster();
							var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+ACTIVE_INACTIVE_HEADER_MASTER+S+$scope.orgId+S+commonId+S+status;
							console.log("ACT_INACT_URI",URI);
							GenericService.serviceAction("GET", URI, $scope.headerMaster).then(function(response) 
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
							var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+EDIT_HEADER_MASTER+S+$scope.orgId+S+$scope.unitId+S+commonId;
							console.log("EDIT",URI);
							GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
							{
								console.log("data",response.data);
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									if(response.data.object.status == "I"){
										growl.warning("This entry is deactivated please active first then update.",
												{
													title : 'warning!'
												});
										$scope.saveBtnFlag = true;
										$scope.updateBtnFlag = false;
										return false;
									}else{
									$scope.common = response.data.object;
									$scope.isParameterCalculation = response.data.object.isParameterCalculation;
									$scope.totalCount = response.data.object.totalCount;
									console.log("$scope.common",$scope.common);
									}
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									alert("Error In Fetching Data");
								}
							});
							$scope.getParameterDetailsListByHeaderList(commonId,0);
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
								$scope.initHeaderMaster();
								console.log("HeaderMaster",JSON.stringify($scope.headerMaster));
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+ADD_HEADER_MASTER;
								GenericService.serviceAction("POST", URI, $scope.headerMaster).then(function(response) 
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
										$scope.initHeaderMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
										$scope.isParameterCalculation = false;
										$scope.totalCount  = 0;
										$scope.parameterDetailList = [];
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
								$scope.initHeaderMaster();
								if($scope.headerMaster.headerParameterMapperDtoList!=null && $scope.headerMaster.headerParameterMapperDtoList.length>0){
									angular.forEach($scope.headerMaster.headerParameterMapperDtoList,function(value,index){
										value.createdBy = $scope.userId;
										value.updatedBy = $scope.userId;
										value.organizationId = $scope.orgId;
										value.unitId = $scope.unitId;
									});
								}
								
								console.log("HeaderMaster",JSON.stringify($scope.headerMaster));
								var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+UPDATE_HEADER_MASTER;
								GenericService.serviceAction("PUT", URI, $scope.headerMaster).then(function(response) 
								{
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										$scope.saveBtnFlag = true;
										$scope.updateBtnFlag = false;
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.$broadcast('show-errors-reset');
										$scope.initCommon();
										$scope.initHeaderMasterList($scope.orgId, $scope.offset, $scope.noOfRecordsPerPage);
										$scope.isParameterCalculation = false;
										$scope.totalCount  = 0;
										$scope.parameterDetailList = [];
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
					
					

					$scope.parameterList = [];
					$scope.getParameterList = function(orgId, orgUnitId) {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL + "/LIS/common/getMultiTestParameterList"
									+ S + orgId + S + orgUnitId;
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.parameterList = response.data.listObject;
											});
						} catch (e) {
							console.log(e.message)
						}
					}
					$scope.getParameterList($scope.orgId,$scope.unitId);
					
					$scope.parameterDetailList = [];
					$scope.addParameterDetails = function(){
						if($scope.parameterObj.id == undefined || $scope.parameterObj.id == null || $scope.parameterObj.id == ""){
							growl.error("Please Select Parameter.",
									{
										title : 'Error!'
									});
							  return false;
						}
						
						if(isNaN($scope.paraSequnce)||Number($scope.paraSequnce)==0)
						{
						growl.error("Please Enter Valid Parameter Sequence",
								{
									title : 'Error!'
								});
						  return false;
						}
						
						var isExists;
						function checkParameterExistsOrNot(arr, val) {
							return arr
									.some(function(arr) {
										return (val.parameterId === arr.parameterId && val.status=='A');
									});
						}
						
						var paraSequenceIsExists;
						function checkParaSequenceExistsOrNot(arr, val) {
							return arr
									.some(function(arr) {
										return (val.paraSequence ==arr.paraSequence && val.status=='A');
									});
						}
						
						var testParamMpprMaster = {};
						testParamMpprMaster.headerParameterMapperId = null;
						testParamMpprMaster.parameterId = $scope.parameterObj.id;
						testParamMpprMaster.parameterName = $scope.parameterObj.name;
						testParamMpprMaster.paraSequence = $scope.paraSequnce;
						testParamMpprMaster.status = 'A';
						testParamMpprMaster.organizationId = $scope.orgId;
						testParamMpprMaster.unitId = $scope.unitId;
						testParamMpprMaster.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						testParamMpprMaster.createdBy = $scope.userId;
						testParamMpprMaster.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
						testParamMpprMaster.updatedBy = $scope.userId;
						testParamMpprMaster.headerId = $scope.common.id!=undefined ? $scope.common.id:null;
						testParamMpprMaster.createdBy = $scope.userId;

						isExists = checkParameterExistsOrNot($scope.parameterDetailList,testParamMpprMaster);
						if(isExists){
							growl.error("Parameter is already Added To.", {
								title : 'Error!'
							});
							return false;
						}
						
						paraSequenceIsExists = checkParaSequenceExistsOrNot($scope.parameterDetailList,testParamMpprMaster);
						if(paraSequenceIsExists){
							growl.error("Parameter sequence is already Added To.", {
								title : 'Error!'
							});
							return false;
						}
						
						$scope.parameterDetailList.push(testParamMpprMaster);
						$scope.parameterObj = {};
						$scope.paraSequnce = "";
						
					}
					
					$scope.removeParameterMapping = function(deleteIndex) {
						if($scope.parameterDetailList[deleteIndex].headerParameterMapperId!=null){
							$scope.parameterDetailList[deleteIndex].status = 'I';
						}else{
							$scope.parameterDetailList.splice(deleteIndex, 1);
						}
					
					}
					
					$scope.saveHeaderParameters = function(){
						if($scope.parameterDetailList.length == 0){
							growl.error("Please add Parameter List", {
								title : 'Error!'
							});
							return false;
						}
					}
					$scope.prevCount = 0;
					$scope.checkIsParam = function(checkParam){
						if(checkParam){
							if($scope.parameterDetailList!=null && $scope.parameterDetailList.length>0){
								angular.forEach($scope.parameterDetailList,function(value,index){
									value.status = 'A';
								});
							}
							$scope.totalCount = $scope.prevCount;
						}else{
							if($scope.parameterDetailList!=null && $scope.parameterDetailList.length>0){
								angular.forEach($scope.parameterDetailList,function(value,index){
									value.status = 'I';
								}); 
								$scope.prevCount = $scope.totalCount;
								$scope.totalCount = 0;
							}
						}
						$scope.isParameterCalculation = checkParam;
					}
					
					$scope.parameterDetailList = [];
					$scope.getParameterDetailsListByHeaderList = function(headerId,flag) {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL + "/LIS/global/getHeaderParameterList"
									+ S + $scope.orgId + S + $scope.unitId + S + headerId;
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												if(flag==0){
													$scope.parameterDetailList = response.data.listObject;
												}else{
													$scope.parameterDetailViewList = response.data.listObject
												}
											});
						} catch (e) {
							console.log(e.message)
						}
					}
				} ]);