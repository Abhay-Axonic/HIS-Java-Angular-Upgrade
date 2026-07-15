angular
		.module('myApp')
		.controller(
				'detailsIssueBloodBagController',
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
						'$stateParams',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$stateParams) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
						
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.common = $stateParams.bloodComponentObj;
							console.log("$scope.common",$scope.common);
							
							$scope.comObj = {
									
									bloodBagNo:"",
									segmentNo:"",
									btoName:"",
									remarks:"",
									issuerName:"",
									receivedBy:"",
									rejectReasonId:"",
									noteReject:"",
									issueBloodBagStatus:"",
									btoId:""
									
							};
							$scope.comObj.btoName=$scope.common.userName;
							$scope.comObj.issuerName=$scope.common.userName;
							$scope.comObj.btoId=$scope.common.btoId;
							
							$scope.patientDetails ={
									uhIdNumber: $scope.common.uhidNumber, 
									patientName:$scope.common.patientName,
									genderCode:$scope.common.genderCode, 
									age:$scope.common.age, 
									birthDate:$scope.common.dob
							};
						
						/*	$scope.reasonList = [];
							$scope.getReasonMasterList = function()
							{
								var data={
										organizationId:$scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL+ GET_REASON_MASTER_LIST ;
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.reasonList = response.data.listObject;
												console.log("reasonList ",response.data.listObject);
											}
										});
							}
							
							
							$scope.getReasonMasterList();*/
							
							$scope.crossMatchRegisterDetailList = [];
							$scope.bloodBagIssueList = [];
							$scope.getCrossMatchDetailsList = function()
							{
								var data={
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										crossMatchRegisterId:$scope.common.crossMatchRegisterId
								};
								var URI = BASE_URL + ROOT_URL+ "/api/bloodbank/crossMatch/getCrossMatchRegisterDetailsByCrossMatchID" ;
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												//$scope.crossMatchRegisterDetailList = response.data.listObject;
												console.log("$scope.crossMatchRegisterDetailList ",$scope.crossMatchRegisterDetailList);
												
												 angular.forEach(response.data.listObject,function(value,index){
														
														var newArr = {};
															newArr.bloodComponentDesc = value.bloodComponentDesc;
														newArr.bloodGroupName = value.bloodGroupName;
														newArr.albuminMajor = value.albuminMajor;
														newArr.albuminMinor = value.albuminMinor;
														newArr.coombsMajor = value.coombsMajor;
														newArr.coombsMinor = value.coombsMinor;
														newArr.salineMajor = value.salineMajor;
														newArr.salineMinor = value.salineMinor;
														newArr.crossMatchRegisterDetailsId = value.crossMatchRegisterDetailsId;
														newArr.bloodBankStockId = value.bloodBankStockId;
														newArr.crossMatchRegisterDetailsStatus = value.crossMatchRegisterDetailsStatus;
														newArr.bloodBagNo = value.bloodBagNo;
														newArr.segmentNo = value.segmentNo;
														newArr.crossMatchReportResult = value.crossMatchReportResult;
														newArr.bloodGroupId = value.bloodGroupId;
														newArr.componentId = value.componentId;
														newArr.chkValue = false;
														newArr.createdBy = $scope.userId;
														newArr.acceptStatus = 'N';
														newArr.issueBloodBagStatusId = 0 ;
														newArr.rejectReasonId = 0 ;
														newArr.note = "" ;
														$scope.crossMatchRegisterDetailList.push(newArr);
													});
											}
										});
								
								$scope.dt =  moment(new Date()).format('YYYY-MM-DD');
							//	 var dateOut = new Date($scope.common.issueDate);
							//	$scope.issueDt = moment(dateOut).format('YYYY-MM-DD');
								
								 if($scope.common.compairIssueDate > $scope.dt)
								 {
								 $scope.msg = "Issue Date is After Current date , Still ";
								 }else{
									 $scope.msg="";
								 }
							 
							 console.log(" $scope.msg", $scope.msg);
							 console.log("$scope.common.issueDate ",$scope.common.compairIssueDate);
							 console.log("new Date() ",$scope.dt);
							}
							
							$scope.getCrossMatchDetailsList();
							
							
							if($scope.common.bloodComponentId!=null && $scope.common.bloodComponentId!=undefined  && $scope.common.bloodComponentDesc!=null && $scope.common.bloodComponentDesc!=undefined)
							{
							$scope.comObj.componentObj.bloodComponentId = $scope.common.bloodComponentId;
							$scope.comObj.componentObj.bloodComponentDesc = $scope.common.bloodComponentDesc;
							
							}
							
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
							
							$scope.crossMatchReportObj = {
									albuminMajor:"",
									albuminMinor:"",
									coombsMajor:"",
									coombsMinor:"",
									salineMajor:"",
									salineMinor:"",
									crossMatchReportResult:""
							};
							$scope.getCrossMatchResultDetails = function(info,index){
								
								$scope.crossMatchReportObj.albuminMajor = info.albuminMajor;
								$scope.crossMatchReportObj.albuminMinor = info.albuminMinor;
								$scope.crossMatchReportObj.coombsMajor = info.coombsMajor;
								$scope.crossMatchReportObj.coombsMinor = info.coombsMinor;
								$scope.crossMatchReportObj.salineMajor = info.salineMajor;
								$scope.crossMatchReportObj.salineMinor = info.salineMinor;
								$scope.crossMatchReportObj.crossMatchReportResult = info.crossMatchReportResult;
								
							}
							
							$scope.changeAcceptStatus = function(info){
								info.acceptStatus = info.chkValue;
							}
							
							$scope.reasonList = [];
							$scope.getReasonMasterList = function()
							{
								var data={
										organizationId:$scope.organizationId,
										unitId:$scope.unitId
								};
								var URI = BASE_URL + ROOT_URL+ "/bloodBank/getAllReasonOfDiscardMasterList" ;
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.reasonList = response.data.listObject;
												console.log("reasonList ",response.data.listObject);
											}
										});
							}
							
							
							$scope.getReasonMasterList();
							
							$scope.showRejecPopup = function(isAccRej,rejectIndx) {
								if(isAccRej==false)
									{
									$scope.rejectIndx = rejectIndx;
									 $("#accepatReject").modal('show');
									}
								
							}
							
							$scope.bloodBagIssueList = [];
							$scope.saveIssueBloodBag = function(){
								try{
								 $rootScope.startLoader(); 
								 if($scope.comObj.receivedBy=="" || $scope.comObj.receivedBy==0){
									 $rootScope.stopLoader();
									 growl.warning("Please select Received BY ",
												{
													title : 'warning!'
												});
								 }else{
									 
								 angular.forEach($scope.crossMatchRegisterDetailList,function(value,index){
									 
									 var newArr = {};
										newArr.createdBy = $scope.userId;
										newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newArr.status = "A";
										newArr.updatedBy = $scope.userId;
										newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newArr.unitId = $scope.unitId;
										newArr.organizationId = $scope.organizationId;
										newArr.crossMatchRegisterDetailsId = value.crossMatchRegisterDetailsId;
										newArr.bloodBankStockId = value.bloodBankStockId;
										//newArr.crossMatchRegisterDetailsStatus = value.crossMatchRegisterDetailsStatus;
										newArr.bagNo = value.bloodBagNo;
										newArr.segmentNo = value.segmentNo;
										newArr.crossMatchReportResult = value.crossMatchReportResult;
										newArr.bloodGroupId = value.bloodGroupId;
										newArr.componentId = value.componentId;
										newArr.reasonId = value.rejectReasonId;
										newArr.note = value.note;
										
									 if(value.chkValue && value.acceptStatus){
										 newArr.issueBloodBagStatusId = 14; //for issued
										 $scope.bloodBagIssueList.push(newArr);
									 }else if(value.chkValue && !value.acceptStatus){
										 newArr.issueBloodBagStatusId = 2;
										 $scope.bloodBagIssueList.push(newArr);
									 }
									
								 });
								 
								 if($scope.bloodBagIssueList.length>0){
								$scope.bloodBagIssueObj = {
										requisitionDate:$scope.common.requisitionDate+" 00:00:00",
										issueDate:$scope.common.issueDate+" 00:00:00",
										patientId:$scope.common.patientId,
										btoId:$scope.comObj.btoId,
										issuerId:$scope.comObj.btoId,
										receivedId:$scope.comObj.receivedBy,
										remarks:$scope.comObj.remarks,
										crossMatchRegisterId:$scope.common.crossMatchRegisterId,
										createdBy:$scope.userId,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										status:"A",
										updatedBy:$scope.userId,
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										bloodBagIssueList: $scope.bloodBagIssueList,
										bloodUnitQty:$scope.common.qty
								};
								
								console.log("$scope.bloodBagIssueObj",$scope.bloodBagIssueObj);
								var URI = BASE_URL + ROOT_URL+ SAVE_ISSUE_BLOOD_BAG ;
								GenericService
								.serviceAction(METHOD_POST, URI ,$scope.bloodBagIssueObj)
								.then(
										function(response) {

											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												$scope.comObj={};
												$scope.bloodBagIssueList=[];
												$scope.crossMatchRegisterDetailList=[];
												$scope.getCrossMatchDetailsList();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												$scope.comObj={};
												$scope.bloodBagIssueList=[];
												$scope.crossMatchRegisterDetailList=[];
												$scope.getCrossMatchDetailsList();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
								 }else
									 {
									 $rootScope.stopLoader();
									 growl.warning("Please select Bag",
												{
													title : 'warning!'
												});
									 }
								 }
								}catch(e){
									console.log("Exception-",e);
								}	
							}
							
							

						}]);
							