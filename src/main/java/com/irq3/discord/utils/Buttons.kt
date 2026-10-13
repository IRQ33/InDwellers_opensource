package com.irq3.discord.utils

import net.dv8tion.jda.api.components.buttons.Button

class Buttons {

    companion object{
        fun nextConversation(): Button{
            return Button.success("next_conversation","Znajdź dwellersa")
        }

        fun leaveQueue(): Button{
            return Button.danger("leave","Wyjdź z kolejki")
        }

        fun acceptRules(): Button{
            return Button.success("accept_rules","Akceptuję zasady")
        }
    }
}