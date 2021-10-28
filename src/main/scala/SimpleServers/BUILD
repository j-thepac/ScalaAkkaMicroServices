load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library", "scala_binary", "scala_test")


scala_binary(
    name="directive",
    main_class="SimpleServers.ServerDirectives",
    srcs=[
        "ServerDirectives.scala",
        ],
    deps = [
        "service",
        ],
)

scala_binary(
    name="lowlevel",
    main_class="SimpleServers.ServerLowLevelAsync",
    srcs=[
        "ServerLowLevelAsync.scala",
        ],
    deps = [
        "service",
        ],
)

scala_binary(
    name="serverstream",
    main_class="SimpleServers.ServerStream",
    srcs=[
        "ServerStream.scala",
        ],
    deps = [
        "service",
        ],
)




java_library(
    name = "service",
    exports = [
        "@maven//:com_typesafe_akka_akka_stream_2_12",
        "@maven//:com_typesafe_akka_akka_http_core_2_12",
        "@maven//:org_json_json_20210307",
        "@maven//:com_typesafe_akka_akka_actor_2_12",
        "@maven//:com_typesafe_akka_akka_http_2_12",
        ],
)

