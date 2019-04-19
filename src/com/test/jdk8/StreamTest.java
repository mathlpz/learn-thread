package com.test.jdk8;

import com.test.jdk8.bean.Student;
import com.test.jdk8.bean.TransactionInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Java 8 中的 StreamTest 是对集合（Collection）对象功能的增强，它专注于对集合对象进行各种非常便利、高效的聚合操作（aggregate operation），或者大批量数据操作 (bulk data operation)。
 * StreamTest API 借助于同样新出现的Lambda表达式，极大的提高编程效率和程序可读性。
 * 同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用 fork/join 并行方式来拆分任务和加速处理过程。
 * <p>
 * 通常编写并行代码很难而且容易出错, 但使用 StreamTest API 无需编写一行多线程的代码，就可以很方便地写出高性能的并发程序。
 * 所以说，Java 8 中首次出现的 java.util.stream 是一个函数式语言+多核时代综合影响的产物。
 * <p>
 * 流有串行和并行两种，串行流上的操作是在一个线程中依次完成，而并行流则是在多个线程上同时执行。
 *
 * @Author: lpz
 * @Date: 2019-04-01 16:04
 */
public class StreamTest {

    List<Student> students = new ArrayList<Student>();

    List<Student> resultStudents = new ArrayList<>();


    @Before
    public void setUp() throws Exception {
        // 初始化
        students = new ArrayList<Student>() {
            {
                add(new Student(20160001, "孔明", 20, 1, "土木工程", "武汉大学"));
                add(new Student(20160002, "伯约", 21, 2, "信息安全", "武汉大学"));
                add(new Student(20160003, "玄德", 22, 3, "经济管理", "武汉大学"));
                add(new Student(20160004, "云长", 21, 2, "信息安全", "武汉大学"));
                add(new Student(20161001, "翼德", 21, 2, "机械与自动化", "华中科技大学"));
                add(new Student(20161002, "元直", 23, 4, "土木工程", "华中科技大学"));
                add(new Student(20161003, "奉孝", 23, 4, "计算机科学", "华中科技大学"));
                add(new Student(20162001, "仲谋", 22, 3, "土木工程", "浙江大学"));
                add(new Student(20162002, "鲁肃", 23, 4, "计算机科学", "浙江大学"));
                add(new Student(20163001, "丁奉", 24, 5, "土木工程", "南京大学"));
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        resultStudents.forEach(student -> System.out.println(student.getName()
                + " -- " + student.getSchool()
                + " -- " + student.getAge()
                + " -- " + student.getMajor()));
    }


    @Test
    public void returnTest() {
        students.forEach(student -> {
            if (student.getId() == 20160003) {
                System.out.println("return...." + student.getId());
                return;
            } else {
                System.out.println(student.getId());
            }
        });
        System.out.println("111111111111222222222222");

        System.out.println("------------------------");
        for(Student student:students){
            if (student.getId() == 20160003) {
                System.out.println("return...." + student.getId());
                return;
            } else {
                System.out.println(student.getId());
            }
        }
        System.out.println("66666666666677777777777");
    }


    /**
     *
     */
    @Test
    public void valueTest() {
        List<String> names = students.stream()
//                .filter(student -> "武汉大学".equals(student.getSchool()))
                .map(Student::getName)
//                .map(student -> student.getName())
                .collect(toList());
        System.out.println(names);
    }

    @Test
    public void sumTest() {
        int totalAge = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .mapToInt(Student::getAge)
                .sum();
        System.out.println(totalAge);
    }

    @Test
    public void flatMapTest() {
        String[] strs = {"java", "is", "easy", "to", "use"};

        // 映射成为Stream<String[]>
        // 在执行map操作以后，我们得到是一个包含多个字符串（构成一个字符串的字符数组）的流，
        // 此时执行distinct操作是基于在这些字符串数组之间的对比，所以达不到我们希望的目的：
        List<String[]> sList = Arrays.stream(strs)
                .map(str -> str.split(""))
                .distinct()
                .collect(toList());
//        sList.forEach(strings -> Arrays.asList(strings).forEach(s -> System.out.println(s)));
        sList.forEach(strings -> System.out.println(strings));

        System.out.println("-----------------------------------");

        // flatMap将由map映射得到的Stream<String[]>，转换成由各个字符串数组映射成的流Stream<String>，
        // 再将这些小的流扁平化成为一个由所有字符串构成的大流Steam<String>，从而能够达到我们的目的。
        List<String> collect = Arrays.stream(strs)
                .map(str -> str.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
        collect.forEach(s -> System.out.println(s));
    }

    /**
     *
     */
    @Test
    public void allMatchTest() {
        boolean flag = students.stream().allMatch(student -> student.getAge() >= 18);
        System.out.println(flag);
    }

    @Test
    public void anyMatchTest() {

        boolean flag = students.parallelStream().anyMatch(student -> "武汉大学".equals(student.getSchool()));
        System.out.println(flag);
    }

    @Test
    public void noneMathchTest() {
        boolean flag = students.stream().noneMatch(student -> "计算机科学".equals(student.getMajor()));
        System.out.println(flag);
    }


    /**
     * findFirst不携带参数，具体的查找条件可以通过filter设置，此外我们可以发现findFirst返回的是一个Optional类型.
     */
    @Test
    public void findFirstTest() {
        Optional<Student> optStu = students.stream().filter(student -> "土木工程".equals(student.getMajor())).findFirst();
        System.out.println(optStu.get().getName());
    }


    /**
     * 实际上对于顺序流式处理而言，findFirst和findAny返回的结果是一样的，至于为什么会这样设计，接下来我们介绍的并行流式处理，
     * 当我们启用并行流式处理的时候，查找第一个元素往往会有很多限制，如果不是特别需求，在并行流式处理中使用findAny的性能要比findFirst好。
     */
    @Test
    public void findAnyTest() {
        Optional<Student> stu = students.parallelStream().filter(student -> "土木工程".equals(student.getMajor())).findAny();
        System.out.println(stu.get().getName());
    }


    @Test
    public void reduceTest() {
        int sum1 = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .mapToInt(Student::getAge)
                .sum();
        System.out.println(sum1);

        sum1 = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .map(Student::getAge)
                .reduce(0, (i1, i2) -> i1 + i2);
        System.out.println(sum1);


        sum1 = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .map(Student::getAge)
                .reduce(0, Integer::sum);
        System.out.println(sum1);

        Optional<Integer> totalAge = students.stream()
                .filter(student -> "计算机科学".equals(student.getMajor()))
                .map(Student::getAge)
                .reduce(Integer::sum);
        System.out.println(totalAge.get().intValue());

    }

    /**
     *
     */
    @Test
    public void filterTest() {
        resultStudents = students.stream()
                .filter(student -> "武汉大学".equals(student.getSchool()))
//                .collect(toList());
                .collect(Collectors.toList());
    }

    /**
     *
     */
    @Test
    public void distinctTest() {
        resultStudents = students.stream()
                .filter(student -> student.getMajor().equals("土木工程"))
//                .distinct()
                .collect(toList());
    }


    @Test
    public void distinctTest2() {
        List<Integer> nums = new ArrayList<Integer>() {
            {
                add(100);
                add(65);
                add(89);
                add(100);
                add(100);
                add(540);
            }
        };
        nums = nums.stream()
                .sorted()
                .distinct()
                .collect(toList());
        System.out.println(nums);

    }


    @Test
    public void limitTest() {
//        resultStudents = students.parallelStream()
//                .filter(student -> "土木工程".equals(student.getMajor()))
//                .limit(2).collect(toList());

        resultStudents = students.parallelStream()
                .filter(student -> "土木工程".equals(student.getMajor()))
                .sorted((o1, o2) -> o1.getAge() - o2.getAge())
                .limit(2)
                .collect(toList());
    }

    @Test
    public void skipTest() {
        resultStudents = students.parallelStream()
                .filter(student -> "土木工程".equals(student.getMajor()))
                .skip(2)
                .collect(toList());
    }

    @Test
    public void sortedTest() {
        System.out.println("哈哈哈");

        List<TransactionInfo> transactions = new ArrayList<>();
        TransactionInfo transaction1 = new TransactionInfo(1, 1, 100);
        TransactionInfo transaction2 = new TransactionInfo(2, 0, 600);
        TransactionInfo transaction3 = new TransactionInfo(3, 1, 300);
        TransactionInfo transaction4 = new TransactionInfo(4, 1, 200);
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        transactions.add(transaction4);

        List<Integer> transactionsIds = transactions.parallelStream().
                filter(t -> t.getType() == 1).
                sorted((o1, o2) -> o1.getValue() - o2.getValue()).
//                sorted(comparing(TransactionInfo::getValue).reversed()).
        map(TransactionInfo::getId).
                        collect(toList());
        System.out.println(transactionsIds);

    }


}
