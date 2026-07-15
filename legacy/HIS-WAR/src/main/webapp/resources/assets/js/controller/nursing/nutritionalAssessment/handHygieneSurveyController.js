(function () {
    "use strict";

    function HandHygieneSurvey($scope, $rootScope, $http, $state, commonDetailService, StatusService, rejectReasonService, $cookies, CONSTANTS, GenericService, growl, PagerService,BillingGenericService,GenericServiceFileUpload) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;

        function initializeController() {
            $rootScope.loginpage = true;

            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;

            // For Get Ward  List
            var URI =  BASE_URL + ROOT_URL+GETACTIVEWARDMASTERLIST;
            var data = {
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId
            }
            GenericService
                    .serviceAction("POST", URI, data)
                    .then(
                        function(response) {
                            $scope.wardList = [];

                            if (response.data.status == "success") {
                                $scope.wardList = response.data.listObject;
                                console.log("wardList",$scope.wardList);
                            }
                        });

            $scope.listOfSelectedCheckbox=[];

            $scope.prescribing = function(row,col){

                $scope.listOfSelectedCheckbox=[];

                if($scope.checkBox1 =='Y'){
                    var obj1 = {
                        questionId:1,
                        optionId:0
                    }
                 $scope.listOfSelectedCheckbox.push(obj1);
                }
                if($scope.checkBox2 =='Y'){
                    var obj2 = {
                        questionId:1,
                        optionId:1
                    }
                 $scope.listOfSelectedCheckbox.push(obj2);
                }
                if($scope.checkBox3 =='Y'){
                    var obj3 = {
                        questionId:2,
                        optionId:0
                    }
                  $scope.listOfSelectedCheckbox.push(obj3);
                }
                if($scope.checkBox4 =='Y'){
                    var obj4 = {
                        questionId:2,
                        optionId:1
                    }
                  $scope.listOfSelectedCheckbox.push(obj4);
                }
                if($scope.checkBox5 =='Y'){
                    var obj5 = {
                        questionId:3,
                        optionId:0
                    }
                    $scope.listOfSelectedCheckbox.push(obj5);
                }
                if($scope.checkBox6 =='Y'){
                     var obj6 = {
                         questionId:3,
                         optionId:1
                     }
                     $scope.listOfSelectedCheckbox.push(obj6);
                }
                if($scope.checkBox7 =='Y'){
                     var obj7 = {
                          questionId:3,
                          optionId:2
                      }
                     $scope.listOfSelectedCheckbox.push(obj7);
                }
                if($scope.checkBox8 =='Y'){
                     var obj8 = {
                       questionId:3,
                       optionId:3
                     }
                     $scope.listOfSelectedCheckbox.push(obj8);
                }
                if($scope.checkBox9 =='Y'){
                     var obj9 = {
                        questionId:3,
                        optionId:4
                     }
                     $scope.listOfSelectedCheckbox.push(obj9);
                }
                if($scope.checkBox10 =='Y'){
                     var obj10 = {
                        questionId:4,
                        optionId:0
                     }
                     $scope.listOfSelectedCheckbox.push(obj10);
                }
                if($scope.checkBox11 =='Y'){
                     var obj11 = {
                         questionId:4,
                         optionId:1
                      }
                     $scope.listOfSelectedCheckbox.push(obj11);
                }
                if($scope.checkBox12 =='Y'){
                     var obj12 = {
                        questionId:4,
                        optionId:2
                     }
                     $scope.listOfSelectedCheckbox.push(obj12);
                }
                if($scope.checkBox13 =='Y'){
                     var obj13 = {
                         questionId:4,
                         optionId:3
                     }
                     $scope.listOfSelectedCheckbox.push(obj13);
                }
                if($scope.checkBox14 =='Y'){
                     var obj14 = {
                          questionId:5,
                          optionId:0
                     }
                     $scope.listOfSelectedCheckbox.push(obj14);
                }
                if($scope.checkBox15 =='Y'){
                     var obj15 = {
                         questionId:5,
                         optionId:1
                      }
                     $scope.listOfSelectedCheckbox.push(obj15);
                }
                if($scope.checkBox16 =='Y'){
                     var obj16 = {
                           questionId:5,
                           optionId:2
                      }
                     $scope.listOfSelectedCheckbox.push(obj16);
                }
                if($scope.checkBox17 =='Y'){
                     var obj17 = {
                           questionId:6,
                           optionId:0
                      }
                     $scope.listOfSelectedCheckbox.push(obj17);
                }
                if($scope.checkBox18 =='Y'){
                     var obj18 = {
                           questionId:6,
                           optionId:1
                      }
                     $scope.listOfSelectedCheckbox.push(obj18);
                }
                if($scope.checkBox19 =='Y'){
                     var obj19 = {
                        questionId:6,
                        optionId:2
                     }
                     $scope.listOfSelectedCheckbox.push(obj19);
                }
                if($scope.checkBox20 =='Y'){
                     var obj20 = {
                         questionId:6,
                         optionId:3
                      }
                     $scope.listOfSelectedCheckbox.push(obj20);
                }
                if($scope.checkBox21 =='Y'){
                     var obj21 = {
                          questionId:6,
                          optionId:4
                      }
                     $scope.listOfSelectedCheckbox.push(obj21);
                }
                if($scope.checkBox22 =='Y'){
                     var obj22 = {
                       questionId:7,
                       optionId:0
                     }
                     $scope.listOfSelectedCheckbox.push(obj22);
                }
                if($scope.checkBox23 =='Y'){
                     var obj23 = {
                       questionId:7,
                       optionId:1
                     }
                     $scope.listOfSelectedCheckbox.push(obj23);
                }
                if($scope.checkBox24 =='Y'){
                     var obj24 = {
                        questionId:8,
                        optionId:0
                     }
                     $scope.listOfSelectedCheckbox.push(obj24);
                }
                if($scope.checkBox25 =='Y'){
                     var obj25 = {
                         questionId:8,
                         optionId:1
                      }
                     $scope.listOfSelectedCheckbox.push(obj25);
                }
                if($scope.checkBox26 =='Y'){
                    var obj26 = {
                        questionId:8,
                        optionId:2
                     }
                     $scope.listOfSelectedCheckbox.push(obj26);
                }
                if($scope.checkBox27 =='Y'){
                     var obj27 = {
                         questionId:8,
                         optionId:3
                      }
                     $scope.listOfSelectedCheckbox.push(obj27);
                }
                if($scope.checkBox28 =='Y'){
                     var obj28 = {
                         questionId:9,
                         optionId:0
                      }
                     $scope.listOfSelectedCheckbox.push(obj28);
                }
                if($scope.checkBox29 =='Y'){
                     var obj29 = {
                         questionId:9,
                         optionId:1
                      }
                     $scope.listOfSelectedCheckbox.push(obj29);
                }
                if($scope.checkBox30 =='Y'){
                     var obj30 = {
                      questionId:10,
                      optionId:0
                    }
                     $scope.listOfSelectedCheckbox.push(obj30);
                }
                if($scope.checkBox31 =='Y'){
                     var obj31 = {
                       questionId:10,
                       optionId:1
                     }
                     $scope.listOfSelectedCheckbox.push(obj31);
                }

                console.log("idsssssss=====",$scope.listOfSelectedCheckbox);
            }

            $scope.saveHandHygieneSurvey = function(saveAsDraft){

                $rootScope.startLoader();

                if($scope.listOfSelectedCheckbox.length>0){
                    var tempAssessmentSelectedList = $scope.listOfSelectedCheckbox;
                    $scope.listOfSelectedCheckbox = [];
                    angular.forEach(tempAssessmentSelectedList, function(value,id) {
                        var obj = {
                            questionId: value.questionId,
                            optionId: value.optionId
                        }
                        $scope.listOfSelectedCheckbox.push(obj);

                    });
                }

                if($scope.wardId==null || $scope.wardId==undefined || $scope.wardId==""){
                    growl.error("Please select the ward", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                if(saveAsDraft=='N'){
                    if($scope.listOfSelectedCheckbox==null || $scope.listOfSelectedCheckbox==undefined || $scope.checkAllQuestionFilled($scope.listOfSelectedCheckbox)==false){
                        growl.error("Please answer all questions", {
                            title: 'Error!'
                        });
                        $rootScope.stopLoader();
                        return;
                    }
                }

                if($scope.listOfSelectedCheckbox==null || $scope.listOfSelectedCheckbox==undefined || $scope.listOfSelectedCheckbox.length==0){
                    growl.error("Please answer at least one question", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                var saveObj = {
                    wardId: $scope.wardId,
                    unitId:$scope.unitId,
                    organizationId:$scope.organizationId,
                    listOfSelectedCheckbox:$scope.listOfSelectedCheckbox,
                    saveAsDraft:saveAsDraft,
                    createdBy : $scope.userId,
                    updatedBy : $scope.userId,
                    createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
                    updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
                }

                var URI = BASE_URL + ROOT_URL +"/adt/saveAsDraftHandHygieneScore";

                GenericService.serviceAction("POST", URI, saveObj).then(function (response) {
                    if (response.data.status == 'success'){
                         $rootScope.stopLoader();
                         if(saveAsDraft=='Y'){
                            $scope.blankAllCheckBox();
                            $scope.getSaveAsDraftHandHygieneSurvey();
                         }
                         if(saveAsDraft=='N'){
                            $scope.wardId = "";
                            $scope.blankAllCheckBox();
                         }

                         growl.success(response.data.message,
                         {
                            title : 'Success!'
                         });
                    }
                    else if(response.data.status =='error'){
                        $rootScope.stopLoader();
                        growl.error(response.data.message,{
                            title : 'Error!'
                        });
                    }
                });

            }

            $scope.getSaveAsDraftHandHygieneSurvey = function(){

                var URI1 = BASE_URL + ROOT_URL +"/adt/getSaveAsDraftHandHygieneScore/"+$scope.unitId+"/"+$scope.organizationId+"/"+$scope.wardId;
                $rootScope.startLoader();

                GenericService.serviceAction("GET", URI1, {}).then(function (response) {
                $scope.assessmentSelectedList = [];
                    if(response.data.status == "success"){
                       $rootScope.stopLoader();
                        $scope.listOfSelectedCheckbox = response.data.listObject;

                        angular.forEach($scope.listOfSelectedCheckbox, function(value,id) {
                            $scope.setSaveAsDraftHandHygieneSurvey(value,id);
                        });

                    }else if(response.data.status =='error'){
                         $rootScope.stopLoader();
                         growl.error(response.data.message,{
                             title : 'Error!'
                         });
                     }
                });

            }

            $scope.getSaveAsDraftHandHygiene = function(wardId){
                $scope.blankAllCheckBox();
                $scope.getSaveAsDraftHandHygieneSurvey();
            }

            $scope.setSaveAsDraftHandHygieneSurvey =  function(value,id){
                if(value.questionId==1){
                    if(value.optionId==0){
                        $scope.checkBox1='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox2='Y';
                    }
                }else if(value.questionId==2){
                    if(value.optionId==0){
                        $scope.checkBox3='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox4='Y';
                    }
                }else if(value.questionId==3){
                    if(value.optionId==0){
                        $scope.checkBox5='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox6='Y';
                    }else if(value.optionId==2){
                        $scope.checkBox7='Y';
                    }else if(value.optionId==3){
                        $scope.checkBox8='Y';
                    }else if(value.optionId==4){
                        $scope.checkBox9='Y';
                    }
                }else if(value.questionId==4){
                    if(value.optionId==0){
                        $scope.checkBox10='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox11='Y';
                    }else if(value.optionId==2){
                        $scope.checkBox12='Y';
                    }else if(value.optionId==3){
                        $scope.checkBox13='Y';
                    }
                }else if(value.questionId==5){
                    if(value.optionId==0){
                        $scope.checkBox14='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox15='Y';
                    }else if(value.optionId==2){
                        $scope.checkBox16='Y';
                    }
                }else if(value.questionId==6){
                    if(value.optionId==0){
                        $scope.checkBox17='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox18='Y';
                    }else if(value.optionId==2){
                        $scope.checkBox19='Y';
                    }else if(value.optionId==3){
                        $scope.checkBox20='Y';
                    }else if(value.optionId==4){
                        $scope.checkBox21='Y';
                    }
                }else if(value.questionId==7){
                    if(value.optionId==0){
                        $scope.checkBox22='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox23='Y';
                    }
                }else if(value.questionId==8){
                    if(value.optionId==0){
                        $scope.checkBox24='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox25='Y';
                    }else if(value.optionId==2){
                        $scope.checkBox26='Y';
                    }else if(value.optionId==3){
                        $scope.checkBox27='Y';
                    }
                }else if(value.questionId==9){
                    if(value.optionId==0){
                        $scope.checkBox28='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox29='Y';
                    }
                }else if(value.questionId==10){
                    if(value.optionId==0){
                        $scope.checkBox30='Y';
                    }else if(value.optionId==1){
                        $scope.checkBox31='Y';
                    }
                }
            }

            $scope.blankAllCheckBox = function(){

                $scope.checkBox1='N';
                $scope.checkBox2='N';
                $scope.checkBox3='N';
                $scope.checkBox4='N';
                $scope.checkBox5='N';
                $scope.checkBox6='N';
                $scope.checkBox7='N';
                $scope.checkBox8='N';
                $scope.checkBox9='N';
                $scope.checkBox10='N';
                $scope.checkBox11='N';
                $scope.checkBox12='N';
                $scope.checkBox13='N';
                $scope.checkBox14='N';
                $scope.checkBox15='N';
                $scope.checkBox16='N';
                $scope.checkBox17='N';
                $scope.checkBox18='N';
                $scope.checkBox19='N';
                $scope.checkBox20='N';
                $scope.checkBox21='N';
                $scope.checkBox22='N';
                $scope.checkBox23='N';
                $scope.checkBox24='N';
                $scope.checkBox25='N';
                $scope.checkBox26='N';
                $scope.checkBox27='N';
                $scope.checkBox28='N';
                $scope.checkBox29='N';
                $scope.checkBox30='N';
                $scope.checkBox31='N';

            }

            $scope.checkAllQuestionFilled = function(list){
                var arr = [];
                angular.forEach(list, function(value,id) {
                    arr.push(value.questionId);
                });

                var uniqueArr = arr.filter((value, index, self) => {
                    return self.indexOf(value) === index;
                });

                uniqueArr.sort((a, b) => a - b);
                var i=1;
                while(i<=10){
                    if(uniqueArr[i-1]!=i){
                        return false;
                    }
                    i++;
                }
                return true;
            }

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("HandHygieneSurvey", {
            url: "/handHygieneSurvey",
            title: 'Hand Hygiene Survey',
            templateUrl: 'views/Nursing/hand-hygiene-survey.html',
            controller: "HandHygieneSurvey.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("HandHygieneSurvey.Controller", HandHygieneSurvey);
})();