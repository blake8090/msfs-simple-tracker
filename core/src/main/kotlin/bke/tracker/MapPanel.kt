package bke.tracker

import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.esri.arcgisruntime.layers.OpenStreetMapLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color

class MapPanel : JFXPanel() {

    private val stackPane = StackPane()
    private val map = ArcGISMap()
    private val layer = OpenStreetMapLayer()
    private val mapView = MapView()
    private val graphicsOverlay = GraphicsOverlay()

    init {
        scene = Scene(stackPane)
        map.basemap = Basemap(layer)
        mapView.map = map
        mapView.graphicsOverlays.add(graphicsOverlay)

        Platform.runLater {
            stackPane.children.add(mapView)
        }
    }

    fun flyTo(latitude: Double, longitude: Double, scale: Double, durationSeconds: Float) {
        val viewpoint = Viewpoint(latitude, longitude, scale)
        mapView.setViewpointAsync(viewpoint, durationSeconds)
    }

    fun drawPoint(latitude: Double, longitude: Double, color: Color, size: Float) {
        val point = Point(longitude, latitude, SpatialReferences.getWgs84())
        val symbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, color, size)
        graphicsOverlay.graphics.add(Graphic(point, symbol))
    }
}
