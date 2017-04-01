package com.github.crob1140.codewiz;

import io.grpc.Server;
import io.grpc.Status;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.UUID;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.Daemon;
import org.eclipse.jgit.transport.DaemonClient;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.crob1140.codewiz.battles.BattleEvaluatorGrpc;
import com.github.crob1140.codewiz.battles.BattleEvaluatorGrpc.BattleEvaluatorBlockingStub;
import com.github.crob1140.codewiz.battles.BattleEvaluatorImpl;
import com.github.crob1140.codewiz.battles.CodeTransferRequest;
import com.github.crob1140.codewiz.battles.RepoDetails;
import com.github.crob1140.codewiz.battles.RepositoryDownloader;

public class TestTransferCode {
	
	private static final String GIT_SUFFIX = ".git";
	private static final Integer TEST_REPO_PORT = 8888;
	private static final String TEST_SERVER_NAME = "testserver";
	
	private static final RepositoryResolver<DaemonClient> TEST_REPO_RESOLVER = new RepositoryResolver<DaemonClient>() {
		@Override
		public Repository open(DaemonClient req, String name)
				throws RepositoryNotFoundException,
				ServiceNotAuthorizedException, ServiceNotEnabledException,
				ServiceMayNotContinueException {
			
			try {
				// Strip the .git prefix
				if (name.endsWith(GIT_SUFFIX)) {
					name = name.substring(0, name.length() - GIT_SUFFIX.length());
				}

				// Get the path to where the repo is expected to be
				File gitDir = Paths.get(TestTransferCode.class.getResource("").toURI())
						.resolve(name)
						.resolve(GIT_SUFFIX)
						.toFile();
				
				// If the directory doesn't exist, return an error indicating that the
				// repo is not found, otherwise return the directory as a FileRepository
				if (!gitDir.exists()) {
					throw new RepositoryNotFoundException(gitDir);
				}
				
				return FileRepositoryBuilder.create(gitDir);
			}
			catch (URISyntaxException | IOException e) {
				throw new ServiceMayNotContinueException(e);
			}
		}
	};
	
	private File repoStorageDir = new File("testdir");
	private Server testServer;
	private Daemon gitServerDaemon;
	private BattleEvaluatorBlockingStub testClientStub;
	
	@BeforeClass
	public void setUp() throws IOException, URISyntaxException {
		
		repoStorageDir = Paths.get(TestTransferCode.class.getResource("").toURI())
			.resolve("repoStorageDir")
			.toFile();
		
		if (!repoStorageDir.exists()) {
			repoStorageDir.mkdir();
		}
		
		gitServerDaemon = new Daemon(new InetSocketAddress(TEST_REPO_PORT));
		gitServerDaemon.getService("git-receive-pack").setEnabled(true);
		gitServerDaemon.setRepositoryResolver(TEST_REPO_RESOLVER);
		gitServerDaemon.start();
		
		testServer = InProcessServerBuilder.forName(TEST_SERVER_NAME)
				.directExecutor()
				.addService(new BattleEvaluatorImpl(repoStorageDir, new RepositoryDownloader()))
				.build()
				.start();
		
	    testClientStub = BattleEvaluatorGrpc.newBlockingStub(InProcessChannelBuilder.forName(TEST_SERVER_NAME)
		        .directExecutor()
		        .build());
	}
	
	@AfterClass
	public void tearDown() {
		repoStorageDir.delete();
		gitServerDaemon.stop();
		testServer.shutdown();
	}
	
	@AfterMethod
	public void cleanUp() {
		try {
			FileUtils.empty(repoStorageDir);
		}
		catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
	
	 @DataProvider(name = "invalid_repos")
	 public static Object[][] invalidRepos() {
		 return new Object[][] {
			 {createRepoDetails("invaliddomain", "testuser", "testrepo", "testbranch", "46e150fea3de1828a3b539209437ce256ce862d8")}, // domain doesnt exist
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "invaliduser", "testrepo", "testbranch", "46e150fea3de1828a3b539209437ce256ce862d8")}, // user doesnt exist
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "testuser", "invalidrepo", "testbranch", "46e150fea3de1828a3b539209437ce256ce862d8")}, // repo doesnt exist
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "testuser", "testrepo", "invalidbranch", "46e150fea3de1828a3b539209437ce256ce862d8")}, // branch doesnt exist
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "testuser", "testrepo", "testbranch", "invalidcommit")}, // commit that isn't a valid SHA-1 hash
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "testuser", "testrepo", "testbranch", "b405da91be55fb2e62bd9b9d882740c5b9e2077e")}, // commit that no longer exists as it was hard reset
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "testuser", "testrepo", "testbranch", "6299a42ed2715e4600616a7e0cb942babb0e5641")}, // commit exists, but on a different branch
		 };
	 }

	/**
	 * This test asserts that:
	 * <ul>
	 * 	<li> An exception that maps to {@link Status#INVALID_ARGUMENT} is thrown if the details cannot be resolved.
	 *  <li> The directory that the repository was going to be cloned in to does not exist if the transfer failed.
	 * </ul> 
	 * 
	 * @param repoDetails
	 */
	@Test(dataProvider = "invalid_repos")
	public void testFetchInvalid(RepoDetails repoDetails) {

		// Need to assert in some way that transfer code calls the repo downloader with this file
		// TODO: should have another way of mapping repo details to expected File that doesn't require this hacked on method
		UUID repoUUID = BattleEvaluatorImpl.createIdentifier(repoDetails);
		File repoClonePath = Paths.get(repoStorageDir.getAbsolutePath(), repoUUID.toString()).toFile();

		Status status = null;
		try {
			testClientStub.transferCode(CodeTransferRequest.newBuilder()
					.setRepoDetails(repoDetails)
					.build());
		}
		catch (Throwable t) {
			status = Status.fromThrowable(t);
		}
		
		Assert.assertEquals(status, Status.INVALID_ARGUMENT);
		Assert.assertFalse(repoClonePath.exists(), "Clone directory should not exist if the fetch failed.");
	}
	
	@DataProvider(name = "valid_repos")
	 public static Object[][] validRepos() {
		 return new Object[][] {
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "testuser", "testrepo", "testbranch", "0413edef62bfd82bc68a45681ef999aa8bea5849")}, // HEAD commit on a branch
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "testuser", "testrepo", "testbranch", "46e150fea3de1828a3b539209437ce256ce862d8")}, // older commit, same branch
			 {createRepoDetails("localhost:" + TEST_REPO_PORT, "testuser", "testrepo", "otherbranch", "6299a42ed2715e4600616a7e0cb942babb0e5641")}, // HEAD commit for a different branch
		 };
	 }
	
	/**
	 * This test asserts that:
	 * <ul>
	 * 	<li> A set of valid details does not cause any exceptions to be thrown during transfer
	 *	<li> A directory is created to store the cloned repository
	 *	<li> When querying the created directory for the hash of the HEAD commit, the commit hash that was included in the details is returned.
	 * 
	 * @param repoDetails
	 * @throws IOException
	 */
	@Test(dataProvider = "valid_repos")
	public void testFetchValid(RepoDetails repoDetails) throws IOException {
		testClientStub.transferCode(CodeTransferRequest.newBuilder()
				.setRepoDetails(repoDetails)
				.build());
		
		UUID repoUUID = BattleEvaluatorImpl.createIdentifier(repoDetails);
		File repoClonePath = Paths.get(repoStorageDir.getAbsolutePath(), repoUUID.toString()).toFile();
		
		Assert.assertTrue(repoClonePath.exists(), "Clone directory should exist if the fetch succeeded.");
		
		// Use JGit to get the commit hash for the HEAD of the cloned repository, and assert
		// that it is the expected value.
		Repository clonedRepo = FileRepositoryBuilder.create(
			Paths.get(repoClonePath.toURI())
			.resolve(GIT_SUFFIX)
			.toFile()
		);
		
		String actualCommitHash = clonedRepo.findRef("HEAD").getObjectId().getName();
		Assert.assertEquals(actualCommitHash, repoDetails.getCommitHash());
	}
	

	private static RepoDetails createRepoDetails(String domain, String username, String repository, String branch, String commitHash) {
		return RepoDetails.newBuilder()
				.setDomain(domain)
				.setUsername(username)
				.setRepository(repository)
				.setBranch(branch)
				.setCommitHash(commitHash)
				.build();
	}
}
