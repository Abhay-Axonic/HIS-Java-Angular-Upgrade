angular
		.module('myApp')
		.controller(
				'detailsKitIndentAcceptanceController',
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
						'PagerService',
						'$stateParams',
						'$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams, $location) {
							
							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
																							
								if($stateParams.issueNo){
									$scope.viewKitissueDetails();
																		
								}
								
								$scope.getReasonsForRejection();

							}
							
														
							$scope.viewKitissueDetails = function() {
						
								 data = {};
								 
								var URI = BASE_URL + ROOT_URL
									+ VIEWKITBYISSUENO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&issueNo=" + $stateParams.issueNo;
																													
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.viewKitIssueObj = {};
											if (response.data.status == "success") {
												var count=0;
												$scope.saveDisabled=false;
												angular.forEach(response.data.object.kitList,function(value,index){
												angular.forEach(value.subKitList,function(value1,index1){	
													if(value.kitId==value1.kitId){
														if(value1.isAccepted==true || value1.isRejected==true){
															value.is_active=false;
															 value.isSelected='Y';
															 count++;
														}
														else{
															value.is_active=true;
															value.isSelected='N';
														}
													value.isAccepted=value1.isAccepted;
													value.isRejected=value1.isRejected;
													}
												});
												});
												if(response.data.object.kitList.length==count)
													$scope.saveDisabled=true;
												$scope.viewKitIssueObj = response.data.object;
												
												console
														.log(
																"viewKitIssueObj",
																$scope.viewKitIssueObj);

											}
										});
							}
							
							$scope.viewKitIssueObj = {};
							$scope.sterilizedKitsList = [];
                            $scope.classNameForSterilizedKits = "";
                            
                            
                            $scope.showSterilizedKitListRow = function (data){
                            	
                            	$scope.subKitList = data.subKitList;
                            	
                            	$scope.classNameForSterilizedKits = ".subKitRow-"+ data.kitId;
                            	angular.element($scope.classNameForSterilizedKits).toggle();
                            	//document.getElementById("indentNo-"+info.indentNo).toggle();
                            	
                            	for(var i = 0; i < $scope.viewKitIssueObj.kitList.length; i++)
                            	{
                            		if(angular.element("subKitRow-"+$scope.viewKitIssueObj.kitList[i].kitId)[0] != undefined &&  angular.element("subKitRow-"+$scope.viewKitIssueObj.kitList[i].kitId)[0].style.display != "none" && $scope.viewKitIssueObj.kitList[i].kitId != data.kitId )
                            			{
                            				angular.element("subKitRow-"+$scope.viewKitIssueObj.kitList[i].kitId).toggle();
                            			}
                            		
                            	}
                            	
                            	
                            	
                            }
                            
                            $scope.currentKitRow = {};
                            $scope.setSubKitList = function(data) {
                            	
                            	$scope.subKitList = data.subKitList;
                            	$scope.currentKitRow = data;
                            	
                            }
                            
                            
                            $scope.toBeRejectedKit;
                            
                            $scope.toRejectItem = {
                            		
                            		id : "",
                            		rejectReasonNote : "",
                            		rejectReasonId : ""
                            		
                            };
                            
                            $scope.toReject = function(kitInfo)
                            {
                            	$scope.toBeRejectedKit = kitInfo;
                            	$scope.getReasonsForRejection();
                            }
							
							
							//For Reasons for rejection
							$scope.getReasonsForRejection = function(){
								var URI = BASE_URL + ROOT_URL
								+ GETREASONSTOREJECT;
								
                            	
							 var data = {};

						
							GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.rejectReasonsList = [];
										if (response.status == "200") {
											$scope.rejectReasonsList = response.data;
											console
													.log(
															"rejectReasonsList",
															$scope.rejectReasonsList);
	
										}
									});
                            }
							
							
							
							
							
							
							
							
							$scope.toBeAcceptedKit;
							
							$scope.toAcceptItem = {
                            		
                            		id : ""                            		                          		
                            };
                            
                            
							$scope.setAcceptItem = function(kitDetails){
								$scope.toBeAcceptedKit = kitDetails;
							}
							
							$scope.setIsAcceptedTrue = function(kitDetails){
								var index = $scope.finalKitList.indexOf(kitDetails);
								if(index==-1){
									$scope.finalKitList.push(kitDetails);
									index = $scope.finalKitList.indexOf(kitDetails);
								}
								$scope.finalKitList[index].isAccepted = true;
								$scope.finalKitList[index].isRejected = false;
								$scope.finalKitList[index].rejectReasonId = null;
								$scope.finalKitList[index].rejectReasonNote = null;
							}
							
							
							$scope.rejectionItem = {};
							$scope.setIsRejectedTrue = function(info){
								$scope.rejectionItem = info;
								var index = $scope.finalKitList.indexOf($scope.rejectionItem);
								if(index==-1){
									$scope.finalKitList.push($scope.rejectionItem);
									index = $scope.finalKitList.indexOf($scope.rejectionItem);
								}
								$scope.finalKitList[index].isAccepted = false;
								$scope.finalKitList[index].isRejected = true;
							}
							
							$scope.setRejectionDetails = function(){
								var index = $scope.finalKitList.indexOf($scope.rejectionItem);
								if(index==-1){
									$scope.finalKitList.push($scope.rejectionItem);
									index = $scope.finalKitList.indexOf($scope.rejectionItem);
								}
								$scope.finalKitList[index].rejectReasonId = $scope.rejectReasonId;
								$scope.finalKitList[index].rejectReasonNote = $scope.rejectReasonNote;
								
								$scope.rejectReasonId = null;
								$scope.rejectReasonNote = null;
							}
							
							$scope.isSaveDisabled = function(){
								var flag = 0;
								
								if($scope.isDisabled()){
									flag = 1;
								}
								
								if($scope.subKitList !=undefined && $scope.subKitList.length > 0){
									for(var i = 0; i < $scope.subKitList.length; i++){
										if($scope.subKitList[i].isAccepted == false && $scope.subKitList[i].isRejected == false)
										{
											flag = 1;
											break;
										}
									}
								}else{
									flag = 1;
								}
								
								if(flag == 1){
									return true;
								}else{
									return false;
								}
								
							}
							
							$scope.cancelAcceptance = function(){
								
								if($scope.currentKitRow != undefined){
									if($scope.currentKitRow.issuedQuantity > $scope.currentKitRow.acceptedQuantity + $scope.currentKitRow.rejectedQuantity){
										for(var i = 0; i < $scope.subKitList.length; i++){
											$scope.subKitList[i].isAccepted = false;
											$scope.subKitList[i].isRejected = false;
											$scope.subKitList[index].rejectReasonId = null;
											$scope.subKitList[index].rejectReasonNote = null;
										}
									}
								}
							}
							
							
							$scope.isDisabled = function(kitDetails){
								
								if($scope.currentKitRow != undefined){
									if($scope.currentKitRow.issuedQuantity == $scope.currentKitRow.acceptedQuantity + $scope.currentKitRow.rejectedQuantity){
										return true;
									}else {
										return false;
									}
								}else{
									return true;
								}
							}
							
							$scope.acceptRejectKits = function ()
                            {
								
								var finalkitList=$scope.finalKitList;
                            	var URI = BASE_URL + ROOT_URL
								+ ACCEPTREJECTKITS ; 
								var data = {};
								$scope.subKitList=[];
								var flag=false;
								var flagForReason=false;
								angular.forEach($scope.finalKitList,function(value,index){
									if(value.isSelected=='Y'){
										if(value.isAccepted==false && value.isRejected==false)
											flag=true;
										if(value.isRejected==true){
											if(value.rejectReasonId==null || value.rejectReasonNote==null)
												flagForReason=true;
										}
									angular.forEach(value.subKitList,function(value1,index1){
										if(value.kitId==value1.kitId){
											value1.isAccepted=value.isAccepted;
											value1.isRejected = value.isRejected;
											value1.rejectReasonId = value.rejectReasonId;
											value1.rejectReasonNote = value.rejectReasonNote;
											$scope.subKitList.push(value1);
										}
									});
								  }
								});
								
								if(flag==true){
									growl.error('Select either Accept or Reject for all selected kits !!',
											{
												title : 'Error!'
											});
									return false;
								}
								if(flagForReason==true){
									growl.error('Reason and Reason Note is required for kit rejection !!',
											{
												title : 'Error!'
											});
									return false;
								}
								if($scope.subKitList.length==0){
									 growl.warning(
												'No kit is selected!!',
												{
													title : 'Warning!'
												});
									return false;
								}
								$rootScope.startLoader();
								$scope.viewKitIssueObj.subKitList = $scope.subKitList;
								$scope.viewKitIssueObj.organizationId = $scope.organizationId; 
								$scope.viewKitIssueObj.unitId = $scope.unitId;
							GenericService
							.serviceAction("PUT",URI,$scope.viewKitIssueObj)
							.then(
									function(response) {
										if (response.data.status == "success") {
											$('.modal-backdrop').remove();
											NavigateToDetailsKitIndentAcceptance();
											$rootScope.stopLoader();
											growl.success('Kit Accepted Successfully...!!',{title : 'Success!'});
											$scope.$broadcast('show-errors-reset');
										} else {
											$rootScope.stopLoader();
											growl.error('Something went wrong !',{title : 'Error!'});
										}
									});
                            	                         	
                            	
                            }
							
							var NavigateToDetailsKitIndentAcceptance = function(){
								$location.path('/detailsKitIndentAcceptance/'+$stateParams.issueNo);
								$scope.init();
							}
							
							
							
							
							
							
							$scope.acceptKitIssue = function ()
                            {
								var toBeAcceptedKit = {}
								toBeAcceptedKit['id'] = $scope.toBeAcceptedKit.id;
								toBeAcceptedKit['transitionNumber'] = $scope.toBeAcceptedKit.transitionNumber;
								
                            	var URI = BASE_URL + ROOT_URL
								+ ACCEPTKITISSUE + "?organizationId=" + $scope.organizationId + "&unitId="+ $scope.unitId + "&issueNo=" + $stateParams.issueNo + "&transitionNumber=" + toBeAcceptedKit.transitionNumber; 
								var data = {};
                               																																
							GenericService
							.serviceAction("PUT", URI)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Accepted Successfully...!!',{
												title : 'Success!'
											});
											$scope.toBeAcceptedKit['isAccepted'] = true;
											$location.path('/detailsKitIndentAcceptance/'+$stateParams.issueNo);
											$scope.$broadcast('show-errors-reset');
											$scope.init();
										} else {
											growl.error('Something went wrong !',{
												title : 'Error!'
											});
										}
									});
                            	                         	
                            	
                            }
							
							//Save on reject modal
							$scope.rejectKitIssue = function ()
                            {
								var toBeRejectedKit = {}
								toBeRejectedKit['id'] = $scope.toBeRejectedKit.id;
								toBeRejectedKit['transitionNumber'] = $scope.toBeRejectedKit.transitionNumber;
								
								
                            	var URI = BASE_URL + ROOT_URL
								+ REJECTKITISSUE + "?organizationId=" + $scope.organizationId + "&unitId="+ $scope.unitId 
								+ "&issueNo=" + $stateParams.issueNo + "&transitionNumber=" + toBeRejectedKit.transitionNumber + "&rejectReasonId="+ $scope.rejectReasonId 
								+"&quantity=1&rejectionNote=" + $scope.rejectReasonNote; 
								
                            	var data = {};

								
							GenericService
							.serviceAction("PUT", URI)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Rejected successfully...!!',{
												title : 'Success!'
											});
											$scope.toBeRejectedKit['isRejected'] = true;
											$location.path('/detailsKitIndentAcceptance/'+$stateParams.issueNo);
											$scope.$broadcast('show-errors-reset');
											$scope.init();
										} else {
											growl.error('Something went wrong !',{
												title : 'Error!'
											});
										}
									});
                            	
                            	
                            	
                            }
							$scope.init();
							
					     $scope.finalKitList=[];
							$scope.selectKitsToIssue=function(data,index){
								var index = $scope.finalKitList.indexOf(data);
								if(index==-1){
									$scope.finalKitList.push(data);
									index = $scope.finalKitList.indexOf(data);
								}
								if(data.isSelected=='N'){
									$scope.finalKitList.splice(index,1);
								}
							}
						}]);



