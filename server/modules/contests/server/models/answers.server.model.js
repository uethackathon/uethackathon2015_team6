'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

/**
 * Article Schema
 */
var AnswerSchema = new Schema({
  date: {
    type: Date,
    default: Date.now
  },
  user: {
    type: Schema.ObjectId,
    ref: 'User'
  },
  username: {
    type: String
  },
  contest: {
    type: Schema.ObjectId,
    ref: 'Contest'
  },
  correct: {
    type: Number
  },
  total: {
    type: Number
  },
  questions: {
  }
});

mongoose.model('Answer', AnswerSchema);
