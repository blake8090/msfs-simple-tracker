package bke.tracker

import com.formdev.flatlaf.FlatDarkLaf
import com.gluonhq.maps.MapLayer
import com.gluonhq.maps.MapPoint
import com.gluonhq.maps.MapView
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
import mu.KotlinLogging
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class Gui {

    private val log = KotlinLogging.logger {}
    private val aircraftLayer = AircraftMapLayer()

    var onClose: () -> Unit = {}

    fun start() {
        FlatDarkLaf.setup()
        SwingUtilities.invokeLater {
            createWindow()
        }
    }

    private fun createWindow() {
        JFrame("Simple Flight Tracker").apply {
            preferredSize = Dimension(1920, 1080)
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

            add(createMapPanel())

            pack()
            setLocationRelativeTo(null)
            isVisible = true

            addWindowListener(object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent) {
                    super.windowClosing(e)
                    log.info { "Closing window" }
                    onClose.invoke()
                }
            })
        }
    }

    private fun createMapPanel(): JFXPanel {
        val panel = JFXPanel()

        Platform.runLater {
            val stackPane = StackPane()
            val scene = Scene(stackPane)
            panel.scene = scene

            val mapView = MapView()
            mapView.zoom = 5.0
            mapView.addLayer(aircraftLayer)
            /*
            Plane latitude: -37.0082111351168
            Plane longitude: 174.78156941940162
            Plane altitude: 24.680170927245964
            */
            val point = MapPoint(-37.0082111351168, 174.78156941940162)
            aircraftLayer.addPoint(point, Circle(7.0, Color.RED))

            stackPane.children.add(mapView)
        }

        return panel
    }
}

class AircraftMapLayer : MapLayer() {

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
