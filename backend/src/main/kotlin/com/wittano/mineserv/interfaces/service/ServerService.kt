package com.wittano.mineserv.interfaces.service

import com.wittano.mineserv.data.Server
import com.wittano.mineserv.interfaces.crud.ModifiedOperations

interface ServerService: Service<Server>, ModifiedOperations<Server>