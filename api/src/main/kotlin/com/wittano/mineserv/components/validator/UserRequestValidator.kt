package com.wittano.mineserv.components.validator

import com.wittano.mineserv.data.request.UserRequest
import org.springframework.batch.item.validator.ValidationException
import org.springframework.batch.item.validator.Validator
import org.springframework.stereotype.Component

@Component
class UserRequestValidator : Validator<UserRequest> {
    override fun validate(value: UserRequest) {
        val nameRegex = Regex("^[\\w]{5,}$")
        val passwordRegex = Regex("^[\\w!#$%^&*]{8,}$")

        if (!nameRegex.matches(value.username) || !passwordRegex.matches(value.password)) {
            throw ValidationException("Invalid login or password")
        }
    }
}