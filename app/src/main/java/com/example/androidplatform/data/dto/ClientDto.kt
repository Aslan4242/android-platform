package com.example.androidplatform.data.dto

import com.example.androidplatform.domain.models.clients.Clients

class ClientDto(
    val items: List<Item>,
)

fun ClientDto.mapToVacancies(): Clients {
    return Clients(
        listClients = this.items.map { it.mapToClientItem() }
    )
}
