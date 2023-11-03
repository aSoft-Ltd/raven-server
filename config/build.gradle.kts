plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

kotlin {
    jvm { library() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.ravenFlixSender)
                api(libs.raven.bus)
                api(projects.ravenSmtp)
                api(libs.raven.console)
                api(kotlinx.serialization.toml)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kommander.coroutines)
            }
        }
    }
}
