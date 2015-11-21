'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

/**
 * Article Schema
 */
var ContestSchema = new Schema({
  dateCreated: {
    type: Date,
    default: Date.now
  },
  dateStart: {
    type: Date
  },
  dateEnd: {
    type: Date
  },
  header: {
    type: String,
    default: 'Default Title'
  },
  creator: {
    type: Schema.ObjectId,
    ref: 'User'
  },
  questions: {
  },
  shuffles: {

  }
});

mongoose.model('Contest', ContestSchema);
