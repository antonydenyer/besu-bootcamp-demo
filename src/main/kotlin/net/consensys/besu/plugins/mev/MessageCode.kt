package net.consensys.besu.plugins.mev

enum class MessageCode(val code: Int) {
    Status(0x00),
    NewBlockHashes(0x01),
    Transactions(0x02),
    GetBlockHeaders(0x03),
    BlockHeaders(0x04),
    GetBlockBodies(0x05),
    BlockBodies(0x06),
    NewBlock(0x07),
    NewPooledTransactionHashes(0x08),
    GetPooledTransactions(0x09),
    PooledTransactions(0x0a),
    GetNodeData(0x0d),
    NodeData(0x0e),
    GetReceipts(0x0f),
    Receipts(0x10),
}
