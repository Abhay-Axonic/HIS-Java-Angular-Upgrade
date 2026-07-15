angular
		.module('myApp')
		.controller(
				'billOfMaterialMasterController',
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
						'$timeout',
						'PaginationService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$timeout,PaginationService) {
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							if (cookieObjectAppConfig == undefined) {
								$state.go('login');
								return;
							}
							
							$scope.tariffId = cookieObjectAppConfig.defaultSelfTariffId;
							$scope.gradeWiseSeviceApplicable=cookieObjectAppConfig.gradeWiseSeviceApplicable;

							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.listBedCategory = [];
							 $scope.list = [];
							 $scope.selectedBillingBedCatList = [];
							 $scope.selectedBillOfMaterialList = [];
							 $scope.billingBedCatDetailsList = [];
							 $scope.isBillOfMaterialDetailsList = [];
							 $scope.procedure = {};
						

							 $scope.pagination = {
					                    currentPage: 1,
					                    totalPages: 0,
					                    pageSize: '10',
					                    search: '',
					                    searchData: PaginationService.searchData,
					                    setTotalPages: PaginationService.setTotalPages,
					                    getPagesList: PaginationService.getPagesList,
					                    setPage: PaginationService.setPage,
					                    setFirstPage: PaginationService.setFirstPage,
					                    setPreviousPage: PaginationService.setPreviousPage,
					                    setNextPage: PaginationService.setNextPage,
					                    setLastPage: PaginationService.setLastPage,
					                    sortBy: PaginationService.sortBy
					                  }
							 
								$scope.commonObj = {
										serviceId:"",
										surgeryGradeId:"",
										procedureName:"",
										gradeSearch:false,
										procSearch:true,
								};
								
								$scope.checkedFlag=function(flag, index){
									if(index == 2){
										$scope.procedure.surgeryGradeId="";
										$scope.commonObj.gradeSearch=false;
										$scope.commonObj.procSearch=true;

									}
									if(index == 1){
										$scope.commonObj.gradeSearch=true;
										$scope.commonObj.procSearch=false;

									}
								}
							
							 $scope.getGradeList = function() {
									var data = {};
									
									var URI = BASE_URL + ROOT_URL + "/api/global/getActiveSurgeryGradeList";
									console.log("URI", URI);
									$scope.gradeLis=[];
									GenericService.serviceAction("GET", URI).then(function(response) {
										$scope.gradeList = response.data.listObject;
										console.log("gradeList",$scope.gradeList);
									});
								}
							 $scope.getGradeList();
							 
								$scope.getBedCategory = function(){
									$scope.listBedCategory = [];
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL+ "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
									var param = {}
									BillingGenericService.serviceAction(METHOD_GET, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$rootScope.stopLoader();
										if(response.data.status == 'success'){
											if(response.data.listObject.length >0 && $scope.selectedBillingBedCatList.length>0){
												angular.forEach($scope.selectedBillingBedCatList,function(value,index){
													angular.forEach(response.data.listObject,function(value1,index1){
														if(value.id==value1.billingBedCategoryId){
															$scope.listBedCategory.push(value1);
														}
													});
												});
											}
											console.log("listBedCategory",$scope.listBedCategory);
										}
									});
								}
								$scope.getBedCategory();
							 
							$scope.getBillOdMaterialListByProcId = function(){
								$scope.prodeduresList = [];
								var isGrade = false;
								if($scope.commonObj.gradeSearch == true){
									isGrade = true;
								}
								if($scope.commonObj.procSearch == true){
									isGrade = false;
								}
								var searchObj = {
										organizationId: $scope.organizationId,
										unitId: $scope.unitId,
										isGrade : isGrade
									};
							/*	
								if($scope.procedure.billingBedCategoryId != null && $scope.procedure.billingBedCategoryId != "0"){
									searchObj.billingBedCatId = $scope.procedure.billingBedCategoryId;

								}*/
								if($scope.procedure.serviceId != null && $scope.procedure.serviceId != "0"){
									searchObj.procedureId = $scope.procedure.serviceId;

								}
								if($scope.procedure.surgeryGradeId != null && $scope.procedure.surgeryGradeId != "0" && $scope.procedure.surgeryGradeId != ""){
									searchObj.procedureId = "";
									searchObj.surgeryGradeId = $scope.procedure.surgeryGradeId;
									searchObj.isGrade =true;

								}
								if($scope.procedure.serviceId != undefined){
									searchObj.procedureId = $scope.procedure.serviceId;
									searchObj.isGrade =false;
								}
									var URI = BASE_URL + ROOT_URL + "/api/procedure/BillOfMaterial/getBillOfMaterialListByProcedureList";
									 BillingGenericService.serviceAction(METHOD_POST, URI, searchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
										if (response.data.status == "success"){
											if(response.data.listObject.length > 0){
													$scope.prodeduresList = response.data.listObject;
												    console.log("Listtgtv",$scope.prodeduresList);
													$scope.pagination.data=[];
													 if($scope.prodeduresList.length > 0){            
								                         PaginationService.searchData($scope.prodeduresList, $scope.pagination);
								                       }
											}
										}
									});
							}
							$scope.getBillOdMaterialListByProcId();
							
							$scope.serviceDetails = function(procedure){
								 $scope.list = [];
									$rootScope.startLoader();

									$scope.listBillOfMaterialServices = [];
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialServices"
									var param = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
									}
									BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$rootScope.stopLoader();
										if(response.data.status == 'success'){
											if(response.data.listObject.length > 0){
												    $scope.listBillOfMaterialServices = response.data.listObject;
													var URI1 = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialListByProcedureIdAll"
													/*var param = {
															unitId : $scope.unitId,
															organizationId : $scope.organizationId,
															isGrade : false
													};*/
													var param = {
															organizationId: $scope.organizationId,
															unitId: $scope.unitId,
															tariffId: procedure.tariffId,
															paymentEntitlementId: procedure.paymentEntitlementId,
															visitTypeId: procedure.visitTypeId,
															patientCategoryId: procedure.patientCategoryId,
															isGrade : false
													};
													if(procedure.procedureId != null){
														param.procedureId = procedure.procedureId;
													}
													if(procedure.surgeryGradeId != null){
														param.surgeryGradeId = procedure.surgeryGradeId;
														param.isGrade = true

													}
													BillingGenericService.serviceAction(METHOD_POST, URI1, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
														$rootScope.stopLoader();
														if(response.data.status == 'success'){
															if(response.data.listObject.length != 0 )
																 $scope.list = response.data.listObject;
															console.log("savedBillOfMaterialList", $scope.list);
															
															$scope.serviceAmtList = [];
															
															
															var flag=true;
															angular.forEach($scope.listBillOfMaterialServices,function(value,index) {//doner
																
																	angular.forEach($scope.listBedCategory,function(value1,index1) {//reciept
																		
																			angular.forEach($scope.list,function(value2,index2) {//database saved list
																			
																				if(value.serviceId == value2.serviceId && value1.billingBedCategoryId == value2.billingBedCatId){
																					var newArr = {};
																					newArr.billOfMaterialId = value2.billOfMaterialId;
																					newArr.billingBedCatId = value2.billingBedCatId;
																					newArr.procedureId = value2.procedureId;
																					newArr.serviceId = value2.serviceId;
																				//	newArr.visitTypeId = value2.visitTypeId;
																					newArr.amount = value2.amount;
																					$scope.serviceAmtList.push(newArr);
																					
																					flag=false;
																				}
																			});
																	if(flag){
																		var newArr = {};
																		newArr.billOfMaterialId = null;
																		newArr.billingBedCatId = value1.billingBedCategoryId;
																		newArr.procedureId = 0;
																		newArr.surgeryGradeId = procedure.surgeryGradeId;
																		newArr.serviceId = value.serviceId;
																		newArr.amount = "";
																		$scope.serviceAmtList.push(newArr);
																	}
																	else{
																		flag=true;
																	}
																	
															});
															});
															console.log("$scope.serviceAmtList",$scope.serviceAmtList);
															}
													});
											}
												
											console.log("listBillOfMaterialServices",$scope.listBillOfMaterialServices);
											
											}
									});
								
								}
				
							
							$scope.getBillingBedCategory = function(){
								$scope.listBedCategory = [];
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/adt/getActiveBillingBedCategoryList/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
								var param = {
									
								}
								BillingGenericService.serviceAction(METHOD_GET, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$rootScope.stopLoader();
									if(response.data.status == 'success'){
										if(response.data.listObject.length != 0 )
											$scope.listBedCategory = response.data.listObject;
										console.log("listBedCategory",$scope.listBedCategory);
									}
								});
							}
							$scope.getBillingBedCategory();
							$scope.serviceAmtList = [];
							$scope.listBillOfMaterialServices = [];
							$scope.getBillOfMaterialServices = function(){
								$scope.listBillOfMaterialServices = [];
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/api/procedure/BillOfMaterial/getBillOfMaterialServices"
								var param = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
								}
								BillingGenericService.serviceAction(METHOD_POST, URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$rootScope.stopLoader();
									if(response.data.status == 'success'){
										if(response.data.listObject.length > 0 && $scope.selectedBillOfMaterialList.length>0){
												angular.forEach($scope.selectedBillOfMaterialList,function(value,index){
													angular.forEach(response.data.listObject,function(value1,index1){
														if(value.id==value1.serviceId){
															$scope.listBillOfMaterialServices.push(value1);
														}
													});
												});
										}
										 $scope.getBedCategory();
											
										console.log("listBillOfMaterialServices",$scope.listBillOfMaterialServices);
										
										}
								});
							}
							//$scope.getBillOfMaterialServices();
							
							$scope.listprocedure = [];
							$scope.searchprocedure = function(searchString){
								$scope.listprocedure = [];
										var URI = BASE_URL + ROOT_URL + "/api/procedure/searchProcedureByNameOrCode";
										var data = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												serviceName : searchString
										};
									
										console.log('URI', URI);
										return BillingGenericService
												.serviceAction(METHOD_POST, URI, data)
												.then(
														function(response) {
															$scope.procedureMasterList = [] ;
														if (response.data.status == "success") {
															return $scope.listprocedure = response.data.listObject;
															}
														});
							
							}

			
							$scope.procedure = {};
							$scope.selectProcedureName = function($item,$model, $label) {
								$scope.procedure.serviceId = $item.serviceMasterId;
								$scope.procedure.serviceStandardName = $item.serviceStandardName;
								$scope.procedure.serviceCode = $item.serviceStandardCode;
								
							}
							
							$scope.updateStatus = function(info,status){
								try{
									$scope.commonUpdate ={};
									$scope.commonUpdate.status=status,
									$scope.commonUpdate.procedureId=info.procedureId,
									$scope.commonUpdate.surgeryGradeId=info.surgeryGradeId,
									$scope.commonUpdate.tariffId=info.tariffId,
									$scope.commonUpdate.visitTypeId=info.visitTypeId,
									$scope.commonUpdate.paymentEntitlementId=info.paymentEntitlementId,
									$scope.commonUpdate.patientCategoryId=info.patientCategoryId,
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/procedure/BillOfMaterial/updateStatusProcedure";
									console.log("update status",$scope.commonUpdate);
									//return false;
									GenericService.serviceAction("POST",URI,$scope.commonUpdate).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
//											$scope.$broadcast('show-errors-reset');
											$scope.getBillOdMaterialListByProcId();
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
			}]);
							