'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:bedTypeAllocationERController
 * @description #bedTypeAllocationERController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'bedTypeAllocationERController',
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
						'$timeout',
						'$interval',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$timeout, $interval) {

							// for datePicker

							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};
							$scope.openDatePicker = function() {
								// alert("in");
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
								opened : false
							};

							$scope.openDatePickerForNewDOA = function() {

								$scope.datepickerForNewDOA.opened = true;
							};

							$scope.datepickerForNewDOA = {
								opened : false
							};

							/* init() function for form object create. */
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
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								$scope.itemSelecteds = {};
								$scope.beditemSelecteds = {};
								$scope.bedTypeAllocationEr = {};
								$scope.bedEr = {};

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var data = {

										organizationId : $scope.organizationId,
										unitId : $scope.unitId
								};
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEDOCTORLIST;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.doctorsList = [];

											if (response.data.status == "success") {
												$scope.doctorsList = response.data.listObject;

											}
										});
						

							}

							$scope.init();

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initBedTypeAllocationERList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initBedTypeAllocationERList = function(
									orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETBEDTYPEALLOCATIONERLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTBEDTYPEALLOCATIONRE;

								var data = {
									organizationId : orgId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								}

								// For Get Bed Type Allocation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.bedTypeAllocationList = [];

													if (response.data.status == "success") {
														$scope.bedTypeAllocationERLists = response.data.listObject;
														angular
																.forEach(
																		$scope.bedTypeAllocationERLists,
																		function(
																				value,
																				index) {
																			var newArr = {};
																			newArr.admissionId = value.admissionId;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.genderName = value.genderName;
																			newArr.patientName = value.patientName;
																			newArr.age = value.age;
																			newArr.ageFormat = value.ageFormat;
																			newArr.tpatientId = value.tPatientId;
																			newArr.patientId = value.patientId;
																			newArr.dob = value.dob;
																			newArr.ageString = value.ageString;
																			newArr.doctorId = 0;
																			newArr.triageCategoryId = 0;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.createdDate = value.createdDate;
																			newArr.createdBy = value.createdBy;
																			newArr.admissionNumber = value.admissionNumber;
																			newArr.priorityDesc = value.priorityDesc;
																			newArr.encounterDate = value.encounterDate;
																			newArr.checkPatientSelect = true;

                                                                            newArr.specialityTriage = value.specialityTriage;
																			newArr.triageDetails = value.triageDetails;
																			newArr.specialityTriageId = value.specialityTriageId;


																			$scope.bedTypeAllocationList.push(newArr);
																		});
														console.log("Bed Allocation list", $scope.bedTypeAllocationList);

													}
												});

								// For Count Bed Type Allocation List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console
																.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope
																.setPage(1,
																		false);

													}
												});
							}

							$scope.getBedTypeAllocationERList = function(orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETBEDTYPEALLOCATIONERLIST;
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get Bed Type Allocation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.bedTypeAllocationList = [];

													if (response.data.status == "success") {
														$scope.bedTypeAllocationERLists = response.data.listObject;
														angular
																.forEach(
																		$scope.bedTypeAllocationERLists,
																		function(
																				value,
																				index) {
																			var newArr = {};
																			newArr.admissionId = value.admissionId;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.genderName = value.genderName;
																			newArr.patientName = value.patientName;
																			newArr.age = value.age;
																			newArr.ageFormat = value.ageFormat;
																			newArr.tpatientId = value.tPatientId;
																			newArr.patientId = value.patientId;
																			newArr.dob = value.dob;
																			newArr.ageString = value.ageString;
																			newArr.doctorId = 0;
																			newArr.triageCategoryId = 0;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.createdDate = value.createdDate;
																			newArr.createdBy = value.createdBy;
																			newArr.admissionNumber = value.admissionNumber;
																			newArr.priorityDesc = value.priorityDesc;
																			newArr.encounterDate = value.encounterDate;
																			newArr.checkPatientSelect = true;
																			newArr.specialityTriage = value.specialityTriage;
																			newArr.triageDetails = value.triageDetails;
																			newArr.specialityTriageId = value.specialityTriageId;
																			$scope.bedTypeAllocationList.push(newArr);
																		});
														console
																.log($scope.bedTypeAllocationList);

													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getBedTypeAllocationERList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initBedTypeAllocationERList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAdmittedPatientER();
							}
							$scope.lastIndex = "";
							$scope.count = 0;
							$scope.selectItem = function(item,index) {
								
								angular.forEach($scope.bedTypeAllocationList,function(value,index1){
									if(value.admissionId == item.admissionId){
										value.checkPatientSelect = false;
									 }else{
										 value.checkPatientSelect = true;
									 }
									
								});
								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.bedTypeAllocationEr) {
									$scope.itemSelecteds = item;
								}
							}
							$scope.selectItemBed = function(item) {
								console.log(item);
								$scope.bedItem = item ;
								$scope.beditemSelecteds = [];
								// If checkbox is checked
								if ($scope.bedEr) {
									$scope.beditemSelecteds = item;
								}
							}

							$scope.selectedBedObject = function() {
								if($scope.bedItem  == undefined ){
									growl.warning(
											"Please select bed",
											{
												title : 'Warning'
											});
								}
								console.log($scope.beditemSelecteds);
								angular.element('#bedAllocationForER').modal(
										'hide');
								$('.modal-backdrop').remove()

                                $(document.body).removeClass("modal-open");
								
							}
							
							
							$scope.getERBedPopup = function(id) {
								
								if( id == "" || id == " " || id == undefined)
								{
									return false;
								}

								$('#bedAllocationForER').modal();
								$scope.activeERBedList = [];
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									erCategoryId: id
								}
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEERBEDLIST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.activeERBedList = [];
													if (response.data.status == "success") {
														if(response.data.listObject.length != 0){
														$scope.activeERBedList = response.data.listObject;
														}
														else{
															growl.warning(
																	"No beds available...!!",
																	{
																		title : 'Warning'
																	});
														}

													}
												});

						}
							
							$scope.defaultAllocationSel=function(triaCat){
//								if(triaCat==1){
//									$scope.getERBedPopup(3);
//								}
							}
							
							$scope.saveAdmittedPatientER = function() {
								$rootScope.startLoader();

								console.log($scope.itemSelecteds);
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
									$rootScope.stopLoader();
								}else if ($scope.itemSelecteds.triageCategoryId==undefined ||  $scope.itemSelecteds.triageCategoryId=='' || $scope.itemSelecteds.triageCategoryId == null) {
									growl
									.warning(
											'Please select triage...!!',
											{
												title : 'Warning!'
											});
							$rootScope.stopLoader();
						}else if ($scope.beditemSelecteds.bedId==undefined ||  $scope.beditemSelecteds.bedId=='' || $scope.beditemSelecteds.bedId == null) {
							growl
							.warning(
									'Please select bed...!!',
									{
										title : 'Warning!'
									});
					$rootScope.stopLoader();
				}else {

									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
	                                    $rootScope.stopLoader();
									} else {
//										return false;
										if ($scope.beditemSelecteds.erCategoryId == 1) {
											if (Object.keys($scope.beditemSelecteds).length === 0) {
												growl
														.error(
																'Please select ER bed for patient...!!',
																{
																	title : 'Error!'
																});
												$rootScope.stopLoader();
											} 
											else if($scope.beditemSelecteds.bedId == null && $scope.beditemSelecteds.bedId==''){
												growl .error(
														'Please select ER bed for patient...!!',
														{
															title : 'Error!'
														});
										$rootScope.stopLoader();
											}
											else {
											
												
												$scope.currentDate = new Date();
												$scope.currentDate = moment($scope.currentDate)
														.format('DD-MM-YYYY HH:mm:ss');
												
												  var now = new Date();
												  now.setHours(now.getHours()+4);
												  now.setMinutes(now.getMinutes()+0);
												// alert(moment(now).format('DD-MM-YYYY HH:mm:ss'))
												 //$scope.ProductionDate = now; 
												  
												var data = {
														
												        admissionId : $scope.itemSelecteds.admissionId,
												        admissionDetailsId :$scope.itemSelecteds.admissionDetailsId,
														floorId : $scope.beditemSelecteds.floorId,
														wardId : $scope.beditemSelecteds.wardId,
														roomId : $scope.beditemSelecteds.roomId,
														pdd :  moment(now).format('DD-MM-YYYY HH:mm:ss'),
														doa :  $scope.currentDate,
														bedId : $scope.beditemSelecteds.bedId,
														doctorId : $scope.itemSelecteds.doctorId,
														updatedBy : $scope.userId,
														updatedDate : $scope.currentDate,
														erBedTypeAllocation : $scope.beditemSelecteds.erCategoryId,
														triageCategoryId : $scope.itemSelecteds.triageCategoryId,
														visitTypeId : '4',
														organizationId:$scope.organizationId,
														unitId : $scope.unitId,
														createdBy : $scope.itemSelecteds.createdBy,
														createdDate : $scope.itemSelecteds.createdDate,
														admissionNumber:$scope.itemSelecteds.admissionNumber,
														bedCategoryId  :$scope.beditemSelecteds.bedCategoryId,
														billingBedCategoryId  :$scope.beditemSelecteds.billingBedCategoryId
													};
												
											}
										}
										else
											{
											if (Object.keys($scope.beditemSelecteds).length === 0) {
												growl
														.error(
																'Please select ER bed for patient...!!',
																{
																	title : 'Error!'
																});
												$rootScope.stopLoader();
											} 
											else if($scope.beditemSelecteds.bedId == null && $scope.beditemSelecteds.bedId==''){
												growl .error(
														'Please select bed for patient...!!',
														{
															title : 'Error!'
														});
										$rootScope.stopLoader();
										return false;
											}
											else {
											
												
												$scope.currentDate = new Date();
												$scope.currentDate = moment($scope.currentDate)
														.format('DD-MM-YYYY HH:mm:ss');
												
												  var now = new Date();
												  now.setHours(now.getHours()+4);
												  now.setMinutes(now.getMinutes()+0);
												// alert(moment(now).format('DD-MM-YYYY HH:mm:ss'))
												 //$scope.ProductionDate = now; 
												  
												var data = {
														
												        admissionId : $scope.itemSelecteds.admissionId,
												        admissionDetailsId :$scope.itemSelecteds.admissionDetailsId,
														floorId : $scope.beditemSelecteds.floorId,
														wardId : $scope.beditemSelecteds.wardId,
														roomId : $scope.beditemSelecteds.roomId,
														pdd :  moment(now).format('DD-MM-YYYY HH:mm:ss'),
														doa :  $scope.currentDate,
														bedId : $scope.beditemSelecteds.bedId,
														doctorId : $scope.itemSelecteds.doctorId,
														updatedBy : $scope.userId,
														updatedDate : $scope.currentDate,
														erBedTypeAllocation : $scope.beditemSelecteds.erCategoryId,
														triageCategoryId : $scope.itemSelecteds.triageCategoryId,
														visitTypeId : '4',
														organizationId:$scope.organizationId,
														unitId : $scope.unitId,
														createdBy : $scope.itemSelecteds.createdBy,
														createdDate : $scope.itemSelecteds.createdDate,
														admissionNumber:$scope.itemSelecteds.admissionNumber,
														bedCategoryId  :$scope.beditemSelecteds.bedCategoryId,
														billingBedCategoryId  :$scope.beditemSelecteds.billingBedCategoryId
													};
												
											}
										}
										if($scope.itemSelecteds.patientId > 0)
										{
											data.patientId = $scope.itemSelecteds.patientId;
										}
									else{
										data.tPatientId = $scope.itemSelecteds.tpatientId;
									}
										console.log(data);
										
										var URI = BASE_URL + ROOT_URL +
										 ADDBEDTYPEALLOCATIONER;
										 GenericService .serviceAction("POST",
										 URI, data) .then( function(response) {
											 
										 if (response.data.status == "success") { 
											 growl .success( 'ER Patient Admitted Successfully...!!', {title : 'Success!' }); 
											 $rootScope.stopLoader();
											 $scope.init();
											 }
										 else { 
											 growl .error( 'Something went wrong...!!', { title : 'Error!' }); 
											 $rootScope.stopLoader();
											 }
											  $('.modal-backdrop').remove()
                                              $(document.body).removeClass("modal-open");
											  $state.reload();

                                              $timeout( function(){
                                                         $scope.initBedTypeAllocationERList($scope.organizationId, $scope.offset,$scope.noOfRecordsPerPage);
                                                   },1500);


										//  $state.reload();
										
										 });
										
									}
								}

							}
							
							
							
							$scope.set_color = function(triageCategoryId) {
							    if(triageCategoryId == 1){
							        return {"background-color": "red"};
							    }
							    else if(triageCategoryId  == 2 ){
							        return {"background-color": "red"};
							        
							    }
							    else if(triageCategoryId  == 3 ){
							        return {"background-color": "Yellow"};
							        
							    }
							    else if(triageCategoryId == 4 ){
							        return {"background-color": "Aquamarine"};
							        
							    }
							    else if(triageCategoryId == 5 ){
							        return {"background-color": "Aquamarine"};
							        
							    }else if(triageCategoryId == 6){
							        return {"background-color": "grey"};
							        
							    }
							};



							$scope.mediaRecorder = null;
							$scope.enableAllSaveCalls = true;
							$scope.audioChunks = [];
							$scope.timer = null;
							$scope.seconds = 0;
							$scope.recordTime = "00:00:00";
							$scope.audioURL = null;     
							$scope.audioBase64 = null;   

							// ===== FORMAT TIME =====
							$scope.formatTime = function(sec) {
								var h = String(Math.floor(sec / 3600)).padStart(2, '0');
								var m = String(Math.floor((sec % 3600) / 60)).padStart(2, '0');
								var s = String(sec % 60).padStart(2, '0');
								return h + ":" + m + ":" + s;
							};

							// ===== START RECORDING =====
							$scope.startRecording = async function(admissionId,checkPatientSelect) {
								if(checkPatientSelect==true){
									growl.warning(
											"Please select Encounter",
											{
												title : 'Warning'
											});
									return  false;
								}
									$('#micIcon_'+admissionId).attr('style', 'color: red !important;');

								$scope.enableAllSaveCalls = false;
								const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
								$scope.mediaRecorder = new MediaRecorder(stream);
								$scope.audioChunks = [];
								$scope.audioURL = null;
								$scope.audioBase64 = null;

								$scope.mediaRecorder.ondataavailable = function(e) {
								$scope.audioChunks.push(e.data);
								};

								$scope.mediaRecorder.start();
								$scope.seconds = 0;

								$scope.timer = $interval(function() {
								$scope.seconds++;
								$scope.$applyAsync(function() {
									$scope.recordTime = $scope.formatTime($scope.seconds);
								});
								}, 1000);
							};

							

							$scope.isPaused = false;

							$scope.pauseRecording = function (admissionId,checkPatientSelect) {

							   if(checkPatientSelect==true){
								growl.warning(
											"Please select Encounter",
											{
												title : 'Warning'
											});
									return  false;
								}

								if (!$scope.mediaRecorder) return;

								var $icon = $('#hold_' + admissionId);

								if ($scope.mediaRecorder.state === "recording") {
								
									$scope.mediaRecorder.pause();
									$interval.cancel($scope.timer);
									$scope.isPaused = true;
									$scope.enableAllSaveCalls = true;
									$('#micIcon_'+admissionId).attr('style', 'color: blue !important;');
									 $icon.removeClass('fa-pause').addClass('fa-play');

								} else if ($scope.mediaRecorder.state === "paused") {
								
									$scope.mediaRecorder.resume();

									$scope.timer = $interval(function () {
										$scope.seconds++;
										$scope.recordTime = $scope.formatTime($scope.seconds);
									}, 1000);

									$scope.isPaused = false;
									$scope.enableAllSaveCalls = false;
								
									$('#micIcon_'+admissionId).attr('style', 'color: red !important;');
									 $icon.removeClass('fa-play').addClass('fa-pause');
								}
							};

							// ===== STOP & SAVE =====
							$scope.stopAndSave = function (admissionId,checkPatientSelect) {

								if(checkPatientSelect==true){
									growl.warning(
											"Please select Encounter",
											{
												title : 'Warning'
											});
									return  false;
								}
								$('#micIcon_'+admissionId).attr('style', 'color: blue !important;');
								if (!$scope.mediaRecorder) return;
								$rootScope.startLoader();
								$scope.mediaRecorder.stop();
								$interval.cancel($scope.timer);

								$scope.mediaRecorder.onstop = function () {

									var audioBlob = new Blob($scope.audioChunks, { type: 'audio/webm' });

									// AUDIO PREVIEW (safe apply)
									$scope.audioURL = URL.createObjectURL(audioBlob);

									// CONVERT TO BASE64
									var reader = new FileReader();

									reader.onloadend = function () {
										var base64String = reader.result.split(',')[1];

										$scope.$applyAsync(function () {
											$scope.audioBase64 = base64String;
										});

										console.log("Base64 Audio:", base64String);

										$scope.uploadAudio(base64String);
									};

									reader.readAsDataURL(audioBlob);
								};
							};




							$scope.uploadAudio = function (base64) {
								//   $rootScope.startLoader();

								console.log("Calling API...");
								// $scope.itemSelecteds.triageCategoryId =1;

								$scope.triageStanadard = {
									"Resuscitation":1,
									"Emergent/Immediate":2,
									"Urgent/Delayed":3,
									"Less Urgent/Minimal":4,
									"Non Urgent":5,
									"Dead/Expectant":6
								}

							$.ajax({
							url: "https://aidata.axonichealth.com/analyzeTriageLevel",
							type: "POST",
							contentType: "application/json",
							dataType: "json",
							data: JSON.stringify({
								file: base64,
								triageStanadard:$scope.triageStanadard
							}),

							success: function (res) {
								$scope.$applyAsync(function () {

								console.log("RES ",res);


                                var triageIdAi = res.triageId;
                                if(triageIdAi===2){
                                   triageIdAi=1;
                                }else if(triageIdAi==4){
                                    triageIdAi=5;
                                }
								$scope.itemSelecteds.triageCategoryId = triageIdAi;
								
								
								
								
								// (Optional) reset UI
								$scope.recordTime = "00:00:00";
								$scope.isPaused = false;
								$scope.enableAllSaveCalls = true;

//								if($scope.itemSelecteds.triageCategoryId==1){
//    								$scope.defaultAllocationSel($scope.itemSelecteds.triageCategoryId);
//								}



								});

								$rootScope.stopLoader();
								console.log("SUCCESS", res);
							},

							error: function (err) {
								console.error("ERROR", err);

								$scope.$applyAsync(function () {
								$scope.recordTime = "00:00:00";
								$scope.isPaused = false;
								$scope.enableAllSaveCalls = true;
								});

								$rootScope.stopLoader();							
								
//								if($scope.itemSelecteds.triageCategoryId==1){
//    								$scope.defaultAllocationSel($scope.itemSelecteds.triageCategoryId);
//								}
							}
							});

							};



							$scope.obsTriage = {
								red: {
									bleeding: false,
									convulsions: false,
									shock: false,
									cordProlapse: false,
									fetalHeart: false,
									respiratory: false,
									sepsis: false,
									trauma: false,
									notes: ""
								},
								yellow: {
									contractions: false,
									bleeding: false,
									fever: false,
									vomiting: false,
									abdominalPain: false,
									rom: false,
									fetalMovement: false,
									notes: ""
								},
								green: {
									routine: false,
									irregular: false,
									backPain: false,
									falseLabor: false,
									routineComplaints: false,
									noRedFlag: false,
									notes: ""
								}
								};

								$scope.paediatricTriage = {
									red: {
										respiratory: false,
										unresponsive: false,
										seizures: false,
										shock: false,
										dehydration: false,
										cardiac: false,
										trauma: false,
										allergy: false,
										notes: ""
									},
									yellow: {
										feverInfant: false,
										vomiting: false,
										dehydration: false,
										abdominalPain: false,
										respiratory: false,
										headInjury: false,
										intake: false,
										notes: ""
									},
									green: {
										fever: false,
										cough: false,
										injury: false,
										diarrhea: false,
										rash: false,
										stable: false,
										notes: ""
									}
									};





									$scope.initTriageModels = function () {

										// 🔴 Obstetric
										$scope.obsTriage = {
											red: {
												bleeding: false,
												convulsions: false,
												shock: false,
												cordProlapse: false,
												fetalHeart: false,
												respiratory: false,
												sepsis: false,
												trauma: false,
												notes: ""
											},
											yellow: {
												contractions: false,
												bleeding: false,
												fever: false,
												vomiting: false,
												abdominalPain: false,
												rom: false,
												fetalMovement: false,
												notes: ""
											},
											green: {
												routine: false,
												irregular: false,
												backPain: false,
												falseLabor: false,
												routineComplaints: false,
												noRedFlag: false,
												notes: ""
											}
										};

										// 🔵 Paediatric
										$scope.paediatricTriage = {
											red: {
												respiratory: false,
												unresponsive: false,
												seizures: false,
												shock: false,
												dehydration: false,
												cardiac: false,
												trauma: false,
												allergy: false,
												notes: ""
											},
											yellow: {
												feverInfant: false,
												vomiting: false,
												dehydration: false,
												abdominalPain: false,
												respiratory: false,
												headInjury: false,
												intake: false,
												notes: ""
											},
											green: {
												fever: false,
												cough: false,
												injury: false,
												diarrhea: false,
												rash: false,
												stable: false,
												notes: ""
											}
										};
									};


							$scope.triagePopUpInfo =null;
							$scope.SpecialityTriage = "Paediatric Triage";
							$scope.openPaediatricTriageModal = function(info) {
								console.log("==============> ",info);
								console.log("$scope.paediatricTriage ",$scope.paediatricTriage);
								
								$scope.initTriageModels();
								if(info.specialityTriage ==="Paediatric Triage"){
										$scope.paediatricTriage = JSON.parse(info.triageDetails);
									}
								$scope.SpecialityTriage = "Paediatric Triage";
								$scope.triagePopUpInfo = info;
    							$('#paediatricTriagePopup').modal('show');
								};
							

								$scope.openObstetricTriageModal = function(info) {
									$scope.initTriageModels();
									console.log("==============> ",info);

									if(info.specialityTriage ==="Obstetric Triage"){
										$scope.obsTriage = JSON.parse(info.triageDetails);
									}
									
								$scope.SpecialityTriage = "Obstetric Triage";
								$scope.triagePopUpInfo = info;
    							$('#obstetricTriagePopup').modal('show');
								};


								$scope.submitTriage = function () {
									$rootScope.startLoader();
									console.log("@@@@@@@ ",$scope.triagePopUpInfo);
									// console.log("$scope.SpecialityTriage = "Paediatric Triage"; ");
									
									var data = {
										specialityTriage: $scope.SpecialityTriage,
										admissionDetailsId: $scope.triagePopUpInfo.admissionDetailsId, 
										admissionId: $scope.triagePopUpInfo.admissionId, 
										specialityTriageId: $scope.triagePopUpInfo.specialityTriageId,
										triageDetails: JSON.stringify($scope.SpecialityTriage === "Obstetric Triage" ? $scope.obsTriage : $scope.paediatricTriage)
									};
									console.log("Final Payload:", data);    
    								var URI = BASE_URL + ROOT_URL + "/adt/saveTriageDetails"; 
									GenericService
										.serviceAction("POST", URI, data)
										.then(function (response) {
												$rootScope.stopLoader();



											if (response.data.status == "success") {
												console.log("Saved Successfully");
												$('#obstetricTriagePopup').modal('hide');
											} else {
												console.log("Error:", response.data.message);
											}

											$('#paediatricTriagePopup').modal('hide');
											$('#obstetricTriagePopup').modal('hide');
											$scope.initTriageModels();

											$scope.getBedTypeAllocationERList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);

											$scope.initTriageModels();

										}, function (error) {
											$('#paediatricTriagePopup').modal('hide');
											$('#obstetricTriagePopup').modal('hide');
											console.log("API Error:", error);
											$scope.initTriageModels();
										});
								};

								$scope.closeTriageModel = function () {
									$('#paediatricTriagePopup').modal('hide');
									$('#obstetricTriagePopup').modal('hide');
									$scope.initTriageModels();
								}


								$scope.isPaediatricAge = function(ageStr) {
									if (!ageStr) return false;
									let years = 0, months = 0;
									let yMatch = ageStr.match(/(\d+)Y/);
									let mMatch = ageStr.match(/(\d+)M/);
									if (yMatch) years = parseInt(yMatch[1]);
									if (mMatch) months = parseInt(mMatch[1]);
									if (years === 0 && months < 1) return false;
									if (years > 12) return false;

									return true;
								};

						} ]);
