'use strict'

const Vorpal = require('vorpal')
const vorpalLogger = require('vorpal-log')
const bcrypt = require('bcryptjs')

const configureHost = require('./configureHost')
const register = require('./register')
/*const login = require('./login')
const upload = require('./upload')
const download = require('./download')*/

const Vars = {}

const LONG_NAME = 'robbie_is_awesome'
const SHORT_NAME = 'week3project'
const DEFAULT_PROMPT = LONG_NAME + '~$'

function run () {
  const cli = Vorpal()

  cli.use(vorpalLogger)
    .delimiter(DEFAULT_PROMPT)

  const exit = cli.find('exit')
  if (exit) {
    exit.description('Exits ' + SHORT_NAME)
  }

  const Log = cli.logger
  Vars.Log = cli.logger

  cli.command('configure_host <port> [host]')
    .alias('host')
    .description('Sets the host and port for connection to the database')
    .action(function (args, callback) { configureHost(args, Vars, callback) })

  cli.command('vars')
    .description('[debug] Echos Vars')
    .action(function (args, callback) {
      let tmp = Vars
      delete tmp.Log
      Log.info(`Vars: ${JSON.stringify(tmp)}`)
      callback()
    })

  cli.command('register <username> <password>')
    .description('Adds credentials for connections to the database')
    .action(function (args, callback) { register(args, Vars, callback) })
/*
  cli.command('login <username> <password>')
    .alias('auth')
    .alias('authenticate_with')
    .description('Connections will be made with the given credentials')
    .action(function (args, callback) { login(Vars, args) })
  cli.command('upload <local file path> [path stored in database]')
    .alias('set')
    .description('Using current credentials for authentication, upload a file to the database')
    .action(function (args, callback) { upload(Vars, args) })
  cli.command('download <database file id> [local file path]')
    .alias('get')
    .description('Using current credentials for authentication, retrieve a file from the database')
    .action(function (args, callback) { download(Vars, args) })
  */

  cli.show()
}

module.exports = { run }
