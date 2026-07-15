'use strict';

/**
 * @Author By  Dinesh Jagatap
 * @name myApp.controller:corporateMasterController
 * @description #corporateMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'corporateMasterController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'GenericServiceFileUpload',
						'growl',
						'$state',
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,BillingGenericService,GenericServiceFileUpload, growl,$state,PagerService) {
							

							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.firstLevelTab = 1;
							$scope.isSetFirstTabLevel = function(tabNumber){
								return $scope.firstLevelTab==tabNumber;
							}
							
						
							$scope.setFirstTabLevel = function(newTab){
								$scope.firstLevelTab = newTab;
							}
//							/$scope.setFirstTabLevel(2);
							
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.companyTypeId=3;
							$scope.searchKeyword="";
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
							
							$scope.CompanyObj={
									companyId:"",
									companyCode:"",
									companyDesc:"",
									companyTypeId:"",
									creditLimit:"",
                                    creditDays:"",
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
								//	arCodeId:"",
									contractNo:"",
									listCompanyContarctGradeMapperDto:"",
									blockReportPrint:"",
									
							}
							
							$scope.dateObj=function(){
								// for From date picker
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};
								$scope.dependantDOBOption = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date()
								}
								$scope.todateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
									
								};
								
								$scope.fromDateOptions = {
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
							
							//init function  for corporate emplyee start
							//For Kin Object
							$scope.init=function(){
								$scope.search="";
								$scope.employee={
										corporateId:"",
										employeeNumber:"",
										firstName:"",
										middleName:"",
										lastName:"",
										mobileNo:"",
										emailId:"",
										address:"",
										countryId:"",
										stateId:"",
										districtId:"",
										cityId:"",
										areaId:"",
										zipCode:"",
										status:"",
										createdBy:$scope.userId,
										updatedBy:$scope.userId,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId
								}
								
							$scope.dataList = [ {
								prefixId : '',
								firstName : '',
								middleName : '',
								lastName : '',
								mobileNo : '',
								genderId : '',
								dobKin : '',
								occupationId : '',
								relationId : '',
								isNok : 'N',
//								typeId:'2',
								identificationTypeId:'',
								identificationNumber:'',
								createdDate:moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss'),
								createdBy:$scope.userId,
								updatedDate:moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss'),
								updatedBy:$scope.userId,
								status:'A',
								organizationId:$scope.organizationId,
								unitId:$scope.unitId
								
							} ];
								
								$scope.searchObj={
										corporateId:"",
										employeeNumber:"",
										firstName:"",
										organizationId:$scope.organizationId,
										unitId:$scope.unitId
								}
								//for next of kin
								$scope.openDatePickerDob = function(index) {
									// alert("in");
									// $scope.expiry.opened = true;
									for (var i = $scope.dataList.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.dobKin.opened[i] = true;
										} else {
											$scope.dobKin.opened[i] = false;
										}
									}
								};

								$scope.dobKin = {
									opened : [ false ],

								};

							var commonObj = {
								unitId : $scope.unitId,
								organizationId : $scope.organizationId
							};
							var commonObj1 = {
								organizationId : $scope.organizationId
							};
							
							var commonObj2 = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									typeId : '2'
								};
							var data = {};
							
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVERELATIONLIST;
					GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.relationList = [];

										if (response.data.status == "success") {
											$scope.relationList = response.data.listObject;

										}
									});
							
					var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterList"

					var data = {
						organizationId : $scope.organizationId,
						unitId :  $scope.unitId,
						companyTypeId:3
					}

					

					/*GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
										$scope.corporateList = [];
										if (response.data.status == "success") {
											$scope.corporateList = response.data.listObject;
											console.log("corporateList",$scope.corporateList);
										}
									});
 */
						
					var URI = BASE_URL + ROOT_URL+"/api/global/companyMaster/unit/"+$scope.unitId+"/org/"+ $scope.organizationId
					GenericService.serviceAction("GET", URI, '')
							.then(function(response) {
										$scope.corporateList = [];
										if (response.data.status == "success") {
											$scope.corporateList = response.data.listObject;
											console.log("corporateList",$scope.corporateList);
										}
									});

							var URI = BASE_URL + ROOT_URL
									+ GETACTIVEPREFIXLIST;
							BillingGenericService
									.serviceAction(METHOD_GET, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.prefixList = [];
												if (response.data.status == "success")
													$scope.prefixList = response.data.listObject;
											});

							var URI = BASE_URL + ROOT_URL
									+ GETACTIVEGENDERLIST;
							BillingGenericService
									.serviceAction(METHOD_GET, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.genderList = [];
												if (response.data.status == "success")
													$scope.genderList = response.data.listObject;
											});
							
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVEOCCUPATIONLIST;
					BillingGenericService
							.serviceAction(METHOD_GET, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.occupationList = [];
										if (response.data.status == "success")
											$scope.occupationList = response.data.listObject;
									});
					
							var URI = BASE_URL + ROOT_URL
									+ GETACTIVENATIONALITYLIST;
							BillingGenericService
									.serviceAction(METHOD_GET, URI, "",
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.nationalityList = [];
												if (response.data.status == "success")
													$scope.nationalityList = response.data.listObject;
											});

							var URI = BASE_URL + ROOT_URL + GETRACELIST;

							BillingGenericService
									.serviceAction(METHOD_POST, URI, commonObj1,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.raceList = [];
												if (response.data.status == "success")
													$scope.raceList = response.data.listObject;
											});
							
							

							var URI = BASE_URL + ROOT_URL
									+ GETACTIVEMARITALSTATUSLIST;

							BillingGenericService
									.serviceAction(METHOD_GET, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.maritalStatusList = [];
												if (response.data.status == "success")
													$scope.maritalStatusList = response.data.listObject;
											});
							

							var URI = BASE_URL + ROOT_URL
									+ GETACTIVEIDENTIFICATION;
							BillingGenericService
							.serviceAction(METHOD_GET, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.identificationList = [];
										if (response.data.status == "success")
											$scope.identificationList = response.data.listObject;
									});

							var URI = BASE_URL + ROOT_URL
									+ GETACTIVECOUNTRYLIST+$scope.organizationId;								
							BillingGenericService
							.serviceAction(METHOD_GET, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.countryList = [];
										if (response.data.status == "success")
											$scope.countryList = response.data.listObject;
									});

						}
						

						$scope.init();
						$scope.getValidationConfiguration=function(){
							//code for registration field configuration start								
							
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

								} else if (response.data.status == 'error') {
									growl.error(response.data.message,
										{
											title: 'Error!'
										});
								}
							});
						
							//getting default field configuration start
						}
						$scope.getValidationConfiguration();

							
							// init function for corporate employee end
							
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

						    $scope.getLedgerList = function(keyword)
						    {
						        $scope.ledgerList=[];
						       var URI = BASE_URL + ROOT_URL
						       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
						       var data = {
						               
						           }
						        GenericService
						       .serviceAction("POST", URI, data)
						       .then(
						               function(response) {
						                   if (response.data.status == "success") {
						                       $rootScope.stopLoader();
						                       $scope.ledgerList=response.data.listObject;
						                   }
						               });
						    }
						    $scope.getLedgerList();

							$scope.ledgerNameSearch = function(keyword)
                            {
                            	$scope.CompanyObj.generalLedgerId=null;
                                var URI = BASE_URL + ROOT_URL
                                + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.organizationId;
                                var data = {
                                        "generalLedgerName":keyword
                                    }
                                return GenericService
                                .serviceAction("POST", URI, data)
                                .then(
                                        function(response) {
                                            if (response.data.status == "success") {
                                                $rootScope.stopLoader();
                                                return response.data.listObject;
                                            }
                                        });
                            }
                            $scope.SelectLedgerName =function(ledger)
                            {
                                $scope.CompanyObj.generalLedgerId=ledger.generalLedgerId;
                            }
                            $scope.clearText = function()
						    {
						    	if($scope.CompanyObj.generalLedgerId==undefined || $scope.CompanyObj.generalLedgerId==null || $scope.CompanyObj.generalLedgerId==0)
						    		{
						    		$scope.CompanyObj.generalLedgerName="";
						    		}
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
									unitId         : $scope.unitId,
									companyTypeId:$scope.companyTypeId
								}

								// For Get tariff List Using Offset
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.companyList = [];
													if (response.data.status == "success") {
														$scope.companyList = response.data.listObject;
														console.log("companyList",$scope.companyList);
														angular.forEach($scope.companyList, function(value , key) {
													          angular.forEach($scope.ledgerList, function(value1 , key) {
													              if(value.generalLedgerId==value1.generalLedgerId)
													                  {
													                  value.generalLedgerName=value1.generalLedgerName;
													                  }
													          });
													        });

													}
												});

								// For Count tariff List
								GenericService.serviceAction("POST", URI1, data1)
										.then(function(response) {
													if (response.data.status == "success") {
														console.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);

													}
												});
							}

							$scope.getList = function(orgId,offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
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
														angular.forEach($scope.companyList, function(value , key) {
													          angular.forEach($scope.ledgerList, function(value1 , key) {
													              if(value.generalLedgerId==value1.generalLedgerId)
													                  {
													                  value.generalLedgerName=value1.generalLedgerName;
													                  }
													          });
													        });
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
								
								if($scope.CompanyObj.companyCode == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Corporate Code ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.companyDesc == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Corporate Description ",
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
								
								if($scope.CompanyObj.creditLimit == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Credit Limit ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
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
								
								/*if($scope.CompanyObj.stateId == "" || isNaN($scope.CompanyObj.stateId)){
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
								
								/*if($scope.CompanyObj.reportPath == ""){
									$rootScope.stopLoader();
									growl.warning("Please Select Document",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
										if($scope.selectedListItem.length == 0)
											{
												growl.warning("Please map atleast one grade ",
													{
														title : 'Warning!'
													});
												//return false;
											}
										else
											{
										$rootScope.stopLoader();
									$scope.$broadcast('show-errors-check-validity');
								if ($scope.corporateMasterForm.$valid) {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										$rootScope.startLoader();
										
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
												$scope.CompanyObj.listCompanyContarctGradeMapperDto = angular.copy($scope.selectedListItem);
												$scope.patientImage1=$scope.CompanyObj.reportPath;
												$scope.CompanyObj.reportPath = "";												
												console.log("save",$scope.CompanyObj); 
												console.log("$scope.CompanyObj.reportPath",$scope.CompanyObj.reportPath);
										
										$rootScope.startLoader();
								console.log('$scope.CompanyObj',$scope.CompanyObj);
								//return false;
								  
								var URI = BASE_URL + ROOT_URL + "/api/global/saveCompanyMaster";
								GenericService.serviceAction("POST", URI, $scope.CompanyObj)
										.then(function(response) {
													if (response.data.status == "success") {
														
														
														$rootScope.stopLoader();
														  growl.success(response.data.message,
																{
																	title : 'Success!'
																});	
														  
														if($scope.patientImage1!='' && $scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1))
														   {
														     $scope.uploadPatientImage($scope.patientImage1,response.data.object.companyId,$scope.organizationId,$scope.unitId);
														   }
														/*else
															{
															$rootScope.stopLoader();
															  growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});	
															}*/
																											
													} else {
														$rootScope.stopLoader();
														angular.element("input[type='file']").val(null);
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
									}
								}
								
							}

							}
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;								
								$scope.selectedListItem=[];
								var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyContractGradeByCompanyId/"+id;
								GenericService
										.serviceAction("GET", URI, data)
										.then(function(response) {
											if (response.data.status == "success") {
														/*$scope.mappedGradeList=response.data.listObject;
														console.log("mappedGradeList",$scope.mappedGradeList);
														
														
														angular.forEach($scope.mappedGradeList,
																function(value, index) {
															var data = {};																
																	data.contractGradeMasterId = value.contractGradeMasterId;
																	data.gradeDesc = value.gradeDesc;															
															
																	$scope.selectedListItem.push(data);
																});
													

														console.log("selectedListItem",$scope.selectedListItem);*/
														angular.forEach($scope.specialityMst,function(value,index){
															$scope.specialityMst[index] = false;
															var i = $scope.tempSelectItem.indexOf(value);
															$scope.specialityMst.splice(i,1);
														})
														$scope.selectedListItem=[];
														$scope.tempSelectItem=[];
														angular.forEach(response.data.listObject,function(value,index){
															var temp = {contractGradeMasterId:value.contractGradeMasterId,gradeDesc:value.gradeDesc};
															$scope.tempSelectItem.push(temp);
															$scope.specialityMst[value.contractGradeMasterId] = true;
															$scope.selectedListItem.push(temp);
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
														$scope.CompanyObj.countryId =response.data.listObject[0].countryId;
														$scope.getActiveStateListByCountryId($scope.CompanyObj.countryId);
														$scope.CompanyObj.stateId =response.data.listObject[0].stateId;
														$scope.getActiveDistrictListById ($scope.CompanyObj.stateId);
														$scope.CompanyObj.districtId =response.data.listObject[0].districtId;
														$scope.getActiveCityListById($scope.CompanyObj.districtId);
														$scope.CompanyObj.cityId =response.data.listObject[0].cityId;
														$scope.getAreaListById($scope.CompanyObj.cityId);
														$scope.CompanyObj.areaId =response.data.listObject[0].areaId;
														$scope.CompanyObj.arCodeId =response.data.listObject[0].arCodeId;
														$scope.CompanyObj.reportPath =response.data.listObject[0].reportPath;
														$scope.CompanyObj.blockReportPrint =response.data.listObject[0].blockReportPrint;
														angular.forEach($scope.ledgerList, function(value , key) {
											                if($scope.CompanyObj.generalLedgerId==value.generalLedgerId)
											                    {
											                	$scope.CompanyObj.generalLedgerName=value.generalLedgerName;
											                	$scope.CompanyObj.generalLedgerId=value.generalLedgerId;
											                    }
											            });
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

							$scope.update = function() {
								
								if($scope.CompanyObj.companyCode == "" || $scope.CompanyObj.companyCode == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Corporate Code ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.companyDesc == "" || $scope.CompanyObj.companyDesc == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Corporate Description ",
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
								
								if($scope.CompanyObj.creditLimit == "" || $scope.CompanyObj.creditLimit == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Credit Limit ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.address == "" || $scope.CompanyObj.address == undefined){
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
								
								/*if($scope.CompanyObj.stateId == "" || isNaN($scope.CompanyObj.stateId)|| $scope.CompanyObj.stateId == undefined){
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
								
								if($scope.CompanyObj.cityId == "" || isNaN($scope.CompanyObj.cityId) || $scope.CompanyObj.cityId == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select City ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.CompanyObj.areaId == "" || isNaN($scope.CompanyObj.areaId) || $scope.CompanyObj.areaId == undefined ){
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
								
								/*if($scope.CompanyObj.reportPath == "" || $scope.CompanyObj.reportPath == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select Document",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
										$scope.$broadcast('show-errors-check-validity');
										
										if($scope.selectedListItem.length == 0)
										{
											growl.warning("Please map atleast one grade ",
												{
													title : 'Warning!'
												});
											//return false;
										}
									else
										{
										
										$scope.currentDate=new Date();
										$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
									//	$scope.CompanyObj.startDate=moment($scope.CompanyObj.startDate).format('DD-MM-YYYY HH:mm:ss'),
									//	$scope.CompanyObj.endDate=moment($scope.CompanyObj.endDate).format('DD-MM-YYYY HH:mm:ss'),
										$scope.CompanyObj.updatedDate=$scope.currentDate,
										$scope.CompanyObj.updatedBy=$scope.userId,
										$scope.CompanyObj.organizationId=$scope.organizationId,
										$scope.CompanyObj.unitId=$scope.unitId,
										$scope.CompanyObj.status='A',
										$scope.patientImage1=$scope.CompanyObj.reportPath,
										$scope.CompanyObj.reportPath='';
										$scope.CompanyObj.listCompanyContarctGradeMapperDto = angular.copy($scope.selectedListItem);
										console.log("update",$scope.CompanyObj);  
						
								if ($scope.corporateMasterForm.$valid) {
									$rootScope.startLoader();
								//console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL+ "/api/global/updateCompanyMaster";
								GenericService.serviceAction("PUT", URI, $scope.CompanyObj).then(
												function(response) {
													if (response.data.status == "success") {
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
														if($scope.patientImage1!='' && $scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1))
														   {
														     $scope.uploadPatientImage($scope.patientImage1,response.data.object,$scope.organizationId,$scope.unitId);
														   }
														$rootScope.stopLoader();
													} else {
														angular.element("input[type='file']").val(null);
														growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
														$rootScope.stopLoader();
													}
													$scope.CompanyObj={};
													$scope.selectedListItem=[];
													$scope.tempSelectItem = [];
													uncheckListItem();
												$scope.initList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);

											});
									}
								}
							}
							
							 $scope.uploadPatientImage = function(inputFile,companyId,orgId,unitId)
							    {
							    	    var file = inputFile;
								        var fd = new FormData();
								        fd.append('file', inputFile);
								        
								     
										var URI1 = BASE_URL + ROOT_URL + "/api/global/company/image/"+orgId+"/"+unitId+"/"+companyId;
										GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$scope.patientImage1="";
												 angular.element("input[type='file']").val(null);
												 $scope.initList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
													$scope.CompanyObj={
															companyId:"",
															companyCode:"",
															companyDesc:"",
															companyTypeId:"",
															creditLimit:"",
                                                            creditDays:"",
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
														//	arCodeId:"",
															contractNo:"",
															listCompanyContarctGradeMapperDto:"",
															blockReportPrint:"",
															
													}
												$rootScope.stopLoader();
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
											}
										});
							    }
							 
							 $scope.openCorporateImage = function(documentPath)
                             { 
//								$rootScope.startLoader();
								if(documentPath!==""){
									var imageUrl = documentPath;
									var href = $rootScope.currentHrefDoccument();
									$.fancybox.open({
										width: 600, // or whatever
										height: 320,
										href:href+imageUrl,
										type: 'iframe'
									});
								}else{
									growl.error("No image found",
											{
												title : 'Error!'
											});
								}
								
//                        $rootScope.stopLoader();
                       }
//							 ========================================Corporate Employee Details==============================
								$scope.add = function() {
									var data = {
											prefixId : '',
											firstName : '',
											middleName : '',
											lastName : '',
											mobileNo : '',
											genderId : '',
											dobKin : '',
											occupationId : '',
											relationId : '',
											isNok : 'N',
//											typeId:'2',
											identificationTypeId:'',
											identificationNumber:'',
											createdDate:moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss'),
											createdBy:$scope.userId,
											updatedDate:moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											status:'A',
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
									};
									$scope.dataList.push(data);
								};

								$scope.remove = function() {
									var newDataList = [];
									var le = $scope.dataList.length;
									var i = 1;
									if(le > 1){
									angular.forEach($scope.dataList, function(v) {
										if (le != i) {
											newDataList.push(v);
										}
										i++;
									});
								
									$scope.dataList = newDataList;
									}
								};
								
								
								 $scope.searchEmployee=function(){
									 console.log("searchObj",$scope.searchObj)
									 $scope.employeeListDetails=[];
										$rootScope.startLoader();
									 var URI = BASE_URL + ROOT_URL+ "/api/global/searchCorporateEmployee";
									 GenericService.serviceAction("POST", URI, $scope.searchObj).then(
													function(response) {
														if (response.data.status == "success") {
																											
															$scope.employeeListDetails=response.data.listObject;
															console.log("emp list",$scope.employeeListDetails);
														} else {
															growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
														}
														$scope.init();
														$rootScope.stopLoader();
									
													});
								 }
								 
								 $scope.searchEmployee();

							 $scope.saveDetails=function(){
								 
								 if($scope.employee.corporateId == "" || $scope.employee.corporateId ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select Corporate Description",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.employeeNumber == "" || $scope.employee.employeeNumber ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter Employee Number",
												{
													title : 'Warning!'
												});
										return false;
									}
									

									if($scope.employee.firstName == "" || $scope.employee.firstName ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter First Name ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.lastName == "" || $scope.employee.lastName ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter Last Name ",
												{
													title : 'Warning!'
												});
										return false;
									}
								
									if($scope.employee.mobileNo == "" || $scope.employee.mobileNo ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter Contact No ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									
									
									if($scope.employee.address == "" || $scope.employee.address ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter Address ",
												{	
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.countryId == "" || isNaN($scope.employee.countryId)){
										$rootScope.stopLoader();
										growl.warning("Please Select Country",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.stateId == "" || isNaN($scope.employee.stateId)){
										$rootScope.stopLoader();
										growl.warning("Please Select State ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.districtId == "" || isNaN($scope.employee.districtId)){
										$rootScope.stopLoader();
										growl.warning("Please Select District ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.cityId == "" || isNaN($scope.employee.cityId) ){
										$rootScope.stopLoader();
										growl.warning("Please Select City ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.areaId == "" || isNaN($scope.employee.areaId) ){
										$rootScope.stopLoader();
										growl.warning("Please Select Area ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.zipCode == "" || $scope.employee.zipCode ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter zipcode ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
								 $scope.$broadcast('show-errors-check-validity');
								 if($scope.employee.emailId!=""){						 
									 
									 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
					           		 var email = $scope.employee.emailId;
					           		    if (!filter.test(email)) {
				        				    growl.error(EMAIL_MSG,{
												title : ERROR_MSG
											});
	//			        				    $('#emailId').focus();
				        				    return false;
					           		    }
								 }
								 var mobileNo=$scope.employee.mobileNo;
				           		 if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
					        			
									}else{
										growl.error("Please enter valid mobile number",{
											title : ERROR_MSG
											});
											$('#mobileNo').focus();
											return false;
				        		}
								 if($scope.corporateEmployeeForm1.$valid){
								 $scope.dataListFinal=[];
								 $scope.mobileFlag = false;
										if($scope.dataList.length > 0)
											{
										angular.forEach($scope.dataList, function(value,key) {
											
											if(value.dobKin!="" ||value.genderId!="" || value.prefixId!="" || value.firstName!=""
												||value.lastName!="" || value.mobileNo!="" || value.identificationNumber!="" || value.identificationTypeId!=""){
											//mobile no validation
											var mobileNo=value.mobileNo;
											if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
							        			
											}else{
												$scope.mobileFlag = true;
											}
											
											if(!$scope.mobileFlag){
											value.dobKin  = moment(value.dobKin).format('DD-MM-YYYY HH:mm:ss');
											value.firstName=$rootScope.capitalize_Words(value.firstName);
										    value.middleName=(value.middleName!="" ? $rootScope.capitalize_Words(value.middleName) : ".");
										    value.lastName=(value.lastName!=undefined || value.lastName!="" || value.lastName.trim()!=null ? $rootScope.capitalize_Words(value.lastName) : "");
											 var isEmpty = !(Object.values(value).every(x =>(x != null &&  x != '')));
											 if(!isEmpty)
												 {
												 if(value.middleName=="."){
													 value.middleName="";
												 }
												 	$scope.dataListFinal.push(value);
												}
											}
											}
										});
										
										if($scope.mobileFlag){
											growl.error("Please enter valid mobile number",{
												title : 'error !'
												});
												return false;
										}
									}
										
										$scope.employee.firstName=$rootScope.capitalize_Words($scope.employee.firstName);
										$scope.employee.middleName=($scope.employee.middleName!=undefined || $scope.employee.middleName!="" || $scope.employee.middleName.trim()!=null ? $rootScope.capitalize_Words($scope.employee.middleName) : " ");
										$scope.employee.lastName=($scope.employee.lastName!=undefined || $scope.employee.lastName!="" || $scope.employee.lastName.trim()!=null ? $rootScope.capitalize_Words($scope.employee.lastName) : "");
										$scope.employee.dependantsDetailsList=$scope.dataListFinal;
									$rootScope.startLoader();
								 var URI = BASE_URL + ROOT_URL+ "/api/global/saveCorporateEmployeeMaster";
								 GenericService.serviceAction("POST", URI, $scope.employee).then(
												function(response) {
													if (response.data.status == "success") {
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});	
														 $scope.searchEmployee();
														
													} else {
														growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
													}
													$('#addEmployeeDetailsModal').modal('hide');
													$scope.init();
													 $scope.employeeListDetails=[];
													$rootScope.stopLoader();
								
												});
								 		}
							 		}
							 
							
							 
							 $scope.getAddtessDetails=function(obj){
								 $scope.addressDetails={};
								 $scope.addressDetails=obj;
							 }
							 $scope.updateStatusCorporateEmp=function(id,status){
								 var data={
										 status:status,
										 corporateEmployeeId:id,
										 updatedBy:$scope.userId,
								 }
									$rootScope.startLoader();
								 var URI = BASE_URL + ROOT_URL+ "/api/global/updateCorporateEmployeeMasterStatus";
								 GenericService.serviceAction("POST", URI, data).then(
												function(response) {
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
													$scope.init();
													$rootScope.stopLoader();
								
												});
							 }
							 $scope.getDependentsDetails = function(corporateEmpId){
								 if(corporateEmpId!=undefined && corporateEmpId!="" && corporateEmpId!=null){
									 $scope.employeeDependentsList=[];
									 var data={
												 corporateEmployeeId:corporateEmpId,
												 organizationId:$scope.organizationId,
												 unitId:$scope.unitId
									 		}
									 console.log("data ",data);
										$rootScope.startLoader();
									 var URI = BASE_URL + ROOT_URL+ "/api/global/getCorporateEmployeeDependantsDetailsByEmpId";
									 GenericService.serviceAction("POST", URI, data).then(
													function(response) {
														if (response.data.status == "success") {
																											
															$scope.employeeDependentsList=response.data.listObject;
																														
															console.log("emp dependents list",$scope.employeeDependentsList);
														} else {
															growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
														}
														
														$scope.init();
														 if($scope.employeeDependentsList.length>0){
																$('#dependentDetailsModal').modal('show');
														 }else{
															 growl.warning('dependent not added.',
																		{
																			title : 'Warning!'
																		});
														 }
														$rootScope.stopLoader();
									
													});
								 }
								 
							 }
							 $scope.showUpdateBtnCorporateEmp=function(corporateEmpId){
								 $scope.saveBtnFlag = false;
								 $scope.updateBtnFlag = true;
								 var data={
										 corporateEmployeeId:corporateEmpId,
										 organizationId:$scope.organizationId,
										 unitId:$scope.unitId
							 		}
								 
								 console.log("data ",data);
								 $scope.employeeDetailsObj="";
								 $rootScope.startLoader();
								 $scope.dataList=[];
								 var URI = BASE_URL + ROOT_URL+ "/api/global/getCorporateEmployeeMasterDetailsById";
								 GenericService.serviceAction("POST", URI, data).then(
												function(response) {
													if (response.data.status == "success") {																										
														 $scope.employeeDetailsObj=response.data.object;	
														 if($scope.employeeDetailsObj.dependantsDetailsList!=null && $scope.employeeDetailsObj.dependantsDetailsList.length > 0)
															{
																angular.forEach($scope.employeeDetailsObj.dependantsDetailsList, function(value,key) {																
																	value.dobKin  = new Date(value.dobKin);
																	 $scope.dataList.push(value);
																});
															}else{
																$scope.dataList = [ {
																	prefixId : '',
																	firstName : '',
																	middleName : '',
																	lastName : '',
																	mobileNo : '',
																	genderId : '',
																	dobKin : '',
																	occupationId : '',
																	relationId : '',
																	isNok : 'N',
//																	typeId:'2',
																	identificationTypeId:'',
																	identificationNumber:'',
																	createdDate:moment($scope.currentDate)
																	.format('DD-MM-YYYY HH:mm:ss'),
																	createdBy:$scope.userId,
																	updatedDate:moment($scope.currentDate)
																	.format('DD-MM-YYYY HH:mm:ss'),
																	updatedBy:$scope.userId,
																	status:'A',
																	organizationId:$scope.organizationId,
																	unitId:$scope.unitId
																	
																} ];
															}
														 $scope.employee= $scope.employeeDetailsObj;
														 $scope.employee.countryId =response.data.object.countryId;
															$scope.getActiveStateListByCountryId($scope.employee.countryId);
															$scope.employee.stateId =response.data.object.stateId;
															$scope.getActiveDistrictListById ($scope.employee.stateId);
															$scope.employee.districtId =$scope.employee.districtId;
															$scope.getActiveCityListById($scope.employee.districtId);
															$scope.employee.cityId =$scope.employee.cityId;
															$scope.getAreaListById($scope.employee.cityId);
															$scope.employee.areaId =$scope.employee.areaId;
														//show pop up
														$('#addEmployeeDetailsModal').modal('show');
													} else {
															growl.error(response.data.message,{title : 'Error!'});
														}													
//													$scope.init();
													$rootScope.stopLoader();
								
												});
							 }
							 $scope.updateDetails=function(){
								 
								 if($scope.employee.corporateId == "" || $scope.employee.corporateId ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select Corporate Description",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.employeeNumber == "" || $scope.employee.employeeNumber ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter Employee Number",
												{
													title : 'Warning!'
												});
										return false;
									}
									

									if($scope.employee.firstName == "" || $scope.employee.firstName ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter First Name ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.lastName == "" || $scope.employee.lastName ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter Last Name ",
												{
													title : 'Warning!'
												});
										return false;
									}
								
									if($scope.employee.mobileNo == "" || $scope.employee.mobileNo ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter Contact No ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									
									
									if($scope.employee.address == "" || $scope.employee.address ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter Address ",
												{	
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.countryId == "" || isNaN($scope.employee.countryId) || $scope.employee.countryId ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select Country",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.stateId == "" || isNaN($scope.employee.stateId) || $scope.employee.stateId ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select State ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.districtId == "" || isNaN($scope.employee.districtId) || $scope.employee.districtId ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select District ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.cityId == "" || isNaN($scope.employee.cityId) || $scope.employee.cityId ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select City ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.areaId == "" || isNaN($scope.employee.areaId) || $scope.employee.areaId ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Select Area ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
									if($scope.employee.zipCode == "" || $scope.employee.zipCode ==undefined){
										$rootScope.stopLoader();
										growl.warning("Please Enter zipcode ",
												{
													title : 'Warning!'
												});
										return false;
									}
									
								 
								//mobile no validation
									var mobileNo=$scope.employee.mobileNo;
									if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
					        			
									}else{
										growl.error("Please enter valid mobile number",{
											title : 'error !'
											});
											$('#mobileNo').focus();
											return false;
									}
									//email validation
									 if($scope.employee.emailId!=""){						 
										 
									 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
					           		 var email = $scope.employee.emailId;
					           		    if (!filter.test(email)) {
				        				    growl.error(EMAIL_MSG,{
												title : 'error'
											});
				        				    return false;
					           		    }
									 }
								 $scope.dataListFinal=[];
								 console.log("dependants details",$scope.dataList)
									
									 console.log("employee",$scope.employee);
								 		$scope.mobileFlag = false;
										if($scope.dataList.length > 0)
											{
										angular.forEach($scope.dataList, function(value,key) {
											
											if(value.dobKin!="" ||value.genderId!="" || value.prefixId!="" || value.firstName!=""
												||value.lastName!="" || value.mobileNo!="" || value.identificationNumber!="" || value.identificationTypeId!=""){
											//mobile no validation
											var mobileNo=value.mobileNo;
											if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
							        			
											}else{
												$scope.mobileFlag = true;
											}
											
											if(!$scope.mobileFlag){
											
											value.middleName=(value.middleName!="" ? $rootScope.capitalize_Words(value.middleName) : ".");
											value.dobKin  = moment(value.dobKin).format('DD-MM-YYYY HH:mm:ss');					
											value.organizationId=$scope.organizationId;
											value.unitId=$scope.unitId;
										    value.employeeNumber=$scope.employee.employeeNumber;
										    value.empName=$scope.employee.firstName;										  	
										    value.createdBy=  $scope.userId;
										    value.updatedBy=  $scope.userId;
										    value.updatedDate=moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
										    value.createdDate=moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
											 var isEmpty = !(Object.values(value).every(x =>(x != null &&  x != '')));
											 if(!isEmpty)
												 {
												 if(value.middleName=="."){
													 value.middleName="";
												 }
												 	$scope.dataListFinal.push(value);
												 }
											}
											}
										});
											
										if($scope.mobileFlag){
											growl.error("Please enter valid mobile number",{
												title : 'error !'
												});
												$('#mobileNo').focus();
												return false;
										}
									}
										 $scope.employee.dependantsDetailsList=$scope.dataListFinal;
									$rootScope.startLoader();
								 var URI = BASE_URL + ROOT_URL+ "/api/global/updateCorporateEmployeeMaster";
								 GenericService.serviceAction("POST", URI, $scope.employee).then(
												function(response) {
													if (response.data.status == "success") {
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});														
														$scope.init();
														 $scope.searchEmployee();
													} else {
														growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
														$scope.init();
													}
													$scope.saveBtnFlag = true;
													$scope.updateBtnFlag = false;
													 $scope.employeeListDetails=[];
													$rootScope.stopLoader();
													$('#addEmployeeDetailsModal').modal('hide');
												});
							 		}
							 
							 $scope.setPostCode = function(areaId,flag){
									if(flag==0){
									angular.forEach($scope.areaList,function(value,key){
												
												if(value.areaId==areaId)
												{
													$scope.CompanyObj.zipcode= value.postCode;
												}
									});
									}else{
										angular.forEach($scope.areaList,function(value,key){
											
											if(value.areaId==areaId)
											{
												$scope.employee.zipCode= value.postCode;
											}
								});
									}
									
								 }
							 
							 $scope.addEmployeeDetailsModal = function(corporateEmployeeId){
								 if(corporateEmployeeId==0){
									 $scope.init();
									 $scope.saveBtnFlag = true;
									$scope.updateBtnFlag = false;
									 $('#addEmployeeDetailsModal').modal('show');
								 }else{
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
									 $scope.showUpdateBtnCorporateEmp(corporateEmployeeId);
								 }
							 }

						} ]);
