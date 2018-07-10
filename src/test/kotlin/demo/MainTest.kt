package demo.test

import kotlin.test.assertEquals
import org.junit.Test

class DemoTest {

    @Test fun testFail() : Unit {
        assertEquals("that", "this")
    }

    @Test fun testPass() : Unit {
        assertEquals("yes", "yes")
    }

}
