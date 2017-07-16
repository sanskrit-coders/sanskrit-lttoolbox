package sanskrit_coders.scl

import dbSchema.grammar.{Qualification, SclAnalysis}
import org.slf4j.{Logger, LoggerFactory}
import sanskrit_coders.common.LtToolboxCommandWrapper

class Analyser(override val binFilePath: String) extends LtToolboxCommandWrapper(binFilePath = binFilePath) {
  override val log: Logger = LoggerFactory.getLogger(getClass.getName)

  // echo 'corayanwi' | /usr/bin/lt-proc -ct /home/vvasuki/scl/build/morph_bin/all_morf.bin
  // cur1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>/
  // <kqw_vrb_rt:cur1><kqw_prawyayaH:Sawq_lat><XAwuH:curaz><gaNaH:curAxiH>corayaw<vargaH:nA><lifgam:napuM><viBakwiH:1><vacanam:bahu><level:2>/
  // <kqw_vrb_rt:cur1><kqw_prawyayaH:Sawq_lat><XAwuH:curaz><gaNaH:curAxiH>corayaw<vargaH:nA><lifgam:napuM><viBakwiH:2><vacanam:bahu><level:2>/cur1<sanAxi_prawyayaH:Nic><prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>
  def analyze(word: String): Seq[SclAnalysis] = {
    val result = queryBin(query = word)
    result.split("/").map(result => {
      val tokens = result.replaceAllLiterally("><", ",").replaceAll("[<>]",",").split(",").filterNot(_.isEmpty)
      var qualifications: Option[Seq[Qualification]] = None
      if (tokens.length > 1) {
        qualifications = Some(tokens.map(token => {
          val subtokens = token.split(":")
          subtokens.length match {
            case 1 => Qualification(category = "root", value = subtokens.last)
            case 2 => Qualification(category = subtokens.head, value = subtokens.last)
            case _ => {
              throw new RuntimeException(s"Cannot parse: $result")
            }
          }
        }))
      }
      new SclAnalysis(qualifications = qualifications)
    })
  }
}

object analyzerTest {
  val log: Logger = LoggerFactory.getLogger(getClass.getName)
  def main(args: Array[String]): Unit = {
    val analyser = new Analyser(binFilePath = "/home/vvasuki/scl/build/morph_bin/all_morf.bin")
    log info analyser.analyze(word = "corayanwi").mkString("\n")
  }
}