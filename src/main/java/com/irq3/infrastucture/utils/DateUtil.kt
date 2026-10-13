package com.irq3.infrastucture.utils

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class DateUtil {
    companion object{
        fun fixInstant(instant: Instant): OffsetDateTime {
            return instant.atZone(ZoneId.systemDefault()).toOffsetDateTime()
        }
    }
}