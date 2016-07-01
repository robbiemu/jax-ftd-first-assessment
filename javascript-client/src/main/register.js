'use strict'

const serviceMessage = require('./serviceMessage')
const hasher = require('./hash')
const encodeEntry = require('./encodeEntry')

function register (args, Vars, callback) {
  return (
    Promise.resolve('host' in Vars && 'port' in Vars)
    .then((hostReady) => {
      Promise.resolve(hasher.encode(args.password, Vars.Log))
      .then((password_hash) => {
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
                  encodeEntry('password_hash', 'string', password_hash)
                ]
              }
            }
          }

          serviceMessage(JSON.stringify(msg), Vars)
            .then((response) => {
              if (response.errors) {
                Vars.Log.error(`Failed to register credentials: [${response.errors.type}] ${response.errors.message}`)
              } else {
                Vars.Log.info(response.result.result)
              }
            })
            .catch((e) => { Vars.Log(e) })
        } else {
          Vars.Log.warn('Host information not configured! please use configure_host first.')
        }
      })
      .then(callback())
    })
  )
}

module.exports = register
