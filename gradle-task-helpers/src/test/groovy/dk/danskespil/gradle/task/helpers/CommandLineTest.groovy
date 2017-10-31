package dk.danskespil.gradle.task.helpers

import spock.lang.Specification
import spock.lang.Unroll

class CommandLineTest extends Specification {
    @Unroll
    def "Can handle #spacedString correctly, splitting them into substrings"() {
        given:
        CommandLine cl = new CommandLine()
        
        when:
        cl.addToEnd(spacedString)
        
        then:
        cl.size() == size

        where:
        spacedString | size
        "1 2 3"      | 3
        " 1 2 3 "    | 3
        ""           | 0
        "11 22 33"   | 3
        "1"          | 1
        " 1"         | 1
        "1 "         | 1
        "1 \t 2"     | 2
        "\t1\t\n2\n" | 2
    }
}
