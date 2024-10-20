package com.threedays.support.common.security

object SecurityContextHolder {

    private val contextHolder: ThreadLocal<SecurityContext<out Authentication>> = ThreadLocal()

    fun <T: Authentication> getContext(): SecurityContext<T>? {
        @Suppress("UNCHECKED_CAST")
        return contextHolder.get() as SecurityContext<T>?
    }

    fun setContext(context: SecurityContext<out Authentication>) {
        contextHolder.set(context)
    }

    fun clearContext() {
        contextHolder.remove()
    }

}
