package io.redandroid.navigator.demo.home

import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.Navigation
import io.redandroid.navigator.demo.details.Details

@Destination(isHome = true)
@Navigation(to = Details::class)
object Home
