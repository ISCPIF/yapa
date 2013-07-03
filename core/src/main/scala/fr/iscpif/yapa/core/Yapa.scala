package fr.iscpif.yapa.core

object Yapa extends App {
  val vm = new VM("qemu-system-x86_64", "/media/martin-port/test/DDV.img")
  val p = vm.process
  Thread.sleep(5000)
  val ssh = new SshObject("localhost", 2222, "yapa", "yapa")
  try {
  ssh.chat
  }
  finally { println("prog end")
    p.destroy()
  }
}