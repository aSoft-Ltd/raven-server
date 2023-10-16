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
                api(libs.koncurrent.later.coroutines)
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
