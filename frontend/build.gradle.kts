plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}

node {
    download.set(true)
    version.set("14.15.4")
    yarnVersion.set("1.22.10")
}

tasks.register("clean", Delete::class) {
    doLast {
        listOf("dist", "node", ".gradle", "build").forEach {
            delete(it)
        }
    }
}

tasks.register("build", com.github.gradle.node.yarn.task.YarnTask::class) {
    dependsOn("nodeSetup")
    dependsOn("yarn")

    outputs.dir("build")
    inputs.dir("src")

    args.add("build")
}