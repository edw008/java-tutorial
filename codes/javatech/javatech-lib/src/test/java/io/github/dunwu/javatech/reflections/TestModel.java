package io.github.dunwu.javatech.reflections;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@SuppressWarnings({ "ALL" })
public interface TestModel {

    public @Retention(RUNTIME)
    @Inherited
    @interface MAI1 {}

    public @Retention(RUNTIME)
    @MAI1
    @interface AI1 {}

    public @AI1
    interface I1 {}

    public @Retention(RUNTIME)
    @Inherited
    @interface AI2 {}

    public @AI2
    interface I2 extends I1 {}

    public @Retention(RUNTIME)
    @Inherited
    @interface AC1 {}

    public @Retention(RUNTIME)
    @interface AC1n {}

    public @AC1
    @AC1n
    class C1 implements I2 {}

    public @Retention(RUNTIME)
    @interface AC2 {

        public abstract String value();

    }

    public @AC2("")
    class C2 extends C1 {}

    public @AC2("ac2")
    class C3 extends C1 {}

    public @Retention(RUNTIME)
    @interface AM1 {

        public abstract String value();

    }

    public @interface AM2 {}

    public @Retention(RUNTIME)
    @interface AF1 {

        public abstract String value();

    }

    public class C4 {

        @AF1("1")
        private String f1;
        @AF1("2")
        protected String f2;
        protected String f3;

        public C4() { }

        @AM1("1")
        public C4(@AM1("1") String f1) { this.f1 = f1; }

        @AM1("1")
        protected void m1() {}

        @AM1("1")
        public void m1(int integer, @AM2 String... strings) {}

        @AM1("1")
        public void m1(int[][] integer, String[][] strings) {}

        @AM1("2")
        public String m3() {return null;}

        public String m4(@AM1("2") @AM2 String string) {return null;}

        public C3 c2toC3(C2 c2) {return null;}

        public int add(int i1, int i2) { return i1 + i2; }

    }

    public class C5 extends C3 {}

    public @AC2("ac2")
    interface I3 {}

    public class C6 implements I3 {}

    public @AC2("ac2")
    @interface AC3 {} // not @Retention(RUNTIME)

    public @AC3
    class C7 {}

}
