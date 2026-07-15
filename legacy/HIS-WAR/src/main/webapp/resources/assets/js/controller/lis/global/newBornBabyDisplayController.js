/**
 * @author  Tushar & Sufiyan
 */

angular.module('myApp').controller(
		"newBornBabyDisplayController",
		[ '$scope',
			'$http',
			'$localStorage',
			'$sessionStorage',
			'$cookies',
			'$rootScope',
			'GenericService',
			'BillingGenericService',
			'GenericServiceParamHeader',
			'growl',
			'$state',
			'$timeout',
			'PagerService',
			function($scope, $http, $localStorage, $sessionStorage,
					$cookies, $rootScope, GenericService,BillingGenericService, GenericServiceParamHeader, growl,
					$state, $timeout, PagerService) {
			
					$scope.init = function() {
						var cookieObject = $cookies.getObject('cookieObject');
						if(cookieObject == undefined){
							$state.go('login');
							return;
						}
						
						$scope.unitId = cookieObject.unitId;
						$scope.organizationId = cookieObject.organizationId;
						$scope.userId = cookieObject.userId;

						/** Variables for pagination */
						$scope.offset = 0;
						$scope.noOfRecordsPerPage = 10;
						$scope.newBornDisplayList = [];
						$scope.totalNoOfRecords;
						$scope.searchKeyword;
						$scope.newBornDisplayListCount;

						;

						$rootScope.loginpage = true;
//						$scope.newBornDisplay = {}
						$scope.saveBtnFlag = true;
						$scope.updateBtnFlag = false;
						$scope.saveOrUpdate = true;
						$scope.stillBirthSave = true;
						$scope.myChartForSearch = false
						// for popup
						$scope.showModal = false;
						$scope.buttonClicked = "";
						$scope.popUpFlag = true;
						$scope.assessmentId = 0;
						$scope.setNewBornBabyStillId = 0;
						$scope.admittedPatientList = [];
						$scope.splitBy = " ";
						
						$scope.apgarCheck =[]; 
						
						$scope.defaultApgarRows = 
							[ {apgarId : 0,time:1,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''},
							{apgarId : 0,time:5,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''},
							{apgarId : 0,time:10,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''},
							{apgarId : 0,time:15,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''},
							{apgarId : 0,time:20,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''}];
						
							$scope.defaultApgarRowsDup = 
								[ {apgarId : 0,time:1,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''},
								{apgarId : 0,time:5,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''},
								{apgarId : 0,time:10,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''},
								{apgarId : 0,time:15,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''},
								{apgarId : 0,time:20,colorScore: '',heartRateScore : '',reflexScore : '',muscleScore : '',respirationScore : '',totalScore : 0,apgarRemark : '',reasonForEdit:''}];
							
						
						/*var data = {
							unitId : $scope.unitId,	
							organizationId : $scope.organizationId
						}
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + GETADMITTEDPATIENTLIST;
						console.log("URI", URI,"\nData ",data);
						GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
								$scope.admittedPatientList1 = [];
								if (response.data.status == "success") {
									console.log(response.data.listObject);
									angular.forEach(response.data.listObject, function(info, key) {
										if(info.deliveryFlag==1  && info.genderId==2 &&  info.isLaborWard=='Y'){
											$scope.admittedPatientList1.push(info);
										}
									});
									console.log("admittedPatientList ", $scope.admittedPatientList1);
									$scope.admittedPatientList = $scope.admittedPatientList1;
								}
							});$rootScope.stopLoader();*/
					}
					$scope.init();
					$scope.flagCheckDetail=false;
					/**List And Pagination Start**/
					$scope.setNoOfRecords = function() {
						$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
					};
					
					$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
					{
						
						
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
						var data = {
								unitId : $scope.unitId,	
								organizationId : $scope.organizationId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage
							}
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/adt/getAdmittedPatientListForNewBorn";
							GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
									$scope.admittedPatientList1 = [];
									if (response.data.status == "success") {
										$scope.admittedPatientList1=response.data.listObject;
										$scope.newBornDisplayListCount=response.data.object;
										/*console.log(response.data.listObject);
										angular.forEach(response.data.listObject, function(info, key) {
											if(info.deliveryFlag==1  && info.genderId==2 &&  info.isLaborWard=='Y'){
												$scope.admittedPatientList1.push(info);
											}
										});
										console.log("admittedPatientList ", $scope.admittedPatientList1);
										$scope.admittedPatientList = $scope.admittedPatientList1;*/
										$scope.pager = {};
										$scope.page;
										$scope.setPage(1,false);
									}
									
								});
							$rootScope.stopLoader();
						
					}		
					
					$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
					
						
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var data = {
								unitId : $scope.unitId,	
								organizationId : $scope.organizationId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage
							}
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/adt/getAdmittedPatientListForNewBorn";
							GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
									$scope.admittedPatientList1 = [];
									if (response.data.status == "success") {
										$scope.admittedPatientList1=response.data.listObject;
										$scope.newBornDisplayListCount=response.data.object;
										/*console.log(response.data.listObject);
										angular.forEach(response.data.listObject, function(info, key) {
											if(info.deliveryFlag==1  && info.genderId==2 &&  info.isLaborWard=='Y'){
												$scope.admittedPatientList1.push(info);
											}
										});
										console.log("admittedPatientList ", $scope.admittedPatientList1);
										$scope.admittedPatientList = $scope.admittedPatientList1;*/
									}
								});
							$rootScope.stopLoader();
					}

					$scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						$scope.flagCheckDetail=false;
						if (page < 1 || page > $scope.pager.totalPages)
						{
							return;
						}
						$scope.pager = PagerService.GetPager($scope.newBornDisplayListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
						if (flag)
						{
							$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
						}
					}

					$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
					
					/**List And Pagination End**/
					
					$scope.newBornDisplay = {};
					
					/* code for setting Label */
					$(".selectedPageName").text("New Born Display");
					
					$scope.colorScore = [{score:"0",label: 'Pale or Blue'},{score:"1",label: 'Acrocyanotic'},{score:"2",label: 'Completely Pink'}];
					$scope.heartRateScore = [{score:"0",label: 'Absent'},{score:"1",label: ' < 100 '},{score:"2",label: ' > 100 '}];
					$scope.reflexScore = [{score:"0",label: 'No Response'},{score:"1",label: 'Grimace'},{score:"2",label: 'Cry or Active Withdrawal'}];
					$scope.muscleScore = [{score:"0",label: 'Limp'},{score:"1",label: 'Some Reflexation'},{score:"2",label: 'Active Motion'}];
					$scope.respirationScore = [{score:"0",label: 'Absent'},{score:"1",label: 'Weak Cry (Hypoventilation)'},{score:"2",label: 'Good, Crying'}];					
					
					$scope.getBabyDetailsByParentId = function(info) {
						$scope.flagCheckDetail=true;
						$(".patient_List_Content").hide(1000);
						$(".new-born-registration").show(1000);
						$(".new-born-registration-content").show(1000);
						$("#babyDetails").hide();
						$("#new-born-reg").hide();
						$("#babyDetails tbody").empty();
						$scope.newBornDisplay.fatherName = "";
//						$scope.newBornDisplay.deliveryType = "";
						$scope.admissionId=info.admissionId;
						var data = {
								patientId : $scope.newBornDisplay.recordSelected,
								unitId : $scope.unitId,	
								organizationId : $scope.organizationId
							};
						console.log("data ",data);
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + GETBABYDETAILSBYPARENTID;
						GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
								$rootScope.stopLoader();
								$scope.babyDetails = [];
								console.log(response.data.listObject);
								if (response.data.status == "success") {
									$scope.babyDetails = response.data.listObject;
									if($scope.babyDetails.length != 0){
										$("#babyDetails").show();
										$("#new-born-reg").show();
										$scope.newBornDisplay.fatherName = response.data.listObject[0].fatherName;
//										$scope.newBornDisplay.deliveryType = response.data.listObject[0].deliveryType;
										$('#noOfBabies').val(response.data.listObject[0].numberOfBabies);
									}else{
										$("#babyDetails").hide();
										$("#new-born-reg").hide();
										$("#babyDetails tbody").empty();
										$scope.newBornDisplay.fatherName = "";
//										$scope.newBornDisplay.deliveryType = "";
									}
								}
							});
					}
					
					$scope.getStillBabyDetails = function(value) {
						$scope.stillBirthSaveFlg = true;
						$scope.stillBabyDetails = "";
						$scope.babyUHID = value;
						var data = {
								uhidNumberBaby : value,
								unitId : $scope.unitId,	
								organizationId : $scope.organizationId
							}
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + GETSTILLBABYLIST;
						console.log(data);
						GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
								$scope.stillBabyDetails = [];
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									console.log(response.data.listObject);
									if(response.data.listObject.length > 0){
										$scope.saveBtnFlag = false;
										$scope.stillBirthSaveFlg = false;
									}
									$scope.stillBabyDetails = response.data.listObject;
								}
							$scope.clearStillAssessment();
						});
					}
					
					$scope.clearStillAssessment = function(){
						$scope.newBornDisplay.weightType = "";
						$scope.newBornDisplay.weight = ""; 
						$scope.newBornDisplay.stillType = "";
						$scope.setNewBornBabyStillId = 0;
					}
					
					$scope.setNewBornStillAssessment = function(i) {
						console.log("Object ",i);
						$scope.stillBirthSaveFlg = false;
						$scope.setNewBornBabyStillId = i.newBornBabyStillId;
						$scope.newBornDisplay.stillType = i.stillType;
						var w = i.weight.split($scope.splitBy);
						$scope.newBornDisplay.weight = w[0];
						$scope.newBornDisplay.weightType = w[1];
						$scope.stillBirthUpdateFlg = true;
					}
					
					$scope.updateNewBornStill = function(id) {
						console.log("update data",$scope.newBornDisplay.weightType);
						if($scope.newBornDisplay.weightType == "" || $scope.newBornDisplay.weight == "" || $scope.newBornDisplay.stillType == "" || $scope.setNewBornBabyStillId == 0
								|| id == 0 || $scope.newBornDisplay.weightType == undefined || $scope.newBornDisplay.weight == undefined || $scope.newBornDisplay.stillType == undefined){
							growl.error('Please fill-in All Data ...!!',{title : 'Error!'});
							return;	
						}
						
						//var weightType = $("input:radio[name=weightType]:checked").val();
						var data = {
							newBornBabyStillId : id,
							stillType : $scope.newBornDisplay.stillType,
							weight : $scope.newBornDisplay.weight +$scope.splitBy+ $scope.newBornDisplay.weightType,
							updatedBy : $scope.userId,
							unitId : $scope.unitId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						};
						console.log("update data",data);
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + UPDATESTILLBIRTHASSESSMENT;
						GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
								$rootScope.stopLoader();
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										growl.success('Still Birth Assessment updated Successfully...!!',{title : 'Success!'});
									} else {
										growl.error('Still Birth Assessment already exits...!!',{title : 'Error!'});
									}$scope.getStillBabyDetails($scope.babyUHID);
							});
						$scope.stillBirthUpdateFlg = false;
						$scope.setNewBornBabyStillId = 0;
//						$("#stillBirthRegistation").modal('hide');	
					};
					

					$scope.updateStillBirthAssessment = function() {
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD MM YYYY HH:mm:ss');
						$scope.$broadcast('show errors check validity');
						if($scope.newBornDisplay.weightType == "" || $scope.newBornDisplay.weight == "" || $scope.newBornDisplay.stillType == "" || 
						$scope.newBornDisplay.weightType == undefined || $scope.newBornDisplay.weight == undefined || $scope.newBornDisplay.stillType == undefined){
							growl.error('Please fill-in All Data ...!!',{title : 'Error!'});
							return;	
						}
						var data = {
							newBornBabyStillId : $scope.newBornDisplay.newBornBabyStillId, 
							stillType : $scope.newBornDisplay.stillType,
							weight : $scope.newBornDisplay.weight + $scope.splitBy + $scope.newBornDisplay.weightType,
							updatedBy : $scope.userId,
							unitId : $scope.unitId,
							updatedDate : $scope.currentDate,
							organizationId : $scope.organizationId
						}
						$rootScope.startLoader();
							console.log(data);
						var URI = BASE_URL + ROOT_URL + UPDATESTILLBIRTHASSESSMENT;
						GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									growl.success('Still Birth Assessment updated Successfully...!!',{title : 'Success!'});
								} else {
									growl.error('Still Birth Assessment already exits...!!',{title : 'Error!'});
								}
								$("#stillBirthRegistation").modal('hide');

							});
						$scope.getStillBabyDetails($scope.babyUHID);
					}
					
					//for datepicker Search
					$scope.dateOptions = {
							formatYear : 'yyyy',
							showWeeks : false
						};

						$scope.openDatePickerSearch = function() {
							$scope.datepicker.opened = true;
						};

						$scope.datepicker = {
							opened : false
						};
						
						// Date For From Date on Search
						$scope.openDatePickerFromDate = function() {
								$scope.datepickerFromDate.opened = true;
						};

						$scope.datepickerFromDate = {
								opened : false
						};
						
						// Date For To Date on Search
						$scope.openDatePickerToDate = function() {
								$scope.datepickerToDate.opened = true;
						};

						$scope.datepickerToDate = {
								opened : false
						};
						
						
						  // For Get Patient Vitals List
						 $scope.getVitalDetails = function(uhid){
//							 $scope.babyUHID = uhid;
							 var headerObj = {
										unit_id : $scope.unitId,
										organization_id : $scope.organizationId
									};
							var paramObjForPer = {
									patientId:$scope.newBornDisplay.recordSelected,
									encounterId : $scope.encounterId,
						    		roleId : 1
							};
							 var data = {
										patientId : $scope.newBornDisplay.recordSelected,
										createdDate:moment(new Date()).format('DD MM YYYY HH:mm:ss'),
										updatedDate:moment(new Date()).format('DD MM YYYY HH:mm:ss'),
										updatedBy:$scope.userId,
										createdBy:$scope.userId,
										status:'A',
										roleId:$scope.roleId,
										/*encounterId: $scope.encounterDetailsObj.encounterId,*/
										listOfPatientHabitDetailsDto : $scope.savePatientHabitFinalArr
								  }
							 $rootScope.startLoader();
							var URIV = BASE_URL + ROOT_URL + GETVITALMASTERLIST;
							GenericServiceParamHeader.serviceAction(METHOD_GET, URIV,data, NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
								.then(function(response) {
	                                    console.log(response);
	                                    if (response.data.status == "success")
	                                    	$rootScope.stopLoader();
	                                        //$scope.vitalsList = response.data.listObject;
	                                    	$scope.wholeVitalsList = response.data.listObject;
	                                    $scope.vitalsListD = [];
		                                    angular.forEach($scope.wholeVitalsList, function(value, key) {
		                                    	if(value.vitalId != 5 && value.vitalId != 8 && value.vitalId != 14){
		                                    		var vital = {
		                                    				maxValue: value.maxValue,
		                                    				minValue: value.minValue,
		                                    				remark: value.remark,
		                                    				status: value.status,
		                                    				unit: value.unit,
		                                    				vitalIconPath: value.vitalIconPath,
		                                    				vitalId: value.vitalId,
		                                    				vitalName: value.vitalName,
		                                    				vitalOrderOpd: value.vitalOrderOpd
		                                    		};
		                                    		$scope.vitalsListD.push(vital);
		                                    	}
		                                    });
		                                    console.log("Whole Vital List", $scope.wholeVitalsList);
		                                    $scope.wholeVitalsList = null;
		                                    $scope.vitalsList = $scope.vitalsListD;
	                                    console.log("Vital List", $scope.vitalsList);
	                                });
							$scope.getNewBornAssessment(uhid);
						 	}

						var babyUhid = "";
						
						$scope.saveStillBirthAssessment = function() {
							if($scope.newBornDisplay.stillType == '' ||  $scope.newBornDisplay.stillType == null ||  $scope.newBornDisplay.stillType == undefined){
								growl.warning('Please select type of still birth..!!',{title : 'Warning!'});
								return false;
							}
							else if($scope.newBornDisplay.weight == '' ||  $scope.newBornDisplay.weight == null ||  $scope.newBornDisplay.weight == undefined){
								growl.warning('Please add weight..!!',{title : 'Warning!'});
								return false;
							}
							else if($scope.newBornDisplay.weightType == '' ||  $scope.newBornDisplay.weightType == null ||  $scope.newBornDisplay.weightType == undefined){
								growl.warning('Please add weight Type..!!',{title : 'Warning!'});
								return false;
							}else
							{
								angular.element('#stillBirthRegistation').modal('hide');
							$scope.currentDate = new Date();
							$scope.currentDate = moment($scope.currentDate)
									.format('DD MM YYYY HH:mm:ss');
							console.log("Scope ",$scope.babyUHID);
							$scope.$broadcast('show errors check validity');
							var data = {
								patientId : $scope.newBornDisplay.recordSelected,
								uhidNumberBaby : $scope.babyUHID,
								stillType : $scope.newBornDisplay.stillType,
								weight : $scope.newBornDisplay.weight + $scope.splitBy + $scope.newBornDisplay.weightType,
								status : 'A',
								createdBy : $scope.userId,
								updatedBy : $scope.userId,
								unitId : $scope.unitId,
								organizationId : $scope.organizationId,
								createdDate : $scope.currentDate,
								updatedDate : $scope.currentDate
							};
						$rootScope.startLoader();							
						var URI = BASE_URL + ROOT_URL + SAVESTILLBIRTHASSESSMENT;
						GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
								$rootScope.stopLoader();
								if (response.data.status == "success") {
									growl.success('Still Birth Assessment Added Successfully...!!',{title : 'Success!'});
									$scope.$broadcast('show errors reset');
									$scope.getStillBabyDetails($scope.babyUHID);
								} else {
									growl.error('Still Birth Assessment already exits...!!',{title : 'Error!'});
								}
							});
							}
						}
					
					// function for popup ok button call
					$scope.PopupOkBtn = function() {
						$scope.popUpFlag = false;
						if($scope.assessmentId !=0 ){
							$scope.updateNewBornAssessment();
						}else{
							$scope.saveNewBornAssessment();
						}
					}
						
					$scope.saveOrUpdateAssessment = function(uhId){
//						if($scope.assessmentId !=0 ){
//							$scope.updateNewBornAssessment();
//						}else{
							$scope.saveNewBornAssessment(uhId);
//						}
					}
						
					$scope.saveNewBornAssessment = function(){
						$scope.saveOrUpdate = false;
						if($scope.assessmentId !=0){
							growl.error('Assessment already exist ...!!',	{	title : 'Error!'});return false;
						}	
//						var proceed = true;
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD MM YYYY HH:mm:ss');
						$scope.$broadcast('show errors check validity');
						
						if ($scope.popUpFlag == false) {
							$rootScope.startLoader();
						}
						//value.vitalId != 5 || value.vitalId != 8 || value.vitalId != 14
						 angular.forEach($scope.vitalsList, function(value, key) {
						  		if(value.vitalId == 1) $scope.weight = value.vitalValue;
						  else if (value.vitalId == 2) $scope.height = value.vitalValue;
						  else if (value.vitalId == 3) $scope.bloodPressureSystolic = value.vitalValue;
						  else if (value.vitalId == 4) $scope.pulseRate = value.vitalValue;
						  else if (value.vitalId == 6) $scope.temperature = value.vitalValue;
						  else if (value.vitalId == 7) $scope.bloodPressureDiastolic = value.vitalValue;
						  else if (value.vitalId == 10) $scope.respiration = value.vitalValue;
						  else if (value.vitalId == 15) $scope.headCircumference = value.vitalValue;
						  else return false;
                  });
						//var apgarCount = Number($scope.apgarCount);  
						 
						 $scope.apgarArrList = [];
						 angular.forEach($scope.defaultApgarRows, function(val, key) {
							 console.log("colorScore ",val.colorScore,val.colorScore != "-","\t heartRateScore",val.heartRateScore,val.heartRateScore != "-",
									 "\t reflexScore", val.reflexScore,val.reflexScore != "-","\t muscleScore",val.muscleScore,val.muscleScore != "-",
									 "\t respirationScore ",val.respirationScore,val.respirationScore != "-");
//							 if((val.colorScore != '-' || val.heartRateScore != '-' ||  val.reflexScore != '-' ||  val.muscleScore != '-' || val.respirationScore != '-' )
//									 && !(val.colorScore == '-' && val.heartRateScore == '-' &&  val.reflexScore == '-' &&  val.muscleScore == '-' && val.respirationScore == '-' )){
//									 growl.error('Please Complete Apgar Score for '+val.time +' minute(s) first...!!',	{	title : 'Incomplete Data!'});
//									  proceed = false;return false;
//								 }
							 
							 if((val.colorScore != "" || parseInt(val.colorScore)  == 0) && 
								 (val.heartRateScore != "" || parseInt(val.heartRateScore)  == 0) && 
								 (val.reflexScore != "" || parseInt(val.reflexScore)  == 0) &&
								 (val.muscleScore != "" || parseInt(val.muscleScore)  == 0) && 
								 (val.respirationScore != "" || parseInt(val.respirationScore) == 0)){
								 var total = 0;
								 total = (!isNaN(Number(val.colorScore))?Number(val.colorScore):0) + (!isNaN(Number(val.heartRateScore))?Number(val.heartRateScore):0) 
									 + (!isNaN(Number(val.reflexScore))?Number(val.reflexScore):0) + (!isNaN(Number(val.muscleScore))?Number(val.muscleScore):0) 
									 + (!isNaN(Number(val.respirationScore))?Number(val.respirationScore):0);
								 var apgarData = {
									  colorScore : val.colorScore,
									  heartRateScore : val.heartRateScore,
									  reflexScore : val.reflexScore,
									  muscleScore : val.muscleScore,
									  respirationScore : val.respirationScore,
									  totalScore : total,
									  apgarRemark : val.apgarRemark,
									  time : val.time
								  };
							  $scope.apgarArrList.push(apgarData);
							 }

                        });  
						
						  var dataToSave = {
								uhidNumberBaby : $scope.babyUHID,
								weight : $scope.weight ,
								height : $scope.height ,
								bloodPressureSystolic : $scope.bloodPressureSystolic ,
								pulseRate : $scope.pulseRate ,
								temperature : $scope.temperature ,
								bloodPressureDiastolic: $scope.bloodPressureDiastolic ,
								respiration : $scope.respiration ,
								headCircumference : $scope.headCircumference ,
								apgarArrList : $scope.apgarArrList,
								breastFeeding :  $scope.breastFeeding , 
								transferOfBabyTo :  $scope.transferBaby , 
								cordClamping : $scope.cordClamping ,
								remarks : $scope.remarks , 
								
                     		 	createdDate : $scope.currentDate,
								updatedDate : $scope.currentDate,
								updatedBy : $scope.userId,
								createdBy : $scope.userId,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								status : 'A'
						  };
						  console.log("Äggar List while save ",$scope.apgarArrList);
//						  return false;
//						  if( proceed == false) return false;
						  console.log("Saving .... !",dataToSave);
//						if ($scope.newBornDisplayForm.$valid) {
//							if ($scope.popUpFlag) {
//								$scope.showModal = !$scope.showModal;
//							} else { 
							  var URIA = BASE_URL + ROOT_URL + SAVENEWBORNASSESSMENT;
							BillingGenericService.serviceAction(METHOD_POST, URIA, dataToSave,NOTIFICATION_MSG_STATUS_TRUE)
								.then(function(response) { 
									if (response.data.status == "success") {
										growl.success('Assessment saved Successfully...!!',	{	title : 'Success!'});
										$scope.savePatientVitals();
										$scope.$broadcast('show errors reset');
										
									} else {
										growl.error('Assessment could not save...!!',	{	title : 'Error!'});
									}
									$("#UHIDDetails").modal('hide');
									$scope.init();
								});$scope.saveOrUpdate = true;
//							}
//						}
					}
					

					$scope.getNewBornApgarScore = function (assessmentId){
						$scope.defaultApgarRows = [];
						var data = {assessmentId : assessmentId,	 
									unitId : $scope.unitId,
									organizationId : $scope.organizationId};
						var URIAP = BASE_URL + ROOT_URL + GETNEWBORNAPGARLIST;
						GenericService.serviceAction(METHOD_POST, URIAP ,data)
							.then(function(response) {
                           console.log(response);
                           if (response.data.status == "success"){
                        	   $scope.apgarList = response.data.listObject;
                        	   console.log("raw apgarList ",$scope.apgarList);
                        	   var sequence = [];
                        	   var order = {};
                        	   angular.forEach($scope.defaultApgarRowsDup, function(value, key) {
                        		   sequence.push(value.time);
                        	        var exists = false;
                        	        angular.forEach($scope.apgarList, function(val2, key) {
                        	          if(angular.equals(value.time, val2.time)){ exists = true }; 
                        	        });
                        	        if(exists == false && value.time != "") { $scope.apgarList.push(value); }
                        	      });
                        	   sequence.forEach(function (time, i) { order[time] = i + 1 });console.log("Seq ",sequence);
                        	   $scope.apgarList.sort(function (a, b) { return order[a.time] - order[b.time]; });console.log("order ",order);
                        	   console.log(".apgarList ",$scope.apgarList);
                        	   $scope.defaultApgarRows = $scope.apgarList; 
                           }
	                    });
						$scope.assessmentId = assessmentId;
					}
					
					
				      $scope.getVitalQualifireList = function(vitalId ,index) {
                     	 $rootScope.startLoaderPopup();
							$scope.vitalId =vitalId;
							$scope.vitalIndex=index;
							var data ={
									organizationId:$scope.organizationId,
									vitalId:vitalId
							};
							
							var URI = BASE_URL + ROOT_URL+ "/api/global/getVitalQualifireListByVitalId";
							GenericService.serviceAction("POST",URI,data).then(function(response){
												console.log(response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													$scope.vitalQualifireList = response.data.listObject;
												console.log("vitalQualifireList", $scope.vitalQualifireList);
												 $rootScope.stopLoaderPopup();
											});
						}
						
                     $scope.getListOfQualifiersForPopUp = function(info) {

                     	 $rootScope.startLoaderPopup();
							var data ={
									organizationId:$scope.organizationId,
									vitalId:info.vitalId,
									appointmentVitalMapperId:info.appointmentVitalMapperId
							};
							
							var URI = BASE_URL + ROOT_URL+ "/api/opd/getQualifiersListForPopUp";
							GenericService.serviceAction("POST",URI,data).then(function(response){
												console.log(response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													$scope.listOfQualifiers = response.data.listObject;
												console.log("listOfQualifiers", $scope.listOfQualifiers);
												 $rootScope.stopLoaderPopup();
											});
						}
                     
                     
                     $scope.qulifierOk= function(){
                  	   $scope.selectedVitalQualifierList = {};
                  	   $scope.selectedQualiPosition=[];
							   $scope.selectedQualiMethod=[];
							   $scope.selectedQualiLocation=[];
							   $scope.selectedQualiSite=[];
							   $scope.selectedQualiQuality=[];
							   $scope.selectedQualiCuff=[];
                     }
                     
                 	$scope.selectedQualiPosition=[];
					$scope.selectedQualiMethod=[];
					$scope.selectedQualiLocation=[];
					$scope.selectedQualiSite=[];
					$scope.selectedQualiQuality=[];
					$scope.selectedQualiCuff=[];
					$scope.selectedVitalQualifierList={};
					$scope.qualifierTemperoryList={};
					

					// for position
					$scope.getVitalsQualifierPosition= function(selectedQualiPosition){

						$scope.selectedQualiPosition1=[];
						$scope.vitalPositionList=[];
						$scope.selectedQualiPosition = selectedQualiPosition;
						angular.forEach($scope.selectedQualiPosition,function(value,index){
						angular.forEach($scope.vitalQualifireList,function(value1,index1){
							angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
								if(index == value2.vitalQualifireId)
									{
									var arrNew = {};
									arrNew.vitalQualifireId=value2.vitalQualifireId;
									arrNew.vitalId=value2.vitalId;
									$scope.selectedQualiPosition1.push(arrNew);
									}
							});
						});
						});
						$scope.selectedVitalQualifierList.vitalPositionList=$scope.selectedQualiPosition1;
//						$scope.qualifierTemperoryList.vitalPositionList=$scope.selectedQualiPosition1;
//						$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
						$scope.vitalPositionList=$scope.selectedQualiPosition1;
						$scope.vitalFinalList[$scope.vitalIndex].vitalPositionList =$scope.selectedQualiPosition1;
						console.log("vitalQualifierDate",$scope.vitalFinalList)
					}
					
					
					// for Location
					$scope.getVitalsQualifierLocation= function(selectedQualiLocation){
						
						$scope.selectedQualiLocation1=[];
						$scope.vitalLocationList=[];
						$scope.selectedQualiLocation = selectedQualiLocation;
						angular.forEach($scope.selectedQualiLocation,function(value,index){
						angular.forEach($scope.vitalQualifireList,function(value1,index1){
							angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
								if(index == value2.vitalQualifireId)
									{
									var arrNew = {};
									arrNew.vitalQualifireId=value2.vitalQualifireId;
									arrNew.vitalId=value2.vitalId;
									$scope.selectedQualiLocation1.push(arrNew);
									}
							});
						});
						});
//						$scope.selectedVitalQualifierList.vitalLocationList=$scope.selectedQualiLocation1;
//						$scope.qualifierTemperoryList.vitalLocationList=$scope.selectedQualiLocation1;
//						$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
						
						$scope.vitalLocationList=$scope.selectedQualiLocation1;
						$scope.vitalFinalList[$scope.vitalIndex].vitalLocationList =$scope.selectedQualiLocation1;
						
						console.log("vitalQualifierDate",$scope.vitalFinalList)
					}
					
					
					// for method
					$scope.getVitalsQualifierMethod= function(selectedQualiMethod){
						
						$scope.selectedQualiMethod1=[];
						$scope.vitalMethodList=[];
						$scope.selectedQualiMethod = selectedQualiMethod;
						angular.forEach($scope.selectedQualiMethod,function(value,index){
						angular.forEach($scope.vitalQualifireList,function(value1,index1){
							angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
								if(index == value2.vitalQualifireId)
									{
									var arrNew = {};
									arrNew.vitalQualifireId=value2.vitalQualifireId;
									arrNew.vitalId=value2.vitalId;
									$scope.selectedQualiMethod1.push(arrNew);
									}
							});
						});
						});
						$scope.selectedVitalQualifierList.vitalMethodList=$scope.selectedQualiMethod1;
//						$scope.qualifierTemperoryList.vitalMethodList=$scope.selectedQualiMethod1;
//						$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
						$scope.vitalMethodList=$scope.selectedQualiMethod1;
						$scope.vitalFinalList[$scope.vitalIndex].vitalMethodList =$scope.selectedQualiMethod1;
						console.log("vitalQualifierDate",$scope.vitalFinalList)
					}
					
					
					//for site
					
					$scope.getVitalsQualifierSite= function(selectedQualiSite){
						
						$scope.selectedQualisite1=[];
						$scope.vitalSiteList=[];
						$scope.selectedQualiSite = selectedQualiSite;
						angular.forEach($scope.selectedQualiSite,function(value,index){
						angular.forEach($scope.vitalQualifireList,function(value1,index1){
							angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
								if(index == value2.vitalQualifireId)
									{
									var arrNew = {};
									arrNew.vitalQualifireId=value2.vitalQualifireId;
									arrNew.vitalId=value2.vitalId;
									$scope.selectedQualisite1.push(arrNew);
									}
							});
						});
						});
						$scope.selectedVitalQualifierList.vitalSiteList=$scope.selectedQualisite1;
//						$scope.qualifierTemperoryList.vitalSiteList=$scope.selectedQualisite1;
//						$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
						
						$scope.vitalSiteList=$scope.selectedQualisite1;
						$scope.vitalFinalList[$scope.vitalIndex].vitalSiteList =$scope.selectedQualisite1;
						console.log("vitalQualifierDate",$scope.vitalFinalList)
					}
					
					//for Quality
					
                   $scope.getVitalsQualifierQuality= function(selectedQualiQuality){
						
						$scope.selectedQualiQuality1=[];
						$scope.vitalQualityList=[];
						$scope.selectedQualiQuality = selectedQualiQuality;
						angular.forEach($scope.selectedQualiQuality,function(value,index){
						angular.forEach($scope.vitalQualifireList,function(value1,index1){
							angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
								if(index == value2.vitalQualifireId)
									{
									var arrNew = {};
									arrNew.vitalQualifireId=value2.vitalQualifireId;
									arrNew.vitalId=value2.vitalId;
									$scope.selectedQualiQuality1.push(arrNew);
									}
							});
						});
						});
						$scope.selectedVitalQualifierList.vitalQualityList=$scope.selectedQualiQuality1;
//						$scope.qualifierTemperoryList.vitalQualityList=$scope.selectedQualiQuality1;
//						$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
						
						$scope.vitalQualityList=$scope.selectedQualiQuality1;
						$scope.vitalFinalList[$scope.vitalIndex].vitalQualityList =$scope.selectedQualiQuality1;
						console.log("vitalQualifierDate",$scope.vitalFinalList)
					}
                   
                   
                   
                 //for Cuff
					
                   $scope.getVitalsQualifierCuff= function(selectedQualiCuff){
						
						$scope.selectedQualiCuff1=[];
						$scope.vitalCuffList=[];
						$scope.selectedQualiCuff = selectedQualiCuff;
						angular.forEach($scope.selectedQualiCuff,function(value,index){
						angular.forEach($scope.vitalQualifireList,function(value1,index1){
							angular.forEach(value1.listOfVitalQualifiresMapperForQualifires,function(value2,index2){
								if(index == value2.vitalQualifireId)
									{
									var arrNew = {};
									arrNew.vitalQualifireId=value2.vitalQualifireId;
									arrNew.vitalId=value2.vitalId;
									$scope.selectedQualiCuff1.push(arrNew);
									}
							});
						});
						});
						$scope.selectedVitalQualifierList.vitalCuffList=$scope.selectedQualiCuff1;
//						$scope.qualifierTemperoryList.vitalCuffList=$scope.selectedQualiCuff1;
//						$scope.vitalFinalList[$scope.vitalIndex].selectedVitalQualifierList =$scope.selectedVitalQualifierList;
						$scope.vitalSiteList=$scope.selectedQualisite1;
						$scope.vitalCuffList=$scope.selectedQualiCuff1;
						$scope.vitalFinalList[$scope.vitalIndex].vitalCuffList =$scope.selectedQualiCuff1;
						console.log("vitalQualifierDate",$scope.vitalFinalList)
					}
                   $scope.openDatePickerPostFacto = function() {
                       $scope.datepicker.opened = true;
                   };

                   $scope.datepicker = {
                       opened : false
                   };
                   
                 $scope.dateOptionsPostFacto = {
                           formatYear : 'yyyy',
                           showWeeks : false,
                           maxDate : new Date(),
                           minDate : new Date(),
                       };
                   $scope.isPostFacto='N';
                   $scope.postFactoTime='';
                   
               	$scope.checkValueVital = function(vitalId){
						
						angular.forEach($scope.vitalFinalList,function(value,index1){
						if(vitalId == value.vitalId){
							if(value.vitalValue > 0){
							$scope.vitalFinalList[index1].checkQualifier=false;
							}
						}else{
							$scope.vitalFinalList[index1].checkQualifier=true;
						}
					})
					}
               	
               	
               	$scope.forthTabLevel = 2;
				$scope.setForthTabLevel = function(newTab) {
					$scope.forthTabLevel = newTab;
					if(newTab == 11){
						$("#SaveVital").show();
						$scope.isSetForthTabLevel(11);
					}
					if(newTab == 12){
						if($scope.myChartForSearch == false){
							$("#SaveVital").hide();
							$scope.onClick();
						}else{
							$("#SaveVital").hide();
							$scope.searchVitalByDates();
							$scope.myChartForSearch = true;
//							$scope.onClickSearch();
						}
					}
					if(newTab == 13){
						$("#SaveVital").hide();
						$scope.isSetForthTabLevel(13);
					}
				};
				
				$scope.isSetForthTabLevel = function(tabNum) {
					return $scope.forthTabLevel === tabNum;
				};
				
            	$scope.setThirdTabLevel1 = function(newTab){
					var commonObj = {
							unitId : $scope.unitId,
							organizationId : $scope.organizationId
						};
		    		// For Get Active Reason List
					var URI = BASE_URL + ROOT_URL + GETACTIVEREASONLIST;
					GenericService.serviceAction("POST", URI, commonObj)
							.then( function(response) {
										$scope.reasonList = [];

										if (response.data.status == "success") {
											$scope.reasonList = response.data.listObject;
											console.log("reasonList",$scope.reasonList );
										}
									});
					$scope.getListOfEnteredInErrorVital();
					
					if (newTab == 1) {

				
				var headerObj = {
						unit_id : $scope.unitId,
						organization_id : $scope.organizationId
					};

				var paramObjForPer = {
						patientId:parseInt($scope.newBornDisplay.recordSelected),
						admissionId : $scope.admissionId,
			    		roleId : $scope.roleId
				};
				var data ={};
			    //For BMI Calculation 
				
				 $scope.countervitalstemp = 0;
   	            $scope.countervitalsDia = 0;
   	            $scope.countervitalsRes = 0;
   	            $scope.countervitalsHead = 0;
   	            $scope.countervitalsBlood = 0;
   	            $scope.countervitalsSpO2 = 0;
   	            $scope.countervitalsPulse = 0;
   	            $scope.countervitalsSys = 0;
   	            $scope.Temperature = 0;
   	            
   	            $scope.calculateTemeperature = function(temperature){
   	            	
   	            	 angular.forEach($scope.vitalFinalList, function(value , key)   {
   	            		 if(value.vitalId == 6 && value.unit !='F' )
	                         {
   	            			 if(temperature == 'F'){
   	            				 if(value.vitalValue != ""){
		                        	 $scope.Temperature = value.vitalValue;
		                        	 value.vitalValueShow = ($scope.Temperature *9/5+32).toFixed(2);
   	            				 }else{
   	            					 if(value.vitalValueShow != "" && value.vitalValueShow != undefined ){
   	            					 $scope.Temperature = value.vitalValueShow;
			                        	 value.vitalValue = (($scope.Temperature-32)*5/9).toFixed(2);
   	            					 }
   	            				 }
		                        	 }
   	            			 
       	            		 if(value.vitalId == 6 && value.unitT =='C'){
       	            			 if(value.vitalValueShow != "" && value.vitalValueShow != undefined){
       	            				 value.vitalValue =((value.vitalValueShow-32)*5/9).toFixed(2); 
       	            			 }
       	            		 }
	                         }else if (value.vitalId == 6 && value.unit !='C'){
	                        	 if(temperature == 'C'){
	                        	 if(value.vitalValue != ""){
		                        	 $scope.Temperature = value.vitalValue;
		                        	 value.vitalValueShow =($scope.Temperature-32*5/9).toFixed(2);
	                        	 }
	                        	 else{
	            					 if(value.vitalValueShow != "" && value.vitalValueShow != undefined ){
	            					 $scope.Temperature = value.vitalValueShow;
		                        	 value.vitalValue =( $scope.Temperature *9/5+32).toFixed(2);
	            					 }
	            				 }
	                        	 }
	                        	 if(value.vitalId == 6 && value.unitT == 'F'){
       	            			 if(value.vitalValueShow != "" && value.vitalValueShow != undefined){
       	            				 value.vitalValue =  (value.vitalValueShow * 9/5+32).toFixed(2);
       	            			 }
       	            		 }
	                         }
   	            		
   	            		
   	            	 });
   	            }
   	            $scope.calculateBSA = function(BSA){
   	            	var bsa = 0.00;
   	            	  var height = 0.00;
		                   var weight = 0.00;
   	            	 angular.forEach($scope.vitalFinalList, function(value , key)   {
   	            		 
   	            		 if(value.vitalId ==2)
	                          {
	                            height = value.vitalValue;
	                          }
	                            if(value.vitalId ==1)
	                             {
	                               weight = value.vitalValue;
	                             }
	                            if(height!=null && height!=0 && weight!=null && weight!=0){
	                          angular.forEach($scope.vitalFinalList, function(value , key)   {  
	                        	  if(value.vitalId ==16)
			                         {
			                         if(height!=null && height!=0)
			                         {
                                     if(BSA == 'Mo'){
			                         bsa = math.pow(height * weight/3600,1/2);
			                         console.log("Mosteller",bsa)
                                     }else{
                                   	  var ht = height / 100;
				                         bsa =0.20247 * math.pow(ht,0.725) * math.pow(weight ,0.425);
                                     }

			                         value.vitalValue = parseFloat(bsa).toFixed(3);
			                         }
			                         }
	                          });
	                            }
   	            	 });
   	            	
   	            }
   	            $scope.vitalFlag = false;
   	           
   	            $scope.calculateHeight = function(Height){
   	            	var HeightCalculation = 0.00;
   	            	console.log("ssdsdsdsddsdsad",$scope.unitt);
   	            		 angular.forEach($scope.vitalFinalList, function(value , key)   {
		                         if(value.vitalId == 2 && value.unit == "cm")
		                         {
			                        	 if(Height == 'cm'){
			                        		 $scope.vitalFlag = false;
			                        	 }
			                        	 else if(Height == 'inch'){
			                        		 if(value.vitalValue != ""){
			                        		 $scope.vitalFlag = true;
			                        		 HeightCalculation = value.vitalValue;
			                        	     value.vitalValueShowHeight = (HeightCalculation / 2.54).toFixed(2);
			                        		 }
			                        	 }
			                        	 else if(Height == "foot"){
			                        		 if(value.vitalValue != ""){
			                        		 $scope.vitalFlag = true;
			                        		 HeightCalculation = value.vitalValue;
					                        value.vitalValueShowHeight =( HeightCalculation/30.48).toFixed(2);
			                        		 }
			                        	 }
			                        	 
			                        	 if(value.vitalId == 2 && value.unitT == 'inch'){
			                        		 $scope.vitalFlag = true;
               	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
               	            				 value.vitalValue =  (value.vitalValueShowHeight * 2.54).toFixed(2);
               	            			 }
               	            		 }
			                        	 if(value.vitalId == 2 && value.unitT == 'foot'){
			                        		 $scope.vitalFlag = true;
               	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
               	            				 value.vitalValue =  (value.vitalValueShowHeight * 30.48).toFixed(2);
               	            			 }
               	            		 }
			                        	 
		                         }else if(value.vitalId == 2 && value.unit == "inch"){
			                        	 if(Height == 'inch'){
			                        		 $scope.vitalFlag = false;
			                        	 }
			                        	 else if(Height == 'cm'){
			                        		 if(value.vitalValue != ""){
			                        		 $scope.vitalFlag = true;
			                        		 HeightCalculation = value.vitalValue;
			                        	     value.vitalValueShowHeight = (HeightCalculation *  2.54).toFixed(2);
			                        		 }
			                        	 }
			                        	 else if(Height == "foot"){
			                        		 if(value.vitalValue != ""){
			                        		   $scope.vitalFlag = true;
			                        		   HeightCalculation = value.vitalValue;
					                           value.vitalValueShowHeight = (HeightCalculation/12).toFixed(2);
			                        		 }
			                        	 }
			                        	 if(value.vitalId == 2 && value.unitT == 'cm'){
			                        		 $scope.vitalFlag = true;
               	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
               	            				 value.vitalValue =  (value.vitalValueShowHeight / 2.54).toFixed(2);
               	            			 }
               	            		 }
		                        	 
			                        	 if(value.vitalId == 2 && value.unitT == 'foot'){
			                        		 $scope.vitalFlag = true;
               	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
               	            				 value.vitalValue =  (value.vitalValueShowHeight * 12).toFixed(2);
               	            			 }
               	            		 }
		                         }else if(value.vitalId == 2 && value.unit == "foot"){
		                        	 
			                        	 if(Height == 'foot'){
			                        		  $scope.vitalFlag = false;
			                        	 }
			                        	 else if(Height == 'inch'){
			                        		 if(value.vitalValue != ""){
			                        		  $scope.vitalFlag = true;
			                        		  HeightCalculation = value.vitalValue;
			                        	      value.vitalValueShowHeight = (HeightCalculation * 12).toFixed(2);
			                        		 }
			                        	 }
			                        	 else if(Height == "cm"){
			                        		 if(value.vitalValue != ""){
			                        		   $scope.vitalFlag = true;
			                        		   HeightCalculation = value.vitalValue;
					                           value.vitalValueShowHeight =( HeightCalculation * 30.48).toFixed(2);
			                        		 }
			                        	 }
			                        	 if(value.vitalId == 2 && value.unitT == 'inch'){
			                        		 $scope.vitalFlag = true;
               	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
               	            				 value.vitalValue =  (value.vitalValueShowHeight / 12).toFixed(2);
               	            			 }
               	            		 }
		                        	 
			                        	 if(value.vitalId == 2 && value.unitT == 'cm'){
			                        		 $scope.vitalFlag = true;
               	            			 if(value.vitalValueShowHeight != "" && value.vitalValueShowHeight != undefined){
               	            				 value.vitalValue = (value.vitalValueShowHeight / 30.48).toFixed(2);
               	            			 }
               	            		 }
		                         }
   	            	});
   	            }
   	            $scope.calculateBMInursingInitialAssesment = function()
	                 {
   	            	   var ht = $scope.vitals.height / 100;
   	            	   $scope.vitals.bmi = parseFloat($scope.vitals.weight/(ht * ht)).toFixed(2);
	                 }
				$scope.calculateBMI = function()
                {
                  var height = 0.00;
                  var weight = 0.00;
                  var bmi = 0.00;
                  var bsa = 0.00;
                  
                   angular.forEach($scope.vitalFinalList, function(value , key) 
                   {
                        if(value.vitalId ==2)
                         {
                           height = value.vitalValue;
                         }
                           if(value.vitalId ==1)
                            {
                              weight = value.vitalValue;
                            }
                     });
                     angular.forEach($scope.vitalFinalList, function(value , key) 
		                    {
		                         if(value.vitalId ==8)
		                         {
		                          if(height!=null && height!=0)
		                           {
		                            var ht = height / 100;
		                                     bmi = weight/(ht * ht);
		                                     value.vitalValue = parseFloat(bmi).toFixed(2);
		                                     
			                                     if(bmi <= 18.5){
			                                    	 $scope.bmiStatus='BMI : Underweight';	 
			                                     }
			                                     if(bmi >18.5 && bmi<=24.9){
			                                    	 $scope.bmiStatus='BMI : Normal';	
			                                     }
			                                     if(bmi >=24.9 && bmi<=29.9){
			                                    	 $scope.bmiStatus='BMI : Overweight';	
			                                     }
			                                     if(bmi >=30.0){
			                                    	 $scope.bmiStatus='BMI : Obese';	
			                                     }
		                                     
		                           }
		                         }
		                         
		                         if(value.vitalId ==16)
		                         {
		                         if(height!=null && height!=0)
		                         {

		                         bsa = math.pow(height * weight/3600,1/2);
		                         console.log("Mosteller",bsa)

		                         /* var ht = height / 100;
		                         bsa =0.20247 math.pow(ht,0.725) math.pow(weight ,0.425);*/
		                         console.log("DuBois & DuBois",bsa)

		                         value.vitalValue = parseFloat(bsa).toFixed(3);
		                         }
		                         }
		                        
		                     });
                
                }
				 
				//End BMI Calculation
				$scope.getVitalList = function() {
					$scope.startLoader();
					 var headerObj = {
                             unit_id : $scope.unitId,
                             organization_id : $scope.organizationId
                         };

                     
                     var paramObjForPer = {
                     };
                     var data ={};
					
					var URI = BASE_URL + ROOT_URL
							+ GETVITALMASTERLIST;
					GenericServiceParamHeader
                 .serviceAction(METHOD_GET, URI, data,
                         NOTIFICATION_MSG_STATUS_FALSE,
                         paramObjForPer, headerObj)
							.then(
									function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.vitalsList = response.data.listObject;
										console.log("Vital List", $scope.vitalsList);
										
									});
					$scope.stopLoader();
				}
				$scope.getVitalList();
               
               $rootScope.startLoader();
               $timeout( function(){
               	  $rootScope.stopLoader();
               },5000);
               	
               $timeout( function(){
               	
               	 var paramObjForVital = {
                  				
                       		        unitId:$scope.unitId,
                      				organizationId:$scope.organizationId,
                      				patientId:parseInt($scope.newBornDisplay.recordSelected),
                  		}
               	 var data={
           			 	   unitId:$scope.unitId,
              				   organizationId:$scope.organizationId,
              				   patientId:parseInt($scope.newBornDisplay.recordSelected),
           	 			}
                  		console.log("jj",paramObjForVital);
                  		// return false;
                          var URI = BASE_URL + ROOT_URL
                          + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
                   GenericServiceParamHeader
                       .serviceAction(METHOD_POST, URI,
                               data,
                               NOTIFICATION_MSG_STATUS_FALSE,data, paramObjForVital,headerObj)
                       .then(
                               function(response) {
                                   console.log(response);
                        
                       
                       if (response.data.status == "success")
                           $scope.vitalFinalList = [];
                           $scope.patientViatalList  = response.data.listObject;
                           if($scope.forthTabLevel == 12){
                           	$scope.onClick();
                           	}
                           	// $scope.onClick();
                       console.log("patientVitalList",response.data.listObject);
                       console.log("vitalList",$scope.vitalsList);
                       
                       $scope.vitalsListFOR=[];
						if($scope.showHeadCircuVital == false){
							angular.forEach( $scope.vitalsList, function( value,key) {
								if(value.vitalId != 15){
									$scope.vitalsListFOR.push(value);	
								}
							});
							$scope.vitalsList=[];
							$scope.vitalsList=$scope.vitalsListFOR;
						}
						
                        angular.forEach($scope.vitalsList, function(value , key) {
                       	 var checkFlag = false;
                            angular.forEach($scope.patientViatalList, function(value1 , key1) {
                                	
                           	
                           	 if(value.vitalId == value1.vitalId)
                           		 {
                           		 	checkFlag = true;
                           		 	$scope.newArr = {};
                           		 	$scope.newArr.vitalId = value.vitalId;
                           		 	$scope.newArr.minValue = value.minValue;
                           		 	$scope.newArr.maxValue = value.maxValue;
                           		 	$scope.newArr.vitalName = value.vitalName;
                           		 	$scope.newArr.vitalIconPath = value.vitalIconPath;
                           		 	$scope.newArr.unit = value.unit;
                           		 	$scope.newArr.vitalValue = value1.value;
                           		 	$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
                           		 	$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
										$scope.newArr.checkQualifier = true;
                           		 	if($scope.roleId == 2)
                       		 		{
                           		 		$scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
                       		 		}
                           		 }
                           	
                            		});
												if (checkFlag == true) {
														$scope.vitalFinalList.push($scope.newArr);
													} else {
														// alert("fdvfd");
														var newArr = {};
														newArr.vitalId = value.vitalId;
                                           		 	newArr.minValue = value.minValue;
                                           		 	newArr.maxValue = value.maxValue;
                                           		 	newArr.vitalName = value.vitalName;
                                           		 	newArr.vitalIconPath = value.vitalIconPath;
                                           		 	newArr.unit = value.unit;
                                           		 	newArr.vitalValue = value.vitalValue;
                                           		 	newArr.isReviewedFlag = 'N';
                                           		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
                                           			newArr.checkQualifier = true;
                                           		 	if($scope.roleId == 2)
                                       		 		{
                                           		 		newArr.appointmentVitalMapperId ='';
                                       		 		}
														$scope.vitalFinalList.push(newArr);
													}
                           });
                        console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
                        var datalistVital = $scope.vitalFinalList;
							for (var i = 0; i < datalistVital.length; i++) {
								$scope.vitalFinalList[i].vitalValue = '';
							}
                          });
               },4000);
					
               /*FOR nursing login***/
               
           /*    $timeout( function(){
               	
               	$rootScope.startLoader();
               	 var paramObjForVital = {
                  				
                       		        unitId:$scope.unitId,
                      			 	 organizationId:$scope.organizationId,
                      			 	patientId:parseInt($scope.newBornDisplay.recordSelected),
                  		}
               	 var data={
           			 	   unitId:$scope.unitId,
              				   organizationId:$scope.organizationId,
              				 patientId:parseInt($scope.newBornDisplay.recordSelected),
           	 			}
                  		console.log("jj",paramObjForVital);
                  		// return false;
                          var URI = BASE_URL + ROOT_URL
                          + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
               GenericServiceParamHeader
                   .serviceAction(METHOD_POST, URI,
                           data,
                           NOTIFICATION_MSG_STATUS_FALSE,data, paramObjForVital,headerObj)
                   .then(
                           function(response) {
                               console.log(response);
                    
                   
                   if (response.data.status == "success")
                       $scope.vitalFinalList = [];
                   
                   $scope.vitalsListFOR=[];
					if($scope.showHeadCircuVital == false){
						angular.forEach( $scope.vitalsList, function( value,key) {
							if(value.vitalId != 15){
								$scope.vitalsListFOR.push(value);	
							}
						});
						$scope.vitalsList=[];
						$scope.vitalsList=$scope.vitalsListFOR;
					}
					
                       $scope.patientViatalList  = response.data.listObject;
                   console.log("patientVitalList",response.data.listObject);
                   console.log("vitalList",$scope.vitalsList);
                    angular.forEach($scope.vitalsList, function(value , key) {
                   	 var checkFlag = false;
                        angular.forEach($scope.patientViatalList, function(value1 , key1) {
                            	
                       	
                       	 if(value.vitalId == value1.vitalId)
                       		 {
                       		 	checkFlag = true;
                       		 	$scope.newArr = {};
                       		 	$scope.newArr.vitalId = value.vitalId;
                       		 	$scope.newArr.minValue = value.minValue;
                       		 	$scope.newArr.maxValue = value.maxValue;
                       		 	$scope.newArr.vitalName = value.vitalName;
                       		 	$scope.newArr.vitalIconPath = value.vitalIconPath;
                       		 	$scope.newArr.unit = value.unit;
                       		 	$scope.newArr.vitalValue = value1.value;
                       		 	$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
                       		 	$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
									$scope.newArr.checkQualifier = true;
                       		 	if($scope.roleId == 3)
                   		 		{
                       		 		$scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
                   		 		}
                       		 	
                                  
                       		 }
                       	
                        		});
											if (checkFlag == true) {
													$scope.vitalFinalList.push($scope.newArr);
												} else {
													var newArr = {};
													newArr.vitalId = value.vitalId;
                                       		 	newArr.minValue = value.minValue;
                                       		 	newArr.maxValue = value.maxValue;
                                       		 	newArr.vitalName = value.vitalName;
                                       		 	newArr.vitalIconPath = value.vitalIconPath;
                                       		 	newArr.unit = value.unit;
                                       		 	newArr.vitalValue = value.vitalValue;
                                       		 	newArr.isReviewedFlag = 'N';
                                       		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
                                       			newArr.checkQualifier = true;
                                       		 	if($scope.roleId == 3)
                                   		 		{
                                       		 		newArr.appointmentVitalMapperId ='';
                                   		 		}
													$scope.vitalFinalList.push(newArr);
												}
                   	       
                       });
                    
                  
                    console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
                    var datalistVital = $scope.vitalFinalList;
						for (var i = 0; i < datalistVital.length; i++) {
							$scope.vitalFinalList[i].vitalValue = '';
						}
						 $scope.onClick();
                    $rootScope.stopLoader();                                                        
                           });
           
           },4000);*/
               var top='';
					 
			}
            	}
		
        		$scope.selectedVital=[];
				// Search Vital By Dates
				$scope.myChartForSearch = false;
				$scope.searchVitalByDates = function()
				{
					
					$scope.myChartForSearch = true;
					$("#SaveVital").show();
					$scope.listVitalId=[];
					angular.forEach($scope.selectedVital, function(value , key) {
						$scope.listVitalId.push(value.id);
					});

					$scope.startLoader();
					var data = {
							
							organizationId: $scope.organizationId,
							unitId:$scope.unitId,
							patientId: parseInt($scope.newBornDisplay.recordSelected),
							fromDate:moment($scope.datepickerFromDate).format('YYYY-MM-DD'),
							toDate:moment($scope.datepickerToDate).format('YYYY-MM-DD'),
							listOfSelectedVitals:$scope.listVitalId,
							flag:'N'
					};
					
					var URI = BASE_URL + ROOT_URL
					+ GETAPPOINTMENTVITALMAPPERBYTRAND;
			
					GenericServiceParamHeader
					.serviceAction(METHOD_POST, URI,
							data,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.searcVitalsList = response.data.listObject;
								console.log("searcVitalsList", $scope.searcVitalsList);
								if($scope.searcVitalsList.length > 0){
									  $scope.onClickSearch();
								}
								
							});
				}
				
				$scope.getVitalListMultiselect = function() {
                    $scope.vitalListMultiselect=[];
                    $scope.startLoader();
                    var headerObj = {
                            unit_id : $scope.unitId,
                            organization_id : $scope.organizationId
                        };

                    
                    var paramObjForPer = {
                    		patientId : parseInt($scope.newBornDisplay.recordSelected),
				    	    admissionId : $scope.admissionId,
                            roleId : $scope.roleId
                    };
                    var data ={};
                    
                    var URI = BASE_URL + ROOT_URL
                            + GETVITALMASTERLIST;
                    GenericServiceParamHeader
                            .serviceAction(METHOD_GET, URI, data,
                                    NOTIFICATION_MSG_STATUS_FALSE,
                                    paramObjForPer, headerObj)
                            .then(
                                    function(response) {
                                        console.log(response);
                                        // $rootScope.startLoader();
                                        if (response.data.status == "success")
                                            angular .forEach(response.data.listObject, function( value, index) {
                                                var newAr = {};
                                                newAr.id = value.vitalId;
                                                newAr.label = value.vitalName;
                                                $scope.vitalListMultiselect.push(newAr);
                                                console.log();
                                            });
                                        console.log("$scope.vitalListMultiselect",$scope.vitalListMultiselect);
                                    });
                                        
                                    
                    $scope.stopLoader();
                }
                $scope.getVitalListMultiselect();
				
            	$scope.onClick = function () {
					   $scope.myChartForSearch = false;
					   $scope.vitalnameGraphList=[];
                 	 $scope.vitalname=[];
                 	 $scope.createdTime=[];
     				 $scope.vitalData=[];
     				 $scope.vitalTime=[];
     				 $scope.vitalLabel=[];
     				 //for label
     				 if($scope.patientViatalList.length > 0){
						 angular.forEach($scope.patientViatalList, function(value,key) {
							 $scope.flag=false;
							  if($scope.vitalnameGraphList.length > 0){
									angular.forEach($scope.vitalnameGraphList, function(value3,key) {
										if(value3.vitalName == value.vitalName){
  										$scope.flag = true;
										}
									});
								}
								 if(!$scope.flag){
									var newArr={};
									 newArr.vitalName=value.vitalName;
									 newArr.unitDesc=value.unitDesc;
									 $scope.vitalnameGraphList.push(newArr);
								 }
								 console.log("$scope.vitalnameGraphList",$scope.vitalnameGraphList);
								 
						});
						 //for time array
						 angular.forEach($scope.patientViatalList, function(value,key) {
							 $scope.flagTime=false;
							  if($scope.createdTime.length > 0){
									angular.forEach($scope.createdTime, function(value3,key) {
										if(value3.createdTime1 == value.postFactoTime){
  										$scope.flagTime = true;
										}
									});
								}
								 if(!$scope.flagTime){
									var newArr={};
									newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
									 newArr.createdTime1=value.postFactoTime;
									 newArr.createdDate=value.createdDate;
									 $scope.createdTime.push(newArr);
								 }
								 console.log("$scope.createdTime",$scope.createdTime);
								 
						});
						 
						for (var i = 0; i < $scope.createdTime.length; i++) {
							$scope.vitalLabel.push($scope.createdTime[i].createdTime);
						}
						console.log("graph label",$scope.vitalLabel);
						
						//for data
						
						 angular.forEach($scope.vitalnameGraphList, function(value1,index) {
							$scope.vitalData=[];
							 angular.forEach($scope.patientViatalList, function(value2,index) {
								 
								if(value1.vitalName == value2.vitalName){
									$scope.vitalData.push(value2.value);
									
								}
							 });
							value1.vitalData = $scope.vitalData;
						 });
					
						 
						$scope.colorArray = [];
						 var letters =[ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'] ;  //"0F"; 
					    $scope.colorArray=[ '#803690', '#00ADF9', '#BE2439', '#46BFBD', '#FDB45C', '#006698', '#6A93B0','#992572','#F5FF00','#FF0000','#154889'] ;
						console.log("$scope.vitalname",$scope.vitalnameGraphList);
						console.log("colorArray",$scope.colorArray);
						$scope.dataForDataset=[];
						angular.forEach($scope.vitalnameGraphList, function(value,index) {
							var newArr = {};
							newArr.label = value.vitalName+" ("+ value.unitDesc+")";
							newArr.data = value.vitalData;
							newArr.backgroundColor =$scope.colorArray[index];
							newArr.borderColor =$scope.colorArray[index];
							newArr.fill = false;
							newArr.borderWidth = 2;
							$scope.dataForDataset.push(newArr);
						});
						console.log("$scope.dataForDatasetVital",$scope.dataForDataset);
                   var ctx = document.getElementById('myChart').getContext('2d');
                   var myChart = new Chart(ctx, {
                 	  type: 'line',
                 	  data: {
                 		    labels: $scope.vitalLabel,
                 		    datasets:$scope.dataForDataset
                 		  },
                 		  options: {
                 			  title:{
                 				  display: true,
                 				  text: 'Vital Graph'
                 			  },
                 		        legend: {
                 		        	 display: true,
                 		            position: "top"
                 		        },
                 		        scales: {
                 		        	animation:true,
                 		            yAxes: [{
                 		                ticks: {
                 		                    fontColor: "rgba(0,0,0,0.5)",
                 		                    fontStyle: "bold",
                 		                    beginAtZero: true,
//                 		                    maxTicksLimit: 5,
                 		                    padding: 10
                 		                },
                 		                gridLines: {
                 		                    drawTicks: false,
                 		                    display: false
                 		                }

                 		            }],
                 		            xAxes: [{
                 		                gridLines: {
                 		                    zeroLineColor: "transparent"
                 		                },
                 		                ticks: {
                 		                    padding: 10,
                 		                    fontColor: "rgba(0,0,0,0.5)",
                 		                    fontStyle: "bold"
                 		                }
                 		            }]
                 		        }
                 		    }

                 	});
     				 }
                 };
                 
                 $scope.onClickSearch = function () {
					    $scope.vitalnameGraphListForSearch=[];
                 	 $scope.vitalnameForSearch=[];
                 	 $scope.createdTimeForSearch=[];
     				 $scope.vitalDataForSearch=[];
     				 $scope.vitalTimeForSearch=[];
     				 $scope.vitalLabelForSearch=[];
     				 $scope.createdDate=[];
     				$scope.myChartForSearch = true;
     				 //for label
     				 if($scope.searcVitalsList.length > 0){
						 angular.forEach($scope.searcVitalsList, function(value,key) {
							 $scope.flag=false;
							  if($scope.vitalnameGraphListForSearch.length > 0){
									angular.forEach($scope.vitalnameGraphListForSearch, function(value3,key) {
										if(value3.vitalName == value.vitalName){
  										$scope.flag = true;
										}
									});
								}
								 if(!$scope.flag){
									var newArr={};
									 newArr.vitalName=value.vitalName 
									 newArr.unitDesc=value.unitDesc;
									 $scope.vitalnameGraphListForSearch.push(newArr);
								 }
								 console.log("$scope.vitalnameGraphListForSearch",$scope.vitalnameGraphListForSearch);
								 
						});
						 /*//for time array
						 angular.forEach($scope.searcVitalsList, function(value,key) {
							 $scope.flagTime=false;
							  if($scope.createdTime.length > 0){
									angular.forEach($scope.createdDate, function(value3,key) {
										if(value3.createdDate == value.createdDate){
  										$scope.flagTime = true;
										}
									});
								}
								 if(!$scope.flagTime){
									var newArr={};
									 newArr.createdDate=value.createdDate;
									 $scope.createdDate.push(newArr);
								 }
								 
						});*/
						
						 
						 angular.forEach($scope.searcVitalsList, function(value,key) {
							 $scope.flagTime=false;
							  if($scope.createdTimeForSearch.length > 0){
									angular.forEach($scope.createdTimeForSearch, function(value3,key) {
										if(value3.createdTime1 == value.postFactoTime){
  										$scope.flagTime = true;
										}
									});
								}
								 if(!$scope.flagTime){
									var newArr={};
									 newArr.createdTime=value.createdDate +"  "+value.postFactoTime;
									 newArr.createdTime1=value.postFactoTime;
									 newArr.createdDate=value.createdDate;
									 $scope.createdTimeForSearch.push(newArr);
								 }
								 console.log("$scope.createdTimeForSearch",$scope.createdTimeForSearch);
								 
						});
						for (var i = 0; i < $scope.createdTimeForSearch.length; i++) {
							$scope.vitalLabelForSearch.push($scope.createdTimeForSearch[i].createdTime);
						}
						console.log("graph vitalLabelForSearch",$scope.vitalLabelForSearch);
						
						//for data
						
						 angular.forEach($scope.vitalnameGraphListForSearch, function(value1,index) {
							$scope.vitalDataForSearch=[];
							 angular.forEach($scope.searcVitalsList, function(value2,index) {
								 
								if(value1.vitalName == value2.vitalName){
									$scope.vitalDataForSearch.push(value2.value);
								}
							 });
							value1.vitalDataForSearch = $scope.vitalDataForSearch;
						 });
					
						 
						$scope.colorArray = [];
						$scope.colorArray=[ '#803690', '#00ADF9', '#BE2439', '#46BFBD', '#FDB45C', '#006698', '#6A93B0','#992572','#F5FF00','#FF0000','#154889'] ;
						console.log("$scope.vitalname",$scope.vitalnameGraphList);
						console.log("colorArray",$scope.colorArray);
						$scope.dataForDatasetForSearch=[];
						angular.forEach($scope.vitalnameGraphListForSearch, function(value,index) {
							var newArr = {};
							newArr.label = value.vitalName +"  ("+value.unitDesc+") ";
							newArr.data = value.vitalDataForSearch;
							newArr.backgroundColor =$scope.colorArray[index];
							newArr.borderColor =$scope.colorArray[index];
							newArr.fill = false;
							newArr.borderWidth = 2;
							$scope.dataForDatasetForSearch.push(newArr);
						});
						console.log("$scope.dataForDatasetForSearch",$scope.dataForDatasetForSearch);
                   var ctx = document.getElementById('myChartForSearch').getContext('2d');
                   var myChart = new Chart(ctx, {
                 	  type: 'line',
                 	  data: {
                 		    labels: $scope.vitalLabelForSearch,
                 		    datasets:$scope.dataForDatasetForSearch
                 		  },
                 		  options: {
                 			  title:{
                 				  display: true,
                 				  text: 'Vital Graph'
                 			  },
                 		        legend: {
                 		        	 display: true,
                 		            position: "top"
                 		        },
                 		        scales: {
                 		        	animation:true,
                 		            yAxes: [{
                 		                ticks: {
                 		                    fontColor: "rgba(0,0,0,0.5)",
                 		                    fontStyle: "bold",
                 		                    beginAtZero: true,
//                 		                    maxTicksLimit: 5,
                 		                    padding: 10
                 		                },
                 		                gridLines: {
                 		                    drawTicks: false,
                 		                    display: false
                 		                }

                 		            }],
                 		            xAxes: [{
                 		                gridLines: {
                 		                    zeroLineColor: "transparent"
                 		                },
                 		                ticks: {
                 		                    padding: 10,
                 		                    fontColor: "rgba(0,0,0,0.5)",
                 		                    fontStyle: "bold"
                 		                }
                 		            }]
                 		        }
                 		    }
                 	});
     				 }
                 };
                 
                 
             	
        		 //cancel Vital
        		$scope.cancelVital=function(item){
        			$scope.appointmentVitalMapperId=item.appointmentVitalMapperId;
        			
        		}
        		$scope.updateVitalEnteredInError = function(reason){
        			$scope.reasonId=reason;
        			$rootScope.startLoader();
        			try{
//        				$rootScope.startLoader();
        				var date = new Date();
        				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
        				var data={
        						updatedBy:$scope.userId,
        						updatedDate:$scope.dt,
        						isEnteredInError:'Y',
        						appointmentVitalMapperId:$scope.appointmentVitalMapperId,
        						reasonId:$scope.reasonId,
        						
        				};
        				
        				if(reason != null && reason !="" && reason != undefined){
        				var URI = BASE_URL + ROOT_URL + "/api/opd/updateVitalForEnteredInError";
        				GenericService.serviceAction("POST",URI,data).then(function(response){
        					if (response.data.status == 'success')
        					{
        						$rootScope.stopLoader();
        						growl.success(response.data.message,
        								{
        									title : 'Success!'
        								});
        						  $scope.reason="";
        						  $scope.searchVitalByDates();
        						  $scope.getListOfEnteredInErrorVital();
        						  $scope.setThirdTabLevel1(1);
        						
        					} else if(response.data.status == 'error')
        					{
        						$rootScope.stopLoader();
        						growl.error(response.data.message,
        								{
        									title : 'Error!'
        								});
        					}
        				});
        			}else{
        				growl.warning(" Please enter reason to mark the vital as entered in error.",
        					{
        						title : 'warning!'
        					});
        				$rootScope.stopLoader();
        		}
        			}catch(e){
        				console.log("Exception",e);
        			}
        		}
        		// end cancel
        		
    			//List Of Cancelled Vital
    			$scope.getListOfEnteredInErrorVital = function() {
    				$scope.cancelledDiagnosisList = [];
    				var data = {
    						organizationId:$scope.organizationId,
    	      				unitId:$scope.unitId,
    	      				patientId:parseInt($scope.newBornDisplay.recordSelected),
    				};
    				var URI = BASE_URL + ROOT_URL
    						+ "/api/opd/getListOfEnteredInErrorVitals";
    				GenericServiceParamHeader
    						.serviceAction(METHOD_POST, URI, data,
    								NOTIFICATION_MSG_STATUS_FALSE)
    						.then(
    								function(response) {
    									if (response.data.status == "success") {
    										$scope.enteredInErrorVitalList = response.data.listObject;
    										console.log("enteredInErrorVitalList",$scope.enteredInErrorVitalList);
    									}
    								});
    			}
    			
                 
            	$scope.savePatientVitals = function()
				{
					
					var currtimePost = moment().format("HH:mm");
					if ($scope.popUpFlag == false) {
						$rootScope.startLoader();
					}
					var paramObj = {};
					var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
					};
					$scope.counter = 0;
					$scope.savePatientVitalFinalArr = [];
					  angular.forEach($scope.vitalFinalList, function(value, key) {
						  if(value.vitalValue == "")
							  {
							  $scope.counter++;
							  }
						  
						  var data = {
									vitalId : value.vitalId,
									value : value.vitalValue,
									isReviewedFlag : value.isReviewedFlag,
									vitalMethodList: value.vitalMethodList,
									vitalPositionList: value.vitalPositionList,
									vitalLocationList: value.vitalLocationList,
									vitalSiteList: value.vitalSiteList,
									vitalQualityList: value.vitalQualityList,
									vitalCuffList: value.vitalCuffList,
									
								}
						  if($scope.roleId == 2)
          		 		{
              		 		data.appointmentVitalMapperId =value.appointmentVitalMapperId;
          		 		}
						  
						  $scope.savePatientVitalFinalArr.push(data);
                    });
					  
					  if($scope.counter == $scope.vitalFinalList.length){
						  growl.error("Please Enter at least one vital",
									{
										title : 'Error!'
									});
							$rootScope.stopLoader();  
					  }
					
					  else{
					  var data = {
							patientId : parseInt($scope.newBornDisplay.recordSelected),
				    		 admissionId : $scope.admissionId,
                 		 	roleId : $scope.roleId,
                 		 	createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							updatedBy:$scope.userId,
							createdBy:$scope.userId,
							status:'A',
							isEnteredInError:'N',
							isPostFacto:$scope.isPostFacto,
                            postFactoDate:moment($scope.postFactoDate).format('DD-MM-YYYY HH:mm:ss'),
                            postFactoTime:$scope.postFactoTime!="" ? $scope.postFactoTime+":00" :currtimePost,
                            listAppointmentVitalMapperDtos : $scope.savePatientVitalFinalArr
					  }
					  console.log("Final Vital Save Arr",data);
					  
//					  if ($scope.popUpFlag) {
//							$scope.showModal = !$scope.showModal;
//							$scope.value=1;
//
//						} else {
					 // return false;
					  var URI = BASE_URL + ROOT_URL
						+ SAVEAPPOINTMENTVITALMAPPER + "/"+ $scope.unitId +"/" + $scope.organizationId;
				
						GenericServiceParamHeader
						.serviceAction(METHOD_POST, URI,
								data,
								NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
						.then(
								function(response) {
									console.log(response);
									if (response.data.status == "success")
										{
											
											
											var keepGoing = true;
											  angular.forEach($scope.vitalFinalList, function(value, key) {
												  if(keepGoing) {
													    if(value.vitalId == 3){
													    	
													    	 if(parseInt(value.vitalValue)>=250){
													    		 $scope.isCriticalObj.isCritical='Y';
																  $scope.saveCritical(2);
													    		  keepGoing = false;
													    	 }
													    		 
													    }else if(value.vitalId==7){
													    	if(parseInt(value.vitalValue)<=50){
													    		
													    		$scope.isCriticalObj.isCritical='Y';
																  $scope.saveCritical(2);
													    		  keepGoing = false;
													    	}
													    	
													    }else if(value.vitalId==4){
													    		if(parseInt(value.vitalValue)>=150 || parseInt(value.vitalValue)<=50 ){
													    			$scope.isCriticalObj.isCritical='Y';
																	  $scope.saveCritical(2);
														    		  keepGoing = false;
													    		}
													    	}
													    }
			                                });
											  $scope.isPostFacto='N';
						                         $scope.postFactoTime='';
						                         $scope.postFactoDate='';
											  $scope.setThirdTabLevel1(1);
											  var datalistVital = $scope.vitalFinalList;
												for (var i = 0; i < datalistVital.length; i++) {
													$scope.vitalFinalList[i].vitalValue = '';
												}
											  if($scope.forthTabLevel == 12){
												  $scope.onClick();
												  }
											  $rootScope.stopLoader();
										return response.data.listObject;
										}
								});
//						}
				
					  }
						
					
					
				}
            	
            	
               	
					$scope.getNewBornAssessment = function(uhId){
						 $scope.babyUHID = uhId;
						 $scope.assessmentId = 0;
						 var data = {
								 uhidNumberBaby : uhId,	 
								 unitId : $scope.unitId,
								 organizationId : $scope.organizationId
						 };console.log("getNewBornAssessment ",data);
						 $rootScope.startLoader();
						var URIG = BASE_URL + ROOT_URL + GETNEWBORNASSESSMENTLIST;
						GenericService.serviceAction(METHOD_POST, URIG,data)
							.then(function(response) {
                            console.log("Response ",response);
                            if (response.data.status == "success"){
                            	if(response.data.object != null){
                            		var assessmentId = response.data.object.assessmentId; 
                                	$scope.patientVitals = response.data.object;
                                	$scope.masterVitalsList = $scope.vitalsList ;
                                	$scope.vitalFinalList = [];
                                	$scope.assessmentId = assessmentId==null?0:assessmentId;
                              	  angular.forEach($scope.masterVitalsList, function(value, keys) {
    	                          	var valyew = '';
    	                          		if(value.vitalId == 1) valyew = $scope.patientVitals.weight !=null ? $scope.patientVitals.weight : '';
    									  else if (value.vitalId == 2) valyew = $scope.patientVitals.height !=null ? $scope.patientVitals.height : ''; 
    									  else if (value.vitalId == 3) valyew = $scope.patientVitals.bloodPressureSystolic !=null ? $scope.patientVitals.bloodPressureSystolic : '';
    									  else if (value.vitalId == 4) valyew = $scope.patientVitals.pulseRate !=null ? $scope.patientVitals.pulseRate : '';
    									  else if (value.vitalId == 6) valyew = $scope.patientVitals.temperature !=null ? $scope.patientVitals.temperature : '';
    									  else if (value.vitalId == 7) valyew = $scope.patientVitals.bloodPressureDiastolic !=null ? $scope.patientVitals.bloodPressureDiastolic : '';
    									  else if (value.vitalId == 10) valyew = $scope.patientVitals.respiration !=null ? $scope.patientVitals.respiration : '';
    									  else if (value.vitalId == 15) valyew = $scope.patientVitals.headCircumference !=null ? $scope.patientVitals.headCircumference : '';
    									  else return false;
    	                          		
    	                          		$scope.newArr = {};
    	                    		 	$scope.newArr.vitalId = value.vitalId;
    	                    		 	$scope.newArr.minValue = value.minValue;
    	                    		 	$scope.newArr.maxValue = value.maxValue;
    	                    		 	$scope.newArr.vitalName = value.vitalName;
    	                    		 	$scope.newArr.vitalIconPath = value.vitalIconPath;
    	                    		 	$scope.newArr.unit = value.unit;
    	                    		 	$scope.newArr.vitalValue = valyew;
    	                    		 	$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
    	                    		 	$scope.newArr.checkQualifier = true;
    	                    		 	$scope.vitalFinalList.push($scope.newArr);
    	                    		 	
                              	  });
                                	$scope.remarks = $scope.patientVitals.remarks;
                                	$scope.cordClamping = $scope.patientVitals.cordClamping == 'true' ? 'true' : 'false';
                              	    $scope.breastFeeding = $scope.patientVitals.breastFeeding == 'true' ? 'true' : 'false';
                              	    $scope.transferBaby = $scope.patientVitals.transferOfBabyTo;
                              	    $scope.getNewBornApgarScore(assessmentId);
                              	
                              		$timeout(function () {
                              			$scope.vitalsList = [];
    	                          		$scope.vitalsList = $scope.vitalFinalList;
    	                          		console.log("patient Vital List",$scope.vitalsList);
    	                          	},1000);
                              		
                            	}else{
                            		$scope.defaultApgarRows=$scope.defaultApgarRowsDup;
                            		$scope.remarks = '';
                                	$scope.cordClamping = null;
                              	    $scope.breastFeeding = null;
                              	    $scope.transferBaby = 'CRADLE';
                            		return false;
                            	}
                          		$rootScope.stopLoader();
                            }
                         }); 
					}	
					
					$scope.updateNewBornAssessment = function(){
						$scope.saveOrUpdate = false;
						if($scope.assessmentId == 0){
							growl.error('No Data to Update...!!',	{	title : 'Error!'});return false;}
						
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD MM YYYY HH:mm:ss');
						$scope.$broadcast('show errors check validity');
						
						 angular.forEach($scope.vitalsList, function(value, key) {
							  		if(value.vitalId == 1) $scope.weight = value.vitalValue;
							  else if (value.vitalId == 2) $scope.height = value.vitalValue;
							  else if (value.vitalId == 3) $scope.bloodPressureSystolic = value.vitalValue;
							  else if (value.vitalId == 4) $scope.pulseRate = value.vitalValue;
							  else if (value.vitalId == 6) $scope.temperature = value.vitalValue;
							  else if (value.vitalId == 7) $scope.bloodPressureDiastolic = value.vitalValue;
							  else if (value.vitalId == 10) $scope.respiration = value.vitalValue;
							  else if (value.vitalId == 15) $scope.headCircumference = value.vitalValue;
							  else return false;
						 });
						 /*$scope.apgarArrList = [];
						 angular.forEach($scope.defaultApgarRows, function(val, key) {
							 console.log("colorScore ",val.colorScore,val.colorScore != "","\t heartRateScore",val.heartRateScore,val.heartRateScore != "",
									 "\t reflexScore", val.reflexScore,val.reflexScore != "","\t muscleScore",val.muscleScore,val.muscleScore != "",
									 "\t respirationScore ",val.respirationScore,val.respirationScore != "");
							 
							 if(val.colorScore != "-" && val.heartRateScore != "-" && val.reflexScore != "-" &&  val.muscleScore != "-" && val.respirationScore != "-" ){
								 var total = 0;
								 total = (!isNaN(Number(val.colorScore))?Number(val.colorScore):0) + (!isNaN(Number(val.heartRateScore))?Number(val.heartRateScore):0) 
									 + (!isNaN(Number(val.reflexScore))?Number(val.reflexScore):0) + (!isNaN(Number(val.muscleScore))?Number(val.muscleScore):0) 
									 + (!isNaN(Number(val.respirationScore))?Number(val.respirationScore):0);
								 var apgarData = {
									  colorScore : val.colorScore,
									  heartRateScore : val.heartRateScore,
									  reflexScore : val.reflexScore,
									  muscleScore : val.muscleScore,
									  respirationScore : val.respirationScore,
									  totalScore : total,
									  apgarRemark : val.apgarRemark,
									  time : val.time,
									  apgarId : val.apgarId
								  };
							  $scope.apgarArrList.push(apgarData);
							 }
                        });*/
						 
						  var dataToUpdate = {
								assessmentId : $scope.assessmentId,
								weight : $scope.weight ,
								height : $scope.height ,
								bloodPressureSystolic : $scope.bloodPressureSystolic ,
								pulseRate : $scope.pulseRate ,
								temperature : $scope.temperature ,
								bloodPressureDiastolic: $scope.bloodPressureDiastolic ,
								respiration : $scope.respiration ,
								headCircumference : $scope.headCircumference ,
//								apgarArrList : $scope.apgarArrList,
								breastFeeding :  $scope.breastFeeding, 
								transferOfBabyTo :  $scope.transferBaby, 
								cordClamping : $scope.cordClamping,
								remarks : $scope.remarks, 
								
								updatedDate : $scope.currentDate,
								updatedBy : $scope.userId,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								status : 'A'
						  };
//						  console.log("apgarArrList while updation",$scope.apgarArrList);
						  console.log("Updating .... !",dataToUpdate);return false;
//						if ($scope.newBornDisplayForm.$valid) {
//							if ($scope.popUpFlag) {
//								$scope.showModal = !$scope.showModal;
//							} else { 	  
								$rootScope.startLoader();
							var URIG = BASE_URL + ROOT_URL + UPDATENEWBORNASSESSMENT;
							GenericService.serviceAction(METHOD_POST, URIG,dataToUpdate)
								.then(function(response) {
			                        if (response.data.status == "success"){
			                        	$rootScope.stopLoader();
										growl.success('Assessment Updated Successfully...!!',	{	title : 'Success!'});
										$scope.init();
									} else {
										growl.error('Assessment could not update...!!',	{	title : 'Error!'});
									}
			                        $("#UHIDDetails").modal('hide');
			                        $scope.init();
								});$rootScope.stopLoader();$scope.saveOrUpdate = true;
//							}
//						}	
					}
					
					$scope.updateNewBornApgar = function(apgarId){
						//apgarReason
						var assessmentId = $scope.assessmentId ;
						console.log("apgarId",apgarId);
						if(Number(apgarId)==0|| apgarId == undefined )return false;
						var dataToUpdate = null;
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD MM YYYY HH:mm:ss');
						$scope.$broadcast('show errors check validity');
						  angular.forEach($scope.defaultApgarRows, function(value, key) {
							  if(apgarId == value.apgarId){
								  var total = 0;
									 total = (!isNaN(Number(value.colorScore))?Number(value.colorScore):0) + (!isNaN(Number(value.heartRateScore))?Number(value.heartRateScore):0) 
									 + (!isNaN(Number(value.reflexScore))?Number(value.reflexScore):0) + (!isNaN(Number(value.muscleScore))?Number(value.muscleScore):0) 
									 + (!isNaN(Number(value.respirationScore))?Number(value.respirationScore):0);
		                       	   var apgarObj = {
		                       			apgarId : value.apgarId ,	 
										colorScore : value.colorScore ,
										heartRateScore : value.heartRateScore,
										reflexScore : value.reflexScore,
										muscleScore : value.muscleScore,
										respirationScore : value.respirationScore,
										totalScore : total,
										apgarRemark : value.apgarRemark,
										time : value.time,
	                       			   	updatedBy : $scope.userId,
	    								updatedDate : $scope.currentDate,
	    								organizationId : $scope.organizationId,
	    								unitId : $scope.unitId
	    							};
		                       	dataToUpdate = apgarObj;
							  }
                        });
						  console.log("update data  ",dataToUpdate);
						  $rootScope.startLoader();						
						var URIG = BASE_URL + ROOT_URL + UPDATENEWBORNAPGAR ;
						GenericService.serviceAction(METHOD_POST, URIG,dataToUpdate,NOTIFICATION_MSG_STATUS_TRUE)
							.then(function(response) {
                            if (response.data.status == "success"){
                            	$rootScope.stopLoader();
								growl.success('Apgar Updated Successfully...!!',	{	title : 'Success!'});
								$scope.init();
								$scope.getNewBornApgarScore(assessmentId);
							} else {
								growl.error('Apgar could not update...!!',	{	title : 'Error!'});
							}
						});$rootScope.stopLoader();
					}

					$scope.saveSingleApgarScore = function(assessId,apgarObj){
						console.log("Assess Id = ",assessId);
						
						var tmpObj = {
								colorScore:apgarObj.colorScore,
								heartRateScore:apgarObj.heartRateScore,
								reflexScore:apgarObj.reflexScore,
								muscleScore:apgarObj.muscleScore,
								respirationScore:apgarObj.respirationScore,
								apgarRemark:apgarObj.apgarRemark
						};
						
						if(apgarObj.colorScore == "" ){
							
							growl.warning("color Score is mandatory",{
								title : WARNING_MSG
							});
							return false;
							
						}else if(apgarObj.heartRateScore == ""){

							growl.warning("Heart Rate Score is mandatory",{
								title : WARNING_MSG
							});
							return false;
						}else if(apgarObj.reflexScore == "" ){
							growl.warning("Reflex Score is mandatory",{
								title : WARNING_MSG
							});
							return false;
						}else if(apgarObj.muscleScore == "" ){
							growl.warning("Muscle Score is mandatory",{
								title : WARNING_MSG
							});
							return false;
						}else if(apgarObj.respirationScore == ""){
							growl.warning("Respiration Score is mandatory",{
								title : WARNING_MSG
							});
							return false;
						}else if(apgarObj.apgarRemark == "" ){
							growl.warning("Remark is mandatory",{
								title : WARNING_MSG
							});
							return false;
						}
						/* var isEmpty = !(Object.values(tmpObj).every(x =>((x != null &&  x != ''))));
					     if(isEmpty)
						 {
					    	 growl.warning("All fields are mandatory",{
									title : WARNING_MSG
								});
								return;
						 }*/
					     else{
					     
						if(assessId==0)return false;
						$scope.currentDate = new Date();
						$scope.currentDate = moment($scope.currentDate).format('DD MM YYYY HH:mm:ss');
						$scope.$broadcast('show errors check validity');
						
						var total = 0;
						 total = (!isNaN(Number(apgarObj.colorScore))?Number(apgarObj.colorScore):0) + (!isNaN(Number(apgarObj.heartRateScore))?Number(apgarObj.heartRateScore):0) 
						 + (!isNaN(Number(apgarObj.reflexScore))?Number(apgarObj.reflexScore):0) + (!isNaN(Number(apgarObj.muscleScore))?Number(apgarObj.muscleScore):0) 
						 + (!isNaN(Number(apgarObj.respirationScore))?Number(apgarObj.respirationScore):0);
						
						apgarObj['assessmentId'] = assessId;
                  	   	apgarObj['totalScore'] = total;
						apgarObj['createdDate'] = $scope.currentDate;
						apgarObj['createdBy'] = $scope.userId;
						apgarObj['organizationId'] = $scope.organizationId;
						apgarObj['unitId'] = $scope.unitId;

						delete apgarObj.apgarId;delete apgarObj.reasonForEdit;
						$rootScope.startLoader();
						var URIG = BASE_URL + ROOT_URL + SAVESINGLEAPGARSCORE;
						GenericService.serviceAction(METHOD_POST, URIG,apgarObj,NOTIFICATION_MSG_STATUS_TRUE)
							.then(function(response) {
                            if (response.data.status == "success"){
								growl.success('Apgar Score Saved Successfully...!!',	{	title : 'Success!'});
								$scope.init();$scope.getNewBornApgarScore(assessId);
							} else {
								growl.error('Apgar Score could not save...!!',	{	title : 'Error!'});
							}
						});$rootScope.stopLoader();
					}
					} 
					
					
					$scope.searchPatientInNewBornDetails = function(){
						var keyword =$scope.searchKeyword;
						console.log("searchKeyword == ",$scope.searchKeyword);
						if(keyword!=""){	
							$scope.admittedPatientList1 = [];
							for (var index = 0; index < $scope.admittedPatientList.length; index++) {
								var name = $scope.admittedPatientList[index].pFirstName.toLowerCase();
							    if (name.includes(keyword.toLowerCase())) {
							    	$scope.admittedPatientList1.push($scope.admittedPatientList[index]);
							    }name="";
							}
						}	else{
							$scope.admittedPatientList1 = $scope.admittedPatientList;
						}
					}
					
					
					$scope.checkApgarRows = function(apgarObj){
						var objId = Number(apgarObj.time);
						console.log("Min ",objId);
						if($scope.apgarCheck.length==0){
							$scope.apgarCheck.push(objId);
						}else{
							var prevId = $scope.apgarCheck[$scope.apgarCheck.length-1];
							console.log("prevId ",prevId);
							if(prevId != objId){
								var isComplete = false;
								for (var index = 0; index < $scope.defaultApgarRows.length;index++) {
									console.log("isComplete ",isComplete);
									if($scope.defaultApgarRows[index].time == prevId && isComplete == false){
										var val = $scope.defaultApgarRows[index];
										console.log("Apgar ",val.colorScore, val.heartRateScore, val.reflexScore, val.muscleScore, val.respirationScore);
										if(val.colorScore === '' || val.heartRateScore === '' ||
												val.reflexScore === '' ||  val.muscleScore === '' || val.respirationScore === '' ){
											console.log("Previous Row InCompleted ",prevId);
											growl.error('Please Complete Apgar Score for '+val.time +' minute(s) first...!!',	{	title : 'Incomplete Data!'});
										}else{
											console.log("Previous Row Completed ",prevId);
											isComplete = true;
											index = 0;
											$scope.apgarCheck.push(objId);
										}
									}
									if($scope.defaultApgarRows[index].time == objId && isComplete == false){
										$scope.defaultApgarRows[index].colorScore ="";
										$scope.defaultApgarRows[index].heartRateScore ="";
										$scope.defaultApgarRows[index].reflexScore ="";
										$scope.defaultApgarRows[index].muscleScore ="";
										$scope.defaultApgarRows[index].respirationScore ="";
									}
								}
							}
						}console.log("IDEES ",$scope.apgarCheck);
					}
					
					$scope.clearApgarRow = function(apgarObj){
						var objId = apgarObj.time;
						if($scope.apgarCheck.length > 0){
							var index = $scope.apgarCheck.indexOf(objId);
							  $scope.apgarCheck.splice(index, 1);
						}
						for (var i = 0; i < $scope.defaultApgarRows.length;i++) {
							if($scope.defaultApgarRows[i].time == objId){
								$scope.defaultApgarRows[i].colorScore = "";
								$scope.defaultApgarRows[i].heartRateScore = "";
								$scope.defaultApgarRows[i].reflexScore = "";
								$scope.defaultApgarRows[i].muscleScore = "";
								$scope.defaultApgarRows[i].respirationScore = "";
								$scope.defaultApgarRows[i].apgarRemark = "";
							}
						}
					}
					
					 $scope.checkCriticalIdentificationRange = function(vitalFinalList){

                                if(vitalFinalList.maxValue != null || vitalFinalList.minValue != null){

                                    if(vitalFinalList.maxValue == null){
                                        vitalFinalList.maxValue = 0;
                                    }else if(vitalFinalList.minValue == null){
                                        vitalFinalList.minValue = 0;
                                    }else if(vitalFinalList.vitalValue == ""){
                                        growl.warning(vitalFinalList.vitalName + " should be greater than " + vitalFinalList.minValue + "");
                                    }

                                    var max = Number(vitalFinalList.maxValue);
                                    var min = Number(vitalFinalList.minValue);
                                    var vitalVal = Number(vitalFinalList.vitalValue);

                                    if(max < vitalVal){
                                        growl.warning(vitalFinalList.vitalName + " should be less than " + max + "");
                                       vitalFinalList.vitalValue = "";
                                    }else if(min > vitalVal){
                                        growl.warning(vitalFinalList.vitalName + " should be greater than " + min + "");
                                       vitalFinalList.vitalValue = "";
                                    }
                                }

                            }

					$scope.setTotalScore = function(apgarObj){
						var total = 0;
						total = (!isNaN(Number(apgarObj.colorScore))?Number(apgarObj.colorScore):0) + (!isNaN(Number(apgarObj.heartRateScore))?Number(apgarObj.heartRateScore):0) 
						 + (!isNaN(Number(apgarObj.reflexScore))?Number(apgarObj.reflexScore):0) + (!isNaN(Number(apgarObj.muscleScore))?Number(apgarObj.muscleScore):0) 
						 + (!isNaN(Number(apgarObj.respirationScore))?Number(apgarObj.respirationScore):0);
						return total;
					}
			}]);
