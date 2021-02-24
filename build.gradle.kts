tasks.register("cleanProject", Delete::class) {
    description = "Clean project and every submodules"
    group = "clean"

    dependsOn(":frontend:clean")
    dependsOn(":backend:clean")

    delete("build")
}

tasks.register("buildProject", Copy::class) {
    description = "Build project to single jar archive"
    group = "build"

    dependsOn(":backend:build")

    val backendDir = project(":backend").projectDir.path

    inputs.dir("${backendDir}/build/libs")
    outputs.dir("build")

    from("${backendDir}/build/libs")
    into("build")
}