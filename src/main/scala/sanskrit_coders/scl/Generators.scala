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


class TinantaGenerator(override val binFilePath: String) extends LtToolboxCommandWrapper(binFilePath=binFilePath) {
  override val log: Logger = LoggerFactory.getLogger(getClass.getName)

  // aMSa1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:eka><paxI:AwmanepaxI><XAwuH:aMSa><gaNaH:curAxiH><level:1>
  def getQuery(root: String, kimpadI: String, dhAtu: String, gaNa: String, prayoga: String,
               lakAra: String, puruSha: String, vachana: String) : String = {
    s"${root}" +
      s"<prayogaH:${prayoga}>" +
      s"<lakAraH:${lakAra}>" +
      s"<puruRaH:${puruSha}>" +
      s"<vacanam:${vachana}>" +
      s"<paxI:${kimpadI}>" +
      s"<XAwuH:${dhAtu}>" +
      s"<gaNaH:${gaNa}>" +
      s"<level:1>"
  }

  // This does not work, even with the following full command:
  //  echo 'cur1<prayogaH:karwari><lakAraH:lqf><puruRaH:u><vacanam:bahu><paxI:AwmanepaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>' | /usr/bin/lt-proc -ct /home/vvasuki/scl/build/morph_bin/wif_gen.bin |/home/vvasuki/scl/build/converters/ri_skt | /home/vvasuki/scl/build/converters/iscii2utf8.py 1
  def getTinanta(root: String, kimpadI: String, dhAtu: String, gaNa: String, prayoga: String,
                 lakAra: String, puruSha: String, vachana: String) : Seq[String] = {
    val result = queryBin(getQuery(root=root, kimpadI = kimpadI, dhAtu = dhAtu, gaNa = gaNa, prayoga = prayoga, lakAra = lakAra, puruSha = puruSha, vachana = vachana))
    result.split("/")
  }

}


//aMSa1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:eka><paxI:AwmanepaxI><XAwuH:aMSa><gaNaH:curAxiH><level:1>
//aMsa1<prayogaH:karwarI><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:AwmanepaxI><XAwuH:aMSa><gaNaH:curAxiH><level:1>
object tinantaGeneratorTest {
  val log = LoggerFactory.getLogger(getClass.getName)
  def main(args: Array[String]): Unit = {
    val tinantaGenerator = new TinantaGenerator(binFilePath = "/home/vvasuki/scl/build/morph_bin/wif_gen.bin")
    var tinanta = tinantaGenerator.getTinanta(root = "aMsa1", kimpadI = "AwmanepaxI", dhAtu = "aMSa",
      gaNa = "curAxiH",
      prayoga = "karwarI", lakAra = "lat", puruSha = "pra", vachana = "eka")
    log info s"tinanta: $tinanta"
  }
}

