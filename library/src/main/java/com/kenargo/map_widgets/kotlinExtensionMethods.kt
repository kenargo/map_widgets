package com.kenargo.map_widgets

import android.text.Editable


fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Int.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this.toString())