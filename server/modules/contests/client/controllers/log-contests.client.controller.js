'use strict';

// contests controller
angular.module('contests').controller('logContestsController', ['$scope', '$stateParams', '$location', '$http', 'Authentication',
  function ($scope, $stateParams, $location, $http, Authentication) {
    $scope.authentication = Authentication;
    
    $scope.selected;

    $scope.setSelect = function(answer){
      $scope.selected = answer;
    }

    $scope.init = function(){
      var id = $location.path().slice(5);
      
      $http.post('/contest/log/',{id: id}).
      success(function(response){
        //console.log(response);
        $scope.answers = response;
      });

      $http.post('/contest/find/',{id: id}).
      success(function(response){
        $scope.contest = response[0];
      });

    }
    

  }
]);
