/************EDITED ************************/
'use strict';

/**
 * @Author Sneha Dalave
 * @name myApp.controller:MISReportsController
 *
 */
angular
		.module('myApp')
		.controller(
				'MISReportsController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'$stateParams',
						'GenericServiceParamHeader',
						'growl',
						'$compile',
						'reportService',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,
								$stateParams,GenericServiceParamHeader,growl,$compile,reportService) {
							
							
							$rootScope.loginpage = true;
				
							var cookieObject = $cookies
							.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							//$scope.unitListUnitId = $scope.unitId;
							//$scope.orgListOrgId = $scope.organizationId;
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							if(cookieObjectAppConfig == undefined){
								$state.go('applicationConfiguration');
								return;
							}
							else
								{
									console.log('cookieObjectAppConfig',cookieObjectAppConfig);
									
								}
							$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
							$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
							$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
							$scope.allergyButtonFlag = true;
							$scope.paediatricsSpecialityId = cookieObjectAppConfig.paediatricsSpecialityId;
							var labConfig  =    $scope.checkLabConfig();
							$('#searchReportHea').focus();
							// function for initializa clockpicker
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							}
							$scope.patientDetails = 
							{
								uhIdNumber:"",
								patientName:"",
								genderCode:"",
								age:"",
								birthDate:"",
								encounterId:""
							};
							
							 $scope.dateOptionsfrom = {
	                             		formatYear : 'yyyy',
	     								showWeeks : false,
	     								maxDate:new Date(), //max 
	     							};
	                             
								 $scope.dateOptionsTo= {
	                             		formatYear : 'yyyy',
	     								showWeeks : false,
	     								maxDate:new Date(), //max 
	     							};
							/* $scope.dateOptionsfromDay = {
                             		formatYear : 'yyyy',
     								showWeeks : false,
     								maxDate:new Date(), //max 
     							};
                             
							 $scope.dateOptionsToDay= {
                             		formatYear : 'yyyy',
     								showWeeks : false,
     								maxDate:new Date(), //max 
     							};*/
                             
							 $scope.dateOptionsExpiry= {
	                             		formatYear : 'yyyy',
	     								showWeeks : false,
	     							};
							 
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
							
							/*$scope.openFromDayPicker = function() {
								$scope.fromDaypicker.opened = true;
							};

							$scope.fromDaypicker = {
									opened : false
							};
				
							$scope.openToDayPicker = function() {
								$scope.toDaypicker.opened = true;
							};

							$scope.toDaypicker = {
									opened : false
							};*/
							
							$scope.openToDatePickerExpiry = function() {
								$scope.expiryDatepicker.opened = true;
							};

							$scope.expiryDatepicker = {
									opened : false
							};
						/****************************Month & year wise***********************************************/
						/*	 $scope.dateOptionsMonth= {
									 'month-format': "'mmm'",
								        'starting-day': 1 
	     							};
							$scope.openMonthDatePicker= function(){
								$scope.monthDatepicker.opened = true;
							}
							
							$scope.monthDatepicker ={
									opened : false
							}
							
							 $scope.dateOptionsToMonth= {
                             		formatYear : 'mmm-yyyy',
     								showWeeks : true,
     								startView: 'months', 
     							    minViewMode: 'months',
     							    autoclose:true
     							};
							$scope.openToMonthDatePicker = function(){
								$scope.toMonthDatepicker.opened = true;
							}
							$scope.toMonthDatepicker ={
									opened : false
							}
							
							 $scope.dateOptionsYear= {
                             		formatYear : 'yyyy',
     								showWeeks : true,
     								startView: 'years', 
     							    minViewMode: 'years',
     							    autoclose:true
     							};
							$scope.openYearDatePicker = function() {
								$scope.expiryDatepicker.opened = true;
							};

							$scope.yearDatepicker = {
									opened : false
							};
							$scope.dateOptionsToYear= {
                             		formatYear : 'yyyy',
     								showWeeks : false,
     								startView: 'years', 
     							    minViewMode: 'years',
     							    autoclose:true
     							};
							$scope.openToYearDatePicker = function() {
								$scope.toYearDatepicker.opened = true;
							};

							$scope.toYearDatepicker = {
									opened : false
							};*/
					/****************************************************************************************************/
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
					  
							$scope.initMaster = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.common = {

										reportName : "",
										reportId : "",
										reportGroupHeaderId:""

								};
								console.log("Common-init", $scope.common);
							}
							$scope.initMaster();
							
							//patient search
							$scope.searchGroupList = function(keyword) {

								var data = {
									firstName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}

								var URI = BASE_URL + ROOT_URL
										+ GETPATIENTBYKEYWORD;
								return GenericService.serviceAction("POST",
										URI, data).then(function(response) {

									if (response.data.status == "success") {
										return response.data.listObject;

									}
								});

							}
						/**************************Date,Month,Year Wise Selection***********************************************/
							
							/*$scope.dateFlag=true;
							$scope.monthFlag=false;
							$scope.yearFlag=false;
							$scope.dateChangePicker="";
							$scope.ShowHideDiv =function(dateChangePicker){
								console.log("dateChangePicker",dateChangePicker)
								if(dateChangePicker=="Day")
									{
									$scope.dateFlag=true;
									$scope.monthFlag=false;
									$scope.yearFlag=false;
									}
								if(dateChangePicker=="Month")
								{
									$scope.dateFlag=false;
									$scope.monthFlag=true;
									$scope.yearFlag=false;
								}
								if(dateChangePicker=="Year")
								{
									$scope.dateFlag=false;
									$scope.monthFlag=false;
									$scope.yearFlag=true;
								}
							}*/
						/***********************************************************************************************/
							
							$scope.selectPatientIdOnSearch = function(info, model,
									label,ind) {
									if(info.patientId != null)
									$scope.patientId  = info.patientId;
									if(info.tPatientId !=null)
									$scope.tPatientId = info.tPatientId;
							}
							
							$scope.selectDignosisIdForUpdateFamily = function(info, model,
									label,ind) {
									$scope.diagnosisId  = info.diagnosisId;
							}
							
							$scope.selectVender = function(info, model,
									label,ind) {
									$scope.vendorName = info.vendorName;
									$scope.vendorId = info.id;
							}
							
							//Item search
							$scope.medicationObj ={
									searchItem : '',
							}
							$scope.searchMedicationItems = function(
									searchKeyword) {
							    if(searchKeyword.length >= 3){
									$scope.itemList = [];
									var unitId = $scope.unitId
									var orgId = $scope.organizationId
									var regex=/[^a-z]/gi;
									searchKeyword=searchKeyword.replace(regex," ");
									var URI = BASE_URL + ROOT_URL
											+ GETMEDICATIONDETAILS + "?unitId="
											+ unitId + "&orgId=" + orgId
											+ "&searchDrug=" + searchKeyword
									return BillingGenericService
											.serviceAction(METHOD_GET, URI, "",
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.itemList = response.data.listObject;
															console
																	.log(
																			"$scope.itemList ",
																			$scope.itemList);
														}
														return $scope.itemList;
													});
								}
							}
							
							$scope.addMedicationItemList = function($item,
									$model, $label, idx) {
								$scope.itemId = $item.drugId;
							}
						/*	
							$scope.searchMedicationItems = function(
									searchKeyword) {

								$scope.itemList = [];
								var unitId = $scope.unitIdgroupId
								var orgId = $scope.organizationId

								var URI = BASE_URL + ROOT_URL
										+ GETMEDICATIONDETAILS + "?unitId="
										+ unitId + "&orgId=" + orgId
										+ "&searchDrug=" + searchKeyword
								return BillingGenericService
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.itemList = response.data.listObject;
														console
																.log(
																		"$scope.itemList ",
																		$scope.itemList);
													}
													return $scope.itemList;
												});
							}*/
							
							
							//service search
							
							$scope.investigationObj ={
									serviceName : '',
							}
							
							$scope.obj = {
									searchPatientName:'',
							}
							
							$scope.diagnosisObj ={
									diagnosisName : '',
							}
							
							$scope.venderObj ={
									vendorName : '',
							}
							
							 $scope.searchInvestigations = function(searchServiceKeyword){
								$scope.investigationObj.serviceName=searchServiceKeyword;
								 var searchObj = { serviceDescription:searchServiceKeyword,
										           groupId:null, 
										           subGroupId:null,
										           orgId:$scope.organizationId, 
										           unitId:$scope.unitId};
									  var URI = BASE_URL + ROOT_URL + "/api/packages/services/orderSet/search";
									  
									  return BillingGenericService.serviceAction(METHOD_POST,URI, searchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											if (response.data.status == "success")
												return response.data.listObject;
										});
							  }
							
							$scope.searchGroupListForFamily = function(keyword) {

								var data = {};
								var paramObj = {
									diagnosisName : keyword
								};
								var headerObj = {
									unit_id : $scope.unitId,
									organization_id : $scope.organizationId
								}

								var URI = BASE_URL + ROOT_URL
										+ DIAGNOSISAUTOFILLSEARCH;

								return GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObj, headerObj)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}
							

							$scope.SearchVendorNames= function(search) {
								
						        if (search.length < 1)
						    	   return;

						        var URI = BASE_URL + ROOT_URL+"/api/global/vendor/search/" + search + "/"+$scope.unitId;
						        return  GenericService.serviceAction("GET", URI, {})
								.then(
										function(response) {
											// $rootScope.startLoader();
												return response.data;
										});
						    }
							
							   $scope.checkServiceMappedWithTest = function(serviceId,specialityId)
								{
									     $rootScope.startLoader();
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
												$rootScope.stopLoader();
												return response.data.object;
											}
											else if(response.data.status == "error"){
												  $rootScope.stopLoader();
												  return response.data.object;
											   }
										 });
								}
							 
							  $scope.selectInvestigation = function(item, model, label)
							  {
								  
								  console.log("selected item" ,item);
								  var promise = $scope.checkServiceMappedWithTest(item.serviceMasterId,item.specialityId);
						  		promise.then(function(obj) 
						  				{
						      	             if(obj>0)
						      	            	 {
						      	            	    $scope.serviceId = item.serviceMasterId;
						      	            	 }
								                   else{
								                	   $scope.serviceId = item.serviceMasterId;
								       	            	 growl.warning(
								       	 						'Service is Not mapped with Test!!',
								       	 						{
								       	 							title : 'Warning!'
								       	 						});
						       	                 }
						   				 });
							  }
							$scope.validateToDate = function() {
								
                           angular.forEach($scope.getparameterbyReportId1, function(value,key) {
                        	  if(value.controlName == "DatePickerFrom"){
											  $scope.fromDate  = moment(value.fromdate$index).format('YYYY-MM-DD');
											}
											if(value.controlName == "DatePickerTo"){
												$scope.toDate  = moment(value.todate$index).format('YYYY-MM-DD');
												if($scope.fromDate > $scope.toDate){
													value.todate$index=new Date();
													 growl.warning(" Please select to date greater than from date.",
																{
																	title : 'warning!'
																});	
												}
											}
											
											// for time validation
											
											if(value.controlName == "TimePickerFrom"){
												  $scope.fromTime  = value.Fromtime$index;
											}
											
											if(value.controlName == "TimePickerTo"){
												$scope.toTime  = value.Totime$index;
											}
											var CurrentDate = moment(new Date()).format('YYYY-MM-DD');
											if( ($scope.fromDate == CurrentDate && $scope.toDate == CurrentDate) ||  ($scope.fromDate == $scope.toDate)){
														if( $scope.fromTime > $scope.toTime){
															var time = moment().format("HH:mm");
															value.Fromtime$index = time;
															value.Totime$index = time;
															 growl.warning(" Please select to time  greater than from time.",
																		{
																			title : 'warning!'
																		});	
														}
											}
											
                           });
									
							}
							
						/*************************************Day & Month & Year Wise***********************************************************/
							/*$scope.validateToDay = function(){
								 angular.forEach($scope.getparameterbyReportId1, function(value,key) {
									 if(value.controlName == "DateFromDay"){
										 $scope.formatFlag= true;
										  $scope.fromDay  = moment(value.fromday$index).format('YYYY-MM-DD');
										}
										if(value.controlName == "DateToDay"){
											$scope.toDay  = moment(value.today$index).format('YYYY-MM-DD');
											if($scope.fromDay > $scope.toDay){
												value.todate$index=new Date();
												 growl.warning(" Please select to date greater than from date.",
															{
																title : 'warning!'
															});	
											}
										}
                    });
							}
							$scope.validateMonth = function() {
								
		                           angular.forEach($scope.getparameterbyReportId1, function(value,key) {
													if(value.controlName == "DatePickerMonth"){
													  $scope.month  = moment(value.month$index).format('YYYY-MM');
													}
													if(value.controlName == "DatePickerToMonth"){
														$scope.tomonth  = moment(value.todate$index).format('YYYY-MM');
														if($scope.month > $scope.tomonth){
															value.tomonth$index=new Date();
															
														}
													}
		                           });
											
									}
							
							$scope.validateYear = function() {
								
		                           angular.forEach($scope.getparameterbyReportId1, function(value,key) {
													if(value.controlName == "DatePickerYear"){
													  $scope.year  = moment(value.year$index).format('YYYY');
													}
													if(value.controlName == "DatePickerToYear"){
														$scope.tomonth  = moment(value.todate$index).format('YYYY');
														if($scope.year > $scope.toyear){
															value.toyear$index=new Date();
															 growl.warning(" Please select to date greater than from year.",
																		{
																			title : 'warning!'
																		});	
														}
													}
		                           });
											
									}*/
							
						/********************************************************************************************************************/
							
							$scope.Obj={
									id:"",
									format:"",
									extension:""
							};
			           $scope.getReportFormatesList = function() {
								var data = {};
								var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/getMISReportFormattesMasterforShowReports";

								GenericService.serviceAction("POST", URI, data).then(function(response) {
											
											$scope.getReportFormatesToShow = [];
											$scope.getReportFormatesToShow = response.data.listObject;
											$scope.Obj.id = response.data.listObject[0].id;
											console.log("formate data list",$scope.getReportFormatesToShow);
										});
							}
			           $scope.getReportFormatesList();
			           
                       $scope.getReportHeader = function() {

								
								var data = {
									       	organizationId : $scope.organizationId,
									       	userId:cookieObject.userId,
									       	unitId:cookieObject.unitId
										   }
								
								var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/getReportGroupHeaderMaster";

								GenericService.serviceAction("POST", URI, data).then(function(response) {

											$rootScope.stopLoader();
											$scope.getHeaderReportList = [];
											$scope.getHeaderReportList = response.data.listObject;
											//$scope.getReportListByReportGroupHeaderId($scope.getHeaderReportList[0])
			                                console.log("getHeaderReportList",$scope.getHeaderReportList);

										});
							}
                       $scope.getReportHeader();
                       
                       $scope.validateAge = function(age) {
                    	   if(age>120 || age<0 ){
                    		   growl.warning("Enter valid age",
										{
											title : 'Warning!'
										});
                    	   }
                       }
                       
							$scope.getReportListByReportGroupHeaderId = function(info) {
								
								$scope.investigationObj.serviceName='';
								$rootScope.startLoader();
								
								var data = {
									       organizationId : $scope.organizationId,
									       reportGroupHeaderId : info.reportGroupHeaderId
									       
										   }
								
								var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/getReportDetailsList";

								GenericService.serviceAction("POST", URI, data).then(function(response) {
											
											$scope.getReportList1 = [];
											
											$scope.getReportList = [];
											$scope.getReportList1 = response.data.listObject;
											
											angular.forEach(info.selectedReprtId, function(value,key) 
											{
												angular.forEach($scope.getReportList1, function(value1,key1) 
												{
													if(value.reportId==value1.reportId)
													{
														$scope.getReportList.push(value1);
													}
												});
											});

											$rootScope.stopLoader();
										});
							}
//							$scope.getReportList();
	
						
						  	/*$scope.formatFlag= false;*/
							$scope.getparameterbyReportId = function(info){
								$scope.investigationObj.serviceName='';
								$scope.reportNamesHOW = info.reportName;
								$rootScope.startLoader();
								$scope.selectMisReportObject = info;
								if(info.reportId != null){
									var data={
											organizationId : $scope.organizationId,
										       unitId : $scope.unitId,
										       reportId:info.reportId,
										       userId :$scope.userId 
										       
									}
								var URI = BASE_URL + ROOT_URL + "/api/global/unit/getReportDetailsByReportId";
								BillingGenericService.serviceAction("POST",URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.subSpecialityMasterList= [];
									if (response.data.status == "success")
										$scope.getparameterbyReportId1 = response.data.listObject;
									var time = moment().format("HH:mm");
									angular.forEach($scope.getparameterbyReportId1 ,function(value,key){
										if(value.controlName == "DatePickerFrom"){
											value.fromdate$index = new Date();
										}
										if(value.controlName == "DatePickerTo"){
											value.todate$index = new Date();
										}
										/*if(value.controlName == "DateFromDay"){
											value.fromday$index = new Date();
										}
										if(value.controlName == "DateToDay"){
											value.today$index = new Date();
										}
										if(value.controlName == "DatePickerMonth"){
											value.month$index = new Date();
										}
										if(value.controlName == "DatePickerToMonth"){
											value.tomonth$index = new Date();
										}
										if(value.controlName == "DatePickerYear"){
											value.year$index = new Date();
										}
										if(value.controlName == "DatePickerToYear"){
											value.toyear$index = new Date();
										}*/
										
										if(value.controlName == "DatePickerExpiry"){
											value.expirydate$index = new Date();
										}
									   if (value.controlName =="TimePickerFrom")
										{
										   value.Fromtime$index = time;
										}
										if (value.controlName =="TimePickerTo")
										{
											value.Totime$index = time;
										}
										if(value.controlName=='unit')
											value.paramValue = $scope.unitId;
										
										if(value.controlName=='user')
											value.paramValue = $scope.userId;
										
										if(value.controlName=='org')
											value.paramValue = $scope.organizationId;
										
										value.multiSelectList =[];
									});
									$scope.medicationObj ={
											searchItem : '',
									}
									$scope.reportName=$scope.getparameterbyReportId1[0].reportName;
									$scope.paramType=$scope.getparameterbyReportId1[0].paramType;
									$scope.parameterName=$scope.getparameterbyReportId1[0].parameterName;
									$scope.action=$scope.getparameterbyReportId1[0].action;
									console.log("label",$scope.getparameterbyReportId1)
									$rootScope.stopLoader();
									
								});
								}
							}
							 /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
							/*First Level*/
							$rootScope.firstTabLevel = 0 ;
							$rootScope.setFirstTabLevel= function(newTab){
								$rootScope.firstTabLevel = newTab;
							};
							$rootScope.isSetFirstTabLevel = function(tabNum){
								return $rootScope.firstTabLevel === tabNum;
							};
							
							/*Second Level*/
							$rootScope.secondTabLevel = 1 ;
							$rootScope.setSecondTabLevel = function(newTab){
								$rootScope.secondTabLevel = newTab;
							};
							$rootScope.isSetSecondTabLevel = function(tabNum){
								return $rootScope.secondTabLevel === tabNum;
							};
							
							/*Third Level*/
							$rootScope.thirdTabLevel = 1 ;
							$rootScope.setThirdTabLevel = function(newTab){
								$rootScope.thirdTabLevel = newTab;
							};
							$rootScope.isSetThirdTabLevel = function(tabNum){
								return $rootScope.thirdTabLevel === tabNum;
							};
							
							/*FORTH Level*/
							$rootScope.forthTabLevel = 1 ;
							$rootScope.setForthTabLevel = function(newTab){
								$rootScope.forthTabLevel = newTab;
							};
							$rootScope.isSetForthTabLevel = function(tabNum){
								return $rootScope.forthTabLevel === tabNum;
							};
							
							
							/*$scope.getSublist=function(info){
								 $scope.selectedId=info;
								 console.log("$scope.selectedId",$scope.selectedId);
							}
							*/
							$scope.getPreview = function()
							{
								console.log($scope.getparameterbyReportId1);
								$scope.finalArr = [];
								var actionNameRPT = "";
								angular.forEach($scope.getparameterbyReportId1, function(value,key) {
									
									var newArr = {};
									actionNameRPT  = value.action;
									newArr.paramName = value.parameterName;
									newArr.paramType = value.paramType;
										if(value.controlName =="DatePickerFrom")
											{
											newArr.paramValue  = moment(value.fromdate$index).format('YYYY-MM-DD');
											}
										else if (value.controlName =="DatePickerTo")
											{
											 newArr.paramValue  = moment(value.todate$index).format('YYYY-MM-DD');
											}
									/*	else if (value.controlName =="DateFromDay")
										{
										 newArr.paramValue  = moment(value.fromday$index).format('YYYY-MM-DD');
										}
										else if (value.controlName =="DateToDay")
										{
										 newArr.paramValue  = moment(value.today$index).format('YYYY-MM-DD');
										}
										else if(value.controlName =="DatePickerMonth"){
											newArr.paramValue  = moment(value.month$index).format('YYYY-MM');
										}
										else if(value.controlName =="DatePickerToMonth"){
											newArr.paramValue  = moment(value.tomonth$index).format('YYYY-MM');
										}
										else if(value.controlName =="DatePickerYear"){
											newArr.paramValue  = moment(value.year$index).format('YYYY');
										}
										else if(value.controlName =="DatePickerToYear"){
											newArr.paramValue  = moment(value.toyear$index).format('YYYY');
										}*/
										else if (value.controlName =="DatePickerExpiry")
										{
										 newArr.paramValue  = moment(value.expirydate$index).format('YYYY-MM-DD');
										}
										else if (value.controlName =="TimePickerFrom")
										{
											
											var str = value.Fromtime$index;
											var fstr = "'"+str+"'";
											var ffdtr = fstr.toString();
											
										   newArr.paramValue  = ffdtr;
										}
										else if (value.controlName =="TimePickerTo")
										{
											
											var strto = value.Totime$index;
											var fstrto = "'"+strto+"'";
											var ffdtrto = fstrto.toString();
											
										   newArr.paramValue  = ffdtrto;
										   
										}
										else if (value.controlName =="text")
										{
											if($scope.obj.searchPatientName == '' || $scope.obj.searchPatientName == undefined){
													 $scope.patientId = 0	;
											}
											 newArr.paramValue  = $scope.patientId != null ? $scope.patientId :0;
										}
										else if (value.controlName =="ItemSearch")
										{
											if($scope.medicationObj.searchItem == ''){
												 newArr.paramValue  = 0 ;
											}else{
												newArr.paramValue  = $scope.itemId;
											}
										}
										else if (value.controlName =="Servicesearch")
										{
											if($scope.investigationObj.serviceName == ''){
												 newArr.paramValue  = 0 ;
											}else{
												newArr.paramValue  = $scope.serviceId;
											}
										}
										else if (value.controlName =="multiSelectlist")
										{
											$scope.paramValueData=[];
											angular.forEach(value.multiSelectList ,function(value,key){
												
												$scope.paramValueData.push(value.id);
												
											});
											newArr.paramValue=$scope.paramValueData.toString();
										}
										else if(value.controlName =="org")
										{
											newArr.paramValue = value.paramValue;

											newArr.paramValue = (value.paramValue==null||value.paramValue==undefined)?0:value.paramValue;

										}
										else if(value.controlName =="unit")
										{
											newArr.paramValue = value.paramValue;

											newArr.paramValue = (value.paramValue==null||value.paramValue==undefined)?0:value.paramValue;

										}
										else if (value.controlName =="DiagnosiSearch")
										{
											if($scope.diagnosisObj.diagnosisName == '' || $scope.diagnosisObj.diagnosisName == undefined){
													$scope.diagnosisId  = 0	;
											}
											 newArr.paramValue  = $scope.diagnosisId   != null ? $scope.diagnosisId :0;
										}
										else if (value.controlName =="VenderSearch")
										{
											if($scope.venderObj.vendorName == '' || $scope.venderObj.vendorName == undefined){
													$scope.vendorId  = 0	;
											}
											 newArr.paramValue  = $scope.vendorId   != null ? $scope.vendorId :0;
										}
										else
											{
											 newArr.paramValue = value.paramValue;

												newArr.paramValue = (value.paramValue==null||value.paramValue==undefined)?0:value.paramValue;

											}
										$scope.finalArr.push(newArr);
							         });
								
								console.log($scope.finalArr);
								
								
								var data =  { 
										 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath":$scope.selectMisReportObject.action,
						                 "reportFormat": "PDF",
						                 "exportFileName": "admission",
						                 "reportParams": $scope.finalArr
							 };
								
							   reportService.generate(REPORT_API_URI_FOR_MIS,data);
							}
					
							$scope.Obj={
									id:"",
									format:"",
									extension:""
							};
							
							
							$scope.getExcel = function()
							{
								
								$rootScope.startLoader();
								
								angular.forEach($scope.getReportFormatesToShow,function(value,key){
									if($scope.Obj.id == value.id){
										$scope.Obj.format=value.format;
										$scope.Obj.extension=value.fileExtension;
									}
								})
								var URI =REPORT_DOWNLOAD_URI+"/"+$scope.organizationId+"/"+$scope.unitId;
								console.log($scope.getparameterbyReportId1);
								$scope.finalArr = [];
								var actionNameRPT = "";
								angular.forEach($scope.getparameterbyReportId1, function(value,key) {
									
									var newArr = {};
									actionNameRPT  = value.action;
									newArr.paramName = value.parameterName;
									newArr.paramType = value.paramType;
										if(value.controlName =="DatePickerFrom")
											{
											newArr.paramValue  = moment(value.fromdate$index).format('YYYY-MM-DD');
											}
										else if (value.controlName =="DatePickerTo")
											{
											 newArr.paramValue  = moment(value.todate$index).format('YYYY-MM-DD');
											}
									/*	else if (value.controlName =="DateFromDay")
										{
										 newArr.paramValue  = moment(value.fromday$index).format('YYYY-MM-DD');
										}
										else if (value.controlName =="DateToDay")
										{
										 newArr.paramValue  = moment(value.today$index).format('YYYY-MM-DD');
										}
										else if(value.controlName =="DatePickerMonth"){
											newArr.paramValue  = moment(value.month$index).format('YYYY-MM');
										}
										else if(value.controlName =="DatePickerToMonth"){
											newArr.paramValue  = moment(value.tomonth$index).format('YYYY-MM');
										}
										else if(value.controlName =="DatePickerYear"){
											newArr.paramValue  = moment(value.year$index).format('yyyy');
										}
										else if(value.controlName =="DatePickerToYear"){
											newArr.paramValue  = moment(value.toyear$index).format('yyyy');
										}*/
										else if (value.controlName =="DatePickerExpiry")
										{
										 newArr.paramValue  = moment(value.expirydate$index).format('YYYY-MM-DD');
										}
										else if (value.controlName =="TimePickerFrom")
										{
											
											var str = value.Fromtime$index;
											var fstr = "'"+str+"'";
											var ffdtr = fstr.toString();
											
										   newArr.paramValue  = ffdtr;
										}
										else if (value.controlName =="TimePickerTo")
										{
											
											var strto = value.Totime$index;
											var fstrto = "'"+strto+"'";
											var ffdtrto = fstrto.toString();
											
										   newArr.paramValue  = ffdtrto;
										   
										}
										else if (value.controlName =="text")
										{
											if($scope.obj.searchPatientName == '' || $scope.obj.searchPatientName == undefined){
												 $scope.patientId = 0	;
										     }
										 newArr.paramValue  = $scope.patientId != null ? $scope.patientId :0;
										}
										else if (value.controlName =="ItemSearch")
										{
											if($scope.medicationObj.searchItem == ''){
												 newArr.paramValue  = 0 ;
											}else{
												newArr.paramValue  = $scope.itemId;
											}
										}
										else if (value.controlName =="Servicesearch")
										{
											if($scope.investigationObj.serviceName == ''){
												 newArr.paramValue  = 0 ;
											}else{
												newArr.paramValue  = $scope.serviceId;
											}
										}
										else if (value.controlName =="multiSelectlist")
										{
											$scope.paramValueData=[];
											angular.forEach(value.multiSelectList ,function(value,key){
												
												$scope.paramValueData.push(value.id);
												
											});
											newArr.paramValue=$scope.paramValueData.toString();
										}else if(value.controlName =="org")
										{
											newArr.paramValue = value.paramValue;

											newArr.paramValue = (value.paramValue==null||value.paramValue==undefined)?0:value.paramValue;

										}
										else if(value.controlName =="unit")
										{
											newArr.paramValue = value.paramValue;

											newArr.paramValue = (value.paramValue==null||value.paramValue==undefined)?0:value.paramValue;

										}
										else if (value.controlName =="DiagnosiSearch")
										{
											if($scope.diagnosisObj.diagnosisName == '' || $scope.diagnosisObj.diagnosisName == undefined){
													$scope.diagnosisId  = 0	;
											}
											 newArr.paramValue  = $scope.diagnosisId   != null ? $scope.diagnosisId :0;
										}else if (value.controlName =="VenderSearch")
										{
											if($scope.venderObj.vendorName == '' || $scope.venderObj.vendorName == undefined){
													$scope.vendorId  = 0	;
											}
											 newArr.paramValue  = $scope.vendorId   != null ? $scope.vendorId :0;
										}
										else
											{
											 newArr.paramValue = value.paramValue;

												newArr.paramValue = (value.paramValue==null||value.paramValue==undefined)?0:value.paramValue;

											}
										$scope.finalArr.push(newArr);
							         });
								if($scope.Obj.format == 'PDF'){
								var data =  { 
										 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath":$scope.selectMisReportObject.action,
						                 "reportFormat": "PDF",
						                 "exportFileName": "admission",
						                 "reportParams": $scope.finalArr
							   };
								
							   reportService.generate(REPORT_API_URI_FOR_MIS,data);
							   $rootScope.stopLoader();
							   
								}
								else{
									var data =  { 
											 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath":$scope.selectMisReportObject.action,
							                 "reportFormat": $scope.Obj.format,
							                 "exportFileName": "admission",
							                 "reportParams": $scope.finalArr
								       };
									
									BillingGenericService.serviceAction("POST",URI,data,NOTIFICATION_MSG_STATUS_FALSE, 'blob').then(function(response) {
										if (response.data!=null)
											{ 
											 var blob = new Blob([response.data], {type: "octet-stream"});
											 var filename = moment().format('DD_MM_YYYY_HH_MM_SS');
											 saveAs(blob, filename+$scope.Obj.extension);
											 $rootScope.stopLoader();
											}
										else {
											$rootScope.stopLoader();
										  }
									});
								}	
							}
							//for search
							$scope.searchReportHeader="";
							$scope.headerIndex=0;
							$scope.reportIndex=0;
							$scope.selectReportSerachId = function(info, model,
									label,ind) {
								
								if(info.reportGroupHeaderId!=null){
									angular.forEach($scope.getHeaderReportList ,function(value, idx){
										if(info.reportGroupHeaderId==value.reportGroupHeaderId){
											$scope.headerIndex=idx;
										}
									});
									$rootScope.isSetFirstTabLevel($scope.headerIndex);
									info.reportGroupHeaderName=info.reportName;
									$scope.investigationObj.serviceName='';
									$rootScope.startLoader();
									
									var data = {
										       organizationId : $scope.organizationId,
										       reportGroupHeaderId : info.reportGroupHeaderId
										       
											   }
									
									var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/getReportDetailsList";

									GenericService.serviceAction("POST", URI, data).then(function(response) {
												
												$scope.getReportList1 = [];
												
												$scope.getReportList = [];
												$scope.getReportList = response.data.listObject;
												angular.forEach($scope.getReportList ,function(value, idx){
													if(info.reportId==value.reportId){
														$scope.reportIndex=idx;
													}
												});
												$rootScope.setFirstTabLevel($scope.reportIndex);
												$rootScope.isSetFirstTabLevel($scope.reportIndex);
												
												$rootScope.stopLoader();
											});
								}
								if(info.reportId!=null){

									
									$scope.investigationObj.serviceName='';
									$scope.reportNamesHOW = info.reportName;
									$rootScope.startLoader();
									$scope.selectMisReportObject = info;
									if(info.reportId != null){
										var data={
												organizationId : $scope.organizationId,
											       unitId : $scope.unitId,
											       reportId:info.reportId,
											       userId :$scope.userId
											       
										}
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/getReportDetailsByReportId";
									BillingGenericService.serviceAction("POST",URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.subSpecialityMasterList= [];
										if (response.data.status == "success")
											$scope.getparameterbyReportId1 = response.data.listObject;
										var time = moment().format("HH:mm");
										angular.forEach($scope.getparameterbyReportId1 ,function(value,key){
											if(value.controlName == "DatePickerFrom"){
												value.fromdate$index = new Date();
											}
											if(value.controlName == "DatePickerTo"){
												value.todate$index = new Date();
											}
											/*if(value.controlName == "DateFromDay"){
												value.fromday$index = new Date();
											}
											if(value.controlName == "DateToDay"){
												value.today$index = new Date();
											}
											if(value.controlName == "DatePickerMonth"){
												value.month$index = new Date();
											}
											if(value.controlName == "DatePickerToMonth"){
												value.tomonth$index = new Date();
											}
											if(value.controlName == "DatePickerYear"){
												value.year$index = new Date();
											}
											if(value.controlName == "DatePickerToYear"){
												value.toyear$index = new Date();
											}*/
											if(value.controlName == "DatePickerExpiry"){
												value.expirydate$index = new Date();
											}
										   if (value.controlName =="TimePickerFrom")
											{
											   value.Fromtime$index = time;
											}
											if (value.controlName =="TimePickerTo")
											{
												value.Totime$index = time;
											}
											if(value.controlName=='unit')
												value.paramValue = $scope.unitId;
											
											if(value.controlName=='org')
												value.paramValue = $scope.organizationId;
											
											value.multiSelectList =[];
										});
										$scope.medicationObj ={
												searchItem : '',
										}
										$scope.reportName=$scope.getparameterbyReportId1[0].reportName;
										$scope.paramType=$scope.getparameterbyReportId1[0].paramType;
										$scope.parameterName=$scope.getparameterbyReportId1[0].parameterName;
										$scope.action=$scope.getparameterbyReportId1[0].action;
										console.log("label",$scope.getparameterbyReportId1)
										$rootScope.stopLoader();
										
									});
									}
								
								}
							}
							// Dynamic stype
                            $scope.getRowStyle = function (index) {
                                                                if(index==6 || index==7 || index==8 || index==9 || index==10 ||
                                                                   index==11 || index==12 || index==12|| index==14
                                                                  || index==15 || index==16 || index==17){
                                                                var marginTop = index * 5;
                                                                return { 'margin-top': marginTop + 'px' };
                                                                }
                                                              };

							$scope.searchedList = [];
							$scope.searchReportName = function(searchReportHeader) {

								
								var data = {
									       	organizationId : $scope.organizationId,
									       	searchString:searchReportHeader,
									       
										   }
								
								var URI = BASE_URL+ ROOT_URL+ "/api/global/unit/searchMISReport";

								return GenericService.serviceAction("POST", URI, data).then(function(response) {

											$rootScope.stopLoader();
											$scope.searchedList = [];
											$scope.searchedList=response.data.listObject;
											return response.data.listObject;
											
											//$scope.getReportListByReportGroupHeaderId($scope.getHeaderReportList[0])

										});
							}
							
							
						 }]);



						