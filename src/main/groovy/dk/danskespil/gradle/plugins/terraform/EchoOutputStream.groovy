package dk.danskespil.gradle.plugins.terraform

/**
 * Apart from doing what an output stream does, this outputstream echos whatever it gets to the printstream its created with
 *
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
