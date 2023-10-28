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
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

fun main() {
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
        }
    }
}

private fun initFX(panel: JFXPanel) {
    val stackPane = StackPane()
    val scene = Scene(stackPane)
    panel.scene = scene

    val mapView = MapView()
    mapView.setPrefSize(800.0, 600.0)
    mapView.zoom = 5.0
    mapView.addLayer(CustomMapLayer())
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
