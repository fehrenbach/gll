EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

// The recommended version of Caliper (1.0-beta-2) is of yet unreleased.
// Clone and install locally:
// $ git clone https://code.google.com/p/caliper/
// $ cd caliper/caliper
// $ mvn clean install

resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

libraryDependencies += "com.google.caliper" % "caliper" % "1.0-beta-SNAPSHOT"

libraryDependencies ++= Seq("com.novocode" % "junit-interface" % "0.10-M2" % "test", "junit" % "junit" % "4.8.2")

javacOptions := Seq("-encoding", "UTF-8")

mainClass in Test := Some("com.google.caliper.Runner")


// enable forking in run
fork in (Test, run) := true

// we need to add the runtime classpath as a "-cp" argument to the `javaOptions in run`, otherwise caliper
// will not see the right classpath and die with a ConfigurationException
javaOptions in (Test, run) <++= (fullClasspath in Test) map { cp => Seq("-cp", sbt.Build.data(cp).mkString(";")) }