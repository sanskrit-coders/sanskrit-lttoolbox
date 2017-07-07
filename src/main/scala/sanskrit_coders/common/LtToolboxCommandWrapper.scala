package sanskrit_coders.common

import org.slf4j.LoggerFactory
import scala.sys.process._

class LtToolboxCommandWrapper(val binFilePath: String) {
  val log = LoggerFactory.getLogger(getClass.getName)
  def queryBin(query: String): String = {
    val command = s"echo ${query}" #| s"/usr/bin/lt-proc -ct $binFilePath"
//     #| "/home/vvasuki/scl/build/converters/ri_skt" #| "/home/vvasuki/scl/build/converters/iscii2utf8.py 1 "
    log debug command.toString
    val stdout = new StringBuilder
    val stderr = new StringBuilder
    val status = command ! ProcessLogger(stdout append _, stderr append _)
    if (stderr.nonEmpty) {
      log error s"status ${status}, stderr: $stderr, stdout: $stdout"
      throw new RuntimeException(stderr.toString())
    }
    log debug stdout.toString()
    stdout.toString()
  }
}

