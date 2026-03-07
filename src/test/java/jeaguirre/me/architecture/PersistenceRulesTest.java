package jeaguirre.me.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Validates persistence layer rules based on current structure.
 * JPA entities are in adapters.outputs.persistence.entities
 * Repository interfaces are in domains.ports
 * Repository implementations are in adapters.outputs.persistence
 */
@DisplayName("Persistence Layer Rules - Current Structure")
class PersistenceRulesTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("jeaguirre.me");
    }

    @Test
    @DisplayName("JPA entities should only be in persistence.entities package")
    void jpaEntitiesShouldOnlyBeInPersistencePackage() {
        ArchRule rule = classes()
                .that().areAnnotatedWith(jakarta.persistence.Entity.class)
                .should().resideInAPackage("..adapters.outputs.persistence.entities..")
                .because("JPA entities are infrastructure concerns in the output adapter");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repository interfaces should be in domains.ports package")
    void repositoryInterfacesShouldBeInDomainPorts() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Repository")
                .and().areInterfaces()
                .should().resideInAPackage("..domains.ports..")
                .because("Repository interfaces are domain ports defining contracts");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repository implementations should be in persistence package")
    void repositoryImplementationsShouldBeInPersistence() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("RepositoryImpl")
                .should().resideInAPackage("..adapters.outputs.persistence..")
                .because("Repository implementations are output adapters");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("EntityManager should only be used in output adapters")
    void entityManagerShouldOnlyBeUsedInRepositories() {
        ArchRule rule = fields()
                .that().haveRawType(jakarta.persistence.EntityManager.class)
                .should().beDeclaredInClassesThat()
                .resideInAPackage("..adapters.outputs.persistence..")
                .because("EntityManager is a JPA infrastructure concern");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Persistence annotations should only be in entity classes")
    void persistenceAnnotationsShouldOnlyBeInEntities() {
        ArchRule rule = classes()
                .that().areAnnotatedWith(jakarta.persistence.Entity.class)
                .or().areAnnotatedWith(jakarta.persistence.Table.class)
                .should().resideInAPackage("..adapters.outputs.persistence.entities..")
                .because("JPA annotations belong to persistence entities");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Only persistence layer should depend on JPA packages")
    void onlyPersistenceLayerShouldDependOnJPA() {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackages("..adapters.outputs.persistence..", "..domains.ports..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("jakarta.persistence..")
                .because("JPA dependencies should be isolated to the persistence layer");

        rule.check(importedClasses);
    }
}
