import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.junit.jupiter.api.Test

class ChecksumTests {
    @Test
    fun `Checksums are computed correctly`() {
        val content = this::class.java.getResource("Test.svg")!!.readBytes()
        SaveData(content).computeChecksums().checksumSegments.apply {
            size `should be equal to` 5
            map { it.computedChecksum } `should contain same` map { it.storedChecksum }
            map { it.computedChecksum } `should contain same` listOf(
                0x1CB295D1u,
                0x187E405Bu,
                0xDE24B146u,
                0xDB8B7367u,
                0x21DB4ED9u,
            )
        }
    }

    @Test
    fun `Checksums are fixed correctly`() {
        val content = this::class.java.getResource("Test.svg")!!.readBytes().also {
            // Set XP from 0 to 2
            it[0x003EE222] = 2
        }
        SaveData(content).apply {
            fixChecksums().checksumSegments.apply {
                map { it.computedChecksum } `should contain same` map { it.storedChecksum }
                map { it.computedChecksum } `should contain same` listOf(
                    0x97034B6Cu,
                    0xD2523FC1u,
                    0x437E1571u,
                    0x46D1D750u,
                    0xBC81EAEEu,
                )
            }
        }
    }
}
