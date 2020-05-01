package com.bunbeauty.ideal.myapplication.cleanArchitecture.business.schedule

import org.joda.time.DateTime

class ScheduleInteractor {

    fun getDateString(dayIndex: Int): String {
        val dayOfWeek = DateTime.now().dayOfWeek - 1
        val lastMonday = DateTime.now().minusDays(dayOfWeek)
        val date = lastMonday.plusDays(dayIndex).dayOfMonth

        return date.toString()
    }

    fun getTineString(timeIndex: Int): String {
        val hours = timeIndex / 2
        val minutes = (timeIndex % 2) * 30
        val minutesString = if (minutes == 0) {
            "00"
        } else {
            minutes.toString()
        }

        return "$hours:$minutesString"
    }

    fun isPastDay(dayIndex: Int): Boolean {
        val dayOfWeek = DateTime.now().dayOfWeek - 2

        return dayIndex >= dayOfWeek
    }
}