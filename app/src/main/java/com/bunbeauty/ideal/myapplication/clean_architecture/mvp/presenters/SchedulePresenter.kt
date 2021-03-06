package com.bunbeauty.ideal.myapplication.clean_architecture.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bunbeauty.ideal.myapplication.clean_architecture.domain.schedule.ScheduleInteractor
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.schedule.SchedulePresenterCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.views.ScheduleView

@InjectViewState
class SchedulePresenter(private val scheduleInteractor: ScheduleInteractor) :
    MvpPresenter<ScheduleView>(), SchedulePresenterCallback {

    fun getSchedule() {
        scheduleInteractor.getSchedule(this)
    }

    override fun showSchedule(dayIndexes: Set<Int>) {
        viewState.showSchedule(dayIndexes)
    }

    fun getStringDate(dayIndex: Int): String {
        return scheduleInteractor.getStringDate(dayIndex)
    }

    fun isPastDay(dayIndex: Int): Boolean {
        return scheduleInteractor.isPastDay(dayIndex)
    }

    fun rememberDay(dayIndex: Int, day: String) {
        scheduleInteractor.selectedDayIndexes.add(dayIndex)
        scheduleInteractor.selectedDays.add(scheduleInteractor.getDayFromString(day))
        scheduleInteractor.getTime(this)
    }

    fun hasSomeSelectedDays(): Boolean {
        return scheduleInteractor.selectedDays.isNotEmpty()
    }

    override fun showAccurateTime(accurateTimeSet: Set<String>) {
        viewState.showAccurateTime(accurateTimeSet)
    }

    override fun showTimeWithOrder(timeWithOrderSet: Set<String>) {
        viewState.showTimeWithOrder(timeWithOrderSet)
    }

    override fun showInaccurateTime(inaccurateTimeSet: Set<String>) {
        viewState.showInaccurateTime(inaccurateTimeSet)
    }

    fun getSelectedDays(): List<Int> {
        return scheduleInteractor.selectedDayIndexes
    }

    fun forgotAllDays() {
        scheduleInteractor.selectedDayIndexes.clear()
        scheduleInteractor.selectedDays.clear()
    }

    fun addToSchedule(days: List<String>, time: String) {
        scheduleInteractor.addToSchedule(days, time, this)
    }

    override fun fillDay(dayIndex: Int) {
        viewState.fillDay(dayIndex)
    }

    fun deleteFromSchedule(days: List<String>, time: String) {
        scheduleInteractor.deleteFromSchedule(days, time, this)
    }

    override fun clearTime(timeString: String) {
        viewState.clearTime(timeString)
    }

    override fun clearDay(dayIndex: Int) {
        viewState.clearDay(dayIndex)
    }

    fun saveSchedule() {
        scheduleInteractor.saveSchedule(this)
    }

    override fun showScheduleSaved() {
        viewState.showMessage("Расписание сохранено")
        viewState.goBack()
    }
}