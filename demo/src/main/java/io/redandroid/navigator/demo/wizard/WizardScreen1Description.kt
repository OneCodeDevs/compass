package io.redandroid.navigator.demo.wizard

import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.Home
import io.redandroid.navigator.api.Navigation
import io.redandroid.navigator.api.Parameter

@Destination(name = "WizardScreen1")
@Home
@Navigation(to = WizardScreen2Description::class)
@Parameter(name = "input", String::class)
object WizardScreen1Description