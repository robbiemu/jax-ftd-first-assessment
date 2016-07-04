'use strict'

const serviceMessage = require('./serviceMessage')
const Defs = require('./defs')

function list (args, Vars, callback) {
  if ('host' in Vars && 'port' in Vars) {
    if ('username' in Vars && 'password' in Vars) {
      let msg = {
        clientMessage: {
          commandClassName: 'ListFiles',
          credentials: {
            username: Vars.username,
            password: Vars.password
          },
          args: {}
        }
      }

      if (Defs.VERBOSE) {
        Vars.Log.info(`sending ${JSON.stringify(msg)}`)
      }

      serviceMessage(JSON.stringify(msg), Vars)
        .then((response) => {
          if (response.errors) {
            Vars.Log.error(`[${response.type}] ${response.message}`)
          } else {
            Vars.Log.info('Server response' + '\n' + response.result.result)
          }
        })
    } else {
      Vars.Log.warn('Server requires authentication! please use login first.')
    }
  } else {
    Vars.Log.warn('Host information not configured! please use configure_host first.')
  }
  callback()
}

module.exports = list
