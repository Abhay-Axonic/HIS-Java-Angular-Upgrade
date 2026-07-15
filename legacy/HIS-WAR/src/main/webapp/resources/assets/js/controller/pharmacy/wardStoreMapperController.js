/**
 * @author Nikita Chintawar
 */

angular.module('myApp').controller("wardStoreMapperController",['$scope','$http','$localStorage','$sessionStorage','$cookies',
    '$rootScope','GenericService','PagerService','promiseFactory','BillingGenericService','GenericServiceFileUpload',
    'reportService','growl','$state','$stateParams','PagerService',
    function($scope, $http, $localStorage, $sessionStorage,$cookies, $rootScope, GenericService,promiseFactory,PagerService,
    BillingGenericService,GenericServiceFileUpload,reportService, growl, $state,$stateParams,PagerService,$modalInstance) 
    {
        $rootScope.loginpage = true;
        $scope.LISDynamicLabel = "Machine ";
        $scope.saveBtnFlag = true;
        $scope.updateBtnFlag = false;

        /** Variables for pagination */
        $scope.offset = 0;
        $scope.noOfRecordsPerPage = 10;
        $scope.commonList = [];
        $scope.totalNoOfRecords;
        $scope.searchKeyword;
        $scope.commonListCount;

        /** * Check user is Loggedin */
        var cookieObject, cookieObjectAppConfig;
        [cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
        $scope.unitId = cookieObject.unitId;
        $scope.orgId = cookieObject.organizationId;
        $scope.userId = cookieObject.userId;
        var data = {};
                    
        $scope.initCommon = function() {
            $scope.saveBtnFlag = true;
            $scope.updateBtnFlag = false;
            $scope.wardObj = {
                "storeId" : '',
                "wardId" : '',
                "wardName" :'',
                "storeName":'',
                "id" : '',
                "createdBy" : $scope.userId,
                "createdDate" : "",
                "updatedBy" : $scope.userId,
                "updatedDate" : "",
                "orgId" : $scope.orgId,
                "unitId" : $scope.unitId,
                "status":'A'
            }
        };
        $scope.initCommon();

        /**List And Pagination Start**/
		$scope.setNoOfRecords = function() {
			$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
		};
		
		$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
		{
			$rootScope.startLoader();
			
			offset = offset != null ? offset : 0;
			noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
			var data1 = {
					    orgId:organizationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
			
			var data2 = {
					      orgId:organizationId,
					      unitId:unitId
						};
						var URI1 = BASE_URL + ROOT_URL + "/api/globals/getWardStoremappedListPagination"; 
			var URI2 = BASE_URL + ROOT_URL + "/api/globals/getWardStoremappedListPaginationCount";
				
			
			GenericService.serviceAction("POST",URI1,data1).then(function(response){
				if(response.data.status == "success"){
				$rootScope.stopLoader();					
				$scope.getWardStoremappedList = response.data.listObject;
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
					orgId:organizationId,
					offset:offset,
					noOfRecordsPerPage:noOfRecordsPerPage,
					unitId:unitId
					};
			
			var URI = BASE_URL + ROOT_URL +  "/api/globals/getWardStoremappedListPagination";
			console.log("URI", URI);
			GenericService.serviceAction("POST", URI,data).then(function(response) {
				$rootScope.stopLoader();
				$scope.getWardStoremappedList = response.data.listObject;
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
		
        $scope.getStoreList = [];
        $scope.storeList = function() {
            var data={};
            var URI = BASE_URL + ROOT_URL + "/api/global/user/stores"+"/"+$scope.organizationId  + '/'+$scope.unitId+"/"+$scope.userId;
            GenericService.serviceAction("GET", URI, data).then(function(response) {
                if (response.status == 200) 
                {
                    $rootScope.stopLoader();
                    $scope.getStoreList = response.data;
                }
            });
        }
        $scope.storeList();

        $scope.getWardList = [];
        $scope.wardList = function() {
            var data = {
                organizationId :$scope.orgId,
                unitId :$scope.unitId
            };
            var URI = BASE_URL + ROOT_URL + "/api/adt/getActiveWardMasterList";
            GenericService.serviceAction("POST", URI, data).then(function(response) {
                if (response.status == 200) 
                {
                    $rootScope.stopLoader();
                    $scope.getWardList = response.data.listObject;
                }
            });				    
        }
        $scope.wardList();			

       /* $scope.getWardStoremappedList = [];

        $scope.wardStoremappedList = function() {
            $rootScope.startLoader();
            var data={
                orgId :$scope.orgId,
                unitId :$scope.unitId
            };
            var URI = BASE_URL + ROOT_URL + "/api/globals/getWardStoremappedList";
            GenericService.serviceAction("POST", URI, data).then(function(response) {
                if (response.status == 200) {
                    $rootScope.stopLoader();
                    $scope.getWardStoremappedList = response.data.listObject;
                }
                else
                {
                    $rootScope.stopLoader();
                }
            });    
        }
        $scope.wardStoremappedList();*/

        //save fuctionality
        $scope.saveWardStoreMapper = function() 
        {
            $scope.$broadcast('show-errors-check-validity');
            try 
            {
            	if($scope.wardObj.id == '' || $scope.wardObj.id == null || $scope.wardObj.id == undefined){
            		  growl.warning("Please select store..!",
                              {
                                  title : 'Warning!'
                              });
            		  return false;
            	}else if($scope.wardObj.wardId == '' || $scope.wardObj.wardId == null || $scope.wardObj.wardId == undefined){
            		  growl.warning("Please select ward..!",
                              {
                                  title : 'Warning!'
                              });
            		  return false;
            	}else{
                $rootScope.startLoader();       
                var URI = BASE_URL + ROOT_URL + "/api/globals/saveWardStoremapper";
                GenericService.serviceAction("POST", URI,$scope.wardObj).then(function(response) {
                    if (response.data.status == 'success') 
                    {
                        $rootScope.stopLoader();
                        growl.success("Department Store Mapped Save Successfully..!",
                        {
                            title : 'Success!'
                        });
                        $scope.$broadcast('show-errors-reset');
                        $scope.wardStoremappedList();
                        $scope.initCommon();
                        $scope.initWardStoreMapped($scope.orgId,$scope.offset,$scope.noOfRecordsPerPage);
                    } 
                    else if (response.data.status == 'error') 
                    {
                        $rootScope.stopLoader();
                        growl.error(response.data.message,
                        {
                            title : 'Error!'
                        });
                        $scope.initCommon();
                        $scope.initWardStoreMapped($scope.orgId,$scope.offset,$scope.noOfRecordsPerPage);
                    }
                });
            }
            } catch (e) 
            {
                console.log("Exception", e.message);
                $rootScope.stopLoader();
            }
        }

        // update Status
        $scope.updateStatus = function(wardStoreMapperId, status) 
        {
            try 
            {
                $rootScope.startLoader();
                $scope.initWardStoreMapped();
                var URI = BASE_URL + ROOT_URL + API + "/globals/activeInactiveWardStoreMapper" + S + $scope.orgId + S + $scope.unitId + S + wardStoreMapperId + S + status;
                GenericService.serviceAction("GET", URI,$scope.wardObj).then(function(response) {
                    if (response.data.status == 'success') {
                        $rootScope.stopLoader();
                        growl.success("Status Update Successfully..!",
                        {
                            title : 'Success!'
                        });
                        $scope.$broadcast('show-errors-reset');
                        $scope.initCommon();
                        $scope.wardStoremappedList();
                    } 
                    else if (response.data.status == 'error') 
                    {
                        $rootScope.stopLoader();
                        growl.error(response.data.message,
                        {
                            title : 'Error!'
                        });
                    }
                });
            } catch (e) {
                console.log("Exception", e.message);
                $rootScope.stopLoader();
            }
        }

        //edit function 
        $scope.showUpdateBtn = function(wardStoreMapperId) {
            try 
            {
                $rootScope.startLoader();
                $scope.saveBtnFlag = false;
                $scope.updateBtnFlag = true;
                var URI = BASE_URL + ROOT_URL + API + "/globals/getWardStoreMapperById" + S + $scope.orgId + S + $scope.unitId + S + wardStoreMapperId;
                GenericService.serviceAction("GET", URI,$scope.common).then(function(response) {
                    if (response.data.status == 'success') {
                        $rootScope.stopLoader();    
                        $scope.wardObj = response.data.object;
                    } 
                    else if (response.data.status == 'error') 
                    {
                        $rootScope.stopLoader();
                        alert("Error In Fetching Data");
                    }
                });
            } catch (e) {
                console.log("Exception", e.message);
            }
        }

        $scope.initWardStoreMapped = function() {
            $scope.wardStoreMapped = {
                "wardStoreMapperId" : $scope.wardObj.wardStoreMapperId,
                "storeId" : $scope.wardObj.storeId,
                "wardId" : $scope.wardObj.wardId,
                "wardName" :$scope.wardObj.wardName,
                "storeName":$scope.wardObj.storeName,
                "id" : $scope.wardObj.id,
                "status" : "A",
                "createdBy" : $scope.userId,
                "createdDate" : "",
                "updatedBy" : $scope.userId,
                "updatedDate" : "",
                "orgId" : $scope.orgId,
                "unitId" : $scope.unitId
            }
        };

        //Update fuctionality
        $scope.updateWardStoreMapper = function() {
            $scope.wardObj.orgId=$scope.orgId;
            $scope.wardObj.unitId=$scope.unitId;
            $scope.$broadcast('show-errors-check-validity');
            try 
            {
            	if($scope.wardObj.id == '' || $scope.wardObj.id == null || $scope.wardObj.id == undefined){
          		  growl.warning("Please select store..!",
                            {
                                title : 'Warning!'
                            });
          		  return false;
          	}else if($scope.wardObj.wardId == '' || $scope.wardObj.wardId == null || $scope.wardObj.wardId == undefined){
          		  growl.warning("Please select ward..!",
                            {
                                title : 'Warning!'
                            });
          		  return false;
          	}else{
                $rootScope.startLoader();      
                var URI = BASE_URL + ROOT_URL + "/api/globals/updateWardStoremapper";
                GenericService.serviceAction("POST", URI, $scope.wardObj).then(function(response) {
                    if (response.data.status == 'success') 
                    {
                        $rootScope.stopLoader();
                        growl.success("Department Strore Mapped Update successfully",
                        {
                            title : 'Success!'
                        });
                        $scope.$broadcast('show-errors-reset');
                        $scope.wardStoremappedList();
                        $scope.initCommon();
                        $scope.initWardStoreMapped($scope.orgId,$scope.offset,$scope.noOfRecordsPerPage);
                    } 
                    else if (response.data.status == 'error') 
                    {
                        $rootScope.stopLoader();
                        growl.error(response.data.message,
                        {
                            title : 'Error!'
                        });
                        $scope.initCommon();
                        $scope.initWardStoreMapped($scope.orgId,$scope.offset,$scope.noOfRecordsPerPage);
                    }
                });
            }
            } catch (e) {
                console.log("Exception", e.message);
            } 
        }
        
    } 
]);