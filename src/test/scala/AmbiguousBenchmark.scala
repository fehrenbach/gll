import gll.grammar.{SortIdentifier, TerminalSymbol}
import gll.parser.Parser
import org.scalameter.api._

import scala.collection.mutable

object AmbiguousBenchmark
  extends PerformanceTest.OfflineReport {

  val jvmflagsBench = "-server -Xss64m -G:+TruffleCompilationExceptionsAreFatal -G:TruffleCompilationThreshold=1"
  val jvmflagsVerbose = jvmflagsBench + " " + "-G:+TraceTruffleInlining -Dtruffle.TraceRewrites=true -Dtruffle.DetailedRewriteReasons=true -G:+TraceTruffleCompilationDetails -G:+TraceTruffleCompilation -XX:+UnlockDiagnosticVMOptions -XX:CompileCommand=print,*::executeHelper"

  val sizes = Gen.enumeration("size")("b" * 1, "b" * 2, "b" * 4, "b" * 8, "b" * 16, "b" * 32, "b" * 64)

  override def reporter = Reporter.Composite(CSVReporter(), RegressionReporter(Tester.Accepter(), Historian.Window(1)), DsvReporter(','), super.reporter)

  performance of "Ambiguous" config (
    exec.minWarmupRuns -> 10,
    exec.maxWarmupRuns -> 10000,
    exec.benchRuns -> 100,
    // Just want to run one VM, but the Graal-enabled one with custom flags.
    exec.independentSamples -> 1,
    exec.jvmcmd -> "/home/stefan/opt/graalvm-jdk1.8.0-0.3/bin/java",
    exec.jvmflags -> jvmflagsVerbose // jvmflagsVerbose
    ) in {
    val parsers: mutable.Map[Int, (Parser, SortIdentifier)] = mutable.HashMap()

    def setupParser() = (_ : String) => {
      parsers getOrElseUpdate(42, {
        val S = new SortIdentifier("S")
        val parser = new Parser(S)

        S.setProductions(
          SortIdentifier.production(TerminalSymbol.singleton('b')),
          SortIdentifier.production(S, S),
          SortIdentifier.production(S, S, S))

        val bs = "b" * 150

        println(s"start our warmup loop")
        for (i <- 1 to 10) {
          parser.parse(bs)
        }
        println(s"finished our warmup loop")

        (parser, S)
      })
    }

    measure method("foo") in {
      using(sizes) setUp {
        setupParser()
      } in {
        bs => {
          val (parser, startSymbol) = parsers(42)
          parser.parse(bs)
        }
      }
    }
  }
}