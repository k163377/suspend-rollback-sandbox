package org.wrongwrong.suspendrollbacksandbox

import kotlinx.coroutines.flow.single
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.jooq.generated.tables.references.FOO_TABLE
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class Repository(private val create: DSLContext) {
    fun findAll() = Flux.from(create.selectFrom(FOO_TABLE)).map { it.into(FOO_TABLE) }.asFlow()

    suspend fun suspendSave(value: String) = Mono.from(create.insertInto(FOO_TABLE).values(value).returning())
        .map { it.into(FOO_TABLE) }
        .asFlow()
        .single()!!
}
