package scl

import dbSchema.grammar.SclAnalysis
import dbUtils.jsonHelper
import org.scalatest.FlatSpec
import org.slf4j.LoggerFactory
import sanskrit_coders.scl.Analyser

class AnalyserTest  extends FlatSpec {
  private val log = LoggerFactory.getLogger(this.getClass)
  log.info(getClass.getResource("/scl_bin").getPath)
  val analyser = new Analyser(binFilePath = getClass.getResource("/scl_bin/all_morf.bin").getPath)

  "Analyser" should "should analyze corayanwi correctly." in {
    // cur1<prayogaH:karwari><lakAraH:lat><puruRaH:pra><vacanam:bahu><paxI:parasmEpaxI><XAwuH:curaz><gaNaH:curAxiH><level:1>/
    val tinantaAnalysisExpected = SclAnalysis(Map("root" -> "cur1", "prayogaH" -> "karwari", "lakAraH" -> "lat", "puruRaH" -> "pra", "vacanam" -> "bahu", "paxI" -> "parasmEpaxI",
      "XAwuH" -> "curaz", "gaNaH" -> "curAxiH")).toAnalysis.copy(sclAnalysis = None)
    val analyses = analyser.analyze(wxWord = "corayanwi")
    log.debug(jsonHelper.asString(analyses))
    assert(analyses.head.copy(sclAnalysis = None) == tinantaAnalysisExpected)
  }

}