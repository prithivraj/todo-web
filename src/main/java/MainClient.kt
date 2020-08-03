import io.grpc.ManagedChannelBuilder

fun main() {
    val target = "localhost:5000"
    val channel =
        ManagedChannelBuilder.forTarget(target)
            .usePlaintext()
            .build()
    val newBlockingStub = TodoServiceGrpc.newBlockingStub(channel)
    val createTodo = newBlockingStub.createTodo(
        TodoProtos.CreateTodoRequest
            .newBuilder()
            .setTodoText("test")
            .build()
    )
    println(createTodo)
}