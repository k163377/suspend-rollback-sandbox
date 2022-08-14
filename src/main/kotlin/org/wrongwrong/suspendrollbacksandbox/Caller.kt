package org.wrongwrong.suspendrollbacksandbox

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

object TestEx : RuntimeException("fail for test")

@Suppress("FunctionName")
@Component
@Transactional
class Caller(private val repository: Repository) {
    private fun getFuncName() = Throwable().stackTrace.let { it[1].methodName }

    // region 呼び出し元がsuspend
    suspend fun suspend_suspend_fail() {
        val funcName = getFuncName()
        println("$funcName on save\n${repository.save(funcName)}")
        throw TestEx
    }
    // endregion
}
