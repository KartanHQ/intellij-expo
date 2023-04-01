package com.nekofar.milad.intellij.expo.cli

import com.intellij.ide.util.projectWizard.WebTemplateNewProjectWizard
import com.intellij.ide.wizard.GeneratorNewProjectWizardBuilderAdapter

class ExpoCliProjectModuleBuilder : GeneratorNewProjectWizardBuilderAdapter(WebTemplateNewProjectWizard(
    ExpoCliProjectGenerator()
))
