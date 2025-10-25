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
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Architecture tests validating the current hexagonal architecture structure.
 * 
 * Current Structure:
 * - domains.ports: Port interfaces (hexagonal ports)
 * - domains.exceptions: Domain exceptions
 * - applications: Application services and use cases
 * - adapters.inputs.rest: REST controllers, DTOs, and mappers
 * - adapters.outputs.persistence: Repository implementations and entities
 */
@DisplayName("Hexagonal Architecture Validation - Current Structure")
class HexagonalArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("jeaguirre.me");
    }

    @Test
    @DisplayName("Domain ports should only depend on entities and standard libraries")
    void domainPortsShouldNotDependOnApplicationOrInputAdapters() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domains.ports..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..adapters.inputs..", "..applications.usescases..")
                .because("Domain ports define interfaces and should not depend on input adapters or use cases");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain exceptions should not depend on other layers")
    void domainExceptionsShouldNotDependOnOtherLayers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domains.exceptions..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..adapters..", "..applications..")
                .because("Domain exceptions should be independent of other layers");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application layer should only depend on domain and entities")
    void applicationLayerShouldOnlyDependOnDomainAndEntities() {
        ArchRule rule = classes()
                .that().resideInAPackage("..applications..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..applications..",
                        "..domains..",
                        "..adapters.outputs.persistence.entities..",
                        "java..",
                        "jakarta..",
                        "org.slf4j.."
                )
                .because("Application layer coordinates use cases and depends on domain ports and entities");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("REST resources should not depend on output adapters implementations")
    void restResourcesShouldNotDependOnOutputAdapterImplementations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..adapters.inputs.rest..")
                .and().haveSimpleNameEndingWith("Resource")
                .should().dependOnClassesThat()
                .haveSimpleNameEndingWith("RepositoryImpl")
                .because("REST resources should depend on use cases, not repository implementations");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Output adapters should not depend on input adapters")
    void outputAdaptersShouldNotDependOnInputAdapters() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..adapters.outputs..")
                .should().dependOnClassesThat()
                .resideInAPackage("..adapters.inputs..")
                .because("Output adapters should not know about input adapters");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use cases should be in applications.usescases package")
    void useCasesShouldBeInApplicationLayer() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("UseCase")
                .should().resideInAPackage("..applications.usescases..")
                .because("Use cases represent application logic and belong in the application layer");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("REST controllers should be in adapters.inputs.rest package")
    void restControllersShouldBeInInputAdapters() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Resource")
                .or().areAnnotatedWith(jakarta.ws.rs.Path.class)
                .should().resideInAPackage("..adapters.inputs.rest..")
                .because("REST controllers are input adapters");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repository implementations should be in adapters.outputs.persistence package")
    void repositoryImplementationsShouldBeInOutputAdapters() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("RepositoryImpl")
                .should().resideInAPackage("..adapters.outputs.persistence..")
                .because("Repository implementations are output adapters");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Port interfaces should be in domains.ports package")
    void portInterfacesShouldBeInDomainPorts() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Repository")
                .and().areInterfaces()
                .should().resideInAPackage("..domains.ports..")
                .because("Port interfaces define the domain contracts");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("JPA entities should be in persistence.entities package")
    void entitiesShouldBeInPersistenceLayer() {
        ArchRule rule = classes()
                .that().areAnnotatedWith(jakarta.persistence.Entity.class)
                .should().resideInAPackage("..adapters.outputs.persistence.entities..")
                .because("JPA entities are persistence implementation details");

        rule.check(importedClasses);
    }
}
