'use strict'

const net = require('net')

function serviceMessage (msg, Vars) {
  return new Promise((resolve, reject) => {
    let server = net.createConnection({host: Vars.host, port: Vars.port}, () => {
      // Vars.Log.info(`connected to server ${Vars.host}:${Vars.port} with client: ${server.address().address}:${server.address().port}`)
      try {
        server.write(`${msg}\n`)

        server.on('data', (data) => {
          Vars.Log.info(`data ${data}`)
          let response = JSON.parse(data)
          resolve((function () {
            server.destroy() // this is happening async so I don't want it in FINALLY
            return response
          })())
        })
      } catch (e) {
        server.destroy()
        reject(e)
      }
    })

    server.on('error', function (err) {
      Vars.Log.error('Connection error. Error message: ' + err.message)
    })
  })
}

module.exports = serviceMessage
