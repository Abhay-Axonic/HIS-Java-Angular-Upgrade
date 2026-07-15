/**
 * @author Vivek
 * @date 21-12-2017 Controller to Mapping Org level Service Master to Unit (Unit
 *       Level)
 */
angular
		.module("myApp")
		.controller(
				"UnitServiceMasterController",
				[
						'$scope',
						'$rootScope',
						'BillingGenericService',
						'GenericService',
						'$sessionStorage',
						'PagerService',
						'promiseFactory',
						'$cookies',
						'$state',
						'promiseFactoryWithObject',
						'growl',
						function($scope, $rootScope, BillingGenericService,
								GenericService, $sessionStorage, PagerService,
								promiseFactory, $cookies, $state,
								promiseFactoryWithObject, growl) {
							var cookieObject = $cookies
									.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.specialityMasterList = [];
							$scope.subSpecialityMasterList = [];
							$scope.unitService = {
								specialityId : "",
								subSpecialityId : "",
								searchServiceName : ""
							};
							$scope.userId = cookieObject.userId;
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							$scope.gradeWiseSeviceApplicable=cookieObjectAppConfig.gradeWiseSeviceApplicable;

                            var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
                            if (cookieObjectAppConfigSecond != undefined) {
	                            $scope.maxInputAmount = cookieObjectAppConfigSecond.maxInputAmount;
                            }
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.unitServiceMasterDetailsList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.pager = {};
							$scope.page;
							
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;

							$scope.initUnitServiceMaster = function() {
								$scope.unitServiceMaster = {
									"serviceMasterId" : "",
									"isOutsource" : "N",
									"isOutsourceJob" : "N",
									"isPackage" : "I",
									"basePrice": "0",
									"costOfService": "0",
									"isRateEditable" : "N",
									"rateEditbleMinValue" : "",
									"rateEditableMaxValue" : "",
									"isTaxApplicable" : "N",
									"isRefDoctorShare" : "N",
									"isDiscount" : "N",
									"isPanel" : "N",
									"serviceStandardCode" : "",
									"isProcedure" : "N",
									"isDocReq" : "N",
									"isBedSide" : "N",
									"isPeriodicity":"N",
									"isPoc" : "N",
									"gstTypeId" : "",
									"isAutoRender":"N",
									"isQuantityEditable":"N",
									"otcGstTypeId" : "",
									"organizationId" : $scope.organizationId,
									"unitId" : $scope.unitId,									
									"updatedBy" : $scope.userId,
									"updatedDate" : new Date().getTime(),
									"isAppointable":"N",
									"gender":"0",
									"procedureTypeId":"",
									"isPacsReq": "N",
									"isReviewApplicable":"N",
									"reviewTime":0,
									"billOfMaterial:":"N",
									"docRefPer":"",
									"isCyclicFlag":"N",
									"serviceBasePriceId":"",
									"sampleCollection" : "N",
									"surgeryGradeId" : "",
									"isDocSharePerWise" : "",
									"isRefSharePerWise" : "",
									"labAutoMailExclusion":"N",
									"preRequisite":"N",
									"preRequisiteDetails":"",
									"ventilatorRequiered":"N",
									"isAdminService":"N"
								}
								var data = {

								}
								var URI = BASE_URL + ROOT_URL
										+ GETALLACTIVETAX + "/" + $scope.organizationId;
								$scope.taxList=[];
								GenericService								
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log("Tax Details",response.data.listObject);														
														$scope.taxList=response.data.listObject;
													} else {
														alert("Error!!");
													}
												});
								//For Get Active Gender List
								
									var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
									BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.sexMasterList = response.data.listObject;
										console.log("Gender list="+$scope.sexMasterList);
									});
								
							}
							$scope.initUnitServiceMaster();
							
							
							$scope.servicesSelectedList = {
								    services: []
								  };
							$scope.servicesSelectedListTariff = {
								    services: []
								  };
								

							// ------------ Get Speciality Master List
							// ------------//
							// $scope.getSpecialityMasterList = function(){
							// try{
							// var URI = BASE_URL + ROOT_URL +
							// GETACTIVESPECIALITY;
							// var obj = {
							// organizationId : $scope.organizationId,
							// unitId :$scope.unitId
							// }
							// BillingGenericService.serviceAction(METHOD_POST,URI,
							// obj,
							// NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							// console.log(response);
							// if(response.data.status == "success")
							// $scope.specialityMasterList =
							// response.data.listObject;
							// });
							// }catch (e) {
							// console.log("Exception",e.message);
							// }
							// }
							// $scope.getSpecialityMasterList();

							// ------------ Get SubSpeciality By SpecialityId
							// ----------//
							// $scope.getSubSpecialityBySpecialityId =
							// function(specialityId){
							// $rootScope.startLoader();
							// $scope.unitService.subSpecialityId="";
							// $scope.servicesList=[];
							// var URI = BASE_URL + ROOT_URL +
							// GET_SUBSPECIALITY_BY_SPECIALITY_ID;
							// var obj = {
							// organizationId :$scope.organizationId,
							// unitId :$scope.unitId,
							// specialityId :specialityId
							// }
							// BillingGenericService.serviceAction(METHOD_POST,URI,
							// obj,
							// NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							// if(response.data.status == "success")
							// $scope.subSpecialityMasterList =
							// response.data.listObject;
							// });
							// }
							//	

							// ------------- Search Service Master
							// ---------------//
							// $scope.searchServiceMaster =
							// function(searchKeyword){
							// if($scope.unitService.specialityId == "" ||
							// $scope.unitService.specialityId == undefined){
							// growl.warning("Please select Department",{
							// title : WARNING_MSG
							// });
							// }else if($scope.unitService.subSpecialityId == ""
							// || $scope.unitService.subSpecialityId ==
							// undefined){
							// growl.warning("Please select Sub Department",{
							// title : WARNING_MSG
							// });
							// }
							// else{
							// var serviceMasterList = [];
							// var URI = BASE_URL + ROOT_URL + GLOBAL +
							// UNIT_SERVICE_MAPPER + SEARCH + SLASH +
							// searchKeyword + SPECIALITY + SLASH +
							// $scope.unitService.specialityId + SUB_SPECIALITY
							// + SLASH + $scope.unitService.subSpecialityId +
							// UNIT + SLASH + $scope.unitId + ORG + SLASH +
							// $scope.organizationId;
							// return
							// BillingGenericService.serviceAction(METHOD_GET,URI,"",NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							// if(response.data.status == "success"){
							// serviceMasterList = response.data.listObject;
							// }
							// return serviceMasterList;
							// });
							// }
							// }

							// ------------- Save Unit Service Master Details
							// ------------//
							$scope.saveUnitServiceMasterDetails = function() {
								try {
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + API
											+ GLOBAL
											+ UNIT_SERVICE_MASTER_DETAILS;
									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													$scope.unitServiceMaster,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope
																	.initUnitServiceMaster();
															$scope.unitService = {
																specialityId : "",
																subSpecialityId : "",
																searchServiceName : ""
															};
															$scope
																	.initUnitServiceMasterDetailsList(
																			$scope.organizationId,
																			$scope.unitId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
														}
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							// ------------- get Unit Service Master Details By
							// Id------------//
							$scope.getUnitServiceMasterDetailsById = function(
									unitServiceMasterDetailsId) {
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								try {
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + API
											+ GLOBAL
											+ UNIT_SERVICE_MASTER_DETAILS
											+ SLASH
											+ unitServiceMasterDetailsId;
									BillingGenericService
											.serviceAction(METHOD_GET, URI, "",
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success") {
															
															$scope.unitServiceMaster = angular
																	.copy(response.data.object);
															console.log($scope.unitServiceMaster);
															$scope.unitServiceMaster.surgeryGradeId=response.data.object.surgeryGradeId;
															$scope.unitServiceMaster.serviceMasterId=response.data.object.serviceMasterId;
															$scope.unitServiceMaster.serviceStandardCode=response.data.object.serviceStandardCode;
															$scope.unitServiceMaster.serviceStandardName=response.data.object.serviceStandardName;
															$scope.unitServiceMaster.specialityName=response.data.object.specialityName;
															$scope.unitServiceMaster.subSpecialityName=response.data.object.subSpecialityName;
															$scope.unitServiceMaster.subSpecialityId=response.data.object.subSpecialityId;
															$scope.unitServiceMaster.specialityId=response.data.object.specialityId;
															$scope.unitServiceMaster.isOutsource=response.data.object.isOutsource;
															$scope.unitServiceMaster.isOutsourceJob=response.data.object.isOutsourceJob;
															$scope.unitServiceMaster.isPackage=response.data.object.isPackage;
															$scope.unitServiceMaster.isRateEditable=response.data.object.isRateEditable;
															$scope.unitServiceMaster.rateEditbleMinValue=response.data.object.rateEditbleMinValue;
															$scope.unitServiceMaster.rateEditableMaxValue=response.data.object.rateEditableMaxValue;
															$scope.unitServiceMaster.isTaxApplicable=response.data.object.isTaxApplicable;
															$scope.unitServiceMaster.isRefDoctorShare=response.data.object.isRefDoctorShare;
															$scope.unitServiceMaster.refShareAmt=response.data.object.refShareAmt;
															$scope.unitServiceMaster.refSharePer=response.data.object.refSharePer;
															$scope.unitServiceMaster.isDiscount=response.data.object.isDiscount;
															$scope.unitServiceMaster.isPanel=response.data.object.isPanel;															
															$scope.unitServiceMaster.isProcedure=response.data.object.isProcedure;
															$scope.unitServiceMaster.isDocReq=response.data.object.isDocReq;
															$scope.unitServiceMaster.isBedSide=response.data.object.isBedSide;
															$scope.unitServiceMaster.isPeriodicity=response.data.object.isPeriodicity;
															$scope.unitServiceMaster.isAppointable = response.data.object.isAppointable;
															$scope.unitServiceMaster.isPoc=response.data.object.isPoc;
															$scope.unitServiceMaster.gender=response.data.object.gender.toString();
															$scope.unitServiceMaster.isReviewApplicable=response.data.object.isReviewApplicable ;
															$scope.unitServiceMaster.reviewTime=response.data.object.reviewTime;
															$scope.unitServiceMaster.refSharePer=response.data.object.refSharePer;
															
															if(response.data.object.gstTypeId!=null){
																$scope.unitServiceMaster.gstTypeId=response.data.object.gstTypeId.toString();
															}															
															if(response.data.object.otcGstTypeId!=null){
																$scope.unitServiceMaster.otcGstTypeId=response.data.object.otcGstTypeId.toString();
															}
															if(response.data.object.billOfMaterial!=null && response.data.object.billOfMaterial != undefined){
																$scope.unitServiceMaster.billOfMaterial=response.data.object.billOfMaterial;
															}else{
																$scope.unitServiceMaster.billOfMaterial="N";
															}
															$scope.unitServiceMaster.isAutoRender=response.data.object.isAutoRender;
															$scope.unitServiceMaster.isQuantityEditable=response.data.object.isQuantityEditable;
															//$scope.unitServiceMaster.discountValue=response.data.object.discountValue;
															$scope.unitServiceMaster.discountValue=0;
															$scope.unitServiceMaster.unitServiceMasterDetailsId=response.data.object.unitServiceMasterDetailsId;
															
															$scope.unitServiceMaster.procedureTypeId=response.data.object.procedureTypeId;
															$scope.unitServiceMaster.isPacsReq=response.data.object.isPacsReq;
															$scope.unitServiceMaster.docRefPer=response.data.object.docRefPer;
															$scope.unitServiceMaster.isCyclicFlag=response.data.object.isCyclicFlag;
															
															$scope.unitServiceMaster.basePrice=response.data.object.basePrice;
															$scope.unitServiceMaster.costOfService=response.data.object.costOfService;
															
															$scope.unitServiceMaster.serviceBasePriceId = response.data.object.serviceBasePriceId;
															
															$scope.unitServiceMaster.sampleCollection = response.data.object.sampleCollection;
															$scope.unitServiceMaster.ventilatorRequiered = response.data.object.ventilatorRequiered;
															$scope.unitServiceMaster.labAutoMailExclusion = response.data.object.labAutoMailExclusion;
															$scope.unitServiceMaster.preRequisite = response.data.object.preRequisite;
															$scope.unitServiceMaster.preRequisiteDetails = response.data.object.preRequisiteDetails;
															$scope.unitServiceMaster.isAdminService = response.data.object.isAdminService;
															if(response.data.object.isDocSharePerWise == 'Y')
																$scope.docShareRadio = 2;
															else if(response.data.object.isDocSharePerWise == 'N')
																$scope.docShareRadio = 1;
															
															if(response.data.object.isRefSharePerWise == 'Y')
																$scope.refShareRadio = 4;
															else if(response.data.object.isRefSharePerWise == 'N')
																$scope.refShareRadio = 3;
															
															$scope.detailView = $scope.unitServiceMaster;
														}
													});
								} catch (e) {
									console.log(e.message);
								}
							}
							
							
							$scope.changeTax = function()
							{
								if($scope.unitServiceMaster.isTaxApplicable=='N')
								{
									$scope.unitServiceMaster.oldGstTypeId=$scope.unitServiceMaster.gstTypeId;
									$scope.unitServiceMaster.oldOtcGstTypeId=$scope.unitServiceMaster.otcGstTypeId;
									$scope.unitServiceMaster.gstTypeId=null;
									$scope.unitServiceMaster.otcGstTypeId=null;
								}else{
									$scope.unitServiceMaster.gstTypeId=$scope.unitServiceMaster.oldGstTypeId;
									$scope.unitServiceMaster.otcGstTypeId=$scope.unitServiceMaster.oldOtcGstTypeId;
								}
							}
							
							$scope.checkPercentage = function(val,type)
							{
								if($scope.unitServiceMaster.rateEditableMaxValue < $scope.unitServiceMaster.rateEditbleMinValue)
									{
												$scope.unitServiceMaster.rateEditbleMinValue = 0;
												$scope.unitServiceMaster.rateEditableMaxValue = 0;
												
												growl.warning("Rate editable max value should be greater than rate editable min value.",{	
													title : WARNING_MSG
												});
									}
							}
							
							
						/*	$scope.amountCheckWithBasePrize = function()
							{
								   var checkAmount = 0;
									if($scope.unitServiceMaster.isDocReq=='Y' 
										&& $scope.unitServiceMaster.docRefPer!= null 
										&& $scope.unitServiceMaster.docRefPer != ""
											&& $scope.unitServiceMaster.docRefPer != 0
											&& $scope.unitServiceMaster.basePrice != null 
											&& $scope.unitServiceMaster.basePrice != "" 
											&& $scope.unitServiceMaster.basePrice != 0 && $scope.docShareRadio==1 )
									{
										if($scope.docShareRadio==1 && $scope.unitServiceMaster.docRefPer > $scope.unitServiceMaster.basePrice){
											      $scope.unitServiceMaster.docRefPer = $scope.unitServiceMaster.basePrice;
											      checkAmount = parseFloat($scope.unitServiceMaster.docRefPer)-parseFloat($scope.unitServiceMaster.basePrice);
											      console.log("first",checkAmount)
											}
										
										if($scope.docShareRadio==1 && $scope.unitServiceMaster.docRefPer < $scope.unitServiceMaster.basePrice){
										      checkAmount = parseFloat($scope.unitServiceMaster.docRefPer)-parseFloat($scope.unitServiceMaster.basePrice);
										      console.log("first",checkAmount)
										}
									}
									
									if($scope.unitServiceMaster.isRefDoctorShare=='Y' 
										&& $scope.unitServiceMaster.refSharePer!= null 
										&& $scope.unitServiceMaster.refSharePer != ""
											&& $scope.unitServiceMaster.refSharePer != 0
											&& $scope.unitServiceMaster.basePrice != null 
											&& $scope.unitServiceMaster.basePrice != "" 
											&& $scope.unitServiceMaster.basePrice != 0 && $scope.refShareRadio==1 )
									{
										if($scope.refShareRadio==4 && $scope.unitServiceMaster.refSharePer > 100)
											$scope.unitServiceMaster.refSharePer = 100;
									}
							}
							*/
							$scope.amountBlank = function()
							{
								if($scope.unitServiceMaster.isDocReq=='Y' && ($scope.unitServiceMaster.docRefPer==null || $scope.unitServiceMaster.docRefPer==""))
								{
									$scope.unitServiceMaster.docRefPer = 0;
									
								}
								if($scope.unitServiceMaster.isRefDoctorShare=='Y' && ($scope.unitServiceMaster.refSharePer==null || $scope.unitServiceMaster.refSharePer==""))
								{
									$scope.unitServiceMaster.refSharePer = 0;
									
								}
								if($scope.docShareRadio==undefined || $scope.docShareRadio==null || $scope.docShareRadio==0)
									$scope.docShareRadio = $scope.unitServiceMaster.isDocReq=='Y' ? 2 : 0;
								
								if($scope.refShareRadio==undefined || $scope.refShareRadio==null || $scope.refShareRadio==0)
									$scope.refShareRadio = $scope.unitServiceMaster.isRefDoctorShare=='Y' ? 4 : 0;
								
								if($scope.docShareRadio==2 && $scope.unitServiceMaster.docRefPer > 100)
									$scope.unitServiceMaster.docRefPer = 100;
								if($scope.refShareRadio==4 && $scope.unitServiceMaster.refSharePer > 100)
									$scope.unitServiceMaster.refSharePer = 100;
								
								
								if($scope.unitServiceMaster.isDocReq=='Y' 
									&& $scope.unitServiceMaster.docRefPer!= null 
									&& $scope.unitServiceMaster.docRefPer != ""
										&& $scope.unitServiceMaster.docRefPer != 0
										&& $scope.unitServiceMaster.basePrice != null 
										&& $scope.unitServiceMaster.basePrice != "" 
										&& $scope.unitServiceMaster.basePrice != 0
								//		&& $scope.docShareRadio==1 
								//		&& $scope.unitServiceMaster.isRefDoctorShare=='N'
											)
								{
									 var checkAmount = 0;
									 checkAmount = parseFloat($scope.unitServiceMaster.docRefPer) + parseFloat(0);
									 if(checkAmount > $scope.unitServiceMaster.basePrice){
										 growl.warning("Amount should not greater than base price.",{	
												title : WARNING_MSG
											});
											$scope.unitServiceMaster.docRefPer = 0;
									 }
								}
								
								if($scope.unitServiceMaster.isRefDoctorShare=='Y' 
									&& $scope.unitServiceMaster.refSharePer!= null 
									&& $scope.unitServiceMaster.refSharePer != ""
										&& $scope.unitServiceMaster.refSharePer != 0
										&& $scope.unitServiceMaster.basePrice != null 
										&& $scope.unitServiceMaster.basePrice != "" 
										&& $scope.unitServiceMaster.basePrice != 0
										//&& $scope.refShareRadio==3 
									//	&& $scope.unitServiceMaster.isDocReq=='N'
											)
								{
									 var checkAmount = 0;
									 checkAmount = parseFloat(0) + parseFloat($scope.unitServiceMaster.refSharePer);
									 if(checkAmount > $scope.unitServiceMaster.basePrice){
										 growl.warning("Amount should not greater than base price.",{	
												title : WARNING_MSG
											});
											$scope.unitServiceMaster.refSharePer = 0;
									 }
								}
								
								if($scope.unitServiceMaster.isDocReq=='Y' 
										&& $scope.docShareRadio==1 
										&& $scope.unitServiceMaster.isRefDoctorShare=='Y' 
										&& $scope.unitServiceMaster.basePrice != "" 
										&& $scope.refShareRadio==3
										)
								{
									 var checkAmount = 0;
									 checkAmount = parseFloat($scope.unitServiceMaster.docRefPer) + parseFloat($scope.unitServiceMaster.refSharePer);
									 if(checkAmount > $scope.unitServiceMaster.basePrice){
										 growl.warning("Sum of doctor share and referral share should not greater than base price.",{	
												title : WARNING_MSG
											});
											$scope.unitServiceMaster.refSharePer = 0;
											$scope.unitServiceMaster.docRefPer = 0;
									 }
								}
								
							}
							
					
							
							
							$scope.getClearDataService = function(radio)
							{
								if(radio==1 || radio==2)
								{
									$scope.docShareRadio = radio;
									$scope.unitServiceMaster.docRefPer=0;
								}
								
								if(radio==3 || radio==4)
								{
									$scope.refShareRadio = radio;
									$scope.unitServiceMaster.refSharePer=0;
								}
										
							}
							
							$scope.perBlank = function()
							{
								if($scope.unitServiceMaster.refShareAmt > 0)
								{
								  $scope.unitServiceMaster.refSharePer = 0;
								}
							}
							$scope.viewDetails = function(serviceDetails)
							{
								$scope.detailView = null;
								$scope.loadingMsg = "Loading..."
								$scope.getUnitServiceMasterDetailsById (serviceDetails.unitServiceMasterDetailsId);
								
							}
							
								  $scope.procedureList = function(){
										var data={
										           };
										var URI = BASE_URL + ROOT_URL
										+ "/api/global/getProcedureTypeList";								
										GenericService
										.serviceAction("GET", URI, data)
								.then(
										function(response) {
											console.log(response);
											$scope.procedurelist = [];
											if (response.data.status == "success")
												$scope.procedurelist = response.data.listObject;
											console.log($scope.procedurelist)
										});
									}
								$scope.procedureList();
							// ------------- Update Unit Service Master Details
							// ------------//
								
								
								$scope.changefinalPrice = function(fp,contractId)
								{
									
									if(fp == '' || fp == undefined || fp == null){
										fp = 0;
									}
									 angular.forEach($scope.contractList,function(value,idx){
	     									  if(value.contractId == contractId)
	     										 {
	     										  /*	var amt = (fp * value.coShare)/100;
	     										  	$scope.notSelectedServiceList[idx].selfPayable = parseFloat(amt).toFixed(2);
	     										  	$scope.notSelectedServiceList[idx].creditPayable = parseFloat(fp - amt).toFixed(2);*/
	     										  
	     										  		var gstAmt =  ((parseFloat(fp) * $scope.contractList[idx].taxPercentage)/100);
	 											
	     										  			//apportionedPrice = parseFloat(apportionedPrice) + parseFloat(isNaN(grpDtls.roundOffAmount) ? 0 : grpDtls.roundOffAmount);
	     										  			$scope.contractList[idx].finalRate =parseFloat(fp) + gstAmt;
	     										  			$scope.contractList[idx].apportionedPrice = $rootScope.parseFloatNum(fp);
	 											
	     										  			var amt = ($scope.contractList[idx].finalRate * value.coShare)/100;
	     										  			$scope.contractList[idx].selfPayable = parseFloat(amt).toFixed(2);
	     										  			$scope.contractList[idx].creditPayable =parseFloat($scope.contractList[idx].finalRate- amt).toFixed(2);
	     										 
	     										 
	     										 }
									});
									
								}
								
								$scope.changeSelfPayble = function(selfAmt,contractId)
								{
									
									
									 angular.forEach($scope.contractList,function(value,idx){
    									  if(value.contractId == contractId)
    										 {
    										 

    											if(selfAmt > $scope.contractList[idx].finalRate){
    												growl.warning("Amount should not greater than variance price.",{	
    													title : WARNING_MSG
    												});
    												$scope.contractList[idx].creditPayable = $scope.contractList[idx].finalRate;
    												$scope.contractList[idx].selfPayable = 0;
    												return false;
    											}else{
    												$scope.contractList[idx].creditPayable = $scope.contractList[idx].finalRate - selfAmt;
    											}
    											
    										 }
								});
									 
									
									
										
								}
								$scope.changeCreditPayble = function(creditAmt,contractId)
								{
									
									
									 angular.forEach($scope.contractList,function(value,idx){
   									  if(value.contractId == contractId)
   										 {
   										 

   										 
   										if(creditAmt > $scope.contractList[idx].finalRate){
   											growl.warning("Amount should not greater than variance price.",{	
   												title : WARNING_MSG
   											});
   											$scope.contractList[idx].selfPayable = $scope.contractList[idx].finalRate;
   											$scope.contractList[idx].creditPayable = 0;
   											return false;
   										}else{
   											$scope.contractList[idx].selfPayable = $scope.contractList[idx].finalRate - creditAmt;
   										}
   											
   										 }
								});
									 
									
									
									
								}
								
								
								$scope.getContractPopupOpen = function(type){
									$scope.contractList = [];
									$scope.tariffMuliSelectList = [];
									if(type == 'Y'){
										if($scope.unitServiceMaster.basePrice > 0 && $scope.unitServiceMaster.serviceMasterId > 0 && $scope.unitServiceMaster.subSpecialityId > 0){
										angular.element('#isApplicableForContract').modal('show');
										
										var data1 = {
												organizationId : $scope.organizationId,
												unitId : $scope.unitId
											}
											// for getting active tariff master
											var URI = BASE_URL + ROOT_URL
													+ GETACTIVETARIFFMASTERLIST;
											GenericService
													.serviceAction("POST", URI, data1)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	$scope.tariffList = response.data.listObject;
																	console.log("ActiveTariffMasterList",$scope.tariffList);
																	
																	angular.forEach(response.data.listObject,
																			function(value,index) {
																				var data = {};
																				data.id = value.tariffId;
																				data.label = value.tariffDesc;
																				data.amount = $scope.unitServiceMaster.basePrice;
																				$scope.tariffMuliSelectList.push(data);
																	});
																	
																}
															});	
											
											
										 var data = {
												    subDepartmentId:$scope.unitServiceMaster.subSpecialityId,
												    	unitId:$scope.unitId,
												    	taxId: $scope.unitServiceMaster.gstTypeId,
												    		rate : $scope.unitServiceMaster.basePrice,
												    		serviceId : $scope.unitServiceMaster.serviceMasterId
										 }
										var URI = "unit/CompanyContract/getContractDetailsBySubDept";
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														
														$scope.contractList =response.data.listObject;
													}
												});
									}else{
										growl.warning("Speciality, SubSpeciality and BasePrice are mandatory.",{
											 title : WARNING_MSG
											 });
										$scope.isApplicableForContract = 'N';
										return false;
									}
										
									}
									else{
										angular.element('#isApplicableForContract').modal('hide');
									}
								}
								
							$scope.pageNo='';
							$scope.updateUnitServiceMasterDetails = function() {
								try {
									if($scope.unitServiceMaster.isRefDoctorShare=='Y')
									{
									if($scope.unitServiceMaster.refSharePer==="" || $scope.unitServiceMaster.refSharePer=== undefined){
										growl.warning("Please Enter Ref.Doctor Share Percentage",{
											 title : WARNING_MSG
											 });
										return false;
										}
									}
									
									if($scope.unitServiceMaster.isRateEditable=='Y'){
										if($scope.unitServiceMaster.rateEditbleMinValue==="" || $scope.unitServiceMaster.rateEditbleMinValue=== undefined){
											growl.warning("Please Enter Rate Editable Min Value",{
												 title : WARNING_MSG
												 });
											return false;
										}
										
										if($scope.unitServiceMaster.rateEditableMaxValue==="" || $scope.unitServiceMaster.rateEditableMaxValue=== undefined){
											growl.warning("Please Enter Rate Editable Max Value",{
												 title : WARNING_MSG
												 });
											return false;
										}
									}
									
									if($scope.unitServiceMaster.isReviewApplicable==='Y'){
										if($scope.unitServiceMaster.reviewTime=="" || $scope.unitServiceMaster.reviewTime== undefined){
											growl.warning("Please Enter Review Time",{
												 title : WARNING_MSG
												 });
											return false;
											}
										}
									
									if($scope.unitServiceMaster.basePrice === "" || $scope.unitServiceMaster.basePrice == null || $scope.unitServiceMaster.basePrice == undefined ){//|| $scope.unitServiceMaster.basePrice == 0
											growl.warning("Please Enter Base Price",{
												 title : WARNING_MSG
												 });
											return false;
										}
									
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + API
											+ GLOBAL
											+ UNIT_SERVICE_MASTER_DETAILS;
									
									var tContractServiceDetailsDtoList  = [];
									 var len = $scope.servicesSelectedList.services.length;
									 for(var i = 0;i < len;i++)
										{
										 	angular.forEach($scope.contractList, function (value, key) {
										 		
										 		
														   if(value.contractId == $scope.servicesSelectedList.services[i])
														   {
														   var newArr = {};
														   newArr.orgId = $scope.organizationId;
														   newArr.unitId = $scope.unitId;
														   newArr.contractId = value.contractId;
														   newArr.servicePrice = value.rate;
														   newArr.finalPrice = value.finalRate;
														   newArr.serviceId =value.serviceId; 
														   newArr.creditPayable = value.creditPayable;
														   newArr.selfPayable = value.selfPayable;
												           newArr.createdBy = $scope.userId;
														   newArr.updatedBy = $scope.userId;
														   newArr.apportionedPrice = value.apportionedPrice;
														   newArr.isPackage = "N";
														   newArr.packageMasterId = null;
														   newArr.billingBedCategoryId = null;
														   newArr.patientCategoryId = null;
														   newArr.taxId = value.taxId;
														   newArr.taxPercentage = value.taxPercentage;
														   tContractServiceDetailsDtoList.push(newArr);
										 	}
										 				});
										}
									
									
									
									data={
											serviceMasterId:$scope.unitServiceMaster.serviceMasterId,
											unitServiceMasterDetailsId:$scope.unitServiceMaster.unitServiceMasterDetailsId,
											isOutsource:$scope.unitServiceMaster.isOutsource,
											isOutsourceJob:$scope.unitServiceMaster.isOutsourceJob,
											isPackage:$scope.unitServiceMaster.isPackage,
											isRateEditable:$scope.unitServiceMaster.isRateEditable,
											rateEditbleMinValue:$scope.unitServiceMaster.rateEditbleMinValue,
											rateEditableMaxValue:$scope.unitServiceMaster.rateEditableMaxValue,
											isTaxApplicable:$scope.unitServiceMaster.isTaxApplicable,
											isRefDoctorShare:$scope.unitServiceMaster.isRefDoctorShare,
											refShareAmt:$scope.unitServiceMaster.refShareAmt,
											refSharePer:$scope.unitServiceMaster.refSharePer,
											isDiscount:$scope.unitServiceMaster.isDiscount,
											//discountValue:$scope.unitServiceMaster.discountValue,
											discountValue:0,
											isPanel:$scope.unitServiceMaster.isPanel,															
											isProcedure:$scope.unitServiceMaster.isProcedure,
											isDocReq:$scope.unitServiceMaster.isDocReq,
											isBedSide:$scope.unitServiceMaster.isBedSide,
											isPoc:$scope.unitServiceMaster.isPoc,
											isPeriodicity:$scope.unitServiceMaster.isPeriodicity,
											gstTypeId:$scope.unitServiceMaster.gstTypeId,
											otcGstTypeId:$scope.unitServiceMaster.otcGstTypeId,
											isAutoRender:$scope.unitServiceMaster.isAutoRender,
											isQuantityEditable:$scope.unitServiceMaster.isQuantityEditable,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,									
											updatedBy : $scope.userId,
											status : "A",
											updatedDate : new Date().getTime(),
											isAppointable:$scope.unitServiceMaster.isAppointable,
											gender:$scope.unitServiceMaster.gender,
											procedureTypeId:(($scope.unitServiceMaster.procedureTypeId != "Select Procedure Order" && $scope.unitServiceMaster.procedureTypeId != null && $scope.unitServiceMaster.procedureTypeId > 0) ? $scope.unitServiceMaster.procedureTypeId  : null),
//											procedureTypeId:$scope.unitServiceMaster.procedureTypeId,
											isPacsReq:$scope.unitServiceMaster.isPacsReq,
											isReviewApplicable:$scope.unitServiceMaster.isReviewApplicable,
											surgeryGradeId : $scope.unitServiceMaster.surgeryGradeId,
											reviewTime:$scope.unitServiceMaster.reviewTime,
											billOfMaterial:$scope.unitServiceMaster.billOfMaterial,
											refSharePer:$scope.unitServiceMaster.refSharePer,
											docRefPer:$scope.unitServiceMaster.docRefPer,
											isCyclicFlag:$scope.unitServiceMaster.isCyclicFlag,
											basePrice : $scope.unitServiceMaster.basePrice,
											costOfService :$scope.unitServiceMaster.costOfService,
											serviceBasePriceId :$scope.unitServiceMaster.serviceBasePriceId,
											sampleCollection :$scope.unitServiceMaster.sampleCollection,
											ventilatorRequiered :$scope.unitServiceMaster.ventilatorRequiered,
											labAutoMailExclusion : $scope.unitServiceMaster.labAutoMailExclusion,
											isDocSharePerWise : $scope.docShareRadio == 2 ? 'Y' : 'N',
											isRefSharePerWise : $scope.refShareRadio == 4 ? 'Y' : 'N',
											preRequisite : $scope.unitServiceMaster.preRequisite,
											preRequisiteDetails : $scope.unitServiceMaster.preRequisiteDetails,
											isFormFApplicable : $scope.unitServiceMaster.isFormFApplicable,
											isAdminService : $scope.unitServiceMaster.isAdminService,
											tContractServiceDetailsDtoList : []
									};
									
									if(tContractServiceDetailsDtoList.length > 0){
										data.tContractServiceDetailsDtoList = tContractServiceDetailsDtoList;
									}
									
									console.log("update data",data);
									 //return false;
									BillingGenericService
											.serviceAction("PUT", URI,
													data,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.unitServiceMaster={}
															/*$scope.initUnitServiceMaster();*/
//															$scope.unitService = "";
															
															//$scope.offset=pageOffset;
															
															console.log("dd"+$scope.offset)
															$scope.initUnitServiceMasterDetailsList(
																			$scope.organizationId,
																			$scope.unitId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
															$scope.pageNo=($scope.offset/10)+1;
														    $scope.setPage($scope.pageNo,true)
														    console.log("pageNo"+$scope.pageNo)
														    $scope.unitServiceMaster.billOfMaterial = "N";
														    $scope.unitServiceMaster.sampleCollection = "N";
														    $scope.unitServiceMaster.ventilatorRequiered = "N";
														    $scope.unitServiceMaster.labAutoMailExclusion = "N";
														    $scope.unitServiceMaster.preRequisite = "N";
														    $scope.isApplicableForContract = 'N';
														    $scope.unitServiceMaster.isAdminService = 'N';
														/*	$scope.initUnitServiceMasterDetailsList(
																			$scope.organizationId,
																			$scope.unitId,
																			$scope.offset,
																			$scope.noOfRecordsPerPage);
															$scope.saveBtnFlag = true;*/
														}
														$scope.saveBtnFlag = true;
													});
								} catch (e) {
									console.log(e.message);
								}
							}

							// ======================= Unit Service Master
							// Details List Pagination
							// (Start)=====================//
							
							
							  $scope.checkAll = function()
							    {
							    	$scope.servicesSelectedList.services = [];
							    	if($scope.checkAllFlag == true)
							    		{
							    	angular.forEach($scope.contractList, function (value, key) {
							    		
							    		$scope.servicesSelectedList.services.push(value.contractId);
							    	});
							    		
							    	}
							    	else
							    		{
							    		$scope.servicesSelectedList.services = [];
							    		}
							    	
							    }
							  
							  $scope.checkAllTarrif = function()
							    {
							    	$scope.servicesSelectedListTariff.services = [];
							    	if($scope.checkAllFlagTariff == true)
							    		{
							    	angular.forEach($scope.tariffMuliSelectList, function (value, key) {
							    		
							    		$scope.servicesSelectedListTariff.services.push(value.id);
							    	});
							    		
							    	}
							    	else
							    		{
							    		$scope.servicesSelectedListTariff.services = [];
							    		}
							    	
							    }
							  
							  
							  
							  
							$scope.setNoOfRecords = function() {
								$scope.initUnitServiceMasterDetailsList(
										$scope.organizationId, $scope.unitId,
										$scope.offset,
										$scope.noOfRecordsPerPage);
							};

							$scope.initUnitServiceMasterDetailsList = function(
									orgId, unitId, offset, noOfRecordsPerPage) {
								$rootScope.startLoader();
								var obj = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI_TO_GET_UNIT_SERVICE_MASTER_DETAILS_LIST = BASE_URL
										+ ROOT_URL
										+ API
										+ GLOBAL
										+ UNIT_SERVICE_MASTER_DETAILS
										+ ORG
										+ SLASH
										+ orgId
										+ UNIT
										+ SLASH
										+ unitId
										+ SLASH
										+ offset
										+ SLASH
										+ noOfRecordsPerPage;
								var URI_TO_GET_UNIT_SERVICE_MASTER_DETAILS_COUNT = BASE_URL
										+ ROOT_URL
										+ API
										+ GLOBAL
										+ UNIT_SERVICE_MASTER_DETAILS
										+ COUNT
										+ ORG
										+ SLASH
										+ orgId
										+ UNIT
										+ SLASH
										+ unitId;
								promiseFactory
										.setPromises(
												URI_TO_GET_UNIT_SERVICE_MASTER_DETAILS_LIST,
												URI_TO_GET_UNIT_SERVICE_MASTER_DETAILS_COUNT,
												"GET", "GET")
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.unitServiceMasterDetailsList = response[0].listObject;
													console
															.log(
																	"unitServiceMasterDetailsList",
																	$scope.unitServiceMasterDetailsList);
													$scope.commonListCount = response[1].object;
													if($scope.updateBtnFlag==true){
														console.log($scope.pageNo)
														$scope.setPage($scope.pageNo,true);
													}
													else{
														console.log("else"+$scope.pageNo)
														$scope.setPage(1,false);
														$scope.saveBtnFlag = true;
														$scope.updateBtnFlag=false;
													}
												});
							}
							var pageOffset='';
							$scope.getUnitServiceMasterDetailsList = function(
									orgId, unitId, offset, noOfRecordsPerPage) {
								pageOffset=offset;
								$rootScope.startLoader();
								var obj = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL + API + GLOBAL
										+ UNIT_SERVICE_MASTER_DETAILS + ORG
										+ SLASH + orgId + UNIT + SLASH + unitId
										+ SLASH + offset + SLASH
										+ noOfRecordsPerPage;
								console.log("URI", URI);
								BillingGenericService
										.serviceAction(METHOD_GET, URI, obj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.unitServiceMasterDetailsList = response.data.listObject;
												});
							}
							
							$scope.loadingMsg="";
							$scope.serachunitServicemaster = function(searchString) {
								//orgId, unitId, offset, noOfRecordsPerPage
								$scope.unitServiceMasterDetailsList=[];
								$scope.loadingMsg="Loading...";
								if(searchString!=''){
									var orgId=$scope.organizationId;
									var unitId=$scope.unitId;
									
									var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											serviceStandardName : searchString
									}
									
								/*var URI = BASE_URL + ROOT_URL + API + GLOBAL
										+ UNIT_SERVICE_MASTER_DETAILS 
										+ SLASH + orgId  + SLASH + unitId
										+ SLASH +searchString;*/
									
									var URI = BASE_URL + ROOT_URL + API + GLOBAL + "/UnitServiceMasterDetailsSearch";
								console.log("URI", URI);
								BillingGenericService
										.serviceAction(METHOD_POST, URI,data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$rootScope.stopLoader();
													$scope.unitServiceMasterDetailsList = response.data.listObject;
													if($scope.unitServiceMasterDetailsList.length==0)
														$scope.loadingMsg="No record found...";
												});
								}else{
									$scope.initUnitServiceMasterDetailsList(
											$scope.organizationId, $scope.unitId,
											$scope.offset, $scope.noOfRecordsPerPage);
								}
								
								
							}

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.getUnitServiceMasterDetailsList(
											$scope.organizationId,
											$scope.unitId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}
							$scope.initUnitServiceMasterDetailsList(
									$scope.organizationId, $scope.unitId,
									$scope.offset, $scope.noOfRecordsPerPage);
							// =================== Service Master Pagination
							// (End)====================//
							
							
							
							$('#numberbox').keyup(function(){
								  if ($(this).val() > 100){
								    $(this).val('100');
								  }
								});
							
							//added by chetana for Grade Search
							$scope.getGradeList = function() {
//								var data = {
//										orgnisationId : $scope.organizationId,
////										unitId :$scope.unitId,
//										};
								
								var URI = BASE_URL + ROOT_URL + "/api/global/getActiveSurgeryGradeList";
							
								$scope.gradeLis=[];
								GenericService.serviceAction("GET", URI).then(function(response) {
									$scope.gradeList = response.data.listObject;
									console.log("gradeList",$scope.gradeList);
								});
							}
							$scope.getGradeList();

                $scope.checkMaxInoutAmount = function(index){
                    var amount = '';
                    if(index === 1){
                        amount = $scope.unitServiceMaster.basePrice;
                    }
                    else {
                        amount = $scope.unitServiceMaster.costOfService;
                    }

                    if(amount !== undefined && amount !== null && amount !== ''){
                        if($scope.maxInputAmount !== undefined && $scope.maxInputAmount !== null && $scope.maxInputAmount !== ''){
                            if(amount > $scope.maxInputAmount){

                                if(index === 1){
                                    $scope.unitServiceMaster.basePrice = '0';
                                }
                                else{
                                    $scope.unitServiceMaster.costOfService = '0';
                                }
                                growl.warning
                                ("You can enter max amount up to " + $scope.maxInputAmount + "...!",
                                    {title : "WARNING!"}
                                );
                            }
                        }
                    }
                }

						} ]);