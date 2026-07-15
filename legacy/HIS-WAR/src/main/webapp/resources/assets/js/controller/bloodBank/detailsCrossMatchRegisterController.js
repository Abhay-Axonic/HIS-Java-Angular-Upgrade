angular
		.module('myApp')
		.controller(
				'detailsCrossMatchRegisterController',
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
							
							$scope.commonObj={
									
									"fromDate":"",
									"toDate":""
								}; 
							
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
						
							//search  
							$scope.common = $stateParams.detailObj;
							console.log("common",$scope.common);
							
							$scope.componentObj = {
									componentId:$scope.common.componentId,
									bloodComponentDesc:$scope.common.bloodComponentDesc
								};
							
							$scope.bloodGroupObj = {
									recipientBloodGroupId:"",
									bloodGroupDesc:""
							};
							
							$scope.bloodBagDetailsObj = {
									bloodBagNo:"",
									bloodBankStockId:"",
									segmentNumber:"",
									crossMatchResult:""
									
							};
							$scope.pendingQty = $scope.common.bloodUnit;
							$scope.detailObj = {
									btoId:"",
									technicianId:"",
									remark:"",
									salineMajor:"",
									salineMinor:"",
									albuminMajor:"",
									albuminMinor:"",
									coombMajor:"",
									coombMinor:"",
									result:"",
									resultId:"",
									crossMatchStatus:""
							}
							$scope.bloodGroupObj.recipientBloodGroupId = $scope.common.requiredBloodGroupId;
							$scope.bloodGroupObj.bloodGroupDesc = $scope.common.requiredBloodGroup;
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
											console.log("$scope.componentList",$scope.componentList);
											
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
							
							
							//get Blood Group  list
							$scope.bloodGroupList = [];
							$scope.getBloodGroupList = function(){
								try{
									$rootScope.startLoader();
									var data = {
											unitId:$scope.unitId,
											organizationId:$scope.organizationId,
											componentId:$scope.componentObj.componentId,
											donorBloodGroupId:$scope.common.patientBloodGroupId
											
									};
									var URI = BASE_URL + ROOT_URL + "/api/bloodBank/getBloodGroupsByComponentAndDonorBloodGroupId"
									GenericService.serviceAction(METHOD_POST,URI,data).then(function(response){
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.bloodGroupList = response.data.listObject;
											console.log("$scope.bloodGroupList",$scope.bloodGroupList);
											
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
							
							
							//get Blood Bag  list
							$scope.bloodBagList = [];
							$scope.getBloodBagList = function(name){
								try{
									$rootScope.startLoader();
									var data = {
											unitId:$scope.unitId,
											organizationId:$scope.organizationId,
											bloodComponentId:$scope.componentObj.componentId,
											bloodGroupId:$scope.common.patientBloodGroupId,
											currentDate:moment(new Date()).format('YYYY-MM-DD'),
											bloodBagNo:name
											
									};
									var URI = BASE_URL + ROOT_URL + "/api/bloodbank/stock/getBagNoListByComponentAndGroupId"
									return GenericService.serviceAction(METHOD_POST,URI,data).then(function(response){
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.bloodBagList = response.data.listObject;
											console.log("$scope.bloodBagList",$scope.bloodBagList);
											return $scope.bloodBagList;
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
							
							  $scope.selectBloodBag = function(info, model,label) {
									
									console.log("info  ",info);
									$scope.bloodBagDetailsObj.bloodBankStockId = info.bloodBankStockId;
									$scope.bloodBagDetailsObj.segmentNumber = info.segmentNumber;
								console.log("bloodBagDetailsObj",$scope.bloodBagDetailsObj);
								}
							if($scope.common.patientBloodGroupId!=null){
								$scope.getBloodGroupList();
							//	$scope.getBloodBagList();
								}
							
							$scope.detailsList = [];
							$scope.cnt=0;
							$scope.addDetails = function()
							{
								try{
									$rootScope.startLoader();
									
									if($scope.componentObj.bloodComponentDesc==""){
										$rootScope.stopLoader();
										growl.warning("Please Select component",
												{
													title : 'warning!'
												});
									}else if($scope.bloodGroupObj.recipientBloodGroupId==""){
										$rootScope.stopLoader();
										growl.warning("Please Select Blood Group",
												{
													title : 'warning!'
												});
									}else if($scope.bloodBagDetailsObj.bloodBagNo=="" || $scope.bloodBagDetailsObj.bloodBagNo==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select Bag Number",
												{
													title : 'warning!'
												});
									}else if($scope.bloodBagDetailsObj.segmentNumber=="" || $scope.bloodBagDetailsObj.segmentNumber==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select Segment Number",
												{
													title : 'warning!'
												});
									}else{
										
										if($scope.pendingQty==0)
										{	
											$rootScope.stopLoader();
											growl.warning("Pending Quantity greater than Required Quantity,You Can not add Details",
													{
														title : 'warning!'
													});
											//$scope.bloodBagDetailsObj = {};
										}
										else{
												$rootScope.stopLoader();
												$scope.countQty=0;
												$scope.statusLeg=0;
												$scope.flag=false;
												angular.forEach($scope.detailsList,function(value,index){
											
													if(value.bloodBagNo==$scope.bloodBagDetailsObj.bloodBagNo){
														$scope.flag =true;
														$scope.bloodBagDetailsObj = {};
													}
													if(value.crossMatchResult==1){
														$scope.countQty += 1;
														$scope.statusLeg += 1;
													}
													if(value.crossMatchResult==""){
														$scope.statusLeg += 1;
												
													}
												});
												console.log("$scope.detailsList",$scope.detailsList);
												console.log("$scope.countQty",$scope.countQty);
												console.log("$scope.statusLeg",$scope.statusLeg);
												if($scope.common.bloodUnit>$scope.statusLeg){
													
													if($scope.common.bloodUnit>$scope.countQty){
												
														if(!$scope.flag){
															
															console.log("$scope.bloodBagDetailsObj",$scope.bloodBagDetailsObj);
																
															var newArr = {};
															newArr.bloodComponentDesc = $scope.componentObj.bloodComponentDesc;
															newArr.componentId = $scope.componentObj.componentId;
															newArr.bloodGroupId = $scope.bloodGroupObj.recipientBloodGroupId;
															newArr.bloodGroupDesc = $scope.bloodGroupObj.bloodGroupDesc;
															newArr.bloodBagNo = $scope.bloodBagDetailsObj.bloodBagNo;
															newArr.bloodBankStockId = $scope.bloodBagDetailsObj.bloodBankStockId;
															newArr.segmentNo = $scope.bloodBagDetailsObj.segmentNumber;
															newArr.salineMajor = $scope.bloodBagDetailsObj.salineMajor;
															newArr.salineMinor = $scope.bloodBagDetailsObj.salineMinor;
															newArr.albuminMajor = $scope.bloodBagDetailsObj.albuminMajor;
															newArr.albuminMinor = $scope.bloodBagDetailsObj.albuminMinor;
															newArr.coombsMajor = $scope.bloodBagDetailsObj.coombsMajor;
															newArr.coombsMinor = $scope.bloodBagDetailsObj.coombsMinor;
															
															if($scope.common.byCrossChecking=='A'){
																
																newArr.crossMatchResult ="";
																newArr.crossMatchReportResult = $scope.detailObj.result;//$scope.bloodBagDetailsObj.crossMatchReportResult;
															
															}else{
																newArr.crossMatchResult =1;
																newArr.crossMatchReportResult ="Compatible";//$scope.bloodBagDetailsObj.crossMatchReportResult;
																
																$scope.pendingQty = $scope.pendingQty - 1;
															}
															newArr.crossMatchRegisterDetailsStatus = $scope.bloodBagDetailsObj.crossMatchRegisterDetailsStatus;
															
															newArr.idx=$scope.cnt;
															$scope.detailsList.push(newArr);
															$scope.cnt =$scope.cnt+1;
															console.log("$scope.detailsList",$scope.detailsList);
																
															//$scope.pendingQty = $scope.common.bloodUnit - $scope.detailsList.length;
															$scope.bloodBagDetailsObj = {};
																
															$rootScope.stopLoader();
														}else{
													
															growl.warning("Please select Different Bag",
																{
																	title : 'warning!'
																});
														}
													}else{
														$rootScope.stopLoader();
														growl.warning("Pending Quantity greater than Required Quantity,You Can not add Details",
																{
																title : 'warning!'
																});
												
													}
												}else{
													$rootScope.stopLoader();
													growl.warning("Please Select cross Match result of all bag,You Can not add Details",
															{
															title : 'warning!'
															});
											
												}
											}
										}
								}catch(e){
									console.log("Exception-",e);
								}
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
							
							$scope.crossMatchingResult = function(){
								console.log("res",$scope.detailObj.salineMajor);
								if($scope.detailObj.salineMajor==1 && $scope.detailObj.salineMinor==1 && $scope.detailObj.albuminMajor==1 && $scope.detailObj.albuminMinor==1 && $scope.detailObj.coombMajor==1 && $scope.detailObj.coombMinor==1){
								$scope.detailObj.result = "Compatible";
								$scope.detailObj.resultId = 1;
								$scope.bloodBagDetailsObj.crossMatchResult=1;
								}else if(($scope.detailObj.salineMinor==2 || $scope.detailObj.albuminMinor==2 || $scope.detailObj.coombMinor==2 ) &&
										($scope.detailObj.salineMajor==1 && $scope.detailObj.albuminMajor==1 && $scope.detailObj.coombMajor==1 )){
											$scope.detailObj.result = "Major Compatible";
											$scope.bloodBagDetailsObj.crossMatchResult=1;
											$scope.detailObj.resultId = 1;
										}else if($scope.detailObj.salineMajor==2 || $scope.detailObj.albuminMajor==2 || $scope.detailObj.coombMajor==2){
									$scope.detailObj.result = "Non Compatible";
									$scope.detailObj.resultId = 2;
								}else if(($scope.detailObj.salineMajor==2 && $scope.detailObj.salineMinor==2) ||
										($scope.detailObj.salineMajor==2 && $scope.detailObj.albuminMinor==2) ||
										($scope.detailObj.salineMajor==2 && $scope.detailObj.coombMinor==2) ||
										($scope.detailObj.albuminMajor==2 && $scope.detailObj.albuminMinor==2) ||
										($scope.detailObj.albuminMajor==2 && $scope.detailObj.salineMinor==2) ||
										($scope.detailObj.albuminMajor==2 && $scope.detailObj.coombMinor==2) ||
										($scope.detailObj.coombMajor==2 && $scope.detailObj.salineMinor==2) ||
										($scope.detailObj.coombMajor==2 && $scope.detailObj.albuminMinor==2) ||
										($scope.detailObj.coombMajor==2 && $scope.detailObj.coombMinor==2)){
									$scope.detailObj.result = "Non Compatible";
									$scope.detailObj.resultId = 2;
								}else if(($scope.detailObj.salineMinor==2 || $scope.detailObj.albuminMinor==2 || $scope.detailObj.coombMinor==2 ) &&
										($scope.detailObj.salineMajor==3 || $scope.detailObj.albuminMajor==3 || $scope.detailObj.coombMajor==3 )){
									$scope.detailObj.result = "Compatible in 37 Degree";
									$scope.bloodBagDetailsObj.crossMatchResult=1;
									$scope.detailObj.resultId = 1;
								}else if(($scope.detailObj.salineMinor==3 || $scope.detailObj.albuminMinor==3 || $scope.detailObj.coombMinor==3 ||
										$scope.detailObj.salineMajor==3 ||$scope.detailObj.albuminMajor==3 || $scope.detailObj.coombMajor==3 ) &&
										($scope.detailObj.salineMinor==1 || $scope.detailObj.albuminMinor==1 || $scope.detailObj.coombMinor==1 ||
										$scope.detailObj.salineMajor==1 ||$scope.detailObj.albuminMajor==1 || $scope.detailObj.coombMajor==1 &&
										$scope.detailObj.salineMajor!=2 && $scope.detailObj.albuminMajor!=2 && $scope.detailObj.coombMajor!=2)){
									$scope.detailObj.result = "Compatible in 37 Degree";
									$scope.bloodBagDetailsObj.crossMatchResult=1;
									$scope.detailObj.resultId = 1;
								}
							}
							
							$scope.crossMatchResultDetails = function(info,index){
								$scope.idx = index;
//								$scope.detailsList[index].crossMatchResult= $scope.detailObj.resultId
								if($scope.detailsList[$scope.idx].crossMatchResult!=null ||$scope.detailsList[$scope.idx].crossMatchResult!="" || $scope.detailsList[$scope.idx].crossMatchResult!=undefined){
									$scope.detailObj.resultId = $scope.detailsList[$scope.idx].crossMatchResult;
								}
								if($scope.detailsList[$scope.idx].salineMajor!=null || $scope.detailsList[$scope.idx].salineMajor!="" || $scope.detailsList[$scope.idx].salineMajor!=undefined){
									$scope.detailObj.salineMajor = $scope.detailsList[$scope.idx].salineMajor;
								}
								
								if($scope.detailsList[$scope.idx].salineMinor!=null || $scope.detailsList[$scope.idx].salineMinor!="" || $scope.detailsList[$scope.idx].salineMinor!=undefined){
									$scope.detailObj.salineMinor = $scope.detailsList[$scope.idx].salineMinor;
								}
								
								if($scope.detailsList[$scope.idx].albuminMajor!=null || $scope.detailsList[$scope.idx].albuminMajor!="" || $scope.detailsList[$scope.idx].albuminMajor!=undefined){
									$scope.detailObj.albuminMajor = $scope.detailsList[$scope.idx].albuminMajor;
								}
								
								if($scope.detailsList[$scope.idx].albuminMinor!=null || $scope.detailsList[$scope.idx].albuminMinor!="" || $scope.detailsList[$scope.idx].albuminMinor!=undefined){
									$scope.detailObj.albuminMinor = $scope.detailsList[$scope.idx].albuminMinor;
								}
								
								if($scope.detailsList[$scope.idx].coombsMajor!=null || $scope.detailsList[$scope.idx].coombsMajor!="" || $scope.detailsList[$scope.idx].coombsMajor!=undefined){
									$scope.detailObj.coombMajor = $scope.detailsList[$scope.idx].coombsMajor;
								}
								
								if($scope.detailsList[$scope.idx].coombsMinor!=null || $scope.detailsList[$scope.idx].coombsMinor!="" || $scope.detailsList[$scope.idx].coombsMinor!=undefined){
									$scope.detailObj.coombMinor = $scope.detailsList[$scope.idx].coombsMinor;
								}
								
								if($scope.detailsList[$scope.idx].crossMatchReportResult!=null || $scope.detailsList[$scope.idx].crossMatchReportResult!="" || $scope.detailsList[$scope.idx].crossMatchReportResult!=undefined){
									$scope.detailObj.result = $scope.detailsList[$scope.idx].crossMatchReportResult;
								}
							
							}
							
							$scope.crossMatchReportObj ={
									resultTitle:"",
									resultId:""
								//	isCrossMatchResultChecked:""
							};
							
							$scope.hideWindow = function(){
								
								$('#bloodBagDetails').modal('hide');
								// angular.element('#denominationOk').modal('hide');
							}
							
							$scope.saveCrossMatchReport = function(){
								
									if($scope.detailObj.salineMajor=="")
									{
										growl.warning("Please Select Saline Major",
												{
													title : 'warning!'
												});
									}else if($scope.detailObj.salineMinor=="")
									{
										growl.warning("Please Select Saline Minor",
												{
													title : 'warning!'
												});
									}
									else if($scope.detailObj.albuminMajor=="")
									{
										growl.warning("Please Select Albumin Minor",
												{
													title : 'warning!'
												});
									}
									else if($scope.detailObj.albuminMinor=="")
									{
										growl.warning("Please Select Albumin Minor",
												{
													title : 'warning!'
												});
									}
									else if($scope.detailObj.coombMajor=="")
									{
										growl.warning("Please Select Coomb Minor",
												{
													title : 'warning!'
												});
									}
									else if($scope.detailObj.coombMinor=="")
									{
										growl.warning("Please Select Coomb Minor",
												{
													title : 'warning!'
												});
									}
									else if($scope.detailObj.resultId=="")
									{
										growl.warning("Can not Save Without Result",
												{
													title : 'warning!'
												});
									}else{
									
									$scope.crossMatchReportObj.resultId = $scope.detailObj.resultId;
									$scope.crossMatchReportObj.isCrossMatchResultChecked = $scope.detailObj.isCrossMatchResultChecked;
									console.log("$scope.idx",$scope.idx);
									$scope.detailsList[$scope.idx].crossMatchResult = $scope.detailObj.resultId;
									$scope.detailsList[$scope.idx].salineMajor = $scope.detailObj.salineMajor;
									$scope.detailsList[$scope.idx].salineMinor = $scope.detailObj.salineMinor;
									$scope.detailsList[$scope.idx].albuminMajor = $scope.detailObj.albuminMajor;
									$scope.detailsList[$scope.idx].albuminMinor =$scope.detailObj.albuminMinor;
									$scope.detailsList[$scope.idx].coombsMajor = $scope.detailObj.coombMajor;
									$scope.detailsList[$scope.idx].coombsMinor = $scope.detailObj.coombMinor;
									$scope.detailsList[$scope.idx].crossMatchReportResult = $scope.detailObj.result;
									if($scope.detailsList[$scope.idx].crossMatchResult ==1){
										$scope.pendingQty = $scope.pendingQty - 1;
									}	
									$scope.detailObj.result="";
									}
									}
									
									$scope.crossMatchRegisterDetailList = [];
									$scope.saveCrossMatchRegister = function(){
										try{
											$rootScope.startLoader();
											//if($scope.common.byCrossChecking=='Y'){
											angular.forEach($scope.detailsList,function(value,index){
												var data = {
														componentId:value.componentId,
														bloodGroupId:value.bloodGroupId,
														bloodBankStockId:value.bloodBankStockId,
														bloodBagNo:value.bloodBagNo,
														segmentNo:value.segmentNo,
														salineMajor:value.salineMajor,
														salineMinor:value.salineMinor,
														albuminMajor:value.albuminMajor,
														albuminMinor:value.albuminMinor,
														coombsMajor:value.coombsMajor,
														coombsMinor:value.coombsMinor,
														crossMatchReportResult:value.crossMatchReportResult,
														crossMatchRegisterDetailsStatus:value.crossMatchResult,
														createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
														status:"A",
														createdBy:$scope.userId,
														updatedBy:$scope.userId,
														updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
														organizationId:$scope.organizationId,
														unitId:$scope.unitId,
												};
												
												$scope.crossMatchRegisterDetailList.push(data);
											});
											if($scope.pendingQty!=0)
												{
												$scope.detailObj.crossMatchStatus=12; //partialy completed
												
												}else
												{
													$scope.detailObj.crossMatchStatus=13; //completed
												}
											$scope.crossMatchObj = {
													btoId:$scope.detailObj.btoId,
													componentId:$scope.common.componentId,
													patientId:$scope.common.patientId,
													issueDate:$rootScope.getChangedFormatedDateShalse($scope.common.issueDate)+" 00:00:00",//$scope.common.issueDate+" 00:00:00",
													patientRequisitionId:$scope.common.patientRequisitionId,
													bloodComponentOrderId:$scope.common.bloodComponentOrderId,
													requisitionDate:$rootScope.getChangedFormatedDateShalse($scope.common.requisitionDate)+" 00:00:00",//$scope.common.requisitionDate+" 00:00:00",//requisitionDate,
													requiredBloodGroup:$scope.common.requiredBloodGroupId,
													patientBloodGroup:$scope.common.patientBloodGroupId,
													requiredQty:$scope.common.bloodUnit,
													pendingQty:$scope.pendingQty,
													technicianId:$scope.detailObj.technicianId,
													remark:$scope.detailObj.remark,
													crossMatchStatus:$scope.detailObj.crossMatchStatus,
													createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													status:"A",
													createdBy:$scope.userId,
													updatedBy:$scope.userId,
													updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													organizationId:$scope.organizationId,
													unitId:$scope.unitId,
													crossMatchRegisterDetails:$scope.crossMatchRegisterDetailList

											};
											
											console.log("$scope.crossMatchObj",$scope.crossMatchObj);
											
											var URI = BASE_URL + ROOT_URL + "/api/bloodbank/crossMatch/saveCrossMatchRegister";
											GenericService.serviceAction("POST", URI,$scope.crossMatchObj).then(function(response) {
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'success!'
															});
													$scope.crossMatchObj ={};
													$scope.detailObj = {};
													$scope.common ={};
													$scope.detailsList = [];
													
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
								
							
}]);			

					