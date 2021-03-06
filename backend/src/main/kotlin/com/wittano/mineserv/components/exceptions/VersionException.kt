package com.wittano.mineserv.components.exceptions

import java.sql.SQLException

/**
 * Exception, which informs that something went wrong on operation of version.
 * For example: version doesn't exist in database
 */
class VersionException(msg: String) : SQLException(msg)