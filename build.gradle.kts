val backendName = "api"
val frontendName = "webClient"

tasks.register<Delete>("clean") {
    description = "Clean project and every submodules"
    group = "project"

    dependsOn(":$frontendName:clean", ":$backendName:clean")

    delete("build")
}

tasks.register("build") {
    description = "Build project to single jar archive"
    group = "project"

    dependsOn(":$backendName:copyJar", "$frontendName:copy")
}