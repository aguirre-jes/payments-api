package jeaguirre.me.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * Validates naming conventions across the project.
 * Based on the current structure and naming patterns.
 */
@DisplayName("Naming Conventions - Current Structure")
class NamingConventionsTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("jeaguirre.me");
    }

    @Test
    @DisplayName("Use cases should follow naming convention *UseCase")
    void useCasesShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that().resideInAPackage("..applications.usescases..")
                .should().haveSimpleNameEndingWith("UseCase")
                .because("Use cases should be easily identifiable by their suffix");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("REST resources should follow naming convention *Resource")
    void restResourcesShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that().resideInAPackage("..adapters.inputs.rest..")
                .and().areAnnotatedWith(jakarta.ws.rs.Path.class)
                .should().haveSimpleNameEndingWith("Resource")
                .because("REST endpoints should be identifiable as resources");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repository interfaces should follow naming convention *Repository")
    void repositoryInterfacesShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domains.ports..")
                .and().areInterfaces()
                .should().haveSimpleNameEndingWith("Repository")
                .because("Repository ports should be identifiable by their suffix");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repository implementations should follow naming convention *RepositoryImpl")
    void repositoryImplementationsShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that().resideInAPackage("..adapters.outputs.persistence..")
                .and().implement(jeaguirre.me.domains.ports.PaymentRepository.class)
                .should().haveSimpleNameEndingWith("RepositoryImpl")
                .because("Repository implementations should be clearly distinguishable from interfaces");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs should be in correct package")
    void dtosShouldBeInCorrectPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Request")
                .or().haveSimpleNameEndingWith("Response")
                .or().haveSimpleNameEndingWith("Details")
                .should().resideInAnyPackage("..adapters.inputs.rest.dto..")
                .because("DTOs belong in the input adapters DTO package");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Mappers should follow naming convention *Mapper")
    void mappersShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Mapper")
                .should().resideInAPackage("..adapters.inputs.rest.mappers..")
                .because("Mappers transform data between layers");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain exceptions should follow naming convention *Exception")
    void domainExceptionsShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domains.exceptions..")
                .should().haveSimpleNameEndingWith("Exception")
                .because("Domain exceptions should be identifiable");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Services should follow naming convention *Service")
    void servicesShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..applications..")
                .because("Services coordinate application logic");

        rule.check(importedClasses);
    }
}
