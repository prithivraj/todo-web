import io.grpc.stub.StreamObserver

class TodoServiceImpl : TodoServiceGrpc.TodoServiceImplBase(){
    override fun createTodo(request: TodoProtos.CreateTodoRequest, responseObserver: StreamObserver<TodoProtos.CreateTodoResponse>) {
        //Server side logic.
        responseObserver.onNext(
            TodoProtos.CreateTodoResponse.newBuilder().setStatus("Ok da " + request.todoText).build()
        )
        responseObserver.onCompleted()
    }
}