'use strict';

/**
 * Module dependencies.
 */
var contest = require('../controllers/contests.server.controller'),
	contestP = require('../controllers/contests-process.server.controller');
module.exports = function (app) {
  app.route('/contest/create')
    .post(contest.create);

  app.route('/contest/list')
    .get(contest.list);
  
  app.route('/contest/find')
    .post(contest.find);

  app.route('/contest/update')
    .post(contest.update);

  app.route('/contest/parsetext')
    .post(contestP.parseText);

  app.route('/contest/get')
    .post(contestP.getcontest);

  app.route('/contest/submit')
    .post(contestP.mark);

  app.route('/contest/quizz')
    .post(contestP.quizz);

  app.route('/contest/log')
    .post(contestP.getLog);

  app.route('/contest/savefile')
    .post(contestP.saveFile)
    .get(contestP.download);

  /*app.get('/download', function(req, res){
    var file = __dirname + '/../../../../../hw.doc';
    res.download(file); // Set disposition and send it.
  });*/
};
