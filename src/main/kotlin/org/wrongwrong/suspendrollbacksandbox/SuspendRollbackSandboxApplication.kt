package org.wrongwrong.suspendrollbacksandbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SuspendRollbackSandboxApplication

fun main(args: Array<String>) {
    runApplication<SuspendRollbackSandboxApplication>(*args)
}
