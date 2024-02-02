plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation( project( ":composeApp" ) )
            implementation( libs.jsoup )
            implementation( libs.okhttp )
        }
    }
    
}