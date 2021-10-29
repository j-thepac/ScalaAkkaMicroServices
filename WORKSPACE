# Scala rules
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# bazel-skylib 0.8.0 released 2019.03.20 (https://github.com/bazelbuild/bazel-skylib/releases/tag/0.8.0)
skylib_version = "0.8.0"
http_archive(
    name = "bazel_skylib",
    type = "tar.gz",
    url = "https://github.com/bazelbuild/bazel-skylib/releases/download/{}/bazel-skylib.{}.tar.gz".format (skylib_version, skylib_version),
    sha256 = "2ef429f5d7ce7111263289644d233707dba35e39696377ebab8b0bc701f7818e",
)

rules_scala_version="72c27ef603fc8b771fa80e3398c3fe7701af1515" # update this as needed

http_archive(
    name = "io_bazel_rules_scala",
    strip_prefix = "rules_scala-%s" % rules_scala_version,
    type = "zip",
    url = "https://github.com/bazelbuild/rules_scala/archive/%s.zip" % rules_scala_version,
    sha256 = "3a9cb1429193c6b005a5f0f8e5f9f0e29f7758339732e690407efd685e96a5eb"
)

# Stores Scala version and other configuration
# 2.12 is a default version, other versions can be use by passing them explicitly:
# scala_config(scala_version = "2.11.12")
load("@io_bazel_rules_scala//:scala_config.bzl", "scala_config")
scala_config()

load("@io_bazel_rules_scala//scala:toolchains.bzl", "scala_register_toolchains")
scala_register_toolchains()

load("@io_bazel_rules_scala//scala:scala.bzl", "scala_repositories")
scala_repositories()


RULES_JVM_EXTERNAL_TAG = "4.1"
RULES_JVM_EXTERNAL_SHA = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.typesafe.akka:akka-stream_2.12:2.6.17",
        "com.typesafe.akka:akka-http_2.12:10.2.6",
        "com.typesafe.akka:akka-http-core_2.12:10.2.6",
        "com.typesafe.akka:akka-actor_2.12:2.6.17",
        "org.json:json:20210307",
        "com.pauldijou:jwt-core_2.12:5.0.0",
        "com.pauldijou:jwt-spray-json_2.12:5.0.0",
        "com.pauldijou:jwt-json-common_2.12:5.0.0"
    ],
    repositories = [
        "https://jcenter.bintray.com",
        "https://maven.google.com",
        "https://repo.maven.apache.org/maven2/",
    ]
)