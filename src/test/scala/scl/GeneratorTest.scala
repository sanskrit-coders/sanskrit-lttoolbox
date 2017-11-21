package scl
import java.util.concurrent.TimeUnit

import akka.pattern.{ask, pipe}
import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import dbSchema.grammar.{SclAnalysis, TinVivaxaa, Tinanta}
import org.scalatest.FlatSpec
import org.slf4j.LoggerFactory
import sanskrit_coders.scl.{Analyser, GeneratorActor, SubantaGenerator, TinantaGenerator}
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.ExecutionContextExecutor

class GeneratorTest extends FlatSpec {
  private val log = LoggerFactory.getLogger(this.getClass)
  private val tinantaGenerator = new TinantaGenerator(binFilePath = "/home/vvasuki/scl/build/morph_bin/wif_gen.bin")
  private val subantaGenerator = new SubantaGenerator(binFilePath = "/home/vvasuki/scl/build/morph_bin/sup_gen.bin")

  "TinantaGenerator" should "should generate curaz forms correctly based on Tinanta." in {
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

class GeneratorActorTest() extends TestKit(ActorSystem("MySpec", ConfigFactory.parseString("{akka.test.single-expect-default = 300000}"))) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  private val tinantaGenerator = new TinantaGenerator(binFilePath = "/home/vvasuki/scl/build/morph_bin/wif_gen.bin")
  private val subantaGenerator = new SubantaGenerator(binFilePath = "/home/vvasuki/scl/build/morph_bin/sup_gen.bin")
  private val analyser = new Analyser(binFilePath = "/home/vvasuki/scl/build/morph_bin/all_morf.bin")

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }


  "GeneratorActor->TinantaGenerator" must {
    "generate curaz forms correctly based on (TinVivaxaa, alternateForm)." in {
      val vivaxaa = TinVivaxaa(prayoga = Some("karwari"), lakaara = Some("lot"), puruSha = Some("pra"), vachana = Some(3), kimpadI = Some("parasmEpaxI"))
      val generatorActor = system.actorOf(Props(classOf[GeneratorActor], subantaGenerator, tinantaGenerator, analyser))
      generatorActor !  (vivaxaa, "corayanwi")
      expectMsg(Seq("corayanwu"))
    }
  }
}