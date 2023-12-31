package io.github.dunwu.javatech.reflections;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static io.github.dunwu.javatech.reflections.ReflectionsQueryTest.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.reflections.scanners.Scanners.SubTypes;

public class ReflectionsExpandSupertypesTest {

    private final FilterBuilder inputsFilter = new FilterBuilder()
        .includePattern("io\\.github\\.dunwu\\.javatech\\.reflections\\.ReflectionsExpandSupertypesTest\\$ExpandTestModel\\$Scanned\\$.*");

    @SuppressWarnings("unused")
    public interface ExpandTestModel {

        interface NotScanned {

            @Retention(RetentionPolicy.RUNTIME)
            @interface MetaAnnotation {} // outside of scanned scope

            @Retention(RetentionPolicy.RUNTIME)
            @Inherited
            @MetaAnnotation
            @interface TestAnnotation {} // outside of scanned scope, but immediate annotation

            interface BaseInterface {} // outside of scanned scope

            @TestAnnotation
            class BaseClass implements BaseInterface {} // outside of scanned scope, but immediate supertype

        }

        interface Scanned {

            class ChildrenClass extends NotScanned.BaseClass {}

        }

    }

    @Test
    public void testExpandSupertypes() {
        ConfigurationBuilder configuration = new ConfigurationBuilder()
            .forPackage("io.github.dunwu.javatech.reflections")
            .filterInputsBy(inputsFilter);

        Reflections reflections = new Reflections(configuration);
        assertThat(reflections.get(SubTypes.of(ExpandTestModel.NotScanned.BaseInterface.class).asClass()),
            equalTo(
                ExpandTestModel.NotScanned.BaseClass.class,
                ExpandTestModel.Scanned.ChildrenClass.class));

        Reflections refNoExpand = new Reflections(configuration.setExpandSuperTypes(false));
        assertThat(refNoExpand.get(SubTypes.of(ExpandTestModel.NotScanned.BaseInterface.class).asClass()),
            equalTo());
    }

    @Test
    void testDetectInheritedAnnotations() {
        ConfigurationBuilder configuration = new ConfigurationBuilder()
            .forPackage("io.github.dunwu.javatech.reflections")
            .filterInputsBy(inputsFilter);

        Reflections reflections = new Reflections(configuration);
        assertThat(reflections.getTypesAnnotatedWith(ExpandTestModel.NotScanned.TestAnnotation.class),
            equalTo(
                ExpandTestModel.NotScanned.BaseClass.class,
                ExpandTestModel.Scanned.ChildrenClass.class));

        Reflections refNoExpand = new Reflections(configuration.setExpandSuperTypes(false));
        assertThat(refNoExpand.getTypesAnnotatedWith(ExpandTestModel.NotScanned.TestAnnotation.class),
            equalTo());
    }

    @Test
    void testExpandMetaAnnotations() {
        ConfigurationBuilder configuration = new ConfigurationBuilder()
            .forPackage("io.github.dunwu.javatech.reflections")
            .filterInputsBy(inputsFilter);

        Reflections reflections = new Reflections(configuration);
        assertThat(reflections.getTypesAnnotatedWith(ExpandTestModel.NotScanned.MetaAnnotation.class),
            equalTo());
        //         todo fix, support expansion of meta annotations outside of scanned scope
        //            equalTo(
        //                NotScanned.TestAnnotation.class,
        //                NotScanned.BaseClass.class,
        //                Scanned.ChildrenClass.class));

        Reflections refNoExpand = new Reflections(configuration.setExpandSuperTypes(false));
        assertThat(refNoExpand.getTypesAnnotatedWith(ExpandTestModel.NotScanned.MetaAnnotation.class),
            equalTo());
    }

}
