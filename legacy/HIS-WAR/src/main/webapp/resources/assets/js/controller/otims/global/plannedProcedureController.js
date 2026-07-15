//created by shital nitin
angular
		.module('myApp')
		.controller(
				'plannedProcedureController',
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
						'$timeout',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService, $stateParams,$timeout) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.updateBtnFlag = false;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							
							
							
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
									position : top,
									
								};

								$scope.datepickerOpen = {
									opened : false
								};

								$scope.openDatepicker = function() {
									$scope.datepickerOpen.opened = true;
								};
								
	                            // function for initializa clockpicker
	                            $scope.initClockPicker = function() {

	                                $('.clockpicker').clockpicker({
	                                    placement: 'top',
	                                    autoclose: false,
	                                });
	                            }

	                            $scope.newProcDate = "";
	        					$scope.newProcTime  =  "";
							
							
							
							
	        					if($stateParams.procedureObj.proReqId == null ){
									$state.go('OTIMSdashboard');
									return;
								}
							$scope.procedureObj = $stateParams.procedureObj;
							
						/*	$scope.uhidNum = $scope.procedureObj.uhidNumber;
							$scope.dateOfAdmission =  $scope.procedureObj.doa;
							$scope.patientDischargeDate = $scope.procedureObj.pdd;
							$scope.bedNumber =  $scope.procedureObj.bedNumber;
							$scope.doctorFullName = $scope.procedureObj.doctorName;
							
							*/
							
							$scope.patientDetails.patientName = $scope.procedureObj.patientName;
							$scope.patientDetails.genderCode = $scope.procedureObj.genderCode;
							$scope.patientDetails.age = $scope.procedureObj.age;
							$scope.patientDetails.uhIdNumber = $scope.procedureObj.uhidNumber;
							$scope.patientDetails.birthDate = $scope.procedureObj.dob;
							$scope.patientDetails.dateOfAdmission = $scope.procedureObj.doa;
							$scope.patientDetails.patientDischargeDate = $scope.procedureObj.pdd;
							$scope.patientDetails.bedNumber = $scope.procedureObj.bedNumber;
							$scope.patientDetails.doctorFullName = $scope.procedureObj.doctorName;
							$scope.patientDetails.wardName = $scope.procedureObj.wardName;
							$scope.patientDetails.visitTypeId = $scope.procedureObj.visitTypeId;
							$scope.patientDetails.isFromOtCompletion = true;
							if($scope.patientDetails.visitTypeId!= null && $scope.patientDetails.visitTypeId!=1){
								var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
								var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
							} 
							var splitDate1 = $scope.procedureObj.scheduleDate.split('-');
							var year1  = splitDate1[0];
							var month1 = splitDate1[1];
							var day1 = splitDate1[2];
							$scope.dStartDate = day1+"/"+month1+"/"+year1;
							console.log("procedureObj",$scope.procedureObj);
							
							//get Details by procedureScheduleId
							$scope.procedureDetails = {};
							$scope.procedureOtDetails = [];
							$scope.procedureStaffScheduleList = [];
							$scope.procedureEquipmentScheduleList = [];
							$scope.getDetailsByProcedureScheduleId = function(procedureScheduleId){
								try {
									$scope.startLoader();
									$scope.procedureDetails = {};
									var data = {
											"procedureScheduleId":procedureScheduleId,
											"organizationId" : $scope.organizationId,
											"unitId" : $scope.unitId
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/dashboard/getProcedureSchedulingDetails";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if(response.data.object!=null){
															$scope.procedureDetails = response.data.object;
															
															if($scope.procedureDetails.procedureOTScheduleList!=null && $scope.procedureDetails.procedureOTScheduleList.length>0){
																$scope.procedureOtDetails  = $scope.procedureDetails.procedureOTScheduleList;
															}
															
															if($scope.procedureDetails.procedureStaffScheduleList!=null && $scope.procedureDetails.procedureStaffScheduleList.length>0 ){
																$scope.procedureStaffScheduleList = $scope.procedureDetails.procedureStaffScheduleList;
															}

															if($scope.procedureDetails.procedureEquipmentScheduleList!=null && $scope.procedureDetails.procedureEquipmentScheduleList.length>0){
																$scope.procedureEquipmentScheduleList = $scope.procedureDetails.procedureEquipmentScheduleList;
															}
														}
														
														$rootScope.stopLoader();
														console.log("$scope.procedureDetails",$scope.procedureDetails);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}
							}
							
							
							if($scope.procedureObj.procedureScheduleId!=null){
								$scope.getDetailsByProcedureScheduleId($scope.procedureObj.procedureScheduleId);
							}
							
							$scope.setColor = function(info){
								if(info.color == "red"){
									  return {"background-color": "red"};
								}else if(info.color == "green"){
									  return {"background-color": "#b4da80"};
								}else if(info.color == "gray"){
									  return {"background-color": "gray"};
								}
							}
							
							$scope.confirmSave = function(){
								$rootScope.startLoader();
							
	        					if($scope.newProcDate == ""){
	        						
	        						$rootScope.stopLoader();
	        						growl.warning("Please select Reschedule Date ",
	        								{
	        									title : 'warning!'
	        								});
	        						return false;
	        						
	        					}else if($scope.newProcTime ==""){
	        						$rootScope.stopLoader();
	        						growl.warning("Please select Reschedule Time ",
	        								{
	        									title : 'warning!'
	        								});
	        						return false;
	        						
	        					}else{
	        						
	        						var currTime = moment(new Date()).format('HH:MM');   
	        						var oldprocedureDate = moment($scope.newProcDate).format('YYYY-MM-DD');
	        						var todayDates = moment(new Date()).format('YYYY-MM-DD');
	        					
	        						if(oldprocedureDate == todayDates && currTime > $scope.newProcTime){
	        						
	        							growl.warning("Please select valid Time", {
	        								title : 'warning!'});
	        							$rootScope.stopLoader();
	        							return false;
	        						
	        							}
	        						$rootScope.stopLoader();
		        					$("#confirmSave").modal("show");
	        					}
	        					
	        					
							}
							$scope.goToDashboard = function(){
								$state.go('OTIMSdashboard');
								return;
							}
							
							$scope.rescheduleProcedure = function(){
								
								$scope.param={
										proReqId : $scope.procedureObj.proReqId,
										changeProcedureDate : moment($scope.newProcDate).format('DD-MM-YYYY HH:mm:ss'),
										changeTime : $scope.newProcTime+":00",
										
									};
									$scope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/otims/dashboard/rescheduleProcedure";
									console.log("$scope.param",$scope.param);
									GenericService.serviceAction("POST",URI,$scope.param).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											 $timeout(function () { 
												 $scope.goToDashboard();
											 }, 3000);
										} else if(response.data.status == 'error')
										{
											$scope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
							}
			}]);
		