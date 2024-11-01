import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.junit.jupiter.api.Test
import structure.SaveData

class ChecksumTests {
    @Test
    fun checksums() {
        SaveData(this::class.java.getResource("Test.svg")!!.readBytes()).apply {
            computeChecksums()
            checksums.size `should be equal to` 5
            checksums.map { it.computedHash } `should contain same` checksums.map { it.storedHash }
            checksums.map { it.computedHash } `should contain same` listOf(
                0x1CB295D1u,
                0x187E405Bu,
                0xDE24B146u,
                0xDB8B7367u,
                0x21DB4ED9u,
            )
        }
    }
}