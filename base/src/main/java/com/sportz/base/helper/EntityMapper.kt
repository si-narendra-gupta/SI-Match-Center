package com.sportz.base.helper

interface EntityMapper<E, D> {
    fun toDomain(entity: E): D
}