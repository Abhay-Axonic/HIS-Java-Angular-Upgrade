angular
.module('myApp')
.controller(
		"vaccinationMasterController",
		[
			'$scope',
			'$rootScope',
			'$state',
			'$cookies',
			'GenericService',
			'PagerService',
			'promiseFactory',
			'$sessionStorage',
			'growl',
			'GenericServiceParamHeader',
			'BillingGenericService',
			function($scope, $rootScope, $state, $cookies,GenericService, PagerService, promiseFactory,$sessionStorage, growl,GenericServiceParamHeader,BillingGenericService) {
				$rootScope.loginpage = true;
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.updateScope = false;
				$scope.popUpFlag = true;

				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.commonList = [];
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				var cookieObject = $cookies.getObject('cookieObject');
				if (cookieObject == undefined) {$state.go('login');
				return;
				}

				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.createdBy = cookieObject.userId;
				$scope.updatedBy = cookieObject.userId;

				$scope.initMaster = function() {
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.common = {

							vaccinationId : "",
							vaccinationCode : "",
							vaccinationDesc : "",
							genericId:"",
							administrationId : "",
							siteId : "",
							doseId : "",
							uomId : "",
							age : "",
							minAge:"",
							minDay:"",
							maxAge:"",
							maxDay:"",
							series:"",
							interval:"",
							chooseInterval:"",
							remark:"",
							Hours:"",
							days : "",
							weeks : "",
							month : "",
							years : "",
							roaName : "",
							description : "",
							siteName : "",
							units : "",
							unit : "",
							name : "",
							vaccineName : "",
							abbreviation:"",
							object:"",
							selectedGenderList:[],
								

					};
					console.log("Common-init", $scope.common);
					$scope.selectedGenderList = [];
				}
				$scope.initMaster();

				/** Crud Operations Start* */

				/** List And Pagination Start* */
				$scope.setNoOfRecords = function() {
					$scope.initMasterList($scope.organizationId,
							$scope.offset,
							$scope.noOfRecordsPerPage,
							$scope.unitId);
				};

				$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
					$rootScope.startLoader();
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
					var data1 = {
							organizationId : organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage,
							unitId : unitId
					};

					var data2 = {
							organizationId : organizationId,
							unitId : unitId
					};

					var URI1 = BASE_URL + ROOT_URL+ GET_VACCINATION_MASTER_LIST;
					var URI2 = BASE_URL + ROOT_URL+ GET_VACCINATION_MASTER_COUNT;

					GenericService.serviceAction("POST", URI1, data1).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.commonList = response.data.listObject;
									console.log("commonList",$scope.commonList);
								}
							});

					GenericService.serviceAction("POST", URI2, data2).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.commonListCount = response.data.object;
									$scope.setPage(1,false);
									console.log("commonListCount",response.data.object);
								}
							});

				   }

				   $scope.masterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
					          $rootScope.startLoader();
					           console.log("offset", offset);
					          offset = offset != null ? offset : 0;
					           noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;

					var data = {
							organizationId : organizationId,
							offset : offset,
							noOfRecordsPerPage : noOfRecordsPerPage,
							unitId : unitId
					};

					var URI = BASE_URL + ROOT_URL+ GET_VACCINATION_MASTER_LIST;
					console.log("URI", URI);
					GenericService.serviceAction("POST", URI, data).then(function(response) {
								$rootScope.stopLoader();
								$scope.commonList = response.data.listObject;
								console.log("commonList",$scope.commonList);
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
							$scope.noOfRecordsPerPage,
							$scope.unitId);
					if (flag) {
						    $scope.masterList($scope.organizationId,
							$scope.pager.startIndex,
							$scope.pager.pageSize,
							$scope.unitId);
					}
				}

				$scope.initMasterList($scope.organizationId,
				$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

				/** List And Pagination End* */

				// function for popup ok button call
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.save();
				}

				 //dose list dropdown function

				$scope.getDoseActiveList = function() {

					var URI = BASE_URL + ROOT_URL+ "/api/common/getDoseMasterList";
					var data = {
                               organizationId : $scope.organizationId
                     }
					GenericService.serviceAction("POST", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.getDoseActiveList = [];
									$scope.getDoseActiveList = response.data.listObject;
									console.log("getDoseActiveList",$scope.getDoseActiveList);
								}
							});
				}
				$scope.getDoseActiveList();

				// Dose list end

				// //Rate of administration list dropdown function

				$scope.getRateActiveList = function() {

					var URI3 = BASE_URL + ROOT_URL+ "/api/billing/getRouteOfAdministrationList"+S + $scope.organizationId+ S + $scope.unitId;;
					var data = {}
					
					GenericService.serviceAction("GET", URI3, data).then(function(response) {
						
								$rootScope.stopLoader();
								$scope.getRateActiveList = [];
								$scope.getRateActiveList = response.data.listObject;
								console.log("getRateActiveList",$scope.getRateActiveList);

							});
				}
				$scope.getRateActiveList();

				// End OF Route of Administration List

				// Start of Site of Administration dropdown function
				$scope.getSiteActiveList = function() {

					var URI = BASE_URL+ ROOT_URL+ "/api/global/clinical/getSiteMasterList";
					var data = {
							organizationId : $scope.organizationId
							}
					GenericService.serviceAction("POST", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.getSiteActiveList = [];
									$scope.getSiteActiveList = response.data.listObject;
									console.log("getSiteActiveList",$scope.getSiteActiveList);
								}
							});
				}
				$scope.getSiteActiveList();

				// End of Site of Administration

				// Start of UOM dropdown function
				$scope.getUOMActiveList = function() {

					var URI = BASE_URL + ROOT_URL+ "/api/globals/strength/units";
					var data = {
                                     organizationId : $scope.organizationId
                               }
					GenericService.serviceAction("GET", URI, data).then(function(response) {

								$rootScope.stopLoader();
								$scope.getUOMActiveList = [];
								$scope.getUOMActiveList = response.data;
                                console.log("getUOMActiveList",$scope.getUOMActiveList);

							});
				}
				$scope.getUOMActiveList();

				// End of UOM

				// Start of Vaccination Name dropdown function 
				$scope.getVaccineNameActiveList = function() {

					var URI = BASE_URL+ ROOT_URL+ "/api/globals/items/getIsVaccineEnabledItems";
					var data = {
							       organizationId : $scope.organizationId
							   }
					GenericService.serviceAction("GET", URI, data).then(function(response) {

								$rootScope.stopLoader();
								$scope.getVaccineNameActiveList = [];
								$scope.getVaccineNameActiveList = response.data;
                                console.log("getVaccineNameActiveList",$scope.getVaccineNameActiveList);

							});
				}
				$scope.getVaccineNameActiveList();

				// End of Vaccination Name
				
				//get Gender list
				
				$scope.getGenderList = function(){
				var data = {};
				var URI = BASE_URL + ROOT_URL+ GETACTIVEGENDERLIST;
				GenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
					$scope.getGenderList = [];
							if (response.data.status == "success")
								angular .forEach(response.data.listObject, function( value, index) {
									var newAr = {};
									newAr.id = value.id;
									newAr.label = value.desc;
									$scope.getGenderList.push(newAr);
									
								});
							console.log("$scope.genderList",$scope.getGenderList);
						});
				}
				$scope.getGenderList();
				
				//get Generic name
				$scope.getGeneric = function(object) {
					
					var unitId = $scope.unitId;
					var orgId = $scope.organizationId;
					var medication=object.name;
					$scope.common.vaccinationCode=object.id;
					
//					var regex=/[^a-z]/gi;
//					medication=medication.replace(regex," ");
					
					var URI = BASE_URL + ROOT_URL + GETMEDICATIONDETAILS + "?unitId=" + 
						unitId + "&orgId=" + orgId + "&searchDrug=" + medication
					
					GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
						if(response.data.status == "success"){
							if(response.data.listObject.length !== 0) {
								$scope.showMedicationOptions = true
								$scope.drugOptions = []
								$scope.drugOptions = response.data.listObject;
								$scope.common.genericName=$scope.drugOptions[0].genericName;
								$scope.common.genericId=$scope.drugOptions[0].genericId;
								$scope.common.doseId=$scope.drugOptions[0].strength;
								$scope.common.uomId=$scope.drugOptions[0].strengthUnitId;
								$scope.common.units=$scope.drugOptions[0].unitDesc;

								console.log("drugList",$scope.drugOptions);
							
							}
						}
					})
				}
				
			        $scope.seriesList = [];
			        $scope.seriesList.push({value:0, text:'0'});
		            $scope.seriesList.push({value:1, text:'1'});
		            $scope.seriesList.push({value:2, text:'2'});
		            $scope.seriesList.push({value:3, text:'3'});
		            $scope.seriesList.push({value:4, text:'4'});
		            $scope.seriesList.push({value:5, text:'5'});
		            $scope.seriesList.push({value:6, text:'6'});
		            $scope.seriesList.push({value:7, text:'7'});
		            $scope.seriesList.push({value:8, text:'8'});
		            $scope.seriesList.push({value:9, text:'9'});
		            $scope.seriesList.push({value:10, text:'10'});
		            $scope.seriesList.push({value:11, text:'11'});
		            $scope.seriesList.push({value:12, text:'12'});
		            $scope.seriesList.push({value:13, text:'Booster 1'});
		            $scope.seriesList.push({value:14, text:'Booster 2'});
				//for SAVE data

				$scope.save = function() {
					try {

						if ($scope.popUpFlag == false) {
							$rootScope.startLoader();
						}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						
						$scope.genderId=[];
						
						angular.forEach($scope.selectedGenderList, function(value , key) {
							$scope.genderId.push(value.id);
						});
						

						var data = {

								vaccinationCode : $scope.common.vaccinationCode,
								genericId : $scope.common.genericId,
								administrationId : $scope.common.administrationId,
								siteId : $scope.common.siteId,
								selectedGenderList:$scope.genderId,
								doseId : $scope.common.doseId,
								uomId : $scope.common.uomId,
								minAge : $scope.common.minAge,
								minDay : $scope.common.minDay,
								maxAge : $scope.common.maxAge,
								maxAge : $scope.common.maxAge,
								maxDay : $scope.common.maxDay,
								abbreviation : $scope.common.abbreviation,
								series:$scope.common.series,
								interval : $scope.common.interval,
								remark : $scope.common.remark,
								chooseInterval : $scope.common.chooseInterval,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								createdBy : $scope.createdBy,
								updatedBy : $scope.updatedBy,
								status    : 'A',
								createdDate : $scope.dt,
								updatedDate : $scope.dt

						}
						if(data.vaccinationCode==""){
							growl.warning("Please select brand name.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}else if(data.abbreviation==""){
							growl.warning("Please select abbreviation.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.administrationId==""){
							growl.warning("Please select route of administration.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.siteId==""){
							growl.warning("Please select site.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.selectedGenderList.length==0){
							growl.warning("Please select gender.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.minAge== undefined || data.minAge==null || data.minAge==""){
							growl.warning("Please select min age.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.minDay==""){
							growl.warning("Please select min duration.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.maxAge== undefined || data.maxAge==null || data.maxAge==""){
							growl.warning("Please select max age.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.maxDay==""){
							growl.warning("Please select max duration.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.series==""){
							growl.warning("Please select series.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						else if(data.interval== undefined || data.interval== null || data.interval==""){
							growl.warning("Please select interval dose.", {
								title : 'Warning!'
							});
							$rootScope.stopLoader();
							return false;
						}
						if($scope.common.minDay =="Hours" ){
							data.age="1";
				         		}
			           	if($scope.common.minDay =="Days"){
			           		data.age="2";
						}
						if($scope.common.minDay == "Weeks"){
							data.age="3";
						}
						if($scope.common.minDay == "Month"){
							data.age="4";
						}
                       if($scope.common.minDay == "Years"){
                    	   data.age="5";
						}
						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {

							//alert($scope.common.day)
							//return false;
							var URI = BASE_URL + ROOT_URL+ SAVE_VACCINATION_MASTER;
							console.log("Save-URI-", URI);
							console.log(" save OBJ", data);

							GenericService.serviceAction("POST", URI,data).then(function(response) {

								if (response.data.status == 'success') {
									growl.success(response.data.message,
											{
												title : 'Success!'
											});

									// $scope.$broadcast('show-errors-reset');
									$scope.initMaster();
									$scope.drugOptions = [];
									$scope.object="";
									$scope.initMasterList(
											$scope.organizationId,
											$scope.offset,
											$scope.noOfRecordsPerPage,
											$scope.unitId);
								    } else if (response.data.status == 'error') {
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								   }
							});
							$rootScope.stopLoader();
						}

					} catch (e) {
						console.log("Exception-", e);
					}
				}

				$scope.showDetailsBtn = function(data) {
					$scope.configDetails = data;
				}
                      
				$scope.showUpdateBtn = function(id) {
					$scope.object={};
					try {
						$rootScope.startLoader();
						$scope.saveBtnFlag = false;
						$scope.updateScope = true;
						$scope.updateBtnFlag = true;
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ GET_VACCINATION_MASTER_BY_ID + S+ id + S + $scope.organizationId+ S + $scope.unitId;
						console.log("EDIT", URI);
						GenericService.serviceAction("GET", URI).then(function(response) {
									console.log("Edit",response);

									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.common = response.data.listObject[0];
										$scope.common.vaccinationId = response.data.listObject[0].vaccinationId.toString();
										$scope.object.id = response.data.listObject[0].vaccinationCode;
										$scope.object.name = response.data.listObject[0].name;
										
										var newAr = {};
										newAr.id = response.data.listObject[0].genderId;
										newAr.label = response.data.listObject[0].genderDesc;
										$scope.selectedGenderList.push(newAr);
									} else if (response.data.status == 'error') {
										$rootScope.stopLoader();
										growl.error(
												response.data.message,
												{
													title : 'Error!'
												});
									}
								});


					} catch (e) {
						console.log("Exception-", e);
					}
				}

				
				
				//For update 
				$scope.update = function() {
					try {
                             var date = new Date();
						     $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						     $rootScope.startLoader();
						     
						 	$scope.genderId=[];
							
							angular.forEach($scope.selectedGenderList, function(value , key) {
								$scope.genderId.push(value.id);
							});

						var data = {
								vaccinationId : $scope.common.vaccinationId,
								vaccinationCode : $scope.common.vaccinationCode,
								genericId : $scope.common.genericId,
								administrationId : $scope.common.administrationId,
								siteId : $scope.common.siteId,
								selectedGenderList:$scope.genderId,
								doseId : $scope.common.doseId,
								uomId : $scope.common.uomId,
								minAge : $scope.common.minAge,
								minDay : $scope.common.minDay,
								maxAge : $scope.common.maxAge,
								maxDay : $scope.common.maxDay,
								abbreviation : $scope.common.abbreviation,
								series:$scope.common.series,
								interval : $scope.common.interval,
								remark : $scope.common.remark,
								chooseInterval : $scope.common.chooseInterval,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								updatedBy : $scope.updatedBy,
								updatedDate : $scope.dt

						}

						var URI = BASE_URL + ROOT_URL+ UPDATE_VACCINATION_MASTER;
						console.log("Update-URI-", URI);
						console.log("Update OBJ", data);
						$rootScope.startLoader();
						GenericService.serviceAction("POST", URI, data).then(function(response) {

							if (response.data.status == 'success') {
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
									title : 'Success!'
										});
								// $scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.initMasterList(
								$scope.organizationId,
								$scope.offset,
								$scope.noOfRecordsPerPage,
								$scope.unitId);
							} else if (response.data.status == 'error') {
								$rootScope.stopLoader();
								growl.error(
										response.data.message,
										{
											title : 'Error!'
										});
							}
						});

					} catch (e) {
						console.log("Exception-", e);
					}
				}


				//update status function 
				$scope.updateStatus = function(id, status) {
					try {
						var date = new Date();
						$scope.dt = moment(date).format(
								'DD-MM-YYYY HH:mm:ss');

						/*$scope.common.updatedBy = $scope.updatedBy,
						$scope.common.updatedDate = $scope.dt,
						$scope.common.status = status,
						$scope.common.vaccinationId = id,*/
						var data={
									updatedBy:$scope.updatedBy,
									updatedDate:$scope.dt,
									status:status,
									vaccinationId:id
								  };
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ UPDATE_VACCINATION_MASTER_STATUS;
						console.log("update status", $scope.common);
						// return false;
						GenericService.serviceAction("POST", URI,data).then(
										function(response) {
											if (response.data.status == 'success') {
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												// $scope.$broadcast('show-errors-reset');
												        $scope.initMaster();
												        $scope.initMasterList(
														$scope.organizationId,
														$scope.monffset,
														$scope.noOfRecordsPerPage,
														$scope.unitId);
											} else if (response.data.status == 'error') {
												$rootScope.stopLoader();
												growl.error(
														response.data.message,
														{
															title : 'Error!'
														});
											}
										});
					} catch (e) {
						console.log("Exception", e);
					}
				}

				/** Crud Operations End* */
			} ]);