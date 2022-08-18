package de.gnarly.navgraph.config.home

import de.gnarly.navgraph.config.details.Details
import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.Navigation

@Destination(isHome = true)
@Navigation(to = Details::class)
object Home
