import io.grpc.ServerBuilder
import io.grpc.health.v1.HealthCheckResponse.ServingStatus
import io.grpc.services.HealthStatusManager
import java.util.concurrent.TimeUnit

fun main() {
    val health = HealthStatusManager()
    val server = ServerBuilder.forPort(5000)
            .addService(TodoServiceImpl())
            .addService(health.healthService)
            .build()
            .start()

    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            // Start graceful shutdown
            server.shutdown()
            try {
                // Wait for RPCs to complete processing
                if (!server.awaitTermination(30, TimeUnit.SECONDS)) {
                    // That was plenty of time. Let's cancel the remaining RPCs
                    server.shutdownNow()
                    // shutdownNow isn't instantaneous, so give a bit of time to clean resources up
                    // gracefully. Normally this will be well under a second.
                    server.awaitTermination(5, TimeUnit.SECONDS)
                }
            } catch (ex: InterruptedException) {
                server.shutdownNow()
            }
        }
    })
    println("Listening on port 5000")
    // This would normally be tied to the service's dependencies. For example, if HostnameGreeter
    // used a Channel to contact a required service, then when 'channel.getState() ==
    // TRANSIENT_FAILURE' we'd want to set NOT_SERVING. But HostnameGreeter has no dependencies, so
    // hard-coding SERVING is appropriate.
    // This would normally be tied to the service's dependencies. For example, if HostnameGreeter
    // used a Channel to contact a required service, then when 'channel.getState() ==
    // TRANSIENT_FAILURE' we'd want to set NOT_SERVING. But HostnameGreeter has no dependencies, so
    // hard-coding SERVING is appropriate.
    health.setStatus("", ServingStatus.SERVING)
    server.awaitTermination()
}