package dk.danskespil.gradle.test.spock.helpers

import dk.danskespil.gradle.task.helpers.BadPathException

/**
 * Helper to make it easier to create nested filestructures with TemporaryFolder
 * See tests on how to use. Someting along the lines of (again, see tests)
 *
 * new TemporaryFolder().newFolder(new PathSlicer('some/path/and/perhaps/a/file.txt').dirNames)
 *
 */
class PathSlicer {
    ArrayList<String> dirs
    String fileName
    String path
    String[] dirNames

    PathSlicer(String path) {
        if (!path || path == '/' || path.contains('\\') || path.startsWith('/')) {
            throw new BadPathException(path)
        }

        this.path = path
        dirs = (List<String>) Arrays.asList(path.split('/'))

        if (!path.endsWith('/')) {
            fileName = dirs.remove(dirs.size() - 1)
        }

        dirNames = dirs.toArray(new String[dirs.size()])
    }
}
