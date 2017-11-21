package sanskrit_coders.scl

import akka.actor.{Actor, ActorLogging}
import dbSchema.grammar.{Subanta, Tinanta}
import org.slf4j.{Logger, LoggerFactory}
import sanskrit_coders.common.LtToolboxCommandWrapper

class SubantaGenerator(override val binFilePath: String) extends LtToolboxCommandWrapper(binFilePath = binFilePath) {
  private val log: Logger = LoggerFactory.getLogger(getClass.getName)
  private final val prakaaraCodeMap = Map[String, String](
    "sAXAraNa" -> "nA",
    "sarvanAma" -> "sarva",
    "safkhyA" -> "saMKyA",
    "safkhyeya" -> "saMKyeyam",
    "pUraNa" -> "pUraNam"
  )
  private val lingaCodeMap = Map[String, String](
    "pum" -> "puM",
    "napum" -> "napuM",
    "swrI" -> "swrI"
  )

  // Example query: jFAna<vargaH:nA><lifgam:napuM><viBakwiH:1><vacanam:bahu><level:1>
  def getQuery(root: String, prakAra: String, linga: String, vibhakti: Int, vachana: Int): String = {
    s"${root}" +
      s"<vargaH:${prakaaraCodeMap(prakAra)}>" +
      s"<lifgam:${lingaCodeMap(linga)}>" +
      s"<viBakwiH:${vibhakti}>" +
      s"<vacanam:${vachanaCodeMap(vachana)}>" +
      s"<level:1>"
  }

  def getSubanta(root: String, prakAra: String, linga: String, vibhakti: Int, vachana: Int): Seq[String] = {
    val result = queryBin(getQuery(root, prakAra, linga, vibhakti, vachana))
    result.split("/")
  }
}


class TinantaGenerator(override val binFilePath: String) extends LtToolboxCommandWrapper(binFilePath = binFilePath) {
  private val log: Logger = LoggerFactory.getLogger(getClass.getName)

  // aMSa1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:eka><paxI:AwmanepaxI><XAwuH:aMSa><gaNaH:curAxiH><level:1>
  // cur1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>
  def getQuery(root: String, kimpadI: String, dhAtu: String, gaNa: String, prayoga: String,
               lakAra: String, puruSha: String, vachana: String): String = {
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

  //  cur1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>
  def getTinanta(root: String, kimpadI: String, dhAtu: String, gaNa: String, prayoga: String,
                 lakAra: String, puruSha: String, vachana: Int): Seq[String] = {
    val result = queryBin(getQuery(root = root, kimpadI = kimpadI, dhAtu = dhAtu, gaNa = gaNa, prayoga = prayoga, lakAra = lakAra, puruSha = puruSha, vachana = vachanaCodeMap(vachana)))
    result.split("/")
  }

}


class GeneratorActor(subantaGenerator: SubantaGenerator, tinantaGenerator: TinantaGenerator) extends Actor with ActorLogging {

  def receive: Receive = {
    case subanta: Subanta => {
      var vibhaktiNum = subanta.vibhakti.get.vibhaktiNum
      if(subanta.vibhakti.get.prakaara.isDefined) {
        vibhaktiNum = 8
      }

      val padas = subantaGenerator.getSubanta(root = subanta.praatipadika.get.root.get, prakAra = subanta.praatipadika.get.prakaara.get, linga = subanta.praatipadika.get.linga.get, vibhakti = vibhaktiNum, vachana = subanta.vachana.get)
      sender() ! padas
    }
    case tinanta: Tinanta => {
      val padas = tinantaGenerator.getTinanta(root = tinanta.dhaatu.get.sclCode.get, kimpadI = tinanta.vivaxaa.get.kimpadI.get, dhAtu = tinanta.dhaatu.get.root.get,
        gaNa = tinanta.dhaatu.get.gaNas.get.head,
        prayoga = tinanta.vivaxaa.get.prayoga.get, lakAra = tinanta.vivaxaa.get.lakaara.get, puruSha = tinanta.vivaxaa.get.puruSha.get, vachana = tinanta.vivaxaa.get.vachana.get)
      sender() ! padas
    }
  }
}

