package com.github.crob1140.codewiz.battles;

import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import javax.xml.bind.DatatypeConverter;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.github.crob1140.codewiz.Wizard;
import com.github.crob1140.codewiz.actions.Action;
import com.github.crob1140.codewiz.battles.BattleDetails.JavaSpecifics;
import com.github.crob1140.codewiz.battles.BattleEvaluatorGrpc.BattleEvaluatorImplBase;
import com.github.crob1140.codewiz.battles.CompilationResult.Error;
import com.github.crob1140.codewiz.battles.CompilationResult.ErrorCollection;
import com.github.crob1140.codewiz.battles.CompilationResult.Success;

public class BattleEvaluatorImpl extends BattleEvaluatorImplBase {
	
	private static final String NAMESPACE_ID = "95cf00c82f9a4d40a00f9d88ced4e3f8";
	
	private File filesRootDirectory;
	
	public BattleEvaluatorImpl(File filesRootDirectory) {
		this.filesRootDirectory = filesRootDirectory;
	}
	
	@Override
	public void transferCode(CodeTransferRequest request, StreamObserver<CodeTransferResponse> responseObserver) {
		switch (request.getTransferCase())
		{
			case REPO_DETAILS:
				try {
					// Join the repo details to create some unique identifier
					RepoDetails repoDetails = request.getRepoDetails();
					String repoIdentifier = String.join("/", repoDetails.getDomain(), 
							repoDetails.getUsername(), 
							repoDetails.getRepository(), 
							repoDetails.getBranch(),
							repoDetails.getCommitHash());
					
					UUID repoUUID = getV3UUID(NAMESPACE_ID, repoIdentifier);
					
					// Only pull the code if we haven't already got this commit
					File repoStoredDir = filesRootDirectory.toPath().resolve(repoUUID.toString()).toFile();
					if (!repoStoredDir.exists()) {
						
						// Clone the branch from the git repo
						String repoUrl = String.format("https://%s/%s/%s.git", repoDetails.getDomain(), repoDetails.getUsername(), repoDetails.getRepository());
						Git git = Git.cloneRepository()
							.setURI(repoUrl)
							.setDirectory(repoStoredDir)
							.setBranchesToClone(Arrays.asList("refs/heads/" + repoDetails.getBranch()))
							.setBranch("refs/heads/" + repoDetails.getBranch())
							.call();
						
						// Hard reset to the specific commit
						git.reset()
							.setMode(ResetType.HARD)
							.setRef(repoDetails.getCommitHash())
							.call();
					}
					
					// Return the unique identifier to the client
					responseObserver.onNext(CodeTransferResponse.newBuilder()
							.setId(repoUUID.toString())
							.build());
					
				} catch (GitAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
			case UPLOAD_REQUEST:
				break;
			case TRANSFER_NOT_SET:
				break;
		}
	}
	
	@Override
	public void compile(CompilationRequest request, StreamObserver<CompilationResult> responseObserver) {
		
		Path sourceDirPath = filesRootDirectory.toPath().resolve(request.getId());
		File sourceDir = sourceDirPath.toFile();
		
		if (sourceDir.exists()) {
			Path outputDirPath = sourceDirPath.resolve("bin");
            File outputDir = outputDirPath.toFile();
			if (!outputDir.exists()) {
				try
				{
					outputDir.mkdir();
					List<CodeWizSourceFile> sourceFiles = Files.walk(sourceDirPath)
							.filter(Files::isRegularFile)
							.filter(file -> getFileExtension(file).equals("java"))
							.map(file -> new CodeWizSourceFile(file))
							.collect(Collectors.toList());
					
					JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
					DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
					List<String> compileOptions = Arrays.asList("-d", outputDirPath.toString()); 
					CompilationTask compilationTask = compiler.getTask(null, null, diagnostics, compileOptions, null, sourceFiles);
					
					boolean success = compilationTask.call();
					if (!success) {
						ErrorCollection.Builder errBuilder = ErrorCollection.newBuilder();
						for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
							URI errorSourceUri = diagnostic.getSource().toUri();
							Path errorSourceAbsPath = Paths.get(errorSourceUri);
							Path errorSourceRelativePath = sourceDirPath.relativize(errorSourceAbsPath);
							errBuilder.addErrors(Error.newBuilder()
									.setFileName(errorSourceRelativePath.toString())
									.setLineNumber((int)diagnostic.getLineNumber())
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
				catch (IOException e) {
					// TODO: return an error that is more descriptive to the client
					responseObserver.onError(e);
				}
			}
		}
		
		responseObserver.onNext(CompilationResult.newBuilder()
				.setSuccess(Success.getDefaultInstance())
				.build());
		
		responseObserver.onCompleted();
	}
	
	@Override
	public StreamObserver<BattleRequest> evaluate(StreamObserver<BattleAction> responseObserver) {
		return new BattleRequestObserver(responseObserver);
	}
	
    private static String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int extensionStartIndex = fileName.lastIndexOf(".");
        if (extensionStartIndex != -1) {
            return fileName.substring(extensionStartIndex+1, fileName.length());
        }
        return "";
    }
    
    private static UUID getV3UUID(String namespace, String name) {
    	// Get the bytes of namespace and name
        byte[] nsbytes = DatatypeConverter.parseHexBinary(namespace);
        byte[] namebytes = name.getBytes(Charset.forName("UTF-8"));

        // Concat both byte arrays
        byte[] allBytes = new byte[nsbytes.length + namebytes.length];
        System.arraycopy(nsbytes, 0, allBytes, 0, nsbytes.length);
        System.arraycopy(namebytes, 0, allBytes, nsbytes.length, namebytes.length);

        return UUID.nameUUIDFromBytes(allBytes);
    }
	
	private class CodeWizSourceFile extends SimpleJavaFileObject {
        
        private Path path;
        
        public CodeWizSourceFile(Path path) {
            super(path.toUri(), Kind.SOURCE);
            this.path = path;
        }
        
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return String.join(System.lineSeparator(), Files.readAllLines(this.path));
        }
    }
	
	private class BattleRequestObserver implements StreamObserver<BattleRequest> {

		private Wizard wizard;
		private StreamObserver<BattleAction> responseObserver;
		
		public BattleRequestObserver(StreamObserver<BattleAction> responseObserver) {
			this.responseObserver = responseObserver;
		}

		@Override
		public void onNext(BattleRequest value) {
			switch (value.getValueCase()) {
			case BATTLE_DETAILS:
				BattleDetails battleDetails = value.getBattleDetails();
                JavaSpecifics javaSpecifics = battleDetails.getJavaSpecifics();
                if (javaSpecifics == null) {
                    this.responseObserver.onError(new IllegalArgumentException("Expected code_details to contain Java specifics"));
                }
                
        		Path sourceDirPath = filesRootDirectory.toPath().resolve(battleDetails.getCodeId());
    			Path outputDirPath = sourceDirPath.resolve("bin");
            
                try {
                	String wizardClassName = javaSpecifics.getWizardClass();
                	URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { outputDirPath.toUri().toURL() });

                	@SuppressWarnings("unchecked")
                	Class<? extends Wizard> wizardClass = (Class<? extends Wizard>) Class.forName(wizardClassName, true, classLoader);
                	this.wizard = wizardClass.newInstance();
                	
                	Stack<Action> actionStack = new Stack<Action>();
                	this.wizard.setActionQueue(actionStack);
                }
                // TODO: each of these catch blocks should return a more descriptive
                // error to the client that doesn't expose as much information about the implementation.
                catch (MalformedURLException e) {
                	responseObserver.onError(e);
                }
                catch (ClassNotFoundException e) {
                	responseObserver.onError(e);
                }
                catch (ClassCastException e) {
                	responseObserver.onError(e);
                } 
                catch (InstantiationException e) {
                	responseObserver.onError(e);
				} 
                catch (IllegalAccessException e) {
                	responseObserver.onError(e);
				} 
			case BATTLE_EVENT:
				BattleEvent event = value.getBattleEvent();
				switch (event.getEventCase())
				{
					case IDLE:
						wizard.onIdle();
					case ENEMY_SIGHTED:
						wizard.onEnemySighted();
					case EVENT_NOT_SET:
						responseObserver.onError(new IllegalArgumentException("Missing mandatory 'event' field."));
				}
			case VALUE_NOT_SET:
				responseObserver.onError(new IllegalArgumentException("Missing mandatory 'value' field."));
			}
		}

		@Override
		public void onError(Throwable t) {
			// TODO: log the error
		}

		@Override
		public void onCompleted() {}
	}
}
