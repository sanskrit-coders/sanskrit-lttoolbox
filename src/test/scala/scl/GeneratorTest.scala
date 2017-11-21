package scl

import org.scalatest.FlatSpec
import org.slf4j.LoggerFactory
import sanskrit_coders.scl.{SubantaGenerator, TinantaGenerator}

class GeneratorTest extends FlatSpec {
  private val log = LoggerFactory.getLogger(this.getClass)
  private val tinantaGenerator = new TinantaGenerator(binFilePath = "/home/vvasuki/scl/build/morph_bin/wif_gen.bin")
  private val subantaGenerator = new SubantaGenerator(binFilePath = "/home/vvasuki/scl/build/morph_bin/sup_gen.bin")

  "TinantaGenerator" should "should generate curaz forms correctly." in {
    var tinanta = tinantaGenerator.getTinanta(root = "cur1", kimpadI = "parasmEpaxI", dhAtu = "curaz",
      gaNa = "curAxiH",
      prayoga = "karwari", lakAra = "lat", puruSha = "pra", vachana = 3)
    log info s"tinanta: $tinanta"
    assert(tinanta.head == "corayanwi")
  }

  "SubantaGenerator" should "should generate jFAna forms correctly." in {
    var subanta = subantaGenerator.getSubanta("jFAna", "sAXAraNa", "napum", 2, 3)
    log info s"subanta: $subanta"
    assert(subanta.head == "jFAnAni")
  }

}
