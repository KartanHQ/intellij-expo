package com.nekofar.milad.intellij.expo.cli

import com.intellij.execution.filters.Filter
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.javascript.CreateRunConfigurationUtil
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.ui.components.JBCheckBox
import com.nekofar.milad.intellij.expo.ExpoBundle.message
import com.nekofar.milad.intellij.expo.ExpoIcons
import javax.swing.JPanel

class ExpoCliProjectGenerator : NpmPackageProjectGenerator() {
    private val packageName = "create-expo-app"
    private val npxCommand = "create-expo-app"

    private val typeScriptKey = Key.create<Boolean>("expo.project.generator.typescript.project")
    private val typeScriptInitial = false

    override fun getName() = message("expo.project.generator.name")

    override fun getDescription() = message("expo.project.generator.description")

    override fun filters(project: Project, baseDir: VirtualFile) = emptyArray<Filter>()

    override fun customizeModule(baseDir: VirtualFile, entry: ContentEntry?) {}

    override fun createPeer(): ProjectGeneratorPeer<Settings> {
        val typeScriptCheckbox = JBCheckBox(
            message("expo.project.generator.typescript.checkbox"),
            typeScriptInitial
        )

        return object : NpmPackageGeneratorPeer() {
            override fun buildUI(settingsStep: SettingsStep) {
                super.buildUI(settingsStep)
                settingsStep.addSettingsComponent(typeScriptCheckbox)
            }

            override fun getSettings(): Settings {
                val settings = super.getSettings()
                settings.putUserData(
                    typeScriptKey,
                    typeScriptCheckbox.isSelected
                )
                return settings
            }

            override fun createPanel(): JPanel {
                val panel = super.createPanel()
                panel.add(typeScriptCheckbox)
                return panel
            }
        }
    }

    override fun packageName() = packageName

    override fun presentablePackageName() = message("expo.project.generator.presentable.package.name")

    override fun getNpxCommands() = listOf(NpxPackageDescriptor.NpxCommand(packageName, npxCommand))

    override fun generateInTemp() = true

    override fun generatorArgs(project: Project, dir: VirtualFile, settings: Settings): Array<String> {
        val typeScript = settings.getUserData(typeScriptKey) ?: typeScriptInitial
        return if (typeScript) {
            arrayOf(
                "-t",
                "expo-template-blank-typescript",
                project.name
            )
        } else {
            arrayOf(project.name)
        }
    }

    override fun getIcon() = ExpoIcons.ProjectGenerator

    override fun onGettingSmartAfterProjectGeneration(project: Project, baseDir: VirtualFile) {
        super.onGettingSmartAfterProjectGeneration(project, baseDir)
        CreateRunConfigurationUtil.npmConfiguration(project, "start")
        CreateRunConfigurationUtil.npmConfiguration(project, "android")
        CreateRunConfigurationUtil.npmConfiguration(project, "ios")
        CreateRunConfigurationUtil.npmConfiguration(project, "web")
    }
}
