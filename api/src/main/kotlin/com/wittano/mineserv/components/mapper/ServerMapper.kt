package com.wittano.mineserv.components.mapper

import com.wittano.mineserv.components.exceptions.MappedException
import com.wittano.mineserv.data.Server
import com.wittano.mineserv.data.request.ServerRequest
import com.wittano.mineserv.enums.server.ServerStatus
import com.wittano.mineserv.interfaces.Mapper
import com.wittano.mineserv.repository.UserRepository
import com.wittano.mineserv.repository.VersionRepository
import org.springframework.stereotype.Component

@Component
class ServerMapper(
    private val userRepo: UserRepository,
    private val versionRepo: VersionRepository
) : Mapper<ServerRequest, Server> {
    override fun map(data: ServerRequest): Server =
        Server(
            null,
            data.name,
            userRepo.findById(data.owner_id!!).orElseThrow {
                throw MappedException("User with ${data.owner_id} id isn't exist")
            },
            versionRepo.findById(data.version_id).orElseThrow {
                throw MappedException("Version with ${data.owner_id} id isn't exist")
            },
            null,
            ServerStatus.STOP
        )
}