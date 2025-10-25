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
 * Mapper rules validating the current structure.
 * Mappers act as translators between DTOs (input) and Entities (persistence).
 * They are allowed to know about both layers as they are the bridge.
 */
@DisplayName("Mapper Rules - Current Structure")
class MapperRulesTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("jeaguirre.me");
    }

    @Test
    @DisplayName("Mappers should be in adapters.inputs.rest.mappers package")
    void mappersShouldBeInCorrectPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Mapper")
                .should().resideInAPackage("..adapters.inputs.rest.mappers..")
                .because("Mappers translate between DTOs and entities in the input adapter layer");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Mappers should be ApplicationScoped")
    void mappersShouldBeApplicationScoped() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Mapper")
                .and().resideInAPackage("..adapters.inputs.rest.mappers..")
                .should().beAnnotatedWith(jakarta.enterprise.context.ApplicationScoped.class)
                .because("Mappers are stateless services that can be application scoped");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Mappers are allowed to access DTOs and entities")
    void mappersCanAccessDtosAndEntities() {
        // This test documents that mappers are explicitly allowed to bridge layers
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Mapper")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..adapters.inputs.rest.dto..",
                        "..adapters.inputs.rest.mappers..",
                        "..adapters.outputs.persistence.entities..",
                        "java..",
                        "jakarta.."
                )
                .because("Mappers bridge between DTOs and entities, so they can access both");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Mappers should not depend on use cases or services")
    void mappersShouldNotDependOnUseCasesOrServices() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Mapper")
                .should().onlyDependOnClassesThat()
                .resideOutsideOfPackages("..applications..")
                .because("Mappers should only transform data, not contain business logic");

        rule.check(importedClasses);
    }
}
