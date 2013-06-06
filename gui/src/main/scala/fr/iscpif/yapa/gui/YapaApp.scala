package fr.iscpif.yapa.gui

import swing._

object YapaApp  extends SimpleSwingApplication{

  def top = new MainFrame {
    title = "Yapa"
    preferredSize = new Dimension(500,300)
    contents = new Button("click")
  }
}
