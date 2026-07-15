'use strict';

/**
 * @Author By  Dinesh Jagatap
 * @name myApp.controller:TPAMasterController
 * @description #TPAMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'TPAMasterController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,$state,PagerService) {
							

							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
							$state.go('login');
							return;
							}

							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							   $scope.companyTypeId=2;
							   /*if($scope.userId == null)
								   {
								   $rootScope.loginpage = false;
								   	$state.go('login');
								   }*/
							   $scope.chekedListItem = [];
								$scope.selectedListItem = [];
								$scope.tempSelectItem = [];
								$scope.specialityMst = [];
								
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							
							// for popup
							$scope.showModal = false;
							$scope.buttonClicked = "";
							$scope.popUpFlag = true;
							
							var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/"+$scope.organizationId+"/"+$scope.unitId;
							
							GenericService.serviceAction("GET", URI).then(function (response) {
								if (response.data.status == 'success') {
									
									$scope.fieldConfigList=response.data.listObject;
									if($scope.fieldConfigList.length>0)
										{
										 $scope.registrationFieldObj=$scope.fieldConfigList[0];
										 console.log("registrationFieldObj",$scope.registrationFieldObj);
						}else{
							$scope.registrationFieldObj={
									prefix: "Y",
									firstName: "Y",
									middleName: "N",
									lastName: "Y",
									birthDate: "Y",
									sex: "Y",
									callingCode: "Y",
									mobileNo: "Y",
									mobileDigit:"10",
									nationality:"Y",
									identificationDoc:"Y",
									race:"Y",
									maritalStatus:"Y",
									occupation:"Y",
									patientCategory:"Y",
									email:"N"
							}
							
						}
								}
								else if (response.data.status == 'error') {
									growl.error(response.data.message,
										{
											title: 'Error!'
										});
								}
							});
							$scope.CompanyObj={
									companyId:"",
									companyCode:"",
									companyDesc:"",
									companyTypeId:"",
									creditLimit:"",
									reportPath:"",
									contactPersonName:"",
									address:"",
									startDate:"",
									endDate:"",
									address:"",
									countryId:"",
									stateId:"",
									districtId:"",
									cityId:"",
									areaId:"",
									zipcode:"",
									mobileNo:"",
									residencePhoneNo:"",
									faxNo:"",
									emailId:"",
									paymentTerms:"",
									arCode:"",
									contractNo:""
									
							}
							
							$scope.dateObj=function(){
								// for From date picker
								$scope.endDateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};

								$scope.startDateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
	                                
								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								// for To date picker
								$scope.openDatePickerTo = function() {
									$scope.datepickerTo.opened = true;
								};

								$scope.datepickerTo = {
									opened : false
								};
							}
							$scope.dateObj();
							/*$scope.init=function(){
								//get TPA list 
								var data = {
									unitId:$scope.unitId ,
									organizationId: $scope.organizationId 
										
								}; 
								console.log("data",data); 
								var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterList";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.companyList = [];
													if (response.data.status == "success") {
														$scope.companyList = response.data.listObject;
														console.log("companyList",$scope.companyList);
													}
												});
							}
							$scope.init();*/
							//for setting value to address details start
							$scope.addressObj={};
							$scope.getAdressDetails=function(data){
								$scope.addressObj=data;
								console.log("address details",$scope.addressObj);
							}
							//for setting value to address details End
							
							/* getActiveCountryList() function for getting country active list strat*/
							$scope.getActiveCountryList = function() {
								
								var data = {};
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST+$scope.organizationId;
								GenericService.serviceAction("GET", URI, data)
										.then(function(response) {
													$scope.countryList = [];
													if (response.data.status == "success") {
														$scope.countryList = response.data.listObject;
														console.log("countryList",$scope.countryList);
													}
												});
							}

							$scope.getActiveCountryList(); 
							

							$scope.getActiveARCode=function(){
								
								var URI = BASE_URL + ROOT_URL+ "/global/billing/getActiveARCodeMaster/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService.serviceAction("GET", URI, {})
										.then(function(response) {
											$scope.arCodeList = [];
											if (response.data.status == "success") {
												$scope.arCodeList = response.data.listObject;
												 console.log("arCodeList",$scope.arCodeList);
											}
											
										});
							}
							$scope.getActiveARCode();
							
							/* getActiveCountryList() function for getting  active state list start*/
							$scope.getActiveStateListByCountryId = function(countryId) {
								var data = {};
								var URI = BASE_URL + ROOT_URL+ "/adt/getStateByCountryId/"+countryId;
								GenericService.serviceAction("GET", URI, data)
										.then(function(response) {
													$scope.stateList = [];
													if (response.data.status == "success") {
														$scope.stateList = response.data.listObject;
														console.log("stateList",$scope.stateList);
													}
												});
							}

							/* getActiveDistrictList() function for getting  active district list start*/
							$scope.getActiveDistrictListById = function(stateId) {
								
								var data = {
										stateId:stateId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDISTRICTLISTBYSTATEID;
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.districtList = [];
													if (response.data.status == "success") {
														$scope.districtList = response.data.listObject;
														console.log("districtList",$scope.districtList);
													}
												});
							}
							/* getActiveCityList() function for getting  active city list start*/
							$scope.getActiveCityListById = function(districtId) {
								
								var data = {
										districtId:districtId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETCITYLISTBYDISTRICTID;
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.cityList = [];
													if (response.data.status == "success") {
														$scope.cityList = response.data.listObject;
														console.log("cityList",$scope.cityList);
													}
												});
							}
							/* getAreaList() function for getting  active area list start*/
							$scope.getAreaListById = function(cityId) {
								
								var data = {
										cityId:cityId
								};
								var URI = BASE_URL + ROOT_URL
										+ GETAREALISTBYCITY;
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.areaList = [];
													if (response.data.status == "success") {
														$scope.areaList = response.data.listObject;
														console.log("areaList",$scope.areaList);
													}
												});
							}
//							return false;
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.save();
							}
							
								// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initList = function(orgId,offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterList"
								var URI1 = BASE_URL + ROOT_URL+ "/api/global/countOfCompanyMasterList";

								var data = {
									organizationId : $scope.organizationId,
									unitId :  $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage,
									companyTypeId:$scope.companyTypeId
								}
								console.log("data", data);

								var data1 = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									companyTypeId:$scope.companyTypeId
								}

								// For Get tariff List Using Offset
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.companyList = [];
													if (response.data.status == "success") {
														$scope.companyList = response.data.listObject;
														console.log("companyList",$scope.companyList);
													}
												});

								// For Count tariff List
								GenericService.serviceAction("POST", URI1, data1)
										.then(function(response) {
													if (response.data.status == "success") {
														console.log(response.data);
														$scope.commonListCount = response.data.object;
														if($scope.updateBtnFlag==true){
															console.log($scope.pageNo)
															$scope.setPage($scope.pageNo,true);
														}
														else{
															console.log("else"+$scope.pageNo)
															$scope.setPage(1,false);

														}
														$scope.saveBtnFlag = true;
														$scope.updateBtnFlag=false;
													}
												});
							}
							var pageOffset='';
							$scope.getList = function(orgId,offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								pageOffset=offset;
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
								var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterList";
								var data = {
									organizationId : $scope.organizationId,
									unitId :  $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage,
									companyTypeId:$scope.companyTypeId
								}

								console.log("URI", URI);
								// For Get designation List Using Offset
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.companyList = [];
													if (response.data.status == "success") {
														$scope.companyList = response.data.listObject;														
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
									$scope.getList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize, $scope.unitId);
								}
							}

							$scope.initList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);

							// For Paginations End
							
							

							$scope.getActiveGradeMaster=function(){
								var data = {
												orgId  : $scope.organizationId,
												unitId : $scope.unitId
												
								      		}
								console.log(data);
								var URI = BASE_URL + ROOT_URL+ "/api/billing/getActiveContractGradeMasterList";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
											$scope.gradeList = [];
											if (response.data.status == "success") {
												$scope.gradeList = response.data.listObject;
												 console.log("gradeList",$scope.gradeList);
											}
											
										});
							}
							$scope.getActiveGradeMaster();
							
							
							//for grade mapping start
							
							function uncheckListItem(){
								angular.forEach($scope.gradeList,function(value,index){
									$scope.specialityMst[value.contractGradeMasterId] = false;
								})
							}
							$scope.checkToSelectedListItem = function(obj){
								$scope.chekedListItem.push(obj);
							}
							
							$scope.selectItem = function(){
								$scope.selectedListItem = angular.copy($scope.tempSelectItem);
								console.log("$scope.selectedListItem",$scope.selectedListItem);
							}
							
							//--------------- Select Specuality --------------//
							$scope.addTempItem = function(contractGradeMasterId, gradeDesc){
								var flag = false;
								angular.forEach($scope.tempSelectItem,function(value,index){
									if(value.contractGradeMasterId == contractGradeMasterId){
										$scope.tempSelectItem.splice(index,1);
										$scope.specialityMst[value.contractGradeMasterId] = false;
										flag = true;
									}
								})
								if(!flag){
									var temp = {contractGradeMasterId:contractGradeMasterId,gradeDesc:gradeDesc};
									$scope.tempSelectItem.push(temp);
								}
							}
							//for grade mapping end
							$scope.save = function() {
//								$scope.$broadcast('show-errors-check-validity');
								
								if($scope.CompanyObj.companyDesc == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter TPA Description",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.arCodeId == "" || $scope.CompanyObj.arCodeId==undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select AR Description",
											{
												title : 'Warning!'
											});
									return false;
								}
								

								/*if($scope.CompanyObj.startDate == "" || $scope.CompanyObj.startDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select StartDate ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.endDate == "" || $scope.CompanyObj.endDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select endDate ",
											{
												title : 'Warning!'
											});
									return false;
								}*/
							
								if($scope.CompanyObj.address == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Address ",
											{	
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.countryId == "" || isNaN($scope.CompanyObj.countryId)){
									$rootScope.stopLoader();
									growl.warning("Please Select Country",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.stateId == "" || isNaN($scope.CompanyObj.stateId)){
									$rootScope.stopLoader();
									growl.warning("Please Select State ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.districtId == "" || isNaN($scope.CompanyObj.districtId)){
									$rootScope.stopLoader();
									growl.warning("Please Select District ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.cityId == "" || isNaN($scope.CompanyObj.cityId) ){
									$rootScope.stopLoader();
									growl.warning("Please Select City ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.areaId == "" || isNaN($scope.CompanyObj.areaId) ){
									$rootScope.stopLoader();
									growl.warning("Please Select Area ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.zipcode == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter zipcode ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								
								
								/*if($scope.CompanyObj.mobileNo == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Contact No ",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
								
								
								/*if($scope.CompanyObj.faxNo == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Fax No",
											{
												title : 'Warning!'
											});
									return false;
								}
						*/
								
								/*if($scope.CompanyObj.contactPersonName == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Contact Person Name",
											{
												title : 'Warning!'
											});
									return false;
								}*/
						
							/*	if($scope.selectedListItem.length == 0)
								{
									growl.warning("Please map atleast one grade ",
										{
											title : 'Warning!'
										});
									return false;
								}*/
							
								var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
								if($scope.CompanyObj.emailId != undefined && $scope.CompanyObj.emailId!=""){
								var email = $scope.CompanyObj.emailId;
								if (!filter.test(email)) {
									growl.error(EMAIL_MSG, {
										title : ERROR_MSG
									});
									$('#email').focus();
									return false;
								}		
								}else if($scope.CompanyObj.emailId == undefined){
									$scope.CompanyObj.emailId = "";
								}		



								if ($scope.CompanyObj.faxNo != undefined
										&& $scope.CompanyObj.faxNo != "") {
									var filter1 = /^\+?[0-9]{6,}$/;
									if ($scope.CompanyObj.faxNo != "") {
										var faxNo = $scope.CompanyObj.faxNo;
										if (!filter1.test(faxNo)) {
											growl.error("Invalid Fax Number", {
												title : ERROR_MSG
											});
											$('#faxNo').focus();
											return false;
										}
									}
								}
								
								
								//mobile no validation
								var mobileNo=$scope.CompanyObj.mobileNo;
								if (mobileNo != undefined && mobileNo != "") {
									if (mobileNo.length == parseInt($scope.registrationFieldObj.mobileDigit)) {

									} else {
										growl
												.warning(
														"Please enter valid mobile number",
														{
															title : 'Warning !'
														});
										$('#mobileNo').focus();
										return false;
									}
								}
								
								$scope.currentDate=new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								
										$scope.CompanyObj.createdDate=$scope.currentDate,
										$scope.CompanyObj.updatedDate=$scope.currentDate,
										//$scope.CompanyObj.startDate=moment($scope.CompanyObj.startDate).format('DD-MM-YYYY HH:mm:ss'),
										//$scope.CompanyObj.endDate=moment($scope.CompanyObj.endDate).format('DD-MM-YYYY HH:mm:ss'),
										$scope.CompanyObj.status = 'A',
										$scope.CompanyObj.createdBy=$scope.userId,
										$scope.CompanyObj.updatedBy=$scope.userId,
										$scope.CompanyObj.organizationId=$scope.organizationId,
										$scope.CompanyObj.unitId=$scope.unitId,
										$scope.CompanyObj.companyTypeId=$scope.companyTypeId,
										//$scope.CompanyObj.listCompanyContarctGradeMapperDto = angular.copy($scope.selectedListItem);
										console.log("save",$scope.CompanyObj); 
								/*
								 * if ($scope.insuranceForm.$valid) { if
								 * ($scope.popUpFlag) { $scope.showModal =
								 * !$scope.showModal;
								 *  } else {
								 */

										$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/global/saveCompanyMaster";
								GenericService.serviceAction("POST", URI, $scope.CompanyObj)
										.then(function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});														
													} else {
														$rootScope.stopLoader();
														growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
													}
													$scope.CompanyObj={};
													$scope.selectedListItem = [];
													$scope.tempSelectItem = [];
													uncheckListItem();
													$scope.initList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
												});
								/*	}
								}*/

							}

							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.selectedListItem=[];
								var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyContractGradeByCompanyId/"+id;
								GenericService
										.serviceAction("GET", URI, data)
										.then(function(response) {
											$scope.mappedGradeList=[];
											if (response.data.status == "success") {
														angular.forEach($scope.specialityMst,function(value,index){
															$scope.specialityMst[index] = false;
															var i = $scope.tempSelectItem.indexOf(value);
															$scope.specialityMst.splice(i,1);
														})
														angular.forEach(response.data.listObject,function(value,index){
															var temp = {contractGradeMasterId:value.contractGradeMasterId,gradeDesc:value.gradeDesc};
															$scope.tempSelectItem.push(temp);
															$scope.specialityMst[value.contractGradeMasterId] = true;
															//$scope.selectedListItem.push(temp);
														});
													} else {
														alert("Error!!");
													}
												});

								var data = {

								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterListById/unit/"+$scope.unitId+"/org/"+$scope.organizationId+"/company/"+id;
								GenericService
										.serviceAction("GET", URI, data)
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.CompanyObj=response.data.listObject[0];
														$scope.CompanyObj.startDate = new Date($scope.CompanyObj.startDate);
														$scope.CompanyObj.endDate = new Date($scope.CompanyObj.endDate);
														$scope.CompanyObj.countryId =response.data.listObject[0].countryId.toString();
														$scope.getActiveStateListByCountryId($scope.CompanyObj.countryId);
														$scope.CompanyObj.stateId =response.data.listObject[0].stateId.toString();
														$scope.getActiveDistrictListById ($scope.CompanyObj.stateId);
														$scope.CompanyObj.districtId =response.data.listObject[0].districtId.toString();
														$scope.getActiveCityListById($scope.CompanyObj.districtId);
														$scope.CompanyObj.cityId =response.data.listObject[0].cityId.toString();
														$scope.getAreaListById($scope.CompanyObj.cityId);
														$scope.CompanyObj.areaId =response.data.listObject[0].areaId.toString();
														console.log("by pencil click",$scope.CompanyObj);
													} else {
														alert("Error!!");
													}
												});

							};						

							$scope.updateStatus = function(id, type) {

								$scope.currentDate=new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								var data = {
												companyId : id,
												status : type,
												updatedBy:$scope.userId,
												updatedDate:$scope.currentDate
								      		}
								console.log(data);
								var URI = BASE_URL + ROOT_URL+ "/api/global/updateCompanyMasterStatus";
								GenericService.serviceAction("PUT", URI, data)
										.then(function(response) {

													if (response.data.status == "success") {
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
													} else {
														growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							$scope.pageNo='';
							$scope.update = function() {
//								$scope.$broadcast('show-errors-check-validity');
								
								if($scope.CompanyObj.companyDesc == "" || $scope.CompanyObj.companyDesc==undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter TPA Description",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.arCodeId == "" || $scope.CompanyObj.arCodeId==undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select Area Code",
											{
												title : 'Warning!'
											});
									return false;
								}
								
/*
								if($scope.CompanyObj.startDate == "" || $scope.CompanyObj.startDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select StartDate ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.endDate == "" || $scope.CompanyObj.endDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select endDate ",
											{
												title : 'Warning!'
											});
									return false;
								}*/
							
								if($scope.CompanyObj.address == "" || $scope.CompanyObj.address == undefined ){
									$rootScope.stopLoader();
									growl.warning("Please Enter Address ",
											{	
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.countryId == "" || isNaN($scope.CompanyObj.countryId) || $scope.CompanyObj.countryId == undefined ){
									$rootScope.stopLoader();
									growl.warning("Please Select Country",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.stateId == "" || isNaN($scope.CompanyObj.stateId) || $scope.CompanyObj.stateId == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select State ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.districtId == "" || isNaN($scope.CompanyObj.districtId) || $scope.CompanyObj.districtId == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select District ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.cityId == "" || isNaN($scope.CompanyObj.cityId) || $scope.CompanyObj.cityId == undefined ){
									$rootScope.stopLoader();
									growl.warning("Please Select City ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.areaId == "" || isNaN($scope.CompanyObj.areaId)|| $scope.CompanyObj.areaId == undefined ){
									$rootScope.stopLoader();
									growl.warning("Please Select Area ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.zipcode == "" || $scope.CompanyObj.zipcode == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter zipcode ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								
								
								/*if($scope.CompanyObj.mobileNo == "" || $scope.CompanyObj.mobileNo == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Contact No ",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
								
								
								/*if($scope.CompanyObj.faxNo == "" || $scope.CompanyObj.faxNo == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Fax No",
											{
												title : 'Warning!'
											});
									return false;
								}*/
						
								
								/*if($scope.CompanyObj.contactPersonName == "" || $scope.CompanyObj.contactPersonName == undefined ){
									$rootScope.stopLoader();
									growl.warning("Please Enter Contact Person Name",
											{
												title : 'Warning!'
											});
									return false;
								}*/
						
							/*	if($scope.selectedListItem.length == 0)
								{
									growl.warning("Please map atleast one grade ",
										{
											title : 'Warning!'
										});
									return false;
								}*/

								
								
								var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
								if($scope.CompanyObj.emailId!= undefined && $scope.CompanyObj.emailId!=""){
								var email = $scope.CompanyObj.emailId;
								if (!filter.test(email)) {
									growl.error(EMAIL_MSG, {
										title : ERROR_MSG
									});
									$('#email').focus();
									return false;
								}		
								}			
								


								if ($scope.CompanyObj.faxNo != undefined
										&& $scope.CompanyObj.faxNo != "") {
									var filter1 = /^\+?[0-9]{6,}$/;
									if ($scope.CompanyObj.faxNo != "") {
										var faxNo = $scope.CompanyObj.faxNo;
										if (!filter1.test(faxNo)) {
											growl.error("Invalid Fax Number", {
												title : ERROR_MSG
											});
											$('#faxNo').focus();
											return false;
										}
									}
								}
										
								//mobile no validation
								var mobileNo=$scope.CompanyObj.mobileNo;
								if(mobileNo!= undefined && mobileNo!=""){
									if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
					        			
									}else{
										growl.warning("Please enter valid mobile number",{
											title : 'Warning !'
											});
											$('#mobileNo').focus();
											return false;
									}
									
								}
								
								
								
										$scope.currentDate=new Date();
										$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
										/*$scope.CompanyObj.startDate=moment($scope.CompanyObj.startDate).format('DD-MM-YYYY HH:mm:ss'),
										$scope.CompanyObj.endDate=moment($scope.CompanyObj.endDate).format('DD-MM-YYYY HH:mm:ss'),*/
										$scope.CompanyObj.updatedDate=$scope.currentDate,
										$scope.CompanyObj.updatedBy=$scope.userId,
										$scope.CompanyObj.organizationId=$scope.organizationId,
										$scope.CompanyObj.unitId=$scope.unitId,
										$scope.CompanyObj.status='A',
										//$scope.CompanyObj.listCompanyContarctGradeMapperDto = angular.copy($scope.selectedListItem);
										console.log("update",$scope.CompanyObj);  
						
//								if ($scope.admissiontypeform.$valid) {
//								console.log(data);
										$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/api/global/updateCompanyMaster";
								GenericService.serviceAction("PUT", URI, $scope.CompanyObj).then(
												function(response) {
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
													} else {
														$rootScope.stopLoader();
														growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
													}
													
													$scope.CompanyObj={};
													$scope.selectedListItem = [];
													$scope.tempSelectItem = [];
													uncheckListItem();
				
													$scope.offset=pageOffset;
													console.log($scope.offset)
													$scope.initList(
																	$scope.organizationId,
																	$scope.offset,
																	$scope.noOfRecordsPerPage,
																	$scope.unitId);
													$scope.pageNo=($scope.offset/10)+1;
												    $scope.setPage($scope.pageNo,true)
												    console.log("pageNo"+$scope.pageNo)
												});
//								}
							}
							
							 $scope.setPostCode = function(areaId){
									
								angular.forEach($scope.areaList,function(value,key){
											
											if(value.areaId==areaId)
											{
												$scope.CompanyObj.zipcode = value.postCode;
											}
								});
							 }
						} ]);
