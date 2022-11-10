package hr.algebra.java2.utilities;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static void readClassInfo(Class<?> clazz, StringBuilder classInfo) {
        appendPackage(clazz, classInfo);
        // we can't find imports because the compiler doesn't put them into the object file
        // import is just a shorthand to the compiler appendModifiers(clazz, classInfo);
        appendModifiers(clazz, classInfo);
        classInfo.append(" ").append(clazz.getName());
        appendParent(clazz, classInfo, true);
        appendInterfaces(clazz, classInfo);
    }

    private static void appendPackage(Class<?> clazz, StringBuilder classInfo) {
        classInfo
                .append(clazz.getPackage())
                .append(System.lineSeparator())
                .append(System.lineSeparator());
    }

    private static void appendModifiers(Class<?> clazz, StringBuilder classInfo) {
//        public static final int PUBLIC           = 0x00000001; // 0001
//        public static final int PRIVATE          = 0x00000002; // 0010
//        public static final int PROTECTED        = 0x00000004; // 0100
//        public static boolean isPublic(int mod) {
//            return (mod & PUBLIC) != 0;
//        }

        /*
        int modifiers = clazz.getModifiers();
        if (Modifier.isPublic(modifiers)) {
            System.out.println(clazz.getName() + " is public");
        }
        if (Modifier.isAbstract(modifiers)) {
            System.out.println(clazz.getName() + " is abstract");
        }
        if (Modifier.isFinal(modifiers)) {
            System.out.println(clazz.getName() + " is final");
        }
        */
        // you may think of encapsulating this in an enum, but it is already done:
        classInfo.append(Modifier.toString(clazz.getModifiers()));
    }

    private static void appendParent(Class<?> clazz, StringBuilder classInfo, boolean first) {
        Class<?> parent = clazz.getSuperclass();
        if(parent == null) {
            return;
        }
        if (first) {
            classInfo
                    .append(System.lineSeparator())
                    .append("extends");
        }
        classInfo
                .append(" ")
                .append(parent.getName());
        appendParent(parent, classInfo, false);
    }

    private static void appendInterfaces(Class<?> clazz, StringBuilder classInfo) {
        if (clazz.getInterfaces().length > 0) {
            classInfo
                    .append(System.lineSeparator())
                    .append("implements ")
                    .append(
                            Arrays.stream(clazz.getInterfaces())
                                    .map(Class::getSimpleName)
                                    .collect(Collectors.joining(" "))
                    );
        }
    }

    public static void readClassAndMembersInfo(Class<?>clazz, StringBuilder classAndMembersInfo) {
        readClassInfo(clazz, classAndMembersInfo);
        appendFields(clazz, classAndMembersInfo);
        appendMethods(clazz, classAndMembersInfo);
        appendConstructors(clazz, classAndMembersInfo);
    }

    private static void appendFields(Class<?> clazz, StringBuilder classAndMembersInfo) {
        //Field[] fields = clazz.getFields();
        Field[] fields = clazz.getDeclaredFields();
        classAndMembersInfo
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(
                Arrays.stream(fields)
                        .map(Objects::toString)
                        .collect(Collectors.joining("\n"))
        );
    }

    private static void appendMethods(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            classAndMembersInfo
                    .append(System.lineSeparator())
                    .append(System.lineSeparator());
            appendAnnotations(method, classAndMembersInfo);
            classAndMembersInfo
                    .append(System.lineSeparator())
                    .append(Modifier.toString(method.getModifiers()))
                    .append(" ")
                    .append(method.getReturnType())
                    .append(" ")
                    .append(method.getName());
            appendParameters(method, classAndMembersInfo);
            appendExceptions(method, classAndMembersInfo);
        }
    }
    private static void appendAnnotations(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append(
                Arrays.stream(executable.getAnnotations())
                        .map(Objects::toString)
                        .collect(Collectors.joining(System.lineSeparator())));
    }
    private static void appendParameters(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append(
                Arrays.stream(executable.getParameters())
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ", "(", ")"))
        );
    }
    private static void appendExceptions(Executable executable, StringBuilder classAndMembersInfo) {
        if (executable.getExceptionTypes().length > 0) {
            classAndMembersInfo.append(" throws ");
            classAndMembersInfo.append(
                    Arrays.stream(executable.getExceptionTypes())
                            .map(Class::getName)
                            .collect(Collectors.joining(" "))
            );
        }
    }

    private static void appendConstructors(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            classAndMembersInfo
                    .append(System.lineSeparator())
                    .append(System.lineSeparator());
            appendAnnotations(constructor, classAndMembersInfo);
            classAndMembersInfo
                    .append(System.lineSeparator())
                    .append(Modifier.toString(constructor.getModifiers()))
                    .append(" ")
                    .append(constructor.getName());
            appendParameters(constructor, classAndMembersInfo);
            appendExceptions(constructor, classAndMembersInfo);
        }
    }
}
