package com.sportz.match_center_base.helper

interface EntityMapper<E, D> {
    fun toDomain(entity: E): D
}