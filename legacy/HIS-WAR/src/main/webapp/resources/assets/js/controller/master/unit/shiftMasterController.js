
angular
.module('myApp')
.controller(
		"shiftMasterController",
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
			'BillingGenericService',
			function($scope, $rootScope, $state, $cookies,GenericService, PagerService, promiseFactory,$sessionStorage, growl, BillingGenericService) {
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
				$scope.userId = cookieObject.userId;
				$scope.createdBy = cookieObject.userId;
				$scope.updatedBy = cookieObject.userId;

				// function for initializa clockpicker
				$scope.initClockPicker = function() {

					$('.clockpicker').clockpicker();
				}
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				
				$scope.initMaster = function() {
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.shiftMasterObj = {

							shiftId : "",
							shiftName : "",
							fromTime : "",
							toTime : "",
							isWeeklyOff:"",
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							createdDate:$scope.dt,
							updatedDate:$scope.dt,
							status:'A'

					};
					console.log("Shift Obj:", $scope.shiftMasterObj);
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

					var URI1 = BASE_URL + ROOT_URL+ GETSHIFTMASTERLIST;
					var URI2 = BASE_URL + ROOT_URL+ GETSHIFTMASTERCOUNT;

					GenericService.serviceAction("POST", URI1, data1).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.shiftMasterList = response.data.listObject;
									console.log("shiftMasterList:",$scope.shiftMasterList);
								}
							});

					GenericService.serviceAction("POST", URI2, data2).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.commonListCount = response.data.object;
									$scope.setPage(1,false);
									console.log("shiftListCount",response.data.object);
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

					var URI = BASE_URL + ROOT_URL+ GETSHIFTMASTERLIST;
					console.log("URI", URI);
					GenericService.serviceAction("POST", URI, data).then(function(response) {
								$rootScope.stopLoader();
								$scope.shiftMasterList = response.data.listObject;
								console.log("shiftMasterList:",$scope.shiftMasterList);
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

				
				//for SAVE data

				$scope.save = function() {
					try {

						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						 $scope.$broadcast('show-errors-check-validity');
					 if($scope.shiftForm.$valid || $scope.shiftForm.$valid){ 
						$scope.shiftMasterObj.status='A';
						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {

							//alert($scope.common.day)
							//return false;
							var URI = BASE_URL + ROOT_URL+ SAVESHIFTMASTER;
							console.log("Save-URI-", URI);
							console.log(" save OBJ", $scope.shiftMasterObj);
							$rootScope.startLoader();
							GenericService.serviceAction("POST", URI,$scope.shiftMasterObj).then(function(response) {

								if (response.data.status == 'success') {
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
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								   }
								$scope.$broadcast('show-errors-reset');
								$rootScope.stopLoader();
							});
							
						}
					  }
					} catch (e) {
						console.log("Exception-", e);
					}
				}

				$scope.showDetailsBtn = function(data) {
					$scope.configDetails = data;
				}

				$scope.showUpdateBtn = function(id) {
					try {
						$rootScope.startLoader();
						$scope.saveBtnFlag = false;
						$scope.updateScope = true;
						$scope.updateBtnFlag = true;
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ GETSHIFTMASTERBYID + S+ id + S + $scope.organizationId+ S + $scope.unitId;
						console.log("EDIT", URI);
						GenericService.serviceAction("GET", URI).then(function(response) {
									console.log("Edit",response);

									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.shiftMasterObj = response.data.object;
										$scope.shiftMasterObj.shiftId = response.data.object.shiftId;
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
					     $scope.$broadcast('show-errors-check-validity');
					   if($scope.shiftForm.$valid || $scope.shiftForm.$valid){ 
					     $rootScope.startLoader();
					     $scope.shiftMasterObj.status='A';
					     $scope.shiftMasterObj.organizationId=$scope.organizationId;
					     $scope.shiftMasterObj.unitId=$scope.unitId;
					     $scope.shiftMasterObj.updatedBy=$scope.userId;
					     $scope.shiftMasterObj.updatedDate=$scope.dt;
						var URI = BASE_URL + ROOT_URL+ UPDATESHIFTMASTER;
						console.log("Update-URI-", URI);
						console.log("Update OBJ",  $scope.shiftMasterObj);
						$rootScope.startLoader();
						GenericService.serviceAction("POST", URI,  $scope.shiftMasterObj).then(function(response) {

							if (response.data.status == 'success') {
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
								growl.error(
										response.data.message,
										{
											title : 'Error!'
										});
							}
							$rootScope.stopLoader();
							$scope.$broadcast('show-errors-reset');
						});
					 }
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

						$scope.shiftMasterObj.status = status,
						$scope.shiftMasterObj.shiftId = id,
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ UPDATESHIFTMASTERSTATUS;
						console.log("update status", $scope.shiftMasterObj);
						// return false;
						GenericService.serviceAction("POST", URI,$scope.shiftMasterObj).then(
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
						console.log("Exception", e);
					}
				}

				/** Crud Operations End* */
			} ]);