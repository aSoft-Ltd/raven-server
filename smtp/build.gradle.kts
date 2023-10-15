plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

kotlin {
    target { library() }

    sourceSets {
        val main by getting {
            dependencies {
                api(libs.raven.api)
                implementation(javax.mail)
            }
        }

        val test by getting {
            dependencies {
                implementation(libs.kommander.coroutines)
            }
        }
    }
}
