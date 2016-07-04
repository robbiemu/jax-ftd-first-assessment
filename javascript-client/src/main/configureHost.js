'use strict'

function configureHost (args, Vars, callback) {
  return (
    Promise
      .resolve(args.host === undefined) // is user already registered?
      .then((blankHost) => {
        if (blankHost) {
          args.host = 'localhost'
        }
        Vars.host = args.host
        Vars.port = args.port
      })
      .then(() => Vars.Log.info('Host configuration successful.'))
      .then(callback())
      .catch((err) => Vars.Log.error(`the end of the world! ${err}`))
  )
}

module.exports = configureHost
