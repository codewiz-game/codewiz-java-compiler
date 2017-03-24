package com.github.crob1140.codewiz.battles;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.1.2)",
    comments = "Source: code_interpreter.proto")
public class BattleEvaluatorGrpc {

  private BattleEvaluatorGrpc() {}

  public static final String SERVICE_NAME = "BattleEvaluator";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.github.crob1140.codewiz.battles.CodeTransferRequest,
      com.github.crob1140.codewiz.battles.CodeTransferResponse> METHOD_TRANSFER_CODE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "BattleEvaluator", "TransferCode"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.codewiz.battles.CodeTransferRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.codewiz.battles.CodeTransferResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.github.crob1140.codewiz.battles.CompilationRequest,
      com.github.crob1140.codewiz.battles.CompilationResult> METHOD_COMPILE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "BattleEvaluator", "Compile"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.codewiz.battles.CompilationRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.codewiz.battles.CompilationResult.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.github.crob1140.codewiz.battles.BattleRequest,
      com.github.crob1140.codewiz.battles.BattleAction> METHOD_EVALUATE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "BattleEvaluator", "Evaluate"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.codewiz.battles.BattleRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.codewiz.battles.BattleAction.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BattleEvaluatorStub newStub(io.grpc.Channel channel) {
    return new BattleEvaluatorStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BattleEvaluatorBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BattleEvaluatorBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static BattleEvaluatorFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BattleEvaluatorFutureStub(channel);
  }

  /**
   */
  public static abstract class BattleEvaluatorImplBase implements io.grpc.BindableService {

    /**
     */
    public void transferCode(com.github.crob1140.codewiz.battles.CodeTransferRequest request,
        io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.CodeTransferResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_TRANSFER_CODE, responseObserver);
    }

    /**
     */
    public void compile(com.github.crob1140.codewiz.battles.CompilationRequest request,
        io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.CompilationResult> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_COMPILE, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.BattleRequest> evaluate(
        io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.BattleAction> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_EVALUATE, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_TRANSFER_CODE,
            asyncUnaryCall(
              new MethodHandlers<
                com.github.crob1140.codewiz.battles.CodeTransferRequest,
                com.github.crob1140.codewiz.battles.CodeTransferResponse>(
                  this, METHODID_TRANSFER_CODE)))
          .addMethod(
            METHOD_COMPILE,
            asyncUnaryCall(
              new MethodHandlers<
                com.github.crob1140.codewiz.battles.CompilationRequest,
                com.github.crob1140.codewiz.battles.CompilationResult>(
                  this, METHODID_COMPILE)))
          .addMethod(
            METHOD_EVALUATE,
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.github.crob1140.codewiz.battles.BattleRequest,
                com.github.crob1140.codewiz.battles.BattleAction>(
                  this, METHODID_EVALUATE)))
          .build();
    }
  }

  /**
   */
  public static final class BattleEvaluatorStub extends io.grpc.stub.AbstractStub<BattleEvaluatorStub> {
    private BattleEvaluatorStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BattleEvaluatorStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BattleEvaluatorStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BattleEvaluatorStub(channel, callOptions);
    }

    /**
     */
    public void transferCode(com.github.crob1140.codewiz.battles.CodeTransferRequest request,
        io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.CodeTransferResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_TRANSFER_CODE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void compile(com.github.crob1140.codewiz.battles.CompilationRequest request,
        io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.CompilationResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_COMPILE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.BattleRequest> evaluate(
        io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.BattleAction> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_EVALUATE, getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class BattleEvaluatorBlockingStub extends io.grpc.stub.AbstractStub<BattleEvaluatorBlockingStub> {
    private BattleEvaluatorBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BattleEvaluatorBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BattleEvaluatorBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BattleEvaluatorBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.github.crob1140.codewiz.battles.CodeTransferResponse transferCode(com.github.crob1140.codewiz.battles.CodeTransferRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_TRANSFER_CODE, getCallOptions(), request);
    }

    /**
     */
    public com.github.crob1140.codewiz.battles.CompilationResult compile(com.github.crob1140.codewiz.battles.CompilationRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_COMPILE, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BattleEvaluatorFutureStub extends io.grpc.stub.AbstractStub<BattleEvaluatorFutureStub> {
    private BattleEvaluatorFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BattleEvaluatorFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BattleEvaluatorFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BattleEvaluatorFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.github.crob1140.codewiz.battles.CodeTransferResponse> transferCode(
        com.github.crob1140.codewiz.battles.CodeTransferRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_TRANSFER_CODE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.github.crob1140.codewiz.battles.CompilationResult> compile(
        com.github.crob1140.codewiz.battles.CompilationRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_COMPILE, getCallOptions()), request);
    }
  }

  private static final int METHODID_TRANSFER_CODE = 0;
  private static final int METHODID_COMPILE = 1;
  private static final int METHODID_EVALUATE = 2;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BattleEvaluatorImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(BattleEvaluatorImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_TRANSFER_CODE:
          serviceImpl.transferCode((com.github.crob1140.codewiz.battles.CodeTransferRequest) request,
              (io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.CodeTransferResponse>) responseObserver);
          break;
        case METHODID_COMPILE:
          serviceImpl.compile((com.github.crob1140.codewiz.battles.CompilationRequest) request,
              (io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.CompilationResult>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_EVALUATE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.evaluate(
              (io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.battles.BattleAction>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class BattleEvaluatorDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.github.crob1140.codewiz.battles.CodeInterpreter.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BattleEvaluatorGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BattleEvaluatorDescriptorSupplier())
              .addMethod(METHOD_TRANSFER_CODE)
              .addMethod(METHOD_COMPILE)
              .addMethod(METHOD_EVALUATE)
              .build();
        }
      }
    }
    return result;
  }
}
