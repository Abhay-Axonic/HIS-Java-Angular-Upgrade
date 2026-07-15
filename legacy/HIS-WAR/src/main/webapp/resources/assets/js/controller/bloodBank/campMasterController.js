angular
		.module('myApp')
		.controller(
				'campMasterController',
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
						'PagerService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,PagerService) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							
							
							$scope.camp ={
									
									"campCode":"",
									"campName":"",
									"address":"",
									"countryId":"",
									"stateId":"",
									"districtId":"",
									"cityId":"",
									"areaId":"",
									"pincode":"",
									"createdBy":$scope.userId,
									"createdDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"status":"A",
									"updatedBy":$scope.userId,
									"updatedDate":moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									"unitId":$scope.unitId ,
									"organizationId":$scope.organizationId,
									"patientTypeId":""
									
									
							}
							
							
							
							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.campList = [];
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
								var data1 = {
									      	organizationId:organizationId,
											offset:offset,
											noOfRecordsPerPage:noOfRecordsPerPage,
											unitId:unitId
											};
								
								var data2 = {
										      organizationId:organizationId,
										      unitId:unitId
											};
								
								var URI1 = BASE_URL + ROOT_URL + "/global/bloodBank/getCampMasterListPaginationList"; 
								var URI2 = BASE_URL + ROOT_URL + "/global/bloodBank/getCampMasterListCount";
									
								
								GenericService.serviceAction("POST",URI1,data1).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.campList = response.data.listObject;
									console.log("commonList",$scope.commonList);
									}
								});
								
								GenericService.serviceAction("POST",URI2,data2).then(function(response){
									if(response.data.status == "success"){					
										$rootScope.stopLoader();
										$scope.commonListCount = response.data.object;
										$scope.setPage(1,false);
										console.log("commonListCount",response.data.object);
									}
								});
								
							}		
							
							$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								console.log("offset", offset);
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								var data = {
										organizationId:organizationId,
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
										unitId:unitId
										};
								
								var URI = BASE_URL + ROOT_URL +  "/global/bloodBank/getCampMasterListPaginationList";
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI,data).then(function(response) {
									$rootScope.stopLoader();
									$scope.campList = response.data.listObject;
									console.log("commonList",$scope.commonList);
								});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
							
							/*$scope.campList = [];
						$scope.getcampList = function(){
								var data = {
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
										};								
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getCampMasterList";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											//$scope.stateList = [];
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.campList = response.data.listObject;
													console.log("$scope.compList", 	$scope.campList);
											}
										});
						}
						$scope.getcampList();*/
						
						$scope.getActivePatientTypeList = function()
					    {
					        var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
					    GenericService
					        .serviceAction(METHOD_GET, URI,
					                NOTIFICATION_MSG_STATUS_FALSE)
					        .then(
					                function(response) {
					                    console.log(response);
					                    if (response.data.status == "success"){
					                        
					                        $scope.patientTypeList = response.data.listObject;
					                        console.log("list",$scope.patientTypeList);
					                    }
					                    
					                });
					        
					    }
						$scope.getActivePatientTypeList();
						
						$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL+ GETACTIVECOUNTRYLIST+$scope.organizationId;								
							BillingGenericService.serviceAction(METHOD_GET, URI,"",NOTIFICATION_MSG_STATUS_FALSE)
							.then(function(response) {
										console.log(response);
										$scope.countryList = [];
										if (response.data.status == "success")
											$scope.countryList = response.data.listObject;
										console.log("$scope.countryList", 	$scope.countryList);
									});
						
							$scope.stateList = [];
							$scope.getStateByCountry = function(id,type) {
								var data = {
									countryId : id
								};								
								var URI = BASE_URL + ROOT_URL+ GETSTATELISTBYCOUNTRYID;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											//$scope.stateList = [];
											if (response.data.status == "success"){
													
													$scope.stateList = response.data.listObject;
													console.log("$scope.stateList", 	$scope.stateList);
											}
										});
							}
							
							$scope.districtList = [];
							$scope.getDistrictByState = function(id,type) {
								var data = {
									stateId : id
								};
								var URI = BASE_URL + ROOT_URL+ GETDISTRICTLISTBYSTATEID;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success")
											{
													$scope.districtList = response.data.listObject;
													console.log("$scope.districtList", 	$scope.districtList);
											}
											
										});
							}
							
							$scope.cityList = [];
							$scope.getCityByDistrict = function(id,type) {
								var data = {
									districtId : id
								};
								var URI = BASE_URL + ROOT_URL+ GETCITYLISTBYDISTRICTID;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success")
											{
													$scope.cityList = response.data.listObject;
													console.log("$scope.cityList", 	$scope.cityList);
											}
										});
							}
							
							$scope.areaList = [];
							$scope.getAreaByCity = function(id,type) {

								var data = {
									cityId : id
								};
								var URI = BASE_URL + ROOT_URL+ GETAREALISTBYCITY;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											
											if (response.data.status == "success")
											{
													$scope.areaList = response.data.listObject;
													console.log("$scope.areaList", 	$scope.areaList);
											}
										});
							}
							
							
							$scope.saveCamp = function(){
								$rootScope.startLoader();
								if($scope.camp.campCode == undefined || $scope.camp.campCode == "" || $scope.camp.campCode == null){
									$rootScope.stopLoader();
									growl.error("Please Enter Camp Code",
											{
												title : 'error!'
											});
									return false;
								}
								if($scope.camp.campName == undefined || $scope.camp.campName == "" || $scope.camp.campName == null){
									$rootScope.stopLoader();
									growl.error("Please Enter Camp Name",
											{
												title : 'error!'
											});
									return false;
								}
								if($scope.camp.address == undefined || $scope.camp.address == "" || $scope.camp.address == null){
									$rootScope.stopLoader();
									growl.error("Please Enter Address",
											{
												title : 'error!'
											});
									return false;
								}
								if($scope.camp.countryId == undefined || $scope.camp.countryId == "" || $scope.camp.countryId == null){
									$rootScope.stopLoader();
									growl.error("Please Select Country",
											{
												title : 'error!'
											});
									return false;
								}
								if($scope.camp.stateId == undefined || $scope.camp.stateId == "" || $scope.camp.stateId == null){
									$rootScope.stopLoader();
									growl.error("Please Select State",
											{
												title : 'error!'
											});
									return false;
								}
								if($scope.camp.districtId == undefined || $scope.camp.districtId == "" || $scope.camp.districtId == null){
									$rootScope.stopLoader();
									growl.error("Please Select District ",
											{
												title : 'error!'
											});
									return false;
								}
								if($scope.camp.cityId == undefined || $scope.camp.cityId == "" || $scope.camp.cityId == null){
									$rootScope.stopLoader();
									growl.error("Please Select City",
											{
												title : 'error!'
											});
									return false;
								}
								if($scope.camp.areaId == undefined || $scope.camp.areaId == "" || $scope.camp.areaId == null){
									$rootScope.stopLoader();
									growl.error("Please Select Area",
											{
												title : 'error!'
											});
									return false;
								}
								if($scope.camp.pincode == undefined || $scope.camp.pincode == "" || $scope.camp.pincode == null){
									$rootScope.stopLoader();
									growl.error("Please Enter PinCode",
											{
												title : 'error!'
											});
									return false;
								}
								$scope.camp.createdBy = $scope.userId;
								$scope.camp.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								$scope.camp.status = "A";
								$scope.camp.updatedBy = $scope.userId;
								$scope.camp.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								$scope.camp.unitId = $scope.unitId;
								$scope.camp.organizationId = $scope.organizationId;
								var URI = BASE_URL + ROOT_URL +"/global/bloodBank/saveCampMaster";
								console.log("$scope.camp", 	$scope.camp);
								GenericService.serviceAction("POST", URI,$scope.camp).then(function(response){
									if (response.data.status == 'success')
									{	$rootScope.stopLoader();
										$scope.camp={};
										$scope.getcampList();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										
									
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}else if(response.data.status == 'Already Exists.')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}
								});
							}
							
							$scope.updateStatus = function(Id,status){
								try{
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data={
											updatedBy:$scope.userId,
											updatedDate:$scope.dt,
											status:status,
											campId:Id,
											unitId:$scope.unitId,
											organizationId:$scope.organizationId
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/global/bloodBank/updateCampMasterStatus";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.getcampList();
											$rootScope.stopLoader();
											
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
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
							
							$scope.campId="";
							$scope.showUpdateBtn = function(info){
								try{
									
									$scope.campId=info.campId;
									$scope.camp.campCode=info.campCode;
									$scope.camp.campName=info.campName;
									$scope.camp.address=info.address;
									$scope.camp.pincode=info.pincode;
									$scope.camp.countryId=info.countryId;
									//$scope.countryList.push({ countryId: info.countryId, countryName: info.countryName });
									$scope.stateList.push({ stateId: info.stateId, stateName: info.stateName });
									$scope.camp.stateId=info.stateId;
									$scope.districtList.push({ districtId: info.districtId, districtName: info.districtName });
									$scope.camp.districtId=info.districtId;
									$scope.cityList.push({ cityId: info.cityId, cityName: info.cityName });
									$scope.camp.cityId=info.cityId;
									$scope.areaList.push({ areaId: info.areaId, areaName: info.areaName });
									$scope.camp.areaId=info.areaId;
									$scope.camp.patientTypeId = info.patientTypeId;
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
								
								}catch(e){
									console.log("Exception-",e);
								}
							}
							
							$scope.updateCamp = function(){
								try{
									$scope.startLoader();
									if($scope.camp.campCode == undefined || $scope.camp.campCode == "" || $scope.camp.campCode == null){
										$rootScope.stopLoader();
										growl.error("Please Enter Camp Code",
												{
													title : 'error!'
												});
										return false;
									}
									if($scope.camp.campName == undefined || $scope.camp.campName == "" || $scope.camp.campName == null){
										$rootScope.stopLoader();
										growl.error("Please Enter Camp Name",
												{
													title : 'error!'
												});
										return false;
									}
									if($scope.camp.address == undefined || $scope.camp.address == "" || $scope.camp.address == null){
										$rootScope.stopLoader();
										growl.error("Please Enter Address",
												{
													title : 'error!'
												});
										return false;
									}
									if($scope.camp.countryId == undefined || $scope.camp.countryId == "" || $scope.camp.countryId == null){
										$rootScope.stopLoader();
										growl.error("Please Select Country",
												{
													title : 'error!'
												});
										return false;
									}
									if($scope.camp.stateId == undefined || $scope.camp.stateId == "" || $scope.camp.stateId == null){
										$rootScope.stopLoader();
										growl.error("Please Select State",
												{
													title : 'error!'
												});
										return false;
									}
									if($scope.camp.districtId == undefined || $scope.camp.districtId == "" || $scope.camp.districtId == null){
										$rootScope.stopLoader();
										growl.error("Please Select District ",
												{
													title : 'error!'
												});
										return false;
									}
									if($scope.camp.cityId == undefined || $scope.camp.cityId == "" || $scope.camp.cityId == null){
										$rootScope.stopLoader();
										growl.error("Please Select City",
												{
													title : 'error!'
												});
										return false;
									}
									if($scope.camp.areaId == undefined || $scope.camp.areaId == "" || $scope.camp.areaId == null){
										$rootScope.stopLoader();
										growl.error("Please Select Area",
												{
													title : 'error!'
												});
										return false;
									}
									if($scope.camp.pincode == undefined || $scope.camp.pincode == "" || $scope.camp.pincode == null){
										$rootScope.stopLoader();
										growl.error("Please Enter PinCode",
												{
													title : 'error!'
												});
										return false;
									}
									
									    $scope.camp.campId = $scope.campId;
									    $scope.camp.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										var URI = BASE_URL + ROOT_URL +"/global/bloodBank/updateCampMaster";
									    GenericService
										.serviceAction(METHOD_POST, URI,$scope.camp,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.startLoader();
													if (response.data.status == "success"){
														$scope.saveBtnFlag = true;
														$scope.updateBtnFlag = false;
														$scope.stopLoader();
														$scope.camp={};
														$scope.getcampList();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													}
													else{
														$scope.stopLoader();
														growl.error(response.data.message,
																{
																	title : 'error!'
																});
													}
												});
										
									
								}catch(e){
									console.log("Exception-",e);
								}
							}
							
			}]);
							