package com.nekofar.milad.intellij.expo.cli

import com.intellij.execution.filters.Filter
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.javascript.CreateRunConfigurationUtil
import com.intellij.lang.javascript.boilerplate.NpmPackageProjectGenerator
import com.intellij.lang.javascript.boilerplate.NpxPackageDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.ui.components.JBCheckBox
import com.nekofar.milad.intellij.expo.ExpoBundle
import com.nekofar.milad.intellij.expo.ExpoIcons
import javax.swing.Icon
import javax.swing.JPanel

class ExpoCliProjectGenerator : NpmPackageProjectGenerator() {
    private val packageName = "create-expo-app"
    private val npxCommand = "create-expo-app"

    private val typeScriptKey = Key.create<Boolean>("expo.project.generator.typescript.project")
    private val typeScriptInitial = false

    override fun getName(): String = ExpoBundle.message("expo.project.generator.name")

    override fun getDescription(): String = ExpoBundle.message("expo.project.generator.description")

    override fun filters(project: Project, baseDir: VirtualFile): Array<Filter> = emptyArray()

    override fun customizeModule(virtualFile: VirtualFile, contentEntry: ContentEntry?) {}

    override fun createPeer(): ProjectGeneratorPeer<Settings> {
        val typeScriptCheckbox = JBCheckBox(
            ExpoBundle.message("expo.project.generator.typescript.checkbox"),
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

    override fun packageName(): String = packageName

    override fun presentablePackageName(): String =
        ExpoBundle.message("expo.project.generator.presentable.package.name")

    override fun getNpxCommands() = listOf(NpxPackageDescriptor.NpxCommand(packageName, npxCommand))

    override fun generateInTemp(): Boolean = true

    override fun generatorArgs(project: Project?, dir: VirtualFile?, settings: Settings?): Array<@NlsSafe String>? {
        val typeScript = settings?.getUserData(typeScriptKey) ?: typeScriptInitial
        return project?.let {
            if (typeScript) arrayOf(
                "-t",
                "expo-template-blank-typescript",
                it.name
            ) else arrayOf(it.name)
        }
    }

    override fun getIcon(): Icon = ExpoIcons.ProjectGenerator

    override fun onGettingSmartAfterProjectGeneration(project: Project, baseDir: VirtualFile) {
        super.onGettingSmartAfterProjectGeneration(project, baseDir)
        CreateRunConfigurationUtil.npmConfiguration(project, "start")
        CreateRunConfigurationUtil.npmConfiguration(project, "android")
        CreateRunConfigurationUtil.npmConfiguration(project, "ios")
        CreateRunConfigurationUtil.npmConfiguration(project, "web")
    }
}
