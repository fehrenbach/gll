#!/bin/bash

sbt 'run-main bench.ChainBenchmark -CresultDir tmp' > /dev/null
sbt 'run-main bench.AmbiguousBenchmark -CresultDir tmp' > /dev/null
sbt 'run-main bench.FactoredBenchmark -CresultDir tmp' > /dev/null
sbt 'run-main bench.SmileysBenchmark -CresultDir tmp' > /dev/null
