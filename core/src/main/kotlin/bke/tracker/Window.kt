package bke.tracker

import com.formdev.flatlaf.FlatDarkLaf
import mu.KotlinLogging
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

class Window(
    title: String,
    private val content: JComponent
) {

    private val log = KotlinLogging.logger {}
    private val frame = JFrame(title)

    var onClose: () -> Unit = {}

    fun open() {
        FlatDarkLaf.setup()
        SwingUtilities.invokeLater {
            createWindow()
        }
    }

    private fun createWindow() {
        frame.apply {
            preferredSize = Dimension(1920, 1080)
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

            add(content)

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
