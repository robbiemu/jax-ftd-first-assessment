'use strict'

const bcrypt = require('bcryptjs')

const EBCRYPT = 'cannot complete hash encoding locally. please try again in a few minutes'

/**
* uses the bcryptjs library to encode a hash of a given string
* accepts a callback to handle the returning hash
* @param pwd - string to encode
* @param callback - function to call after completion
*/
function encode (pwd, Log) {
  pwd = pwd + ''
  return new Promise((res, rej) => {
    bcrypt.genSalt(10, function (err, salt) {
      if (err) {
        Log.warn(EBCRYPT)
        rej()
      }
      bcrypt.hash(pwd, salt, function (err, hash) {
        if (err) {
          Log.warn(EBCRYPT)
          rej()
        }
        res(hash)
      })
    })
  })
}

/**
* compares a candidate password to a hashed password from the credentials object
* @param candidate - raw (non-hash) password to compare
* @param pwd_hash - encoded (hash) password from credentials object
* @param callback - function handles match/non-match comparison result
*/
function compare (candidate, pwd_hash, Log) {
  candidate = candidate + ''
  return new Promise((res, rej) => {
    bcrypt.compare(candidate, pwd_hash, function (err, result) {
      if (err) {
        Log.warn(EBCRYPT)
        rej()
      }
      res(result)
    })
  })
}

module.exports = { encode, compare }
