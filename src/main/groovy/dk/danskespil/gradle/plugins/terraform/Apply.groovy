package dk.danskespil.gradle.plugins.terraform

/**
 * Wraps cli command: terraform apply
 */
class Apply extends TerraformTask {
    @Override
    String getDescription() {
        return """Wraps cli command: terraform apply"""
    }
}
