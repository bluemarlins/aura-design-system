plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
}

group = "com.github.bluemarlins"
version = "2.0.0"

android {
    namespace = "io.github.bluemarlins.aura"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling.preview)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.robolectric)
}

// SpecCodeConsistencyTest reads the Markdown specs directly, so a docs-only edit must rerun the tests.
tasks.withType<Test>().configureEach {
    inputs.dir(rootProject.layout.projectDirectory.dir("docs/components"))
        .withPropertyName("componentSpecs")
        .withPathSensitivity(PathSensitivity.RELATIVE)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.bluemarlins"
            artifactId = "aura-design-system"
            version = "2.0.0"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Aura Design System")
                description.set("One UI-inspired, zero-Material3 Jetpack Compose design system with Glassmorphism, tactile spring physics, and 2-axis adaptive layouts.")
                url.set("https://github.com/bluemarlins/aura-design-system")
            }
        }
    }
    repositories {
        maven {
            name = "ReleaseArtifacts"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}
