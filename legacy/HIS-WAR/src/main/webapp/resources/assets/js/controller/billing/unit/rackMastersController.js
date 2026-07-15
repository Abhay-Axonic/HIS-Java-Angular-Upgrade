(function () {
    "use strict";

    function RackMasters($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService,PagerService, growl,BillingGenericService,GenericServiceFileUpload) {
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
          $scope.rackCode="";
          $scope.rackDescription="";
       }

        $scope.saveRack=function(){

        if($scope.rackCode==undefined || $scope.rackCode==null || $scope.rackCode=="" ){
            growl.error("Please Enter Rack Code",
                {
                    title : 'Error!'
                });
            	 return false;
        }
        if($scope.rackDescription==undefined || $scope.rackDescription==null || $scope.rackDescription=="" ){
                    growl.error("Please Enter Rack Description",
                        {
                            title : 'Error!'
                        });
                    	 return false;
                }
         console.log($scope.unitId)
         console.log($scope.organizationId)
             var saveObj={
                 rackCode:$scope.rackCode,
                 rackDescription:$scope.rackDescription,
                 unitId:$scope.unitId,
                 organizationId :$scope.organizationId,
                 updatedBy :$scope.userId,
                 createdBy :$scope.userId,
                 status :'A'
             }

             var URI1 = BASE_URL + ROOT_URL +"/mrdrack/saveMrdRack";
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
                		console.log(response);
                        $scope.getRack();
                        initializeController();
                        $scope.clearFunction();
                        $rootScope.stopLoader();
                	    	});
        }


            $scope.getRack=function(){
                 var getObj={
                     unitId:$scope.unitId,
                     organizationId :$scope.organizationId,
                     page :$scope.offset,
                     size:$scope.noOfRecordsPerPage
                 }

                 var URI2 = BASE_URL + ROOT_URL +"/mrdrack/getMrdRackList";
                     $rootScope.startLoader();
                            GenericService.serviceAction("POST", URI2, getObj).then(function (response) {
                               $scope.getMrd=[];
                               $scope.getMrd=response.data.listObject;
                                 $scope.commonListCount = response.data.object;
                                 $scope.setPage(1, false);
                                console.log(response);
                             $rootScope.stopLoader();
                                });
            }
            $scope.getRack();


	$scope.setNoOfRecords = function() {
		$scope.initRackGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
	};

	$scope.initRackGroupMasterList = function(offset, noOfRecordsPerPage){
	$scope.startLoader();
		var obj1 = {
				organizationId :$scope.organizationId,
				unitId        :$scope.unitId,
				page        :offset != null ? offset : 0,
				size :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
			}
		var URI2 = BASE_URL + ROOT_URL +"/mrdrack/getMrdRackList";
		GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                                       $scope.getMrd=[];
                                       $scope.getMrd=response.data.listObject;
                                        $scope.commonListCount = response.data.object;
                                        $scope.setPage(1, false);
                                        console.log(response);
                                     $rootScope.stopLoader();
                                        });
	}

	$scope.masterList = function(offset, noOfRecordsPerPage) {
            $scope.startLoader();
                var obj1 = {
                        organizationId :$scope.organizationId,
                        unitId        :$scope.unitId,
                        page        :offset != null ? offset : 0,
                        size :noOfRecordsPerPage != null ? noOfRecordsPerPage : 10
                    }
                var URI2 = BASE_URL + ROOT_URL +"/mrdrack/getMrdRackList";
                GenericService.serviceAction("POST", URI2, obj1).then(function (response) {
                                               $scope.getMrd=[];
                                               $scope.getMrd=response.data.listObject;
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
	$scope.initRackGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);





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
               var URI =  BASE_URL+ROOT_URL+"/mrdrack/getMrdRackById/"+data.rackId+"/"+$scope.organizationId+"/"+$scope.unitId;
               GenericService.serviceAction("GET", URI,{}).then(
               							                function (response) {
               							                    $scope.viewList=response;
               							                    console.log($scope.viewList)
                                                            $scope.rackID=response.data.object.rackId;
                                                            $scope.rackCode=response.data.object.rackCode;
                                                            $scope.rackDescription=response.data.object.rackDescription;
                                                            $scope.saveBtnFlag=true;
                                                            $scope.updateBtnFlag=false;
               							                });
            }

            $scope.showUpdateBtn=function(value){
            		var objUpdate ={
            		         rackId :$scope.rackID,
            		         rackCode: $scope.rackCode,
            		         rackDescription:$scope.rackDescription,
            		         status:'A',
            				 organizationId :$scope.organizationId,
            				 unitId :$scope.unitId,
            				 updatedBy :$scope.userId,
                             createdBy :$scope.userId,
            			}
            			var URI2 = BASE_URL + ROOT_URL +"/mrdrack/updateMrdRack";
            			GenericService.serviceAction("PUT", URI2, objUpdate).then(function (response) {
                                                       if (response.data.status == 'success'){
                                                                                growl.success(response.data.message,
                                                                                   {
                                                                                       title : 'Success!'
                                                                                   });
                                                                       		}
                                                        $scope.initRackGroupMasterList($scope.offset, $scope.noOfRecordsPerPage);
                                                        $scope.clearFunction();
                                                       $scope.getRack();
                                                       $scope.saveBtnFlag=false;
                                                       $scope.updateBtnFlag=true;
                                                        console.log(response);
                                                       });

            }

            	$scope.updateStatus = function(rackId,rackCode,rackDescription,id, status){
            		var URI2 = BASE_URL + ROOT_URL +"/mrdrack/updateMrdRackStatus";
            		$scope.currentDate = new Date();
            		var objStatus = {
            				 rackId :rackId,
                             rackCode: rackCode,
                             rackDescription:rackDescription,
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
                                                                           $scope.getRack();
                                                                            console.log(response);
                                                                           });
            	}

}


        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("rackMasters", {
            url: "/rackMasters",
            title: 'MRD Rack Master',
            templateUrl: 'views/master/UnitMaster/organizationMaster/rack-master.html',
            controller: "RackMasters.Controller",
             resolve : {
                        data: function($q, $state, $timeout) {
                            var deferred = $q.defer();

                            $timeout(function() {
                                var arr = JSON.parse(localStorage.getItem('permissions'))
                                .modules.filter(function(item){
                                   return (item.id == 1068);
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
        .controller("RackMasters.Controller", RackMasters);
})();