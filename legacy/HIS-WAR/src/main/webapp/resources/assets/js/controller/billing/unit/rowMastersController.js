(function () {
    "use strict";

    function RowMasters($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;


       $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.updateBtnFlag=true;

        	/**Variables for pagination */
        	$scope.offset = 0;
        	$scope.noOfRecordsPerPage = 10;
        	$scope.unitDeptMasterList = [];
        	$scope.totalNoOfRecords;
        	$scope.searchKeyword;
        	$scope.commonListCount;
        	$scope.pager = {};
        	$scope.page;

       $scope.clearFunction=function(){
          $scope.rowCode="";
          $scope.rowDescription="";
       }

        $scope.saveRow=function(){

        if($scope.rowCode==undefined || $scope.rowCode==null || $scope.rowCode=="" ){
            growl.error("Please Enter Row Code",
                {
                    title : 'Error!'
                });
            	 return false;
        }
        if($scope.rowDescription==undefined || $scope.rowDescription==null || $scope.rowDescription=="" ){
                    growl.error("Please Enter Row Description",
                        {
                            title : 'Error!'
                        });
                    	 return false;
                }
         console.log($scope.unitId)
         console.log($scope.organizationId)
             var saveObj={
                 rowCode:$scope.rowCode,
                 rowDescription:$scope.rowDescription,
                 unitId:$scope.unitId,
                 organizationId :$scope.organizationId,
                 updatedBy :$scope.userId,
                 createdBy :$scope.userId,
                 status :'A'
             }

             var URI1 = BASE_URL + ROOT_URL +"/mrdrow/saveMrdRow";
                 $rootScope.startLoader();
                		GenericService.serviceAction("POST", URI1, saveObj).then(function (response) {
                		if (response.data.status == 'success'){
                         growl.success(response.data.message,
                            {
                                title : 'Success!'
                            });
                		}
                		else if(response.data.status =='error'){
                        growl.error(response.data.message,
                        {
                            title : 'Error!'
                        });
                        }
                		//$scope.initRowGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
                		console.log(response);
                        $scope.getRow();
                        initializeController();
                        $scope.clearFunction();
                        $rootScope.stopLoader();
                	    	});
        }


            $scope.getRow=function(){
             console.log($scope.unitId)
             console.log($scope.organizationId)
                 var getObj={
                     unitId:$scope.unitId,
                     organizationId :$scope.organizationId,
                     page :$scope.offset,
                     size:$scope.noOfRecordsPerPage
                 }

                 var URI2 = BASE_URL + ROOT_URL +"/mrdrow/getMrdRowList";
                     $rootScope.startLoader();
                            GenericService.serviceAction("POST", URI2, getObj).then(function (response) {
                               $scope.getMrd=[];
                               $scope.getMrd=response.data.listObject;
                               $scope.commonListCount = response.data.object;
                                $scope.setPage(1, false);
                                console.log(response);
                                console.log(response);
                             $rootScope.stopLoader();
                                });
            }
            $scope.getRow();


	$scope.setNoOfRecords = function() {
		$scope.initRowGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
	};

	$scope.initRowGroupMasterList = function(offset, noOfRecordsPerPage){
		var obj1 = {
				organizationId :$scope.organizationId,
				unitId        :$scope.unitId,
				page        :offset != null ? offset : 0,
                size :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
			}

		var obj2 = {
				organizationId :$scope.organizationId,
				 unitId        :$scope.unitId
			}
		var URI2 = BASE_URL + ROOT_URL +"/mrdrow/getMrdRowList";
		GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                                       $scope.getMrd=[];
                                       $scope.getMrd=response.data.listObject;
                                         $scope.unitDeptMasterList = response.data.listObject;
                                         $scope.commonListCount = response.data.object;
                                         $scope.setPage(1, false);
                                        console.log(response);
                                     $rootScope.stopLoader();
                                        });
	}

    $scope.masterList = function(offset, noOfRecordsPerPage) {
		var obj1 = {
				organizationId :$scope.organizationId,
				unitId        :$scope.unitId,
				page        :offset != null ? offset : 0,
                size :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
			}

		var obj2 = {
				organizationId :$scope.organizationId,
				 unitId        :$scope.unitId
			}
		var URI2 = BASE_URL + ROOT_URL +"/mrdrow/getMrdRowList";
		GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                                       $scope.getMrd=[];
                                       $scope.getMrd=response.data.listObject;
                                         $scope.unitDeptMasterList = response.data.listObject;
                                         $scope.commonListCount = response.data.object;
                                        // $scope.setPage(1, false);
                                        console.log(response);
                                     $rootScope.stopLoader();
                                        });
                }

	$scope.setPage = function(page, flag) {
		if (page < 1 || page > $scope.pager.totalPages)
		{
			return;
		}
		$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
		if (flag)
		{
			$scope.masterList($scope.pager.startIndex, $scope.pager.pageSize);
		}
	}
	$scope.initRowGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);





             $scope.viewList = [];
             $scope.viewBtn=function (data){
             console.log(data)
             if(data.status=='I'){
                          					growl.error("Please update status then you can edit.",
                                          {
                                              title : 'Error!'
                                          });
                                      	 return false;
                          				 }
               var URI =  BASE_URL+ROOT_URL+"/mrdrow/getMrdRowById/"+data.rowId+"/"+$scope.organizationId+"/"+$scope.unitId;
               GenericService.serviceAction("GET", URI,{}).then(
               							                function (response) {
               							                    $scope.viewList=response;
               							                    console.log($scope.viewList)
                                                            $scope.rowID=response.data.object.rowId;
                                                            $scope.rowCode=response.data.object.rowCode;
                                                            $scope.rowDescription=response.data.object.rowDescription;
                                                            $scope.saveBtnFlag=true;
                                                            $scope.updateBtnFlag=false;
               							                });
            }

            $scope.showUpdateBtn=function(value){
            		var objUpdate ={
            		         rowId :$scope.rowID,
            		         rowCode: $scope.rowCode,
            		         rowDescription:$scope.rowDescription,
            		         status:'A',
            				 organizationId :$scope.organizationId,
            				 unitId :$scope.unitId,
            				 updatedBy :$scope.userId,
                             createdBy :$scope.userId,
            			}
            			var URI2 = BASE_URL + ROOT_URL +"/mrdrow/updateMrdRow";
            			GenericService.serviceAction("PUT", URI2, objUpdate).then(function (response) {
                                                       if (response.data.status == 'success'){
                                                                                growl.success(response.data.message,
                                                                                   {
                                                                                       title : 'Success!'
                                                                                   });
                                                                       		}
                                                        $scope.initRowGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
                                                        $scope.clearFunction();
                                                       $scope.getRow();
                                                       $scope.saveBtnFlag=false;
                                                       $scope.updateBtnFlag=true
                                                        console.log(response);
                                                       });

            }

            	$scope.updateStatus = function(rowId,rowCode,rowDescription,id, status){
            		var URI2 = BASE_URL + ROOT_URL +"/mrdrow/updateMrdRowStatus";
            		$scope.currentDate = new Date();
            		var objStatus = {
            				 rowId :rowId,
                             rowCode: rowCode,
                             rowDescription:rowDescription,
                             status:status,
                             organizationId :$scope.organizationId,
                             unitId :$scope.unitId,
                             updatedBy :$scope.userId,
                             createdBy :$scope.userId,
            		}
            		GenericService.serviceAction("PUT", URI2, objStatus).then(function (response) {
                                                                           if (response.data.status == 'success'){
                                                                                                    growl.success(response.data.message,
                                                                                                       {
                                                                                                           title : 'Success!'
                                                                                                       });
                                                                                           		}
                                                                            $scope.clearFunction();
                                                                           $scope.getRow();
                                                                            console.log(response);
                                                                           });
            	}

}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("rowMasters", {
            url: "/rowMasters",
            title: 'MRD Row Master',
            templateUrl: 'views/master/UnitMaster/organizationMaster/row-master.html',
            controller: "RowMasters.Controller",
             resolve : {
                        data: function($q, $state, $timeout) {
                            var deferred = $q.defer();
                            $timeout(function() {
                                var arr = JSON.parse(localStorage.getItem('permissions'))
                                .modules.filter(function(item){
                                   return (item.id == 1070);
                                });
                                if(arr.length <= 0){
                                    $state.go("accessDenied")
                                    deferred.reject();
                                 } else {
                                    deferred.resolve();
                                 }
                            });

                            return deferred.promise;
                          }
                     },
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("RowMasters.Controller", RowMasters);
})();