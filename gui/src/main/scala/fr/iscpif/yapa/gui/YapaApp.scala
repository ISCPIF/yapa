package fr.iscpif.yapa.gui

import swing._
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.JFrame
import com.bennavetta.jconsole._

object YapaApp  extends SimpleSwingApplication{

  def top = new MainFrame {
    title = "Yapa"
    preferredSize = new Dimension(500,300)
    peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    val console = new Console(Color.BLACK, Color.GREEN,
                                new Font(Font.MONOSPACED, Font.BOLD, 14), "Yapa> ")
    console.setPreferredSize(new Dimension(800, 600))
   // console.setCompletionSource(new DefaultCompletionSource("help", "list", "die", "dinosaurs"))
   console.setProcessor(new InputProcessor {
        def process(text: String,console: Console) {
             println("You typed: '" + text + "'")
             }
         }
   )
   peer.add(console)
  }
}
