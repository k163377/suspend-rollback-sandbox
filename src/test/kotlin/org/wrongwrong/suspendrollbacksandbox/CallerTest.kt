package org.wrongwrong.suspendrollbacksandbox

import kotlinx.coroutines.flow.toList
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
        exec { runBlocking { caller.suspend_suspend_fail() } }
    }

    @Test
    fun print() {
        println("result:\n${runBlocking { repository.findAll().toList() }}")
    }
}
