plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}

node {
    download.set(true)
    version.set("14.15.4")
    npmVersion.set("7.5.4")
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

tasks.register("copy", Copy::class) {
    dependsOn("build")

    val staticDir = project(":backend").projectDir.path + "/src/main/resources/static"

    outputs.dir(staticDir)
    inputs.dir("build")

    from("build")
    exclude("build/index.html")
    into(staticDir)

    finalizedBy("copyHtml")
}

tasks.register("copyHtml", Copy::class) {
    group = "build"
    description = "Copy index.html to templates directory"

    from("build/index.html")
    into(project(":backend").projectDir.path + "/src/main/resources/templates")
}
