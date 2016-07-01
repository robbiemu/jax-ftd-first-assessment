'use strict'

const serviceMessage = require('./serviceMessage')
const encodeEntry = require('./encodeEntry')
const Base64 = require('./base64')
const fileReady = require('./filePromises')

function upload (args, Vars, callback) {
  return (
    Promise.resolve(fileReady(args.local_file_path))
    .then(new Promise(
      (res, rej) => {
        if ('host' in Vars && 'port' in Vars) {
          Promise.resolve('username' in Vars && 'password' in Vars) // is a password_hash
          .then((authenticated) => {
            Vars.Log.info('testing authentication')

            if (authenticated) {
              Vars.Log.info('preparing to encode file (1/2)')

              let encoder = new Base64()

              Vars.Log.info('preparing to encode file')

              let msg = {
                clientMessage: {
                  commandClassName: 'Upload',
                  credentials: {
                    username: Vars.username,
                    password: Vars.password
                  },
                  args: {
                    entry: [
                      encodeEntry('filepath', 'string', args.local_file_path),
                      encodeEntry('file', 'string', encoder.encode(args.local_file_path))
                    ]
                  }
                }
              }

              Vars.Log.info(`sending ${JSON.stringify(msg)}`)

              serviceMessage(JSON.stringify(msg), Vars)
                .then((response) => {
                  if (response.errors) {
                    Vars.Log.error(`[${response.type}] ${response.message}`)
                  } else {
                    // encode and pass file
                  }
                })
            } else {
              Vars.Log.warn('Server requires authentication! please use login first.')
            }
          }).then(callback())
        } else {
          Vars.Log.warn('Host information not configured! please use configure_host first.')
        }
      })
    )
    .catch((err) => {
      Vars.Log.warn(`File error: ${err}`)
      callback()
    })
  )
}

module.exports = upload
