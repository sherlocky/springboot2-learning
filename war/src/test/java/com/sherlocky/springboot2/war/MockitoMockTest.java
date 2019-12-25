package com.sherlocky.springboot2.war;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

/**
 * Mockito是GitHub上使用最广泛的Mock框架,并与JUnit结合使用.
 * <p>
 * Mockito框架可以创建和配置mock对象，可以简化具有外部依赖的类的测试开发
 * <p>
 * 官方文档： https://javadoc.io/doc/org.mockito/mockito-core/2.19.1/org/mockito/Mockito.html
 * @author: zhangcx
 * @date: 2019/12/25 11:39
 * @since:
 */
public class MockitoMockTest {
    /**
     * 可以验证交互逻辑
     * <p>
     * 一旦创建，模拟程序将记住所有交互。
     * 然后，可以有选择地验证感兴趣的任何交互。
     */
    @Test
    public void testMockitoTest() {
        // mock creation
        List mockedList = Mockito.mock(List.class);

        //使用模拟对象(而不是真实创建出来的对象)
        mockedList.add("one");
        mockedList.clear();

        // 验证
        Mockito.verify(mockedList).add("one");
        Mockito.verify(mockedList).clear();
    }

    /**
     * 可以在代码中打桩(stub)，自定义返回
     *      1.默认情况下，对于所有返回值的方法，模拟将酌情返回null，原始/原始包装器值或空集合。例如，对于int / Integer为0，对于boolean / Boolean为false。
     *      2.打桩可以被覆盖
     *      3.一旦打桩，该方法将始终返回打桩设置的值，而不管其被调用了多少次。
     *      4.当多次对具有相同参数的相同方法进行打桩时时，最后一次打桩更为重要（顺序）。
     */
    @Test
    public void testMockitoStub() {
        /**
         * 可以模拟具体的类，而不仅仅是接口
         */
        LinkedList mockedList = Mockito.mock(LinkedList.class);

        // 在实际执行之前 打桩
        Mockito.when(mockedList.get(0)).thenReturn("first");
        Mockito.when(mockedList.get(1)).thenThrow(new RuntimeException("xxx"));

        // 以下代码会打印 "first"
        System.out.println(mockedList.get(0));
        // 以下代码会打印为"null"，因为未对 get(999) 进行打桩
        System.out.println(mockedList.get(999));
        // 会抛出异常 new RuntimeException("xxx")
        //System.out.println(mockedList.get(1));

        // 还可以使用 anyInt()匹配任何int参数，这意味着参数为任意值，其返回值均是element
        Mockito.when(mockedList.get(Mockito.anyInt())).thenReturn("element");
        // 此时打印是element
        System.out.println(mockedList.get(999));
    }

    /**
     * 验证方法调用次数
     */
    @Test
    public void testMockitoCallTimes() {
        LinkedList mockedList = Mockito.mock(LinkedList.class);
        // 调用add一次
        mockedList.add("once");

        // 下面两个写法验证效果一样，均验证add方法是否被调用了一次
        Mockito.verify(mockedList).add("once");
        Mockito.verify(mockedList, Mockito.times(1)).add("once");
    }

    /**
     * 更多其他用法可以参考官方文档
     */
}