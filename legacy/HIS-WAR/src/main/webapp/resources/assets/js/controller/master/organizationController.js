'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:organizationController
 * @description #organizationController Controller of the myApp
 */
angular.module('myApp').controller(
		'organizationController',
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
				'ivhTreeviewOptions', 'ivhTreeviewMgr', 'ivhTreeviewBfs','CONSTANTS',
				
				function($scope, $http, $localStorage, $sessionStorage,
						$cookies, $rootScope,fileReader,GenericService,GenericServiceFileUpload,PagerService,promiseFactory,growl, BillingGenericService,ivhTreeviewOptions, ivhTreeviewMgr, ivhTreeviewBfs,CONSTANTS) {
					$rootScope.loginpage = true;
					
					$scope.imageSrc = "";
					$scope.myFile;
				    
				    $scope.$on("fileProgress", function(e, progress) {
				      $scope.progress = progress.loaded / progress.total;
				    });
				    
				    
				    $scope.dateOptions = {
							formatYear : 'yyyy',
							showWeeks : false,
							minDate : new Date()
						};
			/*		$scope.openDatePicker = function() {
						$scope.datepicker.opened = true;
					};

					$scope.datepicker = {
						opened : false
					};
					
					*/
		              // for listLicenceDetails
                    $scope.openDatePicker = function(index) {

                        for (var i = $scope.listLicenceDetailsMaster.length - 1; i >= 0; i--) {
                            if (i === index) {

                                $scope.datepicker.opened[i] = true;
                            } else {
                                $scope.datepicker.opened[i] = false;
                            }
                        }
                    };

                    $scope.datepicker = {
                        opened : [ false ],

                    };
					
				    
				    //Declaration
				    $scope.licenceTypeList;
				    
				    $rootScope.loginpage = true;
					$scope.LISDynamicLabel = "";
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.updateScope = false;
					
				    
				    var cookieObject = $cookies.getObject('cookieObject');
					if(cookieObject == undefined){
						$state.go('login');
						return;
					}
					
					$scope.orgId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
					$scope.createdBy =cookieObject.userId;
					$scope.updatedBy =cookieObject.userId;
					
				    $scope.listLicenceDetailsMaster = [];
				    
				   
				    /**Variables for pagination */
					$scope.offset = 0;
					$scope.noOfRecordsPerPage = 10;
					$scope.organizationList = [];
					$scope.totalNoOfRecords;
					$scope.searchKeyword;
					$scope.organizationCount;
					  $scope.numberToDisplay = 5;
				     
				    
				    $scope.addLicenceDetails = function(){
				    	console.log($scope.licenceDetailsMaster);
				    	if($scope.updateScope ==false)
	        	        {
				    	  var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
						  $scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
						  console.log($scope.listLicenceDetailsMaster);
	        	       }
				      else if($scope.updateScope ==true)
			        	{
				    	  var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
				    	  licenceDetailsMaster.organizationId = $scope.licenceDetailsMaster.organizationId;
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
				
				    	$scope.modulesAction = "Add";
				    	
			        	$scope.licenceDetailsMaster={
			        			"licenceTypeId":"",
			        			"licenceNumber":"",
			        			"expiryDate":""
			        	}
			        	
					$scope.organization =
					{
						 "organizationId":"",
						 "organizationCode":"",
						 "organizationName":"",
						 "status":"A",
						 "createdBy":"",
						 "createdDate":$scope.createdDate,
						 "updatedBy":"",
						 "updatedDate":$scope.updatedDate,
					     "organizationAddress":"",
					     "countryId":"",
					     "stateId":"",
					     "cityId":"",
					     "postCode":"",
					     "districtId":"",
					     "channelId":"",
					     "areaId":"",
					     "organizationContact":"",
					    // "organizationFaxNo":"",
					     "organizationEmailId":"",
					     "organizationLogo":"",
					     "listOrganizationUnitLicenceDetailsDto":[],
					     "registrationNumber":"",
						 "isDuoAuthenticationRequired":false,
						 "isSyncScheduling" : true
					 }
			        	
			        	$scope.listLicenceDetailsMaster=[];
						var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
			        	$scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
				
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
				    
				    $scope.timezoneList = [];
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
									+ "/adt/getAllCountryMasterList";
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
//										 $scope.areaList = response.data.listObject;
										 $scope.logEvents = response.data.listObject;
										
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
//														 $scope.areaList = response.data.listObject;
														 $scope.logEvents = response.data.listObject;
														
													}
												});

					}	
				    
			/*	    $scope.loadMore = function()
				       {
				       if ($scope.numberToDisplay < $scope.logEvents.length) {
				       this.numberToDisplay += 5;
				       $scope.initMasterList($scope.offset,this.numberToDisplay);
				       } else {
				       $scope.numberToDisplay = $scope.logEvents.length;
				       }
				       };*/
				       
				    
				    $scope.loadMore1 = function()
				       {
				    	   $scope.numberToDisplay += 5;
				           $scope.initMasterList($scope.offset,$scope.numberToDisplay);
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
						var URI1 = BASE_URL + ROOT_URL + S +"allArganizationList"+ S + offset + S + noOfRecordsPerPage; 
						
						var URI2 = BASE_URL + ROOT_URL + "/organization/count";
						promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
						{
							$rootScope.stopLoader();
							$scope.organizationList = response[0].listObject;
							$scope.organizationCount = response[1].object;
							$scope.setPage(1, false);
							console.log($scope.organizationList);
							
						});
					}
					
					
				    $scope.getOrganizationMasterList = function(offset, noOfRecordsPerPage) {
						$rootScope.startLoader();
						console.log("offset", offset);
						var data = "";
						offset = offset != null ? offset : 0;
						noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
						var URI = BASE_URL + ROOT_URL + "/allArganizationList"+ S + offset + S + noOfRecordsPerPage;
						console.log("URI", URI);
						GenericService.serviceAction("GET", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.organizationList = response.data.listObject;
						});
					}
				    
				    
				    $scope.pager = {};
					$scope.page;

					$scope.setPage = function(page, flag) {
						if (page < 1 || page > $scope.pager.totalPages)
						{
							return;
						}
						$scope.pager = PagerService.GetPager($scope.organizationCount, page, $scope.noOfRecordsPerPage);
						if (flag)
						{
							$scope.getOrganizationMasterList($scope.pager.startIndex, $scope.pager.pageSize);
						}
					}

					$scope.initOrganizationList($scope.offset, $scope.noOfRecordsPerPage);
				  
					/**Code End For List**/
				    
					
					/**Code Start For Crud**/
					
					$scope.updateStatus = function(orgId,status){
						try{
							$rootScope.startLoader();
							//$scope.initRackMaster();
							var URI = BASE_URL + ROOT_URL + "/organization/status"+S+orgId+S+status;
							console.log("ACT_INACT_URI",URI);
							GenericService.serviceAction("GET", URI, $scope.organization).then(function(response) 
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
					
					
					
					
/*					function dataURLtoFile(dataurl, filename) {
					    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
					        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
					    while(n--){
					        u8arr[n] = bstr.charCodeAt(n);
					    }
					    return new File([u8arr], filename, {type:mime});
					}*/

					//Usage example:
					
				
				    $scope.saveOrganization = function(){
				    	
				    	try 
						{
							//$rootScope.startLoader();
/*				    		$scope.organization.listOrganizationUnitLicenceDetailsDto = [];
				    		if($scope.listLicenceDetailsMaster.length > 0){
				    			angular.forEach($scope.listLicenceDetailsMaster,function(value,index){
				    				if(value.expiryDate != '' || value.expiryDate != undefined || value.expiryDate != null){
				    					value.expiryDate = moment(value.expiryDate).format('DD-MM-YYYY HH:mm:ss');
				    				}
				    			});
				    		}
*/				    		//$scope.organization.listOrganizationUnitLicenceDetailsDto= $scope.listLicenceDetailsMaster;
							console.log("OrganizationMaster",JSON.stringify($scope.organization));
							
							$scope.organization.channelId =$scope.organization.channelId;
							console.log("My ChanneId isssss:"+$scope.organization.channelId);
							
							$scope.$broadcast('show-errors-check-validity');
							if ($scope.organizationform.$valid)
							{
								
								$rootScope.stopLoader();
								$scope.organization.orgPrivileges = JSON.stringify($scope.accessFormObj.modules);
								console.log($scope.organization);
								//return false;
						
									 
							var URI = BASE_URL + ROOT_URL + "/organization/save";
							GenericService.serviceAction("POST", URI,$scope.organization).then(function(response) 
							{	
								if (response.data.status == 'success')
								{
									if($scope.myFile!=undefined &&  !angular.equals({}, $scope.myFile))
										{
											var file = $scope.myFile;
									        var fd = new FormData();
									        fd.append('file', file );
											var URI1 = BASE_URL + ROOT_URL + "/organization/image/"+response.data.object;
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
				    
				    
				    $scope.editOrganization = function(orgId)
					{
						try {
							/*$rootScope.startLoader();*/
							$scope.saveBtnFlag = false;
							$scope.updateScope = true;
							$scope.updateBtnFlag = true;
							$scope.moduleList = [];
							$scope.modulesAction = "Edit";
							/*$scope.$broadcast('show-errors-check-validity');
							if ($scope.organizationform.$valid){
								alert();*/
							var URI = BASE_URL + ROOT_URL +"/organization"+S+orgId;
							console.log("EDIT",URI);
							$scope.listLicenceDetailsMaster = [];
							
							GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
							{

								if (response.data.status == 'success')
								{
									$scope.moduleList = response.data.object.moduleDtosList;
									
									if(response.data.object.listOrganizationUnitLicenceDetailsDto.length > 0){
										
										angular.forEach(response.data.object.listOrganizationUnitLicenceDetailsDto,function(value,index){
											var stringDate = "";
											stringDate = typeof(value) != "undefined" ? value.expiryDate : "";
											console.log("StringDate",stringDate);
											var day = stringDate.substring(0,2);
											var month = stringDate.substring(3,5) - 1;
											var	year = stringDate.substring(6,10);
											var date = new Date(year,month,day);
											console.log("date--",date);
											value.expiryDate = date;
											
											$scope.listLicenceDetailsMaster.push(value);
										})
										/*$scope.$broadcast('show-errors-reset');*/
									}else{
										$scope.listLicenceDetailsMaster.push($scope.listLicenceDetailsMaster);
									}
									/*$rootScope.stopLoader();*/
									$scope.organization = response.data.object;
									$scope.getStateMasterList($scope.organization.countryId);
									$scope.getDistrictByState($scope.organization.stateId);
									$scope.getCityByDistrict($scope.organization.districtId);
									$scope.getAreaByCity($scope.organization.cityId);
									
									
									console.log("$scope.organization",$scope.organization);
									console.log("$scope.listLicenceDetailsMaster",$scope.listLicenceDetailsMaster);
									
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									alert("Error In Fetching Data");
								}
							});
						//	}
						} catch (e) {
							console.log("Exception",e.message);
						}
						
					}
				    
				    
				    $scope.organization.channelId = '';

				    $scope.toggleInputBox = function () {
				      
				    };

				    
				    
				    $scope.licenceList = [];
       $scope.updateOrganization = function(){
    	   console.log("OrganizationMaster",$scope.organization);
				    	try 
						{
				    		$scope.licenceList = [];
							//$rootScope.startLoader();
				    		//$scope.organization.listOrganizationUnitLicenceDetailsDto= $scope.listLicenceDetailsMaster;
							//console.log("OrganizationMaster",JSON.stringify($scope.organization));
							//$scope.$broadcast('show-errors-check-validity');
							/*if ($scope.organizationform.$valid){*/
				    		$scope.$broadcast('show-errors-check-validity');
							$rootScope.startLoader();
				    		if($scope.listLicenceDetailsMaster.length > 0){
				    			var found = $scope.listLicenceDetailsMaster.find(ar=>ar.licenceTypeId != undefined && ar.licenceTypeId != "" 
				    					&& ar.licenceNumber != undefined && ar.licenceNumber != "" && ar.expiryDate != undefined && ar.expiryDate != "");
				    			if(found!=undefined){
				    			$scope.licenceList = [];
				    			angular.forEach($scope.listLicenceDetailsMaster,function(value,index){
				    				var newArr = {}; 
				    				newArr.licenceDetailsId = value.licenceDetailsId!=null ? value.licenceDetailsId : null,
				    				newArr.licenceTypeId = value.licenceTypeId,
				    				newArr.licenceNumber = value.licenceNumber,
				    				newArr.expiryDate = moment(value.expiryDate).format('DD-MM-YYYY HH:mm:ss');
				    				$scope.licenceList.push(newArr);
				    			
				    			});
					    		$scope.organization.listOrganizationUnitLicenceDetailsDto = $scope.licenceList;
				    			}else{
				    				$scope.listLicenceDetailsMaster=[];
									var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
						        	$scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
				    			}
				    			}else{
					    			$scope.listLicenceDetailsMaster=[];
									var licenceDetailsMaster = angular.copy($scope.licenceDetailsMaster);
						        	$scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
					    		}
				    		if ($scope.organizationform.$valid){
				    			
				    			 $rootScope.stopLoader();
								 var mobileNumber = $scope.organization.organizationContact;
								 if(mobileNumber.length != 10){
									 growl.error("Please Enter Valid Organization Contact",
												{
													title : 'Error!'
												});
									 return false;
								 }
				    			
							$scope.organization.orgPrivileges = JSON.stringify($scope.accessFormObj.modules);	 
								 
							var URI = BASE_URL + ROOT_URL + "/organization/update";
							GenericService.serviceAction("POST", URI,$scope.organization).then(function(response) 
							{	
								if (response.data.status == 'success')
								{
									if($scope.myFile!=undefined &&  !angular.equals({}, $scope.myFile))
									{
									var file = $scope.myFile;
							        var fd = new FormData();
							        fd.append('file', file );
									var URI1 = BASE_URL + ROOT_URL + "/organization/image/"+response.data.object;
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
									
									$scope.saveBtnFlag = true;
									$scope.updateBtnFlag = false;
									$rootScope.stopLoader();
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
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
				    
 
 		$scope.licenceDetails = function(orgId){
 			
 			try{
 				
 				var data = {
 	 					organizationId : orgId
 	 			}
 				
 				var URI = BASE_URL + ROOT_URL + "/licenceDetailsByOrganization";
 				console.log("LicenceDetails",JSON.stringify(data));
 				
 				GenericService.serviceAction("POST", URI,data).then(function(response) 
						{	
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								/*growl.success(response.data.message,
										{
											title : 'Success!'
										});*/
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
					$scope.organization.postCode = value.postCode;
				}
			});
			
		}
				    /**Code End For Crud**/
 		
 		
 		//start for module access
 		 $scope.accessFormObj = {
                 roleId: '',
                 privilegeId: '',
                 moduleType: '',
                 modules: []
             }
 		  var data;
 		 $scope.ui = {
                 roles: [],
                 modalTitle: '',
                 showModule: false,
                 showSubModule: false,
                 showLink: false,
                 showRoleForm: true,
                 edit: '',
                 deleteId: ''

             }
 		 function selectNodes(moduleObj) {
             // Selecting by node id

             if (moduleObj.moduleType == 1) {
                if(moduleObj.parentId != 0){
                	ivhTreeviewMgr.select($scope.hisBags, moduleObj.moduleId);
                }
             }

         };

 		 function initTree(modules) {
             for (var i = 0; i < modules.length; i++) {
                 var subModules = data.filter(function(item) {
                     return item.parentId == modules[i].moduleId;
                 });
                 modules[i].children = subModules;

                 initTree(modules[i].children);
             }
         }
 		   $scope.getBiModules = function() {
               var URI = BASE_URL + ROOT_URL + "/module/biModules";
               $http.get(URI)
                   .then(function(response) {
                       data = response.data.listObject;
                       var modules = data.filter(function(item) {
                           return item.parentId == 0;
                       });
                       initTree(modules);
                       $scope.biBags = modules;
                   })
           }
 		function getModules() {
            var URI = CONSTANTS.GLOBAL.MODULE_API;
            $http.get(URI) 
                .then(function(response) {
                    data = response.data.listObject;
                    var modules = data.filter(function(item) {
                        return item.parentId == 0;
                    });
                    initTree(modules);
                    $scope.hisBags = modules;
                })
        }
 		
 		function deselectNodes() {
            ivhTreeviewMgr.deselectAll($scope.hisBags);
         //   ivhTreeviewMgr.deselectAll($scope.biBags);
        }

        function collapseAll() {
            ivhTreeviewMgr.collapseRecursive($scope.hisBags, $scope.hisBags);
           // ivhTreeviewMgr.collapseRecursive($scope.biBags, $scope.biBags);
        };
 	   $scope.firstTabLevel = 1;
       $rootScope.setFirstTabLevel = function(newTab) {
           $scope.firstTabLevel = newTab;
       };
       $scope.isSetFirstTabLevel = function(tabNum) {
           return $scope.firstTabLevel === tabNum;
       };

       $scope.getPermissions = function() {
           var URI = CONSTANTS.GLOBAL.PRIVILEGE_API + '/' + parseInt(sessionStorage.getItem("organizationId")) + "/" + parseInt(sessionStorage.getItem("unitId"));
           console.log("URI", URI);
           $rootScope.startLoader();
           $http.get(URI)
               .then(function(response) {
                   $scope.permissionList = response.data.privileges;
                   $rootScope.stopLoader();
               })
       }

       $scope.initModal = function(type) {
       	
       	var element = angular.element('#instructionDetails');
      
           deselectNodes();
           collapseAll();
           $scope.ui.modalTitle = type + ' Permissions';
           $scope.accessFormObj.modules = [];
           if (type === 'Add' || type === 'Edit') {
               $scope.ui.showRoleForm = true;
               $scope.permissions = [];
   
               			if (type === 'Edit') {                             
                               $scope.hisPrivilegeId = 0;
                               $scope.ui.edit = true;                       

                               var dataList = $scope.moduleList;
                                       if(dataList!=null){
                                           angular.forEach(dataList, function(value, index){
                                               if(value.moduleType==1){
                                                    //$scope.hisPrivilegeId = response.data.modules[0].privilegeId;
                                                    selectNodes(value);
                                               }
                                           })
                                       }
                                       $rootScope.stopLoader();
                                       element.modal('show');
                                   
                           }else{
							   $scope.ui.edit = false;
							   $scope.accessFormObj.roleId = "";
							   $scope.accessFormObj.moduleType = "";
							   $scope.accessFormObj.modules = [];
							   element.modal('show');
   							}
	   
           }

       }
       
       $scope.savePermissions = function(moduleType) {

           $scope.accessFormObj.modules = [];

           $scope.accessFormObj.moduleType = moduleType;
           $scope.accessFormObj.privilegeId = $scope.hisPrivilegeId;

         ivhTreeviewBfs($scope.hisBags, function(node) {
           	
               if (node.selected) {
                   $scope.accessFormObj.modules.push({
                	   moduleId: node.moduleId,
                	   moduleName: node.moduleName,
                       actionLink: node.actionLink,
                       parentId: node.parentId,
                       moduleType: 1
                   })
               }
           })
           
             ivhTreeviewBfs($scope.hisBags, function(node) {
            	  if (!node.selected) {
            		  var check = false;
            	 angular.forEach(node.children,function(v){
            		   if (v.selected) {
            			   check = true;
            		   }
            	 });
            	 if(check == true){
            		 $scope.accessFormObj.modules.push({
                    	   moduleId: node.moduleId,
                    	   moduleName: node.moduleName,
                           actionLink: node.actionLink,
                           parentId: node.parentId,
                           moduleType: 1
                       })
            	 }
               }
           })
           
           $scope.accessFormObj.organisationId = parseInt(sessionStorage.getItem("organizationId"));
           $scope.accessFormObj.unitId = parseInt(sessionStorage.getItem("unitId"));
           console.log("module111",JSON.stringify($scope.accessFormObj.modules));

               var URI = CONSTANTS.GLOBAL.PRIVILEGE_API;
               $scope.accessFormObj.privilegeId = "";
              /* $rootScope.startLoader();
               $http.post(URI, $scope.accessFormObj)
                   .then(function(response) {
                       $scope.permissions = [];
                       $scope.getPermissions();
                       $scope.accessFormObj = {
                           modules: [],
                           roleId: '',
                       }
                       growl.success("Saved Successfully");
                       $rootScope.stopLoader();
                       $scope.getPermissions();
                   })
                   .catch(function(err) {
                       growl.error("Failed to save");
                       $scope.getPermissions();
                   })*/
           deselectNodes();
           collapseAll();

       }
 		
 		 getModules();
 		$scope.getPermissions();
 		//$scope.getBiModules();
 		//End for module access
				    
				} ]);
