package com.github.crob1140.filetransfer;

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
    comments = "Source: file_transfer.proto")
public class FileTransferGrpc {

  private FileTransferGrpc() {}

  public static final String SERVICE_NAME = "FileTransfer";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.github.crob1140.filetransfer.UploadRequest,
      com.github.crob1140.filetransfer.UploadAcknowledgement> METHOD_UPLOAD =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "FileTransfer", "Upload"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.filetransfer.UploadRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.github.crob1140.filetransfer.UploadAcknowledgement.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FileTransferStub newStub(io.grpc.Channel channel) {
    return new FileTransferStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FileTransferBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new FileTransferBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static FileTransferFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new FileTransferFutureStub(channel);
  }

  /**
   */
  public static abstract class FileTransferImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.github.crob1140.filetransfer.UploadRequest> upload(
        io.grpc.stub.StreamObserver<com.github.crob1140.filetransfer.UploadAcknowledgement> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_UPLOAD, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_UPLOAD,
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.github.crob1140.filetransfer.UploadRequest,
                com.github.crob1140.filetransfer.UploadAcknowledgement>(
                  this, METHODID_UPLOAD)))
          .build();
    }
  }

  /**
   */
  public static final class FileTransferStub extends io.grpc.stub.AbstractStub<FileTransferStub> {
    private FileTransferStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FileTransferStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FileTransferStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.github.crob1140.filetransfer.UploadRequest> upload(
        io.grpc.stub.StreamObserver<com.github.crob1140.filetransfer.UploadAcknowledgement> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_UPLOAD, getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class FileTransferBlockingStub extends io.grpc.stub.AbstractStub<FileTransferBlockingStub> {
    private FileTransferBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FileTransferBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FileTransferBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class FileTransferFutureStub extends io.grpc.stub.AbstractStub<FileTransferFutureStub> {
    private FileTransferFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private FileTransferFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileTransferFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new FileTransferFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_UPLOAD = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FileTransferImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(FileTransferImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UPLOAD:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.upload(
              (io.grpc.stub.StreamObserver<com.github.crob1140.filetransfer.UploadAcknowledgement>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class FileTransferDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.github.crob1140.filetransfer.FileTransferOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FileTransferGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FileTransferDescriptorSupplier())
              .addMethod(METHOD_UPLOAD)
              .build();
        }
      }
    }
    return result;
  }
}
