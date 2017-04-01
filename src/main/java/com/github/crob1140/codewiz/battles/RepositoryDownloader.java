package com.github.crob1140.codewiz.battles;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;

public class RepositoryDownloader {
	
	public void cloneToDirectory(RepoDetails details, File directory) throws GitAPIException, InvalidRemoteException, IOException, URISyntaxException {
		// Clone the branch from the git repo
		String repoUrl = String.format("git://%s/%s/%s.git", details.getDomain(), details.getUsername(), details.getRepository());
		
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

	public void pullUpdates(File projectDirectory)
			throws WrongRepositoryStateException,
			InvalidConfigurationException, DetachedHeadException,
			InvalidRemoteException, CanceledException, RefNotFoundException,
			RefNotAdvertisedException, NoHeadException,
			org.eclipse.jgit.api.errors.TransportException, GitAPIException,
			IOException {
		Git.open(projectDirectory).pull().call();
	}
}
