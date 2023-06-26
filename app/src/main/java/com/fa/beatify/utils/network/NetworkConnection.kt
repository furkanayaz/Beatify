package com.fa.beatify.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NetworkConnection(
    context: Context
): Connection {
    private val conn: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<Connection.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(Connection.Status.Available) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                launch { send(Connection.Status.Unavailable) }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                launch { send(Connection.Status.Losing) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(Connection.Status.Lost) }
            }
        }

        conn.registerDefaultNetworkCallback(callback)

        awaitClose { conn.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}