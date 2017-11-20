package sanskrit_coders.scl

import akka.actor.{Actor, ActorLogging}
import dbSchema.grammar.{Analysis, SclAnalysis}
import org.slf4j.{Logger, LoggerFactory}
import sanskrit_coders.common.LtToolboxCommandWrapper

import scala.collection.mutable

class Analyser(override val binFilePath: String) extends LtToolboxCommandWrapper(binFilePath = binFilePath) {
  private val log: Logger = LoggerFactory.getLogger(getClass.getName)

  // echo 'corayanwi' | /usr/bin/lt-proc -ct /home/vvasuki/scl/build/morph_bin/all_morf.bin
  // cur1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>/
  // <kqw_vrb_rt:cur1><kqw_prawyayaH:Sawq_lat><XAwuH:curaz><gaNaH:curAxiH>corayaw<vargaH:nA><lifgam:napuM><viBakwiH:1><vacanam:bahu><level:2>/
  // <kqw_vrb_rt:cur1><kqw_prawyayaH:Sawq_lat><XAwuH:curaz><gaNaH:curAxiH>corayaw<vargaH:nA><lifgam:napuM><viBakwiH:2><vacanam:bahu><level:2>/cur1<sanAxi_prawyayaH:Nic><prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>
  def analyze(word: String): Seq[Analysis] = {
    val result = queryBin(query = word)
    result.split("/").map(result => {
      val tokens = result.replaceAllLiterally("><", ",").replaceAll("[<>]",",").split(",").filterNot(_.isEmpty)
      var qualifications = mutable.HashMap[String, String]()
      if (tokens.length > 1) {
        Some(tokens.foreach(token => {
          val subtokens = token.split(":")
          subtokens.length match {
            case 1 => qualifications.put("root", subtokens.last)
            case 2 => qualifications.put(subtokens.head, subtokens.last)
            case _ =>
              throw new RuntimeException(s"Cannot parse: $result")
          }
        }))
      }
      SclAnalysis(qualifications = qualifications.toMap).toAnalysis
    })
  }
}

class AnalyserActor(analyser: Analyser) extends Actor with ActorLogging {

  def receive: Receive = {
    case word: String => { sender() ! analyser.analyze(word = word)}
  }
}

object analyzerTest {
  val log: Logger = LoggerFactory.getLogger(getClass.getName)
  def main(args: Array[String]): Unit = {
    val analyser = new Analyser(binFilePath = "/home/vvasuki/scl/build/morph_bin/all_morf.bin")
    log info analyser.analyze(word = "corayanwi").mkString("\n")
  }
}