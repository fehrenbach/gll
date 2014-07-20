scalaVersion := "2.11.1"

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE18)

libraryDependencies ++= Seq(
  "com.google.caliper" % "caliper" % "0.5-rc1",
  "com.novocode" % "junit-interface" % "0.10-M2" % "test", "junit" % "junit" % "4.8.2",
  "com.storm-enroute" %% "scalameter" % "0.6")

resolvers ++= Seq("Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases")

javacOptions := Seq("-encoding", "UTF-8")

mainClass in Test := Some("com.google.caliper.Runner")


// enable forking in run
fork in (Test, run) := true

// we need to add the runtime classpath as a "-cp" argument to the `javaOptions in run`, otherwise caliper
// will not see the right classpath and die with a ConfigurationException
javaOptions in (Test, run) <++= (fullClasspath in Test) map { cp => Seq("-cp", sbt.Build.data(cp).mkString(";")) }