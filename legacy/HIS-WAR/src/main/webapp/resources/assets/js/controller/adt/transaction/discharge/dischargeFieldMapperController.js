/**
 *
 */


angular.module('myApp')
		.controller("dischargeFieldMapperController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
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
			$scope.searchKeyword=null;
			$scope.commonListCount;

			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}

			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;

			$scope.listFieldDetails=[];
			$scope.selectDischargeSummaryTemplateList=[];
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;

				$scope.fieldDetailsMaster={
						dischargeFieldId:""
				 }
				$scope.common = {
						dischargeSummaryTempId:"",
						dischargeSummaryTempCode:"",
						dischargeSummaryTempDesc:"",
						departmentId:"",
						isHtml : 'N'

				};
				$scope.listLicenceDetailsMaster=[];
				var licenceDetailsMaster = angular.copy($scope.fieldDetailsMaster);
	        	$scope.listLicenceDetailsMaster.push(licenceDetailsMaster);
				$scope.fieldObject=[];

				$scope.listFieldDetails=[];
				var fieldDetailsMaster = angular.copy($scope.fieldDetailsMaster);
	        	$scope.listFieldDetails.push(fieldDetailsMaster);
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();


			//--------------------------------------------Discharge Field Mapper----------------------------------------------------//
			                //Discharge Field Dropdwon//
                            $scope.getFieldList= function()
                            {
                                var URI=BASE_URL + ROOT_URL+ GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MAPPER_MASTER+"/"+$scope.organizationId;
                                GenericService
                                    .serviceAction(METHOD_GET, URI,
                                            NOTIFICATION_MSG_STATUS_FALSE)
                                    .then(
                                            function(response){

                                            $scope.summaryFieldList = [];
                                            if(response.data.status=="success")
                                                 $scope.summaryFieldList = response.data.listObject;
                                            console.log("summaryFieldList",$scope.summaryFieldList);
                                            });
                            }
                            $scope.getFieldList();
                            //end summary field list

                         $scope.clearData= function (){
                                    $scope.dischargeFieldId="";
                                    $scope.optionalName="";
                         }

                            // Save Function//
                            $scope.save = function(){

                                    var date = new Date();
                                    $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
                                    $scope.fieldListTemplate = [];
                                    var data={

                                            dischargeFieldId:$scope.dischargeFieldId,
                                            dischargeChangedName:$scope.optionalName,
                                            organizationId:$scope.organizationId,
                                            unitId:$scope.unitId,
                                            createdBy:$scope.createdBy,
                                            updatedBy:$scope.updatedBy,
                                            status:'A',
                                            createdDate:$scope.dt,
                                            updatedDate:$scope.dt,
                                        }

                                        if($scope.dischargeFieldId== undefined || $scope.dischargeFieldId=="" || $scope.dischargeFieldId==null)
                                          {
                                             growl.error("Please Select Discharge Field.",
                                                 {
                                                      title : 'Error !'
                                                 });
                                                 return false;
                                           }if($scope.optionalName== undefined || $scope.optionalName=="" || $scope.optionalName==null)
                                          {
                                             growl.error("Please Select Optional Name.",
                                                 {
                                                      title : 'Error !'
                                                 });
                                                 return false;
                                           }

                                    var URI = BASE_URL + ROOT_URL + "/adt/saveDischargeFieldUnitWise";
                                    console.log("Save-URI-",URI);
                                    console.log(" save OBJ",data);

                                    GenericService.serviceAction("POST",URI,data).then(function(response){

                                        if (response.data.status == 'success')
                                        {
                                            growl.success(response.data.message,
                                                    {
                                                        title : 'Success!'
                                                    });
                                                $scope.clearData();

                                            $scope.initMaster();
                                            $scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
                                            $scope.fieldListObj=[];
                                            $scope.selectedSpeacilityList=[];
                                        } else if(response.data.status == 'error')
                                        {
                                            growl.error(response.data.message,
                                                    {
                                                        title : 'Error!'
                                                    });
                                        }
                                        });
                                        $rootScope.stopLoader();

                            }
                                 $scope.setNoOfRecords = function() {
                            				$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
                            			};

                            $scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
                                {
                                    $rootScope.startLoader();
                                    offset = offset != null ? offset : 0;
                                    noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;

                                    var data1 = {
                                                    organizationId:organizationId,
                                                    unitId:unitId,
                                                    offset:offset,
                                                    noOfRecordsPerPage:noOfRecordsPerPage,
                                                    searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null
                                                };

                                    var data2 = {
                                                  organizationId:organizationId,
                                                  unitId:unitId,
                                                  searchKeyword: ($scope.searchKeyword != null) ? $scope.searchKeyword : null
                                                };

                                    var searchKey = ($scope.searchKeyword != undefined && $scope.searchKeyword != null && $scope.searchKeyword != "" ) ? $scope.searchKeyword : null;

                                    var URI1 = BASE_URL + ROOT_URL + "/adt/getDischargeFieldUnitWiseList";
                                    var URI2 = BASE_URL + ROOT_URL + "/adt/getDischargeFieldUnitWiseCount/"+organizationId+"/"+unitId+"/"+searchKey;

                                    GenericService.serviceAction("POST",URI1,data1).then(function(response){
                                        if(response.data.status == "success"){
                                        $rootScope.stopLoader();
                                        $scope.commonList = response.data.listObject;
                                        $scope.commonListCount = response.data.listObject.length;
                                        $scope.setPage(1,false);

                                        console.log("DischargeList",$scope.commonList);
                                        }
                                    });

                                    GenericService.serviceAction("GET",URI2,data2).then(function(response){
                                        if(response.data.status == "success"){
                                            $rootScope.stopLoader();
                                            $scope.commonListCount = response.data.object;
                                            $scope.setPage(1,false);
                                            console.log("commonListCount",response.data.object);
                                        }
                                    });
                                  }


                                  $scope.searchDischargeFieldMapper = function(searchKeyword){

                                      $scope.loadingMsg="Loading...";
                                      if(searchKeyword.length>0){

                                          $scope.commonList = [];
                                          $rootScope.startLoader();
                                          var data1 = {
                                                      organizationId:$scope.organizationId,
                                                      unitId:$scope.unitId,
                                                      offset:$scope.offset,
                                                      noOfRecordsPerPage:$scope.noOfRecordsPerPage,
                                                      searchKeyword: searchKeyword
                                                      };

                                          var data2 = {
                                                      organizationId:$scope.organizationId,
                                                      unitId:$scope.unitId,
                                                      searchKeyword: searchKeyword
                                                      };

                                          var URI1 = BASE_URL + ROOT_URL + "/adt/getDischargeFieldUnitWiseList";
                                          var URI2 = BASE_URL + ROOT_URL + "/adt/getDischargeFieldUnitWiseCount/"+$scope.organizationId+"/"+$scope.unitId+"/"+searchKeyword;

                                          GenericService.serviceAction("POST",URI1,data1).then(function(response){
                                              if(response.data.status == "success"){
                                              $rootScope.stopLoader();
                                              $scope.commonList = response.data.listObject;
                                              $scope.commonListCount = response.data.listObject.length;
                                              $scope.setPage(1,false);

                                              console.log("DischargeList",$scope.commonList);
                                              }
                                          });

                                          GenericService.serviceAction("GET",URI2,data2).then(function(response){
                                              if(response.data.status == "success"){
                                                  $rootScope.stopLoader();
                                                  $scope.commonListCount = response.data.object;
                                                  $scope.setPage(1,false);
                                                  console.log("commonListCount",response.data.object);
                                              }
                                          });
                                      }
                                      if(searchKeyword == undefined || searchKeyword == null || searchKeyword ==""){
                                          $scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
                                      }
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

//                                        var URI = BASE_URL + ROOT_URL +  GET_CABIN_MASTER_LIST;
                                        var URI = BASE_URL + ROOT_URL + "/adt/getDischargeFieldUnitWiseList";
                                        console.log("URI", URI);
                                        GenericService.serviceAction("POST", URI,data).then(function(response) {
                                            $rootScope.stopLoader();
                                            $scope.commonList = response.data.listObject;
                                            console.log("commonList",$scope.commonList);
                                        });
                                    }

//                                        $scope.initMasterList();

                                        $scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);


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

//                                        var URI = BASE_URL + ROOT_URL +  GET_CABIN_MASTER_LIST;
                                        var URI = BASE_URL + ROOT_URL + "/adt/getDischargeFieldUnitWiseList";
                                        console.log("URI", URI);
                                        GenericService.serviceAction("POST", URI,data).then(function(response) {
                                            $rootScope.stopLoader();
                                            $scope.commonList = response.data.listObject;
                                            console.log("commonList",$scope.commonList);
                                        });
                                    }

//                                        $scope.initMasterList();
                               			$scope.pager = {};
                               			$scope.page;

                               			$scope.setPage = function(page, flag) {

                               			    if($scope.pager.totalPages==0){
                                                $scope.pager.totalPages=undefined;
                                            }
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

                                $scope.saveFieldList=[];
                                $scope.showUpdateBtn = function(id){
                                          //$rootScope.startLoader();
                                          $scope.saveBtnFlag = false;
                                          $scope.updateScope = true;
                                          $scope.updateBtnFlag = true;

                                        var URI = BASE_URL + ROOT_URL + "/adt/getDischargeFieldUnitWiseById" +"/"+ id;
                                        console.log("EDIT",URI);
                                        GenericService.serviceAction("GET",URI).then(function(response){
                                            console.log("Edit",response);
                                            if(response.data.status == "success"){
                                                $rootScope.stopLoader();
                                                $scope.dischargeId=response.data.object.dischargeFieldUnitWiseId.toString();
                                                $scope.dischargeFieldId=response.data.object.dischargeFieldId.toString();
                                                $scope.optionalName=response.data.object.dischargeChangedName;
                                                $scope.common.dischargeSummaryTempId=response.data.object.dischargeSummaryTempId;
                                            }else if(response.data.status == 'error'){
                                                $rootScope.stopLoader();
                                                growl.error(response.data.message,
                                                        {
                                                            title : 'Error!'
                                                        });
                                            }
                                            console.log("fdfd-",$scope.fieldListObj);
                                        });
                                }


                                			$scope.update = function(){
                                				//try{

                                					var date = new Date();
                                					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
                                					//$rootScope.startLoader();

                                					$scope.previousSavedList=[];
                                					$scope.deptEditedList=[];
                                					$scope.fieldListTemplate = [];

                                					var data={
                                					            dischargeFieldUnitWiseId:$scope.dischargeId,
                                							    dischargeFieldId:$scope.dischargeFieldId,
                                                                dischargeChangedName:$scope.optionalName,
                                                                organizationId:$scope.organizationId,
                                                                unitId:$scope.unitId,
                                                                createdBy:$scope.createdBy,
                                                                updatedBy:$scope.updatedBy,
                                                                status:'A',
                                                                createdDate:$scope.dt,
                                                                updatedDate:$scope.dt,

                                						}

                                					var URI = BASE_URL + ROOT_URL + "/adt/updateDischargeFieldUnitWise";
                                					console.log("Update-URI-",URI);
                                					console.log("Update OBJ",data);
                                					$rootScope.startLoader();
                                					GenericService.serviceAction("POST",URI,data).then(function(response){

                                						if (response.data.status == 'success')
                                						{
                                							$rootScope.stopLoader();
                                							growl.success(response.data.message,
                                									{
                                										title : 'Success!'
                                									});
                                							$scope.clearData();
                                							$scope.initMaster();
//                                							$scope.initMasterList();
                                                            $scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
                                							$scope.fieldListObj=[];
                                						}

                                					});
                                			}

                                			$scope.updateStatus = function(dischargeFieldUnitWiseId,status){

                                            var date = new Date();
                                            $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');

                                            var data={
                                                updatedBy:$scope.updatedBy,
                                                updatedDate:$scope.dt,
                                                status:status,
                                                dischargeFieldUnitWiseId:dischargeFieldUnitWiseId
                                            }
                                            $rootScope.startLoader();
                                            var URI = BASE_URL + ROOT_URL + "/adt/updateDischargeFieldUnitWiseStatus";
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

                                                    $scope.initMaster();
                                                    $scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
                                                }

                                            });
                                    }

}]);