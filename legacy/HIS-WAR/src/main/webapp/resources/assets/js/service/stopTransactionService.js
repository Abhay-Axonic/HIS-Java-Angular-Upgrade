angular.module('myApp').service('stopTransactionService',
  function ($rootScope, $q,$cookies, GenericService, CONSTANTS) {
    var stopTransactionData;
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}
    var unitId = cookieObject.unitId;
    var organizationId = cookieObject.organizationId;
    var userId = cookieObject.userId;
    var storeId = cookieObject.storeId;

    var GetstopTransactionData = function (data) {
      var defer = $q.defer();
      if (data != '') {     
        var URI = CONSTANTS.INVENTORY.STOCK_ADJUSTMENT.STOP_TRANSACTION.CHECK_STOP_TRANSCTION;
        data.unitId = unitId;
        data.organizationId = organizationId;
        GenericService.serviceAction("PATCH", URI,data).then(
          function (response) {            
            defer.resolve(response.data);
          },
          function errorCallback(err) {
            defer.reject(err);
          });
      } else {
        defer.resolve(stopTransactionData);
      }
      return defer.promise;
    }
    return {
      GetstopTransactionData: GetstopTransactionData
    };
  }
);