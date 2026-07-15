
angular.module('myApp').controller(
		'hospitalMasterController',
		[
				'$scope',
				'$http',
				'$localStorage',
				'$sessionStorage',
				'$cookies',
				'$rootScope',
				'fileReader',
				'GenericService',
				'GenericServiceFileUpload',
				'PagerService',
				'promiseFactory',
				'growl',
				'BillingGenericService',
				function($scope, $http, $localStorage, $sessionStorage,
						$cookies, $rootScope,fileReader,GenericService,GenericServiceFileUpload,PagerService,promiseFactory,growl,BillingGenericService) {
					$rootScope.loginpage = true;
					
					$scope.imageSrc = "";
				    
				    $scope.$on("fileProgress", function(e, progress) {
				      $scope.progress = progress.loaded / progress.total;
				    });
				    
				    
				    $scope.dateOptions = {
							formatYear : 'yyyy',
							showWeeks : false,
							minDate : new Date()
						};
					/*$scope.openDatePicker = function() {
						$scope.datepicker.opened = true;
					};

					$scope.datepicker = {
						opened : false
					};*/
				    
					
					$scope.openDatePicker = function(index) {

						for (var i = $scope.listLicenceDetailsMaster.length - 1; i >= 0; i--) {
							if (i === index) {

								$scope.expiryDate.opened[i] = true;
							} else {
								$scope.expiryDate.opened[i] = false;
							}
						}
					};

					$scope.expiryDate = {
						opened : [ false ],

					};
					
				    //Declaration
				    $scope.licenceTypeList;
				    
				    $rootScope.loginpage = true;
					$scope.LISDynamicLabel = "";
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.updateScope = false;
					$scope.subscriConfigForm;
					$scope.mannualSubscriptionForm;
					
					var cookieObject = $cookies.getObject('cookieObject');
					if(cookieObject == undefined){
					$state.go('login');
					return;
					}

					$scope.unitId = cookieObject.unitId;
					$scope.orgId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					
					console.log("User Id is:"+$scope.userId);
					
				    $scope.listLicenceDetailsMaster = [];
				    
				   
				    /**Variables for pagination */
					$scope.offset = 0;
					$scope.noOfRecordsPerPage = 10;
					$scope.unitMasterList = [];
					$scope.totalNoOfRecords;
					$scope.searchKeyword;
					$scope.unitCount;
					$scope.createdBy =$scope.userId ;
					$scope.updatedBy =$scope.userId ;
					$scope.numberToDisplay = 10;
					
					$rootScope.getDefaultRegistrationValidationDetails();
				    
				    $scope.addLicenceDetails = function(){
				    	console.log($scope.licenceDetailsMaster);
				    	if($scope.updateScope ==false)
	        	        {
				    	  var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
						  $scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
						  console.log("$scope.listLicenceDetailsMaster",JSON.stringify($scope.listLicenceDetailsMaster));
	        	       }
				      else if($scope.updateScope ==true)
			        	{
				    	  var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
				    	  licenceDetailsMaster.unitId = $scope.unit.unitId;
						  $scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
						  $scope.listLicenceDetailsMaster.splice(deleteIndex, 1);
						  console.log($scope.listLicenceDetailsMaster);
			        	}	
				    }
				    
				    
				    
				    $scope.removeLicenceDetails = function(deleteIndex)
					{
						$scope.listLicenceDetailsMaster.splice(deleteIndex, 1);
				     }
				    
				    
				    $scope.initOrganizationMaster = function(){
				
			        	$scope.licenceDetailsMaster={
			        			"licenceTypeId":"",
			        			"licenceNumber":"",
			        			"expiryDate":""
			        	}
			        	
					$scope.unit =
					{
			        	 "unitId" :"",		
						 "organizationId":"",
						 "unitCode":"",
						 "unitName":"",
						 "status":"A",
						 "unitDesc":"",
						 "createdBy":$scope.createdBy,
						 "createdDate": moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						 "updatedBy":$scope.updatedBy,
						 "updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					     "unitAddress":"",
					     "unitCountryId":"",
					     "unitDistrictId":"",
					     "unitAreaId":"",
					     "unitStateId":"",
					     "unitCityId":"",
					     "postCode":"",
					     "unitContact":"",
					     "registrationNumber":"",
					    // "organizationFaxNo":"",
					     "unitEmailId":"",
					     "unitLogo":"",
					     "areaName":"",
					     /*"listOrganizationUnitLicenceDetailsDto":""*/
					 }
			        	
			        	$scope.listLicenceDetailsMaster=[];
						var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
			        	$scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
				
			        	$http.get('resources/assets/adminPriviledges.json').success(function (data){
							//$scope.unit.preivilageList = data;
							$scope.unit.previlageString = JSON.stringify(data);
							//console.log($scope.employees);
						});
			        	
			        	
					}
				    
				    $scope.initOrganizationMaster();
				    
				    //get Licence Type list
				    $scope.getLicenceTypeList = function(orgId) {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL
									+ "/org/getLiceneceTypeMasterList" + S
									+ orgId;
							console.log("URI", URI);
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.licenceTypeList = response.data.listObject;
											});
						} catch (e) {
							console.log(e.message)
						}
					}
				    
				    $scope.getLicenceTypeList($scope.orgId);
				    
				    
				    
				    $scope.getSuperAdminUser = function() {
//						try {
//							var data = "";
////							var URI = BASE_URL + ROOT_URL
////									+ "/getSuperAdminUser/" + S
////									+ $scope.orgId  + "/"+S+$scope.unitId+ "/"+S+$scope.userId;
//							
//							var URI = BASE_URL + ROOT_URL
//							//+ "/adt/getSuperAdminUser/"+$scope.orgId+ "/"+$scope.unitId+ "/"+$scope.userId;
//							+GET_SUPERADMIN_DETAILS+"/"+$scope.orgId+ "/"+$scope.unitId+ "/"+$scope.userId;
//							console.log("URI", URI);
//							GenericService
//									.serviceAction("GET", URI, data)
//									.then(
//											function(response) {
//												$scope.isSuperAdmin = response.data.listObject;
//											});

					try {
						var data = "";
						var URI = BASE_URL + ROOT_URL
						+"/global/getSuperAdminUser"+"/"+$scope.orgId+ "/"+$scope.unitId+ "/"+$scope.userId;
						console.log("URI", URI);
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.isSuperAdmin = response.data.object.isSuperUser;
											console.log("SUper Admin is::::"+$scope.isSuperAdmin);
										});
					} catch (e) {
						console.log(e.message)
					}
					}
				    
				    $scope.getSuperAdminUser();
				    
				    
				    
				    $scope.getTimezones = function() 
				    {
						try {
							var URI = BASE_URL + ROOT_URL + "/timezones"
							GenericService
									.serviceAction("GET", URI, "")
									.then(
											function(response) {
												$scope.timezoneList = response.data.listObject;
											});
						} catch (e) {
							console.log(e.message)
						}
					}
				    $scope.getTimezones();				    
				    //get country master list
				    
				    $scope.getCountryMasterList = function() {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVECOUNTRYLIST +$scope.orgId;
							console.log("URI", URI);
							GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.countryList = response.data.listObject;
											});
						} catch (e) {
							console.log(e.message)
						}
					}
				    
				    $scope.getCountryMasterList();
				    
				    $scope.getStateMasterList = function(countryId) {
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL
									+ "/adt//getStateByCountryId/"+countryId;
							console.log("URI", URI);
							GenericService.serviceAction("GET", URI,data)
									.then(
											function(response) {
												$scope.stateList = response.data.listObject;
											});
						} catch (e) {
							console.log(e.message)
						}
					}
				    
				    $scope.getDistrictByState = function(id) {
						var data = {
							stateId : id
						};
						var URI = BASE_URL + ROOT_URL
								+ GETDISTRICTLISTBYSTATEID;
						BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									$scope.districtList = [];
									if (response.data.status == "success")
									{
										$scope.districtList = response.data.listObject;
									}
								});
						

					}
				    
				    $scope.getCityByDistrict = function(id) {

						var data = {
							districtId : id
						};
						var URI = BASE_URL + ROOT_URL
								+ GETCITYLISTBYDISTRICTID;
						BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									$scope.cityList = [];
									if (response.data.status == "success")
									{
										 $scope.cityList = response.data.listObject;
									}
								});

					}
				    
				    $scope.amountPaid = false;
				    
				    $scope.handleCheckboxChange = function() {
				      console.log("Amount Paid:", $scope.amountPaid);
				      $scope.amountPaid=$scope.amountPaid;
				      };
				    
				    $scope.saveSubscriptionDetails = function() {
				    	if($scope.subscriptionType==null || $scope.subscriptionType=='' || $scope.subscriptionType==undefined){
				        	 growl.warning(
									 	'Please Select Subscription Type',
									 	{
									 		title : 'warning!'
									 	});
									 return;
						    }
				    	if($scope.startDate==null || $scope.startDate=='' || $scope.startDate==undefined){
				        	 growl.warning(
									 	'Please Select Start Date',
									 	{
									 		title : 'warning!'
									 	});
									 return;
						    }
				    	
				    	if($scope.endDate==null || $scope.endDate=='' || $scope.endDate==undefined){
				        	 growl.warning(
									 	'Please Select Start Date',
									 	{
									 		title : 'warning!'
									 	});
									 return;
						    }
				    	
						var data = {
							 "subscriptionStartDate" : $scope.startDate,

						 "subscriptionEndDate" :$scope.endDate,
						 
						 "unitId" :$scope.unit.unitId,
						 
						 "organizationId" :$scope.unit.organizationId,

						 "subscriptionType" :$scope.subscriptionType,

						 "amountPaid" : $scope.amountPaid
						
						};
						
						var URI = BASE_URL + ROOT_URL
								+ SAVE_SUBSCRIPTION_DETAILS;
						BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									
									if (response.data.status == "success")
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										 //$scope.cityList = response.data.listObject;
									}
								});
						document.getElementById("subscriptionDetails").style.display = "none";
						$scope.amountPaid=false;
						 $scope.startDate='',
					     $scope.endDate='',
						$rootScope.stopLoader();
					}
				    
				    
				    
				    
				    
				    
				    
					/*$scope.getAreaByCity = function(id) {

						var data = {
							cityId : id
						};
						var URI = BASE_URL + ROOT_URL
								+ GETAREALISTBYCITY;
						BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									$scope.areaList = [];
									if (response.data.status == "success")
									{
										 $scope.areaList = response.data.listObject;
										
									}
								});

					}*/
				   

				    	 
				    	 
				    	 
				    	 
				    	 $scope.formatDateString = function (input) {
				    		    var dateObj = new Date(input);

				    		    // Get date components
				    		    var day = ('0' + dateObj.getDate()).slice(-2);
				    		    var month = ('0' + (dateObj.getMonth() + 1)).slice(-2); // Months are zero-based
				    		    var year = dateObj.getFullYear().toString();

				    		    // Get time components
				    		    var hours = ('0' + dateObj.getHours()).slice(-2);
				    		    var minutes = ('0' + dateObj.getMinutes()).slice(-2);
				    		    var seconds = ('0' + dateObj.getSeconds()).slice(-2);

				    		    // Combine date and time components
				    		    var formattedDate = day + '/' + month + '/' + year;
				    		    var formattedTime = hours + ':' + minutes + ':' + seconds;

				    		    return formattedDate;
				    		};


				    	 
				    	 

				    		

				    				    
				    $scope.logEvents = [];
				    $scope.getAreaByCity = function(id) {
                           $scope.idd = id;
						var data = {
								offset:0,
								noOfRecordsPerPage:10,
							    cityId : id
						};
						
						
						var URI = BASE_URL + ROOT_URL
								+ "/adt/getAreaByCityIdPagination";
						BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									$scope.logEvents = [];
									if (response.data.status == "success")
									{
										$scope.areaList = [];
										$scope.logEvents1 = []
										 $scope.logEvents1 = response.data.listObject;
										 if($scope.savedAreaId != null && $scope.savedAreaId != ''){
											  var found = $scope.logEvents1.find(ar=>ar.areaId == $scope.unit.unitAreaId);
											  var foundvv = $scope.logEvents1.find(ar=>ar.areaId != $scope.unit.unitAreaId);
											  if(found == undefined){
												    foundvv.areaId =$scope.savedAreaId;
												    foundvv.areaName=$scope.savedAreaName;
													  $scope.logEvents1.push(foundvv); 
											  }
											 }
										 $scope.logEvents = $scope.logEvents1;
										 console.log($scope.logEvents);
									}
								});

					}
				    $scope.initMasterList = function(offset, noOfRecordsPerPage)
					{
						$rootScope.startLoader();
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
										var data = {
												offset:offset,
												noOfRecordsPerPage:noOfRecordsPerPage,
											    cityId : $scope.idd
										};
										var URI = BASE_URL + ROOT_URL
												+  "/adt/getAreaByCityIdPagination";
										BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$scope.areaList = [];
													if (response.data.status == "success")
													{
														$scope.logEvents = [];
														$scope.logEvents1 = [];
														 $scope.logEvents1 = response.data.listObject;
														 if($scope.savedAreaId != null && $scope.savedAreaId != ''){
															  var found = $scope.logEvents1.find(ar=>ar.areaId == $scope.unit.unitAreaId);
															  var foundvv = $scope.logEvents1.find(ar=>ar.areaId != $scope.unit.unitAreaId);
															  if(found == undefined){
																    foundvv.areaId =$scope.savedAreaId;
																    foundvv.areaName=$scope.savedAreaName;
																	  $scope.logEvents1.push(foundvv); 
															  }
															 }
														 $scope.logEvents = $scope.logEvents1;
														 console.log($scope.logEvents);
													}
												});

					}	
				    
				    
				    $scope.loadMore1 = function()
				       {
				    	   $scope.numberToDisplay += 5;
				           $scope.initMasterList($scope.offset,$scope.numberToDisplay);
				       }
				    
				    $scope.getOrganizationList = function(){
				    	try{			    		
				    		var data ="";
				    		var URI = BASE_URL + ROOT_URL +"/organization/active";
				    		console.log("URI",URI);
				    		GenericService.serviceAction("GET",URI,data)
				    		.then(
				    				function(response){
				    					$scope.organizationMasterList = response.data.listObject;	
				    					console.log("organizationMasterList",$scope.organizationMasterList);
				    				})
				    	}catch(e){
				    		console.log("Exception",e);
				    	}
				    }
				    $scope.getOrganizationList();
				    
				    
				    $scope.setTimeZoneOrganizationWise = function(id){
				    	angular.forEach($scope.organizationMasterList,function(value,ind){
				    		if(id == value.organizationId){
//				    			if( value.unitTimeZone != null){
				    				$scope.unit.unitTimeZone = value.unitTimeZone;
//				    			}
				    		}
				    	});
				    }
				    
				    /** Code for get list and count for organization **/
				    
				    $scope.setNoOfRecords = function() {
						$scope.initOrganizationList($scope.offset, $scope.noOfRecordsPerPage);
					};
					$scope.initOrganizationList = function(offset, noOfRecordsPerPage)
					{
						$rootScope.startLoader();
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI1 = BASE_URL + ROOT_URL + S +"unit"+ S + offset + S + noOfRecordsPerPage + S + $scope.orgId; 
						
						var URI2 = BASE_URL + ROOT_URL + "/unit/count/"+ $scope.orgId;//minesh
						promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
						{
							$rootScope.stopLoader();
							$scope.unitMasterList = response[0].listObject;
							$scope.unitCount = response[1].object;
							$scope.setPage(1, false);
							console.log($scope.unitMasterList);
							
						});
					}
					
					
				    $scope.getUnitMasterList = function(offset, noOfRecordsPerPage) {
						$rootScope.startLoader();
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + "/unit"+ S + offset + S + noOfRecordsPerPage + S + $scope.orgId;
						console.log("URI", URI);
						GenericService.serviceAction("GET", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.unitMasterList = response.data.listObject;
							console.log("list get ",$scope.unitMasterList);
						});
					}
				    
				    
				    $scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						if (page < 1 || page > $scope.pager.totalPages)
						{
							return;
						}
						$scope.pager = PagerService.GetPager($scope.unitCount, page, $scope.noOfRecordsPerPage);
						if (flag)
						{
							$scope.getUnitMasterList($scope.pager.startIndex, $scope.pager.pageSize);
						}
					}

					$scope.initOrganizationList($scope.offset, $scope.noOfRecordsPerPage);
				  
					/**Code End For List**/
				    
					
					/**Code Start For Crud**/
					
					$scope.updateStatus = function(unitId,status){
						try{
							$rootScope.startLoader();
							//$scope.initRackMaster();
							var URI = BASE_URL + ROOT_URL + "/unit/status"+S+unitId+S+status;
							console.log("ACT_INACT_URI",URI);
							GenericService.serviceAction("GET", URI, $scope.unit).then(function(response) 
							{
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
									$scope.$broadcast('show-errors-reset');
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
							});
						}catch(e){
							console.log(e);
						}
					}
					
					
					
					
				    $scope.saveUnit = function(){
				    	
				    	try 
						{
							$scope.$broadcast('show-errors-check-validity');
							if ($scope.hospitalMasterForm.$valid) 
							{
								$rootScope.startLoader();
								
								/*var mobileNo=$scope.unit.unitContact;
								
								if(mobileNo.length==parseInt($rootScope.registrationFieldObj.mobileDigit)){
				        			
								}else{
									growl.warning("Please enter valid mobile number",{
										title : 'Warning !'
										});
									    $rootScope.stopLoader();
										return false;
								}
								
								 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				           		 var email = $scope.unit.unitEmailId;
				           		    if (!filter.test(email)) {
			        				    growl.warning(EMAIL_MSG,{
											title : "Warning!"

										});
			        				    $rootScope.stopLoader();
			        				    return false;
				           		    }*/
								if($scope.myFile!=undefined &&  !angular.equals({}, $scope.myFile))
								   {
									     var ext = $scope.myFile.name.split(".").pop().toLowerCase();
											if($.inArray(ext, ["jpg","jpeg",'png']) == -1) {
												growl.warning('You can only upload *.jpg, *.jpeg, *.png',
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												return false;
											}
								   }
							
							var URI = BASE_URL + ROOT_URL + "/unit/save";
							GenericService.serviceAction("POST", URI,$scope.unit).then(function(response) 
							{	
								if (response.data.status == 'success')
								{
									if($scope.myFile!=undefined &&  !angular.equals({}, $scope.myFile))
									{
									var file = $scope.myFile;
							        var fd = new FormData();
							        fd.append('file', file );
									var URI1 = BASE_URL + ROOT_URL + "/unit/image/"+response.data.object.unitId;
									GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response1) 
									{	
										if (response1.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.initOrganizationMaster();
											$scope.initOrganizationList($scope.offset, $scope.noOfRecordsPerPage);
											
											$scope.$broadcast('show-errors-reset');
											$scope.init();
											
										} else if(response1.data.status == 'error')
										{
											$rootScope.stopLoader();
										}
									  });
									}
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
									$scope.initOrganizationMaster();
									$scope.initOrganizationList($scope.offset, $scope.noOfRecordsPerPage);
									$rootScope.stopLoader();
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
							/*}*/
							});
							}
						} catch (e) {
							console.log("Exception",e.message);
						}
				    }
				    
				    $scope.editMode = false;
				    
				    $scope.editUnit = function(unitId)
					{
						try {
							
							$rootScope.startLoader();
							$scope.saveBtnFlag = false;
							$scope.updateScope = true;
							$scope.updateBtnFlag = true;
							$scope.editMode = true;
//							$scope.listLicenceDetailsMaster = [];
							$scope.unit =
							{
					        	 "unitId" :"",		
								 "organizationId":"",
								 "unitCode":"",
								 "unitName":"",
								 "status":"A",
								 "unitDesc":"",
								 "createdBy":$scope.createdBy,
								 "createdDate": moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
								 "updatedBy":$scope.updatedBy,
								 "updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							     "unitAddress":"",
							     "unitCountryId":"",
							     "unitDistrictId":"",
							     "unitAreaId":"",
							     "unitStateId":"",
							     "unitCityId":"",
							     "postCode":"",
							     "unitContact":"",
							     "registrationNumber":"",
							    // "organizationFaxNo":"",
							     "unitEmailId":"",
							     "unitLogo":"",
							     "areaName":"",
							     /*"listOrganizationUnitLicenceDetailsDto":""*/
							 }
							var URI = BASE_URL + ROOT_URL +"/unit"+S+unitId;
							console.log("EDIT",URI);
							$scope.imageSrc='';
							GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
							{

								console.log("data",response.data);

								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
															
									//$scope.listLicenceDetailsMaster = [];
									/*if(response.data.object != null && response.data.object.unitAreaId != null){
										$scope.savedAreaId =response.data.object.unitAreaId;
										$scope.savedAreaName =response.data.object.areaName;
									}*/
									
									$scope.getStateMasterList(response.data.object.unitCountryId);
									$scope.getDistrictByState(response.data.object.unitStateId);
									$scope.getCityByDistrict(response.data.object.unitDistrictId);
								//	$scope.getAreaByCity(response.data.object.unitCityId);
									if(response.data.object.unitLogo!=null && response.data.object.unitLogo!=undefined && response.data.object.unitLogo!="")
									{
										$scope.imageSrc = response.data.object.unitLogo;
									}
									$scope.unit = response.data.object;			
									console.log("$scope.unit",$scope.unit);
//									$scope.unit.unitAreaId=response.data.object.unitAreaId;
									console.log("$scope.listLicenceDetailsMaster",$scope.listLicenceDetailsMaster);
									
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									alert("Error In Fetching Data");
								}
							});
						} catch (e) {
							console.log("Exception",e.message);
						}
						
					}
				    
				    
				    $scope.updateUnit = function(){
				    	try 
						{
				    		
				    		$scope.$broadcast('show-errors-check-validity');
				    		/*if($scope.listLicenceDetailsMaster!=null && $scope.listLicenceDetailsMaster!=undefined && $scope.listLicenceDetailsMaster.length>0 && $scope.listLicenceDetailsMaster != ""){
				    		$scope.unit.listOrganizationUnitLicenceDetailsDto= $scope.listLicenceDetailsMaster;
				    		}else{
				    			$scope.listLicenceDetailsMaster=[];
								var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
					        	$scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
				    		}*/
				    		//console.log("Length-",$scope.unit.listOrganizationUnitLicenceDetailsDto.length);	
							console.log("UnitMaster",$scope.unit);
							if ($scope.hospitalMasterForm.$valid)
							{
							var URI = BASE_URL + ROOT_URL + "/unit/update";
						
							
								var mobileNo=$scope.unit.unitContact;
								
								if(mobileNo.length==parseInt($rootScope.registrationFieldObj.mobileDigit)){
				        			
								}else{
									growl.warning("Please enter valid mobile number",{
										title : 'Warning !'
										});
										return false;
								}
								
								 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				           		 var email = $scope.unit.unitEmailId;
				           		    if (!filter.test(email)) {
			        				    growl.warning(EMAIL_MSG,{
											title : "Warning!"

										});
			        				    return false;
				           		    }
									if($scope.myFile!=undefined &&  !angular.equals({}, $scope.myFile))
									   {
										     var ext = $scope.myFile.name.split(".").pop().toLowerCase();
												if($.inArray(ext, ["jpg","jpeg",'png']) == -1) {
													growl.warning('You can only upload *.jpg, *.jpeg, *.png',
															{
																title : 'warning!'
															});
													$rootScope.stopLoader();
													return false;
												}
									   }
									
								
								
								console.log("URI",URI);
								$rootScope.startLoader();
								GenericService.serviceAction("POST", URI,$scope.unit).then(function(response) 
								{	
									if (response.data.status == 'success')
									{
										$scope.imageSrc='';
										if($scope.myFile!=undefined &&  !angular.equals({}, $scope.myFile))
										{
											var file = $scope.myFile;
									        var fd = new FormData();
									        fd.append('file', file );
	//										var URI1 = BASE_URL + ROOT_URL + "/unit/image/"+response.data.object.unitId;
									        var URI1 = BASE_URL + ROOT_URL + "/unit/image/"+$scope.unit.unitId;
											GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response1) 
											{	
												if (response1.data.status == 'success')
												{
													$rootScope.stopLoader();
													$scope.$broadcast('show-errors-reset');
													$scope.initOrganizationMaster();
													$scope.initOrganizationList($scope.offset, $scope.noOfRecordsPerPage);
												} else if(response1.data.status == 'error')
												{
													$rootScope.stopLoader();
												}
											  });
										}
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.saveBtnFlag = true;
										$scope.updateBtnFlag = false;
										$scope.updateScope = false;
										$scope.$broadcast('show-errors-reset');
										$scope.initOrganizationMaster();
										$scope.initOrganizationList($scope.offset, $scope.noOfRecordsPerPage);
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								
								});
							}
						} catch (e) {
							console.log("Exception",e.message);
						}
				    }

			 		$scope.licenceDetailsEdit = function(unitId){
			 			
			 			try{
			 				
			 				var data = {
			 	 					unitId : unitId
			 	 			}
			 				
			 				var URI = BASE_URL + ROOT_URL + "/licenceDetailsByOrganization";
			 				console.log("LicenceDetails",JSON.stringify(data));
			 				
			 				GenericService.serviceAction("POST", URI,data).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.listLicenceDetails = response.data.listObject;
											
											if($scope.updateBtnFlag == true){
											angular.forEach($scope.listLicenceDetails,function (value,key){
												var newArr ={};
												newArr.createdBy= value.createdBy,
												newArr.createdDate= value.createdDate,
												newArr.expiryDate= moment(value.expiryDate,"DD-MM-YYYY").toDate(),
												newArr.isUnit= value.isUnit,
												newArr.licenceDetailsId= value.licenceDetailsId,
												newArr.licenceNumber= value.licenceNumber,
												newArr.licenceType= value.licenceType,
												newArr.licenceTypeId= value.licenceTypeId,
												newArr.organizationId= value.organizationId,
												newArr.unitId= value.unitId,
												$scope.listLicenceDetailsMaster.push(newArr);
											});
											}
											
											console.log("$scope.listLicenceDetails",$scope.listLicenceDetails);
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									
									});
			 				
			 			}catch(e){
			 				console.log("Exception",e);
			 			}
			 		}	    
 
 		$scope.licenceDetails = function(unitId){
 			
 			try{
 				
 				var data = {
 	 					unitId : unitId
 	 			}
 				
 				var URI = BASE_URL + ROOT_URL + "/licenceDetailsByOrganization";
 				console.log("LicenceDetails",JSON.stringify(data));
 				
 				GenericService.serviceAction("POST", URI,data).then(function(response) 
						{	
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								$scope.listLicenceDetails = response.data.listObject;
								
								console.log("$scope.listLicenceDetails",$scope.listLicenceDetails);
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						
						});
 				
 			}catch(e){
 				console.log("Exception",e);
 			}
 		}
 		
 		
 		$scope.setPostCode = function(areaId){
			
			angular.forEach($scope.areaList,function(value,key){
				
				if(value.areaId==areaId)
				{
					$scope.unit.postCode = value.postCode;
				}
			});
			
		}
				    /**Code End For Crud**/
 		
 		$scope.addAreaById = function($item, $model, $label,flag){
	         	$scope.unit.unitAreaId = $item.areaId;
	         }
        
         
         
         $scope.searchAreaByCityId = function(searchKeyword){
				$scope.areaList=[];
				var serData = {
						cityId : $scope.unit.unitCityId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceAreaBysearchKeyword";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.areaList = response.data.listObject;
						if($scope.areaList.length == 0){
							if($scope.patientObj.area != ''& $scope.patientObj.area != null ){
								$scope.patientObj.areaName =$scope.patientObj.area;
							}else{
							$scope.patientObj.areaName =null;
							$scope.patientObj.areaId=null;
							$scope.patientObj.area =null;
							}
							growl.warning("No record found for serarch crateria please add new area",
								{
									title : 'warning!'
								});
						}
					}
					return $scope.areaList;
				});
			}

			//Subscription Model

                    $scope.subscriptionTypeList = [];
					$scope.getSubscriptionTypeList = ()=>{
						try{
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/unit/subcription/types";
							GenericService.serviceAction("GET", URI).then(function(response)
							{
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									$scope.subscriptionTypeList = response.data.listObject;
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
										{
											title : 'Error!'
										});
								}
							});
						}catch(e){
							console.log("Exception",e);
						}
					}
					$scope.getSubscriptionTypeList();

					$scope.subscriptionConfig ={};
					$scope.initSubscriptionConfig =()=>{
						$scope.subscriptionConfig = {subscriptionType:"",
							subscriptionAmount:null,
							subscriptionDiscount:null,
							subscriptionTax:null,
							subscriptionNetAmount:null,
							subscriptionConfigId:null
						}
					}




					$scope.getSubscriptionConfig = ()=>{
						try{
							$rootScope.startLoaderPopup();
							var data = {
								unitId : $scope.unit.unitId
							}
							var URI = BASE_URL + ROOT_URL + "/unit/subcription/config/details";
							GenericService.serviceAction("POST", URI,data).then((response) =>
							{
								if (response.data.status == 'success')
								{
									$rootScope.stopLoaderPopup();
									$scope.subscriptionConfig = response.data.object;
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoaderPopup();
									// growl.error(response.data.message,
									// 	{
									// 		title : 'Error!'
									// 	});
								}
							});
						}catch(e){
							console.log("Exception",e);
						}
					}


					$scope.createSubscriptionConfig = ()=>{
						try{
							$scope.$broadcast('show-errors-check-validity');
							if ($scope.subscriConfigForm.$valid) {
								$rootScope.startLoaderPopup();
								$scope.subscriptionConfig.unitId = $scope.unit.unitId;
								$scope.subscriptionConfig.organizationId = $scope.unit.organizationId;
								var URI = BASE_URL + ROOT_URL + "/unit/subcription/config";
								GenericService.serviceAction("POST", URI, $scope.subscriptionConfig).then((response) => {
									if (response.data.status == 'success') {
										$rootScope.stopLoaderPopup();
										$scope.subscriptionConfig = response.data.object;
										growl.success(response.data.message,
											{
												title: 'Success!'
											});
									} else if (response.data.status == 'error') {
										$rootScope.stopLoaderPopup();
										growl.error(response.data.message,
											{
												title: 'Error!'
											});
									}
								});
							}
						}catch(e){
							console.log("Exception",e);
						}
					}

					$scope.getSubscriptionHistory = ()=>{
						$rootScope.startLoaderPopup();
						$scope.subscriptionList=[];
						var data = {
							"unitId" :$scope.unit.unitId,
							"organizationId" :$scope.unit.organizationId
						};

						var URI = BASE_URL + ROOT_URL + "/unit/subcription/unitDetails";
						GenericService.serviceAction("POST", URI,data)
							.then(
								(response) =>{
									if (response.data.status == "success") {
										$rootScope.stopLoaderPopup();
										$scope.subscriptionList = response.data.listObject;
									 }
									else if(response.data.status == 'error')
									{
										$rootScope.stopLoaderPopup();
										growl.error(response.data.message,
											{
												title : 'Error!'
											});
									}
								});

					  }

					$scope.closeSubscriptionPopup = ()=>{
						document.getElementById("subscriptionDetails").style.display = "none";
					}
					$scope.closeManualSubscriptionPopup = ()=>{
						document.getElementById("manualSubscription").style.display = "none";
					}
					$scope.openSubscriptionPopup = ()=> {
						document.getElementById("subscriptionDetails").style.display = "block";
						$scope.initSubscriptionConfig();
						$scope.getSubscriptionConfig();
						$scope.getSubscriptionHistory();
					}
					$scope.updateEndDate = function() {
						var startDate = new Date($scope.subscription.subscriptionStartDate);
						var currentDate = new Date();
						if (startDate <= currentDate) {
							$scope.startDate = currentDate;
							startDate = currentDate;
						}
						var endDate = new Date(startDate);
						if ($scope.subscription.subscriptionType === "MONTHLY") {
							endDate.setMonth(startDate.getMonth() + 1);
						} else if ($scope.subscription.subscriptionType === "QUARTERLY") {
							endDate.setMonth(startDate.getMonth() + 3);
						} else if ($scope.subscription.subscriptionType === "YEARLY") {
							endDate.setFullYear(startDate.getFullYear() + 1);
						}else if ($scope.subscription.subscriptionType === "HALF-YEARLY") {
							endDate.setMonth(startDate.getMonth() + 6);
						}
						$scope.subscription.subscriptionEndDate = endDate;
					}


					$scope.openDatePicker = function() {
						$scope.datepicker.opened = true;
					};
					$scope.datepicker = {
						opened : false
					};
					$scope.dateOptions = {
						formatYear : 'yyyy',
						showWeeks : false,
						minDate:new Date(),
					};

					$scope.openDatePickerEndDate = function() {
						$scope.enddatepicker.opened = true;
					};

					$scope.enddatepicker = {
						opened : false
					};

					$scope.dateOptions = {
						formatYear : 'yyyy',
						showWeeks : false,
						minDate:new Date(),
					};
					$scope.subscription = {};
					$scope.openManualSubScripionPopup = ()=> {
						if($scope.subscriptionConfig.subscriptionConfigId!=null &&
							$scope.subscriptionConfig.subscriptionConfigId > 0){
							$scope.subscription = {
								"subscriptionConfigId": $scope.subscriptionConfig.subscriptionConfigId,
								"subscriptionType": $scope.subscriptionConfig.subscriptionType,
								"subscriptionAmount": $scope.subscriptionConfig.subscriptionAmount,
								"subscriptionDiscount": $scope.subscriptionConfig.subscriptionDiscount,
								"subscriptionNetAmount": $scope.subscriptionConfig.subscriptionNetAmount,
								"subscriptionTax": $scope.subscriptionConfig.subscriptionTax,
								"paidAmount": null,
								"amountPaidStatus": "PAID",
								"subscriptionStartDate": null,
								"subscriptionEndDate": null,
								"subscriptionStatus": "ACTIVE",
								"paymentType": "BANK",
								"finalReminderCount": 3,
								"reminderCount": 3,
								"organizationId": $scope.unit.organizationId,
								"unitId": $scope.unit.unitId
							}
							document.getElementById("manualSubscription").style.display = "block";
						}
						else{
							growl.error("Please create subscription configuration.",
								{
									title : 'Error!'
								});
						}

					}
					$scope.paidAmtReg = /^(?!\.$)\d*(?:\.\d+)?$/;
					$scope.createSubscription = ()=>{
						try{
							$scope.$broadcast('show-errors-check-validity');
							console.log($scope.mannualSubscriptionForm.$valid);
							if ($scope.mannualSubscriptionForm.$valid) {
								$rootScope.startLoaderPopup();
								$scope.subscription.unitId = $scope.unit.unitId;
								$scope.subscription.organizationId = $scope.unit.organizationId;
								var URI = BASE_URL + ROOT_URL + "/unit/subcription/details";
								GenericService.serviceAction("POST", URI, $scope.subscription).then((response) => {
									if (response.data.status == 'success') {
										$rootScope.stopLoaderPopup();
										$scope.getSubscriptionHistory();
										$scope.closeManualSubscriptionPopup();
										growl.success(response.data.message,
											{
												title: 'Success!'
											});
									} else if (response.data.status == 'error') {
										$rootScope.stopLoaderPopup();
										growl.error(response.data.message,
											{
												title: 'Error!'
											});
									}
								});
							}
						}catch(e){
							console.log("Exception",e);
						}
					}

					$scope.editSubscription = (subscriptionId)=>{
						try{
							$rootScope.startLoaderPopup();
							var data = {
								subscriptionId : subscriptionId
							}
							var URI = BASE_URL + ROOT_URL + "/unit/subcription/fetch";
							GenericService.serviceAction("POST", URI,data).then((response)=>
							{
								if (response.data.status == 'success')
								{
									$rootScope.stopLoaderPopup();
									$scope.subscription = response.data.object;
									if(response.data.object!=null){
										$scope.subscription.subscriptionStartDate = new Date(response.data.object.subscriptionStartDate);
										$scope.subscription.subscriptionEndDate = new Date(response.data.object.subscriptionEndDate);
									 }
									document.getElementById("manualSubscription").style.display = "block";
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoaderPopup();
									growl.error(response.data.message,
										{
											title : 'Error!'
										});
								}
							});
						}catch(e){
							console.log("Exception",e);
						}
					}

					$scope.validateDate = (date,s)=>{
						var selectedDate = new Date(date);
						var currentDate = new Date();
						if(selectedDate <= currentDate || isNaN(selectedDate)){
                            if(s){
								$scope.subscription.subscriptionStartDate = null;
							}
							else {
								$scope.subscription.subscriptionEndDate = null;
							}
						}
						if(!isNaN($scope.subscription.subscriptionStartDate) && !s &&  selectedDate <= new Date($scope.subscription.subscriptionStartDate)){
							$scope.subscription.subscriptionEndDate = null;
						}

					}
		   }]);
