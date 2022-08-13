package org.wrongwrong.suspendrollbacksandbox

import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import org.jooq.generated.tables.references.FOO_TABLE
import org.jooq.impl.DSL
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux

@SpringBootTest
private class CallerTest @Autowired constructor(
    val caller: Caller,
    val cfi: ConnectionFactory
) {
    private fun <T> exec(thrower: () -> T?): Any? = try { thrower() } catch (e: TestEx) { e }

    @BeforeEach
    fun callSaves() {
        exec { runBlocking { caller.suspend_suspend_fail() } }
    }

    @Test
    fun print() {
        val query = DSL.using(cfi).selectFrom(FOO_TABLE)
        println("result:\n${runBlocking { Flux.from(query).map { it.into(FOO_TABLE) }.asFlow().toList() }}")
    }
}
