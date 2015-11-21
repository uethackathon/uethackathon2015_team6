'use strict';

// Setting up route
angular.module('contests').config(['$stateProvider',
  function ($stateProvider) {
    // contests state routing
    $stateProvider
      .state('createContest', {
        url: '/create',
        templateUrl: 'modules/contests/client/views/blank.client.view.html'
      })
      .state('editContest', {
        url: '/edit/:contestID',
        templateUrl: 'modules/contests/client/views/create-contest.client.view.html'
      })
      .state('listContest', {
        url: '/list',
        templateUrl: 'modules/contests/client/views/list-contest.client.view.html'
      })
      .state('doContest', {
        url: '/do/:contestID',
        templateUrl: 'modules/contests/client/views/do-contest.client.view.html'
      })
      .state('logContest', {
        url: '/log/:contestID',
        templateUrl: 'modules/contests/client/views/log-contest.client.view.html'
      });
  }
]);
