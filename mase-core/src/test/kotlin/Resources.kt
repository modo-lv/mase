object Resources {
    fun testSave() = TestSaveData()
    class TestSaveData : SaveData<TestSaveData>(this::class.java.getResource("Test.svg")!!.readBytes())
}