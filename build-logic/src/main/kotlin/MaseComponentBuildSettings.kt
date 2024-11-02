import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Shared build settings for all MASE components.
 */
class MaseComponentBuildSettings : Plugin<Project> {
    override fun apply(project: Project) {
        project.group = "lv.modo.adom.mase"
        // Kotlin compilation
        project.pluginManager.apply(KotlinPluginWrapper::class.java)
        project.pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        // Consistent JVM targeting
        project.tasks.withType(KotlinCompile::class.java) {
            it.compilerOptions.apply {
                jvmTarget.set(JvmTarget.valueOf("JVM_${project.jvmTarget}"))
                freeCompilerArgs.add("-opt-in=kotlin.ExperimentalUnsignedTypes,kotlin.ExperimentalStdlibApi")
            }
        }
        project.tasks.withType(JavaCompile::class.java) { it.targetCompatibility = project.jvmTarget }
        // Cruical dependencies
        project.dependencies.add("implementation", project.versionCatalog.findBundle("core").get())
        // Testing
        project.dependencies.addProvider("testImplementation", project.versionCatalog.findBundle("test").get())
        project.dependencies.add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
        project.tasks.withType(Test::class.java) {
            it.useJUnitPlatform()
        }
    }

    /**
     * Use the JVM target specified in `libs.versions.toml`.
     */
    private val Project.jvmTarget
        get() = this
            .versionCatalog
            .findVersion("jvm")
            .get()
            .displayName

    private val Project.versionCatalog
        get() = this.rootProject.extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")
}