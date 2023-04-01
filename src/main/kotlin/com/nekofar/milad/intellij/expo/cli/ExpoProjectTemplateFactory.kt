package com.nekofar.milad.intellij.expo.cli

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.lang.javascript.boilerplate.JavaScriptNewTemplatesFactoryBase
import com.intellij.platform.ProjectTemplate

class ExpoProjectTemplateFactory : JavaScriptNewTemplatesFactoryBase() {
    override fun createTemplates(context: WizardContext?): Array<ProjectTemplate> = arrayOf(ExpoCliProjectGenerator())
}
