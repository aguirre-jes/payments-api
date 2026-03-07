package jeaguirre.me.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Port and Adapter Rules for Hexagonal Architecture - Current Structure
 * 
 * This validates:
 * - Domain ports (interfaces) are in domains.ports
 * - Output adapters (implementations) are in adapters.outputs.persistence
 * - Input adapters are in adapters.inputs.rest
 * - Ports don't depend on adapters
 * - Application layer uses ports, not implementations
 */
@DisplayName("Ports and Adapters - Current Structure")
class PortsAndAdaptersTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("jeaguirre.me");
    }

    @Test
    @DisplayName("Domain ports should be interfaces")
    void domainPortsShouldBeInterfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domains.ports..")
                .should().beInterfaces()
                .because("Ports define contracts and must be interfaces");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Output adapters should implement domain ports")
    void outputAdaptersShouldImplementDomainPorts() {
        ArchRule rule = classes()
                .that().resideInAPackage("..adapters.outputs.persistence..")
                .and().haveSimpleNameEndingWith("RepositoryImpl")
                .should().implement(jeaguirre.me.domains.ports.PaymentRepository.class)
                .because("Repository implementations must implement the port interface");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application layer should depend on ports, not implementations")
    void applicationLayerShouldDependOnPortsNotImplementations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..applications..")
                .should().dependOnClassesThat()
                .haveSimpleNameEndingWith("RepositoryImpl")
                .because("Application layer should depend on port interfaces, not concrete implementations");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Ports should not depend on adapter implementations")
    void portsShouldNotDependOnAdapterImplementations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domains.ports..")
                .should().dependOnClassesThat()
                .haveSimpleNameEndingWith("RepositoryImpl")
                .because("Ports should not know about their implementations");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Ports should not have JPA-specific dependencies")
    void portsShouldNotHaveJPADependencies() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domains.ports..")
                .should().accessClassesThat()
                .resideInAnyPackage("org.hibernate..", "org.springframework.data..")
                .because("Ports should be technology-agnostic, not tied to specific persistence frameworks");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Input adapters should use application layer, not output adapters directly")
    void inputAdaptersShouldUseApplicationLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..adapters.inputs.rest..")
                .and().haveSimpleNameEndingWith("Resource")
                .should().dependOnClassesThat()
                .haveSimpleNameEndingWith("RepositoryImpl")
                .because("REST resources should call use cases, not repository implementations directly");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain ports can reference entities (current acceptable pattern)")
    void domainPortsCanReferenceEntities() {
        // This documents the current architecture decision:
        // Repository ports reference Payment entity for simplicity
        ArchRule rule = classes()
                .that().resideInAPackage("..domains.ports..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..domains..",
                        "..adapters.outputs.persistence.entities..",
                        "java..",
                        "jakarta.persistence.."
                )
                .because("Ports can reference entities as domain models in this architecture");

        rule.check(importedClasses);
    }
}
