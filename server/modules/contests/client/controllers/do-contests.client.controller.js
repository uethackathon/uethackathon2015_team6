'use strict';

// contests controller
angular.module('contests').controller('doContestsController', ['$scope', '$stateParams', '$location', '$http', 'Authentication', 'ContestService',
  function ($scope, $stateParams, $location, $http, Authentication, ContestService) {
    $scope.authentication = Authentication;
    
    $scope.score = false;
    $scope.chr = [];
    for (var i = 0; i < 26; i++){
      $scope.chr.push(String.fromCharCode(65 + i));
    }
    
    $scope.init = function(){
      $scope.header = {
        id: "header",
        edit: true,
        text: ""  
      };
      $scope.questions = [];
      var id = $location.path().slice(4);

      $http.post('contest/get', {id: id}).success(function(response){
        console.log(response);
        $scope.contest = response;
        $scope.header.text = response.header;
        if (response.questions)
          $scope.questions = response.questions;
      });
    }
      
    $scope.submit = function(){
      var answers = [];
      for(var i = 0; i < $scope.questions.length; i++){
        var choices = $scope.questions[i].choices, answeri = [];
        for(var j = 0; j < choices.length; j++){
          answeri.push(choices[j].correct);
        }
        answers.push(answeri);
      }

      console.log(answers);
      $http.post('contest/submit', {
        id: $scope.contest._id,
        answers: answers
      })
      .success(function(response){
        //console.log(response);
        $scope.total = response.total;
        $scope.correct = response.correct;
        $scope.score = true;
        $("html, body").animate({ scrollTop: 0 }, 500);
      });


    }

    $scope.toggleChoice = function(choice){
      choice.correct = !choice.correct;
    };

  }
]);
