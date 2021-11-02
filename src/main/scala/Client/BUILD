load("@io_bazel_rules_scala//scala:scala.bzl", "scala_library", "scala_binary", "scala_test")


scala_binary(
    name="client",
    main_class="Client.ClientRequest",
    srcs=[
        "ClientRequest.scala",
        ],
    deps = [
        "service",
        ],
)
scala_binary(
    name="simpleclient",
    main_class="Client.VerySimpleClient",
    srcs=[
        "VerySimpleClient.scala",
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
        "@maven//:com_pauldijou_jwt_core_2_12",
        "@maven//:com_pauldijou_jwt_spray_json_2_12",
        "@maven//:com_pauldijou_jwt_json_common_2_12",
        ],
)

