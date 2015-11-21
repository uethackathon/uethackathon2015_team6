'use strict';

/**
 * Module dependencies.
 */
var path = require('path'),
  mongoose = require('mongoose'),
  Contest = mongoose.model('Contest');

exports.create = function (req, res) {
  console.log(req.user);
  var contest = new Contest();
  contest.creator = req.user;

  contest.save(function (err) {
    res.json(contest);
  });
};

exports.list = function (req, res) {
  Contest.find().sort('-dateCreated').populate('creator').exec(function (err, contests) {
    if (err) {
      return res.status(400).send({
        message: errorHandler.getErrorMessage(err)
      });
    } else {
      res.json(contests);
    }
  });
};

exports.find = function (req, res) {
  Contest.find({_id: req.body.id}, function (err, contest) {
    if (!err)
      res.json(contest);
  });
};

exports.update = function (req, res) { 
  var tmp = req.body.contest;
  tmp.questions = req.body.questions;
  tmp.header = req.body.header;
  var id = tmp._id;
  delete tmp._id;
  Contest.findOneAndUpdate({_id: id}, tmp, function (err, contest) {
    //console.log(tmp);
    //console.log(contest);
    if (!err)
      res.json(contest);
    else
      console.log(err);
  });
};

exports.parseText = function(req, res) {
  var fs = require('fs');

  fs.writeFile("../temp.txt", req.body.text, function(err) {
      if(err) {
          return console.log(err);
      }
  }); 

  setTimeout(function() {
      var process = require("child_process").exec(
      'python ' + '../converter.py',
      function (error, stdout, stderr) {
        if (error) {
          console.log(error.stack);
          console.log('Error code: '+error.code);
          console.log('Signal received: '+error.signal);
        }
        console.log('Child Process STDERR: '+stderr);
        res.json(stdout);
      }
    );

    process.on("exit", function (code) {
      console.log("spawnEXIT:", code)
    });
  }, 100);

  setTimeout(function() {
    var obj = JSON.parse(fs.readFileSync('../output.txt', 'utf8'));
    res.json(obj);
  }, 100);
  
}