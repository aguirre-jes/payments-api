package jeaguirre.me.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

@DisplayName("Dependency Injection Rules")
class DependencyInjectionRulesTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("jeaguirre.me");
    }

    @Test
    @DisplayName("Use cases should use constructor injection")
    void useCasesShouldUseConstructorInjection() {
        ArchRule rule = constructors()
                .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("UseCase")
                .and().areDeclaredInClassesThat().resideInAPackage("..applications.usescases..")
                .should().beAnnotatedWith(jakarta.inject.Inject.class);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("REST resources should use constructor injection")
    void restResourcesShouldUseConstructorInjection() {
        ArchRule rule = constructors()
                .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("Resource")
                .and().areDeclaredInClassesThat().resideInAPackage("..adapters.inputs.rest..")
                .should().beAnnotatedWith(jakarta.inject.Inject.class);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use cases should have final fields")
    void useCasesShouldHaveFinalFields() {
        ArchRule rule = fields()
                .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("UseCase")
                .and().areDeclaredInClassesThat().resideInAPackage("..applications.usescases..")
                .should().beFinal();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Services should be application scoped")
    void servicesShouldBeApplicationScoped() {
        ArchRule rule = com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Service")
                .and().resideInAPackage("..applications..")
                .should().beAnnotatedWith(jakarta.enterprise.context.ApplicationScoped.class);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use cases should be application scoped")
    void useCasesShouldBeApplicationScoped() {
        ArchRule rule = com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("UseCase")
                .and().resideInAPackage("..applications.usescases..")
                .should().beAnnotatedWith(jakarta.enterprise.context.ApplicationScoped.class);

        rule.check(importedClasses);
    }
}
