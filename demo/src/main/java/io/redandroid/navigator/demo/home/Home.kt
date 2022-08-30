package io.redandroid.navigator.demo.home

import io.redandroid.navigator.api.Destination
import io.redandroid.navigator.api.Navigation
import io.redandroid.navigator.demo.details.Details

@Destination
@io.redandroid.navigator.api.Home
@Navigation(to = Details::class)
object Home
