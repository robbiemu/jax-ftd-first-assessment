'use strict'

const Vorpal = require('vorpal')
const vorpalLogger = require('vorpal-log')
const bcrypt = require('bcryptjs')

const configureHost = require('./configureHost')
const register = require('./register')
const login = require('./login')
const upload = require('./upload')
const download = require('./download')

const Vars = {}

const LONG_NAME = 'robbie_is_awesome'
const SHORT_NAME = 'wk3prj'
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
  cli.command('login <username> <password>')
    .alias('auth')
    .alias('authenticate_with')
    .description('Connections will be made with the given credentials')
    .action(function (args, callback) {
      login(args, Vars, function (c) {
        Log.log(c)
        if ('username' in c) {
          Vars.username = c.username
          Vars.password = c.password

          let prompt = `${c.username}@${Vars.host}~$`
          cli.delimiter(prompt)
          cli.ui.delimiter = prompt
        }
        callback()
      })
    })

  cli.command('upload <local_file_path>')
    .alias('create')
    .description('Using current credentials for authentication, upload a file to the database')
    .action(function (args, callback) { upload(args, Vars, callback) })

  cli.command('download <local_file_path>')
    .alias('read')
    .description('Using current credentials for authentication, retrieve a file from the database')
    .action(function (args, callback) { download(args, Vars, callback) })

/*  cli.command('list').action(new Promise((res,rej) =>{ res('I promise to implement this list if you want') }))
  cli.command('list')
      .description('Using current credentials for authentication, list files belonging to user')
      .action(function (args, callback) { list(args, Vars, callback) }) */

  cli.show()
}

module.exports = { run }
