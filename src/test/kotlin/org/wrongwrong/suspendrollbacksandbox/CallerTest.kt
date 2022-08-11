package org.wrongwrong.suspendrollbacksandbox

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
private class CallerTest @Autowired constructor(
    val caller: Caller,
    val repository: Repository
) {
    private fun <T> exec(thrower: () -> T?): Any? = try { thrower() } catch (e: TestEx) { e }

    @BeforeEach
    fun callSaves() {
        exec { caller.nonSuspend_nonSuspend() }
        exec { caller.nonSuspend_nonSuspend_fail() }
        exec { caller.nonSuspend_suspend() }
        exec { caller.nonSuspend_suspend_fail() }

        exec { runBlocking { caller.suspend_nonSuspend() } }
        exec { runBlocking { caller.suspend_nonSuspend_fail() } }
        exec { runBlocking { caller.suspend_suspend() } }
        exec { runBlocking { caller.suspend_suspend_fail() } }
    }

    @Test
    fun print() {
        println("result:\n${repository.findAll()}")
    }
}
