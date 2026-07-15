'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:insuranceMasterController
 * @description #insuranceMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'insuranceMasterController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'GenericServiceFileUpload',
						'growl',
						'$state',
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,GenericServiceFileUpload, growl,$state,PagerService) {
							

							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
							$state.go('login');
							return;
							}

							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							   
							$rootScope.loginpage = true;
							$scope.admissiontype = {}
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							
							// for popup
							$scope.showModal = false;
							$scope.buttonClicked = "";
							$scope.popUpFlag = true;
							$rootScope.getDefaultRegistrationValidationDetails();
							$scope.insuranceObj={
									insuranceId:"",
									insuranceCode:"",
									insuranceDesc:"",
									startDate:"",
									endDate:"",
									address:"",
									countryId:"",
									stateId:"",
									districtId:"",
									cityId:"",
									areaId:"",
									zipCode:"",
									contactNo:"",
									faxNo:"",
									email:"",
									contactPerson:"",
                                    blockReportPrint:"",
//									reportPath:""
									
								}
							$scope.init=function(){
								$scope.insuranceObj={
										insuranceId:"",
										insuranceCode:"",
										insuranceDesc:"",
										startDate:"",
										endDate:"",
										address:"",
										countryId:"",
										stateId:"",
										districtId:"",
										cityId:"",
										areaId:"",
										zipCode:"",
										contactNo:"",
										faxNo:"",
										email:"",
										contactPerson:"",
	                                    blockReportPrint:"",
//										reportPath:""
										
									}
							}
							
							$scope.searchKeyword = "";
							
							$scope.dateObj=function(){
								// for From date picker
								$scope.dateOptionsForStartDate = {
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
								$scope.dateOptionsForToDate =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
									
								};
							}
							$scope.dateObj();
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.insuranceObj.startDate.getTime();
								
								/*if(moment($scope.insuranceObj.startDate).isBefore(moment($scope.insuranceObj.endDate)))
								{
									$scope.insuranceObj.endDate=new Date($scope.insuranceObj.startDate.getTime());
								}*/
							}
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
								if(countryId!="" && countryId!=undefined){
								$rootScope.startLoader();
								GenericService.serviceAction("GET", URI, data)
										.then(function(response) {
													$scope.stateList = [];
													if (response.data.status == "success") {
														$scope.stateList = response.data.listObject;
														console.log("stateList",$scope.stateList);
														$rootScope.stopLoader();
													}
												});
								
								}
							}

							/* getActiveDistrictList() function for getting  active district list start*/
							$scope.getActiveDistrictListById = function(stateId) {
								if(stateId!="" && stateId!=undefined){
									$rootScope.startLoader();
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
														$rootScope.stopLoader();
													}
												});
								}
							}
							/* getActiveCityList() function for getting  active city list start*/
							$scope.getActiveCityListById = function(districtId) {
								if(districtId!="" && districtId!=undefined){
									$rootScope.startLoader();
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
														$rootScope.stopLoader();
													}
												});
								}
							}
							/* getAreaList() function for getting  active area list start*/
							$scope.getAreaListById = function(cityId) {
								if(cityId!="" && cityId!=undefined){
									$rootScope.startLoader();
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
														$rootScope.stopLoader();
													}
												});
								}
							}
//							return false;
							
							
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initInsuranceList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initInsuranceList = function(orgId,
									offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL+"/api/unit/billing/getInsuranceMasterList";
								var URI1 = BASE_URL + ROOT_URL+ "/api/unit/billing/getInsuranceMasterCount";

								var data = {
									orgId : orgId,
									unitId:unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								console.log("data", data);

								var data1 = {
									unitId:$scope.unitId,
									orgId : $scope.organizationId
								}

								// For Get designation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.insuranceList = [];

													if (response.data.status == "success") {
														$scope.insuranceList = response.data.listObject;
														angular.forEach($scope.insuranceList, function(value1 , key) {
														angular.forEach($scope.ledgerList, function(value , key) {
												            if(value1.generalLedgerId==value.generalLedgerId)
												                {
												            	value1.generalLedgerName=value.generalLedgerName;
												            	value1.generalLedgerId=value.generalLedgerId;
												                }
												        });
													})
														console.log("insuranceList",$scope.insuranceList);
													}
												});

								// For Count designation List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log(response.data);
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);

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
							$scope.getInsuranceList = function(orgId,offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL+"/api/unit/billing/getInsuranceMasterList";
								var data = {
									orgId : $scope.organizationId,
									unitId:$scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								// For Get designation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.insuranceList = [];

													if (response.data.status == "success") {
														$scope.insuranceList = response.data.listObject;														
													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage,$scope.unitId);
								if (flag) {
									$scope.getInsuranceList($scope.organizationId,$scope.pager.startIndex,$scope.pager.pageSize,$scope.unitId);
								}
							}
							$scope.initInsuranceList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End
							
							//for setting value to address details start
							$scope.addressObj={};
							$scope.getAdressDetails=function(data){
								$scope.addressObj=data;
								console.log("address details",$scope.addressObj);
							}
							//for setting value to address details End
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.save();
							}

							$scope.save = function() {
								
								if($scope.insuranceObj.insuranceCode == "" || $scope.insuranceObj.insuranceCode == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Insurance Code ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.insuranceDesc==""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Insurance Description ",
											{
												title : 'Warning!'
											});
									return false;
								}
								

								/*if($scope.insuranceObj.startDate == "" || $scope.insuranceObj.startDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select StartDate ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.endDate == "" || $scope.insuranceObj.endDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select endDate ",
											{
												title : 'Warning!'
											});
									return false;
								}
							
								if($scope.insuranceObj.address == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Address ",
											{	
												title : 'Warning!'
											});
									return false;
								}*/
								
								if($scope.insuranceObj.countryId == "" || isNaN($scope.insuranceObj.countryId)){
									$rootScope.stopLoader();
									growl.warning("Please Select Country",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.stateId == "" || isNaN($scope.insuranceObj.stateId)){
									$rootScope.stopLoader();
									growl.warning("Please Select State ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.districtId == "" || isNaN($scope.insuranceObj.districtId)){
									$rootScope.stopLoader();
									growl.warning("Please Select District ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.cityId == "" || isNaN($scope.insuranceObj.cityId) ){
									$rootScope.stopLoader();
									growl.warning("Please Select City ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.areaId == "" || isNaN($scope.insuranceObj.areaId) ){
									$rootScope.stopLoader();
									growl.warning("Please Select Area ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.zipCode == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter zipcode ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								
								
								/*if($scope.insuranceObj.contactNo == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Contact No ",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
								
								
								/*if($scope.insuranceObj.faxNo == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Fax No",
											{
												title : 'Warning!'
											});
									return false;
								}
*/								
								if($scope.insuranceObj.email == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Email",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								
								/*if($scope.insuranceObj.contactPerson == ""){
									$rootScope.stopLoader();
									growl.warning("Please Enter Contact Person Name",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
				           		 $scope.$broadcast('show-errors-check-validity');
								if ($scope.insuranceMasterForm.$valid) {
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {

										//fax no Validation
										if ($scope.insuranceObj.faxNo == undefined
												&& $scope.insuranceObj.faxNo !== "") {
											var filter1 = /^\+?[0-9]{6,}$/;
											if ($scope.insuranceObj.faxNo != "") {
												var faxNo = $scope.insuranceObj.faxNo;
												if (!filter1.test(faxNo)) {
													growl
															.error(
																	"Invalid Fax Number",
																	{
																		title : ERROR_MSG
																	});
													$('#faxNo').focus();
													return false;
												}
											}
										}
										//mobile no validation
										var mobileNo=$scope.insuranceObj.contactNo;
										if (mobileNo == undefined && mobileNo !== "") {
											if (mobileNo.length == parseInt($rootScope.registrationFieldObj.mobileDigit)) {

											} else {
												growl
														.error(
																"Please enter valid mobile number",
																{
																	title : 'error !'
																});
												$('#mobileNo').focus();
												return false;
											}
										}
										//email validation
										 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
						           		 var email = $scope.insuranceObj.email;
						           		    if (!filter.test(email)) {
					        				    growl.error(EMAIL_MSG,{
													title : 'error'
												});
					        				    return false;
						           		    }
						           		    
						           		 if($scope.insuranceImage!='' && $scope.insuranceImage!=null && $scope.insuranceImage!=undefined && !angular.equals({}, $scope.insuranceImage)){
													           		
											 
										 var ext = $scope.insuranceImage.name.split(".").pop().toLowerCase();
											if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
												growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												return false;
											}
						           		 }
										
										$scope.currentDate=new Date();
										$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
										     
												$scope.insuranceObj.createdDate=$scope.currentDate,
												$scope.insuranceObj.updatedDate=$scope.currentDate,
												/*$scope.insuranceObj.startDate=moment($scope.insuranceObj.startDate).format('DD-MM-YYYY HH:mm:ss');
												$scope.insuranceObj.endDate=moment($scope.insuranceObj.endDate).format('DD-MM-YYYY HH:mm:ss');*/
												$scope.insuranceObj.status = 'A',
												$scope.insuranceObj.createdBy=$scope.userId,
												$scope.insuranceObj.updatedBy=$scope.userId,
												$scope.insuranceObj.orgId=$scope.organizationId,
												$scope.insuranceObj.unitId=$scope.unitId,
//												$scope.insuranceImage=$scope.insuranceObj.reportPath,
//												$scope.insuranceObj.reportPath='';
												console.log("save",$scope.insuranceObj);// return false;
												$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+"/api/unit/billing/saveInsuranceMaster";
								GenericService.serviceAction("POST", URI, $scope.insuranceObj)
										.then(function(response) {
													if (response.data.status == "success") {
														if($scope.insuranceImage!='' && $scope.insuranceImage!=null && $scope.insuranceImage!=undefined && !angular.equals({}, $scope.insuranceImage)){
															$scope.uploadPatientImage($scope.insuranceImage,response.data.object.insuranceId,$scope.organizationId,$scope.unitId);
														}
														else
															{															
																$scope.init();
																$scope.initInsuranceList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
															}
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
//													$scope.initInsuranceList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
												});
									}
									}
								

							}
                            $scope.ledgerNameSearch = function(keyword)
                            {
                            	$scope.insuranceObj.generalLedgerId=null;
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
                                $scope.insuranceObj.generalLedgerId=ledger.generalLedgerId;
                            }
                            $scope.clearText = function()
						    {
						    	if($scope.insuranceObj.generalLedgerId==undefined || $scope.insuranceObj.generalLedgerId==null || $scope.insuranceObj.generalLedgerId==0)
						    		{
						    		$scope.insuranceObj.generalLedgerName="";
						    		}
						    }
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

								}
								console.log(data);
								var URI = BASE_URL + ROOT_URL+ "/api/unit/billing/getInsuranceMasterById/"+id+"/"+ $scope.organizationId+"/"+$scope.unitId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.insuranceObj=response.data.listObject[0];
														$scope.insuranceObj.startDate = new Date($scope.insuranceObj.startDate);
														$scope.insuranceObj.endDate = new Date($scope.insuranceObj.endDate);
														$scope.insuranceObj.countryId =response.data.listObject[0].countryId.toString();
														$scope.getActiveStateListByCountryId($scope.insuranceObj.countryId);
														$scope.insuranceObj.stateId =response.data.listObject[0].stateId.toString();
														$scope.getActiveDistrictListById ($scope.insuranceObj.stateId);
														$scope.insuranceObj.districtId =response.data.listObject[0].districtId.toString();
														$scope.getActiveCityListById($scope.insuranceObj.districtId);
														$scope.insuranceObj.cityId =response.data.listObject[0].cityId.toString();
														$scope.getAreaListById($scope.insuranceObj.cityId);
														$scope.insuranceObj.areaId =response.data.listObject[0].areaId.toString();
														$scope.insuranceObj.blockReportPrint =response.data.listObject[0].blockReportPrint;
												        angular.forEach($scope.ledgerList, function(value , key) {
												            if(response.data.listObject[0].generalLedgerId==value.generalLedgerId)
												                {
												            	$scope.insuranceObj.generalLedgerName=value.generalLedgerName;
												            	$scope.insuranceObj.generalLedgerId=value.generalLedgerId;
												                }
												        });
														
														console.log("pencil click",$scope.insuranceObj);
													} else {
														alert("Error!!");
													}
												});

							};						

							$scope.updateStatus = function(id, type) {

								$scope.currentDate=new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								var data = {
												insuranceId : id,
												status : type,
												updatedBy:$scope.userId,
												updatedDate:$scope.currentDate
								      		}
								console.log("update status",data);
								var URI = BASE_URL + ROOT_URL+ "/api/unit/billing/updateInsuranceMasterStatus";
								GenericService.serviceAction("POST", URI, data)
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
//								$scope.$broadcast('show-errors-check-validity');
								
								if($scope.insuranceObj.insuranceCode == "" || $scope.insuranceObj.insuranceCode == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Insurance Code ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.insuranceDesc == "" || $scope.insuranceObj.insuranceDesc == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Insurance Description ",
											{
												title : 'Warning!'
											});
									return false;
								}
								

								/*if($scope.insuranceObj.startDate == "" || $scope.insuranceObj.startDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select StartDate ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.endDate == "" || $scope.insuranceObj.endDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select endDate ",
											{
												title : 'Warning!'
											});
									return false;
								}
							
								if($scope.insuranceObj.address == ""  || $scope.insuranceObj.address == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Address ",
											{	
												title : 'Warning!'
											});
									return false;
								}*/
								
								if($scope.insuranceObj.countryId == "" || isNaN($scope.insuranceObj.countryId) || $scope.insuranceObj.countryId == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select Country",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.stateId == "" || isNaN($scope.insuranceObj.stateId) || $scope.insuranceObj.stateId == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select State ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.districtId == "" || isNaN($scope.insuranceObj.districtId) || $scope.insuranceObj.districtId == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select District ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.cityId == "" || isNaN($scope.insuranceObj.cityId) || $scope.insuranceObj.cityId == undefined ){
									$rootScope.stopLoader();
									growl.warning("Please Select City ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.areaId == "" || isNaN($scope.insuranceObj.areaId) || $scope.insuranceObj.areaId == undefined ){
									$rootScope.stopLoader();
									growl.warning("Please Select Area ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								if($scope.insuranceObj.zipCode == "" || $scope.insuranceObj.zipCode == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter zipCode ",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								
								
								/*if($scope.insuranceObj.contactNo == "" || $scope.insuranceObj.contactNo == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Contact No ",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
								
								
								/*if($scope.insuranceObj.faxNo == "" || $scope.insuranceObj.faxNo == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Fax No",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
								if($scope.insuranceObj.email == "" || $scope.insuranceObj.email == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Email",
											{
												title : 'Warning!'
											});
									return false;
								}
								
								
								/*if($scope.insuranceObj.contactPerson == "" || $scope.insuranceObj.contactPerson == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Enter Contact Person Name",
											{
												title : 'Warning!'
											});
									return false;
								}*/
								
								
								//email validation
								 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				           		 var email = $scope.insuranceObj.email;
				           		    if (!filter.test(email)) {
			        				    growl.error(EMAIL_MSG,{
											title : 'error'
										});
			        				    return false;
				           		    }
				           		    
				        
				           		//fax no validation	
								if ($scope.insuranceObj.faxNo == undefined
										&& $scope.insuranceObj.faxNo !== "") {
									var filter1 = /^\+?[0-9]{6,}$/;
									if ($scope.insuranceObj.faxNo != "") {
										var faxNo = $scope.insuranceObj.faxNo;
										if (!filter1.test(faxNo)) {
											growl.error("Invalid Fax Number", {
												title : ERROR_MSG
											});
											$('#faxNo').focus();
											return false;
										}
									}
								}
								
										$scope.currentDate=new Date();
										$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								
										/*$scope.insuranceObj.startDate=moment($scope.insuranceObj.startDate).format('DD-MM-YYYY HH:mm:ss');
										$scope.insuranceObj.endDate=moment($scope.insuranceObj.endDate).format('DD-MM-YYYY HH:mm:ss');*/
										$scope.insuranceObj.updatedDate=$scope.currentDate,
										$scope.insuranceObj.updatedBy=$scope.userId,
										$scope.insuranceObj.orgId=$scope.organizationId,
										$scope.insuranceObj.unitId=$scope.unitId,
//										$scope.insuranceImage=$scope.insuranceObj.reportPath,
//										$scope.insuranceObj.reportPath='';
										console.log("update",$scope.insuranceObj); 
										
						
										if ($scope.insuranceMasterForm.$valid) {
											var mobileNo=$scope.insuranceObj.contactNo;
											if (mobileNo != undefined && mobileNo != "") {
												if (mobileNo.length == parseInt($rootScope.registrationFieldObj.mobileDigit)) {
		
												} else {
													growl
															.error(
																	"Please enter valid mobile number",
																	{
																		title : 'error'
																	});
													$('#mobileNo').focus();
													return false;
												}
											}
											//email validation
											 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
							           		 var email = $scope.insuranceObj.email;
							           		    if (!filter.test(email)) {
						        				    growl.error(EMAIL_MSG,{
														title : 'error'
													});
						        				    return false;
							           		    }
							           		    //file validation
							           		 if($scope.insuranceImage!='' && $scope.insuranceImage!=null && $scope.insuranceImage!=undefined && !angular.equals({}, $scope.insuranceImage)){
														           		
												 
											 var ext = $scope.insuranceImage.name.split(".").pop().toLowerCase();
												if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
													growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
															{
																title : 'warning!'
															});
													$rootScope.stopLoader();
													return false;
												}
							           		 }
							           		 
											$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/api/unit/billing/updateInsuranceMaster";
								GenericService.serviceAction("POST", URI, $scope.insuranceObj).then(
												function(response) {
													if (response.data.status == "success") {
														if($scope.insuranceImage!='' && $scope.insuranceImage!=null && $scope.insuranceImage!=undefined && !angular.equals({}, $scope.insuranceImage)){
															var status=$scope.uploadPatientImage($scope.insuranceImage,response.data.object,$scope.organizationId,$scope.unitId);
														}
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
														$scope.initInsuranceList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
														$rootScope.stopLoader();

												});
								}
							}
							//getting grade details for insurance 
							$scope.getGreadDetails=function(insuranceId){
								var data ={
										insuranceId:insuranceId
								}
								console.log("data",data);
								$scope.mappedGradeList=[];
								var URI = BASE_URL + ROOT_URL+ "/api/unit/billing/getMappedGradeToInsuerance";
								GenericService.serviceAction("POST", URI, data).then(
												function(response) {
													if (response.data.status == "success") {
															 $scope.mappedGradeList=response.data.listObject;
															 console.log("mappedGradeList",$scope.mappedGradeList);
														} else {
															alert("Error!!");
														}

												});
							}

							// for uploading document
							$scope.uploadPatientImage = function(inputFile,insuranceId,orgId,unitId)
						    {
						    	    var file = inputFile;
							        var fd = new FormData();
							        fd.append('file', inputFile);
							        
							     
									var URI1 = BASE_URL + ROOT_URL + "/api/unit/billing/insurance/image/"+orgId+"/"+unitId+"/"+insuranceId;
									GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											$scope.insuranceImage="";
											 angular.element("input[type='file']").val(null);
											 $scope.initInsuranceList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
											$rootScope.stopLoader();
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
										}

									});
									
						    }
							
							 $scope.openInsuranaceImage = function(documentPath)
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
							 
							 $scope.setPostCode = function(areaId){
									
									angular.forEach($scope.areaList,function(value,key){
												
												if(value.areaId==areaId)
												{
													$scope.insuranceObj.zipCode = value.postCode;
												}
									});
								 }
							 
						} ]);
