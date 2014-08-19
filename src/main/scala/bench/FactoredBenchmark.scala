package bench

import gll.grammar.{Sort, TerminalSymbol}
import gll.parser.Parser
import org.scalameter.api._

import scala.collection.mutable

object FactoredBenchmark
  extends PerformanceTest.OfflineReport {

  val jvmflagsBench = "-server -Xss64m -G:TruffleCompilationThreshold=1"
  val jvmflagsVerbose = jvmflagsBench + " " + "-G:+TruffleCompilationExceptionsAreFatal -G:+TraceTruffleInlining -Dtruffle.TraceRewrites=true -Dtruffle.DetailedRewriteReasons=true -G:+TraceTruffleCompilationDetails -G:+TraceTruffleCompilation -XX:+UnlockDiagnosticVMOptions -XX:CompileCommand=print,*::executeHelper"

  val sizes = Gen.enumeration("size")("b" * 1, "b" * 2, "b" * 4, "b" * 8, "b" * 16, "b" * 32, "b" * 64, "b" * 128)

  override def reporter = Reporter.Composite(CSVReporter(), RegressionReporter(Tester.Accepter(), Historian.Window(1)), DsvReporter(','), super.reporter)

  performance of "Factored" config (
    exec.minWarmupRuns -> 100,
    exec.maxWarmupRuns -> 10000,
    exec.benchRuns -> 100,
    // Just want to run one VM, but the Graal-enabled one with custom flags.
    exec.independentSamples -> 1,
    exec.jvmcmd -> "/home/stefan/opt/graalvm-jdk1.8.0-0.3/bin/java",
    exec.jvmflags -> jvmflagsVerbose // jvmflagsVerbose
    ) in {
    val parsers: mutable.Map[Int, Parser] = mutable.HashMap()

    def setupParser() = (_ : String) => {
      parsers getOrElseUpdate(42, {
        val S = new Sort("S")
        val parser = new Parser(S)
        val A = new Sort("A")
        val b = TerminalSymbol.singleton('b')
        S.add(b)
        S.add(S, S, A)

        A.add(S)
        A.add()

        val bs = "b" * 150

        println(s"start our warmup loop")
        for (i <- 1 to 10) {
          parser.parse(bs)
        }
        println(s"finished our warmup loop")

        parser
      })
    }

    measure method("foo") in {
      using(sizes) setUp {
        setupParser()
      } in {
        bs => {
          val parser = parsers(42)
          parser.parse(bs)
        }
      }
    }
  }
}