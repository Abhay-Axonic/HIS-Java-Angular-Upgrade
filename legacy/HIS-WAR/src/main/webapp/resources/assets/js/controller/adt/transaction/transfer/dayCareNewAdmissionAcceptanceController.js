angular
.module('myApp')
.controller(
		"dayCareNewAdmissionAcceptanceController",
		[
			'$scope',
			'$rootScope',
			'$state',
			'$cookies',
			'GenericService',
			'PagerService',
			'promiseFactory',
			'$sessionStorage',
			'growl',
			'BillingGenericService',
			function($scope, $rootScope, $state, $cookies,GenericService, PagerService, promiseFactory,$sessionStorage, growl, BillingGenericService) {
				var cookieObject = $cookies
				.getObject('cookieObject');
					if (cookieObject == undefined) {
						$state.go('login');
						return;
					}
					$scope.unitId = cookieObject.unitId;
					$scope.organizationId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					 $scope.searchKeyword="";
					 
					 $scope.nursingWardList = [];
						var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
						 if(nursingStationsWardsList == undefined){
							 growl.warning(
										'No ward mapped with selected nursing station !!',
										{
											title : 'Success!'
										});
						 }
						 else
						  {
						   console.log('nursingStationsWardsList',nursingStationsWardsList);
						   $scope.nursingWardList =nursingStationsWardsList; 
						  
						   console.log('nursingWardList',$scope.nursingWardList);
						   
						 
						
					$scope.init = function() {
					var commonObjForTransfer = {
							unitId : $scope.unitId,
							organizationId : $scope.organizationId,
							wardList : $scope.nursingWardList
						};
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL+"/adt/getListOfDayCarePatientsForNursing";
					GenericService.serviceAction("POST", URI, commonObjForTransfer)
							.then(
									function(response) {
								$scope.newAdmissionList = [];
								$rootScope.stopLoader();
								if (response.data.status == "success") {
									$scope.newAdmissionList = response.data.listObject;
									console.log("$scope.newAdmissionList", $scope.newAdmissionList);
								}
							});
					}
					$scope.init();
					$scope.acceptBtnDisabled = false;
					/*$scope.getAcceptData = function(item)
					{
						$scope.acceptDataItem = item;
					}*/
					
					$scope.getAcceptData = function(item)
					{
						$scope.acceptDataItem = item;
						if(item.encounterId != null){
						$scope.checkPendingServicesOrder();
						}else{
							angular.element('#acceptForPopup').modal('show');
						}
					}
					
					
					$scope.checkService = function(item)
					{
						if($scope.pendingServicesList.length > 0){
							angular.element('#pendingServicesModal').modal('show');
							  return false;
							}else{
					     	angular.element('#acceptForPopup').modal('hide');
					     	$scope.saveNewAdmissionAccept();
							}
					}
					
					
					$scope.selectAllPendingServices=function(selectAllPending){
						if(selectAllPending == 'Y'){
							angular.forEach($scope.pendingServicesList,function(value,index){
								value.checkOption='Y';
							});
							}else{
								angular.forEach($scope.pendingServicesList,function(value,index){
									value.checkOption='N';
								});
							}
						console.log("checkAll",$scope.pendingServicesList);
					}
					
					$scope.checkAllTrue = function() {
						var pendingServicesLength = $scope.pendingServicesList.length;
						$scope.servicesCount=0;
						angular.forEach($scope.pendingServicesList,function(value,index){
							if(value.checkOption == 'Y'){
								$scope.servicesCount++;
							}
						});
						if($scope.servicesCount == pendingServicesLength){
							$scope.selectAllPending = 'Y';
						}else{
							$scope.selectAllPending = 'N';
						}
					};
					
					
					$scope.cancelPendingService = function(item)
					{
						if($scope.pendingServicesList.length > 0){
							angular.element('#pendingServicesModal').modal('hide');
							}
						angular.forEach($scope.pendingServicesList,function(value,index){
							value.checkOption='N';
						});
						$scope.pendingServicesList=[];
						$scope.checkLengthServicesList=[];
						$scope.orderDetailsIds=[];
						$scope.saveNewAdmissionAccept();
						console.log("notRequiredAll",$scope.pendingServicesList);
						
					}
					
					$scope.saveAdmissionAccept= function(){
						
						$scope.orderDetailsIds=[];
						
						angular.forEach($scope.pendingServicesList,function(value,index){
							if(value.checkOption=='Y'){
								$scope.orderDetailsIds.push(value.orderDetailsId); 
							}
						  });
						 if(Object.keys($scope.orderDetailsIds).length >0)
						 {
							 $("#pendingServicesModal").hide();
								$scope.saveNewAdmissionAccept();
						 }else{
							 growl.warning("Please select at least one serivce..!!",{
									title : WARNING_MSG
								});
								return;
						 }
						
					}
					
					$scope.saveNewAdmissionAccept = function()
					{
						$scope.acceptBtnDisabled = true;
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
						.format('DD-MM-YYYY HH:mm:ss');
						
						if($scope.pendingServicesList.length > 0){
							$("#pendingServicesModal").hide();
							
							}
						var data = {
								updatedDate: $scope.currentDate,
								updatedBy:$scope.userId,
								organizationId:$scope.organizationId,	
								unitId:$scope.unitId,
								admissionId:$scope.acceptDataItem.admissionId,
								bedNumber : $scope.acceptDataItem.bedNumber,
								doctorId : $scope.acceptDataItem.doctorId,
								patientId : $scope.acceptDataItem.patientId,
								visitTypeId : $scope.acceptDataItem.visitTypeId,
								orderDetailsIds:$scope.orderDetailsIds
						}
						
						var URI = BASE_URL
						+ ROOT_URL
						+ "/adt/nursing/patient/request/accept";
				GenericService
						.serviceAction("POST", URI,
								data)
						.then(
								function(response) {

									if (response.data.status == "success") {
										growl
												.success(
														'Admission accepted...!!',
														{
															title : 'Success!'
														});
										$scope.init();
										$('#acceptForPopup').hide('hide');
										$(".modal-backdrop").hide();
										$scope.acceptBtnDisabled = false;
										
									} else {
										growl
												.error(
														'Something went wrongs...!!',
														{
															title : 'Error!'
														});
										$scope.acceptBtnDisabled = false;
									}

								});
				
					}
					
					$scope.getRejectionData = function(item)
					{
						var data = {
								organizationId : $scope.organizationId
						};
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEREASONLIST;
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									$scope.reasonList = [];

									if (response.data.status == "success") {
										$scope.reasonList = response.data.listObject;

									}
								});
						$scope.rejectionDataItem = item;
						
						
					}
					$scope.rejectBtnDisabled = false;
					$scope.saveNewAdmissionReject = function()
					{
						if($scope.rejectReasonId > 0){
						$scope.rejectBtnDisabled = true;
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate)
						.format('DD-MM-YYYY HH:mm:ss');
						var data = {
								updatedDate: $scope.currentDate,
								updatedBy:$scope.userId,
								organizationId:$scope.organizationId,	
								unitId:$scope.unitId,
								admissionId:$scope.rejectionDataItem.admissionId,
								nursingRejectionNote:$scope.noteReject,
								bedId : $scope.rejectionDataItem.bedId,
								bedNumber : $scope.rejectionDataItem.bedNumber,
								admissionDetailsId : $scope.rejectionDataItem.admissionDetailsId
						}
						
							var URI = BASE_URL
							+ ROOT_URL
							+ "/adt/nursing/patient/request/reject";
					GenericService
							.serviceAction("POST", URI,
									data)
							.then(
									function(response) {

										if (response.data.status == "success") {
											growl
													.success(
															'Admission Rejected...!!',
															{
																title : 'Success!'
															});
											$scope.init();
											$('#rejectPopup').hide('hide');
											$(".modal-backdrop").hide();
											$scope.rejectBtnDisabled = false;
										} else {
											growl
													.error(
															'Something went wrongs...!!',
															{
																title : 'Error!'
															});
											$scope.rejectBtnDisabled = false;
										}

									});
						}else
						{
						   growl.warning('Please select reason..!!',{title : 'Warning!'});
						}
					}
					
					//sneha
					
					$scope.pendingServicesList = [];
					$scope.checkPendingServicesOrder= function(){
						$rootScope.startLoader();
						var data1={
								encounterId: $scope.acceptDataItem.encounterId,
								visitTypeId : 1
						}
						var URI = BASE_URL + ROOT_URL + "/api/billing/service/preBilling/orderedServices";
						BillingGenericService.serviceAction(METHOD_POST, URI, data1, NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.pendingServicesList = [];
											$scope.checkLengthServicesList = [];
											if (response.data.status == "success") {
												
												$scope.checkLengthServicesList = response.data.listObject;
												console.log("data of pending services",$scope.checkLengthServicesList);
												if($scope.checkLengthServicesList.length > 0){
													//$("#pendingServicesModal").show();
													angular.element('#pendingServicesModal').modal('show');
													$rootScope.stopLoader();
												}else{
														//$("#acceptForPopup").show();
														angular.element('#acceptForPopup').modal('show');
														$rootScope.stopLoader();
												}
												angular.forEach(response.data.listObject,function(value,index){
													value.checkOption='N';
													if(!value.isPackage)
														{
														  $scope.pendingServicesList.push(value);
														}
												});
												//Code change by Ganesh
												//$scope.pendingServicesList = response.data.listObject;
												$rootScope.stopLoader();
												console.log("PENDING service List",$scope.pendingServicesList );
											}

										});
					}
						  }
			} ]);