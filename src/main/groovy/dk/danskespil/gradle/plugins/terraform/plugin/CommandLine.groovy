package dk.danskespil.gradle.plugins.terraform.plugin

class CommandLine implements Iterable<String> {
    private List<String> commandLine = new ArrayList<String>()

    Iterator<String> iterator() {
        return commandLine.iterator()
    }

    boolean addToEnd(String... elements) {
        elements.each { String s ->
            addToEnd(s)
        }
    }

    boolean addToEnd(String element) {
        return commandLine.add(element)
    }

    void prefix(String prefix) {
        commandLine.add(0, prefix)
    }
}
