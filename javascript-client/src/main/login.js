'use strict'

const serviceMessage = require('./serviceMessage')
const comparer = require('./hash')
const encodeEntry = require('./encodeEntry')

function login (args, Vars, callback) {
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
                      Vars.Log.info('Login successful.')
                    }
                  })
              }
            }
          })
          .catch((e) => { Vars.Log(e) })
      } else {
        Vars.Log.warn('Host information not configured! please use configure_host first.')
      }
    })
    .then(callback())
  )
}

module.exports = login
