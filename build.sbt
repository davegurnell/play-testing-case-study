name         := "play-testing-case-study"
organization := "com.example"
version      := "1.0-SNAPSHOT"
scalaVersion := "2.12.8"

enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play"      %% "play-slick"            % "4.0.0",
  "com.typesafe.play"      %% "play-slick-evolutions" % "4.0.0",
  "com.h2database"          % "h2"                    % "1.4.198",
  "org.scalatestplus.play" %% "scalatestplus-play"    % "4.0.1" % Test,
)
