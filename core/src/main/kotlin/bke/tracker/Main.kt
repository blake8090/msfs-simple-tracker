package bke.tracker

import io.ktor.network.selector.SelectorManager
import javafx.scene.paint.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val server = Server(SelectorManager(Dispatchers.IO))
    val mapPanel = MapPanel()
    val window = Window("Simple Flight Tracker", mapPanel)

    window.onClose = {
        runBlocking {
            server.send("STOP")
            server.stop()
        }
    }

    CoroutineScope(Dispatchers.Default).launch {
        server.acceptClient()
    }

    window.open()

    /*
    SAMPLE DATA:
    Plane latitude: -37.0082111351168
    Plane longitude: 174.78156941940162
    Plane altitude: 24.680170927245964
    */
    val latitude = -37.0082111351168
    val longitude = 174.78156941940162
    mapPanel.drawPoint(latitude, longitude, Color.RED, 10f)
    mapPanel.flyTo(latitude, longitude, 500777.0, 3f)
}
