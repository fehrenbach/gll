package bench

import gll.grammar.{SortIdentifier, TerminalSymbol}
import gll.parser.Parser
import org.scalameter.Parameters
import org.scalameter.api._

import scala.collection.mutable

object ChainBenchmark
  extends PerformanceTest.OfflineReport {

  val jvmflagsBench = "-server -Xss64m -G:TruffleCompilationThreshold=1"
  val jvmflagsVerbose = jvmflagsBench + " " + "-G:+TruffleCompilationExceptionsAreFatal -G:+TraceTruffleInlining -Dtruffle.TraceRewrites=true -Dtruffle.DetailedRewriteReasons=true -G:+TraceTruffleCompilationDetails -G:+TraceTruffleCompilation -XX:+UnlockDiagnosticVMOptions -XX:CompileCommand=print,*::executeHelper"

  val sizes = biggerWarmupset(Gen.enumeration("chainLength")(1, 50, 100, 150, 200))

  def biggerWarmupset[T](t: Gen[T]): Gen[T] = new Gen[T] {
    def warmupset = dataset map generate
    def dataset = t.dataset
    def generate(params: Parameters) = t.generate(params)
  }

  def setupChain(length: Int, start: SortIdentifier, end: SortIdentifier): Unit = {
    if (length == 0) {
      start.setProductions(SortIdentifier.production(end))
    } else {
      val next = new SortIdentifier(s"C $length")
      start.setProductions(SortIdentifier.production(next))
      setupChain(length - 1, next, end)
    }
  }

  override def reporter = Reporter.Composite(CSVReporter(), RegressionReporter(Tester.Accepter(), Historian.Window(1)), DsvReporter(','), super.reporter)

  val as = "a" * 150

  performance of "LongChains" config (
    exec.minWarmupRuns -> 100,
    exec.maxWarmupRuns -> 10000,
    exec.benchRuns -> 1000,
    // Just want to run one VM, but the Graal-enabled one with custom flags.
    exec.independentSamples -> 1,
    exec.jvmcmd -> "/home/stefan/opt/graalvm-jdk1.8.0-0.3/bin/java",
    exec.jvmflags -> jvmflagsBench // jvmflagsVerbose
    ) in {
    val parsers: mutable.Map[Int, (Parser, SortIdentifier)] = mutable.HashMap()

    def setupParser() = (chainLength: Int) => {
      parsers getOrElseUpdate(chainLength, {
        val startSymbol = new SortIdentifier("S")
        val parser = new Parser(startSymbol)
        val end = new SortIdentifier("E")
        end.setProductions(SortIdentifier.production(TerminalSymbol.singleton('a'), startSymbol))

        setupChain(chainLength, startSymbol, end)

        println(s"start our warmup loop for $chainLength")
        for (i <- 1 to 10000) {
          parser.parse(as)
        }
        println(s"finished our warmup loop for $chainLength")

        (parser, startSymbol)
      })
    }

    measure method("truffle") in {
      using(sizes) setUp {
        setupParser()
      } in {
        chainLength => {
          val (parser, startSymbol) = parsers(chainLength)
          parser.parse(as)
        }
      }
    }
  }
}
