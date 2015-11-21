'use strict';

// Configuring the contests module
angular.module('contests').run(['Menus',
  function (Menus) {
    Menus.addMenuItem('topbar', {
      title: 'List contests',
      state: 'listContest'
    });

    Menus.addMenuItem('topbar', {
      title: 'Create contests',
      state: 'createContest'
    });
    
  }
]);
