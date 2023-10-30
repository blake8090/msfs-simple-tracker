package bke.tracker

import io.ktor.network.selector.SelectorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val server = Server(SelectorManager(Dispatchers.IO))

    CoroutineScope(Dispatchers.Default).launch {
        server.acceptNewClient()
    }

    val gui = Gui()
    // TODO: use swing coroutines for this?
    gui.onClose = {
        runBlocking {
            server.send("STOP")
            server.stop()
        }
    }
    gui.start()
}
