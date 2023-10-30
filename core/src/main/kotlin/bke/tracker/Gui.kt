package bke.tracker

import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.esri.arcgisruntime.layers.OpenStreetMapLayer
import com.esri.arcgisruntime.loadable.LoadStatus
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.formdev.flatlaf.FlatDarkLaf
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import mu.KotlinLogging
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class Gui {

    private val log = KotlinLogging.logger {}

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

            val layer = OpenStreetMapLayer()
            // if the layer failed to load, display an alert
            layer.addDoneLoadingListener {
                if (layer.getLoadStatus() !== LoadStatus.LOADED) {
                    Alert(
                        Alert.AlertType.INFORMATION,
                        "Open Street Map Layer failed to load"
                    ).show()
                }
            }

            // create a map and set the open street map layer as its basemap
            val map = ArcGISMap()
            map.basemap = Basemap(layer)

            // create a map view and set the map to it
            val mapView = MapView()
            mapView.setMap(map)

            /*
            SAMPLE DATA:
            Plane latitude: -37.0082111351168
            Plane longitude: 174.78156941940162
            Plane altitude: 24.680170927245964
             */
            val latitude = -37.0082111351168
            val longitude = 174.78156941940162

            // set a viewpoint on the map view
            mapView.setViewpoint(Viewpoint(latitude, longitude, 577790.0))

            val graphicsOverlay = GraphicsOverlay()
            mapView.graphicsOverlays.add(graphicsOverlay)

            // create a point geometry with a location and spatial reference
            val point = Point(longitude, latitude, SpatialReferences.getWgs84())
            // create an opaque orange point symbol with a opaque blue outline symbol
            val simpleMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 10f)
            val blueOutlineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2f)
            simpleMarkerSymbol.outline = blueOutlineSymbol

            // create a graphic with the point geometry and symbol
            val pointGraphic = Graphic(point, simpleMarkerSymbol)

            // add the point graphic to the graphics overlay
            graphicsOverlay.graphics.add(pointGraphic)

            // add the map view to stack pane
            stackPane.children.add(mapView)
        }

        return panel
    }
}
