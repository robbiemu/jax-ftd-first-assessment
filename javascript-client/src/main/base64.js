'use strict'

const fs = require('fs')

class Base64 {
  encode (file_local_path) {
    console.log('reading file')
    var bitmap = fs.readFileSync(file_local_path)
    console.log('done reading file')

    return new Buffer(bitmap).toString('base64')
  }

  decode (base64str, file_local_path) {
    var bitmap = new Buffer(base64str, 'base64')
    fs.writeFileSync(file_local_path, bitmap)
  }
}

module.exports = Base64
