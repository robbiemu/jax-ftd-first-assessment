'use strict'

const fs = require('fs')

function fileReady (file_path) {
  return new Promise(function (res, rej) {
    fs.access(file_path, function (err) {
      // is accessible
      if (err && err.code === 'ENOENT') {
        rej(new Error(`Error accessing file ${file_path}: [${err.code}] - ${err}`))
      } else {
        // is file
        fs.stat(file_path, function (err, stats) {
          if (err) {
            rej(`Error with file stats file ${file_path}: [${err.code}] - ${err}`)
          }
          if (stats.isFile()) {
            res(true)
          } else {
            rej(new Error('is not a file'))
          }
        })
      }
    })
  })
}

function fileNotReady (file_path) {
  return new Promise(function (res, rej) {
    fs.access(file_path, function (err) {
      // is accessible
      if (err && err.code === 'ENOENT') {
        res(true)
      } else {
        rej(`Error: file exists - ${file_path}`)
      }
    })
  })
}

module.exports = { fileReady, fileNotReady }
