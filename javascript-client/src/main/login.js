'use strict'

const serviceMessage = require('./serviceMessage')
const comparer = require('./hash')
const encodeEntry = require('./encodeEntry')
const Defs = require('./defs')

function login (args, Vars, callback) {
  let Credentials = {}
  return (
    Promise.resolve('host' in Vars && 'port' in Vars)
    .then((hostReady) => {
      if (hostReady) {
        let msg = {
          clientMessage: {
            commandClassName: 'Login',
            credentials: {
              username: Vars.username,
              password: Vars.password
            },
            args: {
              entry: [ encodeEntry('username', 'string', args.username) ]
            }
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
              if (response.credentials.password) {
                comparer.compare(args.password, response.credentials.password,
                    Vars.Log)
                  .then((result) => {
                    if (!result) { // login unsuccessful
                      Vars.Log.warn('Failed to authenticate, passwords don\'t match')
                    } else {
                      Credentials.username = args.username
                      Credentials.password = response.credentials.password
                      Vars.Log.info('Login successful.')
                    }
                  })
                  .then(function () { callback(Credentials) })
              }
            }
          })
          .catch((e) => { Vars.Log(e) })
      } else {
        Vars.Log.warn('Host information not configured! please use configure_host first.')
      }
    })
  )
}

module.exports = login
