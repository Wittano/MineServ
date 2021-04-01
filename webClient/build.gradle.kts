plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}

node {
    download.set(true)
    version.set("14.15.4")
    yarnVersion.set("1.22.10")
}

tasks.register<Delete>("clean") {
    listOf("dist", "node", ".gradle", "build", "node_modules").forEach {
        delete(it)
    }
}

tasks.register<com.github.gradle.node.yarn.task.YarnTask>("build") {
    dependsOn("nodeSetup", "yarn")

    outputs.dir("build")
    inputs.dir("src")

    args.add("build")
}

tasks.register<Copy>("copy") {
    description = "Copy bundles to general build directory"

    dependsOn("build")

    from("build")
    into("${project.rootDir.absolutePath}/build/${project.name}")
}