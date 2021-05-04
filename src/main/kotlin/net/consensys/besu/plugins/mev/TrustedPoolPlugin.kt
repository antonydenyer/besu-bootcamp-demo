package net.consensys.besu.plugins.mev

import com.google.auto.service.AutoService
import org.apache.logging.log4j.LogManager
import org.hyperledger.besu.ethereum.p2p.peers.EnodeURLImpl
import org.hyperledger.besu.plugin.BesuContext
import org.hyperledger.besu.plugin.BesuPlugin
import org.hyperledger.besu.plugin.data.EnodeURL
import org.hyperledger.besu.plugin.services.PermissioningService

@AutoService(BesuPlugin::class)
class TrustedPoolPlugin : BesuPlugin {
    private val MINER =
        EnodeURLImpl.fromString("enode://c9b23099ba7ee0e1b95b8088fb32337a908ea86f57512411080a7b9957079c101b996f362dad0f283a640900220e4e0cc0665c0c2491e9acc3970b498923c836@172.20.239.11:30303")

    private val logger = LogManager.getLogger()

    override fun register(context: BesuContext) {
        context.getService(PermissioningService::class.java).map {
            it.registerNodeMessagePermissioningProvider(this::isMessagePermitted)
        }
    }

    private fun isMessagePermitted(destinationEnode: EnodeURL, code: Int): Boolean {
        if (code == MessageCode.Transactions.code) {
            logger.error("is Transactions permitted to $destinationEnode")
        }

        // if (code == MessageCode.Transactions.code && destinationEnode != MINER) {
        //     logger.error("Block message to $destinationEnode")
        //     return false
        // }
        return true
    }

    override fun start() {
    }

    override fun stop() {
    }
}
