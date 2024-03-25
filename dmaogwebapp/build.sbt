val ScalatraVersion = "2.8.2"

organization := "com.herminiogarcia"

name := "DMAOGWebApp"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.8"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.43.v20210629" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",  
  "com.herminiogarcia" %% "dmaog" % "0.1.5" exclude("org.scala-lang.modules", "scala-parser-combinators_2.12"),
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "4.0.1",
  "org.zeroturnaround" % "zt-zip" % "1.17"
)

enablePlugins(SbtTwirl)
enablePlugins(JettyPlugin)