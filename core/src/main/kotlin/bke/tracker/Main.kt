package bke.tracker

import com.formdev.flatlaf.FlatDarkLaf
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

fun main() {
    FlatDarkLaf.setup()

    SwingUtilities.invokeLater {
        JFrame("Simple Flight Tracker").apply {
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

            contentPane.add(JLabel("Hello World", SwingConstants.CENTER))

            preferredSize = Dimension(800, 600)
            pack()
            setLocationRelativeTo(null)
            isVisible = true
        }
    }
}
