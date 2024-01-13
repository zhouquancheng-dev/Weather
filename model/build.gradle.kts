plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.zqc.model"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        resourceConfigurations.addAll(listOf("en", "zh-rCN"))

        /**
         * Room 配置编译器选项
         * room.schemaLocation：配置并启用将数据库架构导出到给定目录中的 JSON 文件的功能。
         * room.incremental：启用 Gradle 增量注解处理器。
         * room.expandProjection：配置 Room 以重写查询，使其顶部星形投影在展开后仅包含 DAO 方法返回类型中定义的列
         */
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    testImplementation(libs.junit.jupiter.api)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.squareup.moshi.kotlin)
    ksp(libs.squareup.moshi.kotlin.codegen)

    // Room https://developer.android.google.cn/jetpack/androidx/releases/room?hl=zh-cn#groovy
    // RoomDatabase它是公共 API 的一部分，因为 AppDatabase 扩展了它并且可能在下游依赖项中使用了该类，
    // 但是 RoomDatabase 被声明为仅实现依赖项，这意味着该类在编译期间通常不可用于下游依赖项。
    api(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}