package demo.test

import org.junit.Test
import kotlin.test.assertEquals

class DemoTest {

    @Test
    fun testFail() : Unit {
        assertEquals("that", "this")
    }

    @Test fun testPass() : Unit {
        assertEquals("yes", "yes")
    }

}
