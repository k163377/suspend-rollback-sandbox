package org.wrongwrong.suspendrollbacksandbox

import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.generated.tables.records.FooTableRecord
import org.jooq.generated.tables.references.FOO_TABLE
import org.jooq.impl.DSL
import org.springframework.r2dbc.connection.ConnectionFactoryUtils
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class Repository(private val cfi: ConnectionFactory) {
    suspend fun save(value: String): FooTableRecord {
        return ConnectionFactoryUtils.getConnection(cfi)
            .flatMap {
                val ctxt = DSL.using(it)
                Mono.from(ctxt.insertInto(FOO_TABLE).values(value).returning())
            }.map { it.into(FOO_TABLE) }
            .awaitSingle()!!
    }
}
