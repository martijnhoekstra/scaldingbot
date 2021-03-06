name := "scalding bot"

version := "0.1"

scalaVersion := "2.10.2"

scalacOptions := Seq("-encoding", "utf8",
                     "-target:jvm-1.6",
                     "-feature",
                     "-language:implicitConversions",
                     "-language:postfixOps",
                     "-unchecked",
                     "-deprecation",
                     "-Xlog-reflective-calls",
                     "-Ywarn-adapted-args"
                    )

resolvers ++= Seq (
  "spray nightlies" at "http://nightlies.spray.io",
  "spray repo" at "http://repo.spray.io",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % "2.2.1",
                            "com.github.nscala-time" %% "nscala-time" % "0.6.0",
                            "com.typesafe.akka" %% "akka-testkit" % "2.2.1",
							"io.spray" % "spray-client" % "1.2-20130822",
							"org.scalatest" %% "scalatest" % "2.0.RC1-SNAP4" % "test",
							"org.scalaz" %% "scalaz-core" % "7.1.0-M1",
							"org.typelevel" % "scalaz-contrib-210_2.10" % "0.1.4",
							"com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2",
							"com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2",
							"io.spray" %%  "spray-json" % "1.2.5"
							)
