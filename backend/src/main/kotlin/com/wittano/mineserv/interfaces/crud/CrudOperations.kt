package com.wittano.mineserv.interfaces.crud

interface CrudOperations<T> : GetOperation<T>, ModifiedOperations<T>, UpdateModification<T>