(function () {
    "use strict";

    function PatientPosController($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl,BillingGenericService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = false;


        }

//    $scope.patientObj.prefixId='Select Prefix';
            //$scope.patientObj.prefixId="select";

       //        ------------  POS Functionality  ---------------
           $scope.prefix="";
           $scope.gender="";
           $scope.reason="";
           var data = {};
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

                        //get calling code by using country id start
                        $scope.getCallingCode=function(){
                            var data = {
                                    countryId : $scope.unitCountryId,
                                    organizationId : $scope.organizationId
                                };
                            var URI = BASE_URL + ROOT_URL
                            + GETSINGLECOUNTRY;
                            GenericService.serviceAction("POST", URI, data).then(   
                                    function(response) {
                                        if (response.data.status == "success" && response.data.listObject.length > 0)
                                        {
                                            $scope.patientObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
                                        }
                                    });
                        }
                        $scope.getCallingCode();
                        //get calling code by using country id end


                           $scope.patienpoClear = function(){
                           $scope.patientObj.firstName="",
                           $scope.patientObj.middleName="",
                           $scope.patientObj.lastName="",
                           $scope.patientObj.email="",
                           $scope.prefix="",
                           $scope.patientObj.mobileNumber="",
                           $scope.reason="",
                           $scope.patientObj.remark="",
                           $scope.gender=""
                           }
                        $scope.patientPosSave = function(){

                         if($scope.patientObj.firstName== undefined || $scope.patientObj.firstName=="" || $scope.patientObj.firstName==null)
                         {
                            growl.error("Please Enter First Name.",
                                {
                                    title : 'Error!'
                                });
                                return false;
                          }
                          if($scope.prefix== undefined || $scope.prefix=="" || $scope.prefix==null)
                         {
                            growl.error("Please Select Prefix.",
                                {
                                    title : 'Error!'
                                });
                                return false;
                          }
                          if($scope.gender== undefined || $scope.gender=="" || $scope.gender==null)
                               {
                                  growl.error("Please Select Gender.",
                                      {
                                          title : 'Error!'
                                      });
                                      return false;
                                }
                                if($scope.patientObj.mobileNumber== undefined || $scope.patientObj.mobileNumber=="" || $scope.patientObj.mobileNumber==null)
                                   {
                                      growl.error("Please Enter Mobile Number.",
                                          {
                                              title : 'Error!'
                                          });
                                          return false;
                                    }

                                    if($scope.reason== undefined || $scope.reason=="" || $scope.reason==null ||  $scope.reason==0)
                                       {
                                          growl.error("Please Select Reason.",
                                              {
                                                  title : 'Error!'
                                              });
                                              return false;
                                        }
                                        if($scope.patientObj.email == null){
                                             $scope.patientObj.email= "";
                                         }
                                         if($scope.patientObj.email == undefined){
                                             $scope.patientObj.email ="";
                                         }
                                         var filter = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/;
                                         var email = $scope.patientObj.email;
                                	   if($scope.patientObj.email == 'filter')
         		           			   {
         		           			   }
                                	 else{
                                		 if (!(email.match(filter))) {
             			           		    growl.error("Please enter valid email id.",{
                             					title : ERROR_MSG
                             					});
                             					return false;;
             			           		    }
                         			 }


                            var date = new Date();
                            $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
                           // $scope.$broadcast('show-errors-check-validity');
                            var data={
                                    firstName:$scope.patientObj.firstName,
                                    middleName:$scope.patientObj.middleName,
                                    lastName:$scope.patientObj.lastName,
                                    emailId:$scope.patientObj.email,
                                    prefixId:$scope.prefix,
                                    mobileNo:$scope.patientObj.mobileNumber,
                                    reasonId:$scope.reason,
                                    remark:$scope.patientObj.remark,
                                    genderId:$scope.gender,
                                    organizationId:$scope.organizationId,
                                    unitId:$scope.unitId,
                                    createdBy:$scope.createdBy,
                                    updatedBy:$scope.updatedBy,
                                    //status:'A',
                                    checkinDateTime:$scope.dt,
                                    createdDate:$scope.dt,
                                    updatedDate:$scope.dt
                                }


                            var URI = BASE_URL + ROOT_URL + "/api/global/savePocMaster";
                            console.log("Save-URI-",URI);
                            console.log(" save OBJ",data);
                                $rootScope.startLoader();
                            GenericService.serviceAction("POST",URI,data).then(function(response){
                                if (response.data.status == 'success')
                                {
                                   $rootScope.stopLoader();
                                    $scope.patienpoClear();
                                    growl.success(response.data.message,
                                            {
                                                title : 'Success!'
                                            });

                                    $rootScope.stopLoader();
                                    console.log(response)
                                }
                                $rootScope.stopLoader();
                            });
                            }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("PatientPosController", {
            url: "/visitorInformation",
            templateUrl: 'views/PatientPos/Patient-Pos.html',
            controller: "PatientPosController.Controller",
            params: {
            obj: {}
                   },
             resolve : {
                  			data: function($q, $state, $timeout) {
                  		        var deferred = $q.defer();

                  		        $timeout(function() {
                  		        	var arr = JSON.parse(localStorage.getItem('permissions'))
                  				 	.modules.filter(function(item){
                  	            	   return (item.id == 1065);
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
        .controller("PatientPosController.Controller", PatientPosController);
})();