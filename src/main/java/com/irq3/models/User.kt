package com.irq3.models

import com.irq3.infrastucture.utils.DateUtil
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import java.time.OffsetDateTime
import kotlin.time.Duration.Companion.days


data class User(val id: Long? = null,
                val idDiscord : Long,
                var isTalking: Boolean,
                val currentConversation: Long?,
                val vipDate: OffsetDateTime? = null
){
    val vipExpiryDate: OffsetDateTime?
        get() = vipDate?.plusDays(30)

    val isVip: Boolean
        get() {
            val expiry = vipExpiryDate ?: return false

            val nowEpoch = Clock.System.now().toJavaInstant().epochSecond
            val expiryEpoch = expiry.toInstant().epochSecond

            return expiryEpoch > nowEpoch
        }
}