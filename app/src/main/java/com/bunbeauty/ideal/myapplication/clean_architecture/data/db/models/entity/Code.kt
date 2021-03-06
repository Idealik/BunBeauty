package com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Code(
    @PrimaryKey
    var id: String = "",
    var code: String = "",
    var count: Int = 0
) {

    var codeStatus: String = ""
    var serviceId: String = ""

    companion object {
        const val CODES = "codes"
        const val CODE = "code"
        const val COUNT = "count"

        const val PREMIUM_ACTIVATED = "0"
        const val OLD_CODE = "1"
        const val WRONG_CODE = "2"
    }
}