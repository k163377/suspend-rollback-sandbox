package org.wrongwrong.suspendrollbacksandbox

import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.jooq.generated.tables.records.FooTableRecord
import org.jooq.generated.tables.references.FOO_TABLE
import org.jooq.impl.DSL
import org.springframework.r2dbc.connection.ConnectionHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.reactive.TransactionSynchronizationManager
import reactor.core.publisher.Mono
import java.lang.RuntimeException

@Repository
class Repository(private val cfi: ConnectionFactory) {
    suspend fun suspendSave(value: String): FooTableRecord {
        val holder: ConnectionHolder = TransactionSynchronizationManager.forCurrentTransaction()
            .mapNotNull { it.getResource(cfi) as ConnectionHolder? }
            .awaitSingleOrNull() ?: throw RuntimeException("トランザクションが取得できませんでした")

        val ctxt = DSL.using(holder.connection)

        return Mono.from(ctxt.insertInto(FOO_TABLE).values(value).returning())
            .map { it.into(FOO_TABLE) }
            .awaitSingle()!!
    }
}
