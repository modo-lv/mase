import models.PlayerCharacter
import models.PlayerCharacter.Addresses
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.junit.jupiter.api.Test
import utils.leNum

class ChecksumTests {
    @Test
    fun `Checksums are computed correctly`() {
        val content = this::class.java.getResource("Test.svg")!!.readBytes()
        SaveData(content).computeChecksums().checksumSegments.apply {
            size `should be equal to` 5
            map { it.computedChecksum } `should contain same` map { it.storedChecksum }
            map { it.computedChecksum } `should contain same` listOf(
                0x18E65772u,
                0x2D2CA30Du,
                0x49B5E299u,
                0x4C1A20B8u,
                0xB64A1D06u,
            )
        }
    }

    @Test
    fun `Checksums are fixed correctly`() {
        val content = this::class.java.getResource("Test.svg")!!.readBytes().also {
            it[Addresses.XP] `should be equal to` 0
            it[Addresses.XP] = 2
        }
        SaveData(content).apply {
            fixChecksums().checksumSegments.apply {
                map { it.computedChecksum } `should contain same` map { it.storedChecksum }
                map { it.computedChecksum } `should contain same` listOf(
                    0x935789CFu,
                    0xE700DC97u,
                    0xD4EF46AEu,
                    0xD140848Fu,
                    0x2B10B931u,
                )
            }
        }
    }

    /**
     * Check that modified checksums are saved correctly, including the preceding "header".
     */
    @Test
    fun `Checksums are saved correctly`() {
        val content = this::class.java.getResource("Test.svg")!!.readBytes().also {
            it[Addresses.XP] `should be equal to` 0
            it[Addresses.XP] = 2
        }
        SaveData(content).apply {
            fixChecksums().checksumSegments.apply {
                val expect = listOf(
                    listOf(0x02F26409u, 0x6F9ADA3Au, 0x2884540Au, 0x471E8E30u, 0x935789CFu),
                    listOf(0x049EBCBBu, 0x1D9DE024u, 0x79252A4Cu, 0x64B8CA68u, 0xE700DC97u),
                    listOf(0x044239BBu, 0x2F52F30Du, 0xE70F6F7Fu, 0xC85D9C72u, 0xD4EF46AEu),
                    listOf(0x042F5ECFu, 0x32EEDA40u, 0xF505F9C0u, 0xC7EB2380u, 0xD140848Fu),
                    listOf(0x00DC7E95u, 0xD5CBC564u, 0x5CB11B34u, 0x897ADE50u, 0x2B10B931u),
                )
                forEachIndexed { s, segment ->
                    for (c in 0 .. 4) {
                        bytes.leNum<Int>(segment.range.last + 1 + (c * 4))
                            .toUInt() `should be equal to` expect[s][c]
                    }
                }
            }
        }
    }
}
