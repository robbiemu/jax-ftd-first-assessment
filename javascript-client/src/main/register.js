'use strict'

const serviceMessage = require('./serviceMessage')

function encodeEntry (fieldname, type, value) {
  return {
    key: fieldname,
    value: {
      type: 'string',
      value: value
    }
  }
}

function register (args, Vars, callback) {
  return (
    Promise.resolve('host' in Vars && 'port' in Vars)
      .then((hostReady) => {
        if (hostReady) {
          let msg = {
            clientMessage: {
              commandClassName: 'RegisterUser',
              credentials: {
                username: Vars.username,
                password: Vars.password
              },
              args: {
                entry: [
                  encodeEntry('username', 'string', args.username),
                  encodeEntry('password_hash', 'string', args.password)
                ]
              }
            }
          }

          let response = serviceMessage(JSON.stringify(msg), Vars)

          if (response.error) {
            Vars.Log.error(response.message)
          } else if (response.message) {
            Vars.Log.info(response.message)
          }
        } else {
          Vars.Log.warn('Host information not configured! please use configure_host first.')
        }
      })
      .then(callback())
  )
}

module.exports = register
