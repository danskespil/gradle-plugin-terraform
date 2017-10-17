package dk.danskespil.gradle.task.helpers

/**
 * When you need a task to execute something on the host OS, you need the output visually as well as in a file.
 * This class helps you do that, by wrapping System.out and a file in this class, like so:
 * for intellij, enable formatter in Preferences > Editor > Code Style
 *
 * @formatter:off
 * <pre>
 *
 *  File outAsText
 *  OutputStream echoOutputHereToo = new EchoOutputStream(new ByteArrayOutputStream(), System.out)
 *  if (outAsText) {
 *    outAsText.createNewFile()
 *    echoOutputHereToo = new EchoOutputStream(echoOutputHereToo, new PrintStream(outAsText))
 *   }
 *
 * </pre>
 * @formatter:on
 *
 * Apart from doing what an output stream does, this outputstream echos whatever it gets to the printstream its created with
 */

class EchoOutputStream extends OutputStream {
    @Delegate
    OutputStream os
    PrintStream ps

    EchoOutputStream(OutputStream os, PrintStream ps) {
        this.ps = ps
        this.os = os
    }

    @Override
    public void write(int b) throws IOException {
        os.write(b)
        this.ps.write(b)
    }
}
