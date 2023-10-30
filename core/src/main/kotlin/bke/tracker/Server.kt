package bke.tracker

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Connection
import io.ktor.network.sockets.ServerSocket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import mu.KotlinLogging
import java.nio.ByteBuffer

const val PORT = 9001

class Server(selectorManager: SelectorManager) {

    private val log = KotlinLogging.logger {}
    private val serverSocket: ServerSocket
    private val connections = mutableSetOf<Connection>()

    init {
        serverSocket = aSocket(selectorManager)
            .tcp()
            .bind("127.0.0.1", PORT)
        log.info { "Server is listening at ${serverSocket.localAddress}" }
    }

    suspend fun acceptNewClient() {
        log.info { "Waiting for client..." }
        val socket = serverSocket.accept()
        connections.add(
            Connection(
                socket,
                socket.openReadChannel(),
                socket.openWriteChannel(autoFlush = true)
            )
        )
        log.info { "Accepted socket: ${socket.localAddress}" }
    }

    suspend fun stop() = coroutineScope {
        log.info { "Stopping server" }
        val tasks = connections.map { connection ->
            async {
                log.info { "Closing socket on address: ${connection.socket.localAddress}" }
                connection.socket.close()
            }
        }
        tasks.awaitAll()
    }

    suspend fun send(message: String) = coroutineScope {
        val tasks = connections.map { connection ->
            async {
                connection.output.writeFully(ByteBuffer.wrap(message.toByteArray()))
                log.debug { "Sent message '$message' to ${connection.socket.localAddress}" }
            }
        }
        tasks.awaitAll()
    }
}
