angular
		.module('myApp')
		.controller(
				'pendingTransfusionListController',
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
						'$stateParams',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.popUpSaveFalg = true;
							$scope.popUpSaveHistory  = true;
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.transfusionStatus="Pending";
							$scope.transfusionPrecheck=" ";
							$scope.transfusionPrecheckList = [];
							$scope.searchObj={
									
									"fromDate":"",
									"toDate":""
								}; 
							
							
							$scope.searchObj.toDate=new Date();
							$scope.searchObj.fromDate=new Date();
							
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
							
							//***date picker for Record Transfusion START ***//
							$scope.openStartDatePicker = function() {
								$scope.startDatepicker.opened = true;
							};

							$scope.startDatepicker = {
									opened : false
							};
				
							$scope.openEndDatePicker = function() {
								$scope.endDatepicker.opened = true;
							};

							$scope.endDatepicker = {
									opened : false
							};
							
							// function for initializa clockpicker
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							}
							
							///*************vital END****************/
							$scope.recordTransfusionObj = {
									"recordTransfusionId":null,
									"startDate":"",
									"startTimeOfStart":"",
									"startTimeOfHold":"",
									"endTimeOfHold":"",
									"comments":"",
									"suspendTime":"",
									"suspendReason":"",
									"remarks":"",
									"endDate":"",
									"endTimeOfComplete":"",
									"totalVolumeTransfused":"",
									"totalVolumeDiscarded":"",
									"isTransfusionReaction":""
							};
							
							$scope.transfusionHistoryObj = {
									"dateOfTransfusion":"",
									"startTime":"",
									"endTime":"",
									"bloodGroup":"",
									"requisitionNo":"",
									"bagNo":"",
									"suspendTime":"",
									"suspendReason":"",
									"remarks":"",
									"endDate":"",
									"endTimeOfComplete":"",
									"totalVolumeTransfused":"",
									"totalVolumeDiscarded":"",
									"isTransfusionReaction":""
							};
							
							$scope.vitalList = [];
							$scope.enableSaveBtn = function(){
                            	if($scope.transfusionPrecheckList.length>0 && $scope.vitalList.length>0 && $scope.recordTransfusionObj.startDate!=""){
    								$scope.popUpSaveFalg=false;
    							}
                            	console.log("$scope.popUpSaveFalg",$scope.popUpSaveFalg);
                            } 
							
							$scope.initAddVital = function(){
								$scope.vitalObj = {
										"vitalDetailsId":null,
										"eventId":"",
										"eventName":"",
										"date":"",
										"time":"",
										"temperature":"",
										"temperatureType":"",
										"pulse":"",
										"respiration":"",
										"bpSystolic":"",
										"bpDiastolic":"",
										"spo2":"",
										"weight":"",
										"weightType":"",
										"checkIdx":true
											
								};
								
								$scope.vitalList.push($scope.vitalObj);
								console.log("$scope.vitalList",$scope.vitalList);
							}
							
							$scope.initAddVital();
							
							$scope.addVital = function(info){
								
								if(info.eventId=="" || info.date=="" || info.time=="" || info.temperature=="" || info.temperatureType=="" || info.pulse=="" || info.respiration=="" || info.bpSystolic=="" ||
										info.bpDiastolic=="" || info.spo2=="" || info.weight=="" || info.weightType==""){
									
									growl.warning(
											"Please fill all fields ",
											{
												title : 'Warning'
											});
								}else{
								$scope.vitalObj = {
										"vitalDetailsId":null,
										"eventId":"",
										"eventName":"",
										"date":"",
										"time":"",
										"temperature":"",
										"temperatureType":"",
										"pulse":"",
										"respiration":"",
										"bpSystolic":"",
										"bpDiastolic":"",
										"spo2":"",
										"weight":"",
										"weightType":"",
										"checkIdx":true
											
								};
								
								$scope.vitalList.push($scope.vitalObj);
								$scope.enableSaveBtn();
								}
								console.log("$scope.vitalList",$scope.vitalList);
							}
							
							
							
							$scope.openVitalDatePicker = function(index) {

                                for (var i = $scope.vitalList.length - 1; i >= 0; i--) {
                                    if (i === index) {

                                        $scope.vitalDatepicker.opened[i] = true;
                                    } else {
                                        $scope.vitalDatepicker.opened[i] = false;
                                    }
                                }
                            };

                            $scope.vitalDatepicker = {
                                opened : [ false ],

                            };

                            
                            //remove vitals
                            $scope.removeVital = function(info){
								$scope.vitalList.splice(info,1);
								$scope.enableSaveBtn();
								console.log("After remove $scope.vitalList",$scope.vitalList);
							}
                            
                            $scope.addPrechecks = function(precheck,info){
                            	$scope.enableSaveBtn();
	                            if(precheck == true){
	                            	var newdata={};
	                            	newdata.transfusionPrecheck= precheck;
	                            	newdata.transfusionPrecheckMasterId = info.transfusionPrecheckMasterId;
	                            	$scope.transfusionPrecheckList.push(newdata);
	                            	
	                            	console.log("$scope.transfusionPrecheckList",$scope.transfusionPrecheckList);
	                            	
	                            }else {
	                            
	                            	$scope.transfusionPrecheckList.splice(info,1);
	                            	console.log("$scope.transfusionPrecheckList",$scope.transfusionPrecheckList);
	                            }
                            }
                            
							$scope.reasonList = [];
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
							
							$scope.getReasonMasterList();	
							
							$scope.issueBloodBagList = [];
							$scope.getissueBloodBagList = function()
							{
								$scope.startLoader();
								var data={
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										fromDate:moment($scope.searchObj.fromDate).format('YYYY-MM-DD'),
										toDate:moment($scope.searchObj.toDate).format('YYYY-MM-DD')
								};
								var URI = BASE_URL + ROOT_URL+ "/api/bloodbank/issueAcceptance/getListOnPendingTransfusion";
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$scope.stopLoader();
												$scope.issueBloodBagList = response.data.listObject;
												console.log("issueBloodBagList ",$scope.issueBloodBagList);
											}
										});
							}
							
							$scope.getissueBloodBagList();	
							$scope.cuerrnetindex = "";
							
							$scope.transfusionStatusChgange = function(transfusionStatus , inx){
								$scope.cuerrnetindex  = inx;
								if(transfusionStatus == "Initiate"){
									
									 angular.element('#statusModal').modal('show');
								}else if(transfusionStatus == "Suspend"){
									
									 angular.element('#suspendModal').modal('show');
								}
								
							}
							
							$scope.preCheckList = [];
							$scope.getPreCheckList = function()
							{
								var data={
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
								};
								var URI = BASE_URL + ROOT_URL+"/global/bloodBank/precheckMaster/getActiveBloodTransfusionPrecheckMasterList";
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.preCheckList = response.data.listObject;
												console.log("preCheckList ",response.data.listObject);
											}
										});
							}
							
							$scope.getPreCheckList();	
							
							
							
						$scope.transfusionHistoryList = [];	
						$scope.saveInitiateStatusDetails = function(){
							
							
							var historyData = {};
							historyData.startDate = moment($scope.recordTransfusionObj.startDate).format('DD/MM/YYYY');
							historyData.startTimeOfStart = $scope.recordTransfusionObj.startTimeOfStart;
							historyData.endTimeOfComplete = $scope.recordTransfusionObj.endTimeOfComplete;
							historyData.isTransfusionReaction = $scope.recordTransfusionObj.isTransfusionReaction;
							historyData.remarks = $scope.recordTransfusionObj.remarks;
							historyData.bloodGroupName = $scope.issueBloodBagList[$scope.cuerrnetindex].bloodGroupName;
							historyData.requisitionNumber = $scope.issueBloodBagList[$scope.cuerrnetindex].requisitionNumber;
							historyData.issueNo = $scope.issueBloodBagList[$scope.cuerrnetindex].issueNo;
							historyData.bloodBagNo = $scope.issueBloodBagList[$scope.cuerrnetindex].bloodBagNo;
							historyData.bloodComponentDesc = $scope.issueBloodBagList[$scope.cuerrnetindex].bloodComponentDesc;
							historyData.donorBloodGroupName = $scope.issueBloodBagList[$scope.cuerrnetindex].bloodGroupName;
							
							$scope.transfusionHistoryList.push(historyData);
							$scope.popUpSaveHistory  = false;
							$scope.popUpSaveFalg=true;
						}	
							
			}]);
							