'use strict'

function encodeEntry (fieldname, type, value) {
  return {
    key: fieldname,
    value: {
      type: 'string',
      value: value
    }
  }
}

module.exports = encodeEntry
