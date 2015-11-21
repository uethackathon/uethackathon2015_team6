'use strict';

/**
 * Module dependencies
 */
var path = require('path'),
  mongoose = require('mongoose'),
  Contest = mongoose.model('Contest'),
  Answer = mongoose.model('Answer');

exports.mark = function(req, res){
  //console.log(req.body);
  var mark = function(id, questions, answers){
    var total = questions.length;
    var correct = 0;
    var answer = {};
    answer.user = req.user;
    if (req.user)
      answer.username = req.user.username;
    answer.questions = [];
    answer.contest = id;

    for(var i = 0; i < questions.length; i++){
      var flag = true;
      answer.questions.push({
        key: [],
        answer: [],
        correct: true
      });
      for(var j = 0; j < questions[i].choices.length; j++){
        if (questions[i].choices[j].correct != answers[i][j])
          flag = false;

        answer.questions[i].key.push(questions[i].choices[j].correct);
        answer.questions[i].answer.push(answers[i][j]);
      }
      answer.questions[i].correct = flag;
      if (flag)
        correct += 1;
    }

    answer.correct = correct;
    answer.total = total;
    Answer(answer).save();

    return {correct: correct, total: total};
  }
  console.log(req.body);
  Contest.findOne({_id: req.body.id}, function(err, contest){
    if (err || !contest){
      res.json("Contest not exists");
      return;
    }
    contest = contest.toObject();

    var result = mark(contest._id, contest.questions, req.body.answers);
    
    res.json(result);
  }); 
}

//for student, get only question
exports.getcontest = function(req, res){
  //console.log(req.body);
  Contest.findOne({_id: req.body.id}, function(err, contest){
    if (err || !contest || !contest.questions){
      res.json("Contest not exists");
      return;
    }
    contest = contest.toObject();
    for(var i = 0; i < contest.questions.length; i++){
      for(var j = 0; j < contest.questions[i].choices.length; j++){
        contest.questions[i].choices[j].correct = false;
      }
    }
    res.json(contest);
  }); 
}

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

exports.saveFile = function(req, res){
  var fs = require('fs');
  fs.writeFile("../input.txt", JSON.stringify(req.body), function(err) {
      if(err) {
          return console.log(err);
      }
  });   
  setTimeout(function() {
      var process = require("child_process").exec(
      'python ' + '../exporter.py',
      function (error, stdout, stderr) {
        if (error) {
          console.log(error.stack);
          console.log('Error code: '+error.code);
          console.log('Signal received: '+error.signal);
        }
        console.log('Child Process STDERR: '+stderr);
      }
    );

    process.on("exit", function (code) {
      console.log("spawnEXIT:", code);
      res.json('ok');
    });
  }, 100);
}

exports.download = function(req, res){
  var file = __dirname + '/../../../../../test.docx';
  res.download(file);
}

exports.quizz = function(req, res){
  Contest.find({}, function(err, contests){
    //console.log(contests);
    var arr = [];
    for(var i = 0; i < contests.length; i++){
      if (!err && contests[i] && contests[i].questions){
        //console.log(contests[i].questions);
        for(var j = 0; j < contests[i].questions.length; j++){
          arr.push(contests[i].questions[j]);
        }
      }  
    }
    //console.log(arr);
    res.json(arr[Math.floor((Math.random() * arr.length) + 1)]);
  });
}

exports.getLog = function(req, res){
  Answer.find({contest: req.body.id}, function(err, answers){
    res.json(answers);
  });
}