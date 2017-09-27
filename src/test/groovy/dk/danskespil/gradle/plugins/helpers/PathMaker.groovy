package dk.danskespil.gradle.plugins.helpers

import org.junit.rules.TemporaryFolder

/**
 * Facade to make it easier to create deep file structures with TemporaryFolder
 */
class PathMaker {

    File createNewPath(TemporaryFolder temporaryFolder, String path) {
        File rv = null

        PathSlicer pathSlicer = new PathSlicer(path)
        if (pathSlicer.dirs.size() > 0) {
            rv = temporaryFolder.newFolder(pathSlicer.dirNames)
        }
        if (pathSlicer.fileName) {
            rv = temporaryFolder.newFile(pathSlicer.path)
        }
        rv
    }
}
