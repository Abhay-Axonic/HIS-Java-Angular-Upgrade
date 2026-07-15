angular
		.module('myApp')
		.controller(
				'bloodBagStockUpdationController',
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
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage) {
							
							
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
							$scope.searchKeyword="";
							
							$scope.comObj={
									fromDate : ""	,
									toDate : "",
									componentId : "",
									expiryDate : ""
							};
							
							$scope.comObj.fromDate = new Date();
							$scope.comObj.toDate = new Date();
							$scope.comObj.expiryDate = new Date();
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};
							
							$scope.openExpiryDatePicker = function() {
								$scope.expiryDatepicker.opened = true;
							};

							$scope.expiryDatepicker = {
									opened : false
							};
							
							//get component list
							$scope.componentList = [];
							$scope.getComponentList = function(){
								try{
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/bloodBank/getActiveBloodComponentMaster"+"/"+ $scope.organizationId +"/"+ $scope.unitId;
									GenericService.serviceAction("GET",URI).then(function(response){
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.componentList = response.data.listObject;
										}else if(response.data.status == 'error'){
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
									
								}catch(e){
									console.log("Exception-",e);
								}
							}
							
							$scope.getComponentList();		
							
							//search Details 
							$scope.fDate="";
							$scope.tDate="";
							$scope.expiryDt="";
							$scope.searchList = [];
							$scope.searchDetails = function()
							{
								$scope.startLoader();
								if(($scope.comObj.fromDate=="") && ($scope.comObj.toDate=="")){
									$scope.fDate=$scope.comObj.fromDate;
									$scope.tDate=$scope.comObj.toDate;
								}
								else
									{
									$scope.fDate = moment($scope.comObj.fromDate).format('YYYY-MM-DD');
									$scope.tDate = moment($scope.comObj.toDate).format('YYYY-MM-DD');
									$scope.expiryDt = moment($scope.comObj.expiryDate).format('YYYY-MM-DD');
									}
								
								var data ={
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										bloodBagNo:$scope.comObj.bloodBagNo,
										fromDate:$scope.fDate,
										toDate:$scope.tDate,
										expiryDate : $scope.expiryDt,
										componentId : $scope.comObj.componentId
								};
								var URI = BASE_URL + ROOT_URL + "/global/blood/component/separation/getComponentSeparationListForStockUpdation";

								GenericService
										.serviceAction(METHOD_POST, URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.searchList = [];
														if(response.data.listObject!= null && response.data.listObject.length>0){
															angular.forEach(response.data.listObject,function(value,index){
																value.expiryDate = moment(moment(new Date(),'YYYY-MM-DD').add(value.expiryInDays,'days')).format('YYYY-MM-DD');
																value.isSelected = false;
																value.createdBy = $scope.userId;
																value.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
																value.status = "A";
																value.updatedBy = $scope.userId;
																value.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
																value.organizationId = $scope.organizationId;
																value.unitId = $scope.unitId;
																$scope.searchList.push(value);
															});
														}
														$scope.stopLoader();
													
													} else {
														$scope.stopLoader();
														growl.warning("No Result Found!",
																		{
																			title : ERROR_MSG
																		});
													}
												});
							}
							$scope.searchDetails();
							
							$scope.usersList = [];
							$scope.getEmployeeList = function(){
								
								try{
									$rootScope.startLoader();
									
									var data = {
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
									};
									var URI = BASE_URL + ROOT_URL
									+ GETUSERSLIST;
							GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.usersList = [];
												if (response.data.status == "success") {
													$scope.usersList = response.data.listObject;
													console.log("$scope.usersList",$scope.usersList);
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
							}
							$scope.getEmployeeList();
							
							$scope.approvedById = $scope.userId;
							
							$scope.finalList = [];
							$scope.updateBloodBankStock = function(){
								try{
									$rootScope.startLoader();
									$scope.finalList = [];
									if($scope.searchList!=null && $scope.searchList.length>0){
										
										var foundIsSelected = $scope.searchList.find(function (obj) {
											return obj.isSelected;
										 });
										if(foundIsSelected == undefined){
											growl.warning("Please Select Data for save",
													{
														title : 'Warning!'
													});
											$rootScope.stopLoader();
											return false;
										}
										angular.forEach($scope.searchList,function(value,index){
											if(value.isSelected){
										var data = {
											batchNo : value.batchNo,
											expiryDate : moment(value.expiryDate).format('DD-MM-YYYY HH:mm:ss'),
											bloodBagNo : value.bloodBagNo,
											bloodBagTypeId : value.bloodBagTypeId,
											bloodBankId : value.bloodBankId,
											bloodBagCapacityId : value.bagCapacityId,
											bloodGroupId : value.bloodGroupId,
											segmentNumber :value.segmentNumber ,
											shortName : value.shortName,
											donorId : value.donorId,
											barcode : value.barcode,
											donorNumber : value.donorNumber,
											bloodComponentId : value.componentId ,
											createdBy:$scope.userId,
									 		createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									 		status:"A",
									 		updatedBy:$scope.userId,
									 		updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									 		organizationId:$scope.organizationId,
									 		unitId:$scope.unitId,
									 		approvedById : $scope.approvedById,
									 		redCellId : value.redCellId,
									 		bloodUnit : value.bloodUnitNumber,
										};
										$scope.finalList.push(data);
											}	
										});
									}
									
									var URI = BASE_URL + ROOT_URL + "/api/bloodbank/stock/updateDonorBloodInStock" ;
							GenericService.serviceAction("POST", URI, $scope.finalList)
									.then(function(response) {
												if (response.data.status == "success") {
													growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
													$scope.finalList = [];
													$scope.usersList = [];
													$scope.getEmployeeList();
													$scope.fDate="";
													$scope.tDate="";
													$scope.expiryDt="";
													$scope.searchList = [];
													$scope.comObj={
																fromDate : new Date()	,
																toDate :  new Date(),
																componentId : "",
																expiryDate : ""
													};
													$scope.searchDetails();
													$scope.approvedById = $scope.userId;
													$scope.componentList = [];
													$scope.getComponentList();		
													$rootScope.stopLoader();
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
							}
			}]);
							