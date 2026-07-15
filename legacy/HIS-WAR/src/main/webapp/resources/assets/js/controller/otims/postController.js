angular.module('myApp').controller(
				'postController',
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
						'$stateParams',
						'CONSTANTS',
						'GenericServiceParamHeader',
						'GenericServiceFileUpload',
						'$sce',
						'$window',
						'$interval',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$timeout,$stateParams,CONSTANTS,GenericServiceParamHeader,GenericServiceFileUpload, $sce, $window, $interval) {
				
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							var cookieObject, cookieObjectAppConfig;
							[ cookieObject, cookieObjectAppConfig ] = $scope
									.checkCookies();
							
							$scope.organisationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.postCommonObj = $stateParams.postCommonObj;
							$scope.postDetailsObj  = $stateParams.postDetailsObj;
							
							
							$scope.intraCommonObj = $stateParams.intraCommonObj;
							$scope.intraDetailsObj  = $stateParams.intraDetailsObj;
							$scope.resultCommonObj  = $stateParams.resultCommonObj;
							$scope.dateFormat = cookieObjectAppConfig.dateFormat;
							$scope.cimsOption = cookieObjectAppConfig.cimsOption;
							$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
							$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
							$scope.paediatricsSpecialityId = cookieObjectAppConfig.paediatricsSpecialityId;
							$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
							   $scope.labConfig  =    $scope.checkLabConfig();
								var labConfig = $scope.checkLabConfig();
								$scope.outsourceLabId = labConfig.outsourceLabId;
								
							if($scope.postCommonObj.patientName == undefined){
								$state.go('OTIMSdashboard');
								return false;
							}
							$scope.patientDetails = {};
							$scope.patientDetails.patientName = $scope.postCommonObj.patientName;
							$scope.patientDetails.uhIdNumber = $scope.postCommonObj.uhidNumber;
							$scope.patientDetails.wardNameCover = $scope.postCommonObj.wardName;
							$scope.patientDetails.genderCode = $scope.postCommonObj.genderCode;
							$scope.patientDetails.age = $scope.postCommonObj.age;
							$scope.patientDetails.ageStr = $scope.postCommonObj.age;
							$scope.patientDetails.birthDate = $scope.postCommonObj.birthDate;
							$scope.patientDetails.admissionId = $scope.postCommonObj.admissionId;
							$scope.patientDetails.patientId = $scope.postCommonObj.patientId;
							$scope.patientDetails.visitTypeId = $scope.postCommonObj.visitTypeId;
							$scope.patientDetails.isTreatingDoctor = 'Y';
							$scope.patientDetails.genderId = $scope.postCommonObj.genderId;
							if($scope.postCommonObj.doa != null && $scope.postCommonObj.doa !=""){
							$scope.patientDetails.dateOfAdmission = $scope.postCommonObj.doa;
							}
							
							if($scope.postCommonObj.pdd != null && $scope.postCommonObj.pdd !=""){
							$scope.patientDetails.patientDischargeDate = $scope.postCommonObj.pdd;
							}
							
							
							$scope.patientDetails.los = $scope.postCommonObj.los
							$scope.patientDetails.bedNumber = $scope.postCommonObj.bedNumber;
							$scope.patientDetails.doctorFullName = $scope.postCommonObj.doctorName;
							
							
							$scope.ckObj={};
							console.log("$scope.postCommonObj",$scope.postCommonObj);
							console.log("$scope.postDetailsObj",$scope.postDetailsObj);
							$scope.postOpNote="";
						
							$scope.surgeryNotes = [];
							$scope.dischargeNotes = [];
							$scope.surgeryNoteNew = [];
							$scope.templatedropdown = false;
							$scope.getPostOperativeCareData = function(){
								$scope.startLoader();
								var data ={
										proReqId :	$scope.postDetailsObj.proReqId	
								}
								var URI = BASE_URL + ROOT_URL + "/api/otims/PostOperativeDetails/getPostOperativeDetails";
								GenericService.serviceAction(METHOD_POST, URI,data)
									.then(function(response) {
										if (response.data.status == "success") {
											$scope.surgeryNotes = [];
											$scope.dischargeNotes = [];
											$scope.surgeryNoteNew = [];
											if(response.data.object!=null && response.data.object.oTCompletionNoteDtoList!=null && response.data.object.oTCompletionNoteDtoList.length>0){
												$scope.postDetailsObj.postOperativeId  = response.data.object.postOperativeId;
												$scope.postOpNote = response.data.object.postOpNote ;
												console.log("saveNotesList",response.data.object.oTCompletionNoteDtoList);
												angular.forEach(response.data.object.oTCompletionNoteDtoList,function(value,index){
													if(value.noteTypeId == 2){
														$scope.templatedropdown  = true;
														$scope.surgeryNoteNew.push(value);
														$scope.surgeryNotes.push(value);
														$scope.ckObj=value;
													}else if(value.noteTypeId == 3){
														$scope.dischargeNotes.push(value);
													}
												});
											} else {
												$scope.startLoader();
												var URI = BASE_URL + ROOT_URL + "/api/procedure/getNotesListByProConfigId/"+$scope.organizationId+"/"+$scope.unitId+"/"+$scope.postCommonObj.procedureConfigId;
												GenericService.serviceAction(METHOD_GET, URI, "")
													.then(function(response) {
														if (response.data.status == "success") {
															$scope.templatedropdown  = false;
															if(response.data.listObject!=null && response.data.listObject.length>0){
																angular.forEach(response.data.listObject,function(value,index){
																	if(value.noteType == 2){
																		if(index == 0){
																		value.noteTypeId = value.noteType;
																		$scope.surgeryNotes.push(value);
																		$scope.ckObj=value;
																		}
																		$scope.surgeryNoteNew.push(value);
																	}else if(value.noteType == 3){
																		value.noteTypeId = value.noteType;
																		$scope.dischargeNotes.push(value);
																	}
																});
															} 
														}
														$scope.stopLoader();
													});
											}
										}
										$scope.stopLoader();
									});
							}
							
							if($scope.postDetailsObj.proReqId!=null && $scope.postDetailsObj.proReqId!=undefined ){
								$scope.getPostOperativeCareData();
							}
														// save postOperative
														// Details
							
							$scope.savePostOperativeDetailsSubmit = function() {
								$rootScope.startLoader();
								$scope.oTCompletionNoteList = [];
								if ($scope.surgeryNotes != null
										&& $scope.surgeryNotes.length > 0) {
									angular.forEach( $scope.surgeryNotes, function(value, index) {
														var newdata = {}
														newdata.otCompletionNoteId = value.otCompletionNoteId!=undefined ? value.otCompletionNoteId : null,
														newdata.description = value.description;
														newdata.template = value.template;
														newdata.noteMasterId = value.noteMasterId;
														newdata.noteTypeId = value.noteType != null ? value.noteType : value.noteTypeId;
														newdata.organizationId = $scope.organizationId;
														newdata.unitId = $scope.unitId;
														newdata.createdBy = $scope.userId;
														newdata.updatedBy = $scope.userId;
														newdata.status = 'A';
														newdata.createdDate = moment( new Date()).format('DD-MM-YYYY HH:mm:ss');
														newdata.updatedDate = moment( new Date()).format('DD-MM-YYYY HH:mm:ss');
														$scope.oTCompletionNoteList.push(newdata);
													});
								}

								/*if ($scope.dischargeNotes != null
										&& $scope.dischargeNotes.length > 0) {
									angular.forEach($scope.dischargeNotes,
													function(value,index) {
														var newdata = {};
														newdata.otCompletionNoteId = value.otCompletionNoteId!=undefined ? value.otCompletionNoteId : null,
														newdata.description = value.description;
														newdata.template = value.template;
														newdata.noteMasterId = value.noteMasterId;
														newdata.noteTypeId = value.noteTypeId;
														newdata.organizationId = $scope.organizationId;
														newdata.unitId = $scope.unitId;
														newdata.createdBy = $scope.userId;
														newdata.updatedBy = $scope.userId;
														newdata.status = 'A';
														newdata.createdDate = moment( new Date()).format( 'DD-MM-YYYY HH:mm:ss');
														newdata.updatedDate = moment( new Date()).format('DD-MM-YYYY HH:mm:ss');
														$scope.oTCompletionNoteList.push(newdata);
													});
								}*/

								var data = {
									postOperativeId : $scope.postDetailsObj.postOperativeId != null ? $scope.postDetailsObj.postOperativeId: null,
									proReqId : $scope.postDetailsObj.proReqId,
									postOpNote : $scope.postOpNote,
									status : "A",
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									oTCompletionNoteDtoList : $scope.oTCompletionNoteList,
								};
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/otims/PostOperativeDetails/savePostOperativeDetails";
								GenericService
										.serviceAction(METHOD_POST,
												URI, data)
										.then(
												function(response) {
													$scope.stopLoader();
													if (response.data.status == "success") {
														 growl.success(response.data.message,{
															 title : 'Success!'
															});
														if($scope.postDetailsObj.proReqId!=null && $scope.postDetailsObj.proReqId!=undefined ){
															$scope.getPostOperativeCareData();
														}
													}

												});

							}
							
						    $scope.getClassForColor = function(info)
		                    {

		                           if(info.priorityId==2){
		                               return 'td-bg-red';
		                           }
		                    }
						    
						    
							var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};

								var URI = BASE_URL + ROOT_URL + GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});
							
							$scope.savePostOperativeDetails = function() {
										
										
										
										if($scope.medicationDetails.length > 0){
											  growl.warning("Please save added mediction !!",{
													title : WARNING_MSG
												});
											  $("#order_modal").modal('show');
												return false;
										}
										
										if($scope.infusionObj.solutionName !=""){
											
											growl.warning(" Please save added infusion !! ", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											$("#order_modal").modal('show');
											return false;
										}
										
                                        if($scope.addedServiceOrdersForDisplay.length > 0){
											
											growl.warning(" Please save added sarvices !! ", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											$("#order_modal").modal('show');
											return false;
										}
                                        $rootScope.startLoader();
                                        $scope.oTCompletionNoteList = [];
										if ($scope.surgeryNotes != null
												&& $scope.surgeryNotes.length > 0) {
											angular.forEach( $scope.surgeryNotes, function(value, index) {
																var newdata = {}
																newdata.otCompletionNoteId = value.otCompletionNoteId!=undefined ? value.otCompletionNoteId : null,
																newdata.description = value.description;
																newdata.template = value.template;
																newdata.noteMasterId = value.noteMasterId;
																newdata.noteTypeId = value.noteType != null ? value.noteType : value.noteTypeId;
																newdata.organizationId = $scope.organizationId;
																newdata.unitId = $scope.unitId;
																newdata.createdBy = $scope.userId;
																newdata.updatedBy = $scope.userId;
																newdata.status = 'A';
																newdata.createdDate = moment( new Date()).format('DD-MM-YYYY HH:mm:ss');
																newdata.updatedDate = moment( new Date()).format('DD-MM-YYYY HH:mm:ss');
																$scope.oTCompletionNoteList.push(newdata);
															});
										}

										/*if ($scope.dischargeNotes != null
												&& $scope.dischargeNotes.length > 0) {
											angular.forEach($scope.dischargeNotes,
															function(value,index) {
																var newdata = {};
																newdata.otCompletionNoteId = value.otCompletionNoteId!=undefined ? value.otCompletionNoteId : null,
																newdata.description = value.description;
																newdata.template = value.template;
																newdata.noteMasterId = value.noteMasterId;
																newdata.noteTypeId = value.noteTypeId;
																newdata.organizationId = $scope.organizationId;
																newdata.unitId = $scope.unitId;
																newdata.createdBy = $scope.userId;
																newdata.updatedBy = $scope.userId;
																newdata.status = 'A';
																newdata.createdDate = moment( new Date()).format( 'DD-MM-YYYY HH:mm:ss');
																newdata.updatedDate = moment( new Date()).format('DD-MM-YYYY HH:mm:ss');
																$scope.oTCompletionNoteList.push(newdata);
															});
										}*/

										var data = {
											postOperativeId : $scope.postDetailsObj.postOperativeId != null ? $scope.postDetailsObj.postOperativeId: null,
											proReqId : $scope.postDetailsObj.proReqId,
											postOpNote : $scope.postOpNote,
											status : "A",
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											oTCompletionNoteDtoList : $scope.oTCompletionNoteList,
										};
										var URI = BASE_URL
												+ ROOT_URL
												+ "/api/otims/PostOperativeDetails/savePostOperativeDetails";
										GenericService
												.serviceAction(METHOD_POST,
														URI, data)
												.then(
														function(response) {
															$scope.stopLoader();
															if (response.data.status == "success") {
																$timeout(
																		function() {
																			$scope
																					.goToPostCare();
																		}, 3000);
															}

														});

									}

							$scope.goToPostCare = function() {
								$state.go('postOperativeCare', {
									intraCommonObj: $stateParams.intraCommonObj,
								 	intraDetailsObj: $stateParams.intraDetailsObj,
								 	resultCommonObj:$stateParams.resultCommonObj,
								 	postCommonObj: $scope.postCommonObj,
								 	postDetailsObj: $scope.postDetailsObj,
								 	postCareCommonObj : $scope.postCommonObj,
									postCareDetailsObj : $scope.postDetailsObj
									
								});
							}
							
							$scope.backScreen = function(){
								 $timeout(function () { 
									 $state.go('intraOperative',
												{
										 		 intraCommonObj: $stateParams.intraCommonObj,
										 		 intraDetailsObj : $stateParams.intraDetailsObj,
										 		 resultCommonObj : $stateParams.resultCommonObj,
												});
								 }, 3000);
							}
							//book appointment
							
							$scope.datepickerScheduleAppointment = new Date();
							$scope.dateOptionsSchedule = {
					        	formatYear : 'yyyy',
								showWeeks : false,
								minDate:new Date()//max 
							};
							$scope.openDatePickerSchedule = function() {
								$scope.datepickerSchedule.opened = true;
							};

							$scope.datepickerSchedule = {
								opened : false
							};
							
						  	$scope.scheduleAppointmentPopup = function() {
					       		$rootScope.startLoader();
					       		var dt = moment($scope.datepickerSchedule).format('YYYY-MM-DD');
					       		if(dt == moment($scope.datepickerScheduleAppointment).format('YYYY-MM-DD')){
					       			$scope.slotCheckTime = moment(new Date()).format("HH:mm");
					       		}else{
					       			var dt = moment($scope.datepickerScheduleAppointment).format('YYYY-MM-DD');
					       			$scope.slotCheckTime=""
					       		}
					       		var data = {
										specialityId : $scope.postCommonObj.specialityId,
										doctorId : $scope.postCommonObj.doctorId,
										appointmentDate : dt,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										slotTypeId :1
									};
					       		
								var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.slotDetailsList = [];
													if (response.data.status == "success") {
														$scope.slotDetailsList = response.data.listObject;
													    if($scope.slotDetailsList.length ==  0){
															   growl.warning("Slots not available",{
																	title : WARNING_MSG
																});
																return;
														  }
														$rootScope.stopLoader();
													}

												});
							}
						  	
							$scope.slotBooking1 = function(item)
							{
								$scope.fromTimeStringSlot = item.fromTimeString;
								$scope.fromTimeStringSlotShow = item.fromTimeString2;
								 $scope.slotId = item.slotId;
								 $scope.appointmentDateSchedule = item.appointmentDate;
								 if(item.slotStatusId==4 || item.slotStatusId==6)
									{
										$scope.slotStatusId = 6;
									}else
										$scope.slotStatusId = 4;
							}
							
							$scope.checkDateIsBefore = function(slobObj)
					    	{
					    	     var endTime = moment(slobObj.originalFromTime, 'hh:mm');
					    	     var startTempTime = moment().format("HH:mm");
					    	     var startTime = moment(startTempTime, 'hh:mm');
					    	     
					    		if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize > 0 && slobObj.slotPatientDetailsListsSize < slobObj.allowedOverBookingSlots)
					    			{
					    				return 'bg-orange cursor-pointer day-slot-div';
					    			}
					    		if(slobObj.isBlockedUnblocked == '2' && slobObj.slotPatientDetailsListsSize == '0')
					    		{
					    			return 'bg-green cursor-pointer day-slot-div';
					    		}
					    		
					    	}
							
							$scope.isFollowUpAppointmentTaken='N';
							$scope.saveAppointmentshedul = function(){
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment( $scope.currentDate).format(  'DD-MM-YYYY HH:mm:ss');
								$scope.datepickerSchedule1 = moment( $scope.datepickerScheduleAppointment).format(  'DD/MM/YYYY HH:mm:ss');

								var data = {
									slotId : $scope.slotId,
									remark : "",
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									status : 'A',
									appointmentTypeId : 1,
									slotStatusId : $scope.slotStatusId,
									priorityId : 1,
									allowedOverBookingSlots : 0,
								    appointmentDate2 : $scope.datepickerSchedule1,
								    patientId : $scope.postCommonObj.patientId,
								    isFromDischargeSummary:'Y',
								    againstAdmissionId : $scope.postCommonObj.admissionId!= null ? $scope.postCommonObj.admissionId: 0
								    
								}
								if($scope.slotId != undefined && $scope.slotId != "" && $scope.datepickerScheduleAppointment != undefined && $scope.datepickerScheduleAppointment !="" ){
								var URI = BASE_URL + ROOT_URL
								+ SCHEDULEAPPOINTMENT;

								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
													console.log(response);
													if (response.data.status == "success") {
														$scope.fromTimeStringSlot = "";
														 $scope.slotId = "";
														 $scope.appointmentDateSchedule = "";
														 $scope.slotDetailsList = [];
														$rootScope.stopLoader();
														angular.element('#info').modal('hide');
														}
													else
														{
															angular.element('#info').modal('hide');
															$rootScope.stopLoader();
														}
													$scope.disableFollowupBtn=true;
													$scope.isFollowUpAppointmentTaken='Y';
												});
								}
								else{
									growl.warning(
											"Please select date and time..",
											{
												title : 'Error!'
											});
									$rootScope.stopLoader();
								}
							}
					       	//End for schedule appointment
							


						    
						    // post 
						    $scope.encounterDetailsContractList = [];
							   $scope.finalDocUploadList=[];
							   $scope.allDocumentFinalList=[];
						    
						    
						    

							$scope.initCommon = function()
										{
											$scope.saveBtnFlag = true; 
											$scope.updateBtnFlag = false;
											$scope.incidentTypeId = 1;
											$scope.incidentReportingDate=new Date();
											$scope.incidentReportingDateTime='',
											$scope.specialityId='';
											$scope.incidentCategoryId='';
											$scope.incidentReportId='';
											$scope.reportingTemplateId='';
											$scope.incidentRemark='';
											$scope.roleId='';
											$scope.incidentUserId=$scope.userId;
											$scope.patientId=$scope.postCommonObj.patientId,
											$scope.incidentReportingMapperId = null;
											$scope.patientSearchObj = {
													"uhIdNumber"      : "",
													"patientName"     : "",
													"genderId"        : "",
													"mobileNo"        : "",
													"birthDate"       : "",
													"identificationNo":"",
													"patientType"	  :"1",
													"unitId"          :$scope.unitId,
													"organizationId"  :$scope.organizationId
											}
											
											$scope.getActiveIncidentReport = function(id){
													
													var URI = BASE_URL + ROOT_URL + "/api/unit/getIncidentReportMasterList";
													offset =  0;
													noOfRecordsPerPage = 10;
													var data1 = {
											  			      organizationId:$scope.organisationId,
											  			      unitId:$scope.unitId,
											  			      offset:offset,
											  			      noOfRecordsPerPage:noOfRecordsPerPage
											  		};
													GenericService.serviceAction("POST",URI,data1).then(function(response){
														if(response.data.status == "success"){
														$rootScope.stopLoader();					
														$scope.getActiveIncidentReportList = [];
														angular.forEach(response.data.listObject , function(value,key){
															if(value.status == 'A'){
																$scope.getActiveIncidentReportList.push(value);
															}
														});
														}
													});
													}
											$scope.getActiveIncidentReport();
											
											
										}
							$scope.initCommon();
							
							$scope.obj2 = {};
						
							/**********from date******************/
							
							$scope.openDatePickerDate = function() {
								$scope.datepickerDate.opened = true;
							};

							$scope.datepickerDate = {
								opened : false
							};
							
							$scope.dateOptionDate = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date(),
								};
							
							
							// function for initializa clockpicker
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							}
								
							$scope.specialityMasterList=[];
							//------------ Get Speciality Master List ------------//
							$scope.getSpecialityMasterList = function(){
								try{
									var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
									var obj = {
												organizationId : $scope.organizationId,
												unitId         :$scope.unitId
										    }
									BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										console.log(response);
										if(response.data.status == "success"){
											$scope.specialityMasterList = response.data.listObject;
										}
									});
								}catch (e) {
									console.log("Exception",e.message);
								}
							}
							$scope.getSpecialityMasterList();
							
						
							
							
							//------------ Get 	Active Category Maste List ------------//
							$scope.getActiveCategoryMaster = function(){
								try{
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/getActiveIncidentCategoryMaster/" +$scope.organizationId + "/"+ $scope.unitId;
									var obj = {};
									BillingGenericService.serviceAction(METHOD_GET,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										console.log(response);
										if(response.data.status == "success"){
											$scope.incidentCategoryMasterList = [];
											$scope.incidentCategoryMasterList = response.data.listObject;
										}
									});
								}catch (e) {
									console.log("Exception",e.message);
								}
							}
							$scope.getActiveCategoryMaster();
							
								$scope.getUserList = function(id){
									
									var URI = BASE_URL + ROOT_URL + "/api/global/getActiveUserRoleMasterList";
									var data = {
											organisationId:$scope.organizationId,
										      unitId:$scope.unitId
									}
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if(response.data.status == "success"){
										$rootScope.stopLoader();					
										$scope.getActiveUserList = [];
										angular.forEach(response.data.listObject,
														function(value,index) {
															var data = {};
															data.id = value.roleId;
															data.label = value.roleName;
															$scope.getActiveUserList.push(data);
														});
										console.log("getActiveRoleList",$scope.getActiveRoleList);
										}
									});
									}

							
								
							
							$scope.getRoleMappedToIncidentReport = function(info){
								$scope.getActiveRoleList = [];
								
								$scope.incidentReportId = info.incidentReportId;
								//get role mapped
								angular.forEach(info.incidentReportRoleMapperList,function(value,index){
									var neeArr = {};
									neeArr.id = value.roleId;
									neeArr.label = value.roleName;
									$scope.getActiveRoleList.push(neeArr);
								
								});
								//get template mapped
								$scope.incidentReportTemplateMapperList=[];
								$scope.incidentReportTemplateMapperList=angular.copy(info.incidentReportTemplateMapperList);
							}
							
							//Role wise user
							$scope.getRolewiseUsers = function(id){
								$scope.getRoleWiseUsersList = [];
								$scope.roleList = [];
								$scope.roleList.push(id);
								var URI = BASE_URL + ROOT_URL + "/api/unit/getRoleWiseUserList";
								offset =  0;
								noOfRecordsPerPage = 10;
								var data1 = {
						  			      organizationId:$scope.organisationId,
						  			      unitId:$scope.unitId,
						  			      roleIdList:$scope.roleList
						  			      
						  		};
								GenericService.serviceAction("POST",URI,data1).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();
									$scope.getRoleWiseUsersList=response.data.listObject;
									console.log("$scope.getRoleWiseUsersList", $scope.getRoleWiseUsersList);
									}
								});
								}
							
							
							$scope.templateFormatId=2;
							$scope.value ="";
							$scope.addTemplate = function(reportingTemplateId) {
								if(reportingTemplateId){
									$scope.reportingTemplateId = reportingTemplateId;
									angular.forEach($scope.incidentReportTemplateMapperList , function(value,key){
										if(value.templateId == reportingTemplateId){
											$scope.template = {};
											$scope.template = value;
										}
									});
									$scope.value ="";
									$scope.templateFormatId =$scope.template.templateFormatId;
									if($scope.template.templateFormatId==1)
										{
										
									     	$scope.obj = {};
											$scope.template.templateObject = JSON.parse($scope.template.templateObject);
											$scope.obj = $scope.template.templateObject.obj;
											var replacedHtml = $scope.template.templateObject.html.replace(/ng-model="/g,'ng-model="obj.');
											document.getElementById("htmlContent").innerHTML = replacedHtml;
											
											
//										document.getElementById("htmlContent").innerHTML = JSON.parse($scope.template.templateObject).html;
										}
									else if($scope.template.templateFormatId==2)
										{
										 $scope.value = $scope.template.templateObject;
										}
								}
							}
							$scope.filepreview = "";
							$scope.fileObj={
									csvfile:'',
							}
						    
						    
							//docuument Upload code
							$scope.uploadEncounterDoc = function(Obj)
							{
								 var fd = new FormData();
							        fd.append('encounterId', 0);
							        fd.append('visitTypeId', 1);
							        fd.append('admissionId', 0);
							        fd.append('patientId',$scope.patientId);
							        fd.append('documentTypeId',1);
							        fd.append('createdBy',$scope.userId);
							        fd.append('updatedBy',$scope.userId);
							        fd.append('unitId', $scope.unitId);
							        fd.append('organisationId', $scope.organisationId);
							        fd.append('file',$scope.csvfile);
							        fd.append('expenseId',0);
							        fd.append('grnId',0);
									fd.append('poId',0);
							        fd.append('incidentReportingMapperId',Obj.incidentReportingMapperId);
							        fd.append('companyExpenseId',0);
							        fd.append('createdDate',null);
									fd.append('fileEditedName',null);
									fd.append('opbId',0);
									fd.append('vendorId',0);
							        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
							        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										return response;
									});
							}
							
							$scope.saveIncidentReporting = function(){
									try {
										$rootScope.startLoader();
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										
										if ($scope.incidentReportingDate == undefined) {
											growl.error("Please Select Date ", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											return false;
										}
										if ($scope.incidentReportingDateTime == undefined
												|| $scope.incidentReportingDateTime == "") {
											growl.error("Please Enter Time", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											return false;
										}
										
										if ($scope.specialityId == undefined
												|| $scope.specialityId == "") {
											growl.error("Please Select Department", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											return false;
										}
										if ($scope.incidentCategoryId == undefined
												|| $scope.incidentCategoryId == "") {
											growl.error("Please Select Category", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											return false;
										}
										
										if ($scope.obj2 == undefined
												|| $scope.obj2 == "") {
											growl.error("Please Select Incident", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											return false;
										}
										
										if ($scope.incidentUserId == undefined
												|| $scope.incidentUserId == "") {
											growl.error("Please Select User", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											return false;
										}
										if ($scope.reportingTemplateId == undefined
												|| $scope.reportingTemplateId == "") {
											growl.error("Please Select Template", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											return false;
										}
										
								
										
										if ($scope.roleId == undefined
												|| $scope.roleId == "") {
											growl.error("Please Select Role", {
												title : 'Warning!'
											});
											$rootScope.stopLoader();
											return false;
										}
										
										if($scope.templateFormatId == 1){
										  $scope.dataObj=document.getElementById("htmlContent").innerHTML;
											console.log("sdfsdfdsd",$scope.dataObj);
										}else{
											 $scope.dataObj=$scope.value;
												console.log("sdfsdfdsd",$scope.dataObj);
										}
										
										
										var data =
										{
											 "incidentReportingMapperId":$scope.incidentReportingMapperId,
											 "incidentReportingDate": moment($scope.incidentReportingDate).format('DD-MM-YYYY HH:mm:ss'),
										     "incidentReportingTime":$scope.incidentReportingDateTime,
										     "specialityId":$scope.specialityId,
										     "incidentTypeId":$scope.incidentTypeId,
										     "incidentCategoryId":$scope.incidentCategoryId,
										     "incidentReportId":$scope.incidentReportId,
										     "reportingTemplateId":$scope.reportingTemplateId,
										     "reportingTemplateObj":$scope.dataObj,
										     "incidentRemark":$scope.incidentRemark,
										     "roleId":$scope.roleId,
										     "userId":$scope.incidentUserId,
										     "patientId":$scope.patientId,
										     "organizationId":$scope.organisationId,
										     "unitId":$scope.unitId,
										     "status":'A',
											 "createdBy":$scope.userId,
											 "createdDate":$scope.dt,
											 "updatedBy":$scope.userId,
											 "updatedDate":$scope.dt,
										 }
										console.log("save data",JSON.stringify(data));
										var URI = BASE_URL + ROOT_URL + "/api/unit/saveIncidentReportingMapper";
										GenericService.serviceAction("POST", URI, data).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
												
												
												var uploadData = {};
												uploadData.incidentReportingMapperId = response.data.object;
												var promise = $scope.uploadEncounterDoc(uploadData);
												promise.then(function(response) 
														{
														  if(response.data.status == "success")
															  {
															    $scope.count++;
															    if($scope.finalDocUploadList.length==$scope.count)
																 {
																  
																   $rootScope.stopLoader();
																   $scope.encounterDetailsContractList = [];
																   $scope.finalDocUploadList=[];
																   $scope.allDocumentFinalList=[];
																 }
															    
														      }
														  else 
															{																						
																 $rootScope.stopLoader();
															}
													    });
												
												$scope.saveBtnFlag = true; 
												$scope.updateBtnFlag = false;
												$scope.incidentTypeId = 1;
												$scope.incidentReportingDate=new Date();
												$scope.incidentReportingDateTime='',
												$scope.specialityId='';
												$scope.incidentCategoryId='';
												$scope.incidentReportId='';
												$scope.reportingTemplateId='';
												$scope.incidentRemark='';
												$scope.roleId='';
												$scope.incidentUserId='';
												$scope.patientId=null,
												$scope.incidentReportingMapperId = null;
												$scope.obj2='';
												$scope.value='';
												
												$rootScope.stopLoader();
												
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
											
										});
									} catch (e) {
										console.log("Exception",e.message);
									}
							}   
							
							
							
							
							$scope.selectNote = function(){
								$scope.surgeryNotes =[];
								$scope.surgeryNotes.push($scope.ckObj);
							}
							
							
							// medication code 
							//MedicationOrdering
							$scope.initMedicationObj = function() {
								$scope.searchItemFre = "";
								$scope.searchItem = "";
								$scope.registrationNumber = "";
								$scope.saveBtnFlag = true;
								$scope.updateScope = true;
								$scope.updateBtnFlag = false;
								$scope.roa_options=[];
								$scope.medicationObj = {
									itemId : "",
									searchItem : "",
									formulationTypeId : '',
									favorite_status : false,
									medicationStartDate : new Date(),
									roaId : '',
									dose : '',
									doseUnitId : '',
									durationCount : '',
									durationUnit : 'Day',
									instructionId : '',
									medicationEndDate : '',
									frequencyId : '',
									quantity : '',
									uomTypeId : '',
									conversions : '',
									strength : '',
									priorityId : '2',
									priorityCode : 'NR',
									isGeneric:'N',
									isInstruction:'N',
									nonFormularyAdvice:'N',
									searchItemFre :"",
									isStat : false,
									favorite_status : false,
									isDischarge : false,
									nonFormularyAdvice:'N',
									prescribedDosageUnitList:[]

								};
							}

							$scope.initMedicationObj();
							$scope.updateFlag = false;
							$scope.updateFlagValidation = false;
							$scope.saveFlag = true; 
							
							$scope.clrNonForData=function(info){
								if(info == 'N'){
									$scope.initMedicationObj();
								}
								else{
									$scope.searchItemFre = "";
									$scope.searchItem = "";
									$scope.registrationNumber = "";
									$scope.medicationObj = {
										itemId : "",
										formulationTypeId : '',
										favorite_status : false,
										medicationStartDate:new Date(),
										roaId : '',
										dose : '',
										doseUnitId : '',
										durationCount : '',
										durationUnit : 'Day',
										instructionId : '',
										medicationEndDate : '',
										frequencyId : '',
										quantity : '',
										uomTypeId : '',
										conversions : '',
										strength : '',
										priorityId : '2',
										priorityCode : 'NR',
										isGeneric:'N',
										isInstruction:'N',
										nonFormularyAdvice:'Y',
										searchItemFre :"",
										isStat : false,
										isDischarge : false,
										prescribedDosageUnitList:[]
									};
								}
							}
							$scope.medicationDetails = [];
						      $scope.GetUOMUnitList = function () {
						          var URI = CONSTANTS.GLOBAL.UOM_UNITS + "/"+ $scope.organizationId;

						          $rootScope.startLoader();
						          GenericService.serviceAction("GET", URI, {}).then(
						            function (response) {
						              $scope.uomUnits = response.data;
						            },
						            function (err) {
						              $rootScope.stopLoader();
						            });
						      }
						      $scope.GetUOMUnitList();
							$scope.addEmptyRow = function() {
								 $scope.medicationExist =false;
								 var isDischarge=false;
								 if($scope.medicationDetails.length>0 && $scope.medicationDetails[-1].isDischarge!=undefined){
									 if($scope.medicationDetails[-1].isDischarge==true){
										 isDischarge=true;
									 }
								 }
								 
								 if($scope.medicationDetails.length>0){
								 if($scope.medicationDetails[$scope.medicationDetails.length-1].isDischarge==false && isDischarge==true){
									 growl.warning(
												'You can add only normal medication',
												{
													title : 'Warning!'
												});
									return false;
								 }
								 if($scope.medicationDetails[$scope.medicationDetails.length-1].isDischarge==true && isDischarge==false){
									 growl.warning(
												'You can add only discharge medication',
												{
													title : 'Warning!'
												});
									return false;
								 }
								 
								 }
								 
								if ( ($scope.medicationObj.searchItem != undefined || $scope.medicationObj.medicationName != ""  &&  $scope.medicationObj.searchItem != "")
										&& $scope.medicationObj.medicationEndDate != undefined
										&& $scope.medicationObj.medicationEndDate != ''
										&& $scope.medicationObj.durationCount != undefined
										&& $scope.medicationObj.durationCount != ''
										&& $scope.medicationObj.doseUnitId != undefined
										&& $scope.medicationObj.doseUnitId != ''
										&& $scope.medicationObj.quantity != 0
										&& $scope.medicationObj.roaId != undefined
										&& $scope.medicationObj.roaId != ''
										&& $scope.medicationObj.quantity != undefined
										&& $scope.medicationObj.searchItemFre != ''
										&& $scope.medicationObj.formulationCounterIndex != ''
										&& $scope.medicationObj.dose != undefined
										&& ($scope.medicationObj.dose != '' || $scope.medicationObj.dose == 0)) {
									
									   if($scope.medicationObj.searchItem != undefined &&  $scope.medicationObj.searchItem != ""){
										   var Arr = $scope.medicationObj.searchItem.split("|");
										   $scope.medicationObj.itemName = Arr[0];
											var roaDesc = $
													.grep(
															$scope.roa_options,
															function(disCat) {
																return disCat.roa_id == $scope.medicationObj.roaId;
															})[0].roa_details;

											$scope.medicationObj.roaDesc = roaDesc;
											
									   }else{
										   
										   var Arr = $scope.medicationObj.medicationName.split("|");
										   $scope.medicationObj.searchItem = Arr[0];
										   $scope.medicationObj.itemName = $scope.medicationObj.medicationName;
										   $scope.medicationObj.medicationName = $scope.medicationObj.medicationName;

											var roaDesc = $
													.grep(
															$scope.roa_options1,
															function(disCat) {
																return disCat.roa_id == $scope.medicationObj.roaId;
															})[0].roa_details;

											$scope.medicationObj.roaDesc = roaDesc;
									   }
								var strengthDesc = $ .grep( $scope.doseList, function(disCat) {
													return disCat.id == $scope.medicationObj.doseUnitId;
												})[0].unit;
								$scope.medicationObj.strengthDesc = strengthDesc;
								$scope.medicationObj.frequencyDesc = $scope.medicationObj.searchItemFre;
								$scope.medicationObj.nonFormularyAdvice = $scope.medicationObj.nonFormularyAdvice;
								$scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
								$scope.medicationObj.favorite_status = $scope.medicationObj.favorite_status;
								if($scope.medicationObj.doctorFavoriteId != undefined){
								$scope.medicationObj.doctorFavoriteId=$scope.medicationObj.doctorFavoriteId
								}
								if($scope.medicationObj.isInstruction == 'Y') {
									$scope.medicationObj.instructionId = null;
									$scope.medicationObj.instructionDesc = null;
								}
								
								if ($scope.medicationObj.instructionId != null && $scope.medicationObj.instructionId!=""
									&& $scope.medicationObj.instructionId!=undefined) {
									var instructionDesc = $.grep($scope.medicationInstructions,
													function(disCat) {
														return disCat.instructionId == $scope.medicationObj.instructionId;
													})[0].instructionDesc;

									$scope.medicationObj.instructionDesc = instructionDesc;
								} else if($scope.medicationObj.isInstruction == 'Y') {
									$scope.medicationObj.instructionDesc = $scope.medicationObj.isInstructionText;
								}else {
									$scope.medicationObj.instructionDesc = "No instruction";
								}
								
								if ($scope.medicationObj.priorityId != null && $scope.medicationObj.priorityId!="" && $scope.medicationObj.priorityId!=undefined) {
								var priorityCode = $.grep( $scope.priorityList, function(disCat) {
													return disCat.priorityId == $scope.medicationObj.priorityId;
												})[0].priorityCode;

								$scope.medicationObj.priorityCode = priorityCode;
								} else {
									$scope.medicationObj.priorityCode = " ";
									$scope.medicationObj.priorityId =2;
								}
								
								$scope.medicationObj.frequencyId = $scope.medicationObj.frequencyId ;
								$scope.medicationObj.noOfAdministrationTime =$scope.freQua ;
								$scope.medicationObj.durationCount =$scope.medicationObj.durationCount ;
								
								
								 if($scope.medicationDetails.length>0){
	                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
	                                    angular.forEach($scope.medicationDetails,function(value,index){
	                                        
	                                        if ( $scope.medicationObj.searchItem == value.itemName
	                                                && $scope.medicationObj.medicationStartDate == value.medicationStartDate
	                                                && $scope.medicationObj.durationCount == value.durationCount
	                                                && $scope.medicationObj.searchItemFre == value.frequencyDesc
	                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
	                                                && $scope.medicationObj.roaId == value.roaId
	                                                && $scope.medicationObj.quantity == value.quantity) {
	                                            
	                                            $scope.medicationExist =true;
	                                        }
	                                    });
	                                }
	                                
	                               if($scope.detailedMedications.length>0){
	                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
	                                    angular.forEach($scope.detailedMedications,function(value,index){
	                                        
	                                        var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
	                                        var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
	                                        var startDT =  moment(sDT).format('YYYY-MM-DD');
	                                        var endDT = moment(eDT).format('YYYY-MM-DD');
	                                        
	                                        if ( $scope.medicationObj.searchItem == value.itemName
	                                                && $scope.medicationObj.medicationStartDate ==  startDT
	                                                && $scope.medicationObj.searchItemFre == value.frequencyDesc
	                                                && $scope.medicationObj.dose== value.doseDesc
	                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
	                                                && $scope.medicationObj.quantity == value.orderQty
	                                                && $scope.medicationObj.roaId == value.roaId) {
	                                            
	                                            $scope.medicationExist =true;
	                                        }
	                                    });
	                                    
	                                }
	                                
	                                
	                                if($scope.medicationExist){
	                                    growl.warning(
	                                            "Medication Already Exist",
	                                            {
	                                                title : 'Warning!'
	                                            });
	                                    $scope.medicationObj.medicationStartDate = new Date();
	                            $rootScope.stopLoader();
	                            return false;
	                            }
							
								$scope.medicationDetails.push($scope.medicationObj);
								
								if(!$scope.medicationExist){
									$scope.initMedicationObj();
									}
								}else{
									growl.warning(
											"Please enter all mandatory fields.",
											{
												title : 'Warning!'
											});
								}
							   
							}
							
							$scope.itemList = [];
							$scope.searchMedicationItems = function(searchKeyword) {
								if(searchKeyword.length >= 3){
									$scope.itemList = [];
									var unitId = $scope.unitId
									var orgId = $scope.organizationId
									var Arr = searchKeyword.split("|");
									searchKeyword = Arr[0];
									var regex=/[^a-z]/gi;
									searchKeyword=searchKeyword.replace(regex," ");
									
									var URI = BASE_URL + ROOT_URL + GETMEDICATIONDETAILS + "?unitId="
											+ unitId + "&orgId=" + orgId + "&searchDrug=" + searchKeyword
									return BillingGenericService
											.serviceAction(METHOD_GET, URI, "",
													NOTIFICATION_MSG_STATUS_FALSE).then(
													function(response) {
														if (response.data.status == "success") {
															$scope.itemList = response.data.listObject;
														}
														return $scope.itemList;
													});
								}
							}
			              	$scope.calculationByDoseCalculation=function(item){
                        		var currentMedication = item;
								var indexOfCurrentMedication = $scope.medicationDetails.indexOf(currentMedication)

                        		$scope.medicationObj.doseCalculationApplicableData=JSON.parse(item.doseCalculationApplicableData);
						        angular.forEach($scope.medicationObj.doseCalculationApplicableData, function(val, key){
						        	$scope.medicationObj.quantityForDoseCal=val.quantityForDoseCal;
						        	$scope.medicationObj.doseForDoseCal=val.doseForDoseCal;
						        	$scope.medicationObj.strengthForDoseCal=val.strengthForDoseCal;
						        	$scope.medicationObj.searchItemFre=val.searchItemFre;
						        	$scope.medicationObj.noOfAdministrationTimeForDoseCal=val.noOfAdministrationTimeForDoseCal;
						        	$scope.medicationObj.durationForDoseCal=val.durationForDoseCal;
						        	$scope.medicationObj.durationCountForDoseCal=val.durationCountForDoseCal;
						        	$scope.freQua =$scope.medicationObj.noOfAdministrationTimeForDoseCal;
									$scope.medicationObj.frequencyId = val.frequencyForDoseCal;
									$scope.frequency_id=val.frequencyForDoseCal;
									$scope.frequency=$scope.medicationObj.searchItemFre;
									$scope.medicationObj.durationCount=$scope.medicationObj.durationCountForDoseCal;
									$scope.medicationObj.duration=$scope.medicationObj.durationForDoseCal;
									$scope.medicationObj.itemStrength=$scope.medicationObj.strengthForDoseCal !=null ? $scope.medicationObj.strengthForDoseCal:(item.strength+" "+(item.unitDesc!=null ? item.unitDesc : " ") );
									
						        });	
						        $scope.idx=indexOfCurrentMedication;
								
								$scope.onDurationCountChange(item);
								$scope.medicationObj.prescribeDose= $scope.dosePerServing;
						      
                        	}
							$scope.checkgenericObj = {};
							$scope.itemStrength=""; 
							$scope.prescribedDosageUnitList=[];
							$scope.addMedicationItemList = function($item,	$model, $label, idx) {
								console.log($item);
								$scope.itemStrength="";
								$rootScope.startLoader();
								var arr = $item.medicationName.split("|");
								var dggd =arr[1].split(":");
								$scope.genericQuantityCheck = true;
								if(dggd[1] == 0){
									growl.warning("Stock not available for the selected Brand" );
									if(dggd[1] == 0)
										{
										$scope.genericQuantityCheck = true;
										}
								}
								$scope.medicationObj.itemStrength=$item.strength+" "+($item.unitDesc!=null ? $item.unitDesc : " ") ;
								$scope.medicationObj.doseUnitId=$item.strengthUnitId;
								$scope.medicationObj.dose=$item.strength;
								$scope.medicationObj.isDoseCalculationApplicable = $item.isDoseCalculationApplicable;
								$scope.medicationObj.doseCalculationApplicableData = $item.doseCalculationApplicableData;
								$scope.medicationObj.itemId = $item.drugId;
								$scope.medicationObj.genericName = "";
								$scope.medicationObj.genericId = $item.genericId;
								$scope.medicationObj.searchGeneric= $item.genericName;
								$scope.medicationObj.prescribedDosageUnitList=[];
								$scope.medicationObj.formulationCounterIndex="";
								$scope.medicationObj.rate=$item.rate;
								$scope.medicationObj.cimsCode=$item.cimsCode;
								$scope.medicationObj.cimsType=$item.cimsType;
								$scope.cimsCode=$item.cimsCode;
								$scope.cimsType=$item.cimsType;
								
                               $scope.favAvailableflag = false;
								
								if($scope.favouriteMedications.length > 0){
									var found = $scope.favouriteMedications.find(ar=>ar.itemId == $scope.medicationObj.itemId);
									   if(found==undefined)
										   {
										      console.log("testOrderSet" ,$scope.medicationDetails);
										   }
									   else{
										   $scope.favAvailableflag = true;
									   }
								}

								var formulationTypeId = $item.formulationTypeId;
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getRoaList"
										+ "?unitId="
										+ $scope.unitId
										+ "&orgId="
										+ $scope.organizationId
										+ "&formulationTypeId="
										+ formulationTypeId
										+ "&genericId="
										+ $item.genericId
										+ "&patientId="
										+ $scope.postCommonObj.patientId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														var responseList = response.data.listObject;
														$scope.medicaRoaList = responseList;
														if (response.data.object == 0) {
															if (response.data.listObject.length !== 0) {

																$scope.roa_options = responseList;
															}
															$scope.medicationObj.genericName = $item.genericName;
															$scope.medicationObj.formulationTypeId = $item.formulationTypeId;
															$scope.medicationObj.strength = $item.strength;
														} else {
															$scope.checkgenericObj = {
																genericName : $item.genericName,
																formulationTypeId : $item.formulationTypeId,
																strength : $item.strength,
															}
															angular.element( '#confirmAddItemPopup').modal('show');
														}
													}
													// get formuation dosage
													// list
													var dataobj = {
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
															formulationTypeId:$item.formulationTypeId
													};
													if($scope.medicationObj.isDoseCalculationApplicable == true){
														$scope.medicationObj.prescribeDose=($scope.medicationObj.prescribeDose !=undefined && $scope.medicationObj.prescribeDose!= null && $scope.medicationObj.prescribeDose != "") ? $scope.medicationObj.prescribeDose: "";

													}else{
														$scope.medicationObj.prescribeDose="";
													}													
													var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
													GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																$rootScope.stopLoader();
																$scope.medicationObj.prescribedDosageUnitList=[];
																$scope.counterFormulation=1;
																
																angular.forEach(response.data.listObject,function(value,index){
																	var unitObj={};
																	unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																	unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																	unitObj.formulationDosageToUnit=value.toConversionUnit;
																	unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																	unitObj.counterIndex=$scope.counterFormulation++;
																	unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																	unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																	unitObj.formulationFactorId=value.factorId;
																	unitObj.formulationDosage=value.dosageForm;
																	unitObj.formulationDosageMappId=value.formulationDosageMappId;
																	$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																});
																
																angular.forEach(response.data.listObject,function(value,index){
																	var unitObj={};
																	if(value.factorId==1){
																		unitObj.formulationDosageFromUnit=value.toConversionUnit;
																		unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																		unitObj.formulationDosageToUnit=value.fromConversionUnit;
																		unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																		unitObj.counterIndex=$scope.counterFormulation++;
																		unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																		unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																		unitObj.formulationFactorId=value.factorId;
																		unitObj.formulationDosage=value.dosageForm;
																		unitObj.formulationDosageMappId=value.formulationDosageMappId;
																		$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																	}
																});
																var list=[];
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
																	 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																        return $scope.medicationObj.doseUnitId === obj.formulationDosageFromUnitId;
																    });
																	if(list.length>0){	
																		$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																		$scope.medicationObj.prescriptionDoseUnit=list[0].formulationDosageFromUnit;
																	}
																}
																
															/*
															 * if($scope.prescribedDosageUnitList.length>0){
															 * $scope.medicationObj.prescribedDoseUnitId=$scope.medicationObj.doseUnitId; }
															 */
													});
													
												});
								
								
								if($scope.favAvailableflag == true){
									var idxFav = $scope.favouriteMedications.findIndex(item => item.itemId == $scope.medicationObj.itemId);
									
									$scope.tempDurationCount=$scope.medicationObj.durationCount;
									$scope.medicationObj = {};
									$scope.medicationObj.rate=$item.rate;
									$scope.medicationObj.itemId = $scope.favouriteMedications[idxFav].itemId;
									$scope.medicationObj.doctorFavoriteId=$scope.favouriteMedications[idxFav].doctorFavoriteId;
									$scope.searchItem = $scope.favouriteMedications[idxFav].itemName;
									$scope.medicationObj.genericName = $scope.favouriteMedications[idxFav].genericName;
									$scope.medicationObj.searchGeneric = $scope.favouriteMedications[idxFav].genericName;
									$scope.medicationObj.genericId = $scope.favouriteMedications[idxFav].genericId;
									$scope.medicationObj.formulationTypeId = $scope.favouriteMedications[idxFav].formulationTypeId;
									$scope.medicationObj.isGeneric ='N';
									$scope.medicationObj.nonFormularyAdvice ='N';
									$scope.medicationObj.isInstruction='N';
									$scope.medicationObj.priorityCode='NR';
									$scope.medicationObj.searchItem =$scope.favouriteMedications[idxFav].itemName;;
									$scope.medicationObj.searchItemFre = $scope.favouriteMedications[idxFav].frequencyDesc;
									$scope.medicationObj.frequencyId = $scope.favouriteMedications[idxFav].frequencyId;
									$scope.medicationObj.prescribedDosageUnitList=[];
									$scope.frequency_id=$scope.favouriteMedications[idxFav].frequencyId;
									$scope.frequency=$scope.medicationObj.searchItemFre;
									$scope.noOfAdministrationTime=$scope.favouriteMedications[idxFav].noOfAdministrationTime;
									$scope.item={
											frequency_id:$scope.frequency_id,
											frequency:$scope.medicationObj.searchItemFre,
											noOfAdministrationTime:$scope.noOfAdministrationTime
									}
									
									$scope.model=$scope.favouriteMedications[idxFav].frequencyDesc;
									$scope.label=$scope.favouriteMedications[idxFav].frequencyDesc;
									$scope.medicationObj.favorite_status =false;
									$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
									var formulationTypeId = $scope.favouriteMedications[idxFav].formulationTypeId;
									var URI = BASE_URL
											+ ROOT_URL
											+ "/api/opd/getRoaList"
											+ "?unitId="
											+ $scope.unitId
											+ "&orgId="
											+ $scope.organizationId
											+ "&formulationTypeId="
											+ formulationTypeId
											+ "&genericId="
											+ $scope.favouriteMedications[idxFav].genericId
											+ "&patientId="
											+ $scope.postCommonObj.patientId;

									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI)
											.then(
													function(response) {
														if (response.data.status == "success") {
															if (response.data.listObject.length !== 0) {
																var responseList = response.data.listObject;
																$scope.roa_options = responseList;
															}
														}
													});

									 if($scope.favouriteMedications[idxFav].roaId != null){
										 $scope.medicationObj.roaId =( $scope.favouriteMedications[idxFav].roaId)
											.toString();
	                                 }
									$scope.medicationObj.dose = $scope.favouriteMedications[idxFav].dose;
									$scope.medicationObj.doseUnitId = $scope.favouriteMedications[idxFav].doseUnitId!=null?($scope.favouriteMedications[idxFav].doseUnitId).toString():0;
									$scope.medicationObj.itemStrength=$scope.favouriteMedications[idxFav].dose+" "+$scope.favouriteMedications[idxFav].doseDesc;
									$scope.medicationObj.unit = $scope.favouriteMedications[idxFav].doseDesc;
									
									
									// get formuation dosage list
									var dataobj = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											formulationTypeId:$scope.favouriteMedications[idxFav].formulationTypeId
									};
									if($scope.medicationObj.isDoseCalculationApplicable == true){
										$scope.medicationObj.prescribeDose=($scope.medicationObj.prescribeDose !=undefined && $scope.medicationObj.prescribeDose!= null && $scope.medicationObj.prescribeDose != "") ? $scope.medicationObj.prescribeDose: "";

									}else{
										$scope.medicationObj.prescribeDose="";
									}
									var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
									GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
												$rootScope.stopLoader();
												$scope.medicationObj.prescribedDosageUnitList=[];
												$scope.counterFormulation=1;
												
												angular.forEach(response.data.listObject,function(value,index){
													var unitObj={};
													unitObj.formulationDosageFromUnit=value.fromConversionUnit;
													unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
													unitObj.formulationDosageToUnit=value.toConversionUnit;
													unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
													unitObj.counterIndex=$scope.counterFormulation++;
													unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
													unitObj.formulationDosageToConversionValue=value.toConversionFactor;
													unitObj.formulationFactorId=value.factorId;
													unitObj.formulationDosage=value.dosageForm;
													unitObj.formulationDosageMappId=value.formulationDosageMappId;
													$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
												});
												
												angular.forEach(response.data.listObject,function(value,index){
													var unitObj={};
													if(value.factorId==1){
														unitObj.formulationDosageFromUnit=value.toConversionUnit;
														unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
														unitObj.formulationDosageToUnit=value.fromConversionUnit;
														unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
														unitObj.counterIndex=$scope.counterFormulation++;
														unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
														unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
														unitObj.formulationFactorId=value.factorId;
														unitObj.formulationDosage=value.dosageForm;
														unitObj.formulationDosageMappId=value.formulationDosageMappId;
														$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
													}
												});
												var list=[];
												if($scope.medicationObj.prescribedDosageUnitList.length>0 ){
													 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
														 return $scope.favouriteMedications[idxFav].formulationDosageMappId === obj.formulationDosageMappId;
												    });
													if(list.length>0){	
														$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
													}
												}
									});
									
									$scope.medicationObj.durationCount = $scope.tempDurationCount;
									$scope.medicationObj.durationUnit = 'Day';
									$scope.medicationObj.isGeneric ='N';
									$scope.medicationObj.nonFormularyAdvice ='N';
									$scope.medicationObj.medicationStartDate = new Date();
									$scope.medicationObj.medicationEndDate = '';
									$scope.medicationObj.instructionId = $scope.favouriteMedications[idxFav].instructionId;
									$scope.medicationObj.priorityId ='2';
									$scope.medicationObj.rate=$item.rate;
									console.log('svdfvdfv', $scope.medicationObj);
									
									if($scope.patientDetails.dischargeStatusId!=null &&
											($scope.patientDetails.dischargeStatusId==1 || $scope.patientDetails.dischargeStatusId==2
													|| $scope.patientDetails.dischargeStatusId == 3)){
										var isDischargeMed = $scope.medicationDetails.find(ar=>ar.isDischarge);
										if(isDischargeMed!=undefined){
											$scope.medicationObj.isDischarge = "discharge";
										}
									}
									
									
								}
								if (!!$scope.medicationObj.itemId) {


                                       	   var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API  +$scope.organizationId + '/' + $scope.medicationObj.itemId;
                                             var data = {};
                                              GenericService.serviceAction("GET", URI, data).then(function (response) {
                                                    if(response.data.unitOfMeasurementDetailsResponse!=null)
                                                    {
                                                    	response.data.unitOfMeasurementDetailsResponse.forEach(function(item){
                                                    	$scope.medicationObj.uom = item.uomList;
                                          			   $scope.medicationObj.uom= [{
                                                          uomDetails: "Select UOM",
                                                      }];

                                                      	$scope.medicationObj.uom =item.uomList;
                                                          $scope.medicationObj.selectedUom = $scope.uomUnits.find(function(units){
                                                            return $scope.medicationObj["uom"][2].uomUnitId == units.id;
                                                          });

                                            	      });
                                                    	
                                               
                                                    	}
                                              });

                              }

								
								if($scope.medicationObj.isDoseCalculationApplicable == true){
									$scope.medicationObj.prescribeDose= $scope.dosePerServing;
									$scope.calculationByDoseCalculation($scope.medicationObj);
								}
							}
							
							$scope.searchGenericItems = function(searchKeyword){
					        	$scope.genericList=[];
					        	var unitId = $scope.unitId
								var orgId = $scope.organizationId
								 var data={};
					        	var Arr = searchKeyword.split("|");
					        	searchKeyword = Arr[0];
								var URI = BASE_URL + ROOT_URL + "/api/opd/getGenericListForGenericSearch" + "?unitId=" + 
									unitId + "&orgId=" + orgId + "&searchDrug=" + searchKeyword
					    		return BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					    			if(response.data.status == "success"){
					    				$scope.genericList = response.data.listObject;
					    			}
					    			return $scope.genericList;
					    		});
					    	}
							
							$scope.addMedicationGenericList = function($item,
									$model, $label, idx) {
								console.log($item);

								$rootScope.startLoader();

								$scope.medicationObj.itemId = $item.drugId;
								$scope.medicationObj.genericName = "";
								$scope.medicationObj.genericId = $item.genericId;
								$scope.medicationObj.searchItem = $item.medicationName;
								$scope.medicationObj.searchGeneric= $item.genericName
								$scope.medicationObj.genericName= $item.genericName

								var formulationTypeId = $item.formulationTypeId;
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getRoaList"
										+ "?unitId="
										+ $scope.unitId
										+ "&orgId="
										+ $scope.organizationId
										+ "&formulationTypeId="
										+ formulationTypeId
										+ "&genericId="
										+ $item.genericId
										+ "&patientId="
										+ $scope.postCommonObj.patientId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														var responseList = response.data.listObject;
														$scope.medicaRoaList = responseList;
														if (response.data.object == 0) {
															if (response.data.listObject.length !== 0) {
																$scope.roa_options = responseList;
															}
															$scope.medicationObj.genericName = $item.genericName;
															$scope.medicationObj.formulationTypeId = $item.formulationTypeId;
															$scope.medicationObj.strength = $item.strength;
														} else {
															$scope.checkgenericObj = {
																genericName : $item.genericName,
																formulationTypeId : $item.formulationTypeId,
																strength : $item.strength,
															}
															angular.element('#confirmAddItemPopup').modal('show');
														}
													}
												});

								var itemId = $item.drugId;
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getUOMList" + "?unitId="
										+ $scope.unitId + "&orgId="
										+ $scope.organizationId + "&itemId="
										+ itemId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														if (response.data.listObject.length !== 0) {
															$scope.medicationObj.uomTypeId = response.data.listObject[0].uomTypeId;
															$scope.medicationObj.conversions = response.data.listObject[0].conversions;
														}
													}
												});

								$scope.showMedicationOptions = false;
								
								$scope.search = "";
								
								//get formuation dosage list
								var dataobj = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										formulationTypeId:formulationTypeId
								};
								$scope.medicationObj.doseUnitId=$item.strengthUnitId;
								$scope.medicationObj.dose=$item.strength;
								
								$scope.medicationObj.prescribeDose="";
								$scope.medicationObj.itemStrength=$item.strength+" "+$item.unitDesc; 
								var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
								GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
											$rootScope.stopLoader();
											$scope.medicationObj.prescribedDosageUnitList=[];
											$scope.counterFormulation=1;
											$scope.medicationObj.formulationCounterIndex="";
											angular.forEach(response.data.listObject,function(value,index){
												var unitObj={};
												unitObj.formulationDosageFromUnit=value.fromConversionUnit;
												unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
												unitObj.formulationDosageToUnit=value.toConversionUnit;
												unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
												unitObj.counterIndex=$scope.counterFormulation++;
												unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
												unitObj.formulationDosageToConversionValue=value.toConversionFactor;
												unitObj.formulationFactorId=value.factorId;
												unitObj.formulationDosage=value.dosageForm;
												unitObj.formulationDosageMappId=value.formulationDosageMappId;
												$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
											});
											
											angular.forEach(response.data.listObject,function(value,index){
												var unitObj={};
												if(value.factorId==1){
													unitObj.formulationDosageFromUnit=value.toConversionUnit;
													unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
													unitObj.formulationDosageToUnit=value.fromConversionUnit;
													unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
													unitObj.counterIndex=$scope.counterFormulation++;
													unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
													unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
													unitObj.formulationFactorId=value.factorId;
													unitObj.formulationDosage=value.dosageForm;
													unitObj.formulationDosageMappId=value.formulationDosageMappId;
													$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
												}
											});
											var list=[];
											if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
												 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
											        return $scope.medicationObj.doseUnitId === obj.formulationDosageFromUnitId;
											    });
												if(list.length>0){	
													$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
												}
											}
								});
							}
							   // get ROA for non formulary start
							$scope.roa_options1 = [];
						    $scope.getROAForNonFormulary=function(){
						    	$scope.medicationObj.prescribedDosageUnitList=[];
						    	var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaListNonFormulary" + "?unitId=" + 
								$scope.unitId + "&orgId=" + $scope.organizationId +"&patientId=" + $scope.postCommonObj.patientId;
							
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
								if(response.data.status == "success"){
										$scope.roa_options1 = response.data.listObject;
										console.log("non formulary ROA="+$scope.roa_options);
											
								}
							});
						    }
							$scope.continueToAllergy = function() {
								$scope.medicationDetails[$scope.checkgenericObj.medicationIndx].genericName = $scope.checkgenericObj.genericName;
								$scope.medicationDetails[$scope.checkgenericObj.medicationIndx].formulationTypeId = $scope.checkgenericObj.formulationTypeId;
								$scope.medicationDetails[$scope.checkgenericObj.medicationIndx].strength = $scope.checkgenericObj.strength;
								$scope.medicationDetails[$scope.checkgenericObj.medicationIndx].favorite_status = false;
								$scope.roa_options = $scope.medicaRoaList;
							}
						    
						    // get doseList
							$scope.doseList = [];
						    $scope.getDoseList = function(){
						    	var URI = BASE_URL + ROOT_URL + "/api/globals/strength/units/"+$scope.organizationId;
						GenericService.serviceAction("GET", URI, {})
								.then(function(response) {
									$scope.doseList = [];
									$scope.doseList = response.data;

								});
						    }
						    
						    $scope.getDoseList();
						    
						    $scope.calculateFre = function(idx) {
								
								var list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
							        return $scope.medicationObj.formulationCounterIndex === obj.counterIndex;
							    });
								var totalDuration="";
								if($scope.medicationObj.durationUnit=="Day"){
									totalDuration=parseInt($scope.medicationObj.durationCount)*1;
								}else if($scope.medicationObj.durationUnit=="Week"){
									totalDuration=parseInt($scope.medicationObj.durationCount)*7;
								}else if($scope.medicationObj.durationUnit=="Month"){
									totalDuration=parseInt($scope.medicationObj.durationCount)*30;
								}else{
									totalDuration=parseInt($scope.medicationObj.durationCount)*30*12;
								}
								if(list.length>0){	
									var presDoseObj=list[0];
									if(list.length>0  && presDoseObj.formulationFactorId==2){
										$scope.medicationObj.prescriptionDoseUnit=presDoseObj.formulationDosage;
									}else{
										$scope.medicationObj.prescriptionDoseUnit=presDoseObj.formulationDosageFromUnit;
									}
									$scope.medicationObj.prescribedDoseUnitId=presDoseObj.formulationDosageFromUnitId;
								}
								if(list.length>0  && $scope.medicationObj.prescribeDose!="" && $scope.medicationObj.prescribeDose!=undefined && $scope.medicationObj.prescribeDose!=null 
										&& $scope.medicationObj.formulationCounterIndex!="" && $scope.medicationObj.formulationCounterIndex!=undefined && $scope.medicationObj.formulationCounterIndex!=null){
									//for conversion factor
										if($scope.medicationObj.prescribedDoseUnitId==$scope.medicationObj.doseUnitId && $scope.medicationObj.dose!=0){
											$scope.medicationObj.quantity =Math.ceil(($scope.freQua* totalDuration*$scope.medicationObj.prescribeDose)/$scope.medicationObj.dose);
										}else{
											if(list.length>0 && $scope.medicationObj.dose!=0){	
											if(presDoseObj.formulationDosageToUnitId==$scope.medicationObj.doseUnitId){
												var convValue=Math.round(($scope.medicationObj.prescribeDose*presDoseObj.formulationDosageToConversionValue)/presDoseObj.formulationDosageFromConversionValue);
												 $scope.medicationObj.quantity = Math.ceil(($scope.freQua* totalDuration*convValue)/$scope.medicationObj.dose);
											}else{
											  $scope.medicationObj.quantity = Math.ceil(($scope.freQua* totalDuration*$scope.medicationObj.prescribeDose)/$scope.medicationObj.dose);
											}
										  }else{
											  $scope.medicationObj.quantity = Math.ceil(($scope.freQua
														* totalDuration)); 
										  }
											
										}
								}else{
									//using frequency factor 
									if(list.length>0  && presDoseObj.formulationFactorId==2){
										var splitString=(presDoseObj.formulationDosage).split(" ");
										var convValue=0;
										for(i=0;i<splitString.length;i++){
											var b=parseFloat(splitString[i]);
											if(Number.isNaN(b)==false){
												convValue=convValue+(math.number(math.fraction(splitString[i])));
											}
										}
										 $scope.medicationObj.quantity = Math.ceil($scope.freQua
													* totalDuration*(convValue==0?1:convValue)); 
									}else{
										$scope.medicationObj.quantity = Math.ceil(($scope.freQua * totalDuration));
									}
								}
							
							}
						    
							$scope.searchMedicationFreItems = function(searchKeyword) {
								$scope.itemList = [];
								var unitId = $scope.unitId
								var orgId = $scope.organizationId

								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getFrequencyList"
										+ "?unitId=" + unitId + "&orgId="
										+ orgId + "&searchPar=" + searchKeyword
								return BillingGenericService
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.itemList = response.data.listObject;
													}
													if($scope.itemList.length > 0)
														{
														return $scope.itemList;
														}
													else{
														$scope.medicationObj.searchItemFre='';
														$scope.medicationObj.frequencyId='';
													}
													
												});
							}
						    
						    $scope.addFrequencyItemList = function($item, $model, $label, idx) {
								$scope.storeItemOrder=$item;
								$scope.medicationObj.searchItemFre=$item.frequency;
								$scope.freQua = 0;
								$scope.medicationObj.frequencyId = $item.frequency_id;
								$scope.freQua = $item.noOfAdministrationTime;
								if($scope.medicationObj.durationCount == ""){
								$scope.medicationObj.durationCount = '';
								}
								var dur = 0;
								if ($scope.medicationObj.durationCount == '') {
									dur = 0;
								} else {
									 if($scope.medicationObj.durationUnit == "Day")
					    				{
				    					 dur = $scope.medicationObj.durationCount;
					    				 }
				    				 else if($scope.medicationObj.durationUnit == "Week")
						    			{
						    			  var  caldur = $scope.medicationObj.durationCount;
						    			    dur = caldur * 7;
						    			}
					    			else if($scope.medicationObj.durationUnit == "Month")
						    			{
						    			  var  caldur = $scope.medicationObj.durationCount;
						    			    dur = caldur * 30;
						    			}
							    		else if($scope.medicationObj.durationUnit == "Year")
						    			{
							    				
							    			var  caldur = $scope.medicationObj.durationCount;
						    				dur = caldur * 365;
						    		    }
								}
								$scope.medicationObj.quantity = $item.noOfAdministrationTime * dur;
								$scope.calculateFre(idx);
								
							}
						    
						    $scope.onDurationCountChange = function(medicationObj) {
								$scope.calculateEndDate(medicationObj)
							}
						    
						    $scope.calculateEndDate = function(medicationObj) {
								var currentMedication = medicationObj
								var medicationStartDate = currentMedication.medicationStartDate
								var indexOfCurrentMedication = $scope.medicationDetails.indexOf(currentMedication)
								var durationCount = parseInt(currentMedication["durationCount"])
								var durationUnit = currentMedication["durationUnit"]
								
							    	 if(durationUnit == "Day") {
										var durationCount = parseInt(currentMedication["durationCount"]);
										}
									    else if(durationUnit == "Week") {
											var durationCount = parseInt(currentMedication["durationCount"])
											var durationCount = durationCount* 7;
										}
										else if(durationUnit == "Month") {
											var durationCount = parseInt(currentMedication["durationCount"])
											var durationCount = durationCount* 30;
										}
										else if(durationUnit == "Year") {
											var durationCount = parseInt(currentMedication["durationCount"])
											var months = durationCount * 12
											var durationCount = months* 30;
										}
								
								if (durationCount && durationUnit && medicationStartDate) {
									if(durationUnit == "Day") {
										var endDate = moment(medicationStartDate).add(durationCount-1, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Week") {
										var days = durationCount * 7
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Month") {
										var days = durationCount * 30
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Year") {
										var days = durationCount * 365;
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									currentMedication["medicationEndDate"] = endDate;
									currentMedication["medicationEndDateFormatted"] = moment(endDate).toDate();
									currentMedication["startDate"] = medicationStartDate;
									$scope.medicationDetails[indexOfCurrentMedication] = currentMedication
									$scope.model=currentMedication.searchItemFre;
									$scope.label=currentMedication.searchItemFre;
									$scope.idx=indexOfCurrentMedication;
									$scope.calculateFre($scope.idx);
								}else{
									$scope.medicationObj.quantity ="";
								}
							}
						    
							$scope.onDurationUnitChange = function(medicationObj) {
								$scope.calculateEndDate(medicationObj)
							}
							
							$scope.onStartDateChange = function(medicationObj) {
								$scope.calculateEndDate(medicationObj)
							}
							
							// for Medication Advice
                        	$scope.openDatePickerMedicationAdvice = function() {
								$scope.datepickerMedicationAdvice.opened = true;
							};

							$scope.datepickerMedicationAdvice = {
								opened : false
							};
                            $scope.dateOptionsMedicationAdvice = {
                            		formatYear : 'yyyy',
    								showWeeks : false,
    								minDate:new Date(), //max 
    							};
                            
                            $scope.openDatePickerMedicationAdviceEndDate = function() {
								$scope.datepickerMedicationAdviceEndDate.opened = true;
							};

							$scope.datepickerMedicationAdviceEndDate = {
								opened : false
							};
                        
                            $scope.dateOptionsMedicationAdviceEndDate = {
                            		formatYear : 'yyyy',
    								showWeeks : false,
    								minDate:new Date(), //max 
    							};
                            
                            // for patient discharge or not
                          $scope.disOrNotCount = {};
                          $scope.checkPatientDischargeOrNot = function(){
							var disObj = {
								unitId : $scope.unitId,
								organizationId : $scope.organizationId,
								admissionId : $scope.postCommonObj.admissionId
							}
							var URI = BASE_URL + ROOT_URL + "/adt/getPatientDischargeOrNot";
							BillingGenericService
									.serviceAction(METHOD_POST, URI,
											disObj,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												if (response.data.status == "success")
													$scope.disOrNotCount = response.data.object;
											});
                          }
                          if($scope.postCommonObj.admissionId!=null){
                        	  $scope.checkPatientDischargeOrNot();
                          }	  
                          $scope.priorityList = [];
                          $scope.getPriorityList=function(){
								var URI = BASE_URL + ROOT_URL+ "/adt/getActivePriorityList";
								GenericService.serviceAction("GET", URI, {})
								.then(function(response) {
									$scope.priorityList = [];
									$scope.priorityList = response.data.listObject;
									console.log("$scope.priorityList",$scope.priorityList);
								});
							}
							$scope.getPriorityList();
							
							
							$scope.updateMedication = function() {
								$scope.medicationDetails[$scope.medicationIndex] = $scope.medicationObj;
								$scope.medicationDetails[$scope.medicationIndex].medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
								$scope.medicationDetails[$scope.medicationIndex].medicationEndDate = moment($scope.medicationObj.medicationEndDate).format('YYYY-MM-DD');
								if($scope.medicationDetails[$scope.medicationIndex].prescriptionId!=null && $scope.medicationDetails[$scope.medicationIndex].prescriptionId>0)
									{
									var prescriptionId = $scope.medicationDetails[$scope.medicationIndex].prescriptionId;
									 var found = $scope.medicationDetails.find(ar=>ar.prescriptionId==prescriptionId);
									   if(found==undefined)
										   {
										     $scope.addEmptyRow();
										     $scope.saveBtnFlag = false;
											 $scope.updateBtnFlag = true;
											 $scope.updateFlag = false;
											 $scope.updateFlagValidation = false;
											 $scope.saveFlag = true;
										   }
									   else{
										   $scope.medicationDetails.splice($scope.medicationIndex, 1);
										   $scope.addEmptyRow();
										    $scope.saveBtnFlag =false ;
											 $scope.updateBtnFlag = true;
											 $scope.updateFlag = false;
											 $scope.updateFlagValidation = false;
											 $scope.saveFlag = true;
									   }
									}
								else{
									$scope.updateFlag = false;
									$scope.updateFlagValidation = false;
									$scope.saveFlag = true;
									$scope.saveBtnFlag = true;
									$scope.updateBtnFlag = false;
									$scope.medicationDetails.splice($scope.medicationIndex, 1);
									 $scope.addEmptyRow();
								}
							}
							$scope.favouriteMedications = [];
							$scope.getFavoritesMedication = function() {
								var data = {
									doctorId :$scope.postCommonObj.doctorId,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId
								};
								var URI = BASE_URL + ROOT_URL + GETFAVOURITEMEDICATION;

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.favouriteMedications = response.data.listObject;
													}
												});
							}
							
							$scope.updateMedicationFavouriteStatus = function(medication) {
								$rootScope.startLoader();
								var data = {
									itemId : medication.itemId,
									doctorId : $scope.postCommonObj.doctorId,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									createdDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
									updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
								};

								if(medication.favorite_status == false)
		        				{
			        				 data.roaId = medication.roaId;
			        				 data.dose = medication.dose;
			        				 data.doseUnitId = medication.doseUnitId;
			        				 data.frequencyId = medication.frequencyId;
			        				 data.doctorFavoriteId=medication.doctorFavoriteId;
			        				 data.status = 'A';
		        				}
		        			else
		        				{
		        				data.doctorFavoriteId=medication.doctorFavoriteId;
		        				data.status = 'I';
		        				}
								var URI = BASE_URL + ROOT_URL + SAVEDOCTORFAVORITS;

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.getFavoritesMedication();
														  var medicationObj =  medication;
														  medicationObj["favorite_status"] =  !medicationObj["favorite_status"];
														  var indexOfMedication =  $scope.medicationDetails.indexOf(medication);
														  $scope.medicationDetails[indexOfMedication] = medicationObj;
														  $rootScope.stopLoader();
													}
												});
							}
							
							$scope.editMedication = function(index) {
								$scope.updateFlag = true;
								$scope.updateFlagValidation = true;
								$scope.saveFlag = false;
								$scope.item={};
								$scope.model={};
								$scope.label={};
								$scope.medicationObj = angular.copy($scope.medicationDetails[index]);
								$scope.medicationObj.medicationStartDate=moment($scope.medicationDetails[index].medicationStartDate,'YYYY-MM-DD').toDate();
								$scope.searchItem = $scope.medicationDetails[index].itemName;
								$scope.medicationObj.searchItem = $scope.medicationDetails[index].itemName;
								$scope.medicationObj.medicationName = $scope.medicationDetails[index].itemName;
								$scope.medicationObj.searchGeneric = $scope.medicationDetails[index].genericName;
								$scope.medicationObj.isGeneric ='N';
								$scope.medicationObj.nonFormularyAdvice ='N';
								$scope.medicationObj.prescribedDosageUnitList =[];
								$scope.medicationObj.searchItemFre = $scope.medicationDetails[index].frequencyDesc;
								$scope.medicationIndex = index;
								$scope.medicationDetails[index].genericId=$scope.medicationObj.genericId;
								$scope.medicationObj.itemStrength=$scope.medicationDetails[index].dose+" "+$scope.medicationDetails[index].strengthDesc;
								
								$scope.frequency_id=$scope.medicationDetails[index].frequencyId;
								$scope.frequencyDesc=$scope.medicationDetails[index].frequencyDesc;
								$scope.noOfAdministrationTime=$scope.medicationDetails[index].noOfAdministrationTime;
								$scope.item={
										frequency_id:$scope.frequency_id,
										frequency:$scope.frequencyDesc,
										noOfAdministrationTime:$scope.noOfAdministrationTime
								}
								$scope.model=$scope.medicationDetails[index].frequencyDesc;
								$scope.label=$scope.medicationDetails[index].frequencyDesc;
								$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label,index);
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getRoaList"
										+ "?unitId="
										+ $scope.unitId
										+ "&orgId="
										+ $scope.organizationId
										+ "&formulationTypeId="
										+ $scope.medicationDetails[index].formulationTypeId
										+ "&genericId="
										+ $scope.medicationDetails[index].genericId
										+ "&patientId="
										+ $scope.postCommonObj.patientId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.roa_options = response.data.listObject;
                                                //prescribed dosage unit list
					  										   $scope.medicationObj.prescribeDose="";
					  										var dataobj = {
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,
																	formulationTypeId:$scope.medicationDetails[index].formulationTypeId
															};
															var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
															GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																$rootScope.stopLoader();
																$scope.medicationObj.prescribedDosageUnitList=[];
																$scope.counterFormulation=1;
																$scope.medicationObj.formulationCounterIndex="";
																angular.forEach(response.data.listObject,function(value,index){
																	var unitObj={};
																	unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																	unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																	unitObj.formulationDosageToUnit=value.toConversionUnit;
																	unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																	unitObj.counterIndex=$scope.counterFormulation++;
																	unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																	unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																	unitObj.formulationFactorId=value.factorId;
																	unitObj.formulationDosage=value.dosageForm;
																	unitObj.formulationDosageMappId=value.formulationDosageMappId;
																	$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																});
																
																angular.forEach(response.data.listObject,function(value,index){
																	var unitObj={};
																	if(value.factorId==1){
																		unitObj.formulationDosageFromUnit=value.toConversionUnit;
																		unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																		unitObj.formulationDosageToUnit=value.fromConversionUnit;
																		unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																		unitObj.counterIndex=$scope.counterFormulation++;
																		unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																		unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																		unitObj.formulationFactorId=value.factorId;
																		unitObj.formulationDosage=value.dosageForm;
																		unitObj.formulationDosageMappId=value.formulationDosageMappId;
																		$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																	}
																});
																var list=[];
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
																	 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																        return $scope.medicationDetails[index].doseUnitId === obj.formulationDosageFromUnitId;
																    });
																	if(list.length>0){	
																		$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																	}
																}
																
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==2){
																	 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																        return $scope.medicationDetails[index].dosageForm === obj.formulationDosage;
																    });
																	if(list.length>0){	
																		$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																	}
																}
													});		
														
													}
												});

							}
							
							$scope.removeMedication = function(medication) {
								if(medication.checkFlagValidation != true){
								if ($scope.medicationDetails.length > 0) {
									var indexOfMedication = $scope.medicationDetails .indexOf(medication);
									$scope.medicationDetails.splice(indexOfMedication, 1);
								}
								}else{
									 $scope.medication= medication;
									 angular.element('#medicationDiagnosis').modal('show');
								}
							}
							
							$scope.removeMedicationOrdderset = function(){
								$scope.medication.checkFlagValidation=false;
								$scope.removeMedication($scope.medication);
							}
							
							$scope.activeMedications = [];
							$scope.getAllActiveMedication = function() {
								$scope.activeMedications = [];
								var patientId = $scope.postCommonObj.patientId;
								var dateObj = moment(new Date()).format('YYYY-MM-DD');

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEMEDICATION + "?patientId="
										+ patientId + "&date=" + dateObj+ "&unitId="+ $scope.unitId
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.activeMedications = response.data.listObject;
													
														angular.forEach($scope.activeMedications,function(value,key){
								    						value.checkTrue='N';
								    					});
													}
												});
							}
							
							//List Of Continued Medication of medication History
							$scope.continuedMedicationHistoryList = [];
							$scope.getContinuedMedicationHistoryList = function() {
								$scope.continuedMedicationHistoryList = [];
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									patientId : $scope.postCommonObj.patientId,
								};
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getContinuedMedicationList";
								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.continuedMedicationHistoryList = response.data.listObject;
														angular.forEach($scope.continuedMedicationHistoryList,function(value,key){
								    						value.checkTrue='N';
								    					});
													}
												});
							}
							$scope.getContinuedMedicationHistoryList();
							
							
							// Detailed Medication
							$scope.detailedMedications = [];
							$scope.getDetailedMedication = function() {
								$scope.detailedMedications = [];
								 var headerObj = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										};
									
									var paramObjForPer = {
											patientId : $scope.postCommonObj.patientId,
											doctorId : $scope.postCommonObj.doctorId
										};

									if($scope.postCommonObj.admissionId!=null){
										paramObjForPer.admissionId = $scope.postCommonObj.admissionId;
									}else{
										paramObjForPer.encounterId = $scope.postCommonObj.encounterId;;
									}
								var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsByDoctorId";
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
										.then(
												function(response) {
													if (response.data.status == "success") {
														if(response.data.listObject!=null && response.data.listObject.length>0){
															angular.forEach(response.data.listObject,function(value,index){
																if(value.isOtims!=null && value.isOtims){
																	$scope.detailedMedications.push(value);
																}
															});
														}
														//$scope.detailedMedications = response.data.listObject;
													}
												});
							}
							$scope.getDetailedMedication();
							
							
							
							
							$scope.deleteMedicationPendingStatus = function(){
								$rootScope.startLoader();
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
										    updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											prescriptionId:$scope.prescriptionId,
											organizationId:$scope.organizationId,
											justificationStop:$scope.justificationDeleteMedi,
											prescriptionDetailsid:$scope.prescriptionDetailsid1,
									};
									
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationDelete";
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success("Deleted Successfully..!!",
													{
														title : 'Success!'
													});
											$scope.getAllActiveMedication();
											$scope.getDetailedMedication();
											$scope.justificationDeleteMedi="";
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.warning(response.data.message,
													{
														title : 'Warning!'
													});
											$scope.getAllActiveMedication();
											$scope.getDetailedMedication();
											$scope.justificationDeleteMedi="";
										}
									});
								}catch(e){
									console.log("Exception",e);
								}
							}
							
							// save Post Operative Care Details
							$scope.savePostOperativeCareDetails = function(){
								$rootScope.startLoader();
							
								
								if($scope.medicationDetails.length > 0){
									var medicationDetails = $scope.medicationDetails;
									
									var medicationDetailList = [];
									if( $scope.medicationDetails[0].priorityId != "" && $scope.medicationDetails[0].priorityId != null && $scope.medicationDetails[0].priorityId != undefined){
										$scope.pri=$scope.medicationDetails[0].priorityId;
									}
								
								var prescriptionObj = {
										"organizationId" : $scope.organizationId,
										"unitId" : $scope.unitId,
										"visitTypeId" : $scope.postCommonObj.visitTypeId,
										"employeeId" : $scope.employeeId,
										"doctorId" : $scope.postCommonObj.doctorId,
										"prescriptionDate" :  moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										"authorisedBy" : $scope.postCommonObj.doctorId,
										"authorisedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										"addedBy" : $scope.userId,
										"addedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										"patientId" : $scope.postCommonObj.patientId,
										"priorityId" : $scope.pri,
										"isDischarge" : false,
										"deptId" : $scope.postCommonObj.specialityId
									};
									if($scope.postCommonObj.admissionId!=null){
										prescriptionObj.admissionId = $scope.postCommonObj.admissionId;
									}else{
										prescriptionObj.encounterId =$scope.postCommonObj.encounterId;
									}
									
									var flag = true;
									angular.forEach( medicationDetails, function(value, key) {
										var instructionId = value.instructionId;
										if (value.medicationStartDate != '' && value.medicationEndDate != '' && value.durationCount != ''
											&& value.durationUnit != '' && value.quantity != '') {
											var medicationDetailsObj = {};
											medicationDetailsObj["itemId"] = value.itemId;
											medicationDetailsObj["storeId"] = $scope.storeId;
											medicationDetailsObj["uomUnitId"] = value.uomTypeId;
											medicationDetailsObj["isMedicationStop"] = 'N';
											medicationDetailsObj["orderQty"] = value.quantity;
											medicationDetailsObj["strength"] = value.strength;
											medicationDetailsObj["prescriptionDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											medicationDetailsObj["authorisedBy"] = $scope.postCommonObj.doctorId;
											medicationDetailsObj["authorisedDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											medicationDetailsObj["dose"] = value.dose;
											medicationDetailsObj["itemName"] = value.medicationName;
											medicationDetailsObj["genericName"] = value.genericName;
											medicationDetailsObj["doseUnitId"] = value.doseUnitId;
											medicationDetailsObj["frequencyId"] = value.frequencyId;
											medicationDetailsObj["routeId"] = value.roaId;
											medicationDetailsObj["isNonFormulary"] = value.nonFormularyAdvice;
											medicationDetailsObj["startDate"] = value.medicationStartDate+ " 00:00:00";
											medicationDetailsObj["stopDate"] = value.medicationEndDate+ " 00:00:00";
											medicationDetailsObj["statflag"] = value.isStat!=null?value.isStat:false;
											medicationDetailsObj.isDischarge = value.isDischarge;
											if(value.prescribedDosageUnitList != undefined){
												if(value.prescribedDosageUnitList.length>0 && value.formulationCounterIndex!="" && value.formulationCounterIndex!=null
														&& value.formulationCounterIndex!=undefined){
															var list=$.grep(value.prescribedDosageUnitList, function (obj) {
															    return value.formulationCounterIndex === obj.counterIndex;
															});
															if(list.length>0){
																if(list[0].formulationFactorId==1){
																	medicationDetailsObj.prescribedDose=value.prescribeDose;
																	medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
																	medicationDetailsObj.factorId=list[0].formulationFactorId;
																	medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																}else{
																	medicationDetailsObj.factorId=list[0].formulationFactorId;
																	medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																}
															}
																
												}
											}
											if(value.instructionId != '' && value.instructionId != undefined  && value.instructionId != null ){
												medicationDetailsObj["instructionId"] = value.instructionId;
											}else{
												medicationDetailsObj["instructionDesc"] = value.instructionDesc;
											}
											medicationDetailList.push(medicationDetailsObj);
										} else {
											flag = false;
										}
														
									});
									if (medicationDetailList.length > 0) {
										prescriptionObj.prescriptionDetailsDtosList = medicationDetailList;
									}
								
								}else{
									growl.warning("Please add medication !!",
											{
											title : 'Warning!'
											});
									return false;
								}
								
								var data = {
									prescriptionDto :prescriptionObj,
									oTCompletionNoteDtoList : $scope.followUpNoteList,
									postOperativeCareId : $scope.postOperativeCareId !=null ? $scope.postOperativeCareId : null,
									proReqId : $scope.postCommonObj.proReqId,
									status : 'A',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdBy : $scope.userId,
									createdDate : moment( new Date()).format('DD-MM-YYYY HH:mm:ss'),
									updatedBy : $scope.userId,
									updatedDate : moment( new Date()).format('DD-MM-YYYY HH:mm:ss'),
									//coversheetInstructionDto :activityInstructionObj,
									//otMealList : otMealList
								};
								
								console.log("data",data);
								$rootScope.stopLoader();
								var URI = BASE_URL + ROOT_URL + "/api/otims/PostOperativeCareDetails/savePostOperativeCare";

								GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(
										function(response) {
										if (response.data.status == "success") {	
										//medication clear data
											
											growl.success(response.data.message,
													{
													title : 'Success!'
													});
											
											$scope.updateFlagValidation = false;
											$scope.medicationDetails = [];
											$scope.searchItem="";
											$scope.searchItemFre="";
											$scope.medicationIndex = undefined;
											$scope.medicationObj = {
												itemId : "",
												searchItem:"",
												searchItemFre:"",
												formulationTypeId : '',
												favorite_status : false,
												medicationStartDate : new Date(),
												roaId : '',
												dose : '',
												doseUnitId : '',
												durationCount : "",
												durationUnit : 'Day',
												instructionId : '',
												medicationEndDate : '',
												frequencyId : '',
												quantity : '',
												uomTypeId : '',
												conversions : '',
												strength : '',
												priorityId : '2',
												priorityCode : 'NR',
												isGeneric:'N',
												isInstruction:'N',
												nonFormularyAdvice:'N',
												searchItemFre : "",
												isStat : false,
												isDischarge : false

											};
											$scope.initMedicationObj();
											$scope.selectedOrderSet=[];
											$scope.getAllActiveMedication();
											$scope.getDetailedMedication();
											$scope.medicationObj.itemStrength="";
											$scope.medicationObj.prescribedDosageUnitList=[];
											
											//Activity Instruction Clear data
											$scope.getInstructions();
											$scope.advicedInstruction='';
											$scope.addedInstructions = [];
											
											//Special Instruction Clear data
											$scope.getSplInstructions();
											$scope.advicedSplInstruction='';
											$scope.addedSplInstructions = [];
											
											$scope.getFollowUpNotes();
											$rootScope.stopLoader();
										}else{
												$rootScope.stopLoader();
										}
								});
								
							}
							
							
							
							/** *********Medication Template code start***************** */
							$scope.getMedicationTemplate = function(){
								$scope.savedTemplateList=[];
								$scope.presTemplateList=[];
								var headerObj={};
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										typeId: 3,
										doctorId:$scope.patientDetails.doctorId
										
								}
								var URI = BASE_URL + ROOT_URL + "/api/opd/getCoversheetAsTemaplateList";
								GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success")
												$scope.savedTemplateList = response.data.listObject;
										});

							}
							$scope.getMedicationTemplate();
							$scope.covTemplateName="";
							$scope.selectedCovTemplateId="";
							$scope.getPrescriptionBycovTemplateId=function(covTemplateId)
								{
								$rootScope.startLoader();
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										covTemplateId:covTemplateId
										
								}
								
								angular.forEach($scope.savedTemplateList, function(value,key){
									if(covTemplateId == value.covTemplateId){
										$scope.covTemplateName=value.templateName;
										$scope.selectedCovTemplateId=value.covTemplateId;
									}
								})
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/getTemplateDataByCovTempId";
								GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											console.log(response);
											if (response.data.status == "success")
											$scope.presTemplateList=response.data.listObject[0].listPrescriptionData;
											$scope.addedMedList();
					                       /*
											 * $scope.medicationObj.medicationStartDate =
											 * (moment($scope.medicationObj.medicationStartDate1,
											 * "YYYY-MM-DD").toDate()).getTime();
											 */
											$rootScope.stopLoader();
										});
								
								
								 
							}
							$scope.addedMedList=function(){
								$scope.withoutTemplateMedications = [];
								angular.forEach($scope.medicationDetails, function(val ,key){
									if(val.isCovtemplate == undefined || val.isCovtemplate == null){
										$scope.withoutTemplateMedications.push(val);
									}
									
								});	
								
								$scope.medicationDetails = [];
								$scope.medicationDetails = $scope.withoutTemplateMedications;
								
								
								$scope.medicationDetailsNew=[];
								if($scope.medicationDetailsNew != null && $scope.medicationDetailsNew.length > 0){
									angular.forEach($scope.medicationDetails, function(value, key){

								if( (value.itemName == undefined || value.itemName == null  ||  value.itemName == "")
										|| (value.medicationEndDate == undefined || value.medicationEndDate == null || value.medicationEndDate == "")
										|| (value.durationCount == undefined || value.durationCount == null || value.durationCount == "")
										|| ((value.quantity == undefined || value.quantity == null || value.quantity == "") || (value.orderQty == undefined || value.orderQty == null || value.orderQty == ""))
										|| (value.frequencyDesc == undefined || value.frequencyDesc == null || value.frequencyDesc == "")
										|| (value.frequencyId == undefined || value.frequencyId == null || value.frequencyId == "")
										)
									{
									growl.warning("Please update the medication.");
									return false;
									$rootScope.stopLoaderPopup();
									}
								});
								}
								  
								  angular.forEach($scope.presTemplateList, function(value, key){	
										if(value.itemName != null ){
											$scope.medicationObjNew = {};
											$scope.medicationObjNew.itemId = value.itemId;
											$scope.medicationObjNew.doctorFavoriteId=value.doctorFavoriteId;
											$scope.medicationObjNew.quantity = value.quantity;
											$scope.medicationObjNew.orderQty = value.quantity;
											$scope.medicationObjNew.durationCount = value.durationCount;
					// $scope.searchItem = value.itemName;
											$scope.medicationObjNew.frequencyId = value.frequencyId;
											$scope.medicationObjNew.frequencyDesc = value.frequencyDesc;
											$scope.model=value.frequencyDesc;
											$scope.label=value.frequencyDesc;
											$scope.medicationObjNew.visitTypeId = 2;
											$scope.medicationObjNew.instructionDesc = (value.instructionDesc != undefined && value.instructionDesc != null && value.instructionDesc != "") ? value.instructionDesc :  value.instructionText;						$scope.medicationObjNew.instructionId = value.instructionId;
											$scope.medicationObjNew.durationCount = value.durationCount;
											$scope.medicationObjNew.durationUnit = value.durationUnit;
					// $scope.medicationObjNew.medicationStartDate = new Date();
					// $scope.medicationObjNew.prescribeDose =value.dosageForm;
											$scope.medicationObjNew.prescribeDose =value.prescribeDose;
											$scope.medicationObjNew.prescriptionDoseUnit =value.prescribeDoseUnit;
											$scope.medicationObjNew.itemName = value.itemName;
											$scope.medicationObjNew.roaDesc = value.roaDesc;
											$scope.medicationObjNew.genericName = value.genericName;
					// $scope.medicationObjNew.searchGeneric = value.genericName;
											$scope.medicationObjNew.genericId = value.genericId;
											$scope.medicationObjNew.formulationTypeId = value.formulationTypeId;
											$scope.medicationObjNew.isGeneric ='N';
					                        $scope.medicationObjNew.nonFormularyAdvice = value.isNonFormulary!= undefined ? value.isNonFormulary :'N';
											$scope.medicationObjNew.isInstruction='N';
											$scope.medicationObjNew.priorityCode=value.priorityCode;
											$scope.medicationObjNew.priorityId =value.priorityId;

					// $scope.medicationObjNew.searchItem =value.itemName;;
					// $scope.medicationObjNew.searchItemFre = value.frequencyDesc;
											$scope.medicationObjNew.frequencyId = value.frequencyId;
											$scope.medicationObjNew.rate = value.rate;
											$scope.medicationObjNew.totalRate= parseInt($scope.medicationObjNew.rate * (($scope.medicationObjNew.quantity!=undefined && $scope.medicationObjNew.quantity!="") ? $scope.medicationObjNew.quantity :0));
											$scope.frequency_id=value.frequencyId;
											$scope.frequency=$scope.medicationObjNew.searchItemFre;
											 $scope.medicationObjNew.medicationType =1;
											$scope.medicationObjNew.noOfAdministrationTime=value.noOfAdministrationTime;
											$scope.medicationObjNew.formulationDosageMappId=value.formulationDosageMappId;
											$scope.medicationObjNew.prescribedDosageUnitList=[];

											$scope.noOfAdministrationTime=value.noOfAdministrationTime;
											$scope.item={
													frequency_id:$scope.frequency_id,
													frequency:$scope.medicationObjNew.searchItemFre,
													noOfAdministrationTime:$scope.noOfAdministrationTime
											}
											
											$scope.model=value.frequencyDesc;
											$scope.label=value.frequencyDesc;
											if ($scope.medicationObjNew.instructionId != null && $scope.medicationObjNew.instructionId!=""
												&& $scope.medicationObjNew.instructionId!=undefined) {
												var instructionDesc = $.grep($scope.medicationInstructions,
																function(disCat) {
																	return disCat.instructionId == $scope.medicationObjNew.instructionId;
																})[0].instructionDesc;
												$scope.medicationObjNew.instructionDesc = instructionDesc;
											} else if(value.instructionText != undefined && value.instructionText != null && value.instructionText != "") {
												$scope.medicationObjNew.instructionDesc = value.instructionText;
												$scope.medicationObjNew.instructionText = value.instructionText;
												$scope.medicationObjNew.instructionTextFlag=true;
											}else {
												$scope.medicationObjNew.instructionDesc = "No instruction";
											}
											$scope.medicationObjNew.isStat=value.isStat;
												if($scope.medicationObjNew.isStat==true){
													$scope.medicationObjNew.isStat="stat";
												}else{
													$scope.medicationObjNew.isStat==false;
												}
											
											 if ($scope.medicationObjNew.isStat =="stat") {
													
													$scope.medicationObjNew.isStat = true;
													} else {
														$scope.medicationObjNew.isStat = false;
													}

											$scope.flagActiMed = true;
											$scope.medicationObj.prescribedDosageUnitList=[];
											$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
											if((value.formulationTypeId != undefined && value.formulationTypeId != null && value.formulationTypeId!= "")
													&& (value.genericId != undefined && value.genericId != null && value.genericId!= "")){

											var formulationTypeId = value.formulationTypeId;
											var URI = BASE_URL
													+ ROOT_URL
													+ "/api/opd/getRoaList"
													+ "?unitId="
													+ $scope.unitId
													+ "&orgId="
													+ $scope.organizationId
													+ "&formulationTypeId="
													+ formulationTypeId
													+ "&genericId="
													+ value.genericId
													+ "&patientId="
													+ $scope.patientDetails.patientId;

											GenericServiceParamHeader
													.serviceAction(METHOD_GET, URI)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	if (response.data.listObject.length !== 0) {
																		var responseList = response.data.listObject;
																		$scope.roa_options = responseList;
																	}
																}
															});
											}
											 if(value.roaId != null){
												 $scope.medicationObjNew.roaId =(value.roaId)
													.toString();
							                 }
											 $scope.medicationObjNew.dose = value.dose;
												$scope.medicationObjNew.strengthDesc=value.strengthDesc;
							                $scope.medicationObjNew.isDischarge = value.isDischarge;
											$scope.medicationObjNew.doseUnitId = value.doseUnitId!=null ? value.doseUnitId.toString():0;
											$scope.medicationObjNew.itemStrength=(value.dose != null ?value.dose :"")+" "+(value.doseDesc != null ? value.doseDesc :"");
											$scope.medicationObjNew.unit = value.doseDesc;
											$scope.medicationObjNew.prescriptionDoseUnit =value.prescribeDoseUnit;
											var sDT =  moment((new Date()),"DD-MM-YYYY").toDate();
							                var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
							                var startDT =  moment(sDT).format('YYYY-MM-DD');
							                var endDT = moment(eDT).format('YYYY-MM-DD');
							                $scope.medicationObjNew.medicationStartDate =  (moment(startDT, 'YYYY-MM-DD').toDate()).getTime();
							                $scope.medicationObjNew.startDateNew = startDT;
							                $scope.medicationObjNew.endDateNew = endDT;
							                $scope.medicationObjNew.endDate = endDT;
							                $scope.medicationObjNew.medicationStartDate1 = startDT;
							                if($scope.medicationObjNew.durationUnit == "Day") {
												var durationCount = parseInt($scope.medicationObjNew.durationCount);
												}
											    else if($scope.medicationObjNew.durationUnit == "Week") {
													var durationCount = parseInt($scope.medicationObjNew.durationCount)
													$scope.medicationObjNew.durationCount = durationCount* 7;
												}
												else if($scope.medicationObjNew.durationUnit == "Month") {
													var durationCount = parseInt($scope.medicationObjNew.durationCount)
													$scope.medicationObjNew.durationCount = durationCount* 30;
												}
												else if($scope.medicationObjNew.durationUnit == "Year") {
													var durationCount = parseInt($scope.medicationObjNew.durationCount)
													var months = durationCount * 12
													$scope.medicationObjNew.durationCount = months* 30;
												}
										
										if ($scope.medicationObjNew.durationCount && $scope.medicationObjNew.durationUnit && $scope.medicationObjNew.medicationStartDate) {
											if($scope.medicationObjNew.durationUnit == "Day") {
												var endDate = moment($scope.medicationObjNew.medicationStartDate).add($scope.medicationObjNew.durationCount-1, 'days').format('YYYY-MM-DD');
											}
											else if($scope.medicationObjNew.durationUnit == "Week") {
												var days = $scope.medicationObjNew.durationCount * 7
												var endDate = moment($scope.medicationObjNew.medicationStartDate).add($scope.medicationObjNew.durationCount, 'days').format('YYYY-MM-DD');
											}
											else if($scope.medicationObjNew.durationUnit == "Month") {
												var days = $scope.medicationObjNew.durationCount * 30
												var endDate = moment($scope.medicationObjNew.medicationStartDate).add($scope.medicationObjNew.durationCount, 'days').format('YYYY-MM-DD');
											}
											else if($scope.medicationObjNew.durationUnit == "Year") {
												var days = $scope.medicationObjNew.durationCount * 365;
												var endDate = moment($scope.medicationObjNew.medicationStartDate).add($scope.medicationObjNew.durationCount, 'days').format('YYYY-MM-DD');
											}
											$scope.medicationObjNew.medicationEndDate = endDate;
											$scope.medicationObjNew.medicationEndDate1 = endDate;
										}
										// added by chetana
										 if($scope.medicationObjNew.isDischarge == true){
												$scope.medicationObjNew.medicationType = 2;
												$scope.medType=$scope.medicationObjNew.medicationType;// discharge
											}else{
												$scope.medicationObjNew.medicationType = 1;// Administered
												$scope.medType=$scope.medicationObjNew.medicationType;
											}
						                   if ($scope.medicationObjNew.isDischarge ==true) {
													
					// $scope.medicationObjNew.isDischarge = "discharge";
													$scope.medicationObjNew.isDischarge = true;
													 $scope.flagDisch=true;
													} else {
														$scope.medicationObjNew.isDischarge = false;
													}
											 if ($scope.medicationObjNew.isStat ==true) {
													
					// $scope.medicationObjNew.isStat = "stat";
													$scope.medicationObjNew.statFlag = true;
													$scope.flagStat=true;
													} else {
														$scope.medicationObjNew.isStat = false;
													}
								                $scope.medicationObjNew.medicationStartDate = moment($scope.medicationObjNew.medicationStartDate).format('YYYY-MM-DD');
										if($scope.medicationDetailsNew.length>0){
					// $scope.medicationObjNew.medicationStartDate =
					// moment($scope.medicationObjNew.medicationStartDate).format('YYYY-MM-DD');
							                angular.forEach($scope.medicationDetailsNew,function(value,index){
							                    
							                    if ( $scope.medicationObjNew.searchItem == value.itemName
							                            && $scope.medicationObjNew.medicationStartDate == value.medicationStartDate
					// && $scope.medicationObjNew.medicationEndDate == value.medicationEndDate
							                            && $scope.medicationObjNew.durationCount == value.durationCount
							                            && $scope.medicationObjNew.searchItemFre == value.frequencyDesc
							                            && $scope.medicationObjNew.doseUnitId == value.doseUnitId
							                            && $scope.medicationObjNew.roaId == value.roaId
							                            && $scope.medicationObjNew.quantity == value.quantity) {
							                        
							                        $scope.medicationExist =true;
							                    }
							                });
							            }
										
										   if($scope.detailedMedications.length>0){
					// $scope.medicationObjNew.medicationStartDate =
					// moment($scope.medicationObjNew.medicationStartDate).format('YYYY-MM-DD');
							                   angular.forEach($scope.detailedMedications,function(value,index){
							                       
							                       var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
							                       var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
							                       var startDT =  moment(sDT).format('YYYY-MM-DD');
							                       var endDT = moment(eDT).format('YYYY-MM-DD');
							                       
							                       if ( $scope.medicationObjNew.searchItem == value.itemName
							                               && $scope.medicationObjNew.medicationStartDate ==  startDT
					// && $scope.medicationObjNew.medicationEndDate == endDT
							                               && $scope.medicationObjNew.searchItemFre == value.frequencyDesc
							                               && $scope.medicationObjNew.dose== value.doseDesc
							                               && $scope.medicationObjNew.doseUnitId == value.doseUnitId
							                               && $scope.medicationObjNew.quantity == value.orderQty
							                               && $scope.medicationObjNew.roaId == value.roaId
							                               && $scope.medicationObjNew.isStat == value.statflag) {
							                           
							                           $scope.medicationExist =true;
							                       }
							                   });
							                   
							               }
										   
										   if($scope.medicationExist){
							                   growl.warning(
							                           "Medication Already Exist",
							                           {
							                               title : 'Warning!'
							                           });
							                   $scope.medicationObjNew.medicationStartDate = new Date();
							                   
							           $rootScope.stopLoader();
							           return false;
							           }
										   
											$scope.medicationDetailsNew.push($scope.medicationObjNew);
												 
												 $scope.totalSumOfRate = 0;
													angular.forEach($scope.medicationDetailsNew , function(value1,key1){
														$scope.totalSumOfRate+=value1.totalRate;
												   	});
												 console.log("Listtttt",$scope.medicationDetailsNew);
												 
												 // get formuation dosage list
											$scope.medicationObjNew.priorityId = '2';
//											
											}
										$scope.medicationObjNew.strengthDesc = value.strengthDesc;
										$scope.medicationObjNew.dose = value.strength;
										$scope.medicationObjNew.roaId = value.roaId;
										$scope.medicationObjNew.quantity = value.quantity;
										$scope.medicationObjNew.strength = value.strength;
										$scope.medicationObjNew.isCovtemplate = true;
										$scope.medicationObjNew.rate = $scope.medicationObjNew.rate;
										 if($scope.medicationObjNew.rate  == undefined){
					                       $scope.medicationObjNew.rate = 0;
					                   }
										$scope.medicationObjNew.totalRate=0;
										 $scope.medicationObjNew.totalRate=$scope.medicationObjNew.rate * $scope.medicationObjNew.quantity;
										
										$scope.medicationDetails.push($scope.medicationObjNew);
										})
										$scope.medicationDetailsNewList=[];
								  var i=0;
								  $scope.medTemp = [];
								  angular.forEach($scope.medicationDetails , function(val, key){
									  if(i==0)
										  {
										  $scope.medTemp.push(val);
										  }
									  else{
										  var check =false;
										  angular.forEach($scope.medTemp , function(val1, key1){
											  if(val.itemId == val1.itemId && val.itemName == val1.itemName){
												  check =true;
											  }
										  });
										  if(check== false)
											  {
											  $scope.medTemp.push(val);
											  }
									  }
									  i++;
								  });
								  $scope.medicationDetails=[];
								  $scope.medicationDetails=$scope.medTemp;
								  $scope.totalSumOfRate = 0;
									angular.forEach($scope.medicationDetails , function(value1,key1){
										$scope.totalSumOfRate+=value1.totalRate;

								   	});
									
									angular.forEach($scope.medicationDetails,function(value){
					                    if(value.itemId != null){
					                        	   var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.ITEM_MASTER.DETAILS_API  +$scope.organizationId + '/' + value.itemId;
					                              var data = {};
					                               GenericService.serviceAction("GET", URI, data).then(function (response) {
					                                     if(response.data.unitOfMeasurementDetailsResponse!=null)
					                                     {
					                                     	response.data.unitOfMeasurementDetailsResponse.forEach(function(item){

					                                    	   value.uom =item.uomList;
					                                    	   value.selectedUom = $scope.uomUnits.find(function(units){
					                                             return value["uom"][2].uomUnitId == units.id;
					                                           });

					                             	      });
					                                     	
					                                
					                                     	}
					                               });
					                    }
					             });
							}
							
							$scope.setTemplateName=function(){
								if( $scope.medicationDetails != null &&  $scope.medicationDetails.length > 0){
									$("#coversheetTemplateModel").show();
								}else{
									growl.warning("Please add medicine.");
								}
								
							}
							$scope.saveCovAsTemplate=function(){
								count=0;
								if($scope.medicationDetails != null && $scope.medicationDetails.length > 0){
									angular.forEach($scope.medicationDetails, function(value, key){
										if( (value.itemName == undefined || value.itemName == null  ||  value.itemName == "")
												|| (value.medicationEndDate == undefined || value.medicationEndDate == null || value.medicationEndDate == "")
												|| (value.durationCount == undefined || value.durationCount == null || value.durationCount == "")
												|| ((value.quantity == undefined || value.quantity == null || value.quantity == "") || (value.orderQty == undefined || value.orderQty == null || value.orderQty == ""))
												|| (value.frequencyDesc == undefined || value.frequencyDesc == null || value.frequencyDesc == "")
												|| (value.frequencyId == undefined || value.frequencyId == null || value.frequencyId == "")
												)
										{
											count++;
											
											}
										});
								}
								

								var medicationDetailsList = [];
								
								angular.forEach( $scope.medicationDetails, function(val, key){
									medicationDetailsList.push(val.selectedUom);
									 delete val.selectedUom;
								});

								var data = {

									      "covTemplateId": ($scope.covTemplateId != undefined  && $scope.covTemplateId != null && $scope.covTemplateId != "") ? $scope.covTemplateId : "",

									      "createdBy": $scope.userId,
									      "visitTypeId": 2,
									      "createdDate": moment(new Date).format('DD-MM-YYYY HH:mm:ss'),

									      "doctorId": $scope.postCommonObj.doctorId,

									      "encounterId": $scope.postCommonObj.admissionId != null ? $scope.postCommonObj.admissionId : $scope.postCommonObj.encounterId,

									      "organizationId": $scope.organizationId,

									      "patientId": $scope.postCommonObj.patientId,

									      "status": "A",

									      "templateName": $scope.covTemplateName,

									      "unitId": $scope.unitId,

									      "updatedBy": $scope.userId,

									      "updatedDate": moment(new Date).format('DD-MM-YYYY HH:mm:ss'),

									      "typeId": 3,// for laborder

									      "listPrescriptionData": $scope.medicationDetails

									    }
								if(count == 0){
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveTemplateListDataForNewEMR";
									GenericService.serviceAction("POST", URI, data).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.$broadcast('show-errors-reset');
											$scope.covTemplateName="";
											$scope.covTemplateId="";
											$scope.selectedCovTemplateId="";
											$scope.initMedicationObj();
											// $scope.totalSumOfRate=0;
											$scope.selectedOrderSet=[];
											$scope.medicationObj.itemStrength="";
											$scope.medicationObj.prescribedDosageUnitList=[];
											$("#coversheetTemplateModel").hide();
											$scope.getMedicationTemplate();
											$rootScope.stopLoaderPopup();
										} else if(response.data.status == 'error')
										{
											
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											$rootScope.stopLoaderPopup();
										}
										
										angular.forEach( $scope.medicationDetails, function(val, key){
											  val.selectedUom = medicationDetailsList[key];
										});
										
									});
								}else{
									
									angular.forEach( $scope.medicationDetails, function(val, key){
										  val.selectedUom = medicationDetailsList[key];
									});
									
									growl.warning("Please update the medication.");
									return false;
									$rootScope.stopLoaderPopup();
								}
								
							}
							
							
							$scope.closeCoveTempModal = function(){
								$("#coversheetTemplateModel").hide();
							}
							
							
				            $scope.validateCIMMSAllergy = function(){
								$scope.allergyData =[];
								$scope.medicationData =[];
								$scope.diagnosisData =[];
								
								angular.forEach($scope.allergyCurrentList,function(value){
									if(value.cimsCode != null)
									$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
								});
								
								angular.forEach($scope.allergyOldList,function(value){
									if(value.cimsCode != null)
									$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
								});
								
									angular.forEach($scope.allergenList,function(value){
										if($scope.patientAllergyObject.allergyId == value.allergyId){
										if(value.cimsCode != null)
										$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
										}
									});
									
									angular.forEach($scope.medicationDetails ,function(value){
										if(value.cimsCode != null)
										$scope.medicationData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
									});
									$scope.todaysdate =  moment(new Date()).format('DD/MM/YYYY');
									angular.forEach($scope.detailedMedications ,function(value){
// if($scope.todaysdate >= value.startDate && $scope.todaysdate <= value.endDate
// ){
										if(value.cimsCode != null){
										$scope.medicationData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
										}
// }
									});
									
									angular.forEach($scope.dataListDiagnosis ,function(value){
										if(value.code != null)
										$scope.diagnosisData.push("<HealthIssueCode code='"+value.code +"'  codeType='ICD 10'/>");
									});
									
									angular.forEach($scope.patientDiagnosisList ,function(value){
										if(value.code != null)
										$scope.diagnosisData.push("<HealthIssueCode code='"+value.code +"'  codeType='ICD 10'/>");
									});
									
									console.log("allergyData",$scope.allergyData);
									var data={
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											reqXml: "<Request><Interaction>"+
											"<Prescribing>"+$scope.medicationData+"</Prescribing>"+
											"<Allergies>"+$scope.allergyData+"</Allergies>"+
											"<HealthIssueCodes>"+$scope.diagnosisData+"</HealthIssueCodes>"+
											"<References/></Interaction></Request>"
										}
									console.log("data",data);
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveCIMMS";
									
									GenericService.serviceAction("POST",URI,data).then(function(response){
										
										if (response.data.status == 'success')
										{
											$scope.cimsResponseObject = response.data.object;
											$scope.Result = response.data.object;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
										} else if(response.data.status == 'error')
										{
											
											$scope.Result = response.data.message;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
				                              
										}
									});
									$rootScope.stopLoader();
							}
							
							$scope.validateCIMMS = function(){
								
								$scope.allergyData =[];
								$scope.medicationData =[];
								$scope.diagnosisData =[];
								
									angular.forEach($scope.allergyCurrentList,function(value){
										if(value.cimsCode != null)
										$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
									});
									
									angular.forEach($scope.allergyOldList,function(value){
										if(value.cimsCode != null)
										$scope.allergyData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
									});
									
									angular.forEach($scope.medicationDetails ,function(value){
										if(value.cimsCode != null)
										$scope.medicationData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
									});
									$scope.todaysdate =  moment(new Date()).format('DD/MM/YYYY');
									angular.forEach($scope.detailedMedications ,function(value){
// if($scope.todaysdate >= value.startDate && $scope.todaysdate <= value.endDate
// ){
										if(value.cimsCode != null){
										$scope.medicationData.push("<"+ value.cimsType +" reference='"+"{"+ value.cimsCode +"}"+"'/>");
										}
// }
									});
									
									angular.forEach($scope.patientDiagnosisList ,function(value){
										if(value.code != null)
										$scope.diagnosisData.push("<HealthIssueCode code='"+value.code +"'  codeType='ICD 10'/>");
									});
									
									console.log("allergyData",$scope.allergyData);
									var data={	
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											reqXml: "<Request><Interaction>"+
											"<Prescribing>"+$scope.medicationData+"</Prescribing>"+
										// "<Allergies>"+$scope.allergyData+"<SubstanceClass
										// reference='{8B9ECB52-CDC9-4460-AB40-6E3C7DF38B1E}'/><SubstanceClass
										// reference='{9E775009-231D-728D-E034-080020E1DD8C}'/></Allergies>"+
											"<Allergies>"+$scope.allergyData+"</Allergies>"+
											"<HealthIssueCodes>"+$scope.diagnosisData+"</HealthIssueCodes>"+
											"<References/></Interaction></Request>"
										}
									
									
									/* * var data = { reqXml: "<Request><Interaction><Prescribing><GGPI
									 * reference='{488F9F61-5D37-4989-925E-1742FFFDAA9E}'/><GGPI
									 * reference='{B3E6B75E-9519-6AE7-E034-080020E1DD8C}'/><GGPI
									 * reference='{BF33752F-6062-0589-E034-0003BA299378}'/><GGPI
									 * reference='{B3E696CF-71F8-6518-E034-080020E1DD8C}'/></Prescribing><Allergies><Molecule
									 * reference='{5143B2B1-DBED-45A2-8308-61636C3A2F0B}'/><Molecule
									 * reference='{7142A19F-EDE2-4A7C-A30B-06AB60B6B71F}'/><Molecule
									 * reference='{74DCDDAF-9349-430F-A2F2-B7F2BC5CD8AA}'/><SubstanceClass
									 * reference='{8B9ECB52-CDC9-4460-AB40-6E3C7DF38B1E}'/><SubstanceClass
									 * reference='{9E775009-231D-728D-E034-080020E1DD8C}'/></Allergies><HealthIssueCodes><HealthIssueCode
									 * code='J45' codeType='ICD10'/><HealthIssueCode
									 * code='N17' codeType='ICD10'/><HealthIssueCode
									 * code='I49.5' codeType='ICD10'/></HealthIssueCodes><References/></Interaction></Request>"
									 * //reqXml: "<Request><Interaction><Prescribing><GGPI
									 * reference='{488F9F61-5D37-4989-925E-1742FFFDAA9E}'/><GGPI
									 * reference='{B3E6B75E-9519-6AE7-E034-080020E1DD8C}'/><GGPI
									 * reference='{BF33752F-6062-0589-E034-0003BA299378}'/><GGPI
									 * reference='{B3E696CF-71F8-6518-E034-080020E1DD8C}'/></Prescribing><Allergies><Molecule
									 * reference='{5143B2B1-DBED-45A2-8308-61636C3A2F0B}'/><Molecule
									 * reference='{7142A19F-EDE2-4A7C-A30B-06AB60B6B71F}'/><Molecule
									 * reference='{74DCDDAF-9349-430F-A2F2-B7F2BC5CD8AA}'/><SubstanceClass
									 * reference='{8B9ECB52-CDC9-4460-AB40-6E3C7DF38B1E}'/><SubstanceClass
									 * reference='{9E775009-231D-728D-E034-080020E1DD8C}'/></Allergies><HealthIssueCodes><HealthIssueCode
									 * code='J45' codeType='ICD10'/><HealthIssueCode
									 * code='N17' codeType='ICD10'/><HealthIssueCode
									 * code='I49.5' codeType='ICD10'/></HealthIssueCodes><References/></Interaction></Request>" }*/
									 
									console.log("data",data);
// return false;
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveCIMMS";
									
									GenericService.serviceAction("POST",URI,data).then(function(response){
										
										if (response.data.status == 'success')
										{
											$scope.cimsResponseObject = response.data.object;
											$scope.Result = response.data.object;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
										} else if(response.data.status == 'error')
										{
											
											$scope.Result = response.data.message;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
				                              
										}
									});
									$rootScope.stopLoader();
							}
							
							$scope.checkInformationRelatedToDrug = function(){
								
								if($scope.cimsOption =='Y'){
								    $scope.medicationData =[];
								
								    $scope.medicationData.push("<"+ $scope.cimsType +" reference='"+"{"+ $scope.cimsCode +"}"+"'/>");
									var data={	
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											reqXml: "<Request>"+
											"<Content>"+$scope.medicationData+"</Content>"+
											"</Request>"
										}
									console.log("data",data);
// return false;
									var URI = BASE_URL + ROOT_URL + "/api/opd/saveCIMMS";
									
									GenericService.serviceAction("POST",URI,data).then(function(response){
										
										if (response.data.status == 'success')
										{
											$scope.cimsResponseObject = response.data.object;
											$scope.Result = response.data.object;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
										} else if(response.data.status == 'error')
										{
											
											$scope.Result = response.data.message;
						    				   var printWindow = window.open('', '', 'height=500,width=700');  
				                              printWindow.document.write($scope.Result);  
				                              printWindow.document.close();  
				                              
										}
									});
									$rootScope.stopLoader();
								}else{
									
									if($scope.cimsCode != undefined && $scope.cimsCode != null && $scope.cimsCode != ''){
										var New_EMR = "https://pharman.his.clinivantage.co.in/pharmandoctor?drugId=" +parseInt($scope.cimsCode);
										window.open(New_EMR,'popUpWindow','height=700,width=1000');
									}
									
								}
							}
							
							$scope.checkInformationRelatedToDrugItem = function(info){
								if($scope.cimsOption =='Y'){
							    $scope.medicationData =[];
							
							    $scope.medicationData.push("<"+ info.cimsType +" reference='"+"{"+ info.cimsCode +"}"+"'/>");
								var data={
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										reqXml: "<Request>"+
										"<Content>"+$scope.medicationData+"</Content>"+
										"</Request>"
									}
								console.log("data",data);
								var URI = BASE_URL + ROOT_URL + "/api/opd/saveCIMMS";
								
								GenericService.serviceAction("POST",URI,data).then(function(response){
									
									if (response.data.status == 'success')
									{
										$scope.cimsResponseObject = response.data.object;
										$scope.Result = response.data.object;
					    				   var printWindow = window.open('', '', 'height=500,width=700');  
			                              printWindow.document.write($scope.Result);  
			                              printWindow.document.close();  
									} else if(response.data.status == 'error')
									{
										
										$scope.Result = response.data.message;
					    				   var printWindow = window.open('', '', 'height=500,width=700');  
			                              printWindow.document.write($scope.Result);  
			                              printWindow.document.close();  
			                              
									}
								});
								$rootScope.stopLoader();
								}else{
									if(info.cimsCode != undefined && info.cimsCode != null && info.cimsCode != ''){
										var New_EMR = "https://pharman.his.clinivantage.co.in/pharmandoctor?drugId=" +parseInt(info.cimsCode);
										window.open(New_EMR,'popUpWindow','height=700,width=1000');
										}
									}
						}
							
							/** **** Infusion Start Date*** */
							$scope.dateOptionsinfusionStartDate = {
								formatYear : 'yyyy',
								showWeeks : false,
								minDate : new Date(),
							};

							$scope.infusionStartOpen = {
								opened : false
							};

							$scope.openinfusionStartDate = function() {
								$scope.infusionStartOpen.opened = true;
							};

							/** ****Infusion End Date*** */
							$scope.dateOptionsinfusionEndDate = {
								formatYear : 'yyyy',
								showWeeks : false,
								minDate : new Date(),
							};

							$scope.infusionEndOpen = {
								opened : false
							};

							$scope.openinfusionEndDate = function() {
								$scope.infusionEndOpen.opened = true;
							};

							$scope.setNormalPriorityForInfusion = function(tabValue) {
								$scope.infusionObj.priority = tabValue;
							}
						   	$scope.addEmptyRowInfusion= function() {
								console.log("infusionDetails",$scope.infusionAdditiveDetails);
								$scope.additiveInfusion = {
										additiveName:"",
										additiveItemId:"",
				 						additivegenericName:"",
				 						additivegenericId:"",
				 						additiveVolume: 0 ,
				 						additiveUnit:"",
				 						additiveroaId:"",
				 						removeInfusionHistory:true
									};
								if($scope.infusionAdditiveDetails.length > 0 ){
									if($scope.infusionAdditiveDetails[0].additiveName != "" &&
									   $scope.infusionAdditiveDetails[0].additiveItemId != "" &&	
									   $scope.infusionAdditiveDetails[0].additivegenericId != "" &&
									   $scope.infusionAdditiveDetails[0].additiveUnit != "" &&
									   $scope.infusionAdditiveDetails[0].additiveName != "" ){
								         $scope.infusionAdditiveDetails.push($scope.additiveInfusion);
									}else{
										growl.warning(
			        	 						'Please enter additive details!!',
			        	 						{
			        	 							title : 'Warning!'
			        	 						});
									}
								}
								console.log($scope.infusionAdditiveDetails);
							}
							/// infusion code
							$scope.searchMedicationName = function(solutionName) {
								$scope.nameList = [];
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									name : solutionName
								}

								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getInfusionSolutionItemList";
								return BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.nameList = response.data.listObject;
														console
																.log(
																		"$scope.nameList ",
																		$scope.nameList);
													}
													return $scope.nameList;
												});
							}
							
							
							$scope.initBloodComponent = function() {

								$scope.infusionStartDate = undefined;
								$scope.infusionEndDate = undefined;

							}

							$scope.additivelist = [];

							$scope.getUOMActiveList = function() {

								var URI = BASE_URL + ROOT_URL
										+ "/api/globals/strength/units/"+$scope.organizationId;
								var data = {};
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {

													$rootScope.stopLoader();
													$scope.getUOMActiveList = [];
													$scope.getUOMActiveList = response.data;
													console
															.log(
																	"getUOMActiveList",
																	$scope.getUOMActiveList);

												});
							}
							$scope.getUOMActiveList();

							$scope.addGenericName = function($item, $model,
									$label, idx, itemMaster) {
								console.log($item);

								$rootScope.startLoader();

								$scope.itemId = $item.solutionItemId;
								$scope.infusionObj.solutionItemId = $item.solutionItemId;
								$scope.infusionObj.solutiongenericId = $item.solutiongenericId;
								$scope.solutiongenericName = "";

								var formulationTypeId = $item.formulationTypeId;
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getRoaList"
										+ "?unitId="
										+ $scope.unitId
										+ "&orgId="
										+ $scope.organizationId
										+ "&formulationTypeId="
										+ formulationTypeId
										+ "&genericId="
										+ $item.solutiongenericId
										+ "&patientId="
										+ $scope.postCommonObj.patientId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														var responseList = response.data.listObject;
														$scope.medicaRoaList = responseList;
														if (response.data.object == 0) {
															if (response.data.listObject.length !== 0) {

																$scope.roa_options = responseList;
																console
																		.log(
																				"ROA",
																				$scope.roa_options)
															}
															$scope.solutiongenericName = $item.solutiongenericName;
															$scope.formulationTypeId = $item.formulationTypeId;
															// $scope.strength =
															// $item.strength;
														} else {
															$scope.checkgenericObj = {
																solutiongenericName : $item.solutiongenericName,
																formulationTypeId : $item.formulationTypeId,
																strength : $item.strength,
															}
															angular
																	.element(
																			'#confirmAddItemPopup')
																	.modal(
																			'show');
														}
													}
												});

							}
							//** ****************** code for additive************ *//*

							$scope.searchMedicationadditiveName = function(
									additiveName) {

								$scope.nameList = [];
								var data = {

									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									name : additiveName

								}

								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getInfusionAdditiveItemList";
								return BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.nameList = response.data.listObject;
														console
																.log(
																		"$scope.nameList ",
																		$scope.nameList);
													}
													return $scope.nameList;
												});
							}

							$scope.addGenericadditiveName = function($item,
									$model, $label, idx) {
								$rootScope.startLoader();
								$scope.itemId = $item.additiveItemId;
								$scope.additiveInfusion.additivegenericName = "";

								$scope.additiveInfusion.additiveItemId = $item.additiveItemId;
								$scope.additiveInfusion.additivegenericId = $item.additivegenericId;

								var formulationTypeId = $item.formulationTypeId;
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getRoaList"
										+ "?unitId="
										+ $scope.unitId
										+ "&orgId="
										+ $scope.organizationId
										+ "&formulationTypeId="
										+ formulationTypeId
										+ "&genericId="
										+ $item.additivegenericId
										+ "&patientId="
										+ $scope.postCommonObj.patientId;

								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														var responseList = response.data.listObject;
														$scope.medicaRoaList = responseList;
														if (response.data.object == 0) {
															if (response.data.listObject.length !== 0) {

																$scope.roa_additive = responseList;
																console
																		.log(
																				"ROA",
																				$scope.roa_options)
															}
															$scope.additiveInfusion.additivegenericName = $item.additivegenericName;
															$scope.formulationTypeId = $item.formulationTypeId;
														} else {
															$scope.checkgenericObj = {
																additivegenericName : $item.additivegenericName,
																formulationTypeId : $item.formulationTypeId,
																strength : $item.strength,
															}
															angular
																	.element(
																			'#confirmAddItemPopup')
																	.modal(
																			'show');
														}
													}
												});

							}

							$scope.getListOfInfusion = function() {

								var URI3 = BASE_URL + ROOT_URL
										+ "/api/opd/getListOfInfusion";
								var data = {

									"organizationId" : $scope.organizationId,
									"unitId" : $scope.unitId,
									"patientId" : $scope.postCommonObj.patientId
								};
								GenericService.serviceAction("POST", URI3, data)
										.then( function(response) {
													$rootScope.stopLoader();
													$scope.getInfusionList = [];
													$scope.getInfusionList = response.data.listObject;
													angular.forEach($scope.getInfusionList,function(value,index)
															{
														var startDate1 =moment(value.treatDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
															var endDate1 =moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
															var updatedDate1 =moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
															if(startDate1=='Invalid date')
																{
																startDate1='';
																}
															else
																{
																startDate1=startDate1;
																}
															if(endDate1=='Invalid date')
															{
																endDate1='';
															}
														else
															{
															endDate1=endDate1;
															}
															if(updatedDate1=='Invalid date')
															{
																updatedDate1='';
															}
														else
															{
															updatedDate1=updatedDate1;
															}
															value["startDate1"]=startDate1;
															value["endDate1"]=endDate1;
															value["updatedDate1"]=updatedDate1;
															});
													console.log("infusion list",$scope.getInfusionList);

												});
							}
							$scope.getListOfInfusion();

							$scope.getadditivelist = function(
									infusionSolutionId) {

								var data = {

									"organizationId" : $scope.organizationId,
									"unitId" : $scope.unitId,
									"infusionSolutionId" : infusionSolutionId
								};
								var URI3 = BASE_URL + ROOT_URL
										+ "/api/opd/getAdditiveListById";

								GenericService
										.serviceAction("POST", URI3, data)
										.then(
												function(response) {

													$rootScope.stopLoader();
													$scope.getsaveadditivelist = [];
													$scope.getsaveadditivelist = response.data.listObject;
												});

							}
							// $scope.getadditivelist();
							$scope.searchMedicationFreItemsInfusion = function(
									searchKeyword) {
								$scope.itemList = [];
								var unitId = $scope.unitId
								var orgId = $scope.organizationId

								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getFrequencyList"
										+ "?unitId=" + unitId + "&orgId="
										+ orgId + "&searchPar=" + searchKeyword
								return BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE)
										.then( function(response) {
													if (response.data.status == "success") {
														$scope.itemList = response.data.listObject;
														console.log( "$scope.itemList", $scope.itemList)
													}
													return $scope.itemList;
												});
							}

							$scope.infusionObj = {
								solutionName : "",
								solutionItemId : "",
								solutiongenericName : "",
								solutiongenericId : "",
								solutionVolume : 0,
								solutionUnit : "",
								solutionroaId : "",
								additiveName : "",
								additiveItemId : "",
								additivegenericName : "",
								additivegenericId : "",
								additiveVolume : "",
								additiveUnit : "",
								additiveroaId : "",
								priority : "0",
								infusionStartDate : "",
								infusionEndDate : "",
								infusionFrequencyId : "",
								totalVolume : 0,
								duration : 0,
								infusionRate : 0,
								comments : "",
								instructionId : "",
								infusionAdditiveDtosList : ""

							};
							
							$scope.eraseAdditive = function(index){
								if(index == 0){
								$scope.initadditiveInfusion();
								$scope.calculateTotalVolume();
								}else{
									$scope.additiveInfusion = {
											additiveName : "",
											additiveItemId : "",
											additivegenericName : "",
											additivegenericId : "",
											additiveVolume : 0,
											additiveUnit : "",
											additiveroaId : "",
										};
								$scope.infusionAdditiveDetails[index] = [ $scope.additiveInfusion ];
								$scope.calculateTotalVolume();
								}
							}
							$scope.eraserInfusion = function(){
								$scope.initadditiveInfusion();
								$scope.infusionObj = {
										solutionName : "",
										solutionItemId : "",
										solutiongenericName : "",
										solutiongenericId : "",
										solutionVolume : 0,
										solutionUnit : "",
										solutionroaId : "",
										additiveName : "",
										additiveItemId : "",
										additivegenericName : "",
										additivegenericId : "",
										additiveVolume : "",
										additiveUnit : "",
										additiveroaId : "",
										priority : "0",
										infusionStartDate : new Date(),
										infusionEndDate : "",
										infusionFrequencyId : "",
										totalVolume : 0,
										duration : 0,
										infusionRate : 0,
										comments : "",
										instructionId : "",
										infusionAdditiveDtosList : ""

									};
								$scope.solutiongenericName="";
								$scope.infusionFrequencyId = "";
							}
							$scope.infusionObj.infusionStartDate = new Date();

							$scope.addFrequencyHistoryItemListInfusion = function(
									$item, $model, $label, idx, infusionObj) {
								$scope.frequencyId = $item.frequency_id;
								$scope.freQua = $item.noOfAdministrationTime;
								$scope.frequency = $item.frequency;
							}
							$scope.infusionAdditiveDetails = [];

							$scope.initadditiveInfusion = function() {

								$scope.additiveInfusion = {
									additiveName : "",
									additiveItemId : "",
									additivegenericName : "",
									additivegenericId : "",
									additiveVolume : 0,
									additiveUnit : "",
									additiveroaId : "",
								};
								$scope.infusionAdditiveDetails = [ $scope.additiveInfusion ];
							}
							$scope.initadditiveInfusion();
							$scope.Infusion = function() {
								console.log("infusionDetails",
										$scope.infusionAdditiveDetails);

								$scope.additiveInfusion = {
									additiveName : "",
									additiveItemId : "",
									additivegenericName : "",
									additivegenericId : "",
									additiveVolume : 0,
									additiveUnit : "",
									additiveroaId : "",
									removeInfusionHistory:false
								};
								$scope.infusionAdditiveDetails
										.push($scope.additiveInfusion);
								console.log($scope.infusionAdditiveDetails);
							}

							
							$scope.removeEmptyRowHistory = function(index) {
								if($scope.infusionAdditiveDetails.length == 1){
									angular.forEach($scope.infusionAdditiveDetails,function(value,key){
										value.removeInfusionHistory = false
									});
								}
								if(index != 0){
								$scope.infusionAdditiveDetails.splice(index, 1);
								$scope.calculateTotalVolume();
								}
							}

							//** *********calculation for total volume********** *//*

							$scope.infusionAdditiveList = [];
							$scope.resultadditiveVolume = 0;
							$scope.calculateTotalVolume = function() {
								$scope.infusionObj.totalVolume = 0;
								$scope.resultadditiveVolume = 0;

								if($scope.infusionAdditiveDetails[0].additiveVolume != 0 && $scope.infusionAdditiveDetails[0].additiveVolume != undefined){
								 angular.forEach($scope.infusionAdditiveDetails, function(value, key) {
									 var additiveVolume=value.additiveVolume;
									 $scope.resultadditiveVolume = $scope.resultadditiveVolume
										+ parseInt(additiveVolume);
								$scope.infusionObj.totalVolume = $scope.infusionObj.solutionVolume
										+ $scope.resultadditiveVolume;
								 });
									
							
								}else {
									$scope.infusionObj.totalVolume = $scope.infusionObj.solutionVolume;
								}

							}

							//** *********calculation for Rate********** *//*
							$scope.calculateRate = function(infusionObj) {

								$scope.infusionObj.infusionRate = ($scope.infusionObj.totalVolume/ $scope.infusionObj.duration).toFixed(3);

							}

							$scope.montantAnnuel = function() {
								if (isNaN(inputMontantAnnuel)) {
									inputMontantAnnuel = "";
								} else {
									inputMontantAnnuel = inputMontantGlobalAffaire
											/ inputDureeAnnee;
								}
							};
	
							
							$scope.saveInfusionDetails = function() {
								$rootScope.startLoader();
							
								
								if($scope.infusionObj.solutionItemId ==""){
									
									growl.warning(" Please enter the infusion details. ", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;
								}else if($scope.infusionObj.solutionVolume == "" ){
								
									growl.warning("Please select solution volume", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;
								}else if($scope.infusionObj.solutionUnit == ""){
									
									growl.warning("Please select strength", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;
								}else if($scope.infusionObj.solutionroaId ==""){
									
									growl.warning("Please select route of administration", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;
								}
							else if($scope.frequencyId == undefined){
									
									growl.warning("Please select frequency", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;
								}else if($scope.infusionObj.infusionEndDate ==""){
										
										growl.warning("Please select end date ", {
											title : 'Warning!'
										});
										$rootScope.stopLoader();
									return false;
								}else if($scope.infusionObj.duration ==""){
									
									growl.warning("Please select duration", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;
								
								}
// if(infusionAdditive[0].additiveItemId!=null){
								else if($scope.infusionObj.infusionEndDate ==""){
									
									growl.warning("Please select end date ", {
										title : 'Warning!'
									});
									$rootScope.stopLoader();
								return false;}
	
								else{
									$scope.saveBtnFlag = true;
									$scope.updateBtnFlag = false;
									var infusionAdditive = $scope.infusionAdditiveDetails;
									var infusionAdditiveList = [];

									// calculate infusion quantity;
									$scope.noOfAdmin=null;
									
									angular.forEach($scope.itemList, function(value, key){
										if($scope.frequencyId == value.frequency_id){
											$scope.noOfAdmin=value.noOfAdministrationTime;
										}
									});
									

									var first = $scope.infusionObj.infusionStartDate;
										var second =$scope.infusionObj.infusionEndDate; 
										var dateFirst = moment(first).format("DD-MM-YYYY");
										var dateSecond =moment(second).format("DD-MM-YYYY"); 
										$scope.test = $rootScope.calcDiff(first,second);
										var dateDiff=0;
// if($scope.test != 1){
// dateDiff=$scope.test + 1;
// }else{
// dateDiff=$scope.test;
// }
										if(dateFirst != dateSecond){
											dateDiff=$scope.test + 1;
										}else{
											dateDiff=$scope.test;
										}
										
										$scope.infusionQty=$scope.noOfAdmin * dateDiff;	
									var data = {
										"itemId" : $scope.infusionObj.solutionItemId,
										"genericId" : $scope.infusionObj.solutiongenericId,
										"solutionVolume" : $scope.infusionObj.solutionVolume,
										"strengthUnitId" : $scope.infusionObj.solutionUnit,
										"roaId" : $scope.infusionObj.solutionroaId,
										"frequencyId" : $scope.frequencyId,
										"priority" : $scope.infusionObj.priority,
										"startDate" : moment($scope.infusionObj.infusionStartDate).format('YYYY-MM-DD HH:mm:ss'),
										"endDate" : moment($scope.infusionObj.infusionEndDate).format('YYYY-MM-DD HH:mm:ss'),
										"totalVolume" : $scope.infusionObj.totalVolume,
										"duration" : $scope.infusionObj.duration,
										"infusionRate" : $scope.infusionObj.infusionRate,
										"comments" : $scope.infusionObj.comments,
										"instructionId" : $scope.infusionObj.instructionId,
										"organizationId" : $scope.organizationId,
										"unitId" : $scope.unitId,
										"createdBy" : $scope.userId,
										"updatedBy" : $scope.userId,
										"status" : "A",
										"createdDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										"updatedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
										"patientId" : $scope.postCommonObj.patientId,
										"admissionId" : $scope.postCommonObj.admissionId,
										"doctorId" : $scope.postCommonObj.doctorId,
										"visitTypeId" : $scope.postCommonObj.visitTypeId,
										 "deptId" :$scope.postCommonObj.specialityId,
										 "infusionQuantity" : $scope.infusionQty,
									};
									console.log("resultdata", data);
									for (var i = 0; i < infusionAdditive.length; i++) {
										

										if (infusionAdditive[i].additiveItemId > 0) {
											 if (infusionAdditive[i].additiveItemId!=null && infusionAdditive[i].additiveVolume==""){
													growl.warning("Please select additive volume", {
														title : 'Warning!'
													});
													$rootScope.stopLoader();
													return false;
												}
												else if (infusionAdditive[i].additiveVolume!=null && infusionAdditive[i].additiveUnit==""){
													growl.warning("Please select additive strength",{
														title : 'Warning!'
													});
													$rootScope.stopLoader();
													return false;
												}else{
											var additiveInfusionDetailsObj = {};
											additiveInfusionDetailsObj["itemId"] = infusionAdditive[i].additiveItemId;
											additiveInfusionDetailsObj["genericId"] = infusionAdditive[i].additivegenericId;
											additiveInfusionDetailsObj["volume"] = infusionAdditive[i].additiveVolume;
											additiveInfusionDetailsObj["additiveName"] = infusionAdditive[i].additiveName;
											additiveInfusionDetailsObj["strengthUnitId"] = infusionAdditive[i].additiveUnit;
											additiveInfusionDetailsObj["roaId"] = infusionAdditive[i].additiveroaId;
											additiveInfusionDetailsObj["organizationId"] = $scope.organizationId;
											additiveInfusionDetailsObj["unitId"] = $scope.unitId;
											additiveInfusionDetailsObj["updatedBy"] = $scope.userId;
											additiveInfusionDetailsObj["status"] = "A";
											additiveInfusionDetailsObj["createdDate"] = moment(
													new Date()).format(
													'YYYY-MM-DD HH:mm:ss');
											additiveInfusionDetailsObj["updatedDate"] = moment(
													new Date()).format(
													'YYYY-MM-DD HH:mm:ss');
											infusionAdditiveList
													.push(additiveInfusionDetailsObj);
												}

										}
									}

									data.infusionAdditiveDtosList = infusionAdditiveList;
									console.log("result", data);
									
								var URI = BASE_URL + ROOT_URL
										+ "/api/pharmacy/ip/pharmacology/validation/saveInfusionPresWithouthPharmaValidation";

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.infusionObj.solutionName = "";
														$scope.solutiongenericName = "";
														$scope.infusionObj.solutionVolume = 0;
														$scope.infusionObj.solutionUnit = "";
														$scope.infusionObj.solutionroaId = "";
														$scope.additiveInfusion.additiveName = "";
														$scope.additiveInfusion.additivegenericName = "";
														$scope.additiveInfusion.additiveVolume = "";
														$scope.additiveInfusion.additiveUnit = "";
														$scope.additiveInfusion.additiveroaId = "";
														$scope.infusionFrequencyId = "";
														$scope.infusionObj.infusionEndDate = "";
														$scope.infusionObj.totalVolume = 0;
														$scope.infusionObj.duration = 0;
														$scope.infusionObj.infusionRate = 0;
														$scope.infusionObj.comments = "";
														$scope.infusionObj.instructionId = "";
														$scope.getListOfInfusion();
														$scope.infusionAdditiveDetails = [];
														$scope.additiveInfusion = {
																additiveName : "",
																additiveItemId : "",
																additivegenericName : "",
																additivegenericId : "",
																additiveVolume : 0,
																additiveUnit : "",
																additiveroaId : "",
															};
														
														$scope.infusionAdditiveDetails.push($scope.additiveInfusion);
														$scope.getDetailedMedication();
														$rootScope.stopLoader();
													}
													$rootScope.stopLoader();
												});
// $rootScope.stopLoader();
								
								}
							}
							
							 $scope.stopInfusionData = function(index,flag,info) {
								      $scope.isStartFlag=flag,
								      $scope.patientId=$scope.postCommonObj.patientId,
								      $scope.infusionSolutionId1=info.infusionSolutionId,
								      $scope.prescriptionDetailList=[],
								      $scope.prescriptionDetailList=info.prescriptionDetailsDto
							 }
							 $scope.stopInfusion = function(justificationStopInfusion) {
								 $rootScope.startLoader();
								      var data = {
												"isStartFlag":$scope.isStartFlag,
												"patientId":$scope.postCommonObj.patientId,
												"infusionSolutionId":  $scope.infusionSolutionId1,
												"justificationStopInfusion":justificationStopInfusion,
										 }
								    if(justificationStopInfusion != "" && justificationStopInfusion != null && justificationStopInfusion != undefined){
									var URI = BASE_URL + ROOT_URL+ "/api/opd/updateInfusionStopFlag";
									
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										if (response.data.status == "success") {
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.getListOfInfusion();
											$rootScope.stopLoader();
											if($scope.prescriptionDetailList.length >0){

												angular.forEach($scope.prescriptionDetailList,function(value,key){
													
													var date = new Date();
													$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
													var data={
														    updatedBy:$scope.userId,
															updatedDate:$scope.dt,
															isMedicationStop:'Y',
															justificationStop:justificationStopInfusion,
															prescriptionDetailsid:value.prescriptionDetailsid,
													};
													
													
												     $timeout( function(){
													if(justificationStopInfusion != null && justificationStopInfusion !="" && justificationStopInfusion != undefined){
													var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationStop";
													GenericService.serviceAction("POST",URI,data).then(function(response){
														if (response.data.status == 'success')
														{
															
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
															$scope.getAllActiveMedication();
															$scope.getDetailedMedication();
															$scope.getContinuedMedicationHistoryList();
															$scope.getDetailedMedicationHistoryList();
															$scope.getMedicationListForMedicalHistory();
														}
													});
													}
													
												},5000);
												});
											
											}
											
									  	}
								  });
							   }
								else{
									 growl.warning(" Please enter reason. ",
												{
													title : 'warning!'
												});
									 $rootScope.stopLoader();
								}
						        }
							 
								var commonObjInst = {
										isMedication : 'Y',
										organizationId : $scope.organizationId
									}

									var URI = BASE_URL + ROOT_URL
											+ GETMEDICATIONINSTRUCTIONS;

									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													commonObjInst,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.medicationInstructions = response.data.listObject;
													});
									
									   $scope.activeMedications = [];
										$scope.getAllActiveMedication = function() {
											$scope.activeMedications = [];
											var patientId = $scope.postCommonObj.patientId;
											var dateObj = moment(new Date()).format(
													'YYYY-MM-DD');

											var URI = BASE_URL + ROOT_URL
													+ GETACTIVEMEDICATION + "?patientId="
													+ patientId + "&date=" + dateObj + "&unitId="+ $scope.unitId
											GenericServiceParamHeader
													.serviceAction(METHOD_GET, URI)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	$scope.activeMedications = response.data.listObject;
																
																	angular.forEach($scope.activeMedications,function(value,key){
											    						value.checkTrue='N';
											    						if(value.strengthDesc == null ){
																			value.strengthDesc = "";
																		}
																		if(value.doseDesc == null){
																			value.doseDesc = "";
																		}
											    					});
																	console.log("activeMedication",$scope.activeMedications);
																}
															});
										}
										$scope.getContinuedMedicationHistoryList = function() {
											$scope.continuedMedicationHistoryList = [];
											var data = {
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												patientId :$scope.postCommonObj.patientId,

											};
											var URI = BASE_URL + ROOT_URL
													+ "/api/opd/getContinuedMedicationList";
											GenericServiceParamHeader
													.serviceAction(METHOD_POST, URI, data,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	$scope.continuedMedicationHistoryList = response.data.listObject;
																	angular.forEach($scope.continuedMedicationHistoryList,function(value,key){
											    						value.checkTrue='N';
											    					});
																}
															});
										}
										
										
										 $scope.saveBtnFlag = true;
											$scope.updateBtnFlag = false;
											$scope.updateScope = false;
											$scope.popUpFlag = true;
											
									  	  $scope.showUpdateBtn = function(info,idx){
							      				try{
							      					
							      					
							      					$rootScope.startLoader();
							      					
							      				// for once the medication saved as normal
												// then user can not be changed it as
												// discharge medication
							      					if(info.medicationType != undefined && info.medicationType != null 
							      							&& info.medicationType != "" && info.medicationType == 1){
							      						$scope.disOrNotCount = 0;
							      					}
							      					
							      					if(info.isNonFormulary == 'N'){
							      						var URI = BASE_URL + ROOT_URL + "/api/opd/getOPDMedicationListById/" +info.prescriptionId +"/"+ info.prescriptionDetailsid +"/"+$scope.postCommonObj.patientId+"/"+info.itemId +"/"+ $scope.unitId+"/"+ $scope.organizationId ;
							      					}else{
							      						var URI = BASE_URL + ROOT_URL + "/api/opd/getOPDMedicationListByIdForNonFormulary/" +info.prescriptionId +"/"+ info.prescriptionDetailsid +"/"+$scope.postCommonObj.patientId+"/"+ $scope.unitId+"/"+ $scope.organizationId ;
							      					}
							      					console.log("EDIT",URI);
							      					GenericService.serviceAction("GET",URI).then(function(response){
							      						console.log("Edit",response);
							      						
							      						if(response.data.status == "success"){
							      							$scope.updateFlag = true;
									      					$scope.updateFlagValidation = true;
															$scope.saveFlag = false;
															$scope.saveBtnFlag = false;
															$scope.updateScope = true;
															$scope.updateBtnFlag = true;
							      							$rootScope.stopLoader();
							      							$scope.list = [];
							      						    $scope.list=response.data.listObject
							      						    $scope.list = response.data.listObject;
							      							console.log("showlist",$scope.list );
							      							
							      							$scope.medicationObj.prescriptionId = info.prescriptionId;
							      							$scope.medicationObj.prescriptionNo = info.prescriptionNo;
							      							$scope.medicationObj.frequencyDesc=response.data.listObject[0].frequencyDesc;
							      							$scope.medicationObj.itemId=response.data.listObject[0].itemId;
							      							$scope.medicationObj.searchItem=response.data.listObject[0].searchItem;
							      							$scope.medicationObj.medicationName = response.data.listObject[0].searchItem;
							      							$scope.medicationObj.genericId=response.data.listObject[0].genericId;
							      							$scope.medicationObj.doseUnitId=response.data.listObject[0].doseUnitId;
							      							$scope.medicationObj.dose=((response.data.listObject[0].dose != undefined && response.data.listObject[0].dose != null && response.data.listObject[0].dose != "") ||  response.data.listObject[0].dose == 0) ? response.data.listObject[0].dose :response.data.listObject[0].prescribeDose;
							      							$scope.medicationObj.itemStrength=response.data.listObject[0].dose+" "+(response.data.listObject[0].strengthDesc!=null ?response.data.listObject[0].strengthDesc : "");
							      							$scope.medicationObj.roaId=response.data.listObject[0].roa_id;
							      							$scope.medicationObj.durationUnit='Day';
							      							$scope.medicationObj.frequencyId=response.data.listObject[0].frequencyId;
							      							$scope.medicationObj.searchItemFre=response.data.listObject[0].searchItemFre;
							      							$scope.medicationObj.genericName=response.data.listObject[0].genericName;
							      							$scope.medicationObj.searchGeneric=response.data.listObject[0].genericName;
							      							$scope.medicationObj.genericId=response.data.listObject[0].genericId;
							      							$scope.medicationObj.rate=response.data.listObject[0].rate;
							      							$scope.medicationObj.prescribeDose=response.data.listObject[0].prescribeDose;
							      							if(response.data.listObject[0].isDoseCalculationApplicable != undefined 
							                                        && response.data.listObject[0].isDoseCalculationApplicable != null 
							                                        && response.data.listObject[0].isDoseCalculationApplicable != false){
							      							$scope.medicationObj.isDoseCalculationApplicable=response.data.listObject[0].isDoseCalculationApplicable;
							      							
							      							$scope.medicationObj.doseCalculationApplicableData = response.data.listObject[0].doseCalculationApplicableData;
															$scope.medicationObj.doseCalculationApplicableData=JSON.parse($scope.medicationObj.doseCalculationApplicableData);
													        angular.forEach($scope.medicationObj.doseCalculationApplicableData, function(val, key){
													        	$scope.medicationObj.quantityForDoseCal=val.quantityForDoseCal;
													        	$scope.medicationObj.doseForDoseCal=val.doseForDoseCal;
													        	$scope.medicationObj.strengthForDoseCal=val.strengthForDoseCal;
													        	$scope.medicationObj.searchItemFre=val.searchItemFre;
													        	$scope.medicationObj.noOfAdministrationTimeForDoseCal=val.noOfAdministrationTimeForDoseCal;
													        	$scope.medicationObj.durationForDoseCal=val.durationForDoseCal;
													        	$scope.medicationObj.durationCountForDoseCal=val.durationCountForDoseCal;
													        	$scope.freQua =$scope.medicationObj.noOfAdministrationTimeForDoseCal;
																$scope.medicationObj.frequencyId = val.frequencyForDoseCal;
																$scope.frequency_id=val.frequencyForDoseCal;
																$scope.frequency=$scope.medicationObj.searchItemFre;
																$scope.medicationObj.durationCount=$scope.medicationObj.durationCountForDoseCal;
																$scope.medicationObj.duration=$scope.medicationObj.durationForDoseCal;
																$scope.medicationObj.itemStrength=$scope.medicationObj.strengthForDoseCal !=null ? $scope.medicationObj.strengthForDoseCal:(item.strength+" "+(item.unitDesc!=null ? item.unitDesc : " ") );
																
													        });	
							      							}
							      							$scope.medicationObj.isInstruction='N';
							      							if(response.data.listObject[0].instructionDesc != null){
							      								$scope.medicationObj.instructionDesc=response.data.listObject[0].instructionDesc;
							      								$scope.medicationObj.isInstruction='N';
							      								$scope.medicationObj.instructionId=response.data.listObject[0].instructionId;
							      							}else{
							      								$scope.medicationObj.isInstructionText=response.data.listObject[0].instructionText;
							      								$scope.medicationObj.isInstruction='Y';
							      								$scope.medicationObj.instructionId='';
							      							}
							      							
							      							if($scope.medicationObj.instructionId == ''&& $scope.medicationObj.isInstructionText == null){
							      								$scope.medicationObj.isInstruction='N';
							      							}
							      							
							      							
							      							$scope.medicationObj.noOfAdministrationTime=response.data.listObject[0].noOfAdministrationTime;
							      							$scope.medicationObj.unit=response.data.listObject[0].strengthDesc;
							      							$scope.medicationObj.priorityId=response.data.listObject[0].priorityId;
							      							$scope.medicationObj.isStat=response.data.listObject[0].isStat;
							      							if($scope.medicationObj.isStat==true){
							      								$scope.medicationObj.isStat="stat";
							      							}else{
							      								$scope.medicationObj.isStat==false;
							      							}
							      							$scope.medicationObj.isDischarge=response.data.listObject[0].isDischargeMedication;
							      							if($scope.medicationObj.isDischarge==true){
							      								$scope.medicationObj.isDischarge="discharge"
							      							}else{
							      								$scope.medicationObj.isDischarge==false;
							      							}
							      							$scope.medicationObj.medicationStartDate=moment(response.data.listObject[0].medicationStartDate,"DD-MM-YYYY").toDate();
							      							$scope.medicationObj.medicationEndDate=moment(response.data.listObject[0].medicationEndDate,"DD-MM-YYYY").toDate();
							      							$scope.freQua =response.data.listObject[0].noOfAdministrationTime;
							      							$scope.medicationObj.formulationTypeId=response.data.listObject[0].formulationTypeId;
							      							$scope.medicationIndex=0;
							      							$scope.medicationObj.isGeneric='N';
							      							if(info.isNonFormulary == 'N'){
							      							    $scope.medicationObj.nonFormularyAdvice='N';
							      							}else{
							      								$scope.medicationObj.nonFormularyAdvice = 'Y';
							      								$scope.medicationObj.searchItem = null;
							      							}
							      							$scope.frequency_id=$scope.medicationObj.frequencyId;
															$scope.noOfAdministrationTime=$scope.medicationObj.noOfAdministrationTime;
															$scope.medicationObj.prescribedDosageUnitList=[];
															$scope.item={
																	frequency_id:$scope.frequency_id,
																	frequency:response.data.listObject[0].searchItemFre,
																	noOfAdministrationTime:$scope.noOfAdministrationTime
															}
															
															$scope.model=response.data.listObject[0].searchItemFre;
															$scope.label=response.data.listObject[0].searchItemFre;
															$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
															if(info.isNonFormulary == 'N'){
							      							var formulationTypeId = response.data.listObject[0].formulationTypeId;
							    							var genericId  =response.data.listObject[0].genericId;
								  							var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
								  							$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + formulationTypeId+ "&genericId=" + genericId+ "&patientId=" +$scope.postCommonObj.patientId;
								  						
								  							$scope.prescriptionDtlObj=response.data.listObject[0];
								  							GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
								  							if(response.data.status == "success"){
								  								$rootScope.stopLoader();
								  								var responseList = response.data.listObject;
							  								$scope.medicaRoaList = responseList;
							  								if(response.data.object == 0)
							  									{
							  										if(response.data.listObject.length !== 0) {
							  											
							  										   $scope.roa_options = responseList;
							  										   console.log("$scope.roa_options",$scope.roa_options);
							  										}
							  										$scope.showMedicationOptions = true
							  										
							  										if($scope.prescriptionDtlObj.prescribeDose!=null && $scope.prescriptionDtlObj.factorId!=null && $scope.prescriptionDtlObj.factorId==1){
							  										   $scope.medicationObj.prescribeDose=$scope.prescriptionDtlObj.prescribeDose;
							  										}else if($scope.prescriptionDtlObj.prescribeDose !=undefined && $scope.prescriptionDtlObj.prescribeDose != null && $scope.prescriptionDtlObj.prescribeDose != ""){
							  											$scope.medicationObj.prescribeDose=$scope.prescriptionDtlObj.prescribeDose;
							  										}
							  										
							  										var dataobj = {
																			organizationId : $scope.organizationId,
																			unitId : $scope.unitId,
																			formulationTypeId:$scope.prescriptionDtlObj.formulationTypeId
																	};
																	var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
																	GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																				$rootScope.stopLoader();
																				$scope.medicationObj.prescribedDosageUnitList=[];
																				$scope.counterFormulation=1;
																				$scope.medicationObj.formulationCounterIndex="";
																				angular.forEach(response.data.listObject,function(value,index){
																					var unitObj={};
																					unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																					unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																					unitObj.formulationDosageToUnit=value.toConversionUnit;
																					unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																					unitObj.counterIndex=$scope.counterFormulation++;
																					unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																					unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																					unitObj.formulationFactorId=value.factorId;
																					unitObj.formulationDosage=value.dosageForm;
																					unitObj.formulationDosageMappId=value.formulationDosageMappId;
																					$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																				});
																				
																				angular.forEach(response.data.listObject,function(value,index){
																					var unitObj={};
																					if(value.factorId==1){
																						unitObj.formulationDosageFromUnit=value.toConversionUnit;
																						unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																						unitObj.formulationDosageToUnit=value.fromConversionUnit;
																						unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																						unitObj.counterIndex=$scope.counterFormulation++;
																						unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																						unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																						unitObj.formulationFactorId=value.factorId;
																						unitObj.formulationDosage=value.dosageForm;
																						unitObj.formulationDosageMappId=value.formulationDosageMappId;
																						$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																					}
																				});
																				var list=[];
																				if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
																					 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																						 if(obj.formulationDosageFromUnit != undefined && obj.formulationDosageFromUnit != null && obj.formulationDosageFromUnit != ""){
																							 return  info.prescribeDoseUnit === obj.formulationDosageFromUnit;
																						 }else{
																							 return  info.dosageForm === obj.formulationDosage;
																						 }
																				    });
																					if(list.length>0){	
																						$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																					}
																				}
																				
																				if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==2){
																					 list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																				        return $scope.prescriptionDtlObj.dosageForm === obj.formulationDosage;
																				    });
																					if(list.length>0){	
																						$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																					}
																				}
																	});
							  									}
								  							}
								  							});
															}else{
																$scope.medicationObj.prescribedDosageUnitList=[];
														    	var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaListNonFormulary" + "?unitId=" + 
																$scope.unitId + "&orgId=" + $scope.organizationId +"&patientId=" +  $scope.postCommonObj.patientId;
															
																GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
																if(response.data.status == "success"){
																		$scope.roa_options1 = response.data.listObject;
																		$scope.medicationObj.roaId=$scope.list[0].roa_id;
																}
															});
																
															}
							      						}else if(response.data.status == 'error'){
							      							$rootScope.stopLoader();
							      							growl.warning(response.data.message,
							      									{
							      										title : 'Warning!'
							      									});
							      						}
							      					});
							      					
							      				}catch(e){
							      					console.log("Exception-",e);
							      				}
							      			}
					            	
									  	$scope.updateFlag = false;
										$scope.saveFlag = true;
										
										// cancel Medication
										$scope.stopMedication=function(item){
			// $scope.prescriptionId=item.prescriptionId;
											$scope.prescriptionDetailsid=item.prescriptionDetailsid;
											
										}
										$scope.cancelMedication = function(justificationStop){
											$scope.justificationStop=justificationStop;
											$rootScope.startLoader();
											try{
			// $rootScope.startLoader();
												var date = new Date();
												$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
												var data={
													    updatedBy:$scope.userId,
														updatedDate:$scope.dt,
														isMedicationStop:'Y',
														justificationStop:$scope.justificationStop,
														/* prescriptionId:$scope.prescriptionId, */
														prescriptionDetailsid:$scope.prescriptionDetailsid,
														
												};
												
												if(justificationStop != null && justificationStop !="" && justificationStop != undefined){
												var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationStop";
												GenericService.serviceAction("POST",URI,data).then(function(response){
													if (response.data.status == 'success')
													{
														
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$rootScope.stopLoader();
														$scope.getAllActiveMedication();
														$scope.getDetailedMedication();
														$scope.getContinuedMedicationHistoryList();
														$scope.getDetailedMedicationHistoryList();
														$scope.getMedicationListForMedicalHistory();
			// $scope.$broadcast('show-errors-reset');
														$scope.justificationStop="";
													} else if(response.data.status == 'error')
													{
														$rootScope.stopLoader();
														growl.warning(response.data.message,
																{
																	title : 'Warning!'
																});
													}
												});
												}else{
													growl.warning(" Please enter reason to discontinue medication. ",
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
										// delete medication
										$scope.deleteFunction = function(info){
											$scope.prescriptionId=info.prescriptionId;
											$scope.prescriptionDetailsid1=info.prescriptionDetailsid;
										}
										$scope.deleteMedicationPendingStatus = function(){
											$rootScope.startLoader();
											try{
												var date = new Date();
												$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
												var data={
													    updatedBy:$scope.userId,
														updatedDate:$scope.dt,
														prescriptionId:$scope.prescriptionId,
														organizationId:$scope.organizationId,
														justificationStop:$scope.justificationDeleteMedi,
														prescriptionDetailsid:$scope.prescriptionDetailsid1,
														
												};
												
												var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationDelete";
												GenericService.serviceAction("POST",URI,data).then(function(response){
													if (response.data.status == 'success')
													{
														$rootScope.stopLoader();
														growl.success("Deleted Successfully..!!",
																{
																	title : 'Success!'
																});
														$scope.getAllActiveMedication();
														$scope.getDetailedMedication();
														$scope.getMedicationListForMedicalHistory();
														$scope.justificationDeleteMedi="";
													} else if(response.data.status == 'error')
													{
														$rootScope.stopLoader();
														growl.warning(response.data.message,
																{
																	title : 'Warning!'
																});
														$scope.getAllActiveMedication();
														$scope.getDetailedMedication();
														$scope.getMedicationListForMedicalHistory();
														$scope.justificationDeleteMedi="";
													}
												});
			
											}catch(e){
												console.log("Exception",e);
											}
										}
										
										
										$scope.updateMedicationList = function() {
						          			$scope.saveBtnFlag = false;
					      					$scope.updateScope = true;
					      					$scope.updateBtnFlag = true;
					      					
											
											console.log("$scope.medicationDetails",
													$scope.medicationDetails);
											
			                                if($scope.detailedMedications.length>0){
			                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
			                                    angular.forEach($scope.detailedMedications,function(value,index){
			                                        
			                                        var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
			                                        var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
			                                        var startDT =  moment(sDT).format('YYYY-MM-DD');
			                                        var endDT = moment(eDT).format('YYYY-MM-DD');
			                                        
			                                        if ( $scope.searchItem == value.itemName
			                                                && $scope.medicationObj.medicationStartDate ==  startDT
			// && $scope.medicationObj.medicationEndDate == endDT
			                                                && $scope.searchItemFre == value.frequencyDesc
			                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
			                                                && $scope.medicationObj.quantity == value.orderQty
			                                                && $scope.medicationObj.roaId == value.roaId
			                                                && $scope.medicationObj.isStat == value.statflag) {
			                                            
			                                            $scope.medicationExist =true;
			                                        }
			                                    });
			                                    
			                                }
			                                
			                                
			                                if($scope.medicationExist){
			                                    growl.warning(
			                                            "Medication Already Exist",
			                                            {
			                                                title : 'Warning!'
			                                            });
			                            $rootScope.stopLoader();
			                            return false;
			                            }
			// alert($scope.list[0].prescriptionId);
											$rootScope.startLoader();
											var medicationDetails = $scope.medicationDetails;

											var medicationDetailList = [];
											if($scope.medicationDetails.length > 0){
											if( $scope.medicationDetails[0].priorityId != "" && $scope.medicationDetails[0].priorityId != null && $scope.medicationDetails[0].priorityId != undefined){
												$scope.pri=$scope.medicationDetails[0].priorityId;
											}
											}

											var data = {
													"prescriptionId":$scope.list[0].prescriptionId,
													"prescriptionNo":$scope.list[0].prescriptionNo,
													"prescriptionDetailsid":$scope.list[0].prescriptionDetailsid,
													"prescriptionFrequencyId":$scope.list[0].prescriptionFrequencyId,
												"organizationId" : $scope.organizationId,
												"unitId" : $scope.unitId,
												"visitTypeId" : $scope.postCommonObj.visitTypeId,
												"doctorId" : $scope.postCommonObj.doctorId,
												"prescriptionDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
												"authorisedBy" : $scope.postCommonObj.doctorId,
												"authorisedDate" :moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
												"addedBy" : $scope.userId,
												"addedDate" :moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
												"admissionId" : $scope.postCommonObj.admissionId,
												"patientId" : $scope.postCommonObj.patientId,
												"priorityId" : $scope.pri,
											  "deptId" : $scope.postCommonObj.specialityId,
												"isDischarge":false,
												"isPharmacologicalValidationSkipIp": true,
												 "medicationType":$scope.medType
											};

											// var medicationStartDate =
											// moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');

											var flag = true;
											if(medicationDetails.length > 0){
											angular.forEach(medicationDetails,function(value, key) {

																var instructionId = value.instructionId;
																if (value.medicationStartDate != ''
																		&& value.medicationEndDate != ''
																		&& value.durationCount != ''
																		&& value.durationUnit != ''
			// && value.instructionId != ''
																		&& value.quantity != '') {

																	var medicationDetailsObj = {};
																	medicationDetailsObj["itemId"] = value.itemId;
																	medicationDetailsObj["prescriptionId"] = $scope.list[0].prescriptionId;
																	medicationDetailsObj["prescriptionDetailsid"] = $scope.list[0].prescriptionDetailsid;
																	medicationDetailsObj["prescriptionFrequencyId"] = $scope.list[0].prescriptionFrequencyId;
																	medicationDetailsObj["storeId"] = 1;
																	medicationDetailsObj["uomUnitId"] = value.uomTypeId;
																	medicationDetailsObj["orderQty"] = value.quantity;
																	medicationDetailsObj["strength"] = value.dose;
																	medicationDetailsObj["prescriptionDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
																	medicationDetailsObj["authorisedBy"] = $scope.postCommonObj.doctorId;
																	medicationDetailsObj["authorisedDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
																	medicationDetailsObj["dose"] = value.dose;
																	medicationDetailsObj["doseUnitId"] = value.doseUnitId;
																	medicationDetailsObj["frequencyId"] = value.frequencyId;
																	medicationDetailsObj["itemName"] =value.medicationName != undefined ? value.medicationName :value.itemName;
																	medicationDetailsObj["genericName"] = value.genericName;
																	medicationDetailsObj["routeId"] = value.roaId;
																	medicationDetailsObj["duration"] = value.durationCount;
							                                        medicationDetailsObj["durationUnit"] = value.durationUnit;
																	medicationDetailsObj["startDate"] = value.medicationStartDate+ " 00:00:00";
																	medicationDetailsObj["stopDate"] = value.medicationEndDate+ " 00:00:00";
																	medicationDetailsObj["priorityId"] = value.priorityId;
																	   if(value.instructionId != '' && value.instructionId != undefined  && value.instructionId != null ){
																			medicationDetailsObj["instructionId"] = value.instructionId;
																			}else{
																			 medicationDetailsObj["instructionDesc"] = value.instructionDesc;
																			}
																	   
																	medicationDetailsObj["statflag"] = value.isStat;
																	medicationDetailsObj["isNonFormulary"] = value.nonFormularyAdvice;
																	// medicationDetailsObj["isDischarge"]
																	// = value.isDischarge;
																	if(value.isDischarge == true)
																		{
																		medicationDetailsObj["isDischarge"] = true;
																		}
																	
																	if(value.prescribedDosageUnitList.length>0 && value.formulationCounterIndex!="" && value.formulationCounterIndex!=null
																			&& value.formulationCounterIndex!=undefined){
																		 var list=$.grep(value.prescribedDosageUnitList, function (obj) {
																	        return value.formulationCounterIndex === obj.counterIndex;
																	    });
																		 if(list.length>0){
																			 if(list[0].formulationFactorId==1){
																				 medicationDetailsObj.prescribedDose=value.prescribeDose;
																				 medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
																				 medicationDetailsObj.factorId=list[0].formulationFactorId;
																				 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																			 }else{
																				 medicationDetailsObj.prescribedDose=value.prescribeDose;
																				 medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
																				 medicationDetailsObj.factorId=list[0].formulationFactorId;
																				 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																			 }
																		 }
																	}else{
																		
																		medicationDetailsObj.prescribedDose=value.prescribeDose != undefined ? value.prescribeDose :null;
																		medicationDetailsObj.factorId=2;
																	}
																	
																	medicationDetailList.push(medicationDetailsObj);
																} else {
																	flag = false;
																}

															});
											}else{
												flag = false;
											}
											if (flag) {
												data.prescriptionDetailsDtosList = medicationDetailList;
												console.log("medicationDetailList", data);
												// return false;
												var URI = BASE_URL + ROOT_URL
														+ "/api/opd/medicationUpdate";

												GenericServiceParamHeader
														.serviceAction(METHOD_POST, URI,
																data,
																NOTIFICATION_MSG_STATUS_TRUE)
														.then(
																function(response) {
																	if (response.data.status == "success") {
																		$scope.updateFlag = false;
																		$scope.updateFlagValidation = false;
																		$scope.saveFlag = true;
																		
																		$scope.medicationDetails = [];

																		$scope.medicationObj = {
																			itemId : "",
																			searchItemFre:"",
																			searchItem:"",
																			formulationTypeId : '',
																			favorite_status : false,
																			medicationStartDate:new Date(),
																			roaId : '',
																			dose : '',
																			doseUnitId : '',
																			durationCount : "",
																			durationUnit : 'Day',
																			instructionId : '',
																			medicationEndDate : '',
																			frequencyId : '',
																			quantity : '',
																			uomTypeId : '',
																			conversions : '',
																			strength : '',
																			priorityId : '',
																			isStat : false,
																			isDischarge:false,
																			priorityCode: 'NR',
																			searchItemFre :"",
																			isGeneric:'N',
																			isInstruction:'N',
																			nonFormularyAdvice:'N'

																		};
																		$scope.initMedicationObj();
																		$scope.getAllActiveMedication();
																		 $scope.getDetailedMedication();
																		 $scope.totalSumOfRate=0;
																		 $scope.searchItem = "";
																		 $scope.searchItemFre = "";
																		 $scope.medicationObj.itemStrength="";
																		 $scope.medicationObj.prescribedDosageUnitList=[];
																		 $scope.saveBtnFlag = true;
																		 $scope.updateBtnFlag = false;
																		 $scope.covTemplateId="";
																			$scope.covTemplateName="";
																		 if($scope.patientDetails.dischargeStatusId==1 || $scope.patientDetails.dischargeStatusId==2
																					|| $scope.patientDetails.dischargeStatusId == 3){
																			 $scope.disOrNotCount=1;
																		 }
																		 
																		$rootScope.stopLoader();
																	} else {
																		$rootScope
																				.stopLoader();
																	}
																});
											} else {
												growl.warning(
														     "Please add Medication and then save !!",
																{
																	title : 'Warning!'
																});
												$scope.medicationDetailList = [];
												$rootScope.stopLoader();
												return false;
											}

										}
										
										
										$scope.getProcedureTemplate = function(){
											
											var URI = BASE_URL + ROOT_URL + "/api/otims/getProcedureConfigTemplate";
											var obj = {
													orgId:$scope.organisationId, 
													unitId:$scope.unitId,
													procedureConfigId:$scope.resultCommonObj.procedureConfigId
													};
											BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success")
												//	$scope.procedureTemplateObj = response.data.object;
													$scope.consentDetailList = response.data.object.consentDetailList;
												$scope.checklistDetailList = response.data.object.checklistDetailList;
												$scope.staffDetailList = response.data.object.staffDetailList;
												$scope.selectedItemList = response.data.object.inventoryItemList;
												$scope.finalServiceList = response.data.object.serviceList;
												$scope.noteList = response.data.object.noteList;
												$scope.equipmentDetailList = response.data.object.equipmentDetailList;
												console.log("$scope.consentDetailListsadvfbghjkl;",$scope.consentDetailList);
											});
											$scope.billingObject = {};
											if($scope.postCommonObj.admissionId != null ){
												var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.postCommonObj.admissionId;
												BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
													if(response.data.status == "success"){
														$scope.billingObject = response.data.object;
													}
													});

												    if($scope.postCommonObj !== undefined && $scope.postCommonObj !== null){
                                                        if($scope.postCommonObj.patientId === undefined || $scope.postCommonObj.patientId === null || $scope.postCommonObj.patientId ===''){
                                                            $scope.postCommonObj.patientId = 0;
                                                        }
                                                    }
                                                    else{
                                                         $scope.postCommonObj.patientId = 0;
                                                    }

													var URI = BASE_URL+ ROOT_URL
													+ "/api/packages/getPackageListForIPDPackageOrder/"+$scope.postCommonObj.patientId+"/"+ $scope.postCommonObj.admissionId
													+ "/" + $scope.organizationId + "/"+ $scope.unitId+"/"+$scope.postCommonObj.visitTypeId;


													GenericService.serviceAction("GET", URI)
															.then(
																	function(response) {

																		$rootScope.stopLoader();
																		$scope.listPackageOrder = [];
																		$scope.listPackageOrder = response.data.listObject==null?[]:response.data.listObject;
																		console.log("ListPackageOrderresult",
																						$scope.listPackageOrder);

																	});
											}
										
											if($scope.postCommonObj.encounterId != null ){
												var data = {};
												
												var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/"+$scope.postCommonObj.encounterId;
												BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
													if(response.data.status == "success"){
														$scope.billingObject = {
																encounterId : response.data.object.encounterId.toString(),
																encounterDateString : moment(response.data.object.encounterDateString,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()),
																encounterDateString2 : moment(response.data.object.encounterDateString2,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase()),
																referralId : response.data.object.referralId,
																referralDesc : response.data.object.referralDesc,
																specialityName : response.data.object.specialityName,
																doctorName : response.data.object.doctorName,
																patientCategory : response.data.object.patientCategory,
																patientId : response.data.object.patientId,
																doctorId : response.data.object.doctorId,
																modalityId : response.data.object.modalityId,
																subSpecialityName : response.data.object.subSpecialityName,
																modalityDesc : response.data.object.modalityDesc,
																specialityId : response.data.object.specialityId,
																patientClassId : response.data.object.patientClassId,
																isTariffRateApplicable : response.data.object.isTariffRateApplicable,
																
														  }
											}
												});
											}
										}
										$scope.getProcedureTemplate();
										
										
										//service code 
										
										$scope.getSpecialityList = function() {
											
											var commonObj = {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												};
												
											
											var URI = BASE_URL + ROOT_URL
													+ GETACTIVESPECIALITY;
											BillingGenericService
													.serviceAction(METHOD_POST, URI,
															commonObj,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																if (response.data.status == "success")
																	$scope.specialityList = response.data.listObject;
																angular.forEach($scope.specialityList, function(value,index){
																	if(value.specialityId == $scope.postCommonObj.specialityId){
																		$scope.medicationSpecialityId=$scope.postCommonObj.specialityId.toString();
																		$scope.medicationDoctorId=$scope.postCommonObj.doctorId.toString();
																	}
																});
															});
										}
										$scope.getSpecialityList();
										
										$scope.getSubSpecialityList = function(id) {
								
											var specialityType = [];
											specialityType.push(id);

											var data = {
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												specialityIdList : specialityType
											}
											console.log(data);
											var URI = BASE_URL
													+ ROOT_URL
													+ GETSUBSPECIALITYBYSPECIALITYARRAYFORTARIFF;
											BillingGenericService
													.serviceAction(METHOD_POST, URI, data,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																console.log(response);
																if (response.data.status == "success") {
																	$scope.subSpecialityList = response.data.listObject;
																}
															});
										}

										$scope.getDoctorId=function(medicationDoctorId){
											$scope.doc=medicationDoctorId;
											}
											$scope.getSpecialityId=function(medicationSpecialityId){
											$scope.spl=medicationSpecialityId;
											}
											
											$scope.medicationVisitTypeId = '';
											
											$scope.doctorListMedication=[];
											 $scope.noDoctorSpeciality=true;
											 $scope.noDoctorSpeciality1=true;
											var obj={
													"firstName":($scope.postCommonObj.doctorName),
													"lastName":""
											}
											$scope.treatingDoctor;
											$scope.treatingDoctorSpeciality;
											$scope.treatingDoctorSpecialityId;
											$scope.treatingDoctorSpeciality=$scope.postCommonObj.specialityName;
											$scope.treatingDoctorSpecialityId=$scope.postCommonObj.specialityId;
											$scope.treatingDoctor=obj;
											$scope.doctorListMedication.push(obj);
											
											$scope.getDoctorBySpecialityMedication = function(id) {
												// alert();
												var data = {
												specialityId : id,
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
												};
												var URI = BASE_URL + ROOT_URL
												+ GETDOCTORBYSPECIALITYID;

												BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
												.then(
												function(response) {
												console.log(response);
												$scope.doctorListMedication = [];
												// $rootScope.startLoader();
												if (response.data.status == "success")
												$scope.doctorListMedication = response.data.listObject;
												console.log("$scope.doctorListMedication",$scope.doctorListMedication);
												$scope.noDoctorSpeciality1=false;
												});

												}
											
											
											$scope.searchServices = function(searchKeyword){
												$scope.servicesList=[];
												searchKeyword=searchKeyword+"---"+$scope.postCommonObj.genderId;
												var serData = {
														organizationId : $scope.organizationId,
														unitId : $scope.unitId,
														searchKeyword : searchKeyword
												}
										
												var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
												return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
													if(response.data.status == "success"){
														$scope.servicesList = response.data.listObject;
													}
													return $scope.servicesList;
												});
											}
											
											
											$scope.checkServiceMappedWithTest = function(serviceId,specialityId)
											{
												     $rootScope.startLoaderPopup();
												     var labSpecialityId = 0;
												     if(specialityId==labConfig.labSpecialityId)
														{
												    	   labSpecialityId = specialityId;
														}
													var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/checkService/"+labSpecialityId;
													var data = {serviceMasterId:serviceId,orgId:$scope.organizationId,unitId:$scope.unitId};
												    return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
													{
														if(response.data.status == "success")
														{
															$rootScope.stopLoaderPopup();
															if(response.data.object==0)
															{
															   growl
																.warning('Service is Not mapped with Test!!',{
																			title : 'Warning!'
																		});
															   response.data.object = 1;
														     }
															return response.data.object;
														}
														else if(response.data.status == "error"){
															  $rootScope.stopLoaderPopup();
															  return response.data.object;
														   }
													 });
											}
											
										    $scope.serviceHideFlag=false;
											$scope.addServiceToTempServiceList = function(
													$item, $model, $label) 
											{
												
												var obj = {
														
														"admissionId" :$scope.postCommonObj.admissionId,
														"encounterId" : $scope.postCommonObj.encounterId,
														"contractId" :$scope.billingObject.contractId,
														"serviceId" :$item.serviceMasterId,
														"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
														"organizationId":$scope.organizationId,
														"unitId":$scope.unitId,
														"visitTypeId":$scope.postCommonObj.visitTypeId,
														"billingBedCategoryId":$scope.billingObject.bedBillingCategoryId,
														"patientCategoryId":$scope.billingObject.patientCategoryId,
														"defaultSelfTariffId":$scope.defaultSelfTariffId,
													    "selfPayeeId" :$scope.selfPayeeId,
													    "quantity":1,
													    "patientId":$scope.patientDetails.patientId,
													    "tariffId" :$scope.defaultSelfTariffId,
													    "paymentEntitlementId": $scope.billingObject.paymentEntitlementId
													    
													};
												$scope.searchObjjj.search= $label;
												console.log('Base prise',obj)
												$rootScope.startLoaderPopup();
												var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
												BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
												{
													if(response.data.object != "undefined" && response.data.object != null)
													{
														$scope.finalPriceService = response.data.object.finalPrice;
														$scope.serviceRateGlobal = response.data.object.serviceRate;
														if(response.data.object.doesNotExist==null)
														{
															$rootScope.stopLoaderPopup();
															$scope.serviceHideFlag=true;
															var promise = $scope.checkServiceMappedWithTest($item.serviceMasterId,$item.specialityId);
												    		promise.then(function(obj) 
												    				{
												        	             if(obj>0)
												        	            	 {
												        	            
																					$scope.orderServiceId = $item.serviceMasterId;
																					$scope.orderServiceName = $item.serviceStandardName;
																					$scope.orderSpecialtyId = $item.specialityId;
																					$scope.orderSpecialtyName = $item.specialityName;
																					$scope.orderSubSpecialtyId = $item.subSpecialityId;
																					$scope.orderSubSpecialtyName = $item.subSpecialityName;
																					$scope.orderIsRefDoctorShare = $item.isRefDoctorShare;
																					$scope.status = $item.status;
																					$scope.medallServiceId = $item.medallServiceId;
																					$scope.selectedServiceTypeSpecialityId = $scope.orderSpecialtyId;
																					$scope.getSubSpecialityList($scope.orderSpecialtyId);
																					$scope.selectedSubServiceTypeSubSpecialityId = $scope.orderSubSpecialtyId;
			
												        	            	 }
												        	             else{
												        	            	  
												        	            	 growl
												        	 				.warning(
												        	 						'Service is Not mapped with Test!!',
												        	 						{
												        	 							title : 'Warning!'
												        	 						});
												        	                 }
												    				 });
														}else{
															$rootScope.stopLoaderPopup();
															$scope.searchObjjj.search = "";
															growl.warning("Service is not mapped with the tariff..!!",{
																title : WARNING_MSG
															});
															return false;
														}
													}
												});
												
											}
											
											$scope.checkServiceMappedOrNot = function(info){
												console.log("data",info);
												if(info.serviceId != null){
													$scope.orderServiceId = info.serviceId;
													$scope.orderSpecialtyId = info.specialityId;
													$scope.orderSubSpecialtyId  = info.subSpecialityId;
													$scope.orderServiceName = info.serviceStandardName;
													$scope.orderSpecialtyName = info.specialityName;
													$scope.orderSubSpecialtyName = info.subSpecialityName;
													$scope.search = info.serviceStandardName;
													$scope.selectedServiceTypeSpecialityId = info.specialityId;
													$scope.selectedSubServiceTypeSubSpecialityId = info.subSpecialityId;
												}
											}
											
											$scope.mriCtList = [];
											$scope.getMRIandCT = function(){
												
												  $rootScope.startLoader();
												   var data = {};
												   data.organizationId = $scope.organizationId;
												 
												   
									    		var URI = BASE_URL + ROOT_URL+ "/adt/getSubSpecialityMRIandCT";
									    		GenericService.serviceAction("POST", URI, data).then(
														function(response) {
															if (response.data.status == "success") {
																$scope.mriCtList = [];
																$scope.mriCtList = response.data.listObject;
																$rootScope.stopLoader();
															}
															else
																{
																$rootScope.stopLoader();
																}
														});
												}
											$scope.getMRIandCT();
											$scope.searchObjjj={
													search:""
											}
											$scope.editserviceHideFlag=false;
											$scope.clinObj = {
													clinicalNote :"",
													showclinicalNote : "",
													servicePriorityId :""
											}
											$scope.setClinicalNote = function(addClinicalNote) {
												if(addClinicalNote == "Y"){
													$scope.clinObj.clinicalNote ="";
													$scope.clinObj.servicePriorityId="";
													$("#clinicalNoteView").show();
												}else{
													$("#clinicalNoteView").hide();
												}
												
											}
											$scope.uncheck=function(){
												$("#clinicalNoteView").hide();
											}
											
											$scope.medallPrifixList =[];
											$scope.medallUnitObj ={};
											$scope.medallDoctorList =[];
											$scope.getMedalDdetails = function(){
												var data = {
														  "organizationId" : $scope.organizationId,
														  "unitId" : $scope.unitId,
												}
												var URI = BASE_URL + ROOT_URL + "/adt/getPrefixMasterAndMedallPrefixMaster";
												BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
													if (response.data.status == "success")
													{
														$scope.medallPrifixList =response.data.listObject;
														$scope.medallUnitObj =response.data.object;
														$scope.medallDoctorList =response.data.resObject;
														
													}
												});
											}
											$scope.getMedalDdetails();
										
											$scope.addClinicalNote = 'N';
											  $scope.medicationSpecialityId=$scope.postCommonObj.specialityId;
												$scope.medicationDoctorId=$scope.postCommonObj.doctorId;
												
												
											
											$scope.showClinicalNote = function(item) {
												
												$scope.clinObj.showclinicalNote =item.ordRemarks;
											}
											
											$scope.removeTempServicesByIndex = function(item,
													index) {
												angular.forEach($scope.addedServiceOrdersForDisplay,function(value,idx){
									     		 	if(index == idx){
									     		 		$scope.totalServiceAmount -= value.finalPrice;
									     		 	}
									 		    });
												if($scope.outsourceLabId == 2){
													angular.forEach($scope.servicesOrdersForMedall,function(value,idx){
										     		 	if(index == idx){
										     		 		$scope.totalServiceAmount -= value.finalPrice;
										     		 	}
										 		    });
												}
												$scope.addedServiceOrdersForDisplay.splice(index, 1);
												if($scope.outsourceLabId == 2){
													$scope.servicesOrdersForMedall.splice(index, 1);
												}
												$scope.objectIndexService = index;
												$scope.serciveObjectForRemove = item;
										    	if($scope.addedServiceOrdersForDisplay.length<=0 || ($scope.outsourceLabId == 2 && $scope.servicesOrdersForMedall !=null && $scope.servicesOrdersForMedall.length<=0))
										    	{
										    		$scope.isServiceAdded=false;
										    		$scope.noDoctorSpeciality1=true;
										    		$scope.noDoctorSpeciality=true;
										    		$scope.flag=false;
										    		angular.forEach($scope.specialityList,function(value,idx){
										    			if($scope.flag==false)
										    				{
										    		if(value.specialityId == $scope.treatingDoctorSpecialityId){
														var obj={
																"specialityName":value.specialityName,
																"specialityId":value.specialityId
															}
															$scope.specialityList.push(obj);
														$scope.flag=true;
													}
										    				}
										    	});
										    		
									                
									                
										    		
										    		var obj=$scope.treatingDoctor;
										    		$scope.doctorListMedication.push(obj);
										    	}
										    	$scope.clinObj.servicePriorityId =1;
												// $scope.cancelSevices();
											}
											// Check Service in exist
											  var isServiceExistsForNotSelected;
												function checkServiceIdExistsForNotSelected(arr, val) {
													if(arr != null && arr.length >0){
													return arr.some(function(arr) {
																if(arr.serviceMasterId === val && arr.serviceRendered == 0 && arr.orderStatus == 'A')
																	{
																		return true;
																	}
																else
																	{
																		return false;
																	}
															});
													}else{
														 return false;
														}
												}
												 var isServiceExistsForNotSelected;
													function checkServiceIdExistsForNotSelectedTemeporaryAdd(arr, val) {
														if(arr != null && arr.length >0){
														return arr.some(function(arr) {
																	if(arr.serviceMasterId === val && arr.serviceRendered == 0 && arr.status == 'A')
																		{
																			return true;
																		}
																	else
																		{
																			return false;
																		}
																});
														}else{
															 return false;
															}
													}
													$scope.itemSelectedServiceList =[];
													$scope.itemSelectedServiceList.items  = [];
													
				                                  				
											$scope.checkServiceRenderOrNot = function()
											{
												    $rootScope.startLoader();
													var newArr = {};
													if ($scope.orderServiceId != undefined
															&& $scope.orderServiceId != "")
														{
															newArr.specialityId = $scope.orderSpecialtyId;
															newArr.serviceSpecialityId = $scope.orderSpecialtyId;
															newArr.serviceSubSpecialityId = $scope.orderSubSpecialtyId;
															newArr.subSpecialityId = $scope.orderSubSpecialtyId;
															newArr.serviceMasterId = $scope.orderServiceId;
															newArr.serviceStandardName = $scope.orderServiceName;
															newArr.specialityName = $scope.orderSpecialtyName;
															newArr.subSpecialityName = $scope.orderSubSpecialtyName;
															newArr.serviceRenderingDeptId = $scope.orderSubSpecialtyId;
															newArr.medallServiceId = $scope.medallServiceId;
															newArr.createdBy = $scope.userId;
															newArr.createdDate = new Date()
																	.getTime();
															newArr.selfPayable = "0";
															newArr.creditPayable = "0";
															newArr.serviceRendered = "0";
															newArr.doctorId = $scope.postCommonObj.doctorId;
															newArr.discountPercentage = 0;
															newArr.discount = 0;
															newArr.quantity = 1;
															newArr.basePrice = 0;
															newArr.basePriceOld = 0;
															newArr.totalAmt = 0;
															newArr.concession = 0;
															// newArr.selectedForFuture =
															// $scope.selectedForFuture;
															newArr.payeeId = $scope.postCommonObj.payeeId;
															newArr.apportionedPrice = null;
															newArr.packageId = null;
															newArr.packageTypeId = null;
															newArr.packageEitherOrGroupId = null;
															newArr.packageCapDetailsId = null;
															newArr.isCapService = null;
															newArr.status = 'A';
															newArr.isRefDoctorShare = $scope.orderIsRefDoctorShare;
															newArr.packageEitherOrGroupId = null;
															newArr.isPackageService = 'N';
															newArr.discountDetailsList = [];
															newArr.selectedForFuture = $scope.selectedForFuture;
															newArr.clinicalNote = $scope.clinObj.clinicalNote;
															newArr.servicePriorityId =$scope.clinObj.servicePriorityId;
															if(newArr.servicePriorityId==1)
											        		{
											                    newArr.servicePriority="Normal";
											            	}else if(newArr.servicePriorityId==2)
											            	{
											                    newArr.servicePriority="Emergency";
											            	}else{
											            		newArr.servicePriority="";
											            	}
															newArr.addClinicalNote = $scope.addClinicalNote;
															newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
															newArr.finalPrice =$scope.finalPriceService;
															newArr.serviceRate =$scope.serviceRateGlobal;
															if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined ){
											            		newArr.investigationFutureDate = $scope.investigationFutureDate
											            	
											            		newArr.investigationFutureDateToShowdddd = moment($scope.investigationFutureDate).format('DD/MM/YYYY')
											            		}
											            		
											            		if( $scope.investigationFutureDate == undefined && $scope.selectedForFuture == "Y"){
											            			
											            			growl.warning("Please select Date !!",
											    					   		{
											    								title : 'Warning!'
											    							});
											    							$rootScope.stopLoader();
											    							return false;
											                		}
											                		
											            		
											                    if( $scope.investigationFutureDate == ""  && $scope.selectedForFuture == "Y"){
											            			
											            			growl.warning("Please select Date !!",
											    					   		{
											    								title : 'Warning!'
											    							});
											    							$rootScope.stopLoader();
											    							return false;
											                		}
											                    $scope.dv = new Date();
											                    if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined ){
											                     if( $scope.investigationFutureDate < $scope.dv){
											            			
											            			growl.warning("Please select greater than todays date !!",
											    					   		{
											    								title : 'Warning!'
											    							});
											    							$rootScope.stopLoader();
											    							return false;
											                		}
											                    }
											                    
															if ($scope.selectedServiceTypeSpecialityId == ''
																	|| $scope.selectedSubServiceTypeSubSpecialityId == '') {
																growl.warning(
																		"Please select Service !!",
																		{
																			title : 'Warning!'
																		});
																$scope.selectedForFuture = 'N';
												        		$scope.fromDatepickerForFuture='';
												        		$scope.investigationFutureDate = "";
												        		$scope.editserviceHideFlag=false;
																$rootScope.stopLoader();
																return false;
															} else {
																
																if($scope.editserviceHideFlag == true){
											        				$scope.addedServiceOrdersForDisplay.splice($scope.editInvestigation , 1);
											        			}
																$scope.totalServiceAmount = 0;
										        			      $scope.totalAmount = 0;
																  $scope.addedServiceOrdersForDisplay.push(newArr);
																  if($scope.outsourceLabId == 2){
																	  $scope.servicesOrdersForMedall.push(newArr);
																  }
																  angular.forEach($scope.addedServiceOrdersForDisplay,function(value,key){
																	  $scope.totalServiceAmount += value.finalPrice;
																  })
																  if($scope.formPastVisitService == true){
																	  $scope.saveInvestigation();
																  }
															}
															$scope.searchObjjj.search = '';
															 $scope.selectedForFuture = 'N';
												        		$scope.fromDatepickerForFuture='';
												        		$scope.investigationFutureDate = "";
																$scope.selectedServiceTypeSpecialityId = '';
																$scope.selectedSubServiceTypeSubSpecialityId = '';
																$scope.clinObj.clinicalNote ="";
																$scope.clinObj.servicePriorityId="";
																 $scope.addClinicalNote = 'N';
																$scope.editserviceHideFlag=false;
																$scope.itemSelectAllListDiagnostic ='';
															$rootScope.stopLoader();
														} else {
															growl.warning("Please select Service !!",
																	{
																		title : 'Warning!'
																	});
															$scope.searchObjjj.search = '';
															$scope.selectedForFuture = 'N';
											        		$scope.fromDatepickerForFuture='';
											        		$scope.investigationFutureDate = "";
											        		$scope.clinObj.clinicalNote ="";
											        		$scope.clinObj.servicePriorityId="1";
											        		 $scope.addClinicalNote = 'N';
															$rootScope.stopLoader();
														}
													
													
											}
											$scope.checkService = function(){
												
			                                    $scope.addedServiceOrdersForDisplay =[];
			                                    var finalData = {
			                                            orgId : $scope.organizationId,
			                                            orgUnitId : $scope.unitId,
			                                        };
			                                    var data ={


			                                            currentDate : moment(new Date()).format(
			                                                    'YYYY-MM-DD HH:mm:ss'),
			                                            createdDate : new Date().getTime(),
			                                            updatedDate : new Date().getTime(),
			                                            orgId : $scope.organizationId,
			                                            orgUnitId : $scope.unitId,
			                                            createdBy : $scope.userId,
			                                            updatedBy : $scope.userId,
			                                            patientId : $scope.postCommonObj.patientId,
			                                            admissionId : $scope.postCommonObj.admissionId,
			                                            encounterId : $scope.postCommonObj.encounterId,
			                                            defaultSelfTariffId : $scope.defaultSelfTariffId,
			                                            paymentEntitlementTypeId : $scope.postCommonObj.paymentEntitlementId,
			                                            doctorId : $scope.medicationDoctorId,
			                                            ordDocSplId :$scope.medicationSpecialityId,
			                                            priorityId : 2,
			                                            visitTypeId : $scope.postCommonObj.visitTypeId,
			                                            orderDate : new Date().getTime(),
			                                            orderStatus : 'A',
			                                            orderStatusId : 1,
			                                            contractId : $scope.postCommonObj.contractId,
			                                            payeeId : $scope.postCommonObj.payeeId,
			                                            billingBedCategoryId : $scope.billingObject.bedBillingCategoryId,
			                                            paymentEntitlementTypeId : $scope.postCommonObj.paymentEntitlementId,
			                                            patientTypeId : $scope.postCommonObj.patientCategoryId
			                                        
			                                    }
			                                        if( $scope.itemSelectedServiceList.items.length > 0){
			                                            $rootScope.startLoader();
			                                            $scope.serviceSelectedList.items =  angular.copy($scope.itemSelectedServiceList.items);
			                                        
			                                            var selectedFavService = $scope.serviceSelectedList.items;
			                                            
			                                              angular.forEach($scope.serviceSelectedList.items, function(value, key) 
			                                                    {var newArr = {};
			                                                    newArr.serviceId = value.serviceMasterId;
			                                                    newArr.specialityId = value.specialityId;
			                                                    newArr.selectedForFuture = 'N';
			                                                    newArr.specialityName = value.specialityName;
			                                                    newArr.subSpecialityId = value.subSpecilaityId;
			                                                    newArr.serviceStandardName = value.serviceStandardName;
			                                                    newArr.selectedForFuture = $scope.selectedForFuture;
			                                                    newArr.serviceSpecialityId = value.specialityId;
			                                                    newArr.subSpecialityId = value.subSpecialityId;
			                                                    newArr.serviceMasterId = value.serviceMasterId;
			                                                    newArr.serviceRenderingDeptId = value.subSpecialityId;
			                                                    newArr.serviceSubSpecialityId = value.subSpecialityId;
			                                                    newArr.isAppointable = value.isAppointable;
			                                                    
			                                                        $scope.orderServiceId= value.serviceMasterId;
			                                                        $scope.orderSpecialtyId = value.specialityId;
			                                                          $scope.orderSubSpecialtyId = value.subSpecilaityId;
			                                                        $scope.orderServiceName = value.serviceStandardName;
			                                                        $scope.orderSpecialtyName = value.specialityName;
			                                                        $scope.orderSubSpecialtyName= value.subSpecialityName;
			                                                        $scope.search = value.serviceStandardName;
			                                                        $scope.selectedServiceTypeSpecialityId = value.specialityId;
			                                                        $scope.selectedSubServiceTypeSubSpecialityId = value.subSpecialityId;
			                                                        
			                                                    newArr.createdBy = $scope.userId;
			                                                    newArr.createdDate = new Date().getTime();
			                                                    newArr.selfPayable = "0";
			                                                    newArr.creditPayable = "0";
			                                                    newArr.serviceRendered = "0";
			                                                    newArr.doctorId = $scope.postCommonObj.doctorId;
			                                                    newArr.discountPercentage = 0;
			                                                    newArr.discount = 0;
			                                                    newArr.quantity = 1;
			                                                    newArr.basePrice = 0;
			                                                    newArr.basePriceOld = 0;
			                                                    newArr.totalAmt = 0;
			                                                    newArr.concession = 0;
			                                                    newArr.payeeId = $scope.postCommonObj.payeeId;
			                                                    newArr.apportionedPrice = null;
			                                                    newArr.packageId = null;
			                                                    newArr.packageTypeId = null;
			                                                    newArr.packageEitherOrGroupId = null;
			                                                    newArr.packageCapDetailsId = null;
			                                                    newArr.isCapService = null;
			                                                    newArr.status = 'A';
			                                                    newArr.isRefDoctorShare = value.isRefDoctorShare;
			                                                    newArr.packageEitherOrGroupId = null;
			                                                    newArr.isPackageService = 'N';
			                                                    newArr.finalPrice =0;
			                                                    newArr.discountDetailsList = [];
			                                                    $scope.fromOrderSetServices = true;
			                                                    newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
			                                                    $scope.addedServiceOrdersForDisplay.push(newArr);
			                                                    });
			                                              
			                                              if($scope.formPastVisitService == true){
			                                                  $scope.saveInvestigation();
			                                              }
											}
											}
											$scope.clearService =function(){
												$scope.addedServiceOrdersForDisplay =[];
												$scope.totalServiceAmount = 0;
							  			      $scope.totalAmount = 0;
											}
											
											$scope.checkFavService = function(){
			                                    /* $scope.addedServiceOrdersForDisplay =[]; */
			                                    var finalData = {
			                                            orgId : $scope.organizationId,
			                                            orgUnitId : $scope.unitId,
			                                        };
			                                    var data ={


			                                            currentDate : moment(new Date()).format(
			                                                    'YYYY-MM-DD HH:mm:ss'),
			                                            createdDate : new Date().getTime(),
			                                            updatedDate : new Date().getTime(),
			                                            orgId : $scope.organizationId,
			                                            orgUnitId : $scope.unitId,
			                                            createdBy : $scope.userId,
			                                            updatedBy : $scope.userId,
			                                            patientId : $scope.postCommonObj.patientId,
			                                            admissionId : $scope.postCommonObj.admissionId,
			                                            encounterId : $scope.postCommonObj.encounterId,
			                                            defaultSelfTariffId : $scope.defaultSelfTariffId,
			                                            paymentEntitlementTypeId : $scope.postCommonObj.paymentEntitlementId,
			                                            doctorId : $scope.medicationDoctorId,
			                                            ordDocSplId :$scope.medicationSpecialityId,
			                                            priorityId : 2,
			                                            visitTypeId : $scope.postCommonObj.visitTypeId,
			                                            orderDate : new Date().getTime(),
			                                            orderStatus : 'A',
			                                            orderStatusId : 1,
			                                            contractId : $scope.postCommonObj.contractId,
			                                            payeeId : $scope.postCommonObj.payeeId,
			                                            billingBedCategoryId : $scope.billingObject.bedBillingCategoryId,
			                                            paymentEntitlementTypeId : $scope.postCommonObj.paymentEntitlementId,
			                                            patientTypeId : $scope.postCommonObj.patientCategoryId
			                                        
			                                    }
			                                        if( $scope.serviceSelectedList.items.length > 0){
			                                            $rootScope.startLoader();
			                                            
			                                              angular.forEach($scope.serviceSelectedList.items, function(value, key) 
			                                                    {var newArr = {};
			                                                    newArr.serviceId = value.serviceMasterId;
			                                                    newArr.specialityId = value.specialityId;
			                                                    newArr.selectedForFuture = 'N';
			                                                    newArr.specialityName = value.specialityName;
			                                                    newArr.subSpecialityId = value.subSpecilaityId;
			                                                    newArr.serviceStandardName = value.serviceStandardName;
			                                                    newArr.selectedForFuture = $scope.selectedForFuture;
			                                                    newArr.serviceSpecialityId = value.specialityId;
			                                                    newArr.subSpecialityId = value.subSpecialityId;
			                                                    newArr.serviceMasterId = value.serviceMasterId;
			                                                    newArr.serviceRenderingDeptId = value.subSpecialityId;
			                                                    newArr.serviceSubSpecialityId = value.subSpecialityId;
			                                                    newArr.isAppointable = value.isAppointable;
			                                                    
			                                                        $scope.orderServiceId= value.serviceMasterId;
			                                                        $scope.orderSpecialtyId = value.specialityId;
			                                                          $scope.orderSubSpecialtyId = value.subSpecilaityId;
			                                                        $scope.orderServiceName = value.serviceStandardName;
			                                                        $scope.orderSpecialtyName = value.specialityName;
			                                                        $scope.orderSubSpecialtyName= value.subSpecialityName;
			                                                        $scope.search = value.serviceStandardName;
			                                                        $scope.selectedServiceTypeSpecialityId = value.specialityId;
			                                                        $scope.selectedSubServiceTypeSubSpecialityId = value.subSpecialityId;
			                                                        
			                                                    newArr.createdBy = $scope.userId;
			                                                    newArr.createdDate = new Date().getTime();
			                                                    newArr.selfPayable = "0";
			                                                    newArr.creditPayable = "0";
			                                                    newArr.serviceRendered = "0";
			                                                    newArr.doctorId = $scope.postCommonObj.doctorId;
			                                                    newArr.discountPercentage = 0;
			                                                    newArr.discount = 0;
			                                                    newArr.quantity = 1;
			                                                    newArr.basePrice = 0;
			                                                    newArr.basePriceOld = 0;
			                                                    newArr.totalAmt = 0;
			                                                    newArr.concession = 0;
			                                                    newArr.payeeId = $scope.postCommonObj.payeeId;
			                                                    newArr.apportionedPrice = null;
			                                                    newArr.packageId = null;
			                                                    newArr.packageTypeId = null;
			                                                    newArr.packageEitherOrGroupId = null;
			                                                    newArr.packageCapDetailsId = null;
			                                                    newArr.isCapService = null;
			                                                    newArr.status = 'A';
			                                                    newArr.isRefDoctorShare = value.isRefDoctorShare;
			                                                    newArr.packageEitherOrGroupId = null;
			                                                    newArr.isPackageService = 'N';
			                                                    newArr.finalPrice = value.finalPrice;
			                                                    newArr.discountDetailsList = [];
			                                                    $scope.fromOrderSetServices = true;
			                                                    newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
			                                                   /* $scope.addedServiceOrdersForDisplay.push(newArr); */
			                                                    $scope.totalServiceAmount = 0;
										        			      $scope.totalAmount = 0;
																 
																  angular.forEach($scope.addedServiceOrdersForDisplay,function(value,key){
																	  $scope.totalServiceAmount += value.finalPrice;
																  })
																  $scope.search = '';
														            	$scope.selectedServiceTypeSpecialityId='';
														            	$scope.selectedSubServiceTypeSubSpecialityId='';
														            	$scope.selectedForFuture = 'N';
														            	$scope.investigationFutureDate='';
														        		$scope.fromDatepickerForFuture='';
			                                                    $rootScope.stopLoader();
			                                                    });
			                                             
			                                        }
			                                    $scope.itemSelectedServiceList = {
			                                            items: []
			                                          };
											}	
											
											
											 $scope.forFutureDatePopup = function(selectedForFuture){
										        	if(selectedForFuture =='Y' && $scope.orderServiceId!=null){
										        		$("#getDateModal").show();
										        	}
										        	else if(selectedForFuture =='N'){
										        		$scope.fromDatepickerForFuture='';
										        		$scope.investigationFutureDate = "";
										        		$("#getDateModal").hide();
										        	}
										        }
										       
										        $scope.dateOptionsForFuture =
												{
													formatYear : 'yyyy',
													showWeeks : false,
													minDate : moment(new Date()).add(1, 'days').toDate()
												};
										        
										        $scope.openFromDatePicker = function() {
													$scope.fromDatepicker.opened = true;
												};
												$scope.fromDatepicker = {
														opened : false
												};
										        
												$scope.saveFutureDate=function(){
													
													if($scope.fromDatepickerForFuture !=""){
														$scope.investigationFutureDate = $scope.fromDatepickerForFuture;
														$("#getDateModal").hide();
													}else{
														growl.warning("Please select future date !!",
														   		{
																	title : 'Warning!'
																});
																$rootScope.stopLoader();
													}
														$scope.investigationFutureDate=$scope.fromDatepickerForFuture;
														console.log("futureDateForInvestigation",$scope.investigationFutureDate);
														
												}
												$scope.cancelFutureDate=function(){
													$scope.selectedForFuture = 'N';
													$("#getDateModal").hide();
											    }
												
										        
											$scope.editTempServicesForFutByIndex = function(item,indx) {
												
												$scope.editInvestigation=indx;
												if($scope.addedServiceOrdersForDisplay.length==1)
												{
												$scope.isServiceAdded=false;
												}
												$scope.editserviceHideFlag=true;
												$scope.model = item.serviceStandardName;
												$scope.label = item.serviceStandardName;
												$scope.search = item.serviceStandardName;
												$scope.clinObj.clinicalNote = item.clinicalNote;
												$scope.clinObj.servicePriorityId = item.servicePriorityId;
												$scope.addClinicalNote = item.addClinicalNote;
												$scope.addServiceToTempServiceList(item, $scope.model, $scope.label);
											}
											$scope.isServiceAdded=false;
											$scope.servicesOrdersForMedall=[];
											 $scope.addedServiceOrdersForDisplay =[];
											$scope.addInvestigation = function() {
												$rootScope.startLoader();

												// validate service in current added package
													var newArr = {};
													if ($scope.orderServiceId != undefined
															&& $scope.orderServiceId != "") {

														
														newArr.specialityId = $scope.orderSpecialtyId;
														newArr.serviceSpecialityId = $scope.orderSpecialtyId;
														newArr.serviceSubSpecialityId = $scope.orderSubSpecialtyId;
														newArr.subSpecialityId = $scope.orderSubSpecialtyId;
														newArr.serviceMasterId = $scope.orderServiceId;
														newArr.serviceStandardName = $scope.orderServiceName;
														newArr.specialityName = $scope.orderSpecialtyName;
														newArr.subSpecialityName = $scope.orderSubSpecialtyName;
														newArr.serviceRenderingDeptId = $scope.orderSubSpecialtyId;
														newArr.medallServiceId = $scope.medallServiceId;
														newArr.createdBy = $scope.userId;
														newArr.createdDate = new Date()
																.getTime();
														newArr.selfPayable = "0";
														newArr.creditPayable = "0";
														newArr.serviceRendered = "0";
														newArr.doctorId = $scope.postCommonObj.doctorId;
														newArr.discountPercentage = 0;
														newArr.discount = 0;
														newArr.quantity = 1;
														newArr.basePrice = 0;
														newArr.basePriceOld = 0;
														newArr.totalAmt = 0;
														newArr.concession = 0;
														newArr.selectedForFuture = $scope.selectedForFuture;
														newArr.payeeId = $scope.postCommonObj.payeeId;
														newArr.apportionedPrice = null;
														newArr.packageId = null;
														newArr.packageTypeId = null;
														newArr.packageEitherOrGroupId = null;
														newArr.packageCapDetailsId = null;
														newArr.isCapService = null;
														newArr.status = 'A';
														newArr.isRefDoctorShare = $scope.orderIsRefDoctorShare;
														newArr.packageEitherOrGroupId = null;
														newArr.isPackageService = 'N';
														newArr.discountDetailsList = [];
														newArr.investigationFutureDateToShowdddd = moment(new Date()).format($scope.dateFormat.toUpperCase());
														newArr.finalPrice =$scope.finalPriceService;
														newArr.serviceRate =$scope.serviceRateGlobal;
														newArr.clinicalNote = $scope.clinObj.clinicalNote;
														newArr.servicePriorityId = $scope.clinObj.servicePriorityId != "" ? $scope.clinObj.servicePriorityId :1;
														if(newArr.servicePriorityId==1)
										        		{
										                    newArr.servicePriority="Normal";
										            	}else if(newArr.servicePriorityId==2)
										            	{
										                    newArr.servicePriority="Emergency";
										            	}else{
										            		newArr.servicePriority="";
										            	}
														newArr.addClinicalNote = $scope.addClinicalNote;
														if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined && $scope.selectedForFuture == "Y" ){
											        		newArr.investigationFutureDate = $scope.investigationFutureDate
											        	
											        		newArr.investigationFutureDateToShowdddd = moment($scope.investigationFutureDate).format($scope.dateFormat.toUpperCase())
											        		}
											        		
											        		if( $scope.investigationFutureDate == undefined && $scope.selectedForFuture == "Y"){
											        			
											        			growl.warning("Please select Date !!",
																   		{
																			title : 'Warning!'
																		});
																		$rootScope.stopLoader();
																		return false;
											            		}
											            		
											        		
											                if( $scope.investigationFutureDate == ""  && $scope.selectedForFuture == "Y"){
											        			
											        			growl.warning("Please select Date !!",
																   		{
																			title : 'Warning!'
																		});
																		$rootScope.stopLoader();
																		return false;
											            		}
											                $scope.dv = new Date();
											                if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined && $scope.selectedForFuture == "Y"){
											                 if( $scope.investigationFutureDate < $scope.dv){
											        			
											        			growl.warning("Please select greater than todays date !!",
																   		{
																			title : 'Warning!'
																		});
																		$rootScope.stopLoader();
																		return false;
											            		}
											                }
											                
														if ($scope.searchObjjj.search == ''
																|| $scope.selectedServiceTypeSpecialityId == ''
																|| $scope.selectedSubServiceTypeSubSpecialityId == '') {
															growl.warning(
																	"Please select Service !!",
																	{
																		title : 'Warning!'
																	});
															$scope.searchObjjj.search="";
															$scope.selectedForFuture = 'N';
											        		$scope.fromDatepickerForFuture='';
											        		$scope.investigationFutureDate = "";
											        		$scope.editserviceHideFlag = false;
															$rootScope.stopLoader();
															return false;
														} else 
														{
															
															$scope.isServiceAdded=true;
															// $scope.addedServiceOrdersForDisplay.push(newArr);
															isServiceExistsForNotSelected=checkServiceIdExistsForNotSelected($scope.currentEncounterOrderLists,$scope.orderServiceId)
															  if (!isServiceExistsForNotSelected) {
																  
																  isServiceExistsForNotSelected=checkServiceIdExistsForNotSelectedTemeporaryAdd($scope.addedServiceOrdersForDisplay,$scope.orderServiceId)
																  if (!isServiceExistsForNotSelected) {
																	  if($scope.editserviceHideFlag == true){
																		  $scope.addedServiceOrdersForDisplay.splice($scope.editInvestigation , 1);
																		  }
																	  if($scope.editserviceHideFlag == true){
													        				$scope.addedServiceOrdersForDisplay.splice($scope.editInvestigation , 1);
													        			}
																	  $scope.addedServiceOrdersForDisplay.push(newArr);
																	  if($scope.outsourceLabId == 2){
																		  $scope.servicesOrdersForMedall.push(newArr);
																	  }
																	  $scope.totalServiceAmount = 0;
											        			      $scope.totalAmount = 0;
																	  angular.forEach($scope.addedServiceOrdersForDisplay,function(value,key){
																		  $scope.totalServiceAmount += value.finalPrice;
																	  })
																	  $scope.searchObjjj.search = '';
																	  $scope.selectedForFuture = 'N';
														            	$scope.investigationFutureDate='';
														        		$scope.fromDatepickerForFuture='';
																		$scope.selectedServiceTypeSpecialityId = '';
																		$scope.selectedSubServiceTypeSubSpecialityId = '';
																	    $scope.clinObj.clinicalNote ="";
																	    $scope.clinObj.servicePriorityId = "1";
																	    $scope.addClinicalNote = 'N';
																		$scope.editserviceHideFlag=false;
																  }
																  else{
																	  angular.element('#pendingServiceAdd').modal('show');
																  }
															  }
															  else
																  {
																    angular.element('#pendingServiceAdd').modal('show');
																  }
														}
														
														if(labConfig.cystopathologyId == newArr.serviceSubSpecialityId || labConfig.histopathologyId == newArr.serviceSubSpecialityId ){
															$("#diagnosisPopCoversheet").show();
															$scope.initPatientDaignosis();
															$scope.getPatientServiceDaignosisData();
														}
														
														var checkCTAndMRI = false;
														
														$scope.mriCtList.forEach(function(value,index) {
															if((newArr.serviceSubSpecialityId == value.subSpecialityMasterId && value.isMriModality =='Y') 
																	|| (newArr.serviceSubSpecialityId == value.subSpecialityMasterId && value.isCTModality =='Y')){
																checkCTAndMRI = true;
																
															}
														})
														
														if(checkCTAndMRI == true){
															$("#diagnosisPopCoversheet").show();
															$scope.initPatientDaignosis();
															$scope.getPatientServiceDaignosisData();
														}
														
														
														$rootScope.stopLoader();
														
														
													} else {
														growl.warning("Please select Service !!",
																{
																	title : 'Warning!'
																});
														$rootScope.stopLoader();
													}
													$timeout(function() {
														 document.getElementById("searchServiceIdDiagnostic").focus();
													}, 500);
											}
											
											
											
											$scope.saveInvestigation = function() {
												$rootScope.startLoaderPopup();
												var listOrderMasterDto = [];
												var finalData = {
														orgId : $scope.organizationId,
														orgUnitId : $scope.unitId,
												};
					
												// Code optimization by Madhuri
												var data = {
				                                		createdDate : new Date().getTime(),
				                                		updatedDate : new Date().getTime(),
				                                		createdBy : $scope.userId,
				                                		updatedBy : $scope.userId,
				                                		organisationId : $scope.organizationId,
				                                		unitId : $scope.unitId,
				                                		patientId : $scope.postCommonObj.patientId,
				                                		admissionId : $scope.postCommonObj.admissionId,
				                                		encounterId : $scope.postCommonObj.encounterId,
				                                		tariffId : $scope.defaultSelfTariffId,
				                                		ordDoctorId : $scope.medicationDoctorId,
				                    	        		ordDocSplId : $scope.medicationSpecialityId,
				                                		priorityId : 2,
				                                		visitTypeId : $scope.postCommonObj.visitTypeId,
				                                		orderDate : new Date().getTime(),
				                                		contractId : $scope.billingObject.contractId,
				                                		isFinalBill : 'N',
				                                		orderDate : new Date().getTime(0),
				                                		billDateTime : new Date().getTime(0),
				                                		isCreditBill : 'N',
				                                		billCancelled : 'N',
				                                		isFinancialClearance : 'N',
				                                		billingBedCategoryId : $scope.billingObject.bedBillingCategoryId,
				                                		isInterimBill : 'N',
				                                		isDrNote : 'N',
				                                		selfPayeeId : $scope.selfPayeeId,
				                                		isPreBilling : $scope.isPreOrPostBilling,
				                                		updatePayee : false,
				                                		capExist :  $scope.capExist,
				                                        packageExist :  ($scope.listPackageOrder.length>0)? true : false,
				                                        isBillManipulationAllowed : true,
				                                		paymentEntitlementId : $scope.billingObject.paymentEntitlementId,
				                                		patientTypeId : $scope.billingObject.patientCategoryId,
				                                		applyAuth : false,
				                                		needToUpdatePharmacyService : false,
				                                		netAmount : $scope.totalServiceAmount,
				                                		discountAmount : 0.00,
				                                		concessionAmount : 0.00,
				                                		gst: 0.00,
				                                		totalBillAmount : $scope.totalServiceAmount,
				                                		roundOff : 0.00,
				                                        isCreditBill : 'N',
				                                        billCancelled : 'N',
				                                        isFinancialClearance : 'N',
				                                        billRemark : '',
				                                        billStatusId : 1,
				                                        selfPayable : $scope.totalServiceAmount,
				                                        creditPayable : "0.00"
				                                }
												
												 var payeeDetails ={
											            payeeId : $scope.postCommonObj.payeeId,
											            billAmount : $scope.totalServiceAmount
				                                }
												var billPayeeDetailsDtosList = [];
												billPayeeDetailsDtosList.push(payeeDetails);


												var finalOrderServiceList = [];
												var finalOrderServiceListForFuture = [];
									        	var justChkFlgFut = 0;
									        	var justChkFlgCurr = 0;


												angular.forEach($scope.addedServiceOrdersForDisplay, function(value, key) {
										            var newArr = {};
										            newArr.discountOwnership = 0;
				                                    newArr.isOrderVisibleDepartment='A';
				                                    newArr.markupStatus = false;
				                                    newArr.patientClassPercentage = 0.00;
				                                    newArr.orgId = $scope.organizationId;
				                                    newArr.orgUnitId = $scope.unitId;
				                                    newArr.orderQty = 1;
				                                    newArr.serviceId = value.serviceMasterId;
				                                    newArr.priorityId = '2';
				                                    newArr.isOutsourced = 'N';
				                                    newArr.orderDate = new Date().getTime();
				                                    newArr.orderDateString =  moment(value.orderDate).format('DD/MM/YYYY HH:mm');
				                                    newArr.orderSchDate=new Date(value.investigationFutureDate).getTime();
				                                    newArr.ordConcession = 0.00;
				                                    newArr.ordDiscount = 0.00;
				                                    newArr.serviceAmt = value.serviceRate;
				                                    newArr.dummyServiceAmt = value.serviceRate;
				                                    newArr.ordDocSplId = $scope.medicationSpecialityId;
				                                    newArr.taxId = value.taxId;
				                                    newArr.taxPer = value.taxPercentage;
				                                    newArr.taxAmount= value.taxAmt;
				                                    newArr.payeeId = $scope.billingObject.payeeId;
				                                    newArr.tariffId = $scope.defaultSelfTariffId;
				                                    newArr.billingClassId=1;
				                                    newArr.ordDocId = $scope.medicationDoctorId;
				                                    newArr.serviceRenderingDeptId = value.subSpecialityId;
				                                    newArr.ordRenderDatetime = new Date().getTime();
				                                    newArr.serviceIsBilled = 0;
				                                    newArr.status = 'A';
				                                    newArr.createdDate = new Date().getTime();
				                                    newArr.updatedDate = new Date().getTime();
				                                    newArr.createdBy = $scope.userId;
				                                    newArr.updatedBy = $scope.userId;
				                                    newArr.ordCancelled ='N';
				                                    newArr.isProcedureService = value.isPackageService;
				                                    newArr.isPostedWhileDischarge = 'N';
				                                    newArr.ordTotalAmt= value.serviceRate;
				                                    newArr.netAmount =  value.finalPrice;
				                                    newArr.ordDiscountPer =  0;
				                                    newArr.isPackage =  false;
				                                    newArr.selfPayable =  value.netAmount;
				                                    newArr.creditPayable =  0;
				                                    newArr.oldCreditPayable =  0;
				                                    newArr.oldSelfPayable =  0;
				                                    newArr.isDiscountApplicable =  'N';
				                                    newArr.isDiscount =  null;
				                                    newArr.orderEmergencyFlag =  'N';
				                                    newArr.isDrug =  'N';
				                                    newArr.packageId =  value.packageId;
				                                    newArr.packageTypeId =  value.packageTypeId;
				                                    newArr.isPackageService =  value.isPackageService;
				                                    newArr.apportionedPrice =  value.apportionedPrice;
				                                    newArr.packageConsumptionAmt =  value.packageConsumptionAmt;
				                                    newArr.departmentId =  value.specialityId;
				                                    newArr.subDepartmentId =  value.subSpecialityId;
				                                    newArr.isRefDoctorShare =  value.isRefDoctorShare;
				                                    newArr.isDocReq =  'Y';
				                                    newArr.packageEitherOrGroupId =   value.packageEitherOrGroupId;
				                                    newArr.admissionId =   $scope.postCommonObj.admissionId;
				                                    newArr.encounterId = $scope.postCommonObj.encounterId;
				                                    newArr.contractId =  null;
				                                    newArr.bedCategoryId =  $scope.billingObject.bedBillingCategoryId;
				                                   	newArr.bedId = $scope.billingObject.bedId;
				                                   	newArr.wardId = $scope.billingObject.wardId;
				                                   	newArr.roomId = $scope.billingObject.roomId;
				                                    newArr.discountDetailsList =  [];
				// newArr.isFromAddservice = 'Y';
				                                    newArr.isFromServiceSerach = 'Y';
				                                    newArr.debitNoteService =  false;
				                                    newArr.isDummyService =  0;
				                                    newArr.isHiddenService =  0;
				                                    newArr.serviceRendered = 0;
				                                    newArr.serviceChargeable = 1;
				                                    newArr.isInvestigationOrder = 'Y';
													newArr.isLabService = labConfig.labSpecialityId==newArr.departmentId ? 'Y' : 'N';
				                                    newArr.orderSchDate=  value.selectedForFuture == 'Y'? new Date(value.investigationFutureDate).getTime():new Date().getTime();
				                                    newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPackageMapperDtosList;
				                                    newArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
				                                    newArr.servicePriorityId =  value.servicePriorityId != "" ? value.servicePriorityId :1;
													finalOrderServiceList.push(newArr);
												});
												 data.billPayeeDetailsDtosList = billPayeeDetailsDtosList;
													data.orderDetailsMasterDtosList = finalOrderServiceList;


												finalData.listOrderMasterDto = listOrderMasterDto;
												console.log ($scope.addedServiceOrdersForDisplay.serviceId);
												console.log("INV", JSON.stringify(finalData));
												if($scope.addedServiceOrdersForDisplay.length > 0){
													var URI = BASE_URL + ROOT_URL
													+ "/api/billing/update/service"
											GenericServiceParamHeader
													.serviceAction(METHOD_POST, URI,
															data,
															NOTIFICATION_MSG_STATUS_TRUE)
													.then(
																	function(response) {
																		if (response.data.status == "success") {
																			$scope.afterSaveCallNueron = true;
																			$rootScope.stopLoaderPopup();
																			$scope.currentsaveOrderId = response.data.listObject2;
																			$scope.getInvetigation();
																			$scope.totalServiceAmount = 0;
				// $scope.selectedServiceTypeSpecialityId = "";
				// $scope.selectedSubServiceTypeSubSpecialityId = "";
																			$scope.itemSelectAllListDiagnostic ='';
																			   $scope.search = '';
																            	$scope.selectedServiceTypeSpecialityId='';
																            	$scope.selectedSubServiceTypeSubSpecialityId='';
																            	$scope.selectedForFuture = 'N';
																            	$scope.investigationFutureDate='';
																        		$scope.fromDatepickerForFuture='';
																        		$scope.clinObj.clinicalNote ="";
																        		$scope.clinObj.servicePriorityId =1;
																        		 $scope.addClinicalNote = 'N';
																        		 $scope.selectedOrderSet=[];
																			$scope.proceduerReqId = undefined;
																			$scope.addedServiceOrdersForDisplay = [];
																			$scope.fromOrderSetServices= false;
																			$scope.editserviceHideFlag=false;
																			$scope.formPastVisitService = false;
																			$scope.itemSelectedServiceList.items  = [];
																			$scope.isServiceAdded=false;

																		}
																	});
												} else {
													growl
															.warning(
																	"Please add investigation and then save !!",
																	{
																		title : 'Warning!'
																	});
													$rootScope.stopLoaderPopup();
													$rootScope.stopLoader();
												}

											}
											
											$scope.serviceSelectedList = {
													items: []
												  };
											$scope.dublicateSerivceList = [];
											 $scope.getFavoritesServices = function()
										        {
												 $scope.serviceSelectedList = {
															items: []
														  };
										        	var data ={
										        			doctorId :$scope.postCommonObj.doctorId,
										        			unitId : $scope.unitId
										        	};
										        	
										    		var URI = BASE_URL + ROOT_URL + "/api/opd/doctorFavoritesServiceListByDocId";
													GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
															data, NOTIFICATION_MSG_STATUS_FALSE)
															.then(function(response) {
																if(response.data.status == "success"){
																	$scope.favouriteServices = [];
																	$scope.favouriteServices = response.data.listObject;
																	console.log("fav services",$scope.favouriteServices);
																}
													});
										        }
											 $scope.totalServiceAmount = 0;
										        $scope.addFavouriteService = function()
										        {

													
													var selectedFavService = $scope.serviceSelectedList.items;
													
													console.log("selected services",$scope.serviceSelectedList);
													console.log("selected services1",$scope.serviceSelectedList.items);
													
													  angular.forEach($scope.serviceSelectedList.items, function(value, key) 
													    		{
													        	var newArr = {};
													        	newArr.serviceId = value.serviceMasterId;
																newArr.specialityId = value.specialityId;
																newArr.selectedForFuture = 'N';
																newArr.specialityName = value.specialityName;
																newArr.subSpecialityId = value.subSpecilaityId;
																newArr.serviceStandardName = value.serviceStandardName;
																newArr.selectedForFuture = $scope.selectedForFuture;
																newArr.serviceSpecialityId = value.specialityId;
																newArr.subSpecialityId = value.subSpecialityId;
																newArr.serviceMasterId = value.serviceMasterId;
																newArr.serviceRenderingDeptId = value.subSpecialityId;
																newArr.serviceSubSpecialityId = value.subSpecialityId;
																newArr.isAppointable = value.isAppointable;
																
																	$scope.orderServiceId= value.serviceMasterId;
													        		$scope.orderSpecialtyId = value.specialityId;
													          		$scope.orderSubSpecialtyId = value.subSpecilaityId;
													        		$scope.orderServiceName = value.serviceStandardName;
													        		$scope.orderSpecialtyName = value.specialityName;
													            	$scope.orderSubSpecialtyName= value.subSpecialityName;
													            	$scope.search = value.serviceStandardName;
													            	$scope.selectedServiceTypeSpecialityId = value.specialityId;
													            	$scope.selectedSubServiceTypeSubSpecialityId = value.subSpecialityId;
													            	
																newArr.createdBy = $scope.userId;
																newArr.createdDate = new Date().getTime();
																newArr.selfPayable = "0";
																newArr.creditPayable = "0";
																newArr.serviceRendered = "0";
																newArr.doctorId = $scope.postCommonObj.doctorId;
																newArr.discountPercentage = 0;
																newArr.discount = 0;
																newArr.quantity = 1;
																newArr.basePrice = 0;
																newArr.basePriceOld = 0;
																newArr.totalAmt = 0;
																newArr.concession = 0;
																newArr.payeeId = $scope.postCommonObj.payeeId;
																newArr.apportionedPrice = null;
																newArr.packageId = null;
																newArr.packageTypeId = null;
																newArr.packageEitherOrGroupId = null;
																newArr.packageCapDetailsId = null;
																newArr.isCapService = null;
																newArr.status = 'A';
																newArr.isRefDoctorShare = value.isRefDoctorShare;
																newArr.packageEitherOrGroupId = null;
																newArr.isPackageService = 'N';
																newArr.finalPrice =0;
																newArr.discountDetailsList = [];
																$scope.fromOrderSetServices = true;
																newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
																
																var obj = {
																		
																		"admissionId" :$scope.postCommonObj.admissionId,
																		"contractId" :$scope.postCommonObj.contractId,
																		"serviceId" :newArr.serviceMasterId,
																		"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
																		"organizationId":$scope.organizationId,
																		"unitId":$scope.unitId,
																		"visitTypeId":$scope.visitTypeId,
																		"billingBedCategoryId":$scope.postCommonObj.billingBedCategoryId,
																		"patientCategoryId":$scope.postCommonObj.patientCategoryId,
																		"defaultSelfTariffId":$scope.defaultSelfTariffId,
																	    "selfPayeeId" :$scope.selfPayeeId,
																	    "quantity":1,
																	    "patientId":$scope.patientDetails.patientId,
																	    "tariffId" :$scope.defaultSelfTariffId,
																	    "paymentEntitlementId": $scope.postCommonObj.paymentEntitlementId
																	    
																	};
											        			
											        			if($scope.editserviceHideFlag == true){
											        				$scope.addedServiceOrdersForDisplay.splice($scope.editInvestigation , 1);
											        			}
											        			isServiceExistsForNotSelected=checkServiceIdExistsForNotSelected($scope.currentEncounterOrderLists,value.serviceMasterId)
																  if (!isServiceExistsForNotSelected) {
																	  
																	  isServiceExistsForNotSelected=checkServiceIdExistsForNotSelectedTemeporaryAdd($scope.addedServiceOrdersForDisplay,value.serviceMasterId)
																	  if (!isServiceExistsForNotSelected) {
																		  
																		  var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
																			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
																			{
																				if(response.data.object != "undefined" && response.data.object != null)
																				{
																					$scope.finalPriceService = response.data.object.finalPrice;
																					$scope.serviceRateGlobal = response.data.object.serviceRate;
																					var idx = $scope.addedServiceOrdersForDisplay.findIndex(item => item.serviceId ==  value.serviceMasterId);
																					$scope.addedServiceOrdersForDisplay[idx].finalPrice = response.data.object.finalPrice;
																					$scope.addedServiceOrdersForDisplay[idx].serviceRate = response.data.object.serviceRate;
																					$scope.totalServiceAmount += parseFloat($scope.addedServiceOrdersForDisplay[idx].finalPrice);
																					value.finalPrice = response.data.object.finalPrice;
																					value.serviceRate = response.data.object.serviceRate;
																				}
																				});
																			
																		  $scope.addedServiceOrdersForDisplay.push(newArr);
																		    $scope.search = '';
															            	$scope.selectedServiceTypeSpecialityId='';
															            	$scope.selectedSubServiceTypeSubSpecialityId='';
															            	$scope.selectedForFuture = 'N';
															            	$scope.investigationFutureDate='';
															        		$scope.fromDatepickerForFuture='';

																	  }
																	  else{
																		  $scope.orderDetailsIdP=null;
																		  $scope.dublicateSerivceList.push(newArr);
																		  angular.element('#pendingServiceAddFav').modal('show');
																	  }
																	  
																  }
																  else
																	  {
																	  var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
																		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
																		{
																			if(response.data.object != "undefined" && response.data.object != null)
																			{
																				$scope.finalPriceService =  response.data.object.finalPrice;
																				$scope.serviceRateGlobal =  response.data.object.serviceRate;
																				var idx = $scope.addedServiceOrdersForDisplay.findIndex(item => item.serviceId ==  value.serviceMasterId);
																				$scope.addedServiceOrdersForDisplay[idx].finalPrice = response.data.object.finalPrice;
																				$scope.addedServiceOrdersForDisplay[idx].serviceRate = response.data.object.serviceRate;
																				$scope.totalServiceAmount += parseFloat($scope.addedServiceOrdersForDisplay[idx].finalPrice);
																				value.finalPrice = response.data.object.finalPrice;
																			}
																			});
																		
																	  $scope.addedServiceOrdersForDisplay.push(newArr);
																	  $scope.search = '';
														            	$scope.selectedServiceTypeSpecialityId='';
														            	$scope.selectedSubServiceTypeSubSpecialityId='';
														            	$scope.selectedForFuture = 'N';
														            	$scope.investigationFutureDate='';
														        		$scope.fromDatepickerForFuture='';
																	    $scope.orderDetailsIdP=null;
																	    $scope.dublicateSerivceList.push(newArr);
																	    angular.element('#pendingServiceAddFav').modal('show');
																	  }
													    	    });
													  
										        }

										        $scope.addedServiceOrdersForDisplay = [];
												$scope.newAddedServiceOrdersForDisplay = [];
												
												$scope.getOutsourceServiceReport = function(obj){
													$('#outSourceServiceModal').modal('show');
													$scope.getPatientDocumentList(obj.labSampleDtlsId);
												}
												
												// investigationDetails
												$scope.getInvestigationDetails = function(invObj) {
													$rootScope.startLoader();
													var paramObj = {};
													$('#panelDetailsModal').modal('show');
													var headers = {
														'panelId' : invObj.panelId,
														'orderDetailsId' : invObj.orderDetailsId,
														'Content-Type' : 'application/json'
													};
													var URI = BASE_URL + ROOT_URL
															+ GETCURRENTINVESTIGATIONDETAILS;
													GenericServiceParamHeader
															.serviceAction(METHOD_GET, URI, data,
																	NOTIFICATION_MSG_STATUS_FALSE,
																	paramObj, headers)
															.then(
																	function(response) {
																		if (response.data.status == "success") {
																			$scope.inveDetails = response.data.listObject;
																			$rootScope.stopLoader();
																		} else if (response.data.status == "error") {
																			$rootScope.stopLoader();
																		}
																	});
												}
												
												
												$scope.getDataForCancelOrder=function(investigation){
													
													$scope.orderDetailsIdf=investigation.orderDetailsId;
													$scope.serviceSpecialityId=investigation.subDeptId;
												}
												
								
												$scope.currentEncounterOrderLists =[];
												
												$scope.getInvetigation = function() {
													$scope.currentEncounterOrderLists =[];
													$rootScope.startLoaderPopup();
													var data = {};
													var paramObj = {};
													var headers = {
															'patientId' : $scope.patientDetails.patientId,
														    'flag' : false,
														    'Content-Type' : 'application/json',
														    'unitId':$scope.unitId
													};
													var URI = BASE_URL + ROOT_URL
													+"/api/opd/getInvestigationsListForCoversheet";
													GenericServiceParamHeader
															.serviceAction(
																	METHOD_GET,
																	URI,
																	data,
																	NOTIFICATION_MSG_STATUS_FALSE,
																	paramObj, headers)
															.then(
																	function(response) {
																		if (response.data.status == "success") {
																			$scope.currentEncounterOrderLists = response.data.listObject;
																			angular.forEach($scope.currentEncounterOrderLists,function(value,index)
													    							{

													    						    var createdDate2 = value.createdDate;
													    							var createdDate1 = moment(value.createdDate,'DD-MM-YYYY HH:mm').format('DD-MMM-YYYY HH:mm');
													    							var orderSchDate1 = moment(value.orderSchDate,'DD-MM-YYYY').format('DD-MMM-YYYY');
													    							var sampleGenDatetime1 = moment(value.sampleGenDatetime,'DD-MM-YYYY HH:mm').format($scope.dateFormat.toUpperCase()+' HH:mm');
													    							if(createdDate1=='Invalid date')
													    								{
													    								createdDate1="";
													    								}else
													    									{
													    									createdDate1=moment(value.createdDate,'DD-MM-YYYY HH:mm').format($scope.dateFormat.toUpperCase()+' HH:mm');
													    									}
													    							if(orderSchDate1=='Invalid date')
												    								{
													    								orderSchDate1="";
												    								}else
												    									{
												    									orderSchDate1=moment(value.orderSchDate,'DD-MM-YYYY HH:mm').format($scope.dateFormat.toUpperCase()+' HH:mm');
												    									}
													    							if(sampleGenDatetime1=='Invalid date')
												    								{
													    								sampleGenDatetime1="";
												    								}else
												    									{
												    									sampleGenDatetime1=sampleGenDatetime1;
												    									}
													    							value["createdDate1"]=createdDate1;
													    							value["orderSchDate1"]=orderSchDate1;
													    							value["sampleGenDatetime1"]=sampleGenDatetime1;
													    							value["createdDate2"] = createdDate2;
													    							});
																			
																			if(	$scope.afterSaveCallNueron == true){
																				if($scope.outsourceLabId == 1){	
																					$scope.ordersIdListNeuron = [];
																					
																				angular.forEach($scope.currentEncounterOrderLists, function (value, key) {
																					if(value.orderId == $scope.currentsaveOrderId ){
																					 if(value.specialityId == $scope.labConfig.labSpecialityId)
																						{
																							var saveNuroPlus = {};
																							saveNuroPlus.branchName = "NphMain";
																							saveNuroPlus.orderId = value.orderId.toString();
																							saveNuroPlus.IPOPNo = $scope.patientDetails.admissionNumber;
																							saveNuroPlus.UHID =  $scope.patientDetails.uhIdNumber;
																							  var ff = moment(value.orderSchDate,"DD/MM/YYYY").toDate();
																							    var xx = moment(ff).format('YYYY-MM-DD');
																							    
																							    var orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
																								var orderSchDate = new Date().getTime();
																								saveNuroPlus.orderTs =value.orderDateString+"T"+value.orderDateTime;
																							// saveNuroPlus.ipoptype
																							// =
																							// "OPD";
																							if($scope.visitTypeId == 2){
																								var tyy= "IPD";
																								}else if($scope.visitTypeId == 3){
																								var tyy= "DC";
																								}else if($scope.visitTypeId == 4){
																								var tyy= "ER";
																								}
																								saveNuroPlus.ipoptype = tyy;
																							saveNuroPlus.salutation =  $scope.patientDetails.genderId.toString();;
																							saveNuroPlus.patientName =  $scope.patientDetails.firstNameNuron;
																							saveNuroPlus.gender = $scope.patientDetails.genderCode;
																						     var Birth = moment($scope.patientDetails.dobNeuron,"DD/MM/YYYY").toDate();
																						     var Bx = moment(Birth).format('YYYY-MM-DD');
																							saveNuroPlus.birthDate = Bx+"T00:00:00";
																							saveNuroPlus.ward =$scope.patientDetails.wardNameCover;;
																							saveNuroPlus.patMobileNo = "";
																							saveNuroPlus.patEmailId = $scope.postCommonObj.email;
																							saveNuroPlus.patAddress = $scope.patientDetails.address;
																							saveNuroPlus.patCity = $scope.patientDetails.cityName;
																							saveNuroPlus.refDrCode = $scope.patientDetails.doctorFullName;;
																							saveNuroPlus.refDrName = $scope.patientDetails.doctorFullName;;
																							saveNuroPlus.feesPaid = false;
																							saveNuroPlus.orderTests=[];
																							if($scope.ordersIdListNeuron.length==0){
																							$scope.ordersIdListNeuron.push(saveNuroPlus);
																						}else{
																							isGroupExistsCap=checkGroupIdExistsOrNotCap($scope.ordersIdListNeuron,saveNuroPlus)
																							  if (!isGroupExistsCap) {
																								$scope.ordersIdListNeuron.push(saveNuroPlus);
																							  }
																						}
																				     }
																					}
																				});
																				
																				angular.forEach($scope.ordersIdListNeuron, function (value, key) {
																				    angular.forEach($scope.currentEncounterOrderLists, function (value1, key1) {
																					    if(value.orderId == value1.orderId){
																					    	if(value1.specialityId == $scope.labConfig.labSpecialityId)
																							{
																					    		var newAer = {};
																					    		newAer.testRecordId = value1.serviceMasterId.toString();
																					    		newAer.testName = value1.serviceStandardName;
																					    		newAer.testCode = value1.serviceStandardCode;
																					    		value.orderTests.push(newAer);
																							}
																					    }
																					});
																				});
																				console.log("nuronlist",$scope.ordersIdListNeuron);
																				angular.forEach($scope.ordersIdListNeuron, function (value, key) {
																					var data ={};
																					data=value;
																					var url = 'https://ngr.dyndns.org/NeuronPlusHosp/Order';
																					var config ={
																							  "Access-Control-Allow-Origin":'*',
																							  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
																							  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
																							  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
																							  'accept' :'application/json, text/javascript, *',
																							  'content-type' :'application/json',
																							  'scheme' :'https',
																					};

																					$http.post(url, data, config).then(function (response) {
																						if(response.data.statusCode==200){
																							var masse=response.data.message;
																							var arr = masse.split(' ');
																							var URI = BASE_URL + ROOT_URL + "/api/global/saveNeuronPlusorderedServices";
																							var data ={
																									"orgId" : $scope.organizationId,
																									 "orgUnitId" :$scope.unitId,
																									"orderId":arr[3],
															    									"neuronPlusUrl":response.data.reportUrl,
																								}
																								GenericService.serviceAction("POST", URI, data).then(function (response) {
																									if (response.data.status == "success") 
																									{ 
																										$scope.afterSaveCallNueron == false;
																						            }
																								});
																						}

																					}, function (response) {

																					});

																				});
																				}else if($scope.outsourceLabId == 2){	
																					
																					$scope.ordersIdListMedall= [];
																					angular.forEach($scope.currentEncounterOrderLists, function (value, key) {
																						if(value.orderId == $scope.currentsaveOrderId ){
																					 if(value.specialityId == labConfig.labSpecialityId)
																						{
																						 if($scope.patientDetails.genderId.toString() == 1){
																							 $scope.patientDetails.genderId="0"
																						 }else if($scope.patientDetails.genderId.toString() == 2){
																							 $scope.patientDetails.genderId="1"
																						 }else{
																							 $scope.patientDetails.genderId="2"
																						 }
																						 
																						 var centerid = ($scope.medallUnitObj.medallCenterId != undefined && $scope.medallUnitObj.medallCenterId != null ? $scope.medallUnitObj.medallCenterId.toString() : "1478");
																							
																						 if($scope.visitTypeId == 2){
																								var tyy= "IPD";
																							}else if($scope.visitTypeId == 3){
																								var tyy= "DC";
																							}else if($scope.visitTypeId == 4){
																								var tyy= "ER";
																							}
																						 
																						 var Bx="";
																						 if($scope.patientDetails.birthDate != undefined && $scope.patientDetails.birthDate != null && $scope.patientDetails.birthDate !=""){
																							 var Birth = moment($scope.patientDetails.birthDate,"DD/MM/YYYY").toDate();
																							  Bx = moment(Birth).format('YYYY-MM-DD');
																						 }else{
																							 Bx = moment(new Date()).format('YYYY-MM-DD');
																						 }
																						 var medallObj =
																						 {
																							    "name": $scope.patientDetails.firstNameNuron,
																							    "gender": $scope.patientDetails.genderId,
																							    "mobile":  ($scope.mobileNumber!= undefined && $scope.mobileNumber != null && $scope.mobileNumber != "") ? $scope.mobileNumber.toString():"",
																							    "pincode":  ($scope.patientDetails.zipCode != undefined && $scope.patientDetails.zipCode != null && $scope.patientDetails.zipCode != "") ? $scope.patientDetails.zipCode :600020,
																							    "address": ($scope.patientDetails.address != undefined && $scope.patientDetails.address != null && $scope.patientDetails.address != "")  ? $scope.patientDetails.address:"",
																							    "state": ($scope.patientDetails.stateName != undefined && $scope.patientDetails.stateName != null && $scope.patientDetails.stateName != "") ? $scope.patientDetails.stateName:"",
																							    "district": ($scope.patientDetails.districtName != undefined && $scope.patientDetails.districtName != null && $scope.patientDetails.districtName != "") ? $scope.patientDetails.districtName : "",
																							    "passport": null,
																							    "srfid": null,
																							    "relationid": "1",
																							    "nationality": "Indian",
																							    "dob": Bx+" 00:00:00",
																							    "centerid": centerid,
																							    "appointmentid": null,
																							    "externalid":  $scope.patientDetails.uhIdNumber,
																							    "emailid": ($scope.patientDetails.email  != undefined && $scope.patientDetails.email != null && $scope.patientDetails.email != "") ? $scope.patientDetails.email:"",
																							    "orderid": value.orderId,
																							    "sourceid":"10019471",
																							    "hospitaltype":tyy,
																							    "studyinfo": []
																							}
																						 

																						 var prefixId = "2513";
																						 
																							angular.forEach($scope.medallPrifixList, function (val, inx) {
																								
																								if($scope.postCommonObj.prefixId == val.prefixId && val.medallSalutationIdPrefixId != null){
																									prefixId = val.medallSalutationIdPrefixId;
																								}
																								
																							});
																							
																							medallObj.salutationid = prefixId.toString();
																							
																							if($scope.postCommonObj.doctorId != undefined && $scope.postCommonObj.doctorId != null &&$scope.postCommonObj.doctorId != 0 ){
																								
																								var check = true;
																								angular.forEach($scope.medallDoctorList, function (val, inx) {
																									
																									if($scope.postCommonObj.doctorId == val.doctorId && val.medallReferalId != null){
																										medallObj.referalid = val.medallReferalId;
																										check = false;
																									}
																									
																								});
																								
																								if(check == true){
																									medallObj.referalid = '5001195';
																								}
																							}else{
																								medallObj.referalid = '5001195';
																							}
																						 
																						 
																						 	if($scope.ordersIdListMedall.length==0){
																							 $scope.ordersIdListMedall.push(medallObj);
																							}
																				     }
																				}
																				});
																				$scope.checkAutoRenderedServicesForMedall = [];
																				angular.forEach($scope.ordersIdListMedall, function (value, key) {
																					angular.forEach($scope.currentEncounterOrderLists, function (value1, key1) {
																						if(value1.orderId == $scope.currentsaveOrderId){
																						    	if(value1.specialityId == labConfig.labSpecialityId)
																								{
																						    		$scope.checkAutoRenderedServicesForMedall.push(value1);
																								}
																						    }
																						});
																			   
																			});
																				
																				angular.forEach($scope.ordersIdListMedall, function (value, key) {
																				    angular.forEach($scope.checkAutoRenderedServicesForMedall, function (value1, key1) {
																					    if(value.orderid == value1.orderId){
																					    	if(value1.specialityId == labConfig.labSpecialityId)
																							{
																					    		if(value1.medallServiceId != undefined && value1.medallServiceId != null && value1.medallServiceId != ""){
																						    			var newAer = {};
																							    		newAer.billingamount="0";
																							    		newAer.billingid="0";
																							    		newAer.billingtype="";
																							    		newAer.corpbillingid=(value1.medallServiceId).toString();
																							    		newAer.discount="0";
																							    		newAer.paidamount="0";
																							    		newAer.topaid="0";
																							    		newAer.extraamount="0";
																							    		value.studyinfo.push(newAer);
																						    		}
																								}
																					    }
																					});
																				});
																				if($scope.ordersIdListMedall != null && $scope.ordersIdListMedall.length >0 ){
																					if( $scope.ordersIdListMedall[0].studyinfo != undefined && $scope.ordersIdListMedall[0].studyinfo != null && $scope.ordersIdListMedall[0].studyinfo.length >0){
																							angular.forEach($scope.ordersIdListMedall, function (value, key) {
																							var data ={};
																							data=value;
																							var url = 'https://medinfra.medallcorp.in/MedallBRviewcommonAPI/api/register';
																							var config ={
																									  "Access-Control-Allow-Origin":'*',
																									  "Access-Control-Allow-Methods":"HEAD, GET, POST, PUT, PATCH, OPTIONS ,DELETE",
																									  "Access-Control-Allow-Headers":'Access-Control-*, Origin, X-Requested-With, Content-Type, Accept,Origin, X-Auth-Token',
																									  'Allow' :'GET, POST, PUT, DELETE, OPTIONS, HEAD',
																									  'accept' :'application/json, text/javascript, *',
																									  'content-type' :'application/json',
																									  'scheme' :'https',
																									  headers: {
																									      'Authorization': 'Basic ' + btoa('svh:medallsvh') // Replace
																									    }
																							};
							
																							$http.post(url, data, config).then(function (response) {
							
																								console.log(response);
																								if(response.data.status=="success"){
																									growl.success("success");
																									
																									var URI = BASE_URL + ROOT_URL + "/lab/saveMedallRequestResponse";
																									var data ={
																											admissionId:$scope.patientDetails.admissionId,
																											patientId:$scope.patientDetails.patientId,
																											requestedPayload:JSON.stringify($scope.ordersIdListMedall),
																											returnedResponse:JSON.stringify(response.data),
																											status:'A',
																											organizationId:$scope.organizationId,
																											unitId:$scope.unitId,
																											addedDate:moment(new Date(),'DD-MM-YYYY HH:mm:ss'),
																											addedBy:$scope.userId,
																											updatedDate:moment(new Date(),'DD-MM-YYYY HH:mm:ss'),
																											updatedBy:$scope.userId
																										}
																										GenericService.serviceAction("POST", URI, data).then(function (response) {
																											if (response.data.status == "success") 
																											{
																												$scope.afterSaveCallNueron = false;
																								            }
																										});
																								}else{
																									growl.warning(response.data.status);
																								}
																							}, function (response) {
							
																							});
							
																						});
																					}else{
																						growl.warning("Service is not mapped with Medall.");
																					}
																				}else{
																					growl.warning("Service is not mapped with Medall.");
																				}
																			}else{
																					$scope.afterSaveCallNueron = false;
																			}
																		}
																			console.log("current order list=" + $scope.currentEncounterOrderLists);
																			$rootScope.stopLoaderPopup();
																			document.getElementById("searchServiceIdDiagnostic").focus();
																			 
																		}
																	});
												}
												
										
												
												$scope.checkReport=function(labObj,idx,printFlag)
												{
													if(labObj.sampleStatusId==6 || labObj.sampleStatusId==17 || labObj.sampleStatusId==8)
													{
														return true;
												   }
													else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
														{
										    	         return true;
										    	         
														}else if(labObj.microTemplateStatusId!=null &&  (labObj.microTemplateStatusId == 39 || labObj.microTemplateStatusId == 40 || labObj.microTemplateStatusId == 41) ){
															  return true;
														}
														 else
														 {
														     return false;
														  }
											 }
												
												$scope.rePrintInvestigation = function(labObj, idx,printFlag) {
													if (labObj.sampleStatusId == 6 || labObj.sampleStatusId == 17 || labObj.sampleStatusId == 8) {
														if (labConfig.biochemistryId == labObj.subDeptId
																|| labConfig.clinicalPathologyId == labObj.subDeptId
																|| labConfig.hematologyId == labObj.subDeptId) {
															var data = {
																"appName" : "HIS",
																"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
																"reportDesignFilePath" : "pathologyReport.rpt",
																"reportFormat" : "PDF",
																"exportFileName" : "pathologyReport",
																"reportParams" : [ {
																	"paramName" : "labtesresid",
																	"paramType" : "Integer",
																	"paramValue" : labObj.labTestResId
																},{
														             "paramName": "isOriginal",
														             "paramType": "Integer",
														             "paramValue": printFlag
													           } ]
															};
															reportService.generate(REPORT_API_URI1, data);
														} else if (labConfig.microbiologyId == labObj.subDeptId) {
															var data = {
																"appName" : "HIS",
																"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
																"reportDesignFilePath" : "pathologyReport_Sensitivity.rpt",
																"reportFormat" : "PDF",
																"exportFileName" : "pathologyReport",
																"reportParams" : [ {
																	"paramName" : "lab_sample_dtls_id",
																	"paramType" : "Integer",
																	"paramValue" : labObj.labSampleDtlsId
																},{
														             "paramName": "isOriginal",
														             "paramType": "Integer",
														             "paramValue": printFlag
													           } ]
															};
															reportService.generate(REPORT_API_URI1,
																	data);
														} else if (labConfig.histopathologyId == labObj.subDeptId
																|| labConfig.cystopathologyId == labObj.subDeptId) {
															var data = {
																"appName" : "HIS",
																"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
																"reportDesignFilePath" : "GetHistopathologyReport.rpt",
																"reportFormat" : "PDF",
																"exportFileName" : "pathologyReport",
																"reportParams" : [ {
																	"paramName" : "labtesresid",
																	"paramType" : "Integer",
																	"paramValue" : labObj.labSampleDtlsId
																},{
														             "paramName": "isOriginal",
														             "paramType": "Integer",
														             "paramValue": printFlag
													           } ]
															};
															reportService.generate(REPORT_API_URI1,data);
														}else if(labConfig.serologyId==labObj.subDeptId)
														{
															var data =  
															{
														            "appName": "HIS",
														            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
														            "reportDesignFilePath": "SerologyReport.rpt",
														            "reportFormat": "PDF",
														            "exportFileName": "pathologyReport",
														            "reportParams": [
														              {
															             "paramName": "lab_sample_dtls_id",
															             "paramType": "Integer",
															             "paramValue": labObj.labSampleDtlsId
														              },
														              {
																             "paramName": "isOriginal",
																             "paramType": "Integer",
																             "paramValue": printFlag
															           }
														            ]
														      };
															reportService.generate(REPORT_API_URI1,data);
															}else{
																var data = {
																		"appName" : "HIS",
																		"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
																		"reportDesignFilePath" : "pathologyReport.rpt",
																		"reportFormat" : "PDF",
																		"exportFileName" : "pathologyReport",
																		"reportParams" : [ {
																			"paramName" : "labtesresid",
																			"paramType" : "Integer",
																			"paramValue" : labObj.labTestResId
																		},{
																             "paramName": "isOriginal",
																             "paramType": "Integer",
																             "paramValue": printFlag
															           } ]
																	};
																	reportService.generate(REPORT_API_URI1, data);
															}
													}else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
													{
										    			var data =  {
										    		            "appName": "HIS",
										    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
										    		            "reportDesignFilePath": "RadiologyReport.rpt",
										    		            "reportFormat": "PDF",
										    		            "exportFileName": "RadiologyReport",
										    		            "reportParams": [
										    		              {
										    			             "paramName": "template_transaction_id",
										    			             "paramType": "Integer",
										    			             "paramValue": labObj.templateTransactionId
										    		              },{
														        		"paramName": "isOriginal",
														        		"paramType": "Integer",
														        		"paramValue": 1
														        		},{
														        		"paramName": "isHeader",
														        		"paramType": "Integer",
														        		"paramValue": 0
														        		}
										    		            ]
										    		    };
										    	           reportService.generate(REPORT_API_URI1,data);
														}
													else if(labObj.microTemplateStatusId!=null && ( labObj.microTemplateStatusId == 39 ||  labObj.microTemplateStatusId == 40 ||  labObj.microTemplateStatusId == 41))
														{
											    			var data =  {
											    		            "appName": "HIS",
											    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
											    		            "reportDesignFilePath": "Pathologytemplatereport.rpt",
											    		            "reportFormat": "PDF",
											    		            "exportFileName": "Pathologytemplatereport",
											    		            "reportParams": [
											    		              {
											    			             "paramName": "lab_sample_dtls_id",
											    			             "paramType": "Integer",
											    			             "paramValue": labObj.labSampleDtlsId
											    		              },{
															        		"paramName": "isOriginal",
															        		"paramType": "Integer",
															        		"paramValue": 1
															        		},{
															        		"paramName": "isHeader",
															        		"paramType": "Integer",
															        		"paramValue": 0
															        		}
											    		            ]
											    		    };
											    	           reportService.generate(REPORT_API_URI1,data);
															}
													
													else {
														growl.warning("Final Report not released!!",
																{
																	title : 'Warning!'
																});

													}
												}
												
												
				                     $scope.cancelPendingServicePrePost = function(reasonId) {
													if(angular.isUndefined(reasonId))
														{
														growl
														.warning(
																'Please select reason...!!',
																{
																	title : 'Warning!'
																});
														}
													else
														{
														
													   $rootScope.startLoader();
													   var data = {};
													   data.orderDetailsId = $scope.orderDetailsIdf;
													   data.ordCancelReasonId = reasonId;
													   data.userId = $scope.userId;
													   data.admissionId = $scope.patientDetails.admissionId;
													   data.specialityId =$scope.serviceSpecialityId;
													   data.visitTypeId=$scope.visitTypeId;
													   data.preOrPostBilling=cookieObjectAppConfig.isPreOrPostBilling;
													   
										    		var URI = BASE_URL + ROOT_URL
													+ "/api/billing/bill/service/cancel";
										    		GenericService.serviceAction("POST", URI, data)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	growl.success( response.data.message,
																			{
																				title : SUCCESS_MSG
																			});
																	$scope.getInvetigation();
																	$rootScope.stopLoader();
																	
																}
																else
																	{
																	$rootScope.stopLoader();
																	}
															});
														}
												
											}
												// For Get ALL Patient Diagnosis List
												 $scope.comorbidityFlag = false;
											$scope.getPatientDiagnosis = function() {
												var data = {};
												var headerObjDia = {
													'unit_id' : $scope.unitId,
													'organization_id' : $scope.organizationId,
													'patient_id' : $scope.patientDetails.patientId,
													'Content-Type' : 'application/json',
												};
												var URI = BASE_URL + ROOT_URL
														+ GETPATIENTDIAGNOSISBYPATIENTID;
												GenericServiceParamHeader
														.serviceAction(METHOD_GET, URI, data,
																NOTIFICATION_MSG_STATUS_FALSE,
																data, headerObjDia)
														.then(
																function(response) {
																	console.log(response);
																	if (response.data.status == "success")
																		 $scope.patientDiagnosisList = [];
						                                            angular.forEach(response.data.listObject,function(value,index)
							 												{
						                                        	 			if($scope.patientDiagnosisList.length == 0){
									                                            	 $scope.patientDiagnosisList.push(value);
									                                             }else{
									                                            	 var found = $scope.patientDiagnosisList.find(ar=>ar.diagnosisId == value.diagnosisId && ar.diagnosisType == value.diagnosisType && ar.type == value.type && ar.remark == value.remark );
												  									   if(found == undefined){
												  										 $scope.patientDiagnosisList.push(value);
												  									   }
									                                             }
										 									});
																	
																	 angular.forEach($scope.patientDiagnosisList,function(value,index)
							                                        		   {

							                                        		  value.created_date1 = value.created_date;
							                                        		   value.created_date =moment(value.created_date,'DD/MM/YYYY').format('DD-MMM-YYYY');
							                                        		   });
																	console.log( "patientDiagnosisList", $scope.patientDiagnosisList);
																	 $scope.complaintDelete=true;
																	if($scope.patientDiagnosisList.length > 0)
																	{
																	  $scope.complaintDelete=false;
																	  $scope.comorbidityFlag = true;
																	}
																	
																     $scope.finalString="";
							                                         angular.forEach($scope.patientDiagnosisList,function(value,index){
							                                        	 if(index == 0){
							                                        		 if(value.type == 1){
							                                        			 var ext =  "(Prov)";
							                                        		 }else{
							                                        			 var ext =  "(F)";
							                                        		 }
							                                        		 if(value.diagnosisType == 1){
								                                        			var ext2 = "(P)" ;
								                                        		 }else{
								                                        			 var ext2 = "(S)" ;
								                                        		 }
							                                        		 
							                                        	   var String = value.diagnosisName +""+ext+"" +ext2;
							                                        	   $scope.finalString = $scope.finalString.concat(String);
							                                        	 }
							                                        	 else{
							                                        		 if(value.type == 1){
							                                        			 var ext =  "(Prov)";
							                                        		 }else{
							                                        			 var ext =  "(F)";
							                                        		 }
							                                        		 if(value.diagnosisType == 1){
								                                        			var ext2 = "(P)" ;
								                                        		 }else{
								                                        			 var ext2 = "(s)" ;
								                                        		 }
							                                        		   var String = value.diagnosisName +""+ext+"" +ext2;
								                                        	   $scope.finalString = $scope.finalString.concat(" , "+String);
							                                        	 }
							                                         });
							                                         
																});
											}
											$scope.getPatientDiagnosis();	
											
											$scope.diablebuttonMedication = false;
											$scope.saveMedication = function() 
											{
												$rootScope.startLoaderPopup();
												$scope.diablebuttonMedication = true;
												if($scope.updateFlagValidation)
													{
													growl.warning("Please update Medication.", {
														title : 'Warning!'
													});
													$scope.diablebuttonMedication = false;
													return false;
												  }
												$scope.saveBtnFlag = true;
												$scope.updateBtnFlag = false;
												
												$scope.updateScope = false;
												$scope.popUpFlag = true;
												
												console.log("$scope.medicationDetails",
														$scope.medicationDetails);

												$rootScope.startLoader();
												var medicationDetails = $scope.medicationDetails;

												var medicationDetailList = [];
												if($scope.medicationDetails.length > 0){
													
												
												if($scope.medicationDetails[0].priorityId != "" && $scope.medicationDetails[0].priorityId != null && $scope.medicationDetails[0].priorityId != undefined){
													$scope.pri=$scope.medicationDetails[0].priorityId;
												}
												angular.forEach( medicationDetails, function(value, key) {
													if(value.priorityId == 1){
														$scope.pri = 1;
													}
												});
												}

												var data = {
													"organizationId" : $scope.organizationId,
													"unitId" : $scope.unitId,
													"visitTypeId" : $scope.patientDetails.visitTypeId,
													"employeeId" : $scope.employeeId,
													"doctorId" : $scope.patientDetails.doctorId,
													"prescriptionDate" :  moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													"authorisedBy" : $scope.patientDetails.doctorId,
													"authorisedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													"addedBy" : $scope.userId,
													"addedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													"admissionId" : $scope.patientDetails.admissionId,
													"encounterId":$scope.patientDetails.encounterId,
													"patientId" : $scope.patientDetails.patientId,
													"priorityId" : $scope.pri,
													"isDischarge" : false,
													 "deptId" : $scope.patientDetails.specialityId,
													 "isPharmacologicalValidationSkipIp": true,
													  "isFromOP" :false,
													  "isOtims" :true,
													  "medicationType":$scope.medType
													 
												};

												
												
												$scope.validationmsmms = false;

												angular.forEach( medicationDetails, function(value, key) {
												 if($scope.roaConfiguration == 'Y'){
													 if(value.roaId == null || value.roaId == undefined || value.roaId == '' || isNaN(value.roaId)){
														 growl.warning(
																 'Please enter route of administration fields.',
																	{
																		title : 'Warning!'
																	});
														 $scope.validationmsmms = true;
														 $scope.diablebuttonMedication = false;
														 $rootScope.stopLoader();
														 $rootScope.stopLoaderPopup();
														return false;
													 }
												 }
												 
												 if(value.nonFormularyAdvice == "N"){
													 if($scope.dosageConfiguration == 'Y'){
														 if(value.prescriptionDoseUnit == null ||  value.prescriptionDoseUnit == undefined){
															 growl.warning(
																	 'Please enter all mandatory fields.',
																		{
																			title : 'Warning!'
																		});
															 $scope.validationmsmms = true;
															 $scope.diablebuttonMedication = false;
															 $rootScope.stopLoader();
															 $rootScope.stopLoaderPopup();
															return false;
														 }
														 if(value.formulationCounterIndex == null || value.formulationCounterIndex == undefined || value.formulationCounterIndex == ''|| isNaN(value.formulationCounterIndex)){
															 growl.warning(
																	 'Please enter all mandatory fields.',
																		{
																			title : 'Warning!'
																		});
															 $scope.validationmsmms = true;
															 $scope.diablebuttonMedication = false;
															 $rootScope.stopLoader();
															 $rootScope.stopLoaderPopup();
															return false;
														 }
													 }
												 }else{
													 if($scope.dosageConfiguration == 'Y'){
														 if(value.prescribeDose == null || value.prescribeDose == undefined || value.prescribeDose == '' || isNaN(value.prescribeDose)){
															 growl.warning(
																	 'Please enter all mandatory fields.',
																		{
																			title : 'Warning!'
																		});
															 $scope.validationmsmms = true;
															 $scope.diablebuttonMedication = false;
															 $rootScope.stopLoader();
															 $rootScope.stopLoaderPopup();
															return false;
														 }
														 
														 if(value.doseUnitId == null || value.doseUnitId == undefined || value.doseUnitId == '' || isNaN(value.doseUnitId)){
															 growl.warning(
																	 'Please enter all mandatory fields.',
																		{
																			title : 'Warning!'
																		});
															 $scope.validationmsmms = true;
															 $scope.diablebuttonMedication = false;
															 $rootScope.stopLoader();
															 $rootScope.stopLoaderPopup();
															return false;
														 }
													 }
												 }
												});
												 
												
												if($scope.validationmsmms == true){
													 growl.warning(
															 'Please enter all mandatory fields.',
																{
																	title : 'Warning!'
																});
													 $scope.validationmsmms = false;
													 $scope.diablebuttonMedication = false;
													 $rootScope.stopLoader();
													 $rootScope.stopLoaderPopup();
													return false;
												}
												

												var flag = true;
												angular.forEach( medicationDetails, function(value, key) {

																	var instructionId = value.instructionId;
																	if (value.medicationStartDate != ''
																			&& value.medicationEndDate != ''
																			&& value.durationCount != ''
																			&& value.durationUnit != ''
				// && value.instructionId != ''
																			&& value.quantity != '') {

																		var medicationDetailsObj = {};
																		medicationDetailsObj["itemId"] = value.itemId != 0 ? value.itemId : "";
																		medicationDetailsObj["storeId"] = $scope.storeId;
																		medicationDetailsObj["uomUnitId"] = value.uomTypeId;
																		medicationDetailsObj["isMedicationStop"] = 'N';
																		medicationDetailsObj["orderQty"] = value.quantity;
																		medicationDetailsObj["strength"] = value.strength != undefined ? value.strength:value.dose;
																		medicationDetailsObj["priorityId"] = value.priorityId;
																		medicationDetailsObj["prescriptionDate"] = moment(
																				new Date())
																				.format(
																						'YYYY-MM-DD HH:MM:SS');
																		medicationDetailsObj["authorisedBy"] = $scope.patientDetails.doctorId;
																		medicationDetailsObj["authorisedDate"] = moment(
																				new Date())
																				.format(
																						'YYYY-MM-DD HH:MM:SS');
																		medicationDetailsObj["dose"] = value.dose;
																		medicationDetailsObj["itemName"] = value.medicationName != undefined ? value.medicationName :value.itemName;
																		medicationDetailsObj["genericName"] = value.genericName;
																		medicationDetailsObj["doseUnitId"] = value.doseUnitId;
																		medicationDetailsObj["frequencyId"] = value.frequencyId;
																		medicationDetailsObj["routeId"] = value.roaId;
																		medicationDetailsObj["isNonFormulary"] = value.nonFormularyAdvice != undefined ? value.nonFormularyAdvice : value.isNonFormulary;
																		medicationDetailsObj["duration"] = value.durationCount;
								                                        medicationDetailsObj["durationUnit"] = value.durationUnit;
																		medicationDetailsObj["startDate"] = value.medicationStartDate+ " 00:00:00";
																		medicationDetailsObj["stopDate"] = value.medicationEndDate
																				+ " 00:00:00";
																		medicationDetailsObj["statflag"] = value.isStat!=null?value.isStat:false;
																		medicationDetailsObj.isDischarge = value.isDischarge;
																		if(value.prescribedDosageUnitList != undefined){
																		if(value.prescribedDosageUnitList.length>0 && value.formulationCounterIndex!="" && value.formulationCounterIndex!=null
																				&& value.formulationCounterIndex!=undefined){
																			 var list=$.grep(value.prescribedDosageUnitList, function (obj) {
																		        return value.formulationCounterIndex === obj.counterIndex;
																		    });
																			 if(list.length>0){
																				 if(list[0].formulationFactorId==1){
																					 medicationDetailsObj.prescribedDose=value.prescribeDose;
																					 medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
																					 medicationDetailsObj.factorId=list[0].formulationFactorId;
																					 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																				 }else{
																					 medicationDetailsObj.prescribedDose=value.prescribeDose;
																					 medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
																					 medicationDetailsObj.factorId=list[0].formulationFactorId;
																					 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																				 }
																			 }
																			
																		}else{
																			medicationDetailsObj.factorId=2;
																			medicationDetailsObj.prescribedDose=value.prescribeDose != undefined ? value.prescribeDose :null;
																		}
																		}
																		if(value.instructionId != '' && value.instructionId != undefined  && value.instructionId != null ){
																			medicationDetailsObj["instructionId"] = value.instructionId;
																			}else{
																			 medicationDetailsObj["instructionDesc"] = value.instructionDesc;
																			}
																		medicationDetailList.push(medicationDetailsObj);
																	} else {
																		flag = false;
																	}
																	
																});
												if (medicationDetailList.length > 0) {
												
													data.prescriptionDetailsDtosList = medicationDetailList;
													console.log("medicationDetailList", data);
						
														var URI = BASE_URL + ROOT_URL
														+ "/api/pharmacy/ip/pharmacology/validation/savePresWithouthPharmaValidation";
																					

													GenericServiceParamHeader
															.serviceAction(METHOD_POST, URI,
																	data,
																	NOTIFICATION_MSG_STATUS_TRUE)
															.then(
																	function(response) {
																		if (response.data.status == "success") {
																			$scope.updateFlag = false;
																			$scope.updateFlagValidation = false;
																			$scope.saveFlag = true;
																			$scope.medicationDetails = [];
																			$scope.searchItem="";
																			$scope.searchItemFre="";
																			$scope.medicationIndex = undefined;
																			$scope.medicationObj = {
																				itemId : "",
																				searchItem:"",
																				searchItemFre:"",
																				formulationTypeId : '',
																				favorite_status : false,
																				medicationStartDate : new Date(),
																				roaId : '',
																				dose : '',
																				doseUnitId : '',
																				durationCount : "",
																				durationUnit : 'Day',
																				instructionId : '',
																				medicationEndDate : '',
																				frequencyId : '',
																				quantity : '',
																				uomTypeId : '',
																				conversions : '',
																				strength : '',
																				priorityId : '2',
																				priorityCode : 'NR',
																				isGeneric:'N',
																				isInstruction:'N',
																				nonFormularyAdvice:'N',
																				searchItemFre : "",
																				isStat : false,
																				isDischarge : false

																			};
																			$rootScope.stopLoaderPopup();
																			$scope.diablebuttonMedication = false;
																			$scope.totalSumOfRate=0;
																			$scope.getDetailedMedication();
																			$scope.medicationObj.itemStrength="";
																			$scope.covTemplateId="";
																			$scope.covTemplateName="";
																			$scope.medicationObj.prescribedDosageUnitList=[];
																			$rootScope.stopLoader();
																		} else {
																			$rootScope.stopLoader();
																			$scope.diablebuttonMedication = false;
																			$rootScope.stopLoaderPopup();
																		}
																	});
													
												} else {
													growl
															.warning(
																	"Please add Medication and then save !!",
																	{
																		title : 'Warning!'
																	});
													$scope.medicationDetailList = [];
													$scope.diablebuttonMedication = false;
													$rootScope.stopLoader();
													$rootScope.stopLoaderPopup();
													return false;
												}
												
											}


				$scope.status = "Idle";
				$scope.isPaused = false;
				$scope.canPause = false;
				$scope.canStop = false;
				$scope.timerInterval;
				$scope.audioPreviewUrl = null;
				$scope.audioBlob = null;
				$scope.totalSeconds = 0;
				$scope.recordingTime = "00:00";


				$scope.getAiTranscriptionCreds = function(){
					var data ={};
					data.unitId = $scope.unitId;
					data.organizationId = $scope.organizationId;

					var URI = BASE_URL + ROOT_URL + "/api/otims/PreOperativeDetails/getAiTranscriptionCreds";
					GenericService.serviceAction(METHOD_POST, URI, data)
					.then(function(response) {
						if (response.data.status == "success") {
							if(response.data.object !== undefined && response.data.object != null){
								$scope.aiTranscriptionApiKey  = response.data.object.aiTranscriptionApiKey;
								$scope.aiTranscriptionUrl = response.data.object.aiTranscriptionUrl;
								$scope.userPrompt = response.data.object.userPrompt;
							}
						}
						$scope.stopLoader();
					});
				}

				$scope.getAiTranscriptionCreds();

				$scope.formatTime = function (sec) {
					var min = Math.floor(sec / 60).toString().padStart(2, '0');
					var secStr = (sec % 60).toString().padStart(2, '0');
					return `${min}:${secStr}`;
				}

				$scope.startTimer =function() {
					$scope.timerInterval = $interval(() => {
						$scope.totalSeconds++;
						$scope.recordingTime = $scope.formatTime($scope.totalSeconds);
					}, 1000);
				}

				$scope.pauseTimer = function() {
					if ($scope.timerInterval) {
						$interval.cancel($scope.timerInterval);
						$scope.timerInterval = null;
					}
				}

				$scope.resetTimer = function() {
					$scope.pauseTimer();
					$scope.totalSeconds = 0;
					$scope.recordingTime = "00:00";
				}

				$scope.startRecording = function() {
					navigator.mediaDevices.getUserMedia({ audio: true })
						.then(function(stream) {
							$scope.mediaRecorder = new MediaRecorder(stream);
							var audioChunks = [];

							$scope.mediaRecorder.ondataavailable = function(e) {
								audioChunks.push(e.data);
							};

							$scope.mediaRecorder.onstop = function() {
								$scope.pauseTimer();
								$scope.isRecording = false;
								$scope.isPaused = false;
								var blob = new Blob(audioChunks, { type: $scope.mediaRecorder.mimeType });

								$scope.audioPreviewUrl = $scope.audioPreviewUrl = $sce.trustAsResourceUrl(URL.createObjectURL(blob));
								$scope.audioBlob = blob;
								$scope.$apply();
							};

							$scope.mediaRecorder.start();
							$scope.status = "Recording...";
							$scope.isRecording = true;
							$scope.isPaused = false;
							$scope.resetTimer();
							$scope.startTimer();
							$scope.$apply();
						})
						.catch(function(err) {
							console.error(err);
							$scope.status = "Microphone access denied.";
						});
				}

				$scope.pauseRecording = function() {
					if (!$scope.mediaRecorder) return;

					if ($scope.mediaRecorder.state === 'recording') {
						$scope.mediaRecorder.pause();
						$scope.pauseTimer();
						$scope.isPaused = true;
						$scope.status = "Paused";
					}
				}

				$scope.stopRecording = function() {
					if ($scope.mediaRecorder && ($scope.mediaRecorder.state === 'recording' || $scope.mediaRecorder.state === 'paused')) {
						$scope.mediaRecorder.stop();
						$scope.resetTimer();
						$scope.status = "Processing...";
						$scope.canPause = false;
						$scope.canStop = false;
					}
				}

				$scope.resumeRecording = function() {
					if ($scope.mediaRecorder && $scope.mediaRecorder.state === 'paused') {
						$scope.mediaRecorder.resume();
						$scope.startTimer();
						$scope.status = "Recording...";
						$scope.isPaused = false;
					}
				};

				$scope.deleteRecording = function() {
					$scope.stopRecording();
					if ($scope.audioPreviewUrl) {
						URL.revokeObjectURL($scope.audioPreviewUrl);
					}

					$scope.audioPreviewUrl = null;
					$scope.audioBlob = null;
					$scope.status = "Recording deleted.";
					$scope.recordingTime = "00:00";
					$scope.totalSeconds = 0;
					$scope.isRecording = false;
					$scope.isPaused = false;
				};

				$scope.openRecordingPopup = function() {
					var popup = angular.element('#recordingPopup');
					popup.modal('show');
				}

				$scope.blobToBase64 = function (blob, callback) {
					var reader = new FileReader();
					reader.onloadend = function () {
						// Remove data URI prefix
						var base64data = reader.result.split(',')[1];
						callback(base64data);
					};
					reader.readAsDataURL(blob);
				};

				$scope.sendToVoice = function () {
					if($scope.aiTranscriptionUrl !== undefined && $scope.aiTranscriptionUrl !== null && $scope.aiTranscriptionApiKey!== undefined && $scope.aiTranscriptionApiKey !== null) {

						if (!$scope.audioBlob) {
							console.error("Audio not recorded");
							return;
						}

						$scope.blobToBase64($scope.audioBlob, function (base64Audio) {

							var payload = {
								audio_base64: base64Audio,
								html_template: $scope.ckObj.template,
								templateId: $scope.ckObj.noteMasterId,
								audio_format: "webm"
							};
							$rootScope.startLoader();
							$scope.startLoader();

							$http({
								method: 'POST',
								url: $scope.aiTranscriptionUrl,
								headers: {
									'Content-Type': 'application/json',
									'X-API-Key': $scope.aiTranscriptionApiKey
								},
								data: payload
							}).then(function success(response) {

								console.log("Voice API response:", response.data.id);

								if (response.data.id && response.data.transcript) {
									$timeout(function () {
										angular.forEach($scope.surgeryNotes, function (info) {
											if(info.noteMasterId === $scope.ckObj.noteMasterId){
												info.template =
													response.data.html_template_filled +
													'<hr/>' +
													'<p>' + response.data.transcript.replace(/\n/g, '<br/>') + '</p>';
											}
										});
										$scope.deleteRecording();
										var popup = angular.element('#recordingPopup');
										popup.modal('hide');
									});
									$rootScope.stopLoader();
									$scope.stopLoader();
								}
								$rootScope.stopLoader();
								$scope.stopLoader();

							}, function error(err) {
								$rootScope.stopLoader();
								$scope.stopLoader();
								console.error("Voice API error:", err);
								$scope.status = "Upload failed.";
							});

						});
					}
					else{
						growl.warning("Please configure Ai Voice Credentials!!",{
							title : WARNING_MSG
						});
					}
				};

									
				}]);

		