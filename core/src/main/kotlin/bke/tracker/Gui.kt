package bke.tracker

import com.formdev.flatlaf.FlatDarkLaf
import mu.KotlinLogging
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class Gui {

    private val log = KotlinLogging.logger {}

    val mapPanel = MapPanel()
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

            add(mapPanel)

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
}
