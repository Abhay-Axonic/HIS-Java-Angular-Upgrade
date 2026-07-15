'use strict';

/**
 * @Author By Ankit Landge
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'nephrologyWorkbenchController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'GenericServiceParamHeader',
						'$compile',
						'growl',
						'reportService','PagerService',
						'$window',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,growl,reportService,PagerService,$window) {

							//alert("radiologyWorkbenchController");
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.commonList = [];
								$scope.totalNoOfRecords;
								$scope.searchKeyword="";
								$scope.commonListCount;
								
							/* init() function for form object create. */
							$scope.init = function() {

								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};

								//Date Object For Search DOB
								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};

								//Date Object For Reshedule
								$scope.openDatePickerReschedule = function() {
									$scope.datepickerReschedule.opened = true;
								};

								$scope.datepickerReschedule = {
									opened : false
								};

								//Date For From Date on Search

								$scope.openDatePickerFromDate = function() {
									$scope.datepickerFromDate.opened = true;
								};

								$scope.datepickerFromDate = {
									opened : false
								};

								//Date For To Date on Search

								$scope.openDatePickerToDate = function() {
									$scope.datepickerToDate.opened = true;
								};

								$scope.datepickerToDate = {
									opened : false
								};

								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.roleId = 2;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								$scope.date = new Date();
							    $scope.today = moment($scope.date).format('DD-MM-YYYY');
								//start radiology
								$scope.visitTypeList = [   {
									id : 3,
									label : "Day Care"
								}, {
									id : 4,
									label : "ER"
								},
								{
									id : 2,
									label : "IP"
								},
								{
									id : 1,
									label : "OP"
								}
								];
								$scope.selectedVisitType = [];
								$scope.selectedHemoChairList=[];
								

								//end radiology

								
								var data = {organizationId : $scope.organizationId};
								var headerObj = {
									organization_id : $scope.organizationId,
									unit_id : $scope.unitId
								}
								var paramObj = {};


								//For Get Active Reason List
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

								$scope.encounterSearchObject = {
									'patientId' : 0,
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'serviceId':0
								}
								
							}

							$scope.init();

							$scope.loadingMsg="";
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
								var visitList =[],dialysisChairList=[];
								$scope.allEncounterList = [];
								$scope.loadingMsg="Loading...";
								$scope.encounterSearchObject.offset=offset;
								$scope.encounterSearchObject.noOfRecordsPerPage=noOfRecordsPerPage;
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								
								 angular.forEach($scope.selectedHemoChairList,
										function(value,index) {
									      dialysisChairList.push(value.id);
										});
								
								$rootScope.startLoader();
								$scope.encounterSearchObject.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								$scope.encounterSearchObject.visitList = visitList;
								$scope.encounterSearchObject.dialysisChairList = dialysisChairList;
								$scope.encounterSearchObject.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;

								if($scope.searchKeyword!=""){
									$scope.encounterSearchObject.patientName=$scope.searchKeyword;
								}else{
									$scope.encounterSearchObject.patientName=null;
								}
								var URI1 = BASE_URL + ROOT_URL+ GETSEARCHEDLISTHEMODIALYSIS;
								GenericService.serviceAction("POST", URI1, $scope.encounterSearchObject).then(function(response) {
											
											if (response.data.status == "success")
												{
												  $scope.allEncounterList = response.data.listObject;
												}
											
											if($scope.allEncounterList.length==0){
												$scope.loadingMsg="No records found...";
											}
											
											$scope.commonListCount = response.data.object;
											$scope.setPage(1,true);
											$rootScope.stopLoader();
										});
								
							   }
							
							
							//Search Appointment List By Filters
							$scope.searchEncounter = function(organizationId,offset, noOfRecordsPerPage, unitId) {
								console.log($scope.selectedHemoChairList)
								console.log($scope.selectedVisitType);
								 offset = offset != null ? offset : 0;
						         noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
						         
								var visitList =[];
								
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								
								/*angular.forEach($scope.selectedHemoChairList,
										function(value,index) {
									      modalityList.push(value.id);
										});*/
								
								$rootScope.startLoader();
								$scope.encounterSearchObject.offset=offset;
								$scope.encounterSearchObject.noOfRecordsPerPage=noOfRecordsPerPage;
								$scope.encounterSearchObject.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								
								$scope.encounterSearchObject.visitList = visitList;
								$scope.encounterSearchObject.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
								
								console.log("cookieObjectAppConfig.hemodialysisSpecialityId"+cookieObjectAppConfig.hemodialysisSpecialityId);

								var paramObj = {};
								var headers = {};

								//return false;
								var URI = BASE_URL + ROOT_URL
										+ GETSEARCHEDLISTHEMODIALYSIS;

								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												$scope.encounterSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE,
												"", "")
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.allEncounterList = [];
													if (response.data.status == "success")
														{
														  $scope.allEncounterList = response.data.listObject;
														}
													$rootScope.stopLoader();
														});

							}
							//$scope.searchEncounter(); 

							
							
							
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 && page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage,
										$scope.unitId);
								if (flag) {
									    $scope.searchEncounter($scope.organizationId,
										$scope.pager.startIndex,
										$scope.pager.pageSize,
										$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							$scope.searchKeyword="";
							$scope.serachPatient= function(searchKeyword){
								//orgId, unitId, offset, noOfRecordsPerPage
								if(searchKeyword!=null && searchKeyword!=""){
									$scope.searchKeyword=searchKeyword;
								}else{
									$scope.searchKeyword="";
								}
								if($scope.encounterSearchObject.patientId=="" || $scope.encounterSearchObject.patientId==0 || $scope.patientName==null || $scope.uhidNumber==null){
									$scope.encounterSearchObject.patientId=0;
								}
								
									$scope.initMasterList($scope.organizationId,
											$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							}
							
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,
										$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.clearSearchObject = function() {
								$scope.encounterSearchObject = {
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'patientId' : 0,
									'serviceId' : 0
								}

								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];
								$scope.selectedHemoChairList=[];
								$scope.patientName = '';
								$scope.uhidNumber = '';

								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
								$scope.initMasterList($scope.organizationId,
										$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							}

							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ PATIENTAUTOFILLSEARCH;

								return BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}

							$scope.selectPatientId = function(info, model,
									label) {

								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;

								$scope.encounterSearchObject.patientId = info.patientId;
							}

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}
								$scope.doctorId="";
							$scope.selectedDoc=function(docId){
								console.log(docId)
								$scope.doctorId=docId;
							}
							
							$scope.assetList=[];
							$scope.hemodialysisChear = function() {
								var dataObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL
								+ "/api/adt/getDialysisChair";		
								$scope.chairList=[];
								GenericService
								.serviceAction("POST", URI, dataObj).then(
								function(response) {
									if (response.data.status == "success")
										$scope.chairList = response.data.listObject;
									angular.forEach($scope.chairList,function(value,index){
										var data = {};
										data.id = value.bedId;
										data.label = value.bedNumber;
										$scope.assetList.push(data);
									})
								});
						     }
							$scope.hemodialysisChear();
							
							$scope.transferObj={};
							$scope.acceptRequestHemodialysis = function(item) {
								
								var currDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
								var dataForTransReq={
								organizationId : $scope.organizationId,
					    		unitId : $scope.unitId,
					    		admissionId : item.admissionId,
					    		fromWardId : item.fromWardId,
					    		fromRoomId :item.fromRoomId,
					    		fromBedId :item.fromBedId,
					    		fromBedCategoryId :item.fromBedCategoryId,
					    		fromBillingBedCategoryId :item.fromBillingBedCategoryId,
					    		toWardId :item.toWardId,
					    		toRoomId :item.toRoomId,
					    		toBedId:item.toBedId,
					    		orderDetailsId :item.orderDetailsId,
					    		updatedBy : $scope.userId,
					    		updatedDate : currDate,
					    		//doctorId : $scope.doctorId,
					    		isPACSRequired :cookieObjectAppConfig.isPACSRequired,
					    		specialityId : cookieObjectAppConfig.hemodialysisSpecialityId,
					    		hemodialysisSpecialityId : cookieObjectAppConfig.hemodialysisSpecialityId,
					    		visitTypeId : item.visitTypeId,
					    		doctorId:item.ordDoctorId,
					    		dialysisDoctorId:item.dialysisDoctorId,
					    		dialysisAppMapperId:item.dialysisAppMapperId,
					    		chairId:item.chairId,
					    		appointmentId:item.appointmentId
					    		//updated doctor from scheduled worklist
								}
								if(item.dialysisDoctorId=="" || item.dialysisDoctorId==null || parseInt(item.dialysisDoctorId)==0 || item.dialysisDoctorId==undefined){
									growl.warning("Please select Doctor...!",{
										title : "Warning..!"
									});
					        		return false;
								}
								if(item.appointmentId==null || item.slotId==null){
									growl.warning("Chair is not allocated...!",{
										title : "Warning..!"
									});
					        		return false;
								}
								
									$scope.transferObj=dataForTransReq;
									$('#acceptNephrologyReqModal').modal('show'); 
							}

							$scope.processRejectRequest=function()
							{
								$('#rejectNephrologyReqModal').modal('hide'); 
								$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/opd/rejectHemodialysisRequest";
							GenericService.serviceAction("POST",URI,$scope.rejectReqObj).then(function(response){
								if(response.data.status == "success"){
									 growl.success(
												'Dialysis request has been cancelled!!',
												{
													title : 'Success!'
												});
									$scope.init();
									//$scope.searchEncounter();
									$scope.initMasterList($scope.organizationId,
											$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
									$rootScope.stopLoader();
								 }
							 });
							}
							
							$scope.processAcceptRequest=function(){
								$('#acceptNephrologyReqModal').modal('hide'); 
								$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/opd/acceptHemodialysisRequest";
							GenericService.serviceAction("POST",URI,$scope.transferObj).then(function(response){
								//$scope.encounterList = response.data.listObject;
								//if(response.data.status == "success"){
									 growl.success(
											 response.data.message,
												{
													title : 'Success!'
												});
									$scope.init();
									//$scope.searchEncounter();
									$scope.initMasterList($scope.organizationId,
											$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
									$rootScope.stopLoader();
								 //}else{
									/* growl.warning(
												'Bed is already occupied !!',
												{
													title : 'Warning!'
												});
									 $rootScope.stopLoader();
									return false;*/
								// }
							 });
							}
							
							
							$scope.stillOnSamePage=function(){
								return false;
							}
							
							$scope.rejectReqObj={};
							$scope.rejectRequest = function(item) {
								var currDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
								var dataForRejectReq=
								{
									organizationId : $scope.organizationId,
						    		unitId : $scope.unitId,
						    		admissionId : item.admissionId,
						    		encounterId : item.encounterId,
						    		fromWardId : item.fromWardId,
						    		fromRoomId :item.fromRoomId,
						    		fromBedId :item.fromBedId,
						    		fromBedCategoryId :item.fromBedCategoryId,
						    		fromBillingBedCategoryId :item.fromBillingBedCategoryId,
						    		toWardId :item.toWardId,
						    		toRoomId :item.toRoomId,
						    		toBedId:item.toBedId,
						    		slotId:item.slotId,
						    		//fromRoomId :admissionObj.fromRoomId,
						    		//createdBy : $scope.userId,
						    		//createdDate : currDate,
						    		orderDetailsId :item.orderDetailsId,
						    		updatedBy : $scope.userId,
						    		updatedDate : currDate,
						    		doctorId : $scope.doctorId,
						    		isPACSRequired :cookieObjectAppConfig.isPACSRequired,
						    		specialityId : cookieObjectAppConfig.hemodialysisSpecialityId,
						    		visitTypeId : item.visitTypeId,
						    		dialysisDoctorId:$scope.doctorId,
						    		dialysisAppMapperId:item.dialysisAppMapperId,
						    		chairId:item.chairId,//updated doctor from scheduled worklist
						    		serviceId : item.serviceId
								}
								$scope.rejectReqObj=dataForRejectReq;
								
								var URI = BASE_URL + ROOT_URL 
								+ "/api/billing/getOrderDetailsPayeeData/"+item.orderDetailsId;
					    		  		
					    		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
								{	
					    			$scope.orderDetailsPayeeMapper = [];
					    			$scope.orderDetailsPackageMapper = [];
					    			if (response.data.status == "success") 
					    				{
					    						$scope.rejectReqObj.orderDetailsPayeeMapperDtosList = response.data.listObject;
					    						$scope.rejectReqObj.orderDetailsPackageMapperDtosList = response.data.object;
												
										}
								});
								
								
								$('#rejectNephrologyReqModal').modal('show');
							}
							
							$scope.search="";
							$scope.searchServices = function(searchKeyword){
								$scope.servicesList=[];
								
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
								
								//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.servicesList = response.data.listObject;
									}
									return $scope.servicesList;
								});
							}
							
							$scope.addServiceToTempServiceList = function($item, $model, $label){
								$scope.encounterSearchObject.serviceId = $item.serviceMasterId
								//$scope.search = '';
							}
							
							
						//Nephrology doctor list  
					        $scope.NephrologyDoctor = function(){
					        	var data={
					        			  organizationId : $scope.organizationId,
						        		  unitId : $scope.unitId,
						        		  specialityId :cookieObjectAppConfig.hemodialysisSpecialityId,
						        		}
								var URI = BASE_URL + ROOT_URL
								+ "/adt/getNephrologyDoctorsList";								
						BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.NephroDocList = [];
									if (response.data.status == "success")
										$scope.NephroDocList = response.data.listObject;
									console.log($scope.NephroDocList)
									
								});
							}
					        $scope.NephrologyDoctor(); 
					        
			//*********get hemodialysis chairs**********
					        
	    	$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
				};

	    	$scope.dateOptionsToDate = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate:new Date(),
				};
			
			$scope.dateOptionsSlot = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date()
				};
			
				$scope.openDatePicker = function() {
					$scope.datepicker.opened = true;
				};

				$scope.datepicker = {
					opened : false
				};
								
			$scope.datepicker = new Date();
			
			//For Date Difference For Modality Start
	       	$scope.daysDiffrence = function(date) {
	       	    var today = new Date();
	       	    var dd = today.getDate();
	       	    var mm = today.getMonth() + 1; //January is 0!
	       	    var yyyy = today.getFullYear();
	       	    if (dd < 10) {
	       	      dd = '0' + dd
	       	    }
	       	    if (mm < 10) {
	       	      mm = '0' + mm
	       	    }
	       	    today = yyyy + '/' + mm + '/' + dd;
	       	    $scope.today = today;
	       	    var date2 = new Date(today);
	       	    var date1 = new Date(date);
	       	    var timeDiff = Math.abs(date2.getTime() - date1.getTime());
	       	    $scope.dayDifference = Math.ceil(timeDiff / (1000 * 3600 * 24));
	       	    return $scope.dayDifference;
	       	  }
			
           $scope.tempAppointmentObj={};	
           $scope.chairId="";
           $scope.bedList = [];
           $scope.selectedPatientIndex="";
			$scope.getHemodialysisChairs=function(obj,index){
				$scope.selectedPatientIndex=index;
				$scope.currentSlotIndex="";
				$scope.selectedChair=="";
				$scope.slotId="";
				$scope.tempAppointmentObj=obj;
						var dataObj = {
								unitId : $scope.unitId,
								organizationId : $scope.organizationId
						};
						var URI = BASE_URL + ROOT_URL
						+ "/api/adt/getDialysisChair";		
						$scope.bedList=[];
						GenericService
						.serviceAction("POST", URI, dataObj)
				.then(
						function(response) {
							if (response.data.status == "success")
								$scope.bedList = response.data.listObject;
						});
						
						$scope.getSlotsOfDate($scope.chairId);
			         }	
			
			//Function For Get Date Wise Slots
			  $scope.itemId="";
			  $scope.itemObj={};
			  $scope.globleObj={};
			  $scope.loadingMsg="";
			  $scope.selectedChair="";
			  $scope.globleObj={};
			  $scope.selectedSlotIndex="";
			$scope.getSlotsOfDate = function(item) {
				$scope.slotDetailsList = [];
				$scope.loadingMsg="Loading....";
				$scope.currentSlotIndex="";
				if(item!=undefined){
					$scope.itemObj=JSON.parse(item);
					$scope.globleObj=JSON.parse(item);
				}else{
					$scope.itemObj=$scope.globleObj;
				}
				$scope.selectedChair=$scope.itemObj.bedId;
				$scope.fromTimeString = "";
				 
				if($scope.itemObj.bedId > 0 )
				{
				var dt = moment($scope.datepicker).format('YYYY-MM-DD');
				$scope.diffDaysMod = $scope.daysDiffrence(moment($scope.datepicker).format('YYYY/MM/DD'));
				
				var data={};
				data.slotTypeId = 8;
				data.bedId = $scope.itemObj.bedId;
				data.specialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
				data.organizationId = $scope.organizationId;
				data.unitId = $scope.unitId;
				data.appointmentDate=dt;
				
				//return false;
				var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									
									// $rootScope.startLoader();
									if (response.data.status == "success") {
										$scope.slotDetailsList = response.data.listObject;
										if($scope.slotDetailsList.length==0){
											$scope.loadingMsg="Slots are not created for selected date...!";
										}
										console.log($scope.slotDetailsList);
	                         	}

								});
			}
			else
				{
				growl.error("Please select chair",{
					title : ERROR_MSG
				});
				return false;
					
				}

			}
			
			$scope.currentSlotIndex="";
			$scope.slotStatusId ="";
			$scope.slotBooking = function(item,index)
			{
				$scope.currentSlotIndex=index+1;
				if(index!=undefined)
					$scope.selectedSlotIndex=index;
				$scope.fromTimeStringSlot = item.fromTimeString;
				$scope.fromTimeStringSlotShow = item.fromTimeString2;
				 $scope.slotId = item.slotId;
				 $scope.slotStatusId ="";
				 if($scope.slotDetailsList[index]==index){
					 
				 }
				 $scope.tempAppointmentObj.appointmentDate =  moment($scope.datepicker).format('YYYY-MM-DD');
				 if(item.slotPatientDetailsListsSize==1 && item.allowedOverBookingSlots>0){
					 $scope.slotStatusId = 6;
				 }
				 

				 /*angular.forEach($scope.slotDetailsList,function(value,idx){
					 if(idx==index){
						 $('#color_'+index).removeClass();
						 $('#color_'+index).addClass('bg-gray cursor-pointer day-slot-div ng-scope');
					 }else{
						 $('#color_'+idx).removeClass();
						 $('#color_'+idx).addClass('bg-green cursor-pointer day-slot-div ng-scope');
					 }
				 });*/
				
			}
			
	//****************Save Hemodialysis appointment
       $scope.createHemodialysisAppointment=function(){
			if($scope.selectedChair=="" || $scope.selectedChair==undefined){
				growl.error("Please select Chair",{
					title : ERROR_MSG
				});
				return false;
			}
			if($scope.slotId=="" || $scope.slotId==undefined){
				growl.error("Please select slot",{
					title : ERROR_MSG
				});
				return false;
			}
			
			$rootScope.startLoader();
			$scope.currentDate = new Date();
			$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
			var data = {
				slotId : $scope.tempAppointmentObj.slotId!=null ?$scope.tempAppointmentObj.slotId:$scope.slotId,
				remark : "",
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				createdDate : $scope.currentDate,
				updatedDate : $scope.currentDate,
				createdBy : $scope.userId,
				updatedBy : $scope.userId,
				status : 'A',
				slotStatusId : $scope.slotStatusId !=""?$scope.slotStatusId:4,
				allowedOverBookingSlots : 0,
				admissionId : $scope.tempAppointmentObj.admissionId,
				appointmentId :$scope.tempAppointmentObj.appointmentId,
				orderDetailsId:$scope.tempAppointmentObj.orderDetailsId,
				appointmentStatusId:1,
				patientId :$scope.tempAppointmentObj.patientId,
				appointmentTypeId : 8,
				encounterId : $scope.tempAppointmentObj.encounterId,
				bedId :$scope.selectedChair,
				appointmentDate :$scope.tempAppointmentObj.appointmentDate
			}
			angular.element('#hemodialysisChairPopup').modal('hide');
				
			var URI = BASE_URL + ROOT_URL+"/api/hemodialysis/scheduleDialysisAppointment";
				//+ "/api/opd/scheduleRadiologyAppointmentForInPatient";
	
			GenericService.serviceAction("POST", URI,data)
					.then(
							function(response) {
								angular.element('#hemodialysisChairPopup').modal('hide');
								if (response.data.status == "success") {
									growl.success("Appointment Scheduled Successfully",
											{
												title : 'Success!'
											});
								}
								else (response.data.status == "error") 
								{
										
								}
								$scope.init();
								//$scope.searchEncounter();
								$scope.initMasterList($scope.organizationId,
										$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							    $rootScope.stopLoader();
							});
                    }	
		       
			       $scope.checkDate=function(){
			    	   var currDate = new Date();
					     $scope.currentDate=moment(currDate).format('YYYY-MM-DD');
					     $scope.fromDate=moment(new Date($scope.datepickerFromDate)).format('YYYY-MM-DD');
					     $scope.toDate=moment(new Date($scope.datepickerToDate)).format('YYYY-MM-DD');
					     var fDate=moment($scope.fromDate).toDate();
					    
					     $scope.openDatePickerToDate = function() {
								$scope.datepickerToDate.opened = true;
							};

							$scope.datepickerToDate = {
								opened : false
							};
					    	$scope.dateOptionsToDate = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate:fDate,
								};
					    	 $scope.datepickerToDate = moment($scope.fromDate).toDate();  
			       }

					$scope.patientDocumnetInformation = function(info) {
						$scope.patientDocumentFlipBook ={};
						$scope.patientDocumentFlipBook =info;
						
						
						
							var data =
							{
							"encounterId":$scope.patientDocumentFlipBook.encounterId,
							"patientId":$scope.patientDocumentFlipBook.patientId,
							"unitId":$scope.unitId,
							"organisationId":$scope.organizationId,
							"userId":$scope.userId
							}
							
							
						var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
						BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								$scope.patientDocumentListTest = [];
								if (response.data.status == "success") 
								{
									$scope.patientDocumentListTest = response.data.listObject;
									if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
										$('#patientDocumentOption').modal('show');
									}
									$rootScope.stopLoader();
								}
								else if(response.data.status == "error")
									{
									$rootScope.stopLoader();
									}
							});
				
			          };
			          
			          $scope.convertDateToTimestamp = function(dateString) {
			                let parts = dateString.split("-");
			                let day = parseInt(parts[0], 10);
			                let month = parseInt(parts[1], 10) - 1; 
			                let year = parseInt(parts[2], 10);
			                let date = new Date(year, month, day);
			                let timestampInMilliseconds = date.getTime();
			                let timestampInSeconds = timestampInMilliseconds / 1000;
			                return timestampInSeconds;
			            };
			            
				     		$scope.generateFlipbookData = function() {
				                  let flipbookDatalist = [];

					     			var filteredlist=[];
					     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
					     		    	
					     		    	if(filterObj.documentPath != null){
					     				var flipbookDataobj={};
					     				 let createdDate = filterObj.createdDate;
					     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
					     				flipbookDataobj.timestamp=timestamp;
					     				flipbookDataobj.url=filterObj.documentPath;
					     				flipbookDataobj.tags=[];
					     				flipbookDataobj.tags.push("Document");
					     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
					     				flipbookDatalist.push(flipbookDataobj);
					     		    	}
					     				
					     			});

				                let data = {
				                    flipbookData: flipbookDatalist,
				                    logo: $scope.firstImgaa,
				                    privacy: "public"
				                };

				                let baseUrlLocation = $window.location.href;
				                let temp = baseUrlLocation.split('/');
				                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

				                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
				                
				        		try {
				        			var param={};
				        				$.ajax({
				        			        url : generateFlipbookurl,
				        					type : "POST",
				        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
				        					contentType : "application/json",
				        					dataType : "json",
				        					data : JSON.stringify(data),
				        			        //headers:getHeaders(),
				        					success : function(response){
				        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
				        							  window.open(''+response.flipbookUrl, '_blank');
				        						}else{
				        							$.notify("Error occurred","error");
				        						}
				        			    	}
				        			});
				        	     } catch (e) {
				        	 		console.log(e );
				        		}
				            };
			       
						} ]);