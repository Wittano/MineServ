package com.wittano.mineserv.components.validator

import com.wittano.mineserv.data.Server
import com.wittano.mineserv.repository.ServerRepository
import org.springframework.batch.item.validator.ValidationException
import org.springframework.batch.item.validator.Validator
import org.springframework.stereotype.Component

@Component
class ServerValidator(
    private val serverRepo: ServerRepository
) : Validator<Server> {
    override fun validate(value: Server) {
        if (serverRepo.existsServerByName(value.name) == true) {
            throw ValidationException("Server ${value.name} is exist!")
        }
    }
}