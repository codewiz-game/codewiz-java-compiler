package com.github.crob1140.codewiz.grpc;

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
    comments = "Source: java_compiler.proto")
public class JavaCompilerGrpc {

  private JavaCompilerGrpc() {}

  public static final String SERVICE_NAME = "JavaCompiler";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.github.crob1140.codewiz.grpc.CommitDetails,
      com.github.crob1140.codewiz.grpc.CompilationResult> METHOD_COMPILE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "JavaCompiler", "Compile"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.codewiz.grpc.CommitDetails.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.codewiz.grpc.CompilationResult.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static JavaCompilerStub newStub(io.grpc.Channel channel) {
    return new JavaCompilerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static JavaCompilerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new JavaCompilerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static JavaCompilerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new JavaCompilerFutureStub(channel);
  }

  /**
   */
  public static abstract class JavaCompilerImplBase implements io.grpc.BindableService {

    /**
     */
    public void compile(com.github.crob1140.codewiz.grpc.CommitDetails request,
        io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.grpc.CompilationResult> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_COMPILE, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_COMPILE,
            asyncUnaryCall(
              new MethodHandlers<
                com.github.crob1140.codewiz.grpc.CommitDetails,
                com.github.crob1140.codewiz.grpc.CompilationResult>(
                  this, METHODID_COMPILE)))
          .build();
    }
  }

  /**
   */
  public static final class JavaCompilerStub extends io.grpc.stub.AbstractStub<JavaCompilerStub> {
    private JavaCompilerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JavaCompilerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JavaCompilerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JavaCompilerStub(channel, callOptions);
    }

    /**
     */
    public void compile(com.github.crob1140.codewiz.grpc.CommitDetails request,
        io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.grpc.CompilationResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_COMPILE, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class JavaCompilerBlockingStub extends io.grpc.stub.AbstractStub<JavaCompilerBlockingStub> {
    private JavaCompilerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JavaCompilerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JavaCompilerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JavaCompilerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.github.crob1140.codewiz.grpc.CompilationResult compile(com.github.crob1140.codewiz.grpc.CommitDetails request) {
      return blockingUnaryCall(
          getChannel(), METHOD_COMPILE, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class JavaCompilerFutureStub extends io.grpc.stub.AbstractStub<JavaCompilerFutureStub> {
    private JavaCompilerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private JavaCompilerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected JavaCompilerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new JavaCompilerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.github.crob1140.codewiz.grpc.CompilationResult> compile(
        com.github.crob1140.codewiz.grpc.CommitDetails request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_COMPILE, getCallOptions()), request);
    }
  }

  private static final int METHODID_COMPILE = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final JavaCompilerImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(JavaCompilerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_COMPILE:
          serviceImpl.compile((com.github.crob1140.codewiz.grpc.CommitDetails) request,
              (io.grpc.stub.StreamObserver<com.github.crob1140.codewiz.grpc.CompilationResult>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class JavaCompilerDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.github.crob1140.codewiz.grpc.JavaCompilerOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (JavaCompilerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new JavaCompilerDescriptorSupplier())
              .addMethod(METHOD_COMPILE)
              .build();
        }
      }
    }
    return result;
  }
}
