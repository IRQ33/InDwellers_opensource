package com.irq3.website.discord

import com.irq3.INDWELLERS
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import org.bouncycastle.util.encoders.Hex

class DiscordUtils {
    companion object {
        fun verifyDiscordSignature(publicKeyHex: String, signatureHex: String, timestamp: String, body: String): Boolean {
            return try {
                val publicKeyBytes = Hex.decode(publicKeyHex)
                val signatureBytes = Hex.decode(signatureHex)
                val data = (timestamp + body).toByteArray(Charsets.UTF_8)

                val publicKeyParams = Ed25519PublicKeyParameters(publicKeyBytes, 0)
                val signer = Ed25519Signer()

                signer.init(false, publicKeyParams)
                signer.update(data, 0, data.size)

                signer.verifySignature(signatureBytes)
            } catch (e: Exception) {
                false
            }
        }

        fun onlineUser(idDiscord: Long, jda: JDA): OnlineStatus? {
            return jda.getGuildById(INDWELLERS.DISCORD_GUILD_ID)?.getMemberById(idDiscord)?.onlineStatus
        }
    }
}