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
            map { it.computedChecksum } `should contain same` listOf<Long>(
                0x1CB295D1,
                0x187E405B,
                0xDE24B146,
                0xDB8B7367,
                0x21DB4ED9,
            ).map { it.toInt() }
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
                map { it.computedChecksum } `should contain same` listOf<Long>(
                    0x97034B6C,
                    0xD2523FC1,
                    0x437E1571,
                    0x46D1D750,
                    0xBC81EAEE,
                ).map { it.toInt() }
            }
        }
    }
}
