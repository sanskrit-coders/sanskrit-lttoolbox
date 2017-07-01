package sanskrit_coders.common

import org.slf4j.LoggerFactory
import scala.sys.process._

class LtToolboxCommandWrapper {
  val log = LoggerFactory.getLogger(getClass.getName)
  def queryBin(binFilePath: String, query: String): String = {
    val command = s"echo ${query}|/usr/bin/lt-proc -ct $binFilePath"
    log info command
    val stdout = new StringBuilder
    val stderr = new StringBuilder
    val status = command ! ProcessLogger(stdout append _, stderr append _)
    if (stderr.nonEmpty) {
      log error s"status ${status}, stderr: $stderr, stdout: $stdout"
      throw new RuntimeException(stderr.toString())
    }
    stdout.toString()
  }
}

