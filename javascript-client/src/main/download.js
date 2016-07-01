'use strict'

const serviceMessage = require('./serviceMessage')
const encodeEntry = require('./encodeEntry')
const Base64 = require('./base64')
const { i, fileNotReady } = require('./filePromises')

function download (args, Vars, callback) {
  return (
    Promise.resolve(fileNotReady(args.local_file_path))
    .then(new Promise(
      (res, rej) => {
        if ('host' in Vars && 'port' in Vars) {
          Promise.resolve('username' in Vars && 'password' in Vars) // is a password_hash
          .then((authenticated) => {
            if (authenticated) {
              let decoder = new Base64()

              let msg = {
                clientMessage: {
                  commandClassName: 'Download',
                  credentials: {
                    username: Vars.username,
                    password: Vars.password
                  },
                  args: {
                    entry: [
                      encodeEntry('filepath', 'string', args.local_file_path)
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
                    Vars.Log.info('Received file ' + args.local_file_path)
                    decoder.decode(response.file, args.local_file_path)
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

module.exports = download
