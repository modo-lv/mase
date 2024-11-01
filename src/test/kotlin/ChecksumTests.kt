import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import structure.SaveData

class ChecksumTests {
    @Test
    fun checksums() {
        SaveData(this::class.java.getResource("Test.svg")!!.readBytes()).apply {
            computeChecksums()
            checksums.size `should be equal to` 5
            checksums[0].computed `should be equal to` 0x1CB295D1u
            checksums[1].computed `should be equal to` 0x187E405Bu
            checksums[2].computed `should be equal to` 0xDE24B146u
            checksums[3].computed `should be equal to` 0xDB8B7367u
            checksums[4].computed `should be equal to` 0x21DB4ED9u
        }
    }
}