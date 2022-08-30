package io.redandroid.navigator.demo.home

import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.Home
import io.redandroid.navigator.api.Navigation
import io.redandroid.navigator.demo.details.DetailsDescription

@Destination(name = "Home")
@Home
@Navigation(to = DetailsDescription::class)
object HomeDescription
