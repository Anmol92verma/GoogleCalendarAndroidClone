plugins {
  id(BuildPlugins.ANDROID_LIBRARY_PLUGIN)
  id(BuildPlugins.KOTLIN_ANDROID_PLUGIN)
  id(BuildPlugins.KOTLIN_PARCELABLE_PLUGIN)
  id(BuildPlugins.KOTLIN_KAPT)
  id(BuildPlugins.DAGGER_HILT)
}

android {
  compileSdk = (ProjectProperties.COMPILE_SDK)

  defaultConfig {
    minSdk = (ProjectProperties.MIN_SDK)
    targetSdk = (ProjectProperties.TARGET_SDK)

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

// Required for annotation processing plugins like Dagger
kapt {
  generateStubs = true
  correctErrorTypes = true
}

dependencies {
  implementation(project(":common"))

  /*Kotlin*/
  api(Lib.Kotlin.KT_STD)
  api(Lib.Async.COROUTINES)
  api(Lib.Logger.TIMBER)

  /* Paging */
  implementation(Lib.Paging.PAGING_3)

  /* Dependency Injection */
  api(Lib.Di.hiltAndroid)
  kapt(Lib.Di.hiltAndroidCompiler)
}