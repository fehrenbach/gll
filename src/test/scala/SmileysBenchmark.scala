import gll.grammar.Sort
import gll.grammar.common.Characters._
import gll.parser.Parser
import org.scalameter.api._

import scala.collection.mutable

object SmileysBenchmark
  extends PerformanceTest.OfflineReport {

  val jvmflagsBench = "-server -Xss64m -G:TruffleCompilationThreshold=1"
  val jvmflagsVerbose = jvmflagsBench + " " + "-G:+TruffleCompilationExceptionsAreFatal -G:+TraceTruffleInlining -Dtruffle.TraceRewrites=true -Dtruffle.DetailedRewriteReasons=true -G:+TraceTruffleCompilationDetails -G:+TraceTruffleCompilation -XX:+UnlockDiagnosticVMOptions -XX:CompileCommand=print,*::executeHelper"

     val sizes = Gen.enumeration("size")(
       "((((((((((((((((((((((((((((((((((((((((((((((((((:))))))))))))))))))))))))))))))))))))))))))))))))))",
       "(((((((((((((((((((())))))))))))))))))))",
       "((((((((((((:))))))))))((((((((((:())))))))))))",
       "((((((((((((:))))))))))((((((((((:)))))))))))))",
       "(((((((((())))))))))",
       "((((((((((:)))))))))) ((((((((((:)))))))))) ((((((((((:)))))))))) ((((((((((:)))))))))) ((((((((((:)))))))))) ((((((((((:))))))))))",
       "((((((((((:)))))))))) ((((((((((:)))))))))) ((((((((((:)))))))))) ((((((((((:)))))))))) ((((((((((:)))))))))) ((((((((((:)))))))))))",
       "((((((((((:))))))))))",
       "((((((((((:)))))))))))",
       "((:):::(()()):)(()()():())aaa)(:(a:)a:((())a(((a(:())aa():a:)((()):)(()(:)(a())a:()a)a():(",
       "()(((a)((aa)))a)a()(a)(aa:a)()(((:())aa)):()():():a:(a)(a())a:)::a:(aa:):()((a:)())aa)a(a:)",
       "()((:a(a()()a))())((:a(:a)(()a((((a((a(()(:aa()()()))):)(():):)(:(a))():(())(():()):):(()a))",
       "():)((()():(:())))::aa((((:(((:)))::a:(:))()a)):(a):::((()a((a(aa(():))(():())((::a)a)):)()",
       "():)a((a:((aaa(()))(((()a))()))a(:)):)a((:())(a:(:):((a(:(::())a()())::()a)(a)):((aa)a(:(())",
       "()a(:)(a:a):(())):a()():((a(:):a()()::)(a:)(()a((a:)(a)a(a:a:)(a)a(a:(()()()::a()a()(()a:())))",
       "(:)",
       "(::a((a)a:()):):a)aa:)a(:::))(a())aa(a():))(:)a)((():)(:a:)a))):a(a)((:()(()())a))()a((()a))",
       "(a((f((g(((g((:))))g))))))::((((((((((((((((((((:)))))))))))))))))))) ((((((((((((((((((((((((((((((((((((((((((((((((((:))))))))))))))))))))))))))))))))))))))))))))))))))",
       "(a((f((g(((g((:))))g))))))::((((((((((((((((((((:)))))))))))))))))))) ((((((((((((((((((((((((((((((((((((((((((((((((((:)))))))))))))))))))))))))))))))))))))))))))))))))))",
       ":(a):(:)aa)a(:()::():))a:aaa:)(:)((()()))a()(((()(:)))(:(aa:()())())a((a)a:(:()))(a((():)))",
       ":)()((a)):(():a:a:)(:a)):)(()(:)::::(a(::a())(a):(:((((:(aa(()))a)(((((((((()a()a):)))((:)))))))))",
       "::((:))(((:)(aaa)(a())()(a:)(:)(:)()):)a())aa)())(():a):()::):)a()())a()):):(:a)a):()(a)(a)",
       ":a:)(:))()(()()a)aaa::a()()a:()()a::)((()(a(a))))try implementing sleep sort if you are stuck:(:)a)",
       "a(a)::(((::)))())((a)(:((:a())):((::(:()(a)))i am trapped in a test case generator :(:(a(:::))",
       "hacker cup: started :):)",
       "hello world",
       "i am sick today (:()"
     )

     override def reporter = Reporter.Composite(CSVReporter(), RegressionReporter(Tester.Accepter(), Historian.Window(1)), DsvReporter(','), super.reporter)

     performance of "Smileys" config (
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

           val P = new Sort("P")

           S.add()
           S.add(LETTER)
           S.add(SPACE)
           S.add(COLON)
           S.add(COLON, P)
           S.add(LPAREN, S, RPAREN)
           S.add(S, S)

           P.add(LPAREN)
           P.add(RPAREN)

           println(s"start our warmup loop")
           for (i <- 1 to 1000) {
             parser.parse("a(a)::(((::)))())((a)(:((:a())):((::(:()(a)))i am trapped in a test case generator :(:(a(:::))")
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