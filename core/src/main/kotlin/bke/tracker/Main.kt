package bke.tracker

import com.formdev.flatlaf.FlatDarkLaf
import com.gluonhq.maps.MapLayer
import com.gluonhq.maps.MapPoint
import com.gluonhq.maps.MapView
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.core.readBytes
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.embed.swing.JFXPanel
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

private const val PORT = 9001

fun main() {

    runBlocking {
        val address = InetSocketAddress("127.0.0.1", PORT)
        val socket = aSocket(ActorSelectorManager(Dispatchers.IO))
            .tcp()
            .connect(address)
        println("Connected to $address")

        val input = socket.openReadChannel()
        val output = socket.openWriteChannel(autoFlush = true)

        while (true) {
            input.awaitContent()
            val packetSize = input.availableForRead
            val packet = input.readPacket(packetSize)
            val message = String(packet.readBytes())
            println("Received packet: '$message' size: ${packetSize} bytes")
        }
    }
}

private fun initGui() {
    FlatDarkLaf.setup()

    SwingUtilities.invokeLater {
        JFrame("Simple Flight Tracker").apply {
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

            val fxPanel = JFXPanel()
            add(fxPanel)

            preferredSize = Dimension(800, 600)
            pack()
            setLocationRelativeTo(null)
            isVisible = true

            Platform.runLater {
                initFX(fxPanel)
            }

            addWindowListener(object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent) {
                    super.windowClosing(e)
                    println("Closing window")
                }
            })
        }
    }
}

/*
Plane latitude: -37.0082111351168
Plane longitude: 174.78156941940162
Plane altitude: 24.680170927245964
 */
private fun initFX(panel: JFXPanel) {
    val stackPane = StackPane()
    val scene = Scene(stackPane)
    panel.scene = scene

    val mapView = MapView()
    mapView.setPrefSize(800.0, 600.0)
    mapView.zoom = 5.0

    val layer = CustomMapLayer()
    mapView.addLayer(layer)
    layer.addPoint(MapPoint(-37.0082111351168, 174.78156941940162), Circle(7.0, Color.RED))

    stackPane.children.add(mapView)
}

class CustomMapLayer : MapLayer() {

    private val points: ObservableList<Pair<MapPoint, Node>> = FXCollections.observableArrayList()

    fun addPoint(p: MapPoint, icon: Node) {
        points.add(p to icon)
        children.add(icon)
        markDirty()
    }

    override fun layoutLayer() {
        for ((point, icon) in points) {
            val mapPoint: Point2D = getMapPoint(point.latitude, point.longitude)
            icon.isVisible = true
            icon.translateX = mapPoint.getX()
            icon.translateY = mapPoint.getY()
        }
    }
}
