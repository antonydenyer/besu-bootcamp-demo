package net.consensys.besu.plugins.mev

import java.io.File
import java.math.BigInteger
import org.allfunds.blockchain.besu.tests.acceptance.contracts.SimpleStorage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.shaded.com.google.common.io.Resources.getResource
import org.web3j.crypto.Credentials
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Async

class AcceptanceTest {

    companion object {
        private val logger = LoggerFactory.getLogger(AcceptanceTest::class.java)

        private val instance: KDockerComposeContainer by lazy { KDockerComposeContainer(File("src/test/resources/docker-compose.yml")) }

        class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            instance.withEnv("BESU_VERSION", System.getenv("BESU_VERSION"))

            listOf("miner", "alice", "bob")
                .forEach {
                    val serviceName = "${it}_1"

                    instance
                        .withExposedService(serviceName, 8545, Wait.forListeningPort())
                        .waitingFor(serviceName, Wait.forLogMessage(".*Ethereum main loop is up.*", 1))
                        .withLogConsumer(
                            serviceName,
                            Slf4jLogConsumer(logger).withPrefix(it)
                        )
                }

            instance.start()
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            instance.stop()
        }
    }

    @Test
    fun canDeployPublicContract() {
        val rpcUrl = "http://${instance.getServiceHost("alice_1", 8545)}:${instance.getServicePort("alice_1", 8545)}"
        val node = JsonRpc2_0Web3j(HttpService(rpcUrl), 2000, Async.defaultExecutorService())

        val simpleStorageContract = SimpleStorage.deploy(
            node,
            Credentials.create(getResource(AcceptanceTest::class.java, "/config/alice/key").readText()),
            DefaultGasProvider()
        ).send()

        simpleStorageContract.set(BigInteger.valueOf(42)).send()

        assertThat(simpleStorageContract.get().send()).isEqualTo(BigInteger.valueOf(42))
    }
}
