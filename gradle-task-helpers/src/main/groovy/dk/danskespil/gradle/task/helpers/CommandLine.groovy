package dk.danskespil.gradle.task.helpers

class CommandLine implements Iterable<String> {
    private List<String> commandLine = new ArrayList<String>()

    Integer size() {
        return commandLine.size()
    }

    Iterator<String> iterator() {
        return commandLine.iterator()
    }

    boolean addToEnd(String... elements) {
        elements.each { String s ->
            addToEnd(s)
        }
    }

    boolean addToEnd(String element) {
        element = element.trim()
        List<String> parts = element.split()
        return commandLine.addAll(parts)
    }

    void prefix(String prefix) {
        commandLine.add(0, prefix)
    }
}
