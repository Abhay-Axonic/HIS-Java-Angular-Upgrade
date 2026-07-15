(function () {
    "use strict";

    function BoxMasters($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl,PagerService,BillingGenericService,GenericServiceFileUpload) {
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
          $scope.boxCode="";
          $scope.boxDescription="";
       }

        $scope.saveBox=function(){

        if($scope.boxCode==undefined || $scope.boxCode==null || $scope.boxCode=="" ){
            growl.error("Please Enter Box Code",
                {
                    title : 'Error!'
                });
            	 return false;
        }
        if($scope.boxDescription==undefined || $scope.boxDescription==null || $scope.boxDescription=="" ){
                    growl.error("Please Enter Box Description",
                        {
                            title : 'Error!'
                        });
                    	 return false;
                }
             var saveObj={
                 boxCode:$scope.boxCode,
                 boxDescription:$scope.boxDescription,
                 unitId:$scope.unitId,
                 organizationId :$scope.organizationId,
                 updatedBy :$scope.userId,
                 createdBy :$scope.userId,
                 status :'A'
             }

             var URI1 = BASE_URL + ROOT_URL +"/mrdbox/saveMrdBox";
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
                		$scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
                		console.log(response);
                        $scope.getBox();
                        initializeController();
                        $scope.clearFunction();
                        $rootScope.stopLoader();
                	    	});
        }


            $scope.getBox=function(){
                 var getObj={
                     unitId:$scope.unitId,
                     organizationId :$scope.organizationId,
                     page :$scope.offset,
                     size:$scope.noOfRecordsPerPage
                 }

                 var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
                     $rootScope.startLoader();
                            GenericService.serviceAction("POST", URI2, getObj).then(function (response) {
                               $scope.getMrd=[];
                               $scope.getMrd=response.data.listObject;
                               $scope.commonListCount = response.data.object;
                               $scope.setNoOfRecords();
                                $scope.setPage(1, false);
                                console.log(response);
                             $rootScope.stopLoader();
                                });
            }
            $scope.getBox();


	$scope.setNoOfRecords = function() {
		$scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
	};

	$scope.initBoxGroupMasterList = function(offset, noOfRecordsPerPage){
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
		var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
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
		var URI2 = BASE_URL + ROOT_URL +"/mrdbox/getMrdBoxList";
		GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                                       $scope.getMrd=[];
                                       $scope.getMrd=response.data.listObject;
                                      $scope.unitDeptMasterList = response.data.listObject;
                                      $scope.commonListCount = response.data.object;
                                      //$scope.setPage(1, false);
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
	$scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);

             $scope.viewList = [];
             $scope.viewBtn=function (data){
             if(data.status=='I'){
                          					growl.error("Please update status then you can edit.",
                                          {
                                              title : 'Error!'
                                          });
                                      	 return false;
                          				 }
             console.log(data)
               var URI =  BASE_URL+ROOT_URL+"/mrdbox/getMrdBoxById/"+data.boxId+"/"+$scope.organizationId+"/"+$scope.unitId;
               GenericService.serviceAction("GET", URI,{}).then(
               							                function (response) {
               							                    $scope.viewList=response;
               							                    console.log($scope.viewList)
                                                            $scope.boxID=response.data.object.boxId;
                                                            $scope.boxCode=response.data.object.boxCode;
                                                            $scope.boxDescription=response.data.object.boxDescription;
                                                            $scope.saveBtnFlag=true;
                                                            $scope.updateBtnFlag=false;
               							                });
            }

            $scope.showUpdateBtn=function(value){
            		var objUpdate ={
            		         boxId :$scope.boxID,
            		         boxCode: $scope.boxCode,
            		         boxDescription:$scope.boxDescription,
            		         status:'A',
            				 organizationId :$scope.organizationId,
            				 unitId :$scope.unitId,
            				 updatedBy :$scope.userId,
                             createdBy :$scope.userId,
            			}
            			var URI2 = BASE_URL + ROOT_URL +"/mrdbox/updateMrdBox";
            			GenericService.serviceAction("PUT", URI2, objUpdate).then(function (response) {
                                                       if (response.data.status == 'success'){
                                                                                growl.success(response.data.message,
                                                                                   {
                                                                                       title : 'Success!'
                                                                                   });
                                                                       		}
                                                        $scope.initBoxGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
                                                        $scope.clearFunction();
                                                       $scope.getBox();
                                                       $scope.saveBtnFlag=false;
                                                       $scope.updateBtnFlag=true
                                                        console.log(response);
                                                       });

            }

            	$scope.updateStatus = function(boxId,boxCode,boxDescription,id, status){
            		var URI2 = BASE_URL + ROOT_URL +"/mrdbox/updateMrdBoxStatus";
            		$scope.currentDate = new Date();
            		var objStatus = {
            				 boxId :boxId,
                             boxCode: boxCode,
                             boxDescription:boxDescription,
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
                           $scope.getBox();
                            console.log(response);
                           });
            	}

}


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("boxMasters", {
            url: "/boxMasters",
            title: 'MRD Box Master',
            templateUrl: 'views/master/UnitMaster/organizationMaster/box-master.html',
            controller: "BoxMasters.Controller",
             resolve : {
                        data: function($q, $state, $timeout) {
                            var deferred = $q.defer();
                            $timeout(function() {
                                var arr = JSON.parse(localStorage.getItem('permissions'))
                                .modules.filter(function(item){
                                   return (item.id == 1069);
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
        .controller("BoxMasters.Controller", BoxMasters);
})();