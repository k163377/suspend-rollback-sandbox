package org.wrongwrong.suspendrollbacksandbox

import org.jooq.DSLContext
import org.jooq.generated.tables.references.FOO_TABLE
import org.springframework.stereotype.Repository

@Repository
class Repository(private val create: DSLContext) {
    fun findAll() = create.selectFrom(FOO_TABLE).fetch()

    fun nonSuspendSave(value: String) = create.insertInto(FOO_TABLE).values(value).returning().fetchAnyInto(FOO_TABLE)

    suspend fun suspendSave(value: String) =
        create.insertInto(FOO_TABLE).values(value).returning().fetchAnyInto(FOO_TABLE)
}
