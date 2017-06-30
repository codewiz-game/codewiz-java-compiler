package com.github.crob1140.codewiz.battles;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;

import com.github.crob1140.codewiz.grpc.CommitDetails;

public class RepositoryDownloader {
	
	public void cloneToDirectory(CommitDetails details, File directory) throws GitAPIException, InvalidRemoteException, IOException, URISyntaxException {
		/**
		Git git = Git.init()
				.setDirectory(directory)
				.call();
		
		StoredConfig config = git.getRepository().getConfig();
	    config.setBoolean( "http", null, "sslVerify", false );
	    config.save();

	    RemoteAddCommand addOriginCmd = git.remoteAdd();
	    addOriginCmd.setName("origin");
	    addOriginCmd.setUri(new URIish(repoUrl));
	    addOriginCmd.call();
	    
	    git.fetch().call();
	    
	    git.checkout().setUpstreamMode(SetupUpstreamMode.TRACK)
	    	.setName("origin/" + details.getBranch())
	    	.call();
    	*/
		
		Git git = null;
		try {
			// Clone the branch from the git repo
			String repoUrl = getRepositoryUrl(details);
			git = Git.cloneRepository()
				.setURI(repoUrl)
				.setDirectory(directory)
				.setBranchesToClone(Arrays.asList("refs/heads/" + details.getBranch()))
				.setBranch("refs/heads/" + details.getBranch())
				.call();
			
			// Hard reset to the specific commit
			git.reset()
				.setMode(ResetType.HARD)
				.setRef(details.getCommitHash())
				.call();			
		}
		finally {
			if(git != null) {
				git.getRepository().close();
			}
		}
	}
	
	public String getRepositoryUrl(CommitDetails details) {
		return String.format("git://%s/%s/%s.git", details.getDomain(), details.getUsername(), details.getRepository());
	}
}
