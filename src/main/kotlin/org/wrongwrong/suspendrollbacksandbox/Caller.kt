package org.wrongwrong.suspendrollbacksandbox

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

object TestEx : RuntimeException("fail for test")

@Suppress("FunctionName")
@Component
@Transactional
class Caller(private val repository: Repository) {
    private fun getFuncName() = Throwable().stackTrace.let { it[1].methodName }

    // region 呼び出し元が非suspend
    fun nonSuspend_nonSuspend() {
        val funcName = getFuncName()
        println("$funcName on save\n${repository.nonSuspendSave(funcName)}")
    }

    fun nonSuspend_nonSuspend_fail() {
        val funcName = getFuncName()
        println("$funcName on save\n${repository.nonSuspendSave(funcName)}")
        throw TestEx
    }

    fun nonSuspend_suspend() {
        val funcName = getFuncName()
        println("$funcName on save\n${runBlocking { repository.suspendSave(funcName) }}")
    }

    fun nonSuspend_suspend_fail() {
        val funcName = getFuncName()
        println("$funcName on save\n${runBlocking { repository.suspendSave(funcName) }}")
        throw TestEx
    }
    // endregion

    // region 呼び出し元がsuspend
    suspend fun suspend_nonSuspend() {
        val funcName = getFuncName()
        println("$funcName on save\n${repository.nonSuspendSave(funcName)}")
    }

    suspend fun suspend_nonSuspend_fail() {
        val funcName = getFuncName()
        println("$funcName on save\n${repository.nonSuspendSave(funcName)}")
        throw TestEx
    }

    suspend fun suspend_suspend() {
        val funcName = getFuncName()
        println("$funcName on save\n${repository.suspendSave(funcName)}")
    }

    suspend fun suspend_suspend_fail() {
        val funcName = getFuncName()
        println("$funcName on save\n${repository.suspendSave(funcName)}")
        throw TestEx
    }
    // endregion
}
