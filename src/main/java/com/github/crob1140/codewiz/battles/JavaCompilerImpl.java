package com.github.crob1140.codewiz.battles;

import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.crob1140.codewiz.grpc.CommitDetails;
import com.github.crob1140.codewiz.grpc.CompilationResult;
import com.github.crob1140.codewiz.grpc.CompilationResult.ErrorCollection;
import com.github.crob1140.codewiz.grpc.CompilationResult.Success;
import com.github.crob1140.codewiz.grpc.JavaCompilerGrpc.JavaCompilerImplBase;

public class JavaCompilerImpl extends JavaCompilerImplBase {
	
	private static final String INTERNAL_ERROR_MESSAGE = "An internal server error has occurred.";

	private File filesRootDirectory;
	private RepositoryDownloader repoDownloader;
	
	public JavaCompilerImpl(File filesRootDirectory, RepositoryDownloader repoDownloader) {
		this.filesRootDirectory = filesRootDirectory;
		this.repoDownloader = repoDownloader;
	}
	
	@Override
    public void compile(CommitDetails details, StreamObserver<CompilationResult> responseObserver) {
		
		Path sourceDirPath = this.filesRootDirectory.toPath()
				.resolve(details.getUsername())
				.resolve(details.getRepository())
				.resolve(details.getCommitHash());
		
		File sourceDir = sourceDirPath.toFile();
		try {
			this.repoDownloader.cloneToDirectory(details, sourceDir);
			
			Path outputDirPath = sourceDirPath.resolve("bin");
	        File outputDir = outputDirPath.toFile();
			if (!outputDir.exists()) {
				outputDir.mkdir();
				List<JavaSourceFile> sourceFiles = Files.walk(sourceDirPath)
						.filter(Files::isRegularFile)
						.filter(file -> getFileExtension(file).equals("java"))
						.map(file -> new JavaSourceFile(file))
						.collect(Collectors.toList());
				
				JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
				DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
				List<String> compileOptions = Arrays.asList("-d", outputDirPath.toString()); 
				CompilationTask compilationTask = compiler.getTask(null, null, diagnostics, compileOptions, null, sourceFiles);
				
				boolean success = compilationTask.call();
				if (success) {
					
					// TODO: Transfer the compiled code to the file service
					// Return some reference in the success response that the caller
					// can use to obtain the compiled code when they need it
					
					responseObserver.onNext(CompilationResult.newBuilder()
							.setSuccess(Success.getDefaultInstance())
							.build());
					
					responseObserver.onCompleted();
				}
				else {
					ErrorCollection.Builder errBuilder = ErrorCollection.newBuilder();
					for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
						URI errorSourceUri = diagnostic.getSource().toUri();
						Path errorSourceAbsPath = Paths.get(errorSourceUri);
						Path errorSourceRelativePath = sourceDirPath.relativize(errorSourceAbsPath);
						errBuilder.addErrors(CompilationResult.Error.newBuilder()
								.setFileName(errorSourceRelativePath.toString())
								.setLineNumber(diagnostic.getLineNumber())
								.setColumnNumber(diagnostic.getColumnNumber())
								.setMessage(diagnostic.getMessage(Locale.getDefault()))
								.build());
					}

					responseObserver.onNext(CompilationResult.newBuilder()
							.setErrors(errBuilder.build())
							.build());
					
					responseObserver.onCompleted();
					outputDir.delete(); 
					return;
				}
			}
		}
		catch (URISyntaxException e) {
			String repoUrl = this.repoDownloader.getRepositoryUrl(details);
			String errorMsg = String.format("%s does not resolve to a valid git repository.", repoUrl);
			StatusException statusException = Status.INVALID_ARGUMENT
					.augmentDescription(errorMsg)
					.asException();
			
			responseObserver.onError(statusException);
		}
		catch (IOException | GitAPIException e) {
			StatusException statusException = Status.INTERNAL
					.augmentDescription(INTERNAL_ERROR_MESSAGE)
					.asException();
			
			responseObserver.onError(statusException);
		}
	}
	
    private static String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int extensionStartIndex = fileName.lastIndexOf(".");
        if (extensionStartIndex != -1) {
            return fileName.substring(extensionStartIndex+1, fileName.length());
        }
        return "";
    }
    
	private class JavaSourceFile extends SimpleJavaFileObject {
        
        private Path path;
        
        public JavaSourceFile(Path path) {
            super(path.toUri(), Kind.SOURCE);
            this.path = path;
        }
        
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return String.join(System.lineSeparator(), Files.readAllLines(this.path));
        }
    }
}
