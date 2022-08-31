package io.redandroid.navigator.demo.wizard

import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.Home
import io.redandroid.navigator.api.Navigation

@Destination(name = "WizardScreen1")
@Home
@Navigation(to = WizardScreen2Description::class)
object WizardScreen1Description