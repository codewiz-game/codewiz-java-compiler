package com.github.crob1140.codewiz.battles;

import io.grpc.stub.StreamObserver;

import java.io.File;
import java.sql.SQLException;

import liquibase.exception.LiquibaseException;

import com.github.crob1140.codewiz.battles.BattleDetails.JavaSpecifics;
import com.github.crob1140.codewiz.battles.BattleEvent.Idle;
import com.github.crob1140.codewiz.battles.CompilationResult.Error;
import com.github.crob1140.codewiz.battles.CompilationResult.ErrorCollection;

/**
 * This class is used as a temporary way to test the functionality of the {@link BattleEvaluatorImpl} until proper
 * unit tests have been put in.
 * 
 * @author CJR
 */
public class BattleEvaluatorImplTester {

	public static void main(String[] args) throws LiquibaseException, SQLException {
		BattleEvaluatorImpl evaluator = new BattleEvaluatorImpl(new File("C:/evaluatortest"));

		StreamObserver<CodeTransferResponse> transferResponseHandler = new StreamObserver<CodeTransferResponse>() {
			
			@Override
			public void onNext(CodeTransferResponse value) {
				String codeId = value.getId();
				TestCompilationResultObserver compilationResultHandler = new TestCompilationResultObserver(evaluator, codeId);
				evaluator.compile(CompilationRequest.newBuilder().setId(codeId).build(), compilationResultHandler);
			}

			@Override
			public void onError(Throwable t) {}

			@Override
			public void onCompleted() {}
		};
		
		evaluator.transferCode(CodeTransferRequest.newBuilder()
			.setRepoDetails(RepoDetails.newBuilder()
				.setDomain("github.com")
				.setUsername("crob1140")
				.setRepository("codewiz-sample-wizard")
				.setBranch("development")
				.setCommitHash("b461b2af801497de4dcc250eb8eca46a273118fa")
				.build()
			).build(), transferResponseHandler);
	}
	
	private static class TestCompilationResultObserver implements StreamObserver<CompilationResult> {

		private static final StreamObserver<BattleAction> actionHandler = new StreamObserver<BattleAction>() {
			@Override
			public void onNext(BattleAction value) {}

			@Override
			public void onError(Throwable t) {}

			@Override
			public void onCompleted() {}
		};
		
		private String codeId;
		private BattleEvaluatorImpl evaluator;
		
		public TestCompilationResultObserver(BattleEvaluatorImpl evaluator, String codeId) {
			this.codeId = codeId;
			this.evaluator = evaluator;
		}
		
		@Override
		public void onNext(CompilationResult value) {
			StreamObserver<BattleRequest> requestHandler = evaluator.evaluate(actionHandler);
			
			switch (value.getResultCase())
			{
				case ERRORS:
					ErrorCollection errors = value.getErrors();
					for (Error error : errors.getErrorsList()) {
						System.err.println(error.getFileName() + " @ line " + 
								error.getLineNumber() + ": " +
								error.getMessage());
					}
					break;
				case SUCCESS:
					requestHandler.onNext(BattleRequest.newBuilder()
						.setBattleDetails(
							BattleDetails.newBuilder()
							.setCodeId(this.codeId)
							.setJavaSpecifics(JavaSpecifics.newBuilder()
								.setWizardClass("SampleWizard")
								.build()
							).build()
						).build()
					);
					
					requestHandler.onNext(BattleRequest.newBuilder()
						.setBattleEvent(BattleEvent.newBuilder()
							.setIdle(Idle.getDefaultInstance())
						).build()
					);
				
					break;
				case RESULT_NOT_SET:
			}
		}

		@Override
		public void onError(Throwable t) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCompleted() {
			// TODO Auto-generated method stub
			
		}
	}
}
