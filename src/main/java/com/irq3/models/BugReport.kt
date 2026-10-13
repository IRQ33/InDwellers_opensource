package com.irq3.models

import java.time.OffsetDateTime

class BugReport(val id:Long?,
                val bugDescription:String,
                val dateCreated: OffsetDateTime,
                val userId: Long) {
}