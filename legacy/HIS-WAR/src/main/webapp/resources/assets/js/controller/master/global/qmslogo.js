(function () {
    "use strict";

    function Qmslogo($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;



        }
        $scope.unitId = cookieObject.unitId;
        $scope.organizationId = cookieObject.organizationId;

        //main screen background
        $scope.mainScreenBackground = function(){
                         $scope.currentDate = new Date();
                         $scope.currentDate = moment($scope.currentDate)
                            .format('DD-MM-YYYY HH:mm:ss');
                            $scope.createdBy=1;
                            $scope.updatedBy=1;
                            $scope.imageTypeId=1
                            $scope.status='A';
                         var Obj={}
                         var fd = new FormData();
                            fd.append('imageTypeId', $scope.imageTypeId);
                            fd.append('status', $scope.status);
                            fd.append('createdBy',$scope.createdBy);
                            fd.append('updatedBy',$scope.updatedBy);
                            fd.append('createdDate',$scope.currentDate );
                            fd.append('updatedDate', $scope.currentDate);
                            fd.append('unitId', $scope.unitId);
                            fd.append('organisationId', $scope.organizationId);
                            fd.append('file',$scope.mainScreen);

                            if($scope.mainScreen==undefined || $scope.mainScreen=="" || $scope.mainScreen==null)
                                {
                                     growl.warning("Please Select QMS Main Screen Background Image",{
                                                title : "warning"
                                            });
                                            return false;
                                      }

                         var URI1 = BASE_URL + ROOT_URL +"/api/unit/applicationConfiguration/screen-images";
                          $rootScope.startLoader();
                            return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                            {
                                growl.success(response.data.message,
                                        {
                                            title : 'Success!'
                                        });
                                     $rootScope.stopLoader();
                            });

       }

        //AfterLogin
       $scope.afterLoginImg = function(){
                            $scope.currentDate = new Date();
                            $scope.currentDate = moment($scope.currentDate)
                               .format('DD-MM-YYYY HH:mm:ss');
                               $scope.createdBy=2;
                               $scope.updatedBy=2;
                               $scope.imageTypeId=2
                               $scope.status='A';
                            var fd = new FormData();
                               fd.append('imageTypeId', $scope.imageTypeId);
                               fd.append('status', $scope.status);
                               fd.append('createdBy',$scope.createdBy);
                               fd.append('updatedBy',$scope.updatedBy);
                               fd.append('createdDate',$scope.currentDate);
                               fd.append('updatedDate', $scope.currentDate);
                               fd.append('unitId', $scope.unitId);
                               fd.append('organisationId', $scope.organizationId);
                               fd.append('file',$scope.afterLogin);

                               if($scope.afterLogin==undefined || $scope.afterLogin=="" || $scope.afterLogin==null)
                                       {
                                            growl.warning("Please Select After loggin Main logo",{
                                                       title : "warning"
                                                   });
                                                   return false;
                                             }

                            var URI1 = BASE_URL + ROOT_URL +"/api/unit/applicationConfiguration/screen-images";
                               $rootScope.startLoader();
                               return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                               {
                                  growl.success(response.data.message,
                                      {
                                          title : 'Success!'
                                      });
                                      $rootScope.stopLoader();
                               });
       }

       //NursingTokenCabin
       $scope.nursingTokenCabinImg = function(){
                               $scope.currentDate = new Date();
                               $scope.currentDate = moment($scope.currentDate)
                                  .format('DD-MM-YYYY HH:mm:ss');
                                  $scope.createdBy=3;
                                  $scope.updatedBy=3;
                                  $scope.imageTypeId=3;
                                  $scope.status='A';
                               var Obj={}
                               var fd = new FormData();
                                  fd.append('imageTypeId', $scope.imageTypeId);
                                  fd.append('status', $scope.status);
                                  fd.append('createdBy',$scope.createdBy);
                                  fd.append('updatedBy',$scope.updatedBy);
                                  fd.append('createdDate',$scope.currentDate );
                                  fd.append('updatedDate', $scope.currentDate);
                                  fd.append('unitId', $scope.unitId);
                                  fd.append('organisationId', $scope.organizationId);
                                  fd.append('file',$scope.nursingTokenCabin);

                                  if($scope.nursingTokenCabin==undefined || $scope.nursingTokenCabin=="" || $scope.nursingTokenCabin==null)
                                                 {
                                                      growl.warning("Please Select Nursing,Cabin,Token,Triage,Consultant logo",{
                                                                 title : "warning"
                                                             });
                                                             return false;
                                                       }

                               var URI1 = BASE_URL + ROOT_URL +"/api/unit/applicationConfiguration/screen-images";
                                   $rootScope.startLoader();
                                  return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                  {
                                      growl.success(response.data.message,
                                                        {
                                                            title : 'Success!'
                                                        });
                                                        $rootScope.stopLoader();
                                  });
       }

       //kioskHeader
       $scope.kioskHeaderImg = function(){
                       $scope.currentDate = new Date();
                                   $scope.currentDate = moment($scope.currentDate)
                                      .format('DD-MM-YYYY HH:mm:ss');
                                      $scope.createdBy=4;
                                      $scope.updatedBy=4;
                                      $scope.imageTypeId=4;
                                      $scope.status='A';
                                   var Obj={}
                                   var fd = new FormData();
                                      fd.append('imageTypeId', $scope.imageTypeId);
                                      fd.append('status', $scope.status);
                                      fd.append('createdBy',$scope.createdBy);
                                      fd.append('updatedBy',$scope.updatedBy);
                                      fd.append('createdDate',$scope.currentDate );
                                      fd.append('updatedDate', $scope.currentDate);
                                      fd.append('unitId', $scope.unitId);
                                      fd.append('organisationId', $scope.organizationId);
                                      fd.append('file',$scope.kioskHeader);

                                      if($scope.kioskHeader==undefined || $scope.kioskHeader=="" || $scope.kioskHeader==null)
                                                {
                                                     growl.warning("Please Select kiosk Header Image",{
                                                                title : "warning"
                                                            });
                                                            return false;
                                                      }

                                   var URI1 = BASE_URL + ROOT_URL +"/api/unit/applicationConfiguration/screen-images";
                                       $rootScope.startLoader();
                                      return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                      {
                                         growl.success(response.data.message,
                                            {
                                                title : 'Success!'
                                            });
                                            $rootScope.stopLoader();
                                      });
       }

       //kioskFooter
       $scope.kioskFooterImg = function(){
                                     $scope.currentDate = new Date();
                                     $scope.currentDate = moment($scope.currentDate)
                                        .format('DD-MM-YYYY HH:mm:ss');
                                        $scope.createdBy=5;
                                        $scope.updatedBy=5;
                                        $scope.imageTypeId=5;
                                        $scope.status='A';
                                     var Obj={}
                                     var fd = new FormData();
                                        fd.append('imageTypeId', $scope.imageTypeId);
                                        fd.append('status', $scope.status);
                                        fd.append('createdBy',$scope.createdBy);
                                        fd.append('updatedBy',$scope.updatedBy);
                                        fd.append('createdDate',$scope.currentDate );
                                        fd.append('updatedDate', $scope.currentDate);
                                        fd.append('unitId', $scope.unitId);
                                        fd.append('organisationId', $scope.organizationId);
                                        fd.append('file',$scope.kioskFooter);

                                        if($scope.kioskFooter==undefined || $scope.kioskFooter=="" || $scope.kioskFooter==null)
                                                  {
                                                       growl.warning("Please Select kiosk Footer Image",{
                                                                  title : "warning"
                                                              });
                                                              return false;
                                                        }

                                     var URI1 = BASE_URL + ROOT_URL +"/api/unit/applicationConfiguration/screen-images";
                                         $rootScope.startLoader();
                                        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                        {
                                           growl.success(response.data.message,
                                              {
                                                  title : 'Success!'
                                              });
                                              $rootScope.stopLoader();
                                        });
       }
       //Powered login Img
       $scope.PoweredloginImg=function(){
                                            $scope.currentDate = new Date();
                                            $scope.currentDate = moment($scope.currentDate)
                                               .format('DD-MM-YYYY HH:mm:ss');
                                               $scope.createdBy=6;
                                               $scope.updatedBy=6;
                                               $scope.imageTypeId=6;
                                               $scope.status='A';
                                            var Obj={}
                                            var fd = new FormData();
                                               fd.append('imageTypeId', $scope.imageTypeId);
                                               fd.append('status', $scope.status);
                                               fd.append('createdBy',$scope.createdBy);
                                               fd.append('updatedBy',$scope.updatedBy);
                                               fd.append('createdDate',$scope.currentDate );
                                               fd.append('updatedDate', $scope.currentDate);
                                               fd.append('unitId', $scope.unitId);
                                               fd.append('organisationId', $scope.organizationId);
                                               fd.append('file',$scope.loginPageImg);

                                               if($scope.loginPageImg==undefined || $scope.loginPageImg=="" || $scope.loginPageImg==null)
                                                         {
                                                              growl.warning("Please Select Powered by login page Image",{
                                                                         title : "warning"
                                                                     });
                                                                     return false;
                                                               }

                                            var URI1 = BASE_URL + ROOT_URL +"/api/unit/applicationConfiguration/screen-images";
                                                $rootScope.startLoader();
                                               return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                               {
                                                  growl.success(response.data.message,
                                                     {
                                                         title : 'Success!'
                                                     });
                                                     $rootScope.stopLoader();
                                               });
       }
       //Application Name
       $scope.applicationNameFun=function(){
                                            $scope.currentDate = new Date();
                                            $scope.currentDate = moment($scope.currentDate)
                                               .format('DD-MM-YYYY HH:mm:ss');
                                               $scope.createdBy=7;
                                               $scope.updatedBy=7;
                                               $scope.imageTypeId=7;
                                               $scope.status='A';
                                            var Obj={}
                                            var fd = new FormData();
                                               fd.append('imageTypeId', $scope.imageTypeId);
                                               fd.append('status', $scope.status);
                                               fd.append('createdBy',$scope.createdBy);
                                               fd.append('updatedBy',$scope.updatedBy);
                                               fd.append('createdDate',$scope.currentDate );
                                               fd.append('updatedDate', $scope.currentDate);
                                               fd.append('unitId', $scope.unitId);
                                               fd.append('organisationId', $scope.organizationId);
                                               fd.append('logoName',$scope.applicationName);

                                               if($scope.applicationName==undefined || $scope.applicationName=="" || $scope.applicationName==null)
                                                         {
                                                              growl.warning("Please Enter Application Name",{
                                                                         title : "warning"
                                                                     });
                                                                     return false;
                                                               }

                                            var URI1 = BASE_URL + ROOT_URL +"/api/unit/applicationConfiguration/screen-images";
                                                $rootScope.startLoader();
                                               return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                               {
                                                  growl.success(response.data.message,
                                                     {
                                                         title : 'Success!'
                                                     });
                                                     $scope.applicationName="";
                                                     $rootScope.stopLoader();
                                               });
       }
              //Copy Write Name
              $scope.copyWriteFun=function(){
                                                   $scope.currentDate = new Date();
                                                   $scope.currentDate = moment($scope.currentDate)
                                                      .format('DD-MM-YYYY HH:mm:ss');
                                                      $scope.createdBy=8;
                                                      $scope.updatedBy=8;
                                                      $scope.imageTypeId=8;
                                                      $scope.status='A';
                                                   var Obj={}
                                                   var fd = new FormData();
                                                      fd.append('imageTypeId', $scope.imageTypeId);
                                                      fd.append('status', $scope.status);
                                                      fd.append('createdBy',$scope.createdBy);
                                                      fd.append('updatedBy',$scope.updatedBy);
                                                      fd.append('createdDate',$scope.currentDate );
                                                      fd.append('updatedDate', $scope.currentDate);
                                                      fd.append('unitId', $scope.unitId);
                                                      fd.append('organisationId', $scope.organizationId);
                                                      fd.append('logoName',$scope.copyWrite);

                                                      if($scope.copyWrite==undefined || $scope.copyWrite=="" || $scope.copyWrite==null)
                                                                {
                                                                     growl.warning("Please Enter Copy Write Name",{
                                                                                title : "warning"
                                                                            });
                                                                            return false;
                                                                      }

                                                   var URI1 = BASE_URL + ROOT_URL +"/api/unit/applicationConfiguration/screen-images";
                                                       $rootScope.startLoader();
                                                      return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                                      {
                                                         growl.success(response.data.message,
                                                            {
                                                                title : 'Success!'
                                                            });
                                                            $scope.copyWrite="";
                                                            $rootScope.stopLoader();
                                                      });
              }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("Qmslogo", {
            url: "/qmslogo",
            title: 'QMS Logo',
            templateUrl: 'views/master/GlobalMaster/qms-logo.html',
            controller: "Qmslogo.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("Qmslogo.Controller", Qmslogo);
})();