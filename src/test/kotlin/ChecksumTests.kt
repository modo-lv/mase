import gui.tabs.Model
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain same`
import org.junit.jupiter.api.Test
import structure.SaveData

class ChecksumTests {
    @Test
    fun `Checksums are computed correctly`() {
        SaveData(this::class.java.getResource("Test.svg")!!.readBytes()).apply {
            computeChecksums()
            Model.checksums.size `should be equal to` 5
            Model.checksums.map { it.computedHash } `should contain same` Model.checksums.map { it.storedHash }
            Model.checksums.map { it.computedHash } `should contain same` listOf(
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
        SaveData(this::class.java.getResource("Test.svg")!!.readBytes()).apply {
            updateXp(2u)
            computeChecksums()
            fixChecksums()
            Model.checksums.map { it.computedHash } `should contain same` Model.checksums.map { it.storedHash }
            Model.checksums.map { it.computedHash } `should contain same` listOf(
                0x97034B6Cu,
                0xD2523FC1u,
                0x437E1571u,
                0x46D1D750u,
                0xBC81EAEEu,
            )
        }
    }
}