(function () {
    "use strict";

    function PatientSearchController($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl,BillingGenericService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = false;


        }



//        ------------  POS Functionality  ---------------
             var data = {};
          $scope.hideShowSearch = true;
            var URI = BASE_URL + ROOT_URL
                    + GETACTIVEPREFIXLIST;
                      BillingGenericService
                    .serviceAction(METHOD_GET, URI, data,
                    NOTIFICATION_MSG_STATUS_FALSE)
                    .then(
                            function(response) {
                            console.log(response);
                            $scope.prefixList = [];
                            if (response.data.status == "success")
                            $scope.prefixList = response.data.listObject;
                        })

                        var URI = BASE_URL + ROOT_URL
                                + GETACTIVEGENDERLIST;
                        BillingGenericService
                                .serviceAction(METHOD_GET, URI, data,
                                        NOTIFICATION_MSG_STATUS_FALSE)
                                .then(
                                function(response) {
                                    console.log(response);
                                    $scope.genderList = [];
                                    if (response.data.status == "success")
                                        $scope.genderList = response.data.listObject;
                                });

//                            search functionality
                              $scope.searchGroupList = function(keyword) {
//                         				var data = {
//               									patientName : keyword,
//               									unitId : $scope.unitId,
//               									organizationId : $scope.organizationId,
//               									//isFromKioskToken : 'Y'
//               								}

                                       if(keyword.length >0){
                                       console.log(data);

                                                      								var URI = BASE_URL + ROOT_URL
                                                      										+ "/api/global/searchPocMaster/"+keyword;

                                                      								return BillingGenericService
                                                                                   .serviceAction(METHOD_GET, URI,
                                                                                           NOTIFICATION_MSG_STATUS_FALSE)
                                                                                   .then(
                                                                                           function(response) {
                                                                                               // $rootScope.startLoader();
                                                                                               if (response.data.status == "success"){

                                                                                               $scope.patientObj={};
                                                                                               $scope.patientObj.firstName  = response.data.listObject[0].firstName;
                                                                                               $scope.patientObj.middleName  = response.data.listObject[0].middleName;
                                                                                               $scope.patientObj.lastName  = response.data.listObject[0].lastName;
                                                                                               $scope.patientObj.emailId  = response.data.listObject[0].emailId;
                                                                                               $scope.patientObj.prefixId  = response.data.listObject[0].prefixId;
                                                                                               $scope.patientObj.mobileNo  = response.data.listObject[0].mobileNo;
                                                                                               $scope.patientObj.reasonId  = response.data.listObject[0].reasonId;
                                                                                               $scope.patientObj.remark  = response.data.listObject[0].remark;
                                                                                               $scope.patientObj.genderId  = response.data.listObject[0].genderId;
                                                                                               $scope.patientObj.patientPOCId=response.data.listObject[0].patientPOCId;
                                                                                               $scope.patientObj.statusIdTemp  = response.data.listObject[0].checkinStatusId;
                                                                                             //  $scope.patientObj=response.data.listObject[0];
                                                                                                  $scope.hideShowSearch = true;
                                                                                                $scope.getClassforColor();

                                                                                                if($scope.patientObj.statusIdTemp==5){
                                                                                                          $scope.statusFlag=false;
                                                                                                        }

                                                                                               }


                                                                                           });

                                       }

               							}
                            $scope.clearSearch = function (){
                              // $scope.patientName="";
                               $scope.hideShowSearch = false;
                            }

                            $scope.statusFlag=true;
                           $scope.getClassforColor1 = function(){
                           if($scope.patientObj.statusIdTemp >0){
                                           return "step current";
                                       }else{
                                           return "step";
                                       }
                           }

                            $scope.getClassforColor2 = function(){
                                  if( $scope.patientObj.statusIdTemp >1){
                                                  return "step current";
                                              }else{
                                                  return "step";
                                              }
                                  }
                           $scope.getClassforColor3 = function(){
                           if($scope.patientObj.statusIdTemp >2 ){
                                           return "step current";
                                       }else{
                                           return "step";
                                       }
                           }

                           $scope.getClassforColor4 = function(){
                                      if($scope.patientObj.statusIdTemp >3 ){
                                                      return "step current";
                                                  }else{
                                                      return "step";
                                                  }
                                      }

                          $scope.getClassforColor5 = function(){
                                     if($scope.patientObj.statusIdTemp >4 ){
                                                     return "step current";
                                                     $scope.statusFlag=false;
                                                 }else{
                                                     return "step";
                                                 }
                                     }
                            $scope.clear=false;
                            $scope.cleardata = function(){
                                     $scope.patientObj.firstName="";
                                     $scope.patientObj.middleName="";
                                     $scope.patientObj.lastName="";
                                     $scope.patientObj.emailId="";
                                     $scope.patientObj.prefixId="";
                                     $scope.patientObj.mobileNo="";
                                     $scope.patientObj.reasonId="";
                                     $scope.patientObj.remark="";
                                     $scope.patientObj.genderId="";
                                     $scope.patientName="";
                                     $scope.patientObj.checkinStatusId="";

                                     $scope.clear=true;

                                    // colorInactive
                                    $scope.patientObj.statusIdTemp=""
                            }

               				$scope.selectPatientId = function(info, model,label)
               							 {

                                         $scope.patientObj.patientPOCId=info.patientPOCId;
               							 $scope.patientObj.firstName=info.firstName;
                                         $scope.patientObj.middleName=info.middleName
                                         $scope.patientObj.lastName=info.lastName
                                         $scope.patientObj.email=info.email
                                         $scope.patientObj.prefixId=info.prefixId
                                         $scope.patientObj.mobileNumber=info.mobileNumber
                                         $scope.patientObj.reasonTypeId=info.reasonTypeId
                                         $scope.patientObj.remark=info.remark
                                         $scope.patientObj.genderId=info.genderId

                                        $scope.patientObj.organizationId = $scope.organizationId,
                                        $scope.patientObj.unitId = info.patientPOCId;
                                        $scope.patientObj.updatedBy = info.patientPOCId;
                                        $scope.patientObj.createdBy = info.patientPOCId;
                                        $scope.saveEnable = false;
                                        $scope.hideShowSearch = true;
                                    }

                                       $scope.firstCheck=false;
                                       $scope.billing=false;
                                        $scope.updateNewPatient=function(data){
                                        //try{
                                            if(data==undefined || data==null || data=="" ){
                                              data=0;
                                            }
                                           console.log(data)
                                          if(data==0  && $scope.selectStatus==0 ||data==0 && $scope.selectStatus==2 || data==0 && $scope.selectStatus==3 || data==0 && $scope.selectStatus==4 || data==0 && $scope.selectStatus==5){
                                             growl.error("Please Select Check In Status",{
                                                  title : ERROR_MSG
                                                  });
                                                  return false;
                                                  }
                                          if( data==1 && $scope.selectStatus==0 || data==1 && $scope.selectStatus==3 || data==1 && $scope.selectStatus==4 || data==1 && $scope.selectStatus==5){
                                             growl.error("Please Select Patient Prep Status",{
                                                 title : ERROR_MSG
                                                 });
                                                 return false;
                                                 }

                                           if( data==2 && $scope.selectStatus==0 || data==2 && $scope.selectStatus==4 || data==2 && $scope.selectStatus==5){
                                              growl.error("Please Select Wheel In Status",{
                                                   title : ERROR_MSG
                                                   });
                                                   return false;
                                                   }
                                           if(data==3 && $scope.selectStatus==0 || data==3 && $scope.selectStatus==5){
                                              growl.error("Please Select Wheel Out Status",{
                                                   title : ERROR_MSG
                                                   });
                                                   return false;
                                                   }
                                           if(data==4 && $scope.selectStatus==0){
                                             growl.error("Please Select Patient Exit Status",{
                                                  title : ERROR_MSG
                                                  });
                                                  return false;
                                                  }
                                            var date = new Date();
                                            $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
                                            //$scope.$broadcast('show-errors-check-validity');
                                            $rootScope.startLoader();

                                            var data={
                                                        patientPOCId: $scope.patientObj.patientPOCId,
                                                        checkinStatusId: $scope.patientObj.checkinStatusId,
                                                        updatedDate:$scope.dt

                                                }

                                            var URI = BASE_URL + ROOT_URL + "/api/global/updatePocMaster";
                                            console.log("Update-URI-",URI);
                                            console.log("Update OBJ",data);
                                            $rootScope.startLoader();

                                            GenericService.serviceAction("POST",URI,data).then(function(response){

                                                if (response.data.status == 'success')
                                                {
                                                        $rootScope.stopLoader();
                                                        $scope.cleardata();
                                                    growl.success(response.data.message,
                                                            {
                                                                title : 'Success!'
                                                            });
                                                            return false;


                                                }
                                            });


                                    }


                                    $scope.changeColor1 = false;
                                   $scope.changeColor2 = 0;
                                    $scope.changeColor3 = 0;
                                     $scope.changeColor4 =0;


			                      $scope.set_color = function(totalScore) {
			                      $scope.selectStatus=totalScore;
                                        if(totalScore == 1){
                                            $scope.changeColor1 = 1;
                                             $scope.changeColor2 = 0;
                                            $scope.changeColor3 = 0;
                                             $scope.changeColor4 =0;
                                        }
                                        else if(totalScore == 2 ){
                                           $scope.changeColor2 = 2;
                                            $scope.changeColor1 = 0;

                                            $scope.changeColor3 = 0;
                                             $scope.changeColor4 =0;
                                        }else if(totalScore == 3){
                                            $scope.changeColor3 = 3;
                                            $scope.changeColor1 = 0;
                                           $scope.changeColor2 = 0;

                                             $scope.changeColor4 =0;
                                        }
                                        else if(totalScore == 4){
                                            $scope.changeColor4 = 4;
                                             $scope.changeColor1 = 0;
                                           $scope.changeColor2 = 0;
                                            $scope.changeColor3 = 0;


                                        }
                                    };



        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("PatientSearchController", {
            url: "/visitorProcess",
            templateUrl: 'views/PatientPos/patientSearch.html',
            controller: "PatientSearchController.Controller",
         resolve : {
                        data: function($q, $state, $timeout) {
                            var deferred = $q.defer();

                            $timeout(function() {
                                var arr = JSON.parse(localStorage.getItem('permissions'))
                                .modules.filter(function(item){
                                   return (item.id == 1066);
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
        .controller("PatientSearchController.Controller", PatientSearchController);
})();