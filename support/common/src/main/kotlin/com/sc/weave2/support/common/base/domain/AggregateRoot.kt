package com.sc.weave2.support.common.base.domain

abstract class AggregateRoot<T : DomainEntity<T, ID>, ID : TypeId<*>> : DomainEntity<T, ID>()
