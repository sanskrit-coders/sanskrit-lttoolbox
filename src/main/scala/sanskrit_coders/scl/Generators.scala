package sanskrit_coders.scl

import org.slf4j.{Logger, LoggerFactory}
import sanskrit_coders.common.LtToolboxCommandWrapper

class SubantaGenerator(override val binFilePath: String) extends LtToolboxCommandWrapper(binFilePath=binFilePath) {
  override val log: Logger = LoggerFactory.getLogger(getClass.getName)
  final val prakaaraCodeMap = Map[String, String](
    "sAXAraNa" -> "nA",
    "sarvanAma" -> "sarva",
    "safkhyA" -> "saMKyA",
    "safkhyeya" -> "saMKyeyam",
    "pUraNa" -> "pUraNam"
  )
  val lingaCodeMap = Map[String, String](
    "pum" -> "puM",
    "napum" -> "napuM",
    "swrI" -> "swrI"
  )
  val vachanaCodeMap = Map[Int, String](
    1 -> "eka",
    2 -> "xwi",
    3 -> "bahu"
  )

  // Example query: jFAna<vargaH:nA><lifgam:napuM><viBakwiH:1><vacanam:bahu><level:1>
  def getQuery(root: String, prakAra: String, linga: String, vibhakti: Int, vachana: Int) : String = {
    s"${root}" +
      s"<vargaH:${prakaaraCodeMap.get(prakAra).get}>" +
      s"<lifgam:${lingaCodeMap.get(linga).get}>" +
      s"<viBakwiH:${vibhakti}>" +
      s"<vacanam:${vachanaCodeMap.get(vachana).get}>" +
      s"<level:1>"
  }

  def getSubanta(root: String, prakAra: String, linga: String, vibhakti: Int, vachana: Int): Seq[String] = {
    val result = queryBin(getQuery(root, prakAra, linga, vibhakti, vachana))
    result.split("/")
  }
}

object subantaGeneratorTest {
  val log = LoggerFactory.getLogger(getClass.getName)
  def main(args: Array[String]): Unit = {
    val subantaGenerator = new SubantaGenerator(binFilePath = "/home/vvasuki/scl/build/morph_bin/sup_gen.bin")
    var subanta = subantaGenerator.getSubanta("jFAna", "sAXAraNa", "napum", 2, 3)
    log info s"subanta: $subanta"
  }
}