package com.soethan.todocompose.util

import android.content.Context


class ResourceComparer {
    fun isEqual(context: Context,stringResource:Int,label:String):Boolean{
        return context.getString(stringResource) == label
    }
}