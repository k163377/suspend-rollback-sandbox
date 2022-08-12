package org.wrongwrong.suspendrollbacksandbox

import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JooqConfig {
    @Bean
    fun jooqDSLContext(cfi: ConnectionFactory): DSLContext {
        return DSL.using(cfi).dsl()
    }
}
