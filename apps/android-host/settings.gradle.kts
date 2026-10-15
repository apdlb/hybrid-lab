pluginManagement {
    includeBuild("../../node_modules/@react-native/gradle-plugin")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.facebook.react.settings")
}
val localProperties = java.util.Properties().apply {
    val localFile = file("local.properties")
    if (localFile.exists()) localFile.inputStream().use { load(it) }
}
val nodeBinary: String = localProperties.getProperty("node.binary") ?: "node"
extensions.configure<com.facebook.react.ReactSettingsExtension> {
    autolinkLibrariesFromCommand(
        command = listOf(
            nodeBinary,
            "-e",
            "process.argv = ['', '', 'config']; require('@react-native-community/cli').run()",
        ),
        workingDirectory = file("../../packages/rn-app"),
        lockFiles = files(
            "../../yarn.lock",
            "../../packages/rn-app/package.json",
            "../../packages/rn-app/react-native.config.js",
        ),
    )
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HostApp"
include(":app")
includeBuild("../../node_modules/@react-native/gradle-plugin")
