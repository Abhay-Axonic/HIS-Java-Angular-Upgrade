/**
 * 
 */

angular.module('myApp')
		.controller("loyaltyCardCreationMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader','reportService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader, reportService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.orgId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			$scope.initMaster = function(){
				
				$scope.firstNo = "";
				$scope.secondNo = "";
				$scope.thirdNo = "";
				$scope.fourthNo = "";
				
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						loyaltyCardId:"",
						cardNo:"",
						firstNo:"",
						secondNo:"",
						thirdNo:"",
						fourthNo:"",
						isDeleted:'N'
							
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
					      organisationId:organisationId,
							offset:offset,
							noOfRecordsPerPage:noOfRecordsPerPage,
							unitId:unitId
							};
				
				var data2 = {
						      organisationId:organisationId,
						      unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL + GET_LOYALTY_CARD_MASTER_LIST; 
				var URI2 = BASE_URL + ROOT_URL + GET_LOYALTY_CARD_MASTER_COUNT;
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
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
			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organisationId:organisationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL +  GET_LOYALTY_CARD_MASTER_LIST;
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
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
					$scope.masterList($scope.organisationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
					
					if($scope.popUpFlag==false)
						{
						  $rootScope.startLoader();
						}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');

					var data={	
							
							cardNo:$scope.common.cardNo,
							isDeleted:$scope.common.isDeleted,
							organisationId:$scope.organisationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt,
							isUserDefined:'Y'
						}
					console.log($scope.common.cardNo);
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
						
						if($scope.common.cardNo == ""){
							
							growl.warning("please generate card No ",
									{
										title : 'warning!'
									});
							$rootScope.stopLoader();
							return false;
						}else{
							var URI = BASE_URL + ROOT_URL + SAVE_LOYALTY_CARD_MASTER;
							console.log("Save-URI-",URI);
							console.log(" save OBJ",data); 
							
							GenericService.serviceAction("POST",URI,data).then(function(response){
								
								if (response.data.status == 'success')
								{
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
									$rootScope.stopLoader();
//									$scope.$broadcast('show-errors-reset');
									$scope.initMaster();
									$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								} else if(response.data.status == 'error')
								{
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
									$rootScope.stopLoader();
								}
							});
							$rootScope.stopLoader();
						}
					
				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
		
			
			$scope.showDetailsBtn=function(data){
				$scope.configDetails=data;
			}
			
		
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={
							status:status,
							isActive:'N',
							isDeleted:'N',
							loyaltyCardId:id,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt
					};
					
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + UPDATE_LOYALTY_CARD_MASTER_STATUS;
					console.log("update status",$scope.common);
					//return false;
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
			
			
			$scope.getRemovedAction = function(loyaltyCardId){
				
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={
							status:'A',
							loyaltyCardId:loyaltyCardId,
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							isDeleted:'Y'
					};
					
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + UPDATE_LOYALTY_CARD_MASTER_STATUS;
					console.log("update status",$scope.common);
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
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
			
			//------Generate Card No.------//
			
			$scope.getGeneratedCardNumber = function(loyaltyCardId)
			{
//				$scope.availableActiveDeposit = 0;
//				$scope.minStartAmount = 0;
				var URI = BASE_URL + ROOT_URL + "/api/unit/loyaltyCardNumber/"+$scope.orgId+"/"+$scope.unitId;
				BillingGenericService.serviceAction(METHOD_GET,URI,$scope.patientLoyaltiCardMapper,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.loyaltiCardObj = response.data.object;
						if($scope.loyaltiCardObj!=null)
							{ 
							$scope.cardNo = response.data.object.toString();
							$scope.firstNo = $scope.cardNo.substring(0, 4);
							$scope.secondNo = $scope.cardNo.substring(4, 8);
							$scope.thirdNo = $scope.cardNo.substring(8, 12);
							$scope.fourthNo = $scope.cardNo.substring(12, 16); 
							$scope.common.cardNo = response.data.object;
							}
						else{
							growl.error("No card Number Available",{
								title : ERROR_MSG
							});
						}
					
						
					}
				});
			}
			
			 $scope.printLoyaltyCardCreation=function(info)
			    {
			    //$scope.save();

			    var data = {
						    "appName": "HIS",
						    "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						    "reportDesignFilePath": "LoyaltyCardPrint.rpt",
						    "reportFormat": "PDF",
						    "exportFileName": "LoyaltyCardCreation",
						    "reportParams": [
											    {
												    "paramName": "loyalty_card_id",
												    "paramType": "Integer",
												    "paramValue": info.loyaltyCardId
								
											    }
											 ]
			    			};
			     reportService.generate(REPORT_API_URI1,data);

			    }
			/**Crud Operations End**/
}]);