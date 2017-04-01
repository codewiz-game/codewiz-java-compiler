package com.github.crob1140.codewiz;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileUtils {

	public static void empty(File directory) throws IOException {
		//FileUtils.delete(directory, FileUtils.RECURSIVE | FileUtils.RETRY);
		Files.walkFileTree(directory.toPath(), new DirectoryEmptierVisitor(directory));
	}
	
	private static class DirectoryEmptierVisitor extends SimpleFileVisitor<Path> {
		
		private final Path rootDirPath;
		
		public DirectoryEmptierVisitor(File directory) {
			this.rootDirPath = directory.toPath();
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			// TODO: Cannot use Files.delete(file) due to an AccessDeniedException thrown on Windows for .idx files
			// See https://dev.eclipse.org/mhonarc/lists/jgit-dev/msg01951.html
			file.toFile().delete();
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			if (!Files.isSameFile(rootDirPath, dir)) {
				Files.delete(dir);
			}
			return FileVisitResult.CONTINUE;
		}
	}
}
