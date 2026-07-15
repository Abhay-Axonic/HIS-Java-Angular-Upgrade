angular.module('myApp').service('rejectReasonService',
  function ($rootScope, $q, GenericService, CONSTANTS,$cookies) {
    var reason;

    var GetReason = function () {
    	
    	var cookieObject = $cookies.getObject("cookieObject");
    	var unitId = null;
		if(cookieObject!=undefined){
			unitId = cookieObject.unitId;
		}
      var defer = $q.defer();

      if (!reason || reason.length==0) {

        var SetReason = function (data) {
          reason = data;
        };

        var URI = CONSTANTS.GLOBAL.REJECT_REASON_API + '/' + unitId;
        
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            SetReason(response.data);
            defer.resolve(response.data);
          },
          function errorCallback(err) {
            defer.reject(err);
          });

      } else {
        defer.resolve(reason);
      }

      return defer.promise;
    }

    return {
      GetReason: GetReason
    };
  }
);