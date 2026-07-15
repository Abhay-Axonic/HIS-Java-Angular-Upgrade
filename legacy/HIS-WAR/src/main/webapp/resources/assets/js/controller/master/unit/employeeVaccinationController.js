(function () {
    "use strict";
    /**
     * @Author By  Akshay Narale
     */
    function employeeVaccination($scope, $rootScope, $http, $state, commonDetailService,GenericServiceParamHeader, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;


        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;
        $scope.updateBtnFlag=true;

//            $scope.openDatePickerSearch = function() {
//                $scope.datepicker5.opened = true;
//            };
//
//            $scope.datepicker5 = {
//                opened : false
//            };


                $scope.selectPatientId = function(info, model,label) {
                				$scope.selectedEmployeeId=info.userId;
                				//$scope.common.userId = info.userId;
                				//$scope.common.userId = info.userId;
                			}

             $scope.getListByName = function(name){

                 var URI = BASE_URL + ROOT_URL + "/api/global/searchEmployeeByNameAndRole";
                 var data = {
                      organizationId:$scope.organizationId,
                      unitId:$scope.unitId,
                      firstName:name,
                      roleId:$scope.role
                 }
                 if($scope.role !== undefined && $scope.role !== null && !isNaN($scope.role) && $scope.role !== ''){
                    return GenericService.serviceAction("POST",URI,data).then(function(response){
                        if (response.data.status == "success"){
                            console.log("getListByName", response.data.listObject);
                            return response.data.listObject;
                        }
                    });
                 }
                 else{
                     growl.warning("Please select role!",
                     {
                        title : "WARNING!"
                     });
                     return false;
                 }
             }

             				$scope.getActiveList = function(){
                                var URI = BASE_URL + ROOT_URL + "/api/global/getActiveUserRoleMasterList";

                                var data = {
                                          organisationId:$scope.organizationId,
                                          offset:0,
                                          noOfRecordsPerPage:100000000,
                                          unitId:$scope.unitId
                                }
                                GenericService.serviceAction("POST",URI,data).then(function(response){
                                    if(response.data.status == "success"){
                                    $rootScope.stopLoader();
                                    $scope.getActiveList = [];
                                    $scope.vaccinationListByRole=[];
                                    $scope.vaccinationListByRole=response.data.listObject;
                                    angular.forEach(response.data.listObject,
                                                    function(value,index) {
                                                        var data = {};
                                                        data.id = value.roleId;
                                                        data.label = value.roleName;
                                                        if($scope.isSuperUser=='Y')
                                                            {
                                                             $scope.getActiveList.push(data);
                                                            }
                                                        else
                                                            {
                                                            if(data.id!=response.data.object)
                                                                {
                                                                $scope.getActiveList.push(data);
                                                                }
                                                            }
                                                    });

                                    console.log("getActiveRoleList",$scope.getActiveList);
                                    }
                                });
                                }
                                $scope.getActiveList();

                                $scope.getRoleId=function(roleId,list){
                                   $scope.selectedRoleId=roleId;
                                    $scope.roleVaccination=[]
                                    angular.forEach($scope.vaccinationListByRole, function(value){
                                       if(value.roleId == $scope.selectedRoleId){
                                          $scope.roleVaccination = value.vaccinations;
                                       }
                                    });

                                     angular.forEach($scope.vaccinationListByRole, function(value){
                                       if(value.roleId == $scope.role){
                                          $scope.roleNameHeader = value.roleName;
                                       }
                                    });

                                     $scope.selectionVaccination=[];
                                     var vaccinationId = $scope.roleVaccination;
                                     angular.forEach(vaccinationId,function(value) {
                                                      $scope.selectionVaccination.push(value.vaccinationId);
                                                       	});
                                    console.log("vacci",$scope.roleVaccination)
                                    console.log("vacci",$scope.selectionVaccination)
                                }

                          //  search Employee Vaccination Function
                          $scope.searchEmployeeVaccination=function(){

                            if($scope.role==undefined || $scope.role==null || $scope.role==""){
                               growl.warning("Please Select Role..!",{title : "WARNING!"});
                               return false;
                            }
                             if($scope.empName==undefined || $scope.empName==null || $scope.empName==""){
                               growl.warning("Please Select Employee Name..!",{title : "WARNING!"});
                               return false;
                            }
                             
                            $scope.selectionVaccination.forEach(function(item, index) {
                                 if (typeof item === 'number') {
                                     $scope.selectionVaccination[index] = {
                                         vaccinationId: item,
                                         selectionDate: ''
                                     };
                                 } else if (typeof item === 'object') {
                                     if (!item.hasOwnProperty('vaccinationId')) {
                                         item.vaccinationId = '';
                                     }
                                     if (!item.hasOwnProperty('selectionDate')) {
                                         item.selectionDate = '';
                                     }
                                 }
                             });


                             var dataObj={
                                     roleId:$scope.selectedRoleId,
                                     userId:$scope.selectedEmployeeId,
                                     status:'A',
                                     organisationId:$scope.organizationId,
                                     unitId:$scope.unitId,
                                     vaccinationDto:$scope.selectionVaccination

                             }


                             var URI2 = BASE_URL + ROOT_URL + "/api/global/searchEmployeeVaccinationByRole";
                             $rootScope.startLoader();
                             GenericService.serviceAction("POST",URI2,dataObj).then(function(response){
                                 if(response.data.status == "success"){
                                
                                 $scope.vaccinationListByRoleList=response.data.listObject;
                                 if($scope.vaccinationListByRoleList.length==0){
                                         growl.warning("No Records Found!!!",{title : "WARNING!"});
                                         $rootScope.stopLoader();
                                          return false;
                                      }
                                 var i = 0;
                                 angular.forEach($scope.vaccinationListByRoleList,function(value) {
                                	 if(value.selectionDate != null && value.selectionDate!="")
                                		 	value.selectionDate=new Date(value.selectionDate);
                                   i++;
                                        });
                                  $rootScope.stopLoader();
                                 }else{
                                	 $rootScope.stopLoader();
                                 }
                             });


                          }



//                         $scope.updateSelectedDate = function(selectedDate) {
//                             console.log("Selected Date:", selectedDate);
//                         };

                         $scope.openDatePicker = function(index) {
                             $scope.vaccinationListByRoleList[index].datepickerOpened = true;

//                             $scope.vaccinationListByRoleList.forEach(function(info) {
//                                                          info.selectionDate = new Date(info.selectionDate);
//                                                      });
                         };

                         $scope.updateSelectedDate = function(info) {
                                if (info.selectionDate && typeof info.selectionDate === 'string') {
                                info.selectionDate = new Date(info.selectionDate);

                                }
                         };
                         $scope.empVaccinationClearFun=function(){
                             $scope.role="";
                             $scope.empName="";
                             $scope.vaccinationListByRoleList=[];
                               }

                          //  Save Employee Vaccination Function
                          $scope.saveEmpVaccination=function(){
                        	  
                            if($scope.role==undefined || $scope.role==null || $scope.role==""){
                               growl.warning("Please Select Role..!",{title : "WARNING!"});
                               return false;
                            }
                             if($scope.empName==undefined || $scope.empName==null || $scope.empName==""){
                               growl.warning("Please Select Employee Name..!",{title : "WARNING!"});
                               return false;
                            }

                             var saveObj={
                                roleId:$scope.selectedRoleId,
                                userId:$scope.selectedEmployeeId,
                                status:'A',
                                organisationId:$scope.organizationId,
                                unitId:$scope.unitId,
                                vaccinationDto:$scope.vaccinationListByRoleList
                             }
                             
                             var list=[];
                             angular.forEach($scope.vaccinationListByRoleList, function (value, key) {
                            	
                                 var data={
                                     "selectionDate":value.selectionDate,
                                     "vaccinationId":value.vaccinationId,
                                     "employeeVaccinationId": value.employeeVaccinationId
                                 }
                                 list.push(data);
                             });
                              saveObj.vaccinationDto=list;
                          
                              $rootScope.startLoader();
                             var URI2 = BASE_URL + ROOT_URL + "/api/global/saveEmployeeVaccination";

                                 GenericService.serviceAction("POST",URI2,saveObj).then(function(response){
                                     if(response.data.status == "success"){
                                     $scope.vaccinationListByRole=response.data.listObject;
                                     $rootScope.stopLoader();
                                     growl.success("Employee Vaccination Save successfully",
         									{
         										title : 'Success!'
         									});
                                     $scope.empVaccinationClearFun();
                                     }else{
                                    	 $rootScope.stopLoader(); 
                                     }
                                 });

                          }

}

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("employeeVaccination", {
            url: "/employeeVaccination",
            title: 'Employee Vaccinations',
            templateUrl: 'views/master/UnitMaster/userManagement/employee-vaccination.html',
            controller: "employeeVaccination.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("employeeVaccination.Controller", employeeVaccination);
})();